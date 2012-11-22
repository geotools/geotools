/**
 */
package org.w3.xlink.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

import org.w3.xlink.ActuateType;
import org.w3.xlink.ArcType;
import org.w3.xlink.ShowType;
import org.w3.xlink.TypeType;
import org.w3.xlink.XlinkPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Arc Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.w3.xlink.impl.ArcTypeImpl#getTitleGroup <em>Title Group</em>}</li>
 *   <li>{@link org.w3.xlink.impl.ArcTypeImpl#getTitle <em>Title</em>}</li>
 *   <li>{@link org.w3.xlink.impl.ArcTypeImpl#getActuate <em>Actuate</em>}</li>
 *   <li>{@link org.w3.xlink.impl.ArcTypeImpl#getArcrole <em>Arcrole</em>}</li>
 *   <li>{@link org.w3.xlink.impl.ArcTypeImpl#getFrom <em>From</em>}</li>
 *   <li>{@link org.w3.xlink.impl.ArcTypeImpl#getShow <em>Show</em>}</li>
 *   <li>{@link org.w3.xlink.impl.ArcTypeImpl#getTitle1 <em>Title1</em>}</li>
 *   <li>{@link org.w3.xlink.impl.ArcTypeImpl#getTo <em>To</em>}</li>
 *   <li>{@link org.w3.xlink.impl.ArcTypeImpl#getType <em>Type</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ArcTypeImpl extends EObjectImpl implements ArcType {
    /**
     * The cached value of the '{@link #getTitleGroup() <em>Title Group</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTitleGroup()
     * @generated
     * @ordered
     */
    protected FeatureMap titleGroup;

    /**
     * The default value of the '{@link #getActuate() <em>Actuate</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getActuate()
     * @generated
     * @ordered
     */
    protected static final ActuateType ACTUATE_EDEFAULT = ActuateType.ON_LOAD_LITERAL;

    /**
     * The cached value of the '{@link #getActuate() <em>Actuate</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getActuate()
     * @generated
     * @ordered
     */
    protected ActuateType actuate = ACTUATE_EDEFAULT;

    /**
     * This is true if the Actuate attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean actuateESet;

    /**
     * The default value of the '{@link #getArcrole() <em>Arcrole</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getArcrole()
     * @generated
     * @ordered
     */
    protected static final String ARCROLE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getArcrole() <em>Arcrole</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getArcrole()
     * @generated
     * @ordered
     */
    protected String arcrole = ARCROLE_EDEFAULT;

    /**
     * The default value of the '{@link #getFrom() <em>From</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFrom()
     * @generated
     * @ordered
     */
    protected static final String FROM_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getFrom() <em>From</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFrom()
     * @generated
     * @ordered
     */
    protected String from = FROM_EDEFAULT;

    /**
     * The default value of the '{@link #getShow() <em>Show</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getShow()
     * @generated
     * @ordered
     */
    protected static final ShowType SHOW_EDEFAULT = ShowType.NEW_LITERAL;

    /**
     * The cached value of the '{@link #getShow() <em>Show</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getShow()
     * @generated
     * @ordered
     */
    protected ShowType show = SHOW_EDEFAULT;

    /**
     * This is true if the Show attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean showESet;

    /**
     * The default value of the '{@link #getTitle1() <em>Title1</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTitle1()
     * @generated
     * @ordered
     */
    protected static final String TITLE1_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getTitle1() <em>Title1</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTitle1()
     * @generated
     * @ordered
     */
    protected String title1 = TITLE1_EDEFAULT;

    /**
     * The default value of the '{@link #getTo() <em>To</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTo()
     * @generated
     * @ordered
     */
    protected static final String TO_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getTo() <em>To</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTo()
     * @generated
     * @ordered
     */
    protected String to = TO_EDEFAULT;

    /**
     * The default value of the '{@link #getType() <em>Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getType()
     * @generated
     * @ordered
     */
    protected static final TypeType TYPE_EDEFAULT = TypeType.ARC_LITERAL;

    /**
     * The cached value of the '{@link #getType() <em>Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getType()
     * @generated
     * @ordered
     */
    protected TypeType type = TYPE_EDEFAULT;

    /**
     * This is true if the Type attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean typeESet;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ArcTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return XlinkPackage.Literals.ARC_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureMap getTitleGroup() {
        if (titleGroup == null) {
            titleGroup = new BasicFeatureMap(this, XlinkPackage.ARC_TYPE__TITLE_GROUP);
        }
        return titleGroup;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList getTitle() {
        return getTitleGroup().list(XlinkPackage.Literals.ARC_TYPE__TITLE);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ActuateType getActuate() {
        return actuate;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setActuate(ActuateType newActuate) {
        ActuateType oldActuate = actuate;
        actuate = newActuate == null ? ACTUATE_EDEFAULT : newActuate;
        boolean oldActuateESet = actuateESet;
        actuateESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, XlinkPackage.ARC_TYPE__ACTUATE, oldActuate, actuate, !oldActuateESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetActuate() {
        ActuateType oldActuate = actuate;
        boolean oldActuateESet = actuateESet;
        actuate = ACTUATE_EDEFAULT;
        actuateESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, XlinkPackage.ARC_TYPE__ACTUATE, oldActuate, ACTUATE_EDEFAULT, oldActuateESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetActuate() {
        return actuateESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getArcrole() {
        return arcrole;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setArcrole(String newArcrole) {
        String oldArcrole = arcrole;
        arcrole = newArcrole;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, XlinkPackage.ARC_TYPE__ARCROLE, oldArcrole, arcrole));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getFrom() {
        return from;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setFrom(String newFrom) {
        String oldFrom = from;
        from = newFrom;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, XlinkPackage.ARC_TYPE__FROM, oldFrom, from));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ShowType getShow() {
        return show;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setShow(ShowType newShow) {
        ShowType oldShow = show;
        show = newShow == null ? SHOW_EDEFAULT : newShow;
        boolean oldShowESet = showESet;
        showESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, XlinkPackage.ARC_TYPE__SHOW, oldShow, show, !oldShowESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetShow() {
        ShowType oldShow = show;
        boolean oldShowESet = showESet;
        show = SHOW_EDEFAULT;
        showESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, XlinkPackage.ARC_TYPE__SHOW, oldShow, SHOW_EDEFAULT, oldShowESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetShow() {
        return showESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getTitle1() {
        return title1;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTitle1(String newTitle1) {
        String oldTitle1 = title1;
        title1 = newTitle1;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, XlinkPackage.ARC_TYPE__TITLE1, oldTitle1, title1));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getTo() {
        return to;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTo(String newTo) {
        String oldTo = to;
        to = newTo;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, XlinkPackage.ARC_TYPE__TO, oldTo, to));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TypeType getType() {
        return type;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setType(TypeType newType) {
        TypeType oldType = type;
        type = newType == null ? TYPE_EDEFAULT : newType;
        boolean oldTypeESet = typeESet;
        typeESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, XlinkPackage.ARC_TYPE__TYPE, oldType, type, !oldTypeESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetType() {
        TypeType oldType = type;
        boolean oldTypeESet = typeESet;
        type = TYPE_EDEFAULT;
        typeESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, XlinkPackage.ARC_TYPE__TYPE, oldType, TYPE_EDEFAULT, oldTypeESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetType() {
        return typeESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case XlinkPackage.ARC_TYPE__TITLE_GROUP:
                return ((InternalEList)getTitleGroup()).basicRemove(otherEnd, msgs);
            case XlinkPackage.ARC_TYPE__TITLE:
                return ((InternalEList)getTitle()).basicRemove(otherEnd, msgs);
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
            case XlinkPackage.ARC_TYPE__TITLE_GROUP:
                if (coreType) return getTitleGroup();
                return ((FeatureMap.Internal)getTitleGroup()).getWrapper();
            case XlinkPackage.ARC_TYPE__TITLE:
                return getTitle();
            case XlinkPackage.ARC_TYPE__ACTUATE:
                return getActuate();
            case XlinkPackage.ARC_TYPE__ARCROLE:
                return getArcrole();
            case XlinkPackage.ARC_TYPE__FROM:
                return getFrom();
            case XlinkPackage.ARC_TYPE__SHOW:
                return getShow();
            case XlinkPackage.ARC_TYPE__TITLE1:
                return getTitle1();
            case XlinkPackage.ARC_TYPE__TO:
                return getTo();
            case XlinkPackage.ARC_TYPE__TYPE:
                return getType();
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
            case XlinkPackage.ARC_TYPE__TITLE_GROUP:
                ((FeatureMap.Internal)getTitleGroup()).set(newValue);
                return;
            case XlinkPackage.ARC_TYPE__ACTUATE:
                setActuate((ActuateType)newValue);
                return;
            case XlinkPackage.ARC_TYPE__ARCROLE:
                setArcrole((String)newValue);
                return;
            case XlinkPackage.ARC_TYPE__FROM:
                setFrom((String)newValue);
                return;
            case XlinkPackage.ARC_TYPE__SHOW:
                setShow((ShowType)newValue);
                return;
            case XlinkPackage.ARC_TYPE__TITLE1:
                setTitle1((String)newValue);
                return;
            case XlinkPackage.ARC_TYPE__TO:
                setTo((String)newValue);
                return;
            case XlinkPackage.ARC_TYPE__TYPE:
                setType((TypeType)newValue);
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
            case XlinkPackage.ARC_TYPE__TITLE_GROUP:
                getTitleGroup().clear();
                return;
            case XlinkPackage.ARC_TYPE__ACTUATE:
                unsetActuate();
                return;
            case XlinkPackage.ARC_TYPE__ARCROLE:
                setArcrole(ARCROLE_EDEFAULT);
                return;
            case XlinkPackage.ARC_TYPE__FROM:
                setFrom(FROM_EDEFAULT);
                return;
            case XlinkPackage.ARC_TYPE__SHOW:
                unsetShow();
                return;
            case XlinkPackage.ARC_TYPE__TITLE1:
                setTitle1(TITLE1_EDEFAULT);
                return;
            case XlinkPackage.ARC_TYPE__TO:
                setTo(TO_EDEFAULT);
                return;
            case XlinkPackage.ARC_TYPE__TYPE:
                unsetType();
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
            case XlinkPackage.ARC_TYPE__TITLE_GROUP:
                return titleGroup != null && !titleGroup.isEmpty();
            case XlinkPackage.ARC_TYPE__TITLE:
                return !getTitle().isEmpty();
            case XlinkPackage.ARC_TYPE__ACTUATE:
                return isSetActuate();
            case XlinkPackage.ARC_TYPE__ARCROLE:
                return ARCROLE_EDEFAULT == null ? arcrole != null : !ARCROLE_EDEFAULT.equals(arcrole);
            case XlinkPackage.ARC_TYPE__FROM:
                return FROM_EDEFAULT == null ? from != null : !FROM_EDEFAULT.equals(from);
            case XlinkPackage.ARC_TYPE__SHOW:
                return isSetShow();
            case XlinkPackage.ARC_TYPE__TITLE1:
                return TITLE1_EDEFAULT == null ? title1 != null : !TITLE1_EDEFAULT.equals(title1);
            case XlinkPackage.ARC_TYPE__TO:
                return TO_EDEFAULT == null ? to != null : !TO_EDEFAULT.equals(to);
            case XlinkPackage.ARC_TYPE__TYPE:
                return isSetType();
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
        result.append(" (titleGroup: ");
        result.append(titleGroup);
        result.append(", actuate: ");
        if (actuateESet) result.append(actuate); else result.append("<unset>");
        result.append(", arcrole: ");
        result.append(arcrole);
        result.append(", from: ");
        result.append(from);
        result.append(", show: ");
        if (showESet) result.append(show); else result.append("<unset>");
        result.append(", title1: ");
        result.append(title1);
        result.append(", to: ");
        result.append(to);
        result.append(", type: ");
        if (typeESet) result.append(type); else result.append("<unset>");
        result.append(')');
        return result.toString();
    }

} //ArcTypeImpl
