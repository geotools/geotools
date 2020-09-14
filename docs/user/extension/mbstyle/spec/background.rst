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

.. ignore

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
     - >= 23.0
     - >= 2.4.0

Paint Properties
^^^^^^^^^^^^^^^^

background-color
""""""""""""""""

*Optional* :ref:`types-color`. *Defaults to* #000000. *Disabled by* background-pattern.

The color with which the background will be drawn.

.. list-table::
   :widths: 34, 22, 22, 22
   :width: 100%
   :header-rows: 1

   * - background-color
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.10.0
     - >= 23.0
     - >= 2.4.0

background-pattern
""""""""""""""""""

*Optional* :ref:`types-string`.

Name of image in sprite to use for drawing an image background. For
seamless patterns, image width and height must be a factor of two (2, 4,
8, ..., 512).
Note that zoom-dependent expressions will be evaluated only at integer zoom levels.

.. list-table::
   :widths: 34, 22, 22, 22
   :width: 100%
   :header-rows: 1

   * - background-pattern
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.10.0
     - >= 23.0
     - Not yet supported
   * - data-driven styling
     - Not yet supported
     - Not yet supported
     - Not yet supported

background-opacity
""""""""""""""""""

*Optional* :ref:`types-number`. *Defaults to* 1.

The opacity at which the background will be drawn.

.. list-table::
   :widths: 34, 22, 22, 22
   :width: 100%
   :header-rows: 1

   * - background-opacity
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.10.0
     - >= 23.0
     - >= 2.4.0

.. include:: footer.txt