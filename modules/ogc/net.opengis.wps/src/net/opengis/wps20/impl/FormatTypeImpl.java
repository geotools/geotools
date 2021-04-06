/**
 */
package net.opengis.wps20.impl;

import java.math.BigInteger;

import net.opengis.wps20.FormatType;
import net.opengis.wps20.Wps20Package;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Format Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wps20.impl.FormatTypeImpl#isDefault <em>Default</em>}</li>
 *   <li>{@link net.opengis.wps20.impl.FormatTypeImpl#getEncoding <em>Encoding</em>}</li>
 *   <li>{@link net.opengis.wps20.impl.FormatTypeImpl#getMaximumMegabytes <em>Maximum Megabytes</em>}</li>
 *   <li>{@link net.opengis.wps20.impl.FormatTypeImpl#getMimeType <em>Mime Type</em>}</li>
 *   <li>{@link net.opengis.wps20.impl.FormatTypeImpl#getSchema <em>Schema</em>}</li>
 * </ul>
 *
 * @generated
 */
public class FormatTypeImpl extends MinimalEObjectImpl.Container implements FormatType {
	/**
	 * The default value of the '{@link #isDefault() <em>Default</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isDefault()
	 * @generated
	 * @ordered
	 */
	protected static final boolean DEFAULT_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isDefault() <em>Default</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isDefault()
	 * @generated
	 * @ordered
	 */
	protected boolean default_ = DEFAULT_EDEFAULT;

	/**
	 * This is true if the Default attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean defaultESet;

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
	 * The default value of the '{@link #getMaximumMegabytes() <em>Maximum Megabytes</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaximumMegabytes()
	 * @generated
	 * @ordered
	 */
	protected static final BigInteger MAXIMUM_MEGABYTES_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getMaximumMegabytes() <em>Maximum Megabytes</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaximumMegabytes()
	 * @generated
	 * @ordered
	 */
	protected BigInteger maximumMegabytes = MAXIMUM_MEGABYTES_EDEFAULT;

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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected FormatTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return Wps20Package.Literals.FORMAT_TYPE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isDefault() {
		return default_;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDefault(boolean newDefault) {
		boolean oldDefault = default_;
		default_ = newDefault;
		boolean oldDefaultESet = defaultESet;
		defaultESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wps20Package.FORMAT_TYPE__DEFAULT, oldDefault, default_, !oldDefaultESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetDefault() {
		boolean oldDefault = default_;
		boolean oldDefaultESet = defaultESet;
		default_ = DEFAULT_EDEFAULT;
		defaultESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, Wps20Package.FORMAT_TYPE__DEFAULT, oldDefault, DEFAULT_EDEFAULT, oldDefaultESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetDefault() {
		return defaultESet;
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
			eNotify(new ENotificationImpl(this, Notification.SET, Wps20Package.FORMAT_TYPE__ENCODING, oldEncoding, encoding));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BigInteger getMaximumMegabytes() {
		return maximumMegabytes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMaximumMegabytes(BigInteger newMaximumMegabytes) {
		BigInteger oldMaximumMegabytes = maximumMegabytes;
		maximumMegabytes = newMaximumMegabytes;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wps20Package.FORMAT_TYPE__MAXIMUM_MEGABYTES, oldMaximumMegabytes, maximumMegabytes));
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
			eNotify(new ENotificationImpl(this, Notification.SET, Wps20Package.FORMAT_TYPE__MIME_TYPE, oldMimeType, mimeType));
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
			eNotify(new ENotificationImpl(this, Notification.SET, Wps20Package.FORMAT_TYPE__SCHEMA, oldSchema, schema));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case Wps20Package.FORMAT_TYPE__DEFAULT:
				return isDefault();
			case Wps20Package.FORMAT_TYPE__ENCODING:
				return getEncoding();
			case Wps20Package.FORMAT_TYPE__MAXIMUM_MEGABYTES:
				return getMaximumMegabytes();
			case Wps20Package.FORMAT_TYPE__MIME_TYPE:
				return getMimeType();
			case Wps20Package.FORMAT_TYPE__SCHEMA:
				return getSchema();
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
			case Wps20Package.FORMAT_TYPE__DEFAULT:
				setDefault((Boolean)newValue);
				return;
			case Wps20Package.FORMAT_TYPE__ENCODING:
				setEncoding((String)newValue);
				return;
			case Wps20Package.FORMAT_TYPE__MAXIMUM_MEGABYTES:
				setMaximumMegabytes((BigInteger)newValue);
				return;
			case Wps20Package.FORMAT_TYPE__MIME_TYPE:
				setMimeType((String)newValue);
				return;
			case Wps20Package.FORMAT_TYPE__SCHEMA:
				setSchema((String)newValue);
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
			case Wps20Package.FORMAT_TYPE__DEFAULT:
				unsetDefault();
				return;
			case Wps20Package.FORMAT_TYPE__ENCODING:
				setEncoding(ENCODING_EDEFAULT);
				return;
			case Wps20Package.FORMAT_TYPE__MAXIMUM_MEGABYTES:
				setMaximumMegabytes(MAXIMUM_MEGABYTES_EDEFAULT);
				return;
			case Wps20Package.FORMAT_TYPE__MIME_TYPE:
				setMimeType(MIME_TYPE_EDEFAULT);
				return;
			case Wps20Package.FORMAT_TYPE__SCHEMA:
				setSchema(SCHEMA_EDEFAULT);
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
			case Wps20Package.FORMAT_TYPE__DEFAULT:
				return isSetDefault();
			case Wps20Package.FORMAT_TYPE__ENCODING:
				return ENCODING_EDEFAULT == null ? encoding != null : !ENCODING_EDEFAULT.equals(encoding);
			case Wps20Package.FORMAT_TYPE__MAXIMUM_MEGABYTES:
				return MAXIMUM_MEGABYTES_EDEFAULT == null ? maximumMegabytes != null : !MAXIMUM_MEGABYTES_EDEFAULT.equals(maximumMegabytes);
			case Wps20Package.FORMAT_TYPE__MIME_TYPE:
				return MIME_TYPE_EDEFAULT == null ? mimeType != null : !MIME_TYPE_EDEFAULT.equals(mimeType);
			case Wps20Package.FORMAT_TYPE__SCHEMA:
				return SCHEMA_EDEFAULT == null ? schema != null : !SCHEMA_EDEFAULT.equals(schema);
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
		result.append(" (default: ");
		if (defaultESet) result.append(default_); else result.append("<unset>");
		result.append(", encoding: ");
		result.append(encoding);
		result.append(", maximumMegabytes: ");
		result.append(maximumMegabytes);
		result.append(", mimeType: ");
		result.append(mimeType);
		result.append(", schema: ");
		result.append(schema);
		result.append(')');
		return result.toString();
	}

} //FormatTypeImpl
