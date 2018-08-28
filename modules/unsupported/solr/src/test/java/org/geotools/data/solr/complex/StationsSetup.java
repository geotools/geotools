/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.solr.complex;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import org.apache.commons.io.IOUtils;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.geotools.data.solr.TestsSolrUtils;

/**
 * Helper class that will setup the stations use case, i.e. configure the necessary schema and load
 * the data in Apache Solr and prepare the App-Schema mappings files and instantiate the App-Schema
 * store.
 */
public final class StationsSetup {

    private StationsSetup() {}

    /**
     * Configures the Apache Solr station index, creating the necessary schema and indexing the
     * station data.
     *
     * @param client HTTP Apache Solr client, the client should should be already pointing at the
     *     correct core
     */
    public static void setupSolrIndex(HttpSolrClient client) {
        TestsSolrUtils.cleanIndex(client);
        setupStationsIndexSchema(client);
        indexStationsData(client);
    }

    /**
     * Prepare the App-Schema configuration files associated with the stations use case. The files
     * will be created inside the provided directory.
     *
     * @param testDirectory where to create the App-Schema configuration files
     * @param solrUrl the Apache Solr core URL
     */
    public static void prepareAppSchemaFiles(File testDirectory, String solrUrl) {
        Map<String, String> context = new HashMap<>();
        context.put("SOLR_URL", solrUrl);
        context.put("CORE_NAME", getUrlBasePathLastElement(solrUrl));
        // add the stations schema
        copyResource(
                "/solr/complex/stations.xsd",
                new File(testDirectory, "stations.xsd"),
                Collections.emptyMap());
        // add the mappings file, replacing the ${SOLR_URL} placeholder
        copyResource(
                "/solr/complex/mappings.xml", new File(testDirectory, "mappings.xml"), context);
    }

    /** Helper method that creates the stations index Apache Solr index schema. */
    private static void setupStationsIndexSchema(HttpSolrClient client) {
        // make sure that the JTS based geometry field type is available
        TestsSolrUtils.createGeometryFieldType(client);
        // get the index schema specification
        SchemaField.SchemaFields schemaFields =
                parseXmlResource(
                        "/solr/complex/stations_schema.xml", SchemaField.SchemaFields.class);
        for (SchemaField schemaField : schemaFields.getFields()) {
            // create the field on the target Apache Solr index schema
            TestsSolrUtils.createField(
                    client, schemaField.getName(), schemaField.getType(), schemaField.getMulti());
        }
    }

    /** Helper method that index the stations data in Apache Solr. */
    private static void indexStationsData(HttpSolrClient client) {
        // parse the stations data
        Station.Stations stations =
                parseXmlResource("/solr/complex/stations_data.xml", Station.Stations.class);
        for (Station station : stations.getStations()) {
            try {
                // index the station data in Apache Solr
                client.add(station.toSolrDoc());
            } catch (Exception exception) {
                throw new RuntimeException(
                        "Error indexing station data in apache Solr.", exception);
            }
        }
        // force Apache solr to index any pending document
        try {
            client.commit();
        } catch (Exception exception) {
            throw new RuntimeException(
                    "Error when forcing Apache Solr to index any pending document.", exception);
        }
    }

    /**
     * Helper method that takes care of parsing a XML file using JAXB, the result type should class
     * should have the necessary annotations.
     */
    @SuppressWarnings("unchecked")
    private static <T> T parseXmlResource(String resource, Class<T> resultType) {
        try (InputStream input = StationsSetup.class.getResourceAsStream(resource)) {
            JAXBContext context = JAXBContext.newInstance(resultType);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (T) unmarshaller.unmarshal(input);
        } catch (Exception exception) {
            throw new RuntimeException(
                    String.format("Error parsing resource '%s'.", resource), exception);
        }
    }

    /**
     * Helper method that coies a resource content to provided destination, if a context is provided
     * placeholders in the resource content will be resolved against it.
     */
    private static void copyResource(
            String resource, File destination, Map<String, String> context) {
        try (InputStream input = StationsSetup.class.getResourceAsStream(resource);
                OutputStream output = new FileOutputStream(destination)) {
            if (context.isEmpty()) {
                // no context provided write the resource content as is
                IOUtils.copy(input, output);
            } else {
                // we have context, substitute placeholders with the provided context
                String content = IOUtils.toString(input);
                content = replace(content, context);
                IOUtils.write(content, output);
            }
        } catch (Exception exception) {
            throw new RuntimeException("Error copying resource to destination.", exception);
        }
    }

    /** Helper method that just replace placeholders ${...} in a text using the provided context. */
    private static String replace(String text, Map<String, String> context) {
        for (Map.Entry<String, String> entry : context.entrySet()) {
            String key = String.format("\\$\\{%s}", entry.getKey());
            // replace all the placeholders occurrences
            text = text.replaceAll(key, entry.getValue());
        }
        return text;
    }

    /** Returns the last path of an URL, or NULL if no path is found. */
    private static String getUrlBasePathLastElement(String url) {
        int index = url.lastIndexOf("/");
        if (index == url.length() - 1) {
            // let's see if we have valid path before
            url = url.substring(0, index);
            index = url.lastIndexOf("/");
        }
        if (index < 0) {
            // no path available, we are done
            return null;
        }
        // return the path
        return url.substring(index + 1, url.length());
    }
}
