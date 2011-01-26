/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2009, Open Source Geospatial Foundation (OSGeo)
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

import java.util.List;

import org.geotools.data.complex.xml.XmlXpathFilterData;
import org.geotools.factory.Hints;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.util.XmlXpathUtilites;
import org.opengis.feature.Feature;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * PropertyAccessorFactory used to create property accessors which can handle xpath expressions
 * against instances of {@link Feature}.
 * 
 * @author Russell Petty, GSV
* 
 *
 * @source $URL$
 */
public class XmlXPathPropertyAccessorFactory implements PropertyAccessorFactory {
    /**
     * Namespace support hint
     */
    public static Hints.Key NAMESPACE_SUPPORT = new Hints.Key(NamespaceSupport.class);

    public PropertyAccessor createPropertyAccessor(Class type, String xpath, Class target,
            Hints hints) {
        if (XmlXpathFilterData.class.isAssignableFrom(type)) {
            return new XmlXPathPropertyAcessor();
        }

        return null;
    }

    static class XmlXPathPropertyAcessor implements PropertyAccessor {
        public boolean canHandle(Object object, String xpath, Class target) {
            // TODO: some better check for a valid xpath expression
            return (xpath != null) && !"".equals(xpath.trim());
        }

        public Object get(Object object, String xpath, Class target) {

            XmlXpathFilterData xmlResponse = (XmlXpathFilterData) object;
            String indexXpath = createIndexedXpath(xmlResponse, xpath);

            List<String> ls = XmlXpathUtilites.getXPathValues(xmlResponse.getNamespaces(),
                    indexXpath, xmlResponse.getDoc());
            if (ls != null && !ls.isEmpty()) {
                return ls.get(0);
            }
            return null;
        }

        public void set(Object object, String xpath, Object value, Class target)
                throws IllegalAttributeException {
            throw new UnsupportedOperationException("Do not support updating.");
            // context(object).setValue(xpath, value);
        }

        private String createIndexedXpath(XmlXpathFilterData xmlResponse, String xpathString) {

            String itemXpath = xmlResponse.getItemXpath();
            int position = xpathString.indexOf(itemXpath);
            if (position != 0) {
                throw new RuntimeException("xpath passed in does not begin with itemXpath"
                        + "/n xpathString =" + xpathString + "/n itemXpath =" + itemXpath);
            }

            StringBuffer sb = new StringBuffer(itemXpath)
                    .append("[" + xmlResponse.getCount() + "]").append(
                            xpathString.substring(itemXpath.length()));
            return sb.toString();
        }
    }
}
