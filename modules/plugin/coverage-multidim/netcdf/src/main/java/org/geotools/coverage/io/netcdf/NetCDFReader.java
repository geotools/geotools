/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.io.netcdf;

import it.geosolutions.imageio.utilities.ImageIOUtilities;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BandedSampleModel;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.SampleModel;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.jai.ImageLayout;
import org.geotools.api.coverage.Coverage;
import org.geotools.api.coverage.grid.Format;
import org.geotools.api.coverage.grid.GridEnvelope;
import org.geotools.api.data.DataSourceException;
import org.geotools.api.data.ResourceInfo;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.Name;
import org.geotools.api.filter.Filter;
import org.geotools.api.geometry.MismatchedDimensionException;
import org.geotools.api.parameter.GeneralParameterValue;
import org.geotools.api.parameter.ParameterDescriptor;
import org.geotools.api.parameter.ParameterValue;
import org.geotools.api.referencing.ReferenceIdentifier;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.datum.PixelInCell;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.referencing.operation.MathTransform2D;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.coverage.grid.GeneralGridGeometry;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.InvalidGridGeometryException;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.DimensionDescriptor;
import org.geotools.coverage.grid.io.GranuleSource;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.HarvestedSource;
import org.geotools.coverage.grid.io.OverviewPolicy;
import org.geotools.coverage.grid.io.StructuredGridCoverage2DReader;
import org.geotools.coverage.io.CoverageAccess;
import org.geotools.coverage.io.CoverageAccess.AccessType;
import org.geotools.coverage.io.CoverageReadRequest;
import org.geotools.coverage.io.CoverageResponse;
import org.geotools.coverage.io.CoverageSource;
import org.geotools.coverage.io.CoverageSource.AdditionalDomain;
import org.geotools.coverage.io.CoverageSource.TemporalDomain;
import org.geotools.coverage.io.CoverageSource.VerticalDomain;
import org.geotools.coverage.io.Driver.DriverCapabilities;
import org.geotools.coverage.io.FileDriver;
import org.geotools.coverage.io.GridCoverageResponse;
import org.geotools.coverage.io.RasterLayout;
import org.geotools.coverage.io.catalog.CoverageSlicesCatalog;
import org.geotools.coverage.io.catalog.CoverageSlicesCatalogSource;
import org.geotools.coverage.io.util.DateRangeTreeSet;
import org.geotools.coverage.io.util.DoubleRangeTreeSet;
import org.geotools.coverage.util.CoverageUtilities;
import org.geotools.feature.NameImpl;
import org.geotools.gce.imagemosaic.ImageMosaicFormat;
import org.geotools.geometry.GeneralBounds;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.imageio.netcdf.NetCDFImageReader;
import org.geotools.imageio.netcdf.VariableAdapter;
import org.geotools.imageio.netcdf.VariableAdapter.UnidataSpatialDomain;
import org.geotools.referencing.operation.transform.IdentityTransform;
import org.geotools.referencing.operation.transform.ProjectiveTransform;
import org.geotools.util.DateRange;
import org.geotools.util.NumberRange;
import org.geotools.util.Range;
import org.geotools.util.SoftValueHashMap;
import org.geotools.util.URLs;
import org.geotools.util.factory.Hints;
import org.geotools.util.logging.Logging;

/**
 * A NetCDF Reader implementation
 *
 * @author Daniele Romagnoli, GeoSolutions SAS
 */
public class NetCDFReader extends AbstractGridCoverage2DReader implements StructuredGridCoverage2DReader {

    static final String UNSPECIFIED = "_UN$PECIFIED_";

    static final String DOMAIN_SUFFIX = "_DOMAIN";

    static final String HAS_PREFIX = "HAS_";

    private static final String MINIMUM_SUFFIX = "_MAXIMUM";

    private static final String MAXIMUM_SUFFIX = "_MINIMUM";

    private static final String DATATYPE_SUFFIX = "_DATATYPE";

    private static final Logger LOGGER = Logging.getLogger(NetCDFReader.class);

    static FileDriver DRIVER = new NetCDFDriver();

    private CoverageAccess access = null;

    private List<Name> names = null;

    private Set<String> setNames = null;

