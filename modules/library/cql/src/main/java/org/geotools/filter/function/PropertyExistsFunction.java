/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.function;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

//import org.apache.commons.beanutils.PropertyUtils;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.util.Utilities;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;

/**
 * A new function to check if a property exists.
 *
 *
 * @source $URL$
 */
public class PropertyExistsFunction extends FunctionExpressionImpl {

    public PropertyExistsFunction() {
        super("PropertyExists");
    }

    public int getArgCount() {
        return 1;
    }

    private String getPropertyName() {
        Expression expr = (Expression) getParameters().get(0);

        return getPropertyName(expr);
    }

    private String getPropertyName(Expression expr) {
        String propertyName;

        if (expr instanceof Literal) {
            propertyName = String.valueOf(((Literal) expr).getValue());
        } else if (expr instanceof PropertyName) {
            propertyName = ((PropertyName) expr).getPropertyName();
        } else {
            throw new IllegalStateException("Not a property name expression: " + expr);
        }

        return propertyName;
    }

    /**
     * @return {@link Boolean#TRUE} if the <code>feature</code>'s
     *         {@link FeatureType} contains an attribute named as the property
     *         name passed as this function argument, {@link Boolean#FALSE}
     *         otherwise.
     */
    public Object evaluate(SimpleFeature feature) {
        String propName = getPropertyName();
        AttributeDescriptor attributeType = feature.getFeatureType().getDescriptor(propName);

        return Boolean.valueOf(attributeType != null);
    }

    /**
     * @return {@link Boolean#TRUE} if the Class of the object passed as
     *         argument defines a property names as the property name passed as
     *         this function argument, following the standard Java Beans naming
     *         conventions for getters. {@link Boolean#FALSE} otherwise.
     */
    public Object evaluate(Object bean) {
        if (bean instanceof SimpleFeature) {
            return evaluate((SimpleFeature) bean);
        }

        final String propName = getPropertyName();

       try {
            Class type = bean.getClass();
            //quick 1
//            try {
//                String getName = "get"+propName.substring(0,1).toUpperCase()+propName.substring(1);
//                if (type.getMethod(getName, new Class[0]) != null) {
//                    return true;
//                }
//            } catch (Exception ignore) {
//            }
//            // quick 2
//            try {
//                String isName = "is"+propName.substring(0,1).toUpperCase()+propName.substring(1);
//                if (type.getMethod(isName, new Class[0]) != null) {
//                    return true;
//                }
//            } catch (Exception ignore) {
//            }
            // okay go for real
            BeanInfo info = Introspector.getBeanInfo( type );
            for( PropertyDescriptor descriptor : info.getPropertyDescriptors() ){
                if( descriptor.getName().equals(propName) ){
                    if( descriptor.getReadMethod() != null ){
                        return true;
                    }
                    else {
                        return false; // property found but not writable
                    }
                }
            }
            //PropertyUtils.getProperty(bean, propName);
            //return true;
        } catch (IntrospectionException ignore) {
        }
        return false;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("PropertyExists('");
        sb.append(getPropertyName());
        sb.append("')");

        String stringVal = sb.toString();

        return stringVal;
    }

    public boolean equals(Object obj) {

        if (obj == this)
            return true;
        if (!(obj instanceof PropertyExistsFunction)) {

            return false;
        }
        PropertyExistsFunction other = (PropertyExistsFunction) obj;

        if (other.getParameters().size() != this.getParameters().size())
            return false;
        if (other.getParameters().size() > 0) {
            final String propName = getPropertyName();

            Expression otherPropNameExpr = (Expression) other.getParameters()
                    .get(0);
            final String otherPropName = getPropertyName(otherPropNameExpr);

            return Utilities.equals(propName, otherPropName);
        } else {
            return true;
        }
    }
}
