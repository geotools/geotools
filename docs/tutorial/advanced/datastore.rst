:Author: Jody Garnett
:Thanks: geotools-devel list
:Version: |release|
:License: Create Commons with attribution

.. _datastore_tutorial:

****************************
 DataStore Developers Guide
****************************

.. sectionauthor:: Jody Garnett <jody.garnett@gmail.org>

.. contents::

Forward
========

The GeoTools2 project strives to support as many geographical data formats as possible because getting data into the GeoTools2 API allows access to a vast suite of tools. In order to transform a data format into the GeoTools2 feature representation one must write an implementation of the DataStore interface.

Once a DataStore implementation is written, any data written in that format becomes available not only for GeoTools2 users, but also for projects built on top of GeoTools2.

Writing a new DataStore for GeoTools2 is one of the best ways to get involved in the project, as writing it will make clear many of the core concepts of the API. Finally, the modular nature of GeoTools2 allows new DataStores to quickly become part of the next release, so that new formats are quickly available to all GeoTools2 users.

The DataStore interface borrows most of its concepts and some of its syntax from the OpenGIS Consortium (OGC) Web Feature Server Specification:

* Feature - atomic unit of geographic information
* FeatureType - keeps track of what attributes each Feature can hold
* FeatureID - a unique id associated with each Feature (must start with a non-numeric character)

Introduction
=============

Earlier was made a bit of code to read in a comma seperated value file; and produce a feature
collection (which we could save out using the shapefile datastore class).

This time out we are going to make a DataStore.

Here was the sample file we used:

#. Create a text file location.csv and copy and paste the following locations into it:

   .. literalinclude:: artifacts/locations.csv

#. Or download :download:`locations.csv <artifacts/locations.csv>`.

Definitions
~~~~~~~~~~~~
As you walk through this tutorial, please remember the following:

* FeatureID or FID - uniquely defines a Feature (row in our csv file)
* FeatureType - same as the name of the .csv file (ie. "locations" for locations.csv)
* DataStore - assess all the FeatureTypes (.csv files) in a directory
* FeatureType or Schema - names of the columns and their types

JavaCSV Reader
~~~~~~~~~~~~~~~

To read csv files this time out we are going to make use of the Java CSV Reader project.

* http://www.csvreader.com/java_csv.php

Time to create a new project making use of this library:

# Create a *csv* project using maven
# Use the following maven dependencies

.. literalinclude:: artifacts/pom.xml
   :language: xml
   :start-after: </properties>
   :end-before: <repositories>

# Or download :download:`pom.xml <artifacts/pom.xml>`  

Creating CSVDataStore
======================

The first step is to create a basic DataStore that only supports feature extraction. We will read
data from a csv file into the GeoTools feature model.

To implement a DataStore we will subclass ContentDataStore. This is a helpful base class for
making new kinds of content available to GeoTools. The GeoTools library works with an interaction
model very similar to a database - with transactions and locks. ContentDataStore is going to handle
all of this for us - as long as we can teach it how to access our content.

ContentDataStore requires us to implement the following two methods:

* createTypeNames() - name of all the different kinds of content (tables or types). In a CSV file we
  will only have one kind of content
* createFeatureSource(ContentEntry entry)

The class *ContentEntry* is a bit of a scratch pad used to keep track of things for each type.

Initially we are going to make a read-only datastore accessing CSV content:

# To begin create the file CSVDataStore extending ContentDataStore

   .. literalinclude:: ../../src/main/java/org/geotools/tutorial/datastore/CSVDataStore.java
      :language: java
      :start-after: // header start
      :end-before: // header end
      
# We are going to be working with a single CSV file

   .. literalinclude:: ../../src/main/java/org/geotools/tutorial/datastore/CSVDataStore.java
      :language: java
      :start-after: // constructor start
      :end-before: // constructor end

Listing TypeNames
~~~~~~~~~~~~~~~~~~

A DataStore may provide access to several different types of information. The method createTypeNames
provides a list of the available types. This is called once; and then the same list is returned
by ContentDataStore.getTypeNames() each time. (This allows you to do some real work; such as
connecting to a web service or parsing a large file, without worrying about doing it
many times).

For our purposes this list will be the name of the csv file.

# We can now implement createTypeNames() returning a the filename

   .. literalinclude:: ../../src/main/java/org/geotools/tutorial/datastore/CSVDataStore.java
      :language: java
      :start-after: // createTypeNames start
      :end-before: // createTypeNames end


