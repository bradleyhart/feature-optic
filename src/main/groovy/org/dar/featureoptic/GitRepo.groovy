package org.dar.featureoptic

class GitRepo implements Serializable,Viewable {
    String localLocation
    String name
    String remoteLocation

    GitRepo(localLocation, remoteLocation, name) {
        this.remoteLocation = remoteLocation
        this.localLocation = localLocation
        this.name = name
    }

    @Override
    String view() {
        """<a href=/repo/$name>$name $remoteLocation</a>"""
    }
}
