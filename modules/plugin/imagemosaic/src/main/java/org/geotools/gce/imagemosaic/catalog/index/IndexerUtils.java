package org.geotools.gce.imagemosaic.catalog.index;

import java.io.File;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.data.DataUtilities;
import org.geotools.gce.imagemosaic.Utils;
import org.geotools.gce.imagemosaic.Utils.Prop;
import org.geotools.gce.imagemosaic.catalog.index.Indexer.Collectors;
import org.geotools.gce.imagemosaic.catalog.index.Indexer.Collectors.Collector;
import org.geotools.gce.imagemosaic.catalog.index.Indexer.Coverages;
import org.geotools.gce.imagemosaic.catalog.index.Indexer.Coverages.Coverage;
import org.geotools.gce.imagemosaic.catalog.index.ParametersType.Parameter;
import org.geotools.util.Utilities;

public class IndexerUtils {

    private final static Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger(IndexerUtils.class.toString());
    
    /**
     * Build {@link Collectors} element by parsing the specified propertyCollectors,
     * and put them on the specified indexer object.
     * 
     * @param indexer
     * @param propertyCollectors
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
                final int squareRPosLast = pcDef.lastIndexOf("]");
                final int roundLPos = pcDef.indexOf("(");
                final int roundRPos = pcDef.indexOf(")");
                final int roundRPosLast = pcDef.lastIndexOf(")");
                if (squareRPos != squareRPosLast) {
                    if (LOGGER.isLoggable(Level.INFO)) {
                        LOGGER.info("Skipping unparseable PropertyCollector definition: " + pcDef);
                    }
                    continue;
                }
                if (squareLPos == -1 || squareRPos == -1) {
                    if (LOGGER.isLoggable(Level.INFO)) {
                        LOGGER.info("Skipping unparseable PropertyCollector definition: " + pcDef);
                    }
                    continue;
                }
                if (squareLPos == 0) {
                    if (LOGGER.isLoggable(Level.INFO)) {
                        LOGGER.info("Skipping unparseable PropertyCollector definition: " + pcDef);
                    }
                    continue;
                }
    
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
                if (roundLPos != squareRPos + 1) {// ]( or exit
                    if (LOGGER.isLoggable(Level.INFO)) {
                        LOGGER.info("Skipping unparseable PropertyCollector definition: " + pcDef);
                    }
                    continue;
                }
                if (roundRPos != (pcDef.length() - 1)) {// end with )
                    if (LOGGER.isLoggable(Level.INFO)) {
                        LOGGER.info("Skipping unparseable PropertyCollector definition: " + pcDef);
                    }
                    continue;
                }
    
                // name
                final String spi = pcDef.substring(0, squareLPos);
    
                // config
                final String config = squareLPos < squareRPos ? pcDef.substring(squareLPos + 1, squareRPos) : "";
                String value = null;
                final File configFile = new File(getParameter(Utils.Prop.ROOT_MOSAIC_DIR, indexer),
                        config + ".properties");
                if (!Utils.checkFileReadable(configFile)) {
                    if (LOGGER.isLoggable(Level.INFO)) {
                        LOGGER.info("Unable to access the file for this PropertyCollector: "
                                + configFile.getAbsolutePath());
                    }
                    continue;
                } else {
                    final Properties properties = Utils.loadPropertiesFromURL(DataUtilities.fileToURL(configFile));
                    if (properties.containsKey("regex")) {
                        value = properties.getProperty("regex");
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
     * Return an indexer {@link Coverage} object from the specified {@link Indexer}, referring to the
     * requested name.
     * @param indexer the main {@link Indexer} instance
     * @param name the name of the {@link Coverage} element to be retrieved.
     * 
     * @return the {@link Coverage} element for the specified name (if any).
     */
    public static Coverage getCoverage(Indexer indexer, String name) {
        Coverages coverages = indexer.getCoverages();
        if (coverages != null) {
            List<Coverage> coverageList = coverages.getCoverage();
            for (Coverage coverage: coverageList) {
                String coverageName = coverage.getName();
                if (coverageName != null && coverageName.equalsIgnoreCase(name)) {
                    return coverage;
                }
            }
        }
        return null;
    }

    /**
     * Utility method which does special checks on specific parameters
     * @param parameterName
     * @param parameterValue
     * @return
     */
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
     * Set the attributes of the specified domain, getting values from the value String
     * In case the value contains a ";" separator, add a different attribute for each element.
     * @param domain
     * @param values
     */
    public static void setAttributes(DomainType domain, String values) {
        if (values.contains(";")) {
            String properties[] = values.split(";");
            for (String prop: properties) {
                addAttribute(domain, prop);
            }
        } else {
            addAttribute(domain, values);
        }
    }

    /**
     * Add a single attribute to that domain with the specified value
     * 
     * @param domain
     * @param attributeValue
     */
    private static void addAttribute(DomainType domain, String attributeValue) {
        AttributeType attribute = Utils.OBJECT_FACTORY.createAttributeType();
        attribute.setAttribute(attributeValue);
        List<AttributeType> listAttributes = domain.getAttributes();
        listAttributes.add(attribute);
    }

