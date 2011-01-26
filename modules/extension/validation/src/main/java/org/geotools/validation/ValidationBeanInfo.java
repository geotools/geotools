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
package org.geotools.validation;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditorManager;
import java.beans.PropertyEditorSupport;
import java.beans.SimpleBeanInfo;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;


/**
 * Utility class extending SimpleBeanInfo with our own helper functions.
 *
 * @author David Zwiers, Refractions Research, Inc.
 * @author $Author: jive $ (last modification)
 * @source $URL$
 * @version $Id$
 */
public class ValidationBeanInfo extends SimpleBeanInfo {
    /**
     * ValidationBeanInfo constructor.
     * 
     * <p>
     * Description
     * </p>
     */
    public ValidationBeanInfo() {
        super();
        PropertyEditorManager.registerEditor(URL.class, URLPropertyEditor.class);
    }

    /**
     * Implementation of getPropertyDescriptors.  This method should be called
     * by all overriding sub-class methods.  Property names 'name',
     * 'description', 'typeNames'
     *
     *
     * @see java.beans.BeanInfo#getPropertyDescriptors()
     */
    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            PropertyDescriptor[] pd = new PropertyDescriptor[2];
            ResourceBundle resourceBundle = getResourceBundle(Validation.class);

            pd[0] = createPropertyDescriptor("name", Validation.class,
                    resourceBundle);
            pd[0].setExpert(false);
            pd[1] = createPropertyDescriptor("description", Validation.class,
                    resourceBundle);
            pd[1].setExpert(false);

            return pd;
        } catch (IntrospectionException e) {
            // TODO error, log here
            e.printStackTrace();

            return new PropertyDescriptor[0];
        }
    }
    /** Based on getCLass().getName() return target Bean type */
    protected Class beanType(){
    	Class type = getClass();
    	String typeName = type.getName();
    	if( typeName.endsWith( "BeanInfo" ) ){
    		typeName = typeName.substring(0,typeName.length()-8);
    		try {
				return Class.forName( typeName );
			} catch (ClassNotFoundException e) {
				return null;
			}    		
    	}
    	return null;    	
    }
    /** Return bundle for the property file for out beanType */
    protected ResourceBundle getResourceBundle() {
    	return getResourceBundle( beanType () );
    }    
    /** Return bundle for the property file for the provided class */
    protected ResourceBundle getResourceBundle(Class cls) {
        Locale locale = Locale.getDefault();

        try {
            return ResourceBundle.getBundle(cls.getName(), locale);
        } catch (MissingResourceException mre) {
            return ResourceBundle.getBundle(cls.getName());
        }
    }
    protected PropertyDescriptor createPropertyDescriptor(String name, ResourceBundle bundle) throws IntrospectionException {
    	return createPropertyDescriptor( name, beanType(), bundle );
    }    
    protected PropertyDescriptor createPropertyDescriptor(String name,
        Class cls, ResourceBundle resourceBundle) throws IntrospectionException {
        PropertyDescriptor pd = new PropertyDescriptor(name, cls);
        String s = resourceBundle.getString(pd.getName() + ".DisplayName");

        if ((s == null) || (s == "")) {
            s = pd.getDisplayName();
        }

        pd.setDisplayName(s);
        s = resourceBundle.getString(pd.getName() + ".Description");

        if ((s == null) || (s == "")) {
            s = pd.getShortDescription();
        }

        pd.setShortDescription(s);

        return pd;
    }

    /**
     * URLPropertyEditor purpose.
     * 
     * <p>
     * Used to support java.net.URL properties in BeanInfo's
     * </p>
     *
     * @author dzwiers, Refractions Research, Inc.
     * @author $Author: jive $ (last modification)
     * @version $Id$
     */
    class URLPropertyEditor extends PropertyEditorSupport {
        /** the editor's data */
        URL url;

        /**
         * Implementation of getAsText.
         *
         *
         * @see java.beans.PropertyEditor#getAsText()
         */
        public String getAsText() {
            return url.toString();
        }

        /**
         * Implementation of setAsText.
         *
         * @param text
         *
         * @throws IllegalArgumentException
         *
         * @see java.beans.PropertyEditor#setAsText(java.lang.String)
         */
        public void setAsText(String text) throws IllegalArgumentException {
            try {
                url = new URL(text);
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException("Invalid URL: " + text);
            }
        }

        /**
         * Implementation of supportsCustomEditor.
         *
         *
         * @see java.beans.PropertyEditor#supportsCustomEditor()
         */
        public boolean supportsCustomEditor() {
            return false;
        }

        /**
         * Implementation of getJavaInitializationString.
         *
         *
         * @see java.beans.PropertyEditor#getJavaInitializationString()
         */
        public String getJavaInitializationString() {
            return "new URL(\"" + url.toString() + "\")";
        }

        /**
         * Implementation of isPaintable.
         *
         *
         * @see java.beans.PropertyEditor#isPaintable()
         */
        public boolean isPaintable() {
            return false;
        }

        /**
         * Implementation of toString.
         *
         *
         * @see java.lang.Object#toString()
         */
        public String toString() {
            return url.toString();
        }

        /**
         * Implementation of setValue.
         *
         * @param value
         *
         * @see java.beans.PropertyEditor#setValue(java.lang.Object)
         */
        public void setValue(Object value) {
            try {
                url = new URL(value.toString());
            } catch (MalformedURLException e) {
                //TODO error, log this
                e.printStackTrace();
            }
        }

        /**
         * Implementation of getValue.
         *
         *
         * @see java.beans.PropertyEditor#getValue()
         */
        public Object getValue() {
            return url;
        }
    }
}
