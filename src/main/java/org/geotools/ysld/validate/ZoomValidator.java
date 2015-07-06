package org.geotools.ysld.validate;

import org.geotools.ysld.parse.Util;
import org.geotools.ysld.parse.ZoomContext;
import org.yaml.snakeyaml.events.ScalarEvent;
import org.yaml.snakeyaml.events.SequenceEndEvent;
import org.yaml.snakeyaml.events.SequenceStartEvent;

/**
 * Validator for Zoom
 * <p>
 * This Validator is stateful, do not re-use it.
 * 
 * @author Kevin Smith, Boundless
 *
 */
public class ZoomValidator extends YsldValidateHandler {
    
    enum State {
        NEW,
        STARTED,
        MIN,
        MAX,
        DONE
    }
    
    State state = State.NEW;
    
    Integer min;
    
    @Override
    public void sequence(SequenceStartEvent evt, YsldValidateContext context) {
        if(state == State.NEW) {
            state = State.STARTED;
        } else {
            context.error("Unexpected Start of Sequence", evt.getStartMark());
        }
    }
    
    @Override
    public void endSequence(SequenceEndEvent evt, YsldValidateContext context) {
        if(state != State.MAX) {
            context.error("Unexpected End of Sequence", evt.getStartMark());
        }
        state = State.DONE;
        context.pop();
    }
    
    @Override
    public void scalar(ScalarEvent evt, YsldValidateContext context){
        String val = evt.getValue();;
        switch(state) {
        case STARTED:
            state = State.MIN;
            if(val==null || val.isEmpty() || val.equalsIgnoreCase("min")){
                min=null;
            } else {
                try {
                    min = Integer.parseInt(evt.getValue());
                    if(!context.getZCtxt().isInRange(min)){
                        context.error(String.format("Zoom level %d is out of range", min), evt.getStartMark());
                    }
                } catch (NumberFormatException ex) {
                    context.error(ex.getMessage(), evt.getStartMark());
                }
            }
            break;
        case MIN:
            state = State.MAX;
            if(val==null || val.isEmpty() || val.equalsIgnoreCase("max")){
                return;
            } else {
                try {
                    int max = Integer.parseInt(evt.getValue());
                    if(!context.getZCtxt().isInRange(max)){
                        context.error(String.format("Zoom level %d is out of range", min), evt.getStartMark());
                    }
                    if(min!=null && min.compareTo(max)>0) {
                        context.error(String.format("Maximum zoom level %d is less than minimum %d", max, min), evt.getStartMark());
                    }
                } catch (NumberFormatException ex) {
                    context.error(ex.getMessage(), evt.getStartMark());
                }
            }
            break;
        default:
            context.error(String.format("Unexpected scalar '%s'", val), evt.getStartMark());
            break;
        }
    }

}
