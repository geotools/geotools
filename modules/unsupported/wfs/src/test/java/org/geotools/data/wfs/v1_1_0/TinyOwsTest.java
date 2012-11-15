package org.geotools.data.wfs.v1_1_0;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;

import org.apache.commons.io.IOUtils;
import org.custommonkey.xmlunit.XMLAssert;
import org.geotools.data.Query;
import org.geotools.data.ows.HTTPClient;
import org.geotools.data.ows.HTTPResponse;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.wfs.MockHttpClient;
import org.geotools.data.wfs.MockHttpResponse;
import org.geotools.data.wfs.WFSDataStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.GeoTools;
import org.geotools.filter.identity.FeatureIdImpl;
import org.geotools.test.TestData;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.identity.FeatureId;
import org.xml.sax.SAXException;


public class TinyOwsTest {

    private String typeName = "comuni:comuni11";
        
    private WFSDataStore getWFSDataStore(HTTPClient httpClient) throws IOException {
        URL capabilitiesUrl = new URL("http://127.0.0.1:8888/cgi-bin/tinyows?service=WFS&version=1.1.0&REQUEST=GetCapabilities");        
        
        HTTPResponse httpResponse = httpClient.get(capabilitiesUrl);
        InputStream inputStream = httpResponse.getResponseStream();
        
        byte[] wfsCapabilitiesRawData = IOUtils.toByteArray(inputStream);
        InputStream capsIn = new ByteArrayInputStream(wfsCapabilitiesRawData);
        WFS_1_1_0_DataStore wfs = new WFS_1_1_0_DataStore(new WFS_1_1_0_Protocol(capsIn, httpClient, null) {
            @Override
            public URL getDescribeFeatureTypeURLGet(String typeName) {
                return TestData.getResource(this, "tinyows/DescribeFeatureType.xsd");
            }            
        });
        wfs.setPreferPostOverGet(true);
        return wfs;
    }

    @Test
    public void testGetCapabilities() throws Exception {
        WFSDataStore wfs = getWFSDataStore(new TinyOwsMockHttpClient());       
        String types[] = wfs.getTypeNames();
        Assert.assertEquals(1, types.length);
        Assert.assertEquals("comuni:comuni11", types[0]);
    }
    
    private void assertXMLEqual(String expectedXmlResource, String actualXml) throws IOException {
        String control = IOUtils.toString(TestData.getResource(this, expectedXmlResource));
        control = control.replace("${project.version}", GeoTools.getVersion().toString());
        try {
            XMLAssert.assertXMLEqual(control, actualXml);
        } catch (SAXException e) {
            e.printStackTrace();
            throw new IOException(e);
        }        
    }
    
    @Test
    public void testGetFirstFeatures() throws Exception {        
        final String queryXml = "<wfs:Query srsName=\"urn:ogc:def:crs:EPSG::3857\" typeName=\"comuni:comuni11\"/>";
        WFSDataStore wfs = getWFSDataStore(new TinyOwsMockHttpClient() {        
            @Override
            public HTTPResponse post(URL url, InputStream postContent, String postContentType) throws IOException {
                String request = new String(IOUtils.toByteArray(postContent), "UTF-8");
                if (stringContains(request, 
                        "<wfs:GetFeature",
                        "maxFeatures=\"20\"",
                        "resultType=\"hits\"",
                        queryXml)) 
                {
                    assertXMLEqual("tinyows/CountFirstFeaturesRequest.xml", request);
                    return new MockHttpResponse(TestData.getResource(this, "tinyows/CountFirstFeatures.xml"), "text/xml");
                } 
                else if (stringContains(request, 
                        "<wfs:GetFeature",
                        "maxFeatures=\"20\"",
                        "resultType=\"results\"",
                        queryXml)) 
                {
                    assertXMLEqual("tinyows/GetFirstFeaturesRequest.xml", request);                    
                    return new MockHttpResponse(TestData.getResource(this, "tinyows/GetFirstFeatures.xml"), "text/xml");
                } else {
                    return super.post(url, new ByteArrayInputStream(request.getBytes("UTF-8")), postContentType);
                }
            }        
        });
        
        SimpleFeatureSource source = wfs.getFeatureSource(typeName);
        
        Query query = new Query(typeName, Filter.INCLUDE, 20, Query.ALL_NAMES, "my query");
        iterate(source.getFeatures(query), 20, true);        
    }
    
