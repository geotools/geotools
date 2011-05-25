package org.geotools.data.wfs.v1_1_0;

import static org.geotools.data.wfs.protocol.wfs.WFSOperationType.DESCRIBE_FEATURETYPE;
import static org.geotools.data.wfs.v1_1_0.DataTestSupport.CUBEWERX_GOVUNITCE;
import static org.geotools.data.wfs.v1_1_0.DataTestSupport.CUBEWERX_ROADSEG;
import static org.geotools.data.wfs.v1_1_0.DataTestSupport.GEOS_ARCHSITES;
import static org.geotools.data.wfs.v1_1_0.DataTestSupport.GEOS_POI;
import static org.geotools.data.wfs.v1_1_0.DataTestSupport.GEOS_ROADS;
import static org.geotools.data.wfs.v1_1_0.DataTestSupport.GEOS_STATES;
import static org.geotools.data.wfs.v1_1_0.DataTestSupport.GEOS_TASMANIA_CITIES;
import static org.geotools.data.wfs.v1_1_0.DataTestSupport.GEOS_TIGER_ROADS;
import static org.geotools.data.wfs.v1_1_0.DataTestSupport.createTestProtocol;
import static org.geotools.data.wfs.v1_1_0.DataTestSupport.wfs;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.geotools.data.DefaultQuery;
import org.geotools.data.wfs.protocol.http.DefaultHTTPProtocol;
import org.geotools.data.wfs.protocol.http.HTTPProtocol;
import org.geotools.data.wfs.protocol.http.HTTPResponse;
import org.geotools.data.wfs.protocol.wfs.GetFeature;
import org.geotools.data.wfs.protocol.wfs.Version;
import org.geotools.data.wfs.protocol.wfs.WFSResponse;
import org.geotools.data.wfs.protocol.wfs.GetFeature.ResultType;
import org.geotools.data.wfs.v1_1_0.DataTestSupport.TestHttpProtocol;
import org.geotools.data.wfs.v1_1_0.DataTestSupport.TestHttpResponse;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.v1_1.OGCConfiguration;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.test.TestData;
import org.geotools.wfs.WFS;
import org.geotools.xml.Parser;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.capability.FilterCapabilities;
import org.opengis.filter.capability.GeometryOperand;
import org.opengis.filter.capability.SpatialCapabilities;
import org.opengis.filter.capability.SpatialOperator;
import org.opengis.filter.capability.SpatialOperators;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * Unit test suite for {@link WFS_1_1_0_Protocol}
 * 
 * @author Gabriel Roldan (OpenGeo)
 * @since 2.6.x
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/unsupported/wfs-ng/src/test/java/org/geotools/data/wfs/v1_1_0/WFS_1_1_0_ProtocolTest.java $
 */
@SuppressWarnings("nls")
public class WFS_1_1_0_ProtocolTest {

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test method for
     * {@link WFS_1_1_0_Protocol#WFS_1_1_0_Protocol(java.io.InputStream, org.geotools.data.wfs.protocol.http.HTTPProtocol)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public void testWFS_1_1_0_Protocol() throws IOException {
        try {
            createTestProtocol(GEOS_STATES.SCHEMA);
            fail("Excpected IOException as a capabilities document was not provided");
        } catch (IOException e) {
            assertTrue(true);
        }
        try {
            InputStream badData = new ByteArrayInputStream(new byte[1024]);
            HTTPProtocol connFac = new DefaultHTTPProtocol();
            new WFS_1_1_0_Protocol(badData, connFac);
            fail("Excpected IOException as a capabilities document was not provided");
        } catch (IOException e) {
            assertTrue(true);
        }

        createTestProtocol(GEOS_ARCHSITES.CAPABILITIES);
        assertNotNull(wfs);
        assertNotNull(((WFS_1_1_0_Protocol) wfs).capabilities);

    }

    /**
     * Test method for {@link WFS_1_1_0_Protocol#getServiceVersion()}.
     * 
     * @throws IOException
     */
    @Test
    public void testGetServiceVersion() throws IOException {
        createTestProtocol(GEOS_ARCHSITES.CAPABILITIES);
        assertSame(Version.v1_1_0, wfs.getServiceVersion());
    }

    /**
     * Test method for {@link WFS_1_1_0_Protocol#getServiceTitle()}.
     * 
     * @throws IOException
     */
    @Test
    public void testGetServiceTitle() throws IOException {
        createTestProtocol(GEOS_ARCHSITES.CAPABILITIES);
        assertEquals("My GeoServer WFS", wfs.getServiceTitle());
    }

    /**
     * Test method for {@link WFS_1_1_0_Protocol#getServiceAbstract()} .
     * 
     * @throws IOException
     */
    @Test
    public void testGetServiceAbstract() throws IOException {
        createTestProtocol(GEOS_ARCHSITES.CAPABILITIES);
        assertEquals("This is a description of your Web Feature Server.", wfs.getServiceAbstract()
                .trim());
    }

