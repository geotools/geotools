/**
 */
package net.opengis.wcs20.impl;

import net.opengis.wcs20.DimensionSubsetType;
import net.opengis.wcs20.Wcs20Package;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Dimension Subset Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs20.impl.DimensionSubsetTypeImpl#getDimension <em>Dimension</em>}</li>
 *   <li>{@link net.opengis.wcs20.impl.DimensionSubsetTypeImpl#getCRS <em>CRS</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class DimensionSubsetTypeImpl extends EObjectImpl implements DimensionSubsetType {
    /**
     * The default value of the '{@link #getDimension() <em>Dimension</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDimension()
     * @generated
     * @ordered
     */
    protected static final String DIMENSION_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getDimension() <em>Dimension</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDimension()
     * @generated
     * @ordered
     */
    protected String dimension = DIMENSION_EDEFAULT;

    /**
     * The default value of the '{@link #getCRS() <em>CRS</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCRS()
     * @generated
     * @ordered
     */
    protected static final String CRS_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getCRS() <em>CRS</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCRS()
     * @generated
     * @ordered
     */
    protected String cRS = CRS_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected DimensionSubsetTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Wcs20Package.Literals.DIMENSION_SUBSET_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getDimension() {
        return dimension;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDimension(String newDimension) {
        String oldDimension = dimension;
        dimension = newDimension;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs20Package.DIMENSION_SUBSET_TYPE__DIMENSION, oldDimension, dimension));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getCRS() {
        return cRS;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setCRS(String newCRS) {
        String oldCRS = cRS;
        cRS = newCRS;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs20Package.DIMENSION_SUBSET_TYPE__CRS, oldCRS, cRS));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Wcs20Package.DIMENSION_SUBSET_TYPE__DIMENSION:
                return getDimension();
            case Wcs20Package.DIMENSION_SUBSET_TYPE__CRS:
                return getCRS();
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
            case Wcs20Package.DIMENSION_SUBSET_TYPE__DIMENSION:
                setDimension((String)newValue);
                return;
            case Wcs20Package.DIMENSION_SUBSET_TYPE__CRS:
                setCRS((String)newValue);
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
            case Wcs20Package.DIMENSION_SUBSET_TYPE__DIMENSION:
                setDimension(DIMENSION_EDEFAULT);
                return;
            case Wcs20Package.DIMENSION_SUBSET_TYPE__CRS:
                setCRS(CRS_EDEFAULT);
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
            case Wcs20Package.DIMENSION_SUBSET_TYPE__DIMENSION:
                return DIMENSION_EDEFAULT == null ? dimension != null : !DIMENSION_EDEFAULT.equals(dimension);
            case Wcs20Package.DIMENSION_SUBSET_TYPE__CRS:
                return CRS_EDEFAULT == null ? cRS != null : !CRS_EDEFAULT.equals(cRS);
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
        result.append(" (dimension: ");
        result.append(dimension);
        result.append(", cRS: ");
        result.append(cRS);
        result.append(')');
        return result.toString();
    }

} //DimensionSubsetTypeImpl
