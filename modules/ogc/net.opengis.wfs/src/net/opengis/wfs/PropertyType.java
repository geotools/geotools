/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs;

import javax.xml.namespace.QName;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Property Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs.PropertyType#getName <em>Name</em>}</li>
 *   <li>{@link net.opengis.wfs.PropertyType#getValue <em>Value</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs.WfsPackage#getPropertyType()
 * @model extendedMetaData="name='PropertyType' kind='elementOnly'"
 * @generated
 */
public interface PropertyType extends EObject {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *                   The Name element contains the name of a feature property
	 *                   to be updated.
	 *                
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(Object)
	 * @see net.opengis.wfs.WFSPackage#getPropertyType_Name()
	 * @model 
	 */
	QName getName();

	/**
     * Sets the value of the '{@link net.opengis.wfs.PropertyType#getName <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @param value the new value of the '<em>Name</em>' attribute.
     * @see #getName()
     * @generated
     */
	void setName(QName value);

	/**
	 * Returns the value of the '<em><b>Value</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *                   The Value element contains the replacement value for the
	 *                   named property.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Value</em>' containment reference.
	 * @see #setValue(EObject)
	 * @see net.opengis.wfs.WFSPackage#getPropertyType_Value()
	 * @model 
	 */
	Object getValue();

	/**
     * Sets the value of the '{@link net.opengis.wfs.PropertyType#getValue <em>Value</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @param value the new value of the '<em>Value</em>' attribute.
     * @see #getValue()
     * @generated
     */
	void setValue(Object value);

} // PropertyType
