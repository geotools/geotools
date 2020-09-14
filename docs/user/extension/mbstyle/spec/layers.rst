.. _layers:

Layers
------

A style's ``layers`` property lists all of the layers available in that
style. The type of layer is specified by the ``"type"`` property, and
must be one of background, fill, line, symbol, raster, circle,
fill-extrusion.

Except for layers of the background type, each layer needs to refer to a
source. Layers take the data that they get from a source, optionally
filter features, and then define how those features are styled.

::

    "layers": [
      {
        "id": "water",
        "source": "sf:roads",
        "source-layer": "water",
        "type": "fill",
        "paint": {
          "fill-color": "#00ffff"
        }
      }
    ]

Layer Properties
~~~~~~~~~~~~~~~~

id
^^

*Required* :ref:`types-string`.


Unique layer name.

type
^^^^

*Optional* :ref:`types-enum`. *One of fill, line, symbol, circle, fill-extrusion, raster, background.*


Rendering type of this layer.


*fill*
    A filled polygon with an optional stroked border.

*line*
    A stroked line.

*symbol*
    An icon or a text label.

*circle*
    A filled circle.

*fill-extrusion*
    An extruded (3D) polygon.

*raster*
    Raster map textures such as satellite imagery.

*:doc:`background`*
    The background color or pattern of the map.

metadata
^^^^^^^^

*Optional*


Arbitrary properties useful to track with the layer, but do not
influence rendering. Properties should be prefixed to avoid collisions,
like ``mapbox:``.

source
^^^^^^

*Optional* :ref:`types-string`.



Name of a source description to be used for this layer.

source-layer
^^^^^^^^^^^^

*Optional* :ref:`types-string`.



Layer to use from a vector tile source. Required if the source supports
multiple layers.

``minzoom``
^^^^^^^^^^^^

*Optional* :ref:`types-number`.



The minimum zoom level on which the layer gets parsed and appears on.

``maxzoom``
^^^^^^^^^^^^

*Optional* :ref:`types-number`.



The maximum zoom level on which the layer gets parsed and appears on.

filter
^^^^^^

*Optional* :ref:`Expression <expressions>`.

A expression specifying conditions on source features. Only features
that match the filter are displayed.

::

    ["to-number", value, fallback: value, fallback: value, ...]: number

.. list-table::
   :widths: 34, 22, 22, 22
   :width: 100%
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.41.0
     - >= 23.0
     - 
   * - collator
     - >= 0.45.0
     - >= Not yet supported
     - 
   * - other filter
     - >= 0.10.0
     - >= 17.1
     - >= 2.4.0
   * - other filter ``has``/``!has``
     - >= 0.19.0
     - >= 17.1
     - >= 2.4.0

layout
^^^^^^

layout properties for the layer

paint
^^^^^

*Optional* paint properties for the layer

Layers have two sub-properties that determine how data from that layer
is rendered: ``layout`` and ``paint`` properties.

*Layout properties* appear in the layer's ``"layout"`` object. They are
applied early in the rendering process and define how data for that
layer is passed to the GPU. For efficiency, a layer can share layout
properties with another layer via the ``"ref"`` layer property, and
should do so where possible. This will decrease processing time and
allow the two layers will share GPU memory and other resources
associated with the layer.

*Paint properties* are applied later in the rendering process. A layer
that shares layout properties with another layer can have independent
paint properties. Paint properties appear in the layer's ``"paint"``
object.

.. include:: footer.txt