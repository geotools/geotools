/* Copyright (c) 2001 - 2013 OpenPlans - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geotools.gce.netcdf;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;
import org.geotools.util.logging.Logging;

import ucar.ma2.IndexIterator;
import ucar.nc2.Attribute;
import ucar.nc2.Dimension;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;

public class NetCdfUtil {

    private static final Logger LOG = Logging.getLogger(NetCdfUtil.class);

    public static final float MIN_LON = -180.0f;
    public static final float MAX_LON = 180.0f;
    public static final float MIN_LAT = -90.0f;
    public static final float MAX_LAT = 90.0f;

    public static final int BOUNDS_INDEX_MIN_LONGITUDE = 0;
    public static final int BOUNDS_INDEX_MAX_LONGITUDE = 1;
    public static final int BOUNDS_INDEX_MIN_LATITUDE = 2;
    public static final int BOUNDS_INDEX_MAX_LATITUDE = 3;

    public static final int NOT_FOUND = -1;

    /**
     * The delimiter used for representing a domain list as a string.
     */
    public static final String LIST_AS_STRING_DELIMITER = ",";

    public static final SimpleDateFormat NETCDF_DATE_FORMAT_1 = new SimpleDateFormat(
	    "yyyy-MM-dd HH:mm:ss");

    public static final SimpleDateFormat NETCDF_DATE_FORMAT_2 = new SimpleDateFormat(
	    "yyyy-MM-dd HH:mm");

    public static final SimpleDateFormat OUTPUT_DATE_FORMAT = new SimpleDateFormat(
	    "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    private static final TimeZone NETCDF_TIME_ZONE = TimeZone
	    .getTimeZone("GMT");

    static {
	NETCDF_DATE_FORMAT_1.setTimeZone(NETCDF_TIME_ZONE);
	NETCDF_DATE_FORMAT_2.setTimeZone(NETCDF_TIME_ZONE);
	OUTPUT_DATE_FORMAT.setTimeZone(NETCDF_TIME_ZONE);
    }

    /**
     * Collections of names that we will watch for in NetCDF files. TODO: add
     * more for other variables and units.
     */
    public static final Collection<String> LON_VARIABLE_NAMES = Arrays.asList(
	    "lon", "longitude");
    public static final Collection<String> LAT_VARIABLE_NAMES = Arrays.asList(
	    "lat", "latitude");
    public static final Collection<String> DEPTH_VARIABLE_NAMES = Arrays
	    .asList("depth");
    public static final Collection<String> RUNTIME_VARIABLE_NAMES = Arrays
	    .asList("runtime");
    public static final Collection<String> TIME_VARIABLE_NAMES = Arrays
	    .asList("time");
    public static final Collection<String> TAU_VARIABLE_NAMES = Arrays
	    .asList("tau");
    public static final Collection<String> WATER_TEMPERATURE_VARIABLE_NAMES = Arrays
	    .asList("water_temp");
    public static final Collection<String> TIME_ORIGIN_ATTRIBUTE_NAMES = Arrays
	    .asList("time_origin");
    public static final Collection<String> TIME_UNIT_ATTRIBUTE_NAMES = Arrays
	    .asList("units", "time_units");

    /**
     * No known rule for which value to return if more than one variable name
     * found. Current implementation will return the first one found.
     */
    public static Variable getFileVariableByName(NetcdfFile netCdfFile,
	    Collection<String> variableNames) {
	if (netCdfFile != null) {
	    Variable result = null;
	    for (String variableName : variableNames) {
		result = netCdfFile.findVariable(variableName);
		if (result != null) {
		    return result;
		}
	    }
	}

	return null;
    }

    /**
     * No known rule for which value to return if more than one attribute name
     * found. Current implementation will return the first one found.
     */
    public static Attribute getVariableAttributeByName(Variable var,
	    Collection<String> attributeNames) {
	if (var != null) {
	    for (Attribute attribute : var.getAttributes()) {
		for (String attributeName : attributeNames) {
		    if (attributeName.equalsIgnoreCase(attribute.getName())) {
			return attribute;
		    }
		}
	    }
	}

	return null;
    }

    /**
     * No known rule for which value to return if more than one dimension name
     * found. Current implementation will return the first one found. Returns -1
     * if not found.
     */
    public static int getVariableDimensionIndexByName(Variable var,
	    Collection<String> dimensionNames) {
	if (var != null) {
	    int dimensionIndex = 0;
	    for (Dimension dimension : var.getDimensions()) {
		for (String dimensionName : dimensionNames) {
		    if (dimension.getName().equals(dimensionName)) {
			return dimensionIndex;
		    }
		}
		dimensionIndex++;
	    }
	}

	return NOT_FOUND;
    }

    /**
     * limitation: so far, only tested for variable with 1D String array.
     */
    public static List<Object> getVariableCachedData(Variable var) {
	List<Object> result = new ArrayList<Object>();
	if (var != null) {
	    try {
		IndexIterator iter = var.read().getIndexIterator();
		while (iter.hasNext()) {
		    result.add(iter.next());
		}
	    } catch (IOException e) {
		LOG.info("Problem reading cached data for " + var.getName());
	    }
	}

	return result;
    }

    /**
     * limitation: so far, only tested for variable with 1D String array.
     */
    public static String getVariableCachedDataAsString(Variable var) {
	String result = null;
	Collection<Object> cachedData = getVariableCachedData(var);
	if (cachedData != null) {
	    result = getDomainListAsString(cachedData);
	}

	return result;
    }

    /**
     * limitation: so far, only tested for variable with 1D String array.
     */
    public static int getIndexOfMatchInVariableCachedData(Variable var,
	    String targetValue) {
	if (var != null && targetValue != null) {
	    try {
		IndexIterator iter = var.read().getIndexIterator();
		while (iter.hasNext()) {
		    if (targetValue.equals(iter.next())) {
			return iter.getCurrentCounter()[0];
		    }
		}
	    } catch (IOException e) {
		LOG.info("Problem reading cached data for " + var.getName());
	    }
	}

	return NetCdfUtil.NOT_FOUND;
    }

    public static Date getDateFromString(String input) {
	Date result = null;

	// eh?
	String dateString = input.replaceAll("\\p{Alpha}", "").trim();

	// Note: order is important!
	List<SimpleDateFormat> netCdfFormats = Arrays.asList(
		NetCdfUtil.NETCDF_DATE_FORMAT_1,
		NetCdfUtil.NETCDF_DATE_FORMAT_2);

	for (SimpleDateFormat netCdfFormat : netCdfFormats) {
	    try {
		result = netCdfFormat.parse(dateString);
	    } catch (ParseException e) {
		LOG.warning("Unparseable date (NetCDF style) " + input);
	    }
	}

	return result;
    }

    public static Date getDateFromOutputStyleString(String input) {
	Date result = null;
	try {
	    result = OUTPUT_DATE_FORMAT.parse(input);
	} catch (ParseException e) {
	    LOG.warning("Unparseable date (Output style) " + input);
	}

	return result;
    }

    /**
     * Returns elements in a <code>Collection</code> joined together using the
     * <code>LIST_AS_STRING_DELIMITER</code> delimiter as a <code>String</code>.
     * 
     * @param collection
     *            the <code>Collection</code> whose elements will be joined
     *            together as a <code>String</code>
     * 
     * @return String contains a delimited <code>List</code>
     */
    public static String getDomainListAsString(Collection<?> collection) {
	return StringUtils
		.join(collection.iterator(), LIST_AS_STRING_DELIMITER);
    }

}
