/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.process.dem;

import java.awt.RenderingHints.Key;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.data.Parameter;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.Hints;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.NameImpl;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.process.Process;
import org.geotools.process.ProcessException;
import org.geotools.process.ProcessFactory;
import org.geotools.process.impl.SingleProcessFactory;
import org.geotools.text.Text;
import org.geotools.util.KVP;
import org.geotools.util.SimpleInternationalString;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.opengis.geometry.Envelope;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.util.InternationalString;
import org.opengis.util.ProgressListener;

import com.vividsolutions.jts.geom.Polygon;

/**
 * ProcessFactory for several digital elevation model processes.
 * 
 * @since 2.7
 * @source $URL:
 *         http://svn.osgeo.org/geotools/trunk/modules/unsupported/process/src/main/java/org/geotools
 *         /process/raster/RasterToVectorFactory.java $
 */
public class DEMProcessFactory implements ProcessFactory {
    private static final String VERSION_STRING = "0.0.1";
    Class<?> TARGET = DEMTools.class;
    String namespace = "http://localhost/dem/";
    
    public Process create(Name name) {
        return new ProcessInvocation(DEMTools.class, name.getLocalPart());
    }
    
    public InternationalString getTitle() {
        return new SimpleInternationalString( "DEM Tools" );
    }

    public InternationalString getDescription(Name name) {
        Method method = method( name.getLocalPart() );
        if( method == null ){
            return null;
        }
        DescribeProcess info = method.getAnnotation( DescribeProcess.class );
        if( info != null ){
            return new SimpleInternationalString( info.description() );
        }
        else {
            return null;
        }
    }

    public Method method( String name) {
        for( Method method : TARGET.getMethods() ){
            DescribeProcess INFO = method.getAnnotation( DescribeProcess.class );
            if( INFO != null ){
                if( name.equalsIgnoreCase( method.getName() )){
                    return method;
                }
            }
        }
        return null;
    }
//    public DescribeProcess info( String name) {
//        for( Method method : TARGET.getMethods() ){
//            DescribeProcess INFO = method.getAnnotation( DescribeProcess.class );
//            if( INFO != null ){
//                if( name.equalsIgnoreCase( method.getName() )){
//                    return INFO;
//                }
//            }
//        }
//        return null;
//    }
    
    public Set<Name> getNames() {
        Set<Name> names = new LinkedHashSet<Name>();
        for( Method method : TARGET.getMethods() ){
            DescribeProcess INFO = method.getAnnotation( DescribeProcess.class );
            if( INFO != null ){
                names.add( new NameImpl( namespace, method.getName() ));
            }
        }
        return names;
    }

