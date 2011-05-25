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
package org.geotools.process.literal;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import org.geotools.data.Parameter;
import org.geotools.feature.NameImpl;
import org.geotools.process.Process;
import org.geotools.process.impl.SingleProcessFactory;
import org.geotools.text.Text;
import org.opengis.util.InternationalString;

import com.vividsolutions.jts.geom.Geometry;

/**
 * A Buffer process used on a geometry object.
 * <p>
 * This process is based on the SFSQL specification and implemented by the JTS Topology Suite
 * 
 * @author gdavis
 *
 *
 * @source $URL$
 */
public class BufferFactory extends SingleProcessFactory {
    // making parameters available as static constants to help java programmers
    /** Geometry for operation */
    static final Parameter<Geometry> GEOM1 =
        new Parameter<Geometry>("geom1", Geometry.class, Text.text("Geometry"), Text.text("Geometry to buffer") );
    
    /** Buffer amount */
    static final Parameter<Double> BUFFER = 
        new Parameter<Double>("buffer", Double.class, Text.text("Buffer Amount"), Text.text("Amount to buffer the geometry by") );
    
    /**
     * Map used for getParameterInfo; used to describe operation requirements for user
     * interface creation.
     */
    static final Map<String,Parameter<?>> prameterInfo = new TreeMap<String,Parameter<?>>();
    static {
        prameterInfo.put( GEOM1.key, GEOM1 );
        prameterInfo.put( BUFFER.key, BUFFER );
    }    
    
    static final Parameter<Geometry> RESULT = 
        new Parameter<Geometry>("result", Geometry.class, Text.text("Result"), Text.text("Result of Geometry.getBuffer( Buffer )") );
     
    /**
     * Map used to describe operation results.
     */
    static final Map<String,Parameter<?>> resultInfo = new TreeMap<String,Parameter<?>>();
    static {
        resultInfo.put( RESULT.key, RESULT );
    }
    
    public BufferFactory() {
        super(new NameImpl("gt", "buffer"));
    }
    
	public Process create(Map<String, Object> parameters)
			throws IllegalArgumentException {
		return new BufferProcess( this );
	}

	public InternationalString getDescription() {
		return Text.text("Buffer a geometry");
	}

	public Map<String, Parameter<?>> getParameterInfo() {
		return Collections.unmodifiableMap( prameterInfo );
	}

	public Map<String, Parameter<?>> getResultInfo(
			Map<String, Object> parameters) throws IllegalArgumentException {
		return Collections.unmodifiableMap( resultInfo );
	}

	public InternationalString getTitle() {
	    // please note that this is a title for display purposes only
	    // finding an specific implementation by name is not possible
	    return Text.text("Buffer");
	}

	public Process create() throws IllegalArgumentException {
	    return new BufferProcess( this );
	}

	public boolean supportsProgress() {
		return true;
	} 	  
	
	public String getVersion() {
		return "1.0.0";
	} 	

}
