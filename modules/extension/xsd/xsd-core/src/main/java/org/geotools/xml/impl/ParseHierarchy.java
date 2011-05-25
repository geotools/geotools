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


/**
 * A utility class used to parse objects in a type hierarchy.
 *
 * @author Justin Deoliveira,Refractions Research Inc.,jdeolive@refractions.net
 *TODO: kill this class
 *
 *
 * @source $URL$
 */
public class ParseHierarchy {
    //	XSDTypeDefinition bottom;
    //	StrategyFactory factory;
    //	
    //	List strategies;
    //	
    //	ParseHierarchy(XSDTypeDefinition bottom, StrategyFactory factory) {
    //		this.bottom = bottom;
    //		this.factory = factory;
    //		strategies = new ArrayList();
    //	}
    //	
    //	/**
    //	 * Initialized the hierarchy by walking from the bottom type up.
    //	 */
    //	void initialize(MutablePicoContainer context) {
    //		strategies.clear();
    //		
    //		XSDTypeDefinition type = bottom;
    //		while(type != null) {
    //			//look up the associated strategy object for this type
    //			Strategy strategy = null; 
    //			if (type.getName() != null) {
    //				QName qName = new QName(type.getTargetNamespace(),type.getName());
    //				Class strategyClass = factory.getStrategy(qName);
    //				strategy = (Strategy) 
    //					context.getComponentInstanceOfType(strategyClass);
    //				if (strategy == null) {
    //					context.registerComponentImplementation(strategyClass);
    //					strategy = (Strategy) 
    //						context.getComponentInstanceOfType(strategyClass);
    //				}
    //			}
    //			else {
    //				//special case check, look for an anonymous complex type 
    //				// with simple content
    //				if (type instanceof XSDComplexTypeDefinition && 
    //						type.getBaseType() instanceof XSDSimpleTypeDefinition) {
    //					//we assign the default complex strategy instread of 
    //					// delegating to parent, because if we dont, any attributes
    //					// defined by the type will not be parsed because simple
    //					// types cannot have attributes.
    //					//TODO: put this somewhere else, perahps in teh factories
    //					// that create the strategy objects
    //					strategy = new XSAnyTypeStrategy();
    //				}
    //			}
    //			
    //			if (strategy != null) {
    //				//add the strategy
    //				strategies.add(strategy);
    //				
    //				//check execution mode, if override break out
    //				if (strategy.getExecutionMode() == Strategy.OVERRIDE)
    //					break;
    //			}
    //			else {
    //				//two posibilities
    //				if (!strategies.isEmpty()) {
    //					//make the last strategy the new root of the hierarchy
    //					break;
    //				}
    //				//else continue on to try to find a strategy further up in 
    //				// type hierarchy	
    //			}
    //		
    //			//get the next base type, if it is equal to this type then we have
    //			// reached the root of the type hierarchy
    //			if (type.equals(type.getBaseType()))
    //				break;
    //			type = type.getBaseType();
    //		}
    //	}
    //	
    //	/**
    //	 * Performs the parse in the order specified by the type hierarchy.
    //	 * 
    //	 * @param element The element being parsed.
    //	 * @param children The parsed values of any children.
    //	 * @param attrs The parsed values of any attributes.
    //	 * 
    //	 * @return the final parsed value for the element.
    //	 */
    //	Object parse(InstanceComponent instance, Node[] children, Node[] attrs) {
    //		//never pass null arguments to strategies
    //		children = children != null ? children : new Node[]{};
    //		attrs = attrs != null ? attrs : new Node[]{};
    //		
    //		//last parsed, null for first in execution order
    //		Object value = null;
    //		
    //		//before we do anything, process any facets
    //		value = parseFacets(instance);
    //	
    //		//simulated call stack
    //		Stack stack = new Stack();
    //		//execute strategies from bottom to top
    //		for (int i = 0; i < strategies.size(); i++) {
    //			Strategy strategy = (Strategy) strategies.get(i);
    //			
    //			if (strategy.getExecutionMode() == Strategy.AFTER) {
    //				//put on stack to execute after parent
    //				stack.push(strategy);
    //				continue;
    //			}
    //			
    //			//execute the strategy
    //			try {
    //				if (strategy instanceof SimpleStrategy) {
    //					value = ((SimpleStrategy)strategy).parse(instance,value);	
    //				}
    //				else {
    //					value = ((ComplexStrategy)strategy)
    //						.parse((Element)instance,children,attrs,value);
    //				}
    //				
    //			} 
    //			catch (Throwable t) {
    //				String msg = "Parsing failed for " + instance.getName();
    //				throw new RuntimeException(msg, t); 
    //			}
    //		}
    //		
    //		//unwind the call stack
    //		while(!stack.isEmpty()) {
    //			Strategy strategy = (Strategy)stack.pop();
    //			try {
    //				if (strategy instanceof SimpleStrategy) {
    //					value = ((SimpleStrategy)strategy).parse(instance,value);	
    //				}
    //				else {
    //					value = ((ComplexStrategy)strategy)
    //						.parse((Element) instance,children,attrs,value);
    //				}
    //			}
    //			catch(Throwable t) {
    //				String msg = "Parsing failed for " + instance.getName();
    //				throw new RuntimeException(msg, t);
    //			}
    //		}
    //		
    //		return value;
    //	
    //	}
}
