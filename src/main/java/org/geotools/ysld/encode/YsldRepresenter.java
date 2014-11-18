package org.geotools.ysld.encode;

import java.awt.Color;

import org.geotools.ysld.parse.Util;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Represent;
import org.yaml.snakeyaml.representer.Representer;

public class YsldRepresenter extends Representer {

    public YsldRepresenter() {
        super();
        this.multiRepresenters.put(Color.class, new RepresentColor());
    }
    
    
    class RepresentColor implements Represent {
        
        @Override
        public Node representData(Object data) {
            Color c = (Color) data;
            String value = Util.serializeColor(c);
            return representScalar(Tag.STR, value);
        }
        
    }
    
}
