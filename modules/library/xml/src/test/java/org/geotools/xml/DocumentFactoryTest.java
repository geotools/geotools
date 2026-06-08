/**
 * GeoTools - The Open Source Java GIS Toolkit http://geotools.org
 *
 * <p>(C) 2016, Open Source Geospatial Foundation (OSGeo)
 *
 * <p>This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation; version 2.1 of the License.
 *
 * <p>This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package org.geotools.xml;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

/**
 * Test for {@link DocumentFactory}
 *
 * @author Aaron Waddell
 */
public class DocumentFactoryTest {
    private static final String DISALLOW_DOCTYPE_DECLAIRATION = "http://apache.org/xml/features/disallow-doctype-decl";

    private static final String EXTERNAL_GENERAL_ENTITIES_FEATURE =
            "http://xml.org/sax/features/external-general-entities";

    private static final String EXTERNAL_PARAMETER_ENTITIES_FEATURE =
            "http://xml.org/sax/features/external-parameter-entities";

    private URI uri;

    private Map<String, Object> hints;

    @Mock
    private SAXParserFactory mockSaxParserFactory;

    private final Map<String, Boolean> features = new HashMap<>();

    @Mock
    private SAXParser mockSaxParser;

    @Mock
    InputStream inputStream;

    @Before
    public void before() throws Exception {
        this.uri = new URI("http://geotools.org");
        this.hints = new HashMap<>();

        MockitoAnnotations.openMocks(this);
        hints.put(XMLHandlerHints.SAX_PARSER_FACTORY, mockSaxParserFactory);

        // mock factory parser
        when(mockSaxParserFactory.newSAXParser()).thenReturn(mockSaxParser);

        Answer<Void> startDocumentAnswer = invocationOnMock -> {
            XMLSAXHandler xmlSaxHandler = (XMLSAXHandler) invocationOnMock.getArguments()[1];
            xmlSaxHandler.startDocument();
            return null;
        };
        doAnswer(startDocumentAnswer).when(mockSaxParser).parse(anyString(), any(XMLSAXHandler.class));
        doAnswer(startDocumentAnswer).when(mockSaxParser).parse(any(InputStream.class), any(XMLSAXHandler.class));

        // mock factory features (stateful which is annoying)
        Answer<Void> setFeatureAnswer = invocationOnMock -> {
            String name = invocationOnMock.getArgument(0);
            Boolean value = invocationOnMock.getArgument(1);
            features.put(name, value);
            return null;
        };

        Answer<Boolean> getFeatureAnswer = invocationOnMock -> {
            String name = invocationOnMock.getArgument(0);
            return features.getOrDefault(name, Boolean.FALSE);
        };
        doAnswer(setFeatureAnswer).when(mockSaxParserFactory).setFeature(anyString(), anyBoolean());
        when(mockSaxParserFactory.getFeature(anyString())).thenAnswer(getFeatureAnswer);
    }

    @Test
    public void testGetInstanceFromURIWithSpecifiedLevelButNoPDisableExternalEntities() throws Exception {
        DocumentFactory.getInstance(uri, hints, Level.WARNING);
        verifyDisableExternalEntities(false);
    }

    @Test
    public void testGetInstanceFromURIWithSpecifiedLevelAndDisableExternalEntitiesTrue() throws Exception {
        hints.put(DocumentFactory.DISABLE_EXTERNAL_ENTITIES, Boolean.TRUE);
        hints.put(DocumentFactory.ENABLE_DTD, Boolean.TRUE);
        DocumentFactory.getInstance(uri, hints, Level.WARNING);

        verifyDisableExternalEntities(true);
    }

    @Test
    public void testGetInstanceFromURIWithSpecifiedLevelAndDisableExternalEntitiesFalse() throws Exception {
        hints.put(DocumentFactory.DISABLE_EXTERNAL_ENTITIES, Boolean.FALSE);
        DocumentFactory.getInstance(uri, hints, Level.WARNING);

        verifyDisableExternalEntities(false);
    }

    @Test(expected = SAXException.class)
    public void testGetInstanceFromURIWithSpecifiedLevelParseFailureIOException() throws Exception {
        doThrow(new IOException("Failure")).when(mockSaxParser).parse(anyString(), any(XMLSAXHandler.class));
        DocumentFactory.getInstance(uri, hints, Level.WARNING);
    }

