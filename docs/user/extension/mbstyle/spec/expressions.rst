
.. _expressions:

Expressions
-----------

The value for any layout property, paint property, or filter may be specified as an expression. An expression defines a formula for computing the value of the property using the operators described below. The set of expression operators provided by Mapbox GL includes:

- Mathematical operators for performing arithmetic and other operations on numeric values
- Logical operators for manipulating boolean values and making conditional decisions
- String operators for manipulating strings
- Data operators, providing access to the properties of source features
- Camera operators, providing access to the parameters defining the current map view

Expressions are represented as JSON arrays. The first element of an expression array is a string naming the expression operator, e.g. ``"*"`` or ``"case"``. Subsequent elements (if any) are the arguments to the expression. Each argument is either a literal value (a string, number, boolean, or null), or another expression array.

    [expression_name, argument_0, argument_1, ...]

**Data expressions**

A *data expression* is any expression that access feature data -- that is, any expression that uses one of the data operators: ``get``, ``has``, ``id``, ``geometry-type``, ``properties``, or ``feature-state``. Data expressions allow a feature's properties or state to determine its appearance. They can be used to differentiate features within the same layer and to create data visualizations.

::

    {
        "circle-color": [
            "rgb",
            // red is higher when feature.properties.temperature is higher
            ["get", "temperature"],
            // green is always zero
            0,
            // blue is higher when feature.properties.temperature is lower
            ["-", 100, ["get", "temperature"]]
        ]
    }

This example uses the ``get`` operator to obtain the temperature value of each feature. That value is used to compute arguments to the ``rgb`` operator, defining a color in terms of its red, green, and blue components.

Data expressions are allowed as the value of the ``filter`` property, and as values for most paint and layout properties. However, some paint and layout properties do not yet support data expressions. The level of support is indicated by the "data-driven styling" row of the "SDK Support" table for each property. Data expressions with the ``feature-state`` operator are allowed only on paint properties.

**Camera expressions**

A *camera expression* is any expression that uses the ``zoom operator``. Such expressions allow the the appearance of a layer to change with the map's zoom level. Camera expressions can be used to create the appearance of depth and to control data density.

::

    {
        "circle-radius": [
            "interpolate", ["linear"], ["zoom"],
            // zoom is 5 (or less) -> circle radius will be 1px
            5, 1,
            // zoom is 10 (or greater) -> circle radius will be 5px
            10, 5
        ]
    }

This example uses the ``interpolate`` operator to define a linear relationship between zoom level and circle size using a set of input-output pairs. In this case, the expression indicates that the circle radius should be 1 pixel when the zoom level is 5 or below, and 5 pixels when the zoom is 10 or above. In between, the radius will be linearly interpolated between 1 and 5 pixels

Camera expressions are allowed anywhere an expression may be used. However, when a camera expression used as the value of a layout or paint property, it must be in one of the following forms::

    [ "interpolate", interpolation, ["zoom"], ... ]

Or::

    [ "step", ["zoom"], ... ]

Or::

    [
        "let",
        ... variable bindings...,
        [ "interpolate", interpolation, ["zoom"], ... ]
    ]

Or::

    [
        "let",
        ... variable bindings...,
        [ "step", ["zoom"], ... ]
    ]

That is, in layout or paint properties, ``["zoom"]`` may appear only as the input to an outer ``interpolate`` or ``step`` expression, or such an expression within a ``let`` expression.

There is an important difference between layout and paint properties in the timing of camera expression evaluation. Paint property camera expressions are re-evaluated whenever the zoom level changes, even fractionally. For example, a paint property camera expression will be re-evaluated continuously as the map moves between zoom levels 4.1 and 4.6. On the other hand, a layout property camera expression is evaluated only at integer zoom levels. It will not be re-evaluated as the zoom changes from 4.1 to 4.6 -- only if it goes above 5 or below 4.

**Composition**

A single expression may use a mix of data operators, camera operators, and other operators. Such composite expressions allows a layer's appearance to be determined by a combination of the zoom level and individual feature properties.

::

    {
        "circle-radius": [
            "interpolate", ["linear"], ["zoom"],
            // when zoom is 0, set each feature's circle radius to the value of its "rating" property
            0, ["get", "rating"],
            // when zoom is 10, set each feature's circle radius to four times the value of its "rating" property
            10, ["*", 4, ["get", "rating"]]
        ]
    }

