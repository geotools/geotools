/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs20;

import org.eclipse.emf.ecore.EObject;
import org.opengis.filter.expression.PropertyName;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Property Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs20.PropertyType#getValueReference <em>Value Reference</em>}</li>
 *   <li>{@link net.opengis.wfs20.PropertyType#getValue <em>Value</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs20.Wfs20Package#getPropertyType()
 * @model extendedMetaData="name='PropertyType' kind='elementOnly'"
 * @generated
 */
public interface PropertyType extends EObject {
    /**
     * Returns the value of the '<em><b>Value Reference</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Value Reference</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Value Reference</em>' containment reference.
     * @see #setValueReference(ValueReferenceType)
     * @see net.opengis.wfs20.Wfs20Package#getPropertyType_ValueReference()
     * @model 
     */
    ValueReferenceType getValueReference();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.PropertyType#getValueReference <em>Value Reference</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Value Reference</em>' reference.
     * @see #getValueReference()
     * @generated
     */
    void setValueReference(ValueReferenceType value);

    /**
     * Returns the value of the '<em><b>Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Value</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Value</em>' containment reference.
     * @see #setValue(EObject)
     * @see net.opengis.wfs20.Wfs20Package#getPropertyType_Value()
     * @model 
     */
    Object getValue();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.PropertyType#getValue <em>Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Value</em>' attribute.
     * @see #getValue()
     * @generated
     */
    void setValue(Object value);

} // PropertyType
