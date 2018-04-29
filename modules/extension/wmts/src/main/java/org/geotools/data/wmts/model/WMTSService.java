/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wmts.model;

import java.util.ArrayList;
import java.util.List;
import net.opengis.ows11.LanguageStringType;
import net.opengis.ows11.ServiceIdentificationType;
import net.opengis.ows11.impl.KeywordsTypeImpl;
import net.opengis.ows11.impl.LanguageStringTypeImpl;
import org.geotools.data.ows.Service;

/** @author ian */
public class WMTSService extends Service {

    public WMTSService(ServiceIdentificationType serviceType) {

        String title =
                serviceType.getTitle().isEmpty()
                        ? "N/A"
                        : ((LanguageStringType) serviceType.getTitle().get(0)).getValue();
        setTitle(title);
        setName(serviceType.getServiceType().getValue());

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
