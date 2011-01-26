.. include:: <isonum.txt>

.. _querylab:

Query Lab
=========

.. note:: 
   This page is being revised and isn't complete yet

This tutorial illustrates how to query feature collections in GeoTools. In the earlier tutorials we have been working
with shapefiles. In this lab we will also bring out the big guns - a real spatial database.

If you are working in an enterprise that has as spatial database (e.g. Oracle, DB2) or geospatial middleware (e.g.
ArcSDE) you can use GeoTools to connect to your existing infrastructure. Here we will use `PostGIS
<http://postgis.refractions.net/>`_ which is a spatially-enabled extension of PostgreSQL supporting *Simple
Features for SQL*. We will build an application that can connect to both a PostGIS database and shapefiles.

.. image:: querylab.png

Dependencies
------------

Please ensure your pom.xml includes the following::

    <properties>
        <geotools.version>2.7-SNAPSHOT</geotools.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.geotools</groupId>
            <artifactId>gt-shapefile</artifactId>
            <version>${geotools.version}</version>
        </dependency>
        <dependency>
            <groupId>org.geotools.jdbc</groupId>
            <artifactId>gt-jdbc-postgis</artifactId>
            <version>${geotools.version}</version>
        </dependency>
        <dependency>
            <groupId>org.geotools</groupId>
            <artifactId>gt-epsg-hsql</artifactId>
            <version>${geotools.version}</version>
        </dependency>
        <dependency>
            <groupId>org.geotools</groupId>
            <artifactId>gt-swing</artifactId>
            <version>${geotools.version}</version>
        </dependency>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.5</version>
            <scope>test</scope>
        </dependency>
    </dependencies>

Example
-------

The example code is available
 * Directly from svn: QueryLab.java_
 * Included in the demo directory when you download the GeoTools source code

.. _QueryLab.java: http://svn.osgeo.org/geotools/tags/2.6.3/demo/example/src/main/java/org/geotools/demo/QueryLab.java 
 
Main Application
----------------
1. Please create the file **QueryLab.java**
2. Copy and paste in the following code:

   .. literalinclude:: ../../../demo/example/src/main/java/org/geotools/demo/QueryLab.java
      :language: java
      :start-after: // docs start source
      :end-before: // docs end main

The Application GUI
-------------------

Next we create the application's GUI which includes a text field to enter a query and a table to display data for the
features that the query selects. Here is the code to create the controls:

   .. literalinclude:: ../../../demo/example/src/main/java/org/geotools/demo/QueryLab.java
      :language: java
      :start-after: // docs start constructor
      :end-before: // docs start file menu

.. _querylab-file-menu:

Next we add menu items and Actions to the File menu to connect to either a shapefile or a PostGIS database:

   .. literalinclude:: ../../../demo/example/src/main/java/org/geotools/demo/QueryLab.java
      :language: java
      :start-after: // docs start file menu
      :end-before: // docs end file menu

Note that each Action is calling the same method but passing in a different DataStore factory (you might like to peek
ahead to the :ref:`connect method <querylab-connect-method>` at this point and then come back here).

Now let's look at the Data menu items and Actions:

   .. literalinclude:: ../../../demo/example/src/main/java/org/geotools/demo/QueryLab.java
      :language: java
      :start-after: // docs start data menu
      :end-before: // docs end constructor

.. _querylab-connect-method:

The connect method
------------------

Here is the method called by the File menu Actions plus a helper method to update the controls:

   .. literalinclude:: ../../../demo/example/src/main/java/org/geotools/demo/QueryLab.java
      :language: java
      :start-after: // docs start connect
      :end-before: // docs end connect

Note how the same code can be used to connect to quite different types of data stores as specified by the the
DataStoreFactorySpi (*Service Provider Interface*) parameter. Recall that the :ref:`File menu Actions
<querylab-file-menu>` call this method with an instance of the either **ShapefileDataStoreFactory** or
**PostgisNGDataStoreFactory**.

The **JDataStoreWizard** displays a dialog with entry fields appropriate to either a shapefile or PostGIS database. It
requires a few more lines of code than **JFileDataStoreChooser** which was used in :ref:`quickstart` to prompt the user
for a shapefile, but adds flexibility.

The query methods
-----------------

*Text coming soon...*

Getting feature data with the filterFeatures method
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

   .. literalinclude:: ../../../demo/example/src/main/java/org/geotools/demo/QueryLab.java
      :language: java
      :start-after: // docs start filterFeatures
      :end-before: // docs end filterFeatures

The countFeatures method
~~~~~~~~~~~~~~~~~~~~~~~~

   .. literalinclude:: ../../../demo/example/src/main/java/org/geotools/demo/QueryLab.java
      :language: java
      :start-after: // docs start countFeatures
      :end-before: // docs end countFeatures

The centerFeatures method
~~~~~~~~~~~~~~~~~~~~~~~~~

   .. literalinclude:: ../../../demo/example/src/main/java/org/geotools/demo/QueryLab.java
      :language: java
      :start-after: // docs start centerFeatures
      :end-before: // docs end centerFeatures

The queryFeatures method
~~~~~~~~~~~~~~~~~~~~~~~~

   .. literalinclude:: ../../../demo/example/src/main/java/org/geotools/demo/QueryLab.java
      :language: java
      :start-after: // docs start queryFeatures
      :end-before: // docs end source

Running the application
-----------------------

Start the application and select either *Open shapefile...* or *Connect to PostGIS database...* from the File menu.

If you choose to open a shapefile, the **JDataStoreWizard** will prompt you for a file:

.. image:: querylab-shapefile-page1.png

Next it will display a page with fields for namespace and creating a spatial index - for this example you can leave both
of these fields blank and just click Finish.

If you choose to connect to a PostGIS database the wizard will display the following page for you to enter the
connection parameters:

.. image:: querylab-postgis-page1.png

You can leave the *schema* and *namespace* fields blank (depending on your local database configuration). If you don't
have a PostGIS database you can try connecting to a public online database at `Refractions Research
<http://www.refractions.net/>`_ with the following credentials:

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
click the Finish button.

Once you have successfully connected to either a shapefile or a database the combobox in the menubar will display the
names of the available feature types: a single type for a shapefile and, possibly, multiple types for a PostGIS
database.

The query field will show 'include' which means select all features. Click the *Data->Get features* menu item and the
table will display the feature data.

The example queries below refer to the *cities* shapefile available as part of the `uDig sample dataset`__

.. _udigdata: http://udig.refractions.net/docs/data-v1_2.zip 

__ udigdata_

Simple attribute queries
~~~~~~~~~~~~~~~~~~~~~~~~

| **CNTRY_NAME = 'France'**
|   Select all features whose CNTRY_NAME attribute contains 'France'
|
| **POP_RANK >= 5**
|   Select all features with value >= 5 for the POP_RANK attribute
|
| **CNTRY_NAME = 'Australia' AND POP_RANK > 5**
|   Select features which satisfy both conditions

Spatial queries
~~~~~~~~~~~~~~~

| **BBOX(the_geom, 110, -45, 155, -10)**
|   This is a bounding box query that will select all features within the
    area bounded by 110 - 155 |deg| W, 10 - 45 |deg| S (a loose box around Australia).
    Notice that we name the geometry attribute which, for the *cities* shapefile, is
    Point type.

*More text coming soon...*
