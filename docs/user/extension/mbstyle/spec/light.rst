.. _light:

Light
-----

A style's ``light`` property provides global light source for that style.

.. code-block:: javascript

    "light": {
      "anchor": "viewport",
      "color": "white",
      "intensity": 0.4
    }

anchor
~~~~~~

*Optional* :ref:`types-enum`. *One of* map, viewport. *Defaults to* viewport.


Whether extruded geometries are lit relative to the map or viewport.


map
    The position of the light source is aligned to the rotation of the
    map.

viewport
    The position of the light source is aligned to the rotation of the
    viewport.

.. code-block:: javascript

    "anchor": "map"

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

color
~~~~~

*Optional* :ref:`types-color`. *Defaults to* ``#ffffff``

Color tint for lighting extruded geometries.


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

intensity
~~~~~~~~~

*Optional* :ref:`types-number`. *Defaults to* 0.5.

Intensity of lighting (on a scale from 0 to 1). Higher numbers will
present as more extreme contrast.

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
     
position
~~~~~~~~

*Optional* :ref:`types-array`. *Defaults to* 1.15,210,30.

Position of the light source relative to lit (extruded) geometries, in
[r radial coordinate, a azimuthal angle, p polar angle] where r
indicates the distance from the center of the base of an object to its
light, a indicates the position of the light relative to 0° (0° when
``light.anchor`` is set to ``viewport`` corresponds to the top of the
viewport, or 0° when ``light.anchor`` is set to ``map`` corresponds to
due north, and degrees proceed clockwise), and p indicates the height of
the light (from 0°, directly above, to 180°, directly below).

.. code-block:: javascript

    "position": [
      1.5,
      90,
      80
    ]

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
     
.. include:: footer.txt