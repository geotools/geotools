/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2013 - 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic.catalog.index;

import java.io.File;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import org.geotools.coverage.util.CoverageUtilities;
import org.geotools.gce.imagemosaic.Utils;
import org.geotools.gce.imagemosaic.catalog.index.Indexer.Collectors;
import org.geotools.gce.imagemosaic.catalog.index.Indexer.Collectors.Collector;
import org.geotools.gce.imagemosaic.catalog.index.Indexer.Coverages;
import org.geotools.gce.imagemosaic.catalog.index.Indexer.Coverages.Coverage;
import org.geotools.gce.imagemosaic.catalog.index.ParametersType.Parameter;
import org.geotools.util.URLs;
import org.geotools.util.Utilities;

public class IndexerUtils {

    public static final String INDEXER_XML = "indexer.xml";

    public static final String INDEXER_PROPERTIES = "indexer.properties";

    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(IndexerUtils.class);

    /**
     * Build {@link Collectors} element by parsing the specified propertyCollectors, and put them on
     * the specified indexer object.
     */
    public static void setPropertyCollectors(Indexer indexer, String propertyCollectors) {
        final Collectors collectors = Utils.OBJECT_FACTORY.createIndexerCollectors();
        indexer.setCollectors(collectors);
        final List<Collector> collectorList = collectors.getCollector();
        propertyCollectors = propertyCollectors.trim();
        if (propertyCollectors != null && propertyCollectors.length() > 0) {
            final String[] propColls = propertyCollectors.split(",");
            for (String pcDef : propColls) {

                // parse this def as NAME[CONFIG_FILE](PROPERTY;PROPERTY;....;PROPERTY)
                final int squareLPos = pcDef.indexOf("[");
                final int squareRPos = pcDef.indexOf("]");
                final int roundLPos = pcDef.indexOf("(");
                final int roundRPos = pcDef.indexOf(")");
                final int roundRPosLast = pcDef.lastIndexOf(")");

                if (roundRPos != roundRPosLast) {
                    if (LOGGER.isLoggable(Level.INFO)) {
                        LOGGER.info("Skipping unparseable PropertyCollector definition: " + pcDef);
                    }
                    continue;
                }
                if (roundLPos == -1 || roundRPos == -1) {
                    if (LOGGER.isLoggable(Level.INFO)) {
                        LOGGER.info("Skipping unparseable PropertyCollector definition: " + pcDef);
                    }
                    continue;
                }
                if (roundLPos == 0) {
                    if (LOGGER.isLoggable(Level.INFO)) {
                        LOGGER.info("Skipping unparseable PropertyCollector definition: " + pcDef);
                    }
                    continue;
                }
                if (roundRPos != (pcDef.length() - 1)) { // end with )
                    if (LOGGER.isLoggable(Level.INFO)) {
                        LOGGER.info("Skipping unparseable PropertyCollector definition: " + pcDef);
                    }
                    continue;
                }

                // name
                String spi = null;
                if (squareLPos != -1) {
                    spi = pcDef.substring(0, squareLPos);
                } else {
                    spi = pcDef.substring(0, roundLPos);
                }

                // config
                final String config =
                        squareLPos < squareRPos ? pcDef.substring(squareLPos + 1, squareRPos) : "";

                String value = null;
                // only need config if provided, some property collectors don't need it
                if (config.length() > 0) {
                    final File configFile =
                            new File(
                                    getParameter(Utils.Prop.ROOT_MOSAIC_DIR, indexer),
                                    config + ".properties");

                    if (!Utils.checkFileReadable(configFile)) {
                        if (LOGGER.isLoggable(Level.INFO)) {
                            LOGGER.info(
                                    "Unable to access the file for this PropertyCollector: "
                                            + configFile.getAbsolutePath());
                        }
                    } else {
                        final Properties properties =
                                CoverageUtilities.loadPropertiesFromURL(URLs.fileToUrl(configFile));
                        if (properties.containsKey("regex")) {
                            value = properties.getProperty("regex");
                        }
                    }
                }

                // property names
                final String propertyNames[] = pcDef.substring(roundLPos + 1, roundRPos).split(",");
                Collector collector = Utils.OBJECT_FACTORY.createIndexerCollectorsCollector();
                collector.setSpi(spi);

                // only 1 propertyCollector for property
                collector.setMapped(propertyNames[0]);
                collector.setValue(value);
                collectorList.add(collector);
            }
        }
    }

