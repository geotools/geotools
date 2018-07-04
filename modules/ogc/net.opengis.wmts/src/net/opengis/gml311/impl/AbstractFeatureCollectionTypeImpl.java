/**
 */
package net.opengis.gml311.impl;

import java.util.Collection;

import net.opengis.gml311.AbstractFeatureCollectionType;
import net.opengis.gml311.FeatureArrayPropertyType;
import net.opengis.gml311.FeaturePropertyType;
import net.opengis.gml311.Gml311Package;

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
 * An implementation of the model object '<em><b>Abstract Feature Collection Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.AbstractFeatureCollectionTypeImpl#getFeatureMember <em>Feature Member</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.AbstractFeatureCollectionTypeImpl#getFeatureMembers <em>Feature Members</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class AbstractFeatureCollectionTypeImpl extends AbstractFeatureTypeImpl implements AbstractFeatureCollectionType {
    /**
     * The cached value of the '{@link #getFeatureMember() <em>Feature Member</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFeatureMember()
     * @generated
     * @ordered
     */
    protected EList<FeaturePropertyType> featureMember;

    /**
     * The cached value of the '{@link #getFeatureMembers() <em>Feature Members</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFeatureMembers()
     * @generated
     * @ordered
     */
    protected FeatureArrayPropertyType featureMembers;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected AbstractFeatureCollectionTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getAbstractFeatureCollectionType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<FeaturePropertyType> getFeatureMember() {
        if (featureMember == null) {
            featureMember = new EObjectContainmentEList<FeaturePropertyType>(FeaturePropertyType.class, this, Gml311Package.ABSTRACT_FEATURE_COLLECTION_TYPE__FEATURE_MEMBER);
        }
        return featureMember;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureArrayPropertyType getFeatureMembers() {
        return featureMembers;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetFeatureMembers(FeatureArrayPropertyType newFeatureMembers, NotificationChain msgs) {
        FeatureArrayPropertyType oldFeatureMembers = featureMembers;
        featureMembers = newFeatureMembers;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.ABSTRACT_FEATURE_COLLECTION_TYPE__FEATURE_MEMBERS, oldFeatureMembers, newFeatureMembers);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setFeatureMembers(FeatureArrayPropertyType newFeatureMembers) {
        if (newFeatureMembers != featureMembers) {
            NotificationChain msgs = null;
            if (featureMembers != null)
                msgs = ((InternalEObject)featureMembers).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.ABSTRACT_FEATURE_COLLECTION_TYPE__FEATURE_MEMBERS, null, msgs);
            if (newFeatureMembers != null)
                msgs = ((InternalEObject)newFeatureMembers).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.ABSTRACT_FEATURE_COLLECTION_TYPE__FEATURE_MEMBERS, null, msgs);
            msgs = basicSetFeatureMembers(newFeatureMembers, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.ABSTRACT_FEATURE_COLLECTION_TYPE__FEATURE_MEMBERS, newFeatureMembers, newFeatureMembers));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.ABSTRACT_FEATURE_COLLECTION_TYPE__FEATURE_MEMBER:
                return ((InternalEList<?>)getFeatureMember()).basicRemove(otherEnd, msgs);
            case Gml311Package.ABSTRACT_FEATURE_COLLECTION_TYPE__FEATURE_MEMBERS:
                return basicSetFeatureMembers(null, msgs);
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
            case Gml311Package.ABSTRACT_FEATURE_COLLECTION_TYPE__FEATURE_MEMBER:
                return getFeatureMember();
            case Gml311Package.ABSTRACT_FEATURE_COLLECTION_TYPE__FEATURE_MEMBERS:
                return getFeatureMembers();
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
            case Gml311Package.ABSTRACT_FEATURE_COLLECTION_TYPE__FEATURE_MEMBER:
                getFeatureMember().clear();
                getFeatureMember().addAll((Collection<? extends FeaturePropertyType>)newValue);
                return;
            case Gml311Package.ABSTRACT_FEATURE_COLLECTION_TYPE__FEATURE_MEMBERS:
                setFeatureMembers((FeatureArrayPropertyType)newValue);
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
            case Gml311Package.ABSTRACT_FEATURE_COLLECTION_TYPE__FEATURE_MEMBER:
                getFeatureMember().clear();
                return;
            case Gml311Package.ABSTRACT_FEATURE_COLLECTION_TYPE__FEATURE_MEMBERS:
                setFeatureMembers((FeatureArrayPropertyType)null);
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
            case Gml311Package.ABSTRACT_FEATURE_COLLECTION_TYPE__FEATURE_MEMBER:
                return featureMember != null && !featureMember.isEmpty();
            case Gml311Package.ABSTRACT_FEATURE_COLLECTION_TYPE__FEATURE_MEMBERS:
                return featureMembers != null;
        }
        return super.eIsSet(featureID);
    }

} //AbstractFeatureCollectionTypeImpl
