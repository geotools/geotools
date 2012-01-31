package org.geotools.wfs.v1_0;

import java.math.BigInteger;
import java.util.Arrays;

import javax.xml.namespace.QName;

import net.opengis.ows10.Ows10Factory;
import net.opengis.ows10.WGS84BoundingBoxType;

import org.geotools.xml.AbstractComplexEMFBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;

public class LatLongBoundingBoxBinding extends AbstractComplexEMFBinding {

    @Override
    public QName getTarget() {
        return WFSCapabilities.LatLongBoundingBox;
    }

    @SuppressWarnings("rawtypes")
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
