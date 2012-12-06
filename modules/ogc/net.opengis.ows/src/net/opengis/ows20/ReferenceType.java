/**
 */
package net.opengis.ows20;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Reference Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Complete reference to a remote or local resource,
 *       allowing including metadata about that resource.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.ows20.ReferenceType#getIdentifier <em>Identifier</em>}</li>
 *   <li>{@link net.opengis.ows20.ReferenceType#getAbstract <em>Abstract</em>}</li>
 *   <li>{@link net.opengis.ows20.ReferenceType#getFormat <em>Format</em>}</li>
 *   <li>{@link net.opengis.ows20.ReferenceType#getMetadataGroup <em>Metadata Group</em>}</li>
 *   <li>{@link net.opengis.ows20.ReferenceType#getMetadata <em>Metadata</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.ows20.Ows20Package#getReferenceType()
 * @model extendedMetaData="name='ReferenceType' kind='elementOnly'"
 * @generated
 */
public interface ReferenceType extends AbstractReferenceBaseType {
    /**
     * Returns the value of the '<em><b>Identifier</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Optional unique identifier of the referenced
     *               resource.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Identifier</em>' containment reference.
     * @see #setIdentifier(CodeType)
     * @see net.opengis.ows20.Ows20Package#getReferenceType_Identifier()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='Identifier' namespace='##targetNamespace'"
     * @generated
     */
    CodeType getIdentifier();

    /**
     * Sets the value of the '{@link net.opengis.ows20.ReferenceType#getIdentifier <em>Identifier</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Identifier</em>' containment reference.
     * @see #getIdentifier()
     * @generated
     */
    void setIdentifier(CodeType value);

    /**
     * Returns the value of the '<em><b>Abstract</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.ows20.LanguageStringType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Brief narrative description of this resource, normally
     *       used for display to humans.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Abstract</em>' containment reference list.
     * @see net.opengis.ows20.Ows20Package#getReferenceType_Abstract()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='Abstract' namespace='##targetNamespace'"
     * @generated
     */
    EList<LanguageStringType> getAbstract();

    /**
     * Returns the value of the '<em><b>Format</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The format of the referenced resource. This
     *               element is omitted when the mime type is indicated in the http
     *               header of the reference.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Format</em>' attribute.
     * @see #setFormat(String)
     * @see net.opengis.ows20.Ows20Package#getReferenceType_Format()
     * @model dataType="net.opengis.ows20.MimeType"
     *        extendedMetaData="kind='element' name='Format' namespace='##targetNamespace'"
     * @generated
     */
    String getFormat();

    /**
     * Sets the value of the '{@link net.opengis.ows20.ReferenceType#getFormat <em>Format</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Format</em>' attribute.
     * @see #getFormat()
     * @generated
     */
    void setFormat(String value);

    /**
     * Returns the value of the '<em><b>Metadata Group</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Optional unordered list of additional metadata
     *               about this resource. A list of optional metadata elements for
     *               this ReferenceType could be specified in the Implementation
     *               Specification for each use of this type in a specific
     *               OWS.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Metadata Group</em>' attribute list.
     * @see net.opengis.ows20.Ows20Package#getReferenceType_MetadataGroup()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
     *        extendedMetaData="kind='group' name='Metadata:group' namespace='##targetNamespace'"
     * @generated
     */
    FeatureMap getMetadataGroup();

    /**
     * Returns the value of the '<em><b>Metadata</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.ows20.MetadataType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Optional unordered list of additional metadata
     *               about this resource. A list of optional metadata elements for
     *               this ReferenceType could be specified in the Implementation
     *               Specification for each use of this type in a specific
     *               OWS.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Metadata</em>' containment reference list.
     * @see net.opengis.ows20.Ows20Package#getReferenceType_Metadata()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Metadata' namespace='##targetNamespace' group='Metadata:group'"
     * @generated
     */
    EList<MetadataType> getMetadata();

} // ReferenceType
