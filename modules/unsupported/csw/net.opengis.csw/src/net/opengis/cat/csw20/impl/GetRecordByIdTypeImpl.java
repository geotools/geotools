/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.cat.csw20.impl;

import java.lang.String;

import net.opengis.cat.csw20.Csw20Package;
import net.opengis.cat.csw20.ElementSetNameType;
import net.opengis.cat.csw20.GetRecordByIdType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Get Record By Id Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.cat.csw20.impl.GetRecordByIdTypeImpl#getId <em>Id</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.GetRecordByIdTypeImpl#getElementSetName <em>Element Set Name</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.GetRecordByIdTypeImpl#getOutputFormat <em>Output Format</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.GetRecordByIdTypeImpl#getOutputSchema <em>Output Schema</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class GetRecordByIdTypeImpl extends RequestBaseTypeImpl implements GetRecordByIdType {
    /**
     * The default value of the '{@link #getId() <em>Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getId()
     * @generated
     * @ordered
     */
    protected static final String ID_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getId() <em>Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getId()
     * @generated
     * @ordered
     */
    protected String id = ID_EDEFAULT;

    /**
     * The cached value of the '{@link #getElementSetName() <em>Element Set Name</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getElementSetName()
     * @generated
     * @ordered
     */
    protected ElementSetNameType elementSetName;

    /**
     * The default value of the '{@link #getOutputFormat() <em>Output Format</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getOutputFormat()
     * @generated
     * @ordered
     */
    protected static final String OUTPUT_FORMAT_EDEFAULT = "application/xml";

    /**
     * The cached value of the '{@link #getOutputFormat() <em>Output Format</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getOutputFormat()
     * @generated
     * @ordered
     */
    protected String outputFormat = OUTPUT_FORMAT_EDEFAULT;

    /**
     * This is true if the Output Format attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean outputFormatESet;

    /**
     * The default value of the '{@link #getOutputSchema() <em>Output Schema</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getOutputSchema()
     * @generated
     * @ordered
     */
    protected static final String OUTPUT_SCHEMA_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getOutputSchema() <em>Output Schema</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getOutputSchema()
     * @generated
     * @ordered
     */
    protected String outputSchema = OUTPUT_SCHEMA_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected GetRecordByIdTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Csw20Package.Literals.GET_RECORD_BY_ID_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getId() {
        return id;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setId(String newId) {
        String oldId = id;
        id = newId;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.GET_RECORD_BY_ID_TYPE__ID, oldId, id));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ElementSetNameType getElementSetName() {
        return elementSetName;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetElementSetName(ElementSetNameType newElementSetName, NotificationChain msgs) {
        ElementSetNameType oldElementSetName = elementSetName;
        elementSetName = newElementSetName;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Csw20Package.GET_RECORD_BY_ID_TYPE__ELEMENT_SET_NAME, oldElementSetName, newElementSetName);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setElementSetName(ElementSetNameType newElementSetName) {
        if (newElementSetName != elementSetName) {
            NotificationChain msgs = null;
            if (elementSetName != null)
                msgs = ((InternalEObject)elementSetName).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Csw20Package.GET_RECORD_BY_ID_TYPE__ELEMENT_SET_NAME, null, msgs);
            if (newElementSetName != null)
                msgs = ((InternalEObject)newElementSetName).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Csw20Package.GET_RECORD_BY_ID_TYPE__ELEMENT_SET_NAME, null, msgs);
            msgs = basicSetElementSetName(newElementSetName, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.GET_RECORD_BY_ID_TYPE__ELEMENT_SET_NAME, newElementSetName, newElementSetName));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getOutputFormat() {
        return outputFormat;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setOutputFormat(String newOutputFormat) {
        String oldOutputFormat = outputFormat;
        outputFormat = newOutputFormat;
        boolean oldOutputFormatESet = outputFormatESet;
        outputFormatESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.GET_RECORD_BY_ID_TYPE__OUTPUT_FORMAT, oldOutputFormat, outputFormat, !oldOutputFormatESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetOutputFormat() {
        String oldOutputFormat = outputFormat;
        boolean oldOutputFormatESet = outputFormatESet;
        outputFormat = OUTPUT_FORMAT_EDEFAULT;
        outputFormatESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Csw20Package.GET_RECORD_BY_ID_TYPE__OUTPUT_FORMAT, oldOutputFormat, OUTPUT_FORMAT_EDEFAULT, oldOutputFormatESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetOutputFormat() {
        return outputFormatESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getOutputSchema() {
        return outputSchema;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setOutputSchema(String newOutputSchema) {
        String oldOutputSchema = outputSchema;
        outputSchema = newOutputSchema;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.GET_RECORD_BY_ID_TYPE__OUTPUT_SCHEMA, oldOutputSchema, outputSchema));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Csw20Package.GET_RECORD_BY_ID_TYPE__ELEMENT_SET_NAME:
                return basicSetElementSetName(null, msgs);
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
            case Csw20Package.GET_RECORD_BY_ID_TYPE__ID:
                return getId();
            case Csw20Package.GET_RECORD_BY_ID_TYPE__ELEMENT_SET_NAME:
                return getElementSetName();
            case Csw20Package.GET_RECORD_BY_ID_TYPE__OUTPUT_FORMAT:
                return getOutputFormat();
            case Csw20Package.GET_RECORD_BY_ID_TYPE__OUTPUT_SCHEMA:
                return getOutputSchema();
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
            case Csw20Package.GET_RECORD_BY_ID_TYPE__ID:
                setId((String)newValue);
                return;
            case Csw20Package.GET_RECORD_BY_ID_TYPE__ELEMENT_SET_NAME:
                setElementSetName((ElementSetNameType)newValue);
                return;
            case Csw20Package.GET_RECORD_BY_ID_TYPE__OUTPUT_FORMAT:
                setOutputFormat((String)newValue);
                return;
            case Csw20Package.GET_RECORD_BY_ID_TYPE__OUTPUT_SCHEMA:
                setOutputSchema((String)newValue);
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
            case Csw20Package.GET_RECORD_BY_ID_TYPE__ID:
                setId(ID_EDEFAULT);
                return;
            case Csw20Package.GET_RECORD_BY_ID_TYPE__ELEMENT_SET_NAME:
                setElementSetName((ElementSetNameType)null);
                return;
            case Csw20Package.GET_RECORD_BY_ID_TYPE__OUTPUT_FORMAT:
                unsetOutputFormat();
                return;
            case Csw20Package.GET_RECORD_BY_ID_TYPE__OUTPUT_SCHEMA:
                setOutputSchema(OUTPUT_SCHEMA_EDEFAULT);
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
            case Csw20Package.GET_RECORD_BY_ID_TYPE__ID:
                return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
            case Csw20Package.GET_RECORD_BY_ID_TYPE__ELEMENT_SET_NAME:
                return elementSetName != null;
            case Csw20Package.GET_RECORD_BY_ID_TYPE__OUTPUT_FORMAT:
                return isSetOutputFormat();
            case Csw20Package.GET_RECORD_BY_ID_TYPE__OUTPUT_SCHEMA:
                return OUTPUT_SCHEMA_EDEFAULT == null ? outputSchema != null : !OUTPUT_SCHEMA_EDEFAULT.equals(outputSchema);
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
        result.append(" (id: ");
        result.append(id);
        result.append(", outputFormat: ");
        if (outputFormatESet) result.append(outputFormat); else result.append("<unset>");
        result.append(", outputSchema: ");
        result.append(outputSchema);
        result.append(')');
        return result.toString();
    }

} //GetRecordByIdTypeImpl
