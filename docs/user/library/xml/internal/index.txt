Internals
---------

The following material has been prepared by Justin Deolivera documenting the GTXML framework:

.. toctree::
   :maxdepth: 1
   
   overview
   bindings
   configuration
   code
   tutorial

Generated objects matching the xml schema: 

* :doc:`net.opengis.ows </extension/ogc/ows>` : open web services schema
* :doc:`net.opengis.wfs </extension/ogc/wfs>` : web feature service
* :doc:`net.opengis.wps </extension/ogc/wps>` : web processing service schema
* :doc:`net.opengis.wcs </extension/ogc/wcs>` : web coverage service schema
* :doc:`net.opengis.wfsv </extension/ogc/wfsv>` : web feature service schema
* :doc:`org.w3.xlink </extension/ogc/xlink>` : xlink schema

Schema and bindings plugins:

* :doc:`gt-xsd-core </extension/xsd/core>` : Basic types defined by XML schema
* :doc:`gt-xsd-fes </extension/xsd/fes>` : filter 2.0
* :doc:`gt-xsd-filter </extension/xsd/filter>` : filter 1.0 (used by ogc cat and wfs)
* :doc:`gt-xsd-kml </extension/xsd/kml>` : keyhole markup language
* :doc:`gt-xsd-wfs </extension/xsd/wfs>` : web feature service
* :doc:`gt-xsd-wps </extension/xsd/wps>` : web processing service
* :doc:`gt-xsd-gml3 </extension/xsd/gml3>` : geographic markup language 3
* :doc:`gt-xsd-gml2 </extension/xsd/gml2>` : geographinc markup language 2
* :doc:`gt-xsd-ows </extension/xsd/ows>` : open web services
* :doc:`gt-xsd-wcs </extension/xsd/wcs>` : web coverage service
* :doc:`gt-xsd-wms </extension/xsd/wms>` : web map service
* :doc:`gt-xsd-sld </extension/xsd/sld>` : style layer descriptor

**XML Development FAQ**

Frequently asked questions for binding developers.

* Q: NullPointerException in createURIWithCache when parsing?
  
  When I try to parse a document, i get the following error::
    
    java.lang.NullPointerException
        at org.eclipse.emf.common.util.URI.createURIWithCache(URI.java:560)
        at org.eclipse.emf.common.util.URI.createURI(URI.java:494)
        at org.eclipse.xsd.impl.XSDSchemaDirectiveImpl.resolve(XSDSchemaDirectiveImpl.java:389)
        at org.eclipse.xsd.impl.XSDImportImpl.importSchema(XSDImportImpl.java:476)
        at org.eclipse.xsd.impl.XSDSchemaImpl.resolveSchema(XSDSchemaImpl.java:2120)
        at org.eclipse.xsd.impl.XSDSchemaImpl.resolveNamedComponent(XSDSchemaImpl.java:2143) 
       ...
  
  What this means is that somewhere in your instance document, or in a schema it references,
  a relative uri cannot be resolved to an absolute location.
  
  Possible solutions:
  
  * Ensure that the getSchemaLocationResolver method has been implemented for the Configuration
    class of your schema. The Code Generator can be used to create a schema location resolver
    specific to your schema.
  * Ensure the Configuration class for your schema declares all the necessary dependencies.

* Q: empty xml element when trying to encode my objects?
  
  When I try to encode an object ( for instance a polygon ), I get an empty xml element::
    
    <ogc:Contains>
       <ogc:PropertyName>SERVICEBBOX</ogc:PropertyName>
       <gml:Polygon /> <!-- Polygon isn't well generated  -->
    </ogc:Contains>
  
  What this means is that the encoding for the element ( Polygon in this case )
  has not yet been implemented. Please post the problem to the users list.