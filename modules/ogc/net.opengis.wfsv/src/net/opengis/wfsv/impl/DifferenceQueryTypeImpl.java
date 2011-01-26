/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfsv.impl;

import javax.xml.namespace.QName;

import net.opengis.wfsv.DifferenceQueryType;
import net.opengis.wfsv.WfsvPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Difference Query Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wfsv.impl.DifferenceQueryTypeImpl#getFilter <em>Filter</em>}</li>
 *   <li>{@link net.opengis.wfsv.impl.DifferenceQueryTypeImpl#getFromFeatureVersion <em>From Feature Version</em>}</li>
 *   <li>{@link net.opengis.wfsv.impl.DifferenceQueryTypeImpl#getSrsName <em>Srs Name</em>}</li>
 *   <li>{@link net.opengis.wfsv.impl.DifferenceQueryTypeImpl#getToFeatureVersion <em>To Feature Version</em>}</li>
 *   <li>{@link net.opengis.wfsv.impl.DifferenceQueryTypeImpl#getTypeName <em>Type Name</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DifferenceQueryTypeImpl extends EObjectImpl implements DifferenceQueryType {
    /**
     * The default value of the '{@link #getFilter() <em>Filter</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFilter()
     * @generated
     * @ordered
     */
    protected static final Object FILTER_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getFilter() <em>Filter</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFilter()
     * @generated
     * @ordered
     */
    protected Object filter = FILTER_EDEFAULT;

    /**
     * The default value of the '{@link #getFromFeatureVersion() <em>From Feature Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFromFeatureVersion()
     * @generated
     * @ordered
     */
    protected static final String FROM_FEATURE_VERSION_EDEFAULT = "FIRST";

    /**
     * The cached value of the '{@link #getFromFeatureVersion() <em>From Feature Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFromFeatureVersion()
     * @generated
     * @ordered
     */
    protected String fromFeatureVersion = FROM_FEATURE_VERSION_EDEFAULT;

    /**
     * This is true if the From Feature Version attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean fromFeatureVersionESet;

    /**
     * The default value of the '{@link #getSrsName() <em>Srs Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSrsName()
     * @generated
     * @ordered
     */
    protected static final String SRS_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getSrsName() <em>Srs Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSrsName()
     * @generated
     * @ordered
     */
    protected String srsName = SRS_NAME_EDEFAULT;

    /**
     * The default value of the '{@link #getToFeatureVersion() <em>To Feature Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getToFeatureVersion()
     * @generated
     * @ordered
     */
    protected static final String TO_FEATURE_VERSION_EDEFAULT = "LAST";

    /**
     * The cached value of the '{@link #getToFeatureVersion() <em>To Feature Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getToFeatureVersion()
     * @generated
     * @ordered
     */
    protected String toFeatureVersion = TO_FEATURE_VERSION_EDEFAULT;

    /**
     * This is true if the To Feature Version attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean toFeatureVersionESet;

    /**
     * The default value of the '{@link #getTypeName() <em>Type Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTypeName()
     * @generated
     * @ordered
     */
    protected static final QName TYPE_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getTypeName() <em>Type Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTypeName()
     * @generated
     * @ordered
     */
    protected QName typeName = TYPE_NAME_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected DifferenceQueryTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return WfsvPackage.Literals.DIFFERENCE_QUERY_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object getFilter() {
        return filter;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setFilter(Object newFilter) {
        Object oldFilter = filter;
        filter = newFilter;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, WfsvPackage.DIFFERENCE_QUERY_TYPE__FILTER, oldFilter, filter));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getFromFeatureVersion() {
        return fromFeatureVersion;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setFromFeatureVersion(String newFromFeatureVersion) {
        String oldFromFeatureVersion = fromFeatureVersion;
        fromFeatureVersion = newFromFeatureVersion;
        boolean oldFromFeatureVersionESet = fromFeatureVersionESet;
        fromFeatureVersionESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, WfsvPackage.DIFFERENCE_QUERY_TYPE__FROM_FEATURE_VERSION, oldFromFeatureVersion, fromFeatureVersion, !oldFromFeatureVersionESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetFromFeatureVersion() {
        String oldFromFeatureVersion = fromFeatureVersion;
        boolean oldFromFeatureVersionESet = fromFeatureVersionESet;
        fromFeatureVersion = FROM_FEATURE_VERSION_EDEFAULT;
        fromFeatureVersionESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, WfsvPackage.DIFFERENCE_QUERY_TYPE__FROM_FEATURE_VERSION, oldFromFeatureVersion, FROM_FEATURE_VERSION_EDEFAULT, oldFromFeatureVersionESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetFromFeatureVersion() {
        return fromFeatureVersionESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getSrsName() {
        return srsName;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSrsName(String newSrsName) {
        String oldSrsName = srsName;
        srsName = newSrsName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, WfsvPackage.DIFFERENCE_QUERY_TYPE__SRS_NAME, oldSrsName, srsName));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getToFeatureVersion() {
        return toFeatureVersion;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setToFeatureVersion(String newToFeatureVersion) {
        String oldToFeatureVersion = toFeatureVersion;
        toFeatureVersion = newToFeatureVersion;
        boolean oldToFeatureVersionESet = toFeatureVersionESet;
        toFeatureVersionESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, WfsvPackage.DIFFERENCE_QUERY_TYPE__TO_FEATURE_VERSION, oldToFeatureVersion, toFeatureVersion, !oldToFeatureVersionESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetToFeatureVersion() {
        String oldToFeatureVersion = toFeatureVersion;
        boolean oldToFeatureVersionESet = toFeatureVersionESet;
        toFeatureVersion = TO_FEATURE_VERSION_EDEFAULT;
        toFeatureVersionESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, WfsvPackage.DIFFERENCE_QUERY_TYPE__TO_FEATURE_VERSION, oldToFeatureVersion, TO_FEATURE_VERSION_EDEFAULT, oldToFeatureVersionESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetToFeatureVersion() {
        return toFeatureVersionESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public QName getTypeName() {
        return typeName;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTypeName(QName newTypeName) {
        QName oldTypeName = typeName;
        typeName = newTypeName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, WfsvPackage.DIFFERENCE_QUERY_TYPE__TYPE_NAME, oldTypeName, typeName));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case WfsvPackage.DIFFERENCE_QUERY_TYPE__FILTER:
                return getFilter();
            case WfsvPackage.DIFFERENCE_QUERY_TYPE__FROM_FEATURE_VERSION:
                return getFromFeatureVersion();
            case WfsvPackage.DIFFERENCE_QUERY_TYPE__SRS_NAME:
                return getSrsName();
            case WfsvPackage.DIFFERENCE_QUERY_TYPE__TO_FEATURE_VERSION:
                return getToFeatureVersion();
            case WfsvPackage.DIFFERENCE_QUERY_TYPE__TYPE_NAME:
                return getTypeName();
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
            case WfsvPackage.DIFFERENCE_QUERY_TYPE__FILTER:
                setFilter(newValue);
                return;
            case WfsvPackage.DIFFERENCE_QUERY_TYPE__FROM_FEATURE_VERSION:
                setFromFeatureVersion((String)newValue);
                return;
            case WfsvPackage.DIFFERENCE_QUERY_TYPE__SRS_NAME:
                setSrsName((String)newValue);
                return;
            case WfsvPackage.DIFFERENCE_QUERY_TYPE__TO_FEATURE_VERSION:
                setToFeatureVersion((String)newValue);
                return;
            case WfsvPackage.DIFFERENCE_QUERY_TYPE__TYPE_NAME:
                setTypeName((QName)newValue);
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
            case WfsvPackage.DIFFERENCE_QUERY_TYPE__FILTER:
                setFilter(FILTER_EDEFAULT);
                return;
            case WfsvPackage.DIFFERENCE_QUERY_TYPE__FROM_FEATURE_VERSION:
                unsetFromFeatureVersion();
                return;
            case WfsvPackage.DIFFERENCE_QUERY_TYPE__SRS_NAME:
                setSrsName(SRS_NAME_EDEFAULT);
                return;
            case WfsvPackage.DIFFERENCE_QUERY_TYPE__TO_FEATURE_VERSION:
                unsetToFeatureVersion();
                return;
            case WfsvPackage.DIFFERENCE_QUERY_TYPE__TYPE_NAME:
                setTypeName(TYPE_NAME_EDEFAULT);
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
            case WfsvPackage.DIFFERENCE_QUERY_TYPE__FILTER:
                return FILTER_EDEFAULT == null ? filter != null : !FILTER_EDEFAULT.equals(filter);
            case WfsvPackage.DIFFERENCE_QUERY_TYPE__FROM_FEATURE_VERSION:
                return isSetFromFeatureVersion();
            case WfsvPackage.DIFFERENCE_QUERY_TYPE__SRS_NAME:
                return SRS_NAME_EDEFAULT == null ? srsName != null : !SRS_NAME_EDEFAULT.equals(srsName);
            case WfsvPackage.DIFFERENCE_QUERY_TYPE__TO_FEATURE_VERSION:
                return isSetToFeatureVersion();
            case WfsvPackage.DIFFERENCE_QUERY_TYPE__TYPE_NAME:
                return TYPE_NAME_EDEFAULT == null ? typeName != null : !TYPE_NAME_EDEFAULT.equals(typeName);
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
        result.append(" (filter: ");
        result.append(filter);
        result.append(", fromFeatureVersion: ");
        if (fromFeatureVersionESet) result.append(fromFeatureVersion); else result.append("<unset>");
        result.append(", srsName: ");
        result.append(srsName);
        result.append(", toFeatureVersion: ");
        if (toFeatureVersionESet) result.append(toFeatureVersion); else result.append("<unset>");
        result.append(", typeName: ");
        result.append(typeName);
        result.append(')');
        return result.toString();
    }

} //DifferenceQueryTypeImpl
