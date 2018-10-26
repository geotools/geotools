/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016 Open Source Geospatial Foundation (OSGeo)
 *    (C) 2014-2016 Boundless Spatial
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
package org.geotools.ysld.encode;

import java.awt.Color;
import javax.measure.Unit;
import javax.measure.quantity.Length;
import org.geotools.ysld.Tuple;
import org.geotools.ysld.UomMapper;
import org.geotools.ysld.parse.Util;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Represent;
import org.yaml.snakeyaml.representer.Representer;

/**
 * Represent YSLD JavaBeans Extends the yaml {@link Representer} for YSLD-specific representations
 * for Color, UOM (unit of measure) and Tuple.
 */
public class YsldRepresenter extends Representer {
    UomMapper uomMapper;

    public YsldRepresenter(UomMapper uomMapper) {
        super();
        this.multiRepresenters.put(Color.class, new RepresentColor());
        this.multiRepresenters.put(Unit.class, new RepresentUom());
        this.multiRepresenters.put(Tuple.class, new RepresentTuple());
        this.uomMapper = uomMapper;
    }

    class RepresentColor implements Represent {

        @Override
        public Node representData(Object data) {
            Color c = (Color) data;
            String value = Util.serializeColor(c);
            return representScalar(Tag.STR, value);
        }
    }

    class RepresentUom implements Represent {

        @Override
        public Node representData(Object data) {
            @SuppressWarnings("unchecked")
            Unit<Length> unit = (Unit<Length>) data;
            String value = uomMapper.getIdentifier(unit);
            return representScalar(Tag.STR, value);
        }
    }

    class RepresentTuple implements Represent {

        @Override
        public Node representData(Object data) {
            Tuple t = (Tuple) data;
            return representSequence(Tag.SEQ, t.toList(), DumperOptions.FlowStyle.FLOW);
        }
    }
}