    @Test
    public void testGetFeatureByIncludeAndOperatorAndInclude() throws Exception {
        WFSDataStore wfs = getWFSDataStore(new TinyOwsMockHttpClient() {
            @Override
            public HTTPResponse post(URL url, InputStream postContent, String postContentType) throws IOException {
                String request = new String(IOUtils.toByteArray(postContent), "UTF-8");
                if (isResultsRequest(request, 
                        "<wfs:GetFeature",
                        "maxFeatures=\"20\"",
                        "resultType=\"results\"",
                        "<ogc:PropertyIsGreaterThan")) {
                    assertXMLEqual("tinyows/GetFeatureIncludeAndPropertyGreaterThanAndIncludeRequest.xml", request);
                    return new MockHttpResponse(TestData.getResource(this, "tinyows/GetFirstFeatures.xml"), "text/xml");
                } else {
                    return super.post(url, postContent, postContentType);
                }
            }
        });
        
        SimpleFeatureSource source = wfs.getFeatureSource(typeName);

        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        Filter and = ff.and(
                Arrays.asList(Filter.INCLUDE, 
                ff.greater(ff.property("gid"), ff.literal(0)), 
                Filter.INCLUDE));
        Query query = new Query(typeName, and, 20, Query.ALL_NAMES, "my query");
        iterate(source.getFeatures(query), 20, false);        
    }
    
    @Test
    public void testGetFeatureById() throws Exception {        
        WFSDataStore wfs = getWFSDataStore(new TinyOwsMockHttpClient());
        
        SimpleFeatureSource source = wfs.getFeatureSource(typeName);

        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        Set<FeatureId> fids = new HashSet<FeatureId>();
        fids.add(new FeatureIdImpl("comuni11.2671"));
        Query query = new Query(typeName, ff.id(fids));
        iterate(source.getFeatures(query), 1, true);
    }    

    @Test
    public void testGetFeaturesByBBox() throws Exception {
        final String[] queryTokens = { "<ogc:BBOX>", 
                "<ogc:PropertyName>the_geom</ogc:PropertyName>", 
                "<gml:Envelope srsDimension=\"2\" srsName=\"urn:x-ogc:def:crs:EPSG:3857\">",
                "<gml:lowerCorner>4623055.0 815134.0</gml:lowerCorner>",
                "<gml:upperCorner>4629904.0 820740.0</gml:upperCorner>" };
        WFSDataStore wfs = getWFSDataStore(new TinyOwsMockHttpClient() {
            @Override
            public HTTPResponse post(URL url, InputStream postContent, String postContentType) throws IOException {
                String request = new String(IOUtils.toByteArray(postContent), "UTF-8");
                if (isHitsRequest(request, queryTokens)) {
                    assertXMLEqual("tinyows/CountFeaturesByBBoxRequest.xml", request);  
                    return new MockHttpResponse(TestData.getResource(this, "tinyows/CountFeaturesByBBox.xml"), "text/xml");
                } else if (isResultsRequest(request, queryTokens)) {
                    assertXMLEqual("tinyows/GetFeaturesByBBoxRequest.xml", request);  
                    return new MockHttpResponse(TestData.getResource(this, "tinyows/GetFeaturesByBBox.xml"), "text/xml");
                } else {
                    return super.post(url, new ByteArrayInputStream(request.getBytes("UTF-8")), postContentType);
                }
            }            
        });
        
        SimpleFeatureSource source = wfs.getFeatureSource(typeName);
        SimpleFeature sf = getSampleSimpleFeature(source);
        
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        PropertyName bboxProperty = ff.property(sf.getDefaultGeometryProperty().getName());
        Query query = new Query(typeName, ff.bbox(bboxProperty, sf.getBounds()));
        iterate(source.getFeatures(query), 6, true);        
    }

