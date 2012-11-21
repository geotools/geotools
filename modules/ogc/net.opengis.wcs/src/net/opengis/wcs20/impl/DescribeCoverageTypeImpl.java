/**
 */
package net.opengis.wcs20.impl;

import net.opengis.wcs20.DescribeCoverageType;
import net.opengis.wcs20.Wcs20Package;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Describe Coverage Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs20.impl.DescribeCoverageTypeImpl#getCoverageId <em>Coverage Id</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DescribeCoverageTypeImpl extends RequestBaseTypeImpl implements DescribeCoverageType {
    /**
     * The default value of the '{@link #getCoverageId() <em>Coverage Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCoverageId()
     * @generated
     * @ordered
     */
    protected static final String COVERAGE_ID_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getCoverageId() <em>Coverage Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCoverageId()
     * @generated
     * @ordered
     */
    protected String coverageId = COVERAGE_ID_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected DescribeCoverageTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Wcs20Package.Literals.DESCRIBE_COVERAGE_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getCoverageId() {
        return coverageId;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setCoverageId(String newCoverageId) {
        String oldCoverageId = coverageId;
        coverageId = newCoverageId;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs20Package.DESCRIBE_COVERAGE_TYPE__COVERAGE_ID, oldCoverageId, coverageId));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Wcs20Package.DESCRIBE_COVERAGE_TYPE__COVERAGE_ID:
                return getCoverageId();
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
            case Wcs20Package.DESCRIBE_COVERAGE_TYPE__COVERAGE_ID:
                setCoverageId((String)newValue);
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
            case Wcs20Package.DESCRIBE_COVERAGE_TYPE__COVERAGE_ID:
                setCoverageId(COVERAGE_ID_EDEFAULT);
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
            case Wcs20Package.DESCRIBE_COVERAGE_TYPE__COVERAGE_ID:
                return COVERAGE_ID_EDEFAULT == null ? coverageId != null : !COVERAGE_ID_EDEFAULT.equals(coverageId);
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
        result.append(" (coverageId: ");
        result.append(coverageId);
        result.append(')');
        return result.toString();
    }

} //DescribeCoverageTypeImpl
