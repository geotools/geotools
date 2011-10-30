Geometry Plugin
---------------

The Geometry module creates and manipulates geometric objects such as Points, Curves, and Surfaces.
Users of this module can assemble the factories needed to create the various objects they will use,
use the factories to make the objects, and then manipulate the objects directly.

**Maven**::
   
    <dependency>
      <groupId>org.geotools</groupId>
      <artifactId>gt-geometry</artifactId>
      <version>${geotools.version}</version>
    </dependency>


**Volunteer Needed**

This module currently has support for 2D objects; with the addition of 2.5D and 3D pending a
willing volunteer.

Internally some of the operations are a fork of the Java Topology Suite code base, since this
is also an LGPL project we are within the license restrictions of the JTS project.

The module was created in 2007 with an initial implementation of 2D objects completed by late
summer 2007. Hopefully this module will eventually become one of the core implementations used
by the GeoTools library.

This module is not currently "hooked-up" to anything in GeoTools, however the GeoTools feature
module can support additional geometry implementations such as ISO Geometry if people are willing
to put in the effort.

.. note::
   
   ISO 19107 Geometry Interfaces
   
   The geometric objects in this module are implementations of the gt-opengis
   Geometry interfaces which are realisations of the ISO 19107 Geometry
   schema.
  
   Users of the objects created by this module should therefore make
   instances using factories and then use only the methods defined in
   the gt-opengis javadocs to avoid accidentally depending on any specific
   implementation details.
   
**Maven**::
   
    <dependency>
      <groupId>org.geotools</groupId>
      <artifactId>gt-geometry</artifactId>
      <version>${geotools.version}</version>
    </dependency>

**Contents**

.. toctree::
   :maxdepth: 1

   build
   operation
