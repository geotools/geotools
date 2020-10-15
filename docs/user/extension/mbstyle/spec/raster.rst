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
   :widths: 34, 22, 22, 22
   :width: 100%
   :header-rows: 1

   * - raster-opacity
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


raster-brightness-min
"""""""""""""""""""""

*Optional* :ref:`types-number`. *Defaults to* 0.


Increase or reduce the brightness of the image. The value is the minimum
brightness.

.. list-table::
   :widths: 34, 22, 22, 22
   :width: 100%
   :header-rows: 1

   * - raster-brightness-min
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
   :widths: 34, 22, 22, 22
   :width: 100%
   :header-rows: 1

   * - raster-brightness-max
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
   :widths: 34, 22, 22, 22
   :width: 100%
   :header-rows: 1

   * - raster-saturation
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
   :widths: 34, 22, 22, 22
   :width: 100%
   :header-rows: 1

   * - raster-contrast
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
   :widths: 34, 22, 22, 22
   :width: 100%
   :header-rows: 1

   * - raster-fade-duration
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

