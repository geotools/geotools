/**
 */
package net.opengis.gml311.impl;

import net.opengis.gml311.AbstractGeneralDerivedCRSType;
import net.opengis.gml311.CoordinateReferenceSystemRefType;
import net.opengis.gml311.GeneralConversionRefType;
import net.opengis.gml311.Gml311Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Abstract General Derived CRS Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.AbstractGeneralDerivedCRSTypeImpl#getBaseCRS <em>Base CRS</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.AbstractGeneralDerivedCRSTypeImpl#getDefinedByConversion <em>Defined By Conversion</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class AbstractGeneralDerivedCRSTypeImpl extends AbstractReferenceSystemTypeImpl implements AbstractGeneralDerivedCRSType {
    /**
     * The cached value of the '{@link #getBaseCRS() <em>Base CRS</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getBaseCRS()
     * @generated
     * @ordered
     */
    protected CoordinateReferenceSystemRefType baseCRS;

    /**
     * The cached value of the '{@link #getDefinedByConversion() <em>Defined By Conversion</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDefinedByConversion()
     * @generated
     * @ordered
     */
    protected GeneralConversionRefType definedByConversion;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected AbstractGeneralDerivedCRSTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getAbstractGeneralDerivedCRSType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CoordinateReferenceSystemRefType getBaseCRS() {
        return baseCRS;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetBaseCRS(CoordinateReferenceSystemRefType newBaseCRS, NotificationChain msgs) {
        CoordinateReferenceSystemRefType oldBaseCRS = baseCRS;
        baseCRS = newBaseCRS;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.ABSTRACT_GENERAL_DERIVED_CRS_TYPE__BASE_CRS, oldBaseCRS, newBaseCRS);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setBaseCRS(CoordinateReferenceSystemRefType newBaseCRS) {
        if (newBaseCRS != baseCRS) {
            NotificationChain msgs = null;
            if (baseCRS != null)
                msgs = ((InternalEObject)baseCRS).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.ABSTRACT_GENERAL_DERIVED_CRS_TYPE__BASE_CRS, null, msgs);
            if (newBaseCRS != null)
                msgs = ((InternalEObject)newBaseCRS).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.ABSTRACT_GENERAL_DERIVED_CRS_TYPE__BASE_CRS, null, msgs);
            msgs = basicSetBaseCRS(newBaseCRS, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.ABSTRACT_GENERAL_DERIVED_CRS_TYPE__BASE_CRS, newBaseCRS, newBaseCRS));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public GeneralConversionRefType getDefinedByConversion() {
        return definedByConversion;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetDefinedByConversion(GeneralConversionRefType newDefinedByConversion, NotificationChain msgs) {
        GeneralConversionRefType oldDefinedByConversion = definedByConversion;
        definedByConversion = newDefinedByConversion;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.ABSTRACT_GENERAL_DERIVED_CRS_TYPE__DEFINED_BY_CONVERSION, oldDefinedByConversion, newDefinedByConversion);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDefinedByConversion(GeneralConversionRefType newDefinedByConversion) {
        if (newDefinedByConversion != definedByConversion) {
            NotificationChain msgs = null;
            if (definedByConversion != null)
                msgs = ((InternalEObject)definedByConversion).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.ABSTRACT_GENERAL_DERIVED_CRS_TYPE__DEFINED_BY_CONVERSION, null, msgs);
            if (newDefinedByConversion != null)
                msgs = ((InternalEObject)newDefinedByConversion).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.ABSTRACT_GENERAL_DERIVED_CRS_TYPE__DEFINED_BY_CONVERSION, null, msgs);
            msgs = basicSetDefinedByConversion(newDefinedByConversion, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.ABSTRACT_GENERAL_DERIVED_CRS_TYPE__DEFINED_BY_CONVERSION, newDefinedByConversion, newDefinedByConversion));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.ABSTRACT_GENERAL_DERIVED_CRS_TYPE__BASE_CRS:
                return basicSetBaseCRS(null, msgs);
            case Gml311Package.ABSTRACT_GENERAL_DERIVED_CRS_TYPE__DEFINED_BY_CONVERSION:
                return basicSetDefinedByConversion(null, msgs);
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
            case Gml311Package.ABSTRACT_GENERAL_DERIVED_CRS_TYPE__BASE_CRS:
                return getBaseCRS();
            case Gml311Package.ABSTRACT_GENERAL_DERIVED_CRS_TYPE__DEFINED_BY_CONVERSION:
                return getDefinedByConversion();
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
            case Gml311Package.ABSTRACT_GENERAL_DERIVED_CRS_TYPE__BASE_CRS:
                setBaseCRS((CoordinateReferenceSystemRefType)newValue);
                return;
            case Gml311Package.ABSTRACT_GENERAL_DERIVED_CRS_TYPE__DEFINED_BY_CONVERSION:
                setDefinedByConversion((GeneralConversionRefType)newValue);
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
            case Gml311Package.ABSTRACT_GENERAL_DERIVED_CRS_TYPE__BASE_CRS:
                setBaseCRS((CoordinateReferenceSystemRefType)null);
                return;
            case Gml311Package.ABSTRACT_GENERAL_DERIVED_CRS_TYPE__DEFINED_BY_CONVERSION:
                setDefinedByConversion((GeneralConversionRefType)null);
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
            case Gml311Package.ABSTRACT_GENERAL_DERIVED_CRS_TYPE__BASE_CRS:
                return baseCRS != null;
            case Gml311Package.ABSTRACT_GENERAL_DERIVED_CRS_TYPE__DEFINED_BY_CONVERSION:
                return definedByConversion != null;
        }
        return super.eIsSet(featureID);
    }

} //AbstractGeneralDerivedCRSTypeImpl
