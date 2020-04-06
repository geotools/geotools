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

*background*
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

background
~~~~~~~~~~

Layout Properties
^^^^^^^^^^^^^^^^^

visibility
""""""""""

*Optional* :ref:`types-enum`. *One of* visible, none, *Defaults to* visible.


Whether this layer is displayed.


visible
    The layer is shown.

none
    The layer is not shown.

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

Paint Properties
^^^^^^^^^^^^^^^^

background-color
""""""""""""""""

*Optional* :ref:`types-color`. *Defaults to* #000000. *Disabled by* background-pattern.


The color with which the background will be drawn.


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

background-pattern
""""""""""""""""""

*Optional* :ref:`types-string`.



Name of image in sprite to use for drawing an image background. For
seamless patterns, image width and height must be a factor of two (2, 4,
8, ..., 512).


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

background-opacity
""""""""""""""""""

*Optional* :ref:`types-number`. *Defaults to* 1.

The opacity at which the background will be drawn.

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

fill
~~~~

Layout Properties
^^^^^^^^^^^^^^^^^

visibility
""""""""""


*Optional* :ref:`types-enum`. *One of* visible, none. *Defaults to* visible.

Whether this layer is displayed.

visible
    The layer is shown.

none
    The layer is not shown.

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.10.0
     - >= 17.1
     - >= 2.4.0

Paint Properties
^^^^^^^^^^^^^^^^

``fill-antialias``
"""""""""""""""""""

*Optional* :ref:`types-boolean`. *Defaults to* true.


Whether or not the fill should be anti-aliased.


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
   * - data-driven styling
     - Not yet supported
     - Not yet supported
     - Not yet supported

fill-opacity
""""""""""""

*Optional* :ref:`types-number`. *Defaults to* 1.


The opacity of the entire fill layer. In contrast to the ``fill-color``,
this value will also affect the 1 px stroke around the fill, if the
stroke is used.

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.10.0
     - >= 17.1
     - >= 2.4.0
   * - data-driven styling
     - >= 0.21.0
     - >= 17.1
     - >= 2.4.0

fill-color
""""""""""

*Optional* :ref:`types-color`. *Defaults to* #000000. *Disabled by* fill-pattern.


The color of the filled part of this layer. This color can be specified
as ``rgba`` with an alpha component and the color's opacity will not
affect the opacity of the 1px stroke, if it is used.


.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.10.0
     - >= 17.1
     - >= 2.4.0
   * - data-driven styling
     - >= 0.19.0
     - >= 17.1
     - >= 2.4.0

fill-outline-color
""""""""""""""""""

*Optional* :ref:`types-color`. *Disabled by* fill-pattern. *Requires* ``fill-antialias = true``.


The outline color of the fill. Matches the value of ``fill-color`` if
unspecified.

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.10.0
     - >= 17.1
     - >= 2.4.0
   * - data-driven styling
     - >= 0.19.0
     - >= 17.1
     - >= 2.4.0

fill-translate
""""""""""""""

*Optional* :ref:`types-array`. *Units in* pixels. *Defaults to* 0.0.


The geometry's offset. Values are [x, y] where negatives indicate left
and up, respectively.

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
   * - data-driven styling
     - Not yet supported
     - Not yet supported
     - Not yet supported

fill-translate-anchor
"""""""""""""""""""""

*Optional* :ref:`types-enum`. *One of* map, viewport. *Defaults to* map. *Requires* fill-translate.

Controls the translation reference point.

map
    The fill is translated relative to the map.

viewport
    The fill is translated relative to the viewport.

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
   * - data-driven styling
     - Not yet supported
     - Not yet supported
     - Not yet supported

fill-pattern
""""""""""""

*Optional* :ref:`types-string`.


Name of image in sprite to use for drawing image fills. For seamless
patterns, image width and height must be a factor of two (2, 4, 8, ...,
512).

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.10.0
     - >= 17.1
     - Not yet supported
   * - data-driven styling
     - Not yet supported
     - >= 17.1
     - Not yet supported

line
~~~~

Layout Properties
^^^^^^^^^^^^^^^^^

line-cap
""""""""

*Optional* :ref:`types-enum`. *One of* butt, round, square. *Defaults to* butt.

The display of line endings.


butt
    A cap with a squared-off end which is drawn to the exact endpoint of
    the line.

round
    A cap with a rounded end which is drawn beyond the endpoint of the
    line at a radius of one-half of the line's width and centered on the
    endpoint of the line.

square
    A cap with a squared-off end which is drawn beyond the endpoint of
    the line at a distance of one-half of the line's width.


.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.10.0
     - >= 17.1
     - >= 2.4.0
   * - data-driven styling
     - Not yet supported
     - >= 17.1
     - >= 2.4.0

line-join
"""""""""

*Optional* :ref:`types-enum`. *One of* bevel, round, miter. *Defaults to* miter.

The display of lines when joining.