    /**
     * Test method for {@link WFS_1_1_0_Protocol#getServiceKeywords()} .
     * 
     * @throws IOException
     */
    @Test
    public void testGetServiceKeywords() throws IOException {
        createTestProtocol(GEOS_ARCHSITES.CAPABILITIES);
        Set<String> serviceKeywords = wfs.getServiceKeywords();
        assertNotNull(serviceKeywords);
        assertEquals(3, serviceKeywords.size());
        assertTrue(serviceKeywords.contains("WFS"));
        assertTrue(serviceKeywords.contains("WMS"));
        assertTrue(serviceKeywords.contains("GEOSERVER"));
    }

    /**
     * Test method for {@link WFS_1_1_0_Protocol#getServiceProviderUri()}.
     * 
     * @throws IOException
     */
    @Test
    public void testGetServiceProviderUri() throws IOException {
        createTestProtocol(GEOS_ARCHSITES.CAPABILITIES);
        assertNotNull(wfs.getServiceProviderUri());
        assertEquals("http://www.geoserver.org", wfs.getServiceProviderUri().toString());
    }

    /**
     * Test method for {@link WFS_1_1_0_Protocol#getSupportedGetFeatureOutputFormats()} .
     * 
     * @throws IOException
     */
    @Test
    public void testGetSupportedGetFeatureOutputFormats() throws IOException {
        createTestProtocol(GEOS_ARCHSITES.CAPABILITIES);
        Set<String> supportedOutputFormats = wfs.getSupportedGetFeatureOutputFormats();
        assertNotNull(supportedOutputFormats);
        assertEquals(8, supportedOutputFormats.size()); // should be 7 once GEOT-3172 is fixed

        assertTrue(supportedOutputFormats.contains("text/gml; subtype=gml/3.1.1"));
        assertTrue(supportedOutputFormats.contains("text/xml; subtype=gml/2.1.2"));
        assertTrue(supportedOutputFormats.contains("text/xml; subtype=gml/3.1.1"));
        assertTrue(supportedOutputFormats.contains("GML2-GZIP"));
        assertTrue(supportedOutputFormats.contains("gml3"));
        assertTrue(supportedOutputFormats.contains("json"));
        assertTrue(supportedOutputFormats.contains("SHAPE-ZIP"));
        assertTrue(supportedOutputFormats.contains("GML2"));        
    }

    /**
     * Test method for {@link WFS_1_1_0_Protocol#getSupportedOutputFormats(java.lang.String)} .
     * 
     * @throws IOException
     */
    @Test
    public void testGetSupportedOutputFormatsByFeatureType() throws IOException {
        createTestProtocol(GEOS_ARCHSITES.CAPABILITIES);
        Set<String> archSitesOutputFormats = wfs
                .getSupportedOutputFormats(GEOS_ARCHSITES.FEATURETYPENAME);
        assertNotNull(archSitesOutputFormats);
        assertEquals(8, archSitesOutputFormats.size());

        assertTrue(archSitesOutputFormats.contains("GML2"));
        assertTrue(archSitesOutputFormats.contains("text/xml; subtype=gml/2.1.2"));
        assertTrue(archSitesOutputFormats.contains("GML2-GZIP"));
        assertTrue(archSitesOutputFormats.contains("text/xml; subtype=gml/3.1.1"));
        assertTrue(archSitesOutputFormats.contains("gml3"));
        assertTrue(archSitesOutputFormats.contains("SHAPE-ZIP"));
        assertTrue(archSitesOutputFormats.contains("json"));
        assertTrue(archSitesOutputFormats.contains("text/gml; subtype=gml/3.1.1"));
    }

    /**
     * Test method for {@link WFS_1_1_0_Protocol#getFeatureTypeNames()}.
     * 
     * @throws IOException
     */
    @Test
    public void testGetFeatureTypeNames() throws IOException {

        // test against a geoserver capabilities
        createTestProtocol(GEOS_ARCHSITES.CAPABILITIES);
        Set<QName> featureTypeNames = wfs.getFeatureTypeNames();
        assertEquals(6, featureTypeNames.size());

        for (QName name : featureTypeNames) {
            assertFalse(name.toString(), XMLConstants.DEFAULT_NS_PREFIX.equals(name.getPrefix()));
        }
        assertTrue(featureTypeNames.contains(GEOS_ARCHSITES.TYPENAME));
        assertTrue(featureTypeNames.contains(GEOS_POI.TYPENAME));
        assertTrue(featureTypeNames.contains(GEOS_ROADS.TYPENAME));
        assertTrue(featureTypeNames.contains(GEOS_STATES.TYPENAME));
        assertTrue(featureTypeNames.contains(GEOS_TASMANIA_CITIES.TYPENAME));
        assertTrue(featureTypeNames.contains(GEOS_TIGER_ROADS.TYPENAME));

        // test against a cubewerx capabilities
        createTestProtocol(CUBEWERX_GOVUNITCE.CAPABILITIES);
        featureTypeNames = wfs.getFeatureTypeNames();
        // there are 14 featuretypes in the capabilities document
        assertEquals(14, featureTypeNames.size());

        for (QName name : featureTypeNames) {
            assertFalse(name.toString(), XMLConstants.DEFAULT_NS_PREFIX.equals(name.getPrefix()));
        }
        assertTrue(featureTypeNames.contains(CUBEWERX_GOVUNITCE.TYPENAME));
        assertTrue(featureTypeNames.contains(CUBEWERX_ROADSEG.TYPENAME));
    }

