/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.imageio.netcdf;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BandedSampleModel;
import java.awt.image.SampleModel;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import javax.measure.Unit;
import javax.measure.format.ParserException;
import org.geotools.coverage.Category;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.DefaultDimensionDescriptor;
import org.geotools.coverage.grid.io.DimensionDescriptor;
import org.geotools.coverage.io.CoverageSource.*;
import org.geotools.coverage.io.CoverageSourceDescriptor;
import org.geotools.coverage.io.RasterLayout;
import org.geotools.coverage.io.catalog.CoverageSlice;
import org.geotools.coverage.io.catalog.CoverageSlicesCatalog;
import org.geotools.coverage.io.range.FieldType;
import org.geotools.coverage.io.range.RangeType;
import org.geotools.coverage.io.range.impl.DefaultFieldType;
import org.geotools.coverage.io.range.impl.DefaultRangeType;
import org.geotools.coverage.io.util.DateRangeComparator;
import org.geotools.coverage.io.util.DateRangeTreeSet;
import org.geotools.coverage.io.util.DoubleRangeTreeSet;
import org.geotools.coverage.io.util.NumberRangeComparator;
import org.geotools.coverage.util.CoverageUtilities;
import org.geotools.data.DataUtilities;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.feature.NameImpl;
import org.geotools.gce.imagemosaic.Utils;
import org.geotools.gce.imagemosaic.catalog.index.Indexer.Coverages.Coverage;
import org.geotools.gce.imagemosaic.catalog.index.SchemaType;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.imageio.netcdf.NetCDFGeoreferenceManager.DimensionMapper;
import org.geotools.imageio.netcdf.cv.CoordinateVariable;
import org.geotools.imageio.netcdf.utilities.NetCDFCRSUtilities;
import org.geotools.imageio.netcdf.utilities.NetCDFUtilities;
import org.geotools.referencing.operation.transform.ProjectiveTransform;
import org.geotools.util.DateRange;
import org.geotools.util.NumberRange;
import org.geotools.util.SimpleInternationalString;
import org.geotools.util.factory.GeoTools;
import org.geotools.util.logging.Logging;
import org.opengis.coverage.SampleDimension;
import org.opengis.coverage.grid.GridEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.Name;
import org.opengis.geometry.BoundingBox;
import org.opengis.metadata.spatial.PixelOrientation;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.TemporalCRS;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.util.InternationalString;
import org.opengis.util.ProgressListener;
import ucar.nc2.Dimension;
import ucar.nc2.constants.AxisType;
import ucar.nc2.dataset.CoordinateAxis;
import ucar.nc2.dataset.CoordinateAxis.AxisComparator;
import ucar.nc2.dataset.CoordinateSystem;
import ucar.nc2.dataset.VariableDS;

/**
 * @author Simone Giannecchini, GeoSolutions SAS
 * @todo lazy initialization
 * @todo management of data read with proper mangling
 */
public class VariableAdapter extends CoverageSourceDescriptor {

    private static final boolean QUICK_SCAN;
    private static final String QUICK_SCAN_KEY = "org.geotools.netcdf.quickscan";
    private static final AxisComparator AXIS_COMPARATOR = new CoordinateAxis.AxisComparator();
    public static final int Z = 0;
    public static final int T = 1;

    /**
     * Simple chars replacing classes to deal with "custom" chars. As an instance, to be compliant
     * with the javax.measure.unit.Unit parser, we should replace kg.m-2 to kg*m^-2 which means
     * replacing the "." sign with the "*" and the "-" sign with "^_".
     */
    static class UnitCharReplacement {
        String from;
        String to;

        public UnitCharReplacement(String from, String to) {
            this.from = from;
            this.to = to;
        }

        String replace(String input) {
            if (input != null && input.contains(from)) {
                return input.replace(from, to);
            }
            return input;
        }
    }

    // TODO: support for more remappings through external configs
    static final Set<UnitCharReplacement> UNIT_CHARS_REPLACEMENTS;

    static {
        QUICK_SCAN = Boolean.getBoolean(QUICK_SCAN_KEY);
        UNIT_CHARS_REPLACEMENTS = new HashSet<UnitCharReplacement>();
        UNIT_CHARS_REPLACEMENTS.add(new UnitCharReplacement("-", "^-"));
        UNIT_CHARS_REPLACEMENTS.add(new UnitCharReplacement(".", "*"));
        UNIT_CHARS_REPLACEMENTS.add(new UnitCharReplacement("1/s", "s^-1"));
    }

    public class UnidataSpatialDomain extends SpatialDomain {

        /** The spatial coordinate reference system */
        private CoordinateReferenceSystem coordinateReferenceSystem;

        /** The spatial referenced envelope */
        private ReferencedEnvelope referencedEnvelope;

        /** The gridGeometry of the spatial domain */
        private GridGeometry2D gridGeometry;

        public ReferencedEnvelope getReferencedEnvelope() {
            return referencedEnvelope;
        }

        public void setReferencedEnvelope(ReferencedEnvelope referencedEnvelope) {
            this.referencedEnvelope = referencedEnvelope;
        }

        public GridGeometry2D getGridGeometry() {
            return gridGeometry;
        }

        public double[] getFullResolution() {
            AffineTransform gridToCRS = (AffineTransform) gridGeometry.getGridToCRS();
            return CoverageUtilities.getResolution(gridToCRS);
        }

        public void setGridGeometry(GridGeometry2D gridGeometry) {
            this.gridGeometry = gridGeometry;
        }

        public void setCoordinateReferenceSystem(
                CoordinateReferenceSystem coordinateReferenceSystem) {
            this.coordinateReferenceSystem = coordinateReferenceSystem;
        }