An expression that uses both data and camera operators is considered both a data expression and a camera expression, and must adhere to the restrictions described above for both.

**Type system**

The input arguments to expressions, and their result values, use the same set of types as the rest of the style specification: boolean, string, number, color, and arrays of these types. Furthermore, expressions are type safe: each use of an expression has a known result type and required argument types, and the SDKs verify that the result type of an expression is appropriate for the context in which it is used. For example, the result type of an expression in the ``filter`` property must be boolean, and the arguments to the ``+`` operator must be numbers.

When working with feature data, the type of a feature property value is typically not known ahead of time by the SDK. In order to preserve type safety, when evaluating a data expression, the SDK will check that the property value is appropriate for the context. For example, if you use the expression ``["get", "feature-color"]`` for the ``circle-color`` property, the SDK will verify that the ``feature-color`` value of each feature is a string identifying a valid color. If this check fails, an error will be indicated in an SDK-specific way (typically a log message), and the default value for the property will be used instead.

In most cases, this verification will occur automatically wherever it is needed. However, in certain situations, the SDK may be unable to automatically determine the expected result type of a data expression from surrounding context. For example, it is not clear whether the expression ``["<", ["get", "a"], ["get", "b"]]`` is attempting to compare strings or numbers. In situations like this, you can use one of the type assertion expression operators to indicate the expected type of a data expression: ``["<", ["number", ["get", "a"]], ["number", ["get", "b"]]]``. A type assertion checks that the feature data actually matches the expected type of the data expression. If this check fails, it produces an error and causes the whole expression to fall back to the default value for the property being defined. The assertion operators are ``array``, ``boolean``, ``number``, and ``string``.

Expressions perform only one kind of implicit type conversion: a data expression used in a context where a color is expected will convert a string representation of a color to a color value. In all other cases, if you want to convert between types, you must use one of the type conversion expression operators: ``to-boolean``, ``to-number``, ``to-string``, or ``to-color``. For example, if you have a feature property that stores numeric values in string format, and you want to use those values as numbers rather than strings, you can use an expression such as ``["to-number", ["get", "property-name"]]``.

**Expression reference**

.. _expressions.types:

Types
~~~~~

The expressions in this section are provided for the purpose of testing for and converting between different data types like strings, numbers, and boolean values.

Often, such tests and conversions are unnecessary, but they may be necessary in some expressions where the type of a certain sub-expression is ambiguous. They can also be useful in cases where your feature data has inconsistent types; for example, you could use to-number to make sure that values like ``"1.5"`` (instead of ``1.5``) are treated as numeric values.

.. _expressions-array:

array
^^^^^

Asserts that the input is an array (optionally with a specific item type and length). If, when the input expression is evaluated, it is not of the asserted type, then this assertion will cause the whole expression to be aborted.

::

    ["array", value]: array

::

    ["array", type: "string" | "number" | "boolean", value]: array<type>

::

    ["array",
        type: "string" | "number" | "boolean",
        N: number (literal),
        value
    ]: array<type, N>


.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.41.0
     - >= 20.0
     - >= 3.0.0

.. _expressions-boolean:

boolean
^^^^^^^

Asserts that the input value is a boolean. If multiple values are provided, each one is evaluated in order until a boolean is obtained. If none of the inputs are boolean, the expression is an error.

::

    ["boolean", value]: boolean

::

    ["boolean", value, fallback: value, fallback: value, ...]: boolean

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.41.0
     - >= 20.0
     - >= 3.0.0

.. _expressions-collator:

collator
^^^^^^^^

Returns a ``collator`` for use in locale-dependent comparison operations. The ``case-sensitive`` and ``diacritic-sensitive`` options default to ``false``. The locale argument specifies the IETF language tag of the locale to use. If none is provided, the default locale is used. If the requested locale is not available, the ``collator`` will use a system-defined fallback locale. Use ``resolved-locale`` to test the results of locale fallback behavior.

::

    ["collator",
        { "case-sensitive": boolean, "diacritic-sensitive": boolean, "locale": string }
    ]: collator

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.45.0
     - >= Not yet supported
     - >= Not yet supported

.. _expressions-format:

format
^^^^^^

