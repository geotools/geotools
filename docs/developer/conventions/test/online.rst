Online Tests
------------

We make use of a naming convention, ie ensure the name of your TestCase ends in OnlineTest, to indicate the use of external web services and databases. If a unit test requires a network connection to pass, it is an online test.

These tests will be skipped as part of the normal build process, but will be executed by certain build boxes.

JUnit 3 OnlineTestCase
^^^^^^^^^^^^^^^^^^^^^^^

JUnit 3 online tests should extend the OnlineTestCase class, since it will extract connection parameters from fixture properties files in the user's **~/.geotools** directory with minimal hassle.

Test cases extending this class will need to implement the getFixtureId() method which will return the identifier for a fixture. A fixture id of "postgis.typical" will attempt to read the file "~/.geotools/postgis/typical.properties".

Fixture property files allow site-specific connection information such as database authentication credentials to be provided outside the public GeoTools code base.

For example, a developer might provide access to a test database in their own PostGIS instance, but not want to share this online resource with the public. Fixture property files make it easy for other developers to configure online tests to use their own private resources.

JUnit 4 OnlineTestSupport
^^^^^^^^^^^^^^^^^^^^^^^^^

JUnit 4 online tests should extend OnlineTestSupport, which provides the same functionality as OnlineTestCase.

Behaviour of Online Test Cases
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
The default behaviour of OnlineTestCase is that, if connect() throws an exception, the test suite is disabled, causing each test to pass without being run. In addition, exceptions thrown by disconnect() are ignored. This behaviour allows tests to be robust against transient outages of online resources, but also means that local software failures in connect() or disconnect() will be silent. To have exceptions thrown by connect() and disconnect() cause tests to fail, set skip.on.failure=false in the fixture property file. This restores the traditional behaviour of unit tests, that is, that exceptions cause unit tests to fail.

Each module should try to provide default fixture files pointing to publicly accessible servers. Users running online tests will copy these defaults and customize them accordingly. If a fixture file is missing, its tests will not be run; therefore, if one deletes the ~/.geotools/oracle directory, for example, all oracle online tests will be disabled.

For more details see: Online Test Fixtures, which defines the identity of each fixture and what its expected qualities and contents are.

Example OnlineTestCase Use
^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Example use (using PostgisOnlineTestCase)::
   
   public abstract class PostgisOnlineTestCase extends OnlineTestCase {
       protected DataStore dataStore;
      
       protected abstract String getFixtureId();
      
       protected void connect() throws Exception {
           Map params = getParams();
           dataStore = new PostgisDataStoreFactory().createDataStore(params);
       }
       
       public Map getParams() {
           Map params = new HashMap();
           
           params.put(PostgisDataStoreFactory.DBTYPE.key, "postgis");
           params.put(PostgisDataStoreFactory.HOST.key, fixture.getProperty("host"));
           params.put(PostgisDataStoreFactory.PORT.key, fixture.getProperty("port"));
           params.put(PostgisDataStoreFactory.SCHEMA.key, fixture.getProperty("schema"));
           params.put(PostgisDataStoreFactory.DATABASE.key, fixture.getProperty("database"));
           params.put(PostgisDataStoreFactory.USER.key, fixture.getProperty("user"));
           params.put(PostgisDataStoreFactory.PASSWD.key, fixture.getProperty("password"));
           
           if (fixture.containsKey("wkbEnabled")) {
               params.put(PostgisDataStoreFactory.WKBENABLED.key, fixture.getProperty("wkbEnabled"));
           }
           if (fixture.containsKey("looseBbox")) {
               params.put(PostgisDataStoreFactory.LOOSEBBOX.key, fixture.getProperty("looseBbox"));
           }
           return params;
       }
       protected void disconnect() throws Exception {
           dataStore = null;
       }
   }

Example LocalPostgisOnlineTest

And here is a sample use::
   
   class LocalPostgisOnlineTest extends PostgisOnlineTestCase  {
       protected abstract String getFixtureId(){
           return "local";
       }
   }

As a rule of thumb, online tests should not fail if a server is unavailable.
Example Fixture

Example fixture ~/.geotools/postgis/local.properties::
   
   host=localhost
   port=15234
   schema=bc
   user=postgres
   passwd=postgres
   database=bc
   wkbEnabled=true


In windows you cannot create a ".geotools" folder!

1. Open up a CMD window
2. cd to your home directory and use mkdir
    
    C:\Documents and Settings\Fred>mkdir .geotools

3. And set up any fixtures you need:
    
    C:\Documents and Settings\Fred>cd .geotools
    C:\Documents and Settings\Fred\.geotools>mkdir postgis
    C:\Documents and Settings\Fred\.geotools>cd postgis
    C:\Documents and Settings\Fred\.geotools\postgis>notepad typical.properties

4. And use the as a guide: http://svn.geotools.org/geotools/trunk/gt/build/fixtures/
   
   Examples:
   
   * PostgisOnlineTestCase - Abstract Testcase class which connects to a specified database and creates a datastore
   * PostgisPermissionOnlineTest - Simple online test which makes use of PostgisOnlineTestCase
