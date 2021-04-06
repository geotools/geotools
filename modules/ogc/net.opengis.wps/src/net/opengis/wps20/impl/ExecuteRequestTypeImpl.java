/**
 */
package net.opengis.wps20.impl;

import java.util.Collection;

import net.opengis.ows20.CodeType;

import net.opengis.wps20.DataInputType;
import net.opengis.wps20.ExecuteRequestType;
import net.opengis.wps20.ModeType;
import net.opengis.wps20.OutputDefinitionType;
import net.opengis.wps20.ResponseType;
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
 * An implementation of the model object '<em><b>Execute Request Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wps20.impl.ExecuteRequestTypeImpl#getIdentifier <em>Identifier</em>}</li>
 *   <li>{@link net.opengis.wps20.impl.ExecuteRequestTypeImpl#getInput <em>Input</em>}</li>
 *   <li>{@link net.opengis.wps20.impl.ExecuteRequestTypeImpl#getOutput <em>Output</em>}</li>
 *   <li>{@link net.opengis.wps20.impl.ExecuteRequestTypeImpl#getMode <em>Mode</em>}</li>
 *   <li>{@link net.opengis.wps20.impl.ExecuteRequestTypeImpl#getResponse <em>Response</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ExecuteRequestTypeImpl extends RequestBaseTypeImpl implements ExecuteRequestType {
	/**
	 * The cached value of the '{@link #getIdentifier() <em>Identifier</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIdentifier()
	 * @generated
	 * @ordered
	 */
	protected CodeType identifier;

	/**
	 * The cached value of the '{@link #getInput() <em>Input</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInput()
	 * @generated
	 * @ordered
	 */
	protected EList<DataInputType> input;

	/**
	 * The cached value of the '{@link #getOutput() <em>Output</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOutput()
	 * @generated
	 * @ordered
	 */
	protected EList<OutputDefinitionType> output;

	/**
	 * The default value of the '{@link #getMode() <em>Mode</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMode()
	 * @generated
	 * @ordered
	 */
	protected static final ModeType MODE_EDEFAULT = ModeType.SYNC;

	/**
	 * The cached value of the '{@link #getMode() <em>Mode</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMode()
	 * @generated
	 * @ordered
	 */
	protected ModeType mode = MODE_EDEFAULT;

	/**
	 * This is true if the Mode attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean modeESet;

	/**
	 * The default value of the '{@link #getResponse() <em>Response</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getResponse()
	 * @generated
	 * @ordered
	 */
	protected static final ResponseType RESPONSE_EDEFAULT = ResponseType.RAW;

	/**
	 * The cached value of the '{@link #getResponse() <em>Response</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getResponse()
	 * @generated
	 * @ordered
	 */
	protected ResponseType response = RESPONSE_EDEFAULT;

	/**
	 * This is true if the Response attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean responseESet;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ExecuteRequestTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return Wps20Package.Literals.EXECUTE_REQUEST_TYPE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CodeType getIdentifier() {
		return identifier;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetIdentifier(CodeType newIdentifier, NotificationChain msgs) {
		CodeType oldIdentifier = identifier;
		identifier = newIdentifier;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wps20Package.EXECUTE_REQUEST_TYPE__IDENTIFIER, oldIdentifier, newIdentifier);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIdentifier(CodeType newIdentifier) {
		if (newIdentifier != identifier) {
			NotificationChain msgs = null;
			if (identifier != null)
				msgs = ((InternalEObject)identifier).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wps20Package.EXECUTE_REQUEST_TYPE__IDENTIFIER, null, msgs);
			if (newIdentifier != null)
				msgs = ((InternalEObject)newIdentifier).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wps20Package.EXECUTE_REQUEST_TYPE__IDENTIFIER, null, msgs);
			msgs = basicSetIdentifier(newIdentifier, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wps20Package.EXECUTE_REQUEST_TYPE__IDENTIFIER, newIdentifier, newIdentifier));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<DataInputType> getInput() {
		if (input == null) {
			input = new EObjectContainmentEList<DataInputType>(DataInputType.class, this, Wps20Package.EXECUTE_REQUEST_TYPE__INPUT);
		}
		return input;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<OutputDefinitionType> getOutput() {
		if (output == null) {
			output = new EObjectContainmentEList<OutputDefinitionType>(OutputDefinitionType.class, this, Wps20Package.EXECUTE_REQUEST_TYPE__OUTPUT);
		}
		return output;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ModeType getMode() {
		return mode;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMode(ModeType newMode) {
		ModeType oldMode = mode;
		mode = newMode == null ? MODE_EDEFAULT : newMode;
		boolean oldModeESet = modeESet;
		modeESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wps20Package.EXECUTE_REQUEST_TYPE__MODE, oldMode, mode, !oldModeESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetMode() {
		ModeType oldMode = mode;
		boolean oldModeESet = modeESet;
		mode = MODE_EDEFAULT;
		modeESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, Wps20Package.EXECUTE_REQUEST_TYPE__MODE, oldMode, MODE_EDEFAULT, oldModeESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetMode() {
		return modeESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ResponseType getResponse() {
		return response;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setResponse(ResponseType newResponse) {
		ResponseType oldResponse = response;
		response = newResponse == null ? RESPONSE_EDEFAULT : newResponse;
		boolean oldResponseESet = responseESet;
		responseESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wps20Package.EXECUTE_REQUEST_TYPE__RESPONSE, oldResponse, response, !oldResponseESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetResponse() {
		ResponseType oldResponse = response;
		boolean oldResponseESet = responseESet;
		response = RESPONSE_EDEFAULT;
		responseESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, Wps20Package.EXECUTE_REQUEST_TYPE__RESPONSE, oldResponse, RESPONSE_EDEFAULT, oldResponseESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetResponse() {
		return responseESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case Wps20Package.EXECUTE_REQUEST_TYPE__IDENTIFIER:
				return basicSetIdentifier(null, msgs);
			case Wps20Package.EXECUTE_REQUEST_TYPE__INPUT:
				return ((InternalEList<?>)getInput()).basicRemove(otherEnd, msgs);
			case Wps20Package.EXECUTE_REQUEST_TYPE__OUTPUT:
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
			case Wps20Package.EXECUTE_REQUEST_TYPE__IDENTIFIER:
				return getIdentifier();
			case Wps20Package.EXECUTE_REQUEST_TYPE__INPUT:
				return getInput();
			case Wps20Package.EXECUTE_REQUEST_TYPE__OUTPUT:
				return getOutput();
			case Wps20Package.EXECUTE_REQUEST_TYPE__MODE:
				return getMode();
			case Wps20Package.EXECUTE_REQUEST_TYPE__RESPONSE:
				return getResponse();
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
			case Wps20Package.EXECUTE_REQUEST_TYPE__IDENTIFIER:
				setIdentifier((CodeType)newValue);
				return;
			case Wps20Package.EXECUTE_REQUEST_TYPE__INPUT:
				getInput().clear();
				getInput().addAll((Collection<? extends DataInputType>)newValue);
				return;
			case Wps20Package.EXECUTE_REQUEST_TYPE__OUTPUT:
				getOutput().clear();
				getOutput().addAll((Collection<? extends OutputDefinitionType>)newValue);
				return;
			case Wps20Package.EXECUTE_REQUEST_TYPE__MODE:
				setMode((ModeType)newValue);
				return;
			case Wps20Package.EXECUTE_REQUEST_TYPE__RESPONSE:
				setResponse((ResponseType)newValue);
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
			case Wps20Package.EXECUTE_REQUEST_TYPE__IDENTIFIER:
				setIdentifier((CodeType)null);
				return;
			case Wps20Package.EXECUTE_REQUEST_TYPE__INPUT:
				getInput().clear();
				return;
			case Wps20Package.EXECUTE_REQUEST_TYPE__OUTPUT:
				getOutput().clear();
				return;
			case Wps20Package.EXECUTE_REQUEST_TYPE__MODE:
				unsetMode();
				return;
			case Wps20Package.EXECUTE_REQUEST_TYPE__RESPONSE:
				unsetResponse();
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
			case Wps20Package.EXECUTE_REQUEST_TYPE__IDENTIFIER:
				return identifier != null;
			case Wps20Package.EXECUTE_REQUEST_TYPE__INPUT:
				return input != null && !input.isEmpty();
			case Wps20Package.EXECUTE_REQUEST_TYPE__OUTPUT:
				return output != null && !output.isEmpty();
			case Wps20Package.EXECUTE_REQUEST_TYPE__MODE:
				return isSetMode();
			case Wps20Package.EXECUTE_REQUEST_TYPE__RESPONSE:
				return isSetResponse();
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
		result.append(" (mode: ");
		if (modeESet) result.append(mode); else result.append("<unset>");
		result.append(", response: ");
		if (responseESet) result.append(response); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}

} //ExecuteRequestTypeImpl
