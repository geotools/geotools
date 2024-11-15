CQL2
====

Quoting from the `OGC specification <https://docs.ogc.org/is/21-065r2/21-065r2.html>`_:

    The Common Query Language (CQL2) ... is a generic filter grammar that can be used to specify
    how resource instances in a source collection of any item type, including features, can be filtered
    to identify a results set.
    Typically, CQL2 is used in query operations to identify the subset of resources, such as features,
    that should be included in a response document

The specification provides two encodings for CQL2:

* ``cql2-text``, a human-readable text encoding
* ``cql2-json``, a JSON encoding for machine to machine communication

Requirement classes and implementation coverage
-----------------------------------------------

The CQL2 specification identifies a number of requirement classes, which are implemented only
partially in GeoTools. The following classes are implemented:

* `http://www.opengis.net/spec/cql2/1.0/req/advanced-comparison-operators` (like, between, in)
* `http://www.opengis.net/spec/cql2/1.0/req/property-property` (comparison between two properties)
* `http://www.opengis.net/spec/cql2/1.0/req/functions` (extensible functions)
* `http://www.opengis.net/spec/cql2/1.0/req/basic-spatial-functions` (point/bbox/s_intersects)
* `http://www.opengis.net/spec/cql2/1.0/req/basic-spatial-functions-plus` (all other single and multi geometry types)
* `http://www.opengis.net/spec/cql2/1.0/req/spatial-functions` (the other common spatial relationships)

The following optional requirement classes are currently not supported, but may be implemented in the future:

* `http://www.opengis.net/spec/cql2/1.0/req/case-insensitive-comparison`` (CASEI function)
* `http://www.opengis.net/spec/cql2/1.0/req/accent-insensitive-comparison` (ACCENTI function)
* `http://www.opengis.net/spec/cql2/1.0/req/temporal-functions` (various temporal comparisons between instants and intervals)
* `http://www.opengis.net/spec/cql2/1.0/req/array-functions` (support for array data types)

Examples
--------

The specification `Abstract Test Suite <https://docs.ogc.org/is/21-065r2/21-065r2.html#ats>`_
provides a wide variety of cql2-text filter examples, with are also used to test the GeoTools implementation.
As as reminder, the specification is not fully implemented, some of those examples will not parse
(check the relevant requirement class to find out if a specific example is supported).

The OGC API Features repository also holds a good number of
`CQL2 examples <https://github.com/opengeospatial/ogcapi-features/blob/master/cql2/standard/schema/examples>`_
in both text and JSON format, in two parallel directories.
For instance, `text/example17.txt` contains::

   (floors>5 AND material='brick') OR swimming_pool=true

while `json/example17.json` contains the equivalent JSON form::

   {
     "op": "or",
     "args": [
       {
         "op": "and",
         "args": [
           {
             "op": ">",
             "args": [
               { "property": "floors" },
               5
             ]
           },
           {
             "op": "=",
             "args": [
               { "property": "material" },
               "brick"
             ]
           }
         ]
       },
       {
         "op": "=",
         "args": [
           { "property": "swimming_pool" },
           true
         ]
       }
     ]
   }

Comparisong with (E)CQL
-----------------------

The basic alphanumeric filtering in CQL2 uses the same syntax as the older CQL.
However more advanced aspects of the two languages show significant differences.

For example, spatial filter functions in CQL2 use an `s_` prefix:

* CQL: ``DISJOINT(ATTR1, POINT(1 2))``
* CQL2: ``S_DISJOINT(ATTR1, POINT(1 2))``

The CQL BBOX filter is gone, replaced with a BBOX geometry constructor instead:

* CQL: ``BBOX(ATTR1, 1, 2, 3, 4)``
* CQL2: ``S_INTERSECTS(ATTR1, BBOX(1, 2, 3, 4))``

As a final example, temporal literals and filters are now expressed as function calls
(warning, the CQL2 temporal filters are not fully implemented in GeoTools yet):

* CQL: ``ATTR1 BEFORE 2006-12-31T01:30:00Z``
* CQL2: ``t_before(ATTR1, timestamp('2006-11-30T01:30:00Z'))``

In summary, please do not assume the two languages are interchangeable, as they share common
traits only in basic alphanumeric filtering.

Using cql2-text
---------------

The `cql2-text` module provides a parser for the human-readable text encoding of CQL2. To use
the module, add the following Maven dependency::

     <dependency>
      <groupId>org.geotools</groupId>
      <artifactId>gt-cql2-text</artifactId>
      <version>${geotools.version}</version>
    </dependency>

The `CQL2` utility class consists of static methods you can call to convert a text String into an
`Expression` or `Filter`. It is also able to take these these items and produce the appropriate text representation.

Simple parsing examples::

   Filter alphaFilter = CQL2.toFilter("attName >= 5");
   Filter geoFilter = CQL2.toFilter("S_INTERSECTS(geom, ENVELOPE(10, 10, 40, 40))");
   Expression expression = CQL2.toExpression("attName + 5");

Simple encoding examples:

.. code-block:: java

    FilterFactory ff = CommonFactoryFinder.getFilterFactory();
    Filter javaFilter =
           ff.less(
                   ff.divide(ff.property("population"), ff.literal(2)),
                   ff.divide(ff.property("pop2000"), ff.literal(2)));
    String cql2 = CQL2.toCQL2(javaFilter); // "population / 2 < pop2000 / 2"


Using cql2-json
---------------

The `cql2-json` module provides a parser for the JSON encoding of CQL2. To use the module, add the following Maven dependency::

     <dependency>
      <groupId>org.geotools</groupId>
      <artifactId>gt-cql2-json</artifactId>
      <version>${geotools.version}</version>
    </dependency>

The `CQL2Json` utility class consists of static methods you can call to convert a JSON string into a
GeoTools `Expression` or `Filter`.

Simple parsing example:

.. code-block:: java

   Filter filter = CQL2Json.toFilter("{\"op\":\"like\",\"args\":[{\"property\":\"eo:instrument\"},\"OLI%\"]}");

And a simple encoding example:

.. code-block:: java

   FilterFactory ff = CommonFactoryFinder.getFilterFactory();
   Filter javaFilter =
          ff.less(
                  ff.divide(ff.property("population"), ff.literal(2)),
                  ff.divide(ff.property("pop2000"), ff.literal(2)));
   String cql2 = CQL2Json.toCQL2(javaFilter);

which results in the following (formatted for readability):

.. code-block:: json

   {
     "op": "<",
     "args": [
       {
         "op": "/",
         "args": [
           {
             "property": "population"
           },
           2
         ]
       },
       {
         "op": "/",
         "args": [
           {
             "property": "pop2000"
           },
           2
         ]
       }
     ]
   }