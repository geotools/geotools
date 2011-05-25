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
package org.geotools.data.wfs.v1_1_0;

import static net.opengis.wfs.ResultTypeType.HITS_LITERAL;
import static net.opengis.wfs.ResultTypeType.RESULTS_LITERAL;
import static org.geotools.data.wfs.protocol.wfs.GetFeature.ResultType.RESULTS;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import net.opengis.wfs.GetFeatureType;
import net.opengis.wfs.QueryType;
import net.opengis.wfs.WfsFactory;

import org.geotools.data.Query;
import org.geotools.data.wfs.protocol.wfs.GetFeature;
import org.geotools.data.wfs.protocol.wfs.WFSOperationType;
import org.geotools.data.wfs.protocol.wfs.WFSProtocol;
import org.geotools.data.wfs.protocol.wfs.GetFeature.ResultType;
import org.geotools.factory.GeoTools;
import org.geotools.filter.Capabilities;
import org.geotools.filter.v1_1.OGC;
import org.geotools.filter.v1_1.OGCConfiguration;
import org.geotools.filter.visitor.CapabilitiesFilterSplitter;
import org.geotools.util.logging.Logging;
import org.geotools.wfs.v1_1.WFSConfiguration;
import org.geotools.xml.Configuration;
import org.geotools.xml.Encoder;
import org.opengis.filter.Filter;
import org.opengis.filter.Id;
import org.opengis.filter.identity.Identifier;
import org.opengis.filter.sort.SortBy;

/**
 * A default strategy for a WFS 1.1.0 implementation that assumes the server sticks to the standard.
 * 
 * @author Gabriel Roldan (OpenGeo)
 * @version $Id: DefaultWFSStrategy.java 35310 2010-04-30 10:32:15Z jive $
 * @since 2.6
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/unsupported/wfs-ng/src/main/java/org/geotools/data/wfs/v1_1_0/DefaultWFSStrategy.java $
 *         http://gtsvn.refractions.net/trunk/modules/plugin/wfs/src/main/java/org/geotools/data
 *         /wfs/v1_1_0/DefaultWFSStrategy.java $
 */
@SuppressWarnings("nls")
public class DefaultWFSStrategy implements WFSStrategy {

    private static final Logger LOGGER = Logging.getLogger("org.geotools.data.wfs");

    protected static final String DEFAULT_OUTPUT_FORMAT = "text/xml; subtype=gml/3.1.1";

    private static final Configuration filter_1_1_0_Configuration = new OGCConfiguration();

    private static final Configuration wfs_1_1_0_Configuration = new WFSConfiguration();

    /**
     * @see WFSStrategy#supportsGet()
     */
    public boolean supportsGet() {
        return true;
    }

    /**
     * @see WFSStrategy#supportsPost()
     */
    public boolean supportsPost() {
        return true;
    }

    /**
     * @return {@code "text/xml; subtype=gml/3.1.1"}
     * @see WFSProtocol#getDefaultOutputFormat()
     */
    public String getDefaultOutputFormat(WFSProtocol wfs, WFSOperationType operation) {
        if (WFSOperationType.GET_FEATURE != operation) {
            throw new UnsupportedOperationException(
                    "Not implemented for other than GET_FEATURE yet");
        }

        Set<String> supportedOutputFormats = wfs.getSupportedGetFeatureOutputFormats();
        if (supportedOutputFormats.contains(DEFAULT_OUTPUT_FORMAT)) {
            return DEFAULT_OUTPUT_FORMAT;
        }
        throw new IllegalArgumentException("Server does not support '" + DEFAULT_OUTPUT_FORMAT
                + "' output format: " + supportedOutputFormats);
    }

    /**
     * Creates the mapping {@link GetFeatureType GetFeature} request for the given {@link Query} and
     * {@code outputFormat}, and post-processing filter based on the server's stated filter
     * capabilities.
     * 
     * @see WFSStrategy#createGetFeatureRequest(WFS_1_1_0_DataStore, WFSProtocol, Query, String)
     */
    @SuppressWarnings("unchecked")
    public RequestComponents createGetFeatureRequest(WFSProtocol wfs, GetFeature query)
            throws IOException {
        final WfsFactory factory = WfsFactory.eINSTANCE;

        GetFeatureType getFeature = factory.createGetFeatureType();
        getFeature.setService("WFS");
        getFeature.setVersion(wfs.getServiceVersion().toString());
        getFeature.setOutputFormat(query.getOutputFormat());

        getFeature.setHandle("GeoTools " + GeoTools.getVersion() + " WFS DataStore");
        Integer maxFeatures = query.getMaxFeatures();
        if (maxFeatures != null) {
            getFeature.setMaxFeatures(BigInteger.valueOf(maxFeatures.intValue()));
        }

        ResultType resultType = query.getResultType();
        getFeature.setResultType(RESULTS == resultType ? RESULTS_LITERAL : HITS_LITERAL);

        QueryType wfsQuery = factory.createQueryType();
        wfsQuery.setTypeName(Collections.singletonList(query.getTypeName()));

        Filter serverFilter = query.getFilter();
        if (!Filter.INCLUDE.equals(serverFilter)) {
            wfsQuery.setFilter(serverFilter);
        }
        String srsName = query.getSrsName();
        try {
            wfsQuery.setSrsName(new URI(srsName));
        } catch (URISyntaxException e) {
            throw new RuntimeException("Can't create a URI from the query CRS: " + srsName, e);
        }
        String[] propertyNames = query.getPropertyNames();
        boolean retrieveAllProperties = propertyNames == null;
        if (!retrieveAllProperties) {
            List propertyName = wfsQuery.getPropertyName();
            for (String propName : propertyNames) {
                propertyName.add(propName);
            }
        }
        SortBy[] sortByList = query.getSortBy();
        if (sortByList != null) {
            for (SortBy sortBy : sortByList) {
                wfsQuery.getSortBy().add(sortBy);
            }
        }

        getFeature.getQuery().add(wfsQuery);

        RequestComponents reqParts = new RequestComponents();
        reqParts.setServerRequest(getFeature);

        Map<String, String> parametersForGet = buildGetFeatureParametersForGet(getFeature);
        reqParts.setKvpParameters(parametersForGet);

        return reqParts;
    }

