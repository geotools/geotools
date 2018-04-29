package org.geotools.xml.impl;

import org.geotools.xml.Binding;

/** @source $URL$ */
public class MismatchedBindingFinder implements BindingWalker.Visitor {

    private Object object;

    private boolean mismatched = false;

    public MismatchedBindingFinder(Object object) {
        this.object = object;
    }

    @SuppressWarnings("unchecked")
    public void visit(Binding binding) {
        if (!binding.getType().isAssignableFrom(object.getClass())) {
            mismatched = true;
        }
    }

    public boolean foundMismatchedBinding() {
        return mismatched;
    }
}