    /**
     * Return an indexer {@link Coverage} object from the specified {@link Indexer}, referring to
     * the requested name.
     *
     * @param indexer the main {@link Indexer} instance
     * @param name the name of the {@link Coverage} element to be retrieved.
     * @return the {@link Coverage} element for the specified name (if any).
     */
    public static Coverage getCoverage(Indexer indexer, String name) {
        Coverages coverages = indexer.getCoverages();
        if (coverages != null) {
            List<Coverage> coverageList = coverages.getCoverage();
            for (Coverage coverage : coverageList) {
                String coverageName = coverage.getName();
                if (coverageName != null && coverageName.equalsIgnoreCase(name)) {
                    return coverage;
                }
            }
        }
        return null;
    }

    /** Utility method which does special checks on specific parameters */
    public static String refineParameterValue(String parameterName, String parameterValue) {
        if (parameterName.equalsIgnoreCase(Utils.Prop.ROOT_MOSAIC_DIR)) {
            // Special management for Root mosaic dir
            Utilities.ensureNonNull("parameterValue", parameterValue);
            String testingDirectory = parameterValue;
            parameterValue = Utils.checkDirectory(testingDirectory, false);
        }
        return parameterValue;
    }

    /**
     * Set the attributes of the specified domain, getting values from the value String In case the
     * value contains a ";" separator, add a different attribute for each element.
     */
    public static void setAttributes(DomainType domain, String values) {
        if (values.contains(";")) {
            String properties[] = values.split(";");
            for (String prop : properties) {
                addAttribute(domain, prop);
            }
        } else {
            addAttribute(domain, values);
        }
    }

    /** Add a single attribute to that domain with the specified value */
    private static void addAttribute(DomainType domain, String attributeValue) {
        AttributeType attribute = Utils.OBJECT_FACTORY.createAttributeType();
        attribute.setAttribute(attributeValue);
        List<AttributeType> listAttributes = domain.getAttributes();
        listAttributes.add(attribute);
    }

    /** Set the parameter having the specified name with the specified value */
    public static void setParam(
            List<Parameter> parameters, String parameterName, String parameterValue) {
        Parameter param = null;
        for (Parameter parameter : parameters) {
            if (parameter.getName().equalsIgnoreCase(parameterName)) {
                param = parameter;
                break;
            }
        }
        if (param == null) {
            param = Utils.OBJECT_FACTORY.createParametersTypeParameter();
            parameters.add(param);
        }
        param.setName(parameterName);
        param.setValue(refineParameterValue(parameterName, parameterValue));
    }

    /**
     * Get the value of a property name from a properties object and set that value to a parameter
     * with the same name
     */
    public static void setParam(List<Parameter> parameters, Properties props, String propName) {
        setParam(parameters, propName, props.getProperty(propName));
    }

    /**
     * Return the parameter value (as a boolean) of the specified parameter name from the provider
     * indexer
     */
    public static boolean getParameterAsBoolean(String parameterName, Indexer indexer) {
        String value = getParameter(parameterName, indexer);
        if (value != null) {
            return Boolean.parseBoolean(value);
        }
        return false;
    }

    /**
     * Return the parameter value (as a boolean) of the specified parameter name from the provider
     * indexer
     */
    public static <T extends Enum> T getParameterAsEnum(
            String parameterName, Class<T> enumClass, Indexer indexer) {
        String value = getParameter(parameterName, indexer);
        if (value != null) {
            return (T) Enum.valueOf(enumClass, value);
        }
        return null;
    }

    /**
     * Return the parameter string value of the specified parameter name from the provided
     * parameters element
     */
    public static String getParam(ParametersType params, String parameterName) {
        List<Parameter> parameters = null;
        if (params != null) {
            parameters = params.getParameter();
            for (Parameter param : parameters) {
                if (param.getName().equalsIgnoreCase(parameterName)) {
                    return param.getValue();
                }
            }
        }
        return null;
    }

    /**
     * Return the parameter string value of the specified parameter name from the provided indexer
     */
    public static String getParameter(String parameterName, Indexer indexer) {
        final ParametersType params = indexer.getParameters();
        return getParam(params, parameterName);
    }

