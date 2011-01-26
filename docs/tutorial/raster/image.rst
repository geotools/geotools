:Author: Jody Garnett
:Author: Micheal Bedward
:Thanks: geotools-user list
:Version: |release|
:License: Create Commons with attribution

.. include:: <isonum.txt>

.. _image:

*****************
Image Tutorial
*****************

.. contents::

Welcome
========

Welcome to Geospatial for Java. This tutorial is aimed at Java developers who are new to geospatial
and would like to get started.

Please ensure you have your IDE set up with access to the GeoTools jars (either through maven or 
a directory of Jar files). For those of you using Maven we will start off each section with the
dependencies required.

This workbook is once again code first giving you a chance to try the concepts out in a Java
program and then read on for more information if you have any questions.

This workbook covers the handling of GridCoverages (in the rest of computing these are known as
rasters or bitmaps). The idea is that a coverage completely covers the surface of a map with no
gaps forming a surface.  A grid coverage is a special case of a coverage in which all the features
end up as small rectangles on the surface of the earth.

This idea is so similar to our concept of pixels we end up using a lot of the same file formats to
represent a grid coverage in our computing systems.

This workbook is part of the FOSS4G 2009 conference proceedings.

Jody Garnett

   Jody Garnett is the lead architect for the uDig project; and on the steering
   committee for GeoTools; GeoServer and uDig. Taking the roll of geospatial
   consultant a bit too literally Jody has presented workshops and training
   courses in every continent (except Antarctica). Jody Garnett is an employee
   of LISAsoft.

Michael Bedward

   Michael Bedward is a researcher with the NSW Department of Environment and
   Climate Change and an active contributor to the GeoTools users' list. He has
   a particularly wide grasp of all the possible mistakes one can make using
   GeoTools.
   
Image Lab Application
======================

In the earlier examples we looked at reading and displaying shapefiles. For 
:download:`ImageLab.java <../../src/main/java/org/geotools/tutorial/raster/ImageLab.java>` we are
going add raster data to the the mix.

By displaying a three-band global satellite image and overlaying it with country boundaries from
a shapefile.

1. Please ensure your pom.xml includes the following.

   Most of these dependencies in the earlier examples, the modules we have  added are **gt-geotiff**
   which allows us to read raster map data from a GeoTIFF file and **gt-image** which allows us to
   read an Image+World format file set (e.g. jpg + jpw).

.. literalinclude:: artifacts/pom.xml
        :language: xml
        :start-after: </properties>
        :end-before: <repositories>

2. Please create the file **ImageLab.java** and copy and paste in the following code:

.. literalinclude:: ../../src/main/java/org/geotools/tutorial/raster/ImageLab.java
   :language: java
   :start-after: // docs start source
   :end-before: // docs end main

Parameters
----------

One thing that has been a mystery until now is how the DataStore wizards are created. The wizards
are created from a description of the parameters needed when connecting.

We are going to use these same facilities now to prompt the user:

1. We will use **JParameterListWizard**, to prompt for the raster file and the shapefile that will
   be displayed over it:

   .. literalinclude:: ../../src/main/java/org/geotools/tutorial/raster/ImageLab.java
      :language: java
      :start-after: // docs start get layers
      :end-before: // docs end get layers

The use of **Parameter** objects for each input file. The arguments passed to the Parameter
constructor are:
   
:key: an identifier for the Parameter

:type: the class of the object that the Parameter refers to

:title: a title which the wizard will use to label the text field

:description: a brief description which the wizard will display below the text field

:metadata: a Map containing additional data for the Parameter - in our case this is one or more file extensions.

.. tip: KVP

  The class **KVP** is a handy class for creating a Map of String:Object pair.
  
  Here is an example of using a Hashmap:: 

  `Map<String, Object> map = new HashMap<String, Object>`
  `map.add(Parameter.EXT, "jpg");`
  `map.add(Parameter.EXT, "tif");`

  The same example can be done in a single line using **KVP**::
  
  `KVP map = new KVP(Parameter.EXT, "jpg", Parameter.EXT, "tif");`

Displaying the map
------------------

To display the map on screen we create a **MapContext**, add the image and the shapefile to it,
and pass it to a **JMapFrame**.

1. Rather than using the static JMapFrame.showMap method, as we have in previous examples, we create a
   map frame and customize it by adding a menu to choose the image display mode. 

.. literalinclude:: ../../src/main/java/org/geotools/tutorial/raster/ImageLab.java
   :language: java
   :start-after: // docs start display layers
   :end-before: // docs end display layers

2. Note that we are creating a **Style** for each of the map layers:
   
   * A greyscale Style for the initial image display, created with a method that we'll examine next
   * A simple outline style for the shapefile using the **SLD** utility class

3. Creating a greyscale Style prompts the user for the image band to display; and then generates
   a style accordingly.

.. literalinclude:: ../../src/main/java/org/geotools/tutorial/raster/ImageLab.java
   :language: java
   :start-after: // docs start create greyscale style
   :end-before: // docs end create greyscale style

