/**
 */
package net.opengis.wps20.impl;

import java.util.Collection;

import net.opengis.wps20.InputDescriptionType;
import net.opengis.wps20.OutputDescriptionType;
import net.opengis.wps20.ProcessDescriptionType;
import net.opengis.wps20.Wps20Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Process Description Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wps20.impl.ProcessDescriptionTypeImpl#getInput <em>Input</em>}</li>
 *   <li>{@link net.opengis.wps20.impl.ProcessDescriptionTypeImpl#getOutput <em>Output</em>}</li>
 *   <li>{@link net.opengis.wps20.impl.ProcessDescriptionTypeImpl#getLang <em>Lang</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ProcessDescriptionTypeImpl extends DescriptionTypeImpl implements ProcessDescriptionType {
	/**
	 * The cached value of the '{@link #getInput() <em>Input</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInput()
	 * @generated
	 * @ordered
	 */
	protected EList<InputDescriptionType> input;

	/**
	 * The cached value of the '{@link #getOutput() <em>Output</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOutput()
	 * @generated
	 * @ordered
	 */
	protected EList<OutputDescriptionType> output;

	/**
	 * The default value of the '{@link #getLang() <em>Lang</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLang()
	 * @generated
	 * @ordered
	 */
	protected static final String LANG_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLang() <em>Lang</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLang()
	 * @generated
	 * @ordered
	 */
	protected String lang = LANG_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ProcessDescriptionTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return Wps20Package.Literals.PROCESS_DESCRIPTION_TYPE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<InputDescriptionType> getInput() {
		if (input == null) {
			input = new EObjectContainmentEList<InputDescriptionType>(InputDescriptionType.class, this, Wps20Package.PROCESS_DESCRIPTION_TYPE__INPUT);
		}
		return input;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<OutputDescriptionType> getOutput() {
		if (output == null) {
			output = new EObjectContainmentEList<OutputDescriptionType>(OutputDescriptionType.class, this, Wps20Package.PROCESS_DESCRIPTION_TYPE__OUTPUT);
		}
		return output;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getLang() {
		return lang;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLang(String newLang) {
		String oldLang = lang;
		lang = newLang;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wps20Package.PROCESS_DESCRIPTION_TYPE__LANG, oldLang, lang));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case Wps20Package.PROCESS_DESCRIPTION_TYPE__INPUT:
				return ((InternalEList<?>)getInput()).basicRemove(otherEnd, msgs);
			case Wps20Package.PROCESS_DESCRIPTION_TYPE__OUTPUT:
				return ((InternalEList<?>)getOutput()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case Wps20Package.PROCESS_DESCRIPTION_TYPE__INPUT:
				return getInput();
			case Wps20Package.PROCESS_DESCRIPTION_TYPE__OUTPUT:
				return getOutput();
			case Wps20Package.PROCESS_DESCRIPTION_TYPE__LANG:
				return getLang();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case Wps20Package.PROCESS_DESCRIPTION_TYPE__INPUT:
				getInput().clear();
				getInput().addAll((Collection<? extends InputDescriptionType>)newValue);
				return;
			case Wps20Package.PROCESS_DESCRIPTION_TYPE__OUTPUT:
				getOutput().clear();
				getOutput().addAll((Collection<? extends OutputDescriptionType>)newValue);
				return;
			case Wps20Package.PROCESS_DESCRIPTION_TYPE__LANG:
				setLang((String)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case Wps20Package.PROCESS_DESCRIPTION_TYPE__INPUT:
				getInput().clear();
				return;
			case Wps20Package.PROCESS_DESCRIPTION_TYPE__OUTPUT:
				getOutput().clear();
				return;
			case Wps20Package.PROCESS_DESCRIPTION_TYPE__LANG:
				setLang(LANG_EDEFAULT);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case Wps20Package.PROCESS_DESCRIPTION_TYPE__INPUT:
				return input != null && !input.isEmpty();
			case Wps20Package.PROCESS_DESCRIPTION_TYPE__OUTPUT:
				return output != null && !output.isEmpty();
			case Wps20Package.PROCESS_DESCRIPTION_TYPE__LANG:
				return LANG_EDEFAULT == null ? lang != null : !LANG_EDEFAULT.equals(lang);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (lang: ");
		result.append(lang);
		result.append(')');
		return result.toString();
	}

} //ProcessDescriptionTypeImpl
