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
package org.geotools.validation.spatial;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.ResourceBundle;
import org.geotools.validation.DefaultIntegrityValidationBeanInfo;

/**
 * LineAbstractValidationBeanInfopurpose.
 *
 * <p>Description of LineAbstractValidationBeanInfo...
 *
 * @author dzwiers, Refractions Research, Inc.
 * @author $Author: dmzwiers $ (last modification)
 * @version $Id$
 */
public class PolygonPointAbstractValidationBeanInfo extends DefaultIntegrityValidationBeanInfo {
    /**
     * LineAbstractValidationBeanInfoconstructor.
     *
     * <p>Description
     */
    public PolygonPointAbstractValidationBeanInfo() {
        super();
    }

    /**
     * Implementation of getPropertyDescriptors.
     *
     * @see java.beans.BeanInfo#getPropertyDescriptors()
     */
    public PropertyDescriptor[] getPropertyDescriptors() {
        PropertyDescriptor[] pd2 = super.getPropertyDescriptors();
        ResourceBundle resourceBundle = getResourceBundle(PolygonPointAbstractValidation.class);

        if (pd2 == null) {
            pd2 = new PropertyDescriptor[0];
        }

        PropertyDescriptor[] pd = new PropertyDescriptor[pd2.length + 2];
        int i = 0;

        for (; i < pd2.length; i++) pd[i] = pd2[i];

        try {
            pd[i] =
                    createPropertyDescriptor(
                            "polygonTypeRef", PolygonPointAbstractValidation.class, resourceBundle);
            pd[i].setExpert(false);
            pd[i + 1] =
                    createPropertyDescriptor(
                            "restrictedPointTypeRef",
                            PolygonPointAbstractValidation.class,
                            resourceBundle);
            pd[i + 1].setExpert(false);
        } catch (IntrospectionException e) {
            pd = pd2;

            // TODO error, log here
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
        }

        return pd;
    }
}
