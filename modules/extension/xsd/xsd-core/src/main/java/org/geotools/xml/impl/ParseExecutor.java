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
package org.geotools.xml.impl;

import org.eclipse.xsd.XSDComplexTypeDefinition;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDEnumerationFacet;
import org.eclipse.xsd.XSDFacet;
import org.eclipse.xsd.XSDFactory;
import org.eclipse.xsd.XSDLengthFacet;
import org.eclipse.xsd.XSDMaxLengthFacet;
import org.eclipse.xsd.XSDMinLengthFacet;
import org.eclipse.xsd.XSDNamedComponent;
import org.eclipse.xsd.XSDSimpleTypeDefinition;
import org.eclipse.xsd.XSDTypeDefinition;
import org.eclipse.xsd.XSDVariety;
import org.eclipse.xsd.XSDWhiteSpace;
import org.eclipse.xsd.XSDWhiteSpaceFacet;
import org.picocontainer.MutablePicoContainer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.namespace.QName;

import org.geotools.xml.AttributeInstance;
import org.geotools.xml.Binding;
import org.geotools.xml.ComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.InstanceComponent;
import org.geotools.xml.Node;
import org.geotools.xml.Schemas;
import org.geotools.xml.SimpleBinding;
import org.geotools.xml.impl.BindingWalker.Visitor;
import org.geotools.xs.facets.Whitespace;


public class ParseExecutor implements Visitor {
    private InstanceComponent instance;
    private Node node;
    private MutablePicoContainer context;
    private ParserHandler parser;

    /**
     * initial binding value
     */
    private Object value;

    /**
     * final parsed result
     */
    private Object result;

    public ParseExecutor(InstanceComponent instance, Node node, MutablePicoContainer context,
        ParserHandler parser) {
        this.instance = instance;
        this.node = node;
        this.context = context;
        this.parser = parser;
    }

    public void visit(Binding binding) {
        //TODO: the check for InstanceBinding is a temporary measure to allow 
        // for bindings that are not registered by class, but by instance. 
        // in the long term we intend to ditch pico container b/c our inection 
        // needs are quite trivial and can be handled by some simple reflection
        if ( !( binding instanceof InstanceBinding ) ) {
            //reload out of context, we do this so that the binding can pick up any new dependencies
            // providedb by this particular context
            Class bindingClass = binding.getClass();
            QName bindingTarget = binding.getTarget();
            
            binding = (Binding) context.getComponentInstanceOfType(binding.getClass());
            if (binding == null) {
                
                binding = parser.getBindingLoader().loadBinding(bindingTarget, context);
                if ( binding == null ) {
                    binding = parser.getBindingLoader().loadBinding(bindingTarget,bindingClass,context);
                }
                if ( binding.getClass() != bindingClass ) {
                    throw new IllegalStateException( "Reloaded binding resulted in different type");
                }
            }    
        }
        

        //execute the binding
        try {
            if (result == null) {
                //no result has been produced yet, should we pass the facet 
                // parsed text in? only for simple types or complex types with
                // mixed content
                XSDTypeDefinition type = null;

                if (Schemas.nameMatches(instance.getDeclaration(), binding.getTarget())) {
                    //instance binding
                    type = instance.getTypeDefinition();
                } else {
                    //type binding
                    type = Schemas.getBaseTypeDefinition(instance.getTypeDefinition(),
                            binding.getTarget());
                }

                if (value == null) {
                    //have not preprocessed raw string yet
                    //value = parseFacets( instance );
                    value = preParse(instance);

                    //if the type is simple or complex and mixed, use the 
                    // text as is, other wise trim it, turning to null if the 
                    // result is empty
                    if ((type != null)
                            && (type instanceof XSDSimpleTypeDefinition
                            || ((XSDComplexTypeDefinition) type).isMixed())) {
                        result = value;
                    } else {
                        if ((value != null) && value instanceof String) {
                            value = ((String) value).trim();

                            if ("".equals(value)) {
                                result = null;
                            } else {
                                result = value;
                            }
                        }
                    }
                }
            }

            if (binding instanceof SimpleBinding) {
                result = ((SimpleBinding) binding).parse(instance, result);
            } else {
                result = ((ComplexBinding) binding).parse((ElementInstance) instance, node, result);
            }

            //only pass the value along if it was non-null
            if (result != null) {
                value = result;
            }
        } catch (Throwable t) {
            String msg = "Parsing failed for " + instance.getName() + ": " + t.toString();
            throw new RuntimeException(msg, t);
        }
    }

    public Object getValue() {
        return value;
    }

