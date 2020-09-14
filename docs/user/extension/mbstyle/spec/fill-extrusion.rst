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
   :widths: 34, 22, 22, 22
   :width: 100%
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
   :widths: 34, 22, 22, 22
   :width: 100%
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
   :widths: 34, 22, 22, 22
   :width: 100%
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
   :widths: 34, 22, 22, 22
   :width: 100%
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
   :widths: 34, 22, 22, 22
   :width: 100%
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
   :widths: 34, 22, 22, 22
   :width: 100%
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
   :widths: 34, 22, 22, 22
   :width: 100%
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
   :widths: 34, 22, 22, 22
   :width: 100%
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
