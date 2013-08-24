/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2013, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.netcdf.fileparser;

import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.gce.netcdf.GrdDataEncapsulator;
import org.geotools.gce.netcdf.NetCdfUtil;
import org.geotools.gce.netcdf.ParamInformation;
import org.geotools.gce.netcdf.index.IndexingStrategy;
import org.geotools.gce.netcdf.index.NearestNeighborIndexingStrategy;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.util.logging.Logging;
import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import ucar.ma2.Array;
import ucar.ma2.IndexIterator;
import ucar.ma2.InvalidRangeException;
import ucar.nc2.Attribute;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;
import ucar.nc2.dataset.NetcdfDataset;

/**
 * Provides file access to a NetCDF data file and a place to assemble data from that file.
 * 
 * Intended as a support object for the NetCDFReader, particularly to generate a GridCoverage2D object as
 * the result of the NetCDFReader read method.
 */
public class NetCDFFileInspector {
    private static final int MIN_INDEX = 0;
    private static final int MAX_INDEX = 1;

    private static final Logger LOG = Logging
	    .getLogger(NetCDFFileInspector.class);

    // I dont know what this is, but it shouldn't be used.
    // We should be calculating the resolution from the file.
    private static final int DEFAULT_POINTS_PER_DEGREE = 7;

    /*
     * The NetCDF file itself.
     * 
     * TODO: Do we really need the file? It appears that everytime we use it, we
     * just get the absolute path from it. Why not just store the path?
     */
    private File file;

    /*
     * float array: [minimum longitude, maximum longitude, minimum latitude,
     * maximum latitude]
     * 
     * In a (-180 to 180) coordinate system.
     */
    private float[] bounds;

    /*
     * A rectangle describing the size of the data in the file. [0, 0, number of
     * longitude points, number of latitude points]
     */
    private Rectangle originalDim;

    /*
     * Whether or not this NetCDF uses a (0 to 360) world grid.
     */
    private boolean isLongitude0to360;

    /*
     * The indexing strategy implementation for finding coordinate indices.
     */
    private IndexingStrategy indexingStrategy;

    /**
     * TODO: Should we have a no arg constructor? There's no way to set the file
     * after construction. ans: Jeff created this for some class method tests
     * that do not need the class's file property.
     * 
     * default no arg constructor.
     */
    public NetCDFFileInspector() {
	this.indexingStrategy = new NearestNeighborIndexingStrategy();
    }

    /**
     * constructor.
     */
    public NetCDFFileInspector(File file) {
	validateNetCdfFile(file.getAbsolutePath());
	this.file = file;
	this.indexingStrategy = new NearestNeighborIndexingStrategy();
	setProperties();
    }

    /*
     * Reads the NetCDF and gets information needed to populate bounds and
     * originalDim.
     */
    private void setProperties() {
	LOG.log(Level.INFO, "Getting Bounds For {0}", file.getAbsolutePath());
	/*
	 * Sane defaults in case something goes wrong
	 */
	float[] minAndMaxLon = new float[] { NetCdfUtil.MIN_LON,
		NetCdfUtil.MAX_LON };
	float[] minAndMaxLat = new float[] { NetCdfUtil.MIN_LAT,
		NetCdfUtil.MAX_LAT };

	NetcdfFile ncFile = null;
	try {
	    // Open NetCDF
	    ncFile = NetcdfDataset.openFile(file.getAbsolutePath(), null);

	    // Get lon and lat Variables from NetCDF
	    Variable lon = NetCdfUtil.getFileVariableByName(ncFile,
		    NetCdfUtil.LON_VARIABLE_NAMES);
	    Variable lat = NetCdfUtil.getFileVariableByName(ncFile,
		    NetCdfUtil.LAT_VARIABLE_NAMES);

	    // Get the mins and maxes for lon and lat
	    minAndMaxLon = getMinAndMaxFromArray(lon.read());
	    minAndMaxLat = getMinAndMaxFromArray(lat.read());

	    // Check to see if this file uses a (0 to 360) world grid
	    if (null != minAndMaxLon) {
		this.isLongitude0to360 = minAndMaxLon[MAX_INDEX] > 180;
	    }

	    // Convert the lons if necessary
	    minAndMaxLon = convertBoundsToNeg180to180(minAndMaxLon[MIN_INDEX],
		    minAndMaxLon[MAX_INDEX]);

	    // Create the bounds array
	    this.bounds = new float[] { minAndMaxLon[MIN_INDEX],
		    minAndMaxLon[MAX_INDEX], minAndMaxLat[MIN_INDEX],
		    minAndMaxLat[MAX_INDEX] };

	    // Get the number of lons and lats
	    int width = lon.getShape(0);
	    int height = lat.getShape(0);

	    // Create the dimension rectangle for this NetCDF
	    // basically the number of points in the original data
	    this.originalDim = new Rectangle(0, 0, width, height);

	} catch (Exception e) {
	    LOG.log(Level.SEVERE, e.getMessage(), e);
	} finally {
	    closeNetCdfFile(ncFile);
	}
    }

    /**
     * This is just a custom float array to hold the bounds. Uses a (-180 to
     * 180) coordinate system.
     * 
     * TODO: Either find an existing class or create one to use instead. Perhaps
     * just use the GeneralEnvelope?
     * 
     * @return float array: [minimum longitude, maximum longitude, minimum
     *         latitude, maximum latitude]
     */
    public float[] getBounds() {
	return this.bounds.clone();
    }

    /**
     * Get the dimension rectangle for this NetCDF. Basically the number of
     * points in the original data.
     * 
     * @return Rectangle
     */
    public Rectangle getOriginalDim() {
	return this.originalDim;
    }

