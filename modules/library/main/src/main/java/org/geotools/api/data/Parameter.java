/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008 - 2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.api.data;

import java.util.Collections;
import java.util.Map;
import org.geotools.api.util.InternationalString;
import org.geotools.referencing.CRS;
import org.geotools.util.SimpleInternationalString;

/**
 * A Parameter defines information about a valid process parameter.
 *
 * @author gdavis
 */
public class Parameter<T> implements org.geotools.api.parameter.Parameter<T> {
    /** This is the key (ie machine readable text) used to represent this parameter in a java.util.Map. */
    public final String key;

    /** Human readable title/name of this parameter. */
    public final InternationalString title;

    /** Human readable description of this parameter. */
    public final InternationalString description;

    /**
     * Class binding for this parameter.
     *
     * <p>When a value is supplied for this key it should be of the provided type.
     */
    public final Class<T> type;

    /**
     * Can the value be missing? Or is null allowed... Return true if a value is required to be both present and non
     * null
     */
    public final boolean required;

    /**
     * What is the min and max number of this paramter there can be ( a value of -1 for min means 0 or more, a value of
     * -1 for max means any number greater than or equal to the min value )
     *
     * <p>eg: a geometry union process can have any number of geom parameters, so by setting the max to -1 and the min
     * to 2 we accomplish that.
     */
    public final int minOccurs;

    public final int maxOccurs;

    /**
     * A sample value; often used as a default when prompting the end-user to fill in the details before executing a
     * process.
     */
    public final T sample;

    /** Hints for the user interface */

    /** "featureType" FeatureType to validate a Feature value against */
    public static final String FEATURE_TYPE = "featureType";

    /**
     * Boolean indicating whether the parameter shall be used as a password field, provides a hint for UI's to mask text
     * fields, configuration systems to encrypt content, etc
     */
    public static final String IS_PASSWORD = "isPassword";

    /**
     * Boolean indicating whether the parameter is meant to be a long text, provides a hint for UI's use long text
     * fields, textareas and the like
     */
    public static final String IS_LARGE_TEXT = "isLargeText";

    /** "length" Integer used to limit the length of strings or literal geometries. */
    public static final String LENGTH = "length";

    /** "crs": CoordinateReferenceSystem used to restrict a Geometry literal */
    public static final String CRS = "crs";

    /**
     * "element": Class to use as the Element type for List<Element>. Please restrict your use of this facility to
     * simple types; for most higher order data structures multiplicity is already accounted for - example
     * MultiGeometry.
     */
    public static final String ELEMENT = "element";

    /** "min" and "max" may be useful for restrictions for things like int sizes, etc. */
    public static final String MIN = "min";

    /** "min" and "max" may be useful for restrictions for things like int sizes, etc. */
    public static final String MAX = "max";

    /**
     * As an alternative to "min" and "max" a speciifc List<T> of options can be provided for a user to choose between.
     * The description should explain what the options mean.
     *
     * <p>Example: a compliance level of (0-low,1-medium,2-high)
     *
     * <p>Although a List<T> is used here (so you can specifiy order) it is assumed you will not confuse your users by
     * placing duplicates in the list.
     */
    public static final String OPTIONS = "options";

    /** File extension expected - "shp", "jpg", etc... */
    public static final String EXT = "ext";

    /**
     * Level or Category of the parameter - "user", "advanced", "program"
     *
     * <p>
     *
     * <ul>
     *   <li>user - should be shown to all users and is used every time.<br>
     *       example: user name and password
     *   <li>advanced - advanced or expert parameter used in special cases<br>
     *       example: choice between get and post requests for WFS
     *   <li>program - intended for programs often tweaking settings for performance<br>
     *       example: JDBC datasource for which it is hard for a user to type in
     * </ul>
     */
    public static final String LEVEL = "level";

    /**
     * Set parameter to deprecated - true, false ;can be used to conditional show the parameter based on deprecated
     * value
     */
    public static final String DEPRECATED = "deprecated";

