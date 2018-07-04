Mapbox Styles Module
--------------------

The **gt-mbstyle** module is an unsupported module that provides a parser/encoder to convert between Mapbox Styles and GeoTools style objects. These docs are under active development, along with the module itself.

.. toctree::
   :maxdepth: 1

   spec

References:

* https://www.mapbox.com/mapbox-gl-style-spec

Code Example
^^^^^^^^^^^^

Quick example of module usage (API change):

.. code-block:: java

    MBStyle style = MapBoxStyle.parse( reader );
    StyleLayerDescriptor sld = style.transform();

Internally the extension provides greater access to the parsed style:

.. code-block:: java

    MBStyle mbstyle = MapBoxStyleParser.parse( reader );

    // pull back all layers for a provided source
    List<MBLayer> layers = mbstyle.layers( "sf:roads" );

    // pull back selected layers for a provided source
    List<MBLayer> layers = mbstyle.layers( "sf:roads" );

    //Which can be used to Generate a List of FeatureTypeStyles:
    List<FeatureTypeStyle> fts = layer.transform( style );

MapBox Types
^^^^^^^^^^^^
  copied from the `MapBox Style Specification <https://www.mapbox.com/mapbox-gl-js/style-spec/>`_

  Color

    Colors are written as JSON strings in a variety of permitted formats: HTML-style hex values, rgb, rgba, hsl, and hsla. Predefined HTML colors names, like yellow and blue, are also permitted.

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

    Especially of note is the support for hsl, which can be easier to reason about than rgb().

  Enum

    One of a fixed list of string values. Use quotes around values.

    ::

      {
        "text-transform": "uppercase"
      }

  String

    A string is basically just text. In Mapbox styles, you're going to put it in quotes. Strings can be anything, though pay attention to the case of text-field - it actually will refer to features, which you refer to by putting them in curly braces, as seen in the example below.

    ::

      {
        "text-field": "{MY_FIELD}"
      }

  Boolean

    Boolean means yes or no, so it accepts the values true or false.

    ::

      {
        "fill-enabled": true
      }

  Number

    A number value, often an integer or floating point (decimal number). Written without quotes.

    ::

      {
        "text-size": 24
      }

  Array

    Arrays are comma-separated lists of one or more numbers in a specific order. For example, they're used in line dash arrays, in which the numbers specify intervals of line, break, and line again.

    ::

      {
        "line-dasharray": [2, 4]
      }

