/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */

package org.geotools.data.complex.config;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

import org.apache.commons.io.FilenameUtils;
import org.geotools.data.DataAccess;
import org.geotools.data.DataAccessFinder;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureSource;
import org.geotools.data.complex.AttributeMapping;
import org.geotools.data.complex.DataAccessRegistry;
import org.geotools.data.complex.FeatureTypeMapping;
import org.geotools.data.complex.FeatureTypeMappingFactory;
import org.geotools.data.complex.NestedAttributeMapping;
import org.geotools.data.complex.TreeAttributeMapping;
import org.geotools.data.complex.filter.XPath;
import org.geotools.data.complex.filter.XPath.Step;
import org.geotools.data.complex.filter.XPath.StepList;
import org.geotools.factory.Hints;
import org.geotools.feature.Types;
import org.geotools.filter.AttributeExpressionImpl;
import org.geotools.filter.FilterFactory;
import org.geotools.filter.FilterFactoryImplReportInvalidProperty;
import org.geotools.filter.expression.FeaturePropertyAccessorFactory;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.xml.AppSchemaCache;
import org.geotools.xml.AppSchemaCatalog;
import org.geotools.xml.AppSchemaResolver;
import org.geotools.xml.SchemaIndex;
import org.opengis.feature.Feature;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.expression.Expression;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * Utility class to create a set of {@linkPlain org.geotools.data.complex.FeatureTypeMapping}
 * objects from a complex datastore's configuration object (
 * {@link org.geotools.data.complex.config.AppSchemaDataAccessDTO}).
 * 
 * @author Gabriel Roldan, Axios Engineering
 * @author Rini Angreani, Curtin University of Technology
 * @author Russell Petty, GSV
 * @version $Id$
 *
 * @source $URL$
 *         http://svn.osgeo.org/geotools/trunk/modules/unsupported/app-schema/app-schema/src/main
 *         /java/org/geotools/data/complex/config/AppSchemaDataAccessConfigurator.java $
 * @since 2.4
 */
public class AppSchemaDataAccessConfigurator {
    /** DOCUMENT ME! */
    private static final Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger(AppSchemaDataAccessConfigurator.class.getPackage().getName());

    /** DOCUMENT ME! */
    private AppSchemaDataAccessDTO config;

    private FeatureTypeRegistry typeRegistry;

    private Map sourceDataStores;
    
    private FilterFactory ff = new FilterFactoryImplReportInvalidProperty();

    /**
     * Placeholder for the prefix:namespaceURI mappings declared in the Namespaces section of the
     * mapping file.
     */
    private NamespaceSupport namespaces;
    
    private Map schemaURIs;

    /**
     * This holds the data access ids when isDataAccess is specified in the mapping connection
     * parameters. A data access differs from a data store where it produces complex features,
     * instead of simple features. This requires the data access to be registered, so that its
     * complex feature source can later be retrieved via the DataAccessRegistry.
     */
    private ArrayList<String> inputDataAccessIds;

    /**
     * Creates a new ComplexDataStoreConfigurator object.
     * 
     * @param config
     *            DOCUMENT ME!
     */
    private AppSchemaDataAccessConfigurator(AppSchemaDataAccessDTO config) {
        this.config = config;
        namespaces = new NamespaceSupport();
        inputDataAccessIds = new ArrayList<String>();
        Map nsMap = config.getNamespaces();
        for (Iterator it = nsMap.entrySet().iterator(); it.hasNext();) {
            Map.Entry entry = (Entry) it.next();
            String prefix = (String) entry.getKey();
            String namespace = (String) entry.getValue();
            namespaces.declarePrefix(prefix, namespace);
        }
    }

