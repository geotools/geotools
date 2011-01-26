/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.ows11;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Values Reference Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.ows11.ValuesReferenceType#getValue <em>Value</em>}</li>
 *   <li>{@link net.opengis.ows11.ValuesReferenceType#getReference <em>Reference</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.ows11.Ows11Package#getValuesReferenceType()
 * @model extendedMetaData="name='ValuesReference_._type' kind='simple'"
 * @generated
 */
public interface ValuesReferenceType extends EObject {
    /**
     * Returns the value of the '<em><b>Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Human-readable name of the list of values provided by the referenced document. Can be empty string when this list has no name. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Value</em>' attribute.
     * @see #setValue(String)
     * @see net.opengis.ows11.Ows11Package#getValuesReferenceType_Value()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="name=':0' kind='simple'"
     * @generated
     */
    String getValue();

    /**
     * Sets the value of the '{@link net.opengis.ows11.ValuesReferenceType#getValue <em>Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Value</em>' attribute.
     * @see #getValue()
     * @generated
     */
    void setValue(String value);

    /**
     * Returns the value of the '<em><b>Reference</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Reference to data or metadata recorded elsewhere, either external to this XML document or within it. Whenever practical, this attribute should be a URL from which this metadata can be electronically retrieved. Alternately, this attribute can reference a URN for well-known metadata. For example, such a URN could be a URN defined in the "ogc" URN namespace. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Reference</em>' attribute.
     * @see #setReference(String)
     * @see net.opengis.ows11.Ows11Package#getValuesReferenceType_Reference()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI" required="true"
     *        extendedMetaData="kind='attribute' name='reference' namespace='##targetNamespace'"
     * @generated
     */
    String getReference();

    /**
     * Sets the value of the '{@link net.opengis.ows11.ValuesReferenceType#getReference <em>Reference</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Reference</em>' attribute.
     * @see #getReference()
     * @generated
     */
    void setReference(String value);

} // ValuesReferenceType
