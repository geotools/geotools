/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs20.impl;

import java.util.Collection;
import net.opengis.wfs20.InsertType;
import net.opengis.wfs20.Wfs20Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Insert Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wfs20.impl.InsertTypeImpl#getAny <em>Any</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.InsertTypeImpl#getInputFormat <em>Input Format</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.InsertTypeImpl#getSrsName <em>Srs Name</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class InsertTypeImpl extends AbstractTransactionActionTypeImpl implements InsertType {
    /**
     * The cached value of the '{@link #getAny() <em>Any</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAny()
     * @generated
     * @ordered
     */
    protected EList<Object> any;

    /**
     * The default value of the '{@link #getInputFormat() <em>Input Format</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getInputFormat()
     * @generated
     * @ordered
     */
    protected static final String INPUT_FORMAT_EDEFAULT = "application/gml+xml; version=3.2";

    /**
     * The cached value of the '{@link #getInputFormat() <em>Input Format</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getInputFormat()
     * @generated
     * @ordered
     */
    protected String inputFormat = INPUT_FORMAT_EDEFAULT;

    /**
     * This is true if the Input Format attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean inputFormatESet;

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
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected InsertTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Wfs20Package.Literals.INSERT_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<Object> getAny() {
        if (any == null) {
            any = new EDataTypeUniqueEList<Object>(Object.class, this, Wfs20Package.INSERT_TYPE__ANY);
        }
        return any;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getInputFormat() {
        return inputFormat;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setInputFormat(String newInputFormat) {
        String oldInputFormat = inputFormat;
        inputFormat = newInputFormat;
        boolean oldInputFormatESet = inputFormatESet;
        inputFormatESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wfs20Package.INSERT_TYPE__INPUT_FORMAT, oldInputFormat, inputFormat, !oldInputFormatESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetInputFormat() {
        String oldInputFormat = inputFormat;
        boolean oldInputFormatESet = inputFormatESet;
        inputFormat = INPUT_FORMAT_EDEFAULT;
        inputFormatESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Wfs20Package.INSERT_TYPE__INPUT_FORMAT, oldInputFormat, INPUT_FORMAT_EDEFAULT, oldInputFormatESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetInputFormat() {
        return inputFormatESet;
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
            eNotify(new ENotificationImpl(this, Notification.SET, Wfs20Package.INSERT_TYPE__SRS_NAME, oldSrsName, srsName));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Wfs20Package.INSERT_TYPE__ANY:
                return getAny();
            case Wfs20Package.INSERT_TYPE__INPUT_FORMAT:
                return getInputFormat();
            case Wfs20Package.INSERT_TYPE__SRS_NAME:
                return getSrsName();
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
            case Wfs20Package.INSERT_TYPE__ANY:
                getAny().clear();
                getAny().addAll((Collection<? extends Object>)newValue);
                return;
            case Wfs20Package.INSERT_TYPE__INPUT_FORMAT:
                setInputFormat((String)newValue);
                return;
            case Wfs20Package.INSERT_TYPE__SRS_NAME:
                setSrsName((String)newValue);
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
            case Wfs20Package.INSERT_TYPE__ANY:
                getAny().clear();
                return;
            case Wfs20Package.INSERT_TYPE__INPUT_FORMAT:
                unsetInputFormat();
                return;
            case Wfs20Package.INSERT_TYPE__SRS_NAME:
                setSrsName(SRS_NAME_EDEFAULT);
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
            case Wfs20Package.INSERT_TYPE__ANY:
                return any != null && !any.isEmpty();
            case Wfs20Package.INSERT_TYPE__INPUT_FORMAT:
                return isSetInputFormat();
            case Wfs20Package.INSERT_TYPE__SRS_NAME:
                return SRS_NAME_EDEFAULT == null ? srsName != null : !SRS_NAME_EDEFAULT.equals(srsName);
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
        result.append(" (any: ");
        result.append(any);
        result.append(", inputFormat: ");
        if (inputFormatESet) result.append(inputFormat); else result.append("<unset>");
        result.append(", srsName: ");
        result.append(srsName);
        result.append(')');
        return result.toString();
    }

} //InsertTypeImpl
