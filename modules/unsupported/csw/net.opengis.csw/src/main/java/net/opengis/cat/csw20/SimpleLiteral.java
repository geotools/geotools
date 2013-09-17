/**
 * <copyright>
 * (C) 2012, Open Source Geospatial Foundation (OSGeo)
 * (C) 2004,2010 Open Geospatial Consortium.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation;
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * </copyright>
 *
 * $Id$
 */
package net.opengis.cat.csw20;

import java.lang.String;
import java.net.URI;

import org.eclipse.emf.ecore.EObject;

/**
 * @model extendedMetaData="name='SimpleLiteral' kind='elementOnly'"
 */
public interface SimpleLiteral extends EObject {
    /**
     * @model
     */
    String getName();
    
    
    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.SimpleLiteral#getName <em>Name</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Name</em>' reference.
     * @see #getName()
     * @generated
     */
    void setName(String value);


    /**
     * @model 
     */
    Object getValue();
    
    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.SimpleLiteral#getValue <em>Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Value</em>' attribute.
     * @see #getValue()
     * @generated
     */
    void setValue(Object value);

    /**
     * @model 
     */
    URI getScheme();


    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.SimpleLiteral#getScheme <em>Scheme</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Scheme</em>' attribute.
     * @see #getScheme()
     * @generated
     */
    void setScheme(URI value);


} // BriefRecordType
