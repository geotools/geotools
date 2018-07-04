/**
 */
package net.opengis.wmts.v_1;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>URL Template Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wmts.v_1.URLTemplateType#getFormat <em>Format</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.URLTemplateType#getResourceType <em>Resource Type</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.URLTemplateType#getTemplate <em>Template</em>}</li>
 * </ul>
 *
 * @see net.opengis.wmts.v_1.wmtsv_1Package#getURLTemplateType()
 * @model extendedMetaData="name='URLTemplateType' kind='empty'"
 * @generated
 */
public interface URLTemplateType extends EObject {
    /**
     * Returns the value of the '<em><b>Format</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Format of the resource representation that can 
     * 				be retrieved one resolved the URL template.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Format</em>' attribute.
     * @see #setFormat(String)
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getURLTemplateType_Format()
     * @model dataType="net.opengis.ows11.MimeType" required="true"
     *        extendedMetaData="kind='attribute' name='format'"
     * @generated
     */
    String getFormat();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.URLTemplateType#getFormat <em>Format</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Format</em>' attribute.
     * @see #getFormat()
     * @generated
     */
    void setFormat(String value);

    /**
     * Returns the value of the '<em><b>Resource Type</b></em>' attribute.
     * The literals are from the enumeration {@link net.opengis.wmts.v_1.ResourceTypeType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Resource type to be retrieved. It can only 
     * 				be "tile" or "FeatureInfo"
     * <!-- end-model-doc -->
     * @return the value of the '<em>Resource Type</em>' attribute.
     * @see net.opengis.wmts.v_1.ResourceTypeType
     * @see #isSetResourceType()
     * @see #unsetResourceType()
     * @see #setResourceType(ResourceTypeType)
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getURLTemplateType_ResourceType()
     * @model unsettable="true" required="true"
     *        extendedMetaData="kind='attribute' name='resourceType'"
     * @generated
     */
    ResourceTypeType getResourceType();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.URLTemplateType#getResourceType <em>Resource Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Resource Type</em>' attribute.
     * @see net.opengis.wmts.v_1.ResourceTypeType
     * @see #isSetResourceType()
     * @see #unsetResourceType()
     * @see #getResourceType()
     * @generated
     */
    void setResourceType(ResourceTypeType value);

    /**
     * Unsets the value of the '{@link net.opengis.wmts.v_1.URLTemplateType#getResourceType <em>Resource Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetResourceType()
     * @see #getResourceType()
     * @see #setResourceType(ResourceTypeType)
     * @generated
     */
    void unsetResourceType();

    /**
     * Returns whether the value of the '{@link net.opengis.wmts.v_1.URLTemplateType#getResourceType <em>Resource Type</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Resource Type</em>' attribute is set.
     * @see #unsetResourceType()
     * @see #getResourceType()
     * @see #setResourceType(ResourceTypeType)
     * @generated
     */
    boolean isSetResourceType();

    /**
     * Returns the value of the '<em><b>Template</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * URL template. A template processor will be 
     * 				applied to substitute some variables between {} for their values
     * 				and get a URL to a resource. 
     * 				We cound not use a anyURI type (that conforms the character 
     * 				restrictions specified in RFC2396 and excludes '{' '}' characters 
     * 				in some XML parsers) because this attribute must accept the 
     * 				'{' '}' caracters.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Template</em>' attribute.
     * @see #setTemplate(String)
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getURLTemplateType_Template()
     * @model dataType="net.opengis.wmts.v_1.TemplateType" required="true"
     *        extendedMetaData="kind='attribute' name='template'"
     * @generated
     */
    String getTemplate();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.URLTemplateType#getTemplate <em>Template</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Template</em>' attribute.
     * @see #getTemplate()
     * @generated
     */
    void setTemplate(String value);

} // URLTemplateType