        @Override
        public Set<? extends BoundingBox> getSpatialElements(
                boolean overall, ProgressListener listener) throws IOException {
            return Collections.singleton(referencedEnvelope);
        }

        @Override
        public CoordinateReferenceSystem getCoordinateReferenceSystem2D() {
            return coordinateReferenceSystem;
        }

        @Override
        public MathTransform2D getGridToWorldTransform(ProgressListener listener)
                throws IOException {
            return gridGeometry.getGridToCRS2D(PixelOrientation.CENTER);
        }

        @Override
        public Set<? extends RasterLayout> getRasterElements(
                boolean overall, ProgressListener listener) throws IOException {
            Rectangle bounds = gridGeometry.getGridRange2D().getBounds();
            return Collections.singleton(new RasterLayout(bounds));
        }
    }

    public class UnidataTemporalDomain extends TemporalDomain {

        /** @param adaptee */
        UnidataTemporalDomain(CoordinateVariable<?> adaptee) {
            if (!Date.class.isAssignableFrom(adaptee.getType())) {
                throw new IllegalArgumentException(
                        "Unable to wrap non temporal CoordinateVariable:" + adaptee.toString());
            }
            this.adaptee = (CoordinateVariable<Date>) adaptee;
        }

        final CoordinateVariable<Date> adaptee;