    /**
     * Takes a config object and creates a set of mappings.
     * 
     * <p>
     * In the process will parse xml schemas to geotools' Feature Model types and descriptors,
     * connect to source datastores and build the mapping objects from source FeatureTypes to the
     * target ones.
     * </p>
     * 
     * @param config
     *            DOCUMENT ME!
     * 
     * @return a Set of {@link org.geotools.data.complex.FeatureTypeMapping} source to target
     *         FeatureType mapping definitions
     * 
     * @throws IOException
     *             if any error occurs while creating the mappings
     */
    public static Set<FeatureTypeMapping> buildMappings(AppSchemaDataAccessDTO config) throws IOException {
        AppSchemaDataAccessConfigurator mappingsBuilder;

        mappingsBuilder = new AppSchemaDataAccessConfigurator(config);
        Set<FeatureTypeMapping> mappingObjects = mappingsBuilder.buildMappings();

        return mappingObjects;
    }

    /**
     * Actually builds the mappings from the config dto.
     * 
     * <p>
     * Build steps are: - parse xml schemas to FM types - connect to source datastores - build
     * mappings
     * </p>
     * 
     * @return
     * 
     * @throws IOException
     *             DOCUMENT ME!
     */
    private Set<FeatureTypeMapping> buildMappings() throws IOException {
        // -parse target xml schemas, let parsed types on <code>registry</code>
        parseGmlSchemas();
        Map<String, DataAccess<FeatureType, Feature>> sourceDataStores = null;
        Set<FeatureTypeMapping> featureTypeMappings = null;
        try {
            // -create source datastores
            sourceDataStores = acquireSourceDatastores();
            // -create FeatureType mappings
            featureTypeMappings = createFeatureTypeMappings(sourceDataStores);
            return featureTypeMappings;
        } finally  {
            disposeUnusedSourceDataStores(sourceDataStores, featureTypeMappings);
        }
    }

    /**
     * Ensure any source data stores not used in a mapping are disposed.
     * 
     * @param sourceDataStores
     * @param featureTypeMappings
     */
    private void disposeUnusedSourceDataStores(
            Map<String, DataAccess<FeatureType, Feature>> sourceDataStores,
            Set<FeatureTypeMapping> featureTypeMappings) {
        if (sourceDataStores == null) {
            return;
        } else if (featureTypeMappings == null) {
            for (DataAccess<FeatureType, Feature> dataAccess : sourceDataStores.values()) {
                dataAccess.dispose();
            }
        } else {
            for (DataAccess<FeatureType, Feature> dataAccess : sourceDataStores.values()) {
                boolean usedDataAccess = false;
                for (FeatureTypeMapping mapping : featureTypeMappings) {
                    if (mapping.getSource().getDataStore() == dataAccess) {
                        usedDataAccess = true;
                        break;
                    }
                }
                if (!usedDataAccess) {
                    dataAccess.dispose();
                }
            }
        }
    }
    
    private Set<FeatureTypeMapping> createFeatureTypeMappings(
            Map<String, DataAccess<FeatureType, Feature>> sourceDataStores) throws IOException {
        Set mappingsConfigs = config.getTypeMappings();

        Set<FeatureTypeMapping> featureTypeMappings = new HashSet<FeatureTypeMapping>();

        for (Iterator it = mappingsConfigs.iterator(); it.hasNext();) {
            TypeMapping dto = (TypeMapping) it.next();

            FeatureSource featureSource = getFeatureSource(dto, sourceDataStores);
            // get CRS from underlying feature source and pass it on
            AttributeDescriptor target = getTargetDescriptor(dto, featureSource.getSchema()
                    .getCoordinateReferenceSystem());

            // set original schema locations for encoding
            target.getType().getUserData().put("schemaURI", schemaURIs);
            
            List attMappings = getAttributeMappings(target, dto.getAttributeMappings(), dto.getItemXpath());

            FeatureTypeMapping mapping;

            mapping = FeatureTypeMappingFactory.getInstance(featureSource, target, attMappings, namespaces,
                    dto.getItemXpath(), dto.isXmlDataStore());
            
            String mappingName = dto.getMappingName();
            if (mappingName != null) {
                mapping.setName(Types.degloseName(mappingName, namespaces));
            }

            featureTypeMappings.add(mapping);
        }
        return featureTypeMappings;
    }

