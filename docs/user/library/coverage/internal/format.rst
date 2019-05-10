Format
^^^^^^

The standard way to access an external data source to create GeoTools GridCoverage instances currently requires creating several classes:

1. A ``javax.imageio.ImageReader`` class to access the data source and providing the methods to create:
   
  * ``java.awt.image.RenderedImage``
  * ``javax.imageio.metadata.IIOMetadata`` instance defining both:
    
  * the meaning of the numeric values in the ``RenderedImage``; and
  * the georeferencing parameters to geolocate the image

2. A builder of the GridCoverage2D which uses the above information to create an instance.

The ``gt-opengis``  module documentation explains the concepts behind this work. It should eventually be possible to reduce the effort required of programmers wishing to use this part of the library (in particular a standard way to define all the different kinds of metadata for grid coverages).

This page explores some of the examples available today.

**ImageReader**

The process of creating a plugin for a new GridCoverage format starts with creating an ImageReader to access the contents of the original data source. We have a range of implementations in you can work from illustrating both disk and database image formats being supported.

This ImageReader will need to provide both a RenderedImage and an object which extends IIOMetaData since the interface calls for these methods.

The final step involves writing a class which can create a GridCoverage2D. That class will need to get the RenderedImage from the ImageReader and will need to parse the IIOMetaData instance also obtained from the ImageReader to extract the required information on both the meaning of the data and the georeferencing of the image. With that information, the class will use the GridCoverageFactory in the coverage module to instantiate the GridCoverage2d.

* Unfortunately, we have not yet arrived at a design sufficiently powerful and flexible to handle the creation of GridCoverage instances from all the different kinds of data a user can have. Therefore, it is the programmer using the GeoTools library who must create a system to generate a new GridCoverage for each data file type they need to access. It is up to that programmer, based on the knowledge of the data format and the data contents of the files their applications use, to extract any generalization which they can get to avoid having to write a new class for every file.
* Some work has been done in GeoAPI and GeoTools to start to create
  a more general framework. For example, the class that builds a
  GridCoverage is generally called a GridCoverageReader since it is
  based on an ImageReader---there is even an interface of that name in
  GeoAPI. Unfortunately, this is work in progress and the API will probably
  change once we start work to finalize the standard way to access grid
  data. Programmers today should ignore all that work and build their
  own. However, the unsupported module ``coverageio``  and ``coverageio-netcdf``
  may have useful ideas for programmers who wish to look at that code.

This page refers to the DEM example from the ``gt-opengis``  explanation of GridCoverage.

========== ==================== ============== ======================
RAW Data   Category             Value          Color
========== ==================== ============== ======================
-9999      QualitativeCategory  NaN            new Color(0,0,0,0)
-8888      QualitativeCategory  NaN            Color.WHITE
0:5000     QuantitativeCategory -99:400 foot   Color.BLUE:Color.GREEN
========== ==================== ============== ======================

**ImageReader**

The ImageReader class is part of the ``javax.imageio`` package. It is an abstract class which needs only a few methods defined to work the critical ones being:

* ``getImageMetadata(..)``: ``IIOMetaData`` 
* ``read(..)``: ``BufferedImage`` 

The key difficulty, apart from being able to generate the image, being how to build the ``IIOMetaData``  object. In our case, we subclass that object adding the metadata information we will need.

The core functionality of this class follows the standard Java Image I/O framework which is explained in the package documentation which includes the useful Java Image I/O API Guide.

* The metadata which is required to build the GridCoverage2D objects may be contained in the image file itself, as is frequently the case in complex file formats such as NetCDF, or may have to be obtained from the user based on their knowledge of the files they are using.
  
  The programmer, obviously, can choose where in their input chain they wish to obtain from the user the required information. Because sometimes the information can be obtained from the file, it seems reasonable to place this query at the level of the image reader; however, that implies designing the exchange of this information through the IIOMetaData file. Alternatively, the programmer could obtain the required information in the next step.

**GridCoverage2D**

We must also create a class which is able to draw on the ImageReader we created above and use its contents to create the GridCoverage2D. This is merely a matter of using the ImageReader to get all the required pieces and reassembling those pieces to work for the Factory we use to create the GridCoverage2D.

The ImageReader can furnish the required RenderedImage using the ``read(..)``  method. The rest of the required information which is described below will need to be extracted from the IIOMetaData object obtained from the ``getImageMetadata(..)``  method (depending, as explained in the note above, on where the programmer decides to obtain all the required metadata).

The builder will have to wrap up the following steps.

