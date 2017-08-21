/**
 */
package net.opengis.gml311;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Identifier Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * An identification of a CRS object. The first use of the IdentifierType for an object, if any, is normally the primary identification code, and any others are aliases.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.IdentifierType#getNameGroup <em>Name Group</em>}</li>
 *   <li>{@link net.opengis.gml311.IdentifierType#getName <em>Name</em>}</li>
 *   <li>{@link net.opengis.gml311.IdentifierType#getVersion <em>Version</em>}</li>
 *   <li>{@link net.opengis.gml311.IdentifierType#getRemarks <em>Remarks</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getIdentifierType()
 * @model extendedMetaData="name='IdentifierType' kind='elementOnly'"
 * @generated
 */
public interface IdentifierType extends EObject {
    /**
     * Returns the value of the '<em><b>Name Group</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The code or name for this Identifier, often from a controlled list or pattern defined by a code space. The optional codeSpace attribute is normally included to identify or reference a code space within which one or more codes are defined. This code space is often defined by some authority organization, where one organization may define multiple code spaces. The range and format of each Code Space identifier is defined by that code space authority. Information about that code space authority can be included as metaDataProperty elements which are optionally allowed in all CRS objects.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Name Group</em>' attribute list.
     * @see net.opengis.gml311.Gml311Package#getIdentifierType_NameGroup()
     * @model dataType="org.eclipse.emf.ecore.EFeatureMapEntry" required="true" many="false"
     *        extendedMetaData="kind='group' name='name:group' namespace='##targetNamespace'"
     * @generated
     */
    FeatureMap getNameGroup();

    /**
     * Returns the value of the '<em><b>Name</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The code or name for this Identifier, often from a controlled list or pattern defined by a code space. The optional codeSpace attribute is normally included to identify or reference a code space within which one or more codes are defined. This code space is often defined by some authority organization, where one organization may define multiple code spaces. The range and format of each Code Space identifier is defined by that code space authority. Information about that code space authority can be included as metaDataProperty elements which are optionally allowed in all CRS objects.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Name</em>' containment reference.
     * @see #setName(CodeType)
     * @see net.opengis.gml311.Gml311Package#getIdentifierType_Name()
     * @model containment="true" required="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='name' namespace='##targetNamespace' group='name:group'"
     * @generated
     */
    CodeType getName();

    /**
     * Sets the value of the '{@link net.opengis.gml311.IdentifierType#getName <em>Name</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Name</em>' containment reference.
     * @see #getName()
     * @generated
     */
    void setName(CodeType value);

    /**
     * Returns the value of the '<em><b>Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Identifier of the version of the associated codeSpace or code, as specified by the codeSpace or code authority. This version is included only when the "code" or "codeSpace" uses versions. When appropriate, the version is identified by the effective date, coded using ISO 8601 date format.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Version</em>' attribute.
     * @see #setVersion(String)
     * @see net.opengis.gml311.Gml311Package#getIdentifierType_Version()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='element' name='version' namespace='##targetNamespace'"
     * @generated
     */
    String getVersion();

    /**
     * Sets the value of the '{@link net.opengis.gml311.IdentifierType#getVersion <em>Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Version</em>' attribute.
     * @see #getVersion()
     * @generated
     */
    void setVersion(String value);

    /**
     * Returns the value of the '<em><b>Remarks</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Remarks about this code or alias.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Remarks</em>' containment reference.
     * @see #setRemarks(StringOrRefType)
     * @see net.opengis.gml311.Gml311Package#getIdentifierType_Remarks()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='remarks' namespace='##targetNamespace'"
     * @generated
     */
    StringOrRefType getRemarks();

    /**
     * Sets the value of the '{@link net.opengis.gml311.IdentifierType#getRemarks <em>Remarks</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Remarks</em>' containment reference.
     * @see #getRemarks()
     * @generated
     */
    void setRemarks(StringOrRefType value);

} // IdentifierType
