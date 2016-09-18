package org.geotools.xml;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import static org.mockito.Mockito.*;

/**
 * GeoTools - The Open Source Java GIS Toolkit
 * http://geotools.org
 * <p>
 * (C) 2016, Open Source Geospatial Foundation (OSGeo)
 * <p>
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation;
 * version 2.1 of the License.
 * <p>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * @author Aaron Waddell
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ DocumentFactory.class, SAXParserFactory.class })
public class DocumentFactoryTest {

        private final String EXTERNAL_GENERAL_ENTITIES_FEATURE =
                "http://xml.org/sax/features/external-general-entities";

        private final String EXTERNAL_PARAMETER_ENTITIES_FEATURE =
                "http://xml.org/sax/features/external-parameter-entities";

        private URI uri;

        private Map<String,Object> hints;

        @Mock
        private SAXParserFactory mockSaxParserFactory;

        @Mock
        private SAXParser mockSaxParser;

        @Mock
        InputStream inputStream;

        @Before
        public void before() throws Exception {
                uri = new URI("http://geotools.org");
                hints = new HashMap<String, Object>();

                MockitoAnnotations.initMocks(this);

                PowerMockito.mockStatic(SAXParserFactory.class);
                PowerMockito.when(SAXParserFactory.newInstance()).thenReturn(mockSaxParserFactory);

                when(mockSaxParserFactory.newSAXParser()).thenReturn(mockSaxParser);

                Answer<Void> startDocumentAnswer = new Answer<Void>() {
                        @Override
                        public Void answer(InvocationOnMock invocationOnMock) throws Throwable {
                                XMLSAXHandler xmlSaxHandler = (XMLSAXHandler) invocationOnMock
                                        .getArguments()[1];
                                xmlSaxHandler.startDocument();
                                return null;
                        }
                };
                doAnswer(startDocumentAnswer).when(mockSaxParser)
                        .parse(anyString(), any(XMLSAXHandler.class));
                doAnswer(startDocumentAnswer).when(mockSaxParser)
                        .parse(any(InputStream.class), any(XMLSAXHandler.class));
        }

        @Test
        public void testGetInstanceFromURIWithSpecifiedLevelButNoParseExternalEntities()
                throws Exception {
                hints.put(DocumentFactory.DISABLE_EXTERNAL_ENTITIES, Boolean.TRUE);
                DocumentFactory.getInstance(uri, hints, Level.WARNING);

                verify(mockSaxParserFactory).setFeature(EXTERNAL_GENERAL_ENTITIES_FEATURE, false);
                verify(mockSaxParserFactory).setFeature(EXTERNAL_PARAMETER_ENTITIES_FEATURE, false);
        }

        @Test
        public void testGetInstanceFromURIWithSpecifiedLevelAndParseExternalEntitiesTrue()
                throws Exception {
                hints.put(DocumentFactory.DISABLE_EXTERNAL_ENTITIES, Boolean.TRUE);
                DocumentFactory.getInstance(uri, hints, Level.WARNING);

                verify(mockSaxParserFactory, never())
                        .setFeature(EXTERNAL_GENERAL_ENTITIES_FEATURE, false);
                verify(mockSaxParserFactory, never())
                        .setFeature(EXTERNAL_PARAMETER_ENTITIES_FEATURE, false);
        }

        @Test
        public void testGetInstanceFromURIWithSpecifiedLevelAndParseExternalEntitiesFalse()
                throws Exception {
                hints.put(DocumentFactory.DISABLE_EXTERNAL_ENTITIES, Boolean.FALSE);
                DocumentFactory.getInstance(uri, hints, Level.WARNING);

                verify(mockSaxParserFactory).setFeature(EXTERNAL_GENERAL_ENTITIES_FEATURE, false);
                verify(mockSaxParserFactory).setFeature(EXTERNAL_PARAMETER_ENTITIES_FEATURE, false);
        }

        @Test(expected = SAXException.class)
        public void testGetInstanceFromURIWithSpecifiedLevelParseFailureIOException()
                throws Exception {
                doThrow(new IOException("Failure")).when(mockSaxParser)
                        .parse(anyString(), any(XMLSAXHandler.class));
                DocumentFactory.getInstance(uri, hints, Level.WARNING);
        }

        @Test(expected = SAXException.class)
        public void testGetInstanceFromURIWithSpecifiedLevelParseFailureSAXException()
                throws Exception {
                doThrow(new SAXException("Failure")).when(mockSaxParser)
                        .parse(anyString(), any(XMLSAXHandler.class));
                DocumentFactory.getInstance(uri, hints, Level.WARNING);
        }

        @Test(expected = SAXException.class)
        public void testGetInstanceFromURIWithSpecifiedLevelNewSAXParserFailure()
                throws Exception {
                when(mockSaxParserFactory.newSAXParser())
                        .thenThrow(new ParserConfigurationException("Failure"));
                DocumentFactory.getInstance(uri, hints, Level.WARNING);
        }

        @Test
        public void testGetInstanceFromInputStreamWithSpecifiedLevelNoParseExternalEntities()
                throws Exception {
                DocumentFactory.getInstance(inputStream, hints, Level.WARNING);

                verify(mockSaxParserFactory).setFeature(EXTERNAL_GENERAL_ENTITIES_FEATURE, false);
                verify(mockSaxParserFactory).setFeature(EXTERNAL_PARAMETER_ENTITIES_FEATURE, false);
        }

        @Test
        public void testGetInstanceFromInputStreamWithSpecifiedLevelAndParseExternalEntitiesTrue()
                throws Exception {
                hints.put(DocumentFactory.DISABLE_EXTERNAL_ENTITIES, Boolean.TRUE);
                DocumentFactory.getInstance(inputStream, hints, Level.WARNING);

                verify(mockSaxParserFactory, never())
                        .setFeature(EXTERNAL_GENERAL_ENTITIES_FEATURE, false);
                verify(mockSaxParserFactory, never())
                        .setFeature(EXTERNAL_PARAMETER_ENTITIES_FEATURE, false);
        }

        @Test
        public void testGetInstanceFromInputStreamWithSpecifiedLevelAndParseExternalEntitiesFalse()
                throws Exception {
                hints.put(DocumentFactory.DISABLE_EXTERNAL_ENTITIES, Boolean.FALSE);
                DocumentFactory.getInstance(inputStream, hints, Level.WARNING);

                verify(mockSaxParserFactory).setFeature(EXTERNAL_GENERAL_ENTITIES_FEATURE, false);
                verify(mockSaxParserFactory).setFeature(EXTERNAL_PARAMETER_ENTITIES_FEATURE, false);
        }

        @Test(expected = SAXException.class)
        public void testGetInstanceFromInputStreamWithSpecifiedLevelParseFailureIOException()
                throws Exception {
                doThrow(new IOException("Parse failure")).when(mockSaxParser)
                        .parse(any(InputStream.class), any(XMLSAXHandler.class));
                DocumentFactory.getInstance(inputStream, hints, Level.WARNING);
        }

        @Test(expected = SAXException.class)
        public void testGetInstanceFromInputStreamWithSpecifiedLevelParseFailureSAXException()
                throws Exception {
                doThrow(new SAXException("Parse failure")).when(mockSaxParser)
                        .parse(any(InputStream.class), any(XMLSAXHandler.class));
                DocumentFactory.getInstance(inputStream, hints, Level.WARNING);
        }

}
