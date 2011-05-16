Tutorial
========

The target audience is an experienced Java developer who is new to GIS. We try and lead with
careful step by step instructions (code first!) followed by any discussion.

The expected way for someone to use the tutorial is to follow the insturctions; and then only
read the details about any section they are unsure of.

With this in mind please do not mix explaining with code.

Here is an example tempalte to get you started.

1. Start with a nice welcome section; credit both yourself (and your employer?) and outline
   what your user should do to prepair.::
   
        :Author: Jody Garnett
        :Author: Micheal Bedward
        :Thanks: geotools-user list
        :Version: |release|
        :License: Create Commons with attribution
        
        .. include:: <isonum.txt>
        
        *****************
        Query Tutorial
        *****************
        
        Welcome
        ========
        
        Welcome to Geospatial for Java. This workbook is aimed at Java developers who are new to geospatial
        and would like to get started.
        
        Please set up your development environment prior to starting this tutorial. We will list the maven
        dependencies required at the start of the workbook.
        
        This tutorial illustrates how ...
        
        We are trying out a code first idea with these workbooks |hyphen| offering you a chance to start with
        source code and explore the ideas that went into it later if you have any questions. 
        
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

2. You can start by giving users a link to download the example code. The download directive
   is good this way in that it will be sure to package the referenced file up as part of the docs
   
   Remember the focus is on providing clear step by step instructions. While you can be friendly
   in the text; when providing instructions do not mix explaination with tasks to be performed::
   
        Query Lab Application
        =====================
        
        The :download:`QueryLab.java </../src/main/java/org/geotools/tutorial/filter/QueryLab.java>`
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
           
           .. literalinclude:: /../src/main/java/org/geotools/tutorial/filter/QueryLab.java
              :language: java
              :start-after: // docs start source
              :end-before: // docs end main

3. You can break your tutorial down into sections if needed.
   
   This is one way to "cheat" and take a break and explain something without mixing up the
   step by step instructions.::
  
        The Application GUI
        -------------------
        
        Next we create the application user interface which includes a text field to enter a query and a table to
        display data for the features that the query selects.
        
        Here is the code to create the controls:
        
        1. Add the following constructor:
        
           .. literalinclude:: /../src/main/java/org/geotools/tutorial/filter/QueryLab.java
              :language: java
              :start-after: // docs start constructor
              :end-before: // docs start file menu
        
        2. Next we add menu items and Actions to the File menu to connect to either a shapefile or a
           PostGIS database:
           
           Each Action is calling the same method but passing in a different DataStore factory
        
           .. literalinclude:: /../src/main/java/org/geotools/tutorial/filter/QueryLab.java
              :language: java
              :start-after: // docs start file menu
              :end-before: // docs end file menu
        
        3. Now let us look at the Data menu items and Actions:
           
           .. literalinclude:: /../src/main/java/org/geotools/tutorial/filter/QueryLab.java
              :language: java
              :start-after: // docs start data menu
              :end-before: // docs end data menu

3. The last section should be instructions for running the applicaiton; with screen snapshots
   showing what the expected result should be.::
   
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

4. The heart of the tutorial is the things "things to try" section.
   
   This is where most of the learning occurs. The preeceeding step by step instructions should be
   aimed for *everyone* to be able to complete (and feel successful). As such the content is often
   very safe.
   
   This is your chance to actually explore the topic now that they have a working and running
   application to start from.
   
   For the Things to Try section try and follow a progression:
   
   * Provide code examples exploring interesting aspects of your topic
   * Ask for something they have already been shown; giving a chance to apply what they have learned
   * Ask for something they can solve by looking only (the user guide or javadocs)
   
   Here is an example::
   
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

5. Finally you can use the rest of the document for a normal tutorial; explaining the topic as
   you see fit.
   
   This is where you can break out class diagrams (ObjectAid recommeneded) and diagrams showing
   how things fit togteher.
   
   Here is an example::
   
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

Example Code
------------

Please add any example code to the doc/src/java/main/ so we can be sure:

* Compiles and Functions
* Is kept up todate as API changes occur

It is the responsibility of those making an API change to update the example code, by always
using literal include you can be sure your tutorial will still function.

You may also wish to be kind and include a link to the tutorial in your javadocs to help
people get started.

Advanced
--------
  
Advanced Tutorials on speific topics; the target audience is now a fellow GeoTools developer
who wishes to implement a new plugin.
