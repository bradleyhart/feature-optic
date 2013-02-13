package org.dar.featureoptic

class PerisitedGitRepositories {
    def repos = [:]

    def add(GitRepo gitRepo){
        repos.put(gitRepo.name, gitRepo)
        persist(repos)
    }

    GitRepo get(def gitRepoName){
        repos = reload()
        repos.get(gitRepoName)
    }

    private def persist(Map repos) {
        def out= new ObjectOutputStream(new FileOutputStream('repos.obj'))
        out.writeObject(repos)
        out.close()
    }

    private Map reload() {
        def inp= new ObjectInputStream(new FileInputStream('repos.obj'))
        def repos = inp.readObject()
        inp.close()
        repos
    }

    def List<GitRepo> all() {
        repos = reload()
        repos.values().toList()
    }
}