    private URL sourceURL;

    String defaultName = null;

    private SoftValueHashMap<String, CoverageSource> coverages = new SoftValueHashMap<>();

    public NetCDFReader(Object input, Hints uHints) throws DataSourceException {
        super(input, uHints);
        sourceURL = checkSource(input);

        if (!DRIVER.canProcess(DriverCapabilities.CONNECT, sourceURL, null)) {
            throw new DataSourceException("unable to connect to the specified source " + sourceURL);
        }

        // getting access to the source
        try {
            access = DRIVER.process(DriverCapabilities.CONNECT, sourceURL, null, uHints, null);
        } catch (IOException e) {
            throw new DataSourceException("Unable to connect", e);
        }
        if (access == null) {
            throw new DataSourceException("Unable to connect");
        }

        LOGGER.info("ACCEPTED: " + source.toString());

        // get the names
        names = access.getNames(null);
        setNames = new TreeSet<>();
        for (Name name : names) {
            String nameString = name.toString();
            if (defaultName == null) {
                defaultName = nameString;
            }
            setNames.add(nameString);
        }
    }

    private URL checkSource(Object input) {
        URL sourceURL = null;
        if (input instanceof URL) {
            sourceURL = (URL) input;
        } else if (input instanceof File) {
            sourceURL = URLs.fileToUrl((File) input);
        }
        return sourceURL;
    }

    @Override
    public Format getFormat() {
        return new NetCDFFormat();
    }

    @Override
    public String[] getMetadataNames(String coverageName) {
        checkIsSupported(coverageName);
        final List<String> metadataNames = new ArrayList<>();

        // standard metadata
        metadataNames.add(GridCoverage2DReader.HAS_TIME_DOMAIN);
        metadataNames.add(GridCoverage2DReader.TIME_DOMAIN);
        metadataNames.add(GridCoverage2DReader.TIME_DOMAIN_MINIMUM);
        metadataNames.add(GridCoverage2DReader.TIME_DOMAIN_MAXIMUM);
        metadataNames.add(GridCoverage2DReader.TIME_DOMAIN_RESOLUTION);
        metadataNames.add(GridCoverage2DReader.TIME_DOMAIN + DATATYPE_SUFFIX);

        metadataNames.add(GridCoverage2DReader.HAS_ELEVATION_DOMAIN);
        metadataNames.add(GridCoverage2DReader.ELEVATION_DOMAIN);
        metadataNames.add(GridCoverage2DReader.ELEVATION_DOMAIN_MINIMUM);
        metadataNames.add(GridCoverage2DReader.ELEVATION_DOMAIN_MAXIMUM);
        metadataNames.add(GridCoverage2DReader.ELEVATION_DOMAIN_RESOLUTION);
        metadataNames.add(GridCoverage2DReader.ELEVATION_DOMAIN + DATATYPE_SUFFIX);

        // additional domains
        addAdditionalMetadata(metadataNames, coverageName);
        return metadataNames.toArray(new String[metadataNames.size()]);
    }

