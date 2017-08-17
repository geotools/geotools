/**
 */
package net.opengis.gml311.impl;

import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.GridCoverageType;
import net.opengis.gml311.GridDomainType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Grid Coverage Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.GridCoverageTypeImpl#getGridDomain <em>Grid Domain</em>}</li>
 * </ul>
 *
 * @generated
 */
public class GridCoverageTypeImpl extends AbstractDiscreteCoverageTypeImpl implements GridCoverageType {
    /**
     * The cached value of the '{@link #getGridDomain() <em>Grid Domain</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getGridDomain()
     * @generated
     * @ordered
     */
    protected GridDomainType gridDomain;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected GridCoverageTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getGridCoverageType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public GridDomainType getGridDomain() {
        return gridDomain;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetGridDomain(GridDomainType newGridDomain, NotificationChain msgs) {
        GridDomainType oldGridDomain = gridDomain;
        gridDomain = newGridDomain;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.GRID_COVERAGE_TYPE__GRID_DOMAIN, oldGridDomain, newGridDomain);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setGridDomain(GridDomainType newGridDomain) {
        if (newGridDomain != gridDomain) {
            NotificationChain msgs = null;
            if (gridDomain != null)
                msgs = ((InternalEObject)gridDomain).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.GRID_COVERAGE_TYPE__GRID_DOMAIN, null, msgs);
            if (newGridDomain != null)
                msgs = ((InternalEObject)newGridDomain).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.GRID_COVERAGE_TYPE__GRID_DOMAIN, null, msgs);
            msgs = basicSetGridDomain(newGridDomain, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.GRID_COVERAGE_TYPE__GRID_DOMAIN, newGridDomain, newGridDomain));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.GRID_COVERAGE_TYPE__GRID_DOMAIN:
                return basicSetGridDomain(null, msgs);
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
            case Gml311Package.GRID_COVERAGE_TYPE__GRID_DOMAIN:
                return getGridDomain();
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
            case Gml311Package.GRID_COVERAGE_TYPE__GRID_DOMAIN:
                setGridDomain((GridDomainType)newValue);
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
            case Gml311Package.GRID_COVERAGE_TYPE__GRID_DOMAIN:
                setGridDomain((GridDomainType)null);
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
            case Gml311Package.GRID_COVERAGE_TYPE__GRID_DOMAIN:
                return gridDomain != null;
        }
        return super.eIsSet(featureID);
    }

} //GridCoverageTypeImpl
