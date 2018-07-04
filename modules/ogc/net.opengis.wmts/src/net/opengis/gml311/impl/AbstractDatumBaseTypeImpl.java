/**
 */
package net.opengis.gml311.impl;

import net.opengis.gml311.AbstractDatumBaseType;
import net.opengis.gml311.CodeType;
import net.opengis.gml311.Gml311Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Abstract Datum Base Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.AbstractDatumBaseTypeImpl#getDatumName <em>Datum Name</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class AbstractDatumBaseTypeImpl extends DefinitionTypeImpl implements AbstractDatumBaseType {
    /**
     * The cached value of the '{@link #getDatumName() <em>Datum Name</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDatumName()
     * @generated
     * @ordered
     */
    protected CodeType datumName;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected AbstractDatumBaseTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getAbstractDatumBaseType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CodeType getDatumName() {
        return datumName;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetDatumName(CodeType newDatumName, NotificationChain msgs) {
        CodeType oldDatumName = datumName;
        datumName = newDatumName;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.ABSTRACT_DATUM_BASE_TYPE__DATUM_NAME, oldDatumName, newDatumName);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDatumName(CodeType newDatumName) {
        if (newDatumName != datumName) {
            NotificationChain msgs = null;
            if (datumName != null)
                msgs = ((InternalEObject)datumName).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.ABSTRACT_DATUM_BASE_TYPE__DATUM_NAME, null, msgs);
            if (newDatumName != null)
                msgs = ((InternalEObject)newDatumName).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.ABSTRACT_DATUM_BASE_TYPE__DATUM_NAME, null, msgs);
            msgs = basicSetDatumName(newDatumName, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.ABSTRACT_DATUM_BASE_TYPE__DATUM_NAME, newDatumName, newDatumName));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.ABSTRACT_DATUM_BASE_TYPE__DATUM_NAME:
                return basicSetDatumName(null, msgs);
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
            case Gml311Package.ABSTRACT_DATUM_BASE_TYPE__DATUM_NAME:
                return getDatumName();
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
            case Gml311Package.ABSTRACT_DATUM_BASE_TYPE__DATUM_NAME:
                setDatumName((CodeType)newValue);
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
            case Gml311Package.ABSTRACT_DATUM_BASE_TYPE__DATUM_NAME:
                setDatumName((CodeType)null);
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
            case Gml311Package.ABSTRACT_DATUM_BASE_TYPE__DATUM_NAME:
                return datumName != null;
        }
        return super.eIsSet(featureID);
    }

} //AbstractDatumBaseTypeImpl