bevel
    A join with a squared-off end which is drawn beyond the endpoint of
    the line at a distance of one-half of the line's width.

round
    A join with a rounded end which is drawn beyond the endpoint of the
    line at a radius of one-half of the line's width and centered on the
    endpoint of the line.

miter
    A join with a sharp, angled corner which is drawn with the outer
    sides beyond the endpoint of the path until they meet.


.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.10.0
     - >= 17.1
     - >= 2.4.0
   * - data-driven styling
     - Not yet supported
     - >= 17.1
     - >= 2.4.0

line-miter-limit
""""""""""""""""

*Optional* :ref:`types-number`. *Defaults to* 2. *Requires* line-join = miter.

Used to automatically convert miter joins to bevel joins for sharp
angles.


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
   * - data-driven styling
     - Not yet supported
     - Not yet supported
     - >= 2.4.0

line-round-limit
""""""""""""""""

*Optional* :ref:`types-number`. *Defaults to* 1.05. *Requires* line-join = round.


Used to automatically convert round joins to miter joins for shallow
angles.


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
   * - data-driven styling
     - Not yet supported
     - Not yet supported
     - Not yet supported


visibility
""""""""""

*Optional* :ref:`types-enum`. *One of* visible, none. *Defaults to* visible.

Whether this layer is displayed.


visible
    The layer is shown.

none
    The layer is not shown.


.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.10.0
     - >= 17.1
     - >= 2.4.0
   * - data-driven styling
     - Not yet supported
     - >= 17.1
     - >= 2.4.0


Paint Properties
^^^^^^^^^^^^^^^^

line-opacity
""""""""""""

*Optional* :ref:`types-number`. *Defaults to* 1.


The opacity at which the line will be drawn.


.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.10.0
     - >= 17.1
     - >= 2.4.0
   * - data-driven styling
     - >= 0.29.0
     - >= 17.1
     - >= 2.4.0


line-color
""""""""""

*Optional* :ref:`types-color`. *Defaults to* #000000. *Disabled by* line-pattern.

The color with which the line will be drawn.


.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.10.0
     - >= 17.1
     - >= 2.4.0
   * - data-driven styling
     - >= 0.23.0
     - >= 17.1
     - >= 2.4.0


line-translate
""""""""""""""

*Optional* :ref:`types-array`. *Units in* pixels. *Defaults to* 0.0.


The geometry's offset. Values are [x, y] where negatives indicate left
and up, respectively.


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
   * - data-driven styling
     - Not yet supported
     - Not yet supported
     - Not yet supported

line-translate-anchor
"""""""""""""""""""""

*Optional* :ref:`types-enum`. *One of* map, viewport. *Defaults to* map. *Requires* line-translate.

Controls the translation reference point.


map
    The line is translated relative to the map.

viewport
    The line is translated relative to the viewport.


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
   * - data-driven styling
     - Not yet supported
     - Not yet supported
     - Not yet supported


line-width
""""""""""

*Optional* :ref:`types-number`. *Units in* pixels. *Defaults to* 1.

Stroke thickness.


.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.10.0
     - >= 17.1
     - >= 2.4.0
   * - data-driven styling
     - Not yet supported
     - >= 17.1
     - >= 2.4.0

line-gap-width
""""""""""""""

*Optional* :ref:`types-number`. *Units in* pixels. *Defaults to* 0.



Draws a line casing outside of a line's actual path. Value indicates the
width of the inner gap.


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
     - 18.0
   * - data-driven styling
     - >= 0.29.0
     - 22.2
     - Not yet supported

line-offset
"""""""""""

*Optional* :ref:`types-number`. *Units in* pixels. *Defaults to* 0.


The line's offset. For linear features, a positive value offsets the
line to the right, relative to the direction of the line, and a negative
value to the left. For polygon features, a positive value results in an
inset, and a negative value results in an outset.

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.12.1
     - >= 17.1
     - Not yet supported
   * - data-driven styling
     - >= 0.29.0
     - >= 17.1
     - Not yet supported

line-blur
"""""""""

*Optional* :ref:`types-number`. *Units in* pixels. *Defaults to* 0.


Blur applied to the line, in pixels.

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
   * - data-driven styling
     - >= 0.29.0
     - Not yet supported
     - Not yet supported


``line-dasharray``
""""""""""""""""""

*Optional* :ref:`types-array`. *Units in* line widths. *Disabled by* line-pattern.

Specifies the lengths of the alternating dashes and gaps that form the
dash pattern. The lengths are later scaled by the line width. To convert
a dash length to pixels, multiply the length by the current line width.

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.10.0
     - >= 17.1
     - >= 2.4.0
   * - data-driven styling
     - Not yet supported
     - >= 17.1
     - >= 2.4.0

line-pattern
""""""""""""

*Optional* :ref:`types-string`.



