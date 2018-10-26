/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.coverage.grid.io.DimensionDescriptor;
import org.geotools.coverage.io.CoverageSource.AdditionalDomain;
import org.geotools.coverage.io.CoverageSource.SpatialDomain;
import org.geotools.coverage.io.CoverageSource.TemporalDomain;
import org.geotools.coverage.io.util.DateRangeTreeSet;
import org.geotools.feature.NameImpl;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultTemporalCRS;
import org.geotools.util.DateRange;
import org.opengis.feature.type.Name;
import org.opengis.geometry.BoundingBox;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.TemporalCRS;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.util.ProgressListener;

public class TestCoverageSourceDescriptor extends CoverageSourceDescriptor {

    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(TestCoverageSourceDescriptor.class);

    private static CoordinateReferenceSystem WGS84;

    static {
        try {
            WGS84 = CRS.decode("EPSG:4326", true);
        } catch (NoSuchAuthorityCodeException e) {
            LOGGER.log(Level.FINER, e.getMessage(), e);
        } catch (FactoryException e) {
            LOGGER.log(Level.FINER, e.getMessage(), e);
        }
    }

    static class TestSpatialDomain extends SpatialDomain {

        Set<RasterLayout> layout;

        public TestSpatialDomain() {
            layout = Collections.singleton(RasterLayoutTest.testRasterLayout);
        }

        @Override
        public Set<? extends BoundingBox> getSpatialElements(
                boolean overall, ProgressListener listener) throws IOException {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public CoordinateReferenceSystem getCoordinateReferenceSystem2D() {
            return WGS84;
        }

        @Override
        public MathTransform2D getGridToWorldTransform(ProgressListener listener)
                throws IOException {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Set<? extends RasterLayout> getRasterElements(
                boolean overall, ProgressListener listener) throws IOException {
            return layout;
        }
    }

    static class TestTemporalDomain extends TemporalDomain {

        TemporalCRS temporalCrs;

        Set<DateRange> dates;

        public TestTemporalDomain(TemporalCRS temporalCrs, Set<DateRange> set) {
            this.temporalCrs = temporalCrs;
            dates = set;
        }

        @Override
        public SortedSet<? extends DateRange> getTemporalElements(
                boolean overall, ProgressListener listener) throws IOException {
            return new DateRangeTreeSet(dates);
        }

        @Override
        public CoordinateReferenceSystem getCoordinateReferenceSystem() {
            return temporalCrs;
        }
    }

    private static SpatialDomain testSpatialDomain;

    private static TemporalDomain testTemporalDomain =
            new TestTemporalDomain(
                    DefaultTemporalCRS.JAVA,
                    Collections.singleton(new DateRange(new Date(10000), new Date(20000))));

    public static final String TEST_COVERAGE = "testCoverage";

    public static final Name TEST_NAME = new NameImpl(TEST_COVERAGE);

    public TestCoverageSourceDescriptor(String name) {
        setName(name);
        setVerticalDomain(null);
        setHasVerticalDomain(false);
        setHasTemporalDomain(true);
        List<DimensionDescriptor> dimensionDescriptors = Collections.emptyList();
        setDimensionDescriptors(dimensionDescriptors);
        List<AdditionalDomain> additionalDomains = Collections.emptyList();
        setAdditionalDomains(additionalDomains);
        setHasAdditionalDomains(false);
        testSpatialDomain = new TestSpatialDomain();
        setSpatialDomain(testSpatialDomain);
        setTemporalDomain(testTemporalDomain);
    }
}