Returns ``formatted`` text containing annotations for use in mixed-format ``text-field`` entries. If set, the ``text-font`` argument overrides the font specified by the root layout properties. If set, the ``font-scale`` argument specifies a scaling factor relative to the ``text-size`` specified in the root layout properties.

::

    ["format",
        input_1: string, options_1: { "font-scale": number, "text-font": array<string> },
        ...,
        input_n: string, options_n: { "font-scale": number, "text-font": array<string> }
    ]: formatted

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.48.0
     - >= Not yet supported
     - >= Not yet supported

.. _expressions-literal:

literal
^^^^^^^

Provides a literal array or object value.

::

    ["literal", [...] (JSON array literal)]: array<T, N>

::

    ["literal", {...} (JSON object literal)]: Object

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.41.0
     - >= 20.0
     - >= 3.0.0

.. _expressions-number:

number
^^^^^^

Asserts that the input value is a number. If multiple values are provided, each one is evaluated in order until a number is obtained. If none of the inputs are numbers, the expression is an error.

::

    ["number", value]: number


::

    ["number", value, fallback: value, fallback: value, ...]: number

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.41.0
     - >= 20.0
     - >= 3.0.0

.. _expressions-object:

object
^^^^^^

Asserts that the input value is an object. If multiple values are provided, each one is evaluated in order until an object is obtained. If none of the inputs are objects, the expression is an error.

::

    ["object", value]: object

::

    ["object", value, fallback: value, fallback: value, ...]: object

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.41.0
     - >= 20.0
     - >= 3.0.0

.. _expressions-string:

string
^^^^^^

Asserts that the input value is a string. If multiple values are provided, each one is evaluated in order until a string is obtained. If none of the inputs are strings, the expression is an error.

::

    ["string", value]: string

::

    ["string", value, fallback: value, fallback: value, ...]: string

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.41.0
     - >= 20.0
     - >= 3.0.0

.. _expressions-to-boolean:

to-boolean
^^^^^^^^^^

Converts the input value to a boolean. The result is false when then input is an empty string, 0, ``false``, ``null``, or ``NaN``; otherwise it is ``true``.

::

    ["to-boolean", value]: boolean

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.41.0
     - >= 20.0
     - >= 3.0.0

.. _expressions-to-color:

to-color
^^^^^^^^

Converts the input value to a color. If multiple values are provided, each one is evaluated in order until the first successful conversion is obtained. If none of the inputs can be converted, the expression is an error.

::

    ["to-color", value, fallback: value, fallback: value, ...]: color

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.41.0
     - >= 20.0
     - >= 3.0.0

.. _expressions-to-number:

to-number
^^^^^^^^^

Converts the input value to a number, if possible. If the input is ``null`` or ``false``, the result is 0. If the input is ``true``, the result is 1. If the input is a string, it is converted to a number as specified by the "ToNumber Applied to the String Type" algorithm of the ECMAScript Language Specification. If multiple values are provided, each one is evaluated in order until the first successful conversion is obtained. If none of the inputs can be converted, the expression is an error.

::

    ["to-number", value, fallback: value, fallback: value, ...]: number

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.41.0
     - >= 20.0
     - >= 3.0.0

.. _expressions-to-string:

to-string
^^^^^^^^^

Converts the input value to a string. If the input is ``null``, the result is ``""``. If the input is a boolean, the result is ``"true"`` or ``"false"``. If the input is a number, it is converted to a string as specified by the "NumberToString" algorithm of the ECMAScript Language Specification. If the input is a color, it is converted to a string of the form ``"rgba(r,g,b,a)"``, where ``r``, ``g``, and ``b`` are numerals ranging from 0 to 255, and ``a`` ranges from 0 to 1. Otherwise, the input is converted to a string in the format specified by the ``JSON.stringify`` function of the ECMAScript Language Specification.

::

    ["to-string", value]: string

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.41.0
     - >= 20.0
     - >= 3.0.0

.. _expressions-typeof:

``typeof``
^^^^^^^^^^^

Returns a string describing the type of the given value.

::

    ["typeof", value]: string

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.41.0
     - >= 20.0
     - >= 3.0.0

.. _expressions.feature_data:

Feature data
~~~~~~~~~~~~

.. _expressions-feature-state:

feature-state
^^^^^^^^^^^^^

