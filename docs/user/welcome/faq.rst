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
  * grassraster
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
* dfx
* edigeo
* geojson
* wfs

The current authoritative list of plugins is of course the source code: 

* http://svn.osgeo.org/geotools/trunk/modules/plugin/
* http://svn.osgeo.org/geotools/trunk/modules/unsupported/

GeoTools versions
^^^^^^^^^^^^^^^^^

Q. How are GeoTools versions organized?
'''''''''''''''''''''''''''''''''''''''

Like many open source projects, GeoTools has a *development* version and one or more *stable* versions active at any
given time. By active, we mean that the project developers are working on new features, improvements and bug fixes.

Source code for the stable branch(es) can be found in the *branches* folder of the GeoTools subversion repository. For
example, at the time of writing the active stable branch is `branches/2.7.x <http://svn.osgeo.org/geotools/branches/2.7.x/>`_.

Formal releases are based on a stable branch. The source code for a formal release is archived in the *tags* folder of
the subversion repository. For example, the version 2.7.1 sources can be found in
`tags/2.7.1 <http://svn.osgeo.org/geotools/tags/2.7.1/>`_.

Source code for the development version are in the `trunk <http://svn.osgeo.org/geotools/trunk/>`_ folder. This is the
bleeding edge code where the latest features are being worked on. Eventually this code will become the next stable
branch.

Commencing with GeoTools version 8, a major.minor.patch numbering system applies. 

major
    An increment of the major identifier (e.g. from version 8.x.y to 9.0.0) indicates substantial changes that can break
    binary compatibility with previous versions.

minor
    An increment in the minor identifier (e.g. from version 8.0.y to 8.1.y) indicates new features and/or improvements
    that do not break binary compatibility with the previous version.

patch
    An increment in the patch identifier (e.g. from version 8.0.0 to 8.0.1) indicates fixes and minor tweaks since the
    previous version.

Q. What is a SNAPSHOT version and how do I use it?
''''''''''''''''''''''''''''''''''''''''''''''''''

A snapshot is the GeoTools code that the developers are actively working on. Usually there will be two active snapshots:
one associated with the most recent formal release (e.g GeoTools 2.7-SNAPSHOT) and a second for the development version
(e.g. GeoTools 8-SNAPSHOT). At times there will also be snapshot releases for an earlier stable branch that is still
being maintained (e.g. GeoTools 2.6-SNAPSHOT).

New snapshot jars are built nightly and deployed to a repository separate from the one used for formal releases. If you
are using Maven as your build tool you can work with a snapshot release by adding the following to your pom.xml::

    <repository>
        <id>opengeo</id>
        <name>OpenGeo Maven Repository</name>
        <url>http://repo.opengeo.org/</url>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
    </repository>

You can now build your project against a snapshot release by setting it as the your version property as shown here::

    <properties>
        <geotools.version>8-SNAPSHOT</geotools.version>
    </properties>


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

