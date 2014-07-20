package org.geotools.ysld.parse;

import org.geotools.ysld.YamlMap;
import org.geotools.ysld.YamlObject;

import java.util.ArrayDeque;
import java.util.Deque;

public class YamlParseContext {

    Deque<Entry> stack;
    Entry curr;

    public YamlParseContext() {
        stack = new ArrayDeque<Entry>();
    }

    public YamlParseContext push(String key, YamlParseHandler handler) {
        return push(curr.obj, key, handler);
    }

    public YamlParseContext push(YamlObject scope, String key, YamlParseHandler handler) {
        YamlMap map = scope.map();
        if (map.has(key)) {
            return doPush(scope.map().obj(key), handler);
        }
        return this;
    }

    public YamlParseContext push(YamlParseHandler handler) {
        return doPush(curr.obj, handler);
    }

    public YamlParseContext push(YamlObject obj, YamlParseHandler handler) {
        return doPush(obj, handler);
    }

    YamlParseContext doPush(YamlObject obj, YamlParseHandler handler) {
        stack.push(new Entry(obj, handler));
        return this;
    }

    public YamlParseContext pop() {
        stack.pop();
        return this;
    }

    public boolean next() {
        curr = stack.pop();
        curr.handler.handle(curr.obj, this);
        return !stack.isEmpty();
    }

    static class Entry {
        YamlObject obj;
        YamlParseHandler handler;

        Entry(YamlObject obj, YamlParseHandler handler) {
            this.obj = obj;
            this.handler = handler;
        }
    }
}
