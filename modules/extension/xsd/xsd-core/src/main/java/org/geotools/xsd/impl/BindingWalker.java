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
import java.util.List;
import java.util.Stack;
import javax.xml.namespace.QName;
import org.eclipse.xsd.XSDComplexTypeDefinition;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDFeature;
import org.eclipse.xsd.XSDNamedComponent;
import org.eclipse.xsd.XSDParticle;
import org.eclipse.xsd.XSDSimpleTypeDefinition;
import org.eclipse.xsd.XSDTypeDefinition;
import org.geotools.util.SoftValueHashMap;
import org.geotools.xs.XS;
import org.geotools.xsd.Binding;
import org.geotools.xsd.Schemas;
import org.picocontainer.MutablePicoContainer;

public class BindingWalker implements TypeWalker.Visitor {
    BindingLoader loader;

    SoftValueHashMap<XSDFeature, BindingExecutionChain> chains;
    TypeWalker typeWalker;
    MutablePicoContainer context;
    List<Binding> bindings;
    XSDFeature component;
    XSDTypeDefinition container;

    public BindingWalker(BindingLoader factory) {
        this.loader = factory;

        chains = new SoftValueHashMap<>(100);
        typeWalker = new TypeWalker();
    }

    public Binding getAnyTypeBinding() {
        return loader.loadBinding(XS.ANYTYPE, context);
    }

    @Override
    public boolean visit(XSDTypeDefinition type) {
        // look up the associated binding object for this type
        QName bindingName = null;

        if (type.getName() != null) {
            bindingName = new QName(type.getTargetNamespace(), type.getName());
        } else {
            // anonymous type, does it belong to a global element
            for (XSDElementDeclaration element : type.getSchema().getElementDeclarations()) {
                if (type.equals(element.getAnonymousTypeDefinition())) {
                    // TODO: this naming convention for anonymous types could conflict with
                    // other types in the schema
                    bindingName = new QName(type.getTargetNamespace(), "_" + element.getName());

                    break;
                }
            }

            if (bindingName == null) {
                // do we have a containing type?
                if (container != null) {
                    XSDNamedComponent base = container;

                    // the container itself could be an anonymous type, check for
                    // a named containing element
                    if (container.getName() == null) {
                        if (container.getContainer() instanceof XSDElementDeclaration) {
                            XSDElementDeclaration e = (XSDElementDeclaration) container.getContainer();

                            // only do this if the containing element is global
                            if (e.isGlobal()) {
                                base = e;
                            }
                        }
                    }

                    // get the anonymous element, and look it up in the container type
                    if (type.getContainer() instanceof XSDElementDeclaration) {
                        XSDElementDeclaration anonymous = (XSDElementDeclaration) type.getContainer();
                        XSDParticle particle = Schemas.getChildElementParticle(container, anonymous.getName(), true);

                        if (particle != null) {
                            bindingName =
                                    new QName(base.getTargetNamespace(), base.getName() + "_" + anonymous.getName());
                        }
                    }
                }
            }

            if (bindingName == null || loader.getBinding(bindingName) == null) {
                // special case check, look for an anonymous complex type
                // with simple content
                if (type instanceof XSDComplexTypeDefinition && type.getBaseType() instanceof XSDSimpleTypeDefinition) {
                    // we assign the default complex binding instread of
                    // delegating to parent, because if we dont, any attributes
                    // defined by the type will not be parsed because simple
                    // types cannot have attributes.
                    // TODO: put this somewhere else, perhaps in the factories
                    // that create the bindings
                    bindingName = XS.ANYTYPE;
                }
            }
        }

        // load the binding into the current context
        Binding binding = loader.loadBinding(bindingName, context);

        if (binding != null) {
            // add the binding
            bindings.add(binding);

            // check execution mode, if override break out
            if (binding.getExecutionMode() == Binding.OVERRIDE) {
                return false;
            }
        }

        return true;
    }

    public void walk(XSDFeature component, Visitor visitor, XSDTypeDefinition container, MutablePicoContainer context) {
        BindingExecutionChain chain = chains.get(component);

        if (chain == null) {
            this.container = container;
            this.component = component;
            this.context = context;
            this.bindings = new ArrayList<>();

            // first walk the type hierarchy to get the binding objects
            typeWalker.walk(component.getType(), this);

            // also look up a binding to the instance itself, if found it will go
            // at the bottom of the binding hierarchy
            if (component.getName() != null) {
                QName qName = new QName(component.getTargetNamespace(), component.getName());
                Binding binding = loader.loadBinding(qName, context);

                if (binding != null) {
                    // check for override
                    if (binding.getExecutionMode() == Binding.OVERRIDE) {
                        // override, clear the binding list
                        bindings.clear();
                        bindings.add(binding);
                    } else {
                        // not override, add as first
                        bindings.add(0, binding);
                    }
                }
            }

            chain = new BindingExecutionChain(bindings);
            chains.put(component, chain);
        }

        chain.execute(visitor);
    }

    public void walk(XSDFeature component, Visitor visitor, MutablePicoContainer context) {
        walk(component, visitor, null, context);
    }

    public static interface Visitor {
        void visit(Binding binding);
    }

    public static class BindingExecutionChain {
        List bindings;

        public BindingExecutionChain(List bindings) {
            this.bindings = bindings;
        }

        public void execute(Visitor visitor) {
            // simulated call stack
            Stack<Binding> stack = new Stack<>();

            // visit from bottom to top
            for (Object o : bindings) {
                Binding binding = (Binding) o;

                if (binding.getExecutionMode() == Binding.AFTER) {
                    // put on stack to execute after parent
                    stack.push(binding);

                    continue;
                }

                // execute the strategy
                visitor.visit(binding);
            }

            // unwind the call stack
            while (!stack.isEmpty()) {
                Binding binding = stack.pop();

                visitor.visit(binding);
            }
        }
    }
}