    /**
     * Refinement of type; such as the FeatureType of a FeatureCollection, or component type of a List.
     *
     * <p>This information is supplied (along with type) to allow a process implementor communicate additional
     * restrictions on the allowed value beyond the strict type.
     *
     * <p>The following keys are understood at this time: LENGTH, FEATURE_TYPE, CRS, ELEMENT .. additional keys will be
     * documented as static final fields over time.
     *
     * <p>Any restrictions mentioned here should be mentioned as part of your parameter description. This metadata is
     * only used to help restrict what the user enters; not all client application will understand and respect these
     * keys - please communicate with your end-user.
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
     * Mandatory parameter - quickly constructed with out a properly internationalized title and description.
     *
     * @param key machine readable key for use in a java.util.Map
     * @param type Java class for the expected value
     * @param title Human readable title used for use in a user interface
     * @param description Human readable description
     */
    public Parameter(String key, Class<T> type, String title, String description) {
        this(key, type, new SimpleInternationalString(title), new SimpleInternationalString(description));
    }

    /**
     * Mandatory parameter - quickly constructed with out a properly internationalized title and description.
     *
     * @param key machine readable key for use in a java.util.Map
     * @param type Java class for the expected value
     * @param title Human readable title used for use in a user interface
     * @param description Human readable description
     */
    public Parameter(String key, Class<T> type, String title, String description, Map<String, Object> metadata) {
        this(key, type, new SimpleInternationalString(title), new SimpleInternationalString(description), metadata);
    }
    /**
     * Mandatory parameter
     *
     * @param key machine readable key for use in a java.util.Map
     * @param type Java class for the expected value
     * @param title Human readable title used for use in a user interface
     * @param description Human readable description
     */
    public Parameter(String key, Class<T> type, InternationalString title, InternationalString description) {
        this(key, type, title, description, true, 1, 1, null, null);
    }

    /**
     * Mandatory parameter with metadata.
     *
     * @param key machine readable key for use in a java.util.Map
     * @param type Java class for the expected value
     * @param title Human readable title used for use in a user interface
     * @param description Human readable description
     * @param metadata Hints to the user interface (read the javadocs for each metadata key)
     * @see #CRS
     * @see #ELEMENT
     * @see #FEATURE_TYPE
     * @see #IS_PASSWORD
     * @see #LENGTH
     * @see #MAX
     * @see #MIN
     */
    public Parameter(
            String key,
            Class<T> type,
            InternationalString title,
            InternationalString description,
            Map<String, Object> metadata) {
        this(key, type, title, description, true, 1, 1, null, metadata);
    }
    /**
     * Addition of optional parameters
     *
     * @param key machine readable key for use in a java.util.Map
     * @param type Java class for the expected value
     * @param title Human readable title used for use in a user interface
     * @param description Human readable description
     * @param required true if the value is required
     * @param min Minimum value; or -1 if not needed
     * @param max Maximum value; or -1 for unbound
     * @param sample Sample value; may be used as a default in a user interface
     * @param metadata Hints to the user interface (read the javadocs for each metadata key)
     * @see #CRS
     * @see #ELEMENT
     * @see #FEATURE_TYPE
     * @see #IS_PASSWORD
     * @see #LENGTH
     * @see #MAX
     * @see #MIN
     */
    public Parameter(
            String key,
            Class<T> type,
            InternationalString title,
            InternationalString description,
            boolean required,
            int min,
            int max,
            T sample,
            Map<String, Object> metadata) {
        this.key = key;
        this.title = title;
        this.type = type;
        this.description = description;
        this.required = required;
        this.minOccurs = min;
        this.maxOccurs = max;
        this.sample = sample;
        this.metadata = metadata == null ? Collections.emptyMap() : Collections.unmodifiableMap(metadata);
    }

