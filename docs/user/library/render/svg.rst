SVG Plugin
----------

The **gt-svg** plugin uses the Batik library to support the use of SVG graphics.

**Maven**::
   
    <dependency>
      <groupId>org.geotools</groupId>
      <artifactId>gt-svg</artifactId>
      <version>${geotools.version}</version>
    </dependency>

External graphic example
^^^^^^^^^^^^^^^^^^^^^^^^

The following example, taken from test cases, uses SVG as an external graphic (thus, using it
as is, with the original colors):

* :download:`house.svg </../../modules/plugin/svg/src/test/resources/org/geotools/renderer/lite/test-data/house.svg>`
* :download:`fillHouse.sld </../../modules/plugin/svg/src/test/resources/org/geotools/renderer/lite/test-data/fillHouse.sld>`

Here is the example fillHouse.sld:

.. literalinclude:: /../../modules/plugin/svg/src/test/resources/org/geotools/renderer/lite/test-data/fillHouse.sld
     :language: xml

Mark example
^^^^^^^^^^^^

As an alternative, it's also possible to use a simple SVG as a Mark, thus picking only the shape
of the SVG but allowing to assign fill and stroke in SLD:

* :download:`house.svg </../../modules/plugin/svg/src/test/resources/org/geotools/renderer/lite/test-data/convenience.svg>`
* :download:`fillHouse.sld </../../modules/plugin/svg/src/test/resources/org/geotools/renderer/lite/test-data/convenience.sld>`

Here is the example convenience.sld:

.. literalinclude:: /../../modules/plugin/svg/src/test/resources/org/geotools/renderer/lite/test-data/convenience.sld
     :language: xml


Parameter extension
^^^^^^^^^^^^^^^^^^^

The plug-in can handle a basic support for parametric styling based on the `SVG Parameters 1.0 specification <https://www.w3.org/TR/SVGParamPrimer>`_ 
The support is limited to attribute values, as the underlying library (Batik) cannot perform parameter expansion on its own.

This is good enough to work again QGis own SVG library, here is a simple excerpt from the square symbol, notice the ``param(paramName)`` calls making the style parametric::

    <rect x="37.064" y="37.065" fill="param(fill)" fill-opacity="param(fill-opacity)" stroke="param(outline)" 
          stroke-opacity="param(outline-opacity)" stroke-width="param(outline-width)" width="505.871" height="505.871"/>

The parameter values can be specified as URL parameters, for example::

    <OnlineResource xlink:type="simple" xlink:href="square.svg?fill=xFF0000&fill-opacity=0.5" />
 
While any type of parameter can be handled, it's best to follow a naming convention, in particular:

  * Any parameter representing a width must contain "width" in its name
  * Any parameter representing a opacity must contain either "opacity" or "alpha" in its name
  * Any other parameter should be a color
  
This is because BATIK will log on the standard error any parsing issue, in particular, any non replaced parameter. The above conventions
give the parameter replacement engine an opportunity to set default values for missing parameters (in particular, 0 for width, 1 for opacity/alpha,
``x000000`` for colors).
