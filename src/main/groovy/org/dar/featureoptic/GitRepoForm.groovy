package org.dar.featureoptic

class GitRepoForm implements Viewable {

    @Override
    String view() {
        """<!DOCTYPE HTML><html><form method="post" action="/repo">
                    Name: <input type="text" name="name"><br/>
                    Remote: <input type="text" name="remote"><br/>
                    <input type="submit" value="Add">
            </form></html>"""
    }
}
