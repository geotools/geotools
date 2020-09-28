/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.filter.text.cqljson.model;

public class FunctionObjectArgument {
    private String property;
    private Function function;
    private Object geometry;
    private Object bbox;
    private Object temporalValue;
    private ArithmeticOperand add;
    private ArithmeticOperand sub;
    private ArithmeticOperand mul;
    private ArithmeticOperand div;

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public Function getFunction() {
        return function;
    }

    public void setFunction(Function function) {
        this.function = function;
    }

    public Object getGeometry() {
        return geometry;
    }

    public void setGeometry(Object geometry) {
        this.geometry = geometry;
    }

    public Object getBbox() {
        return bbox;
    }

    public void setBbox(Object bbox) {
        this.bbox = bbox;
    }

    public Object getTemporalValue() {
        return temporalValue;
    }

    public void setTemporalValue(Object temporalValue) {
        this.temporalValue = temporalValue;
    }

    public ArithmeticOperand getAdd() {
        return add;
    }

    public void setAdd(ArithmeticOperand add) {
        this.add = add;
    }

    public ArithmeticOperand getSub() {
        return sub;
    }

    public void setSub(ArithmeticOperand sub) {
        this.sub = sub;
    }

    public ArithmeticOperand getMul() {
        return mul;
    }

    public void setMul(ArithmeticOperand mul) {
        this.mul = mul;
    }

    public ArithmeticOperand getDiv() {
        return div;
    }

    public void setDiv(ArithmeticOperand div) {
        this.div = div;
    }
}
