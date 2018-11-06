/*
 * GeoTools - The Open Source Java GIS Toolkit http://geotools.org
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

import java.util.ArrayList;
import java.util.List;
import net.opengis.ows11.LanguageStringType;
import net.opengis.ows11.ServiceIdentificationType;
import net.opengis.ows11.impl.KeywordsTypeImpl;
import net.opengis.ows11.impl.LanguageStringTypeImpl;
import org.geotools.data.ows.Service;

/**
 * @author ian
 * @author Matthias Schulze (LDBV at ldbv dot bayern dot de)
 */
public class WMTSService extends Service {

    public WMTSService(ServiceIdentificationType serviceType) {

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
