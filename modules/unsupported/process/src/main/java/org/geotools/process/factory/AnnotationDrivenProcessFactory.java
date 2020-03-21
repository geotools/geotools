/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2009-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.process.factory;

import java.awt.RenderingHints.Key;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.data.Parameter;
import org.geotools.data.Query;
import org.geotools.process.Process;
import org.geotools.process.ProcessException;
import org.geotools.process.ProcessFactory;
import org.geotools.process.RenderingProcess;
import org.geotools.util.Converters;
import org.geotools.util.SimpleInternationalString;
import org.geotools.util.logging.Logging;
import org.opengis.coverage.grid.GridCoverageReader;
import org.opengis.coverage.grid.GridGeometry;
import org.opengis.feature.type.Name;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.util.InternationalString;
import org.opengis.util.ProgressListener;

/**
 * A process factory that uses annotations to determine much of the metadata needed to describe a
 * {@link Process}.
 *
 * <p>The annotations supported are:
 *
 * <ul>
 *   <li>{@link DescribeProcess} - describes the process functionality
 *   <li>{@link DescribeResult} - describes a process result
 *   <li>{@link DescribeParameter} - describes a process parameter
 * </ul>
 *
 * @author jody
 * @author aaime
 */
public abstract class AnnotationDrivenProcessFactory implements ProcessFactory {

    static final Logger LOGGER = Logging.getLogger(AnnotationDrivenProcessFactory.class);

    protected String namespace;

    InternationalString title;

    private static Map<Class, Class> PRIMITIVE_MAPPER =
            new HashMap<Class, Class>() {
                {
                    put(boolean.class, Boolean.class);
                    put(char.class, Character.class);
                    put(byte.class, Byte.class);
                    put(short.class, Short.class);
                    put(int.class, Integer.class);
                    put(long.class, Long.class);
                    put(double.class, Double.class);
                    put(float.class, Float.class);
                }
            };

    public AnnotationDrivenProcessFactory(InternationalString title, String namespace) {
        this.namespace = namespace;
        this.title = title;
    }

    protected abstract DescribeProcess getProcessDescription(Name name);

    protected abstract Method method(String localPart);

    public InternationalString getTitle() {
        return title;
    }

    public InternationalString getDescription(Name name) {
        DescribeProcess info = getProcessDescription(name);
        if (info != null) {
            return new SimpleInternationalString(info.description());
        } else {
            return null;
        }
    }

    public Map<String, Parameter<?>> getParameterInfo(Name name) {
        // build the parameter descriptions by using the DescribeParameter
        // annotations
        Method method = method(name.getLocalPart());
        Map<String, Parameter<?>> input = new LinkedHashMap<String, Parameter<?>>();
        Annotation[][] params = method.getParameterAnnotations();
        Class<?>[] paramTypes = getMethodParamTypes(method);
        for (int i = 0; i < paramTypes.length; i++) {
            if (!(ProgressListener.class.isAssignableFrom(paramTypes[i]))) {
                Parameter<?> param =
                        paramInfo(method.getDeclaringClass(), i, paramTypes[i], params[i]);
                input.put(param.key, param);
            }
        }
        return input;
    }