        public SortedSet<DateRange> getTemporalExtent() {
            // Getting global Extent
            Date startTime;
            try {
                startTime = adaptee.getMinimum();
                Date endTime = adaptee.getMaximum();
                final DateRange global = new DateRange(startTime, endTime);
                final SortedSet<DateRange> globalTemporalExtent = new DateRangeTreeSet();
                globalTemporalExtent.add(global);
                return globalTemporalExtent;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public SortedSet<? extends DateRange> getTemporalElements(
                boolean overall, ProgressListener listener) throws IOException {
            if (overall) {

                // Getting overall Extent
                final SortedSet<DateRange> extent =
                        new TreeSet<DateRange>(new DateRangeComparator());
                for (Date dd : adaptee.read()) {
                    extent.add(new DateRange(dd, dd));
                }
                return extent;
            } else {
                return getTemporalExtent();
            }
        }

        @Override
        public CoordinateReferenceSystem getCoordinateReferenceSystem() {
            return adaptee.getCoordinateReferenceSystem();
        }
    }

    public class UnidataVerticalDomain extends VerticalDomain {

        final CoordinateVariable<? extends Number> adaptee;

        /** @param cv */
        UnidataVerticalDomain(CoordinateVariable<?> cv) {
            if (!Number.class.isAssignableFrom(cv.getType())) {
                throw new IllegalArgumentException(
                        "Unable to wrap a non Number CoordinateVariable:" + cv.toString());
            }
            this.adaptee = (CoordinateVariable<? extends Number>) cv;
        }

        public SortedSet<NumberRange<Double>> getVerticalExtent() {
            // Getting global Extent
            final CoordinateVariable<? extends Number> verticalDimension = this.adaptee;
            NumberRange<Double> global;
            try {
                global =
                        NumberRange.create(
                                verticalDimension.getMinimum().doubleValue(),
                                verticalDimension.getMaximum().doubleValue());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            final SortedSet<NumberRange<Double>> globalVerticalExtent = new DoubleRangeTreeSet();
            globalVerticalExtent.add(global);
            return globalVerticalExtent;
        }

        @Override
        public SortedSet<? extends NumberRange<Double>> getVerticalElements(
                boolean overall, ProgressListener listener) throws IOException {

            if (overall) {
                // Getting overall Extent
                final SortedSet<NumberRange<Double>> extent =
                        new TreeSet<NumberRange<Double>>(new NumberRangeComparator());
                for (Number vv : adaptee.read()) {
                    final double doubleValue = vv.doubleValue();
                    extent.add(NumberRange.create(doubleValue, doubleValue));
                }
                return extent;
            } else {
                return getVerticalExtent();
            }
        }

        @Override
        public CoordinateReferenceSystem getCoordinateReferenceSystem() {
            return adaptee.getCoordinateReferenceSystem();
        }
    }

    /** TODO improve support for this */
    public class UnidataAdditionalDomain extends AdditionalDomain {

        /** The detailed domain extent */
        private final Set<Object> domainExtent = new TreeSet<Object>();

        /** The merged domain extent */
        private final Set<Object> globalDomainExtent =
                new TreeSet<Object>(
                        new Comparator<Object>() {
                            private NumberRangeComparator numberRangeComparator =
                                    new NumberRangeComparator();
                            private DateRangeComparator dateRangeComparator =
                                    new DateRangeComparator();

                            public int compare(Object o1, Object o2) {
                                // assume that o1 and o2 are both not null
                                boolean o1IsDateRange = true;
                                boolean o2IsDateRange = true;

                                if (o1 instanceof NumberRange) {
                                    o1IsDateRange = false;
                                } else if (!(o1 instanceof DateRange)) {
                                    throw new ClassCastException(
                                            o1.getClass() + " is not an known range type");
                                }

                                if (o2 instanceof NumberRange) {
                                    o2IsDateRange = false;
                                } else if (!(o2 instanceof DateRange)) {
                                    throw new ClassCastException(
                                            o2.getClass() + " is not an known range type");
                                }

                                if (o1IsDateRange && o2IsDateRange) {
                                    return dateRangeComparator.compare(
                                            (DateRange) o1, (DateRange) o2);
                                } else if (!o1IsDateRange && !o2IsDateRange) {
                                    return numberRangeComparator.compare(
                                            (NumberRange<?>) o1, (NumberRange<?>) o2);
                                }

                                throw new ClassCastException(
                                        "Incompatible range types: "
                                                + o1.getClass()
                                                + " is not the same as "
                                                + o2.getClass());
                            }
                        });

        /** The domain name */
        private final String name;

        private final DomainType type;

        final CoordinateVariable<?> adaptee;

        /** @param adaptee TODO missing support for Range TODO missing support for String domains */
        UnidataAdditionalDomain(CoordinateVariable<?> adaptee) throws IOException {
            this.adaptee = adaptee;
            name = adaptee.getName();

            // type
            Class<?> type = adaptee.getType();
            if (Date.class.isAssignableFrom(type)) {
                this.type = DomainType.DATE;

                // global domain
                globalDomainExtent.add(
                        new DateRange((Date) adaptee.getMinimum(), (Date) adaptee.getMaximum()));
            } else if (Number.class.isAssignableFrom(type)) {
                this.type = DomainType.NUMBER;

                // global domain
                globalDomainExtent.add(
                        new NumberRange<Double>(
                                Double.class,
                                ((Number) adaptee.getMinimum()).doubleValue(),
                                ((Number) adaptee.getMaximum()).doubleValue()));
            } else {
                throw new UnsupportedOperationException(
                        "Unsupported CoordinateVariable:" + adaptee.toString());
            }

            // domain
            domainExtent.addAll(adaptee.read());
        }

        @Override
        public Set<Object> getElements(boolean overall, ProgressListener listener)
                throws IOException {
            if (overall) {
                return globalDomainExtent;
            } else {
                return domainExtent;
            }
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public DomainType getType() {
            return type;
        }

        public Set<Object> getDomainExtent() {
            return domainExtent;
        }
    }

    final VariableDS variableDS;

    /**
     * Following COARDS or CF Convention, custom dimensions are always at the beginning (lower
     * indexes)
     */
    Set<String> ignoredDimensions = new HashSet<String>();

    private ucar.nc2.dataset.CoordinateSystem coordinateSystem;

    private NetCDFImageReader reader;

    private int numBands;

    private SampleModel sampleModel;

    private int numberOfSlices;

    private int width;

    private int height;

    private CoordinateReferenceSystem coordinateReferenceSystem;

    private Name coverageName;

    private int[] nDimensionIndex;

    private static final java.util.logging.Logger LOGGER = Logging.getLogger(VariableAdapter.class);

    /**
     * Usual schema are the_geom, imageIndex, so the first attribute (time or elevation) will have
     * index = 2
     */
    private static final int FIRST_ATTRIBUTE_INDEX = 2;

    /** Extracts the compound {@link CoordinateReferenceSystem} from the unidata variable. */
    private void init() throws Exception {

        // initialize the various domains
        initSpatialElements();

        // initialize rank and number of 2D slices
        initRange();

        // initialize info about slice
        initSlicesInfo();
    }

    public int getRank() {
        return variableDS.getRank() - ignoredDimensions.size();
    }

    /** @throws Exception */
    private void initSlicesInfo() throws Exception {
        int[] shape = variableDS.getShape();
        numberOfSlices = 1;
        for (int i = 0; i < variableDS.getShape().length - 2; i++) {
            if (!ignoredDimensions.contains(variableDS.getDimension(i).getFullName())) {
                numberOfSlices *= shape[i];
            }
        }
    }

    /** @throws IOException */
    private void initSpatialElements() throws Exception {

        final List<DimensionDescriptor> dimensions = new ArrayList<DimensionDescriptor>();
        initCRS(dimensions);

        // SPATIAL DIMENSIONS
        initSpatialDomain();

        setDimensionDescriptors(dimensions);
        if (reader.ancillaryFileManager.isImposedSchema()) {
            updateDimensions(getDimensionDescriptors());
        }
    }

    /**
     * Update the dimensions to attributes mapping for this variable if needed. Default behaviour is
     * to get attributes from the name of the dimensions of the variable. In case the indexer.xml
     * contains an explicit schema with different attributes for time and elevation we need to remap
     * them and updates the dimensions mapping as well as the DimensionsDescriptors
     */
    private void updateDimensions(List<DimensionDescriptor> dimensionDescriptors)
            throws IOException {
        final Map<Name, String> mapping = reader.ancillaryFileManager.variablesMap;
        final Set<Name> keys = mapping.keySet();
        final String varName = getName();
        for (Name key : keys) {

            // Go to the current variable
            final String origName = mapping.get(key);
            if (origName.equalsIgnoreCase(varName)) {

                // Get the mapped coverage name (as an instance, NO2 for a GOME2 with var = 'z')
                final String coverageName = key.getLocalPart();
                final Coverage coverage =
                        reader.ancillaryFileManager.coveragesMapping.get(coverageName);
                final SchemaType schema = coverage.getSchema();
                if (schema != null) {
                    // look up the name
                    String schName = schema.getName();
                    final CoverageSlicesCatalog catalog = reader.getCatalog();
                    if (catalog != null) {
                        // Current assumption is that we have a typeName for each coverage but we
                        // should keep on working
                        // with shared schemas
                        // try with coveragename
                        SimpleFeatureType schemaType = null;
                        try {
                            if (schName != null) {
                                schemaType = catalog.getSchema(schName);
                            }
                        } catch (IOException e) {
                            // ok, we did not use the schema name, let's use the coverage name
                            schemaType = catalog.getSchema(coverageName);
                        }
                        if (schemaType != null) {
                            // Schema found: proceed with remapping attributes
                            updateMapping(schemaType, dimensionDescriptors);
                            break;
                        }
                        throw new IllegalStateException(
                                "Unable to find the table for this coverage: " + coverageName);
                    }
                }
                break;
            }
        }
    }

    /**
     * Update the dimensionDescriptor attributes mapping by checking the actual attribute names from
     * the schema
     */
    public void updateMapping(SimpleFeatureType indexSchema, List<DimensionDescriptor> descriptors)
            throws IOException {
        DimensionMapper mapper = reader.georeferencing.getDimensionMapper();
        Set<String> dimensionNames = mapper.getDimensionNames();
        // No need to do the mapping update in case one of these conditions apply
        if (dimensionNames == null
                || dimensionNames.isEmpty()
                || descriptors == null
                || descriptors.isEmpty()
                || indexSchema.getAttributeCount() <= FIRST_ATTRIBUTE_INDEX) {
            return;
        }
        int indexAttribute = FIRST_ATTRIBUTE_INDEX;
        final AttributeDescriptor attributeDescriptor = indexSchema.getDescriptor(indexAttribute);
        final String updatedAttribute = attributeDescriptor.getLocalName();
        if ("location".equalsIgnoreCase(updatedAttribute)) {
            // Skip location attribute
            indexAttribute++;
        }

        // Remap time
        String currentDimName = NetCDFUtilities.TIME_DIM;
        if (dimensionNames.contains(currentDimName)) {
            if (remapAttribute(indexSchema, currentDimName, indexAttribute, descriptors, mapper)) {
                indexAttribute++;
            }
        }

        // Remap elevation
        currentDimName = NetCDFUtilities.ELEVATION_DIM;
        if (dimensionNames.contains(currentDimName)) {
            if (remapAttribute(indexSchema, currentDimName, indexAttribute, descriptors, mapper)) {
                indexAttribute++;
            }
        }

        // Remap additional domains
        if (getAdditionalDomains() != null) {
            for (AdditionalDomain dom : getAdditionalDomains()) {
                currentDimName = dom.getName();
                if (remapAttribute(
                        indexSchema, currentDimName, indexAttribute, descriptors, mapper)) {
                    indexAttribute++;
                }
            }
        }
    }

    /**
     * Remap an attribute for a specified dimension. Get it from the schemaType and update both the
     * related dimension Descriptor as well as the dimensions mapping.
     */
    private boolean remapAttribute(
            final SimpleFeatureType indexSchema,
            final String currentDimName,
            final int indexAttribute,
            final List<DimensionDescriptor> descriptors,
            DimensionMapper mapper) {
        final int numAttributes = indexSchema.getAttributeCount();
        if (numAttributes <= indexAttribute) {
            // Stop looking for attributes in case there aren't anymore
            return false;
        }

        // Get the attribute descriptor for that index
        final AttributeDescriptor attributeDescriptor = indexSchema.getDescriptor(indexAttribute);

        // Loop over dimensionDescriptors
        for (DimensionDescriptor descriptor : descriptors) {
            // Find the descriptor related to the current dimension
            if (descriptor.getName().toUpperCase().equalsIgnoreCase(currentDimName)) {
                final String updatedAttribute = attributeDescriptor.getLocalName();
                if (!updatedAttribute.equals(
                        ((DefaultDimensionDescriptor) descriptor).getStartAttribute())) {
                    // Remap attributes in case the schema's attribute doesn't match the current
                    // attribute
                    ((DefaultDimensionDescriptor) descriptor).setStartAttribute(updatedAttribute);

                    // Update the dimensions mapping too
                    mapper.remap(currentDimName, updatedAttribute);
                }
                // the attribute has been found, prepare for the next one
                return true;
            }
        }
        return false;
    }

    /** */
    private void initCRS(List<DimensionDescriptor> dimensions)
            throws IllegalArgumentException, RuntimeException, IOException, IllegalStateException {
        // from UnidataVariableAdapter
        this.coordinateSystem = NetCDFCRSUtilities.getCoordinateSystem(variableDS);
        if (coordinateSystem == null) {
            throw new IllegalArgumentException("Provided CoordinateSystem is null");
        }
        // Wrapper for the CoordinateSystem
        coordinateSystem = new CoordinateSystemAdapter(coordinateSystem);

        // init nDimensionIndex
        List<Integer> nDimensionIndexList = new ArrayList<Integer>(2);
        nDimensionIndexList.add(-1);
        nDimensionIndexList.add(-1);

        /*
         * Adds the axis in reverse order, because the NetCDF image reader put the last dimensions in the rendered image. Typical NetCDF convention is
         * to put axis in the (time, depth, latitude, longitude) order, which typically maps to (longitude, latitude, depth, time) order in GeoTools
         * referencing framework.
         */
        int index = -1;
        List<CoordinateAxis> axesSorted = new ArrayList<>(coordinateSystem.getCoordinateAxes());
        Collections.sort(axesSorted, AXIS_COMPARATOR);
        for (CoordinateAxis axis : axesSorted) {
            index++;
            String fullName = axis.getFullName();
            if (NetCDFUtilities.getIgnoredDimensions().contains(fullName)) {
                ignoredDimensions.add(fullName);
                continue;
            }
            CoordinateVariable<?> cv =
                    reader.georeferencing.getCoordinateVariable(axis.getShortName());
            if (cv == null) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine("Unable to find a coordinate variable for " + fullName);
                }
                index--;
                continue;
            }
            switch (cv.getAxisType()) {
                case Time:
                    initTemporalDomain(cv, dimensions);
                    nDimensionIndexList.set(T, index);
                    continue;
                case GeoZ:
                case Height:
                case Pressure:
                    String axisName = cv.getName();
                    if (NetCDFCRSUtilities.VERTICAL_AXIS_NAMES.contains(axisName)) {
                        initVerticalDomain(cv, dimensions);
                        nDimensionIndexList.set(Z, index);
                    } else {
                        initAdditionalDomain(cv, dimensions);
                        nDimensionIndexList.add(index);
                    }
                    continue;
                case GeoX:
                case GeoY:
                case Lat:
                case Lon:
                    // do nothing
                    continue;
                default:
                    initAdditionalDomain(cv, dimensions);
                    nDimensionIndexList.add(index);
            }
        }
        nDimensionIndex = nDimensionIndexList.stream().mapToInt(i -> i).toArray();

        // ////
        // Creating the CoordinateReferenceSystem
        // ////
        ReferencedEnvelope bbox = reader.georeferencing.getBoundingBox(variableDS.getShortName());
        coordinateReferenceSystem = bbox.getCoordinateReferenceSystem();
    }

    /** */
    private void initVerticalDomain(CoordinateVariable<?> cv, List<DimensionDescriptor> dimensions)
            throws IOException {
        this.setHasVerticalDomain(true);
        final UnidataVerticalDomain verticalDomain = new UnidataVerticalDomain(cv);
        this.setVerticalDomain(verticalDomain);
        // TODO: Map ZAxis unit to UCUM UNIT (depending on type... elevation, level, pressure, ...)
        dimensions.add(
                new DefaultDimensionDescriptor(
                        Utils.ELEVATION_DOMAIN,
                        cv.getUnit(),
                        CoverageUtilities.UCUM.ELEVATION_UNITS.getSymbol(),
                        cv.getName(),
                        null));
    }

    /** */
    private void initTemporalDomain(CoordinateVariable<?> cv, List<DimensionDescriptor> dimensions)
            throws IOException {
        if (!cv.getType().equals(Date.class)) {
            throw new IllegalArgumentException(
                    "Unable to init temporal domain from CoordinateVariable that does not bind to Date");
        }
        if (!(cv.getCoordinateReferenceSystem() instanceof TemporalCRS)) {
            throw new IllegalArgumentException(
                    "Unable to init temporal domain from CoordinateVariable that does not have a TemporalCRS");
        }
        this.setHasTemporalDomain(true);
        final UnidataTemporalDomain temporalDomain = new UnidataTemporalDomain(cv);
        this.setTemporalDomain(temporalDomain);

        String timeAttribute = reader.uniqueTimeAttribute ? NetCDFUtilities.TIME : cv.getName();
        dimensions.add(
                new DefaultDimensionDescriptor(
                        Utils.TIME_DOMAIN,
                        CoverageUtilities.UCUM.TIME_UNITS.getName(),
                        CoverageUtilities.UCUM.TIME_UNITS.getSymbol(),
                        timeAttribute,
                        null));
    }

    /** */
    private void initAdditionalDomain(
            CoordinateVariable<?> cv, List<DimensionDescriptor> dimensions) throws IOException {
        UnidataAdditionalDomain domain;
        try {
            domain = new UnidataAdditionalDomain(cv);
            if (getAdditionalDomains() == null) {
                setAdditionalDomains(new ArrayList<AdditionalDomain>());
            }
            getAdditionalDomains().add(domain);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, e.getMessage(), e);
            return;
        }
        if (cv.getType().equals(Date.class)) {
            if (!(cv.getCoordinateReferenceSystem() instanceof TemporalCRS)) {
                throw new IllegalArgumentException(
                        "Unable to init temporal domain from CoordinateVariable that does not have a TemporalCRS");
            }
            dimensions.add(
                    new DefaultDimensionDescriptor(
                            cv.getName(),
                            CoverageUtilities.UCUM.TIME_UNITS.getName(),
                            CoverageUtilities.UCUM.TIME_UNITS.getSymbol(),
                            cv.getName(),
                            null));
        } else if (Number.class.isAssignableFrom(cv.getType())) {
            // TODO: Parse Units from axis and map them to UCUM units
            dimensions.add(
                    new DefaultDimensionDescriptor(
                            cv.getName(), cv.getUnit(), cv.getUnit(), cv.getName(), null));
        } else {
            throw new IllegalArgumentException(
                    "Unable to init domain from CoordinateVariable of type: "
                            + cv.getType().getName());
        }
        this.setHasAdditionalDomains(true);
    }