4. To display color we need to use a slightly more complex Style that specifies which bands in the
   grid coverage map to the R, G and B colors on screen. 
   
   While this will be very easy for a simple color image; it can be harder for things like
   satellite images where none of the bands quite line up with what human eyes see.
   
   The methods checks the image to see if its bands (known as *sample dimensions*) have labels
   indicating which to use. If not, we just use the first three bands and hope for the best !

.. literalinclude:: ../../src/main/java/org/geotools/tutorial/raster/ImageLab.java
   :language: java
   :start-after: // docs start create rgb style
   :end-before: // docs end source

Running the application
=======================

If you need some sample data to display you can download the uDig
`sample dataset <http://udig.refractions.net/docs/data-v1_2.zip>`_ which contains:

* bluemarble.tif
* countries.shp


1. When you run the program you will first see the wizard dialog prompting your for the image
   and shapefile.
  
  .. image:: images/ImageLab_dialog.png
      :width: 60%

2. The initial map display shows the image as a greyscale, single-band view.
   
   .. image:: images/ImageLab_display.png
      :width: 60%
      
3.   Experiment with displaying different bands in greyscale and swapping to the RGB display.

Extra things to try
===================

* Modify the file prompt wizard, or the menu, to allow additional shapfiles to be overlaid onto
  the image.

* Add a map layer table to the JMapFrame using frame.enableLayerTable(true) so that you can toggle
  the visibility of the layers.

* Advanced: Experiment with Styles for the raster display: e.g. contrast enhancement options;
  display based on ranges of image band values
  
* Advanced: You can also use GeoTools to work with raster information being served up from a remote
  Web Map Service.
  
.. literalinclude:: ../../src/main/java/org/geotools/tutorial/wms/WMSLab.java
   :language: java

Raster Data
============

Grid Coverage
-------------

Support for raster data is provided by the concept of a GridCoverage.  As programmers we are used 
to working with raster data in the form of bitmapped graphics such as JPEG, GIF and PNG files.

On the geospatial side of things there is the concept of a Coverage. A coverage is a collection of
spatially located features. Informally, we equate a coverage with a map (in the geographic rather
than the programming sense).

A GridCoverage is a special case of Coverage where the features are  rectangles forming a grid that
fills the area of the coverage. In our Java code we can use a bitmapped graphic as the backing data
structure for a GridCoverage together with additional elements to record spatial bounds in a 
specific coordinate reference system.

There are many kinds of grid coverage file formats. Some of the most common are:

world plus image
    A normal image format like jpeg or png that has a side-car file describing where it is located
    as well as a prj sidecar file defining the map projection just like a shapefile uses.
                   
Geotiff
    A normal tiff image that has geospatial information stored in the image metadata fields.

JPEG2000
    The sequel to jpeg that uses wavelet compression  to handle massive images. The file
    format also supports metadata fields that can be used to store geospatial information.

There are also more exotic formats such as ECW and MRSID that can be supported if you have installed
the imageio-ext project into your JRE.

Web Map Server
--------------

Another source of imagery is a  Web Map Server (WMS). The Web Map Server specification is defined
by the Open Geospatial Consortium |hyphen| an industry body set up to encourage collaboration on this sort
of thing.

At a basic level we can fetch information from a WMS using a GetMap operation::

    http://localhost:8080/geoserver/wms?bbox=-130,24,-66,50&styles=population&Format=image/png&request=GetMap&layers=topp:states&width=550&height=250&srs=EPSG:4326

The trick is knowing what parameters to fill in for |ldquo| layer |rdquo| and |ldquo| style |rdquo| when making one of these
requests.

The WMS Service offers a GetCapabilities document that describes what layers are available and wha
other operations like GetMap are available to work on those layers.

GeoTools has a great implementation to help out here |hyphen| it can parse that capabilities document for
a list of layers, the supported image formats and so forth.

.. code-block:: java
   
   URL url =  url = new URL("http://www2.dmsolutions.ca/cgi-bin/mswms_gmap?VERSION=1.1.0&REQUEST=GetCapabilities");
   
   WebMapServer wms = new WebMapServer(url);
   WMSCapabilities capabilities = wms.getCapabilities();

   // gets all the layers in a flat list, in the order they appear in
   // the capabilities document (so the rootLayer is at index 0)
   List layers = capabilities.getLayerList();

WebMapServer class also knows how to set up a GetMap request for several different version of the
WMS standard.

.. code-block:: java

   GetMapRequest request = wms.createGetMapRequest();
   request.setFormat("image/png");
   request.setDimensions("583", "420"); //sets the dimensions to be returned from the server
   request.setTransparent(true);
   request.setSRS("EPSG:4326");
   request.setBBox("-131.13151509433965,46.60532747661736,-117.61620566037737,56.34191403281659");

   GetMapResponse response = (GetMapResponse) wms.issueRequest(request);
   BufferedImage image = ImageIO.read(response.getInputStream());

