Map
---

The gt-render module defines a new data structure to represent the contents of a map. MapContent
defines a map as a series of layers (which are drawn in order). This is not part of the formal
gt-opengis module because it is not based on a standard.

MapContent
^^^^^^^^^^

MapContent is used to capture the contents of a map for rendering.

.. image:: /images/map_content.PNG

These ideas are broken down into the following three classes:

* **MapContent**
  
  The contents of a map to be drawn.

* **MapViewport**
  
  Represents the area of the map to drawn. This includes both:
  
  * The area of the world: the spatial extent (and conceptually the height / depth / time
    information) needed to render a slice of data.
    At a pragmatic level the MapViewport includes the "world" coordinate reference system
    into which all content is transformed prior to display.
  * The area of the screen: either expressed as a rectangle or provided as an affine transform
    used to zoom into a specific area.

* **Layer** - represents a layer of content to be drawn; layers are held in a list and are drawn in
  order.

Examples:

* Direct access to layer information is provided::
  
    MapContent content = mapFrame.getMapContent();
    if( content.layers().get(0) instanceof FeatureLayer ){
        FeatureLayer selectionLayer = (FeatureLayer) content.layers().get(0);
        selectLayer.setStyle( style );
    }

* Zoom out to show all content::
    
        MapContent content = mapFrame.getMapContent();
        MapViewport viewport = content.getMapViewport();
        ReferencedEnvelope maxBounds = null;
        CoordinateReferenceSystem mapCRS = viewport.getCoordianteReferenceSystem();
        
        for (Layer layer : content.layers()) {
            ReferencedEnvelope dataBounds = layer.getBounds();
            CoordinateReferenceSystem dataCrs = dataBounds.getCoordinateReferenceSystem();
            if (!CRS.equalsIgnoreMetadata(dataCrs, mapCRS)) {
                dataBounds = dataBounds.transform(mapCRS, true);
            }
            if (maxBounds == null) {
                maxBounds = dataBounds;
            } else {
                maxBounds.expandToInclude(dataBounds);
            }
        }
        viewport.setBounds( maxBounds );

Layer
^^^^^

We have different subclasses of of Layer each specifically made for working with different kinds
of content.

.. image:: /images/layer.PNG

The important information each Layer implementation must provided is:

* Layer.getTitle()
* Layer.setTitle( String )
  
  Used in a legend to display to the user
* Layer.isVisible()
* Layer.setVisible( boolean )
* Layer.getuserData()
  
  A general purpose map used by applications to hold information. This
  approach was preferred as an alternative to loading up Layer with additional methods to
  handle selection, error feedback and so forth.
* Layer.getBounds() - the extent of the data being rendered by the Layer
* Layer.addMapLayerListener()
* Layer.removeMapLayerListener()
  
  Used to notify the renderer of any changes when used in an interactive setting.

With these ideas in mind we can now explore the different kinds of content that can be added
to a map:
  
FeatureLayer
''''''''''''

Feature layer is set up to render information from a FeatureSource.
  
.. image:: /images/feature_layer.PNG
 
You can use the various method of the DataUtilities class to convert your information into
a FeatureSource if it happens to be in another format. This is what the constructor that
takes a FeatureCollection does internally.

GridCoverageLayer
'''''''''''''''''

Used to render a GridCoverage.

.. image:: /images/gridcoverage_layer.PNG

Note that direct use of a GridCoverage in this fashion is generally not as efficent 
as using GridReaderLayer below.

GridReaderLayer
'''''''''''''''

Used to render raster information on the fly directly from a GridCoverageReader.

.. image:: /images/gridreader_layer.PNG
  
This is an efficient solution (much like FeatureSource) in that for many cases the correct visual
can be determined without reading all of the raster into memory:

* When zoomed in the amount of the file read can be limited when working with common formats
  such as geotiff. Other formats such as JPEG require that the entire image be loaded each time.
* When zoomed out information from a raster overlay can be used (if avaialble) to avoid reading the
  entire file.

The performance of GridReaderLayer is dependent on how you have tuned your Java Advanced Imaging
"TileCache" and on the amount of work you have put into prepairing your data for display.

This class has been extended by gt-wms for the rendering of WMS information.

DirectLayer
'''''''''''

*Experimental*: DirectLayer is used fill in your own custom renderer (primarily intended for
drawing scalebars, north arrows and grids to decorate the map).
  
.. image:: /images/direct_layer.PNG
  
This concept is considered experimental and is not currently hooked up.
  
MapContext
^^^^^^^^^^

An earlier draft of these ideas is based on initial OGC discussion papers:
 
* Web Map Context (WMS Context)
* Open Web Service Context (OWS Context)

The GeoTools community actively looking to collaborate with other projects (such as OpenJUMP,
uDig and deegree) in order to collaborate on these ideas. If open source collaboration fails
we will look to traditional collaboration with a standards body in the form of the
OGC working group on "Open Web Context" documents.
   
References:

* http://www.opengeospatial.org/standards/wmc
* http://www.opengeospatial.org/projects/groups/owscontextswg

These initial concepts are preserved with the following extensions to MapContent.

.. image:: /images/map_context.PNG

The critical design difference here is a single *MapLayer* which is general purpose for working
with any kind of content (and also confusing to work with as their is no easy way to check what
kind of content is in use).

.. note::

  Internally this code has been refactored to use MapContent / Layer and MapViewport. As such we do
  not recommend using MapContext and MapLayer for new development.
  
  At a technical level we no longer keep instances of of MapLayer around; instead each is a
  shallow wrapper around a layer holding the specific content (FeatureLayer, GridReaderLayer,
  etc...).
  
  In the event clinet code is expecting a MapLayer; this wrapper is recreated as needed and
  returned from getLayer( int ) method.
  
  In a similar fashion the various methods for managing the area of interest delegate to
  MapViewport.
