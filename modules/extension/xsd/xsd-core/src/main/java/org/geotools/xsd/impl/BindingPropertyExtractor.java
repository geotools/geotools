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

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.xml.namespace.QName;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDFactory;
import org.eclipse.xsd.XSDParticle;
import org.eclipse.xsd.XSDTypeDefinition;
import org.geotools.api.feature.Attribute;
import org.geotools.api.feature.ComplexAttribute;
import org.geotools.api.feature.Property;
import org.geotools.feature.ComplexAttributeImpl;
import org.geotools.xsd.ComplexBinding;
import org.geotools.xsd.Encoder;
import org.geotools.xsd.PropertyExtractor;
import org.geotools.xsd.Schemas;
import org.picocontainer.MutablePicoContainer;
import org.w3c.dom.Element;

/**
 * Uses {@link ComplexBinding#getProperty(Object, QName)} to obtain properties from the object being encoded.
 *
 * @author Justin Deoliveira, The Open Planning Project
 */
public class BindingPropertyExtractor implements PropertyExtractor {
    Encoder encoder;
    MutablePicoContainer context;

    public BindingPropertyExtractor(Encoder encoder, MutablePicoContainer context) {
        this.encoder = encoder;
        this.context = context;
    }

    @Override
    public boolean canHandle(Object object) {
        return true;
    }

    public void setContext(MutablePicoContainer context) {
        this.context = context;
    }

