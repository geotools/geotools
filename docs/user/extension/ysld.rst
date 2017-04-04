YSLD
----

The **gt-ysld** module provides a YAML parser/encoder for GeoTools style objects:

* human readable alternative to machine readable XML
* compact representation
* flexible syntax using indentation, rather than order
* YAML parser provides variables for snippet reuse
* Full access to GeoTools rendering engine not limited by SLD specification

References:

* :geoserver:`YSLD <styling/ysld/index>` (GeoServer User Guide)
* :doc:`gt-api style layer descriptor </library/api/sld>` (interfaces)
* :doc:`gt-opengis symbology encoding</library/opengis/se>` (interfaces)
* :doc:`style </tutorial/map/style>` (tutorial)
* :doc:`gt-render style </library/render/style>` (code examples)

YSLD Syntax
^^^^^^^^^^^

Quick example (the FeatureTypeStyle, Rule and list of Symbolizers is auto-generated):

.. code-block:: yaml

    line:
      stroke-color: '#3333cc'

Rules example (the FeatureTypeStyle is auto-generated):

.. code-block:: yaml

    rules:
    - filter: ${type = 'road'}
      symbolizers:
      - line:
          stroke-color: '#000000'
          stroke-width: 2
    - else: true
      symbolizers:
      - line:
          stroke-color: '#666666'
          stroke-width: 1

Complete example with names, titles and comments:


.. code-block:: yaml

    # Created on 2016-03-4
    name: water
    title: Lakes and Rivers 
    abstract: Water style using transparency to allow for hill
              shade bathymetry effects.
    feature-styles:
    - rules:
      - name: hydro
        title: Hydro
        symbolizers:
        - polygon:
            stroke-color: '#333089'
            stroke-width: 1.25
            fill-color: '#333089'
            fill-opacity: 0.45

Yet another markup language (YAML) uses indentation to form a document structure of mappings and lists. The `specification <http://yaml.org/spec/1.2/spec.html>`__ supports comments, multiline text and definition reuse.


