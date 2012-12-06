/**
 */
package net.opengis.ows20;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Nil Value Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * The value used (e.g. -255) to represent a nil value with
 *       optional nilReason and codeSpace attributes.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.ows20.NilValueType#getNilReason <em>Nil Reason</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.ows20.Ows20Package#getNilValueType()
 * @model extendedMetaData="name='NilValueType' kind='simple'"
 * @generated
 */
public interface NilValueType extends CodeType {
    /**
     * Returns the value of the '<em><b>Nil Reason</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * An anyURI value which refers to a resource that
     *             describes the reason for the nil value
     * <!-- end-model-doc -->
     * @return the value of the '<em>Nil Reason</em>' attribute.
     * @see #setNilReason(String)
     * @see net.opengis.ows20.Ows20Package#getNilValueType_NilReason()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
     *        extendedMetaData="kind='attribute' name='nilReason'"
     * @generated
     */
    String getNilReason();

    /**
     * Sets the value of the '{@link net.opengis.ows20.NilValueType#getNilReason <em>Nil Reason</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Nil Reason</em>' attribute.
     * @see #getNilReason()
     * @generated
     */
    void setNilReason(String value);

} // NilValueType
