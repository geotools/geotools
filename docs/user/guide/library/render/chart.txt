Chart Plugin
------------

The **gt-chart** plugin supports the definition of a Chart as a Mark or ExternalGraphic. This is implemented as a
"dynamic symbolizer" using the JFreeChart and Eastwood projects.

References:

* http://www.jfree.org/eastwood/
* http://www.jfree.org/jfreechart/

Example
^^^^^^^

The following example is taken from test cases:

* :download:`streams.sld </../../modules/plugin/charts/src/test/resources/org/geotools/renderer/chart/test-data/pieCharts.sld>`
* :download:`cities.properties </../../modules/plugin/charts/src/test/resources/org/geotools/renderer/chart/test-data/cities.properties>`

Here is the example sld:

.. literalinclude:: /../../modules/plugin/charts/src/test/resources/org/geotools/renderer/chart/test-data/pieCharts.sld
     :language: xml