Retrieves a property value from the current feature's state. Returns null if the requested property is not present on the feature's state. A feature's state is not part of the GeoJSON or vector tile data, and must be set programmatically on each feature. Note that ``["feature-state"]`` can only be used with paint properties that support data-driven styling.

::

    ["feature-state", string]: value

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.46.0
     - >= Not yet supported
     - >= Not yet supported

.. _expressions-geometry-type:

geometry-type
^^^^^^^^^^^^^

Gets the feature's geometry type: Point, MultiPoint, LineString, MultiLineString, Polygon, MultiPolygon.

::

    ["geometry-type"]: string


.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.41.0
     - >= 20.0
     - >= 3.0.0

.. _expressions-id:

id
^^

Gets the feature's id, if it has one.

::

    ["id"]: value

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.41.0
     - >= 20.0
     - >= 3.0.0

.. _expressions-line-progress:

line-progress
^^^^^^^^^^^^^

Gets the progress along a gradient line. Can only be used in the line-gradient property.

::

    ["line-progress"]: number

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.45.0
     - >= Not yet supported
     - >= Not yet supported

.. _expressions-properties:

properties
^^^^^^^^^^

Gets the feature properties object. Note that in some cases, it may be more efficient to use ["get", "property_name"] directly.

::

    ["properties"]: object

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.41.0
     - >= Not yet supported
     - >= 3.0.0

.. _expressions.lookup:

Lookup
~~~~~~

.. _expressions-at:

at
^^

Retrieves an item from an array.

::

    ["at", number, array]: ItemType

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.41.0
     - >= 20.0
     - >= 3.0.0

.. _expressions-get:

get
^^^

Retrieves a property value from the current feature's properties, or from another object if a second argument is provided. Returns null if the requested property is missing.

::

    ["get", string]: value

::

    ["get", string, object]: value

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.41.0
     - >= 20.0
     - >= 3.0.0

.. _expressions-has:

has
^^^

Tests for the presence of an property value in the current feature's properties, or from another object if a second argument is provided.

::

    ["has", string]: boolean

::

    ["has", string, object]: boolean

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.41.0
     - >= 20.0
     - >= 3.0.0

.. _expressions-length:

length
^^^^^^

Gets the length of an array or string.

::

    ["length", string | array | value]: number

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.41.0
     - >= 20.0
     - >= 3.0.0

.. _expressions.decision:

Decision
~~~~~~~~

The expressions in this section can be used to add conditional logic to your styles. For example, the ``'case'`` expression provides basic "if/then/else" logic, and ``'match'`` allows you to map specific values of an input expression to different output expressions.

.. _expressions-!:

!
^

Logical negation. Returns true if the input is false, and false if the input is true.

::

    ["!", boolean]: boolean

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.41.0
     - >= 20.0
     - >= 3.0.0

.. _expressions-!=:

!=
^^

Returns ``true`` if the input values are not equal, ``false`` otherwise. The comparison is strictly typed: values of different runtime types are always considered unequal. Cases where the types are known to be different at parse time are considered invalid and will produce a parse error. Accepts an optional ``collator`` argument to control locale-dependent string comparisons.

::

    ["!=", value, value]: boolean

::

    ["!=", value, value, collator]: boolean

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.41.0
     - >= 20.0
     - >= 3.0.0
   * - collator
     - >= 0.45.0
     - >= Not yet supported
     - >= Not yet supported

.. _expressions-<:

<
^

Returns ``true`` if the first input is strictly less than the second, ``false`` otherwise. The arguments are required to be either both strings or both numbers; if during evaluation they are not, expression evaluation produces an error. Cases where this constraint is known not to hold at parse time are considered in valid and will produce a parse error. Accepts an optional ``collator`` argument to control locale-dependent string comparisons.

::

    ["<", value, value]: boolean

::

    ["<", value, value, collator]: boolean

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.41.0
     - >= 20.0
     - >= 3.0.0
   * - collator
     - >= 0.45.0
     - >= Not yet supported
     - >= Not yet supported

.. _expressions-<=:

<=
^^

Returns ``true`` if the first input is less than or equal to the second, ``false`` otherwise. The arguments are required to be either both strings or both numbers; if during evaluation they are not, expression evaluation produces an error. Cases where this constraint is known not to hold at parse time are considered in valid and will produce a parse error. Accepts an optional ``collator`` argument to control locale-dependent string comparisons.

