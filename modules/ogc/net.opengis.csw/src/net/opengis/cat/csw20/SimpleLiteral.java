/**
 * <copyright>
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
