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
   :widths: 34, 22, 22, 22
   :width: 100%
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
   :widths: 34, 22, 22, 22
   :width: 100%
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
   :widths: 34, 22, 22, 22
   :width: 100%
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
   :widths: 34, 22, 22, 22
   :width: 100%
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
   :widths: 34, 22, 22, 22
   :width: 100%
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
   :widths: 34, 22, 22, 22
   :width: 100%
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
   :widths: 34, 22, 22, 22
   :width: 100%
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
   :widths: 34, 22, 22, 22
   :width: 100%
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
   :widths: 34, 22, 22, 22
   :width: 100%
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
   :widths: 34, 22, 22, 22
   :width: 100%
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
   :widths: 34, 22, 22, 22
   :width: 100%
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
   :widths: 34, 22, 22, 22
   :width: 100%
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
   :widths: 34, 22, 22, 22
   :width: 100%
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
   :widths: 34, 22, 22, 22
   :width: 100%
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
   :widths: 34, 22, 22, 22
   :width: 100%
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
   :widths: 34, 22, 22, 22
   :width: 100%
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
   :widths: 34, 22, 22, 22
   :width: 100%
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
   :widths: 34, 22, 22, 22
   :width: 100%
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
   :widths: 34, 22, 22, 22
   :width: 100%
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
   :widths: 34, 22, 22, 22
   :width: 100%
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
   :widths: 34, 22, 22, 22
   :width: 100%
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
   :widths: 34, 22, 22, 22
   :width: 100%
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
   :widths: 34, 22, 22, 22
   :width: 100%
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
   :widths: 34, 22, 22, 22
   :width: 100%
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
   :widths: 34, 22, 22, 22
   :width: 100%
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
   :widths: 34, 22, 22, 22
   :width: 100%
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
   :widths: 34, 22, 22, 22
   :width: 100%
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
   :widths: 34, 22, 22, 22
   :width: 100%
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
   :widths: 34, 22, 22, 22
   :width: 100%
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
   :widths: 34, 22, 22, 22
   :width: 100%
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
   :widths: 34, 22, 22, 22
   :width: 100%
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
   :widths: 34, 22, 22, 22
   :width: 100%
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
   :widths: 34, 22, 22, 22
   :width: 100%
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
   :widths: 34, 22, 22, 22
   :width: 100%
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
   :widths: 34, 22, 22, 22
   :width: 100%
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
   :widths: 34, 22, 22, 22
   :width: 100%
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
   :widths: 34, 22, 22, 22
   :width: 100%
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
   :widths: 34, 22, 22, 22
   :width: 100%
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
   :widths: 34, 22, 22, 22
   :width: 100%
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
   :widths: 34, 22, 22, 22
   :width: 100%
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
   :widths: 34, 22, 22, 22
   :width: 100%
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
   :widths: 34, 22, 22, 22
   :width: 100%
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
   :widths: 34, 22, 22, 22
   :width: 100%
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
   :widths: 34, 22, 22, 22
   :width: 100%
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
   :widths: 34, 22, 22, 22
   :width: 100%
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
   :widths: 34, 22, 22, 22
   :width: 100%
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
   :widths: 34, 22, 22, 22
   :width: 100%
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
   :widths: 34, 22, 22, 22
   :width: 100%
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
   :widths: 34, 22, 22, 22
   :width: 100%
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
