/**
 */
package net.opengis.gml311.impl;

import java.util.Collection;

import javax.xml.datatype.XMLGregorianCalendar;

import net.opengis.gml311.AbstractDatumType;
import net.opengis.gml311.CodeType;
import net.opengis.gml311.ExtentType;
import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.IdentifierType;
import net.opengis.gml311.StringOrRefType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Abstract Datum Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.AbstractDatumTypeImpl#getDatumID <em>Datum ID</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.AbstractDatumTypeImpl#getRemarks <em>Remarks</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.AbstractDatumTypeImpl#getAnchorPoint <em>Anchor Point</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.AbstractDatumTypeImpl#getRealizationEpoch <em>Realization Epoch</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.AbstractDatumTypeImpl#getValidArea <em>Valid Area</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.AbstractDatumTypeImpl#getScope <em>Scope</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class AbstractDatumTypeImpl extends AbstractDatumBaseTypeImpl implements AbstractDatumType {
    /**
     * The cached value of the '{@link #getDatumID() <em>Datum ID</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDatumID()
     * @generated
     * @ordered
     */
    protected EList<IdentifierType> datumID;

    /**
     * The cached value of the '{@link #getRemarks() <em>Remarks</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRemarks()
     * @generated
     * @ordered
     */
    protected StringOrRefType remarks;

    /**
     * The cached value of the '{@link #getAnchorPoint() <em>Anchor Point</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAnchorPoint()
     * @generated
     * @ordered
     */
    protected CodeType anchorPoint;

    /**
     * The default value of the '{@link #getRealizationEpoch() <em>Realization Epoch</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRealizationEpoch()
     * @generated
     * @ordered
     */
    protected static final XMLGregorianCalendar REALIZATION_EPOCH_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getRealizationEpoch() <em>Realization Epoch</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRealizationEpoch()
     * @generated
     * @ordered
     */
    protected XMLGregorianCalendar realizationEpoch = REALIZATION_EPOCH_EDEFAULT;

    /**
     * The cached value of the '{@link #getValidArea() <em>Valid Area</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getValidArea()
     * @generated
     * @ordered
     */
    protected ExtentType validArea;

    /**
     * The default value of the '{@link #getScope() <em>Scope</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getScope()
     * @generated
     * @ordered
     */
    protected static final String SCOPE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getScope() <em>Scope</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getScope()
     * @generated
     * @ordered
     */
    protected String scope = SCOPE_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected AbstractDatumTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getAbstractDatumType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<IdentifierType> getDatumID() {
        if (datumID == null) {
            datumID = new EObjectContainmentEList<IdentifierType>(IdentifierType.class, this, Gml311Package.ABSTRACT_DATUM_TYPE__DATUM_ID);
        }
        return datumID;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public StringOrRefType getRemarks() {
        return remarks;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetRemarks(StringOrRefType newRemarks, NotificationChain msgs) {
        StringOrRefType oldRemarks = remarks;
        remarks = newRemarks;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.ABSTRACT_DATUM_TYPE__REMARKS, oldRemarks, newRemarks);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setRemarks(StringOrRefType newRemarks) {
        if (newRemarks != remarks) {
            NotificationChain msgs = null;
            if (remarks != null)
                msgs = ((InternalEObject)remarks).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.ABSTRACT_DATUM_TYPE__REMARKS, null, msgs);
            if (newRemarks != null)
                msgs = ((InternalEObject)newRemarks).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.ABSTRACT_DATUM_TYPE__REMARKS, null, msgs);
            msgs = basicSetRemarks(newRemarks, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.ABSTRACT_DATUM_TYPE__REMARKS, newRemarks, newRemarks));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CodeType getAnchorPoint() {
        return anchorPoint;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetAnchorPoint(CodeType newAnchorPoint, NotificationChain msgs) {
        CodeType oldAnchorPoint = anchorPoint;
        anchorPoint = newAnchorPoint;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.ABSTRACT_DATUM_TYPE__ANCHOR_POINT, oldAnchorPoint, newAnchorPoint);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setAnchorPoint(CodeType newAnchorPoint) {
        if (newAnchorPoint != anchorPoint) {
            NotificationChain msgs = null;
            if (anchorPoint != null)
                msgs = ((InternalEObject)anchorPoint).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.ABSTRACT_DATUM_TYPE__ANCHOR_POINT, null, msgs);
            if (newAnchorPoint != null)
                msgs = ((InternalEObject)newAnchorPoint).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.ABSTRACT_DATUM_TYPE__ANCHOR_POINT, null, msgs);
            msgs = basicSetAnchorPoint(newAnchorPoint, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.ABSTRACT_DATUM_TYPE__ANCHOR_POINT, newAnchorPoint, newAnchorPoint));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public XMLGregorianCalendar getRealizationEpoch() {
        return realizationEpoch;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setRealizationEpoch(XMLGregorianCalendar newRealizationEpoch) {
        XMLGregorianCalendar oldRealizationEpoch = realizationEpoch;
        realizationEpoch = newRealizationEpoch;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.ABSTRACT_DATUM_TYPE__REALIZATION_EPOCH, oldRealizationEpoch, realizationEpoch));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ExtentType getValidArea() {
        return validArea;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetValidArea(ExtentType newValidArea, NotificationChain msgs) {
        ExtentType oldValidArea = validArea;
        validArea = newValidArea;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.ABSTRACT_DATUM_TYPE__VALID_AREA, oldValidArea, newValidArea);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setValidArea(ExtentType newValidArea) {
        if (newValidArea != validArea) {
            NotificationChain msgs = null;
            if (validArea != null)
                msgs = ((InternalEObject)validArea).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.ABSTRACT_DATUM_TYPE__VALID_AREA, null, msgs);
            if (newValidArea != null)
                msgs = ((InternalEObject)newValidArea).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.ABSTRACT_DATUM_TYPE__VALID_AREA, null, msgs);
            msgs = basicSetValidArea(newValidArea, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.ABSTRACT_DATUM_TYPE__VALID_AREA, newValidArea, newValidArea));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getScope() {
        return scope;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setScope(String newScope) {
        String oldScope = scope;
        scope = newScope;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.ABSTRACT_DATUM_TYPE__SCOPE, oldScope, scope));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.ABSTRACT_DATUM_TYPE__DATUM_ID:
                return ((InternalEList<?>)getDatumID()).basicRemove(otherEnd, msgs);
            case Gml311Package.ABSTRACT_DATUM_TYPE__REMARKS:
                return basicSetRemarks(null, msgs);
            case Gml311Package.ABSTRACT_DATUM_TYPE__ANCHOR_POINT:
                return basicSetAnchorPoint(null, msgs);
            case Gml311Package.ABSTRACT_DATUM_TYPE__VALID_AREA:
                return basicSetValidArea(null, msgs);
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
            case Gml311Package.ABSTRACT_DATUM_TYPE__DATUM_ID:
                return getDatumID();
            case Gml311Package.ABSTRACT_DATUM_TYPE__REMARKS:
                return getRemarks();
            case Gml311Package.ABSTRACT_DATUM_TYPE__ANCHOR_POINT:
                return getAnchorPoint();
            case Gml311Package.ABSTRACT_DATUM_TYPE__REALIZATION_EPOCH:
                return getRealizationEpoch();
            case Gml311Package.ABSTRACT_DATUM_TYPE__VALID_AREA:
                return getValidArea();
            case Gml311Package.ABSTRACT_DATUM_TYPE__SCOPE:
                return getScope();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case Gml311Package.ABSTRACT_DATUM_TYPE__DATUM_ID:
                getDatumID().clear();
                getDatumID().addAll((Collection<? extends IdentifierType>)newValue);
                return;
            case Gml311Package.ABSTRACT_DATUM_TYPE__REMARKS:
                setRemarks((StringOrRefType)newValue);
                return;
            case Gml311Package.ABSTRACT_DATUM_TYPE__ANCHOR_POINT:
                setAnchorPoint((CodeType)newValue);
                return;
            case Gml311Package.ABSTRACT_DATUM_TYPE__REALIZATION_EPOCH:
                setRealizationEpoch((XMLGregorianCalendar) newValue);
                return;
            case Gml311Package.ABSTRACT_DATUM_TYPE__VALID_AREA:
                setValidArea((ExtentType)newValue);
                return;
            case Gml311Package.ABSTRACT_DATUM_TYPE__SCOPE:
                setScope((String)newValue);
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
            case Gml311Package.ABSTRACT_DATUM_TYPE__DATUM_ID:
                getDatumID().clear();
                return;
            case Gml311Package.ABSTRACT_DATUM_TYPE__REMARKS:
                setRemarks((StringOrRefType)null);
                return;
            case Gml311Package.ABSTRACT_DATUM_TYPE__ANCHOR_POINT:
                setAnchorPoint((CodeType)null);
                return;
            case Gml311Package.ABSTRACT_DATUM_TYPE__REALIZATION_EPOCH:
                setRealizationEpoch(REALIZATION_EPOCH_EDEFAULT);
                return;
            case Gml311Package.ABSTRACT_DATUM_TYPE__VALID_AREA:
                setValidArea((ExtentType)null);
                return;
            case Gml311Package.ABSTRACT_DATUM_TYPE__SCOPE:
                setScope(SCOPE_EDEFAULT);
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
            case Gml311Package.ABSTRACT_DATUM_TYPE__DATUM_ID:
                return datumID != null && !datumID.isEmpty();
            case Gml311Package.ABSTRACT_DATUM_TYPE__REMARKS:
                return remarks != null;
            case Gml311Package.ABSTRACT_DATUM_TYPE__ANCHOR_POINT:
                return anchorPoint != null;
            case Gml311Package.ABSTRACT_DATUM_TYPE__REALIZATION_EPOCH:
                return REALIZATION_EPOCH_EDEFAULT == null ? realizationEpoch != null : !REALIZATION_EPOCH_EDEFAULT.equals(realizationEpoch);
            case Gml311Package.ABSTRACT_DATUM_TYPE__VALID_AREA:
                return validArea != null;
            case Gml311Package.ABSTRACT_DATUM_TYPE__SCOPE:
                return SCOPE_EDEFAULT == null ? scope != null : !SCOPE_EDEFAULT.equals(scope);
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
        result.append(" (realizationEpoch: ");
        result.append(realizationEpoch);
        result.append(", scope: ");
        result.append(scope);
        result.append(')');
        return result.toString();
    }

} //AbstractDatumTypeImpl
