/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wps10.impl;

import java.math.BigInteger;

import net.opengis.wps10.InputDescriptionType;
import net.opengis.wps10.LiteralInputType;
import net.opengis.wps10.SupportedCRSsType;
import net.opengis.wps10.SupportedComplexDataInputType;
import net.opengis.wps10.Wps10Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Input Description Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wps10.impl.InputDescriptionTypeImpl#getComplexData <em>Complex Data</em>}</li>
 *   <li>{@link net.opengis.wps10.impl.InputDescriptionTypeImpl#getLiteralData <em>Literal Data</em>}</li>
 *   <li>{@link net.opengis.wps10.impl.InputDescriptionTypeImpl#getBoundingBoxData <em>Bounding Box Data</em>}</li>
 *   <li>{@link net.opengis.wps10.impl.InputDescriptionTypeImpl#getMaxOccurs <em>Max Occurs</em>}</li>
 *   <li>{@link net.opengis.wps10.impl.InputDescriptionTypeImpl#getMinOccurs <em>Min Occurs</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class InputDescriptionTypeImpl extends DescriptionTypeImpl implements InputDescriptionType {
    /**
     * The cached value of the '{@link #getComplexData() <em>Complex Data</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getComplexData()
     * @generated
     * @ordered
     */
    protected SupportedComplexDataInputType complexData;

    /**
     * The cached value of the '{@link #getLiteralData() <em>Literal Data</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLiteralData()
     * @generated
     * @ordered
     */
    protected LiteralInputType literalData;

    /**
     * The cached value of the '{@link #getBoundingBoxData() <em>Bounding Box Data</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getBoundingBoxData()
     * @generated
     * @ordered
     */
    protected SupportedCRSsType boundingBoxData;

    /**
     * The default value of the '{@link #getMaxOccurs() <em>Max Occurs</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMaxOccurs()
     * @generated
     * @ordered
     */
    protected static final BigInteger MAX_OCCURS_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getMaxOccurs() <em>Max Occurs</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMaxOccurs()
     * @generated
     * @ordered
     */
    protected BigInteger maxOccurs = MAX_OCCURS_EDEFAULT;

    /**
     * The default value of the '{@link #getMinOccurs() <em>Min Occurs</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMinOccurs()
     * @generated
     * @ordered
     */
    protected static final BigInteger MIN_OCCURS_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getMinOccurs() <em>Min Occurs</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMinOccurs()
     * @generated
     * @ordered
     */
    protected BigInteger minOccurs = MIN_OCCURS_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected InputDescriptionTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return Wps10Package.Literals.INPUT_DESCRIPTION_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SupportedComplexDataInputType getComplexData() {
        return complexData;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetComplexData(SupportedComplexDataInputType newComplexData, NotificationChain msgs) {
        SupportedComplexDataInputType oldComplexData = complexData;
        complexData = newComplexData;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wps10Package.INPUT_DESCRIPTION_TYPE__COMPLEX_DATA, oldComplexData, newComplexData);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setComplexData(SupportedComplexDataInputType newComplexData) {
        if (newComplexData != complexData) {
            NotificationChain msgs = null;
            if (complexData != null)
                msgs = ((InternalEObject)complexData).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wps10Package.INPUT_DESCRIPTION_TYPE__COMPLEX_DATA, null, msgs);
            if (newComplexData != null)
                msgs = ((InternalEObject)newComplexData).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wps10Package.INPUT_DESCRIPTION_TYPE__COMPLEX_DATA, null, msgs);
            msgs = basicSetComplexData(newComplexData, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.INPUT_DESCRIPTION_TYPE__COMPLEX_DATA, newComplexData, newComplexData));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public LiteralInputType getLiteralData() {
        return literalData;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetLiteralData(LiteralInputType newLiteralData, NotificationChain msgs) {
        LiteralInputType oldLiteralData = literalData;
        literalData = newLiteralData;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wps10Package.INPUT_DESCRIPTION_TYPE__LITERAL_DATA, oldLiteralData, newLiteralData);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setLiteralData(LiteralInputType newLiteralData) {
        if (newLiteralData != literalData) {
            NotificationChain msgs = null;
            if (literalData != null)
                msgs = ((InternalEObject)literalData).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wps10Package.INPUT_DESCRIPTION_TYPE__LITERAL_DATA, null, msgs);
            if (newLiteralData != null)
                msgs = ((InternalEObject)newLiteralData).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wps10Package.INPUT_DESCRIPTION_TYPE__LITERAL_DATA, null, msgs);
            msgs = basicSetLiteralData(newLiteralData, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.INPUT_DESCRIPTION_TYPE__LITERAL_DATA, newLiteralData, newLiteralData));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SupportedCRSsType getBoundingBoxData() {
        return boundingBoxData;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetBoundingBoxData(SupportedCRSsType newBoundingBoxData, NotificationChain msgs) {
        SupportedCRSsType oldBoundingBoxData = boundingBoxData;
        boundingBoxData = newBoundingBoxData;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wps10Package.INPUT_DESCRIPTION_TYPE__BOUNDING_BOX_DATA, oldBoundingBoxData, newBoundingBoxData);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setBoundingBoxData(SupportedCRSsType newBoundingBoxData) {
        if (newBoundingBoxData != boundingBoxData) {
            NotificationChain msgs = null;
            if (boundingBoxData != null)
                msgs = ((InternalEObject)boundingBoxData).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wps10Package.INPUT_DESCRIPTION_TYPE__BOUNDING_BOX_DATA, null, msgs);
            if (newBoundingBoxData != null)
                msgs = ((InternalEObject)newBoundingBoxData).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wps10Package.INPUT_DESCRIPTION_TYPE__BOUNDING_BOX_DATA, null, msgs);
            msgs = basicSetBoundingBoxData(newBoundingBoxData, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.INPUT_DESCRIPTION_TYPE__BOUNDING_BOX_DATA, newBoundingBoxData, newBoundingBoxData));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BigInteger getMaxOccurs() {
        return maxOccurs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setMaxOccurs(BigInteger newMaxOccurs) {
        BigInteger oldMaxOccurs = maxOccurs;
        maxOccurs = newMaxOccurs;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.INPUT_DESCRIPTION_TYPE__MAX_OCCURS, oldMaxOccurs, maxOccurs));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BigInteger getMinOccurs() {
        return minOccurs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setMinOccurs(BigInteger newMinOccurs) {
        BigInteger oldMinOccurs = minOccurs;
        minOccurs = newMinOccurs;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.INPUT_DESCRIPTION_TYPE__MIN_OCCURS, oldMinOccurs, minOccurs));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Wps10Package.INPUT_DESCRIPTION_TYPE__COMPLEX_DATA:
                return basicSetComplexData(null, msgs);
            case Wps10Package.INPUT_DESCRIPTION_TYPE__LITERAL_DATA:
                return basicSetLiteralData(null, msgs);
            case Wps10Package.INPUT_DESCRIPTION_TYPE__BOUNDING_BOX_DATA:
                return basicSetBoundingBoxData(null, msgs);
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
            case Wps10Package.INPUT_DESCRIPTION_TYPE__COMPLEX_DATA:
                return getComplexData();
            case Wps10Package.INPUT_DESCRIPTION_TYPE__LITERAL_DATA:
                return getLiteralData();
            case Wps10Package.INPUT_DESCRIPTION_TYPE__BOUNDING_BOX_DATA:
                return getBoundingBoxData();
            case Wps10Package.INPUT_DESCRIPTION_TYPE__MAX_OCCURS:
                return getMaxOccurs();
            case Wps10Package.INPUT_DESCRIPTION_TYPE__MIN_OCCURS:
                return getMinOccurs();
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
            case Wps10Package.INPUT_DESCRIPTION_TYPE__COMPLEX_DATA:
                setComplexData((SupportedComplexDataInputType)newValue);
                return;
            case Wps10Package.INPUT_DESCRIPTION_TYPE__LITERAL_DATA:
                setLiteralData((LiteralInputType)newValue);
                return;
            case Wps10Package.INPUT_DESCRIPTION_TYPE__BOUNDING_BOX_DATA:
                setBoundingBoxData((SupportedCRSsType)newValue);
                return;
            case Wps10Package.INPUT_DESCRIPTION_TYPE__MAX_OCCURS:
                setMaxOccurs((BigInteger)newValue);
                return;
            case Wps10Package.INPUT_DESCRIPTION_TYPE__MIN_OCCURS:
                setMinOccurs((BigInteger)newValue);
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
            case Wps10Package.INPUT_DESCRIPTION_TYPE__COMPLEX_DATA:
                setComplexData((SupportedComplexDataInputType)null);
                return;
            case Wps10Package.INPUT_DESCRIPTION_TYPE__LITERAL_DATA:
                setLiteralData((LiteralInputType)null);
                return;
            case Wps10Package.INPUT_DESCRIPTION_TYPE__BOUNDING_BOX_DATA:
                setBoundingBoxData((SupportedCRSsType)null);
                return;
            case Wps10Package.INPUT_DESCRIPTION_TYPE__MAX_OCCURS:
                setMaxOccurs(MAX_OCCURS_EDEFAULT);
                return;
            case Wps10Package.INPUT_DESCRIPTION_TYPE__MIN_OCCURS:
                setMinOccurs(MIN_OCCURS_EDEFAULT);
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
            case Wps10Package.INPUT_DESCRIPTION_TYPE__COMPLEX_DATA:
                return complexData != null;
            case Wps10Package.INPUT_DESCRIPTION_TYPE__LITERAL_DATA:
                return literalData != null;
            case Wps10Package.INPUT_DESCRIPTION_TYPE__BOUNDING_BOX_DATA:
                return boundingBoxData != null;
            case Wps10Package.INPUT_DESCRIPTION_TYPE__MAX_OCCURS:
                return MAX_OCCURS_EDEFAULT == null ? maxOccurs != null : !MAX_OCCURS_EDEFAULT.equals(maxOccurs);
            case Wps10Package.INPUT_DESCRIPTION_TYPE__MIN_OCCURS:
                return MIN_OCCURS_EDEFAULT == null ? minOccurs != null : !MIN_OCCURS_EDEFAULT.equals(minOccurs);
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
        result.append(" (maxOccurs: ");
        result.append(maxOccurs);
        result.append(", minOccurs: ");
        result.append(minOccurs);
        result.append(')');
        return result.toString();
    }

} //InputDescriptionTypeImpl
