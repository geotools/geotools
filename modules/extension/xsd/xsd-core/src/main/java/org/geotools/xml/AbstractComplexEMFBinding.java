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
package org.geotools.xml;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.xsd.XSDTypeDefinition;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import javax.xml.namespace.QName;
import org.geotools.util.Converters;


/**
 * Base class for complex bindings which map to an EMF model class.
 * <p>
 * Provides implementations for:
 * <ul>
 *         <li>{@link ComplexBinding#getProperty(Object, QName)}.
 * </ul>
 * </p>
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 *
 * @source $URL$
 */
public abstract class AbstractComplexEMFBinding extends AbstractComplexBinding {
    /**
     * Factory used to create model objects
     */
    EFactory factory;
    
    /**
     * type "hint", allows bindings to specify the type that gets parsed manually
     */
    Class type;
    
    /**
     * Default constructor.
     * <p>
     * Creatign the binding with this constructor will force it to perform a
     * noop in the {@link #parse(ElementInstance, Node, Object)} method.
     * </p>
     */
    public AbstractComplexEMFBinding() {
    }

    /**
     * Constructs the binding with an efactory.
     *
     * @param factory Factory used to create model objects.
     */
    public AbstractComplexEMFBinding(EFactory factory) {
        this.factory = factory;
    }

    /**
     * Constructs the binding with an efactory and a type.
     *
     * @param factory Factory used to create model objects.
     */
    public AbstractComplexEMFBinding(EFactory factory, Class type) {
        this(factory);
        this.type = type;
    }
    
    /**
     * Dynamically tries to determine the type of the object using emf naming
     * conventions and the name returned by {@link Binding#getTarget()}.
     * <p>
     * This implementation is a heuristic and is not guaranteed to work. Subclasses
     * may override to provide the type explicitly.
     * </p>
     */
    public Class getType() {
        if ( type != null ) {
            return type;
        }
        
        //try to build up a class name 
        String pkg = factory.getClass().getPackage().getName();

        if (pkg.endsWith(".impl")) {
            pkg = pkg.substring(0, pkg.length() - 5);
        }

        String localName = getTarget().getLocalPart();

        try {
            return Class.forName(pkg + "." + localName);
        } catch (ClassNotFoundException e) {
            //
            //check for anonymous complex type
            //
            int i = localName.indexOf('_');
            if ( i != -1 ) {
                String className = localName.substring(i+1) + "Type";
                
                try {
                    return Class.forName(pkg + "." + className);
                } catch (ClassNotFoundException e1) {
                }
            }
        }

        throw new RuntimeException( "Could not map an EMF model class to:" + localName);
    }

    /**
     * Uses EMF reflection to create an instance of the EMF model object this
     * binding maps to.
     * <p>
     * The properties of the resulting object are set using the the contents of
     * <param>node</param>. In the case that the name of a child element or
     * attributes does not match the name of a property on the object, subclasses
     * may wish to extend this method and set the property explicitly.
     * </p>
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        //does this binding actually map to an eObject?
        if (EObject.class.isAssignableFrom(getType()) && (factory != null)) {
            EObject eObject = createEObject(value);
            if ( eObject == null ) {
                return value;
            }
            
            setProperties( eObject, node, false );
            setProperties( eObject, node, true );
            
            //check for a complex type with simpleContent, in this case use 
            // the string value (if any) to set the value property
            if (instance.getElementDeclaration().getTypeDefinition().getBaseType() instanceof XSDTypeDefinition) {
                if ((value != null) && EMFUtils.has(eObject, "value")) {
                    setProperty(eObject, "value", value, false);
                }
            }

            return eObject;
        }

        //could not do it, just return whatever was passed in
        return value;
    }

    /**
     * Reflectively creates an instance of the target object.
     */
    protected EObject createEObject( Object value ) throws Exception {
        if ((value == null) || !(getType().isAssignableFrom(value.getClass()))) {
            // yes, try and use the factory to dynamically create a new instance

            // get the classname
            String className = getType().getName();
            int index = className.lastIndexOf('.');

            if (index != -1) {
                className = className.substring(index + 1);
            }

            // find the proper create method
            Method create = factory.getClass().getMethod("create" + className, null);

            if (create == null) {
                // no dice
                return null;
            }

            // create the instance
            return (EObject) create.invoke(factory, null);
        } else {
            // value already provided (e.g., by a subtype binding with
            // BEFORE execution mode)
            return (EObject) value;
        }    
    }
    
