/**
 */
package net.opengis.gml311.impl;

import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.VerticalCRSType;
import net.opengis.gml311.VerticalCSRefType;
import net.opengis.gml311.VerticalDatumRefType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Vertical CRS Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.VerticalCRSTypeImpl#getUsesVerticalCS <em>Uses Vertical CS</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.VerticalCRSTypeImpl#getUsesVerticalDatum <em>Uses Vertical Datum</em>}</li>
 * </ul>
 *
 * @generated
 */
public class VerticalCRSTypeImpl extends AbstractReferenceSystemTypeImpl implements VerticalCRSType {
    /**
     * The cached value of the '{@link #getUsesVerticalCS() <em>Uses Vertical CS</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getUsesVerticalCS()
     * @generated
     * @ordered
     */
    protected VerticalCSRefType usesVerticalCS;

    /**
     * The cached value of the '{@link #getUsesVerticalDatum() <em>Uses Vertical Datum</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getUsesVerticalDatum()
     * @generated
     * @ordered
     */
    protected VerticalDatumRefType usesVerticalDatum;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected VerticalCRSTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getVerticalCRSType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public VerticalCSRefType getUsesVerticalCS() {
        return usesVerticalCS;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetUsesVerticalCS(VerticalCSRefType newUsesVerticalCS, NotificationChain msgs) {
        VerticalCSRefType oldUsesVerticalCS = usesVerticalCS;
        usesVerticalCS = newUsesVerticalCS;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.VERTICAL_CRS_TYPE__USES_VERTICAL_CS, oldUsesVerticalCS, newUsesVerticalCS);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setUsesVerticalCS(VerticalCSRefType newUsesVerticalCS) {
        if (newUsesVerticalCS != usesVerticalCS) {
            NotificationChain msgs = null;
            if (usesVerticalCS != null)
                msgs = ((InternalEObject)usesVerticalCS).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.VERTICAL_CRS_TYPE__USES_VERTICAL_CS, null, msgs);
            if (newUsesVerticalCS != null)
                msgs = ((InternalEObject)newUsesVerticalCS).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.VERTICAL_CRS_TYPE__USES_VERTICAL_CS, null, msgs);
            msgs = basicSetUsesVerticalCS(newUsesVerticalCS, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.VERTICAL_CRS_TYPE__USES_VERTICAL_CS, newUsesVerticalCS, newUsesVerticalCS));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public VerticalDatumRefType getUsesVerticalDatum() {
        return usesVerticalDatum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetUsesVerticalDatum(VerticalDatumRefType newUsesVerticalDatum, NotificationChain msgs) {
        VerticalDatumRefType oldUsesVerticalDatum = usesVerticalDatum;
        usesVerticalDatum = newUsesVerticalDatum;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.VERTICAL_CRS_TYPE__USES_VERTICAL_DATUM, oldUsesVerticalDatum, newUsesVerticalDatum);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setUsesVerticalDatum(VerticalDatumRefType newUsesVerticalDatum) {
        if (newUsesVerticalDatum != usesVerticalDatum) {
            NotificationChain msgs = null;
            if (usesVerticalDatum != null)
                msgs = ((InternalEObject)usesVerticalDatum).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.VERTICAL_CRS_TYPE__USES_VERTICAL_DATUM, null, msgs);
            if (newUsesVerticalDatum != null)
                msgs = ((InternalEObject)newUsesVerticalDatum).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.VERTICAL_CRS_TYPE__USES_VERTICAL_DATUM, null, msgs);
            msgs = basicSetUsesVerticalDatum(newUsesVerticalDatum, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.VERTICAL_CRS_TYPE__USES_VERTICAL_DATUM, newUsesVerticalDatum, newUsesVerticalDatum));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.VERTICAL_CRS_TYPE__USES_VERTICAL_CS:
                return basicSetUsesVerticalCS(null, msgs);
            case Gml311Package.VERTICAL_CRS_TYPE__USES_VERTICAL_DATUM:
                return basicSetUsesVerticalDatum(null, msgs);
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
            case Gml311Package.VERTICAL_CRS_TYPE__USES_VERTICAL_CS:
                return getUsesVerticalCS();
            case Gml311Package.VERTICAL_CRS_TYPE__USES_VERTICAL_DATUM:
                return getUsesVerticalDatum();
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
            case Gml311Package.VERTICAL_CRS_TYPE__USES_VERTICAL_CS:
                setUsesVerticalCS((VerticalCSRefType)newValue);
                return;
            case Gml311Package.VERTICAL_CRS_TYPE__USES_VERTICAL_DATUM:
                setUsesVerticalDatum((VerticalDatumRefType)newValue);
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
            case Gml311Package.VERTICAL_CRS_TYPE__USES_VERTICAL_CS:
                setUsesVerticalCS((VerticalCSRefType)null);
                return;
            case Gml311Package.VERTICAL_CRS_TYPE__USES_VERTICAL_DATUM:
                setUsesVerticalDatum((VerticalDatumRefType)null);
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
            case Gml311Package.VERTICAL_CRS_TYPE__USES_VERTICAL_CS:
                return usesVerticalCS != null;
            case Gml311Package.VERTICAL_CRS_TYPE__USES_VERTICAL_DATUM:
                return usesVerticalDatum != null;
        }
        return super.eIsSet(featureID);
    }

} //VerticalCRSTypeImpl
