/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.fes20;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Id Capabilities Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.fes20.IdCapabilitiesType#getResourceIdentifier <em>Resource Identifier</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.fes20.Fes20Package#getIdCapabilitiesType()
 * @model extendedMetaData="name='Id_CapabilitiesType' kind='elementOnly'"
 * @generated
 */
public interface IdCapabilitiesType extends EObject {
    /**
     * Returns the value of the '<em><b>Resource Identifier</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.fes20.ResourceIdentifierType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Resource Identifier</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Resource Identifier</em>' containment reference list.
     * @see net.opengis.fes20.Fes20Package#getIdCapabilitiesType_ResourceIdentifier()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='ResourceIdentifier' namespace='##targetNamespace'"
     * @generated
     */
    EList<ResourceIdentifierType> getResourceIdentifier();

} // IdCapabilitiesType
