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
package org.geotools.coverage.io.grib1;

import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.measure.unit.Unit;

import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.io.CoverageAccess;
import org.geotools.coverage.io.CoverageSource;
import org.geotools.coverage.io.CoverageStore;
import org.geotools.coverage.io.driver.BaseFileDriver;
import org.geotools.coverage.io.driver.Driver;
import org.geotools.coverage.io.grib1.GRIB1ProductFieldType.Product;
import org.geotools.coverage.io.impl.DefaultCoverageAccess;
import org.geotools.coverage.io.impl.range.DefaultRangeType;
import org.geotools.coverage.io.range.FieldType;
import org.geotools.coverage.io.range.RangeType;
import org.geotools.coverage.io.util.Utilities;
import org.geotools.data.DefaultServiceInfo;
import org.geotools.data.Parameter;
import org.geotools.data.ServiceInfo;
import org.geotools.factory.Hints;
import org.geotools.feature.NameImpl;
import org.geotools.geometry.Envelope2D;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.imageio.SliceDescriptor;
import org.geotools.imageio.SpatioTemporalImageReader;
import org.geotools.imageio.VerticalExtent;
import org.geotools.imageio.metadata.Band;
import org.geotools.imageio.metadata.RectifiedGrid;
import org.geotools.imageio.metadata.SpatioTemporalMetadata;
import org.geotools.referencing.operation.transform.ProjectiveTransform;
import org.geotools.resources.coverage.CoverageUtilities;
import org.geotools.util.NullProgressListener;
import org.geotools.util.NumberRange;
import org.geotools.util.SimpleInternationalString;
import org.opengis.coverage.grid.GridEnvelope;
import org.opengis.feature.type.Name;
import org.opengis.geometry.BoundingBox;
import org.opengis.geometry.Envelope;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CompoundCRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.VerticalCRS;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.opengis.temporal.TemporalGeometricPrimitive;
import org.opengis.util.InternationalString;
import org.opengis.util.ProgressListener;

/**
 * {@link CoverageAccess} implementation for GRIB1 Data format.
 * 
 * @author Romagnoli Daniele, GeoSolutions
 * 
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/unsupported/coverage-experiment/grib1/src/main/java/org/geotools/coverage/io/grib1/GRIB1Access.java $
 */
public class GRIB1Access extends DefaultCoverageAccess {
    
    private final static Set<AccessType> allowedAccessTypes;

    private final static Map<String, Parameter<?>> accessParameters;

    private final static Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger("org.geotools.coverage.io.grib1");

    private int numberOfCoverages;

    /**
     * TODO: Should be changed in favor of 2 SoftValueHashMap??
     */
    Map<Integer, SliceDescriptor> sliceDescriptorsMap = new HashMap<Integer, SliceDescriptor>();

    Map<Integer, SpatioTemporalMetadata> spatioTemporalMetadataMap = new HashMap<Integer, SpatioTemporalMetadata>();

    /**
     * Name of this coverage data product.
     */
    ArrayList<Name> coverageNames = new ArrayList<Name>();

    private Map<String, Serializable> connectionParameters;

    HashMap<Name, GeneralEnvelope> envelopesMap = new HashMap<Name, GeneralEnvelope>();

    HashMap<Name, GeneralEnvelope> coverageEnvelope2DMap = new HashMap<Name, GeneralEnvelope>();

    HashMap<Name, CoordinateReferenceSystem> crsMap = new HashMap<Name, CoordinateReferenceSystem>();

    HashMap<Name, MathTransform> raster2ModelMap = new HashMap<Name, MathTransform>();

    HashMap<Name, Set<TemporalGeometricPrimitive>> temporalExtentMap = new HashMap<Name, Set<TemporalGeometricPrimitive>>();

    HashMap<Name, Set<NumberRange<Double>>> verticalExtentMap = new HashMap<Name, Set<NumberRange<Double>>>();
    
    HashMap <String, Name> verticalCRSMap = new HashMap <String, Name>();

    /** Highest resolution available. */
    HashMap<Name, double[]> highestResMap = new HashMap<Name, double[]>();

