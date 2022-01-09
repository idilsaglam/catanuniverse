plugins{
    java
    id("com.diffplug.spotless") version "5.17.1"
}
spotless {
    format("misc") {
        target("*.gradle", "*.md", ".gitignore")
        // define the steps to apply to those files
        trimTrailingWhitespace()
        indentWithTabs() // or spaces. Takes an integer argument if you don't like 4
        endWithNewline()
    }
    java {
        target("*/src/*/java/**/*.java")
        // optional: you can specify a specific version and/or switch to AOSP style
        //   and/or reflow long strings (requires at least 1.8)
        //   and/or use custom group artifact (you probably don't need this)
        googleJavaFormat("1.12.0").aosp().reflowLongStrings().groupArtifact("com.google.googlejavaformat:google-java-format")
        // TODO: Update Licence header with student number and binome members names
        licenseHeader ("/*\n\t22015094 - Idil Saglam*/")
    }
}