Name of image in sprite to use for drawing image lines. For seamless
patterns, image width must be a factor of two (2, 4, 8, ..., 512).

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.10.0
     - >= 17.1
     - Not yet supported
   * - data-driven styling
     - Not yet supported
     - >= 17.1
     - Not yet supported

symbol
~~~~~~

Layout Properties
^^^^^^^^^^^^^^^^^

symbol-placement
""""""""""""""""


*Optional* :ref:`types-enum`. *One of* point, line. *Defaults to* point.

Label placement relative to its geometry.


point
    The label is placed at the point where the geometry is located.

line
    The label is placed along the line of the geometry. Can only be used
    on ``LineString`` and ``Polygon`` geometries.

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.10.0
     - >= 17.1
     - >= 2.10.0
   * - data-driven styling
     - Not yet supported
     - >= 17.1
     - >= 2.10.0


symbol-spacing
""""""""""""""

*Optional* :ref:`types-number`. *Units in* pixels. *Defaults to* 250. *Requires* symbol-placement = line.

Distance between two symbol anchors.

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.10.0
     - >= 17.1
     - Not yet supported
   * - data-driven styling
     - Not yet supported
     - Not yet supported
     - Not yet supported


symbol-avoid-edges
""""""""""""""""""

*Optional* :ref:`types-boolean`. *Defaults to* false.


If true, the symbols will not cross tile edges to avoid mutual
collisions. Recommended in layers that don't have enough padding in the
vector tile to prevent collisions, or if it is a point symbol layer
placed after a line symbol layer.

**In GeoTools the option is ignored and effectively always "on" when translating to SLD, to support server side rendering** 

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
   * - data-driven styling
     - Not yet supported
     - Not yet supported
     - Not yet supported


icon-allow-overlap
""""""""""""""""""

*Optional* :ref:`types-boolean`. *Defaults to* false. *Requires* icon-image.


If true, the icon will be visible even if it collides with other
previously drawn symbols.

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
   * - data-driven styling
     - Not yet supported
     - Not yet supported
     - Not yet supported

icon-ignore-placement
"""""""""""""""""""""

*Optional* :ref:`types-boolean`. *Defaults to* false. *Requires* icon-image.


If true, other symbols can be visible even if they collide with the
icon.

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
   * - data-driven styling
     - Not yet supported
     - Not yet supported
     - Not yet supported


icon-optional
"""""""""""""

*Optional* :ref:`types-boolean`. *Defaults to* false. *<Requires* icon-image, text-field.



If true, text will display without their corresponding icons when the
icon collides with other symbols and the text does not.

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
   * - data-driven styling
     - Not yet supported
     - Not yet supported
     - Not yet supported

icon-rotation-alignment
"""""""""""""""""""""""

*Optional* :ref:`types-enum`. *One of* map, viewport, auto. *Defaults to* auto. *Requires* icon-image.

In combination with ``symbol-placement``, determines the rotation
behavior of icons.


map
    When ``symbol-placement`` is set to ``point``, aligns icons
    east-west. When ``symbol-placement`` is set to ``line``, aligns icon
    x-axes with the line.

viewport
    Produces icons whose x-axes are aligned with the x-axis of the
    viewport, regardless of the value of ``symbol-placement``.

auto
    When ``symbol-placement`` is set to ``point``, this is equivalent to
    ``viewport``. When ``symbol-placement`` is set to ``line``, this is
    equivalent to ``map``.

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
   * - ``auto`` value
     - >= 0.25.0
     - Not yet supported
     - Not yet supported
   * - data-driven styling
     - Not yet supported
     - Not yet supported
     - Not yet supported

icon-size
"""""""""

*Optional* :ref:`types-number`. *Defaults to* 1. *Requires* icon-image.
Scale factor for icon. 1 is original size, 3 triples the size.


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
   * - data-driven styling
     - >= 0.35.0
     - Not yet supported
     - >= 2.4.0

icon-text-fit
"""""""""""""

*Optional* :ref:`types-enum`. *One of* none, width, height, both. *Defaults to* none. *Requires* icon-image, text-field.


Scales the icon to fit around the associated text.


none
    The icon is displayed at its intrinsic aspect ratio.

width
    The icon is scaled in the x-dimension to fit the width of the text.

height
    The icon is scaled in the y-dimension to fit the height of the text.

both
    The icon is scaled in both x- and y-dimensions.


.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.21.0
     - >= 17.1
     - Not yet supported
   * - data-driven styling
     - Not yet supported
     - Not yet supported
     - Not yet supported

icon-text-fit-padding
"""""""""""""""""""""

*Optional :ref:`types-array`. *Units in* pixels. *Defaults to* 0,0,0,0. *Requires* icon-image, text-field, icon-text-fit = one of both, width, height.

Size of the additional area added to dimensions determined by
``icon-text-fit``, in clockwise order: top, right, bottom, left.


.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.21.0
     - >= 17.1
     - Not yet supported
   * - data-driven styling
     - Not yet supported
     - Not yet supported
     - Not yet supported


icon-image
""""""""""

