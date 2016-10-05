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
package org.geotools.gce.imagemosaic;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.media.jai.ImageLayout;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.DecimationPolicy;
import org.geotools.coverage.grid.io.DefaultDimensionDescriptor;
import org.geotools.coverage.grid.io.DimensionDescriptor;
import org.geotools.coverage.grid.io.GranuleSource;
import org.geotools.coverage.grid.io.GranuleStore;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.coverage.grid.io.OverviewPolicy;
import org.geotools.coverage.grid.io.StructuredGridCoverage2DReader;
import org.geotools.data.DataUtilities;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.Hints;
import org.geotools.feature.visitor.CalcResult;
import org.geotools.feature.visitor.FeatureCalc;
import org.geotools.feature.visitor.MaxVisitor;
import org.geotools.feature.visitor.MinVisitor;
import org.geotools.feature.visitor.UniqueVisitor;
import org.geotools.filter.SortByImpl;
import org.geotools.gce.imagemosaic.OverviewsController.OverviewLevel;
import org.geotools.gce.imagemosaic.catalog.CatalogConfigurationBean;
import org.geotools.gce.imagemosaic.catalog.GranuleCatalog;
import org.geotools.gce.imagemosaic.catalog.GranuleCatalogSource;
import org.geotools.gce.imagemosaic.catalog.GranuleCatalogStore;
import org.geotools.gce.imagemosaic.catalog.GranuleCatalogVisitor;
import org.geotools.gce.imagemosaic.catalog.index.Indexer;
import org.geotools.gce.imagemosaic.catalog.index.Indexer.MultipleBandsDimensions.MultipleBandsDimension;
import org.geotools.gce.imagemosaic.catalog.index.IndexerUtils;
import org.geotools.gce.imagemosaic.granulecollector.DefaultSubmosaicProducerFactory;
import org.geotools.gce.imagemosaic.granulecollector.SubmosaicProducerFactory;
import org.geotools.gce.imagemosaic.granulecollector.SubmosaicProducerFactoryFinder;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.parameter.DefaultParameterDescriptor;
import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.builder.GridToEnvelopeMapper;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.geotools.referencing.operation.transform.IdentityTransform;
import org.geotools.referencing.operation.transform.ProjectiveTransform;
import org.geotools.resources.coverage.CoverageUtilities;
import org.geotools.resources.coverage.FeatureUtilities;
import org.geotools.resources.image.ImageUtilities;
import org.geotools.util.Range;
import org.geotools.util.Utilities;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.coverage.grid.GridEnvelope;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.sort.SortOrder;
import org.opengis.geometry.BoundingBox;
import org.opengis.geometry.Envelope;
import org.opengis.metadata.Identifier;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.ReferenceIdentifier;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.referencing.operation.TransformException;