    /**
     * Creates a GeneralEnvelope from the bounds and a given CRS. Needed by the
     * AbstractGridCoverage2DReader.
     * 
     * @param crs
     * @return GeneralEnvelope
     */
    public GeneralEnvelope getOriginalEnvelope(CoordinateReferenceSystem crs) {
	if (null == bounds) {
	    return null;
	}

	GeneralEnvelope env = new GeneralEnvelope(new double[] {
		bounds[NetCdfUtil.BOUNDS_INDEX_MIN_LONGITUDE],
		bounds[NetCdfUtil.BOUNDS_INDEX_MIN_LATITUDE] }, new double[] {
		bounds[NetCdfUtil.BOUNDS_INDEX_MAX_LONGITUDE],
		bounds[NetCdfUtil.BOUNDS_INDEX_MAX_LATITUDE] });
	env.setCoordinateReferenceSystem(crs);

	return env;
    }

    /**
     * Creates a GridEnvelope2D from the original dimensions. Needed by the
     * AbstractGridCoverage2DReader.
     * 
     * @return GridEnvelope2D
     */
    public GridEnvelope2D getOriginalGridRange() {
	if (null == originalDim) {
	    return null;
	}

	return new GridEnvelope2D(originalDim);
    }

    public String getFileName() {
	return file.getName();
    }

    public boolean isLongitudeIn0to360() {
	return this.isLongitude0to360;
    }

    /**
     * We have some files whose longitude's range is greater than 180, i.e.
     * 281.5 to 288.0. There is nothing in the headers to indicate that the file
     * is using a different grid system. So, I'm assuming they are using (0 to
     * 360) as the world range. If min or max is greater than 180, assume the
     * file's world longitude range is (0 to 360) and convert it to (-180 to
     * 180)
     */
    public float[] convertBoundsToNeg180to180(float min, float max) {
	// If the max is greater than 180 we assume the file is using a
	// (0 to 360) world range. If the min is less than 0, we have a mix of
	// (-180 to 180) and (0 to 360), and this is really a problem.
	if (max > 180 && min < 0) {
	    LOG.log(Level.SEVERE,
		    "Invalid file bounds: min longitude < 0 and max longitude > 180: ({0} to {1})",
		    new Object[] { min, max });
	    return null;

	    // If the max is greater than 180 we assume the file is using a
	    // (0 to 360) world range. If the min is less than 180, we end up
	    // with a wrap around scenario, in which case the bounding box
	    // becomes the world.
	} else if (max > 180 && min < 180) {
	    min = -180;
	    max = 180;

	    // If the max is greater than 180 we assume the file is using a
	    // (0 to 360) world range. If the min is also greater than 180, the
	    // bounds are completely in the western hemisphere, just convert
	    // both by subtracting 360.
	} else if (max > 180 && min >= 180) {
	    min -= 360;
	    max -= 360;
	}
	// else both the bounds are less than 180 and dont need to be changed.
	// There is an assumption here that min is actually less than max.

	return new float[] { min, max };
    }

    public synchronized GrdDataEncapsulator parseFiles(
	    ParamInformation paramInfo) {
	String parameterName = paramInfo.getParameter();

	GrdDataEncapsulator data = new GrdDataEncapsulator(
		DEFAULT_POINTS_PER_DEGREE, parameterName, "NOTUSINGTHISYET",
		paramInfo);

	NetcdfFile ncFile = null;
	try {
	    ncFile = NetcdfDataset.openFile(file.getAbsolutePath(), null);

	    // for each value specified in paramInfo, look for match
	    // in the file data. process only if we have a match for each.
	    Integer elevationInFile = getElevationIndexInNCFile(ncFile,
		    paramInfo.getElevation());

	    // variable holds the runtime and time indices.
	    // if value of either is not found, do not process the request.
	    // ok for the runtime to be null, as long as the time is found.
	    // once we have identified one of the two to be nf, no need to
	    // process any further.
	    Integer[] runtimeAndTimeIndexes = getRuntimeAndTimeIndexes(ncFile,
		    paramInfo);

	    boolean paramInfoMatchesFileData = paramInfoMatchesFileData(
		    runtimeAndTimeIndexes, elevationInFile);

	    if (paramInfoMatchesFileData) {
		parseFile(ncFile, parameterName, runtimeAndTimeIndexes,
			elevationInFile, data);
	    }

	} catch (Exception e) {
	    LOG.log(Level.SEVERE, "Error occurred during parseFiles.", e);
	} finally {
	    closeNetCdfFile(ncFile);
	}
	return data;
    }

    /**
     * I(Jeff) am guilty of ugly method names and repeated 'scaffolding code'
     * for the three submethods of this method. TODO needs cleanup! I did at
     * least describe the ugly methods. the description is the pseudocode I
     * wrote before the java code.
     */
    private Integer[] getRuntimeAndTimeIndexes(NetcdfFile ncFile,
	    ParamInformation paramInfo) {
	Integer[] result = new Integer[2];

	boolean hasRuntimeVariable = NetCdfUtil.getFileVariableByName(ncFile,
		NetCdfUtil.RUNTIME_VARIABLE_NAMES) == null ? false : true;

	if (paramInfo.getAnalysisTime() == null) {
	    result = getRuntimeAndTimeA(ncFile, paramInfo);
	} else if (hasRuntimeVariable) {
	    result = getRuntimeAndTimeB(ncFile, paramInfo);
	} else {
	    result = getRuntimeAndTimeC(ncFile, paramInfo);
	}

	return result;
    }

