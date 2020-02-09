.. _sources:

Sources
-------

Sources supply data to be shown on the map. The type of source is
specified by the ``"type"`` property, and must be one of vector, raster,
GeoJSON, image, video, canvas. Adding a source won't immediately make
data appear on the map because sources don't contain styling details
like color or width. Layers refer to a source and give it a visual
representation. This makes it possible to style the same source in
different ways, like differentiating between types of roads in a
highways layer.

Tiled sources (vector and raster) must specify their details in terms of
the `TileJSON
specification <https://github.com/mapbox/tilejson-spec>`__. This can be
done in several ways:

-  By supplying ``TileJSON`` properties such as ``"tiles"``, ``"minzoom"``,
   and ``"maxzoom"`` directly in the source:

   .. code-block:: javascript

       "mapbox-streets": {
         "type": "vector",
         "tiles": [
           "http://a.example.com/tiles/{z}/{x}/{y}.pbf",
           "http://b.example.com/tiles/{z}/{x}/{y}.pbf"
         ],
         "maxzoom": 14
       }

-  By providing a ``"url"`` to a ``TileJSON`` resource:


   .. code-block:: javascript

       "mapbox-streets": {
         "type": "vector",
         "url": "http://api.example.com/tilejson.json"
       }

-  By providing a URL to a WMS server that supports EPSG:3857 (or
   EPSG:900913) as a source of tiled data. The server URL should contain
   a ``"{bbox-epsg-3857}"`` replacement token to supply the ``bbox``
   parameter.

   .. code-block:: javascript

       "wms-imagery": {
         "type": "raster",
         "tiles": [
         'http://a.example.com/wms?bbox={bbox-epsg-3857}&format=image/png&service=WMS&version=1.1.1&request=GetMap&srs=EPSG:3857&width=256&height=256&layers=example'
         ],
         "tileSize": 256
       }

vector
~~~~~~

A vector tile source. Tiles must be in `Mapbox Vector Tile
format <https://www.mapbox.com/developers/vector-tiles/>`__. All
geometric coordinates in vector tiles must be between ``-1 * extent``
and ``(extent * 2) - 1`` inclusive. All layers that use a vector source
must specify a ``"source-layer"`` value. For vector tiles hosted by
Mapbox, the ``"url"`` value should be of the form ``mapbox://mapid``.

.. code-block:: javascript

    "mapbox-streets": {
      "type": "vector",
      "url": "mapbox://mapbox.mapbox-streets-v6"
    }

``url``
^^^^^^^

*Optional* :ref:`types-string`.



A URL to a ``TileJSON`` resource. Supported protocols are ``http:``,
``https:``, and ``mapbox://<mapid>``.

``tiles``
^^^^^^^^^

*Optional* :ref:`types-array`.



An array of one or more tile source URLs, as in the ``TileJSON`` spec.

``minzoom``
^^^^^^^^^^^

*Optional* :ref:`types-number`. *Defaults to* 0.


Minimum zoom level for which tiles are available, as in the ``TileJSON``
spec.

``maxzoom``
^^^^^^^^^^^

*Optional* :ref:`types-number`. *Defaults to* 22.


Maximum zoom level for which tiles are available, as in the ``TileJSON``
spec. Data from tiles at the ``maxzoom`` are used when displaying the map at
higher zoom levels.


.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.10.0
     - Not yet supported
     - >= 2.4.0

``raster``
~~~~~~~~~~

A raster tile source. For raster tiles hosted by Mapbox, the ``"url"``
value should be of the form ``mapbox://mapid``.

.. code-block:: javascript

    "mapbox-satellite": {
      "type": "raster",
      "url": "mapbox://mapbox.satellite",
      "tileSize": 256
    }

``url``
^^^^^^^

*Optional* :ref:`types-string`.


A URL to a ``TileJSON`` resource. Supported protocols are ``http:``,
``https:``, and ``mapbox://<mapid>``.

tiles
^^^^^

*Optional* :ref:`types-array`.



An array of one or more tile source URLs, as in the ``TileJSON`` spec.

``minzoom``
^^^^^^^^^^^^

*Optional* :ref:`types-number`. *Defaults to* 0.


Minimum zoom level for which tiles are available, as in the ``TileJSON``
spec.

``maxzoom``
^^^^^^^^^^^

*Optional* :ref:`types-number`. *Defaults to* 22.


Maximum zoom level for which tiles are available, as in the ``TileJSON``
spec. Data from tiles at the ``maxzoom`` are used when displaying the map at
higher zoom levels.

``tileSize``
^^^^^^^^^^^^

*Optional* :ref:`types-number`. *Defaults to* 512.


The minimum visual size to display tiles for this layer. Only
configurable for raster layers.

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.10.0
     - Not yet supported
     - >= 2.4.0

geojson
~~~~~~~

A `GeoJSON <http://geojson.org/>`__ source. Data must be provided via a
``"data"`` property, whose value can be a URL or inline GeoJSON.

.. code-block:: javascript

    "geojson-marker": {
      "type": "geojson",
      "data": {
        "type": "Feature",
        "geometry": {
          "type": "Point",
          "coordinates": [-77.0323, 38.9131]
        },
        "properties": {
          "title": "Mapbox DC",
          "marker-symbol": "monument"
        }
      }
    }


