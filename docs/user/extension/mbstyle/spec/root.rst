Root Properties
---------------

Root level properties of a Mapbox style specify the map's layers, tile sources and other resources, and default values for the initial camera position when not specified elsewhere.

.. code-block:: javascript

    {
        "version": 8,
        "name": "Mapbox Streets",
        "sprite": "sprites/streets-v8",
        "glyphs": "{fontstack}/{range}.pbf",
        "sources": {...},
        "layers": [...]
    }


bearing
~~~~~~~

*Optional* :ref:`types-number`. *Units in degrees. Defaults to* 0.

Default bearing, in degrees clockwise from true north. The style bearing
will be used only if the map has not been positioned by other means
(e.g. map options or user interaction).

.. code-block:: javascript

    "bearing": 29

.. note:: *unsupported*

center
~~~~~~

*Optional* :ref:`types-array`.


Default map center in longitude and latitude. The style center will be used only if the map has not been positioned by other means (e.g. map options or user interaction).

.. code-block:: javascript

    "center": [
      -73.9749, 40.7736
    ]

.. note:: *unsupported*

glyphs
~~~~~~

*Optional* :ref:`types-string`.


A URL template for loading signed-distance-field glyph sets in PBF
format. The URL must include ``{fontstack}`` and ``{range}`` tokens.
This property is required if any layer uses the ``text-field`` layout
property.

.. code-block:: javascript

    "glyphs": "{fontstack}/{range}.pbf"


layers
~~~~~~

*Required* :ref:`types-array`.



Layers will be drawn in the order of this array.

.. code-block:: javascript

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

light
~~~~~

The global light source.

.. code-block:: javascript

    "light": {
      "anchor": "viewport",
      "color": "white",
      "intensity": 0.4
    }

metadata
~~~~~~~~

*Optional*

Arbitrary properties useful to track with the stylesheet, but do not influence rendering. Properties should be prefixed to avoid collisions.

.. note:: *unsupported.*

name
~~~~

*Optional* :ref:`types-string`.

A human-readable name for the style.

.. code-block:: javascript

    "name": "Bright"

pitch
~~~~~

*Optional* :ref:`types-number`. *Units in degrees. Defaults to* 0.

Default pitch, in degrees. Zero is perpendicular to the surface, for a
look straight down at the map, while a greater value like 60 looks ahead
towards the horizon. The style pitch will be used only if the map has
not been positioned by other means (e.g. map options or user
interaction).

.. code-block:: javascript

    "pitch": 50

sources
~~~~~~~

*Required* :ref:`sources`.


Data source specifications.

.. code-block:: javascript

    "sources": {
      "mapbox-streets": {
        "type": "vector",
        "url": "mapbox://mapbox.mapbox-streets-v6"
      }
    }

sprite
~~~~~~

*Optional* :ref:`types-string`.

A base URL for retrieving the sprite image and metadata. The extensions
``.png``, ``.json`` and scale factor ``@2x.png`` will be automatically
appended. This property is required if any layer uses the
``background-pattern``, ``fill-pattern``, ``line-pattern``,
``fill-extrusion-pattern``, or ``icon-image`` properties.

.. code-block:: javascript

    "sprite" : "/geoserver/styles/mark"

transition
~~~~~~~~~~

*Required* :ref:`transition`.



A global transition definition to use as a default across properties.

.. code-block:: javascript

    "transition": {
      "duration": 300,
      "delay": 0
    }

version
~~~~~~~

*Required* :ref:`types-enum`.

Style specification version number. Must be 8.

.. code-block:: javascript

    "version": 8

zoom
~~~~

*Optional* :ref:`types-number`.


Default zoom level. The style zoom will be used only if the map has not
been positioned by other means (e.g. map options or user interaction).

.. code-block:: javascript

    "zoom": 12.5

.. include:: footer.txt