    @Test
    public void testGetFeaturesByIdAndBBox() throws Exception {
        final String[] queryTokens = { "<ogc:BBOX>", 
                "<ogc:PropertyName>the_geom</ogc:PropertyName>", 
                "<gml:Envelope srsDimension=\"2\" srsName=\"urn:x-ogc:def:crs:EPSG:3857\">",
                "<gml:lowerCorner>4623055.0 815134.0</gml:lowerCorner>",
                "<gml:upperCorner>4629904.0 820740.0</gml:upperCorner>" };
        WFSDataStore wfs = getWFSDataStore(new TinyOwsMockHttpClient() {
            @Override
            public HTTPResponse post(URL url, InputStream postContent, String postContentType) throws IOException {
                String request = new String(IOUtils.toByteArray(postContent), "UTF-8");
                if (isResultsRequest(request, queryTokens)) {
                    assertXMLEqual("tinyows/GetFeaturesByBBoxRequest.xml", request);  
                    return new MockHttpResponse(TestData.getResource(this, "tinyows/GetFeaturesByBBox.xml"), "text/xml");
                } else {
                    return super.post(url, new ByteArrayInputStream(request.getBytes("UTF-8")), postContentType);
                }
            }            
        });
        
        SimpleFeatureSource source = wfs.getFeatureSource(typeName);
        SimpleFeature sf = getSampleSimpleFeature(source);
        
        Set<FeatureId> fids = new HashSet<FeatureId>();
        fids.add(new FeatureIdImpl("comuni11.2671"));
        
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        PropertyName bboxProperty = ff.property(sf.getDefaultGeometryProperty().getName());
        Query query = new Query(typeName, ff.and(
                ff.id(fids), 
                ff.bbox(bboxProperty, sf.getBounds())));
        iterate(source.getFeatures(query), 1, false);        
    }    
    
    @Test
    public void testGetFeaturesByBBoxAndId() throws Exception {
        final String[] queryTokens = { "<ogc:BBOX>", 
                "<ogc:PropertyName>the_geom</ogc:PropertyName>", 
                "<gml:Envelope srsDimension=\"2\" srsName=\"urn:x-ogc:def:crs:EPSG:3857\">",
                "<gml:lowerCorner>4623055.0 815134.0</gml:lowerCorner>",
                "<gml:upperCorner>4629904.0 820740.0</gml:upperCorner>" };
        WFSDataStore wfs = getWFSDataStore(new TinyOwsMockHttpClient() {
            @Override
            public HTTPResponse post(URL url, InputStream postContent, String postContentType) throws IOException {
                String request = new String(IOUtils.toByteArray(postContent), "UTF-8");
                if (isResultsRequest(request, queryTokens)) {
                    assertXMLEqual("tinyows/GetFeaturesByBBoxRequest.xml", request);  
                    return new MockHttpResponse(TestData.getResource(this, "tinyows/GetFeaturesByBBox.xml"), "text/xml");
                } else {
                    return super.post(url, new ByteArrayInputStream(request.getBytes("UTF-8")), postContentType);
                }
            }            
        });
        
        SimpleFeatureSource source = wfs.getFeatureSource(typeName);
        SimpleFeature sf = getSampleSimpleFeature(source);
        
        Set<FeatureId> fids = new HashSet<FeatureId>();
        fids.add(new FeatureIdImpl("comuni11.2671"));
        
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        PropertyName bboxProperty = ff.property(sf.getDefaultGeometryProperty().getName());
        Query query = new Query(typeName, ff.and(
                ff.bbox(bboxProperty, sf.getBounds()), 
                ff.id(fids)));
        iterate(source.getFeatures(query), 1, false);        
    }    

    @Test
    public void testGetFeatures() throws Exception {
        final String[] queryTokens = { "<ogc:BBOX>", 
                "<ogc:PropertyName>the_geom</ogc:PropertyName>", 
                "<gml:Envelope srsDimension=\"2\" srsName=\"urn:x-ogc:def:crs:EPSG:3857\">",
                "<gml:lowerCorner>4623055.0 815134.0</gml:lowerCorner>",
                "<gml:upperCorner>4629904.0 820740.0</gml:upperCorner>" };
        WFSDataStore wfs = getWFSDataStore(new TinyOwsMockHttpClient() {
            @Override
            public HTTPResponse post(URL url, InputStream postContent, String postContentType) throws IOException {
                String request = new String(IOUtils.toByteArray(postContent), "UTF-8");
                if (isHitsRequest(request, queryTokens)) {
                    return new MockHttpResponse(TestData.getResource(this, "tinyows/CountFeaturesByBBox.xml"), "text/xml");
                } else if (isResultsRequest(request, queryTokens)) {
                    return new MockHttpResponse(TestData.getResource(this, "tinyows/GetFeaturesByBBox.xml"), "text/xml");
                } else {
                    return super.post(url, new ByteArrayInputStream(request.getBytes("UTF-8")), postContentType);
                }
            }            
        });
        
        SimpleFeatureSource source = wfs.getFeatureSource(typeName);
        SimpleFeature sf = getSampleSimpleFeature(source);
        
        Set<FeatureId> fids = new HashSet<FeatureId>();
        fids.add(new FeatureIdImpl("comuni11.2671"));
        
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        PropertyName bboxProperty = ff.property(sf.getDefaultGeometryProperty().getName());
        Query query = new Query(typeName, ff.and(
                ff.greater(ff.property("gid"), ff.literal(0)), 
                ff.and(
                        ff.bbox(bboxProperty, sf.getBounds()),
                        ff.id(fids))));        
        iterate(source.getFeatures(query), 1, true); 
        
        query = new Query(typeName, ff.and(
                ff.bbox(bboxProperty, sf.getBounds()), 
                ff.or(
                        ff.bbox(bboxProperty, sf.getBounds()),
                        ff.id(fids))));        
        iterate(source.getFeatures(query), 6, true);
    }    
    
