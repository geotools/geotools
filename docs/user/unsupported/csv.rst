CSV Plugin
----------

The **gt-csv** module is a plugin which provides a CSVDataStore. It is pluggable into geoserver's importer, allowing read/write capabilities for :file:`.csv` files. The plugin supports three strategies: AttributesOnly, LatLon, and SpecifiedWKT. Essentially, each strategy will read and store data in a slightly different way.

The CSVWriteStrategyTest shows how the strategy code functions fairly well. A more detailed explanation can be found in the ContentDataStore tutorial on the :doc:`strategy page</tutorial/datastore/strategy>`.

:download:`CSVWriteStrategyTest.java </../../modules/unsupported/csv/src/test/java/org/geotools/data/csv/parse/CSVWriteStrategyTest.java>`

* CSVAttributesOnlyStrategy

	This strategy will directly read the :file:`.csv` file and use its headers as the attributes for each feature.

	.. literalinclude:: /../../modules/unsupported/csv/src/test/java/org/geotools/data/csv/parse/CSVWriteStrategyTest.java
		:language: java
		:lines: 41-62

* CSVLatLonStrategy

	The LatLonStrategy will attempt to parse the :file:`.csv` file for LAT and LON columns. If specified, it will use the String parameters passed to it as the names of the columns. Otherwise, it will attempt to find the columns using some standard spellings and abbrieviations for latitude and longitude. Internally, the data will be represented as a Point geometry.

	.. literalinclude:: /../../modules/unsupported/csv/src/test/java/org/geotools/data/csv/parse/CSVWriteStrategyTest.java
		:language: java
		:lines: 65-116

* CSVSpecifiedWKTStrategy

	Finally, the SpecifiedWKTStrategy requires a WKT which it will parse for. It is a bit more abstract than the LatLonStrategy, and will store the specified column as a Geometry attribute (it may be a Point, Line, Polygon, etc.).

	.. literalinclude:: /../../modules/unsupported/csv/src/test/java/org/geotools/data/csv/parse/CSVWriteStrategyTest.java
		:language: java
		:lines: 119-142