    /**
     * Return the parameter string value of the specified parameter name from the indexer defined in
     * the specified file (if exists).
     */
    public static String getParameter(String parameterName, File indexerFile) {
        if (indexerFile != null && indexerFile.exists()) {
            try {
                Indexer indexerInstance = Utils.unmarshal(indexerFile);
                if (indexerInstance != null) {
                    String value = IndexerUtils.getParameter(parameterName, indexerInstance);
                    if (value != null) {
                        return value;
                    }
                }
            } catch (JAXBException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    /** Parse additional domains */
    public static void parseAdditionalDomains(String attributes, List<DomainType> domainList) {
        final String[] domainsAttributes = attributes.split(",");
        for (String domainAttributes : domainsAttributes) {
            DomainType domain = Utils.OBJECT_FACTORY.createDomainType();
            String domainName = domainAttributes.trim();
            String domainAttribs = domainName;
            if (domainAttributes.contains("(") && domainAttributes.contains(")")) {
                domainName = domainName.substring(0, domainName.indexOf("(")).trim();
                domainAttribs =
                        domainAttribs
                                .substring(domainAttribs.indexOf("("))
                                .replace("(", "")
                                .replace(")", "");
            }
            domain.setName(domainName);
            // TODO: CHECK THAT
            setAttributes(domain, domainAttribs);
            domainList.add(domain);
        }
    }

    /**
     * Get the attributes from the specified domain. The boolean specifies whether we need to add a
     * domain prefix before returning the attributes.
     */
    private static String getAttributesAsString(
            final DomainType domain, final boolean domainPrefix) {
        final String currentDomainName = domain.getName();
        final List<AttributeType> listAttributes = domain.getAttributes();
        if (!listAttributes.isEmpty()) {
            String attribs = null;
            if (listAttributes.size() == 1) {
                attribs = listAttributes.get(0).getAttribute().trim();
            } else {
                // Only support up to 2 attributes (start;end/low;high/...)
                String attr0 = listAttributes.get(0).getAttribute().trim();
                String attr1 = listAttributes.get(1).getAttribute().trim();
                attribs = attr0 + ";" + attr1;
            }
            if (domainPrefix) {
                return currentDomainName + "(" + attribs + ")";
            } else {
                return attribs;
            }
        }
        return null;
    }

    public static DomainType getDomain(DomainType domain, DomainsType refDomains) {
        String currentDomainName = domain.getName();
        if (currentDomainName == null) {
            String domainRef = domain.getRef();
            if (domainRef != null && refDomains != null) {
                for (DomainType refDomain : refDomains.getDomain()) {
                    if (refDomain.getName().equalsIgnoreCase(domainRef)) {
                        currentDomainName = domainRef;
                        return refDomain;
                    }
                }
            }
        }
        return domain;
    }

    /**
     * Look for the specified coverageName inside the provided Indexer and return the attributes of
     * the specified domain.
     *
     * @return TODO: Code is going complex. We should use a visitor
     */
    public static String getAttribute(
            final String coverageName, final String domainName, final Indexer indexer) {
        if (indexer != null) {
            final Coverages coverages = indexer.getCoverages();
            final DomainsType refDomains = indexer.getDomains();
            if (coverages != null) {
                final List<Coverage> coverageList = coverages.getCoverage();
                if (coverageList != null && !coverageList.isEmpty()) {
                    for (Coverage coverage : coverageList) {

                        // Look for the specified coverage name
                        final String currentCoverageName = coverage.getName();
                        if (currentCoverageName == null
                                || currentCoverageName.equalsIgnoreCase(coverageName)) {
                            final DomainsType domains = coverage.getDomains();
                            if (domains != null) {
                                final List<DomainType> domainList = domains.getDomain();
                                if (domainList != null) {

                                    // Look for the specified domain
                                    if (!domainName.equalsIgnoreCase(Utils.ADDITIONAL_DOMAIN)) {
                                        for (DomainType domain : domainList) {
                                            DomainType currentDomain =
                                                    getDomain(domain, refDomains);
                                            String currentDomainName = currentDomain.getName();
                                            if (currentDomainName != null
                                                    && currentDomainName.equalsIgnoreCase(
                                                            domainName)) {
                                                return getAttributesAsString(currentDomain, false);
                                            }
                                        }
                                    } else {
                                        StringBuilder additionalDomainAttributes =
                                                new StringBuilder();
                                        for (DomainType domain : domainList) {
                                            DomainType currentDomain =
                                                    getDomain(domain, refDomains);
                                            String domName = currentDomain.getName();
                                            if (!domName.equalsIgnoreCase(Utils.TIME_DOMAIN)
                                                    && !domName.equalsIgnoreCase(
                                                            Utils.ELEVATION_DOMAIN)) {
                                                additionalDomainAttributes.append(
                                                        getAttributesAsString(currentDomain, true));
                                                additionalDomainAttributes.append(",");
                                            }
                                        }
                                        String attribs = additionalDomainAttributes.toString();
                                        if (attribs != null && attribs.length() > 0) {
                                            // remove the last ","
                                            attribs = attribs.substring(0, attribs.length() - 1);
                                        }
                                        if (attribs.length() > 0) {
                                            return attribs;
                                        }
                                        return null;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * Get a {@link SchemaType} element for the specified {@link Coverage}. The {@link Indexer}
     * object will be used in case of an externally referenced schema.
     *
     * @param indexer the main {@link Indexer} instance
     * @param coverage the {@link Coverage} element with the Schema to be returned.
     */
    public static SchemaType getSchema(Indexer indexer, Coverage coverage) {
        Utilities.ensureNonNull("coverage", coverage);
        SchemaType schema = coverage.getSchema();
        if (schema != null) {
            String schemaRef = schema.getRef();
            if (schemaRef != null) {
                schema = getSchemaRef(indexer, schemaRef);
                if (schema != null) {
                    return schema;
                }
            }
            return schema;
        }
        return null;
    }

    private static SchemaType getSchemaRef(Indexer indexer, String schemaRef) {
        Utilities.ensureNonNull("schemaRef", schemaRef);
        Utilities.ensureNonNull("indexer", indexer);
        SchemasType schemas = indexer.getSchemas();
        if (schemas != null) {
            List<SchemaType> schemaList = schemas.getSchema();
            for (SchemaType schema : schemaList) {
                final String schemaName = schema.getName();
                if (schemaName != null && schemaName.equalsIgnoreCase(schemaRef)) {
                    return schema;
                }
            }
        }
        return null;
    }

    public static Indexer createDefaultIndexer() {
        final Indexer defaultIndexer = Utils.OBJECT_FACTORY.createIndexer();
        final ParametersType parameters = Utils.OBJECT_FACTORY.createParametersType();
        final List<Parameter> parameterList = parameters.getParameter();
        defaultIndexer.setParameters(parameters);
        setParam(parameterList, Utils.Prop.LOCATION_ATTRIBUTE, Utils.DEFAULT_LOCATION_ATTRIBUTE);
        setParam(parameterList, Utils.Prop.WILDCARD, Utils.DEFAULT_WILCARD);
        setParam(
                parameterList,
                Utils.Prop.FOOTPRINT_MANAGEMENT,
                Boolean.toString(Utils.DEFAULT_FOOTPRINT_MANAGEMENT));
        setParam(
                parameterList,
                Utils.Prop.ABSOLUTE_PATH,
                Boolean.toString(Utils.DEFAULT_PATH_BEHAVIOR));
        setParam(
                parameterList,
                Utils.Prop.RECURSIVE,
                Boolean.toString(Utils.DEFAULT_RECURSION_BEHAVIOR));
        setParam(parameterList, Utils.Prop.INDEX_NAME, Utils.DEFAULT_INDEX_NAME);

        return defaultIndexer;
    }

    public static Indexer initializeIndexer(ParametersType params, File parent) {
        File indexerFile = new File(parent, INDEXER_XML);
        Indexer indexer = null;
        if (Utils.checkFileReadable(indexerFile)) {
            try {
                indexer = Utils.unmarshal(indexerFile);
                if (indexer != null) {
                    copyDefaultParams(params, indexer);
                }
            } catch (JAXBException e) {
                LOGGER.log(Level.WARNING, e.getMessage(), e);
            }
        } else {
            // Backward compatible with old indexing
            indexerFile = new File(parent, INDEXER_PROPERTIES);
            if (Utils.checkFileReadable(indexerFile)) {
                // load it and parse it
                final Properties props =
                        CoverageUtilities.loadPropertiesFromURL(URLs.fileToUrl(indexerFile));
                indexer = createIndexer(props, params);
            }
        }
        if (indexer != null) {
            indexer.setIndexerFile(indexerFile);
        }
        return indexer;
    }

    /** Setup default params to the indexer. */
    private static void copyDefaultParams(ParametersType params, Indexer indexer) {
        if (params != null) {
            List<Parameter> defaultParamList = params.getParameter();
            if (defaultParamList != null && !defaultParamList.isEmpty()) {
                ParametersType parameters = indexer.getParameters();
                if (parameters == null) {
                    parameters = Utils.OBJECT_FACTORY.createParametersType();
                    indexer.setParameters(parameters);
                }
                List<Parameter> parameterList = parameters.getParameter();
                for (Parameter defaultParameter : defaultParamList) {
                    final String defaultParameterName = defaultParameter.getName();
                    if (getParameter(defaultParameterName, indexer) == null) {
                        setParam(parameterList, defaultParameterName, defaultParameter.getValue());
                    }
                }
            }
        }
    }

    private static Indexer createIndexer(Properties props, ParametersType params) {
        // Initializing Indexer objects
        Indexer indexer = Utils.OBJECT_FACTORY.createIndexer();
        indexer.setParameters(
                params != null ? params : Utils.OBJECT_FACTORY.createParametersType());
        Coverages coverages = Utils.OBJECT_FACTORY.createIndexerCoverages();
        indexer.setCoverages(coverages);
        List<Coverage> coverageList = coverages.getCoverage();

        Coverage coverage = Utils.OBJECT_FACTORY.createIndexerCoveragesCoverage();
        coverageList.add(coverage);

        List<Parameter> parameters = indexer.getParameters().getParameter();

        // name
        if (props.containsKey(Utils.Prop.NAME)) {
            setParam(parameters, props, Utils.Prop.NAME);
            coverage.setName(props.getProperty(Utils.Prop.NAME));
        }

        // type name
        if (props.containsKey(Utils.Prop.TYPENAME)) {
            setParam(parameters, props, Utils.Prop.TYPENAME);
            coverage.setName(props.getProperty(Utils.Prop.TYPENAME));
        }

        // absolute
        if (props.containsKey(Utils.Prop.ABSOLUTE_PATH))
            setParam(parameters, props, Utils.Prop.ABSOLUTE_PATH);

        if (props.containsKey(Utils.Prop.PATH_TYPE))
            setParam(parameters, props, Utils.Prop.PATH_TYPE);

        // recursive
        if (props.containsKey(Utils.Prop.RECURSIVE))
            setParam(parameters, props, Utils.Prop.RECURSIVE);

        // wildcard
        if (props.containsKey(Utils.Prop.WILDCARD))
            setParam(parameters, props, Utils.Prop.WILDCARD);

        // granule acceptors string
        if (props.containsKey(Utils.Prop.GRANULE_ACCEPTORS)) {
            setParam(parameters, props, Utils.Prop.GRANULE_ACCEPTORS);
        }

        if (props.containsKey(Utils.Prop.GEOMETRY_HANDLER)) {
            setParam(parameters, props, Utils.Prop.GEOMETRY_HANDLER);
        }

        if (props.containsKey(Utils.Prop.COVERAGE_NAME_COLLECTOR_SPI)) {
            IndexerUtils.setParam(parameters, props, Utils.Prop.COVERAGE_NAME_COLLECTOR_SPI);
        }

        // schema
        if (props.containsKey(Utils.Prop.SCHEMA)) {
            SchemasType schemas = Utils.OBJECT_FACTORY.createSchemasType();
            SchemaType schema = Utils.OBJECT_FACTORY.createSchemaType();
            indexer.setSchemas(schemas);
            schemas.getSchema().add(schema);
            schema.setAttributes(props.getProperty(Utils.Prop.SCHEMA));
            schema.setName(getParameter(Utils.Prop.INDEX_NAME, indexer));
        }

        // add well known domains
        addDomain(props, coverage, Utils.Prop.TIME_ATTRIBUTE, Utils.TIME_DOMAIN);
        addDomain(props, coverage, Utils.Prop.ELEVATION_ATTRIBUTE, Utils.ELEVATION_DOMAIN);
        addDomain(props, coverage, Utils.Prop.CRS_ATTRIBUTE, Utils.CRS_DOMAIN);
        addDomain(props, coverage, Utils.Prop.RESOLUTION_ATTRIBUTE, Utils.RESOLUTION_DOMAIN);
        addDomain(props, coverage, Utils.Prop.RESOLUTION_X_ATTRIBUTE, Utils.RESOLUTION_X_DOMAIN);
        addDomain(props, coverage, Utils.Prop.RESOLUTION_Y_ATTRIBUTE, Utils.RESOLUTION_Y_DOMAIN);

        // Additional domain attr
        if (props.containsKey(Utils.Prop.ADDITIONAL_DOMAIN_ATTRIBUTES)) {
            DomainsType domains = coverage.getDomains();
            if (domains == null) {
                domains = Utils.OBJECT_FACTORY.createDomainsType();
                coverage.setDomains(domains);
            }
            List<DomainType> domainList = domains.getDomain();
            String attributes = props.getProperty(Utils.Prop.ADDITIONAL_DOMAIN_ATTRIBUTES);
            parseAdditionalDomains(attributes, domainList);
        }

        // imposed BBOX
        if (props.containsKey(Utils.Prop.ENVELOPE2D))
            setParam(parameters, props, Utils.Prop.ENVELOPE2D);

        // imposed Pyramid Layout
        if (props.containsKey(Utils.Prop.RESOLUTION_LEVELS))
            setParam(parameters, props, Utils.Prop.RESOLUTION_LEVELS);

        // collectors
        if (props.containsKey(Utils.Prop.PROPERTY_COLLECTORS)) {
            setPropertyCollectors(indexer, props.getProperty(Utils.Prop.PROPERTY_COLLECTORS));
        }

        if (props.containsKey(Utils.Prop.CACHING)) setParam(parameters, props, Utils.Prop.CACHING);

        if (props.containsKey(Utils.Prop.ROOT_MOSAIC_DIR)) {
            // Overriding root mosaic directory
            setParam(parameters, props, Utils.Prop.ROOT_MOSAIC_DIR);
        }

        if (props.containsKey(Utils.Prop.INDEXING_DIRECTORIES)) {
            setParam(parameters, props, Utils.Prop.INDEXING_DIRECTORIES);
        }
        if (props.containsKey(Utils.Prop.AUXILIARY_FILE)) {
            setParam(parameters, props, Utils.Prop.AUXILIARY_FILE);
        }
        if (props.containsKey(Utils.Prop.AUXILIARY_DATASTORE_FILE)) {
            setParam(parameters, props, Utils.Prop.AUXILIARY_DATASTORE_FILE);
        }
        if (props.containsKey(Utils.Prop.CAN_BE_EMPTY)) {
            setParam(parameters, props, Utils.Prop.CAN_BE_EMPTY);
        }
        if (props.containsKey(Utils.Prop.WRAP_STORE)) {
            setParam(parameters, props, Utils.Prop.WRAP_STORE);
        }
        if (props.containsKey(Utils.Prop.USE_EXISTING_SCHEMA)) {
            setParam(parameters, props, Utils.Prop.USE_EXISTING_SCHEMA);
        }
        if (props.containsKey(Utils.Prop.CHECK_AUXILIARY_METADATA)) {
            setParam(parameters, props, Utils.Prop.CHECK_AUXILIARY_METADATA);
        }

        if (props.containsKey(Utils.Prop.GRANULE_COLLECTOR_FACTORY)) {
            setParam(parameters, props, Utils.Prop.GRANULE_COLLECTOR_FACTORY);
        }

        if (props.containsKey(Utils.Prop.HETEROGENEOUS_CRS)) {
            setParam(parameters, props, Utils.Prop.HETEROGENEOUS_CRS);
        }

        if (props.containsKey(Utils.Prop.MOSAIC_CRS)) {
            setParam(parameters, props, Utils.Prop.MOSAIC_CRS);
        }

        if (props.containsKey(Utils.Prop.NO_DATA)) {
            setParam(parameters, props, Utils.Prop.NO_DATA);
        }

        return indexer;
    }

    private static void addDomain(
            Properties props, Coverage coverage, String attributeName, String domainName) {
        if (props.containsKey(attributeName)) {
            DomainsType domains = coverage.getDomains();
            if (domains == null) {
                domains = Utils.OBJECT_FACTORY.createDomainsType();
                coverage.setDomains(domains);
            }
            List<DomainType> domainList = domains.getDomain();
            DomainType domain = Utils.OBJECT_FACTORY.createDomainType();
            domain.setName(domainName.toLowerCase());
            setAttributes(domain, props.getProperty(attributeName));
            domainList.add(domain);
        }
    }

    /**
     * Set the given parameter on the given indexer
     *
     * @param indexer indexer on which to set the param
     * @param paramName parameter name
     * @param paramValue parameter value
     */
    public static void setParam(Indexer indexer, String paramName, String paramValue) {
        setParam(indexer.getParameters().getParameter(), paramName, paramValue);
    }
}
