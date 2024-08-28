# Application Schema Sample Data

Used to build sample data into maven artifacts so they can be used
for integration testing.

This module should not be part of the standard build.
Modules below have no parent, by design.

Once artifacts are built with ``mvn clean install``, selected
artifacts can be deployed to the osgeo GeoTools maven repository.

For example, to deploy ``refdataset-1.0`` under Linux, use something like:

```
cd refdataset-1.0
mvn deploy:deploy-file \
    -DrepositoryId=osgeo \
    -Durl=https://repo.osgeo.org/repository/geotools-releases/ \
    -DpomFile=pom.xml \
    -Dfile=`find target -name "*.jar"`
```

This requires `~/settings.xml` to list a `osgeo` server, configured with your OSGeo User ID credentials.
For more information see [How to add a 3rd party jar
](https://docs.geotools.org/latest/developer/procedures/add.html) in the developer manual.