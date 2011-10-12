/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.fes20.impl;

import javax.xml.datatype.XMLGregorianCalendar;

import net.opengis.fes20.Fes20Package;
import net.opengis.fes20.ResourceIdType;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Resource Id Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.fes20.impl.ResourceIdTypeImpl#getEndDate <em>End Date</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.ResourceIdTypeImpl#getPreviousRid <em>Previous Rid</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.ResourceIdTypeImpl#getRid <em>Rid</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.ResourceIdTypeImpl#getStartDate <em>Start Date</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.ResourceIdTypeImpl#getVersion <em>Version</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ResourceIdTypeImpl extends AbstractIdTypeImpl implements ResourceIdType {
    /**
     * The default value of the '{@link #getEndDate() <em>End Date</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getEndDate()
     * @generated
     * @ordered
     */
    protected static final XMLGregorianCalendar END_DATE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getEndDate() <em>End Date</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getEndDate()
     * @generated
     * @ordered
     */
    protected XMLGregorianCalendar endDate = END_DATE_EDEFAULT;

    /**
     * The default value of the '{@link #getPreviousRid() <em>Previous Rid</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPreviousRid()
     * @generated
     * @ordered
     */
    protected static final String PREVIOUS_RID_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getPreviousRid() <em>Previous Rid</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPreviousRid()
     * @generated
     * @ordered
     */
    protected String previousRid = PREVIOUS_RID_EDEFAULT;

    /**
     * The default value of the '{@link #getRid() <em>Rid</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRid()
     * @generated
     * @ordered
     */
    protected static final String RID_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getRid() <em>Rid</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRid()
     * @generated
     * @ordered
     */
    protected String rid = RID_EDEFAULT;

    /**
     * The default value of the '{@link #getStartDate() <em>Start Date</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getStartDate()
     * @generated
     * @ordered
     */
    protected static final XMLGregorianCalendar START_DATE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getStartDate() <em>Start Date</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getStartDate()
     * @generated
     * @ordered
     */
    protected XMLGregorianCalendar startDate = START_DATE_EDEFAULT;

    /**
     * The default value of the '{@link #getVersion() <em>Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getVersion()
     * @generated
     * @ordered
     */
    protected static final Object VERSION_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getVersion() <em>Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getVersion()
     * @generated
     * @ordered
     */
    protected Object version = VERSION_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ResourceIdTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Fes20Package.Literals.RESOURCE_ID_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public XMLGregorianCalendar getEndDate() {
        return endDate;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setEndDate(XMLGregorianCalendar newEndDate) {
        XMLGregorianCalendar oldEndDate = endDate;
        endDate = newEndDate;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Fes20Package.RESOURCE_ID_TYPE__END_DATE, oldEndDate, endDate));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getPreviousRid() {
        return previousRid;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setPreviousRid(String newPreviousRid) {
        String oldPreviousRid = previousRid;
        previousRid = newPreviousRid;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Fes20Package.RESOURCE_ID_TYPE__PREVIOUS_RID, oldPreviousRid, previousRid));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getRid() {
        return rid;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setRid(String newRid) {
        String oldRid = rid;
        rid = newRid;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Fes20Package.RESOURCE_ID_TYPE__RID, oldRid, rid));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public XMLGregorianCalendar getStartDate() {
        return startDate;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setStartDate(XMLGregorianCalendar newStartDate) {
        XMLGregorianCalendar oldStartDate = startDate;
        startDate = newStartDate;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Fes20Package.RESOURCE_ID_TYPE__START_DATE, oldStartDate, startDate));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object getVersion() {
        return version;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setVersion(Object newVersion) {
        Object oldVersion = version;
        version = newVersion;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Fes20Package.RESOURCE_ID_TYPE__VERSION, oldVersion, version));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Fes20Package.RESOURCE_ID_TYPE__END_DATE:
                return getEndDate();
            case Fes20Package.RESOURCE_ID_TYPE__PREVIOUS_RID:
                return getPreviousRid();
            case Fes20Package.RESOURCE_ID_TYPE__RID:
                return getRid();
            case Fes20Package.RESOURCE_ID_TYPE__START_DATE:
                return getStartDate();
            case Fes20Package.RESOURCE_ID_TYPE__VERSION:
                return getVersion();
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
            case Fes20Package.RESOURCE_ID_TYPE__END_DATE:
                setEndDate((XMLGregorianCalendar)newValue);
                return;
            case Fes20Package.RESOURCE_ID_TYPE__PREVIOUS_RID:
                setPreviousRid((String)newValue);
                return;
            case Fes20Package.RESOURCE_ID_TYPE__RID:
                setRid((String)newValue);
                return;
            case Fes20Package.RESOURCE_ID_TYPE__START_DATE:
                setStartDate((XMLGregorianCalendar)newValue);
                return;
            case Fes20Package.RESOURCE_ID_TYPE__VERSION:
                setVersion(newValue);
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
            case Fes20Package.RESOURCE_ID_TYPE__END_DATE:
                setEndDate(END_DATE_EDEFAULT);
                return;
            case Fes20Package.RESOURCE_ID_TYPE__PREVIOUS_RID:
                setPreviousRid(PREVIOUS_RID_EDEFAULT);
                return;
            case Fes20Package.RESOURCE_ID_TYPE__RID:
                setRid(RID_EDEFAULT);
                return;
            case Fes20Package.RESOURCE_ID_TYPE__START_DATE:
                setStartDate(START_DATE_EDEFAULT);
                return;
            case Fes20Package.RESOURCE_ID_TYPE__VERSION:
                setVersion(VERSION_EDEFAULT);
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
            case Fes20Package.RESOURCE_ID_TYPE__END_DATE:
                return END_DATE_EDEFAULT == null ? endDate != null : !END_DATE_EDEFAULT.equals(endDate);
            case Fes20Package.RESOURCE_ID_TYPE__PREVIOUS_RID:
                return PREVIOUS_RID_EDEFAULT == null ? previousRid != null : !PREVIOUS_RID_EDEFAULT.equals(previousRid);
            case Fes20Package.RESOURCE_ID_TYPE__RID:
                return RID_EDEFAULT == null ? rid != null : !RID_EDEFAULT.equals(rid);
            case Fes20Package.RESOURCE_ID_TYPE__START_DATE:
                return START_DATE_EDEFAULT == null ? startDate != null : !START_DATE_EDEFAULT.equals(startDate);
            case Fes20Package.RESOURCE_ID_TYPE__VERSION:
                return VERSION_EDEFAULT == null ? version != null : !VERSION_EDEFAULT.equals(version);
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
        result.append(" (endDate: ");
        result.append(endDate);
        result.append(", previousRid: ");
        result.append(previousRid);
        result.append(", rid: ");
        result.append(rid);
        result.append(", startDate: ");
        result.append(startDate);
        result.append(", version: ");
        result.append(version);
        result.append(')');
        return result.toString();
    }

} //ResourceIdTypeImpl