    // expand to non primitive, the Process API only allows objects to be passed anyways
    // and using primitives is a source of troubles with converters, since generics cannot
    // deal with primitive values
    private Class<?>[] getMethodParamTypes(Method method) {
        Class<?>[] paramTypes = method.getParameterTypes();
        for (int i = 0; i < paramTypes.length; i++) {
            if (paramTypes[i].isPrimitive()) {
                paramTypes[i] = PRIMITIVE_MAPPER.get(paramTypes[i]);
            }
        }

        return paramTypes;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Parameter<?>> getResultInfo(Name name, Map<String, Object> parameters)
            throws IllegalArgumentException {
        Method method = method(name.getLocalPart());
        // look for the DescribeResult annotations (the result could be a
        // key/value
        // map holding multiple results)
        Map<String, Parameter<?>> result = new LinkedHashMap<String, Parameter<?>>();
        for (Annotation annotation : method.getAnnotations()) {
            if (annotation instanceof DescribeResult) {
                DescribeResult info = (DescribeResult) annotation;
                // see if a type has been declared, otherwise use the annotation
                addResult(method, result, info);
            } else if (annotation instanceof DescribeResults) {
                DescribeResults info = (DescribeResults) annotation;
                for (DescribeResult dr : info.value()) {
                    addResult(method, result, dr);
                }
            }
        }
        // if annotation is not found, return a generic description using
        // the method return type
        if (result.isEmpty()) {
            if (!Void.class.equals(method.getReturnType())) {
                Parameter<?> VALUE =
                        new Parameter(
                                "result",
                                method.getReturnType(),
                                "Process result",
                                "No description is available");
                result.put(VALUE.key, VALUE);
            }
        }
        return result;
    }

    private void addResult(Method method, Map<String, Parameter<?>> result, DescribeResult info) {
        Class resultType = info.type();
        if (Object.class.equals(resultType)) {
            resultType = method.getReturnType();
        }

        // metadata
        Map<String, Object> metadata = null;
        if (info.meta() != null && info.meta().length > 0) {
            String[] meta = info.meta();
            metadata = new HashMap<String, Object>();
            fillParameterMetadata(meta, metadata);
        }

        int min = info.primary() ? 0 : 1;
        Parameter resultParam =
                new Parameter(
                        info.name(),
                        resultType,
                        new SimpleInternationalString(info.name()),
                        new SimpleInternationalString(info.description()),
                        min > 0,
                        min,
                        1,
                        null,
                        metadata);
        result.put(resultParam.key, resultParam);
    }

    public InternationalString getTitle(Name name) {
        DescribeProcess info = getProcessDescription(name);
        if (info != null) {
            return new SimpleInternationalString(info.title());
        } else {
            return null;
        }
    }

    public String getVersion(Name name) {
        DescribeProcess info = getProcessDescription(name);
        if (info != null) {
            return info.version();
        } else {
            return null;
        }
    }

    public boolean supportsProgress(Name name) {
        return false;
    }

    public boolean isAvailable() {
        return true;
    }

    public Map<Key, ?> getImplementationHints() {
        return null;
    }

    @SuppressWarnings("unchecked")
    Parameter<?> paramInfo(Class process, int i, Class<?> type, Annotation[] paramAnnotations) {
        DescribeParameter info = null;
        for (Annotation annotation : paramAnnotations) {
            if (annotation instanceof DescribeParameter) {
                info = (DescribeParameter) annotation;
                break;
            }
        }
        // handle collection type and multiplicity
        boolean collection = Collection.class.isAssignableFrom(type);
        int min = 1;
        int max = 1;
        if (collection) {
            if (info != null) {
                type = info.collectionType();
                if (type == null) {
                    type = Object.class;
                }
                min = info.min() > -1 ? info.min() : 0;
                max = info.max() > -1 ? info.max() : Integer.MAX_VALUE;
            } else {
                type = Object.class;
                min = 0;
                max = Integer.MAX_VALUE;
            }
        } else if (type.isArray()) {
            if (info != null) {
                min = info.min() > -1 ? info.min() : 0;
                max = info.max() > -1 ? info.max() : Integer.MAX_VALUE;
            } else {
                min = 0;
                max = Integer.MAX_VALUE;
            }
            type = type.getComponentType();
        } else {
            if (info != null) {
                if (info.min() > 1) {
                    throw new IllegalArgumentException(
                            "The non collection parameter at index "
                                    + i
                                    + " cannot have a min multiplicity > 1");
                }
                min = info.min() > -1 ? info.min() : 1;
                if (info.max() > 1) {
                    throw new IllegalArgumentException(
                            "The non collection parameter at index "
                                    + i
                                    + " cannot have a max multiplicity > 1");
                }
                max = info.max() > -1 ? info.max() : 1;
            }
        }
        if (min > max) {
            throw new IllegalArgumentException(
                    "Min occurrences > max occurrences for parameter at index " + i);
        }
        if (min == 0 && max == 1 && type.isPrimitive()) {
            throw new IllegalArgumentException(
                    "Optional values cannot be primitives, "
                            + "use the associated object wrapper instead: "
                            + info.name()
                            + " in process "
                            + process.getName());
        }

        HashMap<String, Object> metadata = new HashMap<String, Object>();
        if (info != null) {
            double minValue = info.minValue();
            if (minValue != Double.NEGATIVE_INFINITY) {
                metadata.put(Parameter.MIN, Double.valueOf(minValue));
            }
            double maxValue = info.maxValue();
            if (maxValue != Double.POSITIVE_INFINITY) {
                metadata.put(Parameter.MAX, Double.valueOf(maxValue));
            }
        }

        Object defaultValue = null;
        if (info != null && !DescribeParameter.DEFAULT_NULL.equals(info.defaultValue())) {
            String strDefault = info.defaultValue();

            // lookup for constant value
            defaultValue = lookupConstant(strDefault, process, type);

            // try a string to target value conversion
            if (defaultValue == null) {
                defaultValue = Converters.convert(strDefault, type);
            }

            if (defaultValue == null) {
                throw new IllegalArgumentException(
                        "Default value "
                                + strDefault
                                + " could not be converted to target type "
                                + type);
            }
        }

        // other metadata
        if (info != null && info.meta() != null && info.meta().length > 0) {
            String[] meta = info.meta();
            fillParameterMetadata(meta, metadata);
        }

        // finally build the parameter
        if (info != null) {
            return new Parameter(
                    info.name(),
                    type,
                    new SimpleInternationalString(info.name()),
                    new SimpleInternationalString(info.description()),
                    min > 0,
                    min,
                    max,
                    defaultValue,
                    metadata);
        } else {
            return new Parameter(
                    "arg" + i,
                    type,
                    new SimpleInternationalString("Argument " + i),
                    new SimpleInternationalString("Input " + type.getName() + " value"),
                    min > 0,
                    min,
                    max,
                    defaultValue,
                    metadata);
        }
    }

    private void fillParameterMetadata(String[] metas, Map<String, Object> metadata) {
        for (String pair : metas) {
            int idx = pair.indexOf('=');
            String key, value;
            if (idx > 0) {
                key = pair.substring(0, idx);
                value = pair.substring(idx + 1);
            } else {
                key = pair;
                value = null;
            }

            metadata.put(key, value);
        }
    }

    /** Uses the provided path to look up a constant of the specified type. If the path */
    private Object lookupConstant(String path, Class<?> process, Class<?> type) {
        int hashIdx = path.indexOf("#");
        if (hashIdx == -1) {
            // simple reference into the target type, let's see if the constant is is the
            // process class
            Object result = getConstantValue(path, process, type);
            if (result == null) {
                // see if it's in the target class then
                result = getConstantValue(path, type, type);
            }
            return result;
        } else {
            // an absolute reference
            String className = path.substring(0, hashIdx);
            String field = path.substring(hashIdx + 1);
            try {
                Class holder = Class.forName(className);
                return getConstantValue(field, holder, type);
            } catch (ClassNotFoundException e) {
                throw new IllegalArgumentException("Failed to locate class " + className);
            }
        }
    }

    @SuppressWarnings(
            "deprecation") // Method.isAccessible is deprecated but replacement not available in
    // Java 8
    private Object getConstantValue(String path, Class<?> holder, Class<?> target) {
        Field field = null;
        try {
            field = holder.getDeclaredField(path);
        } catch (NoSuchFieldException e) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(
                        Level.FINE, "Failed to locate the field " + path + " in class " + holder);
            }
            return null;
        }