    private void addAdditionalMetadata(List<String> metadataNames, String coverageName) {
        NetCDFSource source = null;
        try {
            source = (NetCDFSource) getGridCoverageSource(coverageName);
            List<AdditionalDomain> domains = source.getAdditionalDomains();
            if (domains != null && !domains.isEmpty()) {
                for (AdditionalDomain domain : domains) {
                    String domainName = domain.getName().toUpperCase();
                    metadataNames.add(HAS_PREFIX + domainName + DOMAIN_SUFFIX);
                    metadataNames.add(domainName + DOMAIN_SUFFIX);
                    metadataNames.add(domainName + DOMAIN_SUFFIX + MINIMUM_SUFFIX);
                    metadataNames.add(domainName + DOMAIN_SUFFIX + MAXIMUM_SUFFIX);
                    metadataNames.add(domainName + DOMAIN_SUFFIX + DATATYPE_SUFFIX);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getMetadataValue(String coverageName, String name) {
        String value = null;
        NetCDFSource source = null;
        try {
            source = (NetCDFSource) getGridCoverageSource(coverageName);
            final TemporalDomain timeDomain = source.getTemporalDomain();
            final VerticalDomain verticalDomain = source.getVerticalDomain();

            final List<AdditionalDomain> additionalDomains = source.getAdditionalDomains();
            final boolean hasAdditionalDomains = additionalDomains != null && !additionalDomains.isEmpty();
            final boolean hasTimeDomain = timeDomain != null;
            final boolean hasElevationDomain = verticalDomain != null;

            if (name.equalsIgnoreCase(GridCoverage2DReader.HAS_ELEVATION_DOMAIN))
                return String.valueOf(hasElevationDomain);

            if (name.equalsIgnoreCase(GridCoverage2DReader.HAS_TIME_DOMAIN)) {
                return String.valueOf(hasTimeDomain);
            }

            // NOT supported
            if (name.equalsIgnoreCase(GridCoverage2DReader.TIME_DOMAIN_RESOLUTION)) {
                return null;
            }
            // NOT supported
            if (name.equalsIgnoreCase(GridCoverage2DReader.ELEVATION_DOMAIN_RESOLUTION)) {
                return null;
            }

            if (hasTimeDomain) {
                if (name.equalsIgnoreCase("time_domain")) {
                    return parseDomain(name, timeDomain);
                }
                if (name.equalsIgnoreCase("time_domain_minimum") || name.equalsIgnoreCase("time_domain_maximum")) {
                    return parseDomain(name, timeDomain);
                }
                if (name.equalsIgnoreCase("time_domain_datatype")) {
                    return parseDomain(name, timeDomain);
                }
            }

            if (hasElevationDomain) {
                if (name.equalsIgnoreCase("elevation_domain")) {
                    return parseDomain(name, verticalDomain);
                }

                if (name.equalsIgnoreCase("elevation_domain_minimum")
                        || name.equalsIgnoreCase("elevation_domain_maximum")) {
                    return parseDomain(name, verticalDomain);
                }
                if (name.equalsIgnoreCase("elevation_domain_datatype")) {
                    return parseDomain(name, verticalDomain);
                }
            }

            if (hasAdditionalDomains) {
                if (name.startsWith(HAS_PREFIX)) {
                    final String substring =
                            name.substring(HAS_PREFIX.length(), name.length() - DOMAIN_SUFFIX.length());
                    for (AdditionalDomain additionalDomain : additionalDomains) {
                        if (additionalDomain.getName().toUpperCase().contains(substring)) {
                            return "true";
                        }
                    }
                    return "false";
                } else if (name.contains(DATATYPE_SUFFIX)) {
                    for (AdditionalDomain additionalDomain : additionalDomains) {
                        if (name.toUpperCase()
                                .startsWith(
                                        additionalDomain.getName().toUpperCase() + DOMAIN_SUFFIX + DATATYPE_SUFFIX)) {
                            return parseDomain(name, additionalDomain);
                        }
                    }
                } else if (name.contains(DOMAIN_SUFFIX)) {
                    for (AdditionalDomain additionalDomain : additionalDomains) {
                        if (name.toUpperCase()
                                .startsWith(additionalDomain.getName().toUpperCase() + DOMAIN_SUFFIX)) {
                            return parseDomain(name, additionalDomain);
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //
        return value;
    }

    @Override
    public String[] getGridCoverageNames() {
        return setNames.toArray(new String[setNames.size()]);
    }

    @Override
    public Set<ParameterDescriptor<List>> getDynamicParameters(String coverageName) throws IOException {
        NetCDFSource source = (NetCDFSource) getGridCoverageSource(coverageName);
        if (source != null) {
            return source.getDynamicParameters();
        }
        return null;
    }

    @Override
    public int getGridCoverageCount() {
        return setNames.size();
    }

    /** Parse a domain */
    private String parseDomain(String name, Object domain) throws IOException {
        name = name.toLowerCase();
        if (domain instanceof VerticalDomain) {

            // Vertical domain management
            VerticalDomain verticalDomain = (VerticalDomain) domain;
            if (name.endsWith("domain")) {

                // global domain
                SortedSet<? extends NumberRange<Double>> verticalElements =
                        verticalDomain.getVerticalElements(true, null);
                return buildVerticalList(verticalElements);
            } else if (name.endsWith("datatype")) {
                return Double.class.getName();
            } else {
                // min or max requests
                SortedSet<? extends NumberRange<Double>> verticalElements =
                        verticalDomain.getVerticalElements(false, null);
                NumberRange<Double> overall = verticalElements.iterator().next();
                if (name.endsWith("maximum")) {
                    return Double.toString(overall.getMaximum());
                } else if (name.endsWith("minimum")) {
                    return Double.toString(overall.getMinimum());
                } else {
                    throw new IllegalArgumentException("Unsupported metadata name");
                }
            }
        } else if (domain instanceof TemporalDomain) {

            // Temporal domain management
            TemporalDomain temporalDomain = (TemporalDomain) domain;
            if (name.endsWith("domain")) {
                // global domain
                SortedSet<? extends DateRange> temporalElements = temporalDomain.getTemporalElements(true, null);
                return buildTemporalList(temporalElements);
            } else if (name.endsWith("datatype")) {
                return Date.class.getName();
            } else {
                SortedSet<? extends DateRange> temporalElements = temporalDomain.getTemporalElements(false, null);
                DateRange overall = temporalElements.iterator().next();
                // min or max requests
                if (name.endsWith("maximum")) {
                    return ConvertersHack.convert(overall.getMaxValue(), String.class);
                } else if (name.endsWith("minimum")) {
                    return ConvertersHack.convert(overall.getMinValue(), String.class);
                } else {
                    throw new IllegalArgumentException("Unsupported metadata name");
                }
            }
        } else if (domain instanceof AdditionalDomain) {

            // Vertical domain management
            AdditionalDomain additionalDomain = (AdditionalDomain) domain;
            if (name.endsWith("domain")) {
                Set<Object> elements = additionalDomain.getElements(false, null);
                return buildElementsList(elements);
            } else if (name.endsWith("datatype")) {
                switch (additionalDomain.getType()) {
                    case NUMBER:
                        return Double.class.getName();
                    case DATE:
                        return Date.class.getName();
                    default:
                        return String.class.getName();
                }
            } else {
                // min or max requests
                Set<Object> elements = additionalDomain.getElements(true, null);
                Range<?> range = (Range<?>) elements.iterator().next();
                if (name.endsWith("maximum")) {
                    return ConvertersHack.convert(range.getMaxValue(), String.class);
                } else if (name.endsWith("minimum")) {
                    return ConvertersHack.convert(range.getMinValue(), String.class);
                } else {
                    throw new IllegalArgumentException("Unsupported metadata name");
                }
            }
        } else {
            throw new IllegalArgumentException("Unsupported domain ");
        }
    }

    private String buildTemporalList(SortedSet<? extends DateRange> temporalElements) {
        Iterator<? extends DateRange> iterator = temporalElements.iterator();
        final StringBuilder buff = new StringBuilder("");
        while (iterator.hasNext()) {
            DateRange range = iterator.next();
            buff.append(ConvertersHack.convert(range.getMinValue(), String.class)
                    + "/"
                    + ConvertersHack.convert(range.getMaxValue(), String.class));
            if (iterator.hasNext()) {
                buff.append(",");
            }
        }
        return buff.toString();
    }

    /** Setup a String containing vertical domain by doing a scan of a set of vertical Elements */
    private String buildVerticalList(SortedSet<? extends NumberRange<Double>> verticalElements) {
        Iterator<? extends NumberRange<Double>> iterator = verticalElements.iterator();
        LinkedHashSet<String> ranges = new LinkedHashSet<>();
        while (iterator.hasNext()) {
            NumberRange<Double> range = iterator.next();
            ranges.add(range.getMinValue() + "/" + range.getMaxValue());
        }
        return buildResultsString(ranges);
    }

    /**
     * Setup a String containing additional domain by doing a scan of a set of Elements
     *
     * @return TODO: improve that to deal with multiple types
     */
    private String buildElementsList(Set<Object> elements) {
        Iterator<Object> iterator = elements.iterator();
        LinkedHashSet<String> ranges = new LinkedHashSet<>();

        while (iterator.hasNext()) {
            Object value = iterator.next();
            ranges.add(ConvertersHack.convert(value, String.class));
        }
        return buildResultsString(ranges);
    }

    @Override
    public GridCoverage2D read(String coverageName, GeneralParameterValue... parameters)
            throws IllegalArgumentException, IOException {
        final CoverageSource gridSource = getGridCoverageSource(coverageName);
        final CoverageReadRequest request = setupCoverageRequest(parameters, gridSource);
        final CoverageResponse result = gridSource.read(request, null);
        Collection<? extends Coverage> results = result.getResults(null);
        if (results == null || results.isEmpty()) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("No results have been found");
            }
            return null;
        }
        GridCoverageResponse resp = (GridCoverageResponse) results.iterator().next();
        return resp.getGridCoverage2D();
    }

    /** Create the coverageReadRequest on top of the specified read params */
    private CoverageReadRequest setupCoverageRequest(GeneralParameterValue[] params, final CoverageSource gridSource)
            throws IOException {
        CoverageReadRequest request = new CoverageReadRequest();
        if (params != null) {
            for (GeneralParameterValue gParam : params) {
                if (gParam instanceof ParameterValue<?>) {
                    final ParameterValue<?> param = (ParameterValue<?>) gParam;
                    final ReferenceIdentifier name = param.getDescriptor().getName();
                    try {
                        extractParameter(param, name, request, gridSource);
                    } catch (Exception e) {
                        throw new IOException(e);
                    }
                }
            }
        }
        return request;
    }

    private void extractParameter(
            ParameterValue<?> param, ReferenceIdentifier name, CoverageReadRequest request, CoverageSource gridSource)
            throws MismatchedDimensionException, InvalidGridGeometryException, TransformException, IOException {

        // //
        //
        // Requested GridGeometry2D parameter
        //
        // //
        if (name.equals(AbstractGridFormat.READ_GRIDGEOMETRY2D.getName())) {
            final Object value = param.getValue();
            if (value == null) return;
            final GridGeometry2D gg = (GridGeometry2D) value;
            request.setDomainSubset(gg.getGridRange2D(), new ReferencedEnvelope(gg.getEnvelope2D()));
            return;
        }

        // //
        //
        // Time parameter
        //
        // //
        if (name.equals(ImageMosaicFormat.TIME.getName())) {
            final Object value = param.getValue();
            if (value == null) {
                return;
            }
            final List<?> dates = (List<?>) value;
            if (dates != null && !dates.isEmpty()) {
                SortedSet<DateRange> requestedTemporalSubset = new DateRangeTreeSet();
                for (Object val : dates) {
                    if (val instanceof Date) {
                        requestedTemporalSubset.add(new DateRange((Date) val, (Date) val));
                    } else if (val instanceof DateRange) {
                        requestedTemporalSubset.add((DateRange) val);
                    }
                }

                // TODO IMPROVE THAT TO DEAL ON RANGES
                request.setTemporalSubset(requestedTemporalSubset);
            }
            return;
        }

        //
        //
        // Elevation parameter
        //
        // //
        if (name.equals(ImageMosaicFormat.ELEVATION.getName())) {
            final Object value = param.getValue();
            if (value == null) return;
            List<?> values = (List<?>) value;
            if (!values.isEmpty()) {
                Set<NumberRange<Double>> verticalSubset = new DoubleRangeTreeSet();
                for (Object val : values) {
                    if (val instanceof Number) {
                        verticalSubset.add(new NumberRange<>(
                                Double.class, ((Number) val).doubleValue(), ((Number) val).doubleValue()));
                    } else if (val instanceof NumberRange) {
                        @SuppressWarnings("unchecked")
                        NumberRange<Double> casted = (NumberRange<Double>) val;
                        verticalSubset.add(casted);
                    }
                }
                // TODO IMPROVE THAT TO DEAL ON RANGES
                request.setVerticalSubset(verticalSubset);
            }
            return;
        }

        if (name.equals(ImageMosaicFormat.FILTER.getName())) {
            final Object value = param.getValue();
            if (value == null) return;
            request.setFilter((Filter) value);
            return;
        }

        // setup the the bands parameter which defines the order and the bands that should
        // bereturned
        if (name.equals(ImageMosaicFormat.BANDS.getName())) {
            // if the parameter is NULL no problem
            request.setBands((int[]) param.getValue());
            return;
        }

        String paramName = name.getCode();
        if (((NetCDFSource) gridSource).isParameterSupported(name)) {
            final Object value = param.getValue();
            if (value == null) {
                return;
            }
            final Set<Object> values = new HashSet<>();
            if (value instanceof Collection) {
                @SuppressWarnings("unchecked")
                Collection<Object> other = (Collection<Object>) value;
                values.addAll(other);
            } else {
                values.add(value);
            }
            // remove last comma
            Map<String, Set<?>> domainsSubset = request.getAdditionalDomainsSubset();
            if (domainsSubset == null) {
                domainsSubset = new HashMap<>();
                request.setAdditionalDomainsSubset(domainsSubset);
            }
            domainsSubset.put(paramName, values);
        }
    }

    /** Return a {@link CoverageSource} related to the specified coverageName */
    private CoverageSource getGridCoverageSource(final String coverageName) throws IOException {
        // Preliminar check on name availability
        checkIsSupported(coverageName);
        synchronized (coverages) {
            if (coverages.containsKey(coverageName)) {
                return coverages.get(coverageName);
            }

            // create, cache and return
            CoverageSource source = access.access(new NameImpl(coverageName), null, AccessType.READ_ONLY, null, null);
            coverages.put(coverageName, source);
            return source;
        }
    }

    /** Check whether the specified coverageName is one of the coverage available for the reader */
    private void checkIsSupported(final String coverageName) {
        if (!setNames.contains(coverageName)) {
            throw new IllegalArgumentException("the specified coverage is not available: " + coverageName);
        }
    }

    /** Read a GridCoverage2D base on the specified read parameters. */
    @Override
    public GridCoverage2D read(GeneralParameterValue... parameters) throws IllegalArgumentException, IOException {
        if (!names.isEmpty()) {
            if (names.size() > 1) {
                throw new IllegalArgumentException("You need to specify a coverageName");
            } else {
                return read(names.get(0).toString(), parameters);
            }
        }
        return null;
    }

    @Override
    public void dispose() {
        super.dispose();
        synchronized (this) {
            if (coverages != null && !coverages.isEmpty()) {
                Iterator<String> keysIt = coverages.keySet().iterator();
                while (keysIt.hasNext()) {
                    String key = keysIt.next();
                    CoverageSource sourceCov = coverages.get(key);
                    sourceCov.dispose();
                }
                coverages.clear();
            }
            coverages = null;
        }
        if (access != null) {
            try {
                access.dispose();
            } catch (Throwable t) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(Level.FINE, t.getLocalizedMessage(), t);
                }
            }
        }
    }

    /** Build a String containing comma separated values from the result set */
    private String buildResultsString(Set<String> result) {
        if (result.isEmpty()) {
            return "";
        }

        final StringBuilder buff = new StringBuilder();
        for (Iterator<String> it = result.iterator(); it.hasNext(); ) {
            buff.append(ConvertersHack.convert(it.next(), String.class));
            if (it.hasNext()) {
                buff.append(",");
            }
        }
        return buff.toString();
    }

    private String checkUnspecifiedCoverage(String coverageName) {
        if (coverageName.equalsIgnoreCase(UNSPECIFIED)) {
            if (defaultName == null) {
                throw new IllegalArgumentException("coverageName not specified and no defaultName for " + sourceURL);
            } else {
                return defaultName;
            }
        } else if (!setNames.contains(coverageName)) {
            throw new IllegalArgumentException("coverageName " + coverageName + " not found for " + sourceURL);
        } else {
            return coverageName;
        }
    }

    @Override
    public GeneralBounds getOriginalEnvelope() {
        return getOriginalEnvelope(UNSPECIFIED);
    }

    @Override
    public GeneralBounds getOriginalEnvelope(String coverageName) {
        coverageName = checkUnspecifiedCoverage(coverageName);
        try {
            CoverageSource source = getGridCoverageSource(coverageName);
            VariableAdapter.UnidataSpatialDomain spatialDomain = (UnidataSpatialDomain) source.getSpatialDomain();
            GeneralBounds generalEnvelope = new GeneralBounds(spatialDomain.getReferencedEnvelope());
            generalEnvelope.setCoordinateReferenceSystem(spatialDomain.getCoordinateReferenceSystem2D());
            return generalEnvelope;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public GridEnvelope getOriginalGridRange() {
        return getOriginalGridRange(UNSPECIFIED);
    }

    @Override
    public GridEnvelope getOriginalGridRange(String coverageName) {
        coverageName = checkUnspecifiedCoverage(coverageName);
        try {
            final CoverageSource source = getGridCoverageSource(coverageName);
            VariableAdapter.UnidataSpatialDomain spatialDomain = (UnidataSpatialDomain) source.getSpatialDomain();
            return spatialDomain.getGridGeometry().getGridRange2D();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public double[] getReadingResolutions(OverviewPolicy policy, double[] requestedResolution) {
        try {
            return getReadingResolutions(UNSPECIFIED, policy, requestedResolution);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public double[] getReadingResolutions(String coverageName, OverviewPolicy policy, double[] requestedResolution)
            throws IOException {
        // Currently we have no overviews support so we will return the highest resolution
        coverageName = checkUnspecifiedCoverage(coverageName);
        final CoverageSource source = getGridCoverageSource(coverageName);
        VariableAdapter.UnidataSpatialDomain spatialDomain = (UnidataSpatialDomain) source.getSpatialDomain();
        GeneralGridGeometry gridGeometry2D = spatialDomain.getGridGeometry();
        AffineTransform gridToCRS = (AffineTransform) gridGeometry2D.getGridToCRS();
        return CoverageUtilities.getResolution(gridToCRS);
    }

    @Override
    public double[][] getResolutionLevels() throws IOException {
        return getResolutionLevels(UNSPECIFIED);
    }

    @Override
    public double[][] getResolutionLevels(String coverageName) throws IOException {
        coverageName = checkUnspecifiedCoverage(coverageName);
        double[][] res = new double[1][2];
        double[] readRes = getReadingResolutions(coverageName, null, null);
        res[0][0] = readRes[0];
        res[0][1] = readRes[1];
        return res;
    }

    @Override
    public ImageLayout getImageLayout() throws IOException {
        return getImageLayout(UNSPECIFIED);
    }

    @Override
    public ImageLayout getImageLayout(String coverageName) throws IOException {
        coverageName = checkUnspecifiedCoverage(coverageName);
        try {
            final CoverageSource source = getGridCoverageSource(coverageName);
            VariableAdapter.UnidataSpatialDomain spatialDomain = (UnidataSpatialDomain) source.getSpatialDomain();
            GridEnvelope2D gridRange = spatialDomain.getGridGeometry().getGridRange2D();
            RasterLayout rasterElement =
                    spatialDomain.getRasterElements(false, null).iterator().next();
            NetCDFImageReader reader = (NetCDFImageReader) ((NetCDFAccess) access).reader;
            int dataType = DataBuffer.TYPE_DOUBLE;
            VariableAdapter descriptor = reader.getCoverageDescriptor(new NameImpl(coverageName));
            if (descriptor != null) {
                dataType = descriptor.getSampleModel().getDataType();
            }
            SampleModel sampleModel =
                    new BandedSampleModel(dataType, (int) gridRange.getWidth(), (int) gridRange.getHeight(), 1);
            ColorModel colorModel = ImageIOUtilities.createColorModel(sampleModel);
            Rectangle rect = rasterElement.toRectangle();
            ImageLayout layout = new ImageLayout(rect.x, rect.y, rect.width, rect.height);
            layout.setSampleModel(sampleModel);
            layout.setColorModel(colorModel);
            return layout;

            // TODO: DR: need to define a real ImageLayout. We may consider adding ImageLayout to
            // RasterLayout

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CoordinateReferenceSystem getCoordinateReferenceSystem() {
        return getCoordinateReferenceSystem(UNSPECIFIED);
    }

    @Override
    public CoordinateReferenceSystem getCoordinateReferenceSystem(String coverageName) {
        coverageName = checkUnspecifiedCoverage(coverageName);
        try {
            final CoverageSource source = getGridCoverageSource(coverageName);
            VariableAdapter.UnidataSpatialDomain spatialDomain = (UnidataSpatialDomain) source.getSpatialDomain();
            return spatialDomain.getCoordinateReferenceSystem2D();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public MathTransform getOriginalGridToWorld(PixelInCell pixInCell) {
        return getOriginalGridToWorld(UNSPECIFIED, pixInCell);
    }

    @Override
    public MathTransform getOriginalGridToWorld(String coverageName, PixelInCell pixInCell) {
        coverageName = checkUnspecifiedCoverage(coverageName);
        try {
            final CoverageSource source = getGridCoverageSource(coverageName);
            VariableAdapter.UnidataSpatialDomain spatialDomain = (UnidataSpatialDomain) source.getSpatialDomain();
            MathTransform2D gridToWorld = spatialDomain.getGridToWorldTransform(null);
            if (pixInCell == PixelInCell.CELL_CENTER) {
                return gridToWorld;
            }

            // we do have to change the pixel datum
            if (gridToWorld instanceof AffineTransform) {
                final AffineTransform tr = new AffineTransform((AffineTransform) gridToWorld);
                tr.concatenate(AffineTransform.getTranslateInstance(-0.5, -0.5));
                return ProjectiveTransform.create(tr);
            }

            if (gridToWorld instanceof IdentityTransform) {
                final AffineTransform tr = new AffineTransform(1, 0, 0, 1, 0, 0);
                tr.concatenate(AffineTransform.getTranslateInstance(-0.5, -0.5));
                return ProjectiveTransform.create(tr);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        throw new IllegalStateException("This reader's grid to world transform is invalid!");
    }

    @Override
    public GranuleSource getGranules(String coverageName, boolean readOnly)
            throws IOException, UnsupportedOperationException {
        NetCDFImageReader reader = (NetCDFImageReader) ((NetCDFAccess) access).reader;
        final CoverageSlicesCatalog catalog = reader.getCatalog();
        return new CoverageSlicesCatalogSource(catalog, coverageName);
    }

    @Override
    public boolean isReadOnly() {
        return true;
    }

    @Override
    public void createCoverage(String coverageName, SimpleFeatureType schema /*,
            Set<DimensionDescriptor> dimensions*/)
            throws IOException, UnsupportedOperationException {
        throw new UnsupportedOperationException("This operation is not supported on this reader");
    }

    @Override
    public List<HarvestedSource> harvest(String defaultCoverage, Object source, Hints hints)
            throws IOException, UnsupportedOperationException {
        throw new UnsupportedOperationException("This operation is not supported on this reader");
    }

    @Override
    public List<DimensionDescriptor> getDimensionDescriptors(String coverageName) throws IOException {
        final CoverageSource source = getGridCoverageSource(coverageName);
        return source.getDimensionDescriptors();
    }

    @Override
    public boolean removeCoverage(String coverageName, boolean forceDelete)
            throws IOException, UnsupportedOperationException {
        if (setNames.contains(coverageName)) {
            setNames.remove(coverageName);
        }
        Name name = new NameImpl(coverageName);
        if (names.contains(name)) {
            access.delete(name, null, hints);
        } else {
            return false;
        }
        if (Objects.equals(defaultName, coverageName)) {
            Iterator<Name> iterator = names.iterator();
            if (iterator.hasNext()) {
                defaultName = iterator.next().toString();
            } else {
                defaultName = null;
            }
        }
        if (forceDelete) {
            delete(true);
        }
        return true;
    }

    @Override
    public void delete(boolean deleteData) throws IOException {
        ((NetCDFAccess) access).purge();
        if (deleteData) {
            // hold hands off the files first
            dispose();
            // now it's possible to delete them
            File file = URLs.urlToFile(sourceURL);
            if (file != null && file.exists()) {
                file.delete();
            }
        }
    }

    @Override
    public ResourceInfo getInfo(String coverageName) {
        String name = checkUnspecifiedCoverage(coverageName);
        return new NetCDFFileResourceInfo(this, name, ((NetCDFAccess) access).reader.getCatalog(), sourceURL);
    }
}
