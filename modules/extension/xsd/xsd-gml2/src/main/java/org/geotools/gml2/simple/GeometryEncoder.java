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

import org.geotools.gml2.GML;
import org.geotools.xsd.Encoder;
import org.locationtech.jts.geom.Geometry;
import org.xml.sax.helpers.AttributesImpl;

/**
 * Base class for all encoders writing a Geometry. Implementations should provide one of the two
 * "encode" methods, {@link #encode(Geometry, AttributesImpl, GMLWriter)} or {@link
 * #encode(Geometry, AttributesImpl, GMLWriter, String)}, failing to do so will result in a infinite
 * recursion and eventually in a {@link StackOverflowError}
 *
 * @author Justin Deoliveira, OpenGeo
 * @author Andrea Aime - GeoSolutions
 * @param <T>
 */
public abstract class GeometryEncoder<T extends Geometry> extends ObjectEncoder<T> {

    /** Flag for to encode gml:id as attribute if is true. */
    private boolean encodeGmlId = true;

    protected GeometryEncoder(Encoder encoder) {
        super(encoder);
    }

    protected GeometryEncoder(Encoder encoder, boolean encodeGmlId) {
        super(encoder);
        this.encodeGmlId = encodeGmlId;
    }

    /**
     * Encodes a geometry value with a given gmlId (implementations might choose to use it to
     * generate gml:id attributes, depending on the GML version. The default implementation does not
     * use gmlId and simply delegates to {@link #encode(Geometry, AttributesImpl, GMLWriter)}
     *
     * @param geometry The Geometry to be encoded
     * @param atts Its attributes
     * @param handler The handler used to write XML
     * @param gmlId If not null, some implementation will use to as the gml:id (and to build ids for
     *     the nested features)
     */
    public void encode(T geometry, AttributesImpl atts, GMLWriter handler, String gmlId)
            throws Exception {
        encode(geometry, atts, handler);
    }

    /**
     * Returns a new AttributesImpl based on the provided on, with the addition of a gml:id
     * attribute
     *
     * @param atts The base attributes (can be null)
     * @param gmlId The desired gml:id value
     * @return The provided atts object if gmlId is null, a clone of the provided one plus the
     *     gml:id attribute otherwise
     */
    protected AttributesImpl cloneWithGmlId(AttributesImpl atts, String gmlId) {
        if (gmlId == null || !encodeGmlId) {
            return atts;
        }
        AttributesImpl result;
        if (atts == null) {
            result = new AttributesImpl();
        } else {
            result = new AttributesImpl(atts);
        }
        addGmlId(result, gmlId);

        return result;
    }

    /** Adds a gmlId to an existing and non null attribute set */
    protected void addGmlId(AttributesImpl attributes, String gmlId) {
        attributes.addAttribute(GML.NAMESPACE, "id", "gml:id", null, gmlId);
    }

    /**
     * Encodes a geometry value
     *
     * @param geometry The Geometry to be encoded
     * @param atts Its attributes
     * @param handler The handler used to write XML
     */
    public void encode(T geometry, AttributesImpl atts, GMLWriter handler) throws Exception {
        encode(geometry, atts, handler, null);
    }
}
