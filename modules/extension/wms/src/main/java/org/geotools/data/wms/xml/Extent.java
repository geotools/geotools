package org.geotools.data.wms.xml;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Property class for holding and handling of property values declared in Extent-element of a layer
 * 
 * As of WMS Spec 1.3.0 Extent is no longer a valid child element to the element layer. This java
 * representation is maintained to be able to support both WMS Spec 1.1.1 and 1.3.0
 * 
 * http://schemas.opengis.net/wms/1.1.1/WMS_MS_Capabilities.dtd <!-- The Extent element indicates
 * what _values_ along a dimension are valid. --> <!ELEMENT Extent (#PCDATA) > <!ATTLIST Extent name
 * CDATA #REQUIRED default CDATA #IMPLIED nearestValue (0 | 1) "0">
 * 
 * @version SVN $Id$
 * @author Per Engstrom, Curalia AB, pereng@gmail.com
 * 
 */
public class Extent {
    protected String name;

    protected String defaultValue;

    protected boolean nearestValue = false;

    protected boolean multipleValues;
    
    /** Only valid for Time extents */
    protected boolean current = false;

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }

    /**
     * A CDATA section in the XML; this is actually a structured string.
     */
    protected String value;

    public Extent(String name, String defaultValue, Boolean multipleValues, Boolean nearestValue,
            String value) {
        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException(
                    "Error creating Dimension: parameter name must not be null!");
        }
        this.name = name;
        this.defaultValue = defaultValue;
        this.nearestValue = nearestValue;
        this.multipleValues = multipleValues;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public boolean getNearestValue() {
        return nearestValue;
    }

    public void setNearestValue(boolean nearestValue) {
        this.nearestValue = nearestValue;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isMultipleValues() {
        return multipleValues;
    }

    public void setMultipleValues(boolean multipleValues) {
        this.multipleValues = multipleValues;
    }

    public String toString() {
        return name + ": " + value + " (default: " + defaultValue + ")";
    }

    // The following helper methods are here
    // to reduce the effort involved in dealing with an Extent
    /**
     * Parse out the values; should be a list if isMultipleValues; or a list of one otherwise.
     * 
     * @return Values that may be used for this Extent; may be an empty collection.
     */
    List<String> values() {
        if (value == null) {
            return Collections.emptyList();
        }
        if (multipleValues) {
            String[] split = value.split(",");
            return new ArrayList<String>(Arrays.asList(split));
        } else {
            return new ArrayList<String>(Collections.singletonList(value));
        }
    }

    public boolean isEmpty() {
        return value != null || value.length() > 0;
    }

}
