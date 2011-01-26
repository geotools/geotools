:Author: Jody Garnett
:Author: Micheal Bedward
:Thanks: geotools-user list
:Version: |release|
:License: Create Commons with attribution

.. include:: <isonum.txt>

.. _query:

*****************
Query Tutorial
*****************
.. contents::

Welcome
========

Welcome to Geospatial for Java. This workbook is aimed at Java developers who are new to geospatial
and would like to get started.

Please set up your development environment prior to starting this tutorial. We will list the maven
dependencies required at the start of the workbook.

This tutorial illustrates how to query spatial data in GeoTools. In the earlier tutorials we
have been working with shapefiles. The focus of this workbook is the Filter API used to query
DataStores, such as shapefiles and databases, and Web Feature Servers.In this lab we will also bring
out the big guns - a real spatial database.

If you are working in an enterprise that has as spatial database (e.g. Oracle, DB2) or geospatial
middleware (e.g. ArcSDE) you can use GeoTools to connect to your existing infrastructure. Here we
will use `PostGIS <http://postgis.refractions.net/>`_ which is a spatially-enabled extension of
PostgreSQL supporting *Simple Features for SQL*. We will build an application that can connect
to both a PostGIS database and shapefiles.

We are trying out a code first idea with these workbooks |hyphen| offering you a chance to start with
source code and explore the ideas that went into it later if you have any questions. 

This workbook is part of the FOSS4G 2010 conference proceedings.

Jody Garnett

   Jody Garnett is the lead architect for the uDig project; and on the steering
   committee for GeoTools; GeoServer and uDig. Taking the roll of geospatial
   consultant a bit too literally Jody has presented workshops and training
   courses in every continent (except Antarctica). Jody Garnett is an employee
   of LISAsoft.

Michael Bedward

   Michael Bedward is a researcher with the NSW Department of Environment and
   Climate Change and an active contributor to the GeoTools users' list. He has
   a particularly wide grasp of all the possible mistakes one can make using
   GeoTools.
   

Query Lab Application
=====================

The :download:`QueryLab.java <../../src/main/java/org/geotools/tutorial/filter/QueryLab.java>`
example will go through using a Filter to select a FeatureCollection from a shapefile or other
DataStore.

We are going to be using connection parameters to connect to our DataStore this time;
and you will have a chance to try out using PostGIS or a Web Feature Server at the end of this
example.

1. Please ensure your pom.xml includes the following:
   
   .. literalinclude:: artifacts/pom.xml
      :language: xml
      :start-after: <url>http://maven.apache.org</url>
      :end-before: <dependencies>

2. Create the *QueryLab** class and copy and paste the following to get going.
   
   .. literalinclude:: ../../src/main/java/org/geotools/tutorial/filter/QueryLab.java
      :language: java
      :start-after: // docs start source
      :end-before: // docs end main

The Application GUI
-------------------

Next we create the application user interface which includes a text field to enter a query and a table to
display data for the features that the query selects.

Here is the code to create the controls:

1. Add the following constructor:

   .. literalinclude:: ../../src/main/java/org/geotools/tutorial/filter/QueryLab.java
      :language: java
      :start-after: // docs start constructor
      :end-before: // docs start file menu

2. Next we add menu items and Actions to the File menu to connect to either a shapefile or a
   PostGIS database:
   
   Each Action is calling the same method but passing in a different DataStore factory

   .. literalinclude:: ../../src/main/java/org/geotools/tutorial/filter/QueryLab.java
      :language: java
      :start-after: // docs start file menu
      :end-before: // docs end file menu

3. Now let us look at the Data menu items and Actions:
   
   .. literalinclude:: ../../src/main/java/org/geotools/tutorial/filter/QueryLab.java
      :language: java
      :start-after: // docs start data menu
      :end-before: // docs end data menu
   
Connect to DataStore
---------------------

In the quickstart we made use **FileDataStoreFinder** to connect to a specific file. This time
we will be using the more general **DataStoreFinder** which takes a map of connection parameters.

Note the same code can be used to connect to quite different types of data stores as specified by the the
DataStoreFactorySpi (*Service Provider Interface*) parameter. The file menu actions call this
method with an instance of the either **ShapefileDataStoreFactory** or **PostgisNGDataStoreFactory**.

The **JDataStoreWizard** displays a dialog with entry fields appropriate to either a shapefile or
PostGIS database. It requires a few more lines of code than **JFileDataStoreChooser** which was
used in the Quickstart to prompt the user for a shapefile, but allows greater control.

 
1. The File menu actions call this method to connect.
   
   .. literalinclude:: ../../src/main/java/org/geotools/tutorial/filter/QueryLab.java
      :language: java
      :start-after: // docs start connect
      :end-before: // docs end connect

