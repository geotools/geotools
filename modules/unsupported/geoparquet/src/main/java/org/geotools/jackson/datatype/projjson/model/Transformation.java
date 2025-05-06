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
 * Represents a coordinate transformation in PROJJSON.
 *
 * <p>A transformation defines the operation to convert coordinates from one reference system to another. It includes a
 * method and optional parameters. Transformations can be used standalone or as part of a BoundCRS.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Transformation extends AbstractCRS {

    private CoordinateReferenceSystem sourceCrs;
    private CoordinateReferenceSystem targetCrs;
    private Conversion.Method method;
    private List<Conversion.Parameter> parameters;
    private String accuracy;

    /** Creates a new Transformation with default values. */
    public Transformation() {
        super();
    }

    /**
     * Gets the type of coordinate operation.
     *
     * @return Always "Transformation" for this implementation
     */
    @Override
    public String getType() {
        return "Transformation";
    }

    /**
     * Gets the source CRS for the transformation.
     *
     * @return The source CRS
     */
    @JsonProperty("source_crs")
    public CoordinateReferenceSystem getSourceCrs() {
        return sourceCrs;
    }

    /**
     * Sets the source CRS for the transformation.
     *
     * @param sourceCrs The source CRS
     */
    public void setSourceCrs(CoordinateReferenceSystem sourceCrs) {
        this.sourceCrs = sourceCrs;
    }

    /**
     * Gets the target CRS for the transformation.
     *
     * @return The target CRS
     */
    @JsonProperty("target_crs")
    public CoordinateReferenceSystem getTargetCrs() {
        return targetCrs;
    }

    /**
     * Sets the target CRS for the transformation.
     *
     * @param targetCrs The target CRS
     */
    public void setTargetCrs(CoordinateReferenceSystem targetCrs) {
        this.targetCrs = targetCrs;
    }

    /**
     * Gets the method used by this transformation.
     *
     * @return The transformation method
     */
    @JsonProperty("method")
    public Conversion.Method getMethod() {
        return method;
    }

    /**
     * Sets the method used by this transformation.
     *
     * @param method The transformation method
     */
    public void setMethod(Conversion.Method method) {
        this.method = method;
    }

    /**
     * Gets the parameters for this transformation.
     *
     * @return The list of transformation parameters
     */
    @JsonProperty("parameters")
    public List<Conversion.Parameter> getParameters() {
        return parameters;
    }

    /**
     * Sets the parameters for this transformation.
     *
     * @param parameters The list of transformation parameters
     */
    public void setParameters(List<Conversion.Parameter> parameters) {
        this.parameters = parameters;
    }

    /**
     * Gets the accuracy of the transformation.
     *
     * @return The accuracy as a string
     */
    @JsonProperty("accuracy")
    public String getAccuracy() {
        return accuracy;
    }

    /**
     * Sets the accuracy of the transformation.
     *
     * @param accuracy The accuracy as a string
     */
    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }
}
