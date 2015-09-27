Chart Plugin
------------

The **gt-chart** plugin supports the definition of a Chart as a Mark or ExternalGraphic. This is implemented as a
"dynamic symbolizer" using the JFreeChart and Eastwood projects.

References:

* http://www.jfree.org/eastwood/
* http://www.jfree.org/jfreechart/

**Maven**::
   
    <dependency>
      <groupId>org.geotools</groupId>
      <artifactId>gt-charts</artifactId>
      <version>${geotools.version}</version>
    </dependency>

Example
^^^^^^^

The following example is taken from test cases:

* :download:`streams.sld </../../modules/plugin/charts/src/test/resources/org/geotools/renderer/chart/test-data/pieCharts.sld>`
* :download:`cities.properties </../../modules/plugin/charts/src/test/resources/org/geotools/renderer/chart/test-data/cities.properties>`

Here is the example SLD:

.. literalinclude:: /../../modules/plugin/charts/src/test/resources/org/geotools/renderer/chart/test-data/pieCharts.sld
     :language: xml