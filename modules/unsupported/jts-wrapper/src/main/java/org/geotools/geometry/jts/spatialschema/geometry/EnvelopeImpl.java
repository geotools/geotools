/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
/*$************************************************************************************************
 **
 ** $Id$
 **
 ** $Source: /cvs/ctree/LiteGO1/src/jar/com/polexis/lite/spatialschema/geometry/EnvelopeImpl.java,v $
 **
 ** Copyright (C) 2003 Open GIS Consortium, Inc. All Rights Reserved. http://www.opengis.org/Legal/
 **
 *************************************************************************************************/
package org.geotools.geometry.jts.spatialschema.geometry;

import java.util.Objects;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.Envelope;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import si.uom.NonSI;

/**
 * A minimum bounding box or rectangle. Regardless of dimension, an {@code Envelope} can be
 * represented without ambiguity as two direct positions (coordinate points). To encode an {@code
 * Envelope}, it is sufficient to encode these two points. This is consistent with all of the data
 * types in this specification, their state is represented by their publicly accessible
 * attributes. @UML datatype GM_Envelope
 *
 * @author ISO/DIS 19107
 * @author <A HREF="http://www.opengis.org">OpenGIS&reg; consortium</A>
 * @version 2.0
 */
public class EnvelopeImpl implements Envelope {

    // *************************************************************************
    //  Fields
    // *************************************************************************

    /**
     * DirectPosition that has the minimum values for each coordinate dimension (e.g. min x and min
     * y).
     */
    private DirectPosition lowerCorner;

    /**
     * DirectPosition that has the maximum values for each coordinate dimension (e.g. max x and max
     * y).
     */
    private DirectPosition upperCorner;

    // *************************************************************************
    //  Constructors
    // *************************************************************************

    /** Creates a new {@code EnvelopeImpl}. */
    public EnvelopeImpl(final DirectPosition lowerCorner, final DirectPosition upperCorner) {
        this.lowerCorner = new DirectPositionImpl(lowerCorner);
        this.upperCorner = new DirectPositionImpl(upperCorner);
    }

    // *************************************************************************
    //  implement the Envelope interface
    // *************************************************************************

    /**
     * @inheritDoc
     * @see org.opengis.geometry.coordinate.Envelope#getDimension()
     */
    public final int getDimension() {
        return upperCorner.getDimension();
    }

    /**
     * @inheritDoc
     * @see org.opengis.geometry.coordinate.Envelope#getMinimum(int)
     */
    public final double getMinimum(int dimension) {
        return lowerCorner.getOrdinate(dimension);
    }

    /**
     * @inheritDoc
     * @see org.opengis.geometry.coordinate.Envelope#getMaximum(int)
     */
    public final double getMaximum(int dimension) {
        return upperCorner.getOrdinate(dimension);
    }

    /**
     * @inheritDoc
     * @see org.opengis.geometry.coordinate.Envelope#getMedian(int)
     */
    public final double getMedian(int dimension) {
        return 0.5 * (upperCorner.getOrdinate(dimension) + lowerCorner.getOrdinate(dimension));
    }

    /**
     * @inheritDoc
     * @see org.opengis.geometry.coordinate.Envelope#getSpan(int)
     */
    public final double getSpan(int dimension) {
        return upperCorner.getOrdinate(dimension) - lowerCorner.getOrdinate(dimension);
    }

    /**
     * @inheritDoc
     * @see org.opengis.geometry.coordinate.Envelope#getUpperCorner()
     */
    public final DirectPosition getUpperCorner() {
        return new DirectPositionImpl(upperCorner);
    }

    /**
     * @inheritDoc
     * @see org.opengis.geometry.coordinate.Envelope#getLowerCorner()
     */
    public final DirectPosition getLowerCorner() {
        return new DirectPositionImpl(lowerCorner);
    }

    /**
     * @inheritDoc
     * @see java.lang.Object#toString()
     */
    public String toString() {
        final double[] bbox = GeometryUtils.getBBox(this, NonSI.DEGREE_ANGLE);

        final StringBuffer returnable = new StringBuffer("Envelope[").append(bbox[0]);
        for (int i = 1; i < bbox.length; i++) {
            returnable.append(",").append(bbox[i]);
        }
        return returnable.append("]").toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(lowerCorner, upperCorner);
    }

    /**
     * @inheritDoc
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(final Object obj) {
        final Envelope that = (Envelope) obj;
        return GeometryUtils.equals(this, that);
    }

    public CoordinateReferenceSystem getCoordinateReferenceSystem() {
        return getUpperCorner().getCoordinateReferenceSystem();
    }
}
