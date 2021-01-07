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

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.net.URL;
import org.junit.Assert;
import org.junit.Test;

/**
 * GazetteerNameValidationBeanInfoTest purpose.
 *
 * <p>Description of GazetteerNameValidationBeanInfoTest ...
 *
 * @author dzwiers, Refractions Research, Inc.
 * @author $Author: sploreg $ (last modification)
 * @version $Id$
 */
public class GazetteerNameValidationBeanInfoTest {

    /*
     * Class to test for PropertyDescriptor[] getPropertyDescriptors()
     */
    @Test
    public void testGetPropertyDescriptors() {
        try {
            GazetteerNameValidation gnv = new GazetteerNameValidation();
            gnv.setName("test");
            gnv.setGazetteer(new URL("http://http://hydra/time/"));
            BeanInfo bi = Introspector.getBeanInfo(gnv.getClass());
            PropertyDescriptor[] pd = bi.getPropertyDescriptors();
            PropertyDescriptor url, name;
            url = name = null;
            for (PropertyDescriptor propertyDescriptor : pd) {
                if ("name".equals(propertyDescriptor.getName())) {
                    name = propertyDescriptor;
                }
                if ("gazetteer".equals(propertyDescriptor.getName())) {
                    url = propertyDescriptor;
                }
            }
            Assert.assertNotNull(url);
            Assert.assertNotNull(name);
            Assert.assertEquals("test", name.getReadMethod().invoke(gnv, null));
            Assert.assertEquals(
                    (new URL("http://http://hydra/time/")), url.getReadMethod().invoke(gnv, null));
        } catch (Exception e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            Assert.fail(e.toString());
        }
    }
}