    // a. if no analysis time requested, get the time (and possibly the
    // runtime dim?).
    // we will look at all times for the closest match.
    // if we do not have a time match, return time dim not found. [null,
    // nf]
    // if we have a time match, return the time match dim.
    // if we have a runtime dim, also return the runtime dim match. [#,
    // #]
    // if we do not have a runtime dim, return the runtime dim null.
    // [null, #]
    //
    /**
     * @return two dimensional int array. first dim is analysisTime index,
     *         second dim is time index.
     */
    private Integer[] getRuntimeAndTimeA(NetcdfFile ncFile,
	    ParamInformation paramInfo) {

	Integer[] result = new Integer[2];

	// get time.
	Date time = paramInfo.getTime();

	if (time == null) {
	    LOG.info("Time parameter not provided.");
	    result[1] = NetCdfUtil.NOT_FOUND;
	    return result;
	}

	Variable timeVariable = NetCdfUtil.getFileVariableByName(ncFile,
		NetCdfUtil.TIME_VARIABLE_NAMES);
	if (timeVariable == null) {
	    LOG.info("Time variable not found in NetCDF file.");
	    result[1] = NetCdfUtil.NOT_FOUND;
	    return result;
	}

	Attribute timeUnitsAttribute = NetCdfUtil.getVariableAttributeByName(
		timeVariable, NetCdfUtil.TIME_UNIT_ATTRIBUTE_NAMES);
	if (timeUnitsAttribute == null) {
	    LOG.info("Time variable unit attribute not found in NetCDF file.");
	    result[1] = NetCdfUtil.NOT_FOUND;
	    return result;
	}

	double numHours = getTimeAsNumberOfHours(time, timeUnitsAttribute);

	// get runtime variable.
	boolean hasRuntimeVariable = NetCdfUtil.getFileVariableByName(ncFile,
		NetCdfUtil.RUNTIME_VARIABLE_NAMES) == null ? false : true;

	try {
	    if (hasRuntimeVariable) {
		result = findClosestIndexSecondDim(timeVariable.read(),
			numHours, null);
	    } else {
		result[0] = null;
		result[1] = findIndex(timeVariable.read(), numHours);
	    }

	    if (result[1] == NetCdfUtil.NOT_FOUND) {
		LOG.log(Level.INFO,
			"Time match not found in NetCDF file {0} {1}",
			new Object[] { time.toString(), ncFile.getTitle() });
	    }
	    return result;

	} catch (Exception e) {
	    LOG.warning("COULD NOT GET DATE ");
	    LOG.log(Level.SEVERE, e.getMessage(), e);
	    result[1] = NetCdfUtil.NOT_FOUND;
	    return result;
	}
    }

    // b. if analysis time requested, and we have a runtime dim, look
    // for runtime match.
    // if we have a runtime match, look for time dim within the runtime dim.
    // if we have a time match, return time dim of match. [#, #]
    // if we do not have a time match, return time dim of not found. [#,
    // nf]
    // if we do not have a runtime match, return runtime dim not found.
    // [nf, null]
    /**
     * @return two dimensional int array. first dim is analysisTime index,
     *         second dim is time index.
     */
    private Integer[] getRuntimeAndTimeB(NetcdfFile ncFile,
	    ParamInformation paramInfo) {

	Integer[] result = new Integer[2];

	Date analysisTime = paramInfo.getAnalysisTime();

	// get runtime variable.
	boolean hasRuntimeVariable = NetCdfUtil.getFileVariableByName(ncFile,
		NetCdfUtil.RUNTIME_VARIABLE_NAMES) == null ? false : true;

	if (analysisTime != null && hasRuntimeVariable) {
	    int runtimeIndex = getRuntimeDimensionIndexValue(paramInfo, ncFile);

	    if (runtimeIndex == NetCdfUtil.NOT_FOUND) {
		result[0] = NetCdfUtil.NOT_FOUND;
		return result;

	    }

	    // ok, now look for time match.
	    Date time = paramInfo.getTime();

	    if (time == null) {
		LOG.info("Time parameter not provided.");
		result[1] = NetCdfUtil.NOT_FOUND;
		return result;
	    }

	    Variable timeVariable = NetCdfUtil.getFileVariableByName(ncFile,
		    NetCdfUtil.TIME_VARIABLE_NAMES);
	    if (timeVariable == null) {
		LOG.info("Time variable not found in NetCDF file.");
		result[1] = NetCdfUtil.NOT_FOUND;
		return result;
	    }

	    Attribute timeUnitsAttribute = NetCdfUtil
		    .getVariableAttributeByName(timeVariable,
			    NetCdfUtil.TIME_UNIT_ATTRIBUTE_NAMES);
	    if (timeUnitsAttribute == null) {
		LOG.info("Time variable unit attribute not found in NetCDF file.");
		result[1] = NetCdfUtil.NOT_FOUND;
		return result;
	    }

	    double numHours = getTimeAsNumberOfHours(time, timeUnitsAttribute);

	    // only look in the found runtime dimension.
	    try {
		result = findClosestIndexSecondDim(timeVariable.read(),
			numHours, runtimeIndex);

		if (result[1] == NetCdfUtil.NOT_FOUND) {
		    LOG.log(Level.INFO,
			    "Time match not found in NetCDF file {0} {1}",
			    new Object[] { time.toString(), ncFile.getTitle() });
		}
		return result;

	    } catch (Exception e) {
		LOG.warning("COULD NOT GET DATE ");
		LOG.log(Level.SEVERE, e.getMessage(), e);
		result[1] = NetCdfUtil.NOT_FOUND;
		return result;
	    }
	}

	return result;
    }

