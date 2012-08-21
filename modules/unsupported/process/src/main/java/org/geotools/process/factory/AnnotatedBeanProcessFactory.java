/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2009, Open Source Geospatial Foundation (OSGeo)
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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.geotools.feature.NameImpl;
import org.opengis.feature.type.Name;
import org.opengis.util.InternationalString;

/**
 * Annotation driven process factory; used to wrap up a bunch of Java beans as a single
 * Process Factory.
 * <p>
 * To make use of this class you will need to:
 * <ol>
 * <li>Create an instance passing in a list of "bean" classes you wish to publish:<pre>
 * ProcessFactory factory = new AnnotatedBeanProcessFactory( Text.text("Internal"),"internal", ExampleProcess);</pre></li>
 * <li>Create an implementation of each bean class referenced:
 *    <ul>
 *    <li>Annotate the class with {@link DescribeProcess}:<pre>    @DescribeProcess( title = "bounds",
 *                      description = "Computes the overlall bounds of the input features")
 *    public class BoundsProcess {
 *    ...
 *    }</pre></li>
 *    <li>Supply an <b>execute</b> method (which we can call by reflection):<pre>     @DescribeResult(name = "bounds",
 *                     description = "The feature collection bounds")
 *     public ReferencedEnvelope execute( @DescribeParameter(name = "features",
 *                                                           description = "Collection whose bounds will be computed")
 *                                         FeatureCollection features) {
 *         return features.getBounds();
 *    }
 *    </pre></li>
 *    </ul>
 * </li>
 * <li>Optional: If you are using this technique in an environment such as Spring you may wish to use a
 * "marker interface" to allow Spring to discover implementations on the classpath.<pre>
 * public class BoundsProcess implements GeoServerProcess {
 *     ...
 * }
 * </pre></li>
 * </ol>
 * 
 *
 * @source $URL$
 */
public class AnnotatedBeanProcessFactory extends AnnotationDrivenProcessFactory {
    Map<String, Class<?>> classMap;

    public AnnotatedBeanProcessFactory(InternationalString title, String namespace,
            Class<?>... beanClasses) {
        super(title, namespace);
        classMap = classMap( beanClasses );
    }
    
    /**
     * Method responsible for using reflection on the list of bean classes and producing
     * a map of process names to implementing java bean.
     * <p>
     * This is isolated as a static method to allow for unit test; it is called by the constructor.
     * @param beanClasses
     * @return class map from process name to implementing class.
     */
    static  Map<String, Class<?>> classMap( Class<?>... beanClasses ){
        Map<String,Class<?>> map = new HashMap<String, Class<?>>();
        for (Class<?> c : beanClasses) {
            String name = c.getSimpleName();
            if (name.endsWith("Process")) {
                name = name.substring(0, name.indexOf("Process"));
            }
            map.put(name, c);
        }
        return map;
    }
    
    /**
     * Used to go through the list of java beans; returning the DescribeProcess
     * annotation for each one.
     * @param name Process name
     * @return DescribeProcess annotation for the named process
     */
    @Override
    protected DescribeProcess getProcessDescription(Name name) {
        Class<?> c = classMap.get(name.getLocalPart());
        if (c == null) {
            return null;
        } else {
            return (DescribeProcess) c.getAnnotation(DescribeProcess.class);
        }
    }

    /**
     * Resolves to the <b>execute</b> method for the provided java bean.
     * @return the "execute" method of the indicated java bean.
     */
    @Override
    protected Method method(String className) {
        Class<?> c = classMap.get(className);
        if (c != null) {
            for (Method m : c.getMethods()) {
                if ("execute".equals(m.getName())) {
                    return m;
                }
            }
        }
        return null;
    }
    /**
     * List of processes published; generated from the classMap created in the constructuor.
     */
    public Set<Name> getNames() {
        Set<Name> result = new LinkedHashSet<Name>();
        List<String> names = new ArrayList<String>(classMap.keySet());
        Collections.sort(names);
        for (String name : names) {
            result.add(new NameImpl(namespace, name));
        }
        return result;
    }

    /**
     * Create an instance of the named process bean.
     * <p>
     * By having an actual object here we allow implementors to hold onto a bit of state if they
     * wish. The object will need to have an <n>execute</b> method and be annotated with a
     * describe process annotation.
     */
    protected Object createProcessBean(Name name) {
        try {
            Class<?> processClass = classMap.get(name.getLocalPart());
            if(processClass == null) {
                throw new IllegalArgumentException("Process " + name + " is unknown");
            }
            return processClass.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
