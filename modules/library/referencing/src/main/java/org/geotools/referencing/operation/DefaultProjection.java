/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
 *
 *    This package contains documentation from OpenGIS specifications.
 *    OpenGIS consortium's work is fully acknowledged here.
 */
package org.geotools.referencing.operation;

import java.util.Map;

import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.OperationMethod;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.Conversion;
import org.opengis.referencing.operation.Projection;


/**
 * A {@linkplain DefaultConversion conversion} transforming
 * (<var>longitude</var>,<var>latitude</var>) coordinates to cartesian coordinates
 * (<var>x</var>,<var>y</var>).
 *
 * <P>An unofficial list of projections and their parameters can
 * be found <A HREF="http://www.remotesensing.org/geotiff/proj_list/">there</A>.
 * Most projections expect the following parameters:
 *  <code>"central_meridian"</code> (default to 0),
 *  <code>"latitude_of_origin"</code> (default to 0),
 *  <code>"scale_factor"</code> (default to 1),
 *  <code>"false_easting"</code> (default to 0) and
 *  <code>"false_northing"</code> (default to 0).</P>
 *
 * @since 2.1
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 *
 * @see org.geotools.referencing.crs.DefaultProjectedCRS
 * @see <A HREF="http://mathworld.wolfram.com/MapProjection.html">Map projections on MathWorld</A>
 */
public class DefaultProjection extends DefaultConversion implements Projection {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = -7176751851369816864L;

    /**
     * Constructs a new projection with the same values than the specified one, together with the
     * specified source and target CRS. While the source conversion can be an arbitrary one, it is
     * typically a {@linkplain DefiningConversion defining conversion}.
     *
     * @param conversion The defining conversion.
     * @param sourceCRS The source CRS.
     * @param targetCRS The target CRS.
     * @param transform Transform from positions in the {@linkplain #getSourceCRS source CRS}
     *                  to positions in the {@linkplain #getTargetCRS target CRS}.
     */
    public DefaultProjection(final Conversion               conversion,
                             final CoordinateReferenceSystem sourceCRS,
                             final CoordinateReferenceSystem targetCRS,
                             final MathTransform             transform)
    {
        super(conversion, sourceCRS, targetCRS, transform);
    }

    /**
     * Constructs a projection from a set of properties. The properties given in argument
     * follow the same rules than for the {@link AbstractCoordinateOperation} constructor.
     *
     * @param properties Set of properties. Should contains at least {@code "name"}.
     * @param sourceCRS The source CRS, or {@code null} if not available.
     * @param targetCRS The target CRS, or {@code null} if not available.
     * @param transform Transform from positions in the {@linkplain #getSourceCRS source coordinate
     *                  reference system} to positions in the {@linkplain #getTargetCRS target
     *                  coordinate reference system}.
     * @param method    The operation method.
     */
    public DefaultProjection(final Map<String,?>             properties,
                             final CoordinateReferenceSystem sourceCRS,
                             final CoordinateReferenceSystem targetCRS,
                             final MathTransform             transform,
                             final OperationMethod           method)
    {
        super(properties, sourceCRS, targetCRS, transform, method);
    }
}
