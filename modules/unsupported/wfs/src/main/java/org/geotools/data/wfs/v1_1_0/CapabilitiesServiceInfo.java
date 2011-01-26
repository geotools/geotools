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
package org.geotools.data.wfs.v1_1_0;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Icon;

import org.geotools.data.ServiceInfo;
import org.geotools.data.wfs.WFSServiceInfo;
import org.geotools.util.logging.Logging;

/**
 * Adapts a WFS capabilities document to {@link ServiceInfo}
 * 
 * @author Gabriel Roldan
 * @version $Id$
 * @since 2.5.x
 * @source $URL:
 *         http://svn.geotools.org/geotools/trunk/gt/modules/plugin/wfs/src/main/java/org/geotools
 *         /wfs/v_1_1_0/data/CapabilitiesServiceInfo.java $
 */
final class CapabilitiesServiceInfo implements WFSServiceInfo {
    private static final Logger LOGGER = Logging.getLogger("org.geotools.data.wfs");

    private static URI WFS_1_1_0_SCHEMA_URI;
    static {
        try {
            WFS_1_1_0_SCHEMA_URI = new URI("http://schemas.opengis.net/wfs/1.1.0/wfs.xsd");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private WFS_1_1_0_DataStore wfs;

    public CapabilitiesServiceInfo(WFS_1_1_0_DataStore service) {
        this.wfs = service;
    }

    /**
     * Maps to the capabilities' service identification abstract
     * 
     * @see ServiceInfo#getDescription()
     */
    public String getDescription() {
        return wfs.getServiceAbstract();
    }

    /**
     * @return {@code null}
     * @see ServiceInfo#getDescription()
     */
    public Icon getIcon() {
        return null; // talk to Eclesia the icons are in renderer?
    }

    /**
     * Maps to the capabilities' service identification keywords list
     * 
     * @see ServiceInfo#getDescription()
     */
    public Set<String> getKeywords() {
        return wfs.getServiceKeywords();
    }

    /**
     * @see ServiceInfo#getPublisher()
     */
    public URI getPublisher() {
        return wfs.getServiceProviderUri();
    }

    /**
     * Maps to the WFS xsd schema in schemas.opengis.net
     * 
     * @see ServiceInfo#getSchema()
     */
    public URI getSchema() {
        return WFS_1_1_0_SCHEMA_URI;
    }

    /**
     * Maps to the URL of the capabilities document
     * 
     * @see ServiceInfo#getSource()
     */
    public URI getSource() {
        URL url = wfs.getCapabilitiesURL();
        try {
            return url.toURI();
        } catch (URISyntaxException e) {
            LOGGER.log(Level.WARNING, "converting to URI: " + url.toExternalForm());
            return null;
        }
    }

    /**
     * @see ServiceInfo#getTitle()
     */
    public String getTitle() {
        return wfs.getServiceTitle();
    }

    /**
     * @see WFSServiceInfo#getVersion()
     */
    public String getVersion() {
        return wfs.getServiceVersion();
    }
}
