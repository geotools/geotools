/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2012-2016, Open Source Geospatial Foundation (OSGeo)
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

import java.awt.Rectangle;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.io.range.RangeType;
import org.geotools.coverage.io.util.DateRangeTreeSet;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.builder.GridToEnvelopeMapper;
import org.geotools.util.DateRange;
import org.geotools.util.NumberRange;
import org.opengis.filter.Filter;
import org.opengis.geometry.BoundingBox;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.referencing.operation.TransformException;

/**
 * Request information from a {@link CoverageSource}.
 *
 * <p>Note that we are working with the assumption that the queried coverage has separable
 * dimensions.
 */
public class CoverageReadRequest extends CoverageRequest {

    /**
     * The requested area in the destination raster space.
     *
     * <p>This field shall basically contain the screen dimension of the requested area in pixels.
     *
     * @uml.property name="rasterArea"
     */
    protected Rectangle rasterArea;

    /**
     * The requested area in geographic coordinates, which means the area in destination world space
     * which we want to get data for.
     *
     * @uml.property name="geographicArea"
     */
    protected ReferencedEnvelope geographicArea;

    /**
     * The request {@link MathTransform2D} which would map the pixel into the requested world area.
     *
     * <p>Note that having a raster are and a world area is not enough, unless we have a simple
     * scale-and-translate grid-to-workd transform.
     *
     * @uml.property name="gridToWorldTransform"
     */
    protected MathTransform2D gridToWorldTransform;

    /**
     * The subset of the original {@link RangeType} we want to obtain.
     *
     * @uml.property name="rangeSubset"
     * @uml.associationEnd
     */
    private RangeType rangeSubset;

    /**
     * The vertical positions for which we want to get some data.
     *
     * @uml.property name="verticalSubset"
     */
    private Set<NumberRange<Double>> verticalSubset;

    /**
     * The temporal positions for which we want to get some data.
     *
     * @uml.property name="temporalSubset"
     */
    private SortedSet<DateRange> temporalSubset;

    /** Defines the order and the bands that should be returned. */
    private int[] bands;

    private Map<String, Set<?>> additionalDomainsSubset = new HashMap<String, Set<?>>();

    /** Custom filter for additional queries */
    private Filter filter;

    /** @see org.geotools.coverage.io.CoverageReadRequest#getRangeSubset() */
    public RangeType getRangeSubset() {
        return this.rangeSubset;
    }

    /**
     * @see org.geotools.coverage.io.CoverageReadRequest#setDomainSubset(java.awt.Rectangle,
     *     org.opengis.referencing.operation.MathTransform2D,
     *     org.opengis.referencing.crs.CoordinateReferenceSystem)
     */
    public void setDomainSubset(
            final Rectangle rasterArea,
            final MathTransform2D gridToWorldTrasform,
            final CoordinateReferenceSystem crs)
            throws MismatchedDimensionException, TransformException {

        // get input elements
        this.rasterArea = (Rectangle) rasterArea.clone();
        this.gridToWorldTransform = gridToWorldTrasform;

        // create a bbox
        GeneralEnvelope env =
                CRS.transform(
                        gridToWorldTrasform, new ReferencedEnvelope(rasterArea.getBounds2D(), crs));
        this.geographicArea = new ReferencedEnvelope(new ReferencedEnvelope(env), crs);
    }

    /**
     * @see org.geotools.coverage.io.CoverageReadRequest#setDomainSubset(java.awt.Rectangle,
     *     org.opengis.geometry.BoundingBox, org.opengis.referencing.datum.PixelInCell)
     */
    public void setDomainSubset(final Rectangle rasterArea, final ReferencedEnvelope worldArea) {
        // get input elements
        if (rasterArea != null) {
            this.rasterArea = (Rectangle) rasterArea.clone();
        }
        this.geographicArea = worldArea;

        // create a math transform
        final GridToEnvelopeMapper mapper =
                new GridToEnvelopeMapper(
                        new GridEnvelope2D(rasterArea), new ReferencedEnvelope(worldArea));
        mapper.setPixelAnchor(PixelInCell.CELL_CENTER);
        this.gridToWorldTransform = (MathTransform2D) mapper.createTransform();
    }

    /**
     * Set the range subset we are requesting.
     *
     * <p>Note that a null {@link RangeType} means get everything.
     *
     * @uml.property name="rangeSubset"
     */
    public void setRangeSubset(final RangeType value) {
        this.rangeSubset = value;
    }

    /** @see org.geotools.coverage.io.CoverageReadRequest#getVerticalSubset() */
    public Set<NumberRange<Double>> getVerticalSubset() {
        if (verticalSubset == null) {
            Set<NumberRange<Double>> empty = Collections.emptySet();
            verticalSubset = new HashSet<NumberRange<Double>>(empty);
            return verticalSubset;
        } else return new LinkedHashSet<NumberRange<Double>>(verticalSubset);
    }

    /** @see org.geotools.coverage.io.CoverageReadRequest#setVerticalSubset(java.util.SortedSet) */
    public void setVerticalSubset(Set<NumberRange<Double>> verticalSubset) {
        this.verticalSubset = new LinkedHashSet<NumberRange<Double>>(verticalSubset);
    }

    /** @see org.geotools.coverage.io.CoverageReadRequest#getTemporalSubset() */
    public SortedSet<DateRange> getTemporalSubset() {
        if (temporalSubset == null) {
            temporalSubset = new DateRangeTreeSet();
        }

        return new DateRangeTreeSet(temporalSubset);
    }

    /** @see org.geotools.coverage.io.CoverageReadRequest#setTemporalSubset(java.util.SortedSet) */
    public void setTemporalSubset(SortedSet<DateRange> temporalSubset) {
        this.temporalSubset = new DateRangeTreeSet(temporalSubset);
    }

    public Map<String, Set<?>> getAdditionalDomainsSubset() {
        return additionalDomainsSubset;
    }

    public void setAdditionalDomainsSubset(Map<String, Set<?>> additionalDomainsSubset) {
        this.additionalDomainsSubset = additionalDomainsSubset;
    }

    /** @see org.geotools.coverage.io.CoverageReadRequest#getRasterArea() */
    public Rectangle getRasterArea() {
        return rasterArea != null ? (Rectangle) rasterArea.clone() : rasterArea;
    }

    /** @see org.geotools.coverage.io.CoverageReadRequest#getGeographicArea() */
    public BoundingBox getGeographicArea() {
        return geographicArea;
    }

    /** @see org.geotools.coverage.io.CoverageReadRequest#getGridToWorldTransform() */
    public MathTransform2D getGridToWorldTransform() {
        return gridToWorldTransform;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public Filter getFilter() {
        return filter;
    }

    public int[] getBands() {
        return bands;
    }

    public void setBands(int[] bands) {
        this.bands = bands;
    }
}