/**
 * 
 * @author Simone Giannecchini, GeoSolutions SAS
 * @author Daniele Romagnoli, GeoSolutions SAS
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class RasterManager {

    final Hints excludeMosaicHints = new Hints(Utils.EXCLUDE_MOSAIC, true);

    private SubmosaicProducerFactory submosaicProducerFactory = new DefaultSubmosaicProducerFactory();

    /**
     * This class is responsible for putting together all the 2D spatial information needed for a certain raster.
     * 
     * <p>
     * Notice that when this structure will be extended to work in ND this will become much more complex or as an alternative a sibling
     * TemporalDomainManager will be created.
     * 
     * @author Simone Giannecchini, GeoSolutions SAS
     *
     */
    static class SpatialDomainManager {

        /** The base envelope 2D */
        ReferencedEnvelope coverageBBox;

        /** The CRS for the coverage */
        CoordinateReferenceSystem coverageCRS;

        /** The CRS related to the base envelope 2D */
        CoordinateReferenceSystem coverageCRS2D;

        // ////////////////////////////////////////////////////////////////////////
        //
        // Base coverage properties
        //
        // ////////////////////////////////////////////////////////////////////////
        /** The base envelope read from file */
        GeneralEnvelope coverageEnvelope = null;

        double[] coverageFullResolution;

        /** WGS84 envelope 2D for this coverage */
        ReferencedEnvelope coverageGeographicBBox;

        CoordinateReferenceSystem coverageGeographicCRS2D;

        MathTransform2D coverageGridToWorld2D;

        /** The base grid range for the coverage */
        Rectangle coverageRasterArea;

        GridEnvelope gridEnvelope;

        public SpatialDomainManager(final GeneralEnvelope envelope,
                final GridEnvelope2D coverageGridrange, final CoordinateReferenceSystem crs,
                final MathTransform coverageGridToWorld2D,
                final OverviewsController overviewsController)
                throws TransformException, FactoryException {
            this.coverageEnvelope = envelope.clone();
            this.gridEnvelope = coverageGridrange.clone();
            this.coverageRasterArea = (Rectangle) gridEnvelope;
            this.coverageCRS = crs;
            this.coverageGridToWorld2D = (MathTransform2D) coverageGridToWorld2D;
            this.coverageFullResolution = new double[2];
            final OverviewLevel highestLevel = overviewsController.resolutionsLevels.get(0);
            coverageFullResolution[0] = highestLevel.resolutionX;
            coverageFullResolution[1] = highestLevel.resolutionY;

            prepareCoverageSpatialElements();
        }

        /**
         * Initialize the 2D properties (CRS and Envelope) of this coverage
         * 
         * @throws TransformException
         * 
         * @throws FactoryException
         * @throws TransformException
         * @throws FactoryException
         */
        private void prepareCoverageSpatialElements() throws TransformException, FactoryException {
            //
            // basic initialization
            //
            coverageGeographicBBox = ImageUtilities.getWGS84ReferencedEnvelope(coverageEnvelope);
            coverageGeographicCRS2D = coverageGeographicBBox != null
                    ? coverageGeographicBBox.getCoordinateReferenceSystem() : null;

            //
            // Get the original envelope 2d and its spatial reference system
            //
            coverageCRS2D = CRS.getHorizontalCRS(coverageCRS);
            assert coverageCRS2D.getCoordinateSystem().getDimension() == 2;
            if (coverageCRS.getCoordinateSystem().getDimension() != 2) {
                final MathTransform transform = CRS.findMathTransform(coverageCRS, coverageCRS2D);
                final GeneralEnvelope bbox = CRS.transform(transform, coverageEnvelope);
                coverageBBox = ReferencedEnvelope.create(bbox, coverageCRS2D);
            } else {
                // it is already a bbox
                coverageBBox = ReferencedEnvelope.create(coverageEnvelope,
                        coverageEnvelope.getCoordinateReferenceSystem());
            }
        }

        public MathTransform getOriginalGridToWorld(final PixelInCell pixInCell) {
            synchronized (this) {
                if (coverageGridToWorld2D == null) {
                    final GridToEnvelopeMapper geMapper = new GridToEnvelopeMapper(gridEnvelope,
                            coverageEnvelope);
                    geMapper.setPixelAnchor(PixelInCell.CELL_CENTER);
                    coverageGridToWorld2D = (MathTransform2D) geMapper.createTransform();
                }
            }

            // we do not have to change the pixel datum
            if (pixInCell == PixelInCell.CELL_CENTER)
                return coverageGridToWorld2D;

            // we do have to change the pixel datum
            if (coverageGridToWorld2D instanceof AffineTransform) {
                final AffineTransform tr = new AffineTransform(
                        (AffineTransform) coverageGridToWorld2D);
                tr.concatenate(AffineTransform.getTranslateInstance(-0.5, -0.5));
                return ProjectiveTransform.create(tr);
            }
            if (coverageGridToWorld2D instanceof IdentityTransform) {
                final AffineTransform tr = new AffineTransform(1, 0, 0, 1, 0, 0);
                tr.concatenate(AffineTransform.getTranslateInstance(-0.5, -0.5));
                return ProjectiveTransform.create(tr);
            }
            throw new IllegalStateException("This reader's grid to world transform is invalud!");
        }
    }

    /** Logger. */
    private final static Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger(RasterManager.class);

    /** The coverage factory producing a {@link GridCoverage} from an image */
    private GridCoverageFactory coverageFactory;

    /**
     * {@link DomainDescriptor} describe a single domain in terms of name and {@link ParameterDescriptor} that can be used to filter values during a
     * read operation.
     * 
     * <p>
     * Notice that there is no caching of values for the domain itself right now.
     * 
     * <p>
     * The domain must have unique identifiers.
     * 
     * @author Simone Giannecchini, GeoSolutions SAS
     * 
     */
    public class DomainDescriptor {

        public static final String DOMAIN_SUFFIX = "_DOMAIN";

        static final String HAS_PREFIX = "HAS_";

        static final String DATATYPE_SUFFIX = "_DATATYPE";

        private DomainType domainType = DomainType.SINGLE_VALUE;

        /** Unique identifier for this domain. */
        private final String identifier;

        /** propertyName for this domain that tells me which Property from the underlying catalog provides values for it. */
        private final String propertyName;

        /** additionalPropertyName for this domain. It won't be null ONLY in case of ranged domains. */
        private final String additionalPropertyName;

        /** domain dataType */
        private final String dataType;

        /** The {@link ParameterDescriptor} that can be used to filter on this domain during a read operation. */
        private final DefaultParameterDescriptor<List> domainParameterDescriptor;

        /**
         * @return the identifier
         */
        private String getIdentifier() {
            return identifier;
        }

        public boolean isHasRanges() {
            return additionalPropertyName != null;
        }

        public String getDataType() {
            return dataType;
        }

        /**
         * @return the domainaParameterDescriptor
         */
        private DefaultParameterDescriptor<List> getDomainParameterDescriptor() {
            return domainParameterDescriptor;
        }

        private DomainDescriptor(final String identifier, final DomainType domainType,
                final String dataType, final String propertyName,
                final String additionalPropertyName) {
            this.identifier = identifier;
            this.propertyName = propertyName;
            this.domainType = domainType;
            this.dataType = dataType;
            this.additionalPropertyName = additionalPropertyName;
            final String name = identifier.toUpperCase();
            this.domainParameterDescriptor = DefaultParameterDescriptor.create(name,
                    "Additional " + identifier + " domain", List.class, null, false);
        }

        @Override
        public String toString() {
            return "DomainDescriptor [identifier=" + identifier + ", propertyName=" + propertyName
                    + ", dataType=" + dataType + ", additionalPropertyName="
                    + (additionalPropertyName != null ? additionalPropertyName : "__UNAVAILABLE__")
                    + "]";
        }

        /**
         * Extract the time domain extrema.
         * 
         * @param extrema a {@link String} either TIME_DOMAIN_MAXIMUM or TIME_DOMAIN_MINIMUM.
         * 
         * @return either TIME_DOMAIN_MAXIMUM or TIME_DOMAIN_MINIMUM as a {@link String}. TODO use num for extrema
         */
        private String getExtrema(String extrema) {
            try {
                String attribute = propertyName;
                // In case the domain has range, we will check the second element
                // in case we are looking for the maximum
                if (domainType != DomainType.SINGLE_VALUE
                        && extrema.toLowerCase().endsWith("maximum")) {
                    attribute = additionalPropertyName;
                }
                final FeatureCalc visitor = createExtremaQuery(extrema, attribute);

                // check result
                CalcResult tempRes = visitor.getResult();
                if (tempRes == null) {
                    throw new IllegalStateException("Unable to compute extrema value:" + extrema);
                }
                final Object result = tempRes.getValue();
                if (result == null) {
                    throw new IllegalStateException("Unable to compute extrema value:" + extrema);
                }
                return ConvertersHack.convert(result, String.class);
            } catch (IOException e) {
                if (LOGGER.isLoggable(Level.WARNING))
                    LOGGER.log(Level.WARNING, "Unable to compute extrema for TIME_DOMAIN", e);
                return null;
            }
        }

        /**
         * Retrieves the values for this domain
         *
         * @return
         */
        private String getValues() {
            if (domainType == DomainType.SINGLE_VALUE) {
                return getSingleValues();
            }
            return getRangeValues();
        }

        /**
         * Retrieves the Range values for this domain
         *
         * @return
         */
        private String getRangeValues() {
            try {
                Set<String> result = extractDomain(propertyName, additionalPropertyName,
                        domainType);
                if (result.size() <= 0) {
                    return "";
                }

                final StringBuilder buff = new StringBuilder();
                for (Iterator it = result.iterator(); it.hasNext();) {
                    buff.append(ConvertersHack.convert(it.next(), String.class));
                    if (it.hasNext()) {
                        buff.append(",");
                    }
                }
                return buff.toString();
            } catch (IOException e) {
                if (LOGGER.isLoggable(Level.WARNING))
                    LOGGER.log(Level.WARNING, "Unable to parse attribute: " + identifier, e);
                return "";
            }
        }

        /**
         * Retrieves the single values list of this domain (no ranges available)
         *
         * @return
         */
        private String getSingleValues() {
            try {

                // implicit ordering
                final Set result = new TreeSet(extractDomain(propertyName));
                // check result
                if (result.size() <= 0) {
                    return "";
                }

                final StringBuilder buff = new StringBuilder();
                for (Iterator it = result.iterator(); it.hasNext();) {
                    buff.append(ConvertersHack.convert(it.next(), String.class));
                    if (it.hasNext()) {
                        buff.append(",");
                    }
                }
                return buff.toString();
            } catch (IOException e) {
                if (LOGGER.isLoggable(Level.WARNING))
                    LOGGER.log(Level.WARNING, "Unable to parse attribute: " + identifier, e);
                return "";
            }
        }

        /**
         * This method is responsible for creating {@link Filter} that encompasses the provided {@link List} of values for this {@link DomainManager}.
         * 
         * @param values the {@link List} of values to use for building the containment {@link Filter}.
         * @return a {@link Filter} that encompasses the provided {@link List} of values for this {@link DomainManager}.
         */
        private Filter createFilter(List values) {

            // === create the filter
            // loop values and AND them
            final int size = values.size();
            final List<Filter> filters = new ArrayList<Filter>();
            FilterFactory2 ff = FeatureUtilities.DEFAULT_FILTER_FACTORY;
            for (int i = 0; i < size; i++) {
                // checks
                Object value = values.get(i);
                if (value == null) {
                    if (LOGGER.isLoggable(Level.INFO)) {
                        LOGGER.info("Ignoring null date for the filter:" + this.identifier);
                    }
                    continue;
                }
                if (domainType == DomainType.SINGLE_VALUE) {
                    // Domain made of single values
                    if (value instanceof Range) {
                        // RANGE
                        final Range range = (Range) value;
                        filters.add(ff.and(
                                ff.lessOrEqual(ff.property(propertyName),
                                        ff.literal(range.getMaxValue())),
                                ff.greaterOrEqual(ff.property(propertyName),
                                        ff.literal(range.getMinValue()))));
                    } else {
                        // SINGLE value
                        filters.add(ff.equal(ff.property(propertyName), ff.literal(value), true));
                    }
                } else { // domainType == DomainType.RANGE
                    // Domain made of ranges such as (beginTime,endTime) , (beginElevation,endElevation) , ...
                    if (value instanceof Range) {
                        // RANGE
                        final Range range = (Range) value;
                        final Comparable maxValue = range.getMaxValue();
                        final Comparable minValue = range.getMinValue();
                        if (maxValue.compareTo(minValue) != 0) {
                            // logic comes from Range.intersectsNC(Range)
                            // in summary, requestedMax > min && requestedMin < max
                            Filter maxCondition = ff.greaterOrEqual(ff.literal(maxValue),
                                    ff.property(propertyName));
                            Filter minCondition = ff.lessOrEqual(ff.literal(minValue),
                                    ff.property(additionalPropertyName));

                            filters.add(ff.and(Arrays.asList(maxCondition, minCondition)));
                            continue;
                        } else {
                            value = maxValue;
                        }
                    }
                    filters.add(ff.and(ff.lessOrEqual(ff.property(propertyName), ff.literal(value)),
                            ff.greaterOrEqual(ff.property(additionalPropertyName),
                                    ff.literal(value))));
                }
            }
            return ff.or(filters);
        }
    }

    /**
     * A {@link DomainManager} class which allows to deal with additional domains (if any) defined inside the mosaic. It provides DOMAIN_ALIAS
     * <--to--> original attribute mapping capabilities, metadata retrieval, filter creation, and domain support check
     * 
     * @author Daniele Romagnoli, GeoSolutions SAS.
     */
    public class DomainManager {

        private final Map<String, DomainDescriptor> domainsMap = new HashMap<String, DomainDescriptor>();

        private final List<DimensionDescriptor> dimensions = new ArrayList<DimensionDescriptor>();

        private final boolean attributeHasRange(String attribute) {
            return attribute.contains(Utils.RANGE_SPLITTER_CHAR);
        }

        DomainManager(Map<String, String> additionalDomainAttributes,
                SimpleFeatureType simpleFeatureType) {
            Utilities.ensureNonNull("additionalDomainAttributes", additionalDomainAttributes);
            Utilities.ensureNonNull("simpleFeatureType", simpleFeatureType);
            init(additionalDomainAttributes, simpleFeatureType);
        }

        /**
         * @param domainAttributes
         * @param simpleFeatureType
         * @throws IllegalArgumentException
         */
        private void init(Map<String, String> domainAttributes, SimpleFeatureType simpleFeatureType)
                throws IllegalArgumentException {
            for (java.util.Map.Entry<String, String> entry : domainAttributes.entrySet()) {

                DomainType domainType = DomainType.SINGLE_VALUE;
                final String domainName = entry.getKey();
                String propertyName = entry.getValue();
                // is the name equals to the propertyname?
                try {

                    // Domain with ranges management
                    if (attributeHasRange(propertyName)) {
                        domainType = domainAttributes.containsKey(Utils.TIME_DOMAIN)
                                ? DomainType.TIME_RANGE : DomainType.NUMBER_RANGE;
                        addDomain(domainName, propertyName, domainType, simpleFeatureType);
                        continue;
                    } else {
                        propertyName = extractAttributes(propertyName);
                        if (simpleFeatureType.getDescriptor(propertyName) != null) {
                            // add
                            addDomain(domainName, propertyName, domainType, simpleFeatureType);
                            // continue
                            continue;
                        }
                    }

                } catch (Exception e) {
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
                    }
                }

                // ok why we don't have it? Maybe shapefile name truncation?
                if (propertyName.length() > 10) {
                    // hakc for shapes
                    propertyName = propertyName.substring(0, 10);
                    // alias in provided type

                    try {
                        if (simpleFeatureType.getDescriptor(propertyName) != null) {
                            // add
                            addDomain(domainName, propertyName, domainType, simpleFeatureType);

                            // continue
                            continue;
                        }

                    } catch (Exception e) {
                        if (LOGGER.isLoggable(Level.FINE)) {
                            LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
                        }
                    }
                }

                // if I got here, we are in trouble. No way to add this param
                throw new IllegalArgumentException(
                        "Unable to add this domain:" + domainName + "-" + propertyName);

            }
        }

        /**
         * build an AdditionalDomainManager on top of the provided additionalDomainAttributes (a comma separated list of attribute names).
         * 
         * @param additionalDomainAttributes
         * @param simpleFeatureType
         */
        DomainManager(String additionalDomainAttributes, SimpleFeatureType simpleFeatureType) {
            Utilities.ensureNonNull("additionalDomainAttributes", additionalDomainAttributes);
            Utilities.ensureNonNull("simpleFeatureType", simpleFeatureType);

            final Map<String, String> domainPairs = new HashMap<String, String>();

            // split, looking for multiple values
            final String[] additionalDomainsNames = additionalDomainAttributes.split(",");
            if (additionalDomainsNames.length <= 0) {
                throw new IllegalArgumentException("Number of Domains should be > 0");
            }

            // add al the provided domain
            for (String propertyName : additionalDomainsNames) {
                String domainName = cleanupDomainName(propertyName);
                domainPairs.put(domainName, propertyName);
            }
            init(domainPairs, simpleFeatureType);
        }

        /**
         * 
         * @param domainName
         * @return
         * 
         * @TODO We can surely improve it by making use of Regular Expressions
         */
        private String cleanupDomainName(String domainName) {
            if (attributeHasRange(domainName)
                    || (domainName.contains("(") && domainName.contains(")"))) {
                // Getting rid of the attributes definition to get only the domain name
                domainName = domainName.substring(0, domainName.indexOf("("));
            }
            return domainName;
        }

        /**
         * Add a domain to the manager
         * 
         * @param domain the name of the domain
         * @param propertyName
         * @param featureType
         */
        private void addDomain(String name, String propertyName, final DomainType domainType,
                final SimpleFeatureType featureType) {
            Utilities.ensureNonNull("name", name);
            Utilities.ensureNonNull("propertyName", propertyName);

            // === checks
            // existing!
            if (domainsMap.containsKey(name)) {
                throw new IllegalArgumentException(
                        "Trying to add a domain with an existing name" + name);
            }

            // === checks
            // has Ranges
            String basePropertyName = propertyName;
            String additionalPropertyName = null;
            if (domainType != DomainType.SINGLE_VALUE) {

                // Deal with a case like this: time(begin,endtime)
                propertyName = extractAttributes(propertyName);

                // Getting 2 attributes for this domain
                String properties[] = propertyName.split(Utils.RANGE_SPLITTER_CHAR);
                if (properties == null || properties.length != 2) {
                    throw new IllegalArgumentException(
                            "Malformed domain with ranges: it should contain 2 attributes");
                }

                basePropertyName = properties[0];
                additionalPropertyName = properties[1];
            }

            // ad with uppercase and with suffix, the parameter that describes it will match this
            final String upperCase = name.toUpperCase();
            final AttributeDescriptor descriptor = featureType.getDescriptor(basePropertyName);
            final String type = descriptor.getType().getBinding().getName();
            domainsMap.put(upperCase + DomainDescriptor.DOMAIN_SUFFIX, new DomainDescriptor(name,
                    domainType, type, basePropertyName, additionalPropertyName));
            addDimensionDescriptor(name, upperCase, basePropertyName, additionalPropertyName);
        }

        private void addDimensionDescriptor(String name, String upperCase, String basePropertyName,
                String additionalPropertyName) {
            final String unitsName = upperCase.equalsIgnoreCase(Utils.TIME_DOMAIN)
                    ? CoverageUtilities.UCUM.TIME_UNITS.getName()
                    : upperCase.equalsIgnoreCase(Utils.ELEVATION_DOMAIN)
                            ? CoverageUtilities.UCUM.ELEVATION_UNITS.getName() : "FIXME"; // TODO: ADD UCUM units Management
            final String unitsSymbol = upperCase.equalsIgnoreCase(Utils.TIME_DOMAIN)
                    ? CoverageUtilities.UCUM.TIME_UNITS.getSymbol()
                    : upperCase.equalsIgnoreCase(Utils.ELEVATION_DOMAIN)
                            ? CoverageUtilities.UCUM.ELEVATION_UNITS.getSymbol() : "FIXME"; // TODO: ADD UCUM units Management
            final DimensionDescriptor dimensionDescriptor = new DefaultDimensionDescriptor(name,
                    unitsName, unitsSymbol, basePropertyName, additionalPropertyName);
            dimensions.add(dimensionDescriptor);
        }

        private String extractAttributes(String propertyName) {
            if (propertyName.contains("(") && propertyName.contains(")")) {
                // extract the ranges attributes
                propertyName = propertyName.substring(propertyName.indexOf("(")).replace("(", "")
                        .replace(")", "");
            }
            return propertyName;
        }

        /**
         * Check whether a specific parameter (identified by the {@link Identifier} name) is supported by this manager (and therefore, by the reader).
         * 
         * @param name
         * @return
         */
        public boolean isParameterSupported(final Identifier name) {
            if (!domainsMap.isEmpty()) {
                for (DomainDescriptor domain : domainsMap.values()) {
                    final ReferenceIdentifier nameLoc = domain.getDomainParameterDescriptor()
                            .getName();
                    if (nameLoc.equals(name)) {
                        return true;
                    }
                }
            }
            return false;
        }

        /**
         * Setup the List of metadataNames for this additional domains manager
         * 
         * @return
         */
        public List<String> getMetadataNames() {
            final List<String> metadataNames = new ArrayList<String>();
            if (!domainsMap.isEmpty()) {
                for (DomainDescriptor domain : domainsMap.values()) {
                    String domainName = domain.getIdentifier().toUpperCase();
                    metadataNames.add(domainName + DomainDescriptor.DOMAIN_SUFFIX);
                    if (domain.getDataType() != null) {
                        metadataNames.add(domainName + DomainDescriptor.DOMAIN_SUFFIX
                                + DomainDescriptor.DATATYPE_SUFFIX);
                    }
                    metadataNames.add(DomainDescriptor.HAS_PREFIX + domainName
                            + DomainDescriptor.DOMAIN_SUFFIX);
                }
            }
            return metadataNames;
        }

        /**
         * Return the value of a specific metadata by parsing the requested name as a Domain Name
         * 
         * @param name
         * @return
         */
        public String getMetadataValue(String name) {
            Utilities.ensureNonNull("name", name);

            String value = null;
            if (domainsMap.size() > 0) {
                // is a domain?
                if (domainsMap.containsKey(name)) {
                    final DomainDescriptor domainDescriptor = domainsMap.get(name);
                    value = domainDescriptor.getValues();
                } else {
                    // is a simple Has domain query?
                    if (name.startsWith(DomainDescriptor.HAS_PREFIX)) {
                        final String substring = name
                                .substring(DomainDescriptor.HAS_PREFIX.length(), name.length());
                        if (domainsMap.containsKey(substring)) {
                            return Boolean.toString(Boolean.TRUE);
                        } else {
                            return Boolean.toString(Boolean.FALSE);
                        }
                    } else if (name.endsWith(DomainDescriptor.DATATYPE_SUFFIX)) {
                        return domainsMap
                                .get(name.substring(0,
                                        name.lastIndexOf(DomainDescriptor.DATATYPE_SUFFIX)))
                                .getDataType();
                    } else {
                        // MINUM or MAXIMUM
                        if (name.endsWith("MINIMUM") || name.endsWith("MAXIMUM")) {
                            return domainsMap.get(name.substring(0, name.lastIndexOf("_")))
                                    .getExtrema(name);
                        }
                    }
                }
            }
            return value;
        }

        /**
         * Setup a Filter on top of the specified domainRequest which is in the form "key=value"
         * 
         * @param domain
         * @param values
         * @return
         */
        public Filter createFilter(String domain, List values) {
            // === checks
            if (domain == null || domain.isEmpty()) {
                throw new IllegalArgumentException("Null domain requested");
            }
            if (values == null || values.isEmpty()) {
                throw new IllegalArgumentException("Null domain values provided");
            }
            if (domainsMap.isEmpty() || !domainsMap.containsKey(domain)) {
                throw new IllegalArgumentException(
                        "requested domain is not supported by this mosaic: " + domain);
            }

            // get the property name
            DomainDescriptor domainDescriptor = domainsMap.get(domain);
            return domainDescriptor.createFilter(values);
        }

        /**
         * Return the set of dynamic parameterDescriptors (the ones related to domains) for this reader
         * 
         * @return
         */
        public Set<ParameterDescriptor<List>> getDynamicParameters() {
            Set<ParameterDescriptor<List>> dynamicParameters = new HashSet<ParameterDescriptor<List>>();
            if (!domainsMap.isEmpty()) {
                for (DomainDescriptor domain : domainsMap.values()) {
                    dynamicParameters.add(domain.getDomainParameterDescriptor());
                }
            }
            // return
            return dynamicParameters;
        }

    }

    enum DomainType {
        SINGLE_VALUE, TIME_RANGE, NUMBER_RANGE
    }

    /** Default {@link ColorModel}. */
    ColorModel defaultCM;

    /** Default {@link SampleModel}. */
    SampleModel defaultSM;

    /** Default palette */
    byte[][] defaultPalette;

    /**
     * The name of the input coverage TODO consider URI
     */
    private String coverageIdentifier;

    /** The hints to be used to produce this coverage */
    private Hints hints;

    OverviewsController overviewsController;

    OverviewPolicy overviewPolicy;

    DecimationPolicy decimationPolicy;

    private PathType pathType;

    boolean expandMe;

    boolean heterogeneousGranules;

    double[][] levels;

    SpatialDomainManager spatialDomainManager;

    ImageLayout defaultImageLayout;

    /** The inner {@link DomainManager} instance which allows to manage custom dimensions */
    DomainManager domainsManager;

    DomainManager elevationDomainManager;

    DomainManager timeDomainManager;

    volatile boolean enableEvents = false;// start disabled

    List<DimensionDescriptor> dimensionDescriptors = new ArrayList<DimensionDescriptor>();

    ImageMosaicReader parentReader;

    GranuleCatalog granuleCatalog;

    GranuleStore granuleStore;

    GranuleSource granuleSource;

    String typeName;

    Envelope imposedEnvelope;

    MosaicConfigurationBean configuration;

    // contains the bands names for this raster
    String[] providedBandsNames = null;

    public RasterManager(final ImageMosaicReader parentReader,
            MosaicConfigurationBean configuration) throws IOException {

        Utilities.ensureNonNull("ImageMosaicReader", parentReader);
        //may as well check this too, since it's being used without a null check
        Utilities.ensureNonNull("MosaicConfigurationBean", configuration);

        this.parentReader = parentReader;
        this.expandMe = configuration.isExpandToRGB();
        boolean checkAuxiliaryMetadata = configuration.isCheckAuxiliaryMetadata();
        this.heterogeneousGranules = configuration.getCatalogConfigurationBean().isHeterogeneous();
        this.configuration = configuration;
        hints = parentReader.getHints();
        updateHints(hints, configuration, parentReader);

        if (checkAuxiliaryMetadata) {
            hints.add(new RenderingHints(Utils.CHECK_AUXILIARY_METADATA, checkAuxiliaryMetadata));
        }

        // take ownership of the index : TODO: REMOVE THAT ONCE DEALING WITH MORE CATALOGS/RASTERMANAGERS
        // granuleCatalog = new HintedGranuleCatalog(parentReader.granuleCatalog, hints);
        granuleCatalog = parentReader.granuleCatalog;
        this.coverageFactory = parentReader.getGridCoverageFactory();
        this.coverageIdentifier = configuration != null ? configuration.getName()
                : ImageMosaicReader.UNSPECIFIED;
        this.pathType = configuration.getCatalogConfigurationBean().isAbsolutePath()
                ? PathType.ABSOLUTE : PathType.RELATIVE;

        extractOverviewPolicy();
        extractDecimationPolicy();

        // load defaultSM and defaultCM by using the sample_image if it was provided
        loadSampleImage(configuration);

        CatalogConfigurationBean catalogBean = configuration.getCatalogConfigurationBean();
        typeName = catalogBean != null ? catalogBean.getTypeName() : null;
        initDomains(configuration);
        if (defaultSM == null) {
            defaultSM = configuration.getSampleModel();
        }

        if (defaultCM == null) {
            defaultCM = configuration.getColorModel();
        }
        if (defaultPalette == null) {
            defaultPalette = configuration.getPalette();
        }

        if (defaultSM != null && defaultCM != null && defaultImageLayout == null) {
            defaultImageLayout = new ImageLayout().setColorModel(defaultCM)
                    .setSampleModel(defaultSM);
        }

        levels = configuration.getLevels();
        final double[] highRes = levels[0];
        final int numOverviews = configuration.getLevelsNum() - 1;
        double[][] overviews = null;
        if (numOverviews > 0) {
            overviews = new double[numOverviews][2];
            for (int i = 0; i < numOverviews; i++) {
                overviews[i][0] = levels[i + 1][0];
                overviews[i][1] = levels[i + 1][1];
            }
        }
        overviewsController = new OverviewsController(highRes, numOverviews, overviews);
        imposedEnvelope = configuration.getEnvelope();

        if (configuration.getIndexer() != null) {
            //we have indexer configuration, we can set the submosaic producer factory based off
            //that if it's available
            Indexer indexer = configuration.getIndexer();
            // handling multiple bands dimension if needed
            if (indexer.getMultipleBandsDimensions() != null
                    && indexer.getMultipleBandsDimensions().getMultipleBandsDimension() != null
                    && !indexer.getMultipleBandsDimensions().getMultipleBandsDimension().isEmpty()) {
                // we have at least one dimension with multiple bands
                List<MultipleBandsDimension> multipleBandsDimensions = indexer.getMultipleBandsDimensions().getMultipleBandsDimension();
                if (multipleBandsDimensions.size() != 1) {
                    // currently we only support a single dimension with multiple bands
                    throw new IllegalStateException("Only a single dimension with multiple bands is supported.");
                }
                // well we only need to fill the provided bands names
                providedBandsNames = multipleBandsDimensions.get(0).getBandsNames().split("\\s*,\\s*");
            }
            String submosaickerFactory = IndexerUtils
                .getParameter(Utils.Prop.GRANULE_COLLECTOR_FACTORY, indexer);
            if (submosaickerFactory != null) {
                SubmosaicProducerFactory submosaicProducerFactory = SubmosaicProducerFactoryFinder
                    .getGranuleHandlersSPI().get(submosaickerFactory);
                if (submosaicProducerFactory != null) {
                    this.submosaicProducerFactory = submosaicProducerFactory;
                }
                else {
                    LOGGER.warning("Found SubmosaicProducerFactory config in the Image Mosaic "
                        + "indexer, however the specified factory (" + submosaickerFactory
                        + ") could not be found. This may mean the indexer.properties or indexer.xml"
                        + "is misconfigured");
                }
            }
        }
    }

    private void updateHints(Hints hints, MosaicConfigurationBean configuration,
            ImageMosaicReader parentReader) {
        if (configuration != null) {
            String auxiliaryFilePath = configuration.getAuxiliaryFilePath();
            String auxiliaryDatastorePath = configuration.getAuxiliaryDatastorePath();
            boolean update = false;
            if (auxiliaryFilePath != null) {
                hints.add(new RenderingHints(Utils.AUXILIARY_FILES_PATH, auxiliaryFilePath));
                update = true;
            }
            if (auxiliaryDatastorePath != null) {
                hints.add(
                        new RenderingHints(Utils.AUXILIARY_DATASTORE_PATH, auxiliaryDatastorePath));
                update = true;
            }
            if (update && !configuration.getCatalogConfigurationBean().isAbsolutePath()
                    && !hints.containsKey(Utils.PARENT_DIR)) {
                String parentDir = null;
                if (parentReader.parentDirectory != null) {
                    parentDir = parentReader.parentDirectory.getAbsolutePath();
                } else {
                    Object source = parentReader.getSource();
                    if (source != null && source instanceof File && ((File) source).isDirectory()) {
                        parentDir = ((File) source).getAbsolutePath();
                    }
                }
                hints.add(new RenderingHints(Utils.PARENT_DIR, parentDir));
            }
        }
    }

    private void initDomains(MosaicConfigurationBean configuration) throws IOException {
        checkTypeName();
        if (typeName != null) {

            final SimpleFeatureType schema = granuleCatalog.getType(typeName);
            if (schema != null) {
                // additional domain attributes
                final String additionalDomainConfig = configuration.getAdditionalDomainAttributes();
                if (additionalDomainConfig != null && domainsManager == null) {
                    domainsManager = new DomainManager(additionalDomainConfig, schema);
                    dimensionDescriptors.addAll(domainsManager.dimensions);
                }

                // time attribute
                final String timeDomain = configuration.getTimeAttribute();
                if (timeDomain != null && timeDomainManager == null) {
                    final HashMap<String, String> init = new HashMap<String, String>();
                    init.put(Utils.TIME_DOMAIN, timeDomain);
                    timeDomainManager = new DomainManager(init, schema);
                    dimensionDescriptors.addAll(timeDomainManager.dimensions);
                }

                // elevation attribute
                final String elevationAttribute = configuration.getElevationAttribute();
                if (elevationAttribute != null && elevationDomainManager == null) {
                    final HashMap<String, String> init = new HashMap<String, String>();
                    init.put(Utils.ELEVATION_DOMAIN, elevationAttribute);
                    elevationDomainManager = new DomainManager(init, schema);
                    dimensionDescriptors.addAll(elevationDomainManager.dimensions);
                }
            }
        }
    }

    private void checkTypeName() throws IOException {
        if (typeName == null) {
            URL sourceURL = parentReader.sourceURL;
            if (sourceURL.getPath().endsWith("shp")) {
                typeName = FilenameUtils
                        .getBaseName(DataUtilities.urlToFile(sourceURL).getCanonicalPath());
            } else {
                typeName = configuration.getName();
            }
        }
        if (typeName == null && granuleCatalog != null) {
            String[] typeNames = granuleCatalog.getTypeNames();
            typeName = (typeNames != null && typeNames.length > 0) ? typeNames[0] : null;
        }
    }

    /**
     * This code tries to load the sample image from which we can extract SM and CM to use when answering to requests that falls within a hole in the
     * mosaic.
     *
     * @param configuration
     */
    private void loadSampleImage(MosaicConfigurationBean configuration) {
        if (this.parentReader.sourceURL == null) {
            // TODO: I need to define the sampleImage somehow for the ImageMosaicDescriptor case
            return;
        }

        final URL baseURL = this.parentReader.sourceURL;
        final File baseFile = DataUtilities.urlToFile(baseURL);
        // in case we do not manage to convert the source URL we leave right awaycd sr
        if (baseFile == null) {
            if (LOGGER.isLoggable(Level.FINE))
                LOGGER.fine("Unable to find sample image for path " + baseURL);
            return;
        }
        String baseName = baseFile.getParent() + "/";
        String fileName = null;
        File sampleImageFile = null;
        if (configuration != null) {
            String name = configuration.getName();
            if (name != null) {
                fileName = baseName + name + Utils.SAMPLE_IMAGE_NAME;
                sampleImageFile = new File(fileName);
                if (!sampleImageFile.exists() || !sampleImageFile.canRead()) {
                    sampleImageFile = null;
                }
            }
        }

        if (sampleImageFile == null) {
            sampleImageFile = new File(baseName + Utils.SAMPLE_IMAGE_NAME);
        }
        final RenderedImage sampleImage = Utils.loadSampleImage(sampleImageFile);
        if (sampleImage != null) {

            // load SM and CM
            defaultCM = sampleImage.getColorModel();
            defaultSM = sampleImage.getSampleModel();
            if (defaultCM instanceof IndexColorModel) {
                defaultPalette = Utils.extractPalette((IndexColorModel) defaultCM);
            }

            // default ImageLayout
            defaultImageLayout = new ImageLayout().setColorModel(defaultCM)
                    .setSampleModel(defaultSM);
        } else if (LOGGER.isLoggable(Level.WARNING))
            LOGGER.warning("Unable to find sample image for path " + baseURL);
    }

    /**
     * This method is responsible for checking the overview policy as defined by the provided {@link Hints}.
     *
     * @return the overview policy which can be one of {@link OverviewPolicy#IGNORE}, {@link OverviewPolicy#NEAREST}, {@link OverviewPolicy#SPEED},
     *         {@link OverviewPolicy#QUALITY}. Default is {@link OverviewPolicy#NEAREST}.
     */
    private OverviewPolicy extractOverviewPolicy() {

        // check if a policy was provided using hints (check even the
        // deprecated one)
        if (this.hints != null)
            if (this.hints.containsKey(Hints.OVERVIEW_POLICY))
                overviewPolicy = (OverviewPolicy) this.hints.get(Hints.OVERVIEW_POLICY);

        // use default if not provided. Default is nearest
        if (overviewPolicy == null) {
            overviewPolicy = OverviewPolicy.getDefaultPolicy();
        }
        assert overviewPolicy != null;
        return overviewPolicy;
    }

    /**
     * This method is responsible for checking the decimation policy as defined by the provided {@link Hints}.
     *
     * @return the decimation policy which can be one of {@link DecimationPolicy#ALLOW}, {@link DecimationPolicy#DISALLOW}. Default is
     *         {@link DecimationPolicy#ALLOW}.
     */
    private DecimationPolicy extractDecimationPolicy() {
        if (this.hints != null)
            if (this.hints.containsKey(Hints.DECIMATION_POLICY))
                decimationPolicy = (DecimationPolicy) this.hints.get(Hints.DECIMATION_POLICY);

        // use default if not provided. Default is allow
        if (decimationPolicy == null) {
            decimationPolicy = DecimationPolicy.getDefaultPolicy();
        }
        assert decimationPolicy != null;
        return decimationPolicy;

    }

    public Collection<GridCoverage2D> read(final GeneralParameterValue[] params)
            throws IOException {

        // create a request
        final RasterLayerRequest request = new RasterLayerRequest(params, this);
        if (request.isEmpty()) {
            if (LOGGER.isLoggable(Level.FINE))
                LOGGER.log(Level.FINE, "Request is empty: " + request.toString());
            return Collections.emptyList();
        }

        // create a response for the provided request
        final RasterLayerResponse response = new RasterLayerResponse(request, this,
            this.submosaicProducerFactory);

        // execute the request
        final GridCoverage2D elem = response.createResponse();
        if (elem != null) {
            return Collections.singletonList(elem);
        }
        return Collections.emptyList();

    }

    void getGranuleDescriptors(final Query q, final GranuleCatalogVisitor visitor)
            throws IOException {
        granuleCatalog.getGranuleDescriptors(q, visitor);

    }

    public PathType getPathType() {
        return pathType;
    }

    public String getCoverageIdentifier() {
        return coverageIdentifier;
    }

    public Hints getHints() {
        return hints;
    }

    public GridCoverageFactory getCoverageFactory() {
        return coverageFactory;
    }

    public String getTypeName() {
        return typeName;
    }

    /**
     * @param metadataName
     * @param attributeName
     * @return
     * @throws IOException
     */
    FeatureCalc createExtremaQuery(String metadataName, String attributeName) throws IOException {
        final Query query = new Query(typeName);
        query.setPropertyNames(Arrays.asList(attributeName));

        final FeatureCalc visitor = metadataName.toLowerCase().endsWith("maximum")
                ? new MaxVisitor(attributeName) : new MinVisitor(attributeName);
        granuleCatalog.computeAggregateFunction(query, visitor);
        return visitor;
    }

    /**
     * Extract the domain of a dimension as a set of unique values.
     * 
     * <p>
     * It retrieves a comma separated list of values as a Set of {@link String}.
     * 
     * @return a comma separated list of values as a {@link String}.
     * @throws IOException
     */
    Set extractDomain(final String attribute) throws IOException {
        Query query = new Query(typeName);
        query.setPropertyNames(Arrays.asList(attribute));
        final UniqueVisitor visitor = new UniqueVisitor(attribute);
        granuleCatalog.computeAggregateFunction(query, visitor);
        return visitor.getUnique();
    }

    /**
     * Extract the domain of a dimension (with Range) as a set of values.
     * 
     * <p>
     * It retrieves a comma separated list of values as a Set of {@link String}.
     * 
     * @param domainType
     * 
     * @return a comma separated list of values as a Set of {@link String}.
     * @throws IOException
     */
    private Set extractDomain(final String attribute, final String secondAttribute,
            final DomainType domainType) throws IOException {
        final Query query = new Query(typeName);

        final PropertyName propertyName = FeatureUtilities.DEFAULT_FILTER_FACTORY
                .property(attribute);
        query.setPropertyNames(Arrays.asList(attribute, secondAttribute));

        final SortByImpl[] sb = new SortByImpl[] {
                new SortByImpl(propertyName, SortOrder.ASCENDING) };
        // Checking whether it supports sorting capabilities
        if (granuleCatalog.getQueryCapabilities(typeName).supportsSorting(sb)) {
            query.setSortBy(sb);
        }

        final FeatureCalc visitor = domainType == DomainType.TIME_RANGE
                ? new DateRangeVisitor(attribute, secondAttribute)
                : new RangeVisitor(attribute, secondAttribute);
        granuleCatalog.computeAggregateFunction(query, visitor);
        return domainType == DomainType.TIME_RANGE ? ((DateRangeVisitor) visitor).getRange()
                : ((RangeVisitor) visitor).getRange();

    }

    /**
     * TODO this should not leak through
     *
     * @return
     */
    public GranuleCatalog getGranuleCatalog() {
        return granuleCatalog;
    }

    /**
     * Create a store for the coverage related to this {@link RasterManager} using the provided schema
     *
     * @param indexSchema
     * @throws IOException
     */
    public void createStore(SimpleFeatureType indexSchema) throws IOException {
        final String typeName = indexSchema.getTypeName();
        final SimpleFeatureType type = typeName != null ? granuleCatalog.getType(typeName) : null;
        if (type == null) {
            granuleCatalog.createType(indexSchema);
            this.typeName = typeName;
        } else {
            if (this.typeName == null) {
                this.typeName = typeName;
            }
            // remove them all, assuming the schema has not changed
            final Query query = new Query(type.getTypeName());
            query.setFilter(Filter.INCLUDE);
            granuleCatalog.removeGranules(query);
        }
    }

    /**
     * Remove a store for the coverage related to this {@link RasterManager}
     *
     * @param forceDelete
     *
     * @param indexSchema
     * @throws IOException
     */
    public void removeStore(String typeName, boolean forceDelete, boolean checkForReferences)
            throws IOException {
        Utilities.ensureNonNull("typeName", typeName);
        if (typeName != null) {
            // Preliminar granules removal...
            // Should we send a message instead reporting that the catalog
            // still contain some granules before allowing for a removal??
            final Query query = new Query(typeName);
            query.setFilter(Filter.INCLUDE);

            // cleaning up granules and underlying readers
            cleanupGranules(query, checkForReferences, forceDelete);

            // removing records from the catalog
            granuleCatalog.removeGranules(query);
            granuleCatalog.removeType(typeName);
        }
    }

    /**
     * Delete granules from query.
     *
     * @param query
     * @param checkForReferences
     * @throws IOException
     */
    private void cleanupGranules(Query query, boolean checkForReferences, boolean deleteData)
            throws IOException {
        final SimpleFeatureCollection collection = granuleCatalog.getGranules(query);
        UniqueVisitor visitor = new UniqueVisitor(parentReader.locationAttributeName);
        collection.accepts(visitor, null);
        Set<String> features = visitor.getUnique();
        final String coverageName = query.getTypeName();

        for (String feature : features) {
            final URL rasterPath = pathType.resolvePath(
                    DataUtilities.fileToURL(parentReader.parentDirectory).toString(), feature);
            boolean delete = true;
            if (checkForReferences) {
                delete = !checkForReferences(coverageName);

            }
            AbstractGridFormat format = GridFormatFinder.findFormat(rasterPath, excludeMosaicHints);
            if (format != null) {
                GridCoverage2DReader coverageReader = null;
                try {
                    coverageReader = format.getReader(rasterPath, hints);
                    if (coverageReader instanceof StructuredGridCoverage2DReader) {
                        StructuredGridCoverage2DReader reader = (StructuredGridCoverage2DReader) coverageReader;
                        if (delete) {
                            reader.delete(deleteData);
                        } else {
                            reader.removeCoverage(coverageName, false);
                        }
                    } else if (deleteData) {
                        final boolean removed = FileUtils
                                .deleteQuietly(DataUtilities.urlToFile(rasterPath));
                    }
                } finally {
                    if (coverageReader != null) {
                        try {
                            coverageReader.dispose();
                        } catch (Throwable t) {
                            // Ignoring exceptions on disposing readers
                        }
                    }
                }
            }
        }
    }

    /**
     * Check if there is any granule referred by other coverages.
     *
     * @param coverageName
     * @return
     * @throws IOException
     */
    private boolean checkForReferences(String coverageName) throws IOException {
        final String[] coverageNames = parentReader.getGridCoverageNames();
        for (String typeName : coverageNames) {
            if (!coverageName.equalsIgnoreCase(typeName)) {
                Query query = new Query(typeName);
                final SimpleFeatureCollection collection = granuleCatalog.getGranules(query);
                UniqueVisitor visitor = new UniqueVisitor(parentReader.locationAttributeName);
                collection.accepts(visitor, null);
                Set<String> features = visitor.getUnique();
                if (features.size() > 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public GranuleSource getGranuleSource(final boolean readOnly, final Hints hints) {
        synchronized (this) {
            if (readOnly) {
                if (granuleSource == null) {
                    granuleSource = new GranuleCatalogSource(granuleCatalog, typeName, hints);
                }
                return granuleSource;
            } else {
                if (granuleStore == null) {
                    granuleStore = new GranuleCatalogStore(this, granuleCatalog, typeName, hints);
                }
                return granuleStore;
            }
        }
    }

    public List<DimensionDescriptor> getDimensionDescriptors() {
        return dimensionDescriptors;
    }

    public MosaicConfigurationBean getConfiguration() {
        return configuration;
    }

    public void setConfiguration(MosaicConfigurationBean configuration) {
        this.configuration = configuration;
    }

    public void dispose() {
        synchronized (this) {
            try {
                if (granuleCatalog != null) {
                    this.granuleCatalog.dispose();
                }
            } catch (Exception e) {
                if (LOGGER.isLoggable(Level.FINE))
                    LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
            } finally {
                if (granuleSource != null) {
                    granuleSource = null;
                }
                if (granuleStore != null) {
                    granuleStore = null;
                }
                if (granuleCatalog != null) {
                    granuleCatalog = null;
                }
            }
        }
    }

    public void initialize(final boolean checkDomains) throws IOException {
        final BoundingBox bounds = granuleCatalog.getBounds(typeName);
        if (checkDomains) {
            initDomains(configuration);
        }

        // we might have an imposed bbox
        CoordinateReferenceSystem crs = bounds.getCoordinateReferenceSystem();
        GeneralEnvelope originalEnvelope = null;

        if (imposedEnvelope == null) {
            originalEnvelope = new GeneralEnvelope(bounds);
        } else {
            originalEnvelope = new GeneralEnvelope(imposedEnvelope);
            originalEnvelope.setCoordinateReferenceSystem(crs);
        }

        // original gridrange (estimated). I am using the floor here in order to make sure
        // we always stays inside the real area that we have for the granule
        OverviewLevel highResOvLevel = overviewsController.resolutionsLevels.get(0);
        final double highestRes[] = new double[] { highResOvLevel.resolutionX,
                highResOvLevel.resolutionY };
        GridEnvelope2D originalGridRange = new GridEnvelope2D(
                new Rectangle((int) (originalEnvelope.getSpan(0) / highestRes[0]),
                        (int) (originalEnvelope.getSpan(1) / highestRes[1])));
        AffineTransform2D raster2Model = new AffineTransform2D(highestRes[0], 0, 0, -highestRes[1],
                originalEnvelope.getLowerCorner().getOrdinate(0) + 0.5 * highestRes[0],
                originalEnvelope.getUpperCorner().getOrdinate(1) - 0.5 * highestRes[1]);

        try {
            spatialDomainManager = new SpatialDomainManager(originalEnvelope, originalGridRange,
                    crs, raster2Model, overviewsController);
        } catch (TransformException e) {
            throw new IOException("Exception occurred while initializing the SpatialDomainManager",
                    e);
        } catch (FactoryException e) {
            throw new IOException("Exception occurred while initializing the SpatialDomainManager",
                    e);
        }
    }

    /**
     * Return the metadataNames for this manager
     * 
     * @return
     */
    String[] getMetadataNames() {
        final List<String> metadataNames = new ArrayList<String>();
        metadataNames.add(GridCoverage2DReader.TIME_DOMAIN);
        metadataNames.add(GridCoverage2DReader.HAS_TIME_DOMAIN);
        metadataNames.add(GridCoverage2DReader.TIME_DOMAIN_MINIMUM);
        metadataNames.add(GridCoverage2DReader.TIME_DOMAIN_MAXIMUM);
        metadataNames.add(GridCoverage2DReader.TIME_DOMAIN_RESOLUTION);
        metadataNames.add(GridCoverage2DReader.TIME_DOMAIN + DomainDescriptor.DATATYPE_SUFFIX);

        metadataNames.add(GridCoverage2DReader.ELEVATION_DOMAIN);
        metadataNames.add(GridCoverage2DReader.ELEVATION_DOMAIN_MINIMUM);
        metadataNames.add(GridCoverage2DReader.ELEVATION_DOMAIN_MAXIMUM);
        metadataNames.add(GridCoverage2DReader.HAS_ELEVATION_DOMAIN);
        metadataNames.add(GridCoverage2DReader.ELEVATION_DOMAIN_RESOLUTION);
        metadataNames.add(GridCoverage2DReader.ELEVATION_DOMAIN + DomainDescriptor.DATATYPE_SUFFIX);

        if (domainsManager != null) {
            metadataNames.addAll(domainsManager.getMetadataNames());
        }
        return metadataNames.toArray(new String[metadataNames.size()]);
    }

    public ColorModel getDefaultCM() {
        return defaultCM;
    }

    /**
     * Return the metadata value for the specified metadata name
     *
     * @param name the name of the metadata to be returned
     * @return
     */
    String getMetadataValue(String name) {
        String value = null;
        final boolean hasTimeDomain = timeDomainManager != null;
        final boolean hasElevationDomain = elevationDomainManager != null;

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
                return timeDomainManager.getMetadataValue(name);
            }
            if ((name.equalsIgnoreCase("time_domain_minimum")
                    || name.equalsIgnoreCase("time_domain_maximum"))) {
                return timeDomainManager.getMetadataValue(name);
            }
            if (name.equalsIgnoreCase("time_domain_datatype")) {
                return timeDomainManager.getMetadataValue(name);
            }
        }

        if (hasElevationDomain) {
            if (name.equalsIgnoreCase("elevation_domain")) {
                return elevationDomainManager.getMetadataValue(name);
            }

            if (name.equalsIgnoreCase("elevation_domain_minimum")
                    || name.equalsIgnoreCase("elevation_domain_maximum")) {
                return elevationDomainManager.getMetadataValue(name);
            }
            if (name.equalsIgnoreCase("elevation_domain_datatype")) {
                return elevationDomainManager.getMetadataValue(name);
            }
        }

        // check additional domains
        if (domainsManager != null) {
            return domainsManager.getMetadataValue(name);
        }

        //
        return value;
    }

    public byte[][] getDefaultPalette() {
        return defaultPalette;
    }

    public DomainManager getDomainsManager() {
        return domainsManager;
    }

    public boolean isExpandMe() {
        return expandMe;
    }

    public ImageMosaicReader getParentReader() {
        return parentReader;
    }
}
