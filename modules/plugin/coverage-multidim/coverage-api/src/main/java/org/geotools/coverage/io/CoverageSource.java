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

import java.awt.Rectangle;
import java.io.IOException;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import org.geotools.coverage.grid.io.DimensionDescriptor;
import org.geotools.coverage.io.metadata.MetadataNode;
import org.geotools.coverage.io.range.RangeType;
import org.geotools.data.Parameter;
import org.geotools.data.ResourceInfo;
import org.geotools.referencing.CRS;
import org.geotools.util.DateRange;
import org.geotools.util.NumberRange;
import org.opengis.coverage.grid.GridEnvelope;
import org.opengis.feature.type.Name;
import org.opengis.geometry.BoundingBox;
import org.opengis.geometry.Envelope;
import org.opengis.geometry.TransfiniteSet;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.TemporalCRS;
import org.opengis.referencing.crs.VerticalCRS;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.util.ProgressListener;

/**
 * Allows read-only access to a Coverage.
 *
 * @author Simone Giannecchini, GeoSolutions
 * @author Jody Garnett
 */
public interface CoverageSource {

    enum DomainType {
        NUMBER,
        NUMBERRANGE,
        DATE,
        DATERANGE,
        STRING
    }

    public abstract class SpatialDomain {

        /**
         * The first {@link BoundingBox} of this {@link List} should contain the overall bounding
         * for the underlying coverage in its native coordinate reference system. However, by
         * setting the <code>global</code> param to <code>false</code> we can request additional
         * bounding boxes in case the area covered by the mentioned coverage is poorly approximated
         * by a single coverage, like it could happen for a mosaic which has some holes.
         *
         * @see {@link BoundingBox}
         */
        public abstract Set<? extends BoundingBox> getSpatialElements(
                final boolean overall, final ProgressListener listener) throws IOException;

        public abstract CoordinateReferenceSystem getCoordinateReferenceSystem2D();

        /**
         * Transformation between the 2D raster space and the 2D model space. In case the underlying
         * coverage is unrectified this transformation maybe a georeferencing transformation of
         * simply the identity in case we do not have means to georeference the mentioned coverage.
         */
        public abstract MathTransform2D getGridToWorldTransform(final ProgressListener listener)
                throws IOException;

        /**
         * The first {@link Rectangle} should describe the overall bidimensional raster range for
         * the underlying coverage. However, by setting the <code>
         * overall</code> param to true we can request additional raster ranges in case the area
         * covered by the mentioned coverage is poorly approximated by a single {@link Rectangle},
         * like it could happen for a mosaic which has some holes.
         *
         * @todo should we consider {@link GridEnvelope}?? or RasterLayout which also contains
         *     tiling information??? This has also an impact on the {@link
         *     #getOptimalDataBlockSizes()} method, which may become useless
         */
        public abstract Set<? extends RasterLayout> getRasterElements(
                final boolean overall, final ProgressListener listener) throws IOException;
    }

    public abstract class TemporalDomain {

        /**
         * Describes the temporal domain for the underlying {@link RasterDataset} by returning a
         * {@link Set} of {@link DateRange} elements for it. Note that the {@link TemporalCRS} for
         * the listed {@link DateRange} objects can be obtained from the overall {@link CRS} for the
         * underlying coverage.
         *
         * @return a {@link Set} of {@link DateRange}s elements.
         * @todo allow transfinite sets!
         */
        public abstract SortedSet<? extends DateRange> getTemporalElements(
                final boolean overall, final ProgressListener listener) throws IOException;

        public abstract CoordinateReferenceSystem getCoordinateReferenceSystem();
    }

    public abstract class VerticalDomain {

        /**
         * A {@link Set} of {@link Envelope} element for the underlying coverage. Note that the
         * {@link CRS} for such envelope can be <code>null</code> in case the overall spatial {@link
         * CRS} is a non-separable 3D {@link CRS} like WGS84-3D. Otherwise, all the envelopes should
         * share the same {@link VerticalCRS}. Finally, note that the envelope should be
         * 1-dimensional. In case of single vertical value, the lower coordinate should match the
         * upper coordinate while lower and upper coordinates may be different to define vertical
         * intervals.
         *
         * @todo consider {@link TransfiniteSet} as an alternative to {@link SortedSet}
         * @todo allow using an interval as well as a direct position
         * @todo allow transfinite sets!
         */
        public abstract SortedSet<? extends NumberRange<Double>> getVerticalElements(
                final boolean overall, final ProgressListener listener) throws IOException;

        public abstract CoordinateReferenceSystem getCoordinateReferenceSystem();
    }

    public abstract class AdditionalDomain {

