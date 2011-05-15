Reference Material
==================

The bulk of our user guide is devoted to reference material, on a module by module basis. Please
keep in mind that this is reference material focused on results - ie code examples to accomplish
a specific purpose.

We do not need a bunch of design documentation here, show us what your code can do.

You may want to briefly introduce any new concepts specific to your module, but please leave the
definitions of what things are to the javadocs.

As an example:

* When documenting Main you can mention what a Feature is used for - since DefaultFeature is the
  first implementation a user will have used.

* When documenting WFS you can mention what a Web Feature Server is - since that is the new concept
  for your plugin, but please don't define what a Feature again.

Focus on useful results and examples, leave the background information to the specifications,
tutorials, and welcome section.

Do not:

* You do not need to go all out and define the interfaces - we have javadocs for that.

General Approach
----------------

The GeoTools user guide is set up as a one two punch:

1. The Javadocs define the Concepts (users can see what a class is when they look at
   a tool tip in their IDE)

2. The Reference material here provides example code they can cut and paste into their application.

The expected Use of this material is (honestly):

1. User finds the the content using google
2. They cut and paste the source code into their program 
   (They may curse a bit as they hunt down dependencies but that is not your fault)
3. Their IDE can show them the Javadocs for any classes (in case they wonder what something is).

With this in mind our goal is to provide code examples showing how to perform common tasks.

Conventions:

* Heave use of literalinclude for code examples
  
  Use absolute path "/" to indicate the root directory of the document; and then relative
  path from there. This allows us to cut and paste the code example into any file and
  have it work out.
  
  * /../src/main/java/org/geotools/coverage/CoverageExamples.java
  * /../../modules/plugin/grassraster/src/test/java/org/geotools/gce/grassraster/AdvancedReaderTest.java

* Links between documents
  
  By default it will just use the title of the target document::
    
    :doc:`/tutorial/filter/query`
  
  You can also refer to modules "by name"::
    
    :doc:`gt-main </library/main/index>`
    :doc:`gt-main feature collection </library/main/collection>`

Tools:

* Inkscape
  
  Used for the diagrams (please don't use viso); and the result exported as a PNG for the docs

* ObjectAid
  
  Used for the class diagrams (Drag and Drop taht always reflects the current class definition).
  Set to automatically generate a PNG file.

Documenting a Module
--------------------

Each module is required to have one page of documetnation (preferably with a code example).::

    ArcGrid Plugin
    --------------
    
    The arcgrid module in the plugin group provides access to the ARCGRID raster format defined by
    ESRI and used in that company's software suites.
    
    This is a straightforward plugin with no additional information needed beyond that advertised
    by the GridCoverageExchange API.
    
    Please note that this format is well suited to data exchange; especially when considered as a
    text file is is obviously not suited to high performance.
    
    The arcgrid plugin supports:
    
    * Normal GridFormatFinder use::
    
                File f = new File("ArcGrid.asc");
                // Reading the coverage through a file
                AbstractGridFormat format = GridFormatFinder.findFormat( f );
                AbstractGridCoverage2DReader reader = format.getReader(f);
                
                GridCoverage2D gc = reader.read(null);
      
      This is the preferred approach as it allows your code to remain format
      independent.
    
    * Direct use for reading from a file::
    
            File f = new File"arcgrid/spearfish.asc.gz");
            
            GridCoverageReader reader = new ArcGridReader(f);
            GridCoverage2D gc = (GridCoverage2D) reader.read(null);

The above represents a good example of minimal documentation for a simple plugin.

Code Examples
^^^^^^^^^^^^^

As shown above you *can* include code examples directly in your page; it is better if you
can isolate complete working examples for the doc project.

* You can use a relative path to refer to these from your page::
  
    GridCoverage2D provides access to:
      
    .. literalinclude:: /../src/main/java/org/geotools/coverage/CoverageExamples.java
       :language: java
       :start-after: // exampleGridCoverageFactory start
       :end-before: // exampleGridCoverageFactory end
  
  In the above example we do not import the entire file; only the section of the file between
  the two comments.

* You can also use a relative path to pull examples out of your test cases.::
  
    .. literalinclude:: /../../modules/plugin/grassraster/src/test/java/org/geotools/gce/grassraster/AdvancedReaderTest.java
        :language: java
        :start-after: // readgrassraster start
        :end-before: // readgrassraster stop

