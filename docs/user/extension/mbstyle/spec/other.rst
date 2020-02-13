.. _other:

Other
-----

.. _other-function:

Function
~~~~~~~~

The value for any layout or paint property may be specified as a
*function*. Functions allow you to make the appearance of a map feature
change with the current zoom level and/or the feature's properties.


.. _stops:

stops
^^^^^

*Required (except for identity functions) :ref:`types-array`.*




Functions are defined in terms of input and output values. A set of one
input value and one output value is known as a "stop."

.. _property:

property
^^^^^^^^

*Optional* :ref:`types-string`.





If specified, the function will take the specified feature property as
an input. See `Zoom Functions and Property
Functions <#types-function-zoom-property>`__ for more information.

.. _base:

base
^^^^

*Optional* :ref:`types-number`. *Default is* 1.


The exponential base of the interpolation curve. It controls the rate at
which the function output increases. Higher values make the output
increase more towards the high end of the range. With values close to 1
the output increases linearly.

.. _type:

type
^^^^

*Optional* :ref:`types-enum`. *One of* identity, exponential, interval, categorical.

identity
    functions return their input as their output.
exponential
    functions generate an output by interpolating between stops just
    less than and just greater than the function input. The domain must
    be numeric. This is the default for properties marked with , the
    "exponential" symbol.
interval
    functions return the output value of the stop just less than the
    function input. The domain must be numeric. This is the default for
    properties marked with , the "interval" symbol.
categorical
    functions return the output value of the stop equal to the function
    input.

.. _function-default:

default
^^^^^^^

A value to serve as a fallback function result when a value isn't
otherwise available. It is used in the following circumstances:


-  In categorical functions, when the feature value does not match any
   of the stop domain values.
-  In property and zoom-and-property functions, when a feature does not
   contain a value for the specified property.
-  In identity functions, when the feature value is not valid for the
   style property (for example, if the function is being used for a
   circle-color property but the feature property value is not a string
   or not a valid color).
-  In interval or exponential property and zoom-and-property functions,
   when the feature value is not numeric.


If no default is provided, the style property's default is used in these
circumstances.

.. _function-colorspace:

``colorSpace``
^^^^^^^^^^^^^^^

*Optional* :ref:`types-enum`. *One of* ``rgb``, ``lab``, ``hcl``.

The color space in which colors interpolated. Interpolating colors in
perceptual color spaces like LAB and HCL tend to produce color ramps
that look more consistent and produce colors that can be differentiated
more easily than those interpolated in RGB space.


``rgb``
    Use the RGB color space to interpolate color values
``lab``
    Use the LAB color space to interpolate color values.
``hcl``
    Use the HCL color space to interpolate color values, interpolating
    the Hue, Chroma, and Luminance channels individually.


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
   * - ``property``
     - >= 0.18.0
     - >= 17.1
     - >= 2.4.0
   * - ``type``
     - >= 0.18.0
     - >= 17.1
     - >= 2.4.0
   * - ``exponential`` type
     - >= 0.18.0
     - >= 17.1
     - >= 2.4.0
   * - ``interval`` type
     - >= 0.18.0
     - >= 17.1
     - >= 2.4.0
   * - ``categorical`` type
     - >= 0.18.0
     - >= 17.1
     - >= 2.4.0
   * - ``identity`` type
     - >= 0.18.0
     - >= 17.1
     - >= 2.4.0
   * - ``default`` type
     - >= 0.18.0
     - >= 17.1
     - >= 2.4.0
   * - ``colorSpace`` type
     - >= 0.26.0
     - Not yet supported
     - >= 2.4.0


**Zoom functions** allow the appearance of a map feature to change with
map’s zoom level. Zoom functions can be used to create the illusion of
depth and control data density. Each stop is an array with two elements:
the first is a zoom level and the second is a function output value.

::

    {
      "circle-radius": {
        "stops": [

          // zoom is 5 -> circle radius will be 1px
          [5, 1],

          // zoom is 10 -> circle radius will be 2px
          [10, 2]

        ]
      }
    }

The rendered values of :ref:`types-color`,
:ref:`types-number`, and :ref:`types-array` properties are
interpolated between stops. :ref:`types-enum`,
:ref:`types-boolean`, and :ref:`types-string` property
values cannot be interpolated, so their rendered values only change at
the specified stops.

There is an important difference between the way that zoom functions
render for *layout* and *paint* properties. Paint properties are
continuously re-evaluated whenever the zoom level changes, even
fractionally. The rendered value of a paint property will change, for
example, as the map moves between zoom levels ``4.1`` and ``4.6``.
Layout properties, on the other hand, are evaluated only once for each
integer zoom level. To continue the prior example: the rendering of a
layout property will *not* change between zoom levels ``4.1`` and
``4.6``, no matter what stops are specified; but at zoom level ``5``,
the function will be re-evaluated according to the function, and the
property's rendered value will change. (You can include fractional zoom
levels in a layout property zoom function, and it will affect the
generated values; but, still, the rendering will only change at integer
zoom levels.)

