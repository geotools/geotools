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
package org.geotools.data.wfs.internal.v1_x;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.Icon;
import net.opengis.ows10.KeywordsType;
import net.opengis.ows10.OnlineResourceType;
import net.opengis.ows10.ServiceIdentificationType;
import net.opengis.ows10.ServiceProviderType;
import net.opengis.wfs.WFSCapabilitiesType;
import org.geotools.data.ServiceInfo;
import org.geotools.data.wfs.WFSServiceInfo;

/** Adapts a WFS capabilities document to {@link ServiceInfo} */
public final class CapabilitiesServiceInfo implements WFSServiceInfo {

    private final WFSCapabilitiesType capabilities;

    private final URI schemaUri;

    private final URI getCapsUrl;

    public CapabilitiesServiceInfo(
            String schemaUri, URL getCapsUrl, WFSCapabilitiesType capabilities) {
        try {
            this.getCapsUrl = getCapsUrl.toURI();
            this.schemaUri = new URI(schemaUri);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        this.capabilities = capabilities;
    }

    /**
     * Maps to the capabilities' service identification abstract
     *
     * @see ServiceInfo#getDescription()
     */
    public String getDescription() {
        ServiceIdentificationType serviceIdentification = capabilities.getServiceIdentification();
        return serviceIdentification == null ? null : serviceIdentification.getAbstract();
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
        Set<String> kws = new HashSet<String>();
        ServiceIdentificationType serviceIdentification = capabilities.getServiceIdentification();
        if (serviceIdentification != null) {
            @SuppressWarnings("unchecked")
            List<KeywordsType> keywords = serviceIdentification.getKeywords();
            if (keywords != null) {
                for (KeywordsType k : keywords) {
                    kws.addAll(k.getKeyword());
                }
                kws.remove(null);
            }
        }
        return kws;
    }

    /** @see ServiceInfo#getPublisher() */
    public URI getPublisher() {
        ServiceProviderType serviceProvider = capabilities.getServiceProvider();
        if (null == serviceProvider) {
            return null;
        }
        OnlineResourceType providerSite = serviceProvider.getProviderSite();
        if (null == providerSite) {
            return null;
        }
        String href = providerSite.getHref();
        try {
            return href == null ? null : new URI(href);
        } catch (URISyntaxException e) {
            return null;
        }
    }

    /**
     * Maps to the WFS xsd schema in schemas.opengis.net
     *
     * @see ServiceInfo#getSchema()
     */
    public URI getSchema() {
        return schemaUri;
    }

    /**
     * Maps to the URL of the capabilities document
     *
     * @see ServiceInfo#getSource()
     */
    public URI getSource() {
        return getCapsUrl;
    }

    /** @see ServiceInfo#getTitle() */
    public String getTitle() {
        ServiceIdentificationType serviceIdentification = capabilities.getServiceIdentification();
        return serviceIdentification == null ? null : serviceIdentification.getTitle();
    }

    /** @see WFSServiceInfo#getVersion() */
    public String getVersion() {
        return capabilities.getVersion();
    }
}
