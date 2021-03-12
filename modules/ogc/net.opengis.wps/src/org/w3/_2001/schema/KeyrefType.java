/**
 */
package org.w3._2001.schema;

import javax.xml.namespace.QName;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Keyref Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.w3._2001.schema.KeyrefType#getRefer <em>Refer</em>}</li>
 * </ul>
 *
 * @see org.w3._2001.schema.SchemaPackage#getKeyrefType()
 * @model extendedMetaData="name='keyref_._type' kind='elementOnly'"
 * @generated
 */
public interface KeyrefType extends Keybase {
	/**
	 * Returns the value of the '<em><b>Refer</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Refer</em>' attribute.
	 * @see #setRefer(QName)
	 * @see org.w3._2001.schema.SchemaPackage#getKeyrefType_Refer()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.QName" required="true"
	 *        extendedMetaData="kind='attribute' name='refer'"
	 * @generated
	 */
	QName getRefer();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.KeyrefType#getRefer <em>Refer</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Refer</em>' attribute.
	 * @see #getRefer()
	 * @generated
	 */
	void setRefer(QName value);

} // KeyrefType
