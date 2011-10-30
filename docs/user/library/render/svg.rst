SVG Plugin
----------

The **gt-svg** plugin uses the Batik library to support the use of svg graphics.

**Maven**::
   
    <dependency>
      <groupId>org.geotools</groupId>
      <artifactId>gt-svg</artifactId>
      <version>${geotools.version}</version>
    </dependency>

Example
^^^^^^^

The following example is taken from test cases:

* :download:`house.svg </../../modules/plugin/svg/src/test/resources/org/geotools/renderer/lite/test-data/house.svg>`
* :download:`fillHouse.sld </../../modules/plugin/svg/src/test/resources/org/geotools/renderer/lite/test-data/fillHouse.sld>`

Here is the example fillHouse.sld:

.. literalinclude:: /../../modules/plugin/svg/src/test/resources/org/geotools/renderer/lite/test-data/fillHouse.sld
     :language: xml