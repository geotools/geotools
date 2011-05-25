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

import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.geotools.validation.dto.ArgumentDTO;
import org.geotools.validation.xml.ValidationException;


/**
 * Contains the information required for Validation creation.
 * 
 * <p>
 * Currently just used for configuration, may need to be public for dynamic
 * configuration.
 * </p>
 *
 * @see <a http://vwfs.refractions.net/docs/Validating_Web_Feature_Server.pdf>A 
 *      PDF on Validating Web Feature Servers</a>
 *
 * @source $URL$
 */
public class PlugIn {
    Map defaults;
    String plugInName;
    String plugInDescription;
    BeanInfo beanInfo;
    Map propertyMap;

    PlugIn(Map config) throws ValidationException {
        this(get(config, "name"), get(config, "bean", Validation.class),
            get(config, "description"), config);
    }

    public PlugIn(String name, Class type, String description, Map config)
        throws ValidationException {
        if ((type == null)
                || (!Validation.class.isAssignableFrom(type)
                && type.isInterface())) {
            throw new ValidationException("Not a validation test '" + name
                + "' plugIn:" + type);
        }

        try {
            beanInfo = Introspector.getBeanInfo(type);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ValidationException("Could not use the '" + name
                + "' plugIn:" + type.getName());
        }

        if (config != null) {
            defaults = transArgs(config);
        }

        plugInName = name;
        plugInDescription = description;

        propertyMap = propertyMap(beanInfo);
    }

    private Map transArgs(Map config) {
        Map defaults = new HashMap();
        Iterator i = config.keySet().iterator();

        while (i.hasNext()) {
            String key = (String) i.next();
            Object o = config.get(key);

            if (o instanceof ArgumentDTO) {
                defaults.put(key, ((ArgumentDTO) o).getValue());
            } else {
                defaults.put(key, o);
            }
        }

        return defaults;
    }

    protected PropertyDescriptor propertyInfo(String name) {
        return (PropertyDescriptor) propertyMap.get(name);
    }

    protected static Map propertyMap(BeanInfo info) {
        PropertyDescriptor[] properties = info.getPropertyDescriptors();
        Map lookup = new HashMap(properties.length);

        for (int i = 0; i < properties.length; i++) {
            lookup.put(properties[i].getName(), properties[i]);
        }

        return lookup;
    }

    /**
     * Create a Validation based on provided <code>test</code> definition.
     * 
     * <p>
     * Creates the required Java Bean and configures according to the provided
     * test definition, using this plugIn's defaults.
     * </p>
     *
     * @param name Map defining User's test.
     * @param description DOCUMENT ME!
     * @param args DOCUMENT ME!
     *
     * @return Validation ready for use by the ValidationProcessor
     *
     * @throws ValidationException when an error occurs
     */
    public Validation createValidation(String name, String description, Map args)
        throws ValidationException {
        BeanDescriptor beanDescriptor = beanInfo.getBeanDescriptor();
        Class type = beanDescriptor.getBeanClass();

        Constructor create;

        try {
            create = type.getConstructor(new Class[0]);
        } catch (SecurityException e) {
            throw new ValidationException("Could not create '" + plugInName
                + "' as " + type.getName(), e);
        } catch (NoSuchMethodException e) {
            throw new ValidationException("Could not create '" + plugInName
                + "' as " + type.getName(), e);
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Could not create '" + plugInName
                + "' as " + type.getName(), e);
        }

        Validation validate;

        try {
            validate = (Validation) create.newInstance(new Object[0]);
        } catch (InstantiationException e) {
            throw new ValidationException("Could not create '" + name
                + "' as plugIn " + plugInName, e);
        } catch (IllegalAccessException e) {
            throw new ValidationException("Could not create '" + name
                + "' as plugIn " + plugInName, e);
        } catch (InvocationTargetException e) {
            throw new ValidationException("Could not create '" + name
                + "' as plugIn " + plugInName, e);
        }

        validate.setName(name);
        validate.setDescription(description);
        configure(validate, defaults);
        configure(validate, transArgs(args));

        return validate;
    }

    protected void configure(Object bean, Map config)
        throws ValidationException {
        if ((config == null) || (config.size() == 0)) {
            return;
        }

        PropertyDescriptor property;

        for (Iterator i = config.entrySet().iterator(); i.hasNext();) {
            Map.Entry entry = (Map.Entry) i.next();
            property = propertyInfo((String) entry.getKey());

            if (property == null) {
                // error here
                continue;
            }

            try {
                property.getWriteMethod().invoke(bean,
                    new Object[] { entry.getValue() });
            } catch (IllegalArgumentException e) {
            	String val = entry.getValue() == null? entry.getValue().toString():"null";
                throw new ValidationException("test failed to configure "
                    + plugInName + " " + entry.getKey()+ " "+val, e);
            } catch (IllegalAccessException e) {
				String val = entry.getValue() == null? entry.getValue().toString():"null";
				throw new ValidationException("test failed to configure "
					+ plugInName + " " + entry.getKey()+ " "+val, e);
            } catch (InvocationTargetException e) {
				String val = entry.getValue() == null? entry.getValue().toString():"null";
				throw new ValidationException("test failed to configure "
					+ plugInName + " " + entry.getKey()+ " "+val, e);
            }
        }
    }

    /**
     * get purpose.
     * 
     * <p>
     * Gets a String from a map of Strings
     * </p>
     *
     * @param map Map the map to extract the string from
     * @param key String the key for the map.
     *
     * @return String the value in the map.
     *
     * @see Map
     */
    private static String get(Map map, String key) {
        if (map.containsKey(key)) {
            return (String) map.get(key);
        }

        return null;
    }

    /**
     * get purpose.
     * 
     * <p>
     * Gets a Class from a map given the specified key. If the Class is not
     * found the default Class is returned.
     * </p>
     *
     * @param map Map the map to extract the file from
     * @param key String the key to extract the value for
     * @param defaultType The default value should the key not exist.
     *
     * @return Class an boolean as described above.
     */
    private static Class get(Map map, String key, Class defaultType) {
        if (!map.containsKey(key)) {
            return defaultType;
        }

        Object value = map.get(key);

        if (value instanceof Class) {
            return (Class) value;
        }

        if (value instanceof String) {
            try {
                return Class.forName((String) value);
            } catch (ClassNotFoundException e) {
                // error
            }
        }

        return defaultType;
    }

	public Map getPropertyMap() {
		return propertyMap;
	}
}
