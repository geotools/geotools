/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.geotools.util.URLs;
import org.geotools.util.logging.Logging;

/** Static HTTP server that return pre-configure static resources. */
final class HttpStaticServer {

    private static final Logger LOGGER = Logging.getLogger(HttpStaticServer.class);

    private final Map<String, String> resources = new HashMap<>();

    private final ServerConnector connector;
    private final Server server;

    HttpStaticServer() {
        server = new Server(new QueuedThreadPool(7));
        connector = new ServerConnector(server);
        connector.setPort(0);
        server.setConnectors(new Connector[] {connector});
        server.setHandler(new AbstractHandler() {
            @Override
            public void handle(
                    String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) {
                for (Map.Entry<String, String> resource : resources.entrySet()) {
                    if (target != null && target.equalsIgnoreCase("/" + resource.getKey())) {
                        // we found the resource we where looking for
                        response.setContentType("text/xml");
                        response.setStatus(HttpServletResponse.SC_OK);
                        baseRequest.setHandled(true);
                        try (InputStream input =
                                new ByteArrayInputStream(resource.getValue().getBytes(StandardCharsets.UTF_8))) {
                            // write the resource content to the HTTP response output stream
                            IOUtils.copy(input, response.getOutputStream());
                            // we are done
                            return;
                        } catch (Exception exception) {
                            throw new RuntimeException("Error writing HTTP response.", exception);
                        }
                    }
                }
                // request not handled
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                baseRequest.setHandled(true);
            }
        });
    }

    /**
     * Adds a static resource that should be published by the static HTTP server. The resource will be read from the
     * classpath and it will be available at: http://{host}:{port}/{resourceName} Placeholders {host} and {port} will be
     * replace with this server values.
     */
    void putResource(String resourceName, String resourcePath) {
        try (InputStream input = this.getClass().getResourceAsStream(resourcePath)) {
            if (input == null) {
                // we could not find the resource int he classpath
                throw new RuntimeException(
                        String.format("Resource '%s' with path '%s' not found.", resourceName, resourcePath));
            }
            // read the resource from the classpath
            String resource = IOUtils.toString(input, StandardCharsets.UTF_8);
            // substitute host and port place holders
            String relativePath = URLs.urlToFile(
                            URLs.getParentUrl(this.getClass().getResource(resourcePath)))
                    .getPath();
            resource = resource.replace("{relative}", relativePath);
            resource = resource.replace("{host}", getHost());
            resource = resource.replace("{port}", String.valueOf(getPort()));
            // register the resource
            resources.put(resourceName, resource);
            LOGGER.info(String.format("Resource %s added.", buildUrl(resourceName)));
        } catch (Exception exception) {
            throw new RuntimeException(
                    String.format("Error registering resource '%s' with path '%s'.", resourceName, resourcePath),
                    exception);
        }
    }

    /** Starts the static HTTP server. */
    void start() {
        try {
            server.start();
            LOGGER.info(String.format("HTTP static server started '%s:%d'.", getHost(), getPort()));
        } catch (Exception exception) {
            throw new RuntimeException("Error starting the HTTP server.", exception);
        }
    }

    /** Stops the static HTTP server. */
    void stop() {
        try {
            server.stop();
            LOGGER.info("HTTP static server stopped.");
        } catch (Exception exception) {
            throw new RuntimeException("Error stopping the HTTP server.", exception);
        }
    }

    /** Gets the host on which the static HTTP static server is listen on * */
    String getHost() {
        String host = connector.getHost();
        if (host == null) {
            // this means that server is listening on all the available interfaces
            try {
                // get a valid address that cna be used to build valid URLs
                return InetAddress.getLocalHost().getHostName();
            } catch (Exception exception) {
                throw new RuntimeException("Error get local host name.");
            }
        }
        // return the host name the HTTP static server is bind to
        return host;
    }

    /**
     * Gets the port on which the static HTTP static server is listen on. This method will throw an exception if invoked
     * before the server was start or after the server has been stopped.
     */
    int getPort() {
        int port = connector.getLocalPort();
        if (port < 0) {
            // no port was allocated yet, -1 server not started or -2 server stopped
            throw new RuntimeException("Server not started or stopped.");
        }
        return port;
    }

    /**
     * Helper method that build the URL that can be used to request from this HTTP static server the static resource
     * associated with the provided resource name.
     */
    String buildUrl(String resourceName) {
        return String.format("http://%s:%d/%s", getHost(), getPort(), resourceName);
    }

    /** Helper method to let the HTTP static server run in the background, this useful for debugging. */
    void join() {
        try {
            server.join();
        } catch (InterruptedException exception) {
            // we are done, closing time
            LOGGER.info("HTTP static server stop.");
            Thread.currentThread().interrupt();
        }
    }
}
