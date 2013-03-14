/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.awt.Rectangle;
import java.awt.image.ColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.io.File;
import java.io.IOException;
import java.lang.ref.SoftReference;
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

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.io.DecimationPolicy;
import org.geotools.coverage.grid.io.OverviewPolicy;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataUtilities;
import org.geotools.data.Query;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.Hints;
import org.geotools.feature.visitor.CalcResult;
import org.geotools.feature.visitor.FeatureCalc;
import org.geotools.feature.visitor.MaxVisitor;
import org.geotools.feature.visitor.MinVisitor;
import org.geotools.feature.visitor.UniqueVisitor;
import org.geotools.filter.SortByImpl;
import org.geotools.gce.imagemosaic.OverviewsController.OverviewLevel;
import org.geotools.gce.imagemosaic.catalog.GranuleCatalog;
import org.geotools.gce.imagemosaic.catalog.GranuleCatalogVisitor;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.parameter.DefaultParameterDescriptor;
import org.geotools.referencing.CRS;
import org.geotools.resources.coverage.FeatureUtilities;
import org.geotools.resources.image.ImageUtilities;
import org.geotools.util.Range;
import org.geotools.util.Utilities;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.sort.SortOrder;
import org.opengis.geometry.BoundingBox;
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
 *
 */
@SuppressWarnings({"rawtypes","unchecked"})
class RasterManager {
    
	/** Logger. */
	private final static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(RasterManager.class);
	
	/**
	 * This class is responsible for putting together all the 2D spatial information needed for a certain raster.
	 * 
	 * <p>
	 * Notice that when this structure will be extended to work in ND this will become much more complex or as an 
	 * alternative a sibling TemporalDomainManager will be created.
	 * 
	 * @author Simone Giannecchini, GeoSolutions SAS
	 *
	 */
	static class SpatialDomainManager{

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
		 

		public SpatialDomainManager(final GeneralEnvelope envelope,
				final GridEnvelope2D coverageGridrange,
				final CoordinateReferenceSystem crs,
				final MathTransform coverageGridToWorld2D,
				final OverviewsController overviewsController) throws TransformException, FactoryException {
		    this.coverageEnvelope = envelope.clone();
		    this.coverageRasterArea =coverageGridrange.clone();
		    this.coverageCRS = crs;
		    this.coverageGridToWorld2D = (MathTransform2D) coverageGridToWorld2D;
		    this.coverageFullResolution = new double[2];
		    final OverviewLevel highestLevel= overviewsController.resolutionsLevels.get(0);
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
            coverageGeographicCRS2D = coverageGeographicBBox!=null?coverageGeographicBBox.getCoordinateReferenceSystem():null;

            //
            // Get the original envelope 2d and its spatial reference system
            //
            coverageCRS2D = CRS.getHorizontalCRS(coverageCRS);
            assert coverageCRS2D.getCoordinateSystem().getDimension() == 2;
            if (coverageCRS.getCoordinateSystem().getDimension() != 2) {
                final MathTransform transform = CRS.findMathTransform(coverageCRS,
                        (CoordinateReferenceSystem) coverageCRS2D);
                final GeneralEnvelope bbox = CRS.transform(transform, coverageEnvelope);
                bbox.setCoordinateReferenceSystem(coverageCRS2D);
                coverageBBox = new ReferencedEnvelope(bbox);
            } else {
                // it is already a bbox
                coverageBBox = new ReferencedEnvelope(coverageEnvelope);
            }
        }
    }

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
    class DomainDescriptor {

        static final String DOMAIN_SUFFIX = "_DOMAIN";

        static final String HAS_PREFIX = "HAS_";

        static final String TIME_DOMAIN = "TIME";

        static final String ELEVATION_DOMAIN = "ELEVATION";

        private DomainType domainType = DomainType.SINGLE_VALUE;

        /** Unique identifier for this domain. */
        private final String identifier;

