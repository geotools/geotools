/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wps10;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Values Reference Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * References an externally defined finite set of values and ranges for this input.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wps10.ValuesReferenceType#getReference <em>Reference</em>}</li>
 *   <li>{@link net.opengis.wps10.ValuesReferenceType#getValuesForm <em>Values Form</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wps10.Wps10Package#getValuesReferenceType()
 * @model extendedMetaData="name='ValuesReferenceType' kind='empty'"
 * @generated
 */
public interface ValuesReferenceType extends EObject {
    /**
     * Returns the value of the '<em><b>Reference</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Reference to data or metadata recorded elsewhere, either external to this XML document or within it. Whenever practical, this attribute should be a URL from which this metadata can be electronically retrieved. Alternately, this attribute can reference a URN for well-known metadata. For example, such a URN could be a URN defined in the "ogc" URN namespace.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Reference</em>' attribute.
     * @see #setReference(String)
     * @see net.opengis.wps10.Wps10Package#getValuesReferenceType_Reference()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
     *        extendedMetaData="kind='attribute' name='reference' namespace='http://www.opengis.net/ows/1.1'"
     * @generated
     */
    String getReference();

    /**
     * Sets the value of the '{@link net.opengis.wps10.ValuesReferenceType#getReference <em>Reference</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Reference</em>' attribute.
     * @see #getReference()
     * @generated
     */
    void setReference(String value);

    /**
     * Returns the value of the '<em><b>Values Form</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Reference to a description of the mimetype, encoding, and schema used for this set of values and ranges.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Values Form</em>' attribute.
     * @see #setValuesForm(String)
     * @see net.opengis.wps10.Wps10Package#getValuesReferenceType_ValuesForm()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
     *        extendedMetaData="kind='attribute' name='valuesForm'"
     * @generated
     */
    String getValuesForm();

    /**
     * Sets the value of the '{@link net.opengis.wps10.ValuesReferenceType#getValuesForm <em>Values Form</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Values Form</em>' attribute.
     * @see #getValuesForm()
     * @generated
     */
    void setValuesForm(String value);

} // ValuesReferenceType
