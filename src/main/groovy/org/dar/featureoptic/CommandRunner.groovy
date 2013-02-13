package org.dar.featureoptic

class CommandRunner {
    def static run(def command){
        def result = command.execute()
        result.waitFor()
        if (result.exitValue() != 0) {
            throw new RuntimeException("$result failed")
        }
        result
    }
}