        // is it a constant?
        if ((field.getModifiers() & (Modifier.FINAL | Modifier.STATIC)) == 0) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(
                        Level.FINE,
                        "Field " + path + " found in class " + holder + ", but it's not a costant");
            }
            return null;
        }

        try {
            // make the field accessible, the constant is not necessarily public, especially in the
            // process class
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }

            Object result = field.get(null);
            return Converters.convert(result, target);
        } catch (Exception e) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(
                        Level.FINE,
                        "Field " + path + " found in class " + holder + ", but failed to access it",
                        e);
            }
            return null;
        }
    }

    /**
     * Cerate a process (for the indicated name).
     *
     * <p>Subclasses can control the process using their implementation of:
     *
     * <ul>
     *   <li>{@ #method(String)}: must return a non null method
     *   <li>{@link #createProcessBean(Name)}: return a bean to use; or null for static methods
     * </ul>
     */
    public Process create(Name name) {
        String methodName = name.getLocalPart();
        Method meth = method(methodName);
        Object process = createProcessBean(name);
        if (process != null
                && (lookupInvertGridGeometry(process, meth.getName()) != null
                        || lookupInvertQuery(process, meth.getName()) != null
                        || lookupCustomizeReadParams(process, meth.getName()) != null)) {
            return new InvokeMethodRenderingProcess(meth, process);
        } else {
            return new InvokeMethodProcess(meth, process);
        }
    }

    /**
     * Looks up a method in an object by simple name (restricted to public methods).
     *
     * @param targetObject object, usually a java bean, on which to perform reflection
     * @return Method found
     */
    Method lookupMethod(Object targetObject, String methodName) {
        Method method = null;
        for (Method m : targetObject.getClass().getMethods()) {
            if (Modifier.isPublic(m.getModifiers()) && methodName.equals(m.getName())) {
                method = m;
                break;
            }
        }
        return method;
    }

    /**
     * Used to recognise {@link RenderingProcess} implementations; returns a non null method for
     * {@link RenderingProcess#invertGridGeometry(Map, Query, GridGeometry)}.
     *
     * <p>Used to look up the method to use for "invertGridGeometry"; if a specific method name is
     * not provided "invertGridGeometry" will be used.
     *
     * <p>
     *
     * <ul>
     *   <li>For {@literal null} method name "invertGridGeometry" will be used.
     *   <li>For {@literal "execute"} method name "invertGridGeometry" will be used.
     *   <li>For {@literal "buffer"} method name "bufferInvertGridGeometry" will be used
     * </ul>
     *
     * @param targetObject Target object; may be null for static method lookup
     * @param methodName method to use for "invertGridGeometry"
     * @return method to use for RenderingProcess "invertGridGeometry", or null if not a
     *     RenderingProcess
     */
    protected Method lookupInvertGridGeometry(Object targetObject, String methodName) {
        if (methodName == null || "execute".equals(methodName)) {
            methodName = "invertGridGeometry";
        } else {
            methodName = methodName + "InvertGridGeometry";
        }
        return lookupMethod(targetObject, methodName);
    }

    /**
     * Used to recognise {@link RenderingProcess} implementations; returns a non null method for
     * {@link RenderingProcess#customizeReadParams(Map, GridCoverageReader,
     * GeneralParameterValue[])}.
     *
     * <p>Used to look up the method to use for "customizeReadParams"; if a specific method name is
     * not provided "customizeReadParams" will be used.
     *
     * <p>
     *
     * <ul>
     *   <li>For {@literal null} method name "customizeReadParams" will be used.
     *   <li>For {@literal "execute"} method name "customizeReadParams" will be used.
     *   <li>For {@literal "buffer"} method name "customizeReadParams" will be used
     * </ul>
     *
     * @param targetObject Target object; may be null for static method lookup
     * @param methodName method to use for "invertGridGeometry"
     * @return method to use for RenderingProcess "invertGridGeometry", or null if not a
     *     RenderingProcess
     */
    protected Method lookupCustomizeReadParams(Object targetObject, String methodName) {
        if (methodName == null || "execute".equals(methodName)) {
            methodName = "customizeReadParams";
        } else {
            methodName = methodName + "CustomizeReadParams";
        }
        return lookupMethod(targetObject, methodName);
    }

    /**
     * Used to recognise {@link RenderingProcess} implementations; returns a non null method for
     * {@link RenderingProcess#invertQuery(Map, Query, GridGeometry)}.
     *
     * <p>Used to look up the method to use for "invertQuery"; if a specific method name is not
     * provided "invertGridGeometry" will be used.
     *
     * <p>
     *
     * <ul>
     *   <li>For {@literal null} method name "invertQuery" will be used.
     *   <li>For {@literal "execute"} method name "invertQuery" will be used.
     *   <li>For {@literal "buffer"} method name "bufferInvertQuery" will be used
     * </ul>
     *
     * @param targetObject Target object; may be null for static method lookup
     * @param methodName method to use for "invertQuery"
     * @return method to use for RenderingProcess "invertQuery", or <code>null</code> if not a
     *     RenderingProcess
     */
    protected Method lookupInvertQuery(Object targetObject, String methodName) {
        if (methodName == null || "execute".equals(methodName)) {
            methodName = "invertQuery";
        } else {
            methodName = methodName + "InvertQuery";
        }
        return lookupMethod(targetObject, methodName);
    }

    /**
     * Creates the bean upon which the process execution method will be invoked.
     *
     * <p>Can be null in case the method is a static one
     *
     * @param name Name of the process bean
     * @return intance of process bean; or null if the method is a static method
     */
    protected abstract Object createProcessBean(Name name);

    /**
     * A wrapper which executes the given method as a {@link Process}. When the process {@link
     * #execute(Map, ProgressListener)} is called the method is invoked to produce a result. The
     * mapping from the process parameters to the method parameters is determined by the {@link
     * DescribeParameter} annotations on the method parameters.
     */
    class InvokeMethodProcess implements Process {
        /** Method to invoke. */
        Method method;
        /** Target object used to invoke method, may be null when using a static method. */
        Object targetObject;

        /**
         * Creates a wrapper for invoking a method as a process
         *
         * @param method method to invoke. May be static
         * @param targetObject object used to invoke method. May be null
         */
        public InvokeMethodProcess(Method method, Object targetObject) {
            this.method = method;
            this.targetObject = targetObject;
        }

        @SuppressWarnings("unchecked")
        public Map<String, Object> execute(Map<String, Object> input, ProgressListener monitor)
                throws ProcessException {

            Object[] args = buildProcessArguments(method, input, monitor, false);

            // invoke and grab result
            Object value = null;
            try {
                value = method.invoke(targetObject, args);
            } catch (IllegalAccessException e) {
                // report the exception and exit
                if (monitor != null) {
                    monitor.exceptionOccurred(e);
                }
                throw new ProcessException(e);
            } catch (InvocationTargetException e) {
                Throwable t = e.getTargetException();
                // report the exception and exit
                if (monitor != null) {
                    monitor.exceptionOccurred(t);
                }
                if (t instanceof ProcessException) {
                    throw ((ProcessException) t);
                } else {
                    throw new ProcessException(t);
                }
            }

            // build up the result
            if (value instanceof Object[]) {
                // handle the case the implementor used a positional array for
                // returning multiple outputs
                Object values[] = (Object[]) value;
                Map<String, Object> result = new LinkedHashMap<String, Object>();
                int i = 0;
                for (Annotation annotation : method.getAnnotations()) {
                    if (i >= values.length) break; // no more values to encode
                    Object obj = values[i];

                    if (annotation instanceof DescribeResult) {
                        DescribeResult info = (DescribeResult) annotation;
                        addResult(result, obj, info);
                    }
                    if (annotation instanceof DescribeResults) {
                        DescribeResults info = (DescribeResults) annotation;
                        for (DescribeResult dr : info.value()) {
                            addResult(result, obj, dr);
                        }
                    }
                }
                return result;
            } else if (value instanceof Map) {
                Map<String, Object> result = new LinkedHashMap<String, Object>();
                Map<String, Object> map = (Map<String, Object>) value;
                for (Annotation annotation : method.getAnnotations()) {
                    if (annotation instanceof DescribeResult) {
                        DescribeResult info = (DescribeResult) annotation;
                        Object resultValue = map.get(info.name());
                        if (resultValue != null) {
                            addResult(result, resultValue, info);
                        }
                    }
                    if (annotation instanceof DescribeResults) {
                        DescribeResults info = (DescribeResults) annotation;
                        for (DescribeResult dr : info.value()) {
                            Object resultValue = map.get(dr.name());
                            if (resultValue != null) {
                                addResult(result, resultValue, dr);
                            }
                        }
                    }
                }
                return result;
            } else if (!Void.class.equals(method.getReturnType())) {
                // handle the single result case
                Map<String, Object> result = new LinkedHashMap<String, Object>();
                DescribeResult dr = method.getAnnotation(DescribeResult.class);
                if (dr != null) {
                    result.put(dr.name(), value);
                } else {
                    result.put("result", value);
                }
                return result;
            }
            // handle the case where the method returns void
            return null;
        }

        private void addResult(Map<String, Object> result, Object obj, DescribeResult info) {
            if (info.type().isInstance(obj)) {
                result.put(info.name(), obj);
            } else {
                throw new IllegalArgumentException(
                        method.getName()
                                + " unable to encode result "
                                + obj
                                + " as "
                                + info.type());
            }
        }

        protected Object[] buildProcessArguments(
                Method method, Map<String, Object> input, ProgressListener monitor, boolean skip)
                throws ProcessException {
            // build the array of arguments we'll use to invoke the method
            Class<?>[] paramTypes = getMethodParamTypes(method);
            Annotation[][] annotations = method.getParameterAnnotations();
            Object args[] = new Object[paramTypes.length];
            for (int i = 0; i < args.length; i++) {
                if (ProgressListener.class.equals(paramTypes[i])) {
                    // pass in the monitor
                    args[i] = monitor;
                } else {
                    // if we can skip and there is no annotation, skip
                    if ((annotations[i] == null || annotations[i].length == 0) && skip) {
                        continue;
                    }

                    // find the corresponding argument in the input
                    // map and set it
                    Class<? extends Object> target =
                            targetObject == null ? null : targetObject.getClass();
                    Parameter p = paramInfo(target, i, paramTypes[i], annotations[i]);
                    Object value = input.get(p.key);

                    if (value == null && p.getDefaultValue() != null) {
                        value = p.getDefaultValue();
                    }

                    // this takes care of array/collection conversions among
                    // others
                    args[i] = Converters.convert(value, paramTypes[i]);
                    // check the conversion was successful
                    if (args[i] == null && value != null) {
                        throw new ProcessException(
                                "Could not convert "
                                        + value
                                        + " to target type "
                                        + paramTypes[i].getName());
                    }

                    // check multiplicity is respected
                    if (p.minOccurs > 0 && value == null) {
                        throw new ProcessException(
                                "Parameter " + p.key + " is missing but has min multiplicity > 0");
                    } else if (p.maxOccurs > 1) {
                        int size = -1;
                        if (args[i] == null) {
                            size = 0;
                        } else if (paramTypes[i].isArray()) {
                            size = Array.getLength(args[i]);
                        } else {
                            size = ((Collection) args[i]).size();
                        }
                        if (size < p.minOccurs) {
                            throw new ProcessException(
                                    "Parameter "
                                            + p.key
                                            + " has "
                                            + size
                                            + " elements but min occurrences is "
                                            + p.minOccurs);
                        }
                        if (size > p.maxOccurs) {
                            throw new ProcessException(
                                    "Parameter "
                                            + p.key
                                            + " has "
                                            + size
                                            + " elements but max occurrences is "
                                            + p.maxOccurs);
                        }
                    }
                }
            }
            return args;
        }
    }

    /**
     * A wrapper which executes the given method as a {@linkplain RenderingProcess}. When the
     * process {@link #execute(Map, ProgressListener)} is called the method is invoked to produce a
     * result. The mapping from the process parameters to the method parameters is determined by the
     * {@link DescribeParameter} annotations on the method parameters.
     *
     * <p>This implementation supports the additional methods required for a RenderingProcess:
     *
     * <ul>
     *   <li>invertQuery
     *   <li>invertGridGeometry
     * </ul>
     *
     * The signature of these methods in the Process class is annotation-driven. Each method must
     * accept a {@link Query} and a {@link GridGeometry} as its final parameters, but may have any
     * number of parameters preceding them. These parameters must be a subset of the parameters of
     * the given execution method, and they use the same annotation to describe them.
     */
    class InvokeMethodRenderingProcess extends InvokeMethodProcess
            implements Process, RenderingProcess {

        /**
         * Creates a wrapper for invoking a method as a process
         *
         * @param method method to invoke. May be static
         * @param targetObject object used to invoke method. May be null
         */
        public InvokeMethodRenderingProcess(Method method, Object targetObject) {
            super(method, targetObject);
        }

        public Query invertQuery(
                Map<String, Object> input, Query targetQuery, GridGeometry targetGridGeometry)
                throws ProcessException {
            Method invertQueryMethod = lookupInvertQuery(targetObject, method.getName());

            if (invertQueryMethod == null) {
                return targetQuery;
            }

            try {
                Object[] args = buildProcessArguments(invertQueryMethod, input, null, true);
                args[args.length - 2] = targetQuery;
                args[args.length - 1] = targetGridGeometry;

                return (Query) invertQueryMethod.invoke(targetObject, args);
            } catch (IllegalAccessException e) {
                throw new ProcessException(e);
            } catch (InvocationTargetException e) {
                Throwable t = e.getTargetException();
                if (t instanceof ProcessException) {
                    throw ((ProcessException) t);
                } else {
                    throw new ProcessException(t);
                }
            }
        }

        public GridGeometry invertGridGeometry(
                Map<String, Object> input, Query targetQuery, GridGeometry targetGridGeometry)
                throws ProcessException {

            Method invertGridGeometryMethod =
                    lookupInvertGridGeometry(targetObject, this.method.getName());

            if (invertGridGeometryMethod == null) {
                return targetGridGeometry;
            }

            try {
                Object[] args = buildProcessArguments(invertGridGeometryMethod, input, null, true);
                args[args.length - 2] = targetQuery;
                args[args.length - 1] = targetGridGeometry;

                return (GridGeometry) invertGridGeometryMethod.invoke(targetObject, args);
            } catch (IllegalAccessException e) {
                throw new ProcessException(e);
            } catch (InvocationTargetException e) {
                Throwable t = e.getTargetException();
                if (t instanceof ProcessException) {
                    throw ((ProcessException) t);
                } else {
                    throw new ProcessException(t);
                }
            }
        }

        @Override
        public GeneralParameterValue[] customizeReadParams(
                Map<String, Object> input,
                GridCoverageReader reader,
                GeneralParameterValue[] params) {
            Method customizeReadParamsMethod =
                    lookupCustomizeReadParams(targetObject, this.method.getName());

            if (customizeReadParamsMethod == null) {
                return params;
            }

            try {
                Object[] args = buildProcessArguments(customizeReadParamsMethod, input, null, true);
                args[args.length - 2] = reader;
                args[args.length - 1] = params;

                return (GeneralParameterValue[])
                        customizeReadParamsMethod.invoke(targetObject, args);
            } catch (IllegalAccessException e) {
                throw new ProcessException(e);
            } catch (InvocationTargetException e) {
                Throwable t = e.getTargetException();
                if (t instanceof ProcessException) {
                    throw ((ProcessException) t);
                } else {
                    throw new ProcessException(t);
                }
            }
        }
    }
}