    /**
     * Set the parameter having the specified name with the specified value
     * 
     * @param parameters
     * @param parameterName
     * @param parameterValue
     */
    public static void setParam(List<Parameter> parameters, String parameterName, String parameterValue) {
        Parameter param = null;
        for (Parameter parameter: parameters ){
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
     * 
     * @param parameters
     * @param props
     * @param propName
     */
    public static void setParam(List<Parameter> parameters, Properties props, String propName) {
        setParam(parameters, propName, props.getProperty(propName));
    }

    /**
     * Return the parameter value (as a boolean) of the specified parameter name from the provider indexer
     * @param parameterName
     * @param indexer
     * @return
     */
    public static boolean getParameterAsBoolean(String parameterName, Indexer indexer) {
        String value = getParameter(parameterName, indexer);
        if (value != null) {
            return Boolean.parseBoolean(value);
        }
        return false;
    }

    /**
     * Return the parameter string value of the specified parameter name from the provided parameters element
     * @param params
     * @param parameterName
     * @return
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
     * @param parameterName
     * @param indexer
     * @return
     */
    public static String getParameter(String parameterName, Indexer indexer) {
        final ParametersType params = indexer.getParameters();
        return getParam(params, parameterName);
    }

    /**
     * Parse additional domains 
     * @param attributes
     * @param domainList
     */
    public static void parseAdditionalDomains(String attributes, List<DomainType> domainList) {
        final String[] domainsAttributes = attributes.split(",");
        for (String domainAttributes: domainsAttributes) {
            DomainType domain = Utils.OBJECT_FACTORY.createDomainType();
            String domainName = domainAttributes.trim();
            String domainAttribs = domainName;
            if (domainAttributes.contains("(") && domainAttributes.contains(")")) {
                    domainName = domainName.substring(0, domainName.indexOf("(")).trim();
                    domainAttribs = domainAttribs.substring(domainAttribs.indexOf("("))
                            .replace("(", "").replace(")", "");
            }
            domain.setName(domainName);
            //TODO: CHECK THAT
            setAttributes(domain, domainAttribs);
            domainList.add(domain);
        }
        
    }

    /**
     * Get the attributes from the specified domain. The boolean specifies whether we need to 
     * add a domain prefix before returning the attributes.
     * @param domain
     * @param domainPrefix
     * @return
     */
    private static String getAttributesAsString(final DomainType domain, final boolean domainPrefix) {
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
     * Look for the specified coverageName inside the provided Indexer and return the attributes of the specified domain.
     * 
     * @param coverageName
     * @param domainName
     * @param indexer
     * @return
     * 
     * TODO: Code is going complex. We should use a visitor 
     */
    public static String getAttribute(final String coverageName, final String domainName, final Indexer indexer) {
        if (indexer != null) {
            final Coverages coverages = indexer.getCoverages();
            final DomainsType refDomains = indexer.getDomains();
            if (coverages != null) {
                final List<Coverage> coverageList = coverages.getCoverage();
                if (coverageList != null && !coverageList.isEmpty()) {
                    for (Coverage coverage: coverageList) {
                        
                        // Look for the specified coverage name
                        final String currentCoverageName = coverage.getName();
                        if (currentCoverageName == null || currentCoverageName.equalsIgnoreCase(coverageName)) {
                            final DomainsType domains = coverage.getDomains();
                            if (domains != null)  {
                                final List<DomainType> domainList = domains.getDomain();
                                if (domainList != null) {
                                    
                                    // Look for the specified domain
                                    if (!domainName.equalsIgnoreCase(Utils.ADDITIONAL_DOMAIN)) {
                                        for (DomainType domain : domainList) {
                                            DomainType currentDomain = getDomain(domain, refDomains);
                                            String currentDomainName = currentDomain.getName();
                                            if (currentDomainName != null && currentDomainName.equalsIgnoreCase(domainName)) {
                                                return getAttributesAsString(currentDomain, false);
                                            }
                                        }
                                    } else {
                                        StringBuilder additionalDomainAttributes = new StringBuilder();
                                        for (DomainType domain : domainList) {
                                            DomainType currentDomain = getDomain(domain, refDomains);
                                            String domName = currentDomain.getName();
                                            if (!domName.equalsIgnoreCase(Utils.TIME_DOMAIN)
                                                    && !domName.equalsIgnoreCase(Utils.ELEVATION_DOMAIN)) {
                                                additionalDomainAttributes.append(getAttributesAsString(currentDomain, true));
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
     * Get a {@link SchemaType} element for the specified {@link Coverage}.
     * The {@link Indexer} object will be used in case of an externally referenced schema.
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
            for (SchemaType schema: schemaList) {
                final String schemaName = schema.getName();
                if (schemaName != null && schemaName.equalsIgnoreCase(schemaRef)) {
                    return schema;
                }
            }
        }
        return null;
    }

}
