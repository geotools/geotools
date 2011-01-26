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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.namespace.QName;

import net.opengis.wfs.GetFeatureType;
import net.opengis.wfs.QueryType;

import org.geotools.data.wfs.protocol.wfs.GetFeature;
import org.geotools.data.wfs.protocol.wfs.WFSOperationType;
import org.geotools.data.wfs.protocol.wfs.WFSProtocol;
import org.geotools.filter.Capabilities;
import org.geotools.filter.v1_0.OGCConfiguration;
import org.geotools.gml2.GML;
import org.geotools.gml2.bindings.GMLBoxTypeBinding;
import org.geotools.gml2.bindings.GMLCoordinatesTypeBinding;
import org.geotools.util.logging.Logging;
import org.geotools.xml.Configuration;
import org.opengis.filter.Filter;
import org.opengis.filter.capability.FilterCapabilities;
import org.opengis.filter.capability.SpatialCapabilities;
import org.opengis.filter.capability.SpatialOperator;
import org.opengis.filter.capability.SpatialOperators;
import org.opengis.filter.spatial.Intersects;
import org.picocontainer.MutablePicoContainer;

import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.impl.PackedCoordinateSequence;

@SuppressWarnings("nls")
public class IonicStrategy extends DefaultWFSStrategy {

    private static final Logger LOGGER = Logging.getLogger("org.geotools.data.wfs");

    /**
     * A filter 1.0 configuration to encode Filters issued to Ionic
     */
    private static final Configuration filter_1_0_0_Configuration = new OGCConfiguration() {
        @Override
        protected void registerBindings(MutablePicoContainer container) {
            super.registerBindings(container);
            // override the binding for GML.BoxType to use the one producing an output Ionic
            // understands
            container.registerComponentImplementation(GML.BoxType, IonicGML2BoxTypeBinding.class);
        }
    };

    /**
     * We can't use POST at all against Ionic cause it is not a WFS 1.1 implementation and expect
     * the filters to be encoded as per Filter 1.0, and I wasn't able of creating a WFS 1.1 with
     * Filter 1.0 {@link Configuration} that works.
     * 
     * @return false
     * @see WFSStrategy#supportsPost()
     */
    @Override
    public boolean supportsPost() {
        return false;
    }

    /**
     * Ionic does not declare the supported output formats in the caps, yet it fails if asked for
     * {@code text/xml; subtype=gml/3.1.1} but succeeds if asked for {@code GML3}
     */
    @Override
    public String getDefaultOutputFormat(WFSProtocol wfs, WFSOperationType op) {
        if (WFSOperationType.GET_FEATURE != op) {
            throw new UnsupportedOperationException(String.valueOf(op));
        }
        return "GML3";
    }

    /**
     * @return a Filter 1.0 configuration since Ionic expects that instead of 1.1
     */
    @Override
    protected Configuration getFilterConfiguration() {
        return filter_1_0_0_Configuration;
    }

    /**
     * Ionic uses {@code urn:opengis:def:crs:ogc::83} instead of {@code EPSG:4269}. If that's the
     * case, the query srsName is replaced by the kown "EPSG:4269" code
     */
    @Override
    public RequestComponents createGetFeatureRequest(WFSProtocol wfs, GetFeature query)
            throws IOException {
        RequestComponents req = super.createGetFeatureRequest(wfs, query);
        GetFeatureType getFeature = req.getServerRequest();
        QueryType queryType = (QueryType) getFeature.getQuery().get(0);
        URI srsNameUri = queryType.getSrsName();
        final String overrideSrs = "urn:opengis:def:crs:ogc::83";
        if (srsNameUri != null && srsNameUri.toString().equalsIgnoreCase(overrideSrs)) {
            try {
                queryType.setSrsName(new URI("EPSG:4269"));
            } catch (URISyntaxException e) {
                throw new RuntimeException("shouln't happen: " + e.getMessage());
            }
            Map<String, String> kvpParameters = req.getKvpParameters();
            kvpParameters.put("SRSNAME", "EPSG:4269");
        }
        return req;
    }

    /**
     * Ionic's capabilities may state the spatial operator {@code Intersect} instead of {@code
     * Intersects}. If so, we fix that here so intersects is actually recognized as a supported
     * filter.
     */
    @Override
    public Filter[] splitFilters(Capabilities caps, Filter queryFilter) {
        FilterCapabilities filterCapabilities = caps.getContents();
        SpatialCapabilities spatialCapabilities = filterCapabilities.getSpatialCapabilities();
        if (spatialCapabilities != null) {
            SpatialOperators spatialOperators = spatialCapabilities.getSpatialOperators();
            if (spatialOperators != null) {
                SpatialOperator missnamedIntersects = spatialOperators.getOperator("Intersect");
                if (missnamedIntersects != null) {
                    LOGGER.fine("Ionic capabilities states the spatial operator Intersect. "
                            + "Assuming it is Intersects and adding Intersects as a "
                            + "supported filter type");
                    caps.addName(Intersects.NAME);
                }
            }
        }

        return super.splitFilters(caps, queryFilter);
    }

    /**
     * A gml:Box binding to override the default one to adapt to the Ionic server that recognizes
     * {@code <gml:Box><gml:coordinates>} but not {@code <gml:Box><gml:coord>...}
     * 
     * @author Gabriel Roldan
     */
    public static class IonicGML2BoxTypeBinding extends GMLBoxTypeBinding {

        /**
         * Returns a {@link CoordinateSequence} for the {@code coordinates} property so its handled
         * by a {@link GMLCoordinatesTypeBinding} at encoding time as {@code gml:coordinates} that
         * Ionic understands
         */
        @Override
        public Object getProperty(Object object, QName name) throws Exception {
            Envelope e = (Envelope) object;
            if (GML.coordinates.equals(name)) {
                double[] seq = { e.getMinX(), e.getMinY(), e.getMaxX(), e.getMaxY() };
                CoordinateSequence coords = new PackedCoordinateSequence.Double(seq, 2);
                return coords;
            }

            return null;
        }
    }
}
