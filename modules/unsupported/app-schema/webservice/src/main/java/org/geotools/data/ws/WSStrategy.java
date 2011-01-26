/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
import java.util.Map;

import net.opengis.wfs.GetFeatureType;

import org.geotools.data.Query;
import org.geotools.data.ws.protocol.ws.WSProtocol;
import org.geotools.filter.Capabilities;
import org.geotools.wfs.WFSConfiguration;
import org.geotools.xml.Configuration;
import org.opengis.filter.Filter;

import freemarker.template.Template;

/**
 * An interface to allow plugging different strategy objects into a {@link XmlDataStore} to take
 * care of specific WS implementations limitations or deviations from the spec.
 * 
 * @author rpetty
 * @version $Id$
 * @since 2.6
 * @source $URL:
 *         http://gtsvn.refractions.net/trunk/modules/unsupported/app-schema/webservice/src/main/java/org/geotools/data
 *         /wfs/v1_1_0/WSStrategy.java $
 * @see WSDataStoreFactory
 * @see DefaultWSStrategy
 * @see CubeWerxStrategy
 */
public interface WSStrategy {

    /**
     * Returns an xml configuration suitable to parse/encode wfs documents appropriate for the
     * server.
     * <p>
     * Note: most of the time it will just be {@link WFSConfiguration}, but it may be possible, for
     * example, an strategy needs to override some bindings.
     * </p>
     * 
     * @return a WFS xml {@link Configuration}
     */
    public Configuration getWsConfiguration();

    public Filter[] splitFilters(Capabilities filterCaps, Filter filter);

    public Template getTemplate();
    
    public Map getRequestData(Query query) throws IOException;
}
