GeoTools FAQ
------------

Q: What is GeoTools ?  
^^^^^^^^^^^^^^^^^^^^^

GeoTools is a free, open source Java geospatial toolkit for working with both vector and raster data. It is made up of a
large number of modules that allow you to:

 * access GIS data in many file formats and spatial databases
 * work with an extensive range of map projections
 * filter and analyze data in terms of spatial and non-spatial attributes
 * compose and display maps with complex styling
 * create and analyze graphs and networks

GeoTools implements specifications of the `Open Geospatial Consortium <http://www.osgeo.org/>`_ including:

 * Simple Features
 * GridCoverage
 * Styled Layer Descriptor
 * Filter Encoding

GeoTools can be readily extended by adding new modules, either for custom applications or as contributions to the
library.

Q: How do I search the archives of the GeoTools mailing lists?
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Go to `this page <http://n2.nabble.com/GeoTools-the-java-GIS-toolkit-f1936684.html>`_.

Q: What are the features of the GeoTools Library?
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

That is a hard question to answer as GeoTools is a general purpose geospatial library.

Here is a sample of some of the great features in the library today:

* Supports OGC Grid Coverage implementation
* Coordinate reference system and transformation support
* Symbology using OGC Styled Layer Descriptor (SLD) specification
* Attribute and spatial filters using OGC Filter Encoding specification
* Supports graphs and networks
* Java Topology Suite (JTS) - with support for the OGC Simple Features Specification - used as the geometry model for vector features.
* A stateless, low memory renderer, particularly useful in server-side environments
* Powerful "schema asisted" parsing technology using XML Schema to bind to GML content
* Interact with OGC web services with both Web Map Server and Web Feature Server support
* Open plug-in system allowing you to teach the library additional formats
* Plug-ins for the ImageIO-EXT project allowing GeoTools to read additional raster formats from GDAL

Q: Okay what data formats does GeoTools support?
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

GeoTools supports additional formats through the use of plug-ins. You can control the formats supported
by your application by only including the plug-ins you requrie.

* arcgrid
* arcsde
* db2
* raster formats
  
  * geotiff
  * gtopo30
  * image - world plus image files using common image formats such as JPEG, TIFF, GIF and PNG
  * imageio-ext-gdal (allows access to additional GDAL formats thanks to the ImageIO project)
  * imagemoasaic
  * imagepyramid
  * JP2K
  
* Database "jdbc-ng" support
  
  * h2
  * mysql
  * oracle
  * postgis
  * spatialite
  * sqlserver

* postgis
* property - simple text file format often used for testing
* shapefile

Perhaps one of the unsupported modules or plugins may have what you need. These modules
are supplied by the community and do not yet meet the quality expected by the library:

There are also some "unsupported" formats that are either popular or under development:
* app-schema (under development) - allows the remapping and combining of one or more data sources into a provided application schema
* directory (under development)
* dfx
* edigeo
* geojson
* grassraster (under development)
* oracle-spatial
* wfs

The current authoritative list of plugins is of course the source code: 

* http://svn.osgeo.org/geotools/trunk/modules/plugin/
* http://svn.osgeo.org/geotools/trunk/modules/unsupported/

Common License Questions
^^^^^^^^^^^^^^^^^^^^^^^^

Q: What licence does GeoTools use?
''''''''''''''''''''''''''''''''''

All GeoTools modules are released under the GNU Lesser General Public License (LGPL). GeoTools can be used for
commercial applications, any changes made to GeoTools need to be made available to your customers.

An easy way to do this is to contribute the changes back to the GeoTools project (but this is not required).

Q: Can I use GeoTools in my Commercial Project?
'''''''''''''''''''''''''''''''''''''''''''''''

Yes. This is one of the reasons we chose the LGPL license. You can build a
Commercial application which uses GeoTools as a library and re-distribute your
application under any license you choose. Your users will get a license to your
application under the terms of your license and a license to the GeoTools
library under the terms of the LGPL. You only need to give your users some way
to get the source code of the GeoTools library, most easily by pointing your
users to the servers of the GeoTools project.

However, if you choose to modify the GeoTools library itself, then you have to
publish the source code to those changes to the users of your application.

The easiest way to do that will be to submit those changes back to the GeoTools
project so the changes can be incorporated into the core source code.

Q: Can I use GeoTools in my GPL Project?
''''''''''''''''''''''''''''''''''''''''

Yes. This is one of the reasons we chose the LGPL license. You can build a free
software application which uses GeoTools as a library and re-distribute your
application under the GPL license. Your users will get a license to your
application under the terms of the GPL and a license to the GeoTools library
under the terms of the LGPL. You only need to give your users some way to get
the source code of the GeoTools library, either by pointing your users to the
servers of the GeoTools project or by giving them the GeoTools code in the same
way you give them the code to your GPL application.

However, if you choose to modify the GeoTools library itself, then you have to
publish the source code to those changes to your users.

The easiest way to do that will be to submit those changes back to the GeoTools
project so the changes can be incorporated into the core source code.

.. note::

   This means you can use GeoTools as a library but you cannot
   incorporate GeoTools code directly into your GLP application. Legally, the
   latter amounts to re-licensing GeoTools under a new license and you do not have
   the right to do so.

Q: What restrictions are there on my use of GeoTools?
'''''''''''''''''''''''''''''''''''''''''''''''''''''

None. You can read, run, copy, or do anything else you want to do with the
GeoTools code. This is one of the four core freedoms of free software which we
grant you under the LGPL: the freedom to use the software for any purpose you
choose.
   
The only restrictions of the LGPL come when you are re-distributing GeoTools,
that is when you are passing it on to someone else either on its own or as part
of a larger product, such as when you share it or sell it.

