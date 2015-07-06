package org.geotools.ysld.validate;

import org.yaml.snakeyaml.events.ScalarEvent;

public abstract class ScalarValidator extends YsldValidateHandler {

    @Override
    public void scalar(ScalarEvent evt, YsldValidateContext context) {
        String message = validate(evt.getValue(), evt, context);
        if (message != null) {
            context.error(message, evt.getStartMark());
        }
        context.pop();
    }

    protected abstract String validate(String value, ScalarEvent evt, YsldValidateContext context);
}
