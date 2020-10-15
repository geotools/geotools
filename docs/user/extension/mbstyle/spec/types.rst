.. _types:

Types
-----

A Mapbox style contains values of various types, most commonly as values
for the style properties of a layer.

.. _types-color:

Color
~~~~~

Colors are written as JSON strings in a variety of permitted formats:
HTML-style hex values, ``rgb``, ``rgba``, ``hsl``, and ``hsla``. Predefined HTML colors
names, like ``yellow`` and ``blue``, are also permitted.

::

    {
      "line-color": "#ff0",
      "line-color": "#ffff00",
      "line-color": "rgb(255, 255, 0)",
      "line-color": "rgba(255, 255, 0, 1)",
      "line-color": "hsl(100, 50%, 50%)",
      "line-color": "hsla(100, 50%, 50%, 1)",
      "line-color": "yellow"
    }

Especially of note is the support for ``hsl``, which can be `easier to
reason about than rgb() <http://mothereffinghsl.com/>`__.

.. _types-enum:

Enum
~~~~

One of a fixed list of string values. Use quotes around values.

::

    {
      "text-transform": "uppercase"
    }

.. _types-formatted:

Formatted
~~~~~~~~~

The formatted type is a string broken into sections annotated with separate formatting options.

::

    {
        "text-field": ["format",
            "foo", { "font-scale": 1.2 },
            "bar", { "font-scale": 0.8 }
        ]
    }

.. _types-resolveImage:

ResolveImage
~~~~~~~~~~~~

The resolvedImage type is an image (e.g., an icon or pattern) which is used in a layer.

An input to the image expression operator is checked against the current map style to see if it is available to be rendered or not, and the result is returned in the resolvedImage type. This approach allows developers to define a series of images which the map can fall back to if previous images are not found, which cannot be achieved by providing, for example, icon-image with a plain string (because multiple strings cannot be supplied to icon-image and multiple images cannot be defined in a string).

::

    {
        "icon-image": ["coalesce", ["image", "myImage"], ["image", "fallbackImage"]]
    }


.. _types-string:

String
~~~~~~

A string is basically just text. In Mapbox styles, strings are in quotes.

::

    {
        "source": "mySource"
    }

Strings can be anything, though pay attention to the case
of ``text-field`` - it actually will refer to features, which you refer
to by putting them in curly braces, as seen in the example below.

::

    {
      "text-field": "{MY_FIELD}"
    }


.. _types-boolean:

Boolean
~~~~~~~

Boolean means yes or no, so it accepts the values ``true`` or ``false``.

::

    {
      "fill-enabled": true
    }

.. _types-number:

Number
~~~~~~~

A number value, often an integer or floating point (decimal number).
Written without quotes.

::

    {
      "text-size": 24
    }

.. _types-array:

Array
~~~~~~

Arrays are comma-separated lists of one or more numbers in a specific
order. For example, they're used in line dash arrays, in which the
numbers specify intervals of line, break, and line again.

::

    {
      "line-dasharray": [2, 4]
    }

.. include:: footer.txt