    private AttributeDescriptor getTargetDescriptor(TypeMapping dto, CoordinateReferenceSystem crs)
            throws IOException {
        String prefixedTargetName = dto.getTargetElementName();
        Name targetNodeName = Types.degloseName(prefixedTargetName, namespaces);

        AttributeDescriptor targetDescriptor = typeRegistry.getDescriptor(targetNodeName, crs, dto
                .getAttributeMappings());
        if (targetDescriptor == null) {
            throw new NoSuchElementException("descriptor " + targetNodeName
                    + " not found in parsed schema");
        }
        return targetDescriptor;
    }

    /**
     * Creates a list of {@link org.geotools.data.complex.AttributeMapping} from the attribute
     * mapping configurations in the provided list of {@link AttributeMapping}
     * 
     * @param root
     * @param attDtos
     * 
     * @return
     */
    private List getAttributeMappings(final AttributeDescriptor root, final List attDtos,
            String itemXpath) throws IOException {
        List attMappings = new LinkedList();

        for (Iterator it = attDtos.iterator(); it.hasNext();) {

            org.geotools.data.complex.config.AttributeMapping attDto;
            attDto = (org.geotools.data.complex.config.AttributeMapping) it.next();

            String idExpr = attDto.getIdentifierExpression();            
            String idXpath = null;
            if (idExpr == null) {
             // this might be because it's an XPath expression
                idXpath = attDto.getIdentifierPath();
                if (idXpath != null) {
                    //validate without indexed elements
                    final StepList inputXPathSteps = XPath.steps(root, itemXpath + "/"+ idXpath, namespaces);
                    validateConfiguredNamespaces(inputXPathSteps);
                }                         
            }
                        
            String sourceExpr = attDto.getSourceExpression();
            String inputXPath = null;
            if (sourceExpr == null) {
                // this might be because it's an XPath expression
                inputXPath = attDto.getInputAttributePath();
                if (inputXPath != null) {
                    final StepList inputXPathSteps = XPath.steps(root, itemXpath + "/" + inputXPath, namespaces);
                    validateConfiguredNamespaces(inputXPathSteps);
                }
            }
            String expectedInstanceTypeName = attDto.getTargetAttributeSchemaElement();

            final String targetXPath = attDto.getTargetAttributePath();
            final StepList targetXPathSteps = XPath.steps(root, targetXPath, namespaces);
            validateConfiguredNamespaces(targetXPathSteps);

            final boolean isMultiValued = attDto.isMultiple();

            final Expression idExpression = (idXpath == null) ? parseOgcCqlExpression(idExpr)
                    : new AttributeExpressionImpl(idXpath, new Hints(
                            FeaturePropertyAccessorFactory.NAMESPACE_CONTEXT, this.namespaces));
            // if the data source is a data access, the input XPath expression is the source
            // expression
            final Expression sourceExpression; 

            sourceExpression = (inputXPath == null) ? parseOgcCqlExpression(sourceExpr) : new AttributeExpressionImpl(inputXPath, new Hints(
                  FeaturePropertyAccessorFactory.NAMESPACE_CONTEXT, this.namespaces));

            final AttributeType expectedInstanceOf;

            final Map clientProperties = getClientProperties(attDto, itemXpath);

            if (expectedInstanceTypeName != null) {
                Name expectedNodeTypeName = Types.degloseName(expectedInstanceTypeName, namespaces);
                expectedInstanceOf = typeRegistry
                        .getAttributeType(expectedNodeTypeName, null, null);
                if (expectedInstanceOf == null) {
                    String msg = "mapping expects and instance of " + expectedNodeTypeName
                            + " for attribute " + targetXPath
                            + " but the attribute descriptor was not found";
                    throw new DataSourceException(msg);
                }
            } else {
                expectedInstanceOf = null;
            }
            AttributeMapping attMapping;
            String sourceElement = attDto.getLinkElement();
            if(attDto.getLabel() != null || attDto.getParentLabel() != null) {
                attMapping = new TreeAttributeMapping(idExpression, sourceExpression, targetXPathSteps,
                        expectedInstanceOf, isMultiValued, clientProperties, attDto.getLabel(), 
                        attDto.getParentLabel(), attDto.getTargetQueryString(), attDto.getInstancePath());
            } else
            if (sourceElement != null) {
                // nested complex attributes, this could be a function expression for polymorphic types
                Expression elementExpr = parseOgcCqlExpression(sourceElement);
                String sourceField = attDto.getLinkField();
                StepList sourceFieldSteps = null;
                if (sourceField != null) {
                    // it could be null for polymorphism mapping, 
                    // i.e. when the linked element maps to the same table as the container mapping
                    sourceFieldSteps = XPath.steps(root, sourceField, namespaces);
                }
                // a nested feature
                attMapping = new NestedAttributeMapping(idExpression, sourceExpression,
                        targetXPathSteps, isMultiValued, clientProperties,
                        elementExpr, sourceFieldSteps, namespaces);
            } else {
                attMapping = new AttributeMapping(idExpression, sourceExpression, targetXPathSteps,
                        expectedInstanceOf, isMultiValued, clientProperties);
            }
            attMappings.add(attMapping);
        }
        return attMappings;
    }

