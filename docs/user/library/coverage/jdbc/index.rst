.. _im-jdbc:

Image Moasic JDBC
-----------------

This **gt-imagemoasic-jdbc** plugin is intended to handle large images stored as tiles in a JDBC
database. Tiles created by pyramids are also stored in the database. The utility uses the indexing
of databases to speed up access to the requested tiles, the plugin for itself has a multithreaded
architecture to make use of dual/quad core CPUs and multiprocessor systems.

.. sidebar:: Details
   
   .. toctree::
      :maxdepth: 1
      
      internal
      customized
      faq

.. toctree::
   :maxdepth: 1
   
   setup
 
This plugin should give you the possibility to handle large images with their pyramids in a JDBC database.

Credits:

* This module was funded by Google as part of the GSOC 08 program.

Tested Database Systems

* DB2 (with or without Spatial Extender)
* Oracle (with or without Location Based Services, Oracle Spatial not needed)
* MySql
* Postgres (with or without Postgis)
* H2

Example Use
^^^^^^^^^^^

This example shows how to use the module::
  
  // First, get a reader
  // the configUrl references the config xml&nbsp; and is object of one of the following types
  // 1) java.net.URL
  // 2) java.io.File
  // 3) java.lang.String (A filename string or an url string)
  
  
  AbstractGridFormat format =(AbstractGridFormat)GridFormatFinder.findFormat(configUrl);
  ImageMosaicJDBCReader reader= (ImageMosaicJDBCReader)format.getReader(configUrl,null);
  
  // get a parameter object for a grid geometry
  ParameterValue<GridGeometry2D>gg=AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
  
  // create an envelope, 2 Points, lower left and upper right, x,y order
  GeneralEnvelope envelope = new GeneralEnvelope(new double[] {10,20},new double[] {30,40});
  
  // set a CRS for the envelope
  envelope.setCoordinateReferenceSystem(CRS.decode"EPSG:4326");
  
  // Set the envelope into the parameter object
  gg.setValue(new GridGeometry2D(new GeneralGridRange(new Rectangle(0, 0, width, heigth)),envelope));
  
  // create a parameter Object for the background color (NODATA), this param is optional
  final ParameterValue outTransp=ImageMosaicJDBCFormat.BACKGROUND_COLOR.createValue();
  outTransp.setValue(Color.WHITE);

  // if you like a transparent background, use this
  // final ParameterValue outTransp=ImageMosaicJDBCFormat.OUTPUT_TRANSPARENT_COLOR.createValue();
  // outTransp.setValue(Color.WHITE);
  
  // call the plugin passing an array with the two parameter objects
  try {
      GridCoverage2D coverage = (GridCoverage2D) reader.read(new GeneralParameterValue[] { gg, outTransp});
  } catch (IOException e) {
      e.printStackTrace();
  }

Mapping of table and attribute names
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Since there a lot of naming conventions in different enterprises, I is not idea to force the use of predefined table and attribute names. In our example, the names of the spatial and tile tables is selectable, the name of the meta table and all the attribute names were assumed. The mapping of these names is part of the XML configuration. The corresponding XML fragment shows the mapping of the assumed names.

A sample XML fragment file **mapping.xml.inc**::
  
  <!-- possible values: universal,postgis,db2,mysql,oracle -->
  <spatialExtension name="universal"/>
  <mapping>
        <masterTable name="META" >
            <coverageNameAttribute name="Name"/>
            <maxXAttribute name="MaxX"/>
            <maxYAttribute name="MaxY"/>
            <minXAttribute name="MinX"/>
            <minYAttribute name="MinY"/>
            <resXAttribute name="ResX"/>
            <resYAttribute name="RresY"/>
            <tileTableNameAtribute    name="TileTable" />
            <spatialTableNameAtribute name="SpatialTable" />
        </masterTable>
        <tileTable>
            <blobAttributeName name="Data" />
            <keyAttributeName name="Location" />
        </tileTable>
        <spatialTable>
            <keyAttributeName name="Location" />
            <geomAttributeName name="Geom" />
            <tileMaxXAttribute name="MaxX"/>
            <tileMaxYAttribute name="MaxY"/>
            <tileMinXAttribute name="MinX"/>
            <tileMinYAttribute name="MinY"/>
        </spatialTable>
  </mapping>

The structure of this XML Fragment is kept very simple, use it as a pattern.

The name attribute of the <spatialExtension> has to be one of the following values

