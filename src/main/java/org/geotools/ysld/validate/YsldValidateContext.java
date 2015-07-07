package org.geotools.ysld.validate;

import org.geotools.ysld.parse.Factory;
import org.geotools.ysld.parse.Util;
import org.geotools.ysld.parse.ZoomContext;
import org.geotools.ysld.parse.ZoomContextFinder;
import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.error.MarkedYAMLException;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

public class YsldValidateContext {

    Deque<YsldValidateHandler> handlers = new ArrayDeque<>();

    List<MarkedYAMLException> errors = new ArrayList<>();

    Factory factory = new Factory();

    List<ZoomContextFinder> zCtxtFinders = Collections.emptyList();

    ZoomContext zCtxt;

    public List<MarkedYAMLException> errors() {
        return errors;
    }

    public YsldValidateContext error(String problem, Mark mark) {
        return error(new MarkedYAMLException(null, null, problem, mark){});
    }

    public YsldValidateContext error(MarkedYAMLException e) {
        errors.add(e);
        return this;
    }

    public YsldValidateHandler peek() {
        return handlers.peek();
    }

    public void pop() {
        handlers.pop();
    }

    public void push(YsldValidateHandler handler) {
        handlers.push(handler);
    }

    public ZoomContext getZCtxt() {
        if(zCtxt==null) {
            return Util.getNamedZoomContext("DEFAULT", zCtxtFinders);
        } else {
            return zCtxt;
        }
    }
}
