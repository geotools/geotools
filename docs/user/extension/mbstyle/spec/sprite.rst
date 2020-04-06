.. _sprite:

Sprite
------

A style's ``sprite`` property supplies a URL template for loading small
images to use in rendering ``background-pattern``, ``fill-pattern``,
``line-pattern``, and ``icon-image`` style properties.

::

    "sprite" : "/geoserver/styles/mark"


A valid sprite source must supply two types of files:

-  An *index file*, which is a JSON document containing a description of
   each image contained in the sprite. The content of this file must be
   a JSON object whose keys form identifiers to be used as the values of
   the above style properties, and whose values are objects describing
   the dimensions (``width`` and ``height`` properties) and pixel ratio
   (``pixelRatio``) of the image and its location within the sprite
   (``x`` and ``y``). For example, a sprite containing a single image
   might have the following index file contents:

   ::

       {
         "poi": {
           "width": 32,
           "height": 32,
           "x": 0,
           "y": 0,
           "pixelRatio": 1
         }
       }

   Then the style could refer to this sprite image by creating a symbol
   layer with the layout property ``"icon-image": "poi"``, or with the
   tokenized value ``"icon-image": "{icon}"`` and vector tile features
   with a ``icon`` property with the value ``poi``.
-  *Image files*, which are PNG images containing the sprite data.

Mapbox SDKs will use the value of the ``sprite`` property in the style
to generate the URLs for loading both files. First, for both file types,
it will append ``@2x`` to the URL on high-DPI devices. Second, it will
append a file extension: ``.json`` for the index file, and ``.png`` for
the image file. For example, if you specified
``"sprite": "https://example.com/sprite"``, renderers would load
``https://example.com/sprite.json`` and
``https://example.com/sprite.png``, or
``https://example.com/sprite@2x.json`` and
``https://example.com/sprite@2x.png``.

If you are using Mapbox Studio, you will use prebuilt sprites provided
by Mapbox, or you can upload custom SVG images to build your own sprite.
In either case, the sprite will be built automatically and supplied by
Mapbox APIs. If you want to build a sprite by hand and self-host the
files, you can use
`spritezero-cli <https://github.com/mapbox/spritezero-cli>`__, a command
line utility that builds Mapbox GL compatible sprite PNGs and index
files from a directory of SVGs.

.. include:: footer.txt