    /** The base envelope 2D */
    HashMap<Name, Envelope2D> baseEnvelope2DMap = new HashMap<Name, Envelope2D>();

    HashMap<Name, BoundingBox> boundingBoxesMap = new HashMap<Name, BoundingBox>();

    /** WGS84 envelope 2D for this coverage */
    HashMap<Name, Envelope2D> wgs84BaseEnvelope2DMap = new HashMap<Name, Envelope2D>();

    /** The CRS related to the base envelope 2D */
    HashMap<Name, CoordinateReferenceSystem> spatialReferenceSystem2DMap = new HashMap<Name, CoordinateReferenceSystem>();

    private URL input;

    HashMap<Name, RangeType> rangeMap = new HashMap<Name, RangeType>();

    HashMap<Name, GridGeometry2D> gridGeometry2DMap = new HashMap<Name, GridGeometry2D>();

    static {
        final Set<AccessType> types = new HashSet<AccessType>();
        types.add(AccessType.READ_ONLY);
        allowedAccessTypes = Collections.unmodifiableSet(types);
        final Map<String, Parameter<?>> parameters = new HashMap<String, Parameter<?>>();
        parameters.put(BaseFileDriver.URL.key, BaseFileDriver.URL);
        accessParameters = Collections.unmodifiableMap(parameters);
    }

    GRIB1Access(Driver driver, URL source,
            Map<String, Serializable> additionalParameters, Hints hints,
            ProgressListener listener) throws IOException {
        super(driver);

        if (source == null) {
            if (additionalParameters.containsKey(BaseFileDriver.URL.key)) {
                source = (URL) additionalParameters.get(BaseFileDriver.URL.key);
            }
        }
        if (source == null)
            throw new IllegalArgumentException();
        connectionParameters = new HashMap<String, Serializable>();
        if (additionalParameters != null) {
            connectionParameters.putAll(additionalParameters);
        }
        connectionParameters.put(BaseFileDriver.URL.key, source);

        // get the protocol
        final String protocol = source.getProtocol();

        // file
        if (protocol.equalsIgnoreCase("file")) {
            // convert to file
            final File sourceFile = BaseFileDriver.urlToFile(source);

            // check that it is a file,exists and can be at least read
            if (!sourceFile.exists() || !sourceFile.isFile()
                    || !sourceFile.canRead())
                throw new IllegalArgumentException("Invalid input");

            // set the class type
            // this.inputClass = File.class;
            this.input = source;

            // initialize
            this.init();

            return;
        }
    }

