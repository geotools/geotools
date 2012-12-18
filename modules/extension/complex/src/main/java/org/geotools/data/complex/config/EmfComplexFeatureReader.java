package org.geotools.data.complex.config;

/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2011, Open Source Geospatial Foundation (OSGeo)
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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import org.geotools.xml.AppSchemaCatalog;
import org.geotools.xml.AppSchemaConfiguration;
import org.geotools.xml.AppSchemaResolver;
import org.geotools.xml.Binding;
import org.geotools.xml.Configuration;
import org.geotools.xml.SchemaIndex;
import org.geotools.xml.Schemas;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.xmlpull.mxp1.MXParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 * Parses an application schema given by a gtxml {@link Configuration} into a set of {@link AttributeType}s and {@link AttributeDescriptor}s.
 * <p>
 * All the XSD schema locations that comprise the application schema are obtained from the main {@link Configuration} and its dependencies.
 * </p>
 * <p>
 * Of particular interest might be the {@link ApplicationSchemaConfiguration} object, which allows to provide the location of the root xsd schema for
 * a given application schema.
 * </p>
 * 
 * @author Gabriel Roldan
 * @version $Id$
 * 
 * 
 * 
 * @source $URL$ http://svn.geotools.org/geotools/branches/2.4.x/modules/unsupported/community-schemas /community
 *         -schema-ds/src/main/java/org/geotools/data/complex/config/EmfAppSchemaReader.java $
 * @since 2.4
 */
public class EmfComplexFeatureReader {
    /**
     * The initial resolver has support for only file and classpath resolution. Anything more than a test should probably set this to something more
     * useful.
     */
    private AppSchemaResolver resolver = new AppSchemaResolver();

    protected EmfComplexFeatureReader() {
        // do nothing
    }

    public AppSchemaResolver getResolver() {
        return resolver;
    }

    public void setResolver(AppSchemaResolver resolver) {
        this.resolver = resolver;
    }

    /**
     * Set resolver based on catalog. Use this for testing only, because it does not support cached downloads.
     * 
     * @param catalogLocation
     */
    public void setResolver(URL catalogLocation) {
        this.resolver = new AppSchemaResolver(AppSchemaCatalog.build(catalogLocation));
    }

    /**
     * Parses the GML schema represented by the <code>configuration</code>'s {@link Configuration#getSchemaFileURL() schema location} into a
     * {@link SchemaIndex}.
     * 
     * @param configuration configuration object used to access the XSDSchema to parse. This configuration object might contain {@link Binding}s
     * @throws IOException
     */
    public SchemaIndex parse(Configuration configuration) throws IOException {
        // find out the schemas involved in the app schema configuration
        final SchemaIndex appSchemaIndex = Schemas.findSchemas(configuration);

        return appSchemaIndex;
    }

    /**
     * Parses the schema referenced by <code>location</code> into a {@link SchemaIndex}
     * 
     * @param nameSpace the location namespace
     * @param location the physical location of the root xsd schema that comprises the application schema to parse.
     * @throws IOException if any non recoverable problem occurs while parsing the application schema pointed out by <code>location</code> or one of
     *         its dependencies.
     */
    public SchemaIndex parse(String nameSpace, String schemaLocation) throws IOException {
        AppSchemaConfiguration configuration = new AppSchemaConfiguration(nameSpace,
                schemaLocation, resolver);
        return parse(configuration);
    }

    /**
     * Parses the gml schema referenced by <code>location</code> into a {@link SchemaIndex}
     * 
     * @param location the physical location of the root xsd schema that comprises the application schema to parse.
     * @throws IOException if any non recoverable problem occurs while parsing the application schema pointed out by <code>location</code> or one of
     *         its dependencies.
     */
    public SchemaIndex parse(final URL location) throws IOException {

        final String nameSpace = findSchemaNamespace(location);

        final String schemaLocation = location.toExternalForm();

        return parse(nameSpace, schemaLocation);
    }

    /**
     * Finds out the targetNamespace of the xsd schema referenced by <code>location</code>
     * 
     * @param location
     * @return
     * @throws IOException
     */
    public String findSchemaNamespace(URL location) throws IOException {
        String targetNamespace = null;
        // parse some of the instance document to find out the
        // schema location
        URL resolvedLocation = new URL(resolver.resolve(location.toExternalForm()));
        InputStream input = resolvedLocation.openStream();

        // create stream parser
        XmlPullParser parser = null;

        try {
            // parse root element
            parser = new MXParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
            parser.setInput(input, "UTF-8");
            parser.nextTag();

            // look for schema location
            for (int i = 0; i < parser.getAttributeCount(); i++) {
                if ("targetNamespace".equals(parser.getAttributeName(i))) {
                    targetNamespace = parser.getAttributeValue(i);
                    break;
                }
            }
            // reset input stream
            parser.setInput(null);
        } catch (XmlPullParserException e) {
            String msg = "Cannot find target namespace for schema document " + resolvedLocation;
            throw (RuntimeException) new RuntimeException(msg).initCause(e);
        } finally {
            input.close();
        }
        if (targetNamespace == null) {
            throw new IllegalArgumentException(
                    "Input document does not specifies a targetNamespace");
        }
        return targetNamespace;
    }

    public static EmfComplexFeatureReader newInstance() {
        return new EmfComplexFeatureReader();
    }

}
