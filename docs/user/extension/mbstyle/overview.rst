Overview
--------

Code Example
^^^^^^^^^^^^

Quick example of module usage (API change):

.. code-block:: java

    MBStyle style = MapBoxStyle.parse( reader );
    StyleLayerDescriptor sld = style.transform();

Internally the extension provides greater access to the parsed style:

.. code-block:: java

    MBStyle mbstyle = MapBoxStyleParser.parse( reader );

    // pull back all layers for a provided source
    List<MBLayer> layers = mbstyle.layers( "sf:roads" );

    // pull back selected layers for a provided source
    List<MBLayer> layers = mbstyle.layers( "sf:roads" );

    // Which can be used to Generate a List of FeatureTypeStyles:
    List<FeatureTypeStyle> fts = layer.transform( style );


Zoom level tuning
^^^^^^^^^^^^^^^^^

The mapping between zoom levels and scale denominators is performed assuming, by default, a 512 pixels
root tile at zoom level zero. This matches the usage of vector tiles client side.
If a different value is to be used, it's possible to set a system property to change the default.
E.g., the following would match "x4" tiles:

.. code-block:: java

   System.setSystemProperty("MBSTYLE_ROOT_TILE_PIXELS", "1024") 

Example Snippets
^^^^^^^^^^^^^^^^

Before reading the :doc:`specification <spec/index>` it is helpful to look at some example snippets showing the structure of a MapBox style document.

*JSON does not allow for comments within these examples therefore comments will be noted through the placement of the comment between open ``<`` and close ``>`` angle brackets. All properties are optional unless otherwise noted as Requried*