    private void init() {
        // get the needed info from them to set the extent
        try {
            final SpatioTemporalImageReader reader = (SpatioTemporalImageReader) GRIB1Driver.spi.createReaderInstance();
            reader.setInput(this.input);

            int numCoverages = 0;

            // Setting the name
            final Name mainCoverageName = Utilities.buildCoverageName(input);
            Name coverageName = mainCoverageName;

            // TODO: Add more checks on Vertical/Temporal dimension availability
            final int numImages = reader.getNumImages(false);

            // //
            //
            // Setting Envelope and Extents
            //
            // //
            Map<Name, Map<String, GRIB1ProductFieldType>> fieldsMap = new HashMap<Name, Map<String, GRIB1ProductFieldType>>();
            for (int imageIndex = 0; imageIndex < numImages; imageIndex++) {
                final SpatioTemporalMetadata metadata = reader.getSpatioTemporalMetadata(imageIndex);
                final SliceDescriptor sd = reader.getSliceDescriptor(imageIndex);
                if (sd == null)
                    throw new IllegalStateException("unable to get the required sliceDescriptor");

                // //
                //
                // Getting slice extent
                // 
                // //
                VerticalExtent ve = sd.getVerticalExtent();
                TemporalGeometricPrimitive time = sd.getTemporalExtent();
                CoordinateReferenceSystem crs = sd.getCoordinateReferenceSystem();
                String referenceID = "";
                if (crs instanceof CompoundCRS){
                    List<CoordinateReferenceSystem> list = ((CompoundCRS) crs).getCoordinateReferenceSystems();
                    if (list!=null && !list.isEmpty()){
                        for (CoordinateReferenceSystem crsElement : list){
                            if (crsElement instanceof VerticalCRS){
                                referenceID = crsElement.getName().getCode();
//                                CoordinateSystemAxis axis = ((VerticalCRS)crsElement).getCoordinateSystem().getAxis(0);
//                                referenceID = axis.getName().toString();
                                break;
                            }
                        }
                    }
                }

                Band band = metadata.getBand(0);
                Set<TemporalGeometricPrimitive> temporalExtent;
                Set<NumberRange<Double>> verticalExtent;
                Map<String, GRIB1ProductFieldType> fields;

                // //
                //
                // Firstly, group coverages having the same bounding Box.
                // Some datasources may contain data coming from acquisitions
                // on different areas. We divide them in different groups.
                //
                // //

                if (!verticalCRSMap.containsKey(referenceID)) {
                    // //
                    //
                    // This is the first occurrence. Setting coverageName,
                    // as well as the extents.
                    //
                    // //
                    coverageName = new NameImpl(mainCoverageName.getLocalPart()
                            + "_" + Integer.toString(numCoverages++));
                    coverageNames.add(coverageName);
                    verticalCRSMap.put(referenceID, coverageName);

                    temporalExtent = new LinkedHashSet<TemporalGeometricPrimitive>();
                    verticalExtent = new LinkedHashSet<NumberRange<Double>>();
                    fields = new LinkedHashMap<String, GRIB1ProductFieldType>();
                    temporalExtentMap.put(coverageName, temporalExtent);
                    verticalExtentMap.put(coverageName, verticalExtent);
                    fieldsMap.put(coverageName, fields);
                } else {
                    coverageName = verticalCRSMap.get(referenceID);
                }

                if (!envelopesMap.containsKey(coverageName)) {
                    // //
                    //
                    // In case this coverage's properties haven't been
                    // initialized yet, init them.
                    //
                    // //
                    initOriginalEnvelopeAndCRS(coverageName, sd);
                    final GridGeometry2D gridGeometry2D = buildGridGeometry2D(
                            coverageName, metadata);
                    gridGeometry2DMap.put(coverageName, gridGeometry2D);
                } else {
                    GeneralEnvelope envelope = envelopesMap.get(coverageName);
                    envelope.add(sd.getGeneralEnvelope());
                    envelopesMap.put(coverageName, envelope);
                }

                // //
                //
                // Update the temporal, vertical extent for this coverage
                //
                // //
                temporalExtent = (LinkedHashSet<TemporalGeometricPrimitive>) temporalExtentMap.get(coverageName);
                verticalExtent = (LinkedHashSet<NumberRange<Double>>) verticalExtentMap.get(coverageName);
                if (ve != null) {
                	NumberRange<Double> verticalEnvelope = NumberRange.create(ve.getMinimumValue().doubleValue(), ve.getMaximumValue().doubleValue());
                    if (!verticalExtent.contains(verticalEnvelope))
                        verticalExtent.add(verticalEnvelope);
                }

                if (time != null) {
                    if (!temporalExtent.contains(time))
                        temporalExtent.add(time);
                }

                // //
                //
                // Update the fields for this coverage
                //
                // //
                fields = (LinkedHashMap<String, GRIB1ProductFieldType>) fieldsMap.get(coverageName);

                String elementName = band.getName();
                if (!fields.containsKey(elementName)) {

                    Product product = GRIB1ProductFieldType.getProduct(elementName);
                    Unit unit = Unit.ONE;
                    if (product != null) {
                        unit = product.getUoM();
                    } else {
                        String uOm = band.getUoM();
                        if(uOm == null)
                        	 unit = Unit.ONE.alternate(uOm);
                        else
                        	try {
                        		unit = Unit.valueOf(uOm);
	                        } catch (IllegalArgumentException iae) {
	                            try {
	                                unit = Unit.ONE.alternate(uOm);
	                            } catch (IllegalArgumentException iae2) {
	                                if (LOGGER.isLoggable(Level.FINE)) {
	                                    LOGGER.log(Level.FINE,
	                                            "Unable to parse the provided unit "
	                                                    + uOm, iae2);
	                                }
	                            } catch (UnsupportedOperationException uoe) {
	                                if (LOGGER.isLoggable(Level.FINE)) {
	                                    LOGGER.log(Level.FINE,
	                                            "Unable to parse the provided unit "
	                                                    + uOm, uoe);
	                                }
	                            }
	                        }
                    }
                    final Name nameImpl = new NameImpl(coverageName.getLocalPart(), elementName);
                    final InternationalString description = new SimpleInternationalString(elementName);

                    // setting bands names.
                    final GridSampleDimension gridBand = Utilities.buildBands(band, elementName, unit);
                    final GRIB1ProductFieldType fd = new GRIB1ProductFieldType(nameImpl, description, gridBand);
                    fields.put(elementName, fd);
                }

                // //
                //
                // Updating the sliceDescriptor Map
                //
                // //
                sliceDescriptorsMap.put(imageIndex, sd);
                spatioTemporalMetadataMap.put(imageIndex, metadata);
            }

            // //
            //
            // Checking sets
            //
            // //
            for (Name covName : verticalExtentMap.keySet()) {
                Set<NumberRange<Double>> verticalExtent = verticalExtentMap.get(covName);
                if (verticalExtent.size() == 0) {
                    verticalExtent = Collections.emptySet();
                    verticalExtentMap.put(covName, verticalExtent);
                }
                Set<TemporalGeometricPrimitive> temporalExtent = temporalExtentMap
                        .get(covName);
                if (temporalExtent.size() == 0) {
                    temporalExtent = Collections.emptySet();
                    temporalExtentMap.put(covName, temporalExtent);
                }

                // //
                //
                // Setting a proper RangeType using the FieldTypes found
                //
                // //
                Map<String, GRIB1ProductFieldType> fields = (LinkedHashMap<String, GRIB1ProductFieldType>) fieldsMap.get(covName);
                if (fields != null && !fields.isEmpty()) {
                    final Set<FieldType> fieldTypes = new LinkedHashSet<FieldType>(
                            fields.size());
                    final StringBuilder sb = new StringBuilder();
                    for (GRIB1ProductFieldType fd : fields.values()) {
                        fieldTypes.add(fd);
                        final InternationalString description = fd.getDescription();
                        sb.append(description != null ? description.toString()+ "," : "");
                    }
                    String description = sb.toString();
                    int pos = -1;
                    if (description.length() > 0&& (pos = description.lastIndexOf(",")) != -1) {
                        description = description.substring(0, pos);
                    }
                    DefaultRangeType range = new DefaultRangeType(covName,new SimpleInternationalString(description),fieldTypes);
                    rangeMap.put(covName, range);
                }

            }

            numberOfCoverages = numCoverages;
            // dispose the reader
            reader.dispose();
        } catch (IOException e) {
            this.numberOfCoverages = 1;

        } catch (FactoryException fe) {
            this.numberOfCoverages = 1;

        } catch (TransformException fe) {
            this.numberOfCoverages = 1;
        }

    }