*Optional* :ref:`types-string`.



Name of image in sprite to use for drawing an image background. A string
with {tokens} replaced, referencing the data property to pull from.



.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.10.0
     - >= 17.1
     - >= 2.4.0
   * - data-driven styling
     - Not yet supported
     - >= 17.1
     - >= 2.4.0

icon-rotate
"""""""""""

*Optional* :ref:`types-number`. *Units in* degrees. *Defaults to* 0. *Requires* icon-image.

Rotates the icon clockwise.



.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.10.0
     - >= 17.1
     - >= 2.4.0
   * - data-driven styling
     - >= 0.21.0
     - >= 17.1
     - >= 2.4.0

icon-padding
""""""""""""

*Optional* :ref:`types-number`. *Units in* pixels. *Defaults to* 2. *Requires* icon-image.


Size of the additional area around the icon bounding box used for
detecting symbol collisions.


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
   * - data-driven styling
     - Not yet supported
     - Not yet supported
     - Not yet supported

icon-keep-upright
"""""""""""""""""

*Optional* :ref:`types-boolean`. *Defaults to* false. *Requires* icon-image, icon-rotation-alignment = map, symbol-placement = line.


If true, the icon may be flipped to prevent it from being rendered
upside-down.


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
   * - data-driven styling
     - Not yet supported
     - Not yet supported
     - Not yet supported

icon-offset
"""""""""""

*Optional* :ref:`types-array`. *Defaults to* 0,0. *Requires* icon-image.

Offset distance of icon from its anchor. Positive values indicate right
and down, while negative values indicate left and up. When combined with
``icon-rotate`` the offset will be as if the rotated direction was up.


.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.10.0
     - >= Not yet supported
     - Not yet supported
   * - data-driven styling
     - >= 0.29.0
     - >= Not yet supported
     - Not yet supported


text-pitch-alignment
""""""""""""""""""""

*Optional* :ref:`types-enum` *One of* map, viewport, auto. *Defaults to* auto. *Requires* text-field.

Orientation of text when map is pitched.


map
    The text is aligned to the plane of the map.

viewport
    The text is aligned to the plane of the viewport.

auto
    Automatically matches the value of ``text-rotation-alignment``.

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
   * - ``auto`` value
     - >= 0.25.0
     - Not yet supported
     - Not yet supported
   * - data-driven styling
     - Not yet supported
     - Not yet supported
     - Not yet supported


text-rotation-alignment
"""""""""""""""""""""""

*Optional* :ref:`types-enum`. *One of* map, viewport, auto. *Defaults to* auto. *Requires* text-field.

In combination with ``symbol-placement``, determines the rotation
behavior of the individual glyphs forming the text.


map
    When ``symbol-placement`` is set to ``point``, aligns text
    east-west. When ``symbol-placement`` is set to ``line``, aligns text
    x-axes with the line.

viewport
    Produces glyphs whose x-axes are aligned with the x-axis of the
    viewport, regardless of the value of ``symbol-placement``.

auto
    When ``symbol-placement`` is set to ``point``, this is equivalent to
    ``viewport``. When ``symbol-placement`` is set to ``line``, this is
    equivalent to ``map``.

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
   * - ``auto`` value
     - >= 0.25.0
     - Not yet supported
     -
   * - data-driven styling
     - Not yet supported
     - Not yet supported
     - Not yet supported

text-field
""""""""""

*Optional* :ref:`types-string`.



Value to use for a text label. Feature properties are specified using
tokens like {field\_name}. (Token replacement is only supported for
literal ``text-field`` values--not for property functions.)


.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.10.0
     - >= 17.1
     - >= 2.4.0
   * - data-driven styling
     - >= 0.33.0
     - >= 17.1
     - >= 2.4.0

text-font
"""""""""

*Optional* :ref:`types-array`. *Defaults to* Open Sans Regular, Arial Unicode MS Regular. *Requires* text-field.

Font stack to use for displaying text.

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.10.0
     - >= 17.1
     - >= 2.4.0
   * - data-driven styling
     - Not yet supported
     - Not yet supported
     - >= 2.4.0

text-size
"""""""""

*Optional* :ref:`types-number`. *Units in* pixels. *Defaults to* 16. *Requires* text-field.



Font size.



.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.10.0
     - >= 17.1
     - >= 2.4.0
   * - data-driven styling
     - >= 0.35.0
     - >= 17.1
     - >= 2.4.0

text-max-width
""""""""""""""

*Optional* :ref:`types-number`. *Units in* pixels. *Defaults to* 10. *Requires* text-field.



The maximum line width for text wrapping.


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
   * - data-driven styling
     - Not yet supported
     - Not yet supported
     - >= 2.4.0

text-line-height
""""""""""""""""

*Optional* :ref:`types-number`. *Units in* ems. *Defaults to* 1.2. *Requires* text-field.



Text leading value for multi-line text.


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
   * - data-driven styling
     - Not yet supported
     - Not yet supported
     - Not yet supported