MapBox Style document snippet
'''''''''''''''''''''''''''''

The root-properties structure of a MapBox Style document captures the document name, data sources uses, graphics (sprites and glyphs) avaialble, before finally documenting the layers to be shown.

.. code-block:: text

   {
     "version": 8, <Required>
     "name": "Mapbox Streets",
     "sprite": "mapbox://sprites/mapbox/streets-v8",
     "glyphs": "mapbox://fonts/mapbox/{fontstack}/{range}.pbf",
     "sources": {...}, <Required>
     "layers": [...] <Required>
   }

layers structure snippet
''''''''''''''''''''''''

Layers are drawn in the order they appear in the layer array. Layers have two additional properties that determine how data is rendered: *layout* and *paint*.

.. code-block:: text

   {
     "layers" : [
       {
         "id": "string", 
         "type": "...", <what kind of layer to draw>
         "source": "test-source", <source>
         "source-layer": "test-source-layer",  <within source which type to draw>
         "layout": {...}, 
         "paint": {...},
       }
     ]
   }

background layer snippet
''''''''''''''''''''''''

A `background` layer is used to define the background of map. You can only use one of `background-color` or `background-pattern` at a time.

.. code-block:: text

   {
     "layers" : [
       {
         "id": "backgroundcolor",
         "type": "background",
         "source": "test-source",
         "source-layer": "test-source-layer",
         "layout": {
           "visibility": "visible"
         },
         "paint": {
           "background-opacity": 0.45,
           "background-color": "#00FF00"
         }
       }
     ]
   }

fill layer snippet
''''''''''''''''''''''''

A `fill` layer is used to draw polygons:

.. code-block:: text

   {
     "layers": [
       {
         "id": "testid",
         "type": "fill",
         "source": "geoserver-states",
         "source-layer": "states",
         "layout": {
           "visibility": "visible"
         },
         "paint": {
           "fill-antialias": "true",
           "fill-opacity": 0.84,
           "fill-color": "#FF595E",
           "fill-outline-color":"#1982C4",
           "fill-translate": [20,20],
           "fill-translate-anchor": "map",
           "fill-pattern": <String>
         }
       }
     ]
   }

line layer snippet
''''''''''''''''''''''''

A `line` layer is used to draw linetrings:


.. code-block:: javascript

   {
     "layers": [
       {
         "id": "test-id",
         "type": "line",
         "source": "test-source",
         "source-layer": "test-source-layer",
         "layout": {
             "line-cap": "square",
             "line-join": "round",
             "line-mitre-limit": 2, <Optional - Requires line-join=mitre>
             "line-round-limit": 1.05, <Optional - Requires line-join=round>
             "visibility": "visible"
         },
         "paint": {
           "line-color": "#0099ff",
           "line-opacity": 0.5,
           "line-translate": [3,3],
           "line-translate-anchor": "viewport",
           "line-width": 10,
           "line-gap-width": 8,
           "line-offset": 4,
           "line-blur": 2,
           "line-dasharray": [50, 50],
           "line-pattern": <String>
         }
       }
     ],
   }

symbol layer snippet
''''''''''''''''''''

A `symbol` layer is used to draw an icon at each point location. This is by far the most complicated layer type as it is used for two distinct uses, to mark points with icons, and also to generate labels.

.. code-block:: text

   {
     "layers": [
       {
         "id": "test-id",
         "type": "symbol",
         "source": "test-source",
         "source-layer": "test-source-layer",
         "layout": {
             "symbol-placement": "", <Enum, [Point, line] Defaults to Point>
             "symbol-spacing": "", <Number in pixels. Defaults to 250, requires symbol-placement = line>
             "symbol-avoid-edges": "", <Boolean defaults to true>
             "icon-allow-overlap": "", <Boolean defaults to false>
             "icon-ignore-placement": "", <Boolean defaults to false>
             "icon-optional": "", <Boolean defaults to false, requires icon-image and text-field>
             "icon-rotation-alignment": "", <Enum, [map, viewport, auto] defaults to auto requires icon-image>
             "icon-size": "", <Number, defaults to 1>
             "icon-rotation-alignment": "", <Enum, [none, width, height, both] defaults to none requires icon-image and text-field>
             "icon-text-fit-padding": "", <Array, units in pixels, defaults to [0,0,0,0]
                 requires icon-image, text-field and icon-text-fit of one of [both, width, height]>
             "icon-image": "", <String>
             "icon-rotate": "", <Number, in degrees, defaults to 0>
             "icon-padding": "", <Number, units in pixels, defaults to 2>
             "icon-keep-upright": "", <Boolean defaults to false, requires icon-image, icon-rotation-alignment = map
                 and symbol-placement = line>
             "icon-offset": "", <Array, defaults to [0,0] requires icon-image>
             "text-pitch-alignment": "", <Enum, [map, viewport, auto] defaults to auto requires text-field>
             "text-rotation-alignment": "", <Enum, [map, viewport, auto] defaults to auto requires text-field>
             "text-field": "", <String>
             "text-font": "", <Array, defaults to [Open Sans Regular,Arial Unicode MS Regular], requires text-field>
             "text-size": "", <Number, units in pixels, defaults to 16, requires text-field>
             "text-max-width": "", <Number, units in ems, defaults to 10 requires text-field>
             "text-line-height": "", <Number, units in ems, defaults to 1.2 requires text-field>
             "text-letter-spacing": "", <Number, units in ems, defaults to 0 requires text-field>
             "text-justify": "", <Enum, [left, center, right] defaults to center requires text-field>
             "text-anchor": "", <Enum, [center, left, right, top, bottom, top-left,
                top-right, bottom-left, bottom-right] defaults to center>
             "text-max-angle": "", <Number units in degrees, defaults to 45>
             "text-rotate": "", <Number units in degrees, defaults to 0>
             "text-padding": "", <Number units in pixels, defaults to 2>
             "text-keep-upright": "", <Boolean, defaults to true, requires text-field, text-rotation-alignment = map,
                and symbol-placement = line>
             "text-transform": "", <Enum [none, uppercase, lowercase] defaults to none, requires text-field>
             "text-offset": "", <Array, units in ems, defaults to [0,0], requires text-field>
             "text-allow-overlap": "", <Boolean, defaults to false, requires text-field>
             "text-ignore-placement": "", <Boolean, defaults to false, requires text-field>
             "text-optional": "", <Boolean, defaults to false, requires text-field and icon-image>
             "visibility": "visible"
         },
         "paint": {
           "icon-opacity": "", <Number, defaults to 1>
           "icon-color": "", <Color, defaults to #000000, requires icon-image>
           "icon-halo-color": "", <Color, defaults to rgba(0,0,0,0) requires icon-image>
           "icon-halo-width": "", <Number, units in pixels, defaults to 0 requires icon-image>
           "icon-halo-blur": "", <Number, units in pixels, defaults to 0 requires icon-image>
           "icon-translate": "", <Array, units in pixels, defaults to [0,0], requires icon-image>
           "icon-translate-anchor": "", <Enum, [map, viewport], defaults to map, requires icon-image, icon-translate>
           "text-opacity": "", <Number, defaults to 1 requires text-field>
           "text-halo-color": "", <Color, defaults to rgba(0,0,0,0) requires text-field>
           "text-halo-width": "", <Number, units in pixels, defaults to 0 requires text-field>
           "text-halo-blur": "", <Number, units in pixels, defaults to 0 requires text-field>
           "text-translate": "", <Array units in pixels defaults to [0,0] requires text-field>
           "text-translate-anchor": "" <Enum, [map, viewport] defaults to map, requires text-field, text-translate>
         }
       }
     ],
   }

raster layer snippet
''''''''''''''''''''

A `raster` layer is used for image visualization:

.. code-block:: text

   {
     "layers": [
       {
         "id": "test-id",
         "type": "raster",
         "source": "test-source",
         "source-layer": "test-source-layer",
         "layout": {
             "visibility": "visible"
         },
         "paint": {
           "raster-opacity": "", <Number defaults to 1>
           "raster-hue-rotate": "", <Number units in degrees, defaults to 0>
           "raster-brightness-min": "", <Number, defaults to 0>
           "raster-brightness-max": "", <Number, defaults to 1>
           "raster-saturation": "", <Number, defaults to 0>
           "raster-contrast": "", <Number, defaults to 0>
           "raster-fade-duration": "" <Number, units in milliseconds, defaults to 300>
         }
       }
     ],
   }

cricle layer snippet
''''''''''''''''''''

A `circle` layer is used to draw a circle at each point location:

.. code-block:: text

   {
     "layers": [
       {
         "id": "test-id",
         "type": "raster",
         "source": "test-source",
         "source-layer": "test-source-layer",
         "layout": {
             "visibility": "visible"
         },
         "paint": {
           "circle-radius": "", <Number, units in pixels, defaults to 5>
           "circle-color": "", <Color, defaults to #000000>
           "circle-blur": "", <Number, defaults to 0>
           "circle-opacity": "", <Number, defaults to 1>
           "circle-translate": "", <Array, units in pixels, defaults to [0,0]>
           "circle-translate-anchor": "", <Enum, [map, viewport] defaults to map requires circle-translate>
           "circle-pitch-scale": "", <Enum, [map, viewport] defaults to map>
           "circle-stroke-width": "", <Number, units in pixels, defaults to 0>
           "circle-stroke-color": "", <Color, defaults to #000000>
           "circle-stroke-opacity": "", <Number, defaults to 1>
         }
       }
     ]
   }

fill-extrusion layer snippet
''''''''''''''''''''''''''''

A `fill-extrusion` adds a shadow effect.

.. code-block:: text

   {
     "layers": [
       {
         "id": "test-id",
         "type": "fill-extrusion",
         "source": "test-source",
         "source-layer": "test-source-layer",
         "layout": {
             "visibility": "visible"
         },
         "paint": {
           "fill-extrusion-opacity": "", <Number, defaults to 1>
           "fill-extrusion-color": "", <Color, defaults to #000000, disabled by fill-extrusion-pattern>
           "fill-extrusion-translate": "", <Array, units in pixels, defaults to [0,0]>
           "fill-extrusion-translate-anchor": "", <Enum, [map, viewport] defaults to map requires fill-extrusion-translate>
           "fill-extrusion-pattern": "", <String>
           "fill-extrusion-height": "", <Number, units in meters, defaults to 0>
           "fill-extrusion-base": "" <Number, units in meters, defaults to 0, requires fill-extrusion-height>
         }
       }
     ]
   }


