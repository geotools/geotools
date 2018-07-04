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
package org.geotools.validation.attributes;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.ResourceBundle;
import org.geotools.validation.DefaultFeatureValidationBeanInfo;

/**
 * GazetteerNameValidationBeanInfo purpose.
 *
 * <p>Description of GazetteerNameValidationBeanInfo ...
 *
 * @author dzwiers, Refractions Research, Inc.
 * @author $Author: dmzwiers $ (last modification)
 * @source $URL$
 * @version $Id$
 */
public class GazetteerNameValidationBeanInfo extends DefaultFeatureValidationBeanInfo {
    /**
     * GazetteerNameValidationBeanInfo constructor.
     *
     * <p>Description
     */
    public GazetteerNameValidationBeanInfo() {
        super();
    }

    /**
     * Implementation of getPropertyDescriptors.
     *
     * @see java.beans.BeanInfo#getPropertyDescriptors()
     */
    public PropertyDescriptor[] getPropertyDescriptors() {
        PropertyDescriptor[] pd2 = super.getPropertyDescriptors();
        ResourceBundle resourceBundle = getResourceBundle(GazetteerNameValidation.class);

        if (pd2 == null) {
            pd2 = new PropertyDescriptor[0];
        }

        PropertyDescriptor[] pd = new PropertyDescriptor[pd2.length + 2];
        int i = 0;

        for (; i < pd2.length; i++) pd[i] = pd2[i];

        try {
            pd[i] =
                    createPropertyDescriptor(
                            "attributeName", GazetteerNameValidation.class, resourceBundle);
            pd[i].setExpert(false);
            pd[i + 1] =
                    createPropertyDescriptor(
                            "gazetteer", GazetteerNameValidation.class, resourceBundle);
            pd[i + 1].setExpert(false);
        } catch (IntrospectionException e) {
            pd = pd2;

            // TODO error, log here
            e.printStackTrace();
        }

        return pd;
    }
}