    /**
     * Test method for {@link WFS_1_1_0_Protocol#getFeatureTypeName(java.lang.String)} .
     * 
     * @throws IOException
     */
    @Test
    public void testGetFeatureTypeNameGeoServer() throws IOException {
        createTestProtocol(GEOS_ARCHSITES.CAPABILITIES);

        try {
            wfs.getFeatureTypeName("nonExistentTypeName");
            fail("Expected IAE");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }

        // test against a geoserver capabilities
        assertEquals(GEOS_ARCHSITES.TYPENAME, wfs
                .getFeatureTypeName(GEOS_ARCHSITES.FEATURETYPENAME));
        assertEquals(GEOS_POI.TYPENAME, wfs.getFeatureTypeName(GEOS_POI.FEATURETYPENAME));
        assertEquals(GEOS_ROADS.TYPENAME, wfs.getFeatureTypeName(GEOS_ROADS.FEATURETYPENAME));
        assertEquals(GEOS_STATES.TYPENAME, wfs.getFeatureTypeName(GEOS_STATES.FEATURETYPENAME));
        assertEquals(GEOS_TASMANIA_CITIES.TYPENAME, wfs
                .getFeatureTypeName(GEOS_TASMANIA_CITIES.FEATURETYPENAME));
        assertEquals(GEOS_TIGER_ROADS.TYPENAME, wfs
                .getFeatureTypeName(GEOS_TIGER_ROADS.FEATURETYPENAME));

        // test against a cubewerx capabilities
        createTestProtocol(CUBEWERX_GOVUNITCE.CAPABILITIES);

        assertEquals(CUBEWERX_GOVUNITCE.TYPENAME, wfs
                .getFeatureTypeName(CUBEWERX_GOVUNITCE.FEATURETYPENAME));
        assertEquals(CUBEWERX_ROADSEG.TYPENAME, wfs
                .getFeatureTypeName(CUBEWERX_ROADSEG.FEATURETYPENAME));

    }

    /**
     * Test method for {@link WFS_1_1_0_Protocol#getFilterCapabilities()}.
     * 
     * @throws IOException
     */
    @Test
    public void testGetFilterCapabilities() throws IOException {
        createTestProtocol(GEOS_ARCHSITES.CAPABILITIES);
        FilterCapabilities filterCapabilities = wfs.getFilterCapabilities();
        assertNotNull(filterCapabilities);

        SpatialCapabilities spatialCapabilities = filterCapabilities.getSpatialCapabilities();
        Collection<GeometryOperand> geometryOperands = spatialCapabilities.getGeometryOperands();
        assertEquals(4, geometryOperands.size());
        assertTrue(geometryOperands.contains(GeometryOperand.Envelope));
        assertTrue(geometryOperands.contains(GeometryOperand.Point));
        assertTrue(geometryOperands.contains(GeometryOperand.LineString));
        assertTrue(geometryOperands.contains(GeometryOperand.Polygon));

        SpatialOperators spatialOperators = spatialCapabilities.getSpatialOperators();
        Collection<SpatialOperator> operators = spatialOperators.getOperators();
        assertEquals(9, operators.size());
        assertNotNull(spatialOperators.getOperator("Disjoint"));
        assertNotNull(spatialOperators.getOperator("Equals"));
        assertNotNull(spatialOperators.getOperator("DWithin"));
        assertNotNull(spatialOperators.getOperator("Beyond"));
        assertNotNull(spatialOperators.getOperator("Intersects"));
        assertNotNull(spatialOperators.getOperator("Touches"));
        assertNotNull(spatialOperators.getOperator("Crosses"));
        assertNotNull(spatialOperators.getOperator("Contains"));
        assertNotNull(spatialOperators.getOperator("BBOX"));

        // intentionally removed from the test caps doc
        assertNull(spatialOperators.getOperator("Overlaps"));
    }