::

    ["<=", value, value]: boolean

::

    ["<=", value, value, collator]: boolean

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.41.0
     - >= 20.0
     - >= 3.0.0
   * - collator
     - >= 0.45.0
     - >= Not yet supported
     - >= Not yet supported

.. _expressions-==:

==
^^

Returns ``true`` if the input values are equal, ``false`` otherwise. The comparison is strictly typed: values of different runtime types are always considered unequal. Cases where the types are known to be different at parse time are considered invalid and will produce a parse error. Accepts an optional ``collator`` argument to control locale-dependent string comparisons.

::

    ["==", value, value]: boolean

::

    ["==", value, value, collator]: boolean

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.41.0
     - >= 20.0
     - >= 3.0.0
   * - collator
     - >= 0.45.0
     - >= Not yet supported
     - >= Not yet supported

.. _expressions->:

>
^

Returns ``true`` if the first input is strictly greater than the second, ``false`` otherwise. The arguments are required to be either both strings or both numbers; if during evaluation they are not, expression evaluation produces an error. Cases where this constraint is known not to hold at parse time are considered in valid and will produce a parse error. Accepts an optional ``collator`` argument to control locale-dependent string comparisons.

::

    [">", value, value]: boolean

::

    [">", value, value, collator]: boolean

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.41.0
     - >= 20.0
     - >= 3.0.0
   * - collator
     - >= 0.45.0
     - >= Not yet supported
     - >= Not yet supported

.. _expressions->=:

>=
^^

Returns ``true`` if the first input is greater than or equal to the second, ``false`` otherwise. The arguments are required to be either both strings or both numbers; if during evaluation they are not, expression evaluation produces an error. Cases where this constraint is known not to hold at parse time are considered in valid and will produce a parse error. Accepts an optional ``collator`` argument to control locale-dependent string comparisons.

::

    [">=", value, value]: boolean

::

    [">=", value, value, collator]: boolean

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.41.0
     - >= 20.0
     - >= 3.0.0
   * - collator
     - >= 0.45.0
     - >= Not yet supported
     - >= Not yet supported

.. _expressions-all:

all
^^^

Returns ``true`` if all the inputs are true, ``false`` otherwise. The inputs are evaluated in order, and evaluation is short-circuiting: once an input expression evaluates to ``false``, the result is false and no further input expressions are evaluated.

::

    ["all", boolean, boolean]: boolean

::

    ["all", boolean, boolean, ...]: boolean

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.41.0
     - >= 20.0
     - >= 3.0.0

.. _expressions-any:

any
^^^

Returns ``true`` if any of the inputs are ``true``, ``false`` otherwise. The inputs are evaluated in order, and evaluation is short-circuiting: once an input expression evaluates to ``true``, the result is true and no further input expressions are evaluated.

::

    ["any", boolean, boolean]: boolean

::

    ["any", boolean, boolean, ...]: boolean

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.41.0
     - >= 20.0
     - >= 3.0.0

.. _expressions-case:

case
^^^^

Selects the first output whose corresponding test condition evaluates to true.

::

    ["case",
        condition: boolean, output: OutputType, condition: boolean, output: OutputType, ...,
        default: OutputType
    ]: OutputType

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.41.0
     - >= 20.0
     - >= 3.0.0

.. _expressions-coalesce:

coalesce
^^^^^^^^

Evaluates each expression in turn until the first non-null value is obtained, and returns that value.

::

    ["coalesce", OutputType, OutputType, ...]: OutputType

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.41.0
     - >= 20.0
     - >= 3.0.0

.. _expressions-match:

match
^^^^^

Selects the output whose label value matches the input value, or the fallback value if no match is found. The input can be any expression (e.g. ``["get", "building_type"]``). Each label must either be a single literal value or an array of literal values (e.g. ``"a"`` or ``["c", "b"]``), and those values must be all strings or all numbers. (The values ``"1"`` and ``1`` cannot both be labels in the same match expression.) Each label must be unique. If the input type does not match the type of the labels, the result will be the fallback value.

::

    ["match",
        input: InputType (number or string),
        label_1: InputType | [InputType, InputType, ...], output_1: OutputType,
        label_n: InputType | [InputType, InputType, ...], output_n: OutputType, ...,
        default: OutputType
    ]: OutputType

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.41.0
     - >= 20.0
     - >= 3.0.0

