/*
 * GeoTools - The Open Source Java GIS Toolkit
 * http://geotools.org
 *
 * (C) 2017, Open Source Geospatial Foundation (OSGeo)
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; version 2.1 of
 * the License.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */
package org.geotools.ows.wmts.model;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.opengis.ows11.LanguageStringType;
import net.opengis.ows11.ServiceIdentificationType;
import net.opengis.ows11.ServiceProviderType;
import net.opengis.ows11.impl.KeywordsTypeImpl;
import net.opengis.ows11.impl.LanguageStringTypeImpl;
import org.geotools.data.ows.Service;
import org.geotools.metadata.iso.citation.ResponsiblePartyImpl;
import org.geotools.util.SimpleInternationalString;
import org.geotools.util.logging.Logging;

/**
 * @author ian
 * @author Matthias Schulze (LDBV at ldbv dot bayern dot de)
 */
public class WMTSService extends Service {

    static final Logger LOGGER = Logging.getLogger(WMTSService.class);

    public WMTSService(ServiceIdentificationType serviceType, ServiceProviderType serviceProvider) {

        this(serviceType);

        // According to the spec, the serviceProvider section may not exist so guard against it
        if (serviceProvider == null) {
            return;
        }

        ResponsiblePartyImpl contactInfo =
                new ResponsiblePartyImpl(serviceProvider.getServiceContact());

        if (serviceProvider.getProviderName() != null) {
            contactInfo.setOrganisationName(
                    new SimpleInternationalString(serviceProvider.getProviderName()));
        }

        setContactInformation(contactInfo);

        if (serviceProvider.getProviderSite() != null) {
            try {
                URL providerSite = new URL(serviceProvider.getProviderSite().getHref());
                setOnlineResource(providerSite);
            } catch (MalformedURLException e) {
                LOGGER.log(Level.SEVERE, "", e);
            }
        }
    }

    public WMTSService(ServiceIdentificationType serviceType) {

        // Initialise the important items for the service
        setName("");
        setTitle("");

        // According to the spec, the ServiceIdentifier section may not exist so guard against it
        if (serviceType == null) {
            return;
        }

        String title =
                serviceType.getTitle().isEmpty()
                        ? "N/A"
                        : ((LanguageStringType) serviceType.getTitle().get(0)).getValue();
        setTitle(title);
        setName(serviceType.getServiceType().getValue());

        // The Abstract is of Type LanguageStringType, not String.
        StringBuilder sb = new StringBuilder();
        for (Object line : serviceType.getAbstract()) {
            if (line instanceof LanguageStringType) {
                sb.append(((LanguageStringType) line).getValue());
            } else {
                sb.append(line);
            }
        } // end of for

        set_abstract(sb.toString());

        List<String> retList = new ArrayList<>();

        for (Object okti : serviceType.getKeywords()) {
            KeywordsTypeImpl kti = (KeywordsTypeImpl) okti;
            for (Object olsti : kti.getKeyword()) {
                LanguageStringTypeImpl lsti = (LanguageStringTypeImpl) olsti;
                retList.add(lsti.getValue());
            }
        }

        setKeywordList(retList.toArray(new String[retList.size()]));
    }
}
