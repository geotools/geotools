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
package org.geotools.data.jdbc.datasource;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.geotools.data.DataAccessFactory.Param;

public abstract class AbstractDataSourceFactorySpi implements DataSourceFactorySpi {

    /**
     * Default Implementation abuses the naming convention.
     * <p>
     * Will return <code>Foo</code> for <code>org.geotools.data.foo.FooFactory</code>.
     * </p>
     * 
     * @return return display name based on class name
     */
    public String getDisplayName() {
        String name = this.getClass().getName();

        name = name.substring(name.lastIndexOf('.'));
        if (name.endsWith("Factory")) {
            name = name.substring(0, name.length() - 7);
        } else if (name.endsWith("FactorySpi")) {
            name = name.substring(0, name.length() - 10);
        }
        return name;
    }

    public boolean canProcess(Map params) {
        if (params == null) {
            return false;
        }
        Param arrayParameters[] = getParametersInfo();
        for (int i = 0; i < arrayParameters.length; i++) {
            Param param = arrayParameters[i];
            Object value;
            if (!params.containsKey(param.key)) {
                if (param.required) {
                    return false; // missing required key!
                } else {
                    continue;
                }
            }
            try {
                value = param.lookUp(params);
            } catch (IOException e) {
                // could not upconvert/parse to expected type!
                // even if this parameter is not required
                // we are going to refuse to process
                // these params
                return false;
            }
            if (value == null) {
                if (param.required) {
                    return (false);
                }
            } else {
                if (!param.type.isInstance(value)) {
                    return false; // value was not of the required type
                }
            }
        }
        return true;
    }
    
    /**
     * Returns the implementation hints. The default implementation returns en empty map.
     */
    public Map getImplementationHints() {
        return Collections.EMPTY_MAP;
    }
}
