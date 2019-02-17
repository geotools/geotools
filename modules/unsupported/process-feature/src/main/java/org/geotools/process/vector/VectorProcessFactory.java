/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.process.vector;

import org.geotools.process.factory.AnnotatedBeanProcessFactory;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.text.Text;

/**
 * Factory providing a number of processes for working with feature data.
 *
 * <p>Internally this factory makes use of the information provided by the {@link DescribeProcess}
 * annotations to produce the correct process description.
 *
 * @author Jody Garnett (LISAsoft)
 */
public class VectorProcessFactory extends AnnotatedBeanProcessFactory {

    static volatile BeanFactoryRegistry<VectorProcess> registry;

    public static BeanFactoryRegistry<VectorProcess> getRegistry() {
        if (registry == null) {
            synchronized (VectorProcessFactory.class) {
                if (registry == null) {
                    registry = new BeanFactoryRegistry<VectorProcess>(VectorProcess.class);
                }
            }
        }
        return registry;
    }

    public VectorProcessFactory() {
        super(Text.text("Vector processes"), "vec", getRegistry().lookupBeanClasses());
    }
}
