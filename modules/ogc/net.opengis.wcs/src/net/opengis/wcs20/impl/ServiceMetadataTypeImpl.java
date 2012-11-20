/**
 */
package net.opengis.wcs20.impl;

import net.opengis.wcs20.ExtensionType;
import net.opengis.wcs20.ServiceMetadataType;
import net.opengis.wcs20.Wcs20Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Service Metadata Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs20.impl.ServiceMetadataTypeImpl#getFormatSupported <em>Format Supported</em>}</li>
 *   <li>{@link net.opengis.wcs20.impl.ServiceMetadataTypeImpl#getExtension <em>Extension</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ServiceMetadataTypeImpl extends EObjectImpl implements ServiceMetadataType {
    /**
     * The default value of the '{@link #getFormatSupported() <em>Format Supported</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFormatSupported()
     * @generated
     * @ordered
     */
    protected static final String FORMAT_SUPPORTED_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getFormatSupported() <em>Format Supported</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFormatSupported()
     * @generated
     * @ordered
     */
    protected String formatSupported = FORMAT_SUPPORTED_EDEFAULT;

    /**
     * The cached value of the '{@link #getExtension() <em>Extension</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getExtension()
     * @generated
     * @ordered
     */
    protected ExtensionType extension;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ServiceMetadataTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Wcs20Package.Literals.SERVICE_METADATA_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getFormatSupported() {
        return formatSupported;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setFormatSupported(String newFormatSupported) {
        String oldFormatSupported = formatSupported;
        formatSupported = newFormatSupported;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs20Package.SERVICE_METADATA_TYPE__FORMAT_SUPPORTED, oldFormatSupported, formatSupported));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ExtensionType getExtension() {
        return extension;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetExtension(ExtensionType newExtension, NotificationChain msgs) {
        ExtensionType oldExtension = extension;
        extension = newExtension;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wcs20Package.SERVICE_METADATA_TYPE__EXTENSION, oldExtension, newExtension);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setExtension(ExtensionType newExtension) {
        if (newExtension != extension) {
            NotificationChain msgs = null;
            if (extension != null)
                msgs = ((InternalEObject)extension).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wcs20Package.SERVICE_METADATA_TYPE__EXTENSION, null, msgs);
            if (newExtension != null)
                msgs = ((InternalEObject)newExtension).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wcs20Package.SERVICE_METADATA_TYPE__EXTENSION, null, msgs);
            msgs = basicSetExtension(newExtension, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs20Package.SERVICE_METADATA_TYPE__EXTENSION, newExtension, newExtension));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Wcs20Package.SERVICE_METADATA_TYPE__EXTENSION:
                return basicSetExtension(null, msgs);
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
            case Wcs20Package.SERVICE_METADATA_TYPE__FORMAT_SUPPORTED:
                return getFormatSupported();
            case Wcs20Package.SERVICE_METADATA_TYPE__EXTENSION:
                return getExtension();
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
            case Wcs20Package.SERVICE_METADATA_TYPE__FORMAT_SUPPORTED:
                setFormatSupported((String)newValue);
                return;
            case Wcs20Package.SERVICE_METADATA_TYPE__EXTENSION:
                setExtension((ExtensionType)newValue);
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
            case Wcs20Package.SERVICE_METADATA_TYPE__FORMAT_SUPPORTED:
                setFormatSupported(FORMAT_SUPPORTED_EDEFAULT);
                return;
            case Wcs20Package.SERVICE_METADATA_TYPE__EXTENSION:
                setExtension((ExtensionType)null);
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
            case Wcs20Package.SERVICE_METADATA_TYPE__FORMAT_SUPPORTED:
                return FORMAT_SUPPORTED_EDEFAULT == null ? formatSupported != null : !FORMAT_SUPPORTED_EDEFAULT.equals(formatSupported);
            case Wcs20Package.SERVICE_METADATA_TYPE__EXTENSION:
                return extension != null;
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
        result.append(" (formatSupported: ");
        result.append(formatSupported);
        result.append(')');
        return result.toString();
    }

} //ServiceMetadataTypeImpl
