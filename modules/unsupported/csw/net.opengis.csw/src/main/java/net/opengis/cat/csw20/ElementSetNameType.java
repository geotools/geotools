/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.cat.csw20;

import java.util.List;

import javax.xml.namespace.QName;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Element Set Name Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.cat.csw20.ElementSetNameType#getValue <em>Value</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.ElementSetNameType#getTypeNames <em>Type Names</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.cat.csw20.Csw20Package#getElementSetNameType()
 * @model extendedMetaData="name='ElementSetNameType' kind='simple'"
 * @generated
 */
public interface ElementSetNameType extends EObject {
    /**
     * Returns the value of the '<em><b>Value</b></em>' attribute.
     * The literals are from the enumeration {@link net.opengis.cat.csw20.ElementSetType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Value</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Value</em>' attribute.
     * @see net.opengis.cat.csw20.ElementSetType
     * @see #isSetValue()
     * @see #unsetValue()
     * @see #setValue(ElementSetType)
     * @see net.opengis.cat.csw20.Csw20Package#getElementSetNameType_Value()
     * @model unsettable="true"
     *        extendedMetaData="name=':0' kind='simple'"
     * @generated
     */
    ElementSetType getValue();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.ElementSetNameType#getValue <em>Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Value</em>' attribute.
     * @see net.opengis.cat.csw20.ElementSetType
     * @see #isSetValue()
     * @see #unsetValue()
     * @see #getValue()
     * @generated
     */
    void setValue(ElementSetType value);

    /**
     * Unsets the value of the '{@link net.opengis.cat.csw20.ElementSetNameType#getValue <em>Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetValue()
     * @see #getValue()
     * @see #setValue(ElementSetType)
     * @generated
     */
    void unsetValue();

    /**
     * Returns whether the value of the '{@link net.opengis.cat.csw20.ElementSetNameType#getValue <em>Value</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Value</em>' attribute is set.
     * @see #unsetValue()
     * @see #getValue()
     * @see #setValue(ElementSetType)
     * @generated
     */
    boolean isSetValue();

    /**
     * Returns the value of the '<em><b>Type Names</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Type Names</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Type Names</em>' attribute.
     * @see #setTypeNames(List)
     * @see net.opengis.cat.csw20.Csw20Package#getElementSetNameType_TypeNames()
     * @model dataType="net.opengis.cat.csw20.TypeNameListType"
     *        extendedMetaData="kind='attribute' name='typeNames'"
     * @generated
     */
    List<QName> getTypeNames();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.ElementSetNameType#getTypeNames <em>Type Names</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Type Names</em>' attribute.
     * @see #getTypeNames()
     * @generated
     */
    void setTypeNames(List<QName> value);

} // ElementSetNameType
