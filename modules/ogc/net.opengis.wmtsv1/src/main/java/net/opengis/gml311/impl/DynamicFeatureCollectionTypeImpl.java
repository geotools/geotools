/**
 */
package net.opengis.gml311.impl;

import net.opengis.gml311.DynamicFeatureCollectionType;
import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.HistoryPropertyType;
import net.opengis.gml311.StringOrRefType;
import net.opengis.gml311.TimePrimitivePropertyType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Dynamic Feature Collection Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.DynamicFeatureCollectionTypeImpl#getValidTime <em>Valid Time</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.DynamicFeatureCollectionTypeImpl#getHistoryGroup <em>History Group</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.DynamicFeatureCollectionTypeImpl#getHistory <em>History</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.DynamicFeatureCollectionTypeImpl#getDataSource <em>Data Source</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DynamicFeatureCollectionTypeImpl extends FeatureCollectionTypeImpl implements DynamicFeatureCollectionType {
    /**
     * The cached value of the '{@link #getValidTime() <em>Valid Time</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getValidTime()
     * @generated
     * @ordered
     */
    protected TimePrimitivePropertyType validTime;

    /**
     * The cached value of the '{@link #getHistoryGroup() <em>History Group</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getHistoryGroup()
     * @generated
     * @ordered
     */
    protected FeatureMap historyGroup;

    /**
     * The cached value of the '{@link #getDataSource() <em>Data Source</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDataSource()
     * @generated
     * @ordered
     */
    protected StringOrRefType dataSource;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected DynamicFeatureCollectionTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getDynamicFeatureCollectionType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TimePrimitivePropertyType getValidTime() {
        return validTime;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetValidTime(TimePrimitivePropertyType newValidTime, NotificationChain msgs) {
        TimePrimitivePropertyType oldValidTime = validTime;
        validTime = newValidTime;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.DYNAMIC_FEATURE_COLLECTION_TYPE__VALID_TIME, oldValidTime, newValidTime);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setValidTime(TimePrimitivePropertyType newValidTime) {
        if (newValidTime != validTime) {
            NotificationChain msgs = null;
            if (validTime != null)
                msgs = ((InternalEObject)validTime).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.DYNAMIC_FEATURE_COLLECTION_TYPE__VALID_TIME, null, msgs);
            if (newValidTime != null)
                msgs = ((InternalEObject)newValidTime).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.DYNAMIC_FEATURE_COLLECTION_TYPE__VALID_TIME, null, msgs);
            msgs = basicSetValidTime(newValidTime, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.DYNAMIC_FEATURE_COLLECTION_TYPE__VALID_TIME, newValidTime, newValidTime));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureMap getHistoryGroup() {
        if (historyGroup == null) {
            historyGroup = new BasicFeatureMap(this, Gml311Package.DYNAMIC_FEATURE_COLLECTION_TYPE__HISTORY_GROUP);
        }
        return historyGroup;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public HistoryPropertyType getHistory() {
        return (HistoryPropertyType)getHistoryGroup().get(Gml311Package.eINSTANCE.getDynamicFeatureCollectionType_History(), true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetHistory(HistoryPropertyType newHistory, NotificationChain msgs) {
        return ((FeatureMap.Internal)getHistoryGroup()).basicAdd(Gml311Package.eINSTANCE.getDynamicFeatureCollectionType_History(), newHistory, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setHistory(HistoryPropertyType newHistory) {
        ((FeatureMap.Internal)getHistoryGroup()).set(Gml311Package.eINSTANCE.getDynamicFeatureCollectionType_History(), newHistory);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public StringOrRefType getDataSource() {
        return dataSource;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetDataSource(StringOrRefType newDataSource, NotificationChain msgs) {
        StringOrRefType oldDataSource = dataSource;
        dataSource = newDataSource;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.DYNAMIC_FEATURE_COLLECTION_TYPE__DATA_SOURCE, oldDataSource, newDataSource);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDataSource(StringOrRefType newDataSource) {
        if (newDataSource != dataSource) {
            NotificationChain msgs = null;
            if (dataSource != null)
                msgs = ((InternalEObject)dataSource).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.DYNAMIC_FEATURE_COLLECTION_TYPE__DATA_SOURCE, null, msgs);
            if (newDataSource != null)
                msgs = ((InternalEObject)newDataSource).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.DYNAMIC_FEATURE_COLLECTION_TYPE__DATA_SOURCE, null, msgs);
            msgs = basicSetDataSource(newDataSource, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.DYNAMIC_FEATURE_COLLECTION_TYPE__DATA_SOURCE, newDataSource, newDataSource));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.DYNAMIC_FEATURE_COLLECTION_TYPE__VALID_TIME:
                return basicSetValidTime(null, msgs);
            case Gml311Package.DYNAMIC_FEATURE_COLLECTION_TYPE__HISTORY_GROUP:
                return ((InternalEList<?>)getHistoryGroup()).basicRemove(otherEnd, msgs);
            case Gml311Package.DYNAMIC_FEATURE_COLLECTION_TYPE__HISTORY:
                return basicSetHistory(null, msgs);
            case Gml311Package.DYNAMIC_FEATURE_COLLECTION_TYPE__DATA_SOURCE:
                return basicSetDataSource(null, msgs);
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
            case Gml311Package.DYNAMIC_FEATURE_COLLECTION_TYPE__VALID_TIME:
                return getValidTime();
            case Gml311Package.DYNAMIC_FEATURE_COLLECTION_TYPE__HISTORY_GROUP:
                if (coreType) return getHistoryGroup();
                return ((FeatureMap.Internal)getHistoryGroup()).getWrapper();
            case Gml311Package.DYNAMIC_FEATURE_COLLECTION_TYPE__HISTORY:
                return getHistory();
            case Gml311Package.DYNAMIC_FEATURE_COLLECTION_TYPE__DATA_SOURCE:
                return getDataSource();
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
            case Gml311Package.DYNAMIC_FEATURE_COLLECTION_TYPE__VALID_TIME:
                setValidTime((TimePrimitivePropertyType)newValue);
                return;
            case Gml311Package.DYNAMIC_FEATURE_COLLECTION_TYPE__HISTORY_GROUP:
                ((FeatureMap.Internal)getHistoryGroup()).set(newValue);
                return;
            case Gml311Package.DYNAMIC_FEATURE_COLLECTION_TYPE__HISTORY:
                setHistory((HistoryPropertyType)newValue);
                return;
            case Gml311Package.DYNAMIC_FEATURE_COLLECTION_TYPE__DATA_SOURCE:
                setDataSource((StringOrRefType)newValue);
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
            case Gml311Package.DYNAMIC_FEATURE_COLLECTION_TYPE__VALID_TIME:
                setValidTime((TimePrimitivePropertyType)null);
                return;
            case Gml311Package.DYNAMIC_FEATURE_COLLECTION_TYPE__HISTORY_GROUP:
                getHistoryGroup().clear();
                return;
            case Gml311Package.DYNAMIC_FEATURE_COLLECTION_TYPE__HISTORY:
                setHistory((HistoryPropertyType)null);
                return;
            case Gml311Package.DYNAMIC_FEATURE_COLLECTION_TYPE__DATA_SOURCE:
                setDataSource((StringOrRefType)null);
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
            case Gml311Package.DYNAMIC_FEATURE_COLLECTION_TYPE__VALID_TIME:
                return validTime != null;
            case Gml311Package.DYNAMIC_FEATURE_COLLECTION_TYPE__HISTORY_GROUP:
                return historyGroup != null && !historyGroup.isEmpty();
            case Gml311Package.DYNAMIC_FEATURE_COLLECTION_TYPE__HISTORY:
                return getHistory() != null;
            case Gml311Package.DYNAMIC_FEATURE_COLLECTION_TYPE__DATA_SOURCE:
                return dataSource != null;
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
        result.append(" (historyGroup: ");
        result.append(historyGroup);
        result.append(')');
        return result.toString();
    }

} //DynamicFeatureCollectionTypeImpl
