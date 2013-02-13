package org.dar.featureoptic

class Feature implements ListViewable,Viewable {
    def name
    def contents
    GitRepo repo
    def path

    Feature(path, name, contents, GitRepo repo) {
        this.path = path
        this.name = name
        this.contents = contents
        this.repo = repo
    }

    @Override
    String listView() {
        """<a href=/repo/$repo.name/feature/$name>$name</a><br />"""
    }

    String view() {
        """<textarea style="border:none;width:100%;height:1000px">${contents}</textarea>"""
    }
}