2. Helper method to update the combo box used to choose a feature type:
   
   .. literalinclude:: ../../src/main/java/org/geotools/tutorial/filter/QueryLab.java
      :language: java
      :start-after: // docs start update
      :end-before: // docs end update

Query
------

A **Filter** is similar to the where clause of an SQL statement; defining a condition that each
selected feature needs to meet in order to be included.

Here is our strategy for displaying the selected features:
   
    * Get the feature type name selected by the user and retrieve the corresponding FeatureSource
      from the DataStore.
    
    * Get the query condition that was entered in the text field and use the CQL class to create a
      Filter object.
    
    * Pass the filter to the getFeatures method which returns the features matching the query
      as a FeatureCollection.
    
    * Create a FeatureCollectionTableModel for our dialog|apos|s JTable. This GeoTools
      class takes a FeatureCollection and retrieves the feature attribute names and the data for
      each feature.

With this strategy in mind here is the implementation:

1. Getting feature data using featureSource.getFeatures( filter )
   
   .. literalinclude:: ../../src/main/java/org/geotools/tutorial/filter/QueryLab.java
      :language: java
      :start-after: // docs start filterFeatures
      :end-before: // docs end filterFeatures

2. The FeatureCollection behaves as a predefined query or result set and does not load the data
   into memory.
   
   You can ask questions of the FeatureCollection as a whole using the available methods.

   .. literalinclude:: ../../src/main/java/org/geotools/tutorial/filter/QueryLab.java
      :language: java
      :start-after: // docs start countFeatures
      :end-before: // docs end countFeatures

4. By using the **Query** data structure you are afforded greater control over your request
   allowing you to select just the attributes needed; control how many features are returned;
   and ask for a few specific processing steps such as reprojection.
   
   Here is an example of selecting just the geometry attribute and displaying it in the table.

   .. literalinclude:: ../../src/main/java/org/geotools/tutorial/filter/QueryLab.java
      :language: java
      :start-after: // docs start queryFeatures
      :end-before: // docs end queryFeatures

Running the Application
-----------------------

Now we can run the application and try out some of these ideas:

1. Start the application and select either *Open shapefile...* from 
   the File menu.

   The **JDataStoreWizard** will prompt you for a file. Please select the **cities.shp**
   shapefile available as part of the `uDig sample dataset
   <http://udig.refractions.net/docs/data-v1_2.zip>` used in previous tutorials.

   .. image:: images/shapeWizard1.png

2. Press **Next** to advance to a page with optional parameters. For this example please press
   **Finish** to continue past these options.

   .. image:: images/shapeWizard1.png

4. Once you have successfully connected to your shapefile the combobox in the menubar will display
   the names of the available feature types. A single type for a shapefile is not that exciting
   but when you use PostGIS you should be able to choose which table to work with here.
   
5. The query field will indicate we wish to select all features using the common query language:
   
   .. code-block:: sql
      
      include
   
   Select :menuselection:`Data --> Get features` menu item and the table will display the feature
   data.
   
   .. image:: images/citiesInclude.png
   
6. Common query language allows for simple tests such as selecting features where the CNTRY_NAME
   attribute is 'France':
   
   .. code-block:: sql
      
      CNTRY_NAME = 'France'

   And choose Get Features to display.
   
   .. image:: images/citiesFrance.png
   
7. Comparisons are supported such as features with value >= 5 for the POP_RANK attribute:
   
   .. code-block:: sql
      
      POP_RANK >= 5

8. Boolean logic is supported allowing you to combine several tests:
   
   .. code-block:: sql
      
      CNTRY_NAME = 'Australia' AND POP_RANK > 5

9. Spatial queries are also supported:
   
   .. code-block:: sql
      
      BBOX(the_geom, 110, -45, 155, -10)
   
   This is a bounding box query that will select all features within the
   area bounded by 110 - 155 |deg| W, 10 - 45 |deg| S (a loose box around Australia).
   
   Notice that we name the geometry attribute which, for the *cities* shapefile, is
   Point type.

Things to Try
==============

* Try connecting to a public postgis instance.
  
  Select *Connect to PostGIS database...* from the file menu and fill in the following parameters.
  
  .. image:: images/postgisWizard1.png
  
  If you don't have a PostGIS database you can try connecting to a public online database at
  `Refractions Research <http://www.refractions.net/>` with the following credentials:
  
  :host:
    www.refractions.net
  :port:
    5432
  :database:
    bc-demo
  :user:
    demo
  :passwd:
    demo
  
  Next the wizard will display a second page of optional parameters. For this example you can leave this blank and just
  click the *Finish* button.

