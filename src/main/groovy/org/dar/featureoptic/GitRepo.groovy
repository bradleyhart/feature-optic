package org.dar.featureoptic

class GitRepo implements Serializable,Viewable,ListViewable {
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
        """<h1>$name</h1>
           <p>$remoteLocation</p>
           <form method="get" action="/repo/$name/update">
                <input type="submit" value="Update">
           </form>
        </div>"""
    }

    @Override
    String listView() {
        """<a href=/repo/$name>$name $remoteLocation</a>"""
    }
}
