/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2014, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General public abstract License for more details.
 */
package org.geotools.data.wfs.internal;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import javax.xml.namespace.QName;
import org.geotools.data.ows.Specification;
import org.geotools.data.wfs.WFSServiceInfo;
import org.geotools.data.wfs.internal.GetFeatureRequest.ResultType;
import org.geotools.util.Version;
import org.geotools.util.factory.GeoTools;
import org.geotools.xsd.Configuration;
import org.opengis.filter.Filter;
import org.opengis.filter.capability.FilterCapabilities;

/**
 * Facade interface to interact with a WFS instance.
 *
 * <p>Implementations of this interface know how to send and get information back from a WFS service
 * for a specific protocol version, but are <b>not</b> meant to provide any logic other than the
 * conversation with the service. For instance, {@code WFSProtocol} implementations are not required
 * to transform {@link Filter filters} to something appropriate for the service capabilities, nor
 * any other control logic than creating and sending the requests mapping what is given to the
 * operation methods.
 *
 * <p>This interface provides enough information extracted or derived from the WFS capabilities
 * document as for the client code to issue requests appropriate for the server capabilities.
 */
public abstract class WFSStrategy extends Specification {

    private Map<WFSOperationType, AtomicLong> requestHandleSequences;

    public WFSStrategy() {
        requestHandleSequences = new HashMap<WFSOperationType, AtomicLong>();
        for (WFSOperationType operationType : WFSOperationType.values()) {
            requestHandleSequences.put(operationType, new AtomicLong());
        }
    }

    public abstract void setCapabilities(WFSGetCapabilities capabilities);

    public abstract void setConfig(WFSConfig config);

    /**
     * Returns the feature type metadata object parsed from the capabilities document for the given
     * {@code typeName}
     *
     * @param typeName the typeName as stated in the capabilities {@code FeatureTypeList} to get the
     *     info for
     * @return the WFS capabilities metadata for {@code typeName}
     * @throws IllegalArgumentException if {@code typeName} is not the name of a FeatureType stated
     *     in the capabilities document.
     */
    public abstract FeatureTypeInfo getFeatureTypeInfo(QName typeName);

    public abstract WFSConfig getConfig();

    /**
     * Returns the WFS protocol version this facade talks to the WFS instance.
     *
     * @return the protocol version in use by this facade
     */
    public abstract Version getServiceVersion();

    /** @return the output formats the server advertises for the given operation; */
    public abstract Set<String> getServerSupportedOutputFormats(final WFSOperationType operation);

    /**
     * @param typeName the feature type name for which to return the supported output formats
     * @return the output formats the server supports for the given type name and operation
     */
    public abstract Set<String> getServerSupportedOutputFormats(
            final QName typeName, final WFSOperationType operation);

    /**
     * @return the list of output formats supported by the client for the given operation, in
     *     preferred order.
     */
    public abstract List<String> getClientSupportedOutputFormats(final WFSOperationType operation);

    /**
     * Returns the set of type names as extracted from the capabilities document, including the
     * namespace and prefix.
     *
     * @return the set of feature type names as extracted from the capabilities document
     */
    public abstract Set<QName> getFeatureTypeNames();

    /**
     * Returns the parsed version of the FilterCapabilities section in the capabilities document
     *
     * @return a {@link FilterCapabilities} out of the FilterCapabilities section in the
     *     getcapabilities document
     */
    public abstract FilterCapabilities getFilterCapabilities();

    /**
     * Splits the filter provided by the geotools query into the server supported and unsupported
     * ones.
     *
     * @return a two-element array where the first element is the supported filter and the second
     *     the one to post-process
     * @see org.geotools.data.wfs.internal.WFSStrategy#splitFilters(org.opengis.filter.Filter)
     */
    public abstract Filter[] splitFilters(QName typeName, Filter filter);

    /**
     * Returns whether the service supports the given operation for the given HTTP method.
     *
     * @param operation the operation to check if the server supports
     * @param method the HTTP method to check if the server supports for the given operation
     * @return {@code true} if the operation/method is supported as stated in the WFS capabilities
     */
    public abstract boolean supportsOperation(
            final WFSOperationType operation, final HttpMethod method);

    public abstract boolean supports(ResultType resultType);

    public abstract boolean supportsTransaction(QName typeName);

    /**
     * Returns the URL for the given operation name and HTTP protocol as stated in the WFS
     * capabilities.
     *
     * @param operation the name of the WFS operation
     * @param method the HTTP method
     * @return The URL access point for the given operation and method or {@code null} if the
     *     capabilities does not declare an access point for the operation/method combination
     * @see #supportsOperation(WFSOperationType, HttpMethod)
     */
    public abstract URL getOperationURL(final WFSOperationType operation, final HttpMethod method);

    /**
     * Returns the union of the default CRS and the other supported CRS's of the given feature type
     * as declared in the corresponding FeatureType element in the capabilities document, as well as
     * any globally supported CRS identifier declared as a parameter of the GetFeature oepration in
     * the capabilities document.
     *
     * @param typeName the featuretype name as declared in the FeatureType/Name element of the WFS
     *     capabilities
     * @return the list of supported CRS identifiers for the given feature type
     */
    public abstract Set<String> getSupportedCRSIdentifiers(final QName typeName);

    /**
     * Allows to free any resource held.
     *
     * <p>Successive calls to this method should not result in any exception, but the instance is
     * meant to not be usable after the first invocation.
     */
    public abstract void dispose();

    public abstract String getDefaultOutputFormat(WFSOperationType operation);

    public abstract URL buildUrlGET(WFSRequest request);

    public abstract String getPostContentType(WFSRequest wfsRequest);

    /** Returns the input stream with the POST body contents for the given request. */
    public abstract InputStream getPostContents(WFSRequest wfsRequest) throws IOException;

    public abstract WFSServiceInfo getServiceInfo();

    public abstract Configuration getWfsConfiguration();

    public String newRequestHandle(WFSOperationType operation) {
        StringBuilder handle =
                new StringBuilder("GeoTools ")
                        .append(GeoTools.getVersion())
                        .append("(")
                        .append(GeoTools.getBuildRevision())
                        .append(") WFS ")
                        .append(getVersion())
                        .append(" DataStore @");
        try {
            handle.append(InetAddress.getLocalHost().getHostName());
        } catch (Exception ignore) {
            handle.append("<uknown host>");
        }

        AtomicLong reqHandleSeq = requestHandleSequences.get(operation);
        handle.append('#').append(reqHandleSeq.incrementAndGet());
        return handle.toString();
    }

    /**
     * Provide additional field type mappings
     *
     * @return field type mappings
     */
    public abstract Map<QName, Class<?>> getFieldTypeMappings();

    /** @return */
    public boolean canLimit() {
        return true;
    }
}