    public Map<String, Parameter<?>> getParameterInfo(Name name) {
        Method method = method( name.getLocalPart() );
        Map<String,Parameter<?>> input = new LinkedHashMap<String, Parameter<?>>();
        Annotation[][] PARAM_INFO = method.getParameterAnnotations();
        Class<?>[] PARAM_TYPE = method.getParameterTypes();
        for( int i=0; i< PARAM_TYPE.length; i++ ){
            Parameter<?> param = paramInfo( i, PARAM_TYPE[i], PARAM_INFO[i] );
            input.put( param.key, param );
        }        
        return input;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Parameter<?>> getResultInfo(Name name, Map<String, Object> parameters)
            throws IllegalArgumentException {
        Method method = method( name.getLocalPart() );
        Map<String,Parameter<?>> result = new LinkedHashMap<String, Parameter<?>>();
        for( Annotation annotation : method.getAnnotations() ){
            if( annotation instanceof DescribeResult ){
                DescribeResult info = (DescribeResult) annotation;
                Parameter<?> RESULT = new Parameter(
                        info.name(), info.type(), info.name(), info.description() );
                result.put( RESULT.key, RESULT );
            }
        }
        if( result.isEmpty() ){
            Parameter<?> VALUE = new Parameter( "value", Object.class, "Undefined Value", "No description is available" );
            result.put( VALUE.key, VALUE );
        }
        return result;
    }

    public InternationalString getTitle(Name name) {
        return null;
    }

    public String getVersion(Name name) {
        return null;
    }

    public boolean supportsProgress(Name name) {
        return false;
    }

    public boolean isAvailable() {
        return false;
    }

    public Map<Key, ?> getImplementationHints() {
        return null;
    }

    static class ProcessInvocation implements Process {

        private String name;

        private Class<DEMTools> target;

        public ProcessInvocation(Class<DEMTools> target, String method) {
            this.target = target;
            this.name = method;
        }

        Method method() {
            for (Method method : target.getMethods()) {
                if (name.equalsIgnoreCase(method.getName())) {
                    return method;
                }
            }
            return null;
        }

        @SuppressWarnings("unchecked")
        public Map<String, Object> execute(Map<String, Object> input, ProgressListener monitor)
                throws ProcessException {
            Method method = method();
            if (method == null) {
                return null;
            }
            TypeVariable<Method>[] PARAM_TYPE = method.getTypeParameters();
            Annotation[][] PARAM_INFO = method.getParameterAnnotations();
            Object args[] = new Object[PARAM_TYPE.length];
            for (int i = 0; i < args.length; i++) {
                DescribeParameter PARAMETER = paramInfo(i, PARAM_INFO);
                String name = PARAMETER == null ? "arg"+i : PARAMETER.name();
                
                Object value = input.get(name);
                args[i] = value;
            }
            Object value = null;
            try {
                value = method.invoke(null, args);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

            if (value == null) {
                throw new IllegalArgumentException("Unable to encode null result for " + name);
            }
            if (value instanceof Object[]) {
                Object values[] = (Object[]) value;
                Map<String, Object> result = new LinkedHashMap<String, Object>();
                int i = 0;
                for (Annotation annotation : method.getAnnotations()) {
                    if (i >= values.length)
                        break; // no more values to encode
                    Object obj = values[i];

                    if (annotation instanceof DescribeResult) {
                        DescribeResult RESULT = (DescribeResult) annotation;
                        if (RESULT.type().isInstance(obj)) {
                            result.put(RESULT.name(), obj);
                        } else {
                            throw new IllegalArgumentException(name + " unable to encode result "
                                    + obj + " as " + RESULT.type());
                        }
                    }
                }
                return result;
            } else if (value instanceof Map) {
                return (Map<String, Object>) value;
            } else {
                Map<String, Object> result = new LinkedHashMap<String, Object>();
                DescribeResult RESULT = method.getAnnotation(DescribeResult.class);
                if (RESULT != null) {
                    result.put(RESULT.name(), value);
                } else {
                    result.put("value", value);
                }
            }
            return null;
        }
    }
    
    @SuppressWarnings("unchecked")
    Parameter<?> paramInfo( int i, Class<?> TYPE, Annotation[] PARAM_INFO ){
        DescribeParameter info = null;
        for( Annotation annotation : PARAM_INFO ){
            if (annotation instanceof DescribeParameter) {
                info = (DescribeParameter) annotation;
                break;
            }
        }
        if( info != null ){
            Parameter param = new Parameter(info.name(), TYPE, info.name(), info.description() );
            return param;
        }
        else {
            Parameter param = new Parameter( "arg"+i, TYPE, "Argument "+i, "Input "+TYPE.getName()+" value");
            return param;
        }
    }
    /**
     * Lookup a decent DescribeParameter for the indexed parameter.
     * 
     * @param i
     * @param PARAM_INFO
     * @return
     */
    static DescribeParameter paramInfo(int i, Annotation[][] PARAM_INFO) {
        for (Annotation annotation : PARAM_INFO[i]) {
            if (annotation instanceof DescribeParameter) {
                DescribeParameter PARAMETER = (DescribeParameter) annotation;
                return PARAMETER;
            }
        }
        return null;
    }
}
