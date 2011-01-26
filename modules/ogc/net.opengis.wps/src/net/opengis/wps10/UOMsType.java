/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wps10;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>UOMs Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Identifies a UOM supported for this input or output.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wps10.UOMsType#getUOM <em>UOM</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wps10.Wps10Package#getUOMsType()
 * @model extendedMetaData="name='UOMsType' kind='elementOnly'"
 * @generated
 */
public interface UOMsType extends EObject {
    /**
     * Returns the value of the '<em><b>UOM</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.ows11.DomainMetadataType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Reference to a UOM supported for this input or output.
     * <!-- end-model-doc -->
     * @return the value of the '<em>UOM</em>' containment reference list.
     * @see net.opengis.wps10.Wps10Package#getUOMsType_UOM()
     * @model type="javax.measure.unit.Unit"
     */
    EList getUOM();

} // UOMsType
