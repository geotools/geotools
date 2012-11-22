/**
 */
package net.opengis.wcs20.impl;

import javax.xml.namespace.QName;

import net.opengis.wcs20.CoverageSubtypeParentType;
import net.opengis.wcs20.Wcs20Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Coverage Subtype Parent Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs20.impl.CoverageSubtypeParentTypeImpl#getCoverageSubtype <em>Coverage Subtype</em>}</li>
 *   <li>{@link net.opengis.wcs20.impl.CoverageSubtypeParentTypeImpl#getCoverageSubtypeParent <em>Coverage Subtype Parent</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CoverageSubtypeParentTypeImpl extends EObjectImpl implements CoverageSubtypeParentType {
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
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected CoverageSubtypeParentTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Wcs20Package.Literals.COVERAGE_SUBTYPE_PARENT_TYPE;
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
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs20Package.COVERAGE_SUBTYPE_PARENT_TYPE__COVERAGE_SUBTYPE, oldCoverageSubtype, coverageSubtype));
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
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wcs20Package.COVERAGE_SUBTYPE_PARENT_TYPE__COVERAGE_SUBTYPE_PARENT, oldCoverageSubtypeParent, newCoverageSubtypeParent);
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
                msgs = ((InternalEObject)coverageSubtypeParent).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wcs20Package.COVERAGE_SUBTYPE_PARENT_TYPE__COVERAGE_SUBTYPE_PARENT, null, msgs);
            if (newCoverageSubtypeParent != null)
                msgs = ((InternalEObject)newCoverageSubtypeParent).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wcs20Package.COVERAGE_SUBTYPE_PARENT_TYPE__COVERAGE_SUBTYPE_PARENT, null, msgs);
            msgs = basicSetCoverageSubtypeParent(newCoverageSubtypeParent, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs20Package.COVERAGE_SUBTYPE_PARENT_TYPE__COVERAGE_SUBTYPE_PARENT, newCoverageSubtypeParent, newCoverageSubtypeParent));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Wcs20Package.COVERAGE_SUBTYPE_PARENT_TYPE__COVERAGE_SUBTYPE_PARENT:
                return basicSetCoverageSubtypeParent(null, msgs);
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
            case Wcs20Package.COVERAGE_SUBTYPE_PARENT_TYPE__COVERAGE_SUBTYPE:
                return getCoverageSubtype();
            case Wcs20Package.COVERAGE_SUBTYPE_PARENT_TYPE__COVERAGE_SUBTYPE_PARENT:
                return getCoverageSubtypeParent();
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
            case Wcs20Package.COVERAGE_SUBTYPE_PARENT_TYPE__COVERAGE_SUBTYPE:
                setCoverageSubtype((QName)newValue);
                return;
            case Wcs20Package.COVERAGE_SUBTYPE_PARENT_TYPE__COVERAGE_SUBTYPE_PARENT:
                setCoverageSubtypeParent((CoverageSubtypeParentType)newValue);
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
            case Wcs20Package.COVERAGE_SUBTYPE_PARENT_TYPE__COVERAGE_SUBTYPE:
                setCoverageSubtype(COVERAGE_SUBTYPE_EDEFAULT);
                return;
            case Wcs20Package.COVERAGE_SUBTYPE_PARENT_TYPE__COVERAGE_SUBTYPE_PARENT:
                setCoverageSubtypeParent((CoverageSubtypeParentType)null);
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
            case Wcs20Package.COVERAGE_SUBTYPE_PARENT_TYPE__COVERAGE_SUBTYPE:
                return COVERAGE_SUBTYPE_EDEFAULT == null ? coverageSubtype != null : !COVERAGE_SUBTYPE_EDEFAULT.equals(coverageSubtype);
            case Wcs20Package.COVERAGE_SUBTYPE_PARENT_TYPE__COVERAGE_SUBTYPE_PARENT:
                return coverageSubtypeParent != null;
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
        result.append(')');
        return result.toString();
    }

} //CoverageSubtypeParentTypeImpl
