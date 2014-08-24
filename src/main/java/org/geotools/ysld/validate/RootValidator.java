package org.geotools.ysld.validate;

import org.yaml.snakeyaml.events.ScalarEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RootValidator extends YsldValidateHandler {

    static Pattern COLOR = Pattern.compile("(fill|stroke)-color");

    @Override
    public void scalar(ScalarEvent evt, YsldValidateContext context) {
        String key = evt.getValue();

        Matcher m = COLOR.matcher(key);
        if (m.matches()) {
            context.push(new ColorValidator());
        }
        else if ("filter".equals(key)) {
            context.push(new FilterValidator());
        }
    }
}
