/**
 */
package net.opengis.ows20;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Basic Identification Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Basic metadata identifying and describing a set of
 *       data.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.ows20.BasicIdentificationType#getIdentifier <em>Identifier</em>}</li>
 *   <li>{@link net.opengis.ows20.BasicIdentificationType#getMetadataGroup <em>Metadata Group</em>}</li>
 *   <li>{@link net.opengis.ows20.BasicIdentificationType#getMetadata <em>Metadata</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.ows20.Ows20Package#getBasicIdentificationType()
 * @model extendedMetaData="name='BasicIdentificationType' kind='elementOnly'"
 * @generated
 */
public interface BasicIdentificationType extends DescriptionType {
    /**
     * Returns the value of the '<em><b>Identifier</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Optional unique identifier or name of this
     *               dataset.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Identifier</em>' containment reference.
     * @see #setIdentifier(CodeType)
     * @see net.opengis.ows20.Ows20Package#getBasicIdentificationType_Identifier()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='Identifier' namespace='##targetNamespace'"
     * @generated
     */
    CodeType getIdentifier();

    /**
     * Sets the value of the '{@link net.opengis.ows20.BasicIdentificationType#getIdentifier <em>Identifier</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Identifier</em>' containment reference.
     * @see #getIdentifier()
     * @generated
     */
    void setIdentifier(CodeType value);

    /**
     * Returns the value of the '<em><b>Metadata Group</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Optional unordered list of additional metadata
     *               about this data(set). A list of optional metadata elements for
     *               this data identification could be specified in the
     *               Implementation Specification for this service.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Metadata Group</em>' attribute list.
     * @see net.opengis.ows20.Ows20Package#getBasicIdentificationType_MetadataGroup()
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
     *               about this data(set). A list of optional metadata elements for
     *               this data identification could be specified in the
     *               Implementation Specification for this service.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Metadata</em>' containment reference list.
     * @see net.opengis.ows20.Ows20Package#getBasicIdentificationType_Metadata()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Metadata' namespace='##targetNamespace' group='Metadata:group'"
     * @generated
     */
    EList<MetadataType> getMetadata();

} // BasicIdentificationType
