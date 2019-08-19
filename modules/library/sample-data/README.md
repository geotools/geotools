# Sample Data

The Sample Data Module provides a data repository for different formats
(shapefile, gml, sql, ...) which will enable you to make your own tests
with GeoTools libraries.

## Using TestData

The data available in this module is accessed via the `TestData` class;
this class is able to smoothly access and unpack data that has been
bundled up into a jar; it is able to handle some of the platform
differences that have cropped up over the years.

Example use:

Steam access:

```java
InputStream in = TestData.openStream( this, "my_data.properties" );
Properties properties = new Properties( in );
in.close();
assertTrue( properties.contains("something") );
```

File access:

```java
TestData.copy( this, "my.shp" );
File file = TestData.file( this, "my.shp" );
// System.out.println( file.getAbsolutePath() );
// Something like: C:\....\test-data\my.shp"
```

For more information please review out developers guide entry:

 - [Test Data](http://docs.geotools.org/latest/developer/conventions/test/data.html)

## Contributions

Contributions of new data are welcome:

 - We do ask you to mind the file sizes committed (please be kind)
 - Please relocate your JUnit test-data examples to this module
 - Please document what the data is on this page