package org.geotools.ysld.validate;

import org.geotools.ysld.parse.Util;
import org.yaml.snakeyaml.events.MappingEndEvent;
import org.yaml.snakeyaml.events.ScalarEvent;

public class GridValidator extends YsldValidateHandler {
    
    @Override
    public void scalar(ScalarEvent evt, YsldValidateContext context) {
        String key = evt.getValue();
        if ("name".equals(key)) {
            context.push(new ZoomContextNameValidator());
        }
    }
    
    
    class ZoomContextNameValidator extends ScalarValidator {
        
        @Override
        protected String validate(String value, ScalarEvent evt,
                YsldValidateContext context) {
            if(Util.getNamedZoomContext(value, context.zCtxtFinders)!=null) return null;
            return String.format("Unknown Grid: %s", value);
        }
        
    }
    
    @Override
    public void endMapping(MappingEndEvent evt, YsldValidateContext context) {
        context.pop();
    }
    
    
}
