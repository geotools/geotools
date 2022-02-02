# GeoTools @RELEASE@

Thank you for downloading GeoTools @RELEASE@. This release was created
on @DATE@.

This release contains only a subset of the current GeoTools code base.
The full GeoTools code base contains additional unsupported modules
covering experiments with a range of additional data formats. Only the
modules which are considered to be stable have been included in this
release.

For more information, and documentation on the GeoTools library please
see the [GeoTools User
Guide](http://docs.geotools.org/@LINK@/userguide/).

The user guide includes the following tutorials:

- [Quickstart](http://docs.geotools.org/@LINK@/userguide/tutorial/quickstart/index.html)
  ([Eclipse](http://docs.geotools.org/@LINK@/userguide/tutorial/quickstart/eclipse.html)
  ,
  [Netbeans](http://docs.geotools.org/@LINK@/userguide/tutorial/quickstart/netbeans.html)
  or
  [Maven](http://docs.geotools.org/@LINK@/userguide/tutorial/quickstart/maven.html))

- [Feature Tutorial](http://docs.geotools.org/@LINK@/userguide/tutorial/feature/csv2shp.html)

- [Geometry CRSTutorial](http://docs.geotools.org/@LINK@/userguide/tutorial/geometry/geometrycrs.html)

- [Query Tutorial](http://docs.geotools.org/@LINK@/userguide/tutorial/filter/query.html)

- [Image Tutorial](http://docs.geotools.org/@LINK@/userguide/tutorial/raster/image.html)

- [Map Style Tutorial](http://docs.geotools.org/@LINK@/userguide/tutorial/map/style.html)

- Additional [tutorials](http://docs.geotools.org/@LINK@/userguide/tutorial/index.html)
  cover topics such as making your own function, process or datastore.

Welcome to GeoTools development!

## Reference

The following is provided as a quick reference only, please the
[GeoTools User Guide](http://docs.geotools.org/@LINK@/userguide/).

### Runtime Environment

To use the GeoTools libraries you will need:

- Java 11: OpenJDK distribution tested
- Java 8: Oracle JDK implementations tested

Details on JRE compatibility and optional extensions are found in our
[developers guide](http://docs.geotools.org/@LINK@/userguide/build/install/jdk.html).

### Quickstart

As a java library GeoTools is intended for use within your own program, the binary distribution includes jars of the geotools library, and third-party jars required for their operation.

The GeoTools utility class provide a *main* method to verify installation:

````
java -cp "lib/*" org.geotools.util.factory.GeoTools 
````

To compile and run Quickstart.java:

```
mkdir bin
javac -cp "lib/*" -d bin src/org/geotools/tutorial/quickstart/Quickstart.java 
java -cp "lib/*;bin" org.geotools.tutorial.quickstart.Quickstart
```

For more information see user Guide [Quickstart](http://docs.geotools.org/@LINK@/userguide/tutorial/quickstart/index.html).

### Building (Optional)

In order to build GeoTools you will need a copy of
[Apache Maven](http://maven.apache.org/download.html) project management tool.

With maven installed, you can perform a full build from the GeoTools folder and typing:

    mvn install

Please read the [User
Guide](http://docs.geotools.org/@LINK@/userguide/build/maven/index.html)
for more information about maven.

### Getting Involved (Recommended)

The [gt2-users mailing list](mailto:geotools-gt2-users@lists.sourceforge.net) is provided for
general inquiries, with additional [support options](http://docs.geotools.org/@LINK@/userguide/welcome/support.html)
are available in the user guide.

If you are interested in the future development of GeoTools join the
[geotools-devel](http://docs.geotools.org/@LINK@/developer/communication.html)
mailing list. We welcome contributions of new modules as well as keen
developers who want to work on the project as a whole. For background on
how the project functions see the [Developers\'
Guide](http://docs.geotools.org/@LINK@/developer/).

For the latest news, or to find out more about the mailing lists visit
the [GeoTools Homepage](http://geotools.org/).

Thanks for your interest in GeoTools,

*The GeoTools Project Management Committee, Open Source Geospatial
Foundation*
