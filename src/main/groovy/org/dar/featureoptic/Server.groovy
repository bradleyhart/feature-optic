package org.dar.featureoptic

import groovy.io.GroovyPrintWriter

import static org.dar.featureoptic.PostBodyMapper.getQueryMap
import static spark.Spark.*;
import spark.*;

public class Server {

    static PerisitedGitRepositories repositories = new PerisitedGitRepositories()

    public static void main(String[] args) {

        post(route("/repo") {  Request request, Response response ->
            def params = getQueryMap(request.body())
            def localLocation = UUID.randomUUID()
            def remoteLocation = URLDecoder.decode(params.get("remote"))
            def repoName = params.get("name")

            run "git clone ${remoteLocation} ${localLocation}"

            repositories.add(new GitRepo(localLocation, remoteLocation, repoName))
            response.redirect("/repo/${params.get("name")}")
        })

        get(route("/repo") {  Request request, Response response ->
            def sb = new StringBuilder()
            repositories.all().each { sb.append(it.listView()) }
            sb.toString()
        })

        get(route("/repo/form") {  Request request, Response response ->
            new GitRepoForm().view()
        })

        get(route("/repo/:name") { Request request, Response response ->
            repositories.get(request.params(":name")).view()
        })

        get(route("/repo/:name/update") { Request request, Response response ->
            GitRepo repo = repositories.get(request.params(":name"))
//            run "git reset HEAD --hard ${repo.localLocation}"
            run "git --git-dir ${repo.localLocation}/.git fetch"
            run "git --git-dir ${repo.localLocation}/.git --work-tree=${repo.localLocation} merge origin/master"
            response.redirect("/repo/${request.params(":name")}")
        })

    }

    def static run(String command){
        def result = command.execute()
        result.waitFor()
        if (result.exitValue() != 0) {
            throw new RuntimeException("$result failed")
        }
    }

    def static route(def route, Closure closure){
        new Route(route) {
            @Override
            Object handle(Request request, Response response) {
                try {
                    """<!DOCTYPE HTML><html><body>${closure.call(request, response)}</body></html>"""
                } catch (Throwable t){
                    response.status(500)
                    def writer = new StringWriter()
                    t.printStackTrace(new GroovyPrintWriter(writer))
                    """<html><body><h1>Internal Server Error</h1><textarea style="border:none;width:100%;height:1000px">${writer.toString()}</textarea></body></html>"""
                }
            }
        }
    }



}