    @SuppressWarnings("unchecked")
    protected Map<String, String> buildGetFeatureParametersForGet(GetFeatureType request)
            throws IOException {
        Map<String, String> map = new HashMap<String, String>();
        map.put("SERVICE", "WFS");
        map.put("VERSION", "1.1.0");
        map.put("REQUEST", "GetFeature");
        map.put("OUTPUTFORMAT", request.getOutputFormat());

        if (request.getMaxFeatures() != null) {
            map.put("MAXFEATURES", String.valueOf(request.getMaxFeatures()));
        }

        final QueryType query = (QueryType) request.getQuery().get(0);
        final String typeName = (String) query.getTypeName().get(0);
        map.put("TYPENAME", typeName);

        if (query.getPropertyName().size() > 0) {
            List<String> propertyNames = query.getPropertyName();
            StringBuilder pnames = new StringBuilder();
            for (Iterator<String> it = propertyNames.iterator(); it.hasNext();) {
                pnames.append(it.next());
                if (it.hasNext()) {
                    pnames.append(',');
                }
            }
            map.put("PROPERTYNAME", pnames.toString());
        }

        // SRSNAME parameter. Let the server reproject.
        // TODO: should check if the server supports the required crs
        URI srsName = query.getSrsName();
        if (srsName != null) {
            map.put("SRSNAME", srsName.toString());
        }
        final Filter filter = query.getFilter();

        if (filter != null && Filter.INCLUDE != filter) {
            if (filter instanceof Id) {
                final Set<Identifier> identifiers = ((Id) filter).getIdentifiers();
                StringBuffer idValues = new StringBuffer();
                for (Iterator<Identifier> it = identifiers.iterator(); it.hasNext();) {
                    Object id = it.next().getID();
                    // REVISIT: should URL encode the id?
                    idValues.append(String.valueOf(id));
                    if (it.hasNext()) {
                        idValues.append(",");
                    }
                }
                map.put("FEATUREID", idValues.toString());
            } else {
                String xmlEncodedFilter = encodeGetFeatureGetFilter(filter);
                map.put("FILTER", xmlEncodedFilter);
            }
        }

        return map;
    }

    /**
     * Returns a single-line string containing the xml representation of the given filter, as
     * appropriate for the {@code FILTER} parameter in a GetFeature request.
     */
    protected String encodeGetFeatureGetFilter(final Filter filter) throws IOException {
        Configuration filterConfig = getFilterConfiguration();
        Encoder encoder = new Encoder(filterConfig);
        // do not write the xml declaration
        encoder.setOmitXMLDeclaration(true);
        encoder.setEncoding(Charset.forName("UTF-8"));

        OutputStream out = new ByteArrayOutputStream();
        encoder.encode(filter, OGC.Filter, out);
        String encoded = out.toString();
        encoded = encoded.replaceAll("\n", "");
        return encoded;
    }

    protected Configuration getFilterConfiguration() {
        return filter_1_1_0_Configuration;
    }

    /**
     * @see WFSStrategy#getWfsConfiguration()
     */
    public Configuration getWfsConfiguration() {
        return wfs_1_1_0_Configuration;
    }

    /**
     * Splits the filter provided by the geotools query into the server supported and unsupported
     * ones.
     * 
     * @param caps
     *            the server filter capabilities description
     * @param queryFilter
     * @return a two-element array where the first element is the supported filter and the second
     *         the one to post-process
     * @see WFSStrategy#splitFilters(WFS_1_1_0_Protocol, Filter)
     */
    public Filter[] splitFilters(Capabilities caps, Filter queryFilter) {
        CapabilitiesFilterSplitter splitter = new CapabilitiesFilterSplitter(
                caps, null, null);

        queryFilter.accept(splitter, null);

        Filter server = splitter.getFilterPre();
        Filter post = splitter.getFilterPost();

        return new Filter[] { server, post };
    }

}
