/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs11.impl;

import net.opengis.ows11.BoundingBoxType;

import net.opengis.wcs11.DomainSubsetType;
import net.opengis.wcs11.TimeSequenceType;
import net.opengis.wcs11.Wcs111Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Domain Subset Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs11.impl.DomainSubsetTypeImpl#getBoundingBoxGroup <em>Bounding Box Group</em>}</li>
 *   <li>{@link net.opengis.wcs11.impl.DomainSubsetTypeImpl#getBoundingBox <em>Bounding Box</em>}</li>
 *   <li>{@link net.opengis.wcs11.impl.DomainSubsetTypeImpl#getTemporalSubset <em>Temporal Subset</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DomainSubsetTypeImpl extends EObjectImpl implements DomainSubsetType {
    /**
     * The cached value of the '{@link #getBoundingBoxGroup() <em>Bounding Box Group</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getBoundingBoxGroup()
     * @generated
     * @ordered
     */
    protected FeatureMap boundingBoxGroup;

    /**
     * The cached value of the '{@link #getTemporalSubset() <em>Temporal Subset</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTemporalSubset()
     * @generated
     * @ordered
     */
    protected TimeSequenceType temporalSubset;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected DomainSubsetTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return Wcs111Package.Literals.DOMAIN_SUBSET_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureMap getBoundingBoxGroup() {
        if (boundingBoxGroup == null) {
            boundingBoxGroup = new BasicFeatureMap(this, Wcs111Package.DOMAIN_SUBSET_TYPE__BOUNDING_BOX_GROUP);
        }
        return boundingBoxGroup;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BoundingBoxType getBoundingBox() {
        return (BoundingBoxType)getBoundingBoxGroup().get(Wcs111Package.Literals.DOMAIN_SUBSET_TYPE__BOUNDING_BOX, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetBoundingBox(BoundingBoxType newBoundingBox, NotificationChain msgs) {
        return ((FeatureMap.Internal)getBoundingBoxGroup()).basicAdd(Wcs111Package.Literals.DOMAIN_SUBSET_TYPE__BOUNDING_BOX, newBoundingBox, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setBoundingBox(BoundingBoxType newBoundingBox) {
        ((FeatureMap.Internal)getBoundingBoxGroup()).set(Wcs111Package.Literals.DOMAIN_SUBSET_TYPE__BOUNDING_BOX, newBoundingBox);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TimeSequenceType getTemporalSubset() {
        return temporalSubset;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetTemporalSubset(TimeSequenceType newTemporalSubset, NotificationChain msgs) {
        TimeSequenceType oldTemporalSubset = temporalSubset;
        temporalSubset = newTemporalSubset;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wcs111Package.DOMAIN_SUBSET_TYPE__TEMPORAL_SUBSET, oldTemporalSubset, newTemporalSubset);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTemporalSubset(TimeSequenceType newTemporalSubset) {
        if (newTemporalSubset != temporalSubset) {
            NotificationChain msgs = null;
            if (temporalSubset != null)
                msgs = ((InternalEObject)temporalSubset).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wcs111Package.DOMAIN_SUBSET_TYPE__TEMPORAL_SUBSET, null, msgs);
            if (newTemporalSubset != null)
                msgs = ((InternalEObject)newTemporalSubset).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wcs111Package.DOMAIN_SUBSET_TYPE__TEMPORAL_SUBSET, null, msgs);
            msgs = basicSetTemporalSubset(newTemporalSubset, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs111Package.DOMAIN_SUBSET_TYPE__TEMPORAL_SUBSET, newTemporalSubset, newTemporalSubset));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Wcs111Package.DOMAIN_SUBSET_TYPE__BOUNDING_BOX_GROUP:
                return ((InternalEList)getBoundingBoxGroup()).basicRemove(otherEnd, msgs);
            case Wcs111Package.DOMAIN_SUBSET_TYPE__BOUNDING_BOX:
                return basicSetBoundingBox(null, msgs);
            case Wcs111Package.DOMAIN_SUBSET_TYPE__TEMPORAL_SUBSET:
                return basicSetTemporalSubset(null, msgs);
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
            case Wcs111Package.DOMAIN_SUBSET_TYPE__BOUNDING_BOX_GROUP:
                if (coreType) return getBoundingBoxGroup();
                return ((FeatureMap.Internal)getBoundingBoxGroup()).getWrapper();
            case Wcs111Package.DOMAIN_SUBSET_TYPE__BOUNDING_BOX:
                return getBoundingBox();
            case Wcs111Package.DOMAIN_SUBSET_TYPE__TEMPORAL_SUBSET:
                return getTemporalSubset();
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
            case Wcs111Package.DOMAIN_SUBSET_TYPE__BOUNDING_BOX_GROUP:
                ((FeatureMap.Internal)getBoundingBoxGroup()).set(newValue);
                return;
            case Wcs111Package.DOMAIN_SUBSET_TYPE__BOUNDING_BOX:
                setBoundingBox((BoundingBoxType)newValue);
                return;
            case Wcs111Package.DOMAIN_SUBSET_TYPE__TEMPORAL_SUBSET:
                setTemporalSubset((TimeSequenceType)newValue);
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
            case Wcs111Package.DOMAIN_SUBSET_TYPE__BOUNDING_BOX_GROUP:
                getBoundingBoxGroup().clear();
                return;
            case Wcs111Package.DOMAIN_SUBSET_TYPE__BOUNDING_BOX:
                setBoundingBox((BoundingBoxType)null);
                return;
            case Wcs111Package.DOMAIN_SUBSET_TYPE__TEMPORAL_SUBSET:
                setTemporalSubset((TimeSequenceType)null);
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
            case Wcs111Package.DOMAIN_SUBSET_TYPE__BOUNDING_BOX_GROUP:
                return boundingBoxGroup != null && !boundingBoxGroup.isEmpty();
            case Wcs111Package.DOMAIN_SUBSET_TYPE__BOUNDING_BOX:
                return getBoundingBox() != null;
            case Wcs111Package.DOMAIN_SUBSET_TYPE__TEMPORAL_SUBSET:
                return temporalSubset != null;
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
        result.append(" (boundingBoxGroup: ");
        result.append(boundingBoxGroup);
        result.append(')');
        return result.toString();
    }

} //DomainSubsetTypeImpl
