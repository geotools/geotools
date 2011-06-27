Application Schema Support
--------------------------

The Application Schema Support (app-schema) family of modules support the delivery of complex feature types defined by a GML application schema. Key to the configuration of the app-schema module is the *mapping file*; because this is user input data, it is documented in the GeoServer User Manual:

*  `Application Schema Support section of the GeoServer User Manual <http://docs.geoserver.org/latest/en/user/data/app-schema/index.html>`_


Application Schema DataAccess
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Application schema support is provided by the ``gt-app-schema`` module.


Creating an AppSchemaDataAccess
'''''''''''''''''''''''''''''''

Like other data stores, an instance of ``AppSchemaDataAccess`` is constructed by supplying parameters to ``DataAccessFinder.getDataStore``. The parameters map must contain the following:

* ``dbtype`` set to the string ``"app-schema"``
* ``url`` set to a string containing a ``file:`` or ``jar:file:`` URL for a mapping file


Resource Management
'''''''''''''''''''

* Like all other ``DataAccess`` implementations (including ``DataStore`` implementations), ``AppSchemaDataAccess`` has a ``dispose`` method that must be called to release resources associated with the ``DataAccess``. These may include JDBC connections.

* To implement *feature chaining*, in which properties of features are features themselves, all ``AppSchemaDataAccess`` instances are registered in ``DataAccessRegistry`` so that they can locate each other. As a consequence, when a feature type has been defined once, it cannot be redefined. When calling ``dispose`` on an ``AppSchemaDataAccess`` it is automatically removed from the registry. To dispose and remove all ``AppSchemaDataAccess`` instances from the registry at once, call ``DataAccessRegistry.unregisterAndDisposeAll()``. 

* Parsed schemas are cached by ``AppSchemaXSDRegistry``. To clear the cache, call ``AppSchemaXSDRegistry.getInstance().dispose()``.


Schema download
'''''''''''''''

To enable automatic schema download and caching, create a directory ``app-schema-cache`` in the same directory as the mapping file, or one of its parent directories.


Example
'''''''

This example can be found in the the ``app-schema-example`` module.

The following code uses a mapping file to create three complex features from a property file::

    Map<String, Serializable> params = new HashMap<String, Serializable>();
    params.put("dbtype", "app-schema");
    params.put("url", AppSchemaExample.class.getResource("/gsml_MappedFeature.xml").toURI()
            .toString());
    DataAccess<FeatureType, Feature> dataAccess = null;
    try {
        dataAccess = DataAccessFinder.getDataStore(params);
        FeatureSource<FeatureType, Feature> source = dataAccess.getFeatureSource(new NameImpl(
                "urn:cgi:xmlns:CGI:GeoSciML:2.0", "MappedFeature"));
        FeatureCollection<FeatureType, Feature> features = source.getFeatures();
        FeatureIterator<Feature> iterator = features.features();
        try {
            while (iterator.hasNext()) {
                Feature f = iterator.next();
                System.out.println("Feature "
                        + f.getIdentifier().toString()
                        + " has gml:name = "
                        + ((ComplexAttribute) f.getProperty(new NameImpl(GML.name)))
                                .getProperty("simpleContent").getValue());
            }
        } finally {
            iterator.close();
        }
    } finally {
        if (dataAccess != null) {
            dataAccess.dispose();
        }
    }

The mapping file ``gsml_MappedFeature.xml`` used in the example code above::

    <?xml version="1.0" encoding="UTF-8"?>
    <as:AppSchemaDataAccess xmlns:as="http://www.geotools.org/app-schema"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.geotools.org/app-schema AppSchemaDataAccess.xsd">
        <namespaces>
            <Namespace>
                <prefix>gml</prefix>
                <uri>http://www.opengis.net/gml</uri>
            </Namespace>
            <Namespace>
                <prefix>gsml</prefix>
                <uri>urn:cgi:xmlns:CGI:GeoSciML:2.0</uri>
            </Namespace>
        </namespaces>
        <sourceDataStores>
            <DataStore>
                <id>datastore</id>
                <parameters>
                    <Parameter>
                        <name>directory</name>
                        <value>file:./</value>
                    </Parameter>
                </parameters>
            </DataStore>
        </sourceDataStores>
        <targetTypes>
            <FeatureType>
                <schemaUri>http://www.geosciml.org/geosciml/2.0/xsd/geosciml.xsd</schemaUri>
            </FeatureType>
        </targetTypes>
        <typeMappings>
            <FeatureTypeMapping>
                <sourceDataStore>datastore</sourceDataStore>
                <sourceType>gsml_MappedFeature</sourceType>
                <targetElement>gsml:MappedFeature</targetElement>
                <attributeMappings>
                    <AttributeMapping>
                        <targetAttribute>
                            gsml:MappedFeature
                        </targetAttribute>
                        <idExpression>
                            <OCQL>getId()</OCQL>
                        </idExpression>
                    </AttributeMapping>
                    <AttributeMapping>
                        <targetAttribute>
                            gml:name
                        </targetAttribute>
                        <sourceExpression>
                            <OCQL>NAME</OCQL>
                        </sourceExpression>
                    </AttributeMapping>
                    <AttributeMapping>
                        <targetAttribute>
                            gsml:shape
                        </targetAttribute>
                        <sourceExpression>
                            <OCQL>SHAPE</OCQL>
                        </sourceExpression>
                    </AttributeMapping>
                    <AttributeMapping>
                        <targetAttribute>gsml:observationMethod/gsml:CGI_TermValue/gsml:value</targetAttribute>
                        <sourceExpression>
                            <OCQL>METHOD</OCQL>
                        </sourceExpression>
                    </AttributeMapping>
                </attributeMappings>
            </FeatureTypeMapping>
        </typeMappings>
    </as:AppSchemaDataAccess>


The property file ``gsml_MappedFeature.properties`` used in the above mapping file (specified in the ``sourceType`` element)::

    _=NAME:String,METHOD:String,SHAPE:Geometry:srid=4283
    mf.25699=Some basalt|Unknown|POLYGON((143.561948 -38.532217, 143.561012 -38.533360, 143.549986 -38.526470, 143.561948 -38.532217))
    mf.25764=More basalt|Estimate|POLYGON((143.566412 -38.492157, 143.569803 -38.488559, 143.571572 -38.486718, 143.566412 -38.492157))
    mf.26106=Some mudstone|Seismic|POLYGON((143.496091 -38.800309, 143.496241 -38.799286, 143.496136 -38.797775, 143.497646 -38.800192, 143.496091 -38.800309))

This example requires ``gt-app-schema``, ``gt-property``, ``gt-epsg-hsql``, and their dependencies. **Create a directory called app-schema-cache in the same directory as the mapping file to enable automatic schema download.**


Application Schema Resolver
^^^^^^^^^^^^^^^^^^^^^^^^^^^

The ``gt-app-schema-resolver`` module supports resolution of GML application schemas obtained from an OASIS Catalog, the Java classpath, or cached network download, or all three.

This module will resolve any resource type, but it is designed to aid relative imports between XML Schemas; to do this it keeps a reverse-lookup table to convert resolved locations back to their original locations, facilitating correct determination of relative imports and includes. To ensure that this works, use a single instance of ``AppSchemaResolver`` to resolve a schema and all its dependencies.

Optional ``AppSchemaResolver`` constructors arguments allow configuration of permitted resolution methods.


OASIS Catalog
'''''''''''''

The resolver can be configured to use an `OASIS Catalog <http://www.oasis-open.org/committees/entity/spec-2001-08-06.html>`_ to resolve application schema locations. The resolver uses catalog URI semantics to locate application schemas, so ``uri`` or ``rewriteURI`` entries should be present in your catalog.

Example::

    AppSchemaResolver resolver = new AppSchemaResolver(AppSchemaCatalog.build(DataUtilities.fileToURL(new File("/path/to/catalog.xml"))));


Classpath
'''''''''

