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
package org.geotools.coverage.io.impl;

import java.io.IOException;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.geotools.coverage.grid.io.DimensionDescriptor;
import org.geotools.coverage.io.CoverageCapabilities;
import org.geotools.coverage.io.CoverageSource;
import org.geotools.coverage.io.CoverageSourceDescriptor;
import org.geotools.coverage.io.RasterLayout;
import org.geotools.coverage.io.metadata.MetadataNode;
import org.geotools.coverage.io.range.RangeType;
import org.geotools.data.Parameter;
import org.geotools.data.ResourceInfo;
import org.opengis.feature.type.Name;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.util.ProgressListener;

/**
 * Default implementation of {@link CoverageSource}.
 *
 * @author Daniele Romagnoli, GeoSolutions SAS
 */
public abstract class DefaultCoverageSource implements CoverageSource {

    protected static final EnumSet<CoverageCapabilities> CAPABILITIES;

    static {
        CAPABILITIES =
                EnumSet.of(
                        CoverageCapabilities.READ_HORIZONTAL_DOMAIN_SUBSAMBLING,
                        CoverageCapabilities.READ_RANGE_SUBSETTING,
                        CoverageCapabilities.READ_REPROJECTION,
                        CoverageCapabilities.READ_SUBSAMPLING);
    }

    protected final Name name;

    protected final CoverageSourceDescriptor coverageDescriptor;

    private boolean disposed;

    /** */
    protected DefaultCoverageSource(Name name, CoverageSourceDescriptor descriptor) {
        this.name = name;
        this.coverageDescriptor = descriptor;
    }

    @Override
    public Map<String, Parameter<?>> getReadParameterInfo() {
        return null;
    }

    @Override
    public MetadataNode getMetadata(String metadataDomain, ProgressListener listener) {
        return null;
    }

    @Override
    public Set<Name> getMetadataDomains() {
        return null;
    }

    @Override
    public List<? extends RasterLayout> getOverviewsLayouts(ProgressListener listener)
            throws IOException {
        return Collections.emptyList();
    }

    @Override
    public int getOverviewsNumber(ProgressListener listener) throws IOException {
        return 0;
    }

    @Override
    public Name getName(ProgressListener listener) {
        ensureNotDisposed();
        return name;
    }

    protected void ensureNotDisposed() {
        if (disposed) {
            throw new IllegalStateException("Disposed");
        }
    }

    @Override
    public EnumSet<CoverageCapabilities> getCapabilities() {
        return EnumSet.copyOf(CAPABILITIES);
    }

    @Override
    public RangeType getRangeType(ProgressListener listener) throws IOException {
        ensureNotDisposed();
        return coverageDescriptor.getRangeType();
    }

    @Override
    public CoordinateReferenceSystem getCoordinateReferenceSystem() {
        return coverageDescriptor.getSpatialDomain().getCoordinateReferenceSystem2D();
    }

    @Override
    public SpatialDomain getSpatialDomain() throws IOException {
        return coverageDescriptor.getSpatialDomain();
    }

    @Override
    public TemporalDomain getTemporalDomain() throws IOException {
        return coverageDescriptor.getTemporalDomain();
    }

    @Override
    public VerticalDomain getVerticalDomain() throws IOException {
        return coverageDescriptor.getVerticalDomain();
    }

    @Override
    public List<AdditionalDomain> getAdditionalDomains() throws IOException {
        return coverageDescriptor.getAdditionalDomains();
    }

    @Override
    public List<DimensionDescriptor> getDimensionDescriptors() throws IOException {
        return coverageDescriptor.getDimensionDescriptors();
    }

    @Override
    public ResourceInfo getInfo(ProgressListener listener) {
        ensureNotDisposed();
        return null;
    }

    @Override
    public void dispose() {
        if (disposed) {
            return;
        }
        disposed = true;
        coverageDescriptor.dispose();
    }
}