* universal (a vendor neutral JDBC approach which should work with any jdbc database with BLOB support)
* db2 (use spatial extender)
* mysql (use the spatial features of mysql)
* postgis (use the spatial features of postgis)
* oracle (use location based services included in every oracle edition, no oracle spatial needed !!!)

If your spatial extension is universal you need to specify <tileMinXAttribute>,<tileMinYAttribute>,<tileMaxXAttribute>,<tileMaxYAttribute>, but you can omit <geomAttributeName>.

In all other cases, you need the <geomAttributeName> Element and can omit <tileMinXAttribute>,<tileMinYAttribute>,<tileMaxXAttribute>,<tileMaxYAttribute>. 

Be careful with the case sensitivity of different DBMS products. Look here http://www.alberton.info/dbms_identifiers_and_case_sensitivity.html and here http://en.wikibooks.org/wiki/SQL_dialects_reference/Data_structure_definition/Delimited_identifiers.

In the above example, we have  <maxXAttribute name="MaxX"/> which will result in an attribute name of MAXX for DB2 and Oracle, but maxx for postgres. A good strategy would be to use identifiers (table and attribute Names ) consisting of uppercase /lowercase letters, depending on your DBMS.

This approach assures that there are no predefined table and attribute names, allowing seamless integration into existing naming conventions. 

Configuration
^^^^^^^^^^^^^

Each Map with its pyramids is configured by an XML File. The Configuration can be split into 3 parts

1. The database mapping as explained above
2. The JDBC connect configuration
3. The Map configuration

JDBC Connect configuration
^^^^^^^^^^^^^^^^^^^^^^^^^^

Dependent on the deployment environment, there are different possibilities for connection to the database. Using this plugin in a client java application will require a JDBC Driver Connect, operation within a J2EE Container should prefer a JNDI Datasource connect (your admin will be happy) .

A sample XML fragment file for a JDBC Driver Connection to the H2 database **connect.xml.inc**::
  
  <connect>
        <dstype value="DBCP"/>
        <username value="user" />
        <password value="password" />
        <jdbcUrl value="jdbc:h2:target/h2/testdata" />
        <driverClassName value="org.h2.Driver"/>
        <maxActive value="10"/>
        <maxIdle value="0"/>
  </connect>

XML Fragment for connection to a JNDI Datasource **connect.xml.inc**::
  
  <connect>
        <dstype value="JNDI"/>
         <jndiReferenceName value="jdbc/myDataSourceName"/>
  </connect>

Map Configuration
^^^^^^^^^^^^^^^^^

Now we can put the things together and create the config file for a map.

Example file map.xml::
  
  <?xml version="1.0" encoding="UTF-8" standalone="no"?>
  <!DOCTYPE ImageMosaicJDBCConfig [
     <!ENTITY mapping PUBLIC "mapping" "mapping.xml.inc">
     <!ENTITY connect PUBLIC "connect" "connect.xml.inc">
  ]>
  
  
  <config version="1.0">
     <coverageName name="oek"/>
     <coordsys name="EPSG:31287"/>
     <!-- interpolation 1 = nearest neighbour, 2 = bipolar, 3 = bicubic -->
     <scaleop interpolation="1"/>
     <axisOrder ignore="false"/>
     <verify cardinality="false"/>
     &mapping;
     &connect;
  </config>

The DOCTYPE includes two XML entity references to the connct.xml.inc and mapping.xml.inc. The XML parser includes these fragments at parsing time.

Elements:

config
  The root element. The version attribute is reserved for future versions of this plugin. Must be "1.0."

coverageName
  The name of your map. This name is used for searching the meta data in the meta table.

coordsys
  The name of a coordinate reference system, will be referenced by CRS.decode() of the geotools library.

scaleop
  The interpolation method to use (1 = nearest neighbour, 2 = bipolar, 3 = bicubic)

axisOrder
   The module compares the CRS from the read request to the CRS stored in the configuration. If the axis order differ, an x axis switch is performed.
   Some clients are doing this before calling the module and the plugin switches again resulting in the original envelope. A value of "true" suppresses this switch.
    
verify
  if you have image data and georeferencing information in different tables and the the attribute cardinality is true , the plugin will check the number of records in each table. If the numbers are not equal, the image/pyramid will be removed and you see a warning in the log. This check is intended for testing environments, set the value to false in production environments to avoid bad performance.

**Configuration Summary**

A map configuration consists of 3 parts, connect configuration, mapping configuration and map configuration. As a result, you have great flexibilty and can reuse parts of the configuration for many maps.