    /**
     * Helper method for settings properties of an eobject.
     */
    void setProperties(EObject eObject, Node node, boolean lax ) {
        // reflectivley set the properties of it
        for (Iterator c = node.getChildren().iterator(); c.hasNext();) {
            Node child = (Node) c.next();
            String property = child.getComponent().getName();
          
            setProperty(eObject, property, child.getValue(), lax);
        }

        for (Iterator a = node.getAttributes().iterator(); a.hasNext();) {
            Node att = (Node) a.next();
            String property = att.getComponent().getName();
            
            setProperty(eObject, property, att.getValue(), lax);
        }
        
    }
    
    /**
     * Internal method for reflectively setting the property of an eobject.
     * <p>
     * Subclasses may override.
     * </p>
     */
    protected void setProperty(EObject eObject, String property, Object value, boolean lax) {
        try {
            if (EMFUtils.has(eObject, property)) {
                //dont do in lax mode since that means its a second pass
                if ( lax && EMFUtils.isSet(eObject, property) ) {
                    return;
                }
        
                try {
                    if (EMFUtils.isCollection(eObject, property)) {
                            EMFUtils.add(eObject, property, value);
                    } else {
                        EMFUtils.set(eObject, property, value);
                    }
                } catch (ClassCastException e) {
                    //convert to the correct type
                    EStructuralFeature feature = EMFUtils.feature(eObject, property);
                    Class target = feature.getEType().getInstanceClass();

                    Object converted = convert( value, target, e );
                    try {
                        EMFUtils.set(eObject, property, converted);
                    }
                    catch( ClassCastException e1 ) {
                        //try to convert based on method return type
                        //JD: this is a hack
                        try {
                            Method g = eObject.getClass().getMethod( "get" +  
                                property.substring(0,1).toUpperCase() + property.substring(1), null);
                            if ( g == null ) {
                                throw e;
                            }
                            
                            target = g.getReturnType();
                            converted = convert( value, target, e );
                            
                            EMFUtils.set( eObject, property, converted );   
                        } 
                        catch (Exception e2) {
                            throw e;
                        }
                    }
                }
            } 
            else {
                //search by type, this is a bit of a hack so we only do it if the 
                // lax flag is set
                if (lax && value != null) {
                    List features = EMFUtils.features(eObject, value.getClass());

                    if (features.size() == 1) {
                        EStructuralFeature feature = (EStructuralFeature) features.get(0);
                        
                        if(EMFUtils.isCollection(eObject, feature)) {
                            EMFUtils.add(eObject, feature, value);
                        }
                        else {
                            //only set if not previous set
                            if ( !eObject.eIsSet( feature ) ) {
                                eObject.eSet(feature, value);
                            }
                        }
                    }
                }
            }
        }
        catch( RuntimeException e ) {
            String msg = "Unable to set property: " + property + " for eobject: " + getTarget();
            throw new RuntimeException( msg, e );
        }
       
    }

    /**
     * Helper method to convert a value, throwing an exception when it cant be 
     * converted. 
     *
     */
    private Object convert( Object value, Class target, RuntimeException toThrow ) throws RuntimeException {
        Object converted = value;
        if ((converted != null) && !converted.getClass().isAssignableFrom(target)) {
            //TODO: log this
            converted = Converters.convert(value, target);
        }

        if (converted == null) {
            //just throw the oringinal exception
            throw toThrow;
        }
        
        return converted;
    }
    
    /**
     * Uses EMF reflection dynamically return the property with the specified
     * name.
     * <p>
     * In the case that the name of a child element or
     * attributes does not match the name of a property on the object, subclasses
     * may wish to extend this method and set the property explicitly.
     * </p>
     */
    public Object getProperty(Object object, QName name)
        throws Exception {
        if (object instanceof EObject) {
            EObject eObject = (EObject) object;

            if (EMFUtils.has(eObject, name.getLocalPart())) {
                return EMFUtils.get(eObject, name.getLocalPart());
            }
            else {
                //special case check for "_" since emf removes these from bean 
                // property names
                if ( name.getLocalPart().contains( "_" ) ) {
                    String stripped = name.getLocalPart().replaceAll( "_", "" );
                    if (EMFUtils.has(eObject, stripped)) {
                        return EMFUtils.get(eObject, stripped);
                    }        
                }
            }
        }

        return null;
    }
}