    /**
     * Throws an IllegalArgumentException if some Step in the given xpath StepList has a prefix for
     * which no prefix to namespace mapping were provided (as in the Namespaces section of the
     * mappings xml configuration file)
     * 
     * @param targetXPathSteps
     */
    private void validateConfiguredNamespaces(StepList targetXPathSteps) {
        for (Iterator it = targetXPathSteps.iterator(); it.hasNext();) {
            Step step = (Step) it.next();
            QName name = step.getName();
            if (!XMLConstants.DEFAULT_NS_PREFIX.equals(name.getPrefix())) {
                if (XMLConstants.DEFAULT_NS_PREFIX.equals(name.getNamespaceURI())) {
                    throw new IllegalArgumentException("location step " + step + " has prefix "
                            + name.getPrefix() + " for which no namespace was set. "
                            + "(Check the Namespaces section in the config file)");
                }
            }
        }
    }

    private Expression parseOgcCqlExpression(String sourceExpr) throws DataSourceException {
        Expression expression = Expression.NIL;
        if (sourceExpr != null && sourceExpr.trim().length() > 0) {
            try {
                expression = CQL.toExpression(sourceExpr, ff);
            } catch (CQLException e) {
                String formattedErrorMessage = e.getMessage();
                AppSchemaDataAccessConfigurator.LOGGER.log(Level.SEVERE, formattedErrorMessage, e);
                throw new DataSourceException("Error parsing CQL expression " + sourceExpr + ":\n"
                        + formattedErrorMessage);
            } catch (Exception e) {
                e.printStackTrace();
                String msg = "parsing expression " + sourceExpr;
                AppSchemaDataAccessConfigurator.LOGGER.log(Level.SEVERE, msg, e);
                throw new DataSourceException(msg + ": " + e.getMessage(), e);
            }
        }
        return expression;
    }

    /**
     * 
     * @param dto
     * @return Map&lt;Name, Expression&gt; with the values per qualified name (attribute name in the
     *         mapping)
     * @throws DataSourceException
     */
    private Map getClientProperties(org.geotools.data.complex.config.AttributeMapping dto, String inputXPath)
            throws DataSourceException {

        if (dto.getClientProperties().size() == 0) {
            return Collections.EMPTY_MAP;
        }

        Map clientProperties = new HashMap();
        for (Iterator it = dto.getClientProperties().entrySet().iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            String name = (String) entry.getKey();
            Name qName = Types.degloseName(name, namespaces);
            String cqlExpression = (String) entry.getValue();
            
            final Expression expression;
            if (inputXPath == null) {
                expression =  parseOgcCqlExpression(cqlExpression);
            } else if(cqlExpression.startsWith("'")) {
                expression = ff.literal(cqlExpression);
            } else {
                expression =  new AttributeExpressionImpl(cqlExpression, new Hints(
                        FeaturePropertyAccessorFactory.NAMESPACE_CONTEXT, this.namespaces));
            }
                        
            clientProperties.put(qName, expression);
        }
        return clientProperties;
    }

