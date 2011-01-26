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

import org.apache.commons.collections.MultiHashMap;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDFactory;
import org.eclipse.xsd.XSDParticle;
import org.picocontainer.MutablePicoContainer;
import org.w3c.dom.Element;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;

import org.geotools.feature.ComplexAttributeImpl;
import org.geotools.xml.Encoder;
import org.geotools.xml.PropertyExtractor;
import org.geotools.xml.Schemas;


/**
 * Uses {@link org.geotools.xml.ComplexBinding#getProperty(Object, QName)} to obtain
 * properties from the objecet being encoded.
 *
 * @author Justin Deoliveira, The Open Planning Project
 *
 *
 * @source $URL$
 */
public class BindingPropertyExtractor implements PropertyExtractor {
    Encoder encoder;
    MutablePicoContainer context;

    public BindingPropertyExtractor(Encoder encoder, MutablePicoContainer context) {
        this.encoder = encoder;
        this.context = context;
    }

    public boolean canHandle(Object object) {
        return true;
    }

    public void setContext(MutablePicoContainer context) {
        this.context = context;
    }

    public List properties(Object object, XSDElementDeclaration element) {
        List properties = new ArrayList();

        //first get all the properties that can be infered from teh schema
        List children = encoder.getSchemaIndex().getChildElementParticles(element);

O: 
        for (Iterator itr = children.iterator(); itr.hasNext();) {
            XSDParticle particle = (XSDParticle) itr.next();
            XSDElementDeclaration child = (XSDElementDeclaration) particle.getContent();

            if (child.isElementDeclarationReference()) {
                child = child.getResolvedElementDeclaration();
            }

            //get the object(s) for this element 
            GetPropertyExecutor executor = new GetPropertyExecutor(object, child);

            BindingVisitorDispatch.walk(object, encoder.getBindingWalker(), element, executor,
                    context);

            if (executor.getChildObject() != null) {
                properties.add(new Object[] { particle, executor.getChildObject() });
            }
        }

        //second, get the properties which cannot be infereed from the schema
        GetPropertiesExecutor executor = new GetPropertiesExecutor(object,element);

        BindingVisitorDispatch.walk(object, encoder.getBindingWalker(), element, executor, context);

        if (!executor.getProperties().isEmpty()) {
            //group into a map of name, list
            MultiHashMap map = new MultiHashMap();

            for (Iterator p = executor.getProperties().iterator(); p.hasNext();) {
                Object[] property = (Object[]) p.next();
                map.put(property[0], property[1]);
            }

            //turn each map entry into a particle
            HashMap particles = new HashMap();

            for (Iterator e = map.entrySet().iterator(); e.hasNext();) {
                Map.Entry entry = (Map.Entry) e.next();
                
                //key could be a name or a particle
                if ( entry.getKey() instanceof XSDParticle ) {
                    XSDParticle particle = (XSDParticle) entry.getKey();
                    particles.put( Schemas.getParticleName( particle), particle );
                    continue;
                }
                
                QName name = (QName) entry.getKey();
                Collection values = (Collection) entry.getValue();

                //check for comment
                if (Encoder.COMMENT.equals(name)) {
                    //create a dom element which text nodes for the comments
                    Element comment = encoder.getDocument()
                                             .createElement(Encoder.COMMENT.getLocalPart());

                    for (Iterator v = values.iterator(); v.hasNext();) {
                        comment.appendChild(encoder.getDocument().createTextNode(v.next().toString()));
                    }

                    XSDParticle particle = XSDFactory.eINSTANCE.createXSDParticle();

                    XSDElementDeclaration elementDecl = XSDFactory.eINSTANCE
                        .createXSDElementDeclaration();
                    elementDecl.setTargetNamespace(Encoder.COMMENT.getNamespaceURI());
                    elementDecl.setName(Encoder.COMMENT.getLocalPart());
                    elementDecl.setElement(comment);

                    particle.setContent(elementDecl);
                    particles.put(name, particle);

                    continue;
                }

                //find hte element 
                XSDElementDeclaration elementDecl = encoder.getSchemaIndex()
                                                           .getElementDeclaration(name);

                if (elementDecl == null) {
                    //TODO: resolving like this will return an element no 
                    // matter what, modifying the underlying schema, this might
                    // be dangerous. What we shold do is force the schema to 
                    // resolve all of it simports when the encoder starts
                    elementDecl = encoder.getSchema()
                                         .resolveElementDeclaration(name.getNamespaceURI(),
                            name.getLocalPart());
                }

                //look for a particle in the containing type which is either 
                // a) a base type of the element
                // b) in the same subsittuion group
                // if found use the particle to dervice multiplicity
                XSDParticle reference = null;
                for ( Iterator p = Schemas.getChildElementParticles(element.getType(), true).iterator(); p.hasNext(); ) {
                    XSDParticle particle = (XSDParticle) p.next();
                    XSDElementDeclaration el = (XSDElementDeclaration) particle.getContent();
                    if ( el.isElementDeclarationReference() ) {
                        el = el.getResolvedElementDeclaration();
                    }
                    
                    if ( Schemas.isBaseType(elementDecl, el) ) {
                        reference = particle;
                        break;
                    }
                }
                
                //wrap the property in a particle
                XSDParticle particle = XSDFactory.eINSTANCE.createXSDParticle();
                XSDElementDeclaration wrapper = XSDFactory.eINSTANCE.createXSDElementDeclaration();
                wrapper.setResolvedElementDeclaration( elementDecl );
                particle.setContent(wrapper);
                //particle.setContent(elementDecl);

                //if there is a reference, derive multiplicity
                if ( reference != null ) {
                    particle.setMaxOccurs( reference.getMaxOccurs() );
                }
                else {
                    //dervice from collection
                    if ( values.size() > 1) {
                        //make a multi property
                        particle.setMaxOccurs(-1);
                    } else {
                        //single property
                        particle.setMaxOccurs(1);
                    }    
                }
                
                particles.put(name, particle);
            }

            //process the particles in order in which we got the properties
            for (Iterator p = executor.getProperties().iterator(); p.hasNext();) {
                Object[] property = (Object[]) p.next();
                Collection values = (Collection) map.get( property[0] );
                
                QName name;
                if ( property[0]  instanceof XSDParticle ) {
                    name = Schemas.getParticleName( (XSDParticle) property[0] );
                }
                else {
                    name = (QName) property[0];
                }

                XSDParticle particle = (XSDParticle) particles.get(name);

                if (particle == null) {
                    continue; //already processed, must be a multi property
                }
                
                if (values.size() > 1) {
                    //add as is, the encoder will unwrap
                    properties.add(new Object[] { particle, values });
                } else {
                    //unwrap it
                    properties.add(new Object[] { particle, values.iterator().next() });
                }

                //done with this particle
                particles.remove(name);
            }
        }
        
        //return properties;        
        if (properties.size()<=1){
            return properties;
        }
        
        /*
         feature properties in the "properties" list may not be in the same order as they appear in the schema,
         because in the above implementation, simple attributes and complex attributes are processed separately.
          
         to maintain the feature properties order, sort the properties to their original order as in "children" list               
        */
        if (object instanceof ComplexAttributeImpl && propertiesSortable(properties, children)) {
            List sortedProperties = new ArrayList();
            
            //sort properties according to their XSDParticle order in "children"
            for (int i = 0; i<children.size(); i++) {
                XSDParticle particle = (XSDParticle) children.get(i);
                XSDElementDeclaration child = (XSDElementDeclaration) particle.getContent();
                if (child.getResolvedElementDeclaration() != null) {
                    child = child.getResolvedElementDeclaration();
                }

                for (Iterator itr = properties.iterator(); itr.hasNext();) {
                    Object[] prop = (Object[]) itr.next();
                    XSDParticle part = (XSDParticle) prop[0];
                    XSDElementDeclaration partContent = (XSDElementDeclaration) part.getContent();
                    if (partContent.getResolvedElementDeclaration() != null) {
                        partContent = partContent.getResolvedElementDeclaration();
                    }
                    if (child.getName().equals(partContent.getName())
                            && ((child.getTargetNamespace() != null && partContent
                                    .getTargetNamespace() != null) ? child.getTargetNamespace()
                                    .equals(partContent.getTargetNamespace()) : true)) {
                        sortedProperties.add(prop);
                        properties.remove(prop);
                        i--;
                        break;
                    }
                }
            }
            //return properties in order they appear in the schema
            return sortedProperties;
        }
        else{
            return properties;
        }
    }

    /**
     * Check whether properties can be sorted to the order as in the "children" list.
     * This is only possible when all properties have references (XSDParticle) in the "children" list. 
     * 
     * @param properties 
     *                feature properties obtained
     * @param children 
     *                list of XSDParticle in the order as they appear in the schema
     * @return
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

        for (Iterator itr = properties.iterator(); itr.hasNext();) {
            Object[] prop = (Object[]) itr.next();
            XSDParticle part = (XSDParticle) prop[0];
            XSDElementDeclaration partContent = (XSDElementDeclaration) part.getContent();
            if (partContent.getResolvedElementDeclaration() != null) {
                partContent = partContent.getResolvedElementDeclaration();
            }
            boolean notFound = true;
            for (int i = 0; i < children.size(); i++) {
                XSDParticle particle = (XSDParticle) children.get(i);
                XSDElementDeclaration child = (XSDElementDeclaration) particle.getContent();
                if (child.getResolvedElementDeclaration() != null) {
                    child = child.getResolvedElementDeclaration();
                }
                if (child.getName().equals(partContent.getName())) {
                    notFound = false;
                    break;
                }
            }
            if (notFound){
                return false;
            }
            
        }

        return true;
    }
}
