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
package org.geotools.ysld.validate;

import org.yaml.snakeyaml.events.AliasEvent;
import org.yaml.snakeyaml.events.ScalarEvent;
import org.yaml.snakeyaml.events.SequenceEndEvent;
import org.yaml.snakeyaml.events.SequenceStartEvent;

/**
 * Validator for Sequences
 *
 * <p>This Validator is stateful, reset it before re-using it.
 *
 * @author Kevin Smith, Boundless
 */
public class SequenceValidator extends StatefulValidator {

    enum State {
        NEW,
        STARTED,
        DONE
    }

    State state = State.NEW;

    YsldValidateHandler subValidator;

    public SequenceValidator(YsldValidateHandler subValidator) {
        super();
        this.subValidator = subValidator;
    }

    @Override
    public void sequence(SequenceStartEvent evt, YsldValidateContext context) {
        if (state == State.NEW) {
            state = State.STARTED;
        } else if (state == State.STARTED) {
            YsldValidateHandler sub = getSubValidator();
            if (sub instanceof TupleValidator) {
                ((TupleValidator) sub).reset();
            } else if (sub instanceof SequenceValidator) {
                ((SequenceValidator) sub).reset();
            }
            context.push(sub);
            sub.sequence(evt, context);
        } else {
            context.error("Unexpected Start of Sequence", evt.getStartMark());
        }
    }

    @Override
    public void endSequence(SequenceEndEvent evt, YsldValidateContext context) {
        if (state != State.STARTED) {
            context.error("Unexpected End of Sequence", evt.getStartMark());
        }
        state = State.DONE;
        context.pop();
    }

    protected YsldValidateHandler getSubValidator() {
        return subValidator;
    }

    @Override
    public void scalar(ScalarEvent evt, YsldValidateContext context) {
        String val = evt.getValue();
        switch (state) {
            case STARTED:
                YsldValidateHandler sub = getSubValidator();
                if (sub instanceof TupleValidator) {
                    ((TupleValidator) sub).reset();
                } else if (sub instanceof SequenceValidator) {
                    ((SequenceValidator) sub).reset();
                }
                context.push(sub);
                sub.scalar(evt, context);
                break;
            default:
                context.error(String.format("Unexpected scalar '%s'", val), evt.getStartMark());
                break;
        }
    }

    public SequenceValidator clone() {
        return new SequenceValidator(this.subValidator);
    }

    @Override
    void reset() {
        if (state == State.NEW || state == State.DONE) {
            state = State.NEW;
        } else {
            throw new IllegalStateException(
                    "SequenceValidator.reset() called in invalid state: " + state.toString());
        }
    }

    @Override
    public void alias(AliasEvent evt, YsldValidateContext context) {
        switch (state) {
            case NEW:
                state = State.DONE;
                context.pop();
                break;
            case STARTED:
                break;
            default:
                context.error(
                        String.format("Unexpected alias '%s'", evt.getAnchor()),
                        evt.getStartMark());
                break;
        }
    }
}