    private FeatureSource<FeatureType, Feature> getFeatureSource(TypeMapping dto,
            Map<String, DataAccess<FeatureType, Feature>> sourceDataStores) throws IOException {
        String dsId = dto.getSourceDataStore();
        String typeName = dto.getSourceTypeName();

        DataAccess<FeatureType, Feature> sourceDataStore = sourceDataStores.get(dsId);
        if (sourceDataStore == null) {
            throw new DataSourceException("datastore " + dsId + " not found for type mapping "
                    + dto);
        }

        AppSchemaDataAccessConfigurator.LOGGER.fine("asking datastore " + sourceDataStore
                + " for source type " + typeName);
        Name name = Types.degloseName(typeName, namespaces);
        FeatureSource<FeatureType, Feature> fSource = sourceDataStore.getFeatureSource(name);

        if (inputDataAccessIds.contains(dsId)) {
            // reassign with complex feature source
            // since the dsId actually is the parameters for the underlying
            // data store.. but we want to connect to the data access
            fSource = DataAccessRegistry.getFeatureSource(fSource.getName());
            sourceDataStores.put(dsId, fSource.getDataStore());
        }

        AppSchemaDataAccessConfigurator.LOGGER.fine("found feature source for " + typeName);
        return fSource;
    }

    /**
     * Parses the target xml schema files and stores the generated types in {@link #typeRegistry}
     * and AttributeDescriptors in {@link #descriptorRegistry}.
     * 
     * <p>
     * The list of file names to parse is obtained from config.getTargetSchemasUris(). If a file
     * name contained in that list is a relative path (i.e., does not starts with file: or http:,
     * config.getBaseSchemasUrl() is used to resolve relative paths against.
     * </p>
     * 
     * @throws IOException
     */
    private void parseGmlSchemas() throws IOException {
        AppSchemaDataAccessConfigurator.LOGGER.finer("about to parse target schemas");

        final URL baseUrl = new URL(config.getBaseSchemasUrl());

        final List schemaFiles = config.getTargetSchemasUris();

        EmfAppSchemaReader schemaParser;
        schemaParser = EmfAppSchemaReader.newInstance();
        schemaParser.setResolver(buildResolver());

        // create a single type registry for all the schemas in the config
        typeRegistry = new FeatureTypeRegistry(namespaces);

        schemaURIs = new HashMap<String, String>(schemaFiles.size());
        String nameSpace;
        String schemaLocation;
        for (Iterator it = schemaFiles.iterator(); it.hasNext();) {
            schemaLocation = (String) it.next();
            final URL schemaUrl = resolveResourceLocation(baseUrl, schemaLocation);
            AppSchemaDataAccessConfigurator.LOGGER.fine("parsing schema "
                    + schemaUrl.toExternalForm());

            nameSpace = schemaParser.findSchemaNamespace(schemaUrl);
            schemaLocation = schemaUrl.toExternalForm();
            schemaURIs.put(nameSpace, schemaLocation);

            SchemaIndex schemaIndex = schemaParser.parse(nameSpace, schemaLocation);
            // add the resolved EMF schema so typeRegistry can find the needed type tree when it's
            // asked for the mapped FeatureType
            typeRegistry.addSchemas(schemaIndex);
        }
    }

    /**
     * Build the catalog for this data access.
     */
    private AppSchemaCatalog buildCatalog() {
        String catalogLocation = config.getCatalog();
        if (catalogLocation == null) {
            return null;
        } else {
            URL baseUrl;
            try {
                baseUrl = new URL(config.getBaseSchemasUrl());
                URL resolvedCatalogLocation = resolveResourceLocation(baseUrl, catalogLocation);
                return AppSchemaCatalog.build(resolvedCatalogLocation);
            } catch (MalformedURLException e) {
                LOGGER.warning("Malformed URL encountered while setting OASIS catalog location. "
                        + "Mapping file URL: " + config.getBaseSchemasUrl() + " Catalog location: "
                        + config.getCatalog() + " Detail: " + e.getMessage());
                return null;
            }
        }
    }
    