    // c. if analysis time requested, and we do not have a runtime dim,
    // look at attributes for runtime match.
    // if we have an analysis time attribute match, look for time dim match.
    // if we have a time match, return time dim of match. [null, #]
    // if we do not have a time match, return time dim of not found.
    // [null, nf]
    // if we do not have a runtime match, return not found. [nf, null]
    /**
     * @return two dimensional int array. first dim is analysisTime index,
     *         second dim is time index.
     */
    private Integer[] getRuntimeAndTimeC(NetcdfFile ncFile,
	    ParamInformation paramInfo) {

	Integer[] result = new Integer[2];

	Date analysisTime = paramInfo.getAnalysisTime();

	Date analysisTimeInFile = getAnalysisTimeFromTauVariable(ncFile);

	if (analysisTimeInFile == null) {
	    analysisTimeInFile = getAnalysisTimeFromGlobalAttribute(ncFile);
	}

	// if we have analysis time match in file, look for time match.
	if (analysisTime != null && analysisTimeInFile != null
		&& analysisTime.compareTo(analysisTimeInFile) == 0) {
	    Date time = paramInfo.getTime();

	    if (time == null) {
		LOG.info("Time parameter not provided.");
		result[1] = NetCdfUtil.NOT_FOUND;
		return result;
	    }

	    Variable timeVariable = NetCdfUtil.getFileVariableByName(ncFile,
		    NetCdfUtil.TIME_VARIABLE_NAMES);
	    if (timeVariable == null) {
		LOG.info("Time variable not found in NetCDF file.");
		result[1] = NetCdfUtil.NOT_FOUND;
		return result;
	    }

	    Attribute timeUnitsAttribute = NetCdfUtil
		    .getVariableAttributeByName(timeVariable,
			    NetCdfUtil.TIME_UNIT_ATTRIBUTE_NAMES);
	    if (timeUnitsAttribute == null) {
		LOG.info("Time variable unit attribute not found in NetCDF file.");
		result[1] = NetCdfUtil.NOT_FOUND;
		return result;
	    }

	    double numHours = getTimeAsNumberOfHours(time, timeUnitsAttribute);

	    try {
		result[0] = null;
		result[1] = findClosestIndex(timeVariable.read(), numHours);

		if (result[1] == NetCdfUtil.NOT_FOUND) {
		    LOG.log(Level.INFO,
			    "Time match not found in NetCDF file {0} {1}",
			    new Object[] { time.toString(), ncFile.getTitle() });
		}
		return result;

	    } catch (Exception e) {
		LOG.warning("COULD NOT GET DATE ");
		LOG.log(Level.SEVERE, e.getMessage(), e);
		result[1] = NetCdfUtil.NOT_FOUND;
		return result;
	    }
	}

	return result;
    }

    private double getTimeAsNumberOfHours(Date time,
	    Attribute timeUnitsAttribute) {
	Date startTime = getDateFromString(timeUnitsAttribute.getStringValue());
	DateTime requestedDateTime = new DateTime(time.getTime());
	DateTime timeOrigin = new DateTime(startTime.getTime());
	Hours hoursBetween = Hours.hoursBetween(timeOrigin, requestedDateTime);
	return hoursBetween.getHours();
    }

    private int getRuntimeDimensionIndexValue(ParamInformation paramInfo,
	    NetcdfFile ncFile) {
	return getVariableTargetValueIndex(ncFile,
		NetCdfUtil.RUNTIME_VARIABLE_NAMES,
		NetCdfUtil.OUTPUT_DATE_FORMAT.format(paramInfo
			.getAnalysisTime()));
    }

    private boolean paramInfoMatchesFileData(Integer[] runtimeAndTime,
	    Integer elevation) {

	if ((runtimeAndTime[0] != null && runtimeAndTime[0] == NetCdfUtil.NOT_FOUND)
		|| (runtimeAndTime[1] != null && runtimeAndTime[1] == NetCdfUtil.NOT_FOUND)) {
	    return false;
	}

	if (elevation != null && elevation == NetCdfUtil.NOT_FOUND) {
	    return false;
	}

	return true;
    }

    /**
     * Get the index for the elevation dimension. Currently returns the closest.
     * 
     * -1 means the elevation wasn't found.
     * 
     * protected (instead of private) only to put under test.
     */
    protected Integer getElevationIndexInNCFile(NetcdfFile ncfile,
	    Double elevation) {
	if (elevation == null) {
	    LOG.info("Elevation parameter was not provided.");
	    return null;
	}

	// TODO: what if the file has a different elevation name....?
	Variable elevationVariable = getVariableByName(ncfile, "depth(");

	if (elevationVariable == null) {
	    LOG.log(Level.WARNING,
		    "NCFILE {0} does not have a recognized depth variable.",
		    ncfile.getTitle());
	    return -1;
	}

	try {
	    int i = findIndex(elevationVariable.read(), elevation);
	    if (i == -1) {
		LOG.log(Level.WARNING,
			"COULD NOT FIND REQUESTED ELEVATION IN FILE {0} REQUESTED ELEVATION WAS  {1}",
			new Object[] { ncfile.getTitle(), elevation });
	    }
	    return i;
	} catch (IOException e) {
	    LOG.log(Level.INFO, "COULD NOT GET ELEVATION {0}", e.getMessage());
	    return -1;
	}
    }

    public SortedSet<Double> getElevations() {
	/*
	 * TODO: a lot of code duplication here between getTimeString and
	 * getElevation string but I'm in a hurry, try to consolidate some
	 * common logic in the future. Also a lot of duplication in
	 * TimeLayerInNCFile and ElevationLayerInNCFile
	 */
	SortedSet<Double> elevations = new TreeSet<Double>();
	NetcdfFile ncFile = null;
	try {
	    ncFile = NetcdfDataset.openFile(file.getAbsolutePath(), null);
	    Variable elevationVariable = NetCdfUtil.getFileVariableByName(
		    ncFile, NetCdfUtil.DEPTH_VARIABLE_NAMES);

	    if (elevationVariable != null) {
		IndexIterator ii = elevationVariable.read().getIndexIterator();
		while (ii.hasNext()) {
		    double elevation = ii.getDoubleNext();
		    elevations.add(elevation);
		}
	    }
	} catch (IOException e) {
	    LOG.log(Level.SEVERE, "Error occurred during getElevations.", e);
	    return null;
	} finally {
	    closeNetCdfFile(ncFile);
	}

	return elevations;
    }