    /**
     * Test method for
     * {@link WFS_1_1_0_Protocol#supportsOperation(org.geotools.data.wfs.protocol.wfs.WFSOperationType, org.geotools.data.wfs.protocol.http.HttpMethod)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public void testSupportsOperation() throws IOException {
        createTestProtocol(GEOS_ARCHSITES.CAPABILITIES);
        assertTrue(wfs.supportsOperation(DESCRIBE_FEATURETYPE, false));
        // post was deliberately left off on the test capabilities file
        assertFalse(wfs.supportsOperation(DESCRIBE_FEATURETYPE, true));

        createTestProtocol(CUBEWERX_GOVUNITCE.CAPABILITIES);
        assertTrue(wfs.supportsOperation(DESCRIBE_FEATURETYPE, false));
        assertTrue(wfs.supportsOperation(DESCRIBE_FEATURETYPE, true));
    }

    /**
     * Test method for
     * {@link WFS_1_1_0_Protocol#getOperationURL(org.geotools.data.wfs.protocol.wfs.WFSOperationType, org.geotools.data.wfs.protocol.http.HttpMethod)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public void testGetOperationURL() throws IOException {
        createTestProtocol(GEOS_ARCHSITES.CAPABILITIES);
        URL operationURL = wfs.getOperationURL(DESCRIBE_FEATURETYPE, false);
        assertNotNull(operationURL);
        assertEquals("http://localhost:8080/geoserver/wfs?", operationURL.toExternalForm());
        // post was deliberately left off on the test capabilities file
        assertNull(wfs.getOperationURL(DESCRIBE_FEATURETYPE, true));
    }

    /**
     * Test method for {@link WFS_1_1_0_Protocol#getFeatureTypeTitle(java.lang.String)} .
     * 
     * @throws IOException
     */
    @Test
    public void testGetFeatureTypeTitle() throws IOException {
        createTestProtocol(GEOS_ARCHSITES.CAPABILITIES);
        assertEquals("archsites_Type", wfs.getFeatureTypeTitle(GEOS_ARCHSITES.FEATURETYPENAME));

        createTestProtocol(CUBEWERX_GOVUNITCE.CAPABILITIES);
    }

