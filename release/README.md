# Release module

To build a release use:

```
mvn assembly:single
```

To list the bin release:

```
unzip -t target/geotools-27-SNAPSHOT-bin.zip
```

To test the bin release:

```
unzip target/geotools-27-SNAPSHOT-bin.zip
java -cp "geotools-27-SNAPSHOT/lib/*" org.geotools.util.factory.GeoTools
```

For more information see [Controlling the Contents of an Assembly](https://books.sonatype.com/mvnref-book/reference/assemblies-sect-controlling-contents.html)

## DependencySets

The binary assembly is defined using dependencySets.

To add a geotools module update:

* `pom.xml` to list the dependency
* `src/assembly/binaryDistDependency.xml` to include the dependency

The filters to include/exclude jars are strictly enforced:

* The following warning is due to including the ``gt-sample-data-access`` jar when it is not available. To address this issue add ``gt-sample-data-access`` as a dependency to the release pom.xml:

  ```
  [WARNING] The following patterns were never triggered in this artifact inclusion filter:
  o  'org.geotools:gt-sample-data-access'
  ```

* The following warning is due to excluding a jar that is no longer available as a transitive dependency:
  
  ```
  [WARNING] The following patterns were never triggered in this artifact exclusion filter:
  o  'org.geotools:gt-epsg-postgresql'
  ```

## ModuleSets

Due to a an issue calculating the dependencies included via moduleSets we cannot yet use this approach.

When using a moduleSet approach the `assembly:single` goal must be part of a `package` build:

```
mvn -nsu package assembly:single -DskipTests -Drelease -Dfmt.skip=true -Dmaven.test.skip=true -Dmaven.main.skip=true
```

## Testing

To list the resulting contents:

```
unzip -t release/target/gt-release-26-SNAPSHOT-bin.zip 
```

To test the build bundle:

```
unzip release/target/gt-release-26-SNAPSHOT-bin.zip
java -cp "gt-release-26-SNAPSHOT/lib/*" org.geotools.util.factory.GeoTools
```