package org.geotools.xsd.impl;

import static org.easymock.EasyMock.anyInt;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.logging.Level;
import org.eclipse.emf.common.util.URI;
import org.geotools.data.ows.MockURLChecker;
import org.geotools.data.ows.URLCheckers;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;

public class HTTPURIHandlerTest {
    HttpURLConnection conn;
    InputStream is;
    HTTPURIHandler handler;

    @Before
    public void setUp() throws Exception {
        conn = createMock(HttpURLConnection.class);
        is = createMock(InputStream.class);

        conn.setConnectTimeout(anyInt());
        expectLastCall().asStub();
        conn.setReadTimeout(anyInt());
        expectLastCall().asStub();
        expect(conn.getInputStream()).andStubReturn(is);

        handler =
                new HTTPURIHandler() {

                    @Override
                    protected HttpURLConnection getConnection(URI uri) throws IOException {
                        String s = uri.toString();
                        LOGGER.log(Level.INFO, s);
                        URL url = new URL(s);
                        URLCheckers.evaluate(url);
                        return conn;
                    }
                };

        replay(conn, is);
    }

    @After
    public void tearDown() throws Exception {
        verify(conn, is);
    }

    @Test
    public void testCanHandleHttp() throws Exception {
        URI uri = URI.createURI("http://example.com");

        assertThat(handler.canHandle(uri), is(true));

        handler.createInputStream(uri, Collections.EMPTY_MAP);
    }

    @Test
    public void testCanHandleHttps() throws Exception {
        URI uri = URI.createURI("https://example.com");

        assertThat(handler.canHandle(uri), is(true));

        handler.createInputStream(uri, Collections.EMPTY_MAP);
    }

    @Test
    public void testCantHandleFtp() throws Exception {
        URI uri = URI.createURI("ftp://example.com");

        assertThat(handler.canHandle(uri), is(false));
    }

    @Test
    public void testDefaultTimeouts() throws Exception {

        reset(conn);
        {
            conn.setConnectTimeout(HTTPURIHandler.DEFAULT_CONNECTION_TIMEOUT);
            expectLastCall();
            conn.setReadTimeout(HTTPURIHandler.DEFAULT_READ_TIMEOUT);
            expectLastCall();
            expect(conn.getInputStream()).andStubReturn(is);
        }
        replay(conn);

        URI uri = URI.createURI("http://example.com");

        handler.createInputStream(uri, Collections.EMPTY_MAP);
    }

    @Test
    public void testCustomConnectTimeout() throws Exception {
        final int testValue = 42;

        reset(conn);
        {
            conn.setConnectTimeout(testValue);
            expectLastCall();
            conn.setReadTimeout(HTTPURIHandler.DEFAULT_READ_TIMEOUT);
            expectLastCall();
            expect(conn.getInputStream()).andReturn(is);
        }
        replay(conn);

        handler.setConnectionTimeout(testValue);

        URI uri = URI.createURI("http://example.com");

        handler.createInputStream(uri, Collections.EMPTY_MAP);
    }

    @Test
    public void testCustomReadTimeout() throws Exception {
        final int testValue = 42;

        reset(conn);
        {
            conn.setConnectTimeout(HTTPURIHandler.DEFAULT_CONNECTION_TIMEOUT);
            expectLastCall();
            conn.setReadTimeout(testValue);
            expectLastCall();
            expect(conn.getInputStream()).andStubReturn(is);
        }
        replay(conn);

        handler.setReadTimeout(testValue);

        URI uri = URI.createURI("http://example.com");

        handler.createInputStream(uri, Collections.EMPTY_MAP);
    }

    @Test
    public void testTimeout() throws Exception {
        reset(conn);
        {
            conn.setConnectTimeout(anyInt());
            expectLastCall();
            conn.setReadTimeout(anyInt());
            expectLastCall();
            expect(conn.getInputStream()).andThrow(new IOException());
        }
        replay(conn);

        URI uri = URI.createURI("http://example.com");
        Assert.assertThrows(
                IOException.class,
                new ThrowingRunnable() {

                    @Override
                    public void run() throws Throwable {
                        handler.createInputStream(uri, Collections.EMPTY_MAP);
                    }
                });
    }

    @Test
    public void testSecuredHttpClient() throws Exception {

        // mock URL checker is not configured for example.com
        // should throw exception
        MockURLChecker urlChecker = new MockURLChecker();
        urlChecker.setEnabled(true);
        URLCheckers.addURLChecker(urlChecker);

        URI uri = URI.createURI("http://example.com");
        try {
            handler.createInputStream(uri, Collections.EMPTY_MAP);
        } catch (Exception e) {
            MatcherAssert.assertThat(
                    e.getMessage(),
                    CoreMatchers.is(
                            "Evaluation Failure: http://example.com: did not pass security evaluation"));
        } finally {
            URLCheckers.removeURLChecker(urlChecker);
        }
    }
}
