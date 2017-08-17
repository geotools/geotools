/**
 */
package net.opengis.gml311.impl;

import java.math.BigInteger;

import net.opengis.gml311.CovarianceElementType;
import net.opengis.gml311.Gml311Package;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Covariance Element Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.CovarianceElementTypeImpl#getRowIndex <em>Row Index</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.CovarianceElementTypeImpl#getColumnIndex <em>Column Index</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.CovarianceElementTypeImpl#getCovariance <em>Covariance</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CovarianceElementTypeImpl extends MinimalEObjectImpl.Container implements CovarianceElementType {
    /**
     * The default value of the '{@link #getRowIndex() <em>Row Index</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRowIndex()
     * @generated
     * @ordered
     */
    protected static final BigInteger ROW_INDEX_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getRowIndex() <em>Row Index</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRowIndex()
     * @generated
     * @ordered
     */
    protected BigInteger rowIndex = ROW_INDEX_EDEFAULT;

    /**
     * The default value of the '{@link #getColumnIndex() <em>Column Index</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getColumnIndex()
     * @generated
     * @ordered
     */
    protected static final BigInteger COLUMN_INDEX_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getColumnIndex() <em>Column Index</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getColumnIndex()
     * @generated
     * @ordered
     */
    protected BigInteger columnIndex = COLUMN_INDEX_EDEFAULT;

    /**
     * The default value of the '{@link #getCovariance() <em>Covariance</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCovariance()
     * @generated
     * @ordered
     */
    protected static final double COVARIANCE_EDEFAULT = 0.0;

    /**
     * The cached value of the '{@link #getCovariance() <em>Covariance</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCovariance()
     * @generated
     * @ordered
     */
    protected double covariance = COVARIANCE_EDEFAULT;

    /**
     * This is true if the Covariance attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean covarianceESet;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected CovarianceElementTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getCovarianceElementType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BigInteger getRowIndex() {
        return rowIndex;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setRowIndex(BigInteger newRowIndex) {
        BigInteger oldRowIndex = rowIndex;
        rowIndex = newRowIndex;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.COVARIANCE_ELEMENT_TYPE__ROW_INDEX, oldRowIndex, rowIndex));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BigInteger getColumnIndex() {
        return columnIndex;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setColumnIndex(BigInteger newColumnIndex) {
        BigInteger oldColumnIndex = columnIndex;
        columnIndex = newColumnIndex;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.COVARIANCE_ELEMENT_TYPE__COLUMN_INDEX, oldColumnIndex, columnIndex));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public double getCovariance() {
        return covariance;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setCovariance(double newCovariance) {
        double oldCovariance = covariance;
        covariance = newCovariance;
        boolean oldCovarianceESet = covarianceESet;
        covarianceESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.COVARIANCE_ELEMENT_TYPE__COVARIANCE, oldCovariance, covariance, !oldCovarianceESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetCovariance() {
        double oldCovariance = covariance;
        boolean oldCovarianceESet = covarianceESet;
        covariance = COVARIANCE_EDEFAULT;
        covarianceESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Gml311Package.COVARIANCE_ELEMENT_TYPE__COVARIANCE, oldCovariance, COVARIANCE_EDEFAULT, oldCovarianceESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetCovariance() {
        return covarianceESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Gml311Package.COVARIANCE_ELEMENT_TYPE__ROW_INDEX:
                return getRowIndex();
            case Gml311Package.COVARIANCE_ELEMENT_TYPE__COLUMN_INDEX:
                return getColumnIndex();
            case Gml311Package.COVARIANCE_ELEMENT_TYPE__COVARIANCE:
                return getCovariance();
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
            case Gml311Package.COVARIANCE_ELEMENT_TYPE__ROW_INDEX:
                setRowIndex((BigInteger)newValue);
                return;
            case Gml311Package.COVARIANCE_ELEMENT_TYPE__COLUMN_INDEX:
                setColumnIndex((BigInteger)newValue);
                return;
            case Gml311Package.COVARIANCE_ELEMENT_TYPE__COVARIANCE:
                setCovariance((Double)newValue);
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
            case Gml311Package.COVARIANCE_ELEMENT_TYPE__ROW_INDEX:
                setRowIndex(ROW_INDEX_EDEFAULT);
                return;
            case Gml311Package.COVARIANCE_ELEMENT_TYPE__COLUMN_INDEX:
                setColumnIndex(COLUMN_INDEX_EDEFAULT);
                return;
            case Gml311Package.COVARIANCE_ELEMENT_TYPE__COVARIANCE:
                unsetCovariance();
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
            case Gml311Package.COVARIANCE_ELEMENT_TYPE__ROW_INDEX:
                return ROW_INDEX_EDEFAULT == null ? rowIndex != null : !ROW_INDEX_EDEFAULT.equals(rowIndex);
            case Gml311Package.COVARIANCE_ELEMENT_TYPE__COLUMN_INDEX:
                return COLUMN_INDEX_EDEFAULT == null ? columnIndex != null : !COLUMN_INDEX_EDEFAULT.equals(columnIndex);
            case Gml311Package.COVARIANCE_ELEMENT_TYPE__COVARIANCE:
                return isSetCovariance();
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
        result.append(" (rowIndex: ");
        result.append(rowIndex);
        result.append(", columnIndex: ");
        result.append(columnIndex);
        result.append(", covariance: ");
        if (covarianceESet) result.append(covariance); else result.append("<unset>");
        result.append(')');
        return result.toString();
    }

} //CovarianceElementTypeImpl
