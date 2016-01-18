/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geotools.gml2.simple;

import com.vividsolutions.jts.geom.GeometryCollection;
import org.geotools.gml2.GML;
import org.geotools.xml.Encoder;
import org.xml.sax.helpers.AttributesImpl;

/**
 *
 * @author odie
 */
public class GeometryCollectionEncoder extends GeometryEncoder<GeometryCollection>{
 static final QualifiedName GEOMETRY_COLLECTION = new QualifiedName(
        GML.NAMESPACE, "GeometryCollection", "gml");

    QualifiedName element;
    static Encoder encoder;

    public GeometryCollectionEncoder(Encoder encoder, String gmlPrefix) {
        super(encoder);
        GeometryCollectionEncoder.encoder = encoder;
    }

    @Override
    public void encode(GeometryCollection geometry, AttributesImpl atts,
        GMLWriter handler) throws Exception {
        if (geometry.getNumGeometries() < 1) {
            throw new Exception("More than 1 geometry required!");
        } else {
            GenericGeometryEncoder gec = new GenericGeometryEncoder(
                GeometryCollectionEncoder.encoder);
            for (int i = 0; i < geometry.getNumGeometries(); i++) {
                gec.encode(geometry.getGeometryN(i), atts, handler);
            }
        }
    }
}