.. _expressions.ramps_scales_curves:

Ramps, scales, curves
~~~~~~~~~~~~~~~~~~~~~

.. _expressions-interpolate:

interpolate
^^^^^^^^^^^

Produces continuous, smooth results by interpolating between pairs of input and output values ("stops"). The ``input`` may be any numeric expression (e.g., ``["get", "population"]``). Stop inputs must be numeric literals in strictly ascending order. The output type must be ``number``, ``array<number>``, or ``color``.

Interpolation types:

- ``["linear"]``: interpolates linearly between the pair of stops just less than and just greater than the input.
- ``["exponential", base]``: interpolates exponentially between the stops just less than and just greater than the input. Base controls the rate at which the output increases: higher values make the output increase more towards the high end of the range. With values close to 1 the output increases linearly.
- ``["cubic-bezier", x1, y1, x2, y2]``: interpolates using the cubic Bezier curve defined by the given control points.

::

    ["interpolate",
        interpolation: ["linear"] | ["exponential", base] | ["cubic-bezier", x1, y1, x2, y2 ],
        input: number,
        stop_input_1: number, stop_output_1: OutputType,
        stop_input_n: number, stop_output_n: OutputType, ...
    ]: OutputType (number, array<number>, or Color)

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.42.0
     - >= Not yet supported
     - >= 3.0.0

.. _expressions-interpolate-hcl:

``interpolate-hcl``
^^^^^^^^^^^^^^^^^^^

Produces continuous, smooth results by interpolating between pairs of input and output values ("stops"). Works like ``interpolate``, but the output type must be ``color``, and the interpolation is performed in the "Hue-Chroma-Luminance" color space.

::

    ["interpolate-hcl",
        interpolation: ["linear"] | ["exponential", base] | ["cubic-bezier", x1, y1, x2, y2 ],
        input: number,
        stop_input_1: number, stop_output_1: Color,
        stop_input_n: number, stop_output_n: Color, ...
    ]: Color

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.49.0
     - >= Not yet supported
     - >= 3.0.0

.. _expressions-interpolate-lab:

interpolate-lab
^^^^^^^^^^^^^^^

Produces continuous, smooth results by interpolating between pairs of input and output values ("stops"). Works like ``interpolate``, but the output type must be ``color``, and the interpolation is performed in the CIELAB color space.

::

    ["interpolate-lab",
        interpolation: ["linear"] | ["exponential", base] | ["cubic-bezier", x1, y1, x2, y2 ],
        input: number,
        stop_input_1: number, stop_output_1: Color,
        stop_input_n: number, stop_output_n: Color, ...
    ]: Color

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.49.0
     - >= Not yet supported
     - >= 3.0.0

.. _expressions-step:

step
^^^^

Produces discrete, stepped results by evaluating a piece wise-constant function defined by pairs of input and output values ("stops"). The ``input`` may be any numeric expression (e.g.,  ``["get", "population"]``). Stop inputs must be numeric literals in strictly ascending order. Returns the output value of the stop just less than the input, or the first input if the input is less than the first stop.

::

    ["step",
        input: number,
        stop_output_0: OutputType,
        stop_input_1: number, stop_output_1: OutputType,
        stop_input_n: number, stop_output_n: OutputType, ...
    ]: OutputType

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.42.0
     - >= Not yet supported
     - >= 3.0.0

.. _expressions.variable_binding:

Variable binding
~~~~~~~~~~~~~~~~

.. _expressions-let:

let
^^^

Binds expressions to named variables, which can then be referenced in the result expression using ["var", "variable_name"].

::

    ["let",
        string (alphanumeric literal), any, string (alphanumeric literal), any, ...,
        OutputType
    ]: OutputType

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.41.0
     - >= Not yet supported
     - >= 3.0.0

.. _expressions-var:

var
^^^

References variable bound using "let".

::

    ["var", previously bound variable name]: the type of the bound expression

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.41.0
     - >= Not yet supported
     - >= 3.0.0

.. _expressions.string:

String
~~~~~~

.. _expressions-concat:

``concat``
^^^^^^^^^^

Returns a string consisting of the concatenation of the inputs. Each input is converted to a string as if by ``to-string``.