        /** propertyName for this domain that tells me which Property from the underlying catalog provides values for it. */
        private final String propertyName;

        /** additionalPropertyName for this domain. It won't be null ONLY in case of ranged domains. */
        private final String additionalPropertyName;

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

        /**
         * @return the domainaParameterDescriptor
         */
        private DefaultParameterDescriptor<List> getDomainaParameterDescriptor() {
            return domainParameterDescriptor;
        }

        private DomainDescriptor(final String identifier, final DomainType domainType, 
                final String propertyName, final String additionalPropertyName) {
            this.identifier = identifier;
            this.propertyName = propertyName;
            this.domainType = domainType;
            this.additionalPropertyName = additionalPropertyName;
            final String name = identifier.toUpperCase();
            this.domainParameterDescriptor=
                    DefaultParameterDescriptor.create(
                            name, 
                            "Additional " + identifier + " domain", 
                            List.class, 
                            null, 
                            false); 
        }
        @Override
        public String toString() {
            return "DomainDescriptor [identifier=" + identifier + ", propertyName=" + propertyName
                    + ", additionalPropertyName=" + (additionalPropertyName != null ? additionalPropertyName : "__UNAVAILABLE__") +  "]";
        }

        /**
         * Extract the time domain extrema.
         * 
         * @param extrema a {@link String} either TIME_DOMAIN_MAXIMUM or TIME_DOMAIN_MINIMUM.
         * 
         * @return either TIME_DOMAIN_MAXIMUM or TIME_DOMAIN_MINIMUM as a {@link String}.
         * TODO use num for extrema
         */
        private String getExtrema(String extrema) {
            try {
                String attribute = propertyName;
                // In case the domain has range, we will check the second element 
                // in case we are looking for the maximum
                if (domainType != DomainType.SINGLE_VALUE && extrema.toLowerCase().endsWith("maximum")) {
                        attribute = additionalPropertyName;
                }
                final FeatureCalc visitor = createExtremaQuery(extrema, attribute);
                
                // check result
                CalcResult tempRes = visitor.getResult();
                if (tempRes == null){
                    throw new IllegalStateException("Unable to compute extrema value:"+extrema);
                }
                final Object result=tempRes.getValue();
                if (result == null){
                    throw new IllegalStateException("Unable to compute extrema value:"+extrema);
                }                
                return ConvertersHack.convert(result, String.class);
            } catch (IOException e) {
                if(LOGGER.isLoggable(Level.WARNING))
                        LOGGER.log(Level.WARNING,"Unable to compute extrema for TIME_DOMAIN",e);
                return null;
            }
        }
        
        /**
         * Retrieves the values for this domain
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
         * @return
         */
        private String getRangeValues() {
            try {
                Set<String> result = extractDomain(propertyName, additionalPropertyName, domainType);
                if (result.size() <= 0){
                    return "";
                }
                
                final StringBuilder buff= new StringBuilder();
                for(Iterator it = result.iterator(); it.hasNext();){
                    buff.append(ConvertersHack.convert(it.next(), String.class));
                    if (it.hasNext()) {
                        buff.append(",");
                    }
                }
                return buff.toString();
            } catch (IOException e) {
                if(LOGGER.isLoggable(Level.WARNING))
                    LOGGER.log(Level.WARNING,"Unable to parse attribute: " + identifier ,e);
            return "";
            }
        }

        /**
         * Retrieves the single values list of this domain (no ranges available)
         * @return
         */
        private String getSingleValues(){
            try {
                
                // implicit ordering
                final Set result = new TreeSet(extractDomain(propertyName));          
                // check result
                if (result.size() <= 0){
                    return "";
                }
                
                final StringBuilder buff= new StringBuilder();
                for(Iterator it = result.iterator(); it.hasNext();){
                    buff.append(ConvertersHack.convert(it.next(), String.class));
                    if (it.hasNext()) {
                        buff.append(",");
                    }
                }
                return buff.toString();
            } catch (IOException e) {
                if(LOGGER.isLoggable(Level.WARNING))
                        LOGGER.log(Level.WARNING,"Unable to parse attribute: " + identifier ,e);
                return "";
            }
        }
    
