/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2022, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic.catalog;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.geotools.api.data.Query;
import org.geotools.gce.imagemosaic.MosaicConfigurationBean;
import org.geotools.util.factory.Hints;

public class CatalogConfigurationBeans {

    public static final Hints.Key COVERAGE_NAME = new Hints.Key(String.class);

    private Map<String, CatalogConfigurationBean> configurations;
    private CatalogConfigurationBean loneConfiguration;

    public CatalogConfigurationBeans(Map<String, CatalogConfigurationBean> configurations) {
        if (configurations == null || configurations.isEmpty())
            throw new IllegalArgumentException("The configuration map is null or empty");
        this.configurations = configurations;
        if (this.configurations.isEmpty())
            throw new IllegalArgumentException("The configuration map is null or empty");
        else if (this.configurations.size() == 1)
            this.loneConfiguration = this.configurations.values().iterator().next();
    }

    public CatalogConfigurationBeans(CatalogConfigurationBean loneConfiguration) {
        if (loneConfiguration == null)
            throw new IllegalArgumentException("The configuration is null");
        this.loneConfiguration = loneConfiguration;
    }

    public CatalogConfigurationBeans() {
        this.loneConfiguration = new CatalogConfigurationBean();
    }

    public CatalogConfigurationBeans(List<MosaicConfigurationBean> beans) {
        this(
                beans.stream()
                        .collect(
                                Collectors.toMap(
                                        c -> c.getName(),
                                        c -> c.getCatalogConfigurationBean(),
                                        (c1, c2) -> c1)));
    }

    /**
     * Returns the first configuration associated to the specified type name. Multiple coverages
     * might share the same typename.
     *
     * @param typeName
     * @return
     */
    public CatalogConfigurationBean getByTypeName(String typeName) {
        if (loneConfiguration != null) return loneConfiguration;
        return configurations.values().stream()
                .filter(c -> Objects.equals(typeName, c.getTypeName()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Looks up the configurations considering both the query (looking for the COVERAGE_NAME hint)
     * and falls back on the type name, if the first did not provide a match
     */
    public CatalogConfigurationBean getByTypeQuery(Query q) {
        if (loneConfiguration != null) return loneConfiguration;
        String coverageName = (String) q.getHints().get(COVERAGE_NAME);
        if (coverageName != null) return configurations.get(coverageName);
        return null;
    }

    public CatalogConfigurationBean first() {
        if (loneConfiguration != null) return loneConfiguration;
        return configurations.values().iterator().next();
    }

    public int size() {
        if (loneConfiguration != null) return 1;
        return configurations.size();
    }

    public Set<String> getTypeNames() {
        if (loneConfiguration != null && loneConfiguration.getTypeName() != null)
            return Collections.singleton(loneConfiguration.getTypeName());
        if (configurations != null)
            return configurations.values().stream()
                    .filter(c -> c.getTypeName() != null)
                    .map(c -> c.getTypeName())
                    .collect(Collectors.toSet());
        return Collections.emptySet();
    }
}
