:Author: Jody Garnett
:Thanks: geotools-devel list
:Version: |release|
:License: Creative Commons with attribution

Introducing CSVDataStore
------------------------

In our initial :doc:`feature tutorial </tutorial/feature/csv2shp>` we provided a code snippet to read in comma separated value file and produce feature collection.

In this tutorial we will build a CSV DataStore, and in the process explore several aspects of how DataStores work and best to make use of them.

If you would like to follow along with this workshop, start a new Java project in your favourite IDE, and ensure GeoTools is on your CLASSPATH (using maven or downloading the jars).

.. note:: Terminology

   DataStore borrows most of its concepts (and some of its syntax) from the Open Geospatial
   Consortium (OGC) Web Feature Server Specification:

   * Feature - atomic unit of geographic information
   * FeatureType - keeps track of what attributes each Feature can hold
   * FeatureId - a unique id associated with each Feature (must start with a non-numeric character)
   * FID - same as FeatureId
   * Schema - same as FeatureType (familiar to database developers)

Here is the sample :download:`locations.csv <artifacts/locations.csv>` file:

.. literalinclude:: artifacts/locations.csv

The first line of our CSV file is a header that provides the column names:

.. literalinclude:: artifacts/locations.csv
      :lines: 1

Each column name is treated as a simple String. More complicated formats have the option of isolating names into different name spaces.

Each subsequent line is used to capture a single feature of information suitable for mapping.

.. literalinclude:: artifacts/locations.csv
      :lines: 2

In our example the LAT and LON information represents a `POINT(46.066667, 11.116667)`, the CITY  `Trento` and the NUMBER `140` and YEAR `2002` capture details of the *GRASS users conference* (and one of the earliest *Free and Open Source Software for Geomatics (FOSS4G)* events).

Approach to Parsing CSV
^^^^^^^^^^^^^^^^^^^^^^^

Here is our strategy for representing GeoTools concepts with a CSV file.

* FeatureID or FID - uniquely defines a Feature.

  We will use the row number in our CSV file.

* FeatureType Name

  Same as the name of the :file:`.csv` file (i.e. "locations" for :file:`locations.csv`.)

* DataStore

  We will create a **CSVDataStore** to access all the FeatureTypes (.csv files) in a directory

* FeatureType or Schema

  We will represent the names of the columns in our CSV (and if possible their types).

* Geometry

  Initially we will try to recognise several columns and map them into Point x and y ordinates. This technique is used to handle content from websites such as **geonames**.

  We can also look at parsing a column using the Well-Known-Text representation of a Geometry.

# CoordinateReferenceSystem

  Look for a :file:`prj` sidecar file (ie :file:`locations.prj` for :file:`locations.csv` .)

JavaCSV Reader
^^^^^^^^^^^^^^

Rather than go through the joy of parsing a CSV file by hand, we are going to make use of a library to read CSV files.

The **JavaCSV** project looks nice and simple and is available in maven:

* http://opencsv.sourceforge.net (Apache 2.0)

For our purposes a key benefit of this implementation is streaming - it will read one line at a time and avoid loading the entire file into memory.

References:

* `Java CSV Code Samples <http://www.csvreader.com/java_csv_samples.php>`_
* `Comparison of Java CSV libraries <https://github.com/robert-bor/CSVeed/wiki/Comparison-of-Java-CSV-libraries>`_ (Robert Bor)

Time to create a new project making use of this library:

#. Create a new project:

   * Using Eclipse: :menuselection:`New --> Project` to create a `Maven Project` with group `org.geotools.tutorial` and name `csv`.
   * Using Maven: ``mvn archetype:generate -DgroupId=org.geotools.tutorial -DartifactId=csv -Dversion=1.0-SNAPSHOT -DarchetypeGroupId=org.apache.maven.archetypes -DarchetypeArtifactId=maven-archetype-quickstart``

#. Fill in project details, paying careful attention to the *gt.version* property you wish to use. You can choose a stable release (recommended) or use |branch|-SNAPSHOT for access to the latest nightly build.

   .. literalinclude:: artifacts/pom.xml
      :language: xml
      :end-before: <dependencies>
      :append: </project>

#. Add the following dependencies:

   .. literalinclude:: artifacts/pom.xml
      :language: xml
      :start-after: </properties>
      :end-before: <!-- please leave these here as the tutorial docs break if you delete them -->

#. Available from these repositories:

   .. literalinclude:: artifacts/pom.xml
      :language: xml
      :start-after: <!-- please leave these here as the tutorial docs break if you delete them -->
      :end-before: <build>

#. Finally we get to switch to Java 8:

   .. literalinclude:: artifacts/pom.xml
      :language: xml
      :start-after: </repositories>
      :end-before: </project>

#. You can check against the completed :download:`pom.xml <artifacts/pom.xml>`

#. Create a directory `src/test/resources` and in there create package `org.geotools.tutorial.csv`. Then add locations.csv to this package.

   * package: org.geotools.tutorial.csv
   * file: locations.csv

   .. literalinclude:: artifacts/locations.csv

   Download :download:`locations.csv <artifacts/locations.csv>`.

#. Below is a JUnit4 test case to confirm JavaCSV is available and can read our file. Create a directory `src/test/java` and in there create package `org.geotools.tutorial.csv`. Then add CSVTest.java to the package:

   .. literalinclude:: /../src/main/java/org/geotools/tutorial/csv/CSVTest.java
      :language: java
      :end-before: // locations.csv end
      :append: }
