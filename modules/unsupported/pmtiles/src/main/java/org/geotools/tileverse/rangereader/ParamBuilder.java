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
package org.geotools.tileverse.rangereader;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.geotools.api.data.DataAccessFactory;
import org.geotools.api.data.Parameter;
import org.geotools.api.util.InternationalString;
import org.geotools.util.SimpleInternationalString;

/**
 * Builder for {@link DataAccessFactory#Param} instances.
 *
 * <p>Provides a fluent API for creating DataAccessFactory.Param objects, making parameter definition more readable and
 * maintainable. This builder helps with consistent parameter creation and reduces boilerplate code.
 *
 * <p>Example usage:
 *
 * <pre>
 * <code>
 * DataAccessFactory.Param param = new ParamBuilder("uri")
 *     .type(String.class)
 *     .description("URI to the data source")
 *     .required(true)
 *     .metadata(Parameter.LEVEL, "advanced")
 *     .build();
 * </code>
 * </pre>
 */
public class ParamBuilder {

    private String key;
    private Class<?> type;
    private InternationalString description;
    private InternationalString title;
    private boolean required = false;
    private Object defaultValue = null;
    private Map<String, Object> metadata = new HashMap<>();
    private int minOccurs = 0;
    private int maxOccurs = 1;

    ParamBuilder() {}
    /**
     * Creates a new ParamBuilder with the specified key.
     *
     * @param key the parameter key (used in parameter maps)
     */
    public ParamBuilder(String key) {
        this.key = Objects.requireNonNull(key, "Parameter key must not be null");
    }

    public static ParamBuilder builder() {
        return new ParamBuilder();
    }

    public ParamBuilder key(String key) {
        this.key = key;
        return this;
    }

    /**
     * Sets the parameter type.
     *
     * @param type the class representing the parameter type
     * @return this builder for method chaining
     */
    public ParamBuilder type(Class<?> type) {
        this.type = Objects.requireNonNull(type, "Parameter type must not be null");
        return this;
    }

    /**
     * Sets the parameter description.
     *
     * @param description the description text
     * @return this builder for method chaining
     */
    public ParamBuilder description(String description) {
        this.description = new SimpleInternationalString(description);
        return this;
    }

    /**
     * Sets the parameter description using an InternationalString.
     *
     * @param description the internationalized description
     * @return this builder for method chaining
     */
    public ParamBuilder description(InternationalString description) {
        this.description = description;
        return this;
    }

    /**
     * Sets the parameter title.
     *
     * @param title the title text
     * @return this builder for method chaining
     */
    public ParamBuilder title(String title) {
        this.title = new SimpleInternationalString(title);
        return this;
    }

    /**
     * Sets the parameter title using an InternationalString.
     *
     * @param title the internationalized title
     * @return this builder for method chaining
     */
    public ParamBuilder title(InternationalString title) {
        this.title = title;
        return this;
    }

    /** Shortcut for {@code required(false)} */
    public ParamBuilder optional() {
        return required(false);
    }

    /** Shortcut for {@code required(true)} */
    public ParamBuilder mandatory() {
        return required(true);
    }
    /**
     * Sets whether the parameter is required.
     *
     * @param required true if the parameter is required, false otherwise
     * @return this builder for method chaining
     */
    public ParamBuilder required(boolean required) {
        this.required = required;
        return this;
    }

    /**
     * Sets the default value for the parameter.
     *
     * @param defaultValue the default value
     * @return this builder for method chaining
     */
    public ParamBuilder defaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    /**
     * Adds a metadata entry to the parameter.
     *
     * @param key the metadata key
     * @param value the metadata value
     * @return this builder for method chaining
     */
    public ParamBuilder metadata(String key, Object value) {
        this.metadata.put(key, value);
        return this;
    }

    /**
     * Sets the metadata map for the parameter, replacing any existing metadata.
     *
     * @param metadata the complete metadata map
     * @return this builder for method chaining
     */
    public ParamBuilder metadata(Map<String, Object> metadata) {
        this.metadata.clear();
        if (metadata != null) {
            this.metadata.putAll(metadata);
        }
        return this;
    }

    /**
     * Sets the minimum number of occurrences for this parameter.
     *
     * @param minOccurs the minimum number of occurrences
     * @return this builder for method chaining
     * @throws IllegalArgumentException if minOccurs is negative
     * @see Parameter#maxOccurs
     */
    public ParamBuilder minOccurs(int minOccurs) {
        if (minOccurs < 0) {
            throw new IllegalArgumentException("minOccurs cannot be negative");
        }
        this.minOccurs = minOccurs;
        return this;
    }

    /**
     * Sets the maximum number of occurrences for this parameter.
     *
     * @param maxOccurs the maximum number of occurrences
     * @return this builder for method chaining
     * @throws IllegalArgumentException if maxOccurs is less than minOccurs
     * @see Parameter#maxOccurs
     */
    public ParamBuilder maxOccurs(int maxOccurs) {
        if (maxOccurs < minOccurs) {
            throw new IllegalArgumentException("maxOccurs cannot be less than minOccurs");
        }
        this.maxOccurs = maxOccurs;
        return this;
    }

    /**
     * @param options a speciifc List<T> of options can be provided for a user to choose between
     * @see Parameter#OPTIONS
     */
    public ParamBuilder options(Object... options) {
        if (options != null && options.length > 0) {
            return metadata(Parameter.OPTIONS, Arrays.asList(options));
        }
        return this;
    }

    /**
     * Mark this parameters as a password
     *
     * @return this builder for method chaining
     */
    public ParamBuilder password() {
        return password(true);
    }

    /**
     * Mark whether this parameters is a password
     *
     * @param isPassword whether this param is to be marked as a {@link Parameter#IS_PASSWORD password}
     * @return this builder for method chaining
     */
    public ParamBuilder password(boolean isPassword) {
        return metadata(Parameter.IS_PASSWORD, isPassword);
    }

    /**
     * Sets this parameter as part of the UI interface level.
     *
     * @param level the UI level (e.g., "user", "advanced", "program")
     * @return this builder for method chaining
     */
    public ParamBuilder level(String level) {
        return metadata(Parameter.LEVEL, level);
    }

    /**
     * Marks this parameter as a user-level parameter.
     *
     * @return this builder for method chaining
     */
    public ParamBuilder userLevel() {
        return level("user");
    }

    /**
     * Marks this parameter as an advanced-level parameter.
     *
     * @return this builder for method chaining
     */
    public ParamBuilder advancedLevel() {
        return level("advanced");
    }

    /**
     * Marks this parameter as a program-level parameter.
     *
     * @return this builder for method chaining
     */
    public ParamBuilder programLevel() {
        return level("program");
    }

    /**
     * Builds and returns a DataAccessFactory.Param with the configured properties.
     *
     * @return a new DataAccessFactory.Param instance
     * @throws IllegalStateException if required properties are not set
     */
    public DataAccessFactory.Param build() {
        if (type == null) {
            throw new IllegalStateException("Parameter type must be set");
        }

        if (title == null) {
            // Use key as title if not specified
            title = new SimpleInternationalString(key);
        }

        if (description == null) {
            // Use title as description if not specified
            description = title;
        }

        Map<String, Object> metadataCopy = Collections.unmodifiableMap(new HashMap<>(metadata));

        return new DataAccessFactory.Param(
                key, type, title, description, required, minOccurs, maxOccurs, defaultValue, metadataCopy);
    }
}
