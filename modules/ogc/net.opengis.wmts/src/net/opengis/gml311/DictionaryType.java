/**
 */
package net.opengis.gml311;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Dictionary Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A non-abstract bag that is specialized for use as a dictionary which contains a set of definitions. These definitions are referenced from other places, in the same and different XML documents. In this restricted type, the inherited optional "description" element can be used for a description of this dictionary. The inherited optional "name" element can be used for the name(s) of this dictionary. The inherited "metaDataProperty" elements can be used to reference or contain more information about this dictionary. The inherited required gml:id attribute allows the dictionary to be referenced using this handle. 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.DictionaryType#getGroup <em>Group</em>}</li>
 *   <li>{@link net.opengis.gml311.DictionaryType#getDictionaryEntryGroup <em>Dictionary Entry Group</em>}</li>
 *   <li>{@link net.opengis.gml311.DictionaryType#getDictionaryEntry <em>Dictionary Entry</em>}</li>
 *   <li>{@link net.opengis.gml311.DictionaryType#getIndirectEntry <em>Indirect Entry</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getDictionaryType()
 * @model extendedMetaData="name='DictionaryType' kind='elementOnly'"
 * @generated
 */
public interface DictionaryType extends DefinitionType {
    /**
     * Returns the value of the '<em><b>Group</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Group</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Group</em>' attribute list.
     * @see net.opengis.gml311.Gml311Package#getDictionaryType_Group()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
     *        extendedMetaData="kind='group' name='group:5'"
     * @generated
     */
    FeatureMap getGroup();

    /**
     * Returns the value of the '<em><b>Dictionary Entry Group</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * An entry in this dictionary. The content of an entry can itself be a lower level dictionary or definition collection. This element follows the standard GML property model, so the value may be provided directly or by reference. Note that if the value is provided by reference, this definition does not carry a handle (gml:id) in this context, so does not allow external references to this specific entry in this context. When used in this way the referenced definition will usually be in a dictionary in the same XML document. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Dictionary Entry Group</em>' attribute list.
     * @see net.opengis.gml311.Gml311Package#getDictionaryType_DictionaryEntryGroup()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='group' name='dictionaryEntry:group' namespace='##targetNamespace' group='#group:5'"
     * @generated
     */
    FeatureMap getDictionaryEntryGroup();

    /**
     * Returns the value of the '<em><b>Dictionary Entry</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.DictionaryEntryType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * An entry in this dictionary. The content of an entry can itself be a lower level dictionary or definition collection. This element follows the standard GML property model, so the value may be provided directly or by reference. Note that if the value is provided by reference, this definition does not carry a handle (gml:id) in this context, so does not allow external references to this specific entry in this context. When used in this way the referenced definition will usually be in a dictionary in the same XML document. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Dictionary Entry</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getDictionaryType_DictionaryEntry()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='dictionaryEntry' namespace='##targetNamespace' group='dictionaryEntry:group'"
     * @generated
     */
    EList<DictionaryEntryType> getDictionaryEntry();

    /**
     * Returns the value of the '<em><b>Indirect Entry</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.IndirectEntryType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * An identified reference to a remote entry in this dictionary, to be used when this entry should be identified to allow external references to this specific entry. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Indirect Entry</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getDictionaryType_IndirectEntry()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='indirectEntry' namespace='##targetNamespace' group='#group:5'"
     * @generated
     */
    EList<IndirectEntryType> getIndirectEntry();

} // DictionaryType
