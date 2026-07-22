/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.util;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import org.geotools.util.logging.Logging;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * EntityResolver implementation to prevent use external entity resolution to local files.
 *
 * <p>When parsing an XML entity reference to a local file a SAXException is thrown, which can be handled appropriately.
 *
 * <p>For a more restrictive EntityResolver {@link DefaultEntityResolver} is recommended.
 *
 * @author Davide Savazzi - geo-solutions.it
 */
public class PreventLocalEntityResolver implements EntityResolver3, Serializable {

    /** serialVersionUID */
    @Serial
    private static final long serialVersionUID = -5689036455423814933L;

    /** Prefix used for SAXException message */
    public static final String ERROR_MESSAGE_BASE = "Entity resolution disallowed for ";

    protected static final Logger LOGGER = Logging.getLogger(PreventLocalEntityResolver.class);

    /**
     * Allow uri references schema parsing for validation.
     *
     * <ul>
     *   <li>allow {@code xsd} schema parsing for validation
     *   <li>{@code http(s)} - external schema reference
     *   <li>{@code jar} - internal schema reference
     *   <li>{@code jar:nested} - internal schema reference (SpringBoot)
     *   <li>{@code vfs} - internal schema reference (JBoss/WildFly)
     * </ul>
     */
    private static final Pattern ALLOWED_URIS = Pattern.compile("(?i)(jar:file|jar:nested|http|vfs)[^?#;]*\\.xsd");

    /** Max number of redirects followed when fetching an allow-listed http(s) URI, to avoid a redirect loop */
    private static final int MAX_REDIRECTS =
            Integer.getInteger("org.geotools.util.preventLocalEntityResolver.maxRedirects", 5);

    /**
     * Connect/read timeout (seconds) when fetching an allow-listed http(s) URI, same default as
     * {@code SimpleHttpClient}
     */
    private static final int TIMEOUT_SECONDS =
            Integer.getInteger("org.geotools.util.preventLocalEntityResolver.timeout", 30);

    /** Singleton instance of PreventLocalEntityResolver */
    public static final PreventLocalEntityResolver INSTANCE = new PreventLocalEntityResolver();

    protected PreventLocalEntityResolver() {
        // singleton
    }

    /**
     * PreventLocalEntityResolver provides access {@code "http"} and internal {@code "jar:file,jar:nested,vfs"}
     * protocols.
     *
     * @return {@code "http,jar:file,jar:nested,vfs"}
     */
    @Override
    public String getAccess() {
        return "http,jar:file,jar:nested,vfs";
    }

    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        return resolveEntity(null, publicId, null, systemId);
    }

    @Override
    public InputSource getExternalSubset(String name, String baseURI) throws SAXException, IOException {
        return null;
    }

    /**
     * Tells the parser to resolve the systemId against the baseURI and read the entity text from that resulting
     * absolute URI. Note that because the older {@link DefaultHandler#resolveEntity DefaultHandler.resolveEntity()},
     * method is overridden to call this one, this method may sometimes be invoked with null <em>name</em> and
     * <em>baseURI</em>, and with the <em>systemId</em> already absolutized.
     */
    @Override
    public InputSource resolveEntity(String name, String publicId, String baseURI, String systemId)
            throws SAXException, IOException {

        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.finest("resolveEntity request: name=%s, publicId=%s, baseURI=%s, systemId=%s"
                    .formatted(name, publicId, baseURI, systemId));
        }

        String uri = null;
        try {
            uri = systemId;
            // ignore the baseURI if the systemId is absolute
            if (!URI.create(systemId).isAbsolute()) {
                // use the baseURI to convert a relative systemId to absolute
                uri = new URL(new URL(baseURI), systemId).toString();
            }
        } catch (Exception e) {
            // do nothing, uri stays null and the resolution is rejected below
        }

        // check if the absolute systemId is an allowed URI
        if (uri != null && ALLOWED_URIS.matcher(uri).matches()) {
            // only http(s) can redirect; fetch it here so the redirect target gets re-validated too
            if (uri.regionMatches(true, 0, "http", 0, 4)) {
                return fetchAllowedHttpUri(uri);
            }
            return null;
        }

        // do not allow external entities
        throw new SAXException(ERROR_MESSAGE_BASE + systemId);
    }

    /**
     * Fetches the content of an http(s) URI already validated against {@link #ALLOWED_URIS}, following redirects
     * manually and re-validating each redirect target against the same allow-list before following it.
     */
    private static InputSource fetchAllowedHttpUri(String uri) throws IOException, SAXException {

        String currentUri = uri;

        for (int redirects = 0; redirects <= MAX_REDIRECTS; redirects++) {

            HttpURLConnection connection = (HttpURLConnection) new URL(currentUri).openConnection();
            connection.setInstanceFollowRedirects(false);
            connection.setConnectTimeout(TIMEOUT_SECONDS * 1000);
            connection.setReadTimeout(TIMEOUT_SECONDS * 1000);

            int status = connection.getResponseCode();
            if (status < HttpURLConnection.HTTP_MULT_CHOICE || status >= 400) {
                InputSource source = new InputSource(connection.getInputStream());
                source.setSystemId(currentUri);
                return source;
            }

            String location = connection.getHeaderField("Location");

            connection.disconnect();

            String resolvedLocation = resolveRedirectLocation(currentUri, location);

            if (!ALLOWED_URIS.matcher(resolvedLocation).matches()) {
                throw new SAXException(ERROR_MESSAGE_BASE + "redirect to disallowed URL " + resolvedLocation);
            }

            currentUri = resolvedLocation;
        }

        throw new SAXException(ERROR_MESSAGE_BASE + "too many redirects for " + uri);
    }

    /** Resolves a {@code Location} redirect header against the URI it was received from, absolutizing it if needed. */
    private static String resolveRedirectLocation(String currentUri, String location) throws SAXException {
        try {
            return URI.create(location).isAbsolute() ? location : new URL(new URL(currentUri), location).toString();
        } catch (Exception e) {
            throw new SAXException(ERROR_MESSAGE_BASE + "invalid redirect target for " + currentUri);
        }
    }
}
