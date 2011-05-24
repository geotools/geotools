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
package org.geotools.data;

import java.util.Collections;
import java.util.Map;

import org.geotools.util.SimpleInternationalString;
import org.opengis.util.InternationalString;

/**
 * A Parameter defines information about a valid process parameter.
 *
 * @author gdavis
 *
 *
 * @source $URL$
 */
public class Parameter<T> {
    /**
     * This is the key (ie machine readable text) used to represent this parameter in a
     * java.util.Map.
     * 
     * @param key (or machine readable name) for this parameter.
     */
    public final String key;
    
    /**
     * Human readable title/name of this parameter.
     */
    public final InternationalString title;    
    
    /**
     * Human readable description of this parameter.
     */
    public final InternationalString description;
    
    /**
     * Class binding for this parameter.
     * <p>
     * When a value is supplied for this key it should be of the provided type.
     */
    public final Class<T> type;
    
    /** Can the value be missing? Or is null allowed...
     *@return true if a value is required to be both present and non null
     **/
    public final boolean required;
    
    /** What is the min and max number of this paramter there can be
     * ( a value of -1 for min means 0 or more,
     * a value of -1 for max means any number greater than or equal to the min value )
     * 
     * eg: a geometry union process can have any number of geom parameters,
     * so by setting the max to -1 and the min to 2 we accomplish that.
     */
    public final int minOccurs;
    public final int maxOccurs; 
    
    /**
     * A sample value; often used as a default when prompting the end-user
     * to fill in the details before executing a process.
     */
    public final Object sample;
    
    /**
     * Hints for the user interface
     */
    
    /** "featureType" FeatureType to validate a Feature value against */
    public static final String FEATURE_TYPE = "featureType";
    
    /** Boolean indicating whether the parameter shall be used as a password field,
     * provides a hint for UI's to mask text fields, configuration systems to encrypt content, etc
     */
    public static final String IS_PASSWORD = "isPassword";

    /**
     * "length" Integer used to limit the length of strings or literal geometries.
     */
    public static final String LENGTH = "length";
    
    /** "crs": CoordinateReferenceSystem used to restrict a Geometry literal */
    public static final String CRS = "crs";
    
    /** "element": Class to use as the Element type for List<Element>. Please restrict
     * your use of this facility to simple types; for most higher order data structures
     * multiplicity is already accounted for - example MultiGeometry. */
    public static final String ELEMENT = "element";
    
    /**
     * "min" and "max" may be useful for restrictions for things like int sizes, etc.
     */
    public static final String MIN = "min";
    /**
     * "min" and "max" may be useful for restrictions for things like int sizes, etc.
     */
    public static final String MAX = "max";
    
    /**
     * File extension expected - "shp", "jpg", etc...
     */
    public static final String EXT = "ext";
    
    /**
     * Level or Category of the parameter - "user", "advanced", "program"
     * <p>
     * <ul>
     * <li>user - should be shown to all users and is used every time.<br>
     *     example: user name and password
     * </li>
     * <li>advanced - advanced or expert parameter used in special cases<br>
     *     example: choice between get and post requests for WFS
     * </li>
     * <li>program - intended for programs often tweaking settings for performance<br>
     *     example: JDBC datasource for which it is hard for a user to type in
     * </li>
     * </ul>
     */
    public static final String LEVEL = "level";
    
    /**
     * Refinement of type; such as the FeatureType of a FeatureCollection, or component type of a List.
     * <p>
     * This information is supplied (along with type) to allow a process implementor communicate
     * additional restrictions on the allowed value beyond the strict type.
     * <p>
     * The following keys are understood at this time: LENGTH, FEATURE_TYPE, CRS, ELEMENT
     * .. additional keys will be documented as static final fields over time.
     * <p>
     * Any restrictions mentioned here should be mentioned as part of your
     * parameter description. This metadata is only used to help restrict what
     * the user enters; not all client application will understand and respect
     * these keys - please communicate with your end-user.
     * 
     * @see CRS
     * @see ELEMENT
     * @see FEATURE_TYPE
     * @see IS_PASSWORD
     * @see LENGTH
     * @see MAX
     * @see MIN
     */
    public final Map<String, Object> metadata;

