/**
 */
package net.opengis.wmts.v_1;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Text Payload Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wmts.v_1.TextPayloadType#getFormat <em>Format</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.TextPayloadType#getTextContent <em>Text Content</em>}</li>
 * </ul>
 *
 * @see net.opengis.wmts.v_1.wmtsv_1Package#getTextPayloadType()
 * @model extendedMetaData="name='TextPayload_._type' kind='elementOnly'"
 * @generated
 */
public interface TextPayloadType extends EObject {
    /**
     * Returns the value of the '<em><b>Format</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * MIMEType format of the TextContent
     * <!-- end-model-doc -->
     * @return the value of the '<em>Format</em>' attribute.
     * @see #setFormat(String)
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getTextPayloadType_Format()
     * @model dataType="net.opengis.ows11.MimeType" required="true"
     *        extendedMetaData="kind='element' name='Format' namespace='##targetNamespace'"
     * @generated
     */
    String getFormat();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.TextPayloadType#getFormat <em>Format</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Format</em>' attribute.
     * @see #getFormat()
     * @generated
     */
    void setFormat(String value);

    /**
     * Returns the value of the '<em><b>Text Content</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     * 							Text string like HTML, XHTML, XML or TXT. HTML and TXT data has 
     * 							to be enclosed in a CDATA element to avoid XML parsing.
     * 						
     * <!-- end-model-doc -->
     * @return the value of the '<em>Text Content</em>' attribute.
     * @see #setTextContent(String)
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getTextPayloadType_TextContent()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
     *        extendedMetaData="kind='element' name='TextContent' namespace='##targetNamespace'"
     * @generated
     */
    String getTextContent();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.TextPayloadType#getTextContent <em>Text Content</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Text Content</em>' attribute.
     * @see #getTextContent()
     * @generated
     */
    void setTextContent(String value);

} // TextPayloadType
