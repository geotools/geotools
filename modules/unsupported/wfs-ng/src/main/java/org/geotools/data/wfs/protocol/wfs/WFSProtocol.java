/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs.protocol.wfs;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Set;

import javax.xml.namespace.QName;

import org.geotools.data.wfs.protocol.http.HttpMethod;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.filter.Filter;
import org.opengis.filter.capability.FilterCapabilities;

/**
 * Facade interface to interact with a WFS instance.
 * <p>
 * Implementations of this interface know how to send and get information back from a WFS service
 * for a specific protocol version, but are <b>not</b> meant to provide any logic other than the
 * conversation with the service. For instance, {@code WFSProtocol} implementations are not required
 * to transform {@link Filter filters} to something appropriate for the service capabilities, nor
 * any other control logic than creating and sending the requests mapping what is given to the
 * operation methods.
 * </p>
 * <p>
 * This interface provides enough information extracted or derived from the WFS capabilities
 * document as for the client code to issue requests appropriate for the server capabilities.
 * </p>
 * 
 * @author Gabriel Roldan (OpenGeo)
 * @version $Id: WFSProtocol.java 33654 2009-07-30 11:58:11Z groldan $
 * @since 2.6
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/unsupported/wfs-ng/src/main/java/org/geotools/data/wfs/protocol/wfs/WFSProtocol.java $
 *         http://gtsvn.refractions.net/trunk/modules/plugin/wfs/src/main/java/org/geotools/data
 *         /wfs/protocol/wfs/WFSProtocol.java $
 */
public interface WFSProtocol {

    /**
     * Returns the WFS protocol version this facade talks to the WFS instance.
     * 
     * @return the protocol version in use by this facade
     */
    public Version getServiceVersion();

    /**
     * Returns service title as stated in the capabilities document
     * 
     * @return the service title
     */
    public String getServiceTitle();

    /**
     * Returns service abstract as stated in the capabilities document
     * 
     * @return the service abstract, may be {@code null}
     */
    public String getServiceAbstract();

    /**
     * Returns service keywords as stated in the capabilities document
     * 
     * @return the service keywords, may be empty
     */
    public Set<String> getServiceKeywords();

    /**
     * Returns service provider URI as stated in the capabilities document
     * 
     * @return the service provider URI
     */
    public URI getServiceProviderUri();

    /**
     * Returns the output format names declared in the GetFeature operation metadata section of the
     * WFS capabilities document
     * 
     * @return the global GetFeature output formats
     */
    public Set<String> getSupportedGetFeatureOutputFormats();

    /**
     * Returns the union of {@link #getSupportedGetFeatureOutputFormats()} and the output formats
     * declared for the feature type specifically in the FeatureTypeList section of the capabilities
     * document for the given feature type
     * 
     * @param typeName
     *            the feature type name for which to return the supported output formats
     * @return the output formats supported by {@code typeName}
     */
    public Set<String> getSupportedOutputFormats(final String typeName);

    /**
     * Returns the set of type names as extracted from the capabilities document, including the
     * namespace and prefix.
     * 
     * @return the set of feature type names as extracted from the capabilities document
     */
    public Set<QName> getFeatureTypeNames();

    /**
     * Returns the full feature type name for the {@code typeName} as declared in the {@code
     * FeatureTypeList/FeatureType/Name} element of the capabilities document.
     * <p>
     * The returned QName contains the namespace, localname as well as the prefix. {@code typeName}
     * is known to be {@code prefix:localName}.
     * </p>
     * 
     * @param typeName
     *            the prefixed type name to get the full name for
     * @return the full name of the given feature type
     * @throws IllegalArgumentException
     *             if the {@code typeName} does not exist
     */
    public QName getFeatureTypeName(final String typeName);

    /**
     * Returns the parsed version of the FilterCapabilities section in the capabilities document
     * 
     * @return a {@link FilterCapabilities} out of the FilterCapabilities section in the
     *         getcapabilities document
     */
    public FilterCapabilities getFilterCapabilities();

    /**
     * Returns whether the service supports the given operation for the given HTTP method.
     * 
     * @param operation
     *            the operation to check if the server supports
     * @param method
     *            the HTTP method to check if the server supports for the given operation
     * @return {@code true} if the operation/method is supported as stated in the WFS capabilities
     */
    public boolean supportsOperation(final WFSOperationType operation, final boolean post);

    /**
     * Returns the URL for the given operation name and HTTP protocol as stated in the WFS
     * capabilities.
     * 
     * @param operation
     *            the name of the WFS operation
     * @param method
     *            the HTTP method
     * @return The URL access point for the given operation and method or {@code null} if the
     *         capabilities does not declare an access point for the operation/method combination
     * @see #supportsOperation(WFSOperationType, HttpMethod)
     */
    public URL getOperationURL(final WFSOperationType operation, final boolean post);