**Property functions** allow the appearance of a map feature to change
with its properties. Property functions can be used to visually
differentiate types of features within the same layer or create data
visualizations. Each stop is an array with two elements, the first is a
property input value and the second is a function output value. Note
that support for property functions is not available across all
properties and platforms at this time.

::

    {
      "circle-color": {
        "property": "temperature",
        "stops": [

          // "temperature" is 0   -> circle color will be blue
          [0, 'blue'],

          // "temperature" is 100 -> circle color will be red
          [100, 'red']

        ]
      }
    }



**Zoom-and-property functions** allow the appearance of a map feature
to change with both its properties *and* zoom. Each stop is an array
with two elements, the first is an object with a property input value
and a zoom, and the second is a function output value. Note that support
for property functions is not yet complete.

::

    {
      "circle-radius": {
        "property": "rating",
        "stops": [

          // zoom is 0 and "rating" is 0 -> circle radius will be 0px
          [{zoom: 0, value: 0}, 0],

          // zoom is 0 and "rating" is 5 -> circle radius will be 5px
          [{zoom: 0, value: 5}, 5],

          // zoom is 20 and "rating" is 0 -> circle radius will be 0px
          [{zoom: 20, value: 0}, 0],

          // zoom is 20 and "rating" is 5 -> circle radius will be 20px
          [{zoom: 20, value: 5}, 20]

        ]
      }
    }

.. _other-filter:

Filter
~~~~~~

A filter selects specific features from a layer. A filter is an array of
one of the following forms:

Existential Filters
^^^^^^^^^^^^^^^^^^^

``["has", key]`` feature[key] exists


``["!has", key]`` feature[key] does not exist


Comparison Filters
^^^^^^^^^^^^^^^^^^

``["==", key, value]`` equality: feature[key] = value


``["!=", key, value]`` inequality: feature[key] ≠ value


``[">", key, value]`` greater than: feature[key] > value


``[">=", key, value]`` greater than or equal: feature[key] ≥ value


``["<", key, value]`` less than: feature[key] < value


``["<=", key, value]`` less than or equal: feature[key] ≤ value


Set Membership Filters
^^^^^^^^^^^^^^^^^^^^^^

``["in", key, v0, ..., vn]`` set inclusion: feature[key] ∈ {v_0, ..., v_n}


``["!in", key, v0, ..., vn]`` set exclusion: feature[key] ∉ {v_0, ...,
v_n}


Combining Filters
^^^^^^^^^^^^^^^^^

``["all", f0, ..., fn]`` logical ``AND``: f_0 ∧ ... ∧ f_n


``["any", f0, ..., fn]`` logical ``OR``: f_0 ∨ ... ∨ f_n


``["none", f0, ..., fn]`` logical ``NOR``: ¬f_0 ∧ ... ∧ ¬f_n



A key must be a string that identifies a feature property, or one of the
following special keys:

-  ``"$type"``: the feature type. This key may be used with the
   ``"=="``, ``"!="``, ``"in"``, and ``"!in"`` operators. Possible
   values are ``"Point"``, ``"LineString"``, and ``"Polygon"``.
-  ``"$id"``: the feature identifier. This key may be used with the
   ``"=="``, ``"!="``, ``"has"``, ``"!has"``, ``"in"``, and ``"!in"``
   operators.

A value (and v_0, ..., v_n for set operators) must be a
:ref:`types-string`, :ref:`types-number`, or :ref:`types-boolean` to
compare the property value against.

Set membership filters are a compact and efficient way to test whether a
field matches any of multiple values.

The comparison and set membership filters implement strictly-typed
comparisons; for example, all of the following evaluate to false:
``0 < "1"``, ``2 == "2"``, ``"true" in [true, false]``.

The ``"all"``, ``"any"``, and ``"none"`` filter operators are used to
create compound filters. The values f\_0, ..., f\_n must be filter
expressions themselves.

::

    ["==", "$type", "LineString"]


This filter requires that the ``class`` property of each feature is
equal to either "street\_major", "street\_minor", or "street\_limited".

::

    ["in", "class", "street_major", "street_minor", "street_limited"]


The combining filter "all" takes the three other filters that follow it
and requires all of them to be true for a feature to be included: a
feature must have a ``class`` equal to "street\_limited", its
``admin_level`` must be greater than or equal to 3, and its type cannot
be Polygon. You could change the combining filter to "any" to allow
features matching any of those criteria to be included - features that
are Polygons, but have a different ``class`` value, and so on.

::

    [
      "all",
      ["==", "class", "street_limited"],
      [">=", "admin_level", 3],
      ["!in", "$type", "Polygon"]
    ]

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
   * - ``has``/``!has``
     - >= 0.19.0
     - >= 17.1
     - >= 2.4.0

.. include:: footer.txt