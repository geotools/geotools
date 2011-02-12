Test Data
---------

If your tests require test data, there are a couple of things to keep in mind.

There are two guidelines for handling of data required for testing:

* Java: test data (xml files, png files, whatever) should be stored in a test-data directory next to your test case (the use of test-data makes it obvious that the directory is not a package, similar to the use of doc-files for javadoc).
* Maven: The maven build system recommends that all test resources be placed into src/test/resources.

Combining these two guidelines we end up with the following::
   
   src/main/java/org/geotools/example/Fred (the class under test)
   src/test/java/org/geotools/example/FredTest
   src/test/resources/org/geotools/example/test-data/fred.xml

TestData Utility Class
^^^^^^^^^^^^^^^^^^^^^^^

The TestData class provides several convenience methods to aid in this process.::
   
   import junit.framework.TestCase;
   import org.geotools.test.TestData;
   // ... snip other imports ...
   
   public class MyTest extends TestCase {
       public void testImage() throws IOException {
           InputStream in = TestData.openStream( this, "my_data.properties" );
           Properties properties = new Properties( in );
           in.close();
           assertTrue( properties.contains("something") );
       }
   }

TestData provides the following methods:

* file( this, "filename" ): File
* url( this, "filename" ): URL
* openStream( this, "filename" ): InputStream
* openReader( this, "filename" ): BufferedReader
* openChannel( this, "filename" ): ReadableByteChannel
* temp( this, "filename" ): File (uses default suffix of ".tmp")

You can also use the the standard java construct for temporary files::
   
   File victim = File.createTempFile( "graph", "tmp" );
   victim.deleteOnExit();

The main extra that **TestData.temp( this, "filename" )** offers is a predictable location for your temp file, rather than the System specific location. It also make a harder attempt to delete the file on program termination.

Note: temporary files are kind of magic and have a random number inserted between the prefix and suffix (in order to keep them unique).

Open streams must be closed
^^^^^^^^^^^^^^^^^^^^^^^^^^^^
When using any openFoo method, don't forget to close the resource after usage! Do not rely on object finalisation for that. Temporary files are not deleted on program termination if some streams to that files still open.

Sharing test files across many modules
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
Some test files are used by many modules. A typical example is the statepop shape file. Shared files must be in the test-data directory of the sample-data module. 

They can been read as in the following example::
   
   import junit.framework.TestCase;
   import org.geotools.TestData;
   // ... snip other imports ...
   
   public class MyTest extends TestCase {
       public void testShapefile() throws IOException {
           ReadableByteChannel channel = TestData.openChannel("shapes/statepop.shp");
           ShapefileReader     reader  = new ShapefileReader(channel, new Lock());
           
           // ... Peforms tests here ...
           
           channel.close();
       }
   }

Note the differences compared to the first example above in this page:

* Import the TestData class from org.geotools rather than org.geotools.resources. This is needed in order to get access to the org/geotools/test-data directory provided in the sample-data module.

* Invoke TestData.openFoo(filename) method without this argument value. This is because we want TestData to searchs resources into the shared org/geotools/test-data directory instead of some other test-data owned by the module performing the tests.

* Because the statepop.shp files is bundled into the sample-data JAR file, it is available as an URL only. Consequently, some operations usually available for files (like random access) may not be available for shared resources.

But I need a Filename
^^^^^^^^^^^^^^^^^^^^^

Even if the code you are testing wants a filename you can still use the above constructs. The call to TestData.copy(this, ...) copies the named file from the shared resources to the caller test-data directory, which enable access as a file.

Example of staging a file for testing::
   
   TestData.copy( this, "my.shp" );
   File file = TestData.file( this, "my.shp" );
   System.out.println( file.getAbsolutePath() );
   // Something like: C:\....\test-data\my.shp"

Temporary filename::
   
   File temp = TestData.temp( this, "my.shp" );
   System.out.println( temp.getAbsolutePath() );
   // Something like: C:\....\test-data\my12345.shp"