Q: What restrictions are there on my re-distribution of GeoTools?
'''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''

Technically, you have to provide everyone who receives a copy of GeoTools from
you with some way to get the source code to the library. In practice, pointing
those users to the Geotools project itself is considered an adequate solution.
   
However, if you are re-distributing a modified version of GeoTools then you
need to provide users with access to the modified code. This means that you
must give your users some way to get the modified code such as by publishing it
yourself. An alternative way to provide your users with the modifications would
be to work with us to get your changes integrated into the GeoTools library--
-you could then use the new library directly. The best way to do this would be
to open a change request on our issue tracker and add to that request a code
patch containing your changes.

Q: What should I do if I am still unsure what I am allowed to do?
'''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''

You can clarify any questions you have by sending us questions to the user
mailing list: 
   
*  geotools-gt2-users@lists.sourceforge.net

Q: Why can't I find module X in the GeoTools distribution or javadocs?
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

If you're working with a recent GeoTools release then chances are the module that you're looking for is an
:doc:`unsupported module </unsupported/index>`. These modules not part of the standard GeoTools distribution but are
available from the `Subversion repository <http://svn.osgeo.org/geotools>`_ in the **modules/unsupported** folder. If
you are using Maven as your build tool you can include a dependency for an unsupported module as you would any other
GeoTools module.

Q: What is an unsupported module?
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Unsupported modules are those found in the **modules/unsupported** folder of each GeoTools version in the `Subversion
repository <http://svn.osgeo.org/geotools>`_. They are not part of the standard GeoTools distribution but are still
available for use via Subversion, Maven and manual download.

A module can be unsupported for one or more of the following reasons:

* It is under development and has not yet met all of the criteria for usability, test coverage, documentation etc to be
  included in the general GeoTools distribution.

* It lacks a module maintainer.

* It has been superseded by another module and dropped from the general distribution, but still has enough useful bits
  or active users to make it worth keeping (at least for a while).

Unsupported modules are a mixed bag: some are reliable and regularly used while others are in various states of
development or decay. The best way to find out the status of any particular module is to look in the `user list archives
<http://n2.nabble.com/geotools-gt2-users-f1936685.html>`_ and then, if you want to check further, post a question to the
list.

Building FAQ
------------

References:

* `Developers Guide <http://docs.geotools.org/latest/developer/index.html>`_

How do I build from source code?
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

GeoTools makes use of the maven build system (in part to help us reused code from
a number of other java projects).

To build all the modules::
 
  mvn install -Dall

To load the modules into the eclipse IDE.

1. Use :menuselection:`Windows --> Preferences` to open the Preference Dialog. 
   Using the tree on the left navigate to the Java > Build path > Classpath Variables preference
   Page.
   
2. Add an **M2_REPO** classpath variable pointing to your local repository
   where maven downloads jars.

    ==================  ========================================================
       PLATFORM           LOCAL REPOSITORY
    ==================  ========================================================
       Windows XP:      :file:`C:\\Documents and Settings\\Jody\\.m2\\repository`
       Windows:         :file:`C:\\Users\\Jody\.m2\\repository`
       Linux and Mac:   :file:`~/.m2/repository`
    ==================  ========================================================

2. Generate the .project and .classpath files needed for eclipse::
      
      mvn eclipse:eclipse -Dall

4. You can now use the eclipse import wizard to load existing projects.

See also:

* `Building <http://docs.geotools.org/latest/developer/guide/building/building.html>`_ (GeoTools Developers Guide)

How do I create an executable jar for my GeoTools app?
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

If you're familiar with Maven you might have used the `assembly plugin
<http://maven.apache.org/plugins/maven-assembly-plugin/>`_ to create self-contained, executable jars. The bad news is
that this generally won't work with GeoTools. The problem is that GeoTools modules often define one or more files in its
META-INF/services directory with the same names as files defined in other modules.  The assembly plugin just copies
files with the same name over the top of each other rather than merging their contents.

The good news is that the `Maven shade plugin <http://maven.apache.org/plugins/maven-shade-plugin/index.html>`_ can be
used instead and it will correctly merge the META-INF/services files from each of the GeoTools modules used by your
application.

The POM below will create an executable jar for the GeoTools :doc:`/tutorial/quickstart/index` module which includes all of the required
GeoTools modules and their dependencies.

.. sourcecode:: xml

  <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>org.geotools.demo</groupId>
    <artifactId>quickstart</artifactId>
    <packaging>jar</packaging>
    <version>1.0</version>
    <name>GeoTools Quickstart example</name>
    <url>http://geotools.org</url>

    <properties>
        <geotools.version>2.6.2</geotools.version>
    </properties>

    <build>
        <plugins>
            <plugin>
                <artifactId>maven-compiler-plugin</artifactId>
                <configuration>
                    <encoding>UTF-8</encoding>
                    <target>1.5</target>
                    <source>1.5</source>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-shade-plugin</artifactId>
                <version>1.3.1</version>
                <executions>
                    <execution>
                        <phase>package</phase>
                        <goals>
                            <goal>shade</goal>
                        </goals>
                        <configuration>
                            <transformers>
                                <!-- This bit sets the main class for the executable jar as you otherwise -->
                                <!-- would with the assembly plugin                                       -->
                                <transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                                    <manifestEntries>
                                        <Main-Class>org.geotools.demo.Quickstart</Main-Class>
                                    </manifestEntries>
                                </transformer>
                                <!-- This bit merges the various GeoTools META-INF/services files         -->
                                <transformer implementation="org.apache.maven.plugins.shade.resource.ServicesResourceTransformer"/>
                            </transformers>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>

    <dependencies>
        <dependency>
            <groupId>org.geotools</groupId>
            <artifactId>gt-shapefile</artifactId>
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
  </project>