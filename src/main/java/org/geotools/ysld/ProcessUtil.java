package org.geotools.ysld;

import org.geotools.data.Parameter;
import org.geotools.feature.NameImpl;
import org.geotools.filter.FunctionFactory;
import org.geotools.util.logging.Logging;
import org.opengis.feature.type.Name;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utilities for working with process api, not meant to be public.
 */
public class ProcessUtil {

    static Logger LOG = Logging.getLogger(ProcessUtil.class);

    public static Name processName(String name) {
        String[] split = name.split(":");
        if (split.length == 1) {
            return new NameImpl(split[0]);
        }

        return new NameImpl(split[0], split[1]);
    }

    public static FunctionFactory loadProcessFunctionFactory() {
        Class functionFactoryClass = null;
        try {
            functionFactoryClass =
                    Class.forName("org.geotools.process.function.ProcessFunctionFactory");
        }
        catch(ClassNotFoundException e) {
            return null;
        }

        try {
            return (FunctionFactory) functionFactoryClass.newInstance();
        }
        catch(Exception e) {
            LOG.log(Level.WARNING, "Error creating process function factory", e);
        }

        return null;
    }

    public static Map<String,Parameter> loadProcessInfo(Name name) {
        Class processorsClass = null;
        try {
            processorsClass = Class.forName("org.geotools.process.Processors");
            Method getParameterInfo = processorsClass.getMethod("getParameterInfo", Name.class);
            return (Map<String,Parameter>) getParameterInfo.invoke(null, name);
        }
        catch(Exception e) {
            throw new RuntimeException("Error looking up process info", e);
        }
    }
}