text-letter-spacing
"""""""""""""""""""

*Optional* :ref:`types-number`. *Units in* ems. *Defaults to* 0. *Requires* text-field.



Text tracking amount.


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
   * - data-driven styling
     - Not yet supported
     - Not yet supported
     - Not yet supported

text-justify
""""""""""""

*Optional* :ref:`types-enum`. *One of* left, center, right. *Defaults to* center. *Requires* text-field.


Text justification options.


left
    The text is aligned to the left.

center
    The text is centered.

right
    The text is aligned to the right.


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
   * - data-driven styling
     - Not yet supported
     - Not yet supported
     - Not yet supported

text-anchor
"""""""""""

*Optional* :ref:`types-enum`. *One of* center, left, right, top, bottom, top-left, top-right, bottom-left, bottom-right.
*Defaults to* center. *Requires* text-field.



Part of the text placed closest to the anchor.


center
    The center of the text is placed closest to the anchor.

left
    The left side of the text is placed closest to the anchor.

right
    The right side of the text is placed closest to the anchor.

top
    The top of the text is placed closest to the anchor.

bottom
    The bottom of the text is placed closest to the anchor.

top-left
    The top left corner of the text is placed closest to the anchor.

top-right
    The top right corner of the text is placed closest to the anchor.

bottom-left
    The bottom left corner of the text is placed closest to the anchor.

bottom-right
    The bottom right corner of the text is placed closest to the anchor.


.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.10.0
     - 23.0
     - >= 2.4.0
   * - data-driven styling
     - >= 0.35.0
     - >= 22.2
     - >= 2.4.0


text-max-angle
""""""""""""""

*Optional* :ref:`types-number`. *Units in* degrees. *Defaults to* 45. *Requires* text-field, symbol-placement = line.


Maximum angle change between adjacent characters.


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
     - >= 2.10.0
   * - data-driven styling
     - Not yet supported
     - Not yet supported
     - >= 2.10.0

text-rotate
"""""""""""

*Optional* :ref:`types-number`. *Units in* degrees. *Defaults to* 0. *Requires* text-field.



Rotates the text clockwise.


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
     - >= 2.10.0
   * - data-driven styling
     - >= 0.35.0
     - Not yet supported
     - >= 2.10.0

text-padding
""""""""""""

*Optional* :ref:`types-number`. *Units in* pixels. *Defaults to* 2. *Requires* text-field.



Size of the additional area around the text bounding box used for
detecting symbol collisions.


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
   * - data-driven styling
     - Not yet supported
     - Not yet supported
     - Not yet supported


text-keep-upright
"""""""""""""""""

*Optional* :ref:`types-boolean`. *Defaults to* true. *Requires* text-field, text-rotation-alignment = true, symbol-placement = true.



If true, the text may be flipped vertically to prevent it from being
rendered upside-down.


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
   * - data-driven styling
     - Not yet supported
     - Not yet supported
     - Not yet supported

text-transform
""""""""""""""

*Optional* :ref:`types-enum`. *One of* none, uppercase, lowercase. *Defaults to* none. *Requires* text-field.

Specifies how to capitalize text, similar to the CSS ``text-transform``
property.


none
    The text is not altered.

uppercase
    Forces all letters to be displayed in uppercase.

lowercase
    Forces all letters to be displayed in lowercase.


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
   * - data-driven styling
     - >= 0.33.0
     - Not yet supported
     - >= 2.4.0

text-offset
"""""""""""

*Optional* :ref:`types-array`. *Units in* ems. *Defaults to* 0,0. *Requires* icon-image.

Offset distance of text from its anchor. Positive values indicate right
and down, while negative values indicate left and up.


.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.10.0
     - 22.2
     - >= 2.4.0
   * - data-driven styling
     - >= 0.35.0
     - 22.2
     - >= 2.4.0

text-allow-overlap
""""""""""""""""""

*Optional* :ref:`types-boolean`. *Defaults to* false. *Requires* text-field.



If true, the text will be visible even if it collides with other
previously drawn symbols.


.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.10.0
     - >= 17.1
     - Not yet supported
   * - data-driven styling
     - Not yet supported
     - Not yet supported
     - Not yet supported

text-ignore-placement
"""""""""""""""""""""

*Optional* :ref:`types-boolean`. *Defaults to* false. *Requires* text-field



If true, other symbols can be visible even if they collide with the
text.


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
   * - data-driven styling
     - Not yet supported
     - Not yet supported
     - Not yet supported

text-optional
"""""""""""""

*Optional* :ref:`types-boolean`. *Defaults to* false. *Requires* text-field, icon-image.



If true, icons will display without their corresponding text when the
text collides with other symbols and the icon does not.


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
   * - data-driven styling
     - Not yet supported
     - Not yet supported
     - Not yet supported


visibility
""""""""""

*Optional* :ref:`types-enum`. *One of* visible, none. *Defaults to* visible.