    @Override
    public List properties(Object object, XSDElementDeclaration element) {
        final List<Object[]> properties = new ArrayList<>();

        // first get all the properties that can be inferred from the schema
        final List<XSDParticle> children = encoder.getSchemaIndex().getChildElementParticles(element);

        // check if it is an unbounded sequence
        if (isUnboundedSequence(object, element)) {
            processUnboundedSequence(object, properties, children);
        } else {
            for (XSDParticle particle : children) {
                XSDElementDeclaration child = (XSDElementDeclaration) particle.getContent();

                if (child.isElementDeclarationReference()) {
                    child = child.getResolvedElementDeclaration();
                }

                // get the object(s) for this element
                GetPropertyExecutor executor = new GetPropertyExecutor(object, child);

                BindingVisitorDispatch.walk(object, encoder.getBindingWalker(), element, executor, context);

                if (executor.getChildObject() != null) {
                    properties.add(new Object[] {particle, executor.getChildObject()});
                }
            }

            // second, get the properties which cannot be inferred from the schema
            GetPropertiesExecutor executor = new GetPropertiesExecutor(object, element);

            BindingVisitorDispatch.walk(object, encoder.getBindingWalker(), element, executor, context);

            if (!executor.getProperties().isEmpty()) {
                // group into a map of name, list (name can be a QName or a XSDParticle...)
                MultiValuedMap<Object, Object> map = new ArrayListValuedHashMap<>();

                for (Object[] property : executor.getProperties()) {
                    map.put(property[0], property[1]);
                }

                // turn each map entry into a particle
                HashMap<QName, XSDParticle> particles = new HashMap<>();

                for (Object key : map.keySet()) {
                    // key could be a name or a particle
                    if (key instanceof XSDParticle) {
                        XSDParticle particle = (XSDParticle) key;
                        particles.put(Schemas.getParticleName(particle), particle);
                        continue;
                    }

                    QName name = (QName) key;
                    Collection values = map.get(name);

                    // check for comment
                    if (Encoder.COMMENT.equals(name)) {
                        // create a dom element which text nodes for the comments
                        Element comment = encoder.getDocument().createElement(Encoder.COMMENT.getLocalPart());

                        for (Object value : values) {
                            comment.appendChild(encoder.getDocument().createTextNode(value.toString()));
                        }

                        XSDParticle particle = XSDFactory.eINSTANCE.createXSDParticle();

                        XSDElementDeclaration elementDecl = XSDFactory.eINSTANCE.createXSDElementDeclaration();
                        elementDecl.setTargetNamespace(Encoder.COMMENT.getNamespaceURI());
                        elementDecl.setName(Encoder.COMMENT.getLocalPart());
                        elementDecl.setElement(comment);

                        particle.setContent(elementDecl);
                        particles.put(name, particle);

                        continue;
                    }

                    // find hte element
                    XSDElementDeclaration elementDecl = encoder.getSchemaIndex().getElementDeclaration(name);

                    if (elementDecl == null) {
                        // look for the element declaration as a particle of the containing type
                        XSDParticle particle =
                                Schemas.getChildElementParticle(element.getType(), name.getLocalPart(), true);
                        if (particle != null) {
                            particles.put(name, particle);
                            continue;
                        }
                    }

                    if (elementDecl == null) {
                        // TODO: resolving like this will return an element no
                        // matter what, modifying the underlying schema, this might
                        // be dangerous. What we should do is force the schema to
                        // resolve all of its imports when the encoder starts
                        elementDecl = encoder.getSchema()
                                .resolveElementDeclaration(name.getNamespaceURI(), name.getLocalPart());
                    }

                    // look for a particle in the containing type which is either
                    // a) a base type of the element
                    // b) in the same subsittuion group
                    // if found use the particle to dervice multiplicity
                    XSDParticle reference = null;
                    for (XSDParticle particle : Schemas.getChildElementParticles(element.getType(), true)) {
                        XSDElementDeclaration el = (XSDElementDeclaration) particle.getContent();
                        if (el.isElementDeclarationReference()) {
                            el = el.getResolvedElementDeclaration();
                        }

                        if (Schemas.isBaseType(elementDecl, el)) {
                            reference = particle;
                            break;
                        }
                    }

                    // wrap the property in a particle
                    XSDParticle particle = XSDFactory.eINSTANCE.createXSDParticle();
                    XSDElementDeclaration wrapper = XSDFactory.eINSTANCE.createXSDElementDeclaration();
                    wrapper.setResolvedElementDeclaration(elementDecl);
                    particle.setContent(wrapper);

                    // if there is a reference, derive multiplicity
                    if (reference != null) {
                        particle.setMaxOccurs(reference.getMaxOccurs());
                    } else {
                        // dervice from collection
                        if (values.size() > 1) {
                            // make a multi property
                            particle.setMaxOccurs(-1);
                        } else {
                            // single property
                            particle.setMaxOccurs(1);
                        }
                    }

                    particles.put(name, particle);
                }

                // process the particles in order in which we got the properties
                for (Object[] property : executor.getProperties()) {
                    Collection values = map.get(property[0]);

                    QName name;
                    if (property[0] instanceof XSDParticle) {
                        name = Schemas.getParticleName((XSDParticle) property[0]);
                    } else {
                        name = (QName) property[0];
                    }

                    XSDParticle particle = particles.get(name);

                    if (particle == null) {
                        continue; // already processed, must be a multi property
                    }

                    if (values.size() > 1) {
                        // add as is, the encoder will unwrap
                        properties.add(new Object[] {particle, values});
                    } else {
                        // unwrap it
                        properties.add(new Object[] {particle, values.iterator().next()});
                    }

                    // done with this particle
                    particles.remove(name);
                }
            }
        }

        // return properties;
        if (properties.size() <= 1) {
            return properties;
        }

        /*
         feature properties in the "properties" list may not be in the same order as they appear in the schema,
         because in the above implementation, simple attributes and complex attributes are processed separately.

         to maintain the feature properties order, sort the properties to their original order as in "children" list
        */
        if (object instanceof ComplexAttributeImpl && propertiesSortable(properties, children)) {
            List<Object[]> sortedProperties = new ArrayList<>();

            // sort properties according to their XSDParticle order in "children"
            for (int i = 0; i < children.size(); i++) {
                XSDParticle particle = children.get(i);
                XSDElementDeclaration child = (XSDElementDeclaration) particle.getContent();
                if (child.getResolvedElementDeclaration() != null) {
                    child = child.getResolvedElementDeclaration();
                }

                for (Object[] prop : properties) {
                    XSDParticle part = (XSDParticle) prop[0];
                    XSDElementDeclaration partContent = (XSDElementDeclaration) part.getContent();
                    if (partContent.getResolvedElementDeclaration() != null) {
                        partContent = partContent.getResolvedElementDeclaration();
                    }
                    if (child.getName().equals(partContent.getName())
                            && (child.getTargetNamespace() != null && partContent.getTargetNamespace() != null
                                    ? child.getTargetNamespace().equals(partContent.getTargetNamespace())
                                    : true)) {
                        sortedProperties.add(prop);
                        properties.remove(prop);
                        i--;
                        break;
                    }
                }
            }
            // return properties in order they appear in the schema
            return sortedProperties;
        } else {
            return properties;
        }
    }

