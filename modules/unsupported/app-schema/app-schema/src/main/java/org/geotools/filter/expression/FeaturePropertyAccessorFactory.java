/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.filter.expression;

import java.util.Enumeration;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathIntrospector;
import org.apache.commons.jxpath.ri.JXPathContextReferenceImpl;
import org.geotools.factory.Hints;
import org.geotools.feature.AttributeImpl;
import org.geotools.feature.ComplexAttributeImpl;
import org.geotools.feature.FeatureImpl;
import org.geotools.feature.GeometryAttributeImpl;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.simple.SimpleFeatureImpl;
import org.geotools.feature.type.AttributeDescriptorImpl;
import org.geotools.feature.type.FeatureTypeImpl;
import org.geotools.feature.type.UniqueNameFeatureTypeImpl;
import org.geotools.feature.xpath.AttributeDescriptorPropertyHandler;
import org.geotools.feature.xpath.AttributeNodePointerFactory;
import org.geotools.feature.xpath.AttributePropertyHandler;
import org.opengis.feature.Attribute;
import org.opengis.feature.ComplexAttribute;
import org.opengis.feature.Feature;
import org.opengis.feature.GeometryAttribute;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.ComplexType;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.PropertyDescriptor;
import org.xml.sax.helpers.NamespaceSupport;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Creates a namespace aware property accessor for ISO Features.
 * <p>
 * The created accessor handles a small subset of xpath expressions, a non-nested "name" which
 * corresponds to a feature attribute, and "@id", corresponding to the feature id.
 * </p>
 * <p>
 * THe property accessor may be run against {@link org.geotools.feature.Feature}, or against
 * {@link org.geotools.feature.FeatureType}. In the former case the feature property value is
 * returned, in the latter the feature property type is returned.
 * </p>
 * 
 * @author Justin Deoliveira, The Open Planning Project
 * @author Gabriel Roldan, Axios Engineering
 * 
 *
 *
 * @source $URL$
 */
public class FeaturePropertyAccessorFactory implements PropertyAccessorFactory {
   
    static {
        // unfortunatley, jxpath only works against concreate classes
        // JXPathIntrospector.registerDynamicClass(DefaultFeature.class,
        // FeaturePropertyHandler.class);
        JXPathContextReferenceImpl.addNodePointerFactory(new AttributeNodePointerFactory());
    }

    /** Single instnace is fine - we are not stateful */
    static PropertyAccessor ATTRIBUTE_ACCESS = new FeaturePropertyAccessor();

    static PropertyAccessor DEFAULT_GEOMETRY_ACCESS = new DefaultGeometryFeaturePropertyAccessor();

    static PropertyAccessor FID_ACCESS = new FidFeaturePropertyAccessor();

    public PropertyAccessor createPropertyAccessor(Class type, String xpath, Class target,
            Hints hints) {
        
        if (SimpleFeature.class.isAssignableFrom(type)) {
            /*
             * This class is not intended for use with SimpleFeature and causes problems when
             * discovered via SPI and used by code expecting SimpleFeature behaviour. In particular
             * WMS styling code may fail when this class is present. See GEOS-3525.
             */
            return null;
        }

        if (xpath == null)
            return null;

        if (!ComplexAttribute.class.isAssignableFrom(type)
                && !ComplexType.class.isAssignableFrom(type)
                && !AttributeDescriptor.class.isAssignableFrom(type))
            return null;
        if("".equals(xpath))
        //if ("".equals(xpath) && target == Geometry.class)
            return DEFAULT_GEOMETRY_ACCESS;

        // check for fid access
        if (xpath.matches("@(\\w+:)?id"))
            return FID_ACCESS;

        // check for simple property access
        // if (xpath.matches("(\\w+:)?(\\w+)")) {
        NamespaceSupport namespaces = null;
        if (hints != null) {
            namespaces = (NamespaceSupport) hints
                    .get(FeaturePropertyAccessorFactory.NAMESPACE_CONTEXT);
        }
        if (namespaces == null) {
            return ATTRIBUTE_ACCESS;
        } else {
            return new FeaturePropertyAccessor(namespaces);
        }
        // }

        // return null;
    }

    /**
     * We strip off namespace prefix, we need new feature model to do this property
     * <ul>
     * <li>BEFORE: foo:bar
     * <li>AFTER: bar
     * </ul>
     * 
     * @param xpath
     * @return xpath with any XML prefixes removed
     */
    static String stripPrefix(String xpath) {
        int split = xpath.indexOf(":");
        if (split != -1) {
            return xpath.substring(split + 1);
        }
        return xpath;
    }

    /**
     * Access to Feature Identifier.
     * 
     * @author Jody Garnett, Refractions Research Inc.
     */
    static class FidFeaturePropertyAccessor implements PropertyAccessor {

        public boolean canHandle(Object object, String xpath, Class target) {
            // we only work against feature, not feature type
            return object instanceof Attribute && xpath.matches("@(\\w+:)?id");
        }

        public Object get(Object object, String xpath, Class target) {
            Attribute feature = (Attribute) object;
            return feature.getIdentifier().toString();
        }

