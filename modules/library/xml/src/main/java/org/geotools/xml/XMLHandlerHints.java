/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.xml;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Hint object with known parameters for XML parsing.
 * 
 * @author Jesse
 *
 *
 * @source $URL$
 */
public class XMLHandlerHints implements Map {

    /** 
     * Declares the schemas to use for parsing.  
     * Value must be a java.util.Map of <String,URI> objects
     * where String is the Namespace and URI is the URL to use to load the schema. 
     */
    public static final String NAMESPACE_MAPPING = "NAMESPACE_MAPPING";
    /** Declares a FlowHandler for the parser to use */
    public final static String FLOW_HANDLER_HINT = "FLOW_HANDLER_HINT";
    /** Tells the parser to "Stream" */
    public static final String STREAM_HINT = "org.geotools.xml.gml.STREAM_HINT";
    /** Sets the level of compliance that the filter encoder should use */
    public static final String FILTER_COMPLIANCE_STRICTNESS = "org.geotools.xml.filter.FILTER_COMPLIANCE_STRICTNESS";
    /** 
     * The value so that the parser will encode all Geotools filters with no modifications.
     */
	public static final Integer VALUE_FILTER_COMPLIANCE_LOW = new Integer(0);
	/**
	 * The value so the parser will be slightly more compliant to the Filter 1.0.0 spec.
	 *  It will encode:
	 *  
	 *  <pre><code>
	 *  BBoxFilter
	 *  	or
	 *  FidFilter
	 *  </code></pre>
	 *  
	 *  as
	 *  
	 *  <pre><code>
	 *  &lt;Filter&gt;&lt;BBo&gt;...&lt;/BBox&gt;&lt;FidFilter fid="fid"/&gt;&lt;/Filter&gt;
	 *  </code></pre>
	 *  
	 *  It will encode:
	 *   <pre><code>
	 *  BBoxFilter
	 *  	and
	 *  FidFilter
	 *  </code></pre>
	 *  as
	 *  
	 *  <pre><code>
	 *  &lt;Filter&gt;&lt;FidFilter fid="fid"/&gt;&lt;/Filter&gt;
	 *  </code></pre>
	 *  
	 * <p><b>IMPORTANT:</b> If this compliance level is used and a non-strict FilterFactory is used to create
	 * the filter then the original filter must be ran on the retrieved feature because this hint will sometimes
	 * cause more features to be returned than is requested.  Consider the following filter:
	 * 
	 * not(fidFilter).
	 * 
	 * this will return all features and so the filtering must be done on the client.
	 * </p>
	 */
	public static final Integer VALUE_FILTER_COMPLIANCE_MEDIUM = new Integer(1);
	
	/**
	 * The value so the parser will be compliant with the Filter 1.0.0 spec.
	 * 
	 *  It will throw an exception with filters like:
	 *  BBoxFilter
	 *  	or
	 *  FidFilter
	   
	 *  
	 *  It will encode: 
	 *   <pre><code>
	 *  BBoxFilter
	 *  	and
	 *  FidFilter
	 *  </code></pre>
	 *  as
	 *  
	 *  <pre><code>
	 *  &lt;Filter&gt;&lt;FidFilter fid="fid"/&gt;&lt;/Filter&gt;
	 *  </code></pre>
	 * <p><b>IMPORTANT:</b> If this compliance level is used and a non-strict FilterFactory is used to create
	 * the filter then the original filter must be ran on the retrieved feature because this hint will sometimes
	 * cause more features to be returned than is requested.  Consider the following filter:
	 * <p>
	 * not(fidFilter).
	 * </p>
	 * this will return all features and so the filtering must be done on the client.
	 * </p>

	 */
	public static final Integer VALUE_FILTER_COMPLIANCE_HIGH = new Integer(2);


    private Map map=new HashMap();
    public void clear() {
        map.clear();
    }

    public boolean containsKey( Object key ) {
        return map.containsKey(key);
    }

    public boolean containsValue( Object value ) {
        return map.containsValue(value);
    }

    public Set entrySet() {
        return map.entrySet();
    }

    public boolean equals( Object o ) {
        return map.equals(o);
    }

    public Object get( Object key ) {
        return map.get(key);
    }

    public int hashCode() {
        return map.hashCode();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public Set keySet() {
        return map.keySet();
    }

    public Object put( Object arg0, Object arg1 ) {
        return map.put(arg0, arg1);
    }

    public void putAll( Map arg0 ) {
        map.putAll(arg0);
    }

    public Object remove( Object key ) {
        return map.remove(key);
    }

    public int size() {
        return map.size();
    }

    public Collection values() {
        return map.values();
    }
    
    
    

}