    private void processUnboundedSequence(Object object, List<Object[]> properties, List<XSDParticle> children) {
        final ComplexAttribute complexAttr = (ComplexAttribute) object;
        final Deque<Attribute> attrDeque = getChildrenAttributes(complexAttr);
        // separate into repeat tuples
        final int tuplesNumber = children.size();
        final List<List<Attribute>> attributeTuples = new ArrayList<>();
        while (!attrDeque.isEmpty()) {
            final List<Attribute> attrTuple = new ArrayList<>();
            for (int i = 0; i < tuplesNumber; i++) attrTuple.add(attrDeque.poll());
            attributeTuples.add(attrTuple);
        }
        // iterate every tuple and generate the properties
        for (final List<Attribute> tuple : attributeTuples) {
            if (tuple != null && !tuple.isEmpty()) {
                for (XSDParticle particle : children) {
                    XSDElementDeclaration childAux = (XSDElementDeclaration) particle.getContent();
                    if (childAux.isElementDeclarationReference()) childAux = childAux.getResolvedElementDeclaration();
                    final XSDElementDeclaration child = childAux;
                    final Optional<Attribute> attributeOpt = tuple.stream()
                            .filter(x -> attributeMatches(x, child))
                            .findFirst();
                    if (attributeOpt.isPresent()) {
                        final Attribute attribute = attributeOpt.get();
                        properties.add(new Object[] {particle, attribute.getValue()});
                    }
                }
            }
        }
    }

    private boolean attributeMatches(Attribute att, XSDElementDeclaration declaration) {
        return Optional.ofNullable(att)
                        .map(a -> a.getDescriptor())
                        .map(a -> a.getName())
                        .map(a -> a.getLocalPart())
                        .isPresent()
                && Objects.equals(
                        declaration.getName(), att.getDescriptor().getName().getLocalPart())
                && Objects.equals(
                        declaration.getTargetNamespace(),
                        att.getDescriptor().getName().getNamespaceURI());
    }

    private boolean isUnboundedSequence(Object object, XSDElementDeclaration element) {
        // checks for unbounded sequence
        XSDTypeDefinition anonymousTypeDefinition = element.getAnonymousTypeDefinition();
        if (anonymousTypeDefinition == null
                || anonymousTypeDefinition.getComplexType() == null
                || anonymousTypeDefinition.getComplexType().getMaxOccurs() != XSDParticle.UNBOUNDED) {
            return false;
        }
        // checks complex attribute and children
        if (!(object instanceof ComplexAttribute)) return false;
        // check descriptor.name
        final ComplexAttribute complexAttr = (ComplexAttribute) object;
        // check userData map for Multi value unbounded sequence flag
        return Optional.ofNullable(complexAttr.getUserData().get("multi_value_type"))
                .filter(x -> x instanceof String)
                .map(x -> (String) x)
                .filter(x -> x.equalsIgnoreCase("unbounded-multi-value"))
                .isPresent();
    }

    private Deque<Attribute> getChildrenAttributes(ComplexAttribute complesAttribute) {
        final Collection<Property> props = complesAttribute.getProperties();
        final Deque<Attribute> attrDeque = props.stream()
                .filter(x -> x instanceof Attribute)
                .map(x -> (Attribute) x)
                .collect(Collectors.toCollection(ArrayDeque::new));
        return attrDeque;
    }

    /**
     * Check whether properties can be sorted to the order as in the "children" list. This is only possible when all
     * properties have references (XSDParticle) in the "children" list.
     *
     * @param properties feature properties obtained
     * @param children list of XSDParticle in the order as they appear in the schema
     */
    private boolean propertiesSortable(List properties, List children) {
        /*
         *feature properties contains more elements than it's containing feature (_Feature),
         *for example:
         * 	<gsml:specification>
         *		<gsml:GeologicUnit>
         *		...
         *		</gsml:GeologicUnit>
         *	</gsml:specification>
         *in this case, "properties" for GeologicUnit has more elements than "children" for specification
         */

        if (properties.size() > children.size()) {
            return false;
        }

        for (Object property : properties) {
            Object[] prop = (Object[]) property;
            XSDParticle part = (XSDParticle) prop[0];
            XSDElementDeclaration partContent = (XSDElementDeclaration) part.getContent();
            if (partContent.getResolvedElementDeclaration() != null) {
                partContent = partContent.getResolvedElementDeclaration();
            }
            boolean notFound = true;
            for (Object o : children) {
                XSDParticle particle = (XSDParticle) o;
                XSDElementDeclaration child = (XSDElementDeclaration) particle.getContent();
                if (child.getResolvedElementDeclaration() != null) {
                    child = child.getResolvedElementDeclaration();
                }
                if (child.getName().equals(partContent.getName())
                        && (child.getTargetNamespace() == null
                                ? partContent.getTargetNamespace() == null
                                : child.getTargetNamespace().equals(partContent.getTargetNamespace()))) {
                    notFound = false;
                    break;
                }
            }
            if (notFound) {
                return false;
            }
        }

        return true;
    }
}
