/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.cat.csw20.impl;

import java.lang.String;

import javax.xml.namespace.QName;

import net.opengis.cat.csw20.ConceptualSchemeType;
import net.opengis.cat.csw20.Csw20Package;
import net.opengis.cat.csw20.DomainValuesType;
import net.opengis.cat.csw20.ListOfValuesType;
import net.opengis.cat.csw20.RangeOfValuesType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Domain Values Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.cat.csw20.impl.DomainValuesTypeImpl#getPropertyName <em>Property Name</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.DomainValuesTypeImpl#getParameterName <em>Parameter Name</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.DomainValuesTypeImpl#getListOfValues <em>List Of Values</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.DomainValuesTypeImpl#getConceptualScheme <em>Conceptual Scheme</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.DomainValuesTypeImpl#getRangeOfValues <em>Range Of Values</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.DomainValuesTypeImpl#getType <em>Type</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.DomainValuesTypeImpl#getUom <em>Uom</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DomainValuesTypeImpl extends EObjectImpl implements DomainValuesType {
    /**
     * The default value of the '{@link #getPropertyName() <em>Property Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPropertyName()
     * @generated
     * @ordered
     */
    protected static final String PROPERTY_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getPropertyName() <em>Property Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPropertyName()
     * @generated
     * @ordered
     */
    protected String propertyName = PROPERTY_NAME_EDEFAULT;

    /**
     * The default value of the '{@link #getParameterName() <em>Parameter Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getParameterName()
     * @generated
     * @ordered
     */
    protected static final String PARAMETER_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getParameterName() <em>Parameter Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getParameterName()
     * @generated
     * @ordered
     */
    protected String parameterName = PARAMETER_NAME_EDEFAULT;

    /**
     * The cached value of the '{@link #getListOfValues() <em>List Of Values</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getListOfValues()
     * @generated
     * @ordered
     */
    protected ListOfValuesType listOfValues;

    /**
     * The cached value of the '{@link #getConceptualScheme() <em>Conceptual Scheme</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getConceptualScheme()
     * @generated
     * @ordered
     */
    protected ConceptualSchemeType conceptualScheme;

    /**
     * The cached value of the '{@link #getRangeOfValues() <em>Range Of Values</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRangeOfValues()
     * @generated
     * @ordered
     */
    protected RangeOfValuesType rangeOfValues;

    /**
     * The default value of the '{@link #getType() <em>Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getType()
     * @generated
     * @ordered
     */
    protected static final QName TYPE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getType() <em>Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getType()
     * @generated
     * @ordered
     */
    protected QName type = TYPE_EDEFAULT;

    /**
     * The default value of the '{@link #getUom() <em>Uom</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getUom()
     * @generated
     * @ordered
     */
    protected static final String UOM_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getUom() <em>Uom</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getUom()
     * @generated
     * @ordered
     */
    protected String uom = UOM_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected DomainValuesTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Csw20Package.Literals.DOMAIN_VALUES_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setPropertyName(String newPropertyName) {
        String oldPropertyName = propertyName;
        propertyName = newPropertyName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.DOMAIN_VALUES_TYPE__PROPERTY_NAME, oldPropertyName, propertyName));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getParameterName() {
        return parameterName;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setParameterName(String newParameterName) {
        String oldParameterName = parameterName;
        parameterName = newParameterName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.DOMAIN_VALUES_TYPE__PARAMETER_NAME, oldParameterName, parameterName));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ListOfValuesType getListOfValues() {
        return listOfValues;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetListOfValues(ListOfValuesType newListOfValues, NotificationChain msgs) {
        ListOfValuesType oldListOfValues = listOfValues;
        listOfValues = newListOfValues;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Csw20Package.DOMAIN_VALUES_TYPE__LIST_OF_VALUES, oldListOfValues, newListOfValues);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setListOfValues(ListOfValuesType newListOfValues) {
        if (newListOfValues != listOfValues) {
            NotificationChain msgs = null;
            if (listOfValues != null)
                msgs = ((InternalEObject)listOfValues).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Csw20Package.DOMAIN_VALUES_TYPE__LIST_OF_VALUES, null, msgs);
            if (newListOfValues != null)
                msgs = ((InternalEObject)newListOfValues).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Csw20Package.DOMAIN_VALUES_TYPE__LIST_OF_VALUES, null, msgs);
            msgs = basicSetListOfValues(newListOfValues, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.DOMAIN_VALUES_TYPE__LIST_OF_VALUES, newListOfValues, newListOfValues));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ConceptualSchemeType getConceptualScheme() {
        return conceptualScheme;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetConceptualScheme(ConceptualSchemeType newConceptualScheme, NotificationChain msgs) {
        ConceptualSchemeType oldConceptualScheme = conceptualScheme;
        conceptualScheme = newConceptualScheme;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Csw20Package.DOMAIN_VALUES_TYPE__CONCEPTUAL_SCHEME, oldConceptualScheme, newConceptualScheme);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setConceptualScheme(ConceptualSchemeType newConceptualScheme) {
        if (newConceptualScheme != conceptualScheme) {
            NotificationChain msgs = null;
            if (conceptualScheme != null)
                msgs = ((InternalEObject)conceptualScheme).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Csw20Package.DOMAIN_VALUES_TYPE__CONCEPTUAL_SCHEME, null, msgs);
            if (newConceptualScheme != null)
                msgs = ((InternalEObject)newConceptualScheme).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Csw20Package.DOMAIN_VALUES_TYPE__CONCEPTUAL_SCHEME, null, msgs);
            msgs = basicSetConceptualScheme(newConceptualScheme, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.DOMAIN_VALUES_TYPE__CONCEPTUAL_SCHEME, newConceptualScheme, newConceptualScheme));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RangeOfValuesType getRangeOfValues() {
        return rangeOfValues;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetRangeOfValues(RangeOfValuesType newRangeOfValues, NotificationChain msgs) {
        RangeOfValuesType oldRangeOfValues = rangeOfValues;
        rangeOfValues = newRangeOfValues;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Csw20Package.DOMAIN_VALUES_TYPE__RANGE_OF_VALUES, oldRangeOfValues, newRangeOfValues);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setRangeOfValues(RangeOfValuesType newRangeOfValues) {
        if (newRangeOfValues != rangeOfValues) {
            NotificationChain msgs = null;
            if (rangeOfValues != null)
                msgs = ((InternalEObject)rangeOfValues).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Csw20Package.DOMAIN_VALUES_TYPE__RANGE_OF_VALUES, null, msgs);
            if (newRangeOfValues != null)
                msgs = ((InternalEObject)newRangeOfValues).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Csw20Package.DOMAIN_VALUES_TYPE__RANGE_OF_VALUES, null, msgs);
            msgs = basicSetRangeOfValues(newRangeOfValues, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.DOMAIN_VALUES_TYPE__RANGE_OF_VALUES, newRangeOfValues, newRangeOfValues));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public QName getType() {
        return type;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setType(QName newType) {
        QName oldType = type;
        type = newType;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.DOMAIN_VALUES_TYPE__TYPE, oldType, type));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getUom() {
        return uom;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setUom(String newUom) {
        String oldUom = uom;
        uom = newUom;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.DOMAIN_VALUES_TYPE__UOM, oldUom, uom));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Csw20Package.DOMAIN_VALUES_TYPE__LIST_OF_VALUES:
                return basicSetListOfValues(null, msgs);
            case Csw20Package.DOMAIN_VALUES_TYPE__CONCEPTUAL_SCHEME:
                return basicSetConceptualScheme(null, msgs);
            case Csw20Package.DOMAIN_VALUES_TYPE__RANGE_OF_VALUES:
                return basicSetRangeOfValues(null, msgs);
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
            case Csw20Package.DOMAIN_VALUES_TYPE__PROPERTY_NAME:
                return getPropertyName();
            case Csw20Package.DOMAIN_VALUES_TYPE__PARAMETER_NAME:
                return getParameterName();
            case Csw20Package.DOMAIN_VALUES_TYPE__LIST_OF_VALUES:
                return getListOfValues();
            case Csw20Package.DOMAIN_VALUES_TYPE__CONCEPTUAL_SCHEME:
                return getConceptualScheme();
            case Csw20Package.DOMAIN_VALUES_TYPE__RANGE_OF_VALUES:
                return getRangeOfValues();
            case Csw20Package.DOMAIN_VALUES_TYPE__TYPE:
                return getType();
            case Csw20Package.DOMAIN_VALUES_TYPE__UOM:
                return getUom();
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
            case Csw20Package.DOMAIN_VALUES_TYPE__PROPERTY_NAME:
                setPropertyName((String)newValue);
                return;
            case Csw20Package.DOMAIN_VALUES_TYPE__PARAMETER_NAME:
                setParameterName((String)newValue);
                return;
            case Csw20Package.DOMAIN_VALUES_TYPE__LIST_OF_VALUES:
                setListOfValues((ListOfValuesType)newValue);
                return;
            case Csw20Package.DOMAIN_VALUES_TYPE__CONCEPTUAL_SCHEME:
                setConceptualScheme((ConceptualSchemeType)newValue);
                return;
            case Csw20Package.DOMAIN_VALUES_TYPE__RANGE_OF_VALUES:
                setRangeOfValues((RangeOfValuesType)newValue);
                return;
            case Csw20Package.DOMAIN_VALUES_TYPE__TYPE:
                setType((QName) newValue);
                return;
            case Csw20Package.DOMAIN_VALUES_TYPE__UOM:
                setUom((String)newValue);
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
            case Csw20Package.DOMAIN_VALUES_TYPE__PROPERTY_NAME:
                setPropertyName(PROPERTY_NAME_EDEFAULT);
                return;
            case Csw20Package.DOMAIN_VALUES_TYPE__PARAMETER_NAME:
                setParameterName(PARAMETER_NAME_EDEFAULT);
                return;
            case Csw20Package.DOMAIN_VALUES_TYPE__LIST_OF_VALUES:
                setListOfValues((ListOfValuesType)null);
                return;
            case Csw20Package.DOMAIN_VALUES_TYPE__CONCEPTUAL_SCHEME:
                setConceptualScheme((ConceptualSchemeType)null);
                return;
            case Csw20Package.DOMAIN_VALUES_TYPE__RANGE_OF_VALUES:
                setRangeOfValues((RangeOfValuesType)null);
                return;
            case Csw20Package.DOMAIN_VALUES_TYPE__TYPE:
                setType(TYPE_EDEFAULT);
                return;
            case Csw20Package.DOMAIN_VALUES_TYPE__UOM:
                setUom(UOM_EDEFAULT);
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
            case Csw20Package.DOMAIN_VALUES_TYPE__PROPERTY_NAME:
                return PROPERTY_NAME_EDEFAULT == null ? propertyName != null : !PROPERTY_NAME_EDEFAULT.equals(propertyName);
            case Csw20Package.DOMAIN_VALUES_TYPE__PARAMETER_NAME:
                return PARAMETER_NAME_EDEFAULT == null ? parameterName != null : !PARAMETER_NAME_EDEFAULT.equals(parameterName);
            case Csw20Package.DOMAIN_VALUES_TYPE__LIST_OF_VALUES:
                return listOfValues != null;
            case Csw20Package.DOMAIN_VALUES_TYPE__CONCEPTUAL_SCHEME:
                return conceptualScheme != null;
            case Csw20Package.DOMAIN_VALUES_TYPE__RANGE_OF_VALUES:
                return rangeOfValues != null;
            case Csw20Package.DOMAIN_VALUES_TYPE__TYPE:
                return TYPE_EDEFAULT == null ? type != null : !TYPE_EDEFAULT.equals(type);
            case Csw20Package.DOMAIN_VALUES_TYPE__UOM:
                return UOM_EDEFAULT == null ? uom != null : !UOM_EDEFAULT.equals(uom);
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
        result.append(" (propertyName: ");
        result.append(propertyName);
        result.append(", parameterName: ");
        result.append(parameterName);
        result.append(", type: ");
        result.append(type);
        result.append(", uom: ");
        result.append(uom);
        result.append(')');
        return result.toString();
    }

} //DomainValuesTypeImpl