Schema resolution on the classpath is always enabled. For example, a schema ``http://schemas.example.org/exampleml/exml.xsd`` resolves to ``/org/example/schemas/exampleml/exml.xsd`` on the classpath. To create a resolver with only support for schemas on the classpath, use the default constructor::

    AppSchemaResolver resolver = new AppSchemaResolver();


Cache
'''''

If the resolver is configured to use a cache, schemas not resolved by other methods will be downloaded from the network and stored in the cache directory. For example, an application schema published at ``http://schemas.example.org/exampleml/exml.xsd`` would be downloaded and stored as ``org/example/schemas/exampleml/exml.xsd`` in the cache directory.

Example::

    AppSchemaResolver resolver = new AppSchemaResolver(new AppSchemaCache(new File("/path/to/app-schema-cache"), true));

If downloads are not enabled, a prepopulated cache will still be used, but missing schemas will not be downloaded.


AppSchemaConfiguration
''''''''''''''''''''''

Once you have configured your ``AppSchemaResolver``, you can use it to build an ``AppSchemaConfiguration`` that you can use to configure the GeoTools ``Encoder``::

    Configuration configuration = new AppSchemaConfiguration(
        "urn:cgi:xmlns:CGI:GeoSciML:2.0",
        "http://www.geosciml.org/geosciml/2.0/xsd/geosciml.xsd",
        resolver);
    // add a GML Configuration
    configuration.addDependency(new GMLConfiguration());

* If you do not add a GMLConfiguration dependency, Java bindings for GML types will not be found and encoding will not succeed.

* For an example of how to determine which GML version to use, see ``EmfAppSchemaReader`` in ``gt-app-schema``.


Sample DataAccess
^^^^^^^^^^^^^^^^^

The ``gt-sample-data-access`` module supports testing of complex feature support without introducing a dependency on the ``gt-app-schema`` module itself::

    DataAccess<FeatureType, Feature> dataAccess = DataAccessFinder
            .getDataStore(SampleDataAccessFactory.PARAMS);
    FeatureSource<FeatureType, Feature> featureSource = dataAccess
            .getFeatureSource(SampleDataAccessData.MAPPEDFEATURE_TYPE_NAME);
    FeatureCollection<FeatureType, Feature> featureCollection = featureSource.getFeatures();
    int count = 0;
    for (FeatureIterator<Feature> iterator = featureCollection.features(); iterator.hasNext(); iterator
            .next()) {
        count++;
    }


Application Schema Packages
^^^^^^^^^^^^^^^^^^^^^^^^^^^

The Application Schema Packages collection in ``app-schema-packages`` contains GML application schemas that have been packaged into Maven artifacts to support offline testing. These are manually published to the ``osgeo`` Maven repository. Configuring your Maven project to depend on one of these packages will cause ``AppSchemaResolver`` to resolve references to these schemas on the classpath.