    public CoverageSource access(Name name, Map<String, Serializable> params,
            AccessType accessType, Hints hints, ProgressListener listener)
            throws IOException {
        if (listener == null)
            listener = new NullProgressListener();
        listener.started();
        try {
            return new GRIB1Source(this, name);
        } finally {
            listener.complete();
        }
    }

    public boolean canCreate(Name name, Map<String, Serializable> params,
            Hints hints, ProgressListener listener) throws IOException {
        return false;
    }

    public boolean canDelete(Name name, Map<String, Serializable> params,
            Hints hints) throws IOException {
        return false;
    }

    public CoverageStore create(Name name, Map<String, Serializable> params,
            Hints hints, ProgressListener listener) throws IOException {
        throw new UnsupportedOperationException(
                "GRIB1Access only provides read access from a Coverage Source");
    }

    public boolean delete(Name name, Map<String, Serializable> params,
            Hints hints) throws IOException {
        return false;
    }

    public void dispose() {
        input = null;
    }

    public Map<String, Parameter<?>> getAccessParameterInfo(
            AccessType accessType) {
        return accessParameters;
    }

    public Map<String, Serializable> getConnectParameters() {
        return Collections.unmodifiableMap(connectionParameters);
    }

    public Driver getDriver() {
        return new GRIB1Driver();
    }

