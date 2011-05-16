Custom JDBC Access for Images 2
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

As mentioned above, Christian Mueller has set up a series of base classes that can be reused by
those writing plugins for different database backends.

This plugin is an extension to the Image Mosaicing Pyramidal JDBC Plugin. It is targeted to users
having a special database layout for storing their image data or use a special data base extension
concerning raster data.

Credits

* Thanks to some users on the mailing list for their inspiration.

Prerequisites

* JDBC Driver jar file in your CLASSPATH
* An existing database layout (tables,views) holding the raster data.

Design
''''''

The general idea is to use the Image Mosaicing Pyramidal JDBC Plugin interface called org.geotools.gce.imagemosaic.jdbc.JDBCAccess. Classes implementing this interface are responsible for the jdbc communication. Additionally, there is an abstract base class with some default implementations, org.geotools.gce.imagemosaic.jdbc.JDBCAccessCustom.

A customized implementation should subclass this class and implement two methods. The name of this subclass is a configuration parameter in the xml config file.

Implementation
''''''''''''''

The concrete procedure:

1. Create the subclass
   
   The abstract super class has some nice methods, especially
   
   * Connection getConnection()
   * Config getConfig()
   * CoordinateReferenceSystem getCRS
   
   Example::
     
     package org.mycompany.jdbc;
     
     public class MyJDBCAccessImpl extends JDBCAccessCustom {
     
         public MyJDBCAccessImpl(Config config) throws IOException {
             super(config);
             
         }
         
         @Override
         public void initialize() throws SQLException, IOException {
         }
     
         @Override
         public void startTileDecoders(Rectangle pixelDimension, GeneralEnvelope requestEnvelope,
                 ImageLevelInfo info, LinkedBlockingQueue<TileQueueElement> tileQueue,
                 GridCoverageFactory coverageFactory) throws IOException {
     
         }
     }

2. Initialize the meta info
   
   This is done in the method initialize. This method is called only once for each coverage / xml config file.
   The essential part is to create a list of org.geotools.gce.imagemosaic.jdbc.ImageLevelInfo objects. The info
   object at index 0 describes the base image, at index 1 is the info for the first pyramid, and so on. If there
   are no pyramids, the list has only one info object.
   
   A code template::
     
     @Override
     public void initialize() throws SQLException, IOException {
        Connection con = getConnection();
        
        // get the extent in world coordinates, must be implemented
        Envelope extent = getExtent(con); 
     
        // get the crs, method inherited from superclass
        CoordinateReferenceSystem crs = getCRS(); 
        
        // fetch the pixel resolution for each pyramid level
        // in the correct order    
        String stmt = "select RESX,RESY from ... order by level";
        PreparedStatement ps = con.prepareStatement(stmt);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            ImageLevelInfo li = new ImageLevelInfo();
            getLevelInfos().add(li);
            li.setResX(rs.getDouble(1));
            li.setResY(rs.getDouble(2));
            li.setExtentMinX(extent.getMinX());
            li.setExtentMaxX(extent.getMaxX());
            li.setExtentMinY(extent.getMinY());
            li.setExtentMaxY(extent.getMaxY());    
            li.setCrs(crs);
        }
        rs.close();
        ps.close();
        
     }
   
   This is a minimal example setting the meta information absolutely needed. ImageLevelInfo has more
   properties which can be useful. Another option is to subclass from ImageLevelInfo and
   add customized properties.

