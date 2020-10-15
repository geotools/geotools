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
   :widths: 34, 22, 22, 22
   :width: 100%
   :header-rows: 1

   * - line-cap
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
   :widths: 34, 22, 22, 22
   :width: 100%
   :header-rows: 1

   * - line-join
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
   :widths: 34, 22, 22, 22
   :width: 100%
   :header-rows: 1

   * - line-miter-limit
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
   :widths: 34, 22, 22, 22
   :width: 100%
   :header-rows: 1

   * - line-round-limit
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

   * - visibility
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
   :widths: 34, 22, 22, 22
   :width: 100%
   :header-rows: 1

   * - line-opacity
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
   :widths: 34, 22, 22, 22
   :width: 100%
   :header-rows: 1

   * - line-color
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
   :widths: 34, 22, 22, 22
   :width: 100%
   :header-rows: 1

   * - line-translate
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
   :widths: 34, 22, 22, 22
   :width: 100%
   :header-rows: 1

   * - line-translate-anchor
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
   :widths: 34, 22, 22, 22
   :width: 100%
   :header-rows: 1

   * - line-width
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
   :widths: 34, 22, 22, 22
   :width: 100%
   :header-rows: 1

   * - line-gap-width
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
   :widths: 34, 22, 22, 22
   :width: 100%
   :header-rows: 1

   * - line-offset
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
   :widths: 34, 22, 22, 22
   :width: 100%
   :header-rows: 1

   * - line-blur
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


line-dasharray
""""""""""""""

*Optional* :ref:`types-array`. *Units in* line widths. *Disabled by* line-pattern.

Specifies the lengths of the alternating dashes and gaps that form the
dash pattern. The lengths are later scaled by the line width. To convert
a dash length to pixels, multiply the length by the current line width.

.. list-table::
   :widths: 34, 22, 22, 22
   :width: 100%
   :header-rows: 1

   * - line-dasharray
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
   :widths: 34, 22, 22, 22
   :width: 100%
   :header-rows: 1

   * - line-pattern
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
