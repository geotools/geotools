/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>GML Object Type List Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs.GMLObjectTypeListType#getGMLObjectType <em>GML Object Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs.WfsPackage#getGMLObjectTypeListType()
 * @model extendedMetaData="name='GMLObjectTypeListType' kind='elementOnly'"
 * @generated
 */
public interface GMLObjectTypeListType extends EObject {
	/**
     * Returns the value of the '<em><b>GML Object Type</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.wfs.GMLObjectTypeType}.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     *                   Name of this GML object type, including any namespace prefix
     * <!-- end-model-doc -->
     * @return the value of the '<em>GML Object Type</em>' containment reference list.
     * @see net.opengis.wfs.WfsPackage#getGMLObjectTypeListType_GMLObjectType()
     * @model type="net.opengis.wfs.GMLObjectTypeType" containment="true" required="true"
     *        extendedMetaData="kind='element' name='GMLObjectType' namespace='##targetNamespace'"
     * @generated
     */
	EList getGMLObjectType();

} // GMLObjectTypeListType