    @Test(expected = SAXException.class)
    public void testGetInstanceFromURIWithSpecifiedLevelParseFailureSAXException() throws Exception {
        doThrow(new SAXException("Failure")).when(mockSaxParser).parse(anyString(), any(XMLSAXHandler.class));
        DocumentFactory.getInstance(uri, hints, Level.WARNING);
    }

    @Test(expected = SAXException.class)
    public void testGetInstanceFromURIWithSpecifiedLevelNewSAXParserFailure() throws Exception {
        when(mockSaxParserFactory.newSAXParser()).thenThrow(new ParserConfigurationException("Failure"));
        DocumentFactory.getInstance(uri, hints, Level.WARNING);
    }

    @Test
    public void testGetInstanceFromInputStreamWithSpecifiedLevelNoDisableExternalEntities() throws Exception {
        DocumentFactory.getInstance(inputStream, hints, Level.WARNING);

        verifyDisableExternalEntities(false);
    }

    @Test
    public void testGetInstanceFromInputStreamWithSpecifiedLevelAndDisableExternalEntitiesTrue() throws Exception {
        hints.put(DocumentFactory.DISABLE_EXTERNAL_ENTITIES, Boolean.TRUE);
        hints.put(DocumentFactory.ENABLE_DTD, Boolean.TRUE);
        DocumentFactory.getInstance(inputStream, hints, Level.WARNING);

        verifyDisableExternalEntities(true);
    }

    @Test
    public void testGetInstanceFromInputStreamWithSpecifiedLevelAndPDisableExternalEntitiesFalse() throws Exception {
        hints.put(DocumentFactory.DISABLE_EXTERNAL_ENTITIES, Boolean.FALSE);
        DocumentFactory.getInstance(inputStream, hints, Level.WARNING);

        verifyDisableExternalEntities(false);
    }

    @Test(expected = SAXException.class)
    public void testGetInstanceFromInputStreamWithSpecifiedLevelParseFailureIOException() throws Exception {
        doThrow(new IOException("Parse failure"))
                .when(mockSaxParser)
                .parse(any(InputStream.class), any(XMLSAXHandler.class));
        DocumentFactory.getInstance(inputStream, hints, Level.WARNING);
    }

    @Test(expected = SAXException.class)
    public void testGetInstanceFromInputStreamWithSpecifiedLevelParseFailureSAXException() throws Exception {
        doThrow(new SAXException("Parse failure"))
                .when(mockSaxParser)
                .parse(any(InputStream.class), any(XMLSAXHandler.class));
        DocumentFactory.getInstance(inputStream, hints, Level.WARNING);
    }

    void verifyDisableExternalEntities(boolean disabledExternalEntities)
            throws SAXNotRecognizedException, SAXNotSupportedException, ParserConfigurationException {

        assertTrue(XMLConstants.FEATURE_SECURE_PROCESSING, features.get(XMLConstants.FEATURE_SECURE_PROCESSING));

        // double external entity support disabled
        if (disabledExternalEntities) {
            // DTD support is used for force use of external entities
            assertFalse(DISALLOW_DOCTYPE_DECLAIRATION, features.get(DISALLOW_DOCTYPE_DECLAIRATION));

            assertFalse(EXTERNAL_GENERAL_ENTITIES_FEATURE, features.get(EXTERNAL_GENERAL_ENTITIES_FEATURE));
            assertFalse(EXTERNAL_PARAMETER_ENTITIES_FEATURE, features.get(EXTERNAL_PARAMETER_ENTITIES_FEATURE));
        } else {
            assertTrue(DISALLOW_DOCTYPE_DECLAIRATION, features.get(DISALLOW_DOCTYPE_DECLAIRATION));

            assertTrue(EXTERNAL_GENERAL_ENTITIES_FEATURE, features.get(EXTERNAL_GENERAL_ENTITIES_FEATURE));
            assertTrue(EXTERNAL_PARAMETER_ENTITIES_FEATURE, features.get(EXTERNAL_PARAMETER_ENTITIES_FEATURE));
        }
    }
}