        /**
         * Describes the additional domain for the underlying {@link RasterDataset} by returning a
         * {@link Set} of elements for it.
         *
         * @return a {@link Set} of {@link DateRange}s elements.
         * @todo allow transfinite sets!
         */
        public abstract Set<Object> getElements(
                final boolean overall, final ProgressListener listener) throws IOException;

        public abstract String getName();

        public abstract DomainType getType();
    }

    /**
     * Name of the Coverage (ie data product) provided by this CoverageSource.
     *
     * @since 2.5
     * @return Name of the Coverage (ie data product) provided.
     */
    Name getName(final ProgressListener listener);

    /**
     * Information describing the contents of this resource.
     *
     * <p>Please note that for FeatureContent:
     *
     * <ul>
     *   <li>name - unique with in the context of a Service
     *   <li>schema - used to identify the type of resource; usually the format or data product
     *       being represented
     *       <ul>
     *
     * @todo do we need this??
     */
    ResourceInfo getInfo(final ProgressListener listener);

    //
    // /**
    // * The first {@link Rectangle} should describe the overall bidimensional
    // * raster range for the underlying coverage. However, by setting the
    // * <code>overall</code> param to true we can request additional raster
    // * ranges in case the area covered by the mentioned coverage is poorly
    // * approximated by a single {@link Rectangle}, like it could happen for a
    // * mosaic which has some holes.
    // *
    // *
    // * @todo should we consider {@link GridEnvelope}?? or {@link ImageLayout} which also contains
    // tiling information???
    // */
    // public List<Rectangle> getRasterDomain(final boolean overall,
    // final ProgressListener listener) throws IOException;
    /**
     * Describes the required (and optional) parameters that can be passed to the {@link
     * #read(CoverageReadRequest, ProgressListener)} method.
     *
     * <p>
     *
     * @return Param a {@link Map} describing the {@link Map} for {@link #read(CoverageReadRequest,
     *     ProgressListener)}.
     */
    public Map<String, Parameter<?>> getReadParameterInfo();

    /**
     * Obtain a {@link CoverageResponse} from this {@link CoverageSource} given a specified {@link
     * DefaultCoverageRequest}.
     *
     * @param request the input {@link DefaultCoverageRequest}.
     */
    public CoverageResponse read(final CoverageReadRequest request, final ProgressListener listener)
            throws IOException;

    /**
     * Retrieves a {@link RangeType} instance which can be used to describe the codomain for the
     * underlying coverage.
     *
     * @return a {@link RangeType} instance which can be used to describe the codomain for the
     *     underlying coverage.
     * @throws IOException in case something bad occurs
     */
    public RangeType getRangeType(final ProgressListener listener) throws IOException;

    /**
     * Closes this {@link CoverageSource} and releases any lock or cached information it holds.
     *
     * <p>Once a {@link CoverageAccess} has been disposed it can be seen as being in unspecified
     * state, hence calling a method on it may have unpredictable results.
     */
    public void dispose();

    /**
     * Set of supported {@link CoverageCapabilities} which can be used to discover capabilities of a
     * certain {@link CoverageSource}.
     *
     * <p>You can use set membership to quickly test abilities:<code><pre>
     * if( getCapabilities().contains( CoverageCapabilities.READ_SUBSAMPLING ) ){
     *     ...
     * }
     * </code></pre>
     *
     * @return a {@link EnumSet} of CoverageCapabilities which can be used to discover capabilities
     *     of this {@link CoverageSource}.
     */
    public EnumSet<CoverageCapabilities> getCapabilities();

    public MetadataNode getMetadata(String metadataDomain, final ProgressListener listener);

    public Set<Name> getMetadataDomains();

    public CoordinateReferenceSystem getCoordinateReferenceSystem();

    public SpatialDomain getSpatialDomain() throws IOException;

    public TemporalDomain getTemporalDomain() throws IOException;

    public VerticalDomain getVerticalDomain() throws IOException;

    public List<AdditionalDomain> getAdditionalDomains() throws IOException;

    public List<? extends RasterLayout> getOverviewsLayouts(final ProgressListener listener)
            throws IOException;

    public int getOverviewsNumber(final ProgressListener listener) throws IOException;

    public List<DimensionDescriptor> getDimensionDescriptors() throws IOException;

    // /**
    // * @todo TBD, I am not even sure this should leave at the general interface level!
    // *
    // */
    // public Object getGCPManager(final ProgressListener listener)throws IOException;
    //
    // /**
    // * @todo TBD, I am not even sure this should leave at the general interface level!
    // *
    // */
    // public Object getStatisticsManager(final ProgressListener listener)throws IOException;
    //
    //
    //
    // /**
    // * @todo TBD, I am not even sure this should leave at the general interface level!
    // *
    // */
    // public Object getOverviewsManager(final ProgressListener listener)throws IOException;

}
