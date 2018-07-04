/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.wfs.internal.v2_0.storedquery;

import java.io.Serializable;

public class ParameterMappingDefaultValue implements ParameterMapping, Serializable {
    private String defaultValue;
    private String parameterName;
    private boolean forcible;

    public ParameterMappingDefaultValue(String name, boolean forcible, String defaultValue) {
        setParameterName(name);
        setForcible(forcible);
        setDefaultValue(defaultValue);
    }

    public ParameterMappingDefaultValue() {}

    public void setForcible(boolean forcible) {
        this.forcible = forcible;
    }

    @Override
    public boolean isForcible() {
        return forcible;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    @Override
    public String getParameterName() {
        return parameterName;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getDefaultValue() {
        return defaultValue;
    }
}
