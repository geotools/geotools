/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wps;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.opengis.wps10.WPSCapabilitiesType;
import org.geotools.api.data.ResourceInfo;
import org.geotools.api.data.ServiceInfo;
import org.geotools.data.ows.GetCapabilitiesRequest;
import org.geotools.data.ows.Request;
import org.geotools.data.ows.Response;
import org.geotools.data.ows.Specification;
import org.geotools.http.HTTPClient;
import org.geotools.http.HTTPClientFinder;
import org.geotools.http.HTTPResponse;
import org.geotools.ows.ServiceException;

/**
 * This abstract class provides a building block for one to implement a WPS client.
 *
 * <p>This class provides version negotiation, Capabilities document retrieval, and a request/response infrastructure.
 * Implementing subclasses need to provide their own Specifications
 *
 * @author gdavis
 */
public abstract class AbstractWPS<C extends WPSCapabilitiesType, R extends Object> {

    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(AbstractWPS.class);
    protected HTTPClient httpClient;
    protected final URL serverURL;
    protected C capabilities;
    protected ServiceInfo info;
    protected Map<R, ResourceInfo> resourceInfo = new HashMap<>();

    /** Contains the specifications that are to be used with this service */
    protected Specification[] specs;

    protected Specification specification;

    /**
     * Set up the specifications used and retrieve the Capabilities document given by serverURL.
     *
     * @param serverURL a URL that points to the capabilities document of a server
     * @throws IOException if there is an error communicating with the server
     * @throws ServiceException if the server responds with an error
     */
    public AbstractWPS(final URL serverURL) throws IOException, ServiceException {
        this(serverURL, HTTPClientFinder.createClient(), null);

        capabilities = negotiateVersion();
        if (capabilities == null) {
            throw new ServiceException("Unable to retrieve or parse Capabilities document.");
        } else if (capabilities != null) {
            setupSpecification(capabilities);
        }
    }

    public AbstractWPS(final URL serverURL, final HTTPClient httpClient, final C capabilities)
            throws ServiceException, IOException {
        if (serverURL == null) {
            throw new NullPointerException("serverURL");
        }
        if (httpClient == null) {
            throw new NullPointerException("httpClient");
        }

        this.serverURL = serverURL;
        this.httpClient = httpClient;

        setupSpecifications();

        if (capabilities != null) {
            setupSpecification(capabilities);

            this.capabilities = capabilities;
        } else {
            this.capabilities = negotiateVersion();
            if (this.capabilities == null) {
                throw new ServiceException("Unable to retrieve or parse Capabilities document.");
            }
            setupSpecification(this.capabilities);
        }
    }

    /** @param capabilities */
    private void setupSpecification(final C capabilities) {
        for (Specification spec : specs) {
            if (spec.getVersion().equals(capabilities.getVersion())) {
                specification = spec;

                break;
            }
        }

        if (specification == null) {
            specification = specs[specs.length - 1];
            LOGGER.warning("Unable to choose a specification based on cached capabilities. "
                    + "Arbitrarily choosing spec '"
                    + specification.getVersion()
                    + "'.");
        }
    }

    public void setHttpClient(HTTPClient httpClient) {
        this.httpClient = httpClient;
    }

    public HTTPClient getHTTPClient() {
        return this.httpClient;
    }

    /**
     * Description of this service.
     *
     * <p>Provides a very quick description of the service, for more information please review the capabilitie document.
     *
     * <p>
     *
     * @return description of this service.
     */
    public ServiceInfo getInfo() {
        synchronized (capabilities) {
            if (info == null) {
                info = createInfo();
            }

            return info;
        }
    }

    /**
     * Implemented by a subclass to describe service
     *
     * @return ServiceInfo
     */
    protected abstract ServiceInfo createInfo();

    public ResourceInfo getInfo(R resource) {
        synchronized (capabilities) {
            if (!resourceInfo.containsKey(resource)) {
                resourceInfo.put(resource, createInfo(resource));
            }
        }

        return resourceInfo.get(resource);
    }

    protected abstract ResourceInfo createInfo(R resource);

    /** Sets up the specifications/versions that this server is capable of communicating with. */
    protected abstract void setupSpecifications();