    /**
     * Gets the default value for the elevation dimension. Returns NULL if no
     * elevations found
     * 
     * @return Double
     */
    public Double getDefaultElevation() {
	SortedSet<Double> elevations = getElevations();
	if (elevations != null && elevations.size() > 0) {
	    return elevations.first();
	} else {
	    return null;
	}
    }

    public String getElevationString() {
	return NetCdfUtil.getDomainListAsString(getElevations());
    }

    public String getAnalysisTimeString() {
	String result = null;
	NetcdfFile ncFile = null;
	try {
	    ncFile = NetcdfDataset.openFile(file.getAbsolutePath(), null);
	    List<Date> analysisTimes = getAnalysisTimesInNcFile(ncFile);
	    if (analysisTimes != null) {
		List<String> analysisTimesAsStrings = new ArrayList<String>();
		for (Date analysisTime : analysisTimes) {
		    analysisTimesAsStrings.add(NetCdfUtil.OUTPUT_DATE_FORMAT
			    .format(analysisTime));
		}
		result = NetCdfUtil
			.getDomainListAsString(analysisTimesAsStrings);
	    }
	} catch (IOException e) {
	    LOG.log(Level.WARNING, "Problem getting analysis time(s) for "
		    + file.getAbsolutePath(), e);
	    result = null;
	} finally {
	    closeNetCdfFile(ncFile);
	}

	return result;
    }

    /**
     * a. first check for usable runtime variable b. if not found, then check
     * for usable tau variable time_origin attribute c. if not found, then check
     * for global time_origin attribute
     */
    private List<Date> getAnalysisTimesInNcFile(NetcdfFile ncFile) {
	List<Date> result = null;

	result = getAnalysisTimesFromRuntimeVariable(ncFile);

	if (result == null) {
	    Date fromTau = getAnalysisTimeFromTauVariable(ncFile);
	    if (fromTau != null) {
		result = new ArrayList<Date>();
		result.add(fromTau);
	    }
	}

	if (result == null) {
	    Date fromGlobal = getAnalysisTimeFromGlobalAttribute(ncFile);
	    if (fromGlobal != null) {
		result = new ArrayList<Date>();
		result.add(fromGlobal);
	    }
	}

	return result;
    }

    private List<Date> getAnalysisTimesFromRuntimeVariable(NetcdfFile ncFile) {
	List<Date> result = null;

	Variable runtimeVariable = NetCdfUtil.getFileVariableByName(ncFile,
		NetCdfUtil.RUNTIME_VARIABLE_NAMES);
	if (runtimeVariable != null) {
	    List<Object> runtimes = NetCdfUtil
		    .getVariableCachedData(runtimeVariable);
	    if (runtimes.size() > 0) {
		result = new ArrayList<Date>();
		for (Object runtime : runtimes) {
		    result.add(NetCdfUtil
			    .getDateFromOutputStyleString((String) runtime));

		}
	    }

	}

	return result;
    }
    
    /**
     * protected instead of private only for unit test.
     */
    protected Date getAnalysisTimeFromTauVariable(NetcdfFile ncFile) {
	Date result = null;

	Variable tauVariable = NetCdfUtil.getFileVariableByName(ncFile,
		NetCdfUtil.TAU_VARIABLE_NAMES);
	if (tauVariable == null) {
	    LOG.log(Level.INFO, "Tau variable not found for {0}",
		    ncFile.getLocation());
	} else {
	    Attribute timeOriginAttribute = NetCdfUtil
		    .getVariableAttributeByName(tauVariable,
			    NetCdfUtil.TIME_ORIGIN_ATTRIBUTE_NAMES);
	    if (timeOriginAttribute == null) {
		LOG.log(Level.INFO,
			"Tau variable''s time_origin attribute not found for {0}",
			ncFile.getLocation());
	    } else {
		result = NetCdfUtil.getDateFromString(timeOriginAttribute
			.getStringValue());
	    }
	}

	return result;
    }

    private Date getAnalysisTimeFromGlobalAttribute(NetcdfFile ncFile) {
	Date result = null;

	Attribute globalTimeOriginAttribute = ncFile
		.findGlobalAttribute("time_origin");
	if (globalTimeOriginAttribute == null) {
	    LOG.log(Level.INFO,
		    "Global time_origin attribute not found for {0}",
		    ncFile.getLocation());
	} else {
	    result = NetCdfUtil.getDateFromString(globalTimeOriginAttribute
		    .getStringValue());
	}

	return result;
    }

    /**
     * get the desired index of variable based on the String target value.
     * 
     * @return index (return NetCdfUtil.NOT_FOUND if not found)
     */
    private int getVariableTargetValueIndex(NetcdfFile ncFile,
	    Collection<String> variableNames, String targetValue) {

	String variableNameForLog = variableNames.toString();
	String fileDescriptionForLog = ncFile.getTitle();

	if (targetValue == null) {
	    LOG.log(Level.INFO, "{0} target value was not provided.",
		    variableNameForLog);
	    return NetCdfUtil.NOT_FOUND;
	}

	Variable variable = NetCdfUtil.getFileVariableByName(ncFile,
		variableNames);

	if (variable == null) {
	    LOG.log(Level.INFO,
		    "File does not have recognized {0} variable. {1}",
		    new Object[] { variableNameForLog, fileDescriptionForLog });
	    return NetCdfUtil.NOT_FOUND;
	}

	int index = NetCdfUtil.getIndexOfMatchInVariableCachedData(variable,
		targetValue);

	if (index == NetCdfUtil.NOT_FOUND) {
	    LOG.log(Level.INFO,
		    "No match for {0} targetValue {1} in file. {2}",
		    new Object[] { variableNameForLog, targetValue,
			    fileDescriptionForLog });
	}

	return index;
    }