::

    ["concat", value, value, ...]: string

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.41.0
     - >= 20.0
     - >= 3.0.0

.. _expressions-downcase:

``downcase``
^^^^^^^^^^^^

Returns the input string converted to lowercase. Follows the Unicode Default Case Conversion algorithm and the locale-insensitive case mappings in the Unicode Character Database.

::

    ["downcase", string]: string

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.41.0
     - >= 20.0
     - >= Not yet supported

.. _expressions-is-supported-script:

is-supported-script
^^^^^^^^^^^^^^^^^^^

Returns ``true`` if the input string is expected to render legibly. Returns ``false`` if the input string contains sections that cannot be rendered without potential loss of meaning (e.g. Indic scripts that require complex text shaping, or right-to-left scripts if the ``mapbox-gl-rtl-text`` plugin is not in use in Mapbox GL JS).

::

    ["is-supported-script", string]: boolean

.. _expressions-resolved-locale:

resolved-locale
^^^^^^^^^^^^^^^

Returns the IETF language tag of the locale being used by the provided ``collator``. This can be used to determine the default system locale, or to determine if a requested locale was successfully loaded.

::

    ["resolved-locale", collator]: string

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.45.0
     - >= Not yet supported
     - >= Not yet supported

.. _expressions-upcase:

``upcase``
^^^^^^^^^^

Returns the input string converted to uppercase. Follows the Unicode Default Case Conversion algorithm and the locale-insensitive case mappings in the Unicode Character Database.

::

    ["upcase", string]: string

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.41.0
     - >= 20.0
     - >= Not yet supported

.. _expressions.color:

Color
~~~~~

.. _expressions-rgb:

``rgb``
^^^^^^^

Creates a color value from red, green, and blue components, which must range between 0 and 255, and an alpha component of 1. If any component is out of range, the expression is an error.

::

    ["rgb", number, number, number]: color

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.41.0
     - >= 20.0
     - >= 3.0.0

.. _expressions-rgba:

``rgba``
^^^^^^^^^

Creates a color value from red, green, blue components, which must range between 0 and 255, and an alpha component which must range between 0 and 1. If any component is out of range, the expression is an error.

::

    ["rgba", number, number, number, number]: color

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.41.0
     - >= Not yet supported
     - >= 3.0.0

.. _expressions-to-rgba:

``to-rgba``
^^^^^^^^^^^^^^

Returns a four-element array containing the input color's red, green, blue, and alpha components, in that order.

::

    ["to-rgba", color]: array<number, 4>

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.41.0
     - >= Not yet supported
     - >= 3.0.0

.. _expressions.math:

Math
~~~~

.. _expressions--:

\-
^^

For two inputs, returns the result of subtracting the second input from the first. For a single input, returns the result of subtracting it from 0.

::

    ["-", number, number]: number

::

    ["-", number]: number

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.41.0
     - >= 20.0
     - >= 3.0.0

.. _expressions-*:

\*
^^

Returns the product of the inputs.

::

    ["*", number, number, ...]: number

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.41.0
     - >= 20.0
     - >= 3.0.0

.. _expressions-/:

/
^

Returns the result of floating point division of the first input by the second.

::

    ["/", number, number]: number

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.41.0
     - >= 20.0
     - >= 3.0.0

.. _expressions-%:

%
^

Returns the remainder after integer division of the first input by the second.

::

    ["%", number, number]: number

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.41.0
     - >= 20.0
     - >= 3.0.0

.. _expressions-^:

^
^

Returns the result of raising the first input to the power specified by the second.

::

    ["^", number, number]: number

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.41.0
     - >= 20.0
     - >= 3.0.0

.. _expressions-+:

\+
^^

Returns the sum of the inputs.

::

    ["+", number, number, ...]: number

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.41.0
     - >= 20.0
     - >= 3.0.0

.. _expressions-abs:

abs
^^^

Returns the absolute value of the input.

::

    ["abs", number]: number

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.45.0
     - >= Not yet supported
     - >= 3.0.0

.. _expressions-acos:

``acos``
^^^^^^^^^

Returns the ``arccosine`` of the input.

::

    ["acos", number]: number

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.41.0
     - >= 20.0
     - >= 3.0.0

.. _expressions-asin:

``asin``
^^^^^^^^

Returns the ``arcsine`` of the input.