    /** */
    private void initSpatialDomain() throws Exception {
        // SPATIAL DOMAIN
        final UnidataSpatialDomain spatialDomain = new UnidataSpatialDomain();
        this.setSpatialDomain(spatialDomain);

        ReferencedEnvelope bbox = reader.georeferencing.getBoundingBox(variableDS.getShortName());
        spatialDomain.setCoordinateReferenceSystem(coordinateReferenceSystem);
        spatialDomain.setReferencedEnvelope(bbox);
        spatialDomain.setGridGeometry(getGridGeometry());
    }

    /** */
    private void initRange() {

        width =
                variableDS
                        .getDimension(variableDS.getRank() - NetCDFUtilities.X_DIMENSION)
                        .getLength();
        height =
                variableDS
                        .getDimension(variableDS.getRank() - NetCDFUtilities.Y_DIMENSION)
                        .getLength();

        // computing the number of bands, according to COARDS convention ignored dimension are at
        // the beginning
        String candidateDimension = variableDS.getDimensions().get(0).getFullName();
        MultipleBandsDimensionInfo multipleBands =
                reader.ancillaryFileManager.getMultipleBandsDimensionInfo(candidateDimension);
        if (multipleBands != null) {
            // multiple bands are defined for the ignored dimension
            numBands = multipleBands.getNumberOfBands();
        } else {
            numBands = variableDS.getRank() > 2 ? variableDS.getDimension(2).getLength() : 1;
        }

        // let's check if we are in the context of an image mosaic request
        if (reader.getImageMosaicRequest() != null) {
            // if specific bands were selected we need to adapt the number of bands
            int[] selectedBands = reader.getImageMosaicRequest().getBands();
            numBands = selectedBands == null ? numBands : selectedBands.length;
        }

        final int bufferType = NetCDFUtilities.getRawDataType(variableDS);
        sampleModel =
                new BandedSampleModel(
                        bufferType, width, height, multipleBands == null ? 1 : numBands);
        final Number noData = NetCDFUtilities.getNodata(variableDS);
        List<Category> catArray = new ArrayList<Category>();
        Category noDataCategory = null;
        Category dataCategory = null;
        Category[] categories = null;
        if (noData != null) {
            NumberRange noDataRange =
                    NumberRange.create(noData.doubleValue(), true, noData.doubleValue(), true);
            noDataCategory =
                    new Category(
                            Category.NODATA.getName(),
                            new Color[] {new Color(0, 0, 0, 0)},
                            noDataRange);
            catArray.add(noDataCategory);
        }
        NumberRange validRange = NetCDFUtilities.getRange(variableDS);
        if (validRange != null) {
            dataCategory = new Category("RANGE", (Color) null, validRange);
            catArray.add(dataCategory);
        }
        categories = new Category[catArray.size()];
        categories = catArray.toArray(categories);

        // range type
        String description = variableDS.getDescription();
        if (description == null) {
            description = variableDS.getShortName();
        }
        final Set<SampleDimension> sampleDims = new HashSet<SampleDimension>();

        // Parsing the unit of measure of this variable
        Unit unit = null;
        String unitString = variableDS.getUnitsString();
        if (unitString != null) {
            try {
                unit = NetCDFUnitFormat.parse(unitString);
            } catch (ParserException parseException) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine(
                            "Unable to parse the unit:"
                                    + unitString
                                    + "\nNo unit will be assigned");
                }
            }
        }

