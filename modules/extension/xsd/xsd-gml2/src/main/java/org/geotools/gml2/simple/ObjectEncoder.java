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
package org.geotools.gml2.simple;

import org.geotools.xsd.Encoder;
import org.xml.sax.helpers.AttributesImpl;

/**
 * Base class for all encoders used by {@link FeatureCollectionEncoderDelegate}
 *
 * @author Justin Deoliveira, OpenGeo
 */
public abstract class ObjectEncoder<T extends Object> {

    protected Encoder encoder;

    protected ObjectEncoder(Encoder e) {
        this.encoder = e;
    }

    public abstract void encode(T object, AttributesImpl atts, GMLWriter handler) throws Exception;
}
