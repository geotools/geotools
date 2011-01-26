/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.ows11;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Manifest Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Unordered list of one or more groups of references to remote and/or local resources. 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.ows11.ManifestType#getReferenceGroup <em>Reference Group</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.ows11.Ows11Package#getManifestType()
 * @model extendedMetaData="name='ManifestType' kind='elementOnly'"
 * @generated
 */
public interface ManifestType extends BasicIdentificationType {
    /**
     * Returns the value of the '<em><b>Reference Group</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.ows11.ReferenceGroupType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Reference Group</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Reference Group</em>' containment reference list.
     * @see net.opengis.ows11.Ows11Package#getManifestType_ReferenceGroup()
     * @model type="net.opengis.ows11.ReferenceGroupType" containment="true" required="true"
     *        extendedMetaData="kind='element' name='ReferenceGroup' namespace='##targetNamespace'"
     * @generated
     */
    EList getReferenceGroup();

} // ManifestType
