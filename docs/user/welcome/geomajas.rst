Welcome Geomajas Developers
===========================

.. sidebar:: Geomajas
   
   http://www.geomajas.org/
   
   .. image:: /../web/logo/geomajas_logo.gif
   
   Geomajas is an enterprise-ready open source GIS framework for the web. Built using GeoTools
   Spring, and Google Web Toolkit for a fast efficeint all-Java solution!

Geomajas is a Java based client-server GIS framework for the web, that leverages the GeoTools
library on the backend. Altough Geomajas has it's own Object model, many functionalities rely
quite heavily on the GeoTools library. 

Geomajas is very Spring oriented which results in a lot of Spring services that use GeoTools under
the hood. Often the Geomajas interfaces will depend upon GeoTools objects (as parameters in the
methods, or return types).

This page covers  some specific examples of how and where the Geomajas framework leverages
GeoTools, and how to actually use or acquire those GeoTools components.

Plugin: GeoTools Layer
----------------------

One of the implementations of the Geomajas object model consists of a wrapper around the
GeoTools feature model. Through this 'layer', any GeoTools vector DataStore can be used to supply
vector data to a Geomajas layer. 

More information on this specific plug-in can be found here:

* http://www.geomajas.org/plugin/geotools-layer

Plugin: Hibernate Spatial Layer
-------------------------------

Geomajas also supports a Hibernate Spatial based layer:

* http://www.geomajas.org/plugin/hibernate-layer.

This layer allows the use of complex feature attributes based on many-to-one and one-to-many
entity relationships by exploiting the POJO capabilities of Hibernate.

FilterService
-------------

For filtering, Geomajas uses the gt-opengis Filter object model and the GeoTools ECQL standard. In
order to create Filter objects for a specific Geomajas layer, there is the **FilterService**
interface. By default a Spring bean for that service will always be available on the classpath,
so it can always be autowired.

The **FilterService** provides convenience methods for quickly creating OpenGis Filter objects
which can then be used in Geomajas (or GeoTools) layers.

Example use::

    @Autowired
    private FilterService filterService;
    ....
    Filter filter = filterService.parseFilter("<ECQL filter string>");
    filter = and(filter, filterService.createWithinFilter(geometry, geometryName));

GeoService
----------

To work with all coordinate systems known to Geomajas, one must use the **GeoService** provided
by Geomajas.

For defining coordinate systems and transformations between those coordinate systems, the
GeoTools library is used. That being said, Geomajas provides the ability to register extra
coordinate systems through the Spring configuration. As a result those new coordinate systems
are available only through Geomajas **GeoService** interface (and not when
directly using the GeoTools **CRS** helper clas).

This GeoService specifically handles CRS transformations with support for *transformable* areas.
These transformable areas define the boundaries wherein transformations between 2 coordinate
systems are supported. This way unexpected transformation exceptions are avoided.