Whether this layer is displayed.


visible
    The layer is shown.

none
    The layer is not shown.


.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.10.0
     - >= 17.1
     - >= 2.4.0
   * - data-driven styling
     - Not yet supported
     - >= 17.1
     - >= 2.4.0

Paint Properties
^^^^^^^^^^^^^^^^

icon-opacity
""""""""""""

*Optional* :ref:`types-number`. *Defaults to* 1. <i>Requires </i>icon-image.


The opacity at which the icon will be drawn.


.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.10.0
     - >= 17.1
     - >= 2.4.0
   * - data-driven styling
     - >= 0.33.0
     - >= 17.1
     - >= 2.4.0


icon-color
""""""""""

*Optional* :ref:`types-color`. *Defaults to* #000000. *Requires* icon-image.



The color of the icon. This can only be used with SDF icons.


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
     - >= 2.10.0
   * - data-driven styling
     - >= 0.33.0
     - Not yet supported
     - >= 2.10.0

icon-halo-color
"""""""""""""""

*Optional* :ref:`types-color`. *Defaults to* ``rgba(0, 0, 0, 0)``. *Requires* icon-image.



The color of the icon's halo. Icon halos can only be used with SDF
icons.


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
   * - data-driven styling
     - >= 0.33.0
     - Not yet supported
     - Not yet supported

icon-halo-width
"""""""""""""""

*Optional* :ref:`types-number`. *Units in* pixels. *Defaults to* 0. *Requires* icon-image.



Distance of halo to the icon outline.


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
   * - data-driven styling
     - >= 0.33.0
     - Not yet supported
     - Not yet supported

icon-halo-blur
""""""""""""""

*Optional* :ref:`types-number`. *Units in* pixels. *Defaults to* 0. *Requires* icon-image.



Fade out the halo towards the outside.


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
   * - data-driven styling
     - >= 0.33.0
     - Not yet supported
     - Not yet supported

icon-translate
""""""""""""""

*Optional* :ref:`types-array`. *Units in* pixels. *Defaults to* 0,0. *Requires* icon-image.



Distance that the icon's anchor is moved from its original placement.
Positive values indicate right and down, while negative values indicate
left and up.



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
   * - data-driven styling
     - Not yet supported
     - Not yet supported
     - Not yet supported

icon-translate-anchor
"""""""""""""""""""""

*Optional* :ref:`types-enum` *One of* map, viewport. *Defaults to* map. *Requires* icon-image, icon-translate.



Controls the translation reference point.


map
    Icons are translated relative to the map.

viewport
    Icons are translated relative to the viewport.

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
   * - data-driven styling
     - Not yet supported
     - Not yet supported
     - Not yet supported


text-opacity
""""""""""""

*Optional* :ref:`types-number`. *Defaults to* 1. <i>Requires </i>text-field.


The opacity at which the text will be drawn.



.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.10.0
     - >= 17.1
     - Not yet supported
   * - data-driven styling
     - >= 0.33.0
     - >= 17.1
     - Not yet supported


text-color
""""""""""

*Optional* :ref:`types-color`. *Defaults to* #000000. *Requires* text-field.



The color with which the text will be drawn.



.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.10.0
     - >= 17.1
     - >= 2.4.0
   * - data-driven styling
     - >= 0.33.0
     - >= 17.1
     - >= 2.4.0


text-halo-color
"""""""""""""""

*Optional* :ref:`types-color`. *Defaults to* ``rgba(0, 0, 0, 0)``. *Requires* text-field.



The color of the text's halo, which helps it stand out from backgrounds.



.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.10.0
     - >= 17.1
     - >= 2.4.0
   * - data-driven styling
     - >= 0.33.0
     - >= 17.1
     - >= 2.4.0

text-halo-width
"""""""""""""""

*Optional* :ref:`types-number`. *Units in* pixels. *Defaults to* 0. *Requires* text-field.



Distance of halo to the font outline. Max text halo width is 1/4 of the
font-size.



.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.10.0
     - >= 17.1
     - >= 2.4.0
   * - data-driven styling
     - >= 0.33.0
     - >= 17.1
     - >= 2.4.0

text-halo-blur
""""""""""""""

*Optional* :ref:`types-number`. *Units in* pixels. *Defaults to* 0. *Requires* text-field.



The halo's fade out distance towards the outside.


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
   * - data-driven styling
     - >= 0.33.0
     - Not yet supported
     - Not yet supported

text-translate
""""""""""""""

*Optional* :ref:`types-array`. *Units in* pixels. *Defaults to* 0,0. *Requires* text-field.



Distance that the text's anchor is moved from its original placement.
Positive values indicate right and down, while negative values indicate
left and up.



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
   * - data-driven styling
     - Not yet supported
     - Not yet supported
     - Not yet supported

text-translate-anchor
"""""""""""""""""""""

*Optional* :ref:`types-enum` *One of* map, viewport. *Defaults to* map. *Requires* text-field, text-translate.