Expressions
'''''''''''

Expressions are specified as CQL/ECQL parse-able expression strings. See the :doc:`CQL documentation </library/cql/ecql>` 
and this :geoserver:`CQL tutorial <tutorials/cql/cql_tutorial>` for more information about the CQL syntax. 

Literal examples:

.. code-block:: yaml

    line:
      stroke-width: 10
      stroke-linecap: 'butt'

Note: Single quotes are needed for string literals to differentiate them from
attribute references. 

Attribute example:


.. code-block:: yaml

    text:
      label: ${STATE_NAME}

Function example:

.. code-block:: yaml

    point:
      rotation: ${sqrt([STATE_POP])}

Color examples:

.. code-block:: yaml

    polygon:
      stroke-color: '#ff00ff'
      fill-color: rgb(255,0,255)

Color literals can be specified either as a 6 digit hex string or a 3 argument 
rgb function call.

Filters
'''''''

Rule filters are specified as CQL/ECQL parse-able filters. A simple example:

.. code-block:: yaml

    rules:
    - filter: ${type = 'highway'}
      symbolizers:
      - line:
          stroke-width: 5

See the :doc:`CQL documentation </library/cql/ecql>` and this :geoserver:`CQL tutorial <tutorials/cql/cql_tutorial>` for more information about the CQL syntax. 

Tuples
''''''

Some attributes are specified as pairs. For example:

.. code-block:: yaml

    rules:
    - scale: [10000,20000]

.. code-block:: yaml

    point:
      anchor: [0.5,0.5]

The literal strings `min` and `max` can also be used:

.. code-block:: yaml

    rules:
    - scale: [min,10000]
      symbolizers:
      - line:
        stroke-width: 3
    - scale: [10000,max]
      symbolizers:
      - line:
        stroke-width: 1

Arrays
''''''

Lists and arrays are specified as space delimited. For example:

.. code-block:: yaml

    stroke-dasharray: '5 2 1 2'

Anchors & References
''''''''''''''''''''

With Yaml it is possible to reference other parts of a document. With this 
it is possible to support variables and mix ins. An example of a color variable:

.. code-block:: yaml

    redish: &redish '#DD0000'
    point:
      fill-color: *redish

An named "anchor" is declared with the `&` character and then referenced with 
the `*` character. This same feature can be used to do "mix-ins" as well:

.. code-block:: yaml

    define: &highway_zoom10
      scale: (10000,20000)
      filter: type = 'highway'

.. code-block:: yaml

    rules:
    - <<: *highway_zoom10
      symbolizers:
      - point

The syntax in this case is slightly different and is used when referencing an 
entire mapping object rather than just a simple scalar value. 

YSLD Grammar
^^^^^^^^^^^^

Document structure:

.. code-block:: yaml
    
    <YAML variable definition>
    <grid definition>
    <style definition>
    
Grid definition (predefined `WGS84`, `WebMercator`):

.. code-block:: yaml

    # grid definition
    grid:
      name: <text>

Style definition:

.. code-block:: yaml

    # style definition 
    name: <text>
    title: <text>
    abstract: <text>
    feature-styles:
    - <feature style>

Feature style definition:

.. code-block:: yaml

    feature-styles:
    - name: <text>
      title: <text>
      abstract: <text>
      transform:
        <transform>
      rules:
      - <rules>
      x-firstMatch: <boolean>
      x-composite: <text>
      x-composite-base: <boolean>

Rule definition:

.. code-block:: yaml

    # rules
    rules:
    - name: <text>
      title: <text>
      filter: <filter>
      else: <boolean>
      scale: [<min>,<max>]
      zoom: [<min>,<max>]
      symbolizers:
      - <symbolizers>

Line symbolizer definition:

.. code-block:: yaml

    symbolizers:
    - line:
        geometry: <expression>
        uom: <text>
        x-labelObstacle: <boolean>
        x-composite-base: <boolean>
        x-composite: <text>
        stroke-color: <color>
        stroke-width: <expression>
        stroke-opacity: <expression>
        stroke-linejoin: <expression>
        stroke-linecap: <expression>
        stroke-dasharray: <float list>
        stroke-dashoffset: <expression>
        stroke-graphic:
          <graphic_options>
        stroke-graphic-fill:
          <graphic_options>
        offset: <expression>

Polygon symbolizer definition:

.. code-block:: yaml

    symbolizers:
    - polygon:
        geometry: <expression>
        uom: <text>
        x-labelObstacle: <boolean>
        x-composite-base: <boolean>
        x-composite: <text>
        fill-color: <color>
        fill-opacity: <expression>
        fill-graphic:
          <graphic_options>
        stroke-color: <color>
        stroke-width: <expression>
        stroke-opacity: <expression>
        stroke-linejoin: <expression>
        stroke-linecap: <expression>
        stroke-dasharray: <float list>
        stroke-dashoffset: <expression>
        stroke-graphic:
          <graphic_options>
        stroke-graphic-fill:
          <graphic_options>
        offset: <expression>
        displacement: <expression>

Point symbolizer definition:

.. code-block:: yaml

    symbolizers:
    - point:
        geometry: <expression>
        uom: <text>
        x-labelObstacle: <boolean>
        x-composite-base: <boolean>
        x-composite: <text>
        symbols:
        - external:
            url: <text>
            format: <text>
        - mark:
            shape: <shape>
            fill-color: <color>
            fill-opacity: <expression>
            fill-graphic:
              <graphic_options>
            stroke-color: <color>
            stroke-width: <expression>
            stroke-opacity: <expression>
            stroke-linejoin: <expression>
            stroke-linecap: <expression>
            stroke-dasharray: <float list>
            stroke-dashoffset: <expression>
            stroke-graphic:
              <graphic_options>
            stroke-graphic-fill:
              <graphic_options>
        size: <expression>
        rotation: <expression>
        anchor: <tuple>
        displacement: <tuple>
        opacity: <expression>

Raster symbolizer definition:

.. code-block:: yaml

    symbolizers:
    - raster:
        opacity: <expression>
        channels:
          gray:
            <channel_options>
          red:
            <channel_options>
          green:
            <channel_options>
          blue:
            <channel_options>
        color-map:
          type: <ramp|interval|values>
          entries:
          - [color, entry_opacity, band_value, text_label]
        contrast-enhancement:
          mode: <normalize|histogram>
          gamma: <expression>
          
Text symbolizer definition:

.. code-block:: yaml

    symbolizers:
    - text:
        geometry: <expression>
        uom: <text>
        x-composite-base: <boolean>
        x-composite: <text>
        label: <expression>
        fill-color: <color>
        fill-opacity: <expression>
        fill-graphic:
          <graphic_options>
        stroke-graphic:
          <graphic_options>
        stroke-graphic-fill:
          <graphic_options>
        font-family: <expression>
        font-size: <expression>
        font-style: <expression>
        font-weight: <expression>
        placement: <point|line>
        offset: <expression>
        anchor: <tuple>
        displacement: <tuple>
        rotation: <expression>
        halo:
          radius: <expression>
          fill-color: <color>
          fill-opacity: <expression>
          fill-graphic:
            <graphic_options>
        graphic:
          symbols:
            <graphic_options>
          size: <expression>
          opacity: <expression>
          rotation: <expression>
        x-allowOverruns: <boolean>
        x-autoWrap: <expression>
        x-conflictResolution: <boolean>
        x-followLine: <boolean>
        x-forceLeftToRight: <boolean>
        x-goodnessOfFit: <expression>
        x-graphic-margin: <expression>
        x-graphic-resize: <none|proportional|stretch>
        x-group: <boolean>
        x-labelAllGroup: <boolean>
        x-labelPriority: <expression>
        x-repeat: <expression>
        x-maxAngleDelta: <expression>
        x-maxDisplacement: <expression>
        x-minGroupDistance: <expression>
        x-partials: <boolean>
        x-polygonAlign: <boolean>
        x-spaceAround: <expression>

Graphic options used above:

.. code-block:: yaml

    symbols:
    - mark:
        shape: <shape>
        <<: *fill
        <<: *stroke
    - external:
        url: <text>
        format: <text>
    anchor: <tuple>
    displacement: <tuple>
    opacity: <expression>
    rotation: <expression>
    size: <expression>
    options: <options>
    gap: <expression>
    initial-gap: <expression>

Fill used above:

.. code-block:: yaml

    fill: &fill
      fill-color: <color>
      fill-opacity: <expression>
      fill-graphic: 
        <<: *graphic

Stroke used above:

.. code-block:: yaml

    stroke: &stroke 
      stroke-color: <color>
      stroke-width: <expression>
      stroke-opacity: <expression>
      stroke-linejoin: <expression>
      stroke-linecap: <expression>
      stroke-dasharray: <float[]>
      stroke-dashoffset: <expression>
      stroke-graphic-fill: 
        <<: *graphic
      stroke-graphic-stroke: 
        <<: *graphic

Hints
'''''

