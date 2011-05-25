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
package org.geotools.data.ws;

import java.io.IOException;

import org.geotools.data.DataStore;
import org.geotools.data.Query;
import org.geotools.data.complex.xml.XmlResponse;
import org.opengis.feature.type.Name;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * {@link DataStore} extension interface to provide WFS specific extra information.
 * 
 * @author rpetty
 * @version $Id$
 * @since 2.5.x
 *
 * @source $URL$
 *         http://svn.geotools.org/geotools/trunk/gt/modules/unsupported/app-schema/webservice/
 *         src/main/java/org/geotools /data/wfs/WFSDataStore.java $
 */
public interface XmlDataStore extends DataStore {

    void setMaxFeatures(int maxFeatures);

    int getMaxFeatures();

    int getCount(Query query);

    void setItemXpath(String inputAttributeXpathPrefix);

    void setNamespaces(NamespaceSupport namespaces);

    Name getName();

    XmlResponse getXmlReader(Query query) throws IOException;
}
