package org.geotools.ysld.validate;

import org.yaml.snakeyaml.events.AliasEvent;
import org.yaml.snakeyaml.events.Event;
import org.yaml.snakeyaml.events.MappingEndEvent;
import org.yaml.snakeyaml.events.MappingStartEvent;
import org.yaml.snakeyaml.events.ScalarEvent;
import org.yaml.snakeyaml.events.SequenceEndEvent;
import org.yaml.snakeyaml.events.SequenceStartEvent;

/**
 * Validator which accepts a YAML subtree and then removes itself from the stack.
 *
 */
public class PermissiveValidator extends StatefulValidator {

    enum State {
        NEW,
        STARTED,
        DONE
    }
    int depth = 0;
    State state = State.NEW;
    
    private void start(Event evt, YsldValidateContext context){
        if(state == State.NEW) {
            state = State.STARTED;
        }
        depth++;
    }
    private void end(Event evt, YsldValidateContext context){
        if(state == State.STARTED) {
            depth--;
            if(depth==0) {
                state = State.DONE;
                context.pop();
            }
        } else  {
            context.error("Unexpected End of " + ((evt instanceof MappingEndEvent)?"Mapping":"Sequence"), evt.getStartMark());
        }
    }
    
    @Override
    public void mapping(MappingStartEvent evt, YsldValidateContext context) {
        start(evt, context);
    }
    
    @Override
    public void scalar(ScalarEvent evt, YsldValidateContext context) {
        if(state == State.NEW) {
            context.pop();
            state = State.DONE;
        }
    }
    
    @Override
    public void sequence(SequenceStartEvent evt, YsldValidateContext context) {
        start(evt, context);
    }
    
    @Override
    public void endMapping(MappingEndEvent evt, YsldValidateContext context) {
        end(evt, context);
    }
    
    @Override
    public void endSequence(SequenceEndEvent evt, YsldValidateContext context) {
        end(evt, context);
    }
    
    @Override
    void reset() {
        state = State.NEW;
        depth = 0;
    }
    
    @Override
    public void alias(AliasEvent evt, YsldValidateContext context) {
        switch(state) {
        case NEW:
            state = State.DONE;
            context.pop();
            break;
        case STARTED:
            break;
        default:
            context.error(String.format("Unexpected alias '%s'", evt.getAnchor()), evt.getStartMark());
            break;
        }
    }
}