        /**
         * This method is responsible for creating {@link Filter} that encompasses the
         * provided {@link List} of values for this {@link DomainManager}.
         * 
         * @param values the {@link List} of values to use for building the containment {@link Filter}.
         * @return a {@link Filter} that encompasses the
         * provided {@link List} of values for this {@link DomainManager}.
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
                    if(value instanceof Range){
                        // RANGE                        
                        final Range range= (Range)value;
                        filters.add( 
                                ff.and(
                                        ff.lessOrEqual(
                                                ff.property(propertyName), 
                                                ff.literal(range.getMaxValue())),
                                        ff.greaterOrEqual(
                                                ff.property(propertyName), 
                                                ff.literal(range.getMinValue()))
                                ));
                    }  else {
                        // SINGLE value
                        filters.add( 
                                ff.equal(
                                        ff.property(propertyName),
                                        ff.literal(value),true)
                                    );
                    }
                } else { //domainType == DomainType.RANGE
                    // Domain made of ranges such as (beginTime,endTime) , (beginElevation,endElevation) , ...
                    if(value instanceof Range){
                        // RANGE                        
                        final Range range= (Range)value;
                        final Comparable maxValue = range.getMaxValue();
                        final Comparable minValue = range.getMinValue();
                        if(maxValue.compareTo(minValue)!=0){
                            // logic comes from Range.intersectsNC(Range)
                            // in summary, requestedMax > min && requestedMin < max
                            Filter maxCondition = ff.greaterOrEqual(
                                                        ff.literal(maxValue), 
                                                        ff.property(propertyName));
                            Filter minCondition = ff.lessOrEqual(
                                                        ff.literal(minValue), 
                                                        ff.property(additionalPropertyName));
                            
                            filters.add(ff.and(Arrays.asList(maxCondition,minCondition)));
                            continue;
                        } else {
                            value=maxValue;
                        }
                    }
                    filters.add( 
                            ff.and(
                                    ff.lessOrEqual(
                                            ff.property(propertyName), 
                                            ff.literal(value)),
                                    ff.greaterOrEqual(
                                            ff.property(additionalPropertyName), 
                                            ff.literal(value))));
                }
            }
            return ff.or(filters);
        }
    }

    /**
     * An {@link DomainManager} class which allows to deal with additional domains
     * (if any) defined inside the mosaic. It provides DOMAIN_ALIAS <--to--> original attribute mapping
     * capabilities, metadata retrieval, filter creation, and domain support check
     * 
     * @author Daniele Romagnoli, GeoSolutions SAS.
     */
    class DomainManager {

