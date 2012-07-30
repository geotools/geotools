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
package org.geotools.data.wfs.internal.v1_x;

import static org.geotools.data.wfs.internal.Loggers.requestTrace;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.transform.TransformerException;

import net.opengis.wfs.GetFeatureType;

import org.geotools.data.wfs.internal.GetFeatureRequest;
import org.geotools.data.wfs.internal.GetFeatureRequest.ResultType;
import org.geotools.data.wfs.internal.WFSRequest;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.visitor.SimplifyingFilterVisitor;
import org.geotools.xml.Encoder;
import org.opengis.filter.BinaryLogicOperator;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.Or;
import org.opengis.filter.spatial.BinarySpatialOperator;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.Document;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.SAXException;

/**
 * A strategy object to aid in querying a CubeWerx WFS 1.1 server
 * <p>
 * This strategy was created as per the limitations encountered at the CubeWerx server being tested
 * while developing this plugin.
 * </p>
 * <p>
 * For instance, the following issues were found:
 * <ul>
 * <li>resultType parameter is not supported in GetFeature
 * <li>Logically grouped spatial filters can't be handled
 * <li>CubeWerx does not support logical filters containing mixed geometry filters (eg, AND(BBOX,
 * Intersects)), no matter what the capabilities doc says
 * </ul>
 * </p>
 * 
 * @author groldan
 */
public class CubeWerxStrategy extends StrictWFS_1_x_Strategy {

    /**
     * @return {@code true} only if resultType == results, CubeWerx throws a service exception if
     *         the resultType parameter is set on a POST request, no matter it's value, and on a GET
     *         request it's just ignored; also the returned feature collection does not contain the
     *         number of features matched.
     */
    @Override
    public boolean supports(final ResultType resultType) {
        return ResultType.RESULTS.equals(resultType);
    }

    /**
     * Removes the {@code RESULTTYPE}.
     * 
     * @see org.geotools.data.wfs.internal.AbstractWFSStrategy#buildGetFeatureParametersForGET(org.geotools.data.wfs.internal.GetFeatureRequest)
     */
    @Override
    protected Map<String, String> buildGetFeatureParametersForGET(GetFeatureRequest request) {
        Map<String, String> params = super.buildGetFeatureParametersForGET(request);
        params.remove("RESULTTYPE");
        return params;
    }

    @Override
    public InputStream getPostContents(WFSRequest request) throws IOException {
        if (!(request instanceof GetFeatureRequest)) {
            return super.getPostContents(request);
        }

        GetFeatureType requestObject = createGetFeatureRequestPost((GetFeatureRequest) request);

        final Encoder encoder = prepareEncoder(request);
        final QName opName = getOperationName(request.getOperation());

        Document dom;
        try {
            dom = encoder.encodeAsDOM(requestObject, opName);
        } catch (SAXException e) {
            throw new IOException(e);
        } catch (TransformerException e) {
            throw new IOException(e);
        }

        dom.getDocumentElement().removeAttribute("resultType");
        DOMImplementationLS domImpl = (DOMImplementationLS) dom.getImplementation();// safe cast as
                                                                                    // long as we're
                                                                                    // on Java6

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        LSOutput destination = domImpl.createLSOutput();
        destination.setByteStream(out);
        LSSerializer serializer = domImpl.createLSSerializer();
        DOMConfiguration domConfig = serializer.getDomConfig();
        if (domConfig.canSetParameter("format-pretty-print", Boolean.TRUE)) {
            domConfig.setParameter("format-pretty-print", Boolean.TRUE);
        }
        serializer.write(dom, destination);

        requestTrace("Encoded ", request.getOperation(), " request: ", out);

        return new ByteArrayInputStream(out.toByteArray());

    }

    @Override
    public Filter[] splitFilters(final QName typeName, final Filter queryFilter) {

        Filter[] splitFilters = super.splitFilters(typeName, queryFilter);

        Filter serverFilter = splitFilters[0];
        Filter postFilter = splitFilters[1];

        if (!(serverFilter instanceof BinaryLogicOperator)) {
            return splitFilters;
        }

        postFilter = queryFilter;

        if (serverFilter instanceof Or) {
            // can't know...
            serverFilter = Filter.INCLUDE;
        } else {
            boolean spatialAdded = false;
            // if a logical operator, check no more than one geometry filter is enclosed on it
            List<Filter> children = new ArrayList<Filter>(
                    ((BinaryLogicOperator) serverFilter).getChildren());
            for (Iterator<Filter> it = children.iterator(); it.hasNext();) {
                Filter f = it.next();
                if (f instanceof BinarySpatialOperator) {
                    if (spatialAdded) {
                        it.remove();
                    } else {
                        spatialAdded = true;
                    }
                }
            }
            FilterFactory ff = CommonFactoryFinder.getFilterFactory();
            serverFilter = ff.and(children);
            SimplifyingFilterVisitor sfv = new SimplifyingFilterVisitor();
            serverFilter = (Filter) serverFilter.accept(sfv, null);
        }
        return new Filter[] { serverFilter, postFilter };
    }
}