1. Create the Categories
   
   For each band in the input data matrix, we need to create one or more Category objects.
   
   The Category instances will differ depending on how they are constructed and be either a qualitative category or a quantitative category.
   
   For the example in the previous section, we would create three category objects::
     
     Color[] colorramp = {
        Color.BLUE,
        Color.GREEN
     };
     Category[] catset = {
         new Category("NoData", new Color(0,0,0,0), -9999),
         new Category("Clouds", Color.WHITE, -8888),
         new Category("Elevation off benchmark", 
                      colorramp,
                      NumberRange.create(0, 5000),
                      NumberRange.create(-100.0, 400.0))
     };
  
2. Create the SampleDimension
   
   With an array of categories and a unit we can create a SampleDimension. In our case we use the constructor of GridSampleDimension.
   
   For the DEM example, we would have::
     
     GridSampleDimension[] sampdims = {
        new GridSampleDimension("Elevation data", catset, NonSI.FOOT)
     };

3. Create the georeferencing information
   
   The easy way to do this is to create an Envelope with the right CRS and the coordinates of the corners. This will only work for GridCoverage instances which are axis aligned, with axes in the right order, and which do not have any unusual information.
   
   For our DEM example, we would do::
     
     double westmost  =  2.0; //Degrees
     double soutmost  = 51.0;
     double eastmost  =  3.0;
     double northmost = 52.0;
     Envelope env = new Envelope2D(DefaultGeographicCRS.WGS84,
                                   westmost,
                                   soutmost,
                                   eastmost-westmost, 
                                   northmost-soutmost);
   
   More complex cases can be handled by having the user create the actual MathTransform which should be used to go from the pixel row/column offset to georeferenced space.

4. Create the GridCoverage
   
   Finally we have all the pieces to create the GridCoverage itself. We need to find a factory and then use it.
   
   For our DEM example, we would do::
     
     RenderedImage img = myImageReader.read(..);
     //Using null for the hints, we get the first factory the finder obtains.
     GridCoverageFactory fac = CoverageFactoryFinder.getGridCoverageFactory(null);
     GridCoverage2D myGridCoverage = fac.create("Paris Elevation", img, env, sampdims, null, null);
   
   These steps can all be wrapped up into the builder class.

Available Plugins
'''''''''''''''''

Keeping the above implementation advice in mind we can review some of the plugin modules that support reading and writing of images from or to file or databases.

The simple plugins provide access to a single file format of two dimensional images. More complex plugins can read, sometimes even create, image tiles mosaicing original data sets which can be very large, possibly much larger than available memory, into manageable pieces or tiles providing hierarchical levels of generalizations in pyramids or combinations of the pyramids and mosaics. Other complex plugins can handle more complex file formats such as the multi-dimensional file formats used to store temporal series of images, or multi-valued imagery.

These implementations show a range of design approaches and you are free to explore your own ideas.

We do have some ideas for a common approach we would like to see pursued:

* develop a standard set of metadata elements through which we can describe all the various data sets we encounter
* helpful base classes to assist in implementation

For now the available plugins are each creating different solutions based on the particular data types and contents on which they are working.

Simple Imagery
''''''''''''''

Several plugins provide access to simple imagery, generally a single 2 dimensional block of pixels. These Input/Output (I/O) packages are provided in separate plugins modules to GeoTools often in the 'plugin' group of modules.

These plugins use either the georeferencing information stored in the file format itself or use a second file with the georeferencing information, a so-called 'world' file in the 'image+world file' type formats.

Because GeoTools has not yet defined a single approach to for creating GridCoverages, alternative approaches have emerged for reading and writing GridCoverages from external sources. Previous sections have discussed how a user can create their own system to read and write files. This section presents the approaches used in the plugin system.

For now, the following plugins have documentation available.

* ArcGrid Plugin
* GeoTIFF Plugin
* GTOPO30 Plugin
* Image plus World File Plugin

Mosaics and Pyramids
''''''''''''''''''''

GeoTools provides modules capable of working with tiles of images which form mosaics or pyramids. Such tilings are very useful where programs need to provide efficient access to imagery either to small sections of a large image or at several levels of resolution to enable zooming.

GeoSolutions has created a set of modules in the plugins group to read
mosaics and pyramids from pregenerated files.

* Image Mosaic Plugin
* Image Pyramid Plugin

Christian Mueller has created the ``imagemosaic-jdbc`` module to read pregenerated tiles which are stored in a database.

* Image Mosaicing Pyramidal JDBC Plugin

An alternative approach has been created in the ``coverageio`` module of the unsupported module group.

Multi-dimensional
'''''''''''''''''

There are also several approaches to handling multi-dimensional data, again with different approaches taken by different groups. Usually these groups are working with NetCDF files from different organizations (and thus have different conventions to contend with).
