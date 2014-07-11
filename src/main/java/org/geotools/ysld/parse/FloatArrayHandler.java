package org.geotools.ysld.parse;

import org.yaml.snakeyaml.events.ScalarEvent;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public abstract class FloatArrayHandler extends YsldParseHandler {

    List<Float> list;

    public FloatArrayHandler(Factory factory) {
        super(factory);
        list = new ArrayList<Float>();
    }

    @Override
    public void scalar(ScalarEvent evt, Deque<YamlParseHandler> handlers) {
        for (String str : evt.getValue().split(" ")) {
            list.add(Float.parseFloat(str));
        }

        float[] array = new float[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i).floatValue();
        }
        array(array);
        handlers.pop();
    }

    protected abstract void array(float[] array);
}