    /**
     * Test method for {@link WFS_1_1_0_Protocol#getFeatureTypeAbstract(java.lang.String)} .
     * 
     * @throws IOException
     */
    @Test
    public void testGetFeatureTypeAbstract() throws IOException {
        createTestProtocol(GEOS_ARCHSITES.CAPABILITIES);
        assertEquals("Generated from sfArchsites", wfs
                .getFeatureTypeAbstract(GEOS_ARCHSITES.FEATURETYPENAME));

        createTestProtocol(CUBEWERX_GOVUNITCE.CAPABILITIES);
        assertNull(wfs.getFeatureTypeAbstract(CUBEWERX_GOVUNITCE.FEATURETYPENAME));

        try {
            wfs.getFeatureTypeAbstract("nonExistentTypeName");
            fail("Expected IAE");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    /**
     * Test method for {@link WFS_1_1_0_Protocol#getFeatureTypeWGS84Bounds(java.lang.String)} .
     * 
     * @throws IOException
     */
    @Test
    public void testGetFeatureTypeWGS84Bounds() throws IOException {
        createTestProtocol(GEOS_ARCHSITES.CAPABILITIES);
        try {
            wfs.getFeatureTypeAbstract("nonExistentTypeName");
            fail("Expected IAE");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }

        ReferencedEnvelope wgs84Bounds = wfs
                .getFeatureTypeWGS84Bounds(GEOS_ARCHSITES.FEATURETYPENAME);

        assertNotNull(wgs84Bounds);
        assertSame(DefaultGeographicCRS.WGS84, wgs84Bounds.getCoordinateReferenceSystem());
        assertEquals(-103D, wgs84Bounds.getMinX(), 1.0e-3);
        assertEquals(44D, wgs84Bounds.getMinY(), 1.0e-3);
        assertEquals(-102D, wgs84Bounds.getMaxX(), 1.0e-3);
        assertEquals(45D, wgs84Bounds.getMaxY(), 1.0e-3);

        createTestProtocol(CUBEWERX_GOVUNITCE.CAPABILITIES);
        assertNotNull(wfs.getFeatureTypeWGS84Bounds(CUBEWERX_GOVUNITCE.FEATURETYPENAME));
    }

    /**
     * Test method for {@link WFS_1_1_0_Protocol#getDefaultCRS(java.lang.String)}.
     * 
     * @throws IOException
     */
    @Test
    public void testGetDefaultCRS() throws IOException {
        createTestProtocol(GEOS_ARCHSITES.CAPABILITIES);
        try {
            wfs.getDefaultCRS("nonExistentTypeName");
            fail("Expected IAE");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }

        assertEquals("EPSG:26713", wfs.getDefaultCRS(GEOS_ARCHSITES.FEATURETYPENAME));
        assertEquals("EPSG:4326", wfs.getDefaultCRS(GEOS_STATES.FEATURETYPENAME));
    }

    /**
     * Test method for {@link WFS_1_1_0_Protocol#getSupportedCRSIdentifiers(java.lang.String)} .
     * 
     * @throws IOException
     */
    @Test
    public void testGetSupportedCRSIdentifiers() throws IOException {
        createTestProtocol(GEOS_ARCHSITES.CAPABILITIES);
        try {
            wfs.getSupportedCRSIdentifiers("nonExistentTypeName");
            fail("Expected IAE");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }

        Set<String> supportedCRSs;
        supportedCRSs = wfs.getSupportedCRSIdentifiers(GEOS_ARCHSITES.FEATURETYPENAME);

        // capabilities doesn't set other crs's for this feature type than the default one...
        assertNotNull(supportedCRSs);
        assertEquals(1, supportedCRSs.size());
        assertTrue(supportedCRSs.contains("EPSG:26713"));

        createTestProtocol(CUBEWERX_GOVUNITCE.CAPABILITIES);
        supportedCRSs = wfs.getSupportedCRSIdentifiers(CUBEWERX_GOVUNITCE.FEATURETYPENAME);
        // capabilities defines more crs's for this ftype
        assertNotNull(supportedCRSs);
        assertEquals(2, supportedCRSs.size());
        assertTrue(supportedCRSs.contains("EPSG:4269"));
        assertTrue(supportedCRSs.contains("EPSG:4326"));
    }

    /**
     * Test method for {@link WFS_1_1_0_Protocol#getFeatureTypeKeywords(java.lang.String)} .
     * 
     * @throws IOException
     */
    @Test
    public void testGetFeatureTypeKeywords() throws IOException {
        createTestProtocol(GEOS_ARCHSITES.CAPABILITIES);
        try {
            wfs.getFeatureTypeKeywords("nonExistentTypeName");
            fail("Expected IAE");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }

        Set<String> keywords;
        keywords = wfs.getFeatureTypeKeywords(GEOS_ARCHSITES.FEATURETYPENAME);

        assertNotNull(keywords);
        assertEquals(1, keywords.size());
        assertTrue(keywords.contains("archsites sfArchsites"));
    }

    /**
     * Test method for {@link WFS_1_1_0_Protocol#getDescribeFeatureTypeURLGet(java.lang.String)} .
     * 
     * @throws IOException
     */
    @Test
    public void testGetDescribeFeatureTypeURLGet() throws IOException {
        createTestProtocol(GEOS_ARCHSITES.CAPABILITIES);
        try {
            wfs.getDescribeFeatureTypeURLGet("nonExistentTypeName");
            fail("Expected IAE");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }

        URL url;
        url = wfs.getDescribeFeatureTypeURLGet(GEOS_ARCHSITES.FEATURETYPENAME);
        assertNotNull(url);
        String externalForm = url.toExternalForm();
        externalForm = URLDecoder.decode(externalForm, "UTF-8");

        assertTrue(externalForm.startsWith("http://localhost:8080/geoserver/wfs?"));
        assertTrue(externalForm.contains("REQUEST=DescribeFeatureType"));
        assertTrue(externalForm.contains("TYPENAME=sf:archsites"));
        assertTrue(externalForm.contains("VERSION=1.1.0"));
        assertTrue(externalForm.contains("SERVICE=WFS"));
        assertTrue(externalForm.contains("NAMESPACE=xmlns(sf=http://www.openplans.org/spearfish)"));
        // assertTrue(externalForm.contains("OUTPUTFORMAT=text/xml; subtype=gml/3.1.1"));
    }

    /**
     * Test method for
     * {@link WFS_1_1_0_Protocol#describeFeatureType(java.lang.String, java.lang.String)} .
     * 
     * @throws IOException
     */
    @Test
    public void testDescribeFeatureType_HTTP_GET() throws IOException {
        createTestProtocol(GEOS_ARCHSITES.CAPABILITIES);
        try {
            wfs.describeFeatureTypeGET("nonExistentTypeName", "text/xml; subtype=gml/3.1.1");
            fail("Expected IAE");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }

        HTTPResponse httpResponse = new TestHttpResponse("text/xml; subtype=gml/3.1.1", null,
                "mock-content");
        TestHttpProtocol mockHttp = new TestHttpProtocol(httpResponse);

        createTestProtocol(GEOS_ARCHSITES.CAPABILITIES, mockHttp);

        WFSResponse wfsResponse;

        wfsResponse = wfs.describeFeatureTypeGET(GEOS_ARCHSITES.FEATURETYPENAME,
                "text/xml; subtype=gml/3.1.1");

        URL baseUrl = mockHttp.targetUrl;
        assertNotNull(baseUrl);
        String externalForm = baseUrl.toExternalForm();
        externalForm = URLDecoder.decode(externalForm, "UTF-8");

        assertTrue(externalForm.startsWith("http://localhost:8080/geoserver/wfs?"));
        assertTrue(externalForm.contains("REQUEST=DescribeFeatureType"));
        assertTrue(externalForm.contains("TYPENAME=sf:archsites"));
        assertTrue(externalForm.contains("VERSION=1.1.0"));
        assertTrue(externalForm.contains("SERVICE=WFS"));
        assertTrue(externalForm.contains("NAMESPACE=xmlns(sf=http://www.openplans.org/spearfish)"));
        // assertTrue(externalForm.contains("OUTPUTFORMAT=text/xml; subtype=gml/3.1.1"));

        assertNotNull(wfsResponse);
        assertEquals(Charset.forName("UTF-8"), wfsResponse.getCharacterEncoding());
        assertEquals("text/xml; subtype=gml/3.1.1", wfsResponse.getContentType());
        assertNotNull(wfsResponse.getInputStream());
    }

    /**
     * Test method for {@link WFS_1_1_0_Protocol#getFeatureHits(org.geotools.data.Query)} .
     * <p>
     * If the server returns a FeatureCollection with numberOfFeatures=N attribute, {@code
     * getFeatureHits} shall return N
     * </p>
     * 
     * @throws IOException
     */
    /*
     * @Test public void testGetFeatureHitsSupported() throws IOException { String responseContent =
     * "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
     * "<wfs:FeatureCollection numberOfFeatures=\"217\" timeStamp=\"2008-10-24T13:53:53.034-04:00\" "
     * +
     * "xsi:schemaLocation=\"http://www.opengis.net/wfs http://schemas.opengis.net/wfs/1.1.0/wfs.xsd\" "
     * + "xmlns:wfs=\"http://www.opengis.net/wfs\" " +
     * "xmlns:topp=\"http://www.openplans.org/topp\" " + "xmlns:seb=\"http://seb.com\" " +
     * "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
     * "xmlns:ows=\"http://www.opengis.net/ows\" " +
     * "xmlns:gml=\"http://www.opengis.net/gml\" xmlns:xlink=\"http://www.w3.org/1999/xlink\"/>";
     * 
     * final TestHttpResponse response = new TestHttpResponse("text/xml; subtype=gml/3.1.1",
     * "UTF-8", responseContent);
     * 
     * HTTPProtocol mockHttp = new TestHttpProtocol(response);
     * 
     * createTestProtocol(GEOS_ARCHSITES.CAPABILITIES, mockHttp); DefaultQuery query = new
     * DefaultQuery(GEOS_ARCHSITES.FEATURETYPENAME);
     * 
     * int featureHits = wfs.getFeatureHits(query); assertEquals(217, featureHits); }
     */

    /**
     * Test method for {@link WFS_1_1_0_Protocol#getFeatureHits(org.geotools.data.Query)} .
     * <p>
     * If the server returns an exception report, {@code getFeatureHits} shall throw an IOException
     * </p>
     * 
     * @throws IOException
     */
    // @Test
    /*
     * public void testGetFeatureHitsException() throws IOException { String responseContent =
     * "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<ows:ExceptionReport version=\"1.0.0\" " +
     * "xsi:schemaLocation=\"http://www.opengis.net/ows http://localhost:8080/geoserver/schemas/ows/1.0.0/owsExceptionReport.xsd\""
     * +
     * "  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:ows=\"http://www.opengis.net/ows\">"
     * + "  <ows:Exception exceptionCode=\"mockExceptionCode\" locator=\"mockLocatorName\">" +
     * "    <ows:ExceptionText>Feature type sf:archsites2 unknown</ows:ExceptionText>" +
     * "   <ows:ExceptionText>Details:</ows:ExceptionText>" +
     * "   <ows:ExceptionText>mock exception report</ows:ExceptionText>" +
     * " </ows:Exception></ows:ExceptionReport>";
     * 
     * final TestHttpResponse response = new TestHttpResponse(
     * "application/vnd.ogc.se_xml;chatset=UTF-8", "UTF-8", responseContent);
     * 
     * HTTPProtocol mockHttp = new TestHttpProtocol(response);
     * 
     * createTestProtocol(GEOS_ARCHSITES.CAPABILITIES, mockHttp); DefaultQuery query = new
     * DefaultQuery(GEOS_ARCHSITES.FEATURETYPENAME);
     * 
     * try { wfs.getFeatureHits(query);
     * fail("Expected IOException if the server returned an exception report"); } catch (IOException
     * e) { // make sure the error message propagates assertEquals("mock exception report",
     * e.getMessage()); } }
     */

    /**
     * Test method for {@link WFS_1_1_0_Protocol#getFeatureHits(org.geotools.data.Query)} .
     * <p>
     * May the server not support resultType=hits even if declared in the capabilities document (eg.
     * CubeWerx) and hence return the FeatureCollection with full contents and no {@code
     * numberOfFeatures} attribute. In this case return -1.
     * </p>
     * 
     * @throws IOException
     */
    /*
     * @Test public void testGetFeatureHitsNotSupported() throws IOException { String
     * responseContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
     * "<wfs:FeatureCollection timeStamp=\"2008-10-24T13:53:53.034-04:00\" " +
     * "xsi:schemaLocation=\"http://www.opengis.net/wfs http://schemas.opengis.net/wfs/1.1.0/wfs.xsd\" "
     * + "xmlns:wfs=\"http://www.opengis.net/wfs\" " +
     * "xmlns:topp=\"http://www.openplans.org/topp\" " + "xmlns:seb=\"http://seb.com\" " +
     * "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
     * "xmlns:ows=\"http://www.opengis.net/ows\" " +
     * "xmlns:gml=\"http://www.opengis.net/gml\" xmlns:xlink=\"http://www.w3.org/1999/xlink\"/>";
     * 
     * final TestHttpResponse response = new TestHttpResponse("text/xml; subtype=gml/3.1.1",
     * "UTF-8", responseContent);
     * 
     * HTTPProtocol mockHttp = new TestHttpProtocol(response);
     * 
     * createTestProtocol(GEOS_ARCHSITES.CAPABILITIES, mockHttp); DefaultQuery query = new
     * DefaultQuery(GEOS_ARCHSITES.FEATURETYPENAME);
     * 
     * int featureHits = wfs.getFeatureHits(query); assertEquals(-1, featureHits); }
     */

/**
     * Test method for {@link WFS_1_1_0_Protocol#issueGetFeatureGET(net.opengis.wfs.GetFeatureType, Map)
     * 
     * @throws IOException
     */
    @Test
    public void testIssueGetFeature_GET() throws IOException {
        final InputStream responseContent = TestData.openStream(this, GEOS_ARCHSITES.DATA);

        final TestHttpResponse httpResponse;
        final String defaultWfs11OutputFormat = "text/xml; subtype=gml/3.1.1";
        httpResponse = new TestHttpResponse(defaultWfs11OutputFormat, "UTF-16", responseContent);

        TestHttpProtocol mockHttp = new TestHttpProtocol(httpResponse);

        createTestProtocol(GEOS_ARCHSITES.CAPABILITIES, mockHttp);

        WFSStrategy strategy = new GeoServerStrategy();
        wfs.setStrategy(strategy);

        DefaultQuery query = new DefaultQuery(GEOS_ARCHSITES.FEATURETYPENAME);
        GetFeature getFeature = new GetFeatureQueryAdapter(query, defaultWfs11OutputFormat,
                "EPSG:4326", ResultType.RESULTS);

        WFSResponse response;

        response = wfs.issueGetFeatureGET(getFeature);

        assertNotNull(response);
        assertEquals(defaultWfs11OutputFormat, response.getContentType());
        assertNotNull(response.getInputStream());
        assertEquals(Charset.forName("UTF-16"), response.getCharacterEncoding());

        URL baseUrl = mockHttp.targetUrl;
        Map<String, String> kvp = mockHttp.issueGetKvp;
        assertNotNull(baseUrl);
        assertNotNull(kvp);
        assertEquals("http://localhost:8080/geoserver/wfs?", baseUrl.toExternalForm());
        assertEquals("WFS", kvp.get("SERVICE"));
        assertEquals("1.1.0", kvp.get("VERSION"));
        assertEquals("GetFeature", kvp.get("REQUEST"));
        assertEquals(GEOS_ARCHSITES.FEATURETYPENAME, kvp.get("TYPENAME"));
        assertEquals(defaultWfs11OutputFormat, kvp.get("OUTPUTFORMAT"));
        assertNotNull(kvp.get("SRSNAME"));
        assertNull(kvp.get("PROPERTYNAME"));
        assertNull(kvp.get("MAXFEATURES"));
        assertNull(kvp.get("FEATUREID"));
        assertNull(kvp.get("FILTER"));
    }

    @Test
    public void testIssueGetFeature_GET_OptionalParameters() throws Exception {

        final InputStream responseContent = TestData.openStream(this, GEOS_ARCHSITES.DATA);

        final TestHttpResponse httpResponse;
        final String defaultWfs11OutputFormat = "text/xml; subtype=gml/3.1.1";
        httpResponse = new TestHttpResponse(defaultWfs11OutputFormat, "UTF-16", responseContent);

        TestHttpProtocol mockHttp = new TestHttpProtocol(httpResponse);
        createTestProtocol(GEOS_ARCHSITES.CAPABILITIES, mockHttp);

        DefaultQuery query = new DefaultQuery(GEOS_ARCHSITES.FEATURETYPENAME);
        query.setMaxFeatures(1000);
        query.setPropertyNames(new String[] { "cat", "the_geom" });
        query.setCoordinateSystem(CRS.decode("EPSG:23030"));

        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
        Filter filter = ff.id(Collections.singleton(ff.featureId("archsites.1")));
        query.setFilter(filter);

        WFSResponse response;
        WFSStrategy strategy = new GeoServerStrategy();
        wfs.setStrategy(strategy);

        wfs.setDescribeFeatureTypeURLOverride(TestData.url(this, GEOS_ARCHSITES.SCHEMA));

        GetFeature getFeature = new GetFeatureQueryAdapter(query, defaultWfs11OutputFormat,
                "EPSG:26713", ResultType.RESULTS);

        response = wfs.issueGetFeatureGET(getFeature);

        assertNotNull(response);

        Map<String, String> kvp = mockHttp.issueGetKvp;
        assertEquals("1000", kvp.get("MAXFEATURES"));

        String propertyName = kvp.get("PROPERTYNAME");
        assertEquals("cat,the_geom", propertyName);

        String srsName = kvp.get("SRSNAME");
        // 23030 is not in the caps, so we assume its not supported
        // assertEquals("EPSG:23030", srsName);
        assertEquals("EPSG:26713", srsName);

        assertEquals("archsites.1", kvp.get("FEATUREID"));
        assertNull("FEATUREID and FILTER are mutually exclusive", kvp.get("FILTER"));

        // now try with a non feature id filter
        filter = ff.equals(ff.property("cat"), ff.literal(1));
        query.setFilter(filter);

        getFeature = new GetFeatureQueryAdapter(query, defaultWfs11OutputFormat, "EPSG:23030",
                ResultType.RESULTS);

        response = wfs.issueGetFeatureGET(getFeature);
        kvp = mockHttp.issueGetKvp;

        assertNull("FEATUREID and FILTER are mutually exclusive", kvp.get("FEATUREID"));

        String encodedFilter = kvp.get("FILTER");
        assertNotNull(encodedFilter);
        Parser filterParser = new Parser(new OGCConfiguration());
        Filter parsed = (Filter) filterParser.parse(new StringReader(encodedFilter));
        assertTrue(parsed instanceof PropertyIsEqualTo);
    }

/**
     * Test method for {@link WFS_1_1_0_Protocol#issueGetFeaturePOST(net.opengis.wfs.GetFeatureType)
     * 
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    @Test
    public void testIssueGetFeature_POST() throws IOException, ParserConfigurationException,
            SAXException {
        final InputStream responseContent = TestData.openStream(this, GEOS_ARCHSITES.DATA);

        final TestHttpResponse httpResponse;
        final String defaultWfs11OutputFormat = "text/xml; subtype=gml/3.1.1";
        httpResponse = new TestHttpResponse(defaultWfs11OutputFormat, "UTF-16", responseContent);

        TestHttpProtocol mockHttp = new TestHttpProtocol(httpResponse);
        createTestProtocol(GEOS_ARCHSITES.CAPABILITIES, mockHttp);

        WFSStrategy strategy = new GeoServerStrategy();
        wfs.setStrategy(strategy);

        DefaultQuery query = new DefaultQuery(GEOS_ARCHSITES.FEATURETYPENAME);
        GetFeature getFeature = new GetFeatureQueryAdapter(query, defaultWfs11OutputFormat,
                "EPSG:4326", ResultType.RESULTS);

        WFSResponse response;

        response = wfs.issueGetFeaturePOST(getFeature);

        assertNotNull(response);
        assertEquals(defaultWfs11OutputFormat, response.getContentType());
        assertNotNull(response.getInputStream());
        assertEquals(Charset.forName("UTF-16"), response.getCharacterEncoding());

        assertEquals(-1, mockHttp.postCallbackContentLength);
        assertEquals("text/xml", mockHttp.postCallbackContentType);

        Document dom;
        String issuedRequest;
        {
            ByteArrayOutputStream out = mockHttp.postCallbackEncodedRequestBody;
            issuedRequest = out.toString();
            System.out.println("Issued request: " + issuedRequest);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder docBuilder = dbf.newDocumentBuilder();
            dom = docBuilder.parse(new ByteArrayInputStream(out.toByteArray()));
        }
        
        //was the featuretype declaration included?
        String expectedNsDecl = "xmlns:sf=\"" + GEOS_ARCHSITES.TYPENAME.getNamespaceURI() + "\"";
        assertTrue(issuedRequest, issuedRequest.contains(expectedNsDecl));
        Element root = dom.getDocumentElement();
        assertEquals(WFS.GetFeature.getLocalPart(), root.getLocalName());
        assertEquals(WFS.NAMESPACE, root.getNamespaceURI());
    }
}
