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
 ** $Source: /cvs/ctree/LiteGO1/src/jar/com/polexis/lite/spatialschema/geometry/primitive/PointImpl.java,v $
 **
 ** Copyright (C) 2003 Open GIS Consortium, Inc. All Rights Reserved. http://www.opengis.org/Legal/
 **
 *************************************************************************************************/
package org.geotools.geometry.jts.spatialschema.geometry.primitive;

import java.util.Collections;
import java.util.Set;
import org.geotools.factory.BasicFactories;
import org.geotools.geometry.jts.spatialschema.geometry.DirectPositionImpl;
import org.geotools.geometry.jts.spatialschema.geometry.GeometryImpl;
import org.geotools.geometry.jts.spatialschema.geometry.JTSUtils;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.UnmodifiableGeometryException;
import org.opengis.geometry.complex.Composite;
import org.opengis.geometry.coordinate.Position;
import org.opengis.geometry.primitive.Bearing;
import org.opengis.geometry.primitive.OrientablePrimitive;
import org.opengis.geometry.primitive.Point;
import org.opengis.geometry.primitive.PrimitiveBoundary;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.CoordinateOperation;
import org.opengis.referencing.operation.CoordinateOperationFactory;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.OperationNotFoundException;
import org.opengis.referencing.operation.TransformException;

/**
 * Basic data type for a geometric object consisting of one and only one point. In most cases, the
 * state of a {@code Point} is fully determined by its position attribute. The only exception to
 * this is if the {@code Point} has been subclassed to provide additional non-geometric information
 * such as symbology.
 *
 * @author SYS Technologies
 * @author crossley
 * @author cdillard
 * @version $Revision $
 */
public class PointImpl extends GeometryImpl implements Point {

    // *************************************************************************
    //  Members
    // *************************************************************************

    private DirectPosition position;

    // *************************************************************************
    //  Constructors
    // *************************************************************************

    /** Creates a new {@code PointImpl}. */
    public PointImpl() {
        this(null, DefaultGeographicCRS.WGS84);
    }

    /** Creates a new {@code PointImpl}. */
    public PointImpl(final DirectPosition position) {
        this(position, position.getCoordinateReferenceSystem());
    }

    /** Creates a new {@code PointImpl}. */
    public PointImpl(final DirectPosition position, final CoordinateReferenceSystem crs) {
        super(crs);
        this.position = (position == null) ? new DirectPositionImpl(crs) : position;
    }

    // *************************************************************************
    //  Methods
    // *************************************************************************

    /**
     * Returns a copy of this point's position. We must return a copy (and not a reference to our
     * internal object), otherwise the caller could modify the values of the object and we would not
     * know.
     */
    public DirectPosition getDirectPosition() {
        return new DirectPositionImpl(position);
    }

    /**
     * Makes a copy of the given point and keeps that copy around. If the given point is not in the
     * same coordinate reference system as this primitive, then we attempt to convert it.
     */
    public void setDirectPosition(final DirectPosition position)
            throws UnmodifiableGeometryException {
        if (isMutable()) {
            CoordinateReferenceSystem myCRS = getCoordinateReferenceSystem();
            CoordinateReferenceSystem pointCRS = position.getCoordinateReferenceSystem();
            DirectPosition copy = new DirectPositionImpl(position);
            if ((myCRS != null) && (pointCRS != null) && (!myCRS.equals(pointCRS))) {
                // Do the conversion.
                try {
                    BasicFactories commonFactory = BasicFactories.getDefault();
                    CoordinateOperationFactory cof = commonFactory.getCoordinateOperationFactory();
                    CoordinateOperation coordOp = cof.createOperation(pointCRS, myCRS);
                    MathTransform mt = coordOp.getMathTransform();
                    mt.transform(position, copy);
                } catch (OperationNotFoundException e) {
                    throw new RuntimeException("Unable to find an operation", e);
                } catch (FactoryException e) {
                    throw new RuntimeException("Factory exception", e);
                } catch (TransformException e) {
                    throw new RuntimeException("Error transforming", e);
                }
            }
            // Copy the position into our member.
            this.position = copy;
            // Let our cache know that something has changed so we can recompute.
            invalidateCachedJTSPeer();
        } else {
            throw new UnmodifiableGeometryException();
        }
    }

    public PrimitiveBoundary getBoundary() {
        return (PrimitiveBoundary) super.getBoundary();
    }

    /** Not supported in this implementation. */
    public Bearing getBearing(final Position toPoint) {
        throw new UnsupportedOperationException("Bearing calculation is not supported");
    }

    /** Computes the JTS equivalent of this geometry. */
    protected org.locationtech.jts.geom.Geometry computeJTSPeer() {
        return JTSUtils.directPositionToPoint(position);
    }

    public Set getContainedPrimitives() {
        return Collections.EMPTY_SET;
    }

    public Set getContainingPrimitives() {
        throw new UnsupportedOperationException();
    }

    public Set getComplexes() {
        throw new UnsupportedOperationException();
    }

    public Composite getComposite() {
        return null;
    }

    public OrientablePrimitive[] getProxy() {
        return null;
    }

    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((position == null) ? 0 : position.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final PointImpl other = (PointImpl) obj;
        if (position == null) {
            if (other.position != null) return false;
        } else if (!position.equals(other.position)) return false;
        return true;
    }
}
