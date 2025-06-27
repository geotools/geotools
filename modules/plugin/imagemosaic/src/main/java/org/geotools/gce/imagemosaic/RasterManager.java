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

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import it.geosolutions.imageio.pam.PAMDataset;
import it.geosolutions.imageio.pam.PAMParser;
import java.awt.Rectangle;
import java.awt.RenderingHints;
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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.jai.ImageLayout;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.geotools.api.coverage.grid.GridCoverage;
import org.geotools.api.coverage.grid.GridEnvelope;
import org.geotools.api.data.Query;
import org.geotools.api.data.Transaction;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.PropertyIsEqualTo;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.filter.sort.SortOrder;
import org.geotools.api.geometry.BoundingBox;
import org.geotools.api.geometry.Bounds;
import org.geotools.api.metadata.Identifier;
import org.geotools.api.parameter.GeneralParameterValue;
import org.geotools.api.parameter.ParameterDescriptor;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.ReferenceIdentifier;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.datum.PixelInCell;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.referencing.operation.MathTransform2D;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.DecimationPolicy;
import org.geotools.coverage.grid.io.DefaultDimensionDescriptor;
import org.geotools.coverage.grid.io.DimensionDescriptor;
import org.geotools.coverage.grid.io.GranuleSource;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.coverage.grid.io.OverviewPolicy;
import org.geotools.coverage.grid.io.RenamingGranuleSource;
import org.geotools.coverage.grid.io.StructuredGridCoverage2DReader;
import org.geotools.coverage.util.CoverageUtilities;
import org.geotools.coverage.util.FeatureUtilities;
import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
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
import org.geotools.gce.imagemosaic.properties.CRSExtractor;
import org.geotools.geometry.GeneralBounds;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.util.ImageUtilities;
import org.geotools.parameter.DefaultParameterDescriptor;
import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.builder.GridToEnvelopeMapper;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.geotools.referencing.operation.transform.IdentityTransform;
import org.geotools.referencing.operation.transform.ProjectiveTransform;
import org.geotools.renderer.crs.ProjectionHandler;
import org.geotools.renderer.crs.ProjectionHandlerFinder;
import org.geotools.util.URLs;
import org.geotools.util.Utilities;
import org.geotools.util.factory.Hints;

/**
 * @author Simone Giannecchini, GeoSolutions SAS
 * @author Daniele Romagnoli, GeoSolutions SAS
 */
@SuppressWarnings("unchecked")
public class RasterManager implements Cloneable {

    final Hints excludeMosaicHints = new Hints(Utils.EXCLUDE_MOSAIC, true);

    LoadingCache<Integer, Boolean> alternativeCRSCache;

    public static final String ALTERNATIVE_CRS_CACHE_EXPIRATION_SECONDS_KEY =
            "org.geotools.imagemosaic.crscache.expiration.seconds";

    public static final String ALTERNATIVE_CRS_CACHE_SIZE_KEY = "org.geotools.imagemosaic.crscache.size";

    private static final int DEFAULT_ALTERNATIVE_CRS_CACHE_EXPIRATION_SECONDS = 60;

    private static final int DEFAULT_ALTERNATIVE_CRS_CACHE_SIZE = 150;

    private static final Integer ALTERNATIVE_CRS_CACHE_EXPIRATION_SECONDS = Integer.getInteger(
            ALTERNATIVE_CRS_CACHE_EXPIRATION_SECONDS_KEY, DEFAULT_ALTERNATIVE_CRS_CACHE_EXPIRATION_SECONDS);

    private static final Integer ALTERNATIVE_CRS_CACHE_SIZE =
            Integer.getInteger(ALTERNATIVE_CRS_CACHE_SIZE_KEY, DEFAULT_ALTERNATIVE_CRS_CACHE_SIZE);

    private SubmosaicProducerFactory submosaicProducerFactory = new DefaultSubmosaicProducerFactory();

    private PAMDataset pamDataset;

    /**
     * This class is responsible for putting together all the 2D spatial information needed for a certain raster.
     *
     * <p>Notice that when this structure will be extended to work in ND this will become much more complex or as an
     * alternative a sibling TemporalDomainManager will be created.
     *
     * @author Simone Giannecchini, GeoSolutions SAS
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
        GeneralBounds coverageEnvelope = null;

        double[] coverageFullResolution;

        /** WGS84 envelope 2D for this coverage */
        ReferencedEnvelope coverageGeographicBBox;

        CoordinateReferenceSystem coverageGeographicCRS2D;

        MathTransform2D coverageGridToWorld2D;

        /** The base grid range for the coverage */
        Rectangle coverageRasterArea;

        GridEnvelope gridEnvelope;

