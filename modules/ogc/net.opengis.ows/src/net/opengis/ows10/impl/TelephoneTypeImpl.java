/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.ows10.impl;

import net.opengis.ows10.Ows10Package;
import net.opengis.ows10.TelephoneType;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Telephone Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.ows10.impl.TelephoneTypeImpl#getVoice <em>Voice</em>}</li>
 *   <li>{@link net.opengis.ows10.impl.TelephoneTypeImpl#getFacsimile <em>Facsimile</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TelephoneTypeImpl extends EObjectImpl implements TelephoneType {
	/**
	 * The default value of the '{@link #getVoice() <em>Voice</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVoice()
	 * @generated
	 * @ordered
	 */
	protected static final String VOICE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getVoice() <em>Voice</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVoice()
	 * @generated
	 * @ordered
	 */
	protected String voice = VOICE_EDEFAULT;

	/**
	 * The default value of the '{@link #getFacsimile() <em>Facsimile</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFacsimile()
	 * @generated
	 * @ordered
	 */
	protected static final String FACSIMILE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getFacsimile() <em>Facsimile</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFacsimile()
	 * @generated
	 * @ordered
	 */
	protected String facsimile = FACSIMILE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TelephoneTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return Ows10Package.eINSTANCE.getTelephoneType();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getVoice() {
		return voice;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVoice(String newVoice) {
		String oldVoice = voice;
		voice = newVoice;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Ows10Package.TELEPHONE_TYPE__VOICE, oldVoice, voice));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getFacsimile() {
		return facsimile;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFacsimile(String newFacsimile) {
		String oldFacsimile = facsimile;
		facsimile = newFacsimile;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Ows10Package.TELEPHONE_TYPE__FACSIMILE, oldFacsimile, facsimile));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case Ows10Package.TELEPHONE_TYPE__VOICE:
				return getVoice();
			case Ows10Package.TELEPHONE_TYPE__FACSIMILE:
				return getFacsimile();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case Ows10Package.TELEPHONE_TYPE__VOICE:
				setVoice((String)newValue);
				return;
			case Ows10Package.TELEPHONE_TYPE__FACSIMILE:
				setFacsimile((String)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eUnset(int featureID) {
		switch (featureID) {
			case Ows10Package.TELEPHONE_TYPE__VOICE:
				setVoice(VOICE_EDEFAULT);
				return;
			case Ows10Package.TELEPHONE_TYPE__FACSIMILE:
				setFacsimile(FACSIMILE_EDEFAULT);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case Ows10Package.TELEPHONE_TYPE__VOICE:
				return VOICE_EDEFAULT == null ? voice != null : !VOICE_EDEFAULT.equals(voice);
			case Ows10Package.TELEPHONE_TYPE__FACSIMILE:
				return FACSIMILE_EDEFAULT == null ? facsimile != null : !FACSIMILE_EDEFAULT.equals(facsimile);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (voice: ");
		result.append(voice);
		result.append(", facsimile: ");
		result.append(facsimile);
		result.append(')');
		return result.toString();
	}

} //TelephoneTypeImpl
