/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016 Open Source Geospatial Foundation (OSGeo)
 *    (C) 2014-2016 Boundless Spatial
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
package org.geotools.ysld;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.data.Parameter;
import org.geotools.feature.NameImpl;
import org.geotools.filter.FunctionFactory;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.logging.Logging;
import org.opengis.feature.type.Name;
import org.opengis.filter.expression.Expression;

/** Utilities for working with process api, not meant to be public. */
public class ProcessUtil {

    static Logger LOG = Logging.getLogger(ProcessUtil.class);

    public static Name processName(String name) {
        String[] split = name.split(":");
        if (split.length == 1) {
            return new NameImpl(split[0]);
        }

        return new NameImpl(split[0], split[1]);
    }

    /** @return The loaded {@link FunctionFactory}, or null if it could not be loaded. */
    public static FunctionFactory loadProcessFunctionFactory() {
        Class<?> functionFactoryClass = null;
        try {
            functionFactoryClass =
                    Class.forName("org.geotools.process.function.ProcessFunctionFactory");
        } catch (ClassNotFoundException e) {
            LOG.log(Level.WARNING, "Error creating process function factory", e);
            return null;
        }

        try {
            return (FunctionFactory) functionFactoryClass.newInstance();
        } catch (Exception e) {
            LOG.log(Level.WARNING, "Error creating process function factory", e);
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Parameter<?>> loadProcessInfo(Name name) {
        Class<?> processorsClass = null;
        try {
            processorsClass = Class.forName("org.geotools.process.Processors");
            Method getParameterInfo = processorsClass.getMethod("getParameterInfo", Name.class);
            return (Map<String, Parameter<?>>) getParameterInfo.invoke(null, name);
        } catch (Exception e) {
            throw new RuntimeException("Error looking up process info", e);
        }
    }

    public static boolean isProcess(Expression expr) {
        Class<?> processClass = null;
        try {
            processClass = Class.forName("org.geotools.process.function.ProcessFunction");
            return processClass.isAssignableFrom(expr.getClass());
        } catch (Exception e) {
            throw new RuntimeException("Error looking up process info", e);
        }
    }

    private static boolean hasWMSParam(
            Map<String, Parameter<?>> processInfo, String name, Class<?> type) {
        Parameter<?> param = processInfo.get(name);
        if (param == null) return false;
        if (!param.getName().equals(name)) return false;
        if (!param.isRequired()) return false;
        if (!type.isAssignableFrom(param.getType())) return false;
        return true;
    }

    public static boolean hasWMSParams(Map<String, Parameter<?>> processInfo) {
        return hasWMSParam(processInfo, "outputBBOX", ReferencedEnvelope.class)
                && hasWMSParam(processInfo, "outputWidth", Integer.class)
                && hasWMSParam(processInfo, "outputHeight", Integer.class);
    }
}