Function
^^^^^^^^
The value for any layout or paint property may be specified as a function. Functions allow you to make the appearance of a map feature change with the current zoom level and/or the feature's properties.

  stops

    *Required (except for identity functions) array.*

    Functions are defined in terms of input and output values. A set of one input value and one output value is known as a "stop."

  property

    *Optional string*

    If specified, the function will take the specified feature property as an input. See *Zoom Functions and Property Functions* for more information.

  base

    *Optional number. Default is 1.*

    The exponential base of the interpolation curve. It controls the rate at which the function output increases. Higher values make the output increase more towards the high end of the range. With values close to 1 the output increases linearly.

  type

    *Optional enum. One of identity, exponential, interval, categorical*

      identity

        functions return their input as their output.

      exponential

        functions generate an output by interpolating between stops just less than and just greater than the function input. The domain must be numeric.

      interval

        functions return the output value of the stop just less than the function input. The domain must be numeric.

      categorical

        functions return the output value of the stop equal to the function input.

      default

        A value to serve as a fallback function result when a value isn't otherwise available. It is used in the following circumstances:

          * In categorical functions, when the feature value does not match any of the stop domain values.
          * In property and zoom-and-property functions, when a feature does not contain a value for the specified property.
          * In identity functions, when the feature value is not valid for the style property (for example, if the function is being used for a circle-color property but the feature property value is not a string or not a valid color).
          * In interval or exponential property and zoom-and-property functions, when the feature value is not numeric.

      If no default is provided, the style property's default is used in these circumstances.

    colorSpace

      *Optional enum. One of rgb, lab, hcl*

        The color space in which colors interpolated. Interpolating colors in perceptual color spaces like LAB and HCL tend to produce color ramps that look more consistent and produce colors that can be differentiated more easily than those interpolated in RGB space.

      *rgb*

        Use the RGB color space to interpolate color values

      *lab*

        Use the LAB color space to interpolate color values.

      *hcl*

        Use the HCL color space to interpolate color values, interpolating the Hue, Chroma, and Luminance channels individually.

    **Zoom Functions** allow the appearance of a map feature to change with map’s zoom level. Zoom functions can be used to create the illusion of depth and control data density. Each stop is an array with two elements: the first is a zoom level and the second is a function output value.

    ::

      {
        "circle-radius": {
          "stops": [
            [5, 1],
            [10, 2]
          ]
        }
      }

    The rendered values of *color*, *number*, and *array* properties are intepolated between stops. *Enum*, *boolean*, and *string* property values cannot be intepolated, so their rendered values only change at the specified stops.

    There is an important difference between the way that zoom functions render for layout and paint properties. Paint properties are continuously re-evaluated whenever the zoom level changes, even fractionally. The rendered value of a paint property will change, for example, as the map moves between zoom levels 4.1 and 4.6. Layout properties, on the other hand, are evaluated only once for each integer zoom level. To continue the prior example: the rendering of a layout property will not change between zoom levels 4.1 and 4.6, no matter what stops are specified; but at zoom level 5, the function will be re-evaluated according to the function, and the property's rendered value will change. (You can include fractional zoom levels in a layout property zoom function, and it will affect the generated values; but, still, the rendering will only change at integer zoom levels.)

    **Property functions** allow the appearance of a map feature to change with its properties. Property functions can be used to visually differentate types of features within the same layer or create data visualizations. Each stop is an array with two elements, the first is a property input value and the second is a function output value. Note that support for property functions is not available across all properties and platforms at this time.

    ::

      {
        "circle-color": {
          "property": "temperature",
          "stops": [
            [0, "blue"],
            [100, "red"]
          ]
        }
      }

    **Zoom-and-property functions** allow the appearance of a map feature to change with both its properties and zoom. Each stop is an array with two elements, the first is an object with a property input value and a zoom, and the second is a function output value. Note that support for property functions is not yet complete.

    ::

      {
        "circle-radius": {
          "property": "rating",
          "stops": [
            [{"zoom": 0, "value": 0}, 0],
            [{"zoom": 0, "value": 5}, 5],
            [{"zoom": 20, "value": 0}, 0],
            [{"zoom": 20, "value": 5}, 20]
          ]
        }
      }

  Filter


  A filter selects specific features from a layer. A filter is an array of one of the following forms:

    **Existential Filters**

      ["has", *key*]   *feature[key]* exists

      ["!has", *key*] *feature[key]* does not exist

    **Comparison Filters**

      ["==", *key, value*] equality: *feature[key] = value*

      ["!=", *key, value*] inequality: *feature[key] ≠ value*

      [">", *key, value*] greater than: *feature[key] > value*

      [">=", *key, value*] greater than or equal: *feature[key] ≥ value*

      ["<", *key, value*] less than: *feature[key] < value*

      ["<=", *key, value*] less than or equal: *feature[key] ≤ value*

    **Set Membership Filters**

      ["in", *key, v0, ..., vn*] set inclusion: *feature[key] ∈ {v0, ..., vn}*

      ["!in", *key, v0, ..., vn*] set exclusion: *feature[key] ∉ {v0, ..., vn}*

    **Combining Filters**

      ["all", *f0, ..., fn*] logical AND: *f0 ∧ ... ∧ fn*

      ["any", *f0, ..., fn*] logical OR: *f0 ∨ ... ∨ fn*

      ["none", *f0, ..., fn*] logical NOR: *¬f0 ∧ ... ∧ ¬fn*

    A *key* must be a string that identifies a feature property, or one of the following special keys:

      * "$type": the feature type. This key may be used with the "==",  "!=", "in", and "!in" operators. Possible values are  "Point", "LineString", and "Polygon".

      * "$id": the feature identifier. This key may be used with the "==",  "!=", "has", "!has", "in", and "!in" operators.

    A *value* (and *v0, ..., vn* for set operators) must be a string, number, or boolean to compare the property value against.

    Set membership filters are a compact and efficient way to test whether a field matches any of multiple values.

    The comparison and set membership filters implement strictly-typed comparisons; for example, all of the following evaluate to false: 0 < "1", 2 == "2", "true" in [true, false].

    The "all", "any", and "none" filter operators are used to create compound filters. The values *f0, ..., fn* must be filter expressions themselves.

    ::

      ["==", "$type", "LineString"]

    This filter requires that the class property of each feature is equal to either "street_major", "street_minor", or "street_limited".

    ::

      ["in", "class", "street_major", "street_minor", "street_limited"]

    The combining filter "all" takes the three other filters that follow it and requires all of them to be true for a feature to be included: a feature must have a class equal to "street_limited", its admin_level must be greater than or equal to 3, and its type cannot be Polygon. You could change the combining filter to "any" to allow features matching any of those criteria to be included - features that are Polygons, but have a different class value, and so on.

    ::

      [
        "all",
        ["==", "class", "street_limited"],
        [">=", "admin_level", 3],
        ["!in", "$type", "Polygon"]
      ]

