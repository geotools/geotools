/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008-2011, Open Source Geospatial Foundation (OSGeo)
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
import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.geotools.api.data.DataAccess;
import org.geotools.api.data.DataAccessFinder;
import org.geotools.api.data.DataSourceException;
import org.geotools.api.data.FeatureSource;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.feature.Feature;
import org.geotools.api.feature.GeometryAttribute;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.AttributeType;
import org.geotools.api.feature.type.FeatureType;
import org.geotools.api.feature.type.GeometryDescriptor;
import org.geotools.api.feature.type.Name;
import org.geotools.api.feature.type.PropertyDescriptor;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.appschema.filter.FilterFactoryImplReportInvalidProperty;
import org.geotools.data.complex.AppSchemaDataAccessRegistry;
import org.geotools.data.complex.AttributeMapping;
import org.geotools.data.complex.FeatureTypeMapping;
import org.geotools.data.complex.FeatureTypeMappingFactory;
import org.geotools.data.complex.NestedAttributeMapping;
import org.geotools.data.complex.expression.FeaturePropertyAccessorFactory;
import org.geotools.data.complex.feature.type.ComplexFeatureTypeFactoryImpl;
import org.geotools.data.complex.feature.type.Types;
import org.geotools.data.complex.filter.XPath;
import org.geotools.data.complex.spi.CustomImplementationsFinder;
import org.geotools.data.complex.spi.CustomSourceDataStore;
import org.geotools.data.complex.util.ComplexFeatureConstants;
import org.geotools.data.complex.util.EmfComplexFeatureReader;
import org.geotools.data.complex.util.XPathUtil.Step;
import org.geotools.data.complex.util.XPathUtil.StepList;
import org.geotools.data.complex.xml.XmlFeatureSource;
import org.geotools.data.joining.JoiningNestedAttributeMapping;
import org.geotools.feature.NameImpl;
import org.geotools.feature.type.AttributeDescriptorImpl;
import org.geotools.feature.type.FeatureTypeFactoryImpl;
import org.geotools.filter.AttributeExpressionImpl;
import org.geotools.filter.expression.PropertyAccessor;
import org.geotools.filter.expression.PropertyAccessorFactory;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.visitor.DuplicatingFilterVisitor;
import org.geotools.jdbc.JDBCFeatureSource;
import org.geotools.jdbc.JDBCFeatureStore;
import org.geotools.jdbc.JoinPropertyName;
import org.geotools.util.URLs;
import org.geotools.util.factory.Hints;
import org.geotools.xml.resolver.SchemaCache;
import org.geotools.xml.resolver.SchemaCatalog;
import org.geotools.xml.resolver.SchemaResolver;
import org.geotools.xsd.SchemaIndex;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * Utility class to create a set of {@linkPlain org.geotools.data.complex.FeatureTypeMapping} objects from a complex
 * datastore's configuration object ( {@link org.geotools.data.complex.config.AppSchemaDataAccessDTO}).
 *
 * @author Gabriel Roldan (Axios Engineering)
 * @author Rini Angreani (CSIRO Earth Science and Resource Engineering)
 * @author Russell Petty (GeoScience Victoria)
 * @since 2.4
 */
public class AppSchemaDataAccessConfigurator {

    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(AppSchemaDataAccessConfigurator.class);

    public static String PROPERTY_JOINING = "app-schema.joining";

    public static String PROPERTY_ENCODE_NESTED_FILTERS = "app-schema.encodeNestedFilters";

    public static final String PROPERTY_REPLACE_OR_UNION = "app-schema.orUnionReplace";

    /** Whether the mapping is for an include. */
    private boolean isInclude = false;

    private AppSchemaDataAccessDTO config;

    private AppSchemaFeatureTypeRegistry typeRegistry;

    private DataAccessMap dataStoreMap;

    private FilterFactory ff = new FilterFactoryImplReportInvalidProperty();

    /** Placeholder for the prefix:namespaceURI mappings declared in the Namespaces section of the mapping file. */
    private NamespaceSupport namespaces;

    private Map<String, String> schemaURIs;

    /** Convenience method for "joining" property. */
    public static boolean isJoining() {
        String s = AppSchemaDataAccessRegistry.getAppSchemaProperties().getProperty(PROPERTY_JOINING);
        return s == null || s.equalsIgnoreCase("true");
    }

    public static boolean isJoiningSet() {
        String s = AppSchemaDataAccessRegistry.getAppSchemaProperties().getProperty(PROPERTY_JOINING);
        return s != null;
    }

    public static boolean isOrUnionReplacementEnabled() {
        final String orUnionReplacement =
                AppSchemaDataAccessRegistry.getAppSchemaProperties().getProperty(PROPERTY_REPLACE_OR_UNION);
        return !"false".equalsIgnoreCase(orUnionReplacement);
    }

    /**
     * Convenience method to check whether native encoding of nested filters is enabled.
     *
     * @return <code>true</code> if native encoding of nested filters is enabled, <code>false</code> otherwise
     */
    public static boolean shouldEncodeNestedFilters() {
        String propValue =
                AppSchemaDataAccessRegistry.getAppSchemaProperties().getProperty(PROPERTY_ENCODE_NESTED_FILTERS);
        return propValue == null || propValue.equalsIgnoreCase("true");
    }

