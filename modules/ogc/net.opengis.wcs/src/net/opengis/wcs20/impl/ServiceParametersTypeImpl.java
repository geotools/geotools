/**
 */
package net.opengis.wcs20.impl;

import javax.xml.namespace.QName;

import net.opengis.wcs20.CoverageSubtypeParentType;
import net.opengis.wcs20.ExtensionType;
import net.opengis.wcs20.ServiceParametersType;
import net.opengis.wcs20.Wcs20Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Service Parameters Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs20.impl.ServiceParametersTypeImpl#getCoverageSubtype <em>Coverage Subtype</em>}</li>
 *   <li>{@link net.opengis.wcs20.impl.ServiceParametersTypeImpl#getCoverageSubtypeParent <em>Coverage Subtype Parent</em>}</li>
 *   <li>{@link net.opengis.wcs20.impl.ServiceParametersTypeImpl#getNativeFormat <em>Native Format</em>}</li>
 *   <li>{@link net.opengis.wcs20.impl.ServiceParametersTypeImpl#getExtension <em>Extension</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ServiceParametersTypeImpl extends EObjectImpl implements ServiceParametersType {
    /**
     * The default value of the '{@link #getCoverageSubtype() <em>Coverage Subtype</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCoverageSubtype()
     * @generated
     * @ordered
     */
    protected static final QName COVERAGE_SUBTYPE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getCoverageSubtype() <em>Coverage Subtype</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCoverageSubtype()
     * @generated
     * @ordered
     */
    protected QName coverageSubtype = COVERAGE_SUBTYPE_EDEFAULT;

    /**
     * The cached value of the '{@link #getCoverageSubtypeParent() <em>Coverage Subtype Parent</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCoverageSubtypeParent()
     * @generated
     * @ordered
     */
    protected CoverageSubtypeParentType coverageSubtypeParent;

    /**
     * The default value of the '{@link #getNativeFormat() <em>Native Format</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getNativeFormat()
     * @generated
     * @ordered
     */
    protected static final String NATIVE_FORMAT_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getNativeFormat() <em>Native Format</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getNativeFormat()
     * @generated
     * @ordered
     */
    protected String nativeFormat = NATIVE_FORMAT_EDEFAULT;

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
    protected ServiceParametersTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Wcs20Package.Literals.SERVICE_PARAMETERS_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public QName getCoverageSubtype() {
        return coverageSubtype;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setCoverageSubtype(QName newCoverageSubtype) {
        QName oldCoverageSubtype = coverageSubtype;
        coverageSubtype = newCoverageSubtype;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs20Package.SERVICE_PARAMETERS_TYPE__COVERAGE_SUBTYPE, oldCoverageSubtype, coverageSubtype));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CoverageSubtypeParentType getCoverageSubtypeParent() {
        return coverageSubtypeParent;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetCoverageSubtypeParent(CoverageSubtypeParentType newCoverageSubtypeParent, NotificationChain msgs) {
        CoverageSubtypeParentType oldCoverageSubtypeParent = coverageSubtypeParent;
        coverageSubtypeParent = newCoverageSubtypeParent;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wcs20Package.SERVICE_PARAMETERS_TYPE__COVERAGE_SUBTYPE_PARENT, oldCoverageSubtypeParent, newCoverageSubtypeParent);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setCoverageSubtypeParent(CoverageSubtypeParentType newCoverageSubtypeParent) {
        if (newCoverageSubtypeParent != coverageSubtypeParent) {
            NotificationChain msgs = null;
            if (coverageSubtypeParent != null)
                msgs = ((InternalEObject)coverageSubtypeParent).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wcs20Package.SERVICE_PARAMETERS_TYPE__COVERAGE_SUBTYPE_PARENT, null, msgs);
            if (newCoverageSubtypeParent != null)
                msgs = ((InternalEObject)newCoverageSubtypeParent).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wcs20Package.SERVICE_PARAMETERS_TYPE__COVERAGE_SUBTYPE_PARENT, null, msgs);
            msgs = basicSetCoverageSubtypeParent(newCoverageSubtypeParent, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs20Package.SERVICE_PARAMETERS_TYPE__COVERAGE_SUBTYPE_PARENT, newCoverageSubtypeParent, newCoverageSubtypeParent));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getNativeFormat() {
        return nativeFormat;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setNativeFormat(String newNativeFormat) {
        String oldNativeFormat = nativeFormat;
        nativeFormat = newNativeFormat;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs20Package.SERVICE_PARAMETERS_TYPE__NATIVE_FORMAT, oldNativeFormat, nativeFormat));
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
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wcs20Package.SERVICE_PARAMETERS_TYPE__EXTENSION, oldExtension, newExtension);
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
                msgs = ((InternalEObject)extension).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wcs20Package.SERVICE_PARAMETERS_TYPE__EXTENSION, null, msgs);
            if (newExtension != null)
                msgs = ((InternalEObject)newExtension).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wcs20Package.SERVICE_PARAMETERS_TYPE__EXTENSION, null, msgs);
            msgs = basicSetExtension(newExtension, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs20Package.SERVICE_PARAMETERS_TYPE__EXTENSION, newExtension, newExtension));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Wcs20Package.SERVICE_PARAMETERS_TYPE__COVERAGE_SUBTYPE_PARENT:
                return basicSetCoverageSubtypeParent(null, msgs);
            case Wcs20Package.SERVICE_PARAMETERS_TYPE__EXTENSION:
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
            case Wcs20Package.SERVICE_PARAMETERS_TYPE__COVERAGE_SUBTYPE:
                return getCoverageSubtype();
            case Wcs20Package.SERVICE_PARAMETERS_TYPE__COVERAGE_SUBTYPE_PARENT:
                return getCoverageSubtypeParent();
            case Wcs20Package.SERVICE_PARAMETERS_TYPE__NATIVE_FORMAT:
                return getNativeFormat();
            case Wcs20Package.SERVICE_PARAMETERS_TYPE__EXTENSION:
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
            case Wcs20Package.SERVICE_PARAMETERS_TYPE__COVERAGE_SUBTYPE:
                setCoverageSubtype((QName)newValue);
                return;
            case Wcs20Package.SERVICE_PARAMETERS_TYPE__COVERAGE_SUBTYPE_PARENT:
                setCoverageSubtypeParent((CoverageSubtypeParentType)newValue);
                return;
            case Wcs20Package.SERVICE_PARAMETERS_TYPE__NATIVE_FORMAT:
                setNativeFormat((String)newValue);
                return;
            case Wcs20Package.SERVICE_PARAMETERS_TYPE__EXTENSION:
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
            case Wcs20Package.SERVICE_PARAMETERS_TYPE__COVERAGE_SUBTYPE:
                setCoverageSubtype(COVERAGE_SUBTYPE_EDEFAULT);
                return;
            case Wcs20Package.SERVICE_PARAMETERS_TYPE__COVERAGE_SUBTYPE_PARENT:
                setCoverageSubtypeParent((CoverageSubtypeParentType)null);
                return;
            case Wcs20Package.SERVICE_PARAMETERS_TYPE__NATIVE_FORMAT:
                setNativeFormat(NATIVE_FORMAT_EDEFAULT);
                return;
            case Wcs20Package.SERVICE_PARAMETERS_TYPE__EXTENSION:
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
            case Wcs20Package.SERVICE_PARAMETERS_TYPE__COVERAGE_SUBTYPE:
                return COVERAGE_SUBTYPE_EDEFAULT == null ? coverageSubtype != null : !COVERAGE_SUBTYPE_EDEFAULT.equals(coverageSubtype);
            case Wcs20Package.SERVICE_PARAMETERS_TYPE__COVERAGE_SUBTYPE_PARENT:
                return coverageSubtypeParent != null;
            case Wcs20Package.SERVICE_PARAMETERS_TYPE__NATIVE_FORMAT:
                return NATIVE_FORMAT_EDEFAULT == null ? nativeFormat != null : !NATIVE_FORMAT_EDEFAULT.equals(nativeFormat);
            case Wcs20Package.SERVICE_PARAMETERS_TYPE__EXTENSION:
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
        result.append(" (coverageSubtype: ");
        result.append(coverageSubtype);
        result.append(", nativeFormat: ");
        result.append(nativeFormat);
        result.append(')');
        return result.toString();
    }

} //ServiceParametersTypeImpl
