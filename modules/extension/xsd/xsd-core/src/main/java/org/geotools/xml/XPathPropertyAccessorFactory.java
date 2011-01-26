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
package org.geotools.xml;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathContextFactory;
import org.apache.commons.jxpath.ri.JXPathContextReferenceImpl;
import org.xml.sax.helpers.NamespaceSupport;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.geotools.factory.Hints;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.filter.expression.PropertyAccessor;
import org.geotools.filter.expression.PropertyAccessorFactory;
import org.geotools.xml.impl.jxpath.FeatureNodeFactory;


/**
 * PropertyAccessorFactory used to create property accessors which can handle
 * xpath expressions against instances of {@link Feature}.
 *
 * @author Justin Deoliveira, The Open Planning Project
 *
 *
 * @source $URL$
 */
public class XPathPropertyAccessorFactory implements PropertyAccessorFactory {

    static {
        //unfortunatley, jxpath only works against concreate classes
        //JXPathIntrospector.registerDynamicClass(DefaultFeature.class, FeaturePropertyHandler.class);
        JXPathContextReferenceImpl.addNodePointerFactory(new FeatureNodeFactory());
    }

    public PropertyAccessor createPropertyAccessor(Class type, String xpath, Class target,
        Hints hints) {
        if (SimpleFeature.class.isAssignableFrom(type)) {
            return new XPathPropertyAcessor();
        }

        if (SimpleFeatureType.class.isAssignableFrom(type)) {
            return new XPathPropertyAcessor();
        }

        return null;
    }

    static class XPathPropertyAcessor implements PropertyAccessor {
        public boolean canHandle(Object object, String xpath, Class target) {           
            return (xpath != null) && !"".equals(xpath.trim()); 
        }

        public Object get(Object object, String xpath, Class target) {
            return context(object).getValue(xpath);
        }

        public void set(Object object, String xpath, Object value, Class target)
            throws IllegalAttributeException {
            context(object).setValue(xpath, value);
        }

        JXPathContext context(Object object) {
            JXPathContext context = JXPathContextFactory.newInstance().newContext(null, object);
            //context.setLenient(true);

            return context;
        }
    }
}