    /**
     * Version number negotiation occurs as follows (credit OGC):
     *
     * <ul>
     *   <li><b>1) </b> If the server implements the requested version number, the server shall send that version.
     *   <li><b>2a) </b> If a version unknown to the server is requested, the server shall send the highest version less
     *       than the requested version.
     *   <li><b>2b) </b> If the client request is for a version lower than any of those known to the server, then the
     *       server shall send the lowest version it knows.
     *   <li><b>3a) </b> If the client does not understand the new version number sent by the server, it may either
     *       cease communicating with the server or send a new request with a new version number that the client does
     *       understand but which is less than that sent by the server (if the server had responded with a lower
     *       version).
     *   <li><b>3b) </b> If the server had responded with a higher version (because the request was for a version lower
     *       than any known to the server), and the client does not understand the proposed higher version, then the
     *       client may send a new request with a version number higher than that sent by the server.
     * </ul>
     *
     * <p>The OGC tells us to repeat this process (or give up). This means we are actually going to come up with a bit
     * of setup cost in figuring out our GetCapabilities request. This means that it is possible that we may make
     * multiple requests before being satisfied with a response.
     *
     * <p>Also, if we are unable to parse a given version for some reason, for example, malformed XML, we will request a
     * lower version until we have run out of versions to request with. Thus, a server that does not play nicely may
     * take some time to parse and might not even succeed.
     *
     * @return a capabilities object that represents the Capabilities on the server
     * @throws IOException if there is an error communicating with the server, or the XML cannot be parsed
     * @throws ServiceException if the server returns a ServiceException
     */
    protected C negotiateVersion() throws IOException, ServiceException {
        List<String> versions = new ArrayList<>(specs.length);
        Exception exception = null;

        for (int i = 0; i < specs.length; i++) {
            versions.add(i, specs[i].getVersion());
        }

        int minClient = 0;
        int maxClient = specs.length - 1;

        int test = maxClient;

        while (minClient <= test && test <= maxClient) {
            Specification tempSpecification = specs[test];
            String clientVersion = tempSpecification.getVersion();

            GetCapabilitiesRequest request = tempSpecification.createGetCapabilitiesRequest(serverURL);

            // Grab document
            C tempCapabilities;
            try {
                @SuppressWarnings("unchecked")
                C caps = (C) issueRequest(request).getCapabilities();
                tempCapabilities = caps;
            } catch (ServiceException e) {
                tempCapabilities = null;
                exception = e;
            }

            int compare = -1;
            String serverVersion = clientVersion; // Ignored if caps is null

            if (tempCapabilities != null) {

                serverVersion = tempCapabilities.getVersion();

                compare = serverVersion.compareTo(clientVersion);
            }

            if (compare == 0) {
                // we have an exact match and have capabilities as well!
                this.specification = tempSpecification;

                return tempCapabilities;
            }

            if (tempCapabilities != null && versions.contains(serverVersion)) {
                // we can communicate with this server
                int index = versions.indexOf(serverVersion);
                this.specification = specs[index];

                return tempCapabilities;

            } else if (compare < 0) {
                // server responded lower then we asked - and we don't understand.
                maxClient = test - 1; // set current version as limit

                // lets try and go one lower?
                //
                clientVersion = before(versions, serverVersion);

                if (clientVersion == null) {
                    if (exception != null) {
                        if (exception instanceof ServiceException) {
                            throw (ServiceException) exception;
                        }

                        IOException e = new IOException(exception.getMessage());
                        throw e;
                    }

                    return null; // do not know any lower version numbers
                }

                test = versions.indexOf(clientVersion);
            } else {
                // server responsed higher than we asked - and we don't understand
                minClient = test + 1; // set current version as lower limit

                // lets try and go one higher
                clientVersion = after(versions, serverVersion);

                if (clientVersion == null) {
                    if (exception != null) {
                        if (exception instanceof ServiceException) {
                            throw (ServiceException) exception;
                        }

                        IOException e = new IOException(exception.getMessage());
                        throw e;
                    }

                    return null; // do not know any lower version numbers
                }

                test = versions.indexOf(clientVersion);
            }
        }

        // could not talk to this server
        if (exception != null) {
            IOException e = new IOException(exception.getMessage());
            throw e;
        }

        return null;
    }

    /**
     * Utility method returning the known version, just before the provided version
     *
     * @param known List<String> of all known versions
     * @param version the boundary condition
     * @return the version just below the provided boundary version
     */
    String before(List known, String version) {
        if (known.isEmpty()) {
            return null;
        }

        String before = null;

        for (Object o : known) {
            String test = (String) o;

            if (test.compareTo(version) < 0) {

                if (before == null || before.compareTo(test) < 0) {
                    before = test;
                }
            }
        }

        return before;
    }

    /**
     * Utility method returning the known version, just after the provided version
     *
     * @param known a List<String> of all known versions
     * @param version the boundary condition
     * @return a version just after the provided boundary condition
     */
    String after(List known, String version) {
        if (known.isEmpty()) {
            return null;
        }

        String after = null;

        for (Object o : known) {
            String test = (String) o;

            if (test.compareTo(version) > 0) {
                if (after == null || after.compareTo(test) < 0) {
                    after = test;
                }
            }
        }

        return after;
    }

    /**
     * Issues a request to the server and returns that server's response. It asks the server to send the response
     * gzipped to provide a faster transfer time.
     *
     * @param request the request to be issued
     * @return a response from the server, which is created according to the specific Request
     * @throws IOException if there was a problem communicating with the server
     * @throws ServiceException if the server responds with an exception or returns bad content
     */
    protected Response internalIssueRequest(Request request) throws IOException, ServiceException {
        final URL finalURL = request.getFinalURL();

        final HTTPResponse httpResponse;

        if (request.requiresPost()) {

            final String postContentType = request.getPostContentType();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            request.performPostOutput(out);

            try (InputStream in = new ByteArrayInputStream(out.toByteArray())) {
                httpResponse = httpClient.post(finalURL, in, postContentType);
            }
        } else {
            httpResponse = httpClient.get(finalURL);
        }

        final Response response = request.createResponse(httpResponse);

        return response;
    }

    public AbstractWPSGetCapabilitiesResponse issueRequest(GetCapabilitiesRequest request)
            throws IOException, ServiceException {
        return (AbstractWPSGetCapabilitiesResponse) internalIssueRequest(request);
    }

    public void setLoggingLevel(Level newLevel) {
        LOGGER.setLevel(newLevel);
    }
}