::

    ["asin", number]: number

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.41.0
     - >= 20.0
     - >= 3.0.0

.. _expressions-atan:

``atan``
^^^^^^^^^

Returns the ``arctangent`` of the input.

::

    ["atan", number]: number

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.41.0
     - >= 20.0
     - >= 3.0.0

.. _expressions-ceil:

``ceil``
^^^^^^^^^

Returns the smallest integer that is greater than or equal to the input.

::

    ["ceil", number]: number

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.45.0
     - >= Not yet supported
     - >= 3.0.0

.. _expressions-cos:

``cos``
^^^^^^^^

Returns the cosine of the input.

::

    ["cos", number]: number

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.41.0
     - >= 20.0
     - >= 3.0.0

.. _expressions-e:

e
^

Returns the mathematical constant e.

::

    ["e"]: number

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.41.0
     - >= 20.0
     - >= 3.0.0

.. _expressions-floor:

floor
^^^^^

Returns the largest integer that is less than or equal to the input.

::

    ["floor", number]: number

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.45.0
     - >= Not yet supported
     - >= 3.0.0

.. _expressions-ln:

``ln``
^^^^^^^^

Returns the natural logarithm of the input.

::

    ["ln", number]: number

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.41.0
     - >= 20.0
     - >= 3.0.0

.. _expressions-ln2:

``ln2``
^^^^^^^

Returns mathematical constant ``ln(2)``.

::

    ["ln2"]: number

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.41.0
     - >= 20.0
     - >= 3.0.0

.. _expressions-log10:

``log10``
^^^^^^^^^^

Returns the base-ten logarithm of the input.

::

    ["log10", number]: number

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.41.0
     - >= 20.0
     - >= 3.0.0

.. _expressions-log2:

``log2``
^^^^^^^^^

Returns the base-two logarithm of the input.

::

    ["log2", number]: number

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.41.0
     - >= 20.0
     - >= 3.0.0

.. _expressions-max:

max
^^^

Returns the maximum value of the inputs.


::

    ["max", number, number, ...]: number

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.41.0
     - >= 20.0
     - >= 3.0.0

.. _expressions-min:

min
^^^

Returns the minimum value of the inputs.

::

    ["min", number, number, ...]: number

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.41.0
     - >= 20.0
     - >= 3.0.0

.. _expressions-pi:

pi
^^

Returns the mathematical constant pi.

::

    ["pi"]: number

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.41.0
     - >= 20.0
     - >= 3.0.0

.. _expressions-:

round
^^^^^

Rounds the input to the nearest integer. Halfway values are rounded away from zero. For example, ``["round", -1.5]`` evaluates to -2.

::

    ["round", number]: number

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.45.0
     - >= Not yet supported
     - >= 3.0.0

.. _expressions-sin:

sin
^^^

Returns the sine of the input.

::

    ["sin", number]: number

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.41.0
     - >= 20.0
     - >= 3.0.0

.. _expressions-sqrt:

``sqrt``
^^^^^^^^^

Returns the square root of the input.

::

    ["sqrt", number]: number

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.42.0
     - >= 20.0
     - >= 3.0.0

.. _expressions-tan:

tan
^^^

Returns the tangent of the input.

::

    ["tan", number]: number

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.41.0
     - >= 20.0
     - >= 3.0.0

.. _expressions.zoom:

Zoom
~~~~

.. _expressions-zoom:

zoom
^^^^

Gets the current zoom level. Note that in style layout and paint properties, ["zoom"] may only appear as the input to a top-level "step" or "interpolate" expression.

::

    ["zoom"]: number

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.41.0
     - >= 20.0
     - >= 3.0.0

.. _expressions.heatmap:

Heatmap
~~~~~~~

.. _expressions-heatmap-density:

heatmap-density
^^^^^^^^^^^^^^^

Gets the kernel density estimation of a pixel in a heatmap layer, which is a relative measure of how many data points are crowded around a particular pixel. Can only be used in the ``heatmap-color`` property.

::

    ["heatmap-density"]: number

.. list-table::
   :widths: 19, 27, 27, 27
   :header-rows: 1

   * - Support
     - Mapbox
     - GeoTools
     - OpenLayers
   * - basic functionality
     - >= 0.41.0
     - >= Not yet supported
     - >= Not yet supported

.. include:: footer.txt