        private final Map<String, DomainDescriptor> domainsMap = new HashMap<String, DomainDescriptor>();

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
                        domainType = domainAttributes.containsKey(DomainDescriptor.TIME_DOMAIN) ? DomainType.TIME_RANGE
                                : DomainType.NUMBER_RANGE;
                        addDomain(domainName, propertyName, domainType);
                        continue;
                    } else if (simpleFeatureType.getDescriptor(propertyName) != null) {
                        // add
                        addDomain(domainName, propertyName, domainType);

                        // continue
                        continue;
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
                            addDomain(domainName, propertyName, domainType);

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
                throw new IllegalArgumentException("Unable to add this domain:" + domainName + "-"
                        + propertyName);

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
            if (attributeHasRange(domainName) && domainName.contains("(")
                    && domainName.contains(")")) {
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
         */
        private void addDomain(String name, String propertyName, final DomainType domainType) {
            Utilities.ensureNonNull("name", name);
            Utilities.ensureNonNull("propertyName", propertyName);

            // === checks
            // existing!
            if (domainsMap.containsKey(name)) {
                throw new IllegalArgumentException("Trying to add a domain with an existing name"
                        + name);
            }

            // === checks
            // has Ranges
            String basePropertyName = propertyName;
            String additionalPropertyName = null;
            if (domainType != DomainType.SINGLE_VALUE) {

                // Deal with a case like this: time(begin,endtime)
                if (propertyName.contains("(") && propertyName.contains(")")) {
                    // extract the ranges attributes
                    propertyName = propertyName.substring(propertyName.indexOf("("))
                            .replace("(", "").replace(")", "");
                }

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
            domainsMap.put(upperCase + DomainDescriptor.DOMAIN_SUFFIX, new DomainDescriptor(name,
                    domainType, basePropertyName, additionalPropertyName));
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
                    final ReferenceIdentifier nameLoc = domain.getDomainaParameterDescriptor()
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
                        final String substring = name.substring(
                                DomainDescriptor.HAS_PREFIX.length(), name.length());
                        if (domainsMap.containsKey(substring)) {
                            return Boolean.toString(Boolean.TRUE);
                        } else {
                            return Boolean.toString(Boolean.FALSE);
                        }
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
                    dynamicParameters.add(domain.getDomainaParameterDescriptor());
                }
            }
            // return
            return dynamicParameters;
        }

    }

    enum DomainType {
         SINGLE_VALUE, TIME_RANGE, NUMBER_RANGE
     }

    /** Default {@link ColorModel}.*/
	ColorModel defaultCM;
	
	/** Default {@link SampleModel}.*/
	SampleModel defaultSM;
	
	/** The coverage factory producing a {@link GridCoverage} from an image */
	private GridCoverageFactory coverageFactory;

	/** The name of the input coverage 
	 * TODO consider URI
	 */
	private String coverageIdentifier;

	
	/** The hints to be used to produce this coverage */
	private Hints hints;
	OverviewsController overviewsController;
	OverviewPolicy overviewPolicy;
	DecimationPolicy decimationPolicy;
	ImageMosaicReader parent;
	private PathType pathType;
	boolean expandMe;
	boolean heterogeneousGranules;
	SpatialDomainManager spatialDomainManager;

	/** {@link SoftReference} to the index holding the tiles' envelopes. */
	final GranuleCatalog granuleCatalog;
	
	ImageLayout defaultImageLayout;

    /** The inner {@link DomainManager} instance which allows to manage custom dimensions */
    DomainManager domainsManager;

    DomainManager elevationDomainManager;

    DomainManager timeDomainManager;

    public RasterManager(final ImageMosaicReader reader) throws IOException{
        this(reader, null);
    }
	public RasterManager(final ImageMosaicReader reader, MosaicConfigurationBean configuration) throws IOException {
		
		Utilities.ensureNonNull("ImageMosaicReader", reader);
		
		this.parent=reader;
		this.expandMe=parent.expandMe;
		this.heterogeneousGranules = parent.heterogeneousGranules;
        
        //take ownership of the index
        granuleCatalog = parent.catalog;
        parent.catalog = null;
        
        coverageIdentifier=reader.getName();
        hints = reader.getHints();
        this.coverageIdentifier =reader.getName();
        this.coverageFactory = reader.getGridCoverageFactory();
        this.pathType=parent.pathType;
        
        //resolution values
        
        //instantiating controller for subsampling and overviews
        overviewsController=new OverviewsController(
        		reader.getHighestRes(),
        		reader.getNumberOfOvervies(),
        		reader.getOverviewsResolution());
        try {
			spatialDomainManager= new SpatialDomainManager(
					reader.getOriginalEnvelope(),
					(GridEnvelope2D)reader.getOriginalGridRange(),
					reader.getCrs(),
					reader.getOriginalGridToWorld(PixelInCell.CELL_CENTER),
					overviewsController);
		} catch (TransformException e) {
			throw new DataSourceException(e);
		} catch (FactoryException e) {
			throw new DataSourceException(e);
		}
        extractOverviewPolicy();
        extractDecimationPolicy();
        
        // load defaultSM and defaultCM by using the sample_image if it was provided
        loadSampleImage();   
        
        if(configuration!=null){
            final SimpleFeatureType schema = granuleCatalog.getType();
            if (configuration.getAdditionalDomainAttributes() != null) {
                domainsManager = new DomainManager(configuration.getAdditionalDomainAttributes(),schema);
            }
            
            // time attribute
            if(configuration.getTimeAttribute()!=null){
                final HashMap<String, String> init=new HashMap<String, String>();
                init.put(DomainDescriptor.TIME_DOMAIN, configuration.getTimeAttribute());
                timeDomainManager= new DomainManager(init,schema);
            }
            // elevation attribute
            if(configuration.getElevationAttribute()!=null){
                final HashMap<String, String> init=new HashMap<String, String>();
                init.put(DomainDescriptor.ELEVATION_DOMAIN, configuration.getElevationAttribute());
                elevationDomainManager= new DomainManager(init,schema);
            }        
        }
      }

