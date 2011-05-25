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
package org.geotools.coverage.io.driver;

import java.io.IOException;
import java.io.Serializable;
import java.util.EnumSet;
import java.util.Map;

import org.geotools.coverage.io.CoverageAccess;
import org.geotools.data.Parameter;
import org.geotools.factory.Factory;
import org.geotools.factory.Hints;
import org.geotools.factory.OptionalFactory;
import org.opengis.util.InternationalString;
import org.opengis.util.ProgressListener;

/**
 * A driver adding the ability to work with a new coverage format or service.
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
 * files.
 * 
 * 
 * @author Simone Giannecchini, GeoSolutions.
 * @author Jody Garnett
 * @since 2.5
 * 
 * 
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/unsupported/coverage-experiment/coverage-api/src/main/java/org/geotools/coverage/io/driver/Driver.java $
 */
public interface Driver extends Factory, OptionalFactory {
	
	public enum DriverOperation{
		CONNECT,
		CREATE,
		DELETE;
		
	}

    /**
     * Unique name (non human readable) that can be used to refer to this
     * implementation.
     * <p>
     * While the Title and Description will change depending on the users local
     * this name will be consistent.
     * </p>
     * Please note that a given file may be readable by several Drivers (the
     * description of each implementation should be provided to the user so they
     * can make an intellegent choice in the matter).
     * 
     * @return name of this {@link Driver}
     */
    public String getName();

    /**
     * Human readable title for this {@link Driver}.
     * 
     * @return human readable title for presentation in user interfaces
     */
    public InternationalString getTitle();

    /**
     * Describe the nature of this {@link Driver} implementation.
     * <p>
     * A description of this {@link Driver} type; the description should
     * indicate the format or service being made available in human readable
     * terms.
     * </p>
     * 
     * @return A human readable description that is suitable for inclusion in a
     *         list of available {@link Driver}s.
     */
    public InternationalString getDescription();

    /**
     * Test to see if this {@link Driver} is available, if it has all the
     * appropriate dependencies (jars or libraries).
     * <p>
     * One may ask how this is different than {@link #canConnect(Map)}, and
     * basically available can be used by finder mechanisms to list available
     * {@link Driver}s.
     * 
     * @return <tt>true</tt> if and only if this factory has all the
     *         appropriate dependencies on the classpath to create DataStores.
     */
    public boolean isAvailable();
    
    public boolean canProcess(DriverOperation operation,Map<String, Serializable> params);
    
    public Map<String, Parameter<?>> getParameterInfo(DriverOperation operation);
    
    public EnumSet<DriverOperation> getDriverCapabilities();
    
    /**
     * 
     * 
     * Simone: Return <code>null</code> in case the delete succeds.
     * TODO think about a neater approach
     * 
     * @param opreation
     * @param params
     * @param hints
     * @param listener
     * @return
     * @throws IOException
     */
    public CoverageAccess process(DriverOperation opreation, Map<String, Serializable> params,Hints hints, final ProgressListener listener)throws IOException;

}