Controls the translation reference point.


map
    The text is translated relative to the map.

viewport
    The text is translated relative to the viewport.

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
   * - data-driven styling
     - Not yet supported
     - Not yet supported
     - Not yet supported


raster
~~~~~~

Layout Properties
^^^^^^^^^^^^^^^^^

visibility
""""""""""

*Optional* :ref:`types-enum`. *One of* visible, none. *Defaults to* visible.



Whether this layer is displayed.


visible
    The layer is shown.

none
    The layer is not shown.


.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.10.0
     - >= 17.1
     - Not yet supported
   * - data-driven styling
     - Not yet supported
     - >= 17.1
     - Not yet supported

Paint Properties
^^^^^^^^^^^^^^^^

raster-opacity
""""""""""""""

*Optional* :ref:`types-number`. *Defaults to* 1.


The opacity at which the image will be drawn.



.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.10.0
     - >= 17.1
     - Not yet supported
   * - data-driven styling
     - Not yet supported
     - >= 17.1
     - Not yet supported

`raster-hue-rotate <#paint-raster-hue-rotate>`__

*Optional* :ref:`types-number`. *Units in* degrees. *Defaults to* 0.



Rotates hues around the color wheel.



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
   * - data-driven styling
     - Not yet supported
     - Not yet supported
     - Not yet supported


raster-brightness-min
"""""""""""""""""""""

*Optional* :ref:`types-number`. *Defaults to* 0.


Increase or reduce the brightness of the image. The value is the minimum
brightness.

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
   * - data-driven styling
     - Not yet supported
     - Not yet supported
     - Not yet supported


raster-brightness-max
"""""""""""""""""""""

*Optional* :ref:`types-number`. *Defaults to* 1.


Increase or reduce the brightness of the image. The value is the maximum
brightness.

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
   * - data-driven styling
     - Not yet supported
     - Not yet supported
     - Not yet supported

raster-saturation
"""""""""""""""""

*Optional* :ref:`types-number`. *Defaults to* 0.


Increase or reduce the saturation of the image.

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
   * - data-driven styling
     - Not yet supported
     - Not yet supported
     - Not yet supported

raster-contrast
"""""""""""""""

*Optional* :ref:`types-number`. *Defaults to* 0.


Increase or reduce the contrast of the image.

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
   * - data-driven styling
     - Not yet supported
     - Not yet supported
     - Not yet supported


raster-fade-duration
""""""""""""""""""""

*Optional* :ref:`types-number` *Units in* milliseconds. *Defaults to* 300.



Fade duration when a new tile is added.


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
   * - data-driven styling
     - Not yet supported
     - Not yet supported
     - Not yet supported


circle
~~~~~~

Layout Properties
^^^^^^^^^^^^^^^^^

visibility
""""""""""

*Optional* :ref:`types-enum`. *One of* visible, none. *Defaults to* visible.



Whether this layer is displayed.


visible
    The layer is shown.

none
    The layer is not shown.

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.10.0
     - >= 17.1
     - >= 2.4.0


Paint Properties
^^^^^^^^^^^^^^^^

circle-radius
"""""""""""""

*Optional* :ref:`types-number`. *Units in* pixels. *Defaults to* 5.



Circle radius.


.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.10.0
     - >= 17.1
     - >= 2.4.0
   * - data-driven styling
     - >= 0.18.0
     - >= 17.1
     - >= 2.4.0

circle-color
""""""""""""

*Optional* :ref:`types-color`. *Defaults to* #000000.



The fill color of the circle.


.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.10.0
     - >= 17.1
     - >= 2.4.0
   * - data-driven styling
     - >= 0.18.0
     - >= 17.1
     - >= 2.4.0


circle-blur
"""""""""""

*Optional* :ref:`types-number`. *Defaults to* 0.


Amount to blur the circle. 1 blurs the circle such that only the
center point is full opacity.


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
   * - data-driven styling
     - >= 0.20.0
     - Not yet supported
     - Not yet supported


circle-opacity
""""""""""""""

*Optional* :ref:`types-number`. *Defaults to* 1.


The opacity at which the circle will be drawn.


.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.10.0
     - >= 17.1
     - >= 2.10.0
   * - data-driven styling
     - >= 0.20.0
     - >= 17.1
     - >= 2.10.0

circle-translate
""""""""""""""""

*Optional* :ref:`types-array`. *Units in* pixels. *Defaults to* 0,0.



The geometry's offset. Values are [x, y] where negatives indicate left
and up, respectively.


.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.10.0
     - >= 17.1
     - Not yet supported
   * - data-driven styling
     - Not yet supported
     - >= 17.1
     - Not yet supported


