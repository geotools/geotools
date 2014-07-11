package org.geotools.ysld.parse;

import org.opengis.filter.expression.Expression;
import org.yaml.snakeyaml.events.Event;

import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class ColorHandler extends ValueHandler {

    static final Pattern HEX_PATTERN = Pattern.compile("\\s*#?([A-Fa-f0-9]{3}|[A-Fa-f0-9]{6})\\s*");

    static final Pattern RGB_PATTERN = Pattern.compile(
        "\\s*rgb\\s*\\(\\s*(\\d{1,3})\\s*,\\s*(\\d{1,3})\\s*,\\s*(\\d{1,3})\\s*\\)\\s*", Pattern.CASE_INSENSITIVE);

    protected ColorHandler(Factory factory) {
        super(factory);
    }

    @Override
    protected void value(String value, Event event) {
        Color color = null;
        Matcher m = HEX_PATTERN.matcher(value);
        if (m.matches()) {
            color = parseAsHex(m, event);
        }
        if (color == null) {
            m = RGB_PATTERN.matcher(value);
            if (m.matches()) {
                color = parseAsRGB(m, event);
            }
        }


        color(color != null ? factory.filter.literal(color) : ExpressionHandler.parse(value, event, factory));
    }

    Color parseAsHex(Matcher m, Event event) {
        String hex = m.group(1);
        if (hex.length() == 3) {
            hex += hex;
        }

        return new Color(Integer.parseInt(hex.substring(0,2), 16),
            Integer.parseInt(hex.substring(2,4), 16), Integer.parseInt(hex.substring(4,6), 16));
    }

    Color parseAsRGB(Matcher m, Event event) {
        return new Color(Integer.parseInt(m.group(1)),Integer.parseInt(m.group(2)),Integer.parseInt(m.group(3)));
    }

    protected abstract void color(Expression color);

}
