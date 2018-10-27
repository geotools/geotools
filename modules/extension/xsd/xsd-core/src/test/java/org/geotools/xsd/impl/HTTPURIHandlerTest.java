package org.geotools.xsd.impl;

import static org.easymock.classextension.EasyMock.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Collections;
import org.eclipse.emf.common.util.URI;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class HTTPURIHandlerTest {
    HttpURLConnection conn;
    InputStream is;
    HTTPURIHandler handler;

    @Rule public ExpectedException exception = ExpectedException.none();

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
                        // TODO Auto-generated method stub
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

        exception.expect(IOException.class);
        handler.createInputStream(uri, Collections.EMPTY_MAP);
    }
}
