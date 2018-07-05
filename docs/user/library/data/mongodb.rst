Mongodb Plugin
--------------

MongoDB is a popular documentation database. This plugin provides read-write
access to appropriately indexed collections.

**Maven**::

   <dependency>
      <groupId>org.geotools</groupId>
      <artifactId>gt-mongodb</artifactId>
      <version>${geotools.version}</version>
    </dependency>

To publish a collection:

* A compliant collection must have one or more spatial indexes of type 2dsphere. 
  Any collection without an indexed field of type 2dsphere will be ignored.

* When examining compliant collections, the schema store will be queried for an 
  existing schema by using the collection name as the schema name. If a schema 
  exists it will be used, otherwise the schema is inferred from the collection
  and cached to the schema store for reuse.

* Location data is stored in a collection as GeoJSON objects. The coordinate 
  reference system for GeoJSON uses the WGS84 datum with axis order 
  longitude/latitude. Therefore, the geometry attribute descriptor in a schema 
  must specify EPSG:4326 as CRS (always true for inferred schemas).

* A new collection can be created using createSchema method. This should not be 
  used to manually define a schema for an existing collection.

Functionality
-------------

The MongoDataStore supports the use of MongoDB as a data store with the 
following connection parameters:

* data_store: specifies the MongoDB instance and database to connect to.

  This field requires a MongoDB client URI as specified by the MongoDB Manual. A 
  typical URI will be of the form: :kbd:`mongodb://example.com:27017/database` .

  The URI must include a database, but the database will be created if it does 
  not yet exist. Write access is only required if the database needs to be created
  or if other write operations such as WFS Transactions are expected. If needed, 
  credentials can be supplied with the MongoDB client URI, in the form:
  :kbd:`mongodb://username:password@example.com:27017/database` .

* schema_store: Designates the storage for inferred and manually defined 
  schemas.

  This field can accept either a mongodb or file URI. The directory will be 
  created if it does not exist, in which case write permissions will be necessary.

  The database and collection names are optional. If missing, the database name 
  will default to geotools and the collection name to schemas. The database and 
  collection must be writable using the credentials provided with the URI. Schemas 
  are stored as MongoDB documents or files adhering to the JSON schema format with 
  the schema "Type Name" (typeName) as the key.

JSON Schema
-----------

Keep in mind:

* The valid GeoJSON geometry encodings are Point, LineString, 
  Polygon, MultiPoint, MultiLineString, MultiPolygon. GeoJSON multigeometry 
  variants are only supported for MongDB version 2.5 and newer.

* The following Java equivalents of BSON types are valid: String, Double, Long, 
  Integer, Boolean, Date.

For the following GeoJSON feature::

   {   "type": "Feature",
       "geometry": {
           "type": "Point",
           "coordinates": [ 45.52, -122.681944 ]
       },
       "properties": {
           "city": "Portland",
           "year": "2014"
           "attendance": "840"
       }
   }

is described using the following schema::

   {
       "typeName": "places",
       "userData": {
           "collection": "places"
        },
       "geometryDescriptor": {
           "localName": "location",
           "crs": {
               "properties": {
                   "name": "urn:ogc:def:crs:EPSG:4326"
               },
               "type": "name"
           }
       },
       "attributeDescriptors": [
           {
               "localName": "location",
               "type": {
                   "binding": "org.locationtech.jts.geom.Point"
               },
               "userData": {
                   "encoding": "GeoJSON",
                   "mapping": "geometry"
               }
           },
           {   "localName": "city",
               "type": { "binding": "java.lang.String" },
               "userData": { "mapping": "properties.name" }
           },
           {   "localName": "year",
               "type": { "binding": "java.lang.String" },
               "userData": { "mapping": "properties.year" }
           },
           {   "localName": "attendance",
               "type": { "binding": "java.lang.String" },
               "userData": {  "mapping": "properties.attendance" }
           }
       ]
   }

File URI schema stores:

* For the directory-based schema store, edit the JSON document with the typeName 
  requiring modification.

  Schemas are written using createSchema() without indenting, but you can indent the 
  resulting file for readability if desired.

MongoDB URI schema stores:

* Using a MongoDB document manipulation tool, update or insert the schema 
  document in the collection maintaining the document in a form that follows the 
  JSON schema format.

  The JSON files contained in the file schema store are in a format that can be 
  inserted into a MongoDB schema store (as long as the typeName in the file is 
  unique to the document collection ).

  Multiple schemas, or views, can be created for a single MongoDB document 
  collection by creating a new, unique, typeName and specifying the collection 
  under the root-level userData object.

Implementation Notes
--------------------

* Bounding box calculation makes use of a full table scan.

* Multigeometry support requires MongoDB versions 2.5 and newer

* Self-intersecting polygons is a common data problem preventing 
  MongoDBDataStore from functioning. Please note that self-intersection
  may arise due to the transformation to WGS84 coordinates (which is a necessary 
  preliminary step for importing data into MongoDB), even
  if they did not exist in the original dataset.

* All 2dsphere indexes and spatial operations assume the WGS84 datum. All 
  indexed GeoJSON data stored in a MongoDB document collection is assumed to be 
  referenced with the WGS84 coordinate reference system.

* MongoDB versions tested through 2.4.9 do not support more than one operation 
  on a spatial index nested in an $or operation (so splitting a query into two 
  across the dateline will not work).

* Within, Intersects and BBOX filters are implemented with $geoWithin and 
  $geoIntersects operations. These operations are limited when effected by 
  geometries spanning a hemisphere (and will use the smaller geometry).

Usage Notes
--------------------

* Attribute names containing characters other than letters and numbers may cause 
  issues if used in CQL filters and therefore should be enclosed in double quotes 
  (see: 
  http://docs.geoserver.org/latest/en/user/filter/ecql_reference.html#attribute). 
  This is especially relevant for nested properties, which are named after their 
  full path (dot-notation) by the default schema inference algorithm.

