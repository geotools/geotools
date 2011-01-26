/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.io.service;

import java.io.IOException;
import java.io.Serializable;
import java.util.EnumSet;
import java.util.Map;

import org.geotools.coverage.io.RasterDatasetReader;
import org.geotools.coverage.io.RasterDatasetWriter;
import org.geotools.coverage.io.request.CoverageReadRequest;
import org.geotools.data.Parameter;
import org.geotools.data.ServiceInfo;
import org.geotools.factory.Factory;
import org.geotools.factory.Hints;
import org.geotools.factory.OptionalFactory;
import org.opengis.util.InternationalString;
import org.opengis.util.ProgressListener;

/**
 * A driver adds the ability to work with a coverage format or service.
 * <p>
 * Classes implementing this interface basically act as factory for creating
 * connections to coverage sources like files, WCS services, WMS services,
 * databases, etc...
 * <p>
 * This class also offers basic create / delete functionality (which can be
 * useful for file based coverage formats).
 * <p>
 * Purpose of this class is to provide basic information about a certain
 * coverage service/format as well as about the parameters needed in order to
 * connect to a source which such a service/format is able to work against.
 * 
 * <p>
 * Notice that as part as the roll of a "factory" interface this class makes
 * available an {@link #isAvailable()} method which should check if all the
 * needed dependencies which can be jars as well as native libs or configuration
 * files are available.
 * 
 * 
 * @author Simone Giannecchini, GeoSolutions.
 * @author Jody Garnett
 * @since 2.5
 * 
 * 
 */
public interface RasterService extends Factory, OptionalFactory {



	/**
     * Unique name (non human readable) that can be used to refer to this
     * implementation.
     * <p>
     * While the Title and Description will change depending on the users local
     * this name will be consistent.
     * </p>
     * Please note that a given file may be readable by several Drivers (the
     * description of each implementation should be provided to the user so they
     * can make an intelligent choice in the matter).
     * 
     * @return name of this {@link RasterService}
     */
    public String getName();

    /**
     * Human readable title for this {@link RasterService}.
     * 
     * @return human readable title for presentation in user interfaces
     */
    public InternationalString getTitle();

    /**
     * Describe the nature of this {@link RasterService} implementation.
     * <p>
     * A description of this {@link RasterService} type; the description should
     * indicate the format or service being made available in human readable
     * terms.
     * </p>
     * 
     * @return A human readable description that is suitable for inclusion in a
     *         list of available {@link RasterService}s.
     */
    public InternationalString getDescription();

    /**
     * Test to see if this {@link RasterService} is available, if it has all the
     * appropriate dependencies (jars or libraries).
     * <p>
     * One may ask how this is different than {@link #canConnect(Map)}, and
     * basically available can be used by finder mechanisms to list available
     * {@link RasterService}s.
     * 
     * @return <code>true</code> if and only if this factory has all the
     *         appropriate dependencies on the classpath.
     */
    public boolean isAvailable();

    /**
     * Describes the required (and optional) parameters that can be used to
     * connect to a {@link RasterStorage}.
     * <p>
     * You can use this description to build up a valid Map<String,Serializable>
     * that is accepted by the connect / create / delete methods.
     * </p>
     * 
     * @return Param a {@link Map} describing the {@link Map} for
     *         {@link #connect(Map)}.
     */
    public Map<String, Parameter<?>> getParameterInfo();


    public RasterDatasetReader createReader(final Map<String, Serializable>  parameters, Hints hints,ProgressListener listener)throws IOException;
    
    public RasterDatasetWriter createWriter(final Map<String, Serializable>  parameters, Hints hints,ProgressListener listener)throws IOException;
    
    /**
     * TODO Improve me
     * 
     * @param parameters
     * @param hints
     * @return
     * @throws IOException
     */
    public boolean canCreateReader(final Map<String, Serializable>  parameters, Hints hints)throws IOException;

    public boolean canCreateWriter(final Map<String, Serializable>  parameters, Hints hints)throws IOException;
    
	/**
	 * Describes the required (and optional) parameters that
	 * can be passed to the {@link #createInstance(Map, Hints, ProgressListener)} and {@link #accepts(Map, Hints)} methods.
	 * <p>
	 * @return Param a {@link Map} describing the {@link Map} for {@link #update(CoverageReadRequest, ProgressListener)}.
	 */
	public Map<String, Parameter<?>> getDefaultReaderParameter();
	
	public Map<String, Parameter<?>> getDefaultWriterParameter();
	
	public String getVendor();
	
	public String getVersion();
	
	public ServiceInfo getServiceInfo();
	
	public EnumSet<RasterServiceAction> getCapabilities();
}
