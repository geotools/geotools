/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs20.impl;

import java.util.Collection;

import net.opengis.wfs20.EnvelopePropertyType;
import net.opengis.wfs20.MemberPropertyType;
import net.opengis.wfs20.SimpleFeatureCollectionType;
import net.opengis.wfs20.Wfs20Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.geotools.feature.FeatureCollection;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Simple Feature Collection Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wfs20.impl.SimpleFeatureCollectionTypeImpl#getBoundedBy <em>Bounded By</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.SimpleFeatureCollectionTypeImpl#getMember <em>Member</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SimpleFeatureCollectionTypeImpl extends EObjectImpl implements SimpleFeatureCollectionType {
    /**
     * The cached value of the '{@link #getBoundedBy() <em>Bounded By</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getBoundedBy()
     * @generated
     * @ordered
     */
    protected EnvelopePropertyType boundedBy;

    /**
     * The cached value of the '{@link #getMember() <em>Member</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMember()
     * @generated
     * @ordered
     */
    protected EList<FeatureCollection> member;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected SimpleFeatureCollectionTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Wfs20Package.Literals.SIMPLE_FEATURE_COLLECTION_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EnvelopePropertyType getBoundedBy() {
        return boundedBy;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetBoundedBy(EnvelopePropertyType newBoundedBy, NotificationChain msgs) {
        EnvelopePropertyType oldBoundedBy = boundedBy;
        boundedBy = newBoundedBy;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wfs20Package.SIMPLE_FEATURE_COLLECTION_TYPE__BOUNDED_BY, oldBoundedBy, newBoundedBy);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setBoundedBy(EnvelopePropertyType newBoundedBy) {
        if (newBoundedBy != boundedBy) {
            NotificationChain msgs = null;
            if (boundedBy != null)
                msgs = ((InternalEObject)boundedBy).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wfs20Package.SIMPLE_FEATURE_COLLECTION_TYPE__BOUNDED_BY, null, msgs);
            if (newBoundedBy != null)
                msgs = ((InternalEObject)newBoundedBy).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wfs20Package.SIMPLE_FEATURE_COLLECTION_TYPE__BOUNDED_BY, null, msgs);
            msgs = basicSetBoundedBy(newBoundedBy, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wfs20Package.SIMPLE_FEATURE_COLLECTION_TYPE__BOUNDED_BY, newBoundedBy, newBoundedBy));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<FeatureCollection> getMember() {
        if (member == null) {
            member = new EDataTypeUniqueEList<FeatureCollection>(FeatureCollection.class, this, Wfs20Package.SIMPLE_FEATURE_COLLECTION_TYPE__MEMBER);
        }
        return member;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Wfs20Package.SIMPLE_FEATURE_COLLECTION_TYPE__BOUNDED_BY:
                return basicSetBoundedBy(null, msgs);
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
            case Wfs20Package.SIMPLE_FEATURE_COLLECTION_TYPE__BOUNDED_BY:
                return getBoundedBy();
            case Wfs20Package.SIMPLE_FEATURE_COLLECTION_TYPE__MEMBER:
                return getMember();
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
            case Wfs20Package.SIMPLE_FEATURE_COLLECTION_TYPE__BOUNDED_BY:
                setBoundedBy((EnvelopePropertyType)newValue);
                return;
            case Wfs20Package.SIMPLE_FEATURE_COLLECTION_TYPE__MEMBER:
                getMember().clear();
                getMember().addAll((Collection<? extends FeatureCollection>)newValue);
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
            case Wfs20Package.SIMPLE_FEATURE_COLLECTION_TYPE__BOUNDED_BY:
                setBoundedBy((EnvelopePropertyType)null);
                return;
            case Wfs20Package.SIMPLE_FEATURE_COLLECTION_TYPE__MEMBER:
                getMember().clear();
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
            case Wfs20Package.SIMPLE_FEATURE_COLLECTION_TYPE__BOUNDED_BY:
                return boundedBy != null;
            case Wfs20Package.SIMPLE_FEATURE_COLLECTION_TYPE__MEMBER:
                return member != null && !member.isEmpty();
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
        result.append(" (member: ");
        result.append(member);
        result.append(')');
        return result.toString();
    }

} //SimpleFeatureCollectionTypeImpl
