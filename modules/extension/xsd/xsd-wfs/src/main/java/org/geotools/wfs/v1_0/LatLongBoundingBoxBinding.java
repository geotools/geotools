/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
 *
 */

package org.geotools.wfs.v1_0;

import java.math.BigInteger;
import java.util.Arrays;
import javax.xml.namespace.QName;
import net.opengis.ows10.Ows10Factory;
import net.opengis.ows10.WGS84BoundingBoxType;
import org.geotools.xsd.AbstractComplexEMFBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;

public class LatLongBoundingBoxBinding extends AbstractComplexEMFBinding {

    @Override
    public QName getTarget() {
        return WFSCapabilities.LatLongBoundingBox;
    }

    @Override
    public Class getType() {
        return WGS84BoundingBoxType.class;
    }

    @Override
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {

        Double minx = Double.valueOf((String) node.getAttributeValue("minx"));
        Double miny = Double.valueOf((String) node.getAttributeValue("miny"));
        Double maxx = Double.valueOf((String) node.getAttributeValue("maxx"));
        Double maxy = Double.valueOf((String) node.getAttributeValue("maxy"));
        WGS84BoundingBoxType bbox = Ows10Factory.eINSTANCE.createWGS84BoundingBoxType();
        bbox.setCrs("EPSG:4326");
        bbox.setDimensions(BigInteger.valueOf(2));
        bbox.setLowerCorner(Arrays.asList(minx, miny));
        bbox.setUpperCorner(Arrays.asList(maxx, maxy));

        return bbox;
    }
}