 	/**
	 * This code tries to load the sample image from which we can extract SM and CM to use when answering to requests
	 * that falls within a hole in the mosaic.
	 */
	private void loadSampleImage() {
	    if (this.parent.sourceURL == null) {
	        //TODO: I need to define the sampleImage somehow for the ImageMosaicDescriptor case
	        return;
	    }
		
			final URL baseURL=this.parent.sourceURL;
			final File baseFile= DataUtilities.urlToFile(baseURL);
			// in case we do not manage to convert the source URL we leave right awaycd sr
			if (baseFile==null){
				if(LOGGER.isLoggable(Level.FINE))
					LOGGER.fine("Unable to find sample image for path "+baseURL);
				return;
			}
			final File sampleImageFile= new File(baseFile.getParent() + "/sample_image");			
			final RenderedImage sampleImage = Utils.loadSampleImage(sampleImageFile);
			if(sampleImage!=null){
				
				// load SM and CM
				defaultCM= sampleImage.getColorModel();
				defaultSM= sampleImage.getSampleModel();
				
				// default ImageLayout
				defaultImageLayout= new ImageLayout().setColorModel(defaultCM).setSampleModel(defaultSM);
			}
			else
				if(LOGGER.isLoggable(Level.WARNING))
					LOGGER.warning("Unable to find sample image for path "+baseURL);
	}

	/**
	 * This method is responsible for checking the overview policy as defined by
	 * the provided {@link Hints}.
	 * 
	 * @return the overview policy which can be one of
	 *         {@link OverviewPolicy#IGNORE},
	 *         {@link OverviewPolicy#NEAREST},
	 *         {@link OverviewPolicy#SPEED}, {@link OverviewPolicy#QUALITY}.
	 *         Default is {@link OverviewPolicy#NEAREST}.
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
         * This method is responsible for checking the decimation policy as defined by
         * the provided {@link Hints}.
         * 
         * @return the decimation policy which can be one of
         *         {@link DecimationPolicy#ALLOW},
         *         {@link DecimationPolicy#DISALLOW}.
         *         Default is {@link DecimationPolicy#ALLOW}.
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

	public Collection<GridCoverage2D> read(final GeneralParameterValue[] params) throws IOException {

		// create a request
		final RasterLayerRequest request= new RasterLayerRequest(params,this);
		if (request.isEmpty()){
			if(LOGGER.isLoggable(Level.FINE))
				LOGGER.log(Level.FINE,"Request is empty: "+ request.toString());
			return Collections.emptyList();		
		}
		
		// create a response for the provided request
		final RasterLayerResponse response= new RasterLayerResponse(request,this);
		
		// execute the request
		final GridCoverage2D elem = response.createResponse();
		if (elem != null){
			return Collections.singletonList(elem);
		}
		return Collections.emptyList();
		
		
	}
	
	public void dispose() {
	    synchronized (this) {
                try {
                    if (granuleCatalog != null)
                        this.granuleCatalog.dispose();
                } catch (Exception e) {
                    if (LOGGER.isLoggable(Level.FINE))
                        LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
                }                
            }
	}

	/**
	 * Retrieves the list of features that intersect the provided envelope
	 * loading them inside an index in memory where needed.
	 * 
	 * @param envelope
	 *            Envelope for selecting features that intersect.
	 * @return A list of features.
	 * @throws IOException
	 *             In case loading the needed features failes.
	 */
	Collection<GranuleDescriptor> getGranules(final BoundingBox envelope)throws IOException {
		final Collection<GranuleDescriptor> granules = granuleCatalog.getGranules(envelope);
		if (granules != null)
			return granules;
		else
			return Collections.emptyList();
	}
	