    /**
     * Get a list of the time values from the file.
     * 
     * @return SortedSet<String>
     */
    public SortedSet<String> getTimes() {
	SortedSet<String> timeStrings = new TreeSet<String>();
	NetcdfFile ncFile = null;
	try {
	    ncFile = NetcdfDataset.openFile(file.getAbsolutePath(), null);

	    Variable timeVariable = NetCdfUtil.getFileVariableByName(ncFile,
		    NetCdfUtil.TIME_VARIABLE_NAMES);
	    if (timeVariable == null) {
		LOG.info("Time variable not found in NetCDF file.");
		return null;
	    }

	    Attribute timeUnitsAttribute = NetCdfUtil
		    .getVariableAttributeByName(timeVariable,
			    NetCdfUtil.TIME_UNIT_ATTRIBUTE_NAMES);
	    if (timeUnitsAttribute == null) {
		LOG.info("Time variable unit attribute not found in NetCDF file.");
	    } else {
		Date startTime = getDateFromString(timeUnitsAttribute
			.getStringValue());

		Array timeArray = timeVariable.read();
		IndexIterator ii = timeArray.getIndexIterator();
		while (ii.hasNext()) {
		    double time = ii.getDoubleNext();
		    /*
		     * The value here is hours since 2000-01-01 00:00:00, which
		     * is stored in startTime, we will use some joda magic to
		     * add this number of hours and see what date we get
		     */
		    org.joda.time.DateTime startDateTime = new org.joda.time.DateTime(
			    startTime.getTime());
		    org.joda.time.DateTime actualTime = startDateTime
			    .plusHours((int) Math.round(time));
		    timeStrings.add(NetCdfUtil.OUTPUT_DATE_FORMAT
			    .format(actualTime.toDate()));
		}
	    }
	} catch (IOException e) {
	    LOG.log(Level.SEVERE, "Error occurred during getTimes.", e);
	    return null;
	} finally {
	    closeNetCdfFile(ncFile);
	}

	return timeStrings;
    }

    /**
     * This function is designed to get a geoserver formatted time string (some
     * times seperated by commas, for now, this will probably change in the
     * future) for the getCapabilities information
     */
    public String getTimeString() {
	return NetCdfUtil.getDomainListAsString(getTimes());
    }

    /**
     * Get the first time in the file.
     * 
     * @return String
     */
    public String getTimeMinimum() {
	SortedSet<String> times = getTimes();

	if (null != times && !times.isEmpty()) {
	    return times.first();
	}

	return null;
    }

    /**
     * Get the last time in the file.
     * 
     * @return String
     */
    public String getTimeMaximum() {
	SortedSet<String> times = getTimes();

	if (null != times && !times.isEmpty()) {
	    return times.last();
	}

	return null;
    }

    private Date getDateFromString(String dateString) {
	/*
	 * TODO: In my experience NAVO netcdf time values are always
	 * "hour since" *some time*, but we need to confirm this with a subject
	 * matter expert on navo data
	 */

	dateString = dateString.replaceAll("\\p{Alpha}", "").trim();

	// Note: order is important!
	List<SimpleDateFormat> netCdfFormats = Arrays.asList(
		NetCdfUtil.NETCDF_DATE_FORMAT_1,
		NetCdfUtil.NETCDF_DATE_FORMAT_2);

	for (SimpleDateFormat netCdfFormat : netCdfFormats) {
	    try {
		return netCdfFormat.parse(dateString);
	    } catch (ParseException e) {
		if (netCdfFormat.equals(NetCdfUtil.NETCDF_DATE_FORMAT_1)) {
		    LOG.info("Date not in format 1. Will try format 2.");
		} else {
		    LOG.info("Date not in format 2.  Set date to default.");
		}
	    }
	}
	return new Date(0);
    }

    private Variable getVariableByName(NetcdfFile ncFile, String variable) {
	Variable timeVariable = null;

	if (ncFile != null) {
	    for (Variable var : ncFile.getVariables()) {
		if (var.getNameAndDimensions().toLowerCase().contains(variable)) {
		    timeVariable = var;
		    break;
		}
	    }
	}

	return timeVariable;
    }

    /**
     * limitation: works only for 1 dim array.
     */
    private int findIndex(Array a, Double val) {
	IndexIterator ii = a.getIndexIterator();
	int index = -1;
	while (ii.hasNext()) {
	    Double currentVal = ii.getDoubleNext();
	    int currentPos = ii.getCurrentCounter()[0];
	    if (currentVal.equals(val)) {
		index = currentPos;
		break;
	    }
	}

	return index;
    }

    /**
     * limitation: works only for 1 dim array.
     */
    private int findClosestIndex(Array a, Double val) {
	IndexIterator ii = a.getIndexIterator();
	double minValueDiff = Double.MAX_VALUE;
	int minValueIndex = -1;
	while (ii.hasNext()) {
	    Double currentVal = ii.getDoubleNext();
	    int currentPos = ii.getCurrentCounter()[0];
	    if (minValueDiff > Math.abs(currentVal - val)) {
		minValueDiff = Math.abs(currentVal - val);
		minValueIndex = currentPos;
	    }
	}

	return minValueIndex;
    }

    // cases to consider:
    // a. two dim array, first dim value specified, look only at that dim.
    // b. two dim array, first dim not specified, look through all values in
    // order. if more than one value match, choose latest one.
    // for a., we already know first array dim value.
    // for b. we are also interested in the first array dim value.
    /**
     * limitation: works only for 2 dim arrays.
     */
    private Integer[] findClosestIndexSecondDim(Array a, Double val,
	    Integer runtimeDim) {
	Integer[] result = new Integer[2];
	result[0] = NetCdfUtil.NOT_FOUND;
	result[1] = NetCdfUtil.NOT_FOUND;

	double minValueDiff = Double.MAX_VALUE;
	IndexIterator ii = a.getIndexIterator();
	while (ii.hasNext()) {
	    Double currentVal = ii.getDoubleNext();
	    int[] currentPos = ii.getCurrentCounter();
	    if ((runtimeDim == null || currentPos[0] == runtimeDim)
		    && (minValueDiff >= Math.abs(currentVal - val))) {
		// include equal here to let later match overwrite earlier.
		minValueDiff = Math.abs(currentVal - val);
		result[0] = currentPos[0];
		result[1] = currentPos[1];
	    }
	}

	return result;
    }