        if (multipleBands == null) {
            // single band dimension, so we only need one sample dimension
            sampleDims.add(new GridSampleDimension(description, categories, unit));
        } else {
            for (String bandName : multipleBands.getBandsNamesInOrder()) {
                // multiple bands for this dimension, only the bands names is different
                sampleDims.add(new GridSampleDimension(bandName, categories, unit));
            }
        }

        InternationalString desc = null;
        if (description != null && !description.isEmpty()) {
            desc = new SimpleInternationalString(description);
        }
        final FieldType fieldType = new DefaultFieldType(new NameImpl(getName()), desc, sampleDims);
        final RangeType range = new DefaultRangeType(getName(), description, fieldType);
        this.setRangeType(range);
    }

    /**
     * Extracts the {@link GridGeometry2D grid geometry} from the unidata variable.
     *
     * @return the {@link GridGeometry2D}.
     */
    protected GridGeometry2D getGridGeometry() throws IOException {
        int[] low = new int[2];
        int[] high = new int[2];
        double[] origin = new double[2];
        double scaleX = Double.POSITIVE_INFINITY, scaleY = Double.POSITIVE_INFINITY;

        for (CoordinateVariable<?> cv :
                reader.georeferencing.getCoordinatesVariables(variableDS.getShortName())) {
            if (!cv.isNumeric()) {
                continue;
            }
            final AxisType axisType = cv.getAxisType();
            switch (axisType) {
                case Lon:
                case GeoX:
                    // raster space
                    low[0] = 0;
                    high[0] = (int) cv.getSize();

                    // model space
                    if (cv.isRegular()) {
                        // regular model space
                        origin[0] = cv.getStart();
                        scaleX = cv.getIncrement();
                    } else {

                        // model space is not declared to be regular, but we kind of assume it is!!!
                        @SuppressWarnings("unchecked")
                        List<Number> vals = (List<Number>) cv.read();
                        double min = ((Number) cv.getMinimum()).doubleValue();
                        double max = ((Number) cv.getMaximum()).doubleValue();
                        // make sure we skip nodata coords, bah...
                        if (!Double.isNaN(min) && !Double.isNaN(max)) {
                            origin[0] = min;
                            scaleX = (max - min) / vals.size();
                        } else {
                            if (LOGGER.isLoggable(Level.FINE)) {
                                LOGGER.log(
                                        Level.FINE,
                                        "Axis values contains NaN; finding first valid values");
                            }
                            for (int j = 0; j < vals.size(); j++) {
                                double v = ((Number) vals.get(j)).doubleValue();
                                if (!Double.isNaN(v)) {
                                    for (int k = vals.size(); k > j; k--) {
                                        double vv = ((Number) vals.get(k)).doubleValue();
                                        if (!Double.isNaN(vv)) {
                                            origin[0] = v;
                                            scaleX = (vv - v) / vals.size();
                                        }
                                    }
                                }
                            }
                        }
                    }
                    break;
                case Lat:
                case GeoY:
                    // raster space
                    low[1] = 0;
                    high[1] = (int) cv.getSize();

                    // model space
                    if (cv.isRegular()) {

                        if (cv.getIncrement() > 0) {
                            // the latitude axis is increasing! This is a special case so we flip it
                            // around
                            scaleY = -cv.getIncrement();
                            origin[1] = cv.getStart() - scaleY * (high[1] - 1);
                        } else {
                            scaleY = cv.getIncrement();
                            origin[1] = cv.getStart();
                        }
                    } else {
                        // model space is not declared to be regular, but we kind of assume it is!!!
                        @SuppressWarnings("unchecked")
                        List<Number> values = (List<Number>) cv.read();
                        double min = ((Number) cv.getMinimum()).doubleValue();
                        double max = ((Number) cv.getMaximum()).doubleValue();
                        // make sure we skip nodata coords, bah...
                        if (!Double.isNaN(min) && !Double.isNaN(max)) {
                            scaleY = -(max - min) / values.size();
                            origin[1] = max;
                        } else {
                            if (LOGGER.isLoggable(Level.FINE)) {
                                LOGGER.log(
                                        Level.FINE,
                                        "Axis values contains NaN; finding first valid values");
                            }
                            for (int j = 0; j < values.size(); j++) {
                                double v = ((Number) values.get(j)).doubleValue();
                                if (!Double.isNaN(v)) {
                                    for (int k = values.size(); k > j; k--) {
                                        double vv = ((Number) values.get(k)).doubleValue();
                                        if (!Double.isNaN(vv)) {
                                            origin[1] = v;
                                            scaleY = -(vv - v) / values.size();
                                        }
                                    }
                                }
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
        }

        final AffineTransform at = new AffineTransform(scaleX, 0, 0, scaleY, origin[0], origin[1]);
        final GridEnvelope gridRange =
                new GridEnvelope2D(low[0], low[1], high[0] - low[0], high[1] - low[1]);
        final MathTransform raster2Model = ProjectiveTransform.create(at);
        return new GridGeometry2D(
                gridRange,
                PixelInCell.CELL_CENTER,
                raster2Model,
                coordinateReferenceSystem,
                GeoTools.getDefaultHints());
    }

    public int getNumBands() {
        return numBands;
    }

    public SampleModel getSampleModel() {
        return sampleModel;
    }

    public VariableAdapter(NetCDFImageReader reader, Name coverageName, VariableDS variable)
            throws Exception {
        this.variableDS = variable;
        this.reader = reader;
        this.coverageName = coverageName;
        setName(variable.getFullName());
        init();
    }

    @Override
    public UnidataSpatialDomain getSpatialDomain() {
        return (UnidataSpatialDomain) super.getSpatialDomain();
    }

    @Override
    public UnidataTemporalDomain getTemporalDomain() {
        return (UnidataTemporalDomain) super.getTemporalDomain();
    }

    @Override
    public UnidataVerticalDomain getVerticalDomain() {
        return (UnidataVerticalDomain) super.getVerticalDomain();
    }

    /** @return */
    public int getWidth() {
        return width;
    }

    /** @return */
    public int getHeight() {
        return height;
    }

    /**
     * Utility method to retrieve the index of a specific dimension index, for constructing the
     * range
     *
     * @param n the dimension to retrieve the index for (0=Z, 1=T, 2+=Additional)
     * @return the index
     */
    public int getNDimensionIndex(int n) {
        return nDimensionIndex[n];
    }

    /**
     * Utility method to retrieve the n-index of a Variable coverageDescriptor stored on {@link
     * NetCDFImageReader} NetCDF Flat Reader {@link HashMap} indexMap.
     *
     * @param n {@link int} the dimension
     * @param imageIndex {@link int} the image index
     * @return n-index {@link int} -1 if not present
     */
    public int getNIndex(int n, int imageIndex) {
        if (n < nDimensionIndex.length && nDimensionIndex[n] >= 0) {
            int factor = 1;
            for (int i = 0; i < n; i++) {
                if (nDimensionIndex[i] >= 0) {
                    factor *= NetCDFUtilities.getDimensionLength(variableDS, nDimensionIndex[i]);
                }
            }
            return imageIndex
                    % (NetCDFUtilities.getDimensionLength(variableDS, nDimensionIndex[n]) * factor)
                    / factor;
        }
        return -1;
    }

    /**
     * Utility method to split the index of a Variable coverageDescriptor stored on {@link
     * NetCDFImageReader} NetCDF Flat Reader {@link HashMap} indexMap into dimensions.
     *
     * @param imageIndex {@link int} the index
     * @return splitted index
     */
    public int[] splitIndex(int imageIndex) {
        int[] resultIndex = new int[nDimensionIndex.length];
        for (int n = 0; n < nDimensionIndex.length; n++) {
            resultIndex[n] = getNIndex(n, imageIndex);
        }
        return resultIndex;
    }

    public Map<String, Integer> mapIndex(int[] splittedIndex) {
        Map<String, Integer> resultIndex = new HashMap<String, Integer>();
        for (int n = 0; n < splittedIndex.length; n++) {
            if (nDimensionIndex[n] != -1) {
                resultIndex.put(
                        variableDS.getDimension(nDimensionIndex[n]).getFullName(),
                        splittedIndex[n]);
            }
        }
        return resultIndex;
    }

    /** @return the numberOfSlices */
    public int getNumberOfSlices() {
        return QUICK_SCAN ? 1 : numberOfSlices;
    }

    /** @return the shape */
    public int[] getShape() {
        return variableDS.getShape();
    }

    /**
     * Return features for that variable adapter, starting from slices with index = "startIndex",
     * and up to "limit" elements. This allows for paging. Put the created features inside the
     * provided collection
     *
     * @param startIndex the first slice to be returned
     * @param limit the max number of features to be created
     * @param collection the feature collection where features need to be stored
     */
    public int getFeatures(
            final int startIndex, final int limit, final ListFeatureCollection collection) {
        final SimpleFeatureType indexSchema = collection.getSchema();
        final int slicesNum = getNumberOfSlices();
        if (startIndex > slicesNum) {
            throw new IllegalArgumentException(
                    "The paging start index can't be higher than the number of available slices");
        }
        int lastIndex = startIndex + limit;
        if (lastIndex > slicesNum) {
            lastIndex = slicesNum;
        }
        final String varName = variableDS.getFullName();
        for (int imageIndex = startIndex; imageIndex < lastIndex; imageIndex++) {

            int[] index = splitIndex(imageIndex);

            // Put a new sliceIndex in the list
            final Slice2DIndex variableIndex = new Slice2DIndex(index, varName);
            reader.ancillaryFileManager.addSlice(variableIndex);

            // Create a feature for that index to be put in the CoverageSlicesCatalog
            final SimpleFeature feature =
                    createFeature(
                            coverageName.toString(),
                            index,
                            coordinateSystem,
                            imageIndex,
                            indexSchema);
            if (feature != null) {
                collection.add(feature);
            } // or else it is a non-existing slice (not in catalog, but counted)
        }
        // return processed slices
        return lastIndex - startIndex;
    }

    /**
     * Create a SimpleFeature on top of the variable and indexes.
     *
     * @param index the dimension indexes
     * @param cs the {@link CoordinateSystem} associated with that variable
     * @param imageIndex the index to be associated to the feature in the index
     * @param indexSchema the schema to be used to create the feature
     * @return the created {@link SimpleFeature} TODO move to variable wrapper
     */
    private SimpleFeature createFeature(
            final String coverageName,
            final int[] index,
            final CoordinateSystem cs,
            final int imageIndex,
            final SimpleFeatureType indexSchema) {

        final SimpleFeature feature = DataUtilities.template(indexSchema);
        feature.setAttribute(
                CoverageSlice.Attributes.GEOMETRY,
                NetCDFCRSUtilities.GEOM_FACTORY.toGeometry(
                        reader.georeferencing.getBoundingBox(variableDS.getShortName())));
        feature.setAttribute(CoverageSlice.Attributes.INDEX, imageIndex);

        Map<String, Integer> mappedIndex = mapIndex(index);

        // TIME management
        // Check if we have time and elevation domain and set the attribute if needed
        if (nDimensionIndex[T] >= 0) {
            final Date date = getValueByIndex(nDimensionIndex[T], mappedIndex);
            if (date == null) { // non-existing slice, not in catalog
                return null;
            }
            setFeatureTime(feature, date, cs);
        }
        // elevation
        if (nDimensionIndex[Z] >= 0) {
            final Number verticalValue = getValueByIndex(nDimensionIndex[Z], mappedIndex);
            if (verticalValue == null) { // non-existing slice, not in catalog
                return null;
            }
            feature.setAttribute(
                    reader.georeferencing
                            .getDimensionMapper()
                            .getDimension(NetCDFUtilities.ELEVATION_DIM),
                    verticalValue);
        }

        // additional domains
        if (getAdditionalDomains() != null) {
            for (int i = 0; i < getAdditionalDomains().size(); i++) {
                AdditionalDomain domain = getAdditionalDomains().get(i);
                final Object value;
                if (domain.getType().equals(DomainType.DATE)) {
                    value = getValueByIndex(nDimensionIndex[i + 2], mappedIndex);
                } else {
                    value = getValueByIndex(nDimensionIndex[i + 2], mappedIndex);
                }
                if (value == null) { // non-existing slice, not in catalog
                    return null;
                }
                feature.setAttribute(
                        reader.georeferencing
                                .getDimensionMapper()
                                .getDimension(domain.getName().toUpperCase()),
                        value);
            }
        }

        return feature;
    }

    private String setFeatureTime(SimpleFeature feature, Date date, CoordinateSystem cs) {
        String originalTimeAttribute = null;
        if (date != null) {
            originalTimeAttribute = getTimeAttribute(cs);
            String timeAttribute = originalTimeAttribute;
            if (reader.uniqueTimeAttribute) {
                timeAttribute = NetCDFUtilities.TIME;
            }
            feature.setAttribute(timeAttribute, date);
        }
        return originalTimeAttribute;
    }

    private String getTimeAttribute(CoordinateSystem cs) {
        CoordinateAxis timeAxis = cs.getTaxis();
        String name = timeAxis != null ? timeAxis.getFullName() : NetCDFUtilities.TIME_DIM;
        DimensionMapper dimensionMapper = reader.georeferencing.getDimensionMapper();
        String timeAttribute = dimensionMapper.getDimension(name.toUpperCase());
        if (timeAttribute == null) {
            // Fallback on standard name
            timeAttribute = dimensionMapper.getDimension(NetCDFUtilities.TIME_DIM);
        }
        return timeAttribute;
    }

    /**
     * Return the value of a particular dimension.
     *
     * @param dimensionIndex the index of the dimension
     * @return the value
     */
    @SuppressWarnings("unchecked")
    private <T> T getValueByIndex(int dimensionIndex, final Map<String, Integer> mappedIndex) {
        final Dimension dimension = variableDS.getDimension(dimensionIndex);
        return (T)
                reader.georeferencing
                        .getCoordinateVariable(dimension.getFullName())
                        .read(mappedIndex);
    }

    /**
     * Wrapper class used for setting the OSEQD dimension to Vertical, even if the {@link
     * CoordinateSystem} does not handle it.
     *
     * @author Nicola Lagomarsini GeoSolutions S.A.S.
     */
    static class CoordinateSystemAdapter extends CoordinateSystem {
        /** Input coordinate system */
        private CoordinateSystem cs;

        /** Boolean indicating that the vertical axis is present */
        private final boolean vertical;

        CoordinateSystemAdapter(CoordinateSystem cs) {
            this.cs = cs;
            // Check if the Vertical axis is present
            if (cs.hasVerticalAxis()) {
                vertical = true;
            } else {
                // Check if any of the unsupported dimensions is present
                Set<String> unsupported = NetCDFUtilities.getUnsupportedDimensions();
                boolean present = false;
                for (String dimension : unsupported) {
                    if (cs.containsAxis(dimension)) {
                        present = true;
                        break;
                    }
                }
                if (present) {
                    vertical = true;
                } else {
                    vertical = false;
                }
            }
        }

        @Override
        public boolean hasVerticalAxis() {
            return vertical;
        }

        @Override
        public boolean hasTimeAxis() {
            return cs.hasTimeAxis();
        }

        @Override
        public CoordinateAxis getTaxis() {
            return cs.getTaxis();
        }

        @Override
        public List<CoordinateAxis> getCoordinateAxes() {
            return cs.getCoordinateAxes();
        }
    }
}