    /**
     * Mandatory parameter - quickly constructed with out a properly internationalized
     * title and description.
     * 
     * @param key machine readable key for use in a java.util.Map
     * @param type Java class for the expected value
     * @param title Human readable title used for use in a user interface
     * @param description Human readable description
     */
     public Parameter(String key, Class<T> type, String title,
     		String description ) {
         this( key, type, new SimpleInternationalString(title), new SimpleInternationalString(description) );
     }

     /**
      * Mandatory parameter - quickly constructed with out a properly internationalized
      * title and description.
      * 
      * @param key machine readable key for use in a java.util.Map
      * @param type Java class for the expected value
      * @param title Human readable title used for use in a user interface
      * @param description Human readable description
      */
      public Parameter(String key, Class<T> type, String title,
                 String description, Map<String,Object> metadata ) {
          this( key, type, new SimpleInternationalString(title), new SimpleInternationalString(description), metadata);
      }
   /**
    * Mandatory parameter
    * @param key machine readable key for use in a java.util.Map
    * @param type Java class for the expected value
    * @param title Human readable title used for use in a user interface
    * @param description Human readable description
    */
    public Parameter(String key, Class<T> type, InternationalString title,
    		InternationalString description ) {
        this( key, type, title, description, true, 1, 1, null, null );
    }
    
    /**
     * Mandatory parameter with metadata.
     * @param key machine readable key for use in a java.util.Map
     * @param type Java class for the expected value
     * @param title Human readable title used for use in a user interface
     * @param description Human readable description
     * @param metadata Hints to the user interface (read the javadocs for each metadata key)
     * 
     * @see CRS
     * @see ELEMENT
     * @see FEATURE_TYPE
     * @see IS_PASSWORD
     * @see LENGTH
     * @see MAX
     * @see MIN
     */
     public Parameter(String key, Class<T> type, InternationalString title,
     		InternationalString description, Map<String,Object> metadata ) {
         this( key, type, title, description, true, 1, 1, null, metadata );
     }
    /**
     * Addition of optional parameters
     * @param key machine readable key for use in a java.util.Map
     * @param type Java class for the expected value
     * @param title Human readable title used for use in a user interface
     * @param description Human readable description
     * @param required true if the value is required
     * @param min Minimum value; or null if not needed
     * @param max Maximum value; or null if not needed
     * @param sample Sample value; may be used as a default in a user interface
     * @param metadata Hints to the user interface (read the javadocs for each metadata key)
     * 
     * @see CRS
     * @see ELEMENT
     * @see FEATURE_TYPE
     * @see IS_PASSWORD
     * @see LENGTH
     * @see MAX
     * @see MIN
     */
    public Parameter(String key, Class<T> type, InternationalString title,
    				 InternationalString description,
                     boolean required, int min, int max, Object sample, 
                     Map<String,Object> metadata) {
        this.key = key;
        this.title = title;
        this.type = type;
        this.description = description;
        this.required = required;
        this.minOccurs = min;
        this.maxOccurs = max;
        this.sample = sample;
        this.metadata = metadata == null ? null : Collections.unmodifiableMap(metadata);
    }
    
    /**
     * Provides for easy access to the {@link Parameter#IS_PASSWORD} metadata
     * @return true if {@code metadata.get(IS_PASSWORD) == Boolean.TRUE}
     */
    public boolean isPassword(){
        return metadata != null && Boolean.TRUE.equals(metadata.get(IS_PASSWORD));
    }
 
    /**
     * Easy access to check the {@link #LEVEL} metadata
     * @return provided level or "user" by default
     */
    public String getLevel(){
        if( metadata == null ){
            return "user";
        }
        String level = (String) metadata.get(LEVEL);
        if( level == null ){
            return "user";
        }
        return level;        
    }
}
