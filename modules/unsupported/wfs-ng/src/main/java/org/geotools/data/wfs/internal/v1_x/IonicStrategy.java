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

import java.util.Map;

import javax.xml.namespace.QName;

import org.geotools.data.wfs.internal.GetFeatureRequest;
import org.geotools.data.wfs.internal.HttpMethod;
import org.geotools.data.wfs.internal.WFSOperationType;
import org.geotools.filter.v1_0.OGCConfiguration;
import org.geotools.gml2.GML;
import org.geotools.gml2.bindings.GMLBoxTypeBinding;
import org.geotools.gml2.bindings.GMLCoordinatesTypeBinding;
import org.geotools.xml.Configuration;
import org.picocontainer.MutablePicoContainer;

import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.impl.PackedCoordinateSequence;

/**
 */
public class IonicStrategy extends StrictWFS_1_x_Strategy {

    /**
     * A filter 1.0 configuration to encode Filters issued to Ionic
     */
    private static final Configuration Ionic_filter_1_0_0_Configuration = new OGCConfiguration() {
        @Override
        protected void registerBindings(MutablePicoContainer container) {
            super.registerBindings(container);
            // override the binding for GML.BoxType to use the one producing an output Ionic
            // understands
            container.registerComponentImplementation(GML.BoxType, IonicGML2BoxTypeBinding.class);
        }
    };

    /**
     * A gml:Box binding to override the default one to adapt to the Ionic server that recognizes
     * {@code <gml:Box><gml:coordinates>} but not {@code <gml:Box><gml:coord>...}
     * 
     * @author Gabriel Roldan
     */
    private static class IonicGML2BoxTypeBinding extends GMLBoxTypeBinding {

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

    /**
     * We can't use POST at all against Ionic cause it is not a WFS 1.1 implementation and expect
     * the filters to be encoded as per Filter 1.0, and I wasn't able of creating a WFS 1.1 with
     * Filter 1.0 {@link Configuration} that works.
     * 
     * @return false
     */
    @Override
    public boolean supportsOperation(WFSOperationType operation, HttpMethod method) {
        if (operation == WFSOperationType.GET_FEATURE && method == HttpMethod.POST) {
            return false;
        }
        return super.supportsOperation(operation, method);
    }

    /**
     * Ionic does not declare the supported output formats in the caps, yet it fails if asked for
     * {@code text/xml; subtype=gml/3.1.1} but succeeds if asked for {@code GML3}
     */
    @Override
    public String getDefaultOutputFormat(WFSOperationType op) {
        if (WFSOperationType.GET_FEATURE.equals(op)) {
            return "GML3";
        }
        return super.getDefaultOutputFormat(op);
    }

    /**
     * @return a Filter 1.0 configuration since Ionic expects that instead of 1.1
     */
    @Override
    public Configuration getFilterConfiguration() {
        return Ionic_filter_1_0_0_Configuration;
    }

    /**
     * Ionic uses {@code urn:opengis:def:crs:ogc::83} instead of {@code EPSG:4269}. If that's the
     * case, the query srsName is replaced by the kown "EPSG:4269" code
     */
    @Override
    protected Map<String, String> buildGetFeatureParametersForGET(GetFeatureRequest request) {
        Map<String, String> params = super.buildGetFeatureParametersForGET(request);

        final String overrideSrs = "urn:opengis:def:crs:ogc::83";
        String srsName = params.get("SRSNAME");
        if (overrideSrs.equals(srsName)) {
            params.put("SRSNAME", "EPSG:4269");
        }

        return params;
    }

}