    /**
     * Returns the title of the given feature type as declared in the corresponding FeatureType
     * element in the capabilities document.
     * 
     * @param typeName
     *            the featuretype name as declared in the FeatureType/Name element of the WFS
     *            capabilities
     * @return the title for the given feature type
     */
    public String getFeatureTypeTitle(final String typeName);

    /**
     * Returns the abstract of the given feature type as declared in the corresponding FeatureType
     * element in the capabilities document.
     * 
     * @param typeName
     *            the featuretype name as declared in the FeatureType/Name element of the WFS
     *            capabilities
     * @return the abstract for the given feature type
     */
    public String getFeatureTypeAbstract(final String typeName);

    /**
     * Returns the lat lon envelope of the given feature type as declared in the corresponding
     * FeatureType element in the capabilities document.
     * 
     * @param typeName
     *            the featuretype name as declared in the FeatureType/Name element of the WFS
     *            capabilities
     * @return a WGS84 envelope representing the bounds declared for the feature type in the
     *         capabilities document
     */
    public ReferencedEnvelope getFeatureTypeWGS84Bounds(final String typeName);

    /**
     * Returns the CRS identifier of the default CRS for the given feature type as declared in the
     * corresponding FeatureType element in the capabilities document.
     * 
     * @param typeName
     *            the featuretype name as declared in the FeatureType/Name element of the WFS
     *            capabilities
     * @return the default CRS for the given feature type
     */
    public String getDefaultCRS(final String typeName);

    /**
     * Returns the union of the default CRS and the other supported CRS's of the given feature type
     * as declared in the corresponding FeatureType element in the capabilities document.
     * 
     * @param typeName
     *            the featuretype name as declared in the FeatureType/Name element of the WFS
     *            capabilities
     * @return the list of supported CRS identifiers for the given feature type
     */
    public Set<String> getSupportedCRSIdentifiers(final String typeName);

    /**
     * Returns the list of keywords of the given feature type as declared in the corresponding
     * FeatureType element in the capabilities document.
     * 
     * @param typeName
     *            the featuretype name as declared in the FeatureType/Name element of the WFS
     *            capabilities
     * @return the keywords for the given feature type
     */
    public Set<String> getFeatureTypeKeywords(final String typeName);

    /**
     * Returns the http GET request to get the gml schema for the given type name
     * 
     * @param typeName
     * @return
     */
    public URL getDescribeFeatureTypeURLGet(final String typeName);

    /**
     * Issues a DescribeFeatureType request for the given type name and output format using the HTTP
     * GET method
     * 
     * @throws IOException
     * @throws UnsupportedOperationException
     */
    public WFSResponse describeFeatureTypeGET(final String typeName, final String outputFormat)
            throws IOException, UnsupportedOperationException;

    /**
     * Issues a DescribeFeatureType request for the given type name and output format using the HTTP
     * POST method
     * 
     * @throws IOException
     * @throws UnsupportedOperationException
     */
    public WFSResponse describeFeatureTypePOST(final String typeName, final String outputFormat)
            throws IOException, UnsupportedOperationException;

    /**
     * Issues a GetFeature request for the given request, using GET HTTP method
     * <p>
     * The {@code request} shall already be adapted to what the server supports in terms of filter
     * capabilities and CRS reprojection. The {@code WFSProtocol} implementation is not required to
     * check if the query filter is fully supported nor if the CRS is supported for the feature
     * type.
     * </p>
     * 
     * @param request
     *            the request to send to the WFS, as is.
     * @param kvp
     *            the key/value pair representation of the request to build the query string from
     * @return
     * @throws IOException
     * @throws UnsupportedOperationException
     */
    public WFSResponse issueGetFeatureGET(final GetFeature request) throws IOException,
            UnsupportedOperationException;

    /**
     * Issues a GetFeature request for the given request, using POST HTTP method
     * <p>
     * The query to WFS request parameter translation is the same than for
     * {@link #issueGetFeatureGET(GetFeature)}
     * </p>
     */
    public WFSResponse issueGetFeaturePOST(GetFeature request) throws IOException,
            UnsupportedOperationException;

    /**
     * Allows to free any resource held.
     * <p>
     * Successive calls to this method should not result in any exception, but the instance is meant
     * to not be usable after the first invocation.
     * </p>
     */
    public void dispose();

    public String getDefaultOutputFormat(WFSOperationType get_feature);

    public Filter[] splitFilters(Filter filter);
}