        public SpatialDomainManager(
                final GeneralBounds envelope,
                final GridEnvelope2D coverageGridrange,
                final CoordinateReferenceSystem crs,
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

        /** Initialize the 2D properties (CRS and Envelope) of this coverage */
        private void prepareCoverageSpatialElements() throws TransformException, FactoryException {
            //
            // basic initialization
            //
            coverageGeographicBBox = ImageUtilities.getWGS84ReferencedEnvelope(coverageEnvelope);
            coverageGeographicCRS2D =
                    coverageGeographicBBox != null ? coverageGeographicBBox.getCoordinateReferenceSystem() : null;

            //
            // Get the original envelope 2d and its spatial reference system
            //
            coverageCRS2D = CRS.getHorizontalCRS(coverageCRS);
            assert coverageCRS2D.getCoordinateSystem().getDimension() == 2;
            if (coverageCRS.getCoordinateSystem().getDimension() != 2) {
                final MathTransform transform = CRS.findMathTransform(coverageCRS, coverageCRS2D);
                final GeneralBounds bbox = CRS.transform(transform, coverageEnvelope);
                coverageBBox = ReferencedEnvelope.create(bbox, coverageCRS2D);
            } else {
                // it is already a bbox
                coverageBBox =
                        ReferencedEnvelope.create(coverageEnvelope, coverageEnvelope.getCoordinateReferenceSystem());
            }
        }

        public MathTransform getOriginalGridToWorld(final PixelInCell pixInCell) {
            synchronized (this) {
                if (coverageGridToWorld2D == null) {
                    final GridToEnvelopeMapper geMapper = new GridToEnvelopeMapper(gridEnvelope, coverageEnvelope);
                    geMapper.setPixelAnchor(PixelInCell.CELL_CENTER);
                    coverageGridToWorld2D = (MathTransform2D) geMapper.createTransform();
                }
            }

            // we do not have to change the pixel datum
            if (pixInCell == PixelInCell.CELL_CENTER) return coverageGridToWorld2D;

            // we do have to change the pixel datum
            if (coverageGridToWorld2D instanceof AffineTransform) {
                final AffineTransform tr = new AffineTransform((AffineTransform) coverageGridToWorld2D);
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
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(RasterManager.class);

    /** The coverage factory producing a {@link GridCoverage} from an image */
    private GridCoverageFactory coverageFactory;

    /**
     * {@link DomainDescriptor} describe a single domain in terms of name and {@link ParameterDescriptor} that can be
     * used to filter values during a read operation.
     *
     * <p>Notice that there is no caching of values for the domain itself right now.
     *
     * <p>The domain must have unique identifiers.
     *
     * @author Simone Giannecchini, GeoSolutions SAS
     */
    public class DomainDescriptor {

        public static final String DOMAIN_SUFFIX = "_DOMAIN";

        static final String HAS_PREFIX = "HAS_";

        static final String DATATYPE_SUFFIX = "_DATATYPE";

        private DomainType domainType = DomainType.SINGLE_VALUE;

        /** Unique identifier for this domain. */
        private final String identifier;

        /**
         * propertyName for this domain that tells me which Property from the underlying catalog provides values for it.
         */
        private final String propertyName;

        /** additionalPropertyName for this domain. It won't be null ONLY in case of ranged domains. */
        private final String additionalPropertyName;

        /** domain dataType */
        private final String dataType;

        /** The {@link ParameterDescriptor} that can be used to filter on this domain during a read operation. */
        private final DefaultParameterDescriptor<List> domainParameterDescriptor;

        /** @return the identifier */
        private String getIdentifier() {
            return identifier;
        }

        public boolean isHasRanges() {
            return additionalPropertyName != null;
        }

        public String getDataType() {
            return dataType;
        }

        /** @return the domainaParameterDescriptor */
        private DefaultParameterDescriptor<List> getDomainParameterDescriptor() {
            return domainParameterDescriptor;
        }

        private DomainDescriptor(
                final String identifier,
                final DomainType domainType,
                final String dataType,
                final String propertyName,
                final String additionalPropertyName) {
            this.identifier = identifier;
            this.propertyName = propertyName;
            this.domainType = domainType;
            this.dataType = dataType;
            this.additionalPropertyName = additionalPropertyName;
            final String name = identifier.toUpperCase();
            this.domainParameterDescriptor = DefaultParameterDescriptor.create(
                    name, "Additional " + identifier + " domain", List.class, null, false);
        }

        @Override
        public String toString() {
            return "DomainDescriptor [identifier="
                    + identifier
                    + ", propertyName="
                    + propertyName
                    + ", dataType="
                    + dataType
                    + ", additionalPropertyName="
                    + (additionalPropertyName != null ? additionalPropertyName : "__UNAVAILABLE__")
                    + "]";
        }

        /**
         * Extract the time domain extrema.
         *
         * @param extrema a {@link String} either TIME_DOMAIN_MAXIMUM or TIME_DOMAIN_MINIMUM.
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

        /** Retrieves the values for this domain */
        private String getValues() {
            if (domainType == DomainType.SINGLE_VALUE) {
                return getSingleValues();
            }
            return getRangeValues();
        }

        /** Retrieves the Range values for this domain */
        private String getRangeValues() {
            try {
                Set<String> result = extractDomain(propertyName, additionalPropertyName, domainType);
                if (result.isEmpty()) {
                    return "";
                }

                final StringBuilder buff = new StringBuilder();
                for (Iterator it = result.iterator(); it.hasNext(); ) {
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

        /** Retrieves the single values list of this domain (no ranges available) */
        private String getSingleValues() {
            try {

                // implicit ordering
                final Set result = new TreeSet<>(extractDomain(propertyName));
                // check result
                if (result.isEmpty()) {
                    return "";
                }

                final StringBuilder buff = new StringBuilder();
                for (Iterator it = result.iterator(); it.hasNext(); ) {
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
         * This method is responsible for creating {@link Filter} that encompasses the provided {@link List} of values
         * for this {@link DomainManager}.
         *
         * @param values the {@link List} of values to use for building the containment {@link Filter}.
         * @return a {@link Filter} that encompasses the provided {@link List} of values for this {@link DomainManager}.
         */
        private Filter createFilter(List values) {
            return new DomainFilterBuilder(identifier, propertyName, additionalPropertyName).createFilter(values);
        }
    }

    /**
     * A {@link DomainManager} class which allows to deal with additional domains (if any) defined inside the mosaic. It
     * provides DOMAIN_ALIAS <--to--> original attribute mapping capabilities, metadata retrieval, filter creation, and
     * domain support check
     *
     * @author Daniele Romagnoli, GeoSolutions SAS.
     */
    public class DomainManager {

        private final Map<String, DomainDescriptor> domainsMap = new HashMap<>();

        private final List<DimensionDescriptor> dimensions = new ArrayList<>();
        private final SimpleFeatureType simpleFeatureType;

        private final boolean attributeHasRange(String attribute) {
            return attribute.contains(Utils.RANGE_SPLITTER_CHAR);
        }

        DomainManager(Map<String, String> additionalDomainAttributes, SimpleFeatureType simpleFeatureType) {
            Utilities.ensureNonNull("additionalDomainAttributes", additionalDomainAttributes);
            Utilities.ensureNonNull("simpleFeatureType", simpleFeatureType);
            this.simpleFeatureType = simpleFeatureType;
            init(additionalDomainAttributes, simpleFeatureType);
        }

        public DimensionDescriptor addDimension(String name, String attribute) {
            List<DimensionDescriptor> descriptors = init(Collections.singletonMap(name, attribute), simpleFeatureType);
            return descriptors.get(0);
        }

        /** */
        private List<DimensionDescriptor> init(
                Map<String, String> domainAttributes, SimpleFeatureType simpleFeatureType)
                throws IllegalArgumentException {
            List<DimensionDescriptor> descriptors = new ArrayList<>();
            for (java.util.Map.Entry<String, String> entry : domainAttributes.entrySet()) {

                DomainType domainType = DomainType.SINGLE_VALUE;
                final String domainName = entry.getKey();
                String propertyName = entry.getValue();
                // is the name equals to the propertyname?
                try {

                    // Domain with ranges management
                    if (attributeHasRange(propertyName)) {
                        domainType = domainAttributes.containsKey(Utils.TIME_DOMAIN)
                                ? DomainType.TIME_RANGE
                                : DomainType.NUMBER_RANGE;
                        descriptors.add(addDomain(domainName, propertyName, domainType, simpleFeatureType));
                        continue;
                    } else {
                        propertyName = extractAttributes(propertyName);
                        if (simpleFeatureType.getDescriptor(propertyName) != null) {
                            // add
                            descriptors.add(addDomain(domainName, propertyName, domainType, simpleFeatureType));
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
                            descriptors.add(addDomain(domainName, propertyName, domainType, simpleFeatureType));

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
                throw new IllegalArgumentException("Unable to add this domain:" + domainName + "-" + propertyName);
            }
            return descriptors;
        }

        /**
         * build an AdditionalDomainManager on top of the provided additionalDomainAttributes (a comma separated list of
         * attribute names).
         */
        DomainManager(String additionalDomainAttributes, SimpleFeatureType simpleFeatureType) {
            Utilities.ensureNonNull("additionalDomainAttributes", additionalDomainAttributes);
            Utilities.ensureNonNull("simpleFeatureType", simpleFeatureType);
            this.simpleFeatureType = simpleFeatureType;

            final Map<String, String> domainPairs = new HashMap<>();

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

        /** @return @TODO We can surely improve it by making use of Regular Expressions */
        private String cleanupDomainName(String domainName) {
            if (attributeHasRange(domainName) || domainName.contains("(") && domainName.contains(")")) {
                // Getting rid of the attributes definition to get only the domain name
                domainName = domainName.substring(0, domainName.indexOf("("));
            }
            return domainName;
        }

        /** Add a domain to the manager */
        private DimensionDescriptor addDomain(
                String name, String propertyName, final DomainType domainType, final SimpleFeatureType featureType) {
            Utilities.ensureNonNull("name", name);
            Utilities.ensureNonNull("propertyName", propertyName);

            // === checks
            // existing!
            if (domainsMap.containsKey(name)) {
                throw new IllegalArgumentException("Trying to add a domain with an existing name" + name);
            }

            // === checks
            // has Ranges
            String basePropertyName = propertyName;
            String additionalPropertyName = null;
            if (domainType != DomainType.SINGLE_VALUE) {

                // Deal with a case like this: time(begin,endtime)
                propertyName = extractAttributes(propertyName);

                // Getting 2 attributes for this domain
                String[] properties = propertyName.split(Utils.RANGE_SPLITTER_CHAR);
                if (properties == null || properties.length != 2) {
                    throw new IllegalArgumentException("Malformed domain with ranges: it should contain 2 attributes");
                }

                basePropertyName = properties[0];
                additionalPropertyName = properties[1];
            }

            // ad with uppercase and with suffix, the parameter that describes it will match this
            final String upperCase = name.toUpperCase();
            final AttributeDescriptor descriptor = featureType.getDescriptor(basePropertyName);
            final String type = descriptor.getType().getBinding().getName();
            domainsMap.put(
                    upperCase + DomainDescriptor.DOMAIN_SUFFIX,
                    new DomainDescriptor(name, domainType, type, basePropertyName, additionalPropertyName));
            return addDimensionDescriptor(name, upperCase, basePropertyName, additionalPropertyName);
        }

        private DimensionDescriptor addDimensionDescriptor(
                String name, String upperCase, String basePropertyName, String additionalPropertyName) {
            final String unitsName = upperCase.equalsIgnoreCase(Utils.TIME_DOMAIN)
                    ? CoverageUtilities.UCUM.TIME_UNITS.getName()
                    : upperCase.equalsIgnoreCase(Utils.ELEVATION_DOMAIN)
                            ? CoverageUtilities.UCUM.ELEVATION_UNITS.getName()
                            : "FIXME"; // TODO: ADD UCUM units Management
            final String unitsSymbol = upperCase.equalsIgnoreCase(Utils.TIME_DOMAIN)
                    ? CoverageUtilities.UCUM.TIME_UNITS.getSymbol()
                    : upperCase.equalsIgnoreCase(Utils.ELEVATION_DOMAIN)
                            ? CoverageUtilities.UCUM.ELEVATION_UNITS.getSymbol()
                            : "FIXME"; // TODO: ADD UCUM units Management
            final DimensionDescriptor dimensionDescriptor = new DefaultDimensionDescriptor(
                    name, unitsName, unitsSymbol, basePropertyName, additionalPropertyName);
            dimensions.add(dimensionDescriptor);
            return dimensionDescriptor;
        }

        private String extractAttributes(String propertyName) {
            if (propertyName.contains("(") && propertyName.contains(")")) {
                // extract the ranges attributes
                propertyName = propertyName
                        .substring(propertyName.indexOf("("))
                        .replace("(", "")
                        .replace(")", "");
            }
            return propertyName;
        }

        /**
         * Check whether a specific parameter (identified by the {@link Identifier} name) is supported by this manager
         * (and therefore, by the reader).
         */
        public boolean isParameterSupported(final Identifier name) {
            if (!domainsMap.isEmpty()) {
                for (DomainDescriptor domain : domainsMap.values()) {
                    final ReferenceIdentifier nameLoc =
                            domain.getDomainParameterDescriptor().getName();
                    if (nameLoc.equals(name)) {
                        return true;
                    }
                }
            }
            return false;
        }

        /** Setup the List of metadataNames for this additional domains manager */
        public List<String> getMetadataNames() {
            final List<String> metadataNames = new ArrayList<>();
            if (!domainsMap.isEmpty()) {
                for (DomainDescriptor domain : domainsMap.values()) {
                    String domainName = domain.getIdentifier().toUpperCase();
                    metadataNames.add(domainName + DomainDescriptor.DOMAIN_SUFFIX);
                    if (domain.getDataType() != null) {
                        metadataNames.add(
                                domainName + DomainDescriptor.DOMAIN_SUFFIX + DomainDescriptor.DATATYPE_SUFFIX);
                    }
                    metadataNames.add(DomainDescriptor.HAS_PREFIX + domainName + DomainDescriptor.DOMAIN_SUFFIX);
                }
            }
            return metadataNames;
        }

        /** Return the value of a specific metadata by parsing the requested name as a Domain Name */
        public String getMetadataValue(String name) {
            Utilities.ensureNonNull("name", name);

            String value = null;
            if (!domainsMap.isEmpty()) {
                // is a domain?
                if (domainsMap.containsKey(name)) {
                    final DomainDescriptor domainDescriptor = domainsMap.get(name);
                    value = domainDescriptor.getValues();
                } else {
                    // is a simple Has domain query?
                    if (name.startsWith(DomainDescriptor.HAS_PREFIX)) {
                        final String substring = name.substring(DomainDescriptor.HAS_PREFIX.length(), name.length());
                        if (domainsMap.containsKey(substring)) {
                            return Boolean.toString(Boolean.TRUE);
                        } else {
                            return Boolean.toString(Boolean.FALSE);
                        }
                    } else if (name.endsWith(DomainDescriptor.DATATYPE_SUFFIX)) {
                        return domainsMap
                                .get(name.substring(0, name.lastIndexOf(DomainDescriptor.DATATYPE_SUFFIX)))
                                .getDataType();
                    } else {
                        // MINUM or MAXIMUM
                        if (name.endsWith("MINIMUM") || name.endsWith("MAXIMUM")) {
                            return domainsMap
                                    .get(name.substring(0, name.lastIndexOf("_")))
                                    .getExtrema(name);
                        }
                    }
                }
            }
            return value;
        }

        /** Setup a Filter on top of the specified domainRequest which is in the form "key=value" */
        public Filter createFilter(String domain, List values) {
            // === checks
            if (domain == null || domain.isEmpty()) {
                throw new IllegalArgumentException("Null domain requested");
            }
            if (values == null || values.isEmpty()) {
                throw new IllegalArgumentException("Null domain values provided");
            }
            if (domainsMap.isEmpty() || !domainsMap.containsKey(domain)) {
                throw new IllegalArgumentException("requested domain is not supported by this mosaic: " + domain);
            }

            // get the property name
            DomainDescriptor domainDescriptor = domainsMap.get(domain);
            return domainDescriptor.createFilter(values);
        }

        /** Return the set of dynamic parameterDescriptors (the ones related to domains) for this reader */
        public Set<ParameterDescriptor<List>> getDynamicParameters() {
            Set<ParameterDescriptor<List>> dynamicParameters = new HashSet<>();
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
        SINGLE_VALUE,
        TIME_RANGE,
        NUMBER_RANGE
    }

    /** Default {@link ColorModel}. */
    ColorModel defaultCM;

    /** Default {@link SampleModel}. */
    SampleModel defaultSM;

    /** Default palette */
    byte[][] defaultPalette;

    /** The name of the input coverage TODO consider URI */
    private String coverageIdentifier;

    /** The hints to be used to produce this coverage */
    private Hints hints;

    OverviewsController overviewsController;

    OverviewPolicy overviewPolicy;

    DecimationPolicy decimationPolicy;

    private PathType pathType;

    boolean expandMe;

    boolean heterogeneousGranules;

    boolean heterogeneousCRS;

    double[][] levels;

    SpatialDomainManager spatialDomainManager;

    ImageLayout defaultImageLayout;

    /** The inner {@link DomainManager} instance which allows to manage custom dimensions */
    DomainManager domainsManager;

    DomainManager elevationDomainManager;

    DomainManager timeDomainManager;

    volatile boolean enableEvents = false; // start disabled

    List<DimensionDescriptor> dimensionDescriptors = new ArrayList<>();

    ImageMosaicReader parentReader;

    GranuleCatalog granuleCatalog;

    GranuleSource granuleSource;

    String typeName;

    String name;

    Bounds imposedEnvelope;

    MosaicConfigurationBean configuration;

    // contains the bands names for this raster
    String[] providedBandsNames = null;

    public RasterManager(final ImageMosaicReader parentReader, MosaicConfigurationBean configuration)
            throws IOException {

        Utilities.ensureNonNull("ImageMosaicReader", parentReader);
        // may as well check this too, since it's being used without a null check
        Utilities.ensureNonNull("MosaicConfigurationBean", configuration);

        this.parentReader = parentReader;
        this.expandMe = configuration.isExpandToRGB();
        boolean checkAuxiliaryMetadata = configuration.isCheckAuxiliaryMetadata();
        this.heterogeneousGranules = configuration.getCatalogConfigurationBean().isHeterogeneous();
        this.heterogeneousCRS = configuration.getCatalogConfigurationBean().isHeterogeneousCRS();
        this.configuration = configuration;
        hints = parentReader.getHints();
        this.name = configuration.getName();
        updateHints(hints, configuration, parentReader);

        if (checkAuxiliaryMetadata) {
            hints.add(new RenderingHints(Utils.CHECK_AUXILIARY_METADATA, checkAuxiliaryMetadata));
        }

        // take ownership of the index : TODO: REMOVE THAT ONCE DEALING WITH MORE
        // CATALOGS/RASTERMANAGERS
        // granuleCatalog = new HintedGranuleCatalog(parentReader.granuleCatalog, hints);
        granuleCatalog = parentReader.granuleCatalog;
        this.coverageFactory = parentReader.getGridCoverageFactory();
        this.coverageIdentifier = configuration.getName();
        pathType = configuration.getCatalogConfigurationBean().getPathType();

        extractOverviewPolicy();
        extractDecimationPolicy();

        // load defaultSM and defaultCM by using the sample_image if it was provided
        loadSampleImage(configuration);
        loadPamDataset(configuration);

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
            defaultImageLayout = new ImageLayout().setColorModel(defaultCM).setSampleModel(defaultSM);
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
            // we have indexer configuration, we can set the submosaic producer factory based off
            // that if it's available
            Indexer indexer = configuration.getIndexer();
            // handling multiple bands dimension if needed
            if (indexer.getMultipleBandsDimensions() != null
                    && indexer.getMultipleBandsDimensions().getMultipleBandsDimension() != null
                    && !indexer.getMultipleBandsDimensions()
                            .getMultipleBandsDimension()
                            .isEmpty()) {
                // we have at least one dimension with multiple bands
                List<MultipleBandsDimension> multipleBandsDimensions =
                        indexer.getMultipleBandsDimensions().getMultipleBandsDimension();
                if (multipleBandsDimensions.size() != 1) {
                    // currently we only support a single dimension with multiple bands
                    throw new IllegalStateException("Only a single dimension with multiple bands is supported.");
                }
                // well we only need to fill the provided bands names
                providedBandsNames =
                        multipleBandsDimensions.get(0).getBandsNames().split("\\s*,\\s*");
            }
            String submosaickerFactory = IndexerUtils.getParameter(Utils.Prop.GRANULE_COLLECTOR_FACTORY, indexer);
            if (submosaickerFactory != null) {
                SubmosaicProducerFactory submosaicProducerFactory =
                        SubmosaicProducerFactoryFinder.getGranuleHandlersSPI().get(submosaickerFactory);
                if (submosaicProducerFactory != null) {
                    this.submosaicProducerFactory = submosaicProducerFactory;
                } else {
                    LOGGER.warning("Found SubmosaicProducerFactory config in the Image Mosaic "
                            + "indexer, however the specified factory ("
                            + submosaickerFactory
                            + ") could not be found. This may mean the indexer.properties or indexer.xml"
                            + "is misconfigured");
                }
            }
        }
        if (heterogeneousCRS) {
            // If the reader is kept open (and the rasterManager doesn't change)
            // it would be useful to cache the result of a query for
            // a specific EPSG code on the index, so that a DB access won't be
            // repeated while the info is in cache.
            CacheLoader<Integer, Boolean> loader = new CacheLoader<>() {

                @Override
                public Boolean load(Integer epsgCode) throws Exception {
                    Query query = new Query(typeName);
                    String crsAttribute = getCrsAttribute();
                    query.setPropertyNames(Arrays.asList(crsAttribute));
                    FilterFactory ff = FeatureUtilities.DEFAULT_FILTER_FACTORY;
                    query.setFilter(ff.equals(ff.property(crsAttribute), ff.literal("EPSG:" + epsgCode)));
                    query.setMaxFeatures(1);
                    SimpleFeature first = DataUtilities.first(granuleCatalog.getGranules(query));
                    return first != null;
                }
            };
            alternativeCRSCache = CacheBuilder.newBuilder()
                    .maximumSize(ALTERNATIVE_CRS_CACHE_SIZE)
                    .expireAfterWrite(ALTERNATIVE_CRS_CACHE_EXPIRATION_SECONDS, TimeUnit.SECONDS)
                    .build(loader);
        }
    }

    private void updateHints(Hints hints, MosaicConfigurationBean configuration, ImageMosaicReader parentReader) {
        if (configuration != null) {
            String auxiliaryFilePath = configuration.getAuxiliaryFilePath();
            String auxiliaryDatastorePath = configuration.getAuxiliaryDatastorePath();
            boolean update = false;
            if (auxiliaryFilePath != null) {
                hints.add(new RenderingHints(Utils.AUXILIARY_FILES_PATH, auxiliaryFilePath));
                update = true;
            }
            if (auxiliaryDatastorePath != null) {
                hints.add(new RenderingHints(Utils.AUXILIARY_DATASTORE_PATH, auxiliaryDatastorePath));
                update = true;
            }
            if (update && !hints.containsKey(Utils.PARENT_DIR)) {
                String parentDir = null;
                if (parentReader.parentDirectory != null) {
                    parentDir = parentReader.parentDirectory.getAbsolutePath();
                } else {
                    Object source = parentReader.getSource();
                    if (source != null && source instanceof File && ((File) source).isDirectory()) {
                        parentDir = ((File) source).getAbsolutePath();
                    }
                }
                if (parentDir != null) hints.add(new RenderingHints(Utils.PARENT_DIR, parentDir));
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
                    final HashMap<String, String> init = new HashMap<>();
                    init.put(Utils.TIME_DOMAIN, timeDomain);
                    timeDomainManager = new DomainManager(init, schema);
                    dimensionDescriptors.addAll(timeDomainManager.dimensions);
                }

                // elevation attribute
                final String elevationAttribute = configuration.getElevationAttribute();
                if (elevationAttribute != null && elevationDomainManager == null) {
                    final HashMap<String, String> init = new HashMap<>();
                    init.put(Utils.ELEVATION_DOMAIN, elevationAttribute);
                    elevationDomainManager = new DomainManager(init, schema);
                    dimensionDescriptors.addAll(elevationDomainManager.dimensions);
                }

                // other well known attributes
                addExtraAttribute(schema, configuration.getCRSAttribute(), Utils.CRS_DOMAIN);
                addExtraAttribute(schema, configuration.getResolutionAttribute(), Utils.RESOLUTION_DOMAIN);
                addExtraAttribute(schema, configuration.getResolutionXAttribute(), Utils.RESOLUTION_X_DOMAIN);
                addExtraAttribute(schema, configuration.getResolutionYAttribute(), Utils.RESOLUTION_Y_DOMAIN);
            }
        }
    }

    private void addExtraAttribute(SimpleFeatureType schema, String attributeName, String domainName) {
        if (attributeName != null) {
            if (domainsManager == null) {
                domainsManager = new DomainManager(Collections.singletonMap(domainName, attributeName), schema);
                dimensionDescriptors.addAll(domainsManager.dimensions);
            } else {
                DimensionDescriptor crsDimension = domainsManager.addDimension(DimensionDescriptor.CRS, attributeName);
                if (!dimensionDescriptors.stream()
                        .anyMatch(dd -> DimensionDescriptor.CRS.equalsIgnoreCase(dd.getName())))
                    dimensionDescriptors.add(crsDimension);
            }
        }
    }

    private void checkTypeName() throws IOException {
        if (typeName == null) {
            URL sourceURL = parentReader.sourceURL;
            if (sourceURL.getPath().endsWith("shp")) {
                typeName = FilenameUtils.getBaseName(URLs.urlToFile(sourceURL).getCanonicalPath());
            } else {
                typeName = configuration.getName();
            }
        }
        if (typeName == null && granuleCatalog != null) {
            String[] typeNames = granuleCatalog.getTypeNames();
            typeName = typeNames != null && typeNames.length > 0 ? typeNames[0] : null;
        }
    }

    /**
     * This code tries to load the PAMDataset for this coverage, assuming there is one, otherwise sets the field to null
     */
    private void loadPamDataset(MosaicConfigurationBean configuration) {
        if (this.parentReader.sourceURL == null) {
            return;
        }

        File pamDatasetFile = getPamDatasetFile(configuration);
        if (pamDatasetFile == null || !pamDatasetFile.exists()) return;

        try {
            PAMParser parser = new PAMParser();
            this.pamDataset = parser.parsePAM(pamDatasetFile);
        } catch (IOException e) {
            LOGGER.warning("Failed to load PAM dataset: " + e);
        }
    }

    private File getPamDatasetFile(MosaicConfigurationBean configuration) {
        final URL baseURL = this.parentReader.sourceURL;
        final File baseFile = URLs.urlToFile(baseURL);
        // in case we do not manage to convert the source URL we leave right awaycd sr
        if (baseFile == null) {
            if (LOGGER.isLoggable(Level.FINE)) LOGGER.fine("Unable to find PAM dataset for path " + baseURL);
            return null;
        }
        String baseName = baseFile.getParent() + "/";
        String fileName = null;
        File pamDatasetFile = null;
        if (configuration != null) {
            String name = configuration.getName();
            if (name != null) {
                fileName = baseName + name + Utils.PAM_DATASET_NAME;
                pamDatasetFile = new File(fileName);
            }
        }

        if (pamDatasetFile == null) {
            pamDatasetFile = new File(baseName + Utils.PAM_DATASET_NAME);
        }
        return pamDatasetFile;
    }

    /**
     * This code tries to load the sample image from which we can extract SM and CM to use when answering to requests
     * that falls within a hole in the mosaic.
     */
    private void loadSampleImage(MosaicConfigurationBean configuration) {
        if (this.parentReader.sourceURL == null) {
            // TODO: I need to define the sampleImage somehow for the ImageMosaicDescriptor case
            return;
        }

        final URL baseURL = this.parentReader.sourceURL;
        final File baseFile = URLs.urlToFile(baseURL);
        // in case we do not manage to convert the source URL we leave right awaycd sr
        if (baseFile == null) {
            if (LOGGER.isLoggable(Level.FINE)) LOGGER.fine("Unable to find sample image for path " + baseURL);
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
                    fileName = baseName + name + Utils.SAMPLE_IMAGE_NAME_LEGACY;
                    sampleImageFile = new File(fileName);
                    if (!sampleImageFile.exists() || !sampleImageFile.canRead()) {
                        sampleImageFile = null;
                    }
                }
            }
        }

        if (sampleImageFile == null) {
            sampleImageFile = new File(baseName + Utils.SAMPLE_IMAGE_NAME);
            if (!sampleImageFile.exists()) {
                sampleImageFile = new File(baseName + Utils.SAMPLE_IMAGE_NAME_LEGACY);
            }
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
            defaultImageLayout = new ImageLayout().setColorModel(defaultCM).setSampleModel(defaultSM);
        } else if (LOGGER.isLoggable(Level.WARNING)) LOGGER.warning("Unable to find sample image for path " + baseURL);
    }

    /**
     * This method is responsible for checking the overview policy as defined by the provided {@link Hints}.
     *
     * @return the overview policy which can be one of {@link OverviewPolicy#IGNORE}, {@link OverviewPolicy#NEAREST},
     *     {@link OverviewPolicy#SPEED}, {@link OverviewPolicy#QUALITY}. Default is {@link OverviewPolicy#NEAREST}.
     */
    private OverviewPolicy extractOverviewPolicy() {

        // check if a policy was provided using hints (check even the
        // deprecated one)
        overviewPolicy = (OverviewPolicy) Utils.getHintIfAvailable(this.hints, Hints.OVERVIEW_POLICY);

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
     * @return the decimation policy which can be one of {@link DecimationPolicy#ALLOW},
     *     {@link DecimationPolicy#DISALLOW}. Default is {@link DecimationPolicy#ALLOW}.
     */
    private DecimationPolicy extractDecimationPolicy() {
        decimationPolicy = (DecimationPolicy) Utils.getHintIfAvailable(this.hints, Hints.DECIMATION_POLICY);

        // use default if not provided. Default is allow
        if (decimationPolicy == null) {
            decimationPolicy = DecimationPolicy.getDefaultPolicy();
        }
        assert decimationPolicy != null;
        return decimationPolicy;
    }

    public Collection<GridCoverage2D> read(final GeneralParameterValue[] params) throws IOException {

        // create a request
        final RasterLayerRequest request = new RasterLayerRequest(params, this);
        if (request.isEmpty()) {
            if (LOGGER.isLoggable(Level.FINE)) LOGGER.log(Level.FINE, "Request is empty: " + request.toString());
            return Collections.emptyList();
        }

        // create a response for the provided request
        final RasterLayerResponse response = new RasterLayerResponse(request, this, this.submosaicProducerFactory);

        // execute the request
        final GridCoverage2D elem = response.createResponse();
        if (elem != null) {
            return Collections.singletonList(elem);
        }
        return Collections.emptyList();
    }

    void getGranuleDescriptors(final Query q, final GranuleCatalogVisitor visitor) throws IOException {
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

    /** */
    FeatureCalc createExtremaQuery(String metadataName, String attributeName) throws IOException {
        final Query query = new Query(typeName);
        query.setPropertyNames(Arrays.asList(attributeName));

        final FeatureCalc visitor = metadataName.toLowerCase().endsWith("maximum")
                ? new MaxVisitor(attributeName)
                : new MinVisitor(attributeName);
        granuleCatalog.computeAggregateFunction(query, visitor);
        return visitor;
    }

    /**
     * Extract the domain of a dimension as a set of unique values.
     *
     * <p>It retrieves a comma separated list of values as a Set of {@link String}.
     *
     * @return a comma separated list of values as a {@link String}.
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
     * <p>It retrieves a comma separated list of values as a Set of {@link String}.
     *
     * @return a comma separated list of values as a Set of {@link String}.
     */
    private Set extractDomain(final String attribute, final String secondAttribute, final DomainType domainType)
            throws IOException {
        final Query query = new Query(typeName);

        final PropertyName propertyName = FeatureUtilities.DEFAULT_FILTER_FACTORY.property(attribute);
        query.setPropertyNames(Arrays.asList(attribute, secondAttribute));

        final SortByImpl[] sb = {new SortByImpl(propertyName, SortOrder.ASCENDING)};
        // Checking whether it supports sorting capabilities
        if (granuleCatalog.getQueryCapabilities(typeName).supportsSorting(sb)) {
            query.setSortBy(sb);
        } else {
            LOGGER.severe("Sorting parameter ignored, underlying datastore cannot sort on " + Arrays.toString(sb));
        }

        final FeatureCalc visitor = domainType == DomainType.TIME_RANGE
                ? new DateRangeVisitor(attribute, secondAttribute)
                : new RangeVisitor(attribute, secondAttribute);
        granuleCatalog.computeAggregateFunction(query, visitor);
        return domainType == DomainType.TIME_RANGE
                ? ((DateRangeVisitor) visitor).getRange()
                : ((RangeVisitor) visitor).getRange();
    }

    /**
     * Checks if this EPSG code matches at least one feature. Since this method may perform a database query to find
     * out, it's important to call it last while performing conditionals, leaving cheaper tests first.
     */
    public boolean hasAlternativeCRS(Integer epsgCode) throws IOException {
        try {
            return epsgCode != null && heterogeneousCRS ? alternativeCRSCache.get(epsgCode) : false;
        } catch (ExecutionException e) {
            throw new IOException("Exception Occurred while checking for alternative CRS:" + epsgCode, e);
        }
    }

    /** TODO this should not leak through */
    public GranuleCatalog getGranuleCatalog() {
        return granuleCatalog;
    }

    /** Create a store for the coverage related to this {@link RasterManager} using the provided schema */
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
            granuleCatalog.removeGranules(query, Transaction.AUTO_COMMIT);
        }
    }

    /** Remove a store for the coverage related to this {@link RasterManager} */
    public void removeStore(String typeName, boolean forceDelete, boolean checkForReferences) throws IOException {
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
            granuleCatalog.removeGranules(query, Transaction.AUTO_COMMIT);
            granuleCatalog.removeType(typeName);
        }
        if (alternativeCRSCache != null) {
            alternativeCRSCache.invalidateAll();
        }
    }

    /** Delete granules from query. */
    private void cleanupGranules(Query query, boolean checkForReferences, boolean deleteData) throws IOException {
        final SimpleFeatureCollection collection = granuleCatalog.getGranules(query);
        UniqueVisitor visitor = new UniqueVisitor(parentReader.locationAttributeName);
        collection.accepts(visitor, null);
        Set<String> features = visitor.getUnique();
        final String coverageName = query.getTypeName();

        for (String feature : features) {
            final URL rasterPath = pathType.resolvePath(
                    URLs.fileToUrl(parentReader.parentDirectory).toString(), feature);
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
                        FileUtils.deleteQuietly(URLs.urlToFile(rasterPath));
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
        if (alternativeCRSCache != null) {
            alternativeCRSCache.invalidateAll();
        }
    }

    /** Check if there is any granule referred by other coverages. */
    private boolean checkForReferences(String coverageName) throws IOException {
        final String[] coverageNames = parentReader.getGridCoverageNames();
        for (String typeName : coverageNames) {
            if (!coverageName.equalsIgnoreCase(typeName)) {
                Query query = new Query(typeName);
                final SimpleFeatureCollection collection = granuleCatalog.getGranules(query);
                UniqueVisitor visitor = new UniqueVisitor(parentReader.locationAttributeName);
                collection.accepts(visitor, null);
                Set<String> features = visitor.getUnique();
                if (!features.isEmpty()) {
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
                    granuleSource = new GranuleCatalogSource(this, granuleCatalog, typeName, hints);
                    if (!typeName.equalsIgnoreCase(name)) {
                        // need to rename
                        granuleSource = new RenamingGranuleSource(name, granuleSource);
                    }
                }
                return granuleSource;
            } else {
                // stateful (holds transaction), do not cache
                return new GranuleCatalogStore(this, granuleCatalog, typeName, hints);
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
                if (alternativeCRSCache != null) {
                    alternativeCRSCache.invalidateAll();
                }
            } catch (Exception e) {
                if (LOGGER.isLoggable(Level.FINE)) LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
            } finally {
                if (granuleSource != null) {
                    granuleSource = null;
                }
                if (granuleCatalog != null) {
                    granuleCatalog = null;
                }
            }
        }
    }

    public void initialize(final boolean checkDomains) throws IOException {
        initialize(checkDomains, Transaction.AUTO_COMMIT);
    }

    public void initialize(final boolean checkDomains, Transaction transaction) throws IOException {
        final BoundingBox bounds = granuleCatalog.getBounds(typeName, transaction);
        if (checkDomains) {
            initDomains(configuration);
        }

        // we might have an imposed bbox
        CoordinateReferenceSystem crs = bounds.getCoordinateReferenceSystem();
        GeneralBounds originalEnvelope = null;

        if (imposedEnvelope == null) {
            originalEnvelope = new GeneralBounds(bounds);
        } else {
            originalEnvelope = new GeneralBounds(imposedEnvelope);
            originalEnvelope.setCoordinateReferenceSystem(crs);
        }

        // original gridrange (estimated). I am using the floor here in order to make sure
        // we always stays inside the real area that we have for the granule
        OverviewLevel highResOvLevel = overviewsController.resolutionsLevels.get(0);
        final double[] highestRes = {highResOvLevel.resolutionX, highResOvLevel.resolutionY};
        GridEnvelope2D originalGridRange =
                new GridEnvelope2D(new Rectangle((int) (originalEnvelope.getSpan(0) / highestRes[0]), (int)
                        (originalEnvelope.getSpan(1) / highestRes[1])));
        AffineTransform2D raster2Model = new AffineTransform2D(
                highestRes[0],
                0,
                0,
                -highestRes[1],
                originalEnvelope.getLowerCorner().getOrdinate(0) + 0.5 * highestRes[0],
                originalEnvelope.getUpperCorner().getOrdinate(1) - 0.5 * highestRes[1]);

        try {
            spatialDomainManager = new SpatialDomainManager(
                    originalEnvelope, originalGridRange, crs, raster2Model, overviewsController);
        } catch (TransformException | FactoryException e) {
            throw new IOException("Exception occurred while initializing the SpatialDomainManager", e);
        }
        if (alternativeCRSCache != null) {
            alternativeCRSCache.invalidateAll();
        }
    }

    /** Return the metadataNames for this manager */
    String[] getMetadataNames() {
        final List<String> metadataNames = new ArrayList<>();
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
        metadataNames.add(AbstractGridCoverage2DReader.MULTICRS_READER);
        return metadataNames.toArray(new String[metadataNames.size()]);
    }

    public ColorModel getDefaultCM() {
        return defaultCM;
    }

    /**
     * Return the metadata value for the specified metadata name
     *
     * @param name the name of the metadata to be returned
     */
    String getMetadataValue(String name) {
        String value = null;
        final boolean hasTimeDomain = timeDomainManager != null;
        final boolean hasElevationDomain = elevationDomainManager != null;

        if (name.equalsIgnoreCase(GridCoverage2DReader.HAS_ELEVATION_DOMAIN)) return String.valueOf(hasElevationDomain);

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
            if (name.equalsIgnoreCase("time_domain_minimum") || name.equalsIgnoreCase("time_domain_maximum")) {
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

        // check if heterogeneous CRS
        if (name.equalsIgnoreCase(AbstractGridCoverage2DReader.MULTICRS_READER)) {
            return String.valueOf(heterogeneousCRS);
        }

        if (name.equalsIgnoreCase(AbstractGridCoverage2DReader.MULTICRS_EPSGCODES) && heterogeneousCRS) {

            // Extract the internal EPSG Codes found on the catalog
            String crsAttribute = null;
            try {
                crsAttribute = getCrsAttribute();
                if (crsAttribute != null) {
                    Set<String> crsSet = extractDomain(crsAttribute);
                    for (String crs : crsSet) {
                        // Opportunistic caching:
                        // LoadingCache usually loads objects at first need.
                        // However, since this metadata method is doing a scan
                        // of the available codes when invoked, let's take
                        // advantage of that by putting the values on cache
                        String epsgCode = crs.replaceAll("[^0-9,]", "");
                        alternativeCRSCache.put(Integer.valueOf(epsgCode), true);
                    }
                    return String.join(",", crsSet);
                }
            } catch (IOException e) {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING, "Unable to retrieve the list of supported CRSs", e);
                }
                return "";
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

    /** Builds a RasterManager for the sub mosaic of a given template granule, and within a given search bounds */
    public RasterManager getForGranuleCRS(
            RasterLayerRequest request,
            GranuleDescriptor templateDescriptor,
            ReferencedEnvelope requestBounds,
            ReferencedEnvelope requestBoundsQuery)
            throws Exception {
        CoordinateReferenceSystem granuleCRS =
                templateDescriptor.getGranuleEnvelope().getCoordinateReferenceSystem();
        CoordinateReferenceSystem requestedCRS = requestBounds.getCoordinateReferenceSystem();

        // When no requesting AlternativeCRSOutput, the provided
        // requestBounds and requestBoundsQuery are the same object
        boolean useAlternativeCRS = heterogeneousCRS
                && !requestBounds.equals(requestBoundsQuery)
                && hasAlternativeCRS(CRS.lookupEpsgCode(requestedCRS, false));
        CoordinateReferenceSystem referenceCRS = useAlternativeCRS ? requestedCRS : spatialDomainManager.coverageCRS2D;
        if (CRS.equalsIgnoreMetadata(referenceCRS, granuleCRS)) {
            return this;
        }

        // compute the bounds of the sub-mosaic in that CRS
        ReferencedEnvelope bounds = getBoundsForGranuleCRS(request, templateDescriptor, requestBoundsQuery);
        ReferencedEnvelope targetBounds = reprojectBounds(requestBounds, granuleCRS, bounds);

        // rebuild the raster manager
        RasterManager reprojected = (RasterManager) this.clone();
        reprojected.configuration = new MosaicConfigurationBean(this.configuration);
        reprojected.configuration.setCrs(granuleCRS);
        if (useAlternativeCRS) {
            // We are going to produce a submosaic in the requested CRS,
            // so that it won't be handled as heterogeneous anymore.
            reprojected.heterogeneousCRS = false;
        }
        reprojected.configuration.setEnvelope(targetBounds);
        if (reprojected.imposedEnvelope != null) {
            // we might have an imposed bbox
            reprojected.imposedEnvelope = targetBounds;
        }
        if (templateDescriptor.getOverviewsController() != null) {
            reprojected.overviewsController = templateDescriptor.getOverviewsController();
        }

        OverviewLevel level = templateDescriptor.getOverviewsController().getLevel(0);
        // original gridrange (estimated). I am using the floor here in order to make sure
        // we always stays inside the real area that we have for the granule
        final double[] highestRes = {level.resolutionX, level.resolutionY};
        GridEnvelope2D originalGridRange = new GridEnvelope2D(new Rectangle(
                (int) (targetBounds.getSpan(0) / highestRes[0]), (int) (targetBounds.getSpan(1) / highestRes[1])));
        AffineTransform2D raster2Model = new AffineTransform2D(
                highestRes[0],
                0,
                0,
                -highestRes[1],
                targetBounds.getLowerCorner().getOrdinate(0) + 0.5 * highestRes[0],
                targetBounds.getUpperCorner().getOrdinate(1) - 0.5 * highestRes[1]);
        reprojected.spatialDomainManager = new SpatialDomainManager(
                new GeneralBounds(targetBounds),
                originalGridRange,
                granuleCRS,
                raster2Model,
                reprojected.overviewsController);

        return reprojected;
    }

    private ReferencedEnvelope reprojectBounds(
            ReferencedEnvelope referenceBounds, CoordinateReferenceSystem targetCRS, ReferencedEnvelope bounds)
            throws FactoryException, TransformException {
        ProjectionHandler ph = ProjectionHandlerFinder.getHandler(referenceBounds, targetCRS, true);
        ReferencedEnvelope targetBounds = null;
        if (ph != null) {
            List<ReferencedEnvelope> queryEnvelopes = ph.getQueryEnvelopes();
            for (ReferencedEnvelope envelope : queryEnvelopes) {
                ReferencedEnvelope transformed = envelope.transform(targetCRS, true);
                if (targetBounds == null) {
                    targetBounds = transformed;
                } else {
                    targetBounds.expandToInclude(transformed);
                }
            }
        } else {
            targetBounds = bounds.transform(targetCRS, true);
        }
        return targetBounds;
    }

    /**
     * Grab the bounds of the mosaic granules in the template granule CRS (cannot reproject, in general, the whole
     * mosaic bounds in the granule local CRS)
     */
    private ReferencedEnvelope getBoundsForGranuleCRS(
            RasterLayerRequest request, GranuleDescriptor templateDescriptor, ReferencedEnvelope requestBounds)
            throws IOException, FactoryException, TransformException {

        String crsAttribute = getCrsAttribute();
        if (crsAttribute == null) {
            throw new IllegalStateException("Invalid heterogeneous mosaic configuration, "
                    + "the 'crs' property is missing from the index schema");
        }

        Object granuleCRSCode = Utils.getAttribute(templateDescriptor.getOriginator(), crsAttribute);
        FilterFactory ff = FeatureUtilities.DEFAULT_FILTER_FACTORY;
        PropertyIsEqualTo crsFilter = ff.equal(ff.property(crsAttribute), ff.literal(granuleCRSCode), false);

        GranuleSource granuleSource = getGranuleSource(true, null);
        MosaicQueryBuilder builder = new MosaicQueryBuilder(request, requestBounds);
        Query q = builder.build();
        q.setFilter(ff.and(q.getFilter(), crsFilter));
        SimpleFeatureCollection granules = granuleSource.getGranules(q);
        ReferencedEnvelope bounds = granules.getBounds();
        return bounds;
    }

    /**
     * Returns the name of the crs attribute in heterogeneous mosaics (for non-heterogenous ones, it will return null)
     */
    public String getCrsAttribute() throws IOException {
        String crsAttribute = configuration.getCRSAttribute();
        if (crsAttribute == null) {
            crsAttribute = CRSExtractor.DEFAULT_ATTRIBUTE_NAME;
        }

        GranuleSource granuleSource = getGranuleSource(true, null);
        if (granuleSource.getSchema().getDescriptor(crsAttribute) == null) {
            if (heterogeneousCRS)
                throw new IllegalStateException("Invalid heterogeneous mosaic configuration, "
                        + "the 'crs' property is missing from the index schema");
            return null;
        }

        return crsAttribute;
    }

    /** The parent directory that can be used with the {@link PathType} enumeration */
    public String getParentLocation() {
        return URLs.fileToUrl(getParentReader().parentDirectory).toString();
    }

    /** The attribute containing the location information for the single granules */
    public String getLocationAttribute() {
        return getParentReader().locationAttributeName;
    }

    /** Returns the coverage name */
    public String getName() {
        return name;
    }

    /** Returns the PAM dataset for this coverage, if one is available */
    public PAMDataset getPamDataset() {
        return pamDataset;
    }

    /**
     * Recomputes the summary PAM databaset
     *
     * @throws IOException
     */
    public void reloadPamDataset() throws IOException {
        // TODO: PAM collection works only on local files, eventually we'll want to
        // make it work also on remote ones
        Query query = new Query(typeName);
        SimpleFeatureCollection granules = getGranuleCatalog().getGranules(query);
        File pamDatasetFile = getPamDatasetFile(configuration);
        RATCollectorListener ratCollector = new RATCollectorListener(pamDatasetFile);
        GranuleDescriptor.PathResolver pathResolver = new GranuleDescriptor.PathResolver(pathType, getParentLocation());
        try (SimpleFeatureIterator it = granules.features()) {
            while (it.hasNext()) {
                SimpleFeature feature = it.next();
                String location = (String) feature.getAttribute(getLocationAttribute());
                if (location != null) {
                    URL resolved = pathResolver.resolve(location);
                    if (resolved != null) {
                        File file = URLs.urlToFile(resolved);
                        if (file.exists()) {
                            ratCollector.collectRAT(file);
                        }
                    }
                }
            }
        }
        ratCollector.generateMosaicRAT();
        loadPamDataset(configuration);
    }
}
