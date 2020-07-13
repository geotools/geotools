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

.. _types-string:

String
~~~~~~

A string is basically just text. In Mapbox styles, you're going to put
it in quotes. Strings can be anything, though pay attention to the case
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