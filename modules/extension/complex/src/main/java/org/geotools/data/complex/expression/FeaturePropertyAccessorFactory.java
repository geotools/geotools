/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.complex.expression;

import static org.geotools.filter.expression.SimpleFeaturePropertyAccessorFactory.DEFAULT_GEOMETRY_NAME;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.JXPathContextReferenceImpl;
import org.geotools.api.feature.Attribute;
import org.geotools.api.feature.ComplexAttribute;
import org.geotools.api.feature.Feature;
import org.geotools.api.feature.GeometryAttribute;
import org.geotools.api.feature.IllegalAttributeException;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.AttributeType;
import org.geotools.api.feature.type.ComplexType;
import org.geotools.api.feature.type.FeatureType;
import org.geotools.api.feature.type.GeometryDescriptor;
import org.geotools.api.feature.type.PropertyDescriptor;
import org.geotools.data.complex.feature.xpath.AttributeNodePointer;
import org.geotools.data.complex.feature.xpath.AttributeNodePointerFactory;
import org.geotools.filter.expression.PropertyAccessor;
import org.geotools.filter.expression.PropertyAccessorFactory;
import org.geotools.util.factory.Hints;
import org.geotools.xsd.impl.jxpath.JXPathUtils;
import org.locationtech.jts.geom.Geometry;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * Creates a namespace aware property accessor for ISO Features.
 *
 * <p>The created accessor handles a small subset of xpath expressions, a non-nested "name" which corresponds to a
 * feature attribute, and "@id", corresponding to the feature id.
 *
 * <p>THe property accessor may be run against {@link org.geotools.feature.Feature}, or against
 * {@link org.geotools.feature.FeatureType}. In the former case the feature property value is returned, in the latter a
 * descriptor is returned (in case of "@" attributes, a Name is returned or null if the attribute doesn't exist - can be
 * used to validate an x-path!) .
 *
 * @author Justin Deoliveira (The Open Planning Project)
 * @author Gabriel Roldan (Axios Engineering)
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

    static final Pattern FID_PATTERN = Pattern.compile("@(\\w+:)?id");

    @Override
    public PropertyAccessor createPropertyAccessor(Class type, String xpath, Class target, Hints hints) {

        if (SimpleFeature.class.isAssignableFrom(type) || SimpleFeatureType.class.isAssignableFrom(type)) {
            /*
             * This class is not intended for use with SimpleFeature and causes problems when
             * discovered via SPI and used by code expecting SimpleFeature behaviour. In particular
             * WMS styling code may fail when this class is present. See GEOS-3525.
             */
            return null;
        }

        if (xpath == null) return null;

        if (!ComplexAttribute.class.isAssignableFrom(type)
                && !ComplexType.class.isAssignableFrom(type)
                && !AttributeDescriptor.class.isAssignableFrom(type)) return null;
        if (DEFAULT_GEOMETRY_NAME.equals(xpath)) return DEFAULT_GEOMETRY_ACCESS;

        // check for fid access
        if (FID_PATTERN.matcher(xpath).matches()) return FID_ACCESS;

        // check for simple property access
        // if (xpath.matches("(\\w+:)?(\\w+)")) {
        NamespaceSupport namespaces = null;
        if (hints != null) {
            namespaces = (NamespaceSupport) hints.get(FeaturePropertyAccessorFactory.NAMESPACE_CONTEXT);
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
     * Access to Feature Identifier.
     *
     * @author Jody Garnett (Refractions Research)
     */
    static class FidFeaturePropertyAccessor implements PropertyAccessor {

        @Override
        public boolean canHandle(Object object, String xpath, Class target) {
            // we only work against feature, not feature type
            return object instanceof Attribute && FID_PATTERN.matcher(xpath).matches();
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T> T get(Object object, String xpath, Class<T> target) throws IllegalArgumentException {
            Attribute feature = (Attribute) object;
            return (T) feature.getIdentifier().toString();
        }

        @Override
        public void set(Object object, String xpath, Object value, Class target) {
            throw new org.geotools.api.feature.IllegalAttributeException(null, value, "feature id is immutable");
        }
    }

    static class DefaultGeometryFeaturePropertyAccessor implements PropertyAccessor {

        @Override
        public boolean canHandle(Object object, String xpath, Class target) {
            if (!DEFAULT_GEOMETRY_NAME.equals(xpath)) return false;

            // if (target != Geometry.class || target != GeometryAttribute.class)
            //    return false;

            return object instanceof Feature || object instanceof FeatureType;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T> T get(Object object, String xpath, Class<T> target) throws IllegalArgumentException {
            if (object instanceof Feature) return (T) ((Feature) object).getDefaultGeometryProperty();
            if (object instanceof FeatureType) {
                FeatureType ft = (FeatureType) object;
                GeometryDescriptor gd = ft.getGeometryDescriptor();
                if (gd == null) {
                    // look for any geometry descriptor
                    for (PropertyDescriptor pd : ft.getDescriptors()) {
                        if (Geometry.class.isAssignableFrom(pd.getType().getBinding())) {
                            return (T) pd;
                        }
                    }
                }
                return (T) gd;
            }
            return null;
        }

        @Override
        public void set(Object object, String xpath, Object value, Class target) throws IllegalAttributeException {

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
                throw new IllegalAttributeException(null, "feature type is immutable");
            }
        }
    }

    static class FeaturePropertyAccessor implements PropertyAccessor {
        /*static {
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
                }*/

        private NamespaceSupport namespaces;

        public FeaturePropertyAccessor() {
            namespaces = new NamespaceSupport();
        }

        public FeaturePropertyAccessor(NamespaceSupport namespaces) {
            this.namespaces = namespaces;
        }

        @Override
        public boolean canHandle(Object object, String xpath, Class target) {

            return object instanceof Attribute
                    || object instanceof AttributeType
                    || object instanceof AttributeDescriptor;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T> T get(Object object, String xpath, Class<T> target) throws IllegalArgumentException {

            JXPathContext context = JXPathUtils.newSafeContext(object, false, this.namespaces, true);
            Iterator it = context.iteratePointers(xpath);
            List results = new ArrayList<>();
            while (it.hasNext()) {
                Pointer pointer = (Pointer) it.next();
                if (pointer instanceof AttributeNodePointer) {
                    results.add(((AttributeNodePointer) pointer).getImmediateAttribute());
                } else {
                    results.add(pointer.getValue());
                }
            }

            if (results.isEmpty()) {
                throw new IllegalArgumentException("x-path gives no results.");
            } else if (results.size() == 1) {
                return (T) results.get(0);
            } else {
                return (T) results;
            }
        }

        @Override
        public void set(Object object, String xpath, Object value, Class target) throws IllegalAttributeException {

            if (object instanceof FeatureType) {
                throw new IllegalAttributeException(null, "feature type is immutable");
            }

            JXPathContext context = JXPathUtils.newSafeContext(object, false, this.namespaces, true);
            context.setValue(xpath, value);

            assert value == context.getValue(xpath);
        }
    }
}