This example of a GeoJSON source refers to an external GeoJSON document
via its URL. The GeoJSON document must be on the same domain or
accessible using `CORS <http://enable-cors.org/>`__.

.. code-block:: javascript

    "geojson-lines": {
      "type": "geojson",
      "data": "./lines.geojson"
    }

data
^^^^

*Optional*


A URL to a GeoJSON file, or inline GeoJSON.

``maxzoom``
^^^^^^^^^^^

*Optional* :ref:`types-number`. *Defaults to* 18.


Maximum zoom level at which to create vector tiles (higher means greater
detail at high zoom levels).

buffer
^^^^^^

*Optional* :ref:`types-number`. *Defaults to* 128.


Size of the tile buffer on each side. A value of 0 produces no buffer. A
value of 512 produces a buffer as wide as the tile itself. Larger values
produce fewer rendering artifacts near tile edges and slower
performance.

tolerance
^^^^^^^^^

*Optional* :ref:`types-number`. *Defaults to* 0.375.


Douglas-Peucker simplification tolerance (higher means simpler
geometries and faster performance).

cluster
^^^^^^^

*Optional* :ref:`types-boolean`. *Defaults to* false.


If the data is a collection of point features, setting this to true
clusters the points by radius into groups.

``clusterRadius``
^^^^^^^^^^^^^^^^^^

*Optional* :ref:`types-number`. *Defaults to* 50.



Radius of each cluster if clustering is enabled. A value of 512
indicates a radius equal to the width of a tile.

``clusterMaxZoom``
^^^^^^^^^^^^^^^^^^

*Optional* :ref:`types-number`.


Max zoom on which to cluster points if clustering is enabled. Defaults
to one zoom less than ``maxzoom`` (so that last zoom features are not
clustered).

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.10.0
     - Not yet supported
     - >= 2.4.0
   * - clustering
     - >= 0.14.0
     - Not yet supported
     - Not yet supported

image
~~~~~

An image source. The ``"url"`` value contains the image location.

The ``"coordinates"`` array contains ``[longitude, latitude]`` pairs for
the image corners listed in clockwise order: top left, top right, bottom
right, bottom left.

.. code-block:: javascript

    "image": {
      "type": "image",
      "url": "/mapbox-gl-js/assets/radar.gif",
      "coordinates": [
          [-80.425, 46.437],
          [-71.516, 46.437],
          [-71.516, 37.936],
          [-80.425, 37.936]
      ]
    }

``url``
^^^^^^^

*Required* :ref:`types-string`.

URL that points to an image.

coordinates
^^^^^^^^^^^

*Required* :ref:`types-array`.

Corners of image specified in longitude, latitude pairs.

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.10.0
     - Not yet supported
     - Not yet supported

video
~~~~~

A video source. The ``"urls"`` value is an array. For each URL in the
array, a video element
`source <https://developer.mozilla.org/en-US/docs/Web/HTML/Element/source>`__
will be created, in order to support same media in multiple formats
supported by different browsers.

The ``"coordinates"`` array contains ``[longitude, latitude]`` pairs for
the video corners listed in clockwise order: top left, top right, bottom
right, bottom left.

.. code-block:: javascript

    "video": {
      "type": "video",
      "urls": [
        "https://www.mapbox.com/drone/video/drone.mp4",
        "https://www.mapbox.com/drone/video/drone.webm"
      ],
      "coordinates": [
         [-122.51596391201019, 37.56238816766053],
         [-122.51467645168304, 37.56410183312965],
         [-122.51309394836426, 37.563391708549425],
         [-122.51423120498657, 37.56161849366671]
      ]
    }

``urls``
^^^^^^^^^

*Required* :ref:`types-array`.



URLs to video content in order of preferred format.

coordinates
^^^^^^^^^^^

*Required* :ref:`types-array`.


Corners of video specified in longitude, latitude pairs.

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.10.0
     - Not yet supported
     - Not yet supported

canvas
~~~~~~

A canvas source. The ``"canvas"`` value is the ID of the canvas element
in the document.

The ``"coordinates"`` array contains ``[longitude, latitude]`` pairs for
the video corners listed in clockwise order: top left, top right, bottom
right, bottom left.

If an HTML document contains a canvas such as this:
::

    <canvas id="mycanvas" width="400" height="300" style="display: none;"></canvas>


the corresponding canvas source would be specified as follows:

.. code-block:: javascript

    "canvas": {
      "type": "canvas",
      "canvas": "mycanvas",
      "coordinates": [
         [-122.51596391201019, 37.56238816766053],
         [-122.51467645168304, 37.56410183312965],
         [-122.51309394836426, 37.563391708549425],
         [-122.51423120498657, 37.56161849366671]
      ]
    }

coordinates
^^^^^^^^^^^

*Required* :ref:`types-array`.

Corners of canvas specified in longitude, latitude pairs.

animate
^^^^^^^

Whether the canvas source is animated. If the canvas is static,
``animate`` should be set to ``false`` to improve performance.

canvas
^^^^^^

*Required* :ref:`types-string`.

HTML ID of the canvas from which to read pixels.


.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.32.0
     - Not yet supported
     - Not yet supported
     
.. include:: footer.txt