	Collection<GranuleDescriptor> getGranules(final Query q)throws IOException {
		final Collection<GranuleDescriptor> granules = granuleCatalog.getGranules(q);
		if (granules != null)
			return granules;
		else
			return Collections.emptyList();
	}
	
	void getGranules(final Query q,final GranuleCatalogVisitor visitor)throws IOException {
		granuleCatalog.getGranules(q,visitor);

	}

	/**
	 * Retrieves the list of features that intersect the provided envelope
	 * loading them inside an index in memory where needed.
	 * 
	 * @param envelope
	 *            Envelope for selecting features that intersect.
	 * @return A list of features.
	 * @throws IOException
	 *             In case loading the needed features fails.
	 */
	void getGranules(final BoundingBox envelope,final GranuleCatalogVisitor visitor)throws IOException {
		granuleCatalog.getGranules(envelope,visitor);
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

    /**
     * @param metadataName
     * @param attributeName 
     * @return
     * @throws IOException
     */
    FeatureCalc createExtremaQuery(String metadataName, String attributeName) throws IOException {
        final Query query = new Query(granuleCatalog.getType().getTypeName());
        query.setPropertyNames(Arrays.asList(attributeName));

        final FeatureCalc visitor= 
            metadataName.toLowerCase().endsWith("maximum")?
                new MaxVisitor(attributeName):new MinVisitor(attributeName);
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
        private Set extractDomain(final String attribute)
                throws IOException {
            Query query = new Query(granuleCatalog.getType().getTypeName());
            query.setPropertyNames(Arrays.asList(attribute));
            final UniqueVisitor visitor= new UniqueVisitor(attribute);
            granuleCatalog.computeAggregateFunction(query, visitor);
            return visitor.getUnique();
        }
        
        /**
         * Extract the domain of a dimension (with Range) as a set of values.
         * 
         * <p>
         * It retrieves a comma separated list of values as a Set of {@link String}.
         * @param domainType 
         * 
         * @return a comma separated list of values as a Set of {@link String}.
         * @throws IOException
         */
        private Set extractDomain(final String attribute, final String secondAttribute, final DomainType domainType)
                throws IOException {
            final Query query = new Query(granuleCatalog.getType().getTypeName());
            
            final PropertyName propertyName = FeatureUtilities.DEFAULT_FILTER_FACTORY.property(attribute);
            query.setPropertyNames(Arrays.asList(attribute, secondAttribute));
            
            final SortByImpl[] sb = new SortByImpl[]{new SortByImpl(propertyName, SortOrder.ASCENDING)};
            // Checking whether it supports sorting capabilities
            if(granuleCatalog.getQueryCapabilities().supportsSorting(sb)){
                query.setSortBy(sb);
            }
            
            final FeatureCalc visitor = domainType == DomainType.TIME_RANGE ? new DateRangeVisitor(attribute, secondAttribute) : new RangeVisitor(attribute, secondAttribute);
            granuleCatalog.computeAggregateFunction(query, visitor);
            return domainType == DomainType.TIME_RANGE ? ((DateRangeVisitor)visitor).getRange() : ((RangeVisitor)visitor).getRange() ;
            
        }

}
