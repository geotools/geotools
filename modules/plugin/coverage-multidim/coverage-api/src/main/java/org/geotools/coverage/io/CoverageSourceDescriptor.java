/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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

import java.io.IOException;
import java.util.List;
import org.geotools.coverage.grid.io.DimensionDescriptor;
import org.geotools.coverage.io.CoverageSource.AdditionalDomain;
import org.geotools.coverage.io.CoverageSource.SpatialDomain;
import org.geotools.coverage.io.CoverageSource.TemporalDomain;
import org.geotools.coverage.io.CoverageSource.VerticalDomain;
import org.geotools.coverage.io.range.RangeType;

/**
 * Describes a {@link CoverageSource} in terms of Name, {@link SpatialDomain}, {@link
 * VerticalDomain}, {@link TemporalDomain}, {@link AdditionalDomain}s, {@link DimensionDescriptor}s
 */
public class CoverageSourceDescriptor {

    private boolean hasTemporalDomain = false;

    private boolean hasVerticalDomain = false;

    private boolean hasAdditionalDomains = false;

    private String name;

    /** range type of the wrapped coverage source */
    private RangeType rangeType;

    /** spatial domain of the wrapped coverage source */
    private SpatialDomain spatialDomain;

    /** temporal domain of the wrapped coverage source */
    private TemporalDomain temporalDomain;

    /** vertical domain of the wrapped coverage source */
    private VerticalDomain verticalDomain;

    /** additional domains of the wrapped coverage source */
    private List<AdditionalDomain> additionalDomains;

    private List<DimensionDescriptor> dimensionDescriptors;

    public String getName() {
        return name;
    }

    protected void setName(String varName) {
        this.name = varName;
    }

    public RangeType getRangeType() {
        return rangeType;
    }

    public SpatialDomain getSpatialDomain() {
        return spatialDomain;
    }

    public TemporalDomain getTemporalDomain() {
        return temporalDomain;
    }

    public VerticalDomain getVerticalDomain() {
        return verticalDomain;
    }

    public boolean isHasTemporalDomain() {
        return hasTemporalDomain;
    }

    public boolean isHasVerticalDomain() {
        return hasVerticalDomain;
    }

    protected void setHasTemporalDomain(boolean hasTemporalDomain) {
        this.hasTemporalDomain = hasTemporalDomain;
    }

    protected void setHasVerticalDomain(boolean hasVerticalDomain) {
        this.hasVerticalDomain = hasVerticalDomain;
    }

    public boolean isHasAdditionalDomains() {
        return hasAdditionalDomains;
    }

    public void setHasAdditionalDomains(boolean hasAdditionalDomains) {
        this.hasAdditionalDomains = hasAdditionalDomains;
    }

    protected void setRangeType(RangeType rangeType) {
        this.rangeType = rangeType;
    }

    protected void setSpatialDomain(SpatialDomain spatialDomain) {
        this.spatialDomain = spatialDomain;
    }

    protected void setTemporalDomain(TemporalDomain temporalDomain) {
        this.temporalDomain = temporalDomain;
    }

    protected void setVerticalDomain(VerticalDomain verticalDomain) {
        this.verticalDomain = verticalDomain;
    }

    public List<AdditionalDomain> getAdditionalDomains() {
        return additionalDomains;
    }

    public void setAdditionalDomains(List<AdditionalDomain> additionalDomains) {
        this.additionalDomains = additionalDomains;
    }

    public List<DimensionDescriptor> getDimensionDescriptors() throws IOException {
        return dimensionDescriptors;
    }

    public void setDimensionDescriptors(List<DimensionDescriptor> dimensionDescriptors) {
        this.dimensionDescriptors = dimensionDescriptors;
    }

    /** */
    public void dispose() {
        // default impl, do nothing

    }
}
