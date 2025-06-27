/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2009-2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.appschema.filter.expression;

import java.util.List;
import org.geotools.api.feature.Feature;
import org.geotools.api.feature.IllegalAttributeException;
import org.geotools.appschema.util.XmlXpathUtilites;
import org.geotools.data.complex.xml.XmlXpathFilterData;
import org.geotools.filter.expression.PropertyAccessor;
import org.geotools.filter.expression.PropertyAccessorFactory;
import org.geotools.util.factory.Hints;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * PropertyAccessorFactory used to create property accessors which can handle xpath expressions against instances of
 * {@link Feature}.
 *
 * @author Russell Petty (GeoScience Victoria)
 * @author Rini Angreani (CSIRO Earth Science and Resource Engineering)
 */
public class XmlXPathPropertyAccessorFactory implements PropertyAccessorFactory {
    /** Namespace support hint */
    public static Hints.Key NAMESPACE_SUPPORT = new Hints.Key(NamespaceSupport.class);

    @Override
    public PropertyAccessor createPropertyAccessor(Class type, String xpath, Class target, Hints hints) {
        if (XmlXpathFilterData.class.isAssignableFrom(type)) {
            return new XmlXPathPropertyAcessor();
        }

        return null;
    }

    static class XmlXPathPropertyAcessor implements PropertyAccessor {
        @Override
        public boolean canHandle(Object object, String xpath, Class target) {
            // TODO: some better check for a valid xpath expression
            return xpath != null && !"".equals(xpath.trim());
        }

        @Override
        public <T> T get(Object object, String xpath, Class<T> target) throws IllegalArgumentException {

            XmlXpathFilterData xmlResponse = (XmlXpathFilterData) object;
            String indexXpath = createIndexedXpath(xmlResponse, xpath);

            List<String> ls =
                    XmlXpathUtilites.getXPathValues(xmlResponse.getNamespaces(), indexXpath, xmlResponse.getDoc());
            if (ls != null && !ls.isEmpty()) {
                return target.cast(ls.get(0));
            }
            return null;
        }

        @Override
        public void set(Object object, String xpath, Object value, Class target) throws IllegalAttributeException {
            throw new UnsupportedOperationException("Do not support updating.");
            // context(object).setValue(xpath, value);
        }

        private String createIndexedXpath(XmlXpathFilterData xmlResponse, String xpathString) {

            String itemXpath = xmlResponse.getItemXpath();
            // if xpathString is from mapping file, as a function expression or inputattribute
            // it wouldn't be indexed
            // however the itemXpath would be indexed as it comes from the node
            // so need to remove the indexes when doing a search
            String unindexedXpath = XmlXpathUtilites.removeIndexes(itemXpath);
            int position = xpathString.indexOf(unindexedXpath);
            if (position != 0) {
                throw new RuntimeException("xpath passed in does not begin with itemXpath"
                        + "/n xpathString ="
                        + xpathString
                        + "/n itemXpath ="
                        + itemXpath);
            }

            StringBuffer sb = new StringBuffer(itemXpath);
            int count = xmlResponse.getCount();
            if (count > -1) {
                sb.append("[" + xmlResponse.getCount() + "]");
            }
            sb.append(xpathString.substring(unindexedXpath.length()));
            return sb.toString();
        }
    }
}
