/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Output Format List Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs.OutputFormatListType#getGroup <em>Group</em>}</li>
 *   <li>{@link net.opengis.wfs.OutputFormatListType#getFormat <em>Format</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs.WfsPackage#getOutputFormatListType()
 * @model extendedMetaData="name='OutputFormatListType' kind='elementOnly'"
 * @generated
 */
public interface OutputFormatListType extends EObject {
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
     * @see net.opengis.wfs.WfsPackage#getOutputFormatListType_Group()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
     *        extendedMetaData="kind='group' name='group:0'"
     * @generated
     */
	FeatureMap getGroup();

	/**
     * Returns the value of the '<em><b>Format</b></em>' attribute list.
     * The list contents are of type {@link java.lang.String}.
     * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Format</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
     * @return the value of the '<em>Format</em>' attribute list.
     * @see net.opengis.wfs.WfsPackage#getOutputFormatListType_Format()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" required="true" transient="true" volatile="true" derived="true" many="true"
     *        extendedMetaData="kind='element' name='Format' namespace='##targetNamespace' group='#group:0'"
     * @generated
     */
	EList getFormat();

} // OutputFormatListType
