/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.io;

import javax.measure.unit.Unit;
import org.geotools.image.io.metadata.GeographicMetadata;
import org.geotools.image.io.metadata.ImageReferencing;
import org.geotools.referencing.CRS;
import org.geotools.resources.CRSUtilities;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.ProjectedCRS;
import org.opengis.referencing.cs.CoordinateSystem;
import org.opengis.referencing.datum.Datum;
import org.opengis.referencing.datum.Ellipsoid;
import org.opengis.referencing.operation.Projection;


/**
 * Gets geographic metadata from a {@linkplain GridCoverage grid coverage}, and fills
 * a {@link GeographicMetadata} object using these data.
 *
 * For the moment, this class only contains static getters which return various
 * information from a {@linkplain GridCoverage grid coverage}.
 *
 * @todo Uses those information, gotten from getters, to write into a
 *       {@link GeographicMetadata}.
 *
 * @source $URL$
 * @version $Id$
 * @author Cédric Briançon
 *
 * @since 2.5
 */
class MetadataWriter {
    /**
     * The {@linkplain GeographicMetadata geographic metadata} in which different
     * values will be written.
     */
    private final GeographicMetadata metadata;

    /**
     *
     * @param metadata A {@linkplain GeographicMetadata geographic metadata}. Can be
     *                 {@code null} in this case a default metadata will be instanciated.
     */
    public MetadataWriter(final GeographicMetadata metadata) {
        this.metadata = (metadata != null) ? metadata : new GeographicMetadata();
    }

    /**
     * Fills the {@linkplain GeographicMetadata metadata tree} with information found
     * from a {@linkplain GridCoverage grid coverage}.
     *
     * @param coverage A {@linkplain GridCoverage grid coverage}. Should not be {@code null}.
     */
    public void fillMetadataTree(final GridCoverage coverage) {
        final ImageReferencing referencing = metadata.getReferencing();
        final CoordinateReferenceSystem crs = getCoordinateReferenceSystem(coverage);
        final Datum datum = getDatum(coverage);
        final Ellipsoid ellipsoid = getEllipsoid(coverage);
        final Projection projection = getProjection(coverage);
        referencing.setCoordinateReferenceSystem(crs.getName().getCode(), null);
        referencing.setDatum(datum.getName().getCode(), null);
        referencing.setEllipsoidName(ellipsoid.getName().getCode());
        referencing.setProjectionName(projection.getName().getCode());
    }

    /**
     * Returns the {@linkplain CoordinateReferenceSystem coordinate reference system}
     * from a {@linkplain GridCoverage grid coverage}.
     *
     * @param coverage A {@linkplain GridCoverage grid coverage}. Should not be
     *                 {@code null}.
     */
    public static CoordinateReferenceSystem getCoordinateReferenceSystem(
                  final GridCoverage coverage)
    {
        return coverage.getCoordinateReferenceSystem();
    }

    /**
     * Returns the {@linkplain Datum datum} from a {@linkplain GridCoverage grid coverage},
     * or {@code null} if not defined.
     *
     * @param coverage A {@linkplain GridCoverage grid coverage}. Should not be
     *                 {@code null}.
     */
    public static Datum getDatum(final GridCoverage coverage) {
        return CRSUtilities.getDatum(coverage.getCoordinateReferenceSystem());
    }

    /**
     * Returns the {@linkplain Ellipsoid ellipsoid} from a
     * {@linkplain GridCoverage grid coverage}, or {@code null} if not defined.
     *
     * @param coverage A {@linkplain GridCoverage grid coverage}. Should not be
     *                 {@code null}.
     */
    public static Ellipsoid getEllipsoid(final GridCoverage coverage) {
        return CRS.getEllipsoid(coverage.getCoordinateReferenceSystem());
    }

    /**
     * Returns the {@linkplain OperationMethod operation method name} from a
     * {@linkplain GridCoverage grid coverage}, or {@code null} if not defined.
     *
     * @param coverage A {@linkplain GridCoverage grid coverage}. Should not be
     *                 {@code null}.
     */
    public static String getOperationMethod(final GridCoverage coverage) {
        final Projection projection = getProjection(coverage);
        return (projection != null) ? projection.getName().getCode() : null;
    }

    /**
     * Returns the {@linkplain Projection projection} from a
     * {@linkplain GridCoverage grid coverage}, or {@code null} if not defined.
     *
     * @param coverage A {@linkplain GridCoverage grid coverage}. Should not be
     *                 {@code null}.
     */
    public static Projection getProjection(final GridCoverage coverage) {
        final ProjectedCRS crs = CRS.getProjectedCRS(
                coverage.getCoordinateReferenceSystem());
        return (crs != null) ? crs.getConversionFromBase() : null;
    }

    /**
     * Returns the {@linkplain Unit unit} from a {@linkplain GridCoverage grid coverage},
     * or {@code null} if the unit was not correctly defined.
     *
     * @param coverage A {@linkplain GridCoverage grid coverage}. Should not be
     *                 {@code null}.
     */
    public static Unit getUnit(final GridCoverage coverage) {
        Unit unit = null;
        final CoordinateReferenceSystem crs = coverage.getCoordinateReferenceSystem();
        if (crs != null) {
            final CoordinateSystem cs = crs.getCoordinateSystem();
            if (cs != null) {
                for (int i = cs.getDimension(); --i >= 0;) {
                    final Unit<?> candidate = cs.getAxis(i).getUnit();
                    if (candidate != null) {
                        if (unit == null) {
                            unit = candidate;
                        } else if (!unit.equals(candidate)) {
                            return null;
                        }
                    }
                }
            }
        }
        return unit;
    }
}
