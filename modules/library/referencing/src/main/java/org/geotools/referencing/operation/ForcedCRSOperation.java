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
package org.geotools.referencing.operation;

import java.util.Collection;
import java.util.Set;
import org.opengis.metadata.extent.Extent;
import org.opengis.metadata.quality.PositionalAccuracy;
import org.opengis.referencing.ReferenceIdentifier;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.CoordinateOperation;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.util.GenericName;
import org.opengis.util.InternationalString;

/**
 * Used by {@link AuthorityBackedFactory} when concanating operations, in case we're concatenating
 * identities, but so that the source or target CRS are not the same as the non identity part. This
 * happens, for example, when trying to work against a projected CRS with a wrapped geographic CRS
 * axis in lon/lat order, and with the database providing an operation that uses the same projected
 * CRS around a geographic CRS with axis in lat/lon order
 *
 * @author Andrea Aime - GeoSolutions
 */
class ForcedCRSOperation implements CoordinateOperation {

    CoordinateOperation delegate;

    CoordinateReferenceSystem sourceCRS;

    CoordinateReferenceSystem targetCRS;

    public ForcedCRSOperation(
            CoordinateOperation delegate,
            CoordinateReferenceSystem sourceCRS,
            CoordinateReferenceSystem targetCRS) {
        this.delegate = delegate;
        this.sourceCRS = sourceCRS;
        this.targetCRS = targetCRS;
    }

    @Override
    public CoordinateReferenceSystem getSourceCRS() {
        return sourceCRS;
    }

    @Override
    public CoordinateReferenceSystem getTargetCRS() {
        return targetCRS;
    }

    @Override
    public ReferenceIdentifier getName() {
        return delegate.getName();
    }

    @Override
    public Collection<GenericName> getAlias() {
        return delegate.getAlias();
    }

    @Override
    public Set<ReferenceIdentifier> getIdentifiers() {
        return delegate.getIdentifiers();
    }

    @Override
    public InternationalString getRemarks() {
        return delegate.getRemarks();
    }

    @Override
    public String toWKT() throws UnsupportedOperationException {
        return delegate.toWKT();
    }

    @Override
    public String getOperationVersion() {
        return delegate.getOperationVersion();
    }

    @Override
    public Collection<PositionalAccuracy> getCoordinateOperationAccuracy() {
        return delegate.getCoordinateOperationAccuracy();
    }

    @Override
    public Extent getDomainOfValidity() {
        return delegate.getDomainOfValidity();
    }

    @Override
    public InternationalString getScope() {
        return delegate.getScope();
    }

    @Override
    public MathTransform getMathTransform() {
        return delegate.getMathTransform();
    }
}