MapBox Style Grammar
^^^^^^^^^^^^^^^^^^^^
  *JSON does not allow for comments within the data therefore comments will be noted through the placement of the comment between open < and close > angle brackets. All properties are optional unless otherwise noted as Requried*

  Root Properties


  ::

    {
      "version": 8, <Required>
      "name": "Mapbox Streets",
      "sprite": "mapbox://sprites/mapbox/streets-v8",
      "glyphs": "mapbox://fonts/mapbox/{fontstack}/{range}.pbf",
      "sources": {...}, <Required>
      "layers": [...] <Required>
    }

  layers

  Layers are drawn in the order they appear in the layer array. Layers have two additional properties that determine how data is rendered: *layout* and *paint*

  Background Layer definition

  ::

    {
      "layers" : [
        {
          "id": "backgroundcolor",
          "type": "background",
          "source": "test-source",
          "source-layer": "test-source-layer",
          "layout": {
            "visibility": "visible"
          },
          "paint": {
            "background-opacity": 0.45,
            "background-color": "#00FF00"
          }
        }
      ]
    }

  *background-color* is disabled by the presence of *background-pattern*

  Fill Layer Definition

  ::

    {
      "layers": [
        {
          "id": "testid",
          "type": "fill",
          "source": "geoserver-states",
          "source-layer": "states",
          "layout": {
            "visibility": "visible"
          },
          "paint": {
            "fill-antialias": "true",
            "fill-opacity": 0.84,
            "fill-color": "#FF595E",
            "fill-outline-color":"#1982C4",
            "fill-translate": [20,20],
            "fill-translate-anchor": "map",
            "fill-pattern": <String>
          }
        }
      ]
    }

  Line Layer Definition


  ::

    {
      "layers": [
        {
          "id": "test-id",
          "type": "line",
          "source": "test-source",
          "source-layer": "test-source-layer",
          "layout": {
              "line-cap": "square",
              "line-join": "round",
              "line-mitre-limit": 2, <Optional - Requires line-join=mitre>
              "line-round-limit": 1.05, <Optional - Requires line-join=round>
              "visibility": "visible"
          },
          "paint": {
            "line-color": "#0099ff",
            "line-opacity": 0.5,
            "line-translate": [3,3],
            "line-translate-anchor": "viewport",
            "line-width": 10,
            "line-gap-width": 8,
            "line-offset": 4,
            "line-blur": 2,
            "line-dasharray": [50, 50],
            "line-pattern": <String>
          }
        }
      ],
    }

  Symbol Layer Definition


  ::

    {
      "layers": [
        {
          "id": "test-id",
          "type": "symbol",
          "source": "test-source",
          "source-layer": "test-source-layer",
          "layout": {
              "symbol-placement": "", <Enum, [Point, line] Defaults to Point>
              "symbol-spacing": "", <Number in pixels. Defaults to 250, requires symbol-placement = line>
              "symbol-avoid-edges": "", <Boolean defaults to true>
              "icon-allow-overlap": "", <Boolean defaults to false>
              "icon-ignore-placement": "", <Boolean defaults to false>
              "icon-optional": "", <Boolean defaults to false, requires icon-image and text-field>
              "icon-rotation-alignment": "", <Enum, [map, viewport, auto] defaults to auto requires icon-image>
              "icon-size": "", <Number, defaults to 1>
              "icon-rotation-alignment": "", <Enum, [none, width, height, both] defaults to none requires icon-image and text-field>
              "icon-text-fit-padding": "", <Array, units in pixels, defaults to [0,0,0,0]
                  requires icon-image, text-field and icon-text-fit of one of [both, width, height]>
              "icon-image": "", <String>
              "icon-rotate": "", <Number, in degrees, defaults to 0>
              "icon-padding": "", <Number, units in pixels, defaults to 2>
              "icon-keep-upright": "", <Boolean defaults to false, requires icon-image, icon-rotation-alignment = map
                  and symbol-placement = line>
              "icon-offset": "", <Array, defaults to [0,0] requires icon-image>
              "text-pitch-alignment": "", <Enum, [map, viewport, auto] defaults to auto requires text-field>
              "text-rotation-alignment": "", <Enum, [map, viewport, auto] defaults to auto requires text-field>
              "text-field": "", <String>
              "text-font": "", <Array, defaults to [Open Sans Regular,Arial Unicode MS Regular], requires text-field>
              "text-size": "", <Number, units in pixels, defaults to 16, requires text-field>
              "text-max-width": "", <Number, units in ems, defaults to 10 requires text-field>
              "text-line-height": "", <Number, units in ems, defaults to 1.2 requires text-field>
              "text-letter-spacing": "", <Number, units in ems, defaults to 0 requires text-field>
              "text-justify": "", <Enum, [left, center, right] defaults to center requires text-field>
              "text-anchor": "", <Enum, [center, left, right, top, bottom, top-left,
                 top-right, bottom-left, bottom-right] defaults to center>
              "text-max-angle": "", <Number units in degrees, defaults to 45>
              "text-rotate": "", <Number units in degrees, defaults to 0>
              "text-padding": "", <Number units in pixels, defaults to 2>
              "text-keep-upright": "", <Boolean, defaults to true, requires text-field, text-rotation-alignment = map,
                 and symbol-placement = line>
              "text-transform": "", <Enum [none, uppercase, lowercase] defaults to none, requires text-field>
              "text-offset": "", <Array, units in ems, defaults to [0,0], requires text-field>
              "text-allow-overlap": "", <Boolean, defaults to false, requires text-field>
              "text-ignore-placement": "", <Boolean, defaults to false, requires text-field>
              "text-optional": "", <Boolean, defaults to false, requires text-field and icon-image>
              "visibility": "visible"
          },
          "paint": {
            "icon-opacity": "", <Number, defaults to 1>
            "icon-color": "", <Color, defaults to #000000, requires icon-image>
            "icon-halo-color": "", <Color, defaults to rgba(0,0,0,0) requires icon-image>
            "icon-halo-width": "", <Number, units in pixels, defaults to 0 requires icon-image>
            "icon-halo-blur": "", <Number, units in pixels, defaults to 0 requires icon-image>
            "icon-translate": "", <Array, units in pixels, defaults to [0,0], requires icon-image>
            "icon-translate-anchor": "", <Enum, [map, viewport], defaults to map, requires icon-image, icon-translate>
            "text-opacity": "", <Number, defaults to 1 requires text-field>
            "text-halo-color": "", <Color, defaults to rgba(0,0,0,0) requires text-field>
            "text-halo-width": "", <Number, units in pixels, defaults to 0 requires text-field>
            "text-halo-blur": "", <Number, units in pixels, defaults to 0 requires text-field>
            "text-translate": "", <Array units in pixels defaults to [0,0] requires text-field>
            "text-translate-anchor": "" <Enum, [map, viewport] defaults to map, requires text-field, text-translate>
          }
        }
      ],
    }

  Raster Layer Definition

  ::

    {
      "layers": [
        {
          "id": "test-id",
          "type": "raster",
          "source": "test-source",
          "source-layer": "test-source-layer",
          "layout": {
              "visibility": "visible"
          },
          "paint": {
            "raster-opacity": "", <Number defaults to 1>
            "raster-hue-rotate": "", <Number units in degrees, defaults to 0>
            "raster-brightness-min": "", <Number, defaults to 0>
            "raster-brightness-max": "", <Number, defaults to 1>
            "raster-saturation": "", <Number, defaults to 0>
            "raster-contrast": "", <Number, defaults to 0>
            "raster-fade-duration": "" <Number, units in milliseconds, defaults to 300>
          }
        }
      ],
    }

  Circle Layer definition

  ::

    {
      "layers": [
        {
          "id": "test-id",
          "type": "raster",
          "source": "test-source",
          "source-layer": "test-source-layer",
          "layout": {
              "visibility": "visible"
          },
          "paint": {
            "circle-radius": "", <Number, units in pixels, defaults to 5>
            "circle-color": "", <Color, defaults to #000000>
            "circle-blur": "", <Number, defaults to 0>
            "circle-opacity": "", <Number, defaults to 1>
            "circle-translate": "", <Array, units in pixels, defaults to [0,0]>
            "circle-translate-anchor": "", <Enum, [map, viewport] defaults to map requires circle-translate>
            "circle-pitch-scale": "", <Enum, [map, viewport] defaults to map>
            "circle-stroke-width": "", <Number, units in pixels, defaults to 0>
            "circle-stroke-color": "", <Color, defaults to #000000>
            "circle-stroke-opacity": "", <Number, defaults to 1>
          }
        }
      ],
    }

  Fill-Extrusion Layer Definition

  ::

    {
      {
        "layers": [
          {
            "id": "test-id",
            "type": "fill-extrusion",
            "source": "test-source",
            "source-layer": "test-source-layer",
            "layout": {
                "visibility": "visible"
            },
            "paint": {
              "fill-extrusion-opacity": "", <Number, defaults to 1>
              "fill-extrusion-color": "", <Color, defaults to #000000, disabled by fill-extrusion-pattern>
              "fill-extrusion-translate": "", <Array, units in pixels, defaults to [0,0]>
              "fill-extrusion-translate-anchor": "", <Enum, [map, viewport] defaults to map requires fill-extrusion-translate>
              "fill-extrusion-pattern": "", <String>
              "fill-extrusion-height": "", <Number, units in meters, defaults to 0>
              "fill-extrusion-base": "" <Number, units in meters, defaults to 0, requires fill-extrusion-height>
            }
          }
        ],
    }
