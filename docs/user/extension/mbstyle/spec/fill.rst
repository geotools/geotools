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

Paint Properties
^^^^^^^^^^^^^^^^

``fill-antialias``
"""""""""""""""""""

*Optional* :ref:`types-boolean`. *Defaults to* true.


Whether or not the fill should be anti-aliased.


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

fill-opacity
""""""""""""

*Optional* :ref:`types-number`. *Defaults to* 1.


The opacity of the entire fill layer. In contrast to the ``fill-color``,
this value will also affect the 1 px stroke around the fill, if the
stroke is used.

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

fill-color
""""""""""

*Optional* :ref:`types-color`. *Defaults to* #000000. *Disabled by* fill-pattern.


The color of the filled part of this layer. This color can be specified
as ``rgba`` with an alpha component and the color's opacity will not
affect the opacity of the 1px stroke, if it is used.


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
     - >= 0.19.0
     - >= 17.1
     - >= 2.4.0

fill-outline-color
""""""""""""""""""

*Optional* :ref:`types-color`. *Disabled by* fill-pattern. *Requires* ``fill-antialias = true``.


The outline color of the fill. Matches the value of ``fill-color`` if
unspecified.

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
     - >= 0.19.0
     - >= 17.1
     - >= 2.4.0

fill-translate
""""""""""""""

*Optional* :ref:`types-array`. *Units in* pixels. *Defaults to* 0.0.


The geometry's offset. Values are [x, y] where negatives indicate left
and up, respectively.

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

fill-translate-anchor
"""""""""""""""""""""

*Optional* :ref:`types-enum`. *One of* map, viewport. *Defaults to* map. *Requires* fill-translate.

Controls the translation reference point.

map
    The fill is translated relative to the map.

viewport
    The fill is translated relative to the viewport.

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

fill-pattern
""""""""""""

*Optional* :ref:`types-string`.


Name of image in sprite to use for drawing image fills. For seamless
patterns, image width and height must be a factor of two (2, 4, 8, ...,
512).

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
     - >= 17.1
     - Not yet supported
