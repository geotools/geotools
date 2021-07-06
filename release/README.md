# Release module

The release module uses maven moduleSets and requires the context of a complete build to function.

To quickly build:

```
mvn -nsu package -DskipTests -Drelease -Dfmt.skip=true -Dmaven.test.skip=true -Dmaven.main.skip=true
```

To test the resulting contents:

```
unzip -t release/target/gt-release-26-SNAPSHOT-bin.zip   
```

To test the build bundle:

```
unzip release/target/gt-release-26-SNAPSHOT-bin.zip
java -cp "gt-release-26-SNAPSHOT/lib/*" org.geotools.util.factory.GeoTools
```