    /** Creates a new ComplexDataStoreConfigurator object. */
    private AppSchemaDataAccessConfigurator(
            AppSchemaDataAccessDTO config, DataAccessMap dataStoreMap, boolean isInclude) {
        this.config = config;
        this.dataStoreMap = dataStoreMap;
        this.isInclude = isInclude;
        namespaces = new NamespaceSupport();
        declareNamespaces(config);
    }

    private void declareNamespaces(AppSchemaDataAccessDTO config) {
        Map nsMap = config.getNamespaces();
        for (Object o : nsMap.entrySet()) {
            Entry entry = (Entry) o;
            String prefix = (String) entry.getKey();
            String namespace = (String) entry.getValue();
            namespaces.declarePrefix(prefix, namespace);
        }
        // check included namespaces
        final Set<String> evaluatedURLs = new HashSet<>();
        config.getIncludes()
                .forEach(filename -> processNamespaces(config.getBaseSchemasUrl(), filename, evaluatedURLs));
    }

    private void processNamespaces(String baseURL, String filename, Set<String> evaluatedURLs) {
        try {
            XMLConfigDigester configReader = new XMLConfigDigester();
            URI baseUri = new URL(baseURL).toURI();
            URL url = baseUri.resolve(filename).toURL();
            if (evaluatedURLs.contains(url.toExternalForm())) return;
            else evaluatedURLs.add(url.toExternalForm());
            AppSchemaDataAccessDTO config = configReader.parse(url);
            for (Entry<String, String> stringStringEntry :
                    config.getNamespaces().entrySet()) {
                Entry entry = stringStringEntry;
                String prefix = (String) entry.getKey();
                String namespace = (String) entry.getValue();
                if (namespaces.getURI(prefix) == null) namespaces.declarePrefix(prefix, namespace);
            }
            if (config.getIncludes() != null && !config.getIncludes().isEmpty()) {
                for (String fname : config.getIncludes()) {
                    processNamespaces(config.getBaseSchemasUrl(), fname, evaluatedURLs);
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Takes a config object and creates a set of mappings.
     *
     * <p>In the process will parse xml schemas to geotools' Feature Model types and descriptors, connect to source
     * datastores and build the mapping objects from source FeatureTypes to the target ones.
     *
     * @return a Set of {@link org.geotools.data.complex.FeatureTypeMapping} source to target FeatureType mapping
     *     definitions
     * @throws IOException if any error occurs while creating the mappings
     */
    public static Set<FeatureTypeMapping> buildMappings(AppSchemaDataAccessDTO config, boolean isInclude)
            throws IOException {
        return buildMappings(config, new DataAccessMap(), isInclude);
    }

    /**
     * Takes a config object and creates a set of mappings, assumes the mappings are not from includes
     *
     * @param config the configuration object
     * @return a Set of {@link org.geotools.data.complex.FeatureTypeMapping} source to target
     * @throws IOException if any error occurs while creating the mappings
     */
    public static Set<FeatureTypeMapping> buildMappings(AppSchemaDataAccessDTO config) throws IOException {
        return buildMappings(config, new DataAccessMap(), false);
    }

    /**
     * This method will not create a source data store if an equivalent one (i.e. same configuration parameters) is
     * found in the provided <code>sourceDataStoreMap</code>.
     *
     * @see AppSchemaDataAccessConfigurator#buildMappings(AppSchemaDataAccessDTO)
     * @param sourceDataStoreMap map holding the source data stores created so far, e.g. while parsing App-Schema
     *     configuration files included by the one currently being processed
     * @param isInclude whether the mappings are for an include
     */
    public static Set<FeatureTypeMapping> buildMappings(
            AppSchemaDataAccessDTO config, DataAccessMap sourceDataStoreMap, boolean isInclude) throws IOException {

        AppSchemaDataAccessConfigurator mappingsBuilder =
                new AppSchemaDataAccessConfigurator(config, sourceDataStoreMap, isInclude);
        Set<FeatureTypeMapping> mappingObjects = mappingsBuilder.buildMappings();

        return mappingObjects;
    }

    /**
     * Actually builds the mappings from the config dto.
     *
     * <p>Build steps are: - parse xml schemas to FM types - connect to source datastores - build mappings
     */
    private Set<FeatureTypeMapping> buildMappings() throws IOException {
        // -parse target xml schemas, let parsed types on <code>registry</code>
        try {
            parseGmlSchemas();
            Map<String, DataAccess<FeatureType, Feature>> sourceDataStores = null;
            Set<FeatureTypeMapping> featureTypeMappings = null;
            try {
                // -create source datastores
                sourceDataStores = acquireSourceDatastores();
                // -create FeatureType mappings
                featureTypeMappings = createFeatureTypeMappings(sourceDataStores, isInclude);
                return featureTypeMappings;
            } finally {
                disposeUnusedSourceDataStores(sourceDataStores, featureTypeMappings);
            }
        } finally {
            if (typeRegistry != null) {
                typeRegistry.disposeSchemaIndexes();
                typeRegistry = null;
            }
        }
    }

    /** Ensure any source data stores not used in a mapping are disposed. */
    private void disposeUnusedSourceDataStores(
            Map<String, DataAccess<FeatureType, Feature>> sourceDataStores,
            Set<FeatureTypeMapping> featureTypeMappings) {
        if (sourceDataStores == null) return;

        if (featureTypeMappings == null) {
            for (DataAccess<FeatureType, Feature> dataAccess : sourceDataStores.values()) {
                dataAccess.dispose();
            }
        } else {
            for (DataAccess<FeatureType, Feature> dataAccess : sourceDataStores.values()) {
                boolean usedDataAccess = false;
                for (FeatureTypeMapping mapping : featureTypeMappings) {
                    if (mapping.getSource().getDataStore() == dataAccess
                            || mapping.getIndexSource() != null
                                    && Objects.equals(mapping.getIndexSource().getDataStore(), dataAccess)) {
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
            Map<String, DataAccess<FeatureType, Feature>> sourceDataStores, boolean isInclude) throws IOException {
        Set mappingsConfigs = config.getTypeMappings();

        Set<FeatureTypeMapping> featureTypeMappings = new HashSet<>();

        for (Object mappingsConfig : mappingsConfigs) {
            TypeMapping dto = (TypeMapping) mappingsConfig;
            try {
                FeatureSource<? extends FeatureType, ? extends Feature> featureSource =
                        getFeatureSource(dto, sourceDataStores);
                // get CRS from underlying feature source and pass it on
                CoordinateReferenceSystem crs;
                try {
                    crs = featureSource.getSchema().getCoordinateReferenceSystem();
                } catch (UnsupportedOperationException e) {
                    // web service back end doesn't support getSchema
                    crs = null;
                }
                AttributeDescriptor target = getTargetDescriptor(dto, crs);

                // set original schema locations for encoding
                target.getType().getUserData().put("schemaURI", schemaURIs);

                // set mappings namespaces
                target.getType().getUserData().put(Types.DECLARED_NAMESPACES_MAP, getNamespacesMap());

                boolean isDatabaseBackend =
                        featureSource instanceof JDBCFeatureSource || featureSource instanceof JDBCFeatureStore;

                List<AttributeMapping> attMappings = getAttributeMappings(
                        target, dto.getAttributeMappings(), dto.getItemXpath(), crs, isDatabaseBackend);

                // if an external index (e.g. Solr) is used in the mappings, get its data store
                FeatureSource<SimpleFeatureType, SimpleFeature> indexFeatureSource =
                        getIndexFeatureSource(dto, sourceDataStores);

                FeatureTypeMapping mapping = FeatureTypeMappingFactory.getInstance(
                        featureSource,
                        indexFeatureSource,
                        target,
                        dto.getDefaultGeometryXPath(),
                        attMappings,
                        namespaces,
                        dto.getItemXpath(),
                        dto.isXmlDataStore(),
                        dto.isDenormalised(),
                        dto.getSourceDataStore());

                String mappingName = dto.getMappingName();
                if (mappingName != null) {
                    mapping.setName(Types.degloseName(mappingName, namespaces));
                }
                // indicate whether the mapping is from an include
                mapping.setInclude(isInclude);
                featureTypeMappings.add(mapping);
            } catch (Exception e) {
                LOGGER.warning("Error creating app-schema data store for '"
                        + (dto.getMappingName() != null ? dto.getMappingName() : dto.getTargetElementName())
                        + "', caused by: "
                        + e.getMessage());
                throw new IOException(e);
            }
        }
        return featureTypeMappings;
    }

    private AttributeDescriptor getTargetDescriptor(TypeMapping dto, CoordinateReferenceSystem crs) throws IOException {
        String prefixedTargetName = dto.getTargetElementName();
        Name targetNodeName = Types.degloseName(prefixedTargetName, namespaces);

        AttributeDescriptor targetDescriptor = typeRegistry.getDescriptor(targetNodeName, crs);
        if (targetDescriptor == null) {
            throw new NoSuchElementException("descriptor " + targetNodeName + " not found in parsed schema");
        }

        // check if default geometry was set in FeatureTypeMapping
        // NOTE: if a default geometry is already set, it will be overridden
        String defaultGeomXPath = dto.getDefaultGeometryXPath();
        if (defaultGeomXPath != null && !defaultGeomXPath.isEmpty()) {
            targetDescriptor = retypeAddingDefaultGeometry(targetDescriptor, defaultGeomXPath);
        }

        return targetDescriptor;
    }

    private AttributeDescriptor retypeAddingDefaultGeometry(AttributeDescriptor descriptor, String defaultGeomXPath) {
        AttributeDescriptor newDescriptor = descriptor;
        FeatureType type = (FeatureType) descriptor.getType();

        GeometryDescriptor geom = getDefaultGeometryDescriptor(type, defaultGeomXPath);
        if (geom != null) {
            FeatureTypeFactoryImpl ftf = new ComplexFeatureTypeFactoryImpl();

            String defGeomNamespace = type.getName().getNamespaceURI();
            String defGeomLocalName = ComplexFeatureConstants.DEFAULT_GEOMETRY_LOCAL_NAME;
            GeometryDescriptor defGeom = ftf.createGeometryDescriptor(
                    geom.getType(),
                    new NameImpl(defGeomNamespace, defGeomLocalName),
                    geom.getMinOccurs(),
                    geom.getMaxOccurs(),
                    geom.isNillable(),
                    geom.getDefaultValue());

            Collection<PropertyDescriptor> descriptors = new HashSet<>(type.getDescriptors());
            descriptors.add(defGeom);

            FeatureType newType = ftf.createFeatureType(
                    type.getName(),
                    descriptors,
                    defGeom,
                    type.isAbstract(),
                    type.getRestrictions(),
                    type.getSuper(),
                    type.getDescription());
            newType.getUserData().putAll(type.getUserData());

            newDescriptor = new AttributeDescriptorImpl(
                    newType,
                    descriptor.getName(),
                    descriptor.getMinOccurs(),
                    descriptor.getMaxOccurs(),
                    descriptor.isNillable(),
                    descriptor.getDefaultValue());
            newDescriptor.getUserData().putAll(descriptor.getUserData());
        } else {
            throw new IllegalArgumentException(String.format(
                    "Default geometry descriptor could not be found for type \"%s\" at x-path \"%s\"",
                    descriptor.getName().toString(), defaultGeomXPath));
        }

        return newDescriptor;
    }

    private GeometryDescriptor getDefaultGeometryDescriptor(FeatureType featureType, String xpath) {
        // directly instantiating the factory I need instead of scanning the registry improves perf.
        FeaturePropertyAccessorFactory accessorFactory = new FeaturePropertyAccessorFactory();
        Hints hints = new Hints(PropertyAccessorFactory.NAMESPACE_CONTEXT, namespaces);
        PropertyAccessor accessor =
                accessorFactory.createPropertyAccessor(featureType.getClass(), xpath, GeometryAttribute.class, hints);

        GeometryDescriptor geom = null;
        if (accessor != null) {
            try {
                geom = accessor.get(featureType, xpath, GeometryDescriptor.class);
            } catch (Exception e) {
                LOGGER.finest(String.format(
                        "Exception occurred retrieving geometry descriptor " + "at x-path \"%s\": %s",
                        xpath, e.getMessage()));
            }
        }

        return geom;
    }

    /**
     * Creates a list of {@link org.geotools.data.complex.AttributeMapping} from the attribute mapping configurations in
     * the provided list of {@link AttributeMapping}
     */
    private List<AttributeMapping> getAttributeMappings(
            final AttributeDescriptor root,
            final List attDtos,
            String itemXpath,
            CoordinateReferenceSystem crs,
            boolean isJDBC)
            throws IOException {
        List<AttributeMapping> attMappings = new LinkedList<>();

        /** Label and parent label are specific for web service backend */
        for (Object dto : attDtos) {

            org.geotools.data.complex.config.AttributeMapping attDto =
                    (org.geotools.data.complex.config.AttributeMapping) dto;

            String idExpr = attDto.getIdentifierExpression();
            String idXpath = null;
            if (idExpr == null) {
                // this might be because it's an XPath expression
                idXpath = attDto.getIdentifierPath();
                if (idXpath != null) {
                    // validate without indexed elements
                    final StepList inputXPathSteps = XPath.steps(root, itemXpath + "/" + idXpath, namespaces);
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

            final Expression idExpression = idXpath == null
                    ? parseOgcCqlExpression(idExpr)
                    : new AttributeExpressionImpl(
                            idXpath, new Hints(FeaturePropertyAccessorFactory.NAMESPACE_CONTEXT, this.namespaces));
            // if the data source is a data access, the input XPath expression is the source
            // expression

            final Expression sourceExpression = inputXPath == null
                    ? parseOgcCqlExpression(sourceExpr)
                    : new AttributeExpressionImpl(
                            inputXPath, new Hints(FeaturePropertyAccessorFactory.NAMESPACE_CONTEXT, this.namespaces));

            final AttributeType expectedInstanceOf;

            final Map<Name, Expression> clientProperties = getClientProperties(attDto);

            if (expectedInstanceTypeName != null) {
                Name expectedNodeTypeName = Types.degloseName(expectedInstanceTypeName, namespaces);
                expectedInstanceOf = typeRegistry.getAttributeType(expectedNodeTypeName, null, crs);
                if (expectedInstanceOf == null) {
                    String msg = "mapping expects and instance of "
                            + expectedNodeTypeName
                            + " for attribute "
                            + targetXPath
                            + " but the attribute descriptor was not found";
                    throw new DataSourceException(msg);
                }
            } else {
                expectedInstanceOf = null;
            }
            AttributeMapping attMapping;
            String sourceElement = attDto.getLinkElement();
            if (sourceElement != null) {
                // nested complex attributes, this could be a function expression for polymorphic
                // types
                Expression elementExpr = parseOgcCqlExpression(sourceElement);
                String sourceField = attDto.getLinkField();
                StepList sourceFieldSteps = null;
                if (sourceField != null) {
                    // it could be null for polymorphism mapping,
                    // i.e. when the linked element maps to the same table as the container mapping
                    sourceFieldSteps = XPath.steps(root, sourceField, namespaces);
                }
                // a nested feature
                NestedAttributeMapping customNestedMapping = CustomImplementationsFinder.find(
                        this,
                        idExpression,
                        sourceExpression,
                        targetXPathSteps,
                        isMultiValued,
                        clientProperties,
                        elementExpr,
                        sourceFieldSteps,
                        namespaces);
                if (customNestedMapping != null) {
                    attMapping = customNestedMapping;
                } else if (isJoining() && isJDBC) {
                    attMapping = new JoiningNestedAttributeMapping(
                            idExpression,
                            sourceExpression,
                            targetXPathSteps,
                            isMultiValued,
                            clientProperties,
                            elementExpr,
                            sourceFieldSteps,
                            namespaces);
                } else {
                    attMapping = new NestedAttributeMapping(
                            idExpression,
                            sourceExpression,
                            targetXPathSteps,
                            isMultiValued,
                            clientProperties,
                            elementExpr,
                            sourceFieldSteps,
                            namespaces);
                }

            } else {
                attMapping = new AttributeMapping(
                        idExpression,
                        sourceExpression,
                        attDto.getSourceIndex(),
                        targetXPathSteps,
                        expectedInstanceOf,
                        isMultiValued,
                        clientProperties,
                        attDto.getMultipleValue());
            }

            if (attDto.isList()) {
                attMapping.setList(true);
            }

            if (attDto.encodeIfEmpty()) {
                attMapping.setEncodeIfEmpty(true);
            }

            /** Label and parent label are specific for web service backend */
            if (attDto.getLabel() != null) {
                attMapping.setLabel(attDto.getLabel());
            }
            if (attDto.getParentLabel() != null) {
                attMapping.setParentLabel(attDto.getParentLabel());
            }
            if (attDto.getInstancePath() != null) {
                attMapping.setInstanceXpath(attDto.getInstancePath());
            }

            // sets the external index (e.g. Solr) field for the current attribute mapping
            // the value will be NULL if no external index is being used
            attMapping.setIndexField(attDto.getIndexField());

            attMappings.add(attMapping);
        }
        return attMappings;
    }

    /**
     * Throws an IllegalArgumentException if some Step in the given xpath StepList has a prefix for which no prefix to
     * namespace mapping were provided (as in the Namespaces section of the mappings xml configuration file)
     */
    private void validateConfiguredNamespaces(StepList targetXPathSteps) {
        for (Step step : targetXPathSteps) {
            QName name = step.getName();
            if (!XMLConstants.DEFAULT_NS_PREFIX.equals(name.getPrefix())) {
                if (XMLConstants.DEFAULT_NS_PREFIX.equals(name.getNamespaceURI())) {
                    throw new IllegalArgumentException("location step "
                            + step
                            + " has prefix "
                            + name.getPrefix()
                            + " for which no namespace was set. "
                            + "(Check the Namespaces section in the config file)");
                }
            }
        }
    }

    private Expression parseOgcCqlExpression(String sourceExpr) throws DataSourceException {
        return parseOgcCqlExpression(sourceExpr, ff);
    }

    /**
     * Utility method that parses an CQL expression from its text representation. If the provided expression test
     * representation is NULL, {@link Expression.NIL} is returned.
     *
     * @param sourceExpr the expression in its text representation
     * @param ff filter factory used to build the final expression
     * @return the parsed expression
     * @throws DataSourceException if the expression text representation could not be parsed
     */
    public static Expression parseOgcCqlExpression(String sourceExpr, FilterFactory ff) throws DataSourceException {
        Expression expression = Expression.NIL;
        if (sourceExpr != null && !sourceExpr.trim().isEmpty()) {
            try {
                expression = CQL.toExpression(sourceExpr, ff);
            } catch (CQLException e) {
                String formattedErrorMessage = e.getMessage();
                AppSchemaDataAccessConfigurator.LOGGER.log(Level.SEVERE, formattedErrorMessage, e);
                throw new DataSourceException(
                        "Error parsing CQL expression " + sourceExpr + ":\n" + formattedErrorMessage);
            } catch (Exception e) {
                java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
                String msg = "parsing expression " + sourceExpr;
                AppSchemaDataAccessConfigurator.LOGGER.log(Level.SEVERE, msg, e);
                throw new DataSourceException(msg + ": " + e.getMessage(), e);
            }
        }
        return expression;
    }

    /** @return Map&lt;Name, Expression&gt; with the values per qualified name (attribute name in the mapping) */
    private Map<Name, Expression> getClientProperties(org.geotools.data.complex.config.AttributeMapping dto)
            throws DataSourceException {
        final Map<Name, Expression> clientProperties = new HashMap<>();

        if (!dto.getClientProperties().isEmpty()) {
            for (Entry<String, String> stringStringEntry :
                    dto.getClientProperties().entrySet()) {
                Entry entry = stringStringEntry;
                String name = (String) entry.getKey();
                Name qName = Types.degloseName(name, namespaces);
                String cqlExpression = (String) entry.getValue();
                Expression expression = parseOgcCqlExpression(cqlExpression);
                Expression fixedExpr = fixClientPropertyIfJDBCMultipleValueIsPresent(dto, expression);
                clientProperties.put(qName, fixedExpr);
            }
        }
        // add anonymous attributes
        addAnonymousAttributes(dto, clientProperties);

        return clientProperties;
    }

    // fix the PropertyName of a ClientProperty specified for an AttributeMapping
    // that has JDBCMultipleValueDirective
    private Expression fixClientPropertyIfJDBCMultipleValueIsPresent(
            org.geotools.data.complex.config.AttributeMapping dto, Expression clientPropertyExpr) {
        MultipleValue multipleValue = dto.getMultipleValue();
        if (multipleValue instanceof JdbcMultipleValue) {
            JdbcMultipleValue jdbcMultipleValue = (JdbcMultipleValue) multipleValue;
            DuplicatingFilterVisitor duplicatingFilterVisitor = new DuplicatingFilterVisitor() {
                @Override
                public Object visit(PropertyName propertyName, Object extraData) {
                    // create a JoinPropertyName out of the PropertyName
                    // using the JDBCMultipleValue id as an alias.
                    // the same alias is being used by the JoiningJDBCFeatureSource
                    // when joining the targetTable
                    JoinPropertyName joinPn =
                            new JoinPropertyName(null, jdbcMultipleValue.getId(), propertyName.getPropertyName());
                    joinPn.namespaceSupport = propertyName.getNamespaceContext();
                    return joinPn;
                }
            };
            clientPropertyExpr = (Expression) clientPropertyExpr.accept(duplicatingFilterVisitor, null);
        }
        return clientPropertyExpr;
    }

    private void addAnonymousAttributes(
            org.geotools.data.complex.config.AttributeMapping dto, final Map<Name, Expression> clientProperties)
            throws DataSourceException {
        for (Map.Entry<String, String> entry : dto.getAnonymousAttributes().entrySet()) {
            Name qname = Types.degloseName(entry.getKey(), namespaces);
            ComplexNameImpl complexName = new ComplexNameImpl(qname.getNamespaceURI(), qname.getLocalPart(), true);
            Expression expression = parseOgcCqlExpression(entry.getValue());
            clientProperties.put(complexName, expression);
        }
    }

    private FeatureSource<? extends FeatureType, ? extends Feature> getFeatureSource(
            TypeMapping dto, Map<String, DataAccess<FeatureType, Feature>> sourceDataStores) throws IOException {
        String dsId = dto.getSourceDataStore();
        String typeName = dto.getSourceTypeName();

        DataAccess<FeatureType, Feature> sourceDataStore = sourceDataStores.get(dsId);
        if (sourceDataStore == null) {
            throw new DataSourceException("datastore " + dsId + " not found for type mapping " + dto);
        }

        AppSchemaDataAccessConfigurator.LOGGER.fine(
                "asking datastore " + sourceDataStore + " for source type " + typeName);
        Name name = Types.degloseName(typeName, namespaces);
        FeatureSource<? extends FeatureType, ? extends Feature> fSource = sourceDataStore.getFeatureSource(name);
        if (fSource instanceof XmlFeatureSource) {
            ((XmlFeatureSource) fSource).setNamespaces(namespaces);
        }
        AppSchemaDataAccessConfigurator.LOGGER.fine("found feature source for " + typeName);
        return fSource;
    }

    /**
     * Parses the target xml schema files and stores the generated types in {@link #typeRegistry} and
     * AttributeDescriptors in {@link #descriptorRegistry}.
     *
     * <p>The list of file names to parse is obtained from config.getTargetSchemasUris(). If a file name contained in
     * that list is a relative path (i.e., does not starts with file: or http:, config.getBaseSchemasUrl() is used to
     * resolve relative paths against.
     */
    private void parseGmlSchemas() throws IOException {
        AppSchemaDataAccessConfigurator.LOGGER.finer("about to parse target schemas");

        final URL baseUrl = new URL(config.getBaseSchemasUrl());

        final List schemaFiles = config.getTargetSchemasUris();

        EmfComplexFeatureReader schemaParser = EmfComplexFeatureReader.newInstance();
        schemaParser.setResolver(buildResolver());

        // create a single type registry for all the schemas in the config
        typeRegistry = new AppSchemaFeatureTypeRegistry(namespaces);

        schemaURIs = new HashMap<>(schemaFiles.size());
        String nameSpace;
        String schemaLocation;
        for (Object schemaFile : schemaFiles) {
            schemaLocation = (String) schemaFile;
            final URL schemaUrl = resolveResourceLocation(baseUrl, schemaLocation);
            AppSchemaDataAccessConfigurator.LOGGER.fine("parsing schema " + schemaUrl.toExternalForm());

            nameSpace = schemaParser.findSchemaNamespace(schemaUrl);
            schemaLocation = schemaUrl.toExternalForm();
            schemaURIs.put(nameSpace, schemaLocation);

            SchemaIndex schemaIndex = schemaParser.parse(nameSpace, schemaLocation);
            // add the resolved EMF schema so typeRegistry can find the needed type tree when it's
            // asked for the mapped FeatureType
            typeRegistry.addSchemas(schemaIndex);
        }
    }

    /** Build the catalog for this data access. */
    private SchemaCatalog buildCatalog() {
        String catalogLocation = config.getCatalog();
        if (catalogLocation == null) {
            return null;
        } else {
            URL baseUrl;
            try {
                baseUrl = new URL(config.getBaseSchemasUrl());
                URL resolvedCatalogLocation = resolveResourceLocation(baseUrl, catalogLocation);
                return SchemaCatalog.build(resolvedCatalogLocation);
            } catch (MalformedURLException e) {
                LOGGER.warning("Malformed URL encountered while setting OASIS catalog location. "
                        + "Mapping file URL: "
                        + config.getBaseSchemasUrl()
                        + " Catalog location: "
                        + config.getCatalog()
                        + " Detail: "
                        + e.getMessage());
                return null;
            }
        }
    }

    /** Build the cache for this data access. */
    private SchemaCache buildCache() {
        try {
            return SchemaCache.buildAutomaticallyConfiguredUsingFileUrl(new URL(config.getBaseSchemasUrl()));
        } catch (MalformedURLException e) {
            LOGGER.warning("Malformed mapping file URL: " + config.getBaseSchemasUrl() + " Detail: " + e.getMessage());
            return null;
        }
    }

    /** Build the resolver (catalog plus cache) for this data access. */
    private SchemaResolver buildResolver() {
        return new SchemaResolver(buildCatalog(), buildCache());
    }

    private URL resolveResourceLocation(final URL baseUrl, String schemaLocation) throws MalformedURLException {
        final URL schemaUrl;
        if (schemaLocation.startsWith("file:") || schemaLocation.startsWith("http:")) {
            AppSchemaDataAccessConfigurator.LOGGER.fine("using resource location as absolute path: " + schemaLocation);
            schemaUrl = new URL(schemaLocation);
        } else {
            if (baseUrl == null) {
                schemaUrl = new URL(schemaLocation);
                AppSchemaDataAccessConfigurator.LOGGER.warning("base url not provided, may be unable to locate"
                        + schemaLocation
                        + ". Path resolved to: "
                        + schemaUrl.toExternalForm());
            } else {
                AppSchemaDataAccessConfigurator.LOGGER.fine(
                        "using schema location " + schemaLocation + " as relative to " + baseUrl);
                schemaUrl = new URL(baseUrl, schemaLocation);
            }
        }
        return schemaUrl;
    }

    /** @return a Map&lt;String,DataStore&gt; where the key is the id given to the datastore in the configuration. */
    private Map<String, DataAccess<FeatureType, Feature>> acquireSourceDatastores() throws IOException {
        AppSchemaDataAccessConfigurator.LOGGER.entering(getClass().getName(), "acquireSourceDatastores");

        final Map<String, DataAccess<FeatureType, Feature>> datastores = new LinkedHashMap<>();
        final List<SourceDataStore> dsParams = config.getSourceDataStores();
        String id;

        for (SourceDataStore dsconfig : dsParams) {
            id = dsconfig.getId();

            Map<String, Serializable> datastoreParams = dsconfig.getParams();

            datastoreParams = resolveRelativePaths(datastoreParams);

            AppSchemaDataAccessConfigurator.LOGGER.fine("looking for datastore " + id);

            DataAccess<FeatureType, Feature> dataStore = null;
            if (dataStoreMap != null) {
                if (dataStoreMap.containsKey(datastoreParams)) {
                    dataStore = dataStoreMap.get(datastoreParams);
                } else {
                    // let's check if any data store provided a custom syntax for its configuration
                    List<CustomSourceDataStore> extensions = CustomSourceDataStore.loadExtensions();
                    dataStore = buildDataStore(extensions, dsconfig, config);
                    // if no custom data store handled this configuration let's fallback on the
                    // default
                    // constructor
                    dataStore = dataStore == null ? DataAccessFinder.getDataStore(datastoreParams) : dataStore;
                    // store the store in the data stores map
                    dataStoreMap.put(datastoreParams, dataStore);
                }
            }

            if (dataStore == null) {
                AppSchemaDataAccessConfigurator.LOGGER.log(
                        Level.SEVERE, "Cannot find a DataAccess for parameters " + datastoreParams);
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
     * Iterate over all the custom data store extensions to check if anyone can \ wants to build a data store based on
     * the provided configuration. The first build data store is returned, if no data store is build NULL is returned.
     *
     * @param extensions the custom data stores extensions
     * @param dataStoreConfig the data store configuration
     * @param appSchemaConfig the full mappings file configuration
     * @return the store built or NULL
     */
    @SuppressWarnings("unchecked")
    private DataAccess<FeatureType, Feature> buildDataStore(
            List<CustomSourceDataStore> extensions,
            SourceDataStore dataStoreConfig,
            AppSchemaDataAccessDTO appSchemaConfig) {
        for (CustomSourceDataStore extension : extensions) {
            // let's see if this extension wants to build a store
            DataAccess<? extends FeatureType, ? extends Feature> dataStore =
                    extension.buildDataStore(dataStoreConfig, appSchemaConfig);
            if (dataStore != null) {
                // we have a store, we are done
                return (DataAccess<FeatureType, Feature>) dataStore;
            }
        }
        return null;
    }

    /**
     * Database connection parameters that are probably safe to report to the end user. (Things we can be pretty sure
     * are not passwords.)
     */
    private static final List<String> SAFE_DATASTORE_PARAMS = List.of(
            "url", // shapefile
            "directory", // propertyfile
            "namespace", // just about everything
            "dbtype", // jdbc
            "jndiReferenceName", // jdni
            // these are all various jdbc options
            "host",
            "port",
            "database",
            "schema",
            "user");

    /**
     * Return datastore params filtered to include only known-safe parameters. We cannot try to find passwords, because
     * even dbtype could be misspelled.
     */
    @SuppressWarnings("unchecked")
    private Map filterDatastoreParams(Map datastoreParams) {
        Map filteredDatastoreParams = new LinkedHashMap<>();
        for (String key : SAFE_DATASTORE_PARAMS) {
            if (datastoreParams.containsKey(key)) {
                filteredDatastoreParams.put(key, datastoreParams.get(key));
            }
        }
        return filteredDatastoreParams;
    }

    /**
     * Resolves any source datastore parameter settled as a file path relative to the location of the xml mappings
     * configuration file as an absolute path and returns a new Map with it.
     *
     * @return parameter map with resolved file url
     */
    private Map<String, Serializable> resolveRelativePaths(final Map<String, Serializable> datastoreParams) {
        Map<String, Serializable> resolvedParams = new HashMap<>();

        AppSchemaDataAccessConfigurator.LOGGER.entering(getClass().getName(), "resolveRelativePaths");
        for (Map.Entry<String, Serializable> entry : datastoreParams.entrySet()) {
            String key = entry.getKey();
            String value = (String) entry.getValue();

            if (value != null && value.startsWith("file:")) {
                // a parameter prefix of "file:" is the only case that will be resolved
                // any file paths entered without this prefix will remain unchanged
                String oldValue = value;
                String resolvedDataPath = null;
                String inputDataPath = value.substring("file:".length());
                File f = new File(inputDataPath);

                if (!f.isAbsolute()) {
                    AppSchemaDataAccessConfigurator.LOGGER.fine(
                            "resolving original parameter " + value + " for datastore parameter " + key);
                    try {
                        // use of URL here should be safe as the base schema url should
                        // not yet have undergone any conversion to file or
                        // encoding/decoding
                        URL mappingFileUrl = new URL(config.getBaseSchemasUrl());
                        AppSchemaDataAccessConfigurator.LOGGER.finer(
                                "mapping file URL is " + mappingFileUrl.toString());

                        String mappingFileDirPath =
                                URLs.urlToFile(mappingFileUrl).getParent();
                        AppSchemaDataAccessConfigurator.LOGGER.finer(
                                "mapping file parent directory is " + mappingFileDirPath);

                        // FilenameUtils.concat handles a number of system-dependent issues here
                        // but it might be better to add this method to DataUtilities
                        resolvedDataPath = FilenameUtils.concat(mappingFileDirPath, inputDataPath);
                        if (resolvedDataPath == null) {
                            throw new RuntimeException("Relative path to datastore is incompatible with"
                                    + " the base path - check double dot steps.");
                        }
                    } catch (Exception e) {
                        AppSchemaDataAccessConfigurator.LOGGER.throwing(
                                getClass().getName(), "resolveRelativePaths", e);
                        throw new RuntimeException(e);
                    }
                } else {
                    resolvedDataPath = inputDataPath;
                }
                AppSchemaDataAccessConfigurator.LOGGER.finer("Path to data has been resolved to " + resolvedDataPath);

                /*
                 * Shapefile expects the protocol "file:" at the beginning of the parameter value,
                 * other file-based datastores do not. We can distinguish shapefiles from other
                 * cases because the key is "url" and as of 2010-09-25 no other file-based datastore
                 * uses this key (properties files use the key "directory"). If a new file-based
                 * datastore is created, everything will work fine provided the key "url" is used if
                 * and only if the datastore expects a parameter value starting with "file:"
                 */
                if ("url".equals(key) || key.startsWith("WSDataStoreFactory")) {
                    value = "file:" + resolvedDataPath;
                } else {
                    value = resolvedDataPath;
                }
                AppSchemaDataAccessConfigurator.LOGGER.fine("Resolved " + oldValue + " -> " + value);
            }
            resolvedParams.put(key, value);
        }
        return resolvedParams;
    }

    /**
     * If an external index (e.g. Solr) is used by the provided feature type mapping, this method will retrieve the
     * corresponding data store definition, otherwise NULL will be returned. If the data source cannot be found an
     * exception will be throw.
     */
    private FeatureSource<SimpleFeatureType, SimpleFeature> getIndexFeatureSource(
            TypeMapping dto, Map<String, DataAccess<FeatureType, Feature>> sourceDataStores) throws IOException {
        String dsId = dto.getIndexDataStore();
        String typeName = dto.getIndexTypeName();

        // let's check if an external index (e.g. Solr) was configured
        if (StringUtils.isEmpty(dsId) || StringUtils.isEmpty(typeName)) return null;

        DataAccess sourceDataStore = sourceDataStores.get(dsId);
        if (sourceDataStore == null) {
            throw new DataSourceException("datastore " + dsId + " not found for type mapping " + dto);
        }

        Name name = Types.degloseName(typeName, namespaces);
        SimpleFeatureSource fSource = (SimpleFeatureSource) sourceDataStore.getFeatureSource(name);
        if (fSource == null) {
            throw new RuntimeException("Feature source not found '" + typeName + "'.");
        }
        if (fSource instanceof XmlFeatureSource) {
            ((XmlFeatureSource) fSource).setNamespaces(namespaces);
        }
        return fSource;
    }

    private Map<String, String> getNamespacesMap() {
        final Map<String, String> namespacesMap = new HashMap<>();
        final Enumeration prefixes = namespaces.getPrefixes();
        while (prefixes.hasMoreElements()) {
            final String prefix = (String) prefixes.nextElement();
            final String uri = namespaces.getURI(prefix);
            namespacesMap.put(prefix, uri);
        }
        return namespacesMap;
    }

    /** Name implementation capable of store more information about the attribute/element represented. */
    public static class ComplexNameImpl extends NameImpl {

        private boolean isNestedElement;

        public ComplexNameImpl(String namespace, String local, boolean isNestedElement) {
            super(namespace, local);
            this.isNestedElement = isNestedElement;
        }

        @Override
        public String getLocalPart() {
            return super.getLocalPart();
        }

        @Override
        public String getNamespaceURI() {
            return super.getNamespaceURI();
        }

        /** Returns true if represented Name is a nested element instead an attribute. */
        public boolean isNestedElement() {
            return isNestedElement;
        }
    }
}
