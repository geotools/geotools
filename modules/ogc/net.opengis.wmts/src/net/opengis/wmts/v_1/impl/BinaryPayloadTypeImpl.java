/**
 */
package net.opengis.wmts.v_1.impl;

import net.opengis.wmts.v_1.BinaryPayloadType;
import net.opengis.wmts.v_1.wmtsv_1Package;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Binary Payload Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wmts.v_1.impl.BinaryPayloadTypeImpl#getFormat <em>Format</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.BinaryPayloadTypeImpl#getBinaryContent <em>Binary Content</em>}</li>
 * </ul>
 *
 * @generated
 */
public class BinaryPayloadTypeImpl extends MinimalEObjectImpl.Container implements BinaryPayloadType {
    /**
     * The default value of the '{@link #getFormat() <em>Format</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFormat()
     * @generated
     * @ordered
     */
    protected static final String FORMAT_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getFormat() <em>Format</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFormat()
     * @generated
     * @ordered
     */
    protected String format = FORMAT_EDEFAULT;

    /**
     * The default value of the '{@link #getBinaryContent() <em>Binary Content</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getBinaryContent()
     * @generated
     * @ordered
     */
    protected static final byte[] BINARY_CONTENT_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getBinaryContent() <em>Binary Content</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getBinaryContent()
     * @generated
     * @ordered
     */
    protected byte[] binaryContent = BINARY_CONTENT_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected BinaryPayloadTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return wmtsv_1Package.Literals.BINARY_PAYLOAD_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getFormat() {
        return format;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setFormat(String newFormat) {
        String oldFormat = format;
        format = newFormat;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, wmtsv_1Package.BINARY_PAYLOAD_TYPE__FORMAT, oldFormat, format));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public byte[] getBinaryContent() {
        return binaryContent;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setBinaryContent(byte[] newBinaryContent) {
        byte[] oldBinaryContent = binaryContent;
        binaryContent = newBinaryContent;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, wmtsv_1Package.BINARY_PAYLOAD_TYPE__BINARY_CONTENT, oldBinaryContent, binaryContent));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case wmtsv_1Package.BINARY_PAYLOAD_TYPE__FORMAT:
                return getFormat();
            case wmtsv_1Package.BINARY_PAYLOAD_TYPE__BINARY_CONTENT:
                return getBinaryContent();
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
            case wmtsv_1Package.BINARY_PAYLOAD_TYPE__FORMAT:
                setFormat((String)newValue);
                return;
            case wmtsv_1Package.BINARY_PAYLOAD_TYPE__BINARY_CONTENT:
                setBinaryContent((byte[])newValue);
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
            case wmtsv_1Package.BINARY_PAYLOAD_TYPE__FORMAT:
                setFormat(FORMAT_EDEFAULT);
                return;
            case wmtsv_1Package.BINARY_PAYLOAD_TYPE__BINARY_CONTENT:
                setBinaryContent(BINARY_CONTENT_EDEFAULT);
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
            case wmtsv_1Package.BINARY_PAYLOAD_TYPE__FORMAT:
                return FORMAT_EDEFAULT == null ? format != null : !FORMAT_EDEFAULT.equals(format);
            case wmtsv_1Package.BINARY_PAYLOAD_TYPE__BINARY_CONTENT:
                return BINARY_CONTENT_EDEFAULT == null ? binaryContent != null : !BINARY_CONTENT_EDEFAULT.equals(binaryContent);
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

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (format: ");
        result.append(format);
        result.append(", binaryContent: ");
        result.append(binaryContent);
        result.append(')');
        return result.toString();
    }

} //BinaryPayloadTypeImpl