Recommended Pages
^^^^^^^^^^^^^^^^^

We have the following recommended page structure:

* module/index.txt
* module/faq.txt (frequently asked questions; included in overall geotools faq)
* module/internal.xt (design documentation, class diagrams and any other stuff not involved in use)
* module/foo.txt
* module/bar.txt

Index Page
''''''''''

Index is similar in structure to the simple example above; the difference is we will be
gathering up the other pages.

1. Create a page similar to the following tempalte (or copy an example page you like)::

        ====
        NAME
        ====
        
        The **gt-NAME** module is where we publish ....
        
        .. sidebar:: Details
           
           .. toctree::
              :maxdepth: 1
              
              faq
              internal
        
        .. toctree::
           :maxdepth: 1
        
           foo
           bar
        
        A chance to provide more background information about the module here...
        
        .. image:: /images/gt-name.png
        
        The gt-name module provides:
        
        * List the core responsibilities
        * :doc:`gt-main style<../main/index>` reference to related module

2. There is an svg file you can use to get a nice geotools overview picture; it is located in
   the user/images/geotools.svg
   
   Use of InkScape is recommended.

3. Listing Plug-ins
   
   We use the table of contents to list the plugins for a module.
   
   List the plugins directly under the normal documentation. The following is from gt-coverage
   listing the supported "Format" plugins at the time of writing::

        Format plugins:
        
        .. toctree::
           :maxdepth: 1
           
           arcgrid
           arcsde
           geotiff
           gtopo30
           image
           imageio
           jdbc/index
           oracle
           mosaic
           pyramid
           
4. If there any unsupported plugins you can mention them as well::

        Unsupported plugins:
           
        .. toctree::
           :maxdepth: 1
           
           coverageio
           experiment
           geotiff_new
           grassraster
           jp2k
           netCDF
           matlab      
           tools

FAQ Page
''''''''
The faq is pretty simple; please use headings so that people can quickly find content.

1. Create a faq.txt page similar to the example below::

        Swing FAQ
        ---------
        
        Q: What is JMapPane for?
        ^^^^^^^^^^^^^^^^^^^^^^^^
        
        The JMapPane class is primiarly used as a teaching aid for the
        :doc:`tutorials </tutorial/index>` used to explore the GeoTools library.
        
        It is developed in collaboration with the user list, and while not a intended as a
        GIS application it is a good starting point for trying out your ideas.
        
        Q: JMapPane is Slow how do I make it faster?
        ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
        
        This really comes down to how you use the GeoTools renderer. Remember that the GeoTools renderer
        is doing a lot of calculation and data access; not what you want in the middle of animation.
        
        The gt-renderer is optimised for memory use; it does not loading your data into memory
        (it is drawing from disk, or database, each time). You can experiment with loading your data
        into memory (specifically into a spatial index) if you want faster performance out of it.
        
        For raster rendering you have a great deal of control over performance using JAI TileCache settings
        in addition to convering your rasters into an efficient format (anything is better than jpeg).
        
        References:
        
        * `FeatureCollection Performance </library/main/collection>`_

2. Update the root faq.txt to include your new page::

        .. include:: /unsupported/name/faq.txt

Internal
''''''''

Covers the internal details of your module with the target audience being a Java developer
who is looking to write a plugin for your module.

Library
-------

Documents the core library.

* library/module
* library/internal
  
  This is **Advanced** covering integration of GeoTools with facilities provided by an external
  developer (who is often hooking GeoTools up to facilities provided by an existing application).
  
  * Easy: Logging (teaching GeoTools how to make use of an application's existing logging facilities)
  * Medium: Making use of a Java EE application server that shares JDBC Connection Pools between
    running applications
  * Hard: An organisation that has a single EPSG database for a workgroup (and have registered this
    database as a JNDI service).

  All of these issues boil down to careful application of:
  
  * Factories (how you can teach GeoTools new tricks)
  * Hints (generally used to inject your own Factory into GeoTools)

Extension
---------

Document extensions to the core GeoTools concepts.

Unsupported
-----------

Holding area to collect documentation for modules before they become supported.
