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
package org.geotools.data.solr;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.client.solrj.request.schema.FieldTypeDefinition;
import org.apache.solr.client.solrj.request.schema.SchemaRequest;
import org.apache.solr.client.solrj.response.SolrResponseBase;
import org.apache.solr.common.SolrInputDocument;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This class contains helper methods typically used when dealing with Apache Solr integration
 * tests.
 */
public final class TestsSolrUtils {

    /**
     * Instantiates a new Apache Solr client using the provided base URL. The provided base URL
     * should point to a specific Solr core.
     *
     * @param baseUrl base URL of the Apache Solr instance, http://localhost:8983/solr/core
     * @return Apache Solr client instance
     */
    public static HttpSolrClient instantiateClient(String baseUrl) {
        HttpSolrClient client =
                new HttpSolrClient.Builder()
                        .withBaseSolrUrl(baseUrl)
                        .allowCompression(true)
                        .build();
        // we can use low timeouts values for tests
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);
        client.setFollowRedirects(true);
        return client;
    }

    /**
     * Performs an update request extracting the necessary Solr documents from the provided stream.
     * It is expected that the stream provides Solr documents encoded in the XML format:
     *
     * <p>
     *
     * <pre>{@code
     * (...)
     * <doc>
     *  <field name="id">1</field>
     *  <field name="status_s">active</field>
     *  <field name="vendor_s">D-Link</field>
     *  (...)
     * </doc>
     * }</pre>
     *
     * @param client Sorl client to use, should already point to the desired core
     * @param xml stream contain the Solr documents encoded in XML
     */
    public static void runUpdateRequest(HttpSolrClient client, InputStream xml) {
        UpdateRequest request = new UpdateRequest();
        // parse Solr documents from the provided XML stream
        List<SolrInputDocument> documents = parseDocuments(xml);
        request.add(documents);
        // execute the update requests, if something went wrong an exception will be throw
        runSolrRequest(client, request).throwIfNeeded();
    }

    /**
     * Create a field type named wkt capable of handling WKT geometries. If a field type named wkt
     * already exists its definition will be replaced.
     *
     * @param client Sorl client to use, should already point to the desired core
     */
    public static void createWktFieldType(HttpSolrClient client) {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("name", "wkt");
        attributes.put("class", "solr.SpatialRecursivePrefixTreeFieldType");
        attributes.put(
                "spatialContextFactory",
                "org.locationtech.spatial4j.context.jts.JtsSpatialContextFactory");
        // create or replace the field type definition
        createFieldType(client, attributes);
    }

    /**
     * Create field type named bbox capable of handling envelopes. If a field type named bbox
     * already exists its definition will be replaced.
     *
     * @param client Sorl client to use, should already point to the desired core
     */
    public static void createBboxFieldType(HttpSolrClient client) {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("name", "bbox");
        attributes.put("class", "solr.BBoxField");
        attributes.put("numberType", "double");
        attributes.put("geo", "true");
        // create or replace the field type definition
        createFieldType(client, attributes);
    }

    /**
     * Adds a new wkt field type to the schema, i.e. a field capable of handling WKT geometries. If
     * a field with the same name already exists its definition will be replaced.
     *
     * @param client Sorl client to use, should already point to the desired core
     * @param name name of the field
     */
    public static void createWktField(HttpSolrClient client, String name) {
        createField(client, name, "wkt");
    }

    /**
     * Adds a new bbox field type to the schema, i.e. a field capable of handling envelopes. If a
     * field with the same name already exists its definition will be replaced.
     *
     * @param client Sorl client to use, should already point to the desired core
     * @param name name of the field
     */
    public static void createBboxField(HttpSolrClient client, String name) {
        createField(client, name, "bbox");
    }

    /**
     * Helper method that opens an input stream for a classpath resource using the resource absolute
     * path.
     *
     * <p>An exception will be throw if the resource is not found.
     *
     * @param resourceAbsolutePath absolute path of the resource
     * @return resource input stream
     */
    static InputStream resourceToStream(String resourceAbsolutePath) {
        InputStream input = TestsSolrUtils.class.getResourceAsStream(resourceAbsolutePath);
        if (input == null) {
            // the resource was not found
            throw new RuntimeException(
                    String.format("Could not find resource '%s'.", resourceAbsolutePath));
        }
        return input;
    }

    /**
     * Helper method that creates a new field type using the provided attributes. If a field type
     * with the same name already exists its definition will be replaced.
     *
     * @param client Sorl client to use, should already point to the desired core
     * @param attributes attributes of the field type to create
     */
    public static void createFieldType(HttpSolrClient client, Map<String, Object> attributes) {
        FieldTypeDefinition typeDefinition = new FieldTypeDefinition();
        typeDefinition.setAttributes(attributes);
        // try to create the field type
        Response addResponse =
                runSolrRequest(client, new SchemaRequest.AddFieldType(typeDefinition));
        if (!addResponse.hasErrors()) {
            // no errors, which means that the field type was correctly created
            return;
        }
        // something bad happen, let's assume that a field type with the same name already exists
        Response replaceResponse =
                runSolrRequest(client, new SchemaRequest.ReplaceFieldType(typeDefinition));
        if (replaceResponse.hasErrors()) {
            // trying to replace the field type failed, let's throw an exception with all the
            // messages errors
            Response.throwIfNeeded(addResponse, replaceResponse);
        }
    }

    /**
     * Helper method that creates a new field using the provided name and type (field type name). If
     * a field with the same name already exists itd definition will be replaced.
     *
     * @param client Sorl client to use, should already point to the desired core
     * @param name name of the field
     * @param type field type name
     */
    public static void createField(HttpSolrClient client, String name, String type) {
        Map<String, Object> field = new HashMap<>();
        field.put("name", name);
        field.put("type", type);
        // try to create the field
        Response addResponse = runSolrRequest(client, new SchemaRequest.AddField(field));
        if (!addResponse.hasErrors()) {
            // no errors, which means that the field was correctly created
            return;
        }
        // something bad happen, let's assume that a field with the same name already exists
        Response replaceResponse = runSolrRequest(client, new SchemaRequest.ReplaceField(field));
        if (replaceResponse.hasErrors()) {
            // trying to replace the field definition failed, let's throw an exception with all the
            // messages errors
            Response.throwIfNeeded(addResponse, replaceResponse);
        }
    }

    /**
     * Helper method that just runs a Solr request taking care of the commit action and error
     * handling.
     *
     * <p>Note that an exception may be throw if something bad happen when executing the request on
     * the client side.
     *
     * @param client Sorl client to use, should already point to the desired core
     * @param request Solr request
     * @return response containing any found error or none
     */
    private static Response runSolrRequest(
            HttpSolrClient client, SolrRequest<? extends SolrResponseBase> request) {
        try {
            // execute the requests and parse is result
            Response response = Response.parse(request.process(client));
            if (response.hasErrors()) {
                // some thing bad happen with this request execution on the server side
                return response;
            }
            // everything looks good let's commit
            client.commit();
            return response;
        } catch (Exception exception) {
            // soem exception was thrown during the request execution or its commit
            throw new RuntimeException("Error executing Solr request.", exception);
        }
    }

    /**
     * Extracts Solr documents from a XML document.
     *
     * @param xml stream contain a XML document
     * @return list of extracted Solr document, this list may be empty but never NULL
     */
    private static List<SolrInputDocument> parseDocuments(InputStream xml) {
        ArrayList<SolrInputDocument> solrDocuments = new ArrayList<>();
        // parse the XMl document
        Document document = parseXmlDocument(xml);
        // get all the Solr documents XML nodes and parse them to a Solr document
        NodeList documentsNodes = document.getElementsByTagName("doc");
        for (int i = 0; i < documentsNodes.getLength(); i++) {
            Node documentNode = documentsNodes.item(i);
            if (documentNode.getNodeType() != Node.ELEMENT_NODE) {
                // not a Solr document, let's move to the next one
                continue;
            }
            SolrInputDocument solrDocument = new SolrInputDocument();
            NodeList fieldsNodes = documentNode.getChildNodes();
            // let's add the document XML node fields to the Solr document
            for (int j = 0; j < fieldsNodes.getLength(); j++) {
                Node fieldNode = fieldsNodes.item(j);
                if (fieldNode.getNodeType() == Node.ELEMENT_NODE) {
                    // this si a valid element node, let's extract the necessary information
                    Element field = (Element) fieldNode;
                    String fieldName = field.getAttribute("name");
                    String fieldValue = field.getTextContent();
                    // add the field to the Solr document
                    solrDocument.addField(fieldName, fieldValue);
                }
            }
            solrDocuments.add(solrDocument);
        }
        return solrDocuments;
    }

    /**
     * Helper method that just parsed a XML document to its DOM model.
     *
     * @param xml XML document
     * @return the document DOM model
     */
    private static Document parseXmlDocument(InputStream xml) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            // parse the XML document
            return builder.parse(xml);
        } catch (Exception exception) {
            throw new RuntimeException("Error parsing XML file.", exception);
        }
    }

    /**
     * Helper method that creates a geometry field type.
     *
     * @param client the Solr client
     */
    public static void createGeometryFieldType(HttpSolrClient client) {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("name", "geometry");
        attributes.put("class", "solr.SpatialRecursivePrefixTreeFieldType");
        attributes.put("geo", "true");
        attributes.put("maxDistErr", "0.001");
        attributes.put("distErrPct", "0.025");
        attributes.put("distanceUnits", "kilometers");
        attributes.put("spatialContextFactory", "JTS");
        // create or replace the field type definition
        createFieldType(client, attributes);
    }

    /**
     * Helper method that creates a new field in the target index schema.
     *
     * @param client the HTTP Solr client
     * @param name the field name
     * @param type the field type
     * @param multiValued TRUE if the type is multi valued, otherwise FALSE
     */
    public static void createField(
            HttpSolrClient client, String name, String type, boolean multiValued) {
        Map<String, Object> field = new HashMap<>();
        field.put("name", name);
        field.put("type", type);
        field.put("multiValued", multiValued ? "true" : "false");
        // try to create the field
        Response addResponse = runSolrRequest(client, new SchemaRequest.AddField(field));
        if (!addResponse.hasErrors()) {
            // no errors, which means that the field was correctly created
            return;
        }
        // something bad happen, let's assume that a field with the same name already exists
        Response replaceResponse = runSolrRequest(client, new SchemaRequest.ReplaceField(field));
        if (replaceResponse.hasErrors()) {
            // trying to replace the field definition failed, let's throw an exception with all the
            // messages errors
            Response.throwIfNeeded(addResponse, replaceResponse);
        }
    }

    /** Helper container class for Solr responses errors messages. */
    public static final class Response {

        private final boolean errors;
        private final String message;

        private static final Response NO_ERRORS = new Response(false, null);

        private Response(boolean errors, String message) {
            this.errors = errors;
            this.message = message;
        }

        private boolean hasErrors() {
            return errors;
        }

        private String getMessage() {
            return message;
        }

        /** If this response contains any message errors throw an exception. */
        private void throwIfNeeded() {
            if (errors) {
                throw new RuntimeException(
                        String.format(
                                "Something bad happen when executing Solr request '%s'.", message));
            }
        }

        /**
         * Extracts from a Sorl response any message errors.
         *
         * @param response Sorl response
         * @return a response container
         */
        private static Response parse(SolrResponseBase response) {
            if (response.getResponse().get("errors") != null) {
                // at least one error happen during the request execution
                return new Response(true, response.toString());
            }
            // the Solr response doesn't contain any error
            return NO_ERRORS;
        }

        /**
         * Merge all the errors messages from the provided responses containers and throw a single
         * exception with all of them.
         *
         * <p>If no messages errors can be found no exception will be throw.
         *
         * @param responses responses containers
         */
        private static void throwIfNeeded(Response... responses) {
            // merge all errors messages in a single string
            String errors =
                    Arrays.stream(responses)
                            .filter(Response::hasErrors)
                            .map(Response::getMessage)
                            .collect(Collectors.joining(", "));
            if (!errors.isEmpty()) {
                // at leats one message errors exists, let's throw an exception
                throw new RuntimeException(
                        String.format(
                                "Something bad happen when executing Solr request(s) '%s'.",
                                errors));
            }
        }
    }

    /** Helper method that creates a temporary directory. */
    public static File createTempDirectory(String prefix) {
        try {
            return Files.createTempDirectory(prefix).toFile();
        } catch (Exception exception) {
            throw new RuntimeException("Error creating temporary directory.", exception);
        }
    }

    /**
     * Removes all the data indexed in the target Apache Solr core. If the index contains more than
     * 25 documents the operation is aborted.
     *
     * @param client HTTP Apache Solr client, the client should should be already pointing at the
     *     correct core
     */
    public static void cleanIndex(HttpSolrClient client) {
        // get the number of documents indexed in the target core
        SolrQuery query = new SolrQuery("*:*");
        // don't return any results
        query.setRows(0);
        long indexed;
        try {
            // get the number of results that match the query
            indexed = client.query(query).getResults().getNumFound();
        } catch (Exception exception) {
            throw new RuntimeException(
                    "Error counting the number of document indexed int he Apache Solr target core.",
                    exception);
        }
        if (indexed > 25) {
            // we have problem, this doesn't looks like a test Apache Solr core
            throw new RuntimeException(
                    "The target core contains more than 25 documents, "
                            + "please double check the correct core is used and manually delete all documents.");
        }
        try {
            // remove all the index stations data
            client.deleteByQuery("*:*");
            client.commit();
        } catch (Exception exception) {
            throw new RuntimeException("Error removing Apache Solr indexed data.", exception);
        }
    }
}
