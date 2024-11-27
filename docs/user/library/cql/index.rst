CQL
===

The ``gt-cql`` module a human readable "Contextual Query Language" for writing filter expressions
for working with geospatial information.

CQL was originally known as Common Query Language (so you will find lots of examples that still
refer to this name). The standard comes out of library science and was picked up by the OGC when
they were implementing their catalog server specification.

.. figure:: /images/gt-cql.svg
   
   gt-cql module

For our purposes it provides a great human readable way to expression Filter similar to an
SQL "where clause". Indeed we have our own extension that allows you to represent the full
range of GeoTools Filter and Expression ideas using simple text strings.

**Maven**::
   
    <dependency>
      <groupId>org.geotools</groupId>
      <artifactId>gt-cql</artifactId>
      <version>${geotools.version}</version>
    </dependency>

**References**

* http://en.wikipedia.org/wiki/Contextual_Query_Language
* http://www.opengeospatial.org/standards/specifications/catalog


CQL2
====

CQL2 is OGC evolution of CQL designed for use in OGC APIs. 
Unlike CQL/ECQL, it is not limited to a human readable text encoding, 
but also provides a JSON encoding (an equivalent to the XML encoding of Filter and Expression).

**Maven**::
   
    <dependency>
      <groupId>org.geotools</groupId>
      <artifactId>gt-cql2-text</artifactId>
      <version>${geotools.version}</version>
    </dependency>
    <dependency>
      <groupId>org.geotools</groupId>
      <artifactId>gt-cql2-json</artifactId>
      <version>${geotools.version}</version>
    </dependency>    


** References ** 

* https://docs.ogc.org/is/21-065r2/21-065r2.html


**Contents**

.. sidebar:: Details
   
   .. toctree::
      :maxdepth: 1
      
      faq
      internal

.. toctree::
   :maxdepth: 1
   
   cql
   ecql
   cql2
