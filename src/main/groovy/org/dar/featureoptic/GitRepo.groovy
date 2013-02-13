package org.dar.featureoptic

import static org.dar.featureoptic.CommandRunner.run

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

           <div>
                <h2>Features</h2>
                ${features()}
           </div>
        </div>"""
    }

    private def features() {
        def sb = new StringBuilder()
        resolveFeatures().each { sb.append(it.listView()) }
        sb.toString()
    }

    private List<Feature> resolveFeatures(){
        def result = run(["find", ".", "-path", "*${localLocation}*.feature"])
        String text = result.text
        def featurePaths = text.split("\\r?\\n")

        def features = []
        for (def featurePath  : featurePaths) {
            def name = featurePath.substring(featurePath.lastIndexOf("/")+1, featurePath.lastIndexOf("."))
            def contents = new File(featurePath).text
            features.add(new Feature(featurePath,name, contents, this))
        }

        features
    }

    @Override
    String listView() {
        """<a href=/repo/$name>$name $remoteLocation</a><br />"""
    }

    Feature getFeature(String featureName) {
        for (def feature  : resolveFeatures()) {
            if (featureName.equals(feature.name)) {
                return feature
            }
        }
        throw new RuntimeException("Feature not found")
    }
}
