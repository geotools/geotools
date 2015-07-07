package org.geotools.ysld.validate;

import java.util.ArrayList;
import java.util.List;

import org.geotools.ysld.parse.Util;
import org.geotools.ysld.parse.ZoomContext;
import org.yaml.snakeyaml.events.ScalarEvent;
import org.yaml.snakeyaml.events.SequenceEndEvent;
import org.yaml.snakeyaml.events.SequenceStartEvent;

/**
 * Validator for Tuples
 * <p>
 * This Validator is stateful, reset it before re-using it.
 * 
 * @author Kevin Smith, Boundless
 *
 */
public class TupleValidator extends YsldValidateHandler {
    
    enum State {
        NEW,
        STARTED,
        DONE
    }
    
    State state = State.NEW;
    
    int valuesValidated = 0;
    
    List<ScalarValidator> subValidators;
    
    public TupleValidator(List<? extends ScalarValidator> subValidators) {
        super();
        this.subValidators = new ArrayList<>(subValidators);
    }

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
        if(state != State.STARTED) {
            context.error("Unexpected End of Sequence", evt.getStartMark());
        } else if(valuesValidated!=getSubValidators().size()) {
            context.error(String.format("Expected tuple of size %d but was %d", getSubValidators().size(), valuesValidated), evt.getStartMark());
        }
        state = State.DONE;
        context.pop();
    }

    protected List<ScalarValidator> getSubValidators() {
        return subValidators;
    }
    
    @Override
    public void scalar(ScalarEvent evt, YsldValidateContext context){
        String val = evt.getValue();
        switch(state) {
        case STARTED:
            try{
                context.push(getSubValidators().get(valuesValidated));
                
                context.peek().scalar(evt, context);
            } catch (IndexOutOfBoundsException ex) {
                // Do nothing, this will be detected when valuesValidated is too large.
            }
            valuesValidated++;
            break;
        default:
            context.error(String.format("Unexpected scalar '%s'", val), evt.getStartMark());
            break;
        }
    }

    public TupleValidator clone() {
        return new TupleValidator(this.subValidators);
    }

    protected void reset() {
        assert state==State.NEW || state==State.DONE;
        state = State.NEW;
        valuesValidated = 0;
    }

}
