# Application Schema Support Packages

Used to build remote schema resources into maven artifacts so they
can be used in offline GeoTools testing. This module should not be
part of the standard build. Modules below have no parent, by design.

Once artifacts are built with ``mvn clean install``, selected
artifacts can be deployed to the osgeo GeoTools maven repository.
See the [How to add a 3rd party jar
](http://docs.geotools.org/latest/developer/procedures/add.html) in
the developer manual.

Please be sure to deploy with the provided ``pom.xml``, so that
dependency information is preserved.

For example, to deploy ``cgiutilities-1.0`` and ``geosciml-2.0`` (only),
under Linux, use something like:

```
for groupId in cgiutilities-1.0 geosciml-2.0; do
    mvn deploy:deploy-file \
        -DrepositoryId=osgeo \
        -Durl=dav:http://download.osgeo.org/webdav/geotools/ \
        -DpomFile=$groupId/pom.xml \
        -Dfile=`find $groupId -name "*.jar"`
done
```

If you have trouble building a single child module with Maven 3
failing to honour local dependencies that were downloaded from
the osgeo repo, Google for "_maven.repositories", curse the new
Maven 3 behaviour, and delete every _maven.repositories file in
`~/.m2/repository/org/geotools/schemas` with, for example:

```
    find ~/.m2/repository/org/geotools/schemas \
        -name _maven.repositories \
        -exec rm -f {} \;
```

Then ``mvn clean install`` should work for a single child module.