4. Fetch raster data
   
   This is done in the method startTileDecoders. There are a lot of parameters, maybe not all are
   useful for a custom implementation.
   
   Rectangle pixelDimension
     The requested size in pixel of the result image, perhaps not needed
   
   GeneralEnvelope requestEnvelope
     The requested size in world coordinates
   
   ImageLevelInfo info
     The info object of the pyramid to use
   
   LinkedBlockingQueue<TileQueueElement> tileQueue
     Queue for holding tile queue elements
   
   GridCoverageFactory coverageFactory
     perhaps not needed
   
   This method is responsible for:
   
   1. Fetching the tiles for the given level, the area covered may be larger than the area
      requested in the **requestEnvelope** parameter. This is the minimum to implement.
   2. Then mosiac the the tiles to one image.
   3. Then crop the image according to the **requestEnvelope** param
   4. Then use the pixel dimension of the image and the **pixelDimension** parameter to rescale the image.
   
   The interesting construct is the tile queue and a tile queue element. Before this method is called,
   a tile queue is created. Additionally an **ImageComposerThread** is created an started. This thread is
   responsible for creating the result image. Depending on the implementation possibilities described
   above, this thread is responsible to do the missing steps. As an example, if the custom implementation
   of **startTileDecoders** implements step 1 and 2 , the **ImageComposerThread** will do the missing steps 3 and 4.
   
   The primary job of the **startTileDecoders** method is to fetch the image data as fast as possible, creating
   on or more tile queue elements and put these elements into the queue. The **ImageCompoerThread** starts working
   when the first element is in the queue. It stops working when it reads a special END Element.
   
   A tile queue element for itself has
   
   * an optional name
   * a BufferedImage object
   * a GeneralEnvelope describing the the tile rectangle in world coordinates
   
   A code template::
     
     @Override
      public void startTileDecoders(Rectangle pixelDimension, GeneralEnvelope requestEnvelope,
            ImageLevelInfo info, LinkedBlockingQueue<TileQueueElement> tileQueue,
            GridCoverageFactory coverageFactory) throws IOException {
        try {
            Connection con = getConnection();
      
            // getting the index of the level info object
            int level = getLevelInfos().indexOf(info);
      
            // this example reads exactly one tile
            BufferedImage img = getBufferedImage(level, con);

            GeneralEnvelope genv = new GeneralEnvelope(info.getCrs());
            genv.setRange(0, info.getExtentMinX(), info.getExtentMaxX());
            genv.setRange(1, info.getExtentMinY(), info.getExtentMaxY());
            TileQueueElement tqElem = new TileQueueElement("oek",img,genv);;
            tileQueue.add(tqElem);
            con.close();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        // IMPORTANT, this must be the last element
        tileQueue.add(TileQueueElement.ENDELEMENT);
      }
  
  This is a simple template. A more complex implementation can be found in class **JDBCAccessBase**. This
  implementation fetches tiles, starts decoder threads to utilize full CPU power, waits for all decoder
  threads to finish and sends the end element.
  
  HINT: Hurry up to bring your first tile queue element into the queue.
  
  IMPORTANT: This method must be thread safe, do not modify instance vars or implement other actions causing problems under load.

Config file
'''''''''''

Here is an example config file::
  
  <?xml version="1.0" encoding="UTF-8" standalone="no"?>
  <config version="1.0">
    <coverageName name="oek"/>
    <coordsys name="EPSG:4326"/>
    <!-- interpolation 1 = nearest neighbour, 2 = bipolar, 3 = bicubic -->
    <scaleop  interpolation="1"/>
    <spatialExtension name="custom"/>
    <jdbcAccessClassName name="org.mycompany.jdbc.MyJDBCAccessImpl" />
    <connect>
        <!-- value DBCP or JNDI -->
        <dstype value="DBCP"/>
    <!--<jndiReferenceName value=""/>-->
        <username value="geotools" />
        <password value="geotools" />
        <jdbcUrl value="jdbc:oracle:thin:@ux-mc01.ux-home.local:1521:geotools102" />
        <driverClassName value="oracle.jdbc.OracleDriver"/> 
        <maxActive value="10"/>
        <maxIdle value="0"/>
    </connect>
  </config>

Most elements are self explanatory, the detailed documentation is in Image Mosaicing Pyramidal JDBC Plugin 
The *name* attribute of the <spatialExtension> Element must be **custom**.
The *name* attribute of the <jdbcAccessClassName> Element holds the class name of your implementation (**org.mycompany.jdbc.MyJDBCAccessImpl** in this example).

Deployment
''''''''''

Package the Java class(es) in a jar file and copy this jar file to your CLASSPATH.

For an example how to use the new coverage see at the end of Image Mosaicing Pyramidal JDBC Plugin