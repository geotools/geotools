Schema Resolving
----------------

XSD Schema resolving is done through the ``SchemaResolver`` class. 


SchemaResolver
^^^^^^^^^^^^^^

This supports resolution of schemas obtained from an OASIS Catalog, the Java classpath, or cached network download, or all three.

This class will resolve any resource type, but it is designed to aid relative imports between XML Schemas; to do this it keeps a reverse-lookup table to convert resolved locations back to their original locations, facilitating correct determination of relative imports and includes. To ensure that this works, use a single instance of ``SchemaResolver`` to resolve a schema and all its dependencies.

Optional ``SchemaResolver`` constructors arguments allow configuration of permitted resolution methods.

OASIS Catalog
'''''''''''''

The resolver can be configured to use an `OASIS Catalog <http://www.oasis-open.org/committees/entity/spec-2001-08-06.html>`_ to resolve schema locations. The resolver uses catalog URI semantics to locate schemas, so ``uri`` or ``rewriteURI`` entries should be present in your catalog.

Example::

    SchemaResolver resolver = new SchemaResolver(SchemaCatalog.build(URLs.fileToUrl(new File("/path/to/catalog.xml"))));


Classpath
'''''''''

Schema resolution on the classpath is always enabled. For example, a schema ``http://schemas.example.org/exampleml/exml.xsd`` resolves to ``/org/example/schemas/exampleml/exml.xsd`` on the classpath. To create a resolver with only support for schemas on the classpath, use the default constructor::

    SchemaResolver resolver = new SchemaResolver();


Cache
'''''

If the resolver is configured to use a cache, schemas not resolved by other methods will be downloaded from the network and stored in the cache directory. For example, a schema published at ``http://schemas.example.org/exampleml/exml.xsd`` would be downloaded and stored as ``org/example/schemas/exampleml/exml.xsd`` in the cache directory.

Example::

    SchemaResolver resolver = new SchemaResolver(new SchemaCache(new File("/path/to/schema-cache"), true));

If downloads are not enabled, a prepopulated cache will still be used, but missing schemas will not be downloaded.