    public boolean validateNetCdfFile(String absoluteFilePath) {
	boolean valid = false;
	try {
	    valid = NetcdfFile.canOpen(absoluteFilePath);
	} catch (IOException e) {
	    LOG.log(Level.SEVERE, e.getMessage(), e);
	}
	return valid;
    }

    private void parseFile(NetcdfFile ncFile, String parameterName,
	    Integer[] runtimeAndTimeIndexes, Integer level,
	    GrdDataEncapsulator data) throws IOException, InvalidRangeException {
	LOG.log(Level.INFO, "parseFile method for " + ncFile.getLocation());

	Variable variable = null;

	// look for the requested variable in the ncfile.
	for (Variable t : ncFile.getVariables()) {
	    if (t.getShortName().equals(parameterName)) {
		variable = t;
		break;
	    }
	}

	if (variable == null) {
	    LOG.log(Level.WARNING, "NetCDF does not contain parameter: {0}",
		    parameterName);
	    return;
	}

	// Set some 'handling variables' to defaults and then override if
	// present in nc file's variable's attributes.
	float scaleFactor = 1.0f;
	float addOffset = 0.0f;
	float missingValue = Float.NaN;
	float fillValue = Float.NaN;
	List<Attribute> attributeList = variable.getAttributes();
	for (Attribute attribute : attributeList) {
	    if (attribute.getName().equals("scale_factor")) {
		scaleFactor = attribute.getNumericValue().floatValue();
	    }
	    if (attribute.getName().equals("add_offset")) {
		addOffset = attribute.getNumericValue().floatValue();
	    }
	    if (attribute.getName().equals("_FillValue")) {
		fillValue = attribute.getNumericValue().floatValue();
	    }
	    if (attribute.getName().equals("missing_value")) {
		missingValue = attribute.getNumericValue().floatValue();
	    }
	}

	// We assume that the requested variable is at least 3
	// dimensional(time, lat, lon). We are also prepared to handle
	// dimensions for depth and runtime if present.
	// TODO: consider adding validation here, so we can react if our
	// assumption is not correct for the input data.
	String[] range = new String[variable.getDimensions().size()];

	// If variable has time, elevation or runtime as one of its dimensions
	// and we have usable values for the dimensions, set them.
	int timeIndex = NetCdfUtil.getVariableDimensionIndexByName(variable,
		NetCdfUtil.TIME_VARIABLE_NAMES);
	if (timeIndex != NetCdfUtil.NOT_FOUND) {
	    range[timeIndex] = String.valueOf(runtimeAndTimeIndexes[1]);
	}

	int depthIndex = NetCdfUtil.getVariableDimensionIndexByName(variable,
		NetCdfUtil.DEPTH_VARIABLE_NAMES);
	if (depthIndex != NetCdfUtil.NOT_FOUND) {
	    range[depthIndex] = String.valueOf(level);
	}

	int runtimeIndex = NetCdfUtil.getVariableDimensionIndexByName(variable,
		NetCdfUtil.RUNTIME_VARIABLE_NAMES);
	if ((runtimeIndex != NetCdfUtil.NOT_FOUND)
		&& (runtimeAndTimeIndexes[0] != null && runtimeAndTimeIndexes[0] != NetCdfUtil.NOT_FOUND)) {
	    range[runtimeIndex] = String.valueOf(runtimeAndTimeIndexes[0]);
	}

	// Now we need to get the remaining two range indexes, lat and lon.

	// The range index for lon. Get the target indexes in the nc file that
	// correspond with the GridDataEncapsulator's desired lons. We need to
	// keep the target's position, for later, so we know where to write.
	List<Double> lonList = data.getDesiredLons();
	Array lonValuesFromNcFile = NetCdfUtil.getFileVariableByName(ncFile,
		NetCdfUtil.LON_VARIABLE_NAMES).read();
	Map<Integer, Integer> lonImagePositionAndIndexInFile = getTargetIndexes(
		lonList, lonValuesFromNcFile, true);

	Integer lonMinIndexOfInterestInFile = Collections
		.min(lonImagePositionAndIndexInFile.values());
	Integer lonMaxIndexOfInterestInFile = Collections
		.max(lonImagePositionAndIndexInFile.values());

	// Now have enough info to set the range index for lon.
	String desiredLonRangeInFortran90Format = lonMinIndexOfInterestInFile
		+ ":" + lonMaxIndexOfInterestInFile;
	int variableLonIndex = NetCdfUtil.getVariableDimensionIndexByName(
		variable, NetCdfUtil.LON_VARIABLE_NAMES);
	range[variableLonIndex] = desiredLonRangeInFortran90Format;

	// The range index for lat. Get the target indexes in the nc file that
	// correspond with the GridDataEncapsulator's desired lats. We need to
	// keep the target's position, for later, so we know where to write.
	List<Double> latList = data.getDesiredLats();
	Array latValuesFromNcFile = NetCdfUtil.getFileVariableByName(ncFile,
		NetCdfUtil.LAT_VARIABLE_NAMES).read();
	Map<Integer, Integer> latImagePositionAndIndexInFile = getTargetIndexes(
		latList, latValuesFromNcFile, false);

	Integer latMinIndexOfInterestInFile = Collections
		.min(latImagePositionAndIndexInFile.values());
	Integer latMaxIndexOfInterestInFile = Collections
		.max(latImagePositionAndIndexInFile.values());

	// Now have enough info to set the range index for lat.
	String desiredLatRangeInFortran90Format = latMinIndexOfInterestInFile
		+ ":" + latMaxIndexOfInterestInFile;
	int variableLatIndex = NetCdfUtil.getVariableDimensionIndexByName(
		variable, NetCdfUtil.LAT_VARIABLE_NAMES);
	range[variableLatIndex] = desiredLatRangeInFortran90Format;

	// Now all range variables are known, can write the section selector
	// and read from the nc file using the range.
	String sectionSelector = buildReadParameter(range.length, range);
	Array array = variable.read(sectionSelector);

	// Now we have read all the data we need from the nc file, so we are
	// ready to start writing it to the image array.
	// We do not necessarily need all data we retrieved, only the data for
	// the target image positions.
	// To get the correct result, we base our outer loop on whichever is the
	// variable's outer dimension, lat or lon.
	boolean isLatOuterLon = variableLatIndex < variableLonIndex;

	if (isLatOuterLon) {
	    int rowWidth = lonMaxIndexOfInterestInFile
		    - lonMinIndexOfInterestInFile + 1;
	    for (Entry<Integer, Integer> latEntry : latImagePositionAndIndexInFile
		    .entrySet()) {
		// Determine the 'row' in the input array for the outer loop.
		// Could use ucar reduce, expensive, let's just use math.
		int row = latEntry.getValue() - latMinIndexOfInterestInFile;

		for (Entry<Integer, Integer> lonEntry : lonImagePositionAndIndexInFile
			.entrySet()) {
		    // Within the 'lat row', determine which 'lon column'.
		    int column = lonEntry.getValue()
			    - lonMinIndexOfInterestInFile;

		    // Calculate the specific array element.
		    int element = (row * rowWidth) + column;

		    float dataValue = array.getFloat(element);
		    float adjustedValue = (dataValue * scaleFactor) + addOffset;

		    if (dataValue == missingValue || dataValue == fillValue
			    || adjustedValue == fillValue
			    || adjustedValue == missingValue) {
			continue;
		    }

		    if (LOG.isLoggable(Level.FINEST)) {
			LOG.finest(String
				.format("PUTTING DATA IN FOR POINT %d %d IN DA FILE, THE VALUE IS %f",
					lonEntry.getKey(), (latList.size() - 1)
						- latEntry.getKey(),
					adjustedValue));
		    }
		    data.getImageArray()[lonEntry.getKey()][(latList.size() - 1)
			    - latEntry.getKey()] = adjustedValue;
		}
	    }
	} else {
	    int rowWidth = latMaxIndexOfInterestInFile
		    - latMinIndexOfInterestInFile + 1;
	    for (Entry<Integer, Integer> lonEntry : lonImagePositionAndIndexInFile
		    .entrySet()) {
		// Determine the 'row' in the input array for the outer loop.
		// Could use ucar reduce, expensive, let's just use math.
		int row = lonEntry.getValue() - lonMinIndexOfInterestInFile;

		for (Entry<Integer, Integer> latEntry : latImagePositionAndIndexInFile
			.entrySet()) {
		    // Within the 'lon row', determine which 'lat column'.
		    int column = latEntry.getValue()
			    - latMinIndexOfInterestInFile;

		    // Calculate the specific array element.
		    int element = (row * rowWidth) + column;

		    float f = array.getFloat(element);
		    float adjustedValue = (f * scaleFactor) + addOffset;

		    if (f == missingValue || f == fillValue
			    || adjustedValue == fillValue
			    || adjustedValue == missingValue) {
			continue;
		    }

		    if (LOG.isLoggable(Level.FINEST)) {
			LOG.finest(String
				.format("PUTTING DATA IN FOR POINT %d %d IN DA FILE, THE VALUE IS %f",
					lonEntry.getKey(), (latList.size() - 1)
						- latEntry.getKey(),
					adjustedValue));
		    }
		    data.getImageArray()[lonEntry.getKey()][(latList.size() - 1)
			    - latEntry.getKey()] = adjustedValue;
		}
	    }
	}
    }

