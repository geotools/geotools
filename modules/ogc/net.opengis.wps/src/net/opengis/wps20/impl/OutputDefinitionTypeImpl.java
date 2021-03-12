/**
 */
package net.opengis.wps20.impl;

import net.opengis.wps20.DataTransmissionModeType;
import net.opengis.wps20.OutputDefinitionType;
import net.opengis.wps20.Wps20Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Output Definition Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wps20.impl.OutputDefinitionTypeImpl#getOutput <em>Output</em>}</li>
 *   <li>{@link net.opengis.wps20.impl.OutputDefinitionTypeImpl#getEncoding <em>Encoding</em>}</li>
 *   <li>{@link net.opengis.wps20.impl.OutputDefinitionTypeImpl#getId <em>Id</em>}</li>
 *   <li>{@link net.opengis.wps20.impl.OutputDefinitionTypeImpl#getMimeType <em>Mime Type</em>}</li>
 *   <li>{@link net.opengis.wps20.impl.OutputDefinitionTypeImpl#getSchema <em>Schema</em>}</li>
 *   <li>{@link net.opengis.wps20.impl.OutputDefinitionTypeImpl#getTransmission <em>Transmission</em>}</li>
 * </ul>
 *
 * @generated
 */
public class OutputDefinitionTypeImpl extends MinimalEObjectImpl.Container implements OutputDefinitionType {
	/**
	 * The cached value of the '{@link #getOutput() <em>Output</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOutput()
	 * @generated
	 * @ordered
	 */
	protected OutputDefinitionType output;

	/**
	 * The default value of the '{@link #getEncoding() <em>Encoding</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEncoding()
	 * @generated
	 * @ordered
	 */
	protected static final String ENCODING_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getEncoding() <em>Encoding</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEncoding()
	 * @generated
	 * @ordered
	 */
	protected String encoding = ENCODING_EDEFAULT;

	/**
	 * The default value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected static final String ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected String id = ID_EDEFAULT;

	/**
	 * The default value of the '{@link #getMimeType() <em>Mime Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMimeType()
	 * @generated
	 * @ordered
	 */
	protected static final String MIME_TYPE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getMimeType() <em>Mime Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMimeType()
	 * @generated
	 * @ordered
	 */
	protected String mimeType = MIME_TYPE_EDEFAULT;

	/**
	 * The default value of the '{@link #getSchema() <em>Schema</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSchema()
	 * @generated
	 * @ordered
	 */
	protected static final String SCHEMA_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getSchema() <em>Schema</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSchema()
	 * @generated
	 * @ordered
	 */
	protected String schema = SCHEMA_EDEFAULT;

	/**
	 * The default value of the '{@link #getTransmission() <em>Transmission</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTransmission()
	 * @generated
	 * @ordered
	 */
	protected static final DataTransmissionModeType TRANSMISSION_EDEFAULT = DataTransmissionModeType.VALUE;

	/**
	 * The cached value of the '{@link #getTransmission() <em>Transmission</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTransmission()
	 * @generated
	 * @ordered
	 */
	protected DataTransmissionModeType transmission = TRANSMISSION_EDEFAULT;

	/**
	 * This is true if the Transmission attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean transmissionESet;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected OutputDefinitionTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return Wps20Package.Literals.OUTPUT_DEFINITION_TYPE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OutputDefinitionType getOutput() {
		return output;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetOutput(OutputDefinitionType newOutput, NotificationChain msgs) {
		OutputDefinitionType oldOutput = output;
		output = newOutput;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wps20Package.OUTPUT_DEFINITION_TYPE__OUTPUT, oldOutput, newOutput);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOutput(OutputDefinitionType newOutput) {
		if (newOutput != output) {
			NotificationChain msgs = null;
			if (output != null)
				msgs = ((InternalEObject)output).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wps20Package.OUTPUT_DEFINITION_TYPE__OUTPUT, null, msgs);
			if (newOutput != null)
				msgs = ((InternalEObject)newOutput).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wps20Package.OUTPUT_DEFINITION_TYPE__OUTPUT, null, msgs);
			msgs = basicSetOutput(newOutput, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wps20Package.OUTPUT_DEFINITION_TYPE__OUTPUT, newOutput, newOutput));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getEncoding() {
		return encoding;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setEncoding(String newEncoding) {
		String oldEncoding = encoding;
		encoding = newEncoding;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wps20Package.OUTPUT_DEFINITION_TYPE__ENCODING, oldEncoding, encoding));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getId() {
		return id;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setId(String newId) {
		String oldId = id;
		id = newId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wps20Package.OUTPUT_DEFINITION_TYPE__ID, oldId, id));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getMimeType() {
		return mimeType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMimeType(String newMimeType) {
		String oldMimeType = mimeType;
		mimeType = newMimeType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wps20Package.OUTPUT_DEFINITION_TYPE__MIME_TYPE, oldMimeType, mimeType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getSchema() {
		return schema;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSchema(String newSchema) {
		String oldSchema = schema;
		schema = newSchema;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wps20Package.OUTPUT_DEFINITION_TYPE__SCHEMA, oldSchema, schema));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DataTransmissionModeType getTransmission() {
		return transmission;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTransmission(DataTransmissionModeType newTransmission) {
		DataTransmissionModeType oldTransmission = transmission;
		transmission = newTransmission == null ? TRANSMISSION_EDEFAULT : newTransmission;
		boolean oldTransmissionESet = transmissionESet;
		transmissionESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wps20Package.OUTPUT_DEFINITION_TYPE__TRANSMISSION, oldTransmission, transmission, !oldTransmissionESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetTransmission() {
		DataTransmissionModeType oldTransmission = transmission;
		boolean oldTransmissionESet = transmissionESet;
		transmission = TRANSMISSION_EDEFAULT;
		transmissionESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, Wps20Package.OUTPUT_DEFINITION_TYPE__TRANSMISSION, oldTransmission, TRANSMISSION_EDEFAULT, oldTransmissionESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetTransmission() {
		return transmissionESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case Wps20Package.OUTPUT_DEFINITION_TYPE__OUTPUT:
				return basicSetOutput(null, msgs);
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
			case Wps20Package.OUTPUT_DEFINITION_TYPE__OUTPUT:
				return getOutput();
			case Wps20Package.OUTPUT_DEFINITION_TYPE__ENCODING:
				return getEncoding();
			case Wps20Package.OUTPUT_DEFINITION_TYPE__ID:
				return getId();
			case Wps20Package.OUTPUT_DEFINITION_TYPE__MIME_TYPE:
				return getMimeType();
			case Wps20Package.OUTPUT_DEFINITION_TYPE__SCHEMA:
				return getSchema();
			case Wps20Package.OUTPUT_DEFINITION_TYPE__TRANSMISSION:
				return getTransmission();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case Wps20Package.OUTPUT_DEFINITION_TYPE__OUTPUT:
				setOutput((OutputDefinitionType)newValue);
				return;
			case Wps20Package.OUTPUT_DEFINITION_TYPE__ENCODING:
				setEncoding((String)newValue);
				return;
			case Wps20Package.OUTPUT_DEFINITION_TYPE__ID:
				setId((String)newValue);
				return;
			case Wps20Package.OUTPUT_DEFINITION_TYPE__MIME_TYPE:
				setMimeType((String)newValue);
				return;
			case Wps20Package.OUTPUT_DEFINITION_TYPE__SCHEMA:
				setSchema((String)newValue);
				return;
			case Wps20Package.OUTPUT_DEFINITION_TYPE__TRANSMISSION:
				setTransmission((DataTransmissionModeType)newValue);
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
			case Wps20Package.OUTPUT_DEFINITION_TYPE__OUTPUT:
				setOutput((OutputDefinitionType)null);
				return;
			case Wps20Package.OUTPUT_DEFINITION_TYPE__ENCODING:
				setEncoding(ENCODING_EDEFAULT);
				return;
			case Wps20Package.OUTPUT_DEFINITION_TYPE__ID:
				setId(ID_EDEFAULT);
				return;
			case Wps20Package.OUTPUT_DEFINITION_TYPE__MIME_TYPE:
				setMimeType(MIME_TYPE_EDEFAULT);
				return;
			case Wps20Package.OUTPUT_DEFINITION_TYPE__SCHEMA:
				setSchema(SCHEMA_EDEFAULT);
				return;
			case Wps20Package.OUTPUT_DEFINITION_TYPE__TRANSMISSION:
				unsetTransmission();
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
			case Wps20Package.OUTPUT_DEFINITION_TYPE__OUTPUT:
				return output != null;
			case Wps20Package.OUTPUT_DEFINITION_TYPE__ENCODING:
				return ENCODING_EDEFAULT == null ? encoding != null : !ENCODING_EDEFAULT.equals(encoding);
			case Wps20Package.OUTPUT_DEFINITION_TYPE__ID:
				return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
			case Wps20Package.OUTPUT_DEFINITION_TYPE__MIME_TYPE:
				return MIME_TYPE_EDEFAULT == null ? mimeType != null : !MIME_TYPE_EDEFAULT.equals(mimeType);
			case Wps20Package.OUTPUT_DEFINITION_TYPE__SCHEMA:
				return SCHEMA_EDEFAULT == null ? schema != null : !SCHEMA_EDEFAULT.equals(schema);
			case Wps20Package.OUTPUT_DEFINITION_TYPE__TRANSMISSION:
				return isSetTransmission();
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
		result.append(" (encoding: ");
		result.append(encoding);
		result.append(", id: ");
		result.append(id);
		result.append(", mimeType: ");
		result.append(mimeType);
		result.append(", schema: ");
		result.append(schema);
		result.append(", transmission: ");
		if (transmissionESet) result.append(transmission); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}

} //OutputDefinitionTypeImpl