circle-translate-anchor
"""""""""""""""""""""""

*Optional* :ref:`types-enum` *One of* map, viewport. *Defaults to* map. *Requires* circle-translate.



Controls the translation reference point.


map
    The circle is translated relative to the map.

viewport
    The circle is translated relative to the viewport.

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
   * - data-driven styling
     - Not yet supported
     - Not yet supported
     - Not yet supported


circle-pitch-scale
""""""""""""""""""

*Optional* :ref:`types-enum` *One of* map, viewport. *Defaults to* map.



Controls the scaling behavior of the circle when the map is pitched.


map
    Circles are scaled according to their apparent distance to the
    camera.

viewport
    Circles are not scaled.

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.21.0
     - Not yet supported
     - Not yet supported
   * - data-driven styling
     - Not yet supported
     - Not yet supported
     - Not yet supported


circle-stroke-width
"""""""""""""""""""

*Optional* :ref:`types-number`. *Units in* pixels. *Defaults to* 5.



The width of the circle's stroke. Strokes are placed outside of the
``circle-radius``.


.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.29.0
     - >= 17.1
     - >= 2.10.0
   * - data-driven styling
     - >= 0.29.0
     - >= 17.1
     - >= 2.10.0

circle-stroke-color
"""""""""""""""""""

*Optional* :ref:`types-color`. *Defaults to* #000000.



The stroke color of the circle.


.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.29.0
     - >= 17.1
     - >= 2.4.0
   * - data-driven styling
     - >= 0.29.0
     - >= 17.1
     - >= 2.4.0

circle-stroke-opacity
"""""""""""""""""""""

*Optional* :ref:`types-number`. *Defaults to* 1.


The opacity of the circle's stroke.


.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.29.0
     - >= 17.1
     - Not yet supported
   * - data-driven styling
     - >= 0.29.0
     - >= 17.1
     - Not yet supported

fill-extrusion
~~~~~~~~~~~~~~

Layout Properties
^^^^^^^^^^^^^^^^^

visibility
""""""""""

*Optional* :ref:`types-enum`. *One of* visible, none. *Defaults to* visible.



Whether this layer is displayed.


visible
    The layer is shown.

none
    The layer is not shown.

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.27.0
     - >= 17.1
     - Not yet supported


Paint Properties
^^^^^^^^^^^^^^^^

fill-extrusion-opacity
""""""""""""""""""""""

*Optional* :ref:`types-number`. *Defaults to* 1.


The opacity of the entire fill extrusion layer. This is rendered on a
per-layer, not per-feature, basis, and data-driven styling is not
available.



.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.27.0
     - >= 17.1
     - Not yet supported

fill-extrusion-color
""""""""""""""""""""

*Optional* :ref:`types-color`. *Defaults to* #000000. *Disabled by* fill-extrusion-pattern.


The base color of the extruded fill. The extrusion's surfaces will be
shaded differently based on this color in combination with the root
``light`` settings. If this color is specified as ``rgba`` with an alpha
component, the alpha component will be ignored; use
``fill-extrusion-opacity`` to set layer opacity.


.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.27.0
     - >= 17.1
     - Not yet supported

fill-extrusion-translate
""""""""""""""""""""""""

*Optional* :ref:`types-array`. *Units in* pixels. *Defaults to* 0,0.



The geometry's offset. Values are [x, y] where negatives indicate left
and up (on the flat plane), respectively.


.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.27.0
     - Not yet supported
     - Not yet supported
   * - data-driven styling
     - Not yet supported
     - Not yet supported
     - Not yet supported


*Optional* :ref:`types-enum` *One of* map, viewport. *Defaults to* map. *Requires* fill-extrusion-translate.



Controls the translation reference point.


map
    The fill extrusion is translated relative to the map.

viewport
    The fill extrusion is translated relative to the viewport.

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.27.0
     - Not yet supported
     - Not yet supported
   * - data-driven styling
     - Not yet supported
     - Not yet supported
     - Not yet supported


fill-extrusion-pattern
""""""""""""""""""""""

*Optional* :ref:`types-string`.



Name of image in sprite to use for drawing images on extruded fills. For
seamless patterns, image width and height must be a factor of two (2, 4,
8, ..., 512).

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.27.0
     - Not yet supported
     - Not yet supported
   * - data-driven styling
     - Not yet supported
     - Not yet supported
     - Not yet supported

fill-extrusion-height
"""""""""""""""""""""

*Optional* :ref:`types-number` *Units in* meters. *Defaults to* 0.


The height with which to extrude this layer.


.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.27.0
     - Not yet supported
     - Not yet supported
   * - data-driven styling
     - >= 0.27.0
     - Not yet supported
     - Not yet supported


fill-extrusion-base
"""""""""""""""""""

*Optional* :ref:`types-number` *Units in* meters. *Defaults to* 0. *Requires* fill-extrusion-height.



The height with which to extrude the base of this layer. Must be less
than or equal to ``fill-extrusion-height``.

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.27.0
     - Not yet supported
     - Not yet supported
   * - data-driven styling
     - >= 0.27.0
     - Not yet supported
     - Not yet supported

.. include:: footer.txt