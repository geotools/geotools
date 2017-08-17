/**
 */
package net.opengis.gml311.impl;

import java.util.Collection;

import net.opengis.gml311.AbstractCoordinateOperationType;
import net.opengis.gml311.AbstractPositionalAccuracyType;
import net.opengis.gml311.CRSRefType;
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

import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Abstract Coordinate Operation Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.AbstractCoordinateOperationTypeImpl#getCoordinateOperationID <em>Coordinate Operation ID</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.AbstractCoordinateOperationTypeImpl#getRemarks <em>Remarks</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.AbstractCoordinateOperationTypeImpl#getOperationVersion <em>Operation Version</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.AbstractCoordinateOperationTypeImpl#getValidArea <em>Valid Area</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.AbstractCoordinateOperationTypeImpl#getScope <em>Scope</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.AbstractCoordinateOperationTypeImpl#getPositionalAccuracyGroup <em>Positional Accuracy Group</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.AbstractCoordinateOperationTypeImpl#getPositionalAccuracy <em>Positional Accuracy</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.AbstractCoordinateOperationTypeImpl#getSourceCRS <em>Source CRS</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.AbstractCoordinateOperationTypeImpl#getTargetCRS <em>Target CRS</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class AbstractCoordinateOperationTypeImpl extends AbstractCoordinateOperationBaseTypeImpl implements AbstractCoordinateOperationType {
    /**
     * The cached value of the '{@link #getCoordinateOperationID() <em>Coordinate Operation ID</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCoordinateOperationID()
     * @generated
     * @ordered
     */
    protected EList<IdentifierType> coordinateOperationID;

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
     * The default value of the '{@link #getOperationVersion() <em>Operation Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getOperationVersion()
     * @generated
     * @ordered
     */
    protected static final String OPERATION_VERSION_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getOperationVersion() <em>Operation Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getOperationVersion()
     * @generated
     * @ordered
     */
    protected String operationVersion = OPERATION_VERSION_EDEFAULT;

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
     * The cached value of the '{@link #getPositionalAccuracyGroup() <em>Positional Accuracy Group</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPositionalAccuracyGroup()
     * @generated
     * @ordered
     */
    protected FeatureMap positionalAccuracyGroup;

    /**
     * The cached value of the '{@link #getSourceCRS() <em>Source CRS</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSourceCRS()
     * @generated
     * @ordered
     */
    protected CRSRefType sourceCRS;

    /**
     * The cached value of the '{@link #getTargetCRS() <em>Target CRS</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTargetCRS()
     * @generated
     * @ordered
     */
    protected CRSRefType targetCRS;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected AbstractCoordinateOperationTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getAbstractCoordinateOperationType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<IdentifierType> getCoordinateOperationID() {
        if (coordinateOperationID == null) {
            coordinateOperationID = new EObjectContainmentEList<IdentifierType>(IdentifierType.class, this, Gml311Package.ABSTRACT_COORDINATE_OPERATION_TYPE__COORDINATE_OPERATION_ID);
        }
        return coordinateOperationID;
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
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.ABSTRACT_COORDINATE_OPERATION_TYPE__REMARKS, oldRemarks, newRemarks);
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
                msgs = ((InternalEObject)remarks).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.ABSTRACT_COORDINATE_OPERATION_TYPE__REMARKS, null, msgs);
            if (newRemarks != null)
                msgs = ((InternalEObject)newRemarks).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.ABSTRACT_COORDINATE_OPERATION_TYPE__REMARKS, null, msgs);
            msgs = basicSetRemarks(newRemarks, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.ABSTRACT_COORDINATE_OPERATION_TYPE__REMARKS, newRemarks, newRemarks));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getOperationVersion() {
        return operationVersion;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setOperationVersion(String newOperationVersion) {
        String oldOperationVersion = operationVersion;
        operationVersion = newOperationVersion;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.ABSTRACT_COORDINATE_OPERATION_TYPE__OPERATION_VERSION, oldOperationVersion, operationVersion));
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
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.ABSTRACT_COORDINATE_OPERATION_TYPE__VALID_AREA, oldValidArea, newValidArea);
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
                msgs = ((InternalEObject)validArea).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.ABSTRACT_COORDINATE_OPERATION_TYPE__VALID_AREA, null, msgs);
            if (newValidArea != null)
                msgs = ((InternalEObject)newValidArea).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.ABSTRACT_COORDINATE_OPERATION_TYPE__VALID_AREA, null, msgs);
            msgs = basicSetValidArea(newValidArea, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.ABSTRACT_COORDINATE_OPERATION_TYPE__VALID_AREA, newValidArea, newValidArea));
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
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.ABSTRACT_COORDINATE_OPERATION_TYPE__SCOPE, oldScope, scope));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureMap getPositionalAccuracyGroup() {
        if (positionalAccuracyGroup == null) {
            positionalAccuracyGroup = new BasicFeatureMap(this, Gml311Package.ABSTRACT_COORDINATE_OPERATION_TYPE__POSITIONAL_ACCURACY_GROUP);
        }
        return positionalAccuracyGroup;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<AbstractPositionalAccuracyType> getPositionalAccuracy() {
        return getPositionalAccuracyGroup().list(Gml311Package.eINSTANCE.getAbstractCoordinateOperationType_PositionalAccuracy());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CRSRefType getSourceCRS() {
        return sourceCRS;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetSourceCRS(CRSRefType newSourceCRS, NotificationChain msgs) {
        CRSRefType oldSourceCRS = sourceCRS;
        sourceCRS = newSourceCRS;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.ABSTRACT_COORDINATE_OPERATION_TYPE__SOURCE_CRS, oldSourceCRS, newSourceCRS);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSourceCRS(CRSRefType newSourceCRS) {
        if (newSourceCRS != sourceCRS) {
            NotificationChain msgs = null;
            if (sourceCRS != null)
                msgs = ((InternalEObject)sourceCRS).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.ABSTRACT_COORDINATE_OPERATION_TYPE__SOURCE_CRS, null, msgs);
            if (newSourceCRS != null)
                msgs = ((InternalEObject)newSourceCRS).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.ABSTRACT_COORDINATE_OPERATION_TYPE__SOURCE_CRS, null, msgs);
            msgs = basicSetSourceCRS(newSourceCRS, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.ABSTRACT_COORDINATE_OPERATION_TYPE__SOURCE_CRS, newSourceCRS, newSourceCRS));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CRSRefType getTargetCRS() {
        return targetCRS;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetTargetCRS(CRSRefType newTargetCRS, NotificationChain msgs) {
        CRSRefType oldTargetCRS = targetCRS;
        targetCRS = newTargetCRS;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.ABSTRACT_COORDINATE_OPERATION_TYPE__TARGET_CRS, oldTargetCRS, newTargetCRS);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTargetCRS(CRSRefType newTargetCRS) {
        if (newTargetCRS != targetCRS) {
            NotificationChain msgs = null;
            if (targetCRS != null)
                msgs = ((InternalEObject)targetCRS).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.ABSTRACT_COORDINATE_OPERATION_TYPE__TARGET_CRS, null, msgs);
            if (newTargetCRS != null)
                msgs = ((InternalEObject)newTargetCRS).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.ABSTRACT_COORDINATE_OPERATION_TYPE__TARGET_CRS, null, msgs);
            msgs = basicSetTargetCRS(newTargetCRS, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.ABSTRACT_COORDINATE_OPERATION_TYPE__TARGET_CRS, newTargetCRS, newTargetCRS));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.ABSTRACT_COORDINATE_OPERATION_TYPE__COORDINATE_OPERATION_ID:
                return ((InternalEList<?>)getCoordinateOperationID()).basicRemove(otherEnd, msgs);
            case Gml311Package.ABSTRACT_COORDINATE_OPERATION_TYPE__REMARKS:
                return basicSetRemarks(null, msgs);
            case Gml311Package.ABSTRACT_COORDINATE_OPERATION_TYPE__VALID_AREA:
                return basicSetValidArea(null, msgs);
            case Gml311Package.ABSTRACT_COORDINATE_OPERATION_TYPE__POSITIONAL_ACCURACY_GROUP:
                return ((InternalEList<?>)getPositionalAccuracyGroup()).basicRemove(otherEnd, msgs);
            case Gml311Package.ABSTRACT_COORDINATE_OPERATION_TYPE__POSITIONAL_ACCURACY:
                return ((InternalEList<?>)getPositionalAccuracy()).basicRemove(otherEnd, msgs);
            case Gml311Package.ABSTRACT_COORDINATE_OPERATION_TYPE__SOURCE_CRS:
                return basicSetSourceCRS(null, msgs);
            case Gml311Package.ABSTRACT_COORDINATE_OPERATION_TYPE__TARGET_CRS:
                return basicSetTargetCRS(null, msgs);
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
            case Gml311Package.ABSTRACT_COORDINATE_OPERATION_TYPE__COORDINATE_OPERATION_ID:
                return getCoordinateOperationID();
            case Gml311Package.ABSTRACT_COORDINATE_OPERATION_TYPE__REMARKS:
                return getRemarks();
            case Gml311Package.ABSTRACT_COORDINATE_OPERATION_TYPE__OPERATION_VERSION:
                return getOperationVersion();
            case Gml311Package.ABSTRACT_COORDINATE_OPERATION_TYPE__VALID_AREA:
                return getValidArea();
            case Gml311Package.ABSTRACT_COORDINATE_OPERATION_TYPE__SCOPE:
                return getScope();
            case Gml311Package.ABSTRACT_COORDINATE_OPERATION_TYPE__POSITIONAL_ACCURACY_GROUP:
                if (coreType) return getPositionalAccuracyGroup();
                return ((FeatureMap.Internal)getPositionalAccuracyGroup()).getWrapper();
            case Gml311Package.ABSTRACT_COORDINATE_OPERATION_TYPE__POSITIONAL_ACCURACY:
                return getPositionalAccuracy();
            case Gml311Package.ABSTRACT_COORDINATE_OPERATION_TYPE__SOURCE_CRS:
                return getSourceCRS();
            case Gml311Package.ABSTRACT_COORDINATE_OPERATION_TYPE__TARGET_CRS:
                return getTargetCRS();
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
            case Gml311Package.ABSTRACT_COORDINATE_OPERATION_TYPE__COORDINATE_OPERATION_ID:
                getCoordinateOperationID().clear();
                getCoordinateOperationID().addAll((Collection<? extends IdentifierType>)newValue);
                return;
            case Gml311Package.ABSTRACT_COORDINATE_OPERATION_TYPE__REMARKS:
                setRemarks((StringOrRefType)newValue);
                return;
            case Gml311Package.ABSTRACT_COORDINATE_OPERATION_TYPE__OPERATION_VERSION:
                setOperationVersion((String)newValue);
                return;
            case Gml311Package.ABSTRACT_COORDINATE_OPERATION_TYPE__VALID_AREA:
                setValidArea((ExtentType)newValue);
                return;
            case Gml311Package.ABSTRACT_COORDINATE_OPERATION_TYPE__SCOPE:
                setScope((String)newValue);
                return;
            case Gml311Package.ABSTRACT_COORDINATE_OPERATION_TYPE__POSITIONAL_ACCURACY_GROUP:
                ((FeatureMap.Internal)getPositionalAccuracyGroup()).set(newValue);
                return;
            case Gml311Package.ABSTRACT_COORDINATE_OPERATION_TYPE__SOURCE_CRS:
                setSourceCRS((CRSRefType)newValue);
                return;
            case Gml311Package.ABSTRACT_COORDINATE_OPERATION_TYPE__TARGET_CRS:
                setTargetCRS((CRSRefType)newValue);
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
            case Gml311Package.ABSTRACT_COORDINATE_OPERATION_TYPE__COORDINATE_OPERATION_ID:
                getCoordinateOperationID().clear();
                return;
            case Gml311Package.ABSTRACT_COORDINATE_OPERATION_TYPE__REMARKS:
                setRemarks((StringOrRefType)null);
                return;
            case Gml311Package.ABSTRACT_COORDINATE_OPERATION_TYPE__OPERATION_VERSION:
                setOperationVersion(OPERATION_VERSION_EDEFAULT);
                return;
            case Gml311Package.ABSTRACT_COORDINATE_OPERATION_TYPE__VALID_AREA:
                setValidArea((ExtentType)null);
                return;
            case Gml311Package.ABSTRACT_COORDINATE_OPERATION_TYPE__SCOPE:
                setScope(SCOPE_EDEFAULT);
                return;
            case Gml311Package.ABSTRACT_COORDINATE_OPERATION_TYPE__POSITIONAL_ACCURACY_GROUP:
                getPositionalAccuracyGroup().clear();
                return;
            case Gml311Package.ABSTRACT_COORDINATE_OPERATION_TYPE__SOURCE_CRS:
                setSourceCRS((CRSRefType)null);
                return;
            case Gml311Package.ABSTRACT_COORDINATE_OPERATION_TYPE__TARGET_CRS:
                setTargetCRS((CRSRefType)null);
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
            case Gml311Package.ABSTRACT_COORDINATE_OPERATION_TYPE__COORDINATE_OPERATION_ID:
                return coordinateOperationID != null && !coordinateOperationID.isEmpty();
            case Gml311Package.ABSTRACT_COORDINATE_OPERATION_TYPE__REMARKS:
                return remarks != null;
            case Gml311Package.ABSTRACT_COORDINATE_OPERATION_TYPE__OPERATION_VERSION:
                return OPERATION_VERSION_EDEFAULT == null ? operationVersion != null : !OPERATION_VERSION_EDEFAULT.equals(operationVersion);
            case Gml311Package.ABSTRACT_COORDINATE_OPERATION_TYPE__VALID_AREA:
                return validArea != null;
            case Gml311Package.ABSTRACT_COORDINATE_OPERATION_TYPE__SCOPE:
                return SCOPE_EDEFAULT == null ? scope != null : !SCOPE_EDEFAULT.equals(scope);
            case Gml311Package.ABSTRACT_COORDINATE_OPERATION_TYPE__POSITIONAL_ACCURACY_GROUP:
                return positionalAccuracyGroup != null && !positionalAccuracyGroup.isEmpty();
            case Gml311Package.ABSTRACT_COORDINATE_OPERATION_TYPE__POSITIONAL_ACCURACY:
                return !getPositionalAccuracy().isEmpty();
            case Gml311Package.ABSTRACT_COORDINATE_OPERATION_TYPE__SOURCE_CRS:
                return sourceCRS != null;
            case Gml311Package.ABSTRACT_COORDINATE_OPERATION_TYPE__TARGET_CRS:
                return targetCRS != null;
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
        result.append(" (operationVersion: ");
        result.append(operationVersion);
        result.append(", scope: ");
        result.append(scope);
        result.append(", positionalAccuracyGroup: ");
        result.append(positionalAccuracyGroup);
        result.append(')');
        return result.toString();
    }

} //AbstractCoordinateOperationTypeImpl