    /**
     * Pre-parses the instance compontent checking the following:
     * <p>
     *
     * </p>
     * @param instance
     */
    protected Object preParse(InstanceComponent instance) {
        // we only preparse text, so simple types
        XSDSimpleTypeDefinition type = null;

        if (instance.getTypeDefinition() instanceof XSDSimpleTypeDefinition) {
            type = (XSDSimpleTypeDefinition) instance.getTypeDefinition();
        } else {
            XSDComplexTypeDefinition complexType = (XSDComplexTypeDefinition) instance
                .getTypeDefinition();

            if (complexType.getContentType() instanceof XSDSimpleTypeDefinition) {
                type = (XSDSimpleTypeDefinition) complexType.getContentType();
            }
        }

        String text = instance.getText();

        if (type != null) {
            //alright, lets preparse some text
            //first base on variety
            if (type.getVariety() == XSDVariety.LIST_LITERAL) {
                //list, whiteSpace is fixed to "COLLAPSE 
                text = Whitespace.COLLAPSE.preparse(text);

                //lists are seperated by spaces
                String[] list = text.split(" +");

                //apply the facets
                // 1. length
                // 2. maxLength
                // 3. minLength
                // 4. enumeration
                if (type.getLengthFacet() != null) {
                    XSDLengthFacet length = type.getLengthFacet();

                    if (list.length != length.getValue()) {
                        //validation exception
                    }
                }

                if (type.getMaxLengthFacet() != null) {
                    XSDMaxLengthFacet length = type.getMaxLengthFacet();

                    if (list.length > length.getValue()) {
                        //validation exception
                    }
                }

                if (type.getMinLengthFacet() != null) {
                    XSDMinLengthFacet length = type.getMinLengthFacet();

                    if (list.length < length.getValue()) {
                        //validation exception
                    }
                }

                if (!type.getEnumerationFacets().isEmpty()) {
                    //gather up all teh possible values
                    Set values = new HashSet();

                    for (Iterator e = type.getEnumerationFacets().iterator(); e.hasNext();) {
                        XSDEnumerationFacet enumeration = (XSDEnumerationFacet) e.next();

                        for (Iterator v = enumeration.getValue().iterator(); v.hasNext();) {
                            values.add(v.next());
                        }
                    }

                    for (int i = 0; i < list.length; i++) {
                        if (!values.contains(list[i])) {
                            //validation exception
                        }
                    }
                }

                //now we must parse the items up
                final XSDSimpleTypeDefinition itemType = type.getItemTypeDefinition();
                List parsed = new ArrayList();

                //create a pseudo declaration
                final XSDElementDeclaration element = XSDFactory.eINSTANCE
                    .createXSDElementDeclaration();
                element.setTypeDefinition(itemType);

                if (instance.getName() != null) {
                    element.setName(instance.getName());
                }

                if (instance.getNamespace() != null) {
                    element.setTargetNamespace(instance.getNamespace());
                }

                //create a new instance of the specified type
                InstanceComponentImpl theInstance = new InstanceComponentImpl() {
                        public XSDTypeDefinition getTypeDefinition() {
                            return itemType;
                        }

                        public XSDNamedComponent getDeclaration() {
                            return element;
                        }
                        ;
                    };
                    
                for (int i = 0; i < list.length; i++) {
                    theInstance.setText(list[i]);

                    //perform the parse
                    ParseExecutor executor = new ParseExecutor(theInstance, null, context, parser);
                    parser.getBindingWalker().walk(element, executor, context);

                    parsed.add(executor.getValue());
                }

                return parsed;
            } else if (type.getVariety() == XSDVariety.UNION_LITERAL) {
                //union, "valueSpace" and "lexicalSpace" facets are the union of the contained
                // datatypes
                return text;
            } else {
                //atomic

                //walk through the facets and preparse as necessary 
                for (Iterator f = type.getFacets().iterator(); f.hasNext();) {
                    XSDFacet facet = (XSDFacet) f.next();

                    //white space
                    if (facet instanceof XSDWhiteSpaceFacet) {
                        XSDWhiteSpaceFacet whitespace = (XSDWhiteSpaceFacet) facet;

                        if (whitespace.getValue() == XSDWhiteSpace.REPLACE_LITERAL) {
                            text = Whitespace.REPLACE.preparse(text);
                        }

                        if (whitespace.getValue() == XSDWhiteSpace.COLLAPSE_LITERAL) {
                            text = Whitespace.COLLAPSE.preparse(text);
                        }

                        if (whitespace.getValue() == XSDWhiteSpace.PRESERVE_LITERAL) {
                            //do nothing
                        }
                    }
                }

                return text;
            }
        } else {
            //type is not simple, or complex with simple content, do a check 
            // for mixed
            if (instance.getTypeDefinition() instanceof XSDComplexTypeDefinition
                    && ((XSDComplexTypeDefinition) instance.getTypeDefinition()).isMixed()) {
                //collape the text
                text = Whitespace.COLLAPSE.preparse(text);
            }
        }

        return text;
    }

    protected Object parseFacets(InstanceComponent instance) {
        XSDTypeDefinition type = instance.getTypeDefinition();

        String value = instance.getText();

        while (type != null) {
            if (type instanceof XSDSimpleTypeDefinition) {
                XSDSimpleTypeDefinition simpleType = (XSDSimpleTypeDefinition) type;
                List facets = simpleType.getFacets();

                for (Iterator itr = facets.iterator(); itr.hasNext();) {
                    XSDFacet facet = (XSDFacet) itr.next();

                    if ("whiteSpace".equals(facet.getFacetName())) {
                        Whitespace whitespace = Whitespace.valueOf(facet.getLexicalValue());

                        if (whitespace != null) {
                            value = whitespace.preparse(value);
                        }

                        //else TODO: check for validation, throw exception? 
                    }

                    //TODO: other facets
                }
            }

            if (type.equals(type.getBaseType())) {
                break;
            }

            type = type.getBaseType();
        }

        return value;
    }
}
