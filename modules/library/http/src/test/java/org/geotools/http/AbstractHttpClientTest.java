/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2026, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.http;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.net.MalformedURLException;
import java.net.URL;
import org.junit.Test;

/** Unit tests for the {@link AbstractHttpClient#encodeURL(URL)} request line percent-encoding. */
public class AbstractHttpClientTest {

    @Test
    public void testLeavesAlreadyEncoded() throws MalformedURLException {
        // raw non-ASCII gets encoded, existing %XX and legal chars are preserved (no double encoding)
        assertEquals(
                "http://h/wms?a=citt%C3%A0&b=citt%C3%A0&c=1,2&d=a%20b",
                AbstractHttpClient.encodeURL(new URL("http://h/wms?a=città&b=citt%C3%A0&c=1,2&d=a b"))
                        .toExternalForm());
    }

    @Test
    public void testStrayPercent() throws MalformedURLException {
        // a valid %XX escape is kept, a stray '%' (percentage sign, %zz, trailing %) is encoded to %25 so the request
        // line stays a parseable URI, something HttpClient 4 never handled
        assertEquals(
                "http://h/wms?d=50%25&e=%C3%A0&f=%25zz&g=abc%25",
                AbstractHttpClient.encodeURL(new URL("http://h/wms?d=50%&e=%C3%A0&f=%zz&g=abc%"))
                        .toExternalForm());
    }

    @Test
    public void testFragmentAndStrayHash() throws MalformedURLException {
        // a legit fragment is kept as a single delimiter; a stray '#' inside the fragment is encoded to %23 so the
        // reconstructed URL cannot grow a second fragment marker (query/fragment boundary differential)
        assertEquals(
                "http://h/p?a=b#frag",
                AbstractHttpClient.encodeURL(new URL("http://h/p?a=b#frag")).toExternalForm());
        assertEquals(
                "http://h/p?a=b#x%23y",
                AbstractHttpClient.encodeURL(new URL("http://h/p?a=b#x#y")).toExternalForm());
    }

    @Test
    public void testNonAsciiPath() throws MalformedURLException {
        // encoding applies to the path, not just the query
        assertEquals(
                "http://h/citt%C3%A0/wms?a=b",
                AbstractHttpClient.encodeURL(new URL("http://h/città/wms?a=b")).toExternalForm());
    }

    @Test
    public void testNonAsciiFragment() throws MalformedURLException {
        // fragment content is UTF-8 encoded as well (kept as a single delimiter)
        assertEquals(
                "http://h/wms?a=b#citt%C3%A0",
                AbstractHttpClient.encodeURL(new URL("http://h/wms?a=b#città")).toExternalForm());
    }

    @Test
    public void testAsciiUrlLeftUnchanged() throws MalformedURLException {
        // a fully legal URL is returned as the same instance, no reconstruction
        URL url = new URL("http://h:8080/geoserver/wms?service=WMS&request=GetMap&bbox=1,2,3,4");
        assertSame(url, AbstractHttpClient.encodeURL(url));
    }

    @Test
    public void testLoneSurrogateBecomesReplacementChar() throws MalformedURLException {
        // an unpaired high or low surrogate is not a valid code point: it must encode as U+FFFD (%EF%BF%BD), not as an
        // invalid UTF-8 (CESU-8) sequence like %ED%A0%80
        assertEquals(
                "http://h/wms?a=x%EF%BF%BDy",
                AbstractHttpClient.encodeURL(new URL("http://h/wms?a=x\uD800y")).toExternalForm());
        assertEquals(
                "http://h/wms?a=%EF%BF%BD",
                AbstractHttpClient.encodeURL(new URL("http://h/wms?a=\uDC00")).toExternalForm());
    }

    @Test
    public void testMultiByteAndAstral() throws MalformedURLException {
        // 3-byte CJK, 2-byte Cyrillic, and a 4-byte astral code point (emoji, encoded via a surrogate pair)
        assertEquals(
                "http://h/wms?cjk=%E5%8C%97%E4%BA%AC&cyr=%D0%9C&emoji=%F0%9F%98%80",
                AbstractHttpClient.encodeURL(new URL("http://h/wms?cjk=北京&cyr=М&emoji=😀"))
                        .toExternalForm());
    }
}
