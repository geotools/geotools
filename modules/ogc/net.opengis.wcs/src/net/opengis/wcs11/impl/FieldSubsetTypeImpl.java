/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs11.impl;

import java.util.Collection;

import net.opengis.ows11.CodeType;

import net.opengis.wcs11.AxisSubsetType;
import net.opengis.wcs11.FieldSubsetType;
import net.opengis.wcs11.Wcs111Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Field Subset Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs11.impl.FieldSubsetTypeImpl#getIdentifier <em>Identifier</em>}</li>
 *   <li>{@link net.opengis.wcs11.impl.FieldSubsetTypeImpl#getInterpolationType <em>Interpolation Type</em>}</li>
 *   <li>{@link net.opengis.wcs11.impl.FieldSubsetTypeImpl#getAxisSubset <em>Axis Subset</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class FieldSubsetTypeImpl extends EObjectImpl implements FieldSubsetType {
    /**
     * The cached value of the '{@link #getIdentifier() <em>Identifier</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getIdentifier()
     * @generated
     * @ordered
     */
    protected CodeType identifier;

    /**
     * The default value of the '{@link #getInterpolationType() <em>Interpolation Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getInterpolationType()
     * @generated
     * @ordered
     */
    protected static final String INTERPOLATION_TYPE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getInterpolationType() <em>Interpolation Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getInterpolationType()
     * @generated
     * @ordered
     */
    protected String interpolationType = INTERPOLATION_TYPE_EDEFAULT;

    /**
     * The cached value of the '{@link #getAxisSubset() <em>Axis Subset</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAxisSubset()
     * @generated
     * @ordered
     */
    protected EList axisSubset;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected FieldSubsetTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return Wcs111Package.Literals.FIELD_SUBSET_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CodeType getIdentifier() {
        return identifier;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetIdentifier(CodeType newIdentifier, NotificationChain msgs) {
        CodeType oldIdentifier = identifier;
        identifier = newIdentifier;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wcs111Package.FIELD_SUBSET_TYPE__IDENTIFIER, oldIdentifier, newIdentifier);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setIdentifier(CodeType newIdentifier) {
        if (newIdentifier != identifier) {
            NotificationChain msgs = null;
            if (identifier != null)
                msgs = ((InternalEObject)identifier).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wcs111Package.FIELD_SUBSET_TYPE__IDENTIFIER, null, msgs);
            if (newIdentifier != null)
                msgs = ((InternalEObject)newIdentifier).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wcs111Package.FIELD_SUBSET_TYPE__IDENTIFIER, null, msgs);
            msgs = basicSetIdentifier(newIdentifier, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs111Package.FIELD_SUBSET_TYPE__IDENTIFIER, newIdentifier, newIdentifier));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getInterpolationType() {
        return interpolationType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setInterpolationType(String newInterpolationType) {
        String oldInterpolationType = interpolationType;
        interpolationType = newInterpolationType;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs111Package.FIELD_SUBSET_TYPE__INTERPOLATION_TYPE, oldInterpolationType, interpolationType));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList getAxisSubset() {
        if (axisSubset == null) {
            axisSubset = new EObjectContainmentEList(AxisSubsetType.class, this, Wcs111Package.FIELD_SUBSET_TYPE__AXIS_SUBSET);
        }
        return axisSubset;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Wcs111Package.FIELD_SUBSET_TYPE__IDENTIFIER:
                return basicSetIdentifier(null, msgs);
            case Wcs111Package.FIELD_SUBSET_TYPE__AXIS_SUBSET:
                return ((InternalEList)getAxisSubset()).basicRemove(otherEnd, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Wcs111Package.FIELD_SUBSET_TYPE__IDENTIFIER:
                return getIdentifier();
            case Wcs111Package.FIELD_SUBSET_TYPE__INTERPOLATION_TYPE:
                return getInterpolationType();
            case Wcs111Package.FIELD_SUBSET_TYPE__AXIS_SUBSET:
                return getAxisSubset();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case Wcs111Package.FIELD_SUBSET_TYPE__IDENTIFIER:
                setIdentifier((CodeType)newValue);
                return;
            case Wcs111Package.FIELD_SUBSET_TYPE__INTERPOLATION_TYPE:
                setInterpolationType((String)newValue);
                return;
            case Wcs111Package.FIELD_SUBSET_TYPE__AXIS_SUBSET:
                getAxisSubset().clear();
                getAxisSubset().addAll((Collection)newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void eUnset(int featureID) {
        switch (featureID) {
            case Wcs111Package.FIELD_SUBSET_TYPE__IDENTIFIER:
                setIdentifier((CodeType)null);
                return;
            case Wcs111Package.FIELD_SUBSET_TYPE__INTERPOLATION_TYPE:
                setInterpolationType(INTERPOLATION_TYPE_EDEFAULT);
                return;
            case Wcs111Package.FIELD_SUBSET_TYPE__AXIS_SUBSET:
                getAxisSubset().clear();
                return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case Wcs111Package.FIELD_SUBSET_TYPE__IDENTIFIER:
                return identifier != null;
            case Wcs111Package.FIELD_SUBSET_TYPE__INTERPOLATION_TYPE:
                return INTERPOLATION_TYPE_EDEFAULT == null ? interpolationType != null : !INTERPOLATION_TYPE_EDEFAULT.equals(interpolationType);
            case Wcs111Package.FIELD_SUBSET_TYPE__AXIS_SUBSET:
                return axisSubset != null && !axisSubset.isEmpty();
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String toString() {
        if (eIsProxy()) return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (interpolationType: ");
        result.append(interpolationType);
        result.append(')');
        return result.toString();
    }

} //FieldSubsetTypeImpl
