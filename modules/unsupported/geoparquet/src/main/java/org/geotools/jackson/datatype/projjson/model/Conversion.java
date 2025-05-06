/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2025, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.jackson.datatype.projjson.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Represents a Conversion (map projection) in PROJJSON.
 *
 * <p>A conversion defines the mathematical transformation from one coordinate system to another. In the context of a
 * projected CRS, it represents the map projection that transforms geographic coordinates (latitude/longitude) into
 * projected coordinates (easting/northing).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Conversion {

    private String name;
    private Method method;
    private List<Parameter> parameters;
    private Object id;

    /** Creates a new Conversion with default values. */
    public Conversion() {
        // Default constructor for Jackson
    }

    /**
     * Gets the name of the conversion.
     *
     * @return The conversion name, e.g., "UTM zone 31N"
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the conversion.
     *
     * @param name The conversion name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the method used by this conversion.
     *
     * @return The conversion method
     */
    @JsonProperty("method")
    public Method getMethod() {
        return method;
    }

    /**
     * Sets the method used by this conversion.
     *
     * @param method The conversion method
     */
    public void setMethod(Method method) {
        this.method = method;
    }

    /**
     * Gets the parameters for this conversion.
     *
     * @return The list of conversion parameters
     */
    @JsonProperty("parameters")
    public List<Parameter> getParameters() {
        return parameters;
    }

    /**
     * Sets the parameters for this conversion.
     *
     * @param parameters The list of conversion parameters
     */
    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    /**
     * Gets the ID of the conversion.
     *
     * @return The conversion ID
     */
    @JsonProperty("id")
    public Object getId() {
        return id;
    }

    /**
     * Sets the ID of the conversion.
     *
     * @param id The conversion ID
     */
    public void setId(Object id) {
        this.id = id;
    }

    /** Represents a conversion method in PROJJSON. */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Method {

        private String name;
        private Object id;

        /** Creates a new Method with default values. */
        public Method() {
            // Default constructor for Jackson
        }

        /**
         * Gets the name of the method.
         *
         * @return The method name, e.g., "Transverse Mercator"
         */
        @JsonProperty("name")
        public String getName() {
            return name;
        }

        /**
         * Sets the name of the method.
         *
         * @param name The method name
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * Gets the ID of the method.
         *
         * @return The method ID
         */
        @JsonProperty("id")
        public Object getId() {
            return id;
        }

        /**
         * Sets the ID of the method.
         *
         * @param id The method ID
         */
        public void setId(Object id) {
            this.id = id;
        }
    }

    /** Represents a conversion parameter in PROJJSON. */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Parameter {

        private String name;
        private Object value;
        private Object unit;
        private Object id;

        /** Creates a new Parameter with default values. */
        public Parameter() {
            // Default constructor for Jackson
        }

        /**
         * Gets the name of the parameter.
         *
         * @return The parameter name, e.g., "Latitude of natural origin"
         */
        @JsonProperty("name")
        public String getName() {
            return name;
        }

        /**
         * Sets the name of the parameter.
         *
         * @param name The parameter name
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * Gets the value of the parameter.
         *
         * @return The parameter value, which can be a number or a string (e.g., file name)
         */
        @JsonProperty("value")
        public Object getValue() {
            return value;
        }

        /**
         * Sets the value of the parameter.
         *
         * @param value The parameter value
         */
        public void setValue(Object value) {
            this.value = value;
        }

        /**
         * Gets the unit of measurement for the parameter.
         *
         * <p>This can be either a string (for common units like "degree" or "metre") or a Unit object with detailed
         * unit information.
         *
         * @return The unit of measurement
         */
        @JsonProperty("unit")
        public Object getUnit() {
            return unit;
        }

        /**
         * Sets the unit of measurement for the parameter.
         *
         * @param unit The unit of measurement
         */
        public void setUnit(Object unit) {
            this.unit = unit;
        }

        /**
         * Gets the ID of the parameter.
         *
         * @return The parameter ID
         */
        @JsonProperty("id")
        public Object getId() {
            return id;
        }

        /**
         * Sets the ID of the parameter.
         *
         * @param id The parameter ID
         */
        public void setId(Object id) {
            this.id = id;
        }
    }
}