    /**
     * Build the cache for this data access.
     */
    private AppSchemaCache buildCache() {
        try {
            return AppSchemaCache.buildFromGeoserverUrl(new URL(config.getBaseSchemasUrl()));
        } catch (MalformedURLException e) {
            LOGGER.warning("Malformed mapping file URL: " + config.getBaseSchemasUrl() + " Detail: "
                    + e.getMessage());
            return null;
        }
    }
    
    /**
     * Build the resolver (catalog plus cache) for this data access.
     */
    private AppSchemaResolver buildResolver() {
        return new AppSchemaResolver(buildCatalog(), buildCache());
    }

    private URL resolveResourceLocation(final URL baseUrl, String schemaLocation)
            throws MalformedURLException {
        final URL schemaUrl;
        if (schemaLocation.startsWith("file:") || schemaLocation.startsWith("http:")) {
            AppSchemaDataAccessConfigurator.LOGGER
                    .fine("using resource location as absolute path: " + schemaLocation);
            schemaUrl = new URL(schemaLocation);
        } else {
            if (baseUrl == null) {
                schemaUrl = new URL(schemaLocation);
                AppSchemaDataAccessConfigurator.LOGGER
                        .warning("base url not provided, may be unable to locate" + schemaLocation
                                + ". Path resolved to: " + schemaUrl.toExternalForm());
            } else {
                AppSchemaDataAccessConfigurator.LOGGER.fine("using schema location "
                        + schemaLocation + " as relative to " + baseUrl);
                schemaUrl = new URL(baseUrl, schemaLocation);
            }
        }
        return schemaUrl;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return a Map&lt;String,DataStore&gt; where the key is the id given to the datastore in the
     *         configuration.
     * 
     * @throws IOException
     * @throws DataSourceException
     *             DOCUMENT ME!
     */
    private Map<String, DataAccess<FeatureType, Feature>> acquireSourceDatastores() throws IOException {
        AppSchemaDataAccessConfigurator.LOGGER.entering(getClass().getName(),
                "acquireSourceDatastores");

        final Map<String, DataAccess<FeatureType, Feature>> datastores = new LinkedHashMap<String, DataAccess<FeatureType, Feature>>();
        @SuppressWarnings("unchecked")
        final List<SourceDataStore> dsParams = config.getSourceDataStores();
        String id;

        for (SourceDataStore dsconfig : dsParams) {
            id = dsconfig.getId();

            if (dsconfig.isDataAccess()) {
                inputDataAccessIds.add(id);
            }

            @SuppressWarnings("unchecked")
            Map<String, Serializable> datastoreParams = dsconfig.getParams();

            datastoreParams = resolveRelativePaths(datastoreParams);

            AppSchemaDataAccessConfigurator.LOGGER.fine("looking for datastore " + id);

            DataAccess<FeatureType, Feature> dataStore = DataAccessFinder.getDataStore(datastoreParams);

            if (dataStore == null) {
                AppSchemaDataAccessConfigurator.LOGGER.log(Level.SEVERE,
                        "Cannot find a DataAccess for parameters " + datastoreParams);
                throw new DataSourceException("Cannot find a DataAccess for parameters "
                        + "(some not shown) "
                        + filterDatastoreParams(datastoreParams));
            }

            AppSchemaDataAccessConfigurator.LOGGER.fine("got datastore " + dataStore);
            datastores.put(id, dataStore);
        }

        return datastores;
    }
    
    /**
     * Database connection parameters that are probably safe to report to the end user.
     * (Things we can be pretty sure are not passwords.)
     */
    @SuppressWarnings("serial")
    private static final List<String> SAFE_DATASTORE_PARAMS = Collections
            .unmodifiableList(new ArrayList<String>() {
                {
                    add("url"); // shapefile
                    add("directory"); // propertyfile
                    add("namespace"); // just about everything
                    add("dbtype"); // jdbc
                    add("jndiReferenceName"); // jdni
                    // these are all various jdbc options
                    add("host");
                    add("port");
                    add("database");
                    add("schema");
                    add("user");
                }
            });
    
    /**
     * Return datastore params filtered to include only known-safe parameters.
     * We cannot try to find passwords, because even dbtype could be misspelled.
     * 
     * @param datastoreParams
     * @return
     */
    @SuppressWarnings("unchecked")
    private Map filterDatastoreParams(Map datastoreParams) {
        Map filteredDatastoreParams = new LinkedHashMap();
        for (String key : SAFE_DATASTORE_PARAMS) {
            if (datastoreParams.containsKey(key)) {
                filteredDatastoreParams.put(key, datastoreParams.get(key));
            }
        }
        return filteredDatastoreParams;
    }

    /**
     * Resolves any source datastore parameter settled as a file path relative to the location of
     * the xml mappings configuration file as an absolute path and returns a new Map with it.
     * 
     * @param datastoreParams
     * @return parameter map with resolved file url
     */
    private Map<String, Serializable> resolveRelativePaths(
            final Map<String, Serializable> datastoreParams) {
        Map<String, Serializable> resolvedParams = new HashMap<String, Serializable>();

        AppSchemaDataAccessConfigurator.LOGGER.entering(getClass().getName(),
                "resolveRelativePaths");
        for (Map.Entry<String, Serializable> entry : (Set<Map.Entry<String, Serializable>>) datastoreParams
                .entrySet()) {
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();

            if (value != null && value.startsWith("file:")) {
                // a parameter prefix of "file:" is the only case that will be resolved
                // any file paths entered without this prefix will remain unchanged
                String oldValue = value;
                String resolvedDataPath = null;
                String inputDataPath = (String) value.substring("file:".length());
                File f = new File(inputDataPath);

                if (!f.isAbsolute()) {
                    AppSchemaDataAccessConfigurator.LOGGER.fine("resolving original parameter "
                            + value + " for datastore parameter " + key);
                    try {
                        // use of URL here should be safe as the base schema url should
                        // not yet have undergone any conversion to file or
                        // encoding/decoding
                        URL mappingFileUrl = new URL(config.getBaseSchemasUrl());
                        AppSchemaDataAccessConfigurator.LOGGER.finer("mapping file URL is "
                                + mappingFileUrl.toString());

                        String mappingFileDirPath = DataUtilities.urlToFile(mappingFileUrl)
                                .getParent();
                        AppSchemaDataAccessConfigurator.LOGGER
                                .finer("mapping file parent directory is " + mappingFileDirPath);

                        // FilenameUtils.concat handles a number of system-dependent issues here
                        // but it might be better to add this method to DataUtilities
                        resolvedDataPath = FilenameUtils.concat(mappingFileDirPath, inputDataPath);
                        if (resolvedDataPath == null) {
                            throw new RuntimeException(
                                    "Relative path to datastore is incompatible with"
                                            + " the base path - check double dot steps.");
                        }
                    } catch (Exception e) {
                        AppSchemaDataAccessConfigurator.LOGGER.throwing(getClass().getName(),
                                "resolveRelativePaths", e);
                        throw new RuntimeException(e);
                    }
                } else {
                    resolvedDataPath = inputDataPath;
                }
                AppSchemaDataAccessConfigurator.LOGGER.finer("Path to data has been resolved to "
                        + resolvedDataPath);

                /*
                 * Shapefile expects the protocol "file:" at the beginning of the parameter value,
                 * other file-based datastores do not. We can distinguish shapefiles from other
                 * cases because the key is "url" and as of 2010-09-25 no other file-based datastore
                 * uses this key (properties files use the key "directory"). If a new file-based
                 * datastore is created, everything will work fine provided the key "url" is used if
                 * and only if the datastore expects a parameter value starting with "file:"
                 */
                if ("url".equals(key)) {
                    value = "file:" + resolvedDataPath;
                } else {
                    value = resolvedDataPath;
                }
                AppSchemaDataAccessConfigurator.LOGGER
                        .fine("Resolved " + oldValue + " -> " + value);
            }
            resolvedParams.put(key, value);
        }
        return resolvedParams;
    }
    
}
