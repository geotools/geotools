package org.geotools.ysld.encode;

import java.awt.Color;

import javax.measure.quantity.Length;
import javax.measure.unit.Unit;

import org.geotools.ysld.UomMapper;
import org.geotools.ysld.parse.Util;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Represent;
import org.yaml.snakeyaml.representer.Representer;

public class YsldRepresenter extends Representer {
    UomMapper uomMapper;
    
    public YsldRepresenter(UomMapper uomMapper) {
        super();
        this.multiRepresenters.put(Color.class, new RepresentColor());
        this.multiRepresenters.put(Unit.class, new RepresentUom());
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
}