    public ServiceInfo getInfo(ProgressListener listener) {
        if (listener == null)
            listener = new NullProgressListener();
        listener.started();
        final DefaultServiceInfo info = new DefaultServiceInfo();


        info.setTitle(coverageNames.get(0).toString());
        try {

            info.setSource(input.toURI());
        } catch (URISyntaxException e1) {

        } finally {
            listener.complete();
        }
        return info;
    }

    public Envelope getExtent(Name coverageName, ProgressListener listener) {
        if (listener == null)
            listener = new NullProgressListener();
        listener.started();
        try {
            return envelopesMap.get(coverageName).clone();
        } finally {
            listener.complete();
        }
    }

    public List<Name> getNames(ProgressListener listener) {
        if (listener == null)
            listener = new NullProgressListener();
        listener.started();
        try {
            return Collections.unmodifiableList(this.coverageNames);
        } finally {
            listener.complete();
        }
    }

    public int getNumCoverages(ProgressListener listener) {
        if (listener == null)
            listener = new NullProgressListener();
        listener.started();
        try {
            return numberOfCoverages;
        } finally {
            listener.complete();
        }
    }

    public Set<AccessType> getSupportedAccessTypes() {
        return allowedAccessTypes;
    }

    public boolean isCreateSupported() {
        return false;
    }

    public boolean isDeleteSupported() {
        return false;
    }

    private GridGeometry2D buildGridGeometry2D(Name coverageName, SpatioTemporalMetadata metadata) {
        GridGeometry2D g2d = null;
        if (metadata != null) {
            // //
            //
            // Getting the rectifiedGrid node
            //
            // //
            final RectifiedGrid rg = metadata.getRectifiedGrid();
            final AffineTransform at = Utilities.getAffineTransform(rg);
            final GridEnvelope gridRange = Utilities.getGridRange(rg);
            final MathTransform raster2Model = ProjectiveTransform.create(at);
            raster2ModelMap.put(coverageName, raster2Model);
            final double[] highestRes = CoverageUtilities.getResolution((AffineTransform) raster2Model);
            highestResMap.put(coverageName, highestRes);
            g2d = new GridGeometry2D(gridRange, raster2Model,spatialReferenceSystem2DMap.get(coverageName));
        }
        return g2d;
    }

    private void initOriginalEnvelopeAndCRS(Name coverageName,
            SliceDescriptor sd) throws FactoryException, TransformException {
        if (sd == null)
            throw new IllegalArgumentException("Provided Slice Descriptor is null");
        final GeneralEnvelope envelope = sd.getGeneralEnvelope().clone();
        envelopesMap.put(coverageName, envelope);
        crsMap.put(coverageName, sd.getCoordinateReferenceSystem());
        final BoundingBox bb = sd.getHorizontalExtent();
        boundingBoxesMap.put(coverageName, bb);
        final GeneralEnvelope coverageEnvelope2D = new GeneralEnvelope(bb);
        coverageEnvelope2DMap.put(coverageName, coverageEnvelope2D);
        spatialReferenceSystem2DMap.put(coverageName, coverageEnvelope2D.getCoordinateReferenceSystem());
        final Envelope2D wgs84BaseEnvelope2D = (Envelope2D) Utilities.getEnvelopeAsWGS84(coverageEnvelope2D, true);
        wgs84BaseEnvelope2DMap.put(coverageName, wgs84BaseEnvelope2D);
        baseEnvelope2DMap.put(coverageName, new Envelope2D(coverageEnvelope2D));
    }

    public URL getInput() {
        return input;
    }

}