        public void set(Object object, String xpath, Object value, Class target) {
            throw new org.opengis.feature.IllegalAttributeException(null, value,
                    "feature id is immutable");
        }
    }

    static class DefaultGeometryFeaturePropertyAccessor implements PropertyAccessor {

        public boolean canHandle(Object object, String xpath, Class target) {
            if (!"".equals(xpath))
                return false;

            //if (target != Geometry.class || target != GeometryAttribute.class)
            //    return false;

            return (object instanceof Feature || object instanceof FeatureType);
        }

        public Object get(Object object, String xpath, Class target) {
            if (object instanceof Feature)
                return ((Feature) object).getDefaultGeometryProperty();            
            if (object instanceof FeatureType) {
                FeatureType ft = (FeatureType) object;
                GeometryDescriptor gd = ft.getGeometryDescriptor();            
                if ( gd == null ) {
                    //look for any geometry descriptor
                    for ( PropertyDescriptor pd : ft.getDescriptors() ) {
                        if ( Geometry.class.isAssignableFrom( pd.getType().getBinding() ) ) {
                            return pd;
                        }
                    }
                }                
                return gd;
            }            
            return null;
        }

        public void set(Object object, String xpath, Object value, Class target)
                throws IllegalAttributeException {

            if (object instanceof Feature) {
                final Feature f = (Feature) object;
                GeometryAttribute geom;
                if (value instanceof GeometryAttribute) {
                    geom = (GeometryAttribute) value;
                    f.setDefaultGeometryProperty(geom);
                } else if (value instanceof Geometry) {
                    geom = f.getDefaultGeometryProperty();
                    geom.setValue(value);
                } else {
                    throw new IllegalArgumentException("Argument is not a geometry: " + value);
                }
            }
            if (object instanceof FeatureType) {
                throw new IllegalAttributeException("feature type is immutable");
            }
        }
    }

    static class FeaturePropertyAccessor implements PropertyAccessor {
        static {
            // TODO: use a wrapper public class for Feature in order to
            // support any implementation. Reason being that JXPath works
            // over concrete classes and hence we cannot set it up over the
            // interface
            JXPathIntrospector.registerDynamicClass(FeatureImpl.class,
                    AttributePropertyHandler.class);
            JXPathIntrospector.registerDynamicClass(SimpleFeatureImpl.class,
                    AttributePropertyHandler.class);
            JXPathIntrospector.registerDynamicClass(ComplexAttributeImpl.class,
                    AttributePropertyHandler.class);
            JXPathIntrospector.registerDynamicClass(AttributeImpl.class,
                    AttributePropertyHandler.class);
            JXPathIntrospector.registerDynamicClass(GeometryAttributeImpl.class,
                    AttributePropertyHandler.class);
//            JXPathIntrospector.registerDynamicClass(BooleanAttribute.class,
//                    AttributePropertyHandler.class);
//            JXPathIntrospector.registerDynamicClass(NumericAttribute.class,
//                    AttributePropertyHandler.class);
//            JXPathIntrospector.registerDynamicClass(TemporalAttribute.class,
//                    AttributePropertyHandler.class);
//            JXPathIntrospector.registerDynamicClass(TextualAttribute.class,
//                    AttributePropertyHandler.class);

            JXPathIntrospector.registerDynamicClass(AttributeDescriptorImpl.class,
                    AttributeDescriptorPropertyHandler.class);
            JXPathIntrospector.registerDynamicClass(FeatureTypeImpl.class,
                    AttributeDescriptorPropertyHandler.class);
            JXPathIntrospector.registerDynamicClass(UniqueNameFeatureTypeImpl.class,
                    AttributeDescriptorPropertyHandler.class);
        }

        private NamespaceSupport namespaces;

        public FeaturePropertyAccessor() {
            namespaces = new NamespaceSupport();
        }

        public FeaturePropertyAccessor(NamespaceSupport namespaces) {
            this.namespaces = namespaces;
        }

        public boolean canHandle(Object object, String xpath, Class target) {
            // xpath = stripPrefix(xpath);

            return object instanceof Attribute || object instanceof AttributeType
                    || object instanceof AttributeDescriptor;

        }

        public Object get(Object object, String xpath, Class target) {
            // xpath = stripPrefix(xpath);

            JXPathContext context = JXPathContext.newContext(object);
            // context.setLenient(true); -- NC, edited
            Enumeration declaredPrefixes = namespaces.getDeclaredPrefixes();
            while (declaredPrefixes.hasMoreElements()) {
                String prefix = (String) declaredPrefixes.nextElement();
                String uri = namespaces.getURI(prefix);
                context.registerNamespace(prefix, uri);
            }

            Object value = context.getValue(xpath);

            return value;
        }

        public void set(Object object, String xpath, Object value, Class target)
                throws IllegalAttributeException {
            // xpath = stripPrefix(xpath);

            if (object instanceof FeatureType) {
                throw new IllegalAttributeException("feature type is immutable");
            }

            JXPathContext context = JXPathContext.newContext(object);
            // context.setLenient(true); --NC -edited
            context.setValue(xpath, value);

            assert value == context.getValue(xpath);
        }
    }

}
