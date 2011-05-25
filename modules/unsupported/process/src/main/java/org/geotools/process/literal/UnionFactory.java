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
import org.geotools.util.SimpleInternationalString;
import org.opengis.util.InternationalString;

import com.vividsolutions.jts.geom.Geometry;

/**
 * A simple process showing how to interact with a couple of geometry literals.
 * <p>
 * This process is based on the SFSQL specification and implemented by the JTS Topology Suite;
 * the interesting part is the process api used to make this concept avaialble to:
 * <ul>
 * <li>Java Programmer: via GEOM1 and RESULT
 * <li>User Interface: via getParameterInfo
 * </li>
 * 
 * @author gdavis
 *
 *
 * @source $URL$
 */
public class UnionFactory extends SingleProcessFactory {
    // making parameters available as static constants to help java programmers
    /** First geometry for union */
    static final Parameter<Geometry> GEOM1 =
        new Parameter<Geometry>("geom", Geometry.class, Text.text("Geometry"),
        		Text.text("Geometries to union"), true, 2, -1, null, null);
    

    /**
     * Map used for getParameterInfo; used to describe operation requirements for user
     * interface creation.
     */
    static final Map<String,Parameter<?>> prameterInfo = new TreeMap<String,Parameter<?>>();
    static {
        prameterInfo.put( GEOM1.key, GEOM1 );
    }

    static final Parameter<Geometry> RESULT = 
        new Parameter<Geometry>("result", Geometry.class, Text.text("Result"), Text.text("Result Unioning given geometries") );
        
    /**
     * Map used for getParameterInfo; used to describe operation requirements for user
     * interface creation.
     */
    static final Map<String,Parameter<?>> resultInfo = new TreeMap<String,Parameter<?>>();
    static {
        resultInfo.put( RESULT.key, RESULT );
    }
    
    public UnionFactory() {
        super(new NameImpl("gt", "Union"));
    }
    
	public Process create(){
		return new UnionProcess();
	}

	public InternationalString getDescription() {
		return new SimpleInternationalString("Union 2 or more geometries together");
	}

	public Map<String,Parameter<?>> getParameterInfo() {
	    return Collections.unmodifiableMap( prameterInfo );
	}
	
	public Map<String,Parameter<?>> getResultInfo(Map<String, Object> parameters)
			throws IllegalArgumentException {
		return Collections.unmodifiableMap( resultInfo );
	}

	public InternationalString getTitle() {
	    // please note that this is a title for display purposes only
	    // finding an specific implementation by name is not possible
	    //
	    return Text.text("Union");
	}

	public boolean supportsProgress() {
		return true;
	} 	  
	
	public String getVersion() {
		return "1.0.0";
	} 	

}
