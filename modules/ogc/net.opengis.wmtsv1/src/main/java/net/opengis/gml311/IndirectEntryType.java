/**
 */
package net.opengis.gml311;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Indirect Entry Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * An entry in a dictionary of definitions that contains a GML object which references a remote definition object. This entry is expected to be convenient in allowing multiple elements in one XML document to contain short (abbreviated XPointer) references, which are resolved to an external definition provided in a Dictionary element in the same XML document. Specialized descendents of this dictionaryEntry might be restricted in an application schema to allow only including specified types of definitions as valid entries in a dictionary. 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.IndirectEntryType#getDefinitionProxy <em>Definition Proxy</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getIndirectEntryType()
 * @model extendedMetaData="name='IndirectEntryType' kind='elementOnly'"
 * @generated
 */
public interface IndirectEntryType extends EObject {
    /**
     * Returns the value of the '<em><b>Definition Proxy</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Definition Proxy</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Definition Proxy</em>' containment reference.
     * @see #setDefinitionProxy(DefinitionProxyType)
     * @see net.opengis.gml311.Gml311Package#getIndirectEntryType_DefinitionProxy()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='DefinitionProxy' namespace='##targetNamespace'"
     * @generated
     */
    DefinitionProxyType getDefinitionProxy();

    /**
     * Sets the value of the '{@link net.opengis.gml311.IndirectEntryType#getDefinitionProxy <em>Definition Proxy</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Definition Proxy</em>' containment reference.
     * @see #getDefinitionProxy()
     * @generated
     */
    void setDefinitionProxy(DefinitionProxyType value);

} // IndirectEntryType
