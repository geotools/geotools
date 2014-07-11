package org.geotools.ysld.parse;


import org.geotools.ysld.Tuple;
import org.geotools.styling.ColorMap;
import org.geotools.styling.ColorMapEntry;
import org.yaml.snakeyaml.events.Event;
import org.yaml.snakeyaml.events.MappingEndEvent;
import org.yaml.snakeyaml.events.ScalarEvent;
import org.yaml.snakeyaml.events.SequenceEndEvent;

public abstract class ColorMapHandler extends YsldParseHandler {

    ColorMap colorMap;

    public ColorMapHandler(Factory factory) {
        super(factory);
        colorMap = factory.style.createColorMap();
    }

    @Override
    public void scalar(ScalarEvent evt, YamlParseContext context) {
        String val = evt.getValue();
        if ("type".equals(val)) {
            context.push(new ValueHandler(factory) {
                @Override
                protected void value(String value, Event event) {
                    if ("ramp".equals(value)) {
                        colorMap.setType(ColorMap.TYPE_RAMP);
                    }
                    else if ("intervals".equals(value)) {
                        colorMap.setType(ColorMap.TYPE_INTERVALS);
                    }
                    else if ("values".equals(value)) {
                        colorMap.setType(ColorMap.TYPE_VALUES);
                    }
                    else {
                        LOG.warning("Unknown color map type: " + value);
                    }
                }
            });
        }
        else if ("entries".equals(val)) {
            context.push(new EntriesHandler());
        }
    }

    @Override
    public void endMapping(MappingEndEvent evt, YamlParseContext context) {
        super.endMapping(evt, context);
        colorMap(colorMap);
        context.pop();
    }

    protected abstract void colorMap(ColorMap colorMap);

    class EntriesHandler extends YsldParseHandler {

        protected EntriesHandler() {
            super(ColorMapHandler.this.factory);
        }

        @Override
        public void scalar(ScalarEvent evt, YamlParseContext context) {
            String val = evt.getValue();
            Tuple q = null;
            try {
                q = Tuple.of(4).parse(val);
            }
            catch(IllegalArgumentException e) {
                throw new ParseException(String.format(
                    "Bad entry: '%s', must be of form (<color>,[<opacity>],[<value>],[<label>])", val), evt);
            }

            ColorMapEntry e = factory.style.createColorMapEntry();
            if (q.at(0) != null) {
                e.setColor(ExpressionHandler.parse(q.at(0), evt, factory));
            }
            if (q.at(1) != null) {
                e.setOpacity(ExpressionHandler.parse(q.at(1), evt, factory));
            }
            if (q.at(2)!= null) {
                e.setQuantity(ExpressionHandler.parse(q.at(2), evt, factory));
            }
            if (q.at(3) != null) {
                e.setLabel(q.at(3));
            }

            colorMap.addColorMapEntry(e);
        }

        @Override
        public void endSequence(SequenceEndEvent evt, YamlParseContext context) {
            context.pop();
        }
    }
}
