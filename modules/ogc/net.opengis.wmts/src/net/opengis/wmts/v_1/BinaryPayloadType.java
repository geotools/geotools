/**
 */
package net.opengis.wmts.v_1;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Binary Payload Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wmts.v_1.BinaryPayloadType#getFormat <em>Format</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.BinaryPayloadType#getBinaryContent <em>Binary Content</em>}</li>
 * </ul>
 *
 * @see net.opengis.wmts.v_1.wmtsv_1Package#getBinaryPayloadType()
 * @model extendedMetaData="name='BinaryPayload_._type' kind='elementOnly'"
 * @generated
 */
public interface BinaryPayloadType extends EObject {
    /**
     * Returns the value of the '<em><b>Format</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     * 							MIMEType format of the PayloadContent 
     * 							once base64 decodified.
     * 						
     * <!-- end-model-doc -->
     * @return the value of the '<em>Format</em>' attribute.
     * @see #setFormat(String)
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getBinaryPayloadType_Format()
     * @model dataType="net.opengis.ows11.MimeType" required="true"
     *        extendedMetaData="kind='element' name='Format' namespace='##targetNamespace'"
     * @generated
     */
    String getFormat();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.BinaryPayloadType#getFormat <em>Format</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Format</em>' attribute.
     * @see #getFormat()
     * @generated
     */
    void setFormat(String value);

    /**
     * Returns the value of the '<em><b>Binary Content</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     * 							Binary content encoded in base64. It could be useful to 
     * 							enclose it in a CDATA element to avoid XML parsing.
     * 						
     * <!-- end-model-doc -->
     * @return the value of the '<em>Binary Content</em>' attribute.
     * @see #setBinaryContent(byte[])
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getBinaryPayloadType_BinaryContent()
     * @model dataType="org.eclipse.emf.ecore.xml.type.Base64Binary" required="true"
     *        extendedMetaData="kind='element' name='BinaryContent' namespace='##targetNamespace'"
     * @generated
     */
    byte[] getBinaryContent();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.BinaryPayloadType#getBinaryContent <em>Binary Content</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Binary Content</em>' attribute.
     * @see #getBinaryContent()
     * @generated
     */
    void setBinaryContent(byte[] value);

} // BinaryPayloadType
