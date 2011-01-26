/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.vpf.util;

import java.util.HashMap;

import org.geotools.data.vpf.io.TableRow;
import org.opengis.feature.simple.SimpleFeatureType;

/*
 * PrimitiveDataFactory.java
 *
 * Created on July 8, 2004, 12:10 PM
 *
 * @author  <a href="mailto:knuterik@onemap.org">Knut-Erik Johnsen</a>, Project OneMap
 * @source $URL$
 */
public class PrimitiveDataFactory {
    protected EdgeData readEdge(TableRow edge) {
        EdgeData ed = null;

        try {
            ed = new EdgeData();
            ed.put("id", new Integer(edge.get("id").intValue()));
            ed.put("start_node", new Integer(edge.get("start_node").intValue()));
            ed.put("end_node", new Integer(edge.get("end_node").intValue()));
            ed.put("right_face", edge.get("right_face"));
            ed.put("left_face", edge.get("left_face"));
            ed.put("right_edge", edge.get("right_edge"));
            ed.put("left_edge", edge.get("left_edge"));
            ed.put("coordinates", edge.get("coordinates"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ed;
    }

    protected HashMap readFeature(TableRow line, SimpleFeatureType type) {
        HashMap tmp = new HashMap();

        String name = null;

        for (int i = 0; i < type.getAttributeCount(); i++) {
            name = type.getDescriptor(i).getLocalName();
            tmp.put(name, line.get(name));
        }

        return tmp;
    }

    protected HashMap readFace(TableRow face) {
        HashMap fd = new HashMap();
        fd.put("id", face.get("id").toString());
        fd.put("ext_id", new Integer(face.get(1).intValue()));
        fd.put("ring_ptr", new Integer(face.get("ring_ptr").intValue()));

        return fd;
    }

    protected PointData readPoint(TableRow point) {
        PointData pd = new PointData();
        pd.put("id", point.get("id").toString());
        pd.put("coordinate", point.get("coordinate").toString());

        return pd;
    }

    protected HashMap readRing(TableRow ring) {
        HashMap rd = new HashMap();
        rd.put("id", ring.get("id").toString());
        rd.put("face_id", new Integer(ring.get("face_id").intValue()));
        rd.put("start_edge", new Integer(ring.get("start_edge").intValue()));

        return rd;
    }
}