Symbolizer hints are specified as normal mappings on a symbolizer object. Hints start with the prefix 'x-' and are limited to numeric, bool and text (no expressions).

If you are checking the GeoServer docs hints are called "vendor options":

* :geoserver:`user manual <styling/sld/reference/labeling>`
* `style workshop <https://github.com/boundlessgeo/workshops/tree/master/workshops/geoserver/style/source/style>`__
* `javadocs <http://docs.geotools.org/stable/javadocs/org/geotools/styling/TextSymbolizer.html>`__

Hints can be used with any symbolizer:

.. code-block:: yaml

    point:
      ...
      # No labels should overlap this feature, used to ensure point graphics are clearly visible
      # and not obscured by text
      x-labelObstacle: true

The majority of hints focus on controlling text:

.. code-block:: yaml

    text:
      # When false does not allow labels on lines to get beyond the beginning/end of the line. 
      # By default a partial overrun is tolerated, set to false to disallow it.
      x-allowOverruns: false
      
      # Number of pixels are which a long label should be split into multiple lines. Works on all
      # geometries, on lines it is mutually exclusive with the followLine option
      x-autoWrap: 400
      
      # Enables conflict resolution (default, true) meaning no two labels will be allowed to
      # overlap. Symbolizers with conflict resolution off are considered outside of the
      # conflict resolution game, they don't reserve area and can overlap with other labels.
      x-conflictResolution: true
      
      # When true activates curved labels on linear geometries. The label will follow the shape of 
      # the current line, as opposed to being drawn a tangent straight line
      x-followLine: true
      
      # When true forces labels to a readable orientation, when false they make follow the line
      # orientation even if that means the label will look upside down (useful when using
      # TTF symbol fonts to add direction markers along a line)
      x-forceLeftToRight: true
      
      # Sets the percentage of the label that must sit inside the geometry to allow drawing
      # the label. Works only on polygons.
      x-goodnessOfFit: 90
      
      # Pixels between the stretched graphic and the text, applies when graphic stretching is in use
      x-graphic-margin: 10
      
      # Stretches the graphic below a label to fit the label size. Possible values are 'stretch',
      # 'proportional'.
      x-graphic-resize: true

      # If true, geometries with the same labels are grouped and considered a single entity to be
      # abeled. This allows to avoid or control repeated labels
      x-group: false

      # When false,  only the biggest geometry in a group is labelled (the biggest is obtained by
      # merging, when possible, the original geometries). When true, also the smaller items in the
      # group are labeled. Works only on lines at the moment.
      x-labelAllGroup: false
      
      # When positive it's the desired distance between two subsequent labels on a "big" geometry.
      # Works only on lines at the moment. If zero only one label is drawn no matter how big the
      # geometry is
      x-repeat: 0

      # When drawing curved labels, max allowed angle between two subsequent characters. Higher
      # angles may cause disconnected words or overlapping characters
      x-maxAngleDelta: 90

      # The distance, in pixel, a label can be displaced from its natural position in an attempt to
      # find a position that does not conflict with already drawn labels.
      x-maxDisplacement: 400
      
      # Minimum distance between two labels in the same label group. To be used when both
      # displacement and repeat are used to avoid having two labels too close to each other
      x-minGroupDistance: 3

      # Option to truncate labels placed on the border of the displayArea (display partial labels).
      x-partials: true
      
      # Option overriding manual rotation to align label rotation automatically for polygons.
      x-polygonAlign: true
      
      # The minimum distance between two labels, in pixels
      x-spaceAround: 50
      
      # When true enables text kerning (adjustment of space between characters to get a more compact and readable layout)
      x-kerning: true

