/* Copyright (c) 2001 - 2013 OpenPlans - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geotools.gce.netcdf;

import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.gce.netcdf.fileparser.NetCDFFileInspector;
import org.geotools.gce.netcdf.log.LogUtil;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.OverviewPolicy;
import org.geotools.data.DataSourceException;
import org.geotools.factory.Hints;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.util.logging.Logging;
import org.opengis.coverage.grid.Format;
import org.opengis.coverage.grid.GridCoverageReader;
import org.opengis.coverage.grid.GridEnvelope;
import org.opengis.geometry.Envelope;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

public class NetCDFReader extends AbstractGridCoverage2DReader implements
	GridCoverageReader {

    public static final String HAS_DIM_ANALYSIS_TIME_DOMAIN = "HAS_ANALYSIS_TIME_DOMAIN";
    public static final String DIM_ANALYSIS_TIME_DOMAIN = "ANALYSIS_TIME_DOMAIN";
    private static final Logger LOG = Logging.getLogger(NetCDFReader.class);

    private NetCDFFileInspector fileInsp;

    /**
     * default no arg constructor.
     */
    public NetCDFReader() {

    }

    /**
     * constructor.
     */
    public NetCDFReader(File cdmFile, Hints hints) {
	Date methodBeginDate = new Date();

	fileInsp = new NetCDFFileInspector(cdmFile);
	coverageName = cdmFile.getName();

	// Set up a bunch of properties needed by the super class:
	crs = calculateCoordinateReferenceSystem();

	// originalEnvelope contains the bounds of the file
	originalEnvelope = fileInsp.getOriginalEnvelope(crs);

	// originalGridRange is a GridEnvelope2D created from the dimensions
	// Rectangle. Basically the number of lat and lon points in the file.
	originalGridRange = fileInsp.getOriginalGridRange();

	// highestRes is a double array containing the data resolution of the
	// file.
	// Basically the lat/lon range divided by the number of lat/lon points.
	highestRes = calculateHighestResolution(originalEnvelope,
		fileInsp.getOriginalDim(), crs);

	numOverviews = 0;

	LogUtil.logElapsedTime(LOG, methodBeginDate, this.getFileInsp()
		.getFileName());
    }

    private CoordinateReferenceSystem calculateCoordinateReferenceSystem() {
	CoordinateReferenceSystem result = null;

	try {
	    // TODO support other coords
	    result = CRS.decode("EPSG:4326");
	} catch (NoSuchAuthorityCodeException e1) {
	    LOG.log(Level.SEVERE, "Unable to get CRS EPSG:4326.", e1);
	} catch (FactoryException e1) {
	    LOG.log(Level.SEVERE, "Unable to get CRS EPSG:4326.", e1);
	}

	return result;
    }

    private double[] calculateHighestResolution(GeneralEnvelope envelope,
	    Rectangle2D rectangle2D, CoordinateReferenceSystem crs) {
	double[] result = null;

	try {
	    // TODO fix setting this resolution; research highestRes
	    result = getResolution(envelope, rectangle2D, crs);
	} catch (DataSourceException e) {
	    LOG.log(Level.SEVERE,
		    "Unable to get resolution for NetCDF coverage.", e);
	}
	return result;
    }

    @Override
    public GridCoverage2D read(GeneralParameterValue[] params)
	    throws IOException {
	Date methodBeginDate = new Date();

	ParamInformation paramInfo = paramReader(params);
	if (params == null) {
	    throw new IllegalArgumentException("Params must not be null");
	}
	/*
	 * Our default "in the real world" is the current time and an elevation
	 * of 0
	 */
	if (paramInfo.getTime() == null) {
	    paramInfo.setTime(new Date());
	}

	if (paramInfo.getElevation() == null) {
	    Double defaultElevation = this.getFileInsp().getDefaultElevation();
	    paramInfo.setElevation(defaultElevation);
	    // TODO: set default but do not set elevation to 0.0 as some netcdf
	    // files don't have depth at all and others have ISBL
	}

	GrdDataEncapsulator ncData = this.getFileInsp().parseFiles(paramInfo);

	final GridCoverageFactory factory = new GridCoverageFactory(hints);
	GridCoverage2D coverage = factory.create(this.coverageName,
		ncData.getWritableRaster(), ncData.getGeneralEnvelope());

	LogUtil.logElapsedTime(LOG, methodBeginDate, this.getFileInsp()
		.getFileName());

	return coverage;

    }

    /*
     * paramReader, toNativeCrs and toReferencedEnvelope are heavily based on
     * the ArcSDEGridCoverage2DReaderJAI class in geotools.
     */
    @SuppressWarnings("rawtypes")
    private ParamInformation paramReader(GeneralParameterValue[] params) {
	ParamInformation parsedParams = new ParamInformation();

	if (params == null) {
	    throw new IllegalArgumentException(
		    "No GeneralParameterValue given to read operation");
	}

	GeneralEnvelope reqEnvelope = null;
	GridEnvelope dim = null;
	OverviewPolicy overviewPolicy = null;
	Double elevation = null;
	Date time = null;
	Date analysisTime = null;
	String parameter = null;

	for (int i = 0; i < params.length; i++) {
	    if (params[i] == null) {
		LOG.log(Level.INFO, "Parameter was null");
		continue;
	    }
	    final ParameterValue<?> param = (ParameterValue<?>) params[i];
	    final String name = param.getDescriptor().getName().getCode();
	    if (name.equals(NetCDFFormat.TIME.getName().toString())) {
		if (param.getValue() != null) {
		    List timeList = (List) param.getValue();
		    if (!timeList.isEmpty() && timeList.get(0) instanceof Date) {
			time = (Date) timeList.get(0);
		    }
		    if (timeList.size() > 1) {
			LOG.log(Level.INFO,
				"Requested Parameter TIME list has more than one value: {0}",
				timeList.size());
		    }
		}
	    } else if (name.equals(NetCDFFormat.ANALYSIS_TIME.getName()
		    .toString())) {
		if (param.getValue() != null) {
		    List timeList = (List) param.getValue();
		    Object requestedAnalysisTimeList = timeList.get(0);
		    if (!timeList.isEmpty()
			    && requestedAnalysisTimeList instanceof Date) {
			analysisTime = (Date) requestedAnalysisTimeList;
		    } else if (!timeList.isEmpty()
			    && requestedAnalysisTimeList instanceof String) {
			SimpleDateFormat sdf = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
			try {
			    analysisTime = sdf
				    .parse((String) requestedAnalysisTimeList);
			} catch (ParseException e) {
			    LOG.log(Level.WARNING, "Could not parse date: "
				    + requestedAnalysisTimeList, e);
			}
		    }
		    if (timeList.size() > 1) {
			LOG.log(Level.INFO,
				"Requested Parameter ANALYSIS_TIME list has more than one value: {0}",
				timeList.size());
		    }
		}
	    } else if (name.equals(NetCDFFormat.ELEVATION.getName().toString())) {
		final Object value = param.getValue();
		if (value != null) {
		    elevation = (Double) ((List<?>) value).get(0);
		}
	    } else if (name.equals(NetCDFFormat.PARAMETER.getName().toString())) {
		parameter = (String) param.getValue();
	    } else if (name.equals(AbstractGridFormat.READ_GRIDGEOMETRY2D
		    .getName().toString())) {
		final GridGeometry2D gg = (GridGeometry2D) param.getValue();
		reqEnvelope = new GeneralEnvelope((Envelope) gg.getEnvelope2D());

		final GeneralEnvelope coverageEnvelope = getOriginalEnvelope();
		CoordinateReferenceSystem nativeCrs = coverageEnvelope
			.getCoordinateReferenceSystem();
		CoordinateReferenceSystem requestCrs = reqEnvelope
			.getCoordinateReferenceSystem();
		if (!CRS.equalsIgnoreMetadata(nativeCrs, requestCrs)) {
		    ReferencedEnvelope nativeCrsEnv;
		    nativeCrsEnv = toNativeCrs(reqEnvelope, nativeCrs);
		    reqEnvelope = new GeneralEnvelope(nativeCrsEnv);
		}

		dim = gg.getGridRange2D();
		continue;
	    } else if (name.equals(AbstractGridFormat.OVERVIEW_POLICY.getName()
		    .toString())) {
		overviewPolicy = (OverviewPolicy) param.getValue();
		continue;
	    } else {
		LOG.log(Level.INFO,
			"During request for 'read', parameter name '" + name
				+ "' was not handled.");
	    }
	}

	if (reqEnvelope == null && dim == null) {
	    reqEnvelope = getOriginalEnvelope();
	    dim = getOriginalGridRange();
	}

	if (reqEnvelope == null) {
	    reqEnvelope = getOriginalEnvelope();
	}
	if (dim == null) {
	    final GeneralEnvelope adjustedGRange;
	    try {
		MathTransform gridToWorld = getOriginalGridToWorld(PixelInCell.CELL_CENTER);
		MathTransform worldToGrid = gridToWorld.inverse();
		adjustedGRange = CRS.transform(worldToGrid, reqEnvelope);
	    } catch (Exception e) {
		throw new RuntimeException(e);
	    }
	    int xmin = (int) Math.floor(adjustedGRange.getMinimum(0));
	    int ymin = (int) Math.floor(adjustedGRange.getMinimum(1));
	    int xmax = (int) Math.ceil(adjustedGRange.getMaximum(0));
	    int ymax = (int) Math.ceil(adjustedGRange.getMaximum(1));
	    dim = new GridEnvelope2D(xmin, ymin, xmax - xmin, ymax - ymin);
	}

	if (!reqEnvelope.intersects(getOriginalEnvelope(), true)) {
	    throw new IllegalArgumentException(
		    "The requested extend does not overlap the coverage extent: "
			    + getOriginalEnvelope());
	}

	if (overviewPolicy == null) {
	    overviewPolicy = OverviewPolicy.NEAREST;
	}

	parsedParams.setRequestedEnvelope(reqEnvelope);
	parsedParams.setDim(dim);
	parsedParams.setOverviewPolicy(overviewPolicy);
	parsedParams.setTime(time);
	parsedParams.setElevation(elevation);
	parsedParams.setParameter(parameter);
	parsedParams.setAnalysisTime(analysisTime);

	return parsedParams;

    }

    private static ReferencedEnvelope toNativeCrs(
	    final GeneralEnvelope requestedEnvelope,
	    final CoordinateReferenceSystem nativeCRS) {

	ReferencedEnvelope reqEnv = toReferencedEnvelope(requestedEnvelope);

	if (!CRS.equalsIgnoreMetadata(nativeCRS,
		reqEnv.getCoordinateReferenceSystem())) {
	    try {
		reqEnv = reqEnv.transform(nativeCRS, true);
	    } catch (FactoryException fe) {
		throw new IllegalArgumentException(
			"Unable to find a reprojection from requested "
				+ "coordsys to native coordsys for this request",
			fe);
	    } catch (TransformException te) {
		throw new IllegalArgumentException(
			"Unable to perform reprojection from requested "
				+ "coordsys to native coordsys for this request",
			te);
	    }
	}
	return reqEnv;
    }

    private static ReferencedEnvelope toReferencedEnvelope(
	    GeneralEnvelope envelope) {
	double minx = envelope.getMinimum(0);
	double maxx = envelope.getMaximum(0);
	double miny = envelope.getMinimum(1);
	double maxy = envelope.getMaximum(1);
	CoordinateReferenceSystem crs = envelope.getCoordinateReferenceSystem();
	ReferencedEnvelope refEnv = new ReferencedEnvelope(minx, maxx, miny,
		maxy, crs);
	return refEnv;
    }

    /*
     * These two methods provide the support for Time and Elevation. Check out
     * ImageMosaicReader from GeoTools-8 imagemosaic datasource module for more
     * inspiration.
     */
    @Override
    public String[] getMetadataNames() {
	final String[] parentNames = super.getMetadataNames();
	final List<String> metadataNames = new ArrayList<String>();
	metadataNames.add(TIME_DOMAIN);
	metadataNames.add(HAS_TIME_DOMAIN);
	metadataNames.add(TIME_DOMAIN_MINIMUM);
	metadataNames.add(TIME_DOMAIN_MAXIMUM);
	metadataNames.add(TIME_DOMAIN_RESOLUTION);
	metadataNames.add(ELEVATION_DOMAIN);
	metadataNames.add(ELEVATION_DOMAIN_MINIMUM);
	metadataNames.add(ELEVATION_DOMAIN_MAXIMUM);
	metadataNames.add(HAS_ELEVATION_DOMAIN);
	metadataNames.add(ELEVATION_DOMAIN_RESOLUTION);
	metadataNames.add(DIM_ANALYSIS_TIME_DOMAIN);
	metadataNames.add(HAS_DIM_ANALYSIS_TIME_DOMAIN);

	if (parentNames != null) {
	    metadataNames.addAll(Arrays.asList(parentNames));
	}

	return metadataNames.toArray(new String[metadataNames.size()]);
    }

    @Override
    public String getMetadataValue(final String name) {
	final String superValue = super.getMetadataValue(name);
	if (superValue != null) {
	    return superValue;
	}

	/*
	 * TODO: This is a huge assumption, our data will NOT always have time
	 * and elevation information, we are just assuming it will always be 4d
	 * data for simplicitiy, in the future this must be fixed!
	 */
	if (name.equalsIgnoreCase(HAS_ELEVATION_DOMAIN)) {
	    if (fileInsp.getElevations().size() > 0) {
		return String.valueOf(true);
	    } else {
		return String.valueOf(false);
	    }
	}

	if (name.equalsIgnoreCase(HAS_TIME_DOMAIN)) {
	    return String.valueOf(true);
	}

	if (name.equalsIgnoreCase(HAS_DIM_ANALYSIS_TIME_DOMAIN)) {
	    return String.valueOf(true);
	}

	/* Get the time string */
	if (name.equalsIgnoreCase(TIME_DOMAIN)) {
	    return fileInsp.getTimeString();
	}
	/* Get the elevation string */
	if (name.equalsIgnoreCase(ELEVATION_DOMAIN)) {
	    return fileInsp.getElevationString();
	}
	/* Get the analysis_time (model run time) string */
	if (name.equalsIgnoreCase(DIM_ANALYSIS_TIME_DOMAIN)) {
	    return fileInsp.getAnalysisTimeString();
	}

	if (name.equalsIgnoreCase(TIME_DOMAIN_MINIMUM)) {
	    return fileInsp.getTimeMinimum();
	}
	if (name.equalsIgnoreCase(TIME_DOMAIN_MAXIMUM)) {
	    return fileInsp.getTimeMaximum();
	}
	return superValue;
    }

    /**
     * Trying to abstract out a way to get default values into the
     * GetCapabilities. Ideally, this method signature would get pulled up to
     * GridCoverageReader, so the GetCapabilities and such would be able to use
     * this method to get default values for all the metadatas (including
     * dimensions) from any coverage file.
     */
    public String getMetadataDefaultValue(final String name) {

	/* Get the default value for the elevation dimension */
	if (ELEVATION_DOMAIN.equalsIgnoreCase(name)
		&& null != fileInsp.getDefaultElevation()) {
	    return fileInsp.getDefaultElevation().toString();
	}

	return null;
    }

    /**
     * @return fileInsp
     */
    public NetCDFFileInspector getFileInsp() {
	return fileInsp;
    }

    /**
     * @param fileInsp
     */
    public void setFileInsp(NetCDFFileInspector fileInsp) {
	this.fileInsp = fileInsp;
    }

    public Format getFormat() {
	return new NetCDFFormat();
    }

}