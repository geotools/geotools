/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs10;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Telephone Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Telephone numbers for contacting the responsible individual or organization.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs10.TelephoneType#getVoice <em>Voice</em>}</li>
 *   <li>{@link net.opengis.wcs10.TelephoneType#getFacsimile <em>Facsimile</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs10.Wcs10Package#getTelephoneType()
 * @model extendedMetaData="name='TelephoneType' kind='elementOnly'"
 * @generated
 */
public interface TelephoneType extends EObject {
    /**
	 * Returns the value of the '<em><b>Voice</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Telephone number by which individuals can speak to the responsible organization or individual.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Voice</em>' attribute.
	 * @see #setVoice(String)
	 * @see net.opengis.wcs10.Wcs10Package#getTelephoneType_Voice()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='element' name='voice' namespace='##targetNamespace'"
	 * @generated
	 */
    String getVoice();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.TelephoneType#getVoice <em>Voice</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Voice</em>' attribute.
	 * @see #getVoice()
	 * @generated
	 */
	void setVoice(String value);

				/**
	 * Returns the value of the '<em><b>Facsimile</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Telephone number of a facsimile machine for the responsibleorganization or individual.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Facsimile</em>' attribute.
	 * @see #setFacsimile(String)
	 * @see net.opengis.wcs10.Wcs10Package#getTelephoneType_Facsimile()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='element' name='facsimile' namespace='##targetNamespace'"
	 * @generated
	 */
    String getFacsimile();

				/**
	 * Sets the value of the '{@link net.opengis.wcs10.TelephoneType#getFacsimile <em>Facsimile</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Facsimile</em>' attribute.
	 * @see #getFacsimile()
	 * @generated
	 */
	void setFacsimile(String value);

} // TelephoneType
