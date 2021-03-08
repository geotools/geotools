/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.xsd.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.geotools.xsd.InstanceComponent;
import org.geotools.xsd.Node;
import org.geotools.xsd.Text;

public class NodeImpl implements Node {
    private InstanceComponent component;
    private Object value;
    List<Node> children;
    List<Node> attributes;
    Node parent;

    public NodeImpl(InstanceComponent component) {
        this.component = component;
        children = new ArrayList<>();
        attributes = new ArrayList<>();
    }

    public NodeImpl(InstanceComponent component, Object value) {
        this(component);
        this.value = value;
    }

    @Override
    public InstanceComponent getComponent() {
        return component;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public boolean hasChild(String name) {
        if (name == null) {
            return false;
        }

        for (Node child : children) {
            if (name.equals(child.getComponent().getName())) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean hasChild(Class<?> clazz) {
        if (clazz == null) {
            return false;
        }

        for (Node child : children) {
            if (child.getValue() == null) {
                continue;
            }

            if (clazz.isAssignableFrom(child.getValue().getClass())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Contents of this node.
     *
     * <p>XXX: either return unmodifeable Collection, or return the collection directly. Client code
     * should make the copy iff they need it. Going to try changing it and see what breaks.
     *
     * @see Node#getChildren()
     */
    @Override
    public List<Node> getChildren() {
        return Collections.unmodifiableList(children);
    }

    public int getNChildren() {
        return children.size();
    }

    @Override
    public List<Node> getChildren(String name) {
        List<Node> matches = new ArrayList<>();

        if (name == null) {
            return matches;
        }

        for (Node child : children) {
            if (name.equals(child.getComponent().getName())) {
                matches.add(child);
            }
        }

        return matches;
    }

    @Override
    public List<Node> getChildren(Class<?> clazz) {
        List<Node> matches = new ArrayList<>();

        if (clazz == null) {
            return matches;
        }

        for (Node child : children) {
            if (child.getValue() == null) {
                continue;
            }

            if (clazz.isAssignableFrom(child.getValue().getClass())) {
                matches.add(child);
            }
        }

        return matches;
    }

    @Override
    public Node getChild(String name) {
        if (name == null) {
            return null;
        }

        for (Node child : children) {
            if (name.equals(child.getComponent().getName())) {
                return child;
            }
        }

        return null;
    }

    @Override
    public Node getChild(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }

        for (Node child : children) {
            if (child.getValue() == null) {
                continue;
            }

            if (clazz.isAssignableFrom(child.getValue().getClass())) {
                return child;
            }
        }

        return null;
    }

    @Override
    public boolean hasAttribute(Class<?> clazz) {
        if (clazz == null) {
            return false;
        }

        for (Node att : attributes) {
            if (att.getValue() == null) {
                continue;
            }

            if (clazz.isAssignableFrom(att.getValue().getClass())) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean hasAttribute(String name) {
        if (name == null) {
            return false;
        }

        for (Node att : attributes) {
            if (name.equals(att.getComponent().getName())) {
                return true;
            }
        }

        return false;
    }

    @Override
    public List<Node> getAttributes() {
        return new ArrayList<>(attributes);
    }

    @Override
    public List<Node> getAttributes(Class<?> clazz) {
        List<Node> matches = new ArrayList<>();

        if (clazz == null) {
            return matches;
        }

        for (Node att : attributes) {
            if (att.getValue() == null) {
                continue;
            }

            if (clazz.isAssignableFrom(att.getValue().getClass())) {
                matches.add(att);
            }
        }

        return matches;
    }

    public int getNAttributes() {
        return attributes.size();
    }

    @Override
    public Node getAttribute(String name) {
        if (name == null) {
            return null;
        }

        for (Node att : attributes) {
            if (name.equals(att.getComponent().getName())) {
                return att;
            }
        }

        return null;
    }

    @Override
    public Node getAttribute(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }

        for (Node att : attributes) {
            if (att.getValue() == null) {
                continue;
            }

            if (clazz.isAssignableFrom(att.getValue().getClass())) {
                return att;
            }
        }

        return null;
    }

    @Override
    public Object getAttributeValue(String name) {
        Node node = getAttribute(name);

        if (node != null) {
            return node.getValue();
        }

        return null;
    }

    @Override
    public Object getAttributeValue(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }

        for (Node att : attributes) {
            if (att.getValue() == null) {
                continue;
            }

            if (clazz.isAssignableFrom(att.getValue().getClass())) {
                return att.getValue();
            }
        }

        return null;
    }

    @Override
    public List<Object> getAttributeValues(Class<?> clazz) {
        List<Object> matches = new ArrayList<>();

        if (clazz == null) {
            return matches;
        }

        for (Node att : attributes) {
            if (att.getValue() == null) {
                continue;
            }

            if (clazz.isAssignableFrom(att.getValue().getClass())) {
                matches.add(att.getValue());
            }
        }

        return matches;
    }

    @Override
    public String toString() {
        return getComponent().getName() + "=" + getValue();
    }

    @Override
    public Object getChildValue(int index) {
        return children.get(index).getValue();
    }

    @Override
    public Object getChildValue(String name) {
        Node node = getChild(name);

        if (node != null) {
            return node.getValue();
        }

        return null;
    }

    @Override
    public <T> T getChildValue(Class<T> clazz) {
        Node node = getChild(clazz);

        if (node != null) {
            return clazz.cast(node.getValue());
        }

        return null;
    }

    @Override
    public List<Object> getChildValues(String name) {
        List<Object> matches = new ArrayList<>();

        if (name == null) {
            return matches;
        }

        for (Node child : children) {
            if (name.equals(child.getComponent().getName())) {
                matches.add(child.getValue());
            }
        }

        return matches;
    }

    @Override
    public <T> List<T> getChildValues(Class<T> clazz) {
        List<T> matches = new ArrayList<>();

        if (clazz == null) {
            return matches;
        }

        for (Node child : children) {
            Object parsed = child.getValue();

            if (parsed == null) {
                continue;
            }

            if (clazz.isAssignableFrom(parsed.getClass())) {
                matches.add(clazz.cast(parsed));
            }
        }

        return matches;
    }

    @Override
    public Object getAttributeValue(String name, Object defaultValue) {
        Object o = getAttributeValue(name);

        if (o == null) {
            o = defaultValue;
        }

        return o;
    }

    @Override
    public Object getAttributeValue(Class clazz, Object defaultValue) {
        Object o = getAttributeValue(clazz);

        if (o == null) {
            o = defaultValue;
        }

        return o;
    }

    @Override
    public Object getChildValue(String name, Object defaultValue) {
        Object o = getChildValue(name);

        if (o == null) {
            o = defaultValue;
        }

        return o;
    }

    @Override
    public <T> T getChildValue(Class<T> clazz, T defaultValue) {
        Object o = getChildValue(clazz);

        if (o == null) {
            o = defaultValue;
        }

        return clazz.cast(o);
    }

    // additional methods, not part of public api
    public void addChild(Node child) {
        children.add(child);
        child.setParent(this);
    }

    public Node removeChild(String name) {
        Node child = getChild(name);

        if (child != null) {
            if (children.remove(child)) {
                child.setParent(null);
            }
        }

        return child;
    }

    public void removeChild(Node child) {
        if (children.remove(child)) {
            child.setParent(null);
        }
    }

    @Override
    public Node getParent() {
        return parent;
    }

    @Override
    public void setParent(Node parent) {
        this.parent = parent;
    }

    public void addAttribute(Node attribute) {
        attributes.add(attribute);
    }

    public Node removeAttribute(String name) {
        Node attribute = getAttribute(name);

        if (attribute != null) {
            attributes.remove(attribute);
        }

        return attribute;
    }

    public void collapseWhitespace() {
        // leading whitespace
        for (Iterator<Node> it = children.iterator(); it.hasNext(); ) {
            Text t = text(it.next());
            if (t == null) break;

            if (t.isWhitespace()) {
                it.remove();
            } else {
                t.trimLeading();
                break;
            }
        }

        // trailing whitespace
        for (int i = children.size() - 1; i > -1; i--) {
            Text t = text(children.get(i));
            if (t == null) break;

            if (t.isWhitespace()) {
                children.remove(i);
            } else {
                t.trimTrailing();
                break;
            }
        }

        // inner whitespace
        boolean remove = false;
        for (Iterator<Node> it = children.iterator(); it.hasNext(); ) {
            Text t = text(it.next());
            if (t == null) continue;

            t.trimInner();
            if (t.isWhitespace()) {
                if (remove) {
                    it.remove();
                } else {
                    remove = true;
                }
            } else {
                remove = false;
            }
        }
    }

    Text text(Node n) {
        if (n.getValue() instanceof Text) {
            return (Text) n.getValue();
        }
        return null;
    }
}
