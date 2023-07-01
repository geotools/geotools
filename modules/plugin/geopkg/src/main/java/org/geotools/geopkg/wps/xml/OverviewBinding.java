/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geopkg.wps.xml;

import javax.xml.namespace.QName;
import org.geotools.api.filter.Filter;
import org.geotools.geopkg.wps.GeoPackageProcessRequest;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;

public class OverviewBinding extends AbstractComplexBinding {

    private static final String DISTANCE = "distance";
    private static final String SCALE_DENOMINATOR = "scaleDenominator";

    @Override
    public QName getTarget() {
        return GPKG.overview;
    }

    @Override
    public Class getType() {
        return GeoPackageProcessRequest.Overview.class;
    }

    @Override
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        GeoPackageProcessRequest.Overview overview = new GeoPackageProcessRequest.Overview();
        overview.setName((String) node.getChildValue("name"));
        if (node.hasChild(DISTANCE)) {
            overview.setDistance((Double) node.getChildValue(DISTANCE));
        }
        if (node.hasChild(SCALE_DENOMINATOR)) {
            overview.setScaleDenominator((Double) node.getChildValue(SCALE_DENOMINATOR));
        }
        overview.setFilter((Filter) node.getChildValue("filter"));

        return overview;
    }
}
