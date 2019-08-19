/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.wfs.v1_0;

import javax.xml.namespace.QName;
import net.opengis.wfs.FeatureCollectionType;
import net.opengis.wfs.WfsFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.gml2.GMLConfiguration;
import org.geotools.gml2.simple.GML2FeatureCollectionEncoderDelegate;
import org.geotools.gml3.GML;
import org.geotools.xsd.Encoder;

/**
 * A WFS 1.0 specific binding with optimizations for {@link SimpleFeatureCollection} encoding
 *
 * @author Andrea Aime - GeoSolutions
 */
public class FeatureCollectionTypeBinding
        extends org.geotools.wfs.bindings.FeatureCollectionTypeBinding {

    private Encoder encoder;

    public FeatureCollectionTypeBinding(WfsFactory factory, Encoder encoder) {
        super(factory);
        this.encoder = encoder;
    }

    public FeatureCollectionTypeBinding(WfsFactory factory) {
        super(factory);
    }

    @Override
    public Object getProperty(Object object, QName name) throws Exception {
        if (GML.featureMember.equals(name)) {
            FeatureCollectionType fc = (FeatureCollectionType) object;
            if (fc.getFeature().size() == 1
                    && fc.getFeature().get(0) instanceof SimpleFeatureCollection
                    && encoder.getConfiguration()
                            .hasProperty(GMLConfiguration.OPTIMIZED_ENCODING)) {
                return new GML2FeatureCollectionEncoderDelegate(
                        (SimpleFeatureCollection) fc.getFeature().get(0), encoder);
            }
        }

        return super.getProperty(object, name);
    }

    /** @return the encoder */
    public Encoder getEncoder() {
        return encoder;
    }

    /** @param encoder the encoder to set */
    public void setEncoder(Encoder encoder) {
        this.encoder = encoder;
    }
}
