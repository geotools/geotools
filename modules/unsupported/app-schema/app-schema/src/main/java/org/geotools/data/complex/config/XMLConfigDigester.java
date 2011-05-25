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

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.digester.Digester;
import org.geotools.data.complex.AppSchemaDataAccessFactory;
import org.xml.sax.SAXException;

/**
 * Digester to consume the app-schema {@link AppSchemaDataAccessFactory} configuration file.
 * 
 * @author Gabriel Roldan, Axios Engineering
 * @author Rini Angreani, Curtin University of Technology
 * @author Ben Caradoc-Davies, CSIRO Exploration and Mining
 * @author Russell Petty, GSV
 * @version $Id$
 *
 * @source $URL$
 * @since 2.4
 */
public class XMLConfigDigester {
    /** DOCUMENT ME! */
    private static final Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger(XMLConfigDigester.class.getPackage().getName());

    /** Namespace URI for the AppSchemaDataAccess configuration files */
    private static final String CONFIG_NS_URI = "http://www.geotools.org/app-schema";

    /**
     * Creates a new XMLConfigReader object.
     */
    public XMLConfigDigester() {
        super();
    }

    /**
     * Parses a complex datastore configuration file in xml format into a
     * {@link AppSchemaDataAccessDTO}
     * 
     * @param dataStoreConfigUrl
     *            config file location
     * 
     * @return a DTO object representing the datastore's configuration
     * 
     * @throws IOException
     *             if an error occurs parsing the file
     */
    public AppSchemaDataAccessDTO parse(URL dataStoreConfigUrl) throws IOException {
        AppSchemaDataAccessDTO config = digest(dataStoreConfigUrl);
        return config;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param dataStoreConfigUrl
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws IOException
     *             DOCUMENT ME!
     * @throws NullPointerException
     *             DOCUMENT ME!
     */
    private AppSchemaDataAccessDTO digest(final URL dataStoreConfigUrl) throws IOException {
        if (dataStoreConfigUrl == null) {
            throw new NullPointerException("datastore config url");
        }

        // read mapping file into configString and interpolate properties
        InputStream configStream = null;
        String configString = null;
        try {
            configStream = dataStoreConfigUrl.openStream();
            if (configStream == null) {
                throw new IOException("Can't open datastore config file " + dataStoreConfigUrl);
            } else {
                configString = PropertyInterpolationUtils.interpolate(PropertyInterpolationUtils
                        .loadProperties(AppSchemaDataAccessFactory.DBTYPE_STRING),
                        PropertyInterpolationUtils.readAll(configStream));
            }
        } finally {
            if (configStream != null) {
                configStream.close();
            }
        }

        XMLConfigDigester.LOGGER.fine("parsing complex datastore config: "
                + dataStoreConfigUrl.toExternalForm());

        Digester digester = new Digester();
        XMLConfigDigester.LOGGER.fine("digester created");
        // URL schema = getClass()
        // .getResource("../test-data/AppSchemaDataAccess.xsd");
        // digester.setSchema(schema.toExternalForm());
        digester.setValidating(false);
        digester.setNamespaceAware(true);
        digester.setRuleNamespaceURI(XMLConfigDigester.CONFIG_NS_URI);

        // digester.setRuleNamespaceURI(OGC_NS_URI);
        AppSchemaDataAccessDTO configDto = new AppSchemaDataAccessDTO();
        configDto.setBaseSchemasUrl(dataStoreConfigUrl.toExternalForm());

        digester.push(configDto);

        try {
            setNamespacesRules(digester);

            setIncludedTypesRules(digester);

            setSourceDataStoresRules(digester);

            setTargetSchemaUriRules(digester);

            setTypeMappingsRules(digester);

        } catch (Exception e) {
            e.printStackTrace();
            XMLConfigDigester.LOGGER.log(Level.SEVERE, "setting digester properties: ", e);
            throw new IOException("Error setting digester properties: " + e.getMessage());
        }

        try {
            digester.parse(new StringReader(configString));
        } catch (SAXException e) {
            e.printStackTrace();
            XMLConfigDigester.LOGGER.log(Level.SEVERE, "parsing " + dataStoreConfigUrl, e);

            IOException ioe = new IOException("Can't parse complex datastore config. ");
            ioe.initCause(e);
            throw ioe;
        }

        AppSchemaDataAccessDTO config = (AppSchemaDataAccessDTO) digester.getRoot();

        return config;
    }

    private void setTypeMappingsRules(Digester digester) {
        final String mappings = "AppSchemaDataAccess/typeMappings";
        digester.addObjectCreate(mappings, XMLConfigDigester.CONFIG_NS_URI, HashSet.class);

        final String typeMapping = mappings + "/FeatureTypeMapping";

        digester.addObjectCreate(typeMapping, XMLConfigDigester.CONFIG_NS_URI, TypeMapping.class);
        digester.addCallMethod(typeMapping + "/mappingName", "setMappingName", 1);
        digester.addCallParam(typeMapping + "/mappingName", 0);
        digester.addCallMethod(typeMapping + "/sourceDataStore", "setSourceDataStore", 1);
        digester.addCallParam(typeMapping + "/sourceDataStore", 0);
        digester.addCallMethod(typeMapping + "/sourceType", "setSourceTypeName", 1);
        digester.addCallParam(typeMapping + "/sourceType", 0);
        digester.addCallMethod(typeMapping + "/targetElement", "setTargetElementName", 1);
        digester.addCallParam(typeMapping + "/targetElement", 0);
        digester.addCallMethod(typeMapping + "/itemXpath", "setItemXpath", 1);
        digester.addCallParam(typeMapping + "/itemXpath", 0);

        // isXmlDataStore is a flag to denote that AppSchema needs to process
        // the data from the datastore differently as it returns xml rather than features.
        digester.addCallMethod(typeMapping + "/isXmlDataStore", "setXmlDataStore", 1);
        digester.addCallParam(typeMapping + "/isXmlDataStore", 0);

        // create attribute mappings
        final String attMappings = typeMapping + "/attributeMappings";
        digester.addObjectCreate(attMappings, XMLConfigDigester.CONFIG_NS_URI, ArrayList.class);

        final String attMap = attMappings + "/AttributeMapping";
        digester.addObjectCreate(attMap, XMLConfigDigester.CONFIG_NS_URI, AttributeMapping.class);

        digester.addCallMethod(attMap + "/label", "setLabel", 1);
        digester.addCallParam(attMap + "/label", 0);

        digester.addCallMethod(attMap + "/parentLabel", "setParentLabel", 1);
        digester.addCallParam(attMap + "/parentLabel", 0);

        digester.addCallMethod(attMap + "/targetQueryString", "setTargetQueryString", 1);
        digester.addCallParam(attMap + "/targetQueryString", 0);

        digester.addCallMethod(attMap + "/instancePath", "setInstancePath", 1);
        digester.addCallParam(attMap + "/instancePath", 0);

        digester.addCallMethod(attMap + "/isMultiple", "setMultiple", 1);
        digester.addCallParam(attMap + "/isMultiple", 0);

        digester.addCallMethod(attMap + "/targetAttribute", "setTargetAttributePath", 1);
        digester.addCallParam(attMap + "/targetAttribute", 0);

        digester.addCallMethod(attMap + "/targetAttributeNode", "setTargetAttributeSchemaElement",
                1);
        digester.addCallParam(attMap + "/targetAttributeNode", 0);

        digester.addCallMethod(attMap + "/idExpression/OCQL", "setIdentifierExpression", 1);
        digester.addCallParam(attMap + "/idExpression/OCQL", 0);

        digester.addCallMethod(attMap + "/sourceExpression/OCQL", "setSourceExpression", 1);
        digester.addCallParam(attMap + "/sourceExpression/OCQL", 0);

        digester.addCallMethod(attMap + "/idExpression/inputAttribute", "setIdentifierPath", 1);
        digester.addCallParam(attMap + "/idExpression/inputAttribute", 0);

        // if the source is a data access, then the input is in XPath expression
        digester.addCallMethod(attMap + "/sourceExpression/inputAttribute",
                "setInputAttributePath", 1);
        digester.addCallParam(attMap + "/sourceExpression/inputAttribute", 0);

        // for feature chaining: this refers to the nested feature type
        digester.addCallMethod(attMap + "/sourceExpression/linkElement", "setLinkElement", 1);
        digester.addCallParam(attMap + "/sourceExpression/linkElement", 0);

        // for feature chaining: this refers to the nested feature attribute
        digester.addCallMethod(attMap + "/sourceExpression/linkField", "setLinkField", 1);
        digester.addCallParam(attMap + "/sourceExpression/linkField", 0);

        digester.addCallMethod(attMap + "/ClientProperty", "putClientProperty", 2);
        digester.addCallParam(attMap + "/ClientProperty/name", 0);
        digester.addCallParam(attMap + "/ClientProperty/value", 1);

        // add the AttributeMapping to the list
        digester.addSetNext(attMap, "add");

        // set attribute mappings
        digester.addSetNext(attMappings, "setAttributeMappings");

        // add the TypeMapping to the Set
        digester.addSetNext(typeMapping, "add");

        // set the TypeMapping on AppSchemaDataAccessDTO
        digester.addSetNext(mappings, "setTypeMappings");
    }

    private void setTargetSchemaUriRules(Digester digester) {
        final String targetSchemas = "AppSchemaDataAccess/targetTypes";

        digester.addBeanPropertySetter("AppSchemaDataAccess/catalog");
        // digester.addCallMethod(targetSchemas + "/Catalog", "setCatalog", 1);
        // digester.addCallParam(targetSchemas + "/Catalog", 0);

        digester.addObjectCreate(targetSchemas, XMLConfigDigester.CONFIG_NS_URI, ArrayList.class);

        final String schema = targetSchemas + "/FeatureType/schemaUri";
        digester.addCallMethod(schema, "add", 1);
        digester.addCallParam(schema, 0);
        // set the list of XSD file uris on AppSchemaDataAccessDTO
        digester.addSetNext(targetSchemas, "setTargetSchemasUris");
    }

    private void setSourceDataStoresRules(Digester digester) {
        final String dataStores = "AppSchemaDataAccess/sourceDataStores";
        digester.addObjectCreate(dataStores, XMLConfigDigester.CONFIG_NS_URI, ArrayList.class);

        // create a SourceDataStore for each DataStore tag
        digester.addObjectCreate(dataStores + "/DataStore", XMLConfigDigester.CONFIG_NS_URI,
                SourceDataStore.class);
        digester.addCallMethod(dataStores + "/DataStore/id", "setId", 1);
        digester.addCallParam(dataStores + "/DataStore/id", 0);

        digester.addObjectCreate(dataStores + "/DataStore/parameters",
                XMLConfigDigester.CONFIG_NS_URI, HashMap.class);
        digester.addCallMethod(dataStores + "/DataStore/parameters/Parameter", "put", 2);
        digester.addCallParam(dataStores + "/DataStore/parameters/Parameter/name", 0);
        digester.addCallParam(dataStores + "/DataStore/parameters/Parameter/value", 1);
        digester.addSetNext(dataStores + "/DataStore/parameters", "setParams");

        // isDataAccess is a flag to denote that we want to connect to the data access
        // that is connected to the data store specified
        digester.addCallMethod(dataStores + "/DataStore/isDataAccess", "setDataAccess", 1);
        digester.addCallParam(dataStores + "/DataStore/isDataAccess", 0);

        // add the SourceDataStore to the list
        digester.addSetNext(dataStores + "/DataStore", "add");

        // set the list of SourceDataStores for ComlexDataStoreDTO
        digester.addSetNext(dataStores, "setSourceDataStores");
    }

    private void setNamespacesRules(Digester digester) {
        final String ns = "AppSchemaDataAccess/namespaces";
        digester.addObjectCreate(ns, XMLConfigDigester.CONFIG_NS_URI, HashMap.class);
        digester.addCallMethod(ns + "/Namespace", "put", 2);
        digester.addCallParam(ns + "/Namespace/prefix", 0);
        digester.addCallParam(ns + "/Namespace/uri", 1);
        digester.addSetNext(ns, "setNamespaces");
    }

    private void setIncludedTypesRules(Digester digester) {
        final String includes = "AppSchemaDataAccess/includedTypes";
        digester.addObjectCreate(includes, XMLConfigDigester.CONFIG_NS_URI, ArrayList.class);
        // point to related type config path relative to this mapping file location
        final String includePath = includes + "/Include";
        digester.addCallMethod(includePath, "add", 1);
        digester.addCallParam(includePath, 0);
        digester.addSetNext(includes, "setIncludedTypes");
    }
}
