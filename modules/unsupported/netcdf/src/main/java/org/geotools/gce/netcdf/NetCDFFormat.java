/* Copyright (c) 2001 - 2013 OpenPlans - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geotools.gce.netcdf;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.imageio.GeoToolsWriteParams;
import org.geotools.factory.Hints;
import org.geotools.parameter.DefaultParameterDescriptor;
import org.geotools.parameter.DefaultParameterDescriptorGroup;
import org.geotools.parameter.ParameterGroup;
import org.opengis.coverage.grid.Format;
import org.opengis.coverage.grid.GridCoverageWriter;
import org.opengis.filter.Filter;
import org.opengis.parameter.GeneralParameterDescriptor;
import org.opengis.parameter.ParameterDescriptor;

public class NetCDFFormat extends AbstractGridFormat implements Format {

    private static final String UNSUPPORTED_WRITING_OPERATION_MESSAGE = "Writing operations are not implemented";
    private static final String CANNOT_CREATE_READER_MESSAGE = "Cannot create Netcdf Reader without a directory of netcdf files";
    private static final String NETCDF_NAME = "NetCDF";
    
    public static  final String ANALYSIS_TIME_NAME = "ANALYSIS_TIME";

    /* The NetCDF Parameter component */
    public static final ParameterDescriptor<String> PARAMETER = new DefaultParameterDescriptor<String>(
	    "NETCDFPARAMETER", String.class, null, null);
    /* The WMS time component */
    @SuppressWarnings("rawtypes")
    public static final ParameterDescriptor<List> TIME = DefaultParameterDescriptor.create("TIME",
	    "A list of time objects", List.class, null, false);
    /* The WMS elevation component */
    @SuppressWarnings("rawtypes")
    public static final ParameterDescriptor<List> ELEVATION = DefaultParameterDescriptor.create(
	    "ELEVATION", "An elevation value", List.class, null, false);
    /* The custom dimension analysis_time component */
    @SuppressWarnings("rawtypes")
    public static final ParameterDescriptor<List> ANALYSIS_TIME = DefaultParameterDescriptor
	    .create(ANALYSIS_TIME_NAME, "Model run time", List.class, null, false);
    /* The CQL filter contents */
    public static final ParameterDescriptor<Filter> FILTER = new DefaultParameterDescriptor<Filter>(
	    "Filter", Filter.class, null, null);

    public NetCDFFormat() {
	writeParameters = null;
	mInfo = new HashMap<String, String>();
	mInfo.put("name", NETCDF_NAME);
	mInfo.put("description", "NetCDF gridded data fomat");
	mInfo.put("vendor", "Geocent");
	mInfo.put("version", "0.2");
	readParameters = new ParameterGroup(new DefaultParameterDescriptorGroup(mInfo,
		new GeneralParameterDescriptor[] { READ_GRIDGEOMETRY2D, PARAMETER, ELEVATION, TIME,
			ANALYSIS_TIME, FILTER }));
    }

    @Override
    public boolean accepts(Object arg0, Hints arg1) {
	return true;
    }

    @Override
    public GeoToolsWriteParams getDefaultImageIOWriteParameters() {
	throw new UnsupportedOperationException(UNSUPPORTED_WRITING_OPERATION_MESSAGE);
    }

    @Override
    public AbstractGridCoverage2DReader getReader(Object arg0) {
	return getReader(arg0, null);
    }

    @Override
    public AbstractGridCoverage2DReader getReader(Object arg0, Hints arg1) {
	if (arg0 instanceof File) {
	    File path = (File) arg0;
	    return new NetCDFReader(path, arg1);
	} else {
	    throw new UnsupportedOperationException(CANNOT_CREATE_READER_MESSAGE);
	}
    }

    @Override
    public GridCoverageWriter getWriter(Object arg0) {
	throw new UnsupportedOperationException(UNSUPPORTED_WRITING_OPERATION_MESSAGE);
    }

    @Override
    public GridCoverageWriter getWriter(Object arg0, Hints arg1) {
	throw new UnsupportedOperationException(UNSUPPORTED_WRITING_OPERATION_MESSAGE);
    }
}
