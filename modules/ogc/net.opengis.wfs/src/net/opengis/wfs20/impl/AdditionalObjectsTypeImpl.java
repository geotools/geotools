/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs20.impl;

import net.opengis.wfs20.AdditionalObjectsType;
import net.opengis.wfs20.SimpleFeatureCollectionType;
import net.opengis.wfs20.ValueCollectionType;
import net.opengis.wfs20.Wfs20Package;

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
 * An implementation of the model object '<em><b>Additional Objects Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wfs20.impl.AdditionalObjectsTypeImpl#getValueCollection <em>Value Collection</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.AdditionalObjectsTypeImpl#getSimpleFeatureCollectionGroup <em>Simple Feature Collection Group</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.AdditionalObjectsTypeImpl#getSimpleFeatureCollection <em>Simple Feature Collection</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class AdditionalObjectsTypeImpl extends EObjectImpl implements AdditionalObjectsType {
    /**
     * The cached value of the '{@link #getValueCollection() <em>Value Collection</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getValueCollection()
     * @generated
     * @ordered
     */
    protected ValueCollectionType valueCollection;

    /**
     * The cached value of the '{@link #getSimpleFeatureCollectionGroup() <em>Simple Feature Collection Group</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSimpleFeatureCollectionGroup()
     * @generated
     * @ordered
     */
    protected FeatureMap simpleFeatureCollectionGroup;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected AdditionalObjectsTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Wfs20Package.Literals.ADDITIONAL_OBJECTS_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ValueCollectionType getValueCollection() {
        return valueCollection;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetValueCollection(ValueCollectionType newValueCollection, NotificationChain msgs) {
        ValueCollectionType oldValueCollection = valueCollection;
        valueCollection = newValueCollection;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wfs20Package.ADDITIONAL_OBJECTS_TYPE__VALUE_COLLECTION, oldValueCollection, newValueCollection);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setValueCollection(ValueCollectionType newValueCollection) {
        if (newValueCollection != valueCollection) {
            NotificationChain msgs = null;
            if (valueCollection != null)
                msgs = ((InternalEObject)valueCollection).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wfs20Package.ADDITIONAL_OBJECTS_TYPE__VALUE_COLLECTION, null, msgs);
            if (newValueCollection != null)
                msgs = ((InternalEObject)newValueCollection).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wfs20Package.ADDITIONAL_OBJECTS_TYPE__VALUE_COLLECTION, null, msgs);
            msgs = basicSetValueCollection(newValueCollection, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wfs20Package.ADDITIONAL_OBJECTS_TYPE__VALUE_COLLECTION, newValueCollection, newValueCollection));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureMap getSimpleFeatureCollectionGroup() {
        if (simpleFeatureCollectionGroup == null) {
            simpleFeatureCollectionGroup = new BasicFeatureMap(this, Wfs20Package.ADDITIONAL_OBJECTS_TYPE__SIMPLE_FEATURE_COLLECTION_GROUP);
        }
        return simpleFeatureCollectionGroup;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SimpleFeatureCollectionType getSimpleFeatureCollection() {
        return (SimpleFeatureCollectionType)getSimpleFeatureCollectionGroup().get(Wfs20Package.Literals.ADDITIONAL_OBJECTS_TYPE__SIMPLE_FEATURE_COLLECTION, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetSimpleFeatureCollection(SimpleFeatureCollectionType newSimpleFeatureCollection, NotificationChain msgs) {
        return ((FeatureMap.Internal)getSimpleFeatureCollectionGroup()).basicAdd(Wfs20Package.Literals.ADDITIONAL_OBJECTS_TYPE__SIMPLE_FEATURE_COLLECTION, newSimpleFeatureCollection, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSimpleFeatureCollection(SimpleFeatureCollectionType newSimpleFeatureCollection) {
        ((FeatureMap.Internal)getSimpleFeatureCollectionGroup()).set(Wfs20Package.Literals.ADDITIONAL_OBJECTS_TYPE__SIMPLE_FEATURE_COLLECTION, newSimpleFeatureCollection);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Wfs20Package.ADDITIONAL_OBJECTS_TYPE__VALUE_COLLECTION:
                return basicSetValueCollection(null, msgs);
            case Wfs20Package.ADDITIONAL_OBJECTS_TYPE__SIMPLE_FEATURE_COLLECTION_GROUP:
                return ((InternalEList<?>)getSimpleFeatureCollectionGroup()).basicRemove(otherEnd, msgs);
            case Wfs20Package.ADDITIONAL_OBJECTS_TYPE__SIMPLE_FEATURE_COLLECTION:
                return basicSetSimpleFeatureCollection(null, msgs);
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
            case Wfs20Package.ADDITIONAL_OBJECTS_TYPE__VALUE_COLLECTION:
                return getValueCollection();
            case Wfs20Package.ADDITIONAL_OBJECTS_TYPE__SIMPLE_FEATURE_COLLECTION_GROUP:
                if (coreType) return getSimpleFeatureCollectionGroup();
                return ((FeatureMap.Internal)getSimpleFeatureCollectionGroup()).getWrapper();
            case Wfs20Package.ADDITIONAL_OBJECTS_TYPE__SIMPLE_FEATURE_COLLECTION:
                return getSimpleFeatureCollection();
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
            case Wfs20Package.ADDITIONAL_OBJECTS_TYPE__VALUE_COLLECTION:
                setValueCollection((ValueCollectionType)newValue);
                return;
            case Wfs20Package.ADDITIONAL_OBJECTS_TYPE__SIMPLE_FEATURE_COLLECTION_GROUP:
                ((FeatureMap.Internal)getSimpleFeatureCollectionGroup()).set(newValue);
                return;
            case Wfs20Package.ADDITIONAL_OBJECTS_TYPE__SIMPLE_FEATURE_COLLECTION:
                setSimpleFeatureCollection((SimpleFeatureCollectionType)newValue);
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
            case Wfs20Package.ADDITIONAL_OBJECTS_TYPE__VALUE_COLLECTION:
                setValueCollection((ValueCollectionType)null);
                return;
            case Wfs20Package.ADDITIONAL_OBJECTS_TYPE__SIMPLE_FEATURE_COLLECTION_GROUP:
                getSimpleFeatureCollectionGroup().clear();
                return;
            case Wfs20Package.ADDITIONAL_OBJECTS_TYPE__SIMPLE_FEATURE_COLLECTION:
                setSimpleFeatureCollection((SimpleFeatureCollectionType)null);
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
            case Wfs20Package.ADDITIONAL_OBJECTS_TYPE__VALUE_COLLECTION:
                return valueCollection != null;
            case Wfs20Package.ADDITIONAL_OBJECTS_TYPE__SIMPLE_FEATURE_COLLECTION_GROUP:
                return simpleFeatureCollectionGroup != null && !simpleFeatureCollectionGroup.isEmpty();
            case Wfs20Package.ADDITIONAL_OBJECTS_TYPE__SIMPLE_FEATURE_COLLECTION:
                return getSimpleFeatureCollection() != null;
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
        result.append(" (simpleFeatureCollectionGroup: ");
        result.append(simpleFeatureCollectionGroup);
        result.append(')');
        return result.toString();
    }

} //AdditionalObjectsTypeImpl