    private Map<Integer, Integer> getTargetIndexes(List<Double> targetValues,
	    Array lookupValues, boolean isLongitude) {
	Map<Integer, Integer> desiredIndexes = new HashMap<Integer, Integer>();

	for (int targetValueIndex = 0; targetValueIndex < targetValues.size(); targetValueIndex++) {
	    Double targetValue = targetValues.get(targetValueIndex);
	    // If the file uses a (0 to 360) world grid and the requested point
	    // is in the western hemisphere, convert the longitude
	    if (isLongitude && isLongitudeIn0to360()
		    && targetValues.get(targetValueIndex) < 0) {
		targetValue += 360;
	    }

	    int lookupValueIndex = this.indexingStrategy.getCoordinateIndex(
		    lookupValues, targetValue);
	    if (lookupValueIndex != NetCdfUtil.NOT_FOUND) {
		desiredIndexes.put(targetValueIndex, lookupValueIndex);
	    }
	}

	return desiredIndexes;
    }

    /**
     * protected instead of private only so can unit test.
     */
    protected String buildReadParameter(int dimensionSize, String[] range) {
	StringBuffer sb = new StringBuffer();
	for (int sbPos = 0; sbPos < dimensionSize; sbPos++) {
	    sb.append(range[sbPos]);
	    sb.append(", ");
	}

	return sb.substring(0, sb.length() - 2);
    }

    private float[] getMinAndMaxFromArray(Array a) {
	Float max = null;
	Float min = null;
	IndexIterator ii = a.getIndexIterator();
	while (ii.hasNext()) {
	    float f = ii.getFloatNext();
	    if (min == null || f < min) {
		min = f;
	    }
	    if (max == null || f > max) {
		max = f;
	    }
	}

	return new float[] { min, max };
    }

    private void closeNetCdfFile(NetcdfFile file) {
	if (file != null) {
	    try {
		file.close();
	    } catch (IOException e) {
		LOG.log(Level.SEVERE, "Close NetCDF file: " + e.getMessage(), e);
	    }
	}
    }
}