Additional hints for working with graphic fills:

.. code-block:: yaml

     # Used to specify top, right, bottom and left margins around the graphic used in the fill.
     # Allowed values:
     # top right bottom left (one explicit value per margin)
     x-graphic-margin: 5 10 5 10
     
     # top right-left bottom (three values, with right and left sharing the same value)
     x-graphic-margin: 5 10 5
     
     # top-bottom right-left (two values, top and bottom sharing the same value)
     x-graphic-margin: 5 10
     
     # top-right-bottom-left (single value for all four margins)
     x-graphic-margin: 5
     
     # Activates random distribution of symbol.
     # none disables random distribution
     x-random: none
     
     # free generates a completely random distribution
     x-random: free
     # grid will generate a regular grid of positions, and only randomizes the position of the symbol
     # around the cell centers, providing a more even distribution in space
     x-random: grid
     
     # Determines the size the the texture fill tile that will contain the randomly distributed symbols.
     # A smaller tile size will create a more regular pattern
     x-random-tile-size: 10
     
     # Activates random symbol rotation. Possible values are none (no rotation) or free.
     x-random-rotation: free
     
     # Determines the number of symbols in the tile. Increasing this number will generate a more dense distribution of symbols
     x-random-symbol-count: 5
     
     # The “seed” used to generate the random distribution. Changing this value will result in a
     # different symbol distribution.
     x-random-seed property: 42

Using YSLD
^^^^^^^^^^

YSLD relies on the :doc:`gt-api style layer descriptor </library/api/sld>`. Style files are parsed to a StyledLayerDescripter object, which can then be used by the GeoTools style renderer.

To read a YSLD style:

.. code-block:: java

    StyledLayerDescriptor style = Ysld.parse(ysld);

The value of `ysld` can be any of `java.io.Reader`, `java.io.InputStream`, `@link java.io.File` or `java.lang.String` containing a valid YSLD style.

To write a YSLD style, given a Styled Layer Descriptor:

.. code-block:: java

    Ysld.encode(sld, output);

The value of `output` can be any of `java.io.Reader`, `java.io.InputStream`, or `java.io.File`.

YSLD also provides a validator, which can be called using:

.. code-block:: java

    Ysld.validate(ysld);

This call accepts the everything that `Ysld.parse(ysld)` does, and returns a list of exceptions corresponding to syntax errors.