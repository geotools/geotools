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


Paint Properties
^^^^^^^^^^^^^^^^

circle-radius
"""""""""""""

*Optional* :ref:`types-number`. *Units in* pixels. *Defaults to* 5.



Circle radius.


.. list-table::
   :widths: 34, 22, 22, 22
   :width: 100%
   :header-rows: 1

   * - circle-radius
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
   :widths: 34, 22, 22, 22
   :width: 100%
   :header-rows: 1

   * - circle-color
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
   :widths: 34, 22, 22, 22
   :width: 100%
   :header-rows: 1

   * - circle-blur
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
   :widths: 34, 22, 22, 22
   :width: 100%
   :header-rows: 1

   * - circle-opacity
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
   :widths: 34, 22, 22, 22
   :width: 100%
   :header-rows: 1

   * - circle-translate
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
   :widths: 34, 22, 22, 22
   :width: 100%
   :header-rows: 1

   * - circle-translate-anchor
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
   :widths: 34, 22, 22, 22
   :width: 100%
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
   :widths: 34, 22, 22, 22
   :width: 100%
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
   :widths: 34, 22, 22, 22
   :width: 100%
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
   :widths: 34, 22, 22, 22
   :width: 100%
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