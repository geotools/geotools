/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.StringJoiner;

/**
 * A base class for HTTPClient, that implements everything except the get and post methods.
 *
 * @author Andrea Aime - GeoSolutions
 */
public abstract class AbstractHttpClient implements HTTPClient {

    protected String user;

    protected String password;

    protected Map<String, String> extraParams = Collections.emptyMap();

    protected int connectTimeout;

    protected int readTimeout;

    protected boolean tryGzip;

    @Override
    public String getUser() {
        return user;
    }

    @Override
    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public void setExtraParams(Map<String, String> extraParams) {
        this.extraParams = extraParams;
    }

    @Override
    public Map<String, String> getExtraParams() {
        return this.extraParams;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int getConnectTimeout() {
        return connectTimeout;
    }

    @Override
    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    @Override
    public int getReadTimeout() {
        return this.readTimeout;
    }

    @Override
    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    /** @see HTTPClient#setTryGzip(boolean) */
    @Override
    public void setTryGzip(boolean tryGZIP) {
        this.tryGzip = tryGZIP;
    }

    /** @see HTTPClient#isTryGzip() */
    @Override
    public boolean isTryGzip() {
        return tryGzip;
    }

    /**
     * Appends query parameters to an existing URL.
     *
     * @param oldUrl The original URL to which parameters will be appended.
     * @param appendQuery A map containing key-value pairs to be appended as query parameters.
     * @return A new URL with the appended query parameters.
     * @throws MalformedURLException If the resulting URL is malformed.
     */
    protected static URL appendURL(URL oldUrl, Map<String, String> appendQuery) throws MalformedURLException {
        String oldQuery = oldUrl.getQuery();

        StringJoiner stringJoiner = new StringJoiner("&");
        appendQuery.forEach((key, value) -> {
            stringJoiner.add(URLEncoder.encode(key, StandardCharsets.UTF_8) + "="
                    + URLEncoder.encode(value, StandardCharsets.UTF_8));
        });
        String query = stringJoiner.toString();

        String newQuery = oldQuery != null ? oldQuery + "&" + query : query;

        return new URL(oldUrl.getProtocol(), oldUrl.getHost(), oldUrl.getPort(), oldUrl.getPath() + "?" + newQuery);
    }

    /**
     * Percent-encodes the illegal characters (non ASCII and disallowed ASCII) in the path, query and fragment of the
     * given URL, leaving legal characters and existing {@code %XX} escapes untouched (a stray {@code %} becomes
     * {@code %25}). This restores the automatic request line encoding Apache HttpClient 4 did and that HttpClient 5 and
     * {@link URLConnection} do not: without it a capabilities document exposing an online resource with raw UTF
     * characters breaks cascading. The scheme and authority are kept verbatim: only the path, query and fragment are
     * encoded. A raw non-ASCII host is left untouched: percent-encoding it would be wrong, a host needs IDNA/punycode
     * (see RFC 5890) rather than percent escapes, but in practice capabilities hosts are already ASCII.
     */
    protected static URL encodeURL(URL url) throws MalformedURLException {
        String file = url.getFile();
        String ref = url.getRef();
        String encFile = encodeIllegalCharacters(file);
        String encRef = ref == null ? null : encodeIllegalCharacters(ref);
        if (encFile.equals(file) && (ref == null || encRef.equals(ref))) {
            return url;
        }
        StringBuilder sb = new StringBuilder(url.getProtocol()).append("://");
        if (url.getAuthority() != null) {
            sb.append(url.getAuthority());
        }
        sb.append(encFile);
        if (encRef != null) {
            // kept for a faithful transform only: clients strip the fragment before the wire (see RFC 9110 section 7.1)
            sb.append('#').append(encRef);
        }
        return new URL(sb.toString());
    }

    private static final char[] HEX = "0123456789ABCDEF".toCharArray();

    private static String encodeIllegalCharacters(String segment) {
        // iterate by Unicode code point, not by char. A Java char is 16 bit, so characters above U+FFFF ("astral"
        // characters, like most emoji) are stored as two chars, a "surrogate pair". codePointAt combines a valid pair
        // into a single code point and charCount tells how many chars it spanned, so we advance past both.
        StringBuilder sb = null; // allocated lazily, only once the first illegal char or stray '%' is found
        int i = 0;
        while (i < segment.length()) {
            int cp = segment.codePointAt(i);
            boolean legal;
            if (cp == '%') {
                // keep only a well formed %XX escape, a stray '%' becomes %25 (handled below) so it does not produce an
                // invalid escape that the downstream URI parser would reject
                legal = i + 2 < segment.length() && isHex(segment.charAt(i + 1)) && isHex(segment.charAt(i + 2));
            } else {
                legal = cp < 128 && isLegalUriChar((char) cp);
            }
            if (legal) {
                if (sb != null) {
                    sb.append((char) cp);
                }
            } else {
                // first illegal char: materialize the legal prefix scanned so far, then encode from here on
                if (sb == null) {
                    sb = new StringBuilder(segment.length() + 8).append(segment, 0, i);
                }
                appendUtf8PercentEncoded(sb, cp);
            }
            i += Character.charCount(cp);
        }
        return sb == null ? segment : sb.toString();
    }

    private static boolean isHex(char c) {
        return (c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F');
    }

    /**
     * Appends the UTF-8 bytes of the code point as {@code %XX} sequences, computing the encoding without allocating.
     */
    private static void appendUtf8PercentEncoded(StringBuilder sb, int cp) {
        // if a surrogate reaches here it is unpaired (a dangling half of an astral pair, from malformed input):
        // codePointAt returns its raw value in 0xD800..0xDFFF. That is not a real character, so encode it as the
        // Unicode replacement character U+FFFD instead of emitting an invalid UTF-8 sequence the server could not
        // decode
        if (cp >= 0xD800 && cp <= 0xDFFF) {
            cp = 0xFFFD;
        }
        if (cp <= 0x7F) {
            appendPercentByte(sb, cp);
        } else if (cp <= 0x7FF) {
            appendPercentByte(sb, 0xC0 | (cp >> 6));
            appendPercentByte(sb, 0x80 | (cp & 0x3F));
        } else if (cp <= 0xFFFF) {
            appendPercentByte(sb, 0xE0 | (cp >> 12));
            appendPercentByte(sb, 0x80 | ((cp >> 6) & 0x3F));
            appendPercentByte(sb, 0x80 | (cp & 0x3F));
        } else {
            appendPercentByte(sb, 0xF0 | (cp >> 18));
            appendPercentByte(sb, 0x80 | ((cp >> 12) & 0x3F));
            appendPercentByte(sb, 0x80 | ((cp >> 6) & 0x3F));
            appendPercentByte(sb, 0x80 | (cp & 0x3F));
        }
    }

    private static void appendPercentByte(StringBuilder sb, int b) {
        // %XX: high nibble (top 4 bits) then low nibble (bottom 4 bits) as hex digits, e.g. 0xC3 -> "%C3"
        sb.append('%').append(HEX[(b >> 4) & 0xF]).append(HEX[b & 0xF]);
    }

    /**
     * The characters that may appear unescaped in a URI, per <a
     * href="https://www.rfc-editor.org/rfc/rfc3986#section-2">RFC 3986 section 2</a> (unreserved plus the reserved
     * gen-delims and sub-delims), minus {@code #}. {@code %} is absent too. Both are handled outside this set:
     * {@code %} only survives as part of a valid {@code %XX} escape, and {@code #} is always encoded, so the only
     * fragment delimiter is the one {@link #encodeURL(URL)} adds itself (this method sees the path+query and fragment
     * already split, so a raw {@code #} in either would be a spurious delimiter).
     */
    private static boolean isLegalUriChar(char c) {
        return (c >= 'A' && c <= 'Z')
                || (c >= 'a' && c <= 'z')
                || (c >= '0' && c <= '9')
                || "-._~:/?[]@!$&'()*+,;=".indexOf(c) >= 0;
    }

    protected boolean isFile(URL url) {
        return "file".equalsIgnoreCase(url.getProtocol());
    }

    protected HTTPResponse createFileResponse(URL url) throws IOException {
        URLConnection connection = url.openConnection();
        connection.connect();
        return new DefaultHttpResponse(connection);
    }

    @Override
    public abstract HTTPResponse post(URL url, InputStream content, String contentType) throws IOException;
}