    private SimpleFeature getSampleSimpleFeature(SimpleFeatureSource source) throws IOException {
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        Set<FeatureId> fids = new HashSet<FeatureId>();
        fids.add(new FeatureIdImpl("comuni11.2671"));
        Query query = new Query(typeName, ff.id(fids));
        SimpleFeatureIterator reader = source.getFeatures(query).features();
        try {
            return reader.next();
        } finally {
            reader.close();
        }
    }    
    
    private boolean isHitsRequest(String text, String ... tokens) {
        return stringContains(text, 
                "<wfs:GetFeature",
                "resultType=\"hits\"", 
                "<wfs:Query srsName=\"urn:ogc:def:crs:EPSG::3857\" typeName=\"comuni:comuni11\"") &&
                stringContains(text, tokens);
    }
    
    private boolean isResultsRequest(String text, String ... tokens) {
        return stringContains(text, 
                "<wfs:GetFeature",
                "resultType=\"results\"", 
                "<wfs:Query srsName=\"urn:ogc:def:crs:EPSG::3857\" typeName=\"comuni:comuni11\"") &&
                stringContains(text, tokens);
    }
    
    private boolean stringContains(String text, String ... tokens) {
        for (String token : tokens) {
            if (!text.contains(token)) {
                return false;
            }
        }
        return true;
    }
    
    private static SimpleFeature iterate(SimpleFeatureCollection features, int expectedSize, boolean getSize) {
        int size = -1;
        if (getSize) {
            size = features.size();
            if (size > -1) {
                Assert.assertEquals(expectedSize, size);
            }
        }
            
        size = 0;
        SimpleFeatureIterator reader = features.features();
        SimpleFeature sf = null;
        try {
            while (reader.hasNext()) {
                if (sf == null) {
                    sf = reader.next();
                } else {
                    reader.next().getIdentifier();
                }
                size++;
            }
        } finally {
            reader.close();
        }  
        
        Assert.assertEquals(expectedSize, size);
        
        return sf;
    }
    
    class TinyOwsMockHttpClient extends MockHttpClient {
        @Override
        public HTTPResponse get(URL url) throws IOException {
            if (url.getQuery().contains("REQUEST=GetCapabilities")) {
                return new MockHttpResponse(TestData.getResource(this, "tinyows/GetCapabilities.xml"), "text/xml");
            } else {
                return super.get(url);
            }
        }
        
        @Override
        public HTTPResponse post(URL url, InputStream postContent, String postContentType) throws IOException {
            String query = "<ogc:FeatureId fid=\"comuni11.2671\"/>";
            String request = new String(IOUtils.toByteArray(postContent), "UTF-8");
            if (isHitsRequest(request, query)) 
            {
                assertXMLEqual("tinyows/CountFeatureByIdRequest.xml", request);
                return new MockHttpResponse(TestData.getResource(this, "tinyows/CountFeatureById.xml"), "text/xml");
            } 
            else if (isResultsRequest(request, query)) 
            {
                assertXMLEqual("tinyows/GetFeatureByIdRequest.xml", request);
                return new MockHttpResponse(TestData.getResource(this, "tinyows/GetFeatureById.xml"), "text/xml");
            } 
            else 
            {
                return super.post(url, new ByteArrayInputStream(request.getBytes("UTF-8")), postContentType);
            }
        }                
    }
}