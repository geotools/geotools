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
import java.util.Collections;
import org.eclipse.emf.common.util.URI;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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

        handler.createInputStream(uri, Collections.emptyMap());
    }

    @Test
    public void testCanHandleHttps() throws Exception {
        URI uri = URI.createURI("https://example.com");

        assertThat(handler.canHandle(uri), is(true));

        handler.createInputStream(uri, Collections.emptyMap());
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

        handler.createInputStream(uri, Collections.emptyMap());
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

        handler.createInputStream(uri, Collections.emptyMap());
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

        handler.createInputStream(uri, Collections.emptyMap());
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
                IOException.class, () -> handler.createInputStream(uri, Collections.EMPTY_MAP));
    }
}
