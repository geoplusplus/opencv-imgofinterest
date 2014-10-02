# opencv-imgofinterest

This is a Scala project using OpenCV's Java bindings for testing ideas on finding images of interest in underwater video and photo streams.

This project is built using [SBT](http://www.scala-sbt.org/)

## OpenCV Info

### How I set it up
- This project uses [OpenCV](http://opencv.org/) version 2.4.9 built using homebrew on Mac OS X 10.9.5. 
- OpenCV's Java libs were located at /usr/local/Cellar/opencv/2.4.9/share/OpenCV/java and the jar file in that directory was copied do this projects _lib_ directory. 
- I bundled the dynlib in a jar under the path _native_ and also dropped it into _lib_.

### Initializing OpenCV
Basically, in Scala, just call `OpenCV` at least once somewhere in your application so that the native libraries get added to `java.library.path`. For example:

```scala
val cv = org.mbari.opencv.OpenCV
```
[OpenCV](https://github.com/hohonuuli/opencv-imgofinterest/blob/master/src/main/scala/org/mbari/opencv/OpenCV.scala) is an object, which in scala is essentially a [Singleton](https://en.wikipedia.org/wiki/Singleton_pattern); calling it multiple times won't hurt the intialization.

If opencv's dynlib is not found on `java.library.path`, it get's extracted out of the jar file and copied to your _TMPDIR_. Then it's added to the library path at runtime. No muss, no fuss for you.

## Useful [SBT commands](http://www.scala-sbt.org/release/docs/Command-Line-Reference.html) for this project

- __checkVersions__: Show dependency updates
- __clean__
- __cleanall__: Does __clean__ and __clean-files__
- __compile__ or __~compile__ (continuous)
- __console__: Opens a scala console that includes the projects dependencies and code on the classpath
- __dependency-graph__: Shows an ASCII dependency graph
- __dependencyUpdates__: Show dependency updates
- __doc__: Generate Scaladoc into target/api
- __install__
- __ivy-report__: build a report of dependencies using ivy in XML (viewable in a browser)
- __offline__: Use SBT offline
- __pack__: Builds a standalone distribution of this project under __target/pack__
- __package__: Creates the main artifact (e.g. a jar) under __target__
- __publish-local__ or __~publish-local__ (continous): Publish to the local maven repo
- __reload__: Reloads the build. Useful if you edit build.sbt.
- __show ivy-report__: Show the location of the dependency report
- __tasks -V__: Shows all available tasks/commands
- __test__ or __~test__ (continuous)
- __update-classifiers__: Download sources and javadoc for all dependencies
- __version-report__: Shows a flat listing of all dependencies in this project, including transitive ones.

### To run main class using SBT to launch it
`sbt 'run-main org.mbari.foo.Main'`

### To run a single test
`sbt 'test-only org.mbari.foo.org.mbari.opencv.ExampleSpec'`

## References

- [OpenCV API](http://docs.opencv.org/modules/refman.html)
- [OpenCV Java API](http://docs.opencv.org/java/)
  - Many of the OpenCV functions are under the [Imgproc](http://docs.opencv.org/java/org/opencv/imgproc/Imgproc.html) class.