* We have sceen how to represent a Filter using CQL. There is also the origional XML representation
  used by web features servers to work with.
  
  .. code-block:: java
     
     Configuration configuration = new org.geotools.filter.v1_0.OGCConfiguration();
     Parser parser = new Parser( configuration );
     ...
     Filter filter = (Filter) parser.parse( inputstream );
  
  If you need an xml file to start from you can write one out using.
  
  .. code-block:: java
     
     Configuration = new org.geotools.filter.v1_0.OGCConfiguration();
     Encoder encoder = new org.geotools.xml.Encoder( configuration );
     
     encoder.encode( filter, org.geotools.filter.v1_0.OGC.FILTER, outputstream );
  
  For these examples to work you will need a dependency on *gt-xml*.

* One of the stated advantages of using **Query** is to cut down on the amount of data requested
  when working. Here is an example of calculating the center of the 

*  Earlier we covered the use FeatureIterator to sift through the contents of a FeatureCollection.
   Using this idea with **Query** allows you to work with just the geometry when determining the
   center of a collection of features.
   
   .. literalinclude:: ../../src/main/java/org/geotools/tutorial/filter/QueryLab.java
      :language: java
      :start-after: // docs start centerFeatures
      :end-before: // docs end centerFeatures

Filter
=======

.. sidebar: CQL
   
   CQL is defined in OGC Catalog specification; the standard comes from library science.

To request information from a FeatureSource we are going to need to describe (or select)  what
information we want back. The data structure we use for this is called a Filter.

We have a nice parser in GeoTools that can be used to create a Filter in a human readable form:

.. code-block:: java
   
   Filter filter = CQL.toFilter("POPULATION > 30000");
   
We can also make spatial filters using CQL |hyphen| geometry is expressed using the same Well Known Text
format employed earlier for JTS Geometry:

.. code-block:: java
   
   Filter pointInPolygon = CQL.toFilter("CONTAINS(THE_GEOM, POINT(1 2))");
   Filter clickedOn = CQL.toFilter("BBOX(ATTR1, 151.12, 151.14, -33.5, -33.51)";
   
You may also skip CQL and make direct use of a FilterFactory:

.. code-block:: java
   
   FilterFactory ff = CommonFactoryFinder.getFilterFactory( null );
   
   Filter filter = ff.propertyGreaterThan( ff.property( "POPULATION"), ff.literal( 12 ) );

Your IDE should provide command completion allowing you to quickly see what is available from
FilterFactory.

Note, filter is a real live java object that you can use do to work:

.. code-block:: java
   
   if( filter.evaluate( feature ) ){
       System.out.println( "Selected "+ feature.getId();
   }

The implementation in GeoTools is very flexible and able to work on Features, HashMaps and
JavaBeans.

.. HINT::

   You may of noticed that Filter is actually an interface. Because the Filter data structure is
   defined by a specification we cannot support the definition of new kinds of Filter objects and
   exepect them to be understood by the external services we communicate with.

   The good news is that Filter can be extended with new Functions; and our implementation can be
   taught how to work on new kinds of data using *PropertyAccessors*.

Expression
----------

You may have missed it in the last section; but we also described how to access data using an
expression.

Here are some examples:

.. code-block:: java
   
   ff.property( "POPULATION" ); // expression used to access the attribute POPULATION from a feature
   ff.literal( 12 );            // the number 12

You can also make function calls using the expression library.

Here are some examples:

.. code-block:: java
   
   CQL.toExpression("buffer( THE_GEOM)");
   CQL.toExpression("strConcat( CITY_NAME, POPULATION)");
   CQL.toExpression("distance( THE_GEOM, POINT(151.14,-33.51) )");

Query
-----

The Query data structure is used to offer finer grain control on the results returned. The
following query will request THE_GEOM and POPULATION from a FeatureSource |ldquo| cities |rdquo|:

.. code-block:: java
   
   Query query = new Query( "cities", filter, new String[]{ "THE_GEOM", "POPULATION" } );

FeatureCollection
-----------------
Previously we added features to a FeatureCollection during the CSV2SHP example. This was easy as the
FeatureCollection was in memory at the time. When working with spatial data we try to not have a
FeatureCollection in memory because spatial data gets big in a hurry.

Special care is needed when stepping through the contents of a FeatureCollection with a
FeatureIterator. A FeatureIterator will actually be streaming the data off disk and we need to
remember to close the stream  when we are done.

Even though a FeatureCollection is a |ldquo| Collection |rdquo| it is very lazy and does not load
anything until you start iterating through the contents. 

The closest Java concepts I have to FeatureCollection and FeatureIterator come from JDBC as show
below.

 ================== ====================
   GeoTools          JDBC
 ================== ====================
  FeatureSource      View
  FeatureStore       Table
  FeatureCollection  PreparedStatement
  FeatureIterator    ResultSet
 ================== ====================

If that is too much just remember |hyphen| please close your feature iterator when you are done. If
not you will leak resources and get into trouble.
