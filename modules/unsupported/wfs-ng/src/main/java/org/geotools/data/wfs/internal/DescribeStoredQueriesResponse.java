package org.geotools.data.wfs.internal;

import static org.geotools.data.wfs.internal.AbstractWFSStrategy.WFS_2_0_CONFIGURATION;
import static org.geotools.data.wfs.internal.Loggers.MODULE;
import static org.geotools.data.wfs.internal.Loggers.RESPONSES;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.opengis.wfs20.DescribeStoredQueriesResponseType;
import net.opengis.wfs20.QueryExpressionTextType;
import net.opengis.wfs20.StoredQueryDescriptionType;

import org.apache.commons.io.IOUtils;
import org.geotools.data.DataSourceException;
import org.geotools.data.ows.HTTPResponse;
import org.geotools.data.wfs.internal.parsers.EmfAppSchemaParser;
import org.geotools.ows.ServiceException;
import org.geotools.xml.Configuration;
import org.geotools.xml.DOMParser;
import org.opengis.feature.type.FeatureType;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DescribeStoredQueriesResponse extends WFSResponse {

	private DescribeStoredQueriesResponseType describeStoredQueriesResponse;
	
	private Map<String, List<FeatureType>> featureTypesPerStoredQueryId;
	
	public DescribeStoredQueriesResponse(WFSRequest originatingRequest, HTTPResponse response) throws IOException, ServiceException {
		super(originatingRequest, response);
		
		final WFSStrategy strategy = originatingRequest.getStrategy();
		final Configuration wfsConfiguration = strategy.getWfsConfiguration();
		
		MODULE.finer("Parsing DescribeStoredQueries response");
		try {
			final Document rawDocument;
			final byte[] rawResponse;
			{
				ByteArrayOutputStream buff = new ByteArrayOutputStream();
				InputStream inputStream = response.getResponseStream();
				try {
					IOUtils.copy(inputStream, buff);
				} finally {
					inputStream.close();
				}
				rawResponse = buff.toByteArray();
			}
			if (RESPONSES.isLoggable(Level.FINE)) {
				RESPONSES.fine("Full ListStoredQueries response: " + new String(rawResponse));
			}
			try {
				DocumentBuilderFactory builderFactory = DocumentBuilderFactory
						.newInstance();
				builderFactory.setNamespaceAware(true);
				builderFactory.setValidating(false);
				DocumentBuilder documentBuilder = builderFactory
						.newDocumentBuilder();
				rawDocument = documentBuilder.parse(new ByteArrayInputStream(
						rawResponse));
			} catch (Exception e) {
				throw new IOException("Error parsing capabilities document: "
						+ e.getMessage(), e);
			}

			
			describeStoredQueriesResponse = parseStoredQueries(rawDocument, WFS_2_0_CONFIGURATION);

			
			if (null == describeStoredQueriesResponse) {
				throw new IllegalStateException(
						"Unable to parse DescribeStoredQueriesResponse document");
			}

			featureTypesPerStoredQueryId = new HashMap<String, List<FeatureType>>();
			

			for (StoredQueryDescriptionType sqdt : describeStoredQueriesResponse.getStoredQueryDescription()) {
				String id = sqdt.getId();
				
				List<FeatureType> featureTypesForId = new ArrayList<FeatureType>();
				featureTypesPerStoredQueryId.put(id, featureTypesForId);
				
				for (QueryExpressionTextType qett : sqdt.getQueryExpressionText()) {
					for (QName remoteTypeName : qett.getReturnFeatureTypes()) {
						URL schemaLocation = findSchemaLocation(rawDocument, remoteTypeName.getNamespaceURI());
						
						FeatureType tmp = EmfAppSchemaParser.parse(wfsConfiguration, remoteTypeName,
								schemaLocation, null);
						
						featureTypesForId.add(tmp);
					}
				}
			}
			
		} finally {
			response.dispose();
		}
		
	}
	

	private URL findSchemaLocation(Document rawDocument, String namespaceURI) {
		return findSchemaLocation(rawDocument.getChildNodes(), namespaceURI);
	}

	private URL findSchemaLocation(NodeList nodes, String namespaceURI) {
		URL ret = null;
		for (int i = 0 ; i < nodes.getLength(); i++) {
			ret = findSchemaLocation(nodes.item(i), namespaceURI);
			if (ret != null) return ret;
		}

		return ret;
	}

	private URL findSchemaLocation(Node node, String namespaceURI)
	{
		NamedNodeMap attributes = node.getAttributes();
		Node attr = attributes.getNamedItemNS("http://www.w3.org/2001/XMLSchema-instance", "schemaLocation");
		if (attr == null) return null;
		
		URL ret = null;
		
		String [] parts = attr.getTextContent().split("\\s+");
		for (int i = 0; i < parts.length; i += 2) {
			if (namespaceURI.equals(parts[i])) {
				try {
					ret = new URL(parts[i+1]);
				} catch(MalformedURLException mue) {
					MODULE.log(Level.WARNING, "SchemaLocation for "+namespaceURI+" ("+parts[i+1]+") is malformed", mue);
				}
			}
		}
		
		return ret;
	}

	private DescribeStoredQueriesResponseType parseStoredQueries(Document document,
			Configuration wfsConfig) throws DataSourceException {
		DOMParser parser = new DOMParser(wfsConfig, document);
        final Object parsed;
        try {
            parsed = parser.parse();
        } catch (Exception e) {
            throw new DataSourceException("Exception parsing DescribeStoredQueriesResponse", e);
        }
        
        return (DescribeStoredQueriesResponseType)parsed;
	}
	
	public List<StoredQueryDescriptionType> getStoredQueryDescriptions() {
		return describeStoredQueriesResponse.getStoredQueryDescription();
	}
	
	public List<FeatureType> getFeatureTypes(String id) {
		List<FeatureType> ret = featureTypesPerStoredQueryId.get(id);
		if (ret == null) {
			ret = Collections.emptyList();
		}
		return ret;
	}

}