    /**
     * Constructs a parameter from key and type
     *
     * @param key machine readable key for use in a java.util.Map
     * @param type Java class for the expected value
     */
    public Parameter(String key, Class<T> type) {
        this(key, type, null, null, true, 1, 1, null, null);
    }

    /**
     * Constructs a parameter from key, type, and min/max occurs.
     *
     * @param key machine readable key for use in a java.util.Map
     * @param type Java class for the expected value
     * @param min Minimum value of occurrences, -1 if not needed
     * @param max Maximum value of occurrences, -1 for unbound
     */
    public Parameter(String key, Class<T> type, int min, int max) {
        this(key, type, null, null, min > 0, min, max, null, null);
    }

    @Override
    public String getName() {
        return key;
    }

    @Override
    public InternationalString getTitle() {
        return title;
    }

    @Override
    public InternationalString getDescription() {
        return description;
    }

    @Override
    public Class<T> getType() {
        return type;
    }

    @Override
    public Boolean isRequired() {
        return required;
    }

    @Override
    public int getMinOccurs() {
        return minOccurs;
    }

    @Override
    public int getMaxOccurs() {
        return maxOccurs;
    }

    @Override
    public T getDefaultValue() {
        return sample;
    }

    /**
     * Provides for easy access to the {@link Parameter#IS_PASSWORD} metadata
     *
     * @return true if {@code metadata.get(IS_PASSWORD) == Boolean.TRUE}
     */
    public boolean isPassword() {
        return metadata != null && Boolean.TRUE.equals(metadata.get(IS_PASSWORD));
    }

    /**
     * Easy access to check the {@link #DEPRECATED} metadata
     *
     * @return provided deprecated or false by default
     */
    public Boolean isDeprecated() {
        if (metadata == null) {
            return false;
        }
        Boolean deprecated = (Boolean) metadata.get(DEPRECATED);
        if (deprecated == null) {
            return false;
        }
        return deprecated;
    }

    /**
     * Easy access to check the {@link #LEVEL} metadata
     *
     * @return provided level or "user" by default
     */
    public String getLevel() {
        if (metadata == null) {
            return "user";
        }
        String level = (String) metadata.get(LEVEL);
        if (level == null) {
            return "user";
        }
        return level;
    }

    @Override
    public String toString() {
        StringBuilder build = new StringBuilder();
        build.append(key);
        if (type != Object.class) {
            build.append(":");
            build.append(type.getSimpleName());
        }
        if (!required) {
            build.append("{optional}");
        }
        if (minOccurs != 1 || maxOccurs != 1) {
            build.append("{");
            build.append(minOccurs);
            build.append(":");
            build.append(maxOccurs);
            build.append("}");
        }
        return build.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (description == null ? 0 : description.hashCode());
        result = prime * result + (key == null ? 0 : key.hashCode());
        result = prime * result + maxOccurs;
        result = prime * result + (metadata == null ? 0 : metadata.hashCode());
        result = prime * result + minOccurs;
        result = prime * result + (required ? 1231 : 1237);
        result = prime * result + (sample == null ? 0 : sample.hashCode());
        result = prime * result + (title == null ? 0 : title.hashCode());
        result = prime * result + (type == null ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Parameter other = (Parameter) obj;
        if (description == null) {
            if (other.description != null) return false;
        } else if (!description.equals(other.description)) return false;
        if (key == null) {
            if (other.key != null) return false;
        } else if (!key.equals(other.key)) return false;
        if (maxOccurs != other.maxOccurs) return false;
        if (metadata == null) {
            if (other.metadata != null) return false;
        } else if (!metadata.equals(other.metadata)) return false;
        if (minOccurs != other.minOccurs) return false;
        if (required != other.required) return false;
        if (sample == null) {
            if (other.sample != null) return false;
        } else if (!sample.equals(other.sample)) return false;
        if (title == null) {
            if (other.title != null) return false;
        } else if (!title.equals(other.title)) return false;
        if (type == null) {
            if (other.type != null) return false;
        } else if (!type.equals(other.type)) return false;
        return true;
    }
}
