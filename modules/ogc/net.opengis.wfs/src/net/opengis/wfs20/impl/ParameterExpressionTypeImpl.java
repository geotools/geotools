/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs20.impl;

import java.util.Collection;

import javax.xml.namespace.QName;

import net.opengis.ows11.MetadataType;

import net.opengis.wfs20.AbstractType;
import net.opengis.wfs20.ParameterExpressionType;
import net.opengis.wfs20.TitleType;
import net.opengis.wfs20.Wfs20Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Parameter Expression Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wfs20.impl.ParameterExpressionTypeImpl#getTitle <em>Title</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.ParameterExpressionTypeImpl#getAbstract <em>Abstract</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.ParameterExpressionTypeImpl#getMetadata <em>Metadata</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.ParameterExpressionTypeImpl#getName <em>Name</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.ParameterExpressionTypeImpl#getType <em>Type</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ParameterExpressionTypeImpl extends EObjectImpl implements ParameterExpressionType {
    /**
     * The cached value of the '{@link #getTitle() <em>Title</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTitle()
     * @generated
     * @ordered
     */
    protected EList<TitleType> title;

    /**
     * The cached value of the '{@link #getAbstract() <em>Abstract</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAbstract()
     * @generated
     * @ordered
     */
    protected EList<AbstractType> abstract_;

    /**
     * The cached value of the '{@link #getMetadata() <em>Metadata</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMetadata()
     * @generated
     * @ordered
     */
    protected EList<MetadataType> metadata;

    /**
     * The default value of the '{@link #getName() <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getName()
     * @generated
     * @ordered
     */
    protected static final String NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getName()
     * @generated
     * @ordered
     */
    protected String name = NAME_EDEFAULT;

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
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ParameterExpressionTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Wfs20Package.Literals.PARAMETER_EXPRESSION_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<TitleType> getTitle() {
        if (title == null) {
            title = new EObjectContainmentEList<TitleType>(TitleType.class, this, Wfs20Package.PARAMETER_EXPRESSION_TYPE__TITLE);
        }
        return title;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<AbstractType> getAbstract() {
        if (abstract_ == null) {
            abstract_ = new EObjectContainmentEList<AbstractType>(AbstractType.class, this, Wfs20Package.PARAMETER_EXPRESSION_TYPE__ABSTRACT);
        }
        return abstract_;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<MetadataType> getMetadata() {
        if (metadata == null) {
            metadata = new EObjectContainmentEList<MetadataType>(MetadataType.class, this, Wfs20Package.PARAMETER_EXPRESSION_TYPE__METADATA);
        }
        return metadata;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getName() {
        return name;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setName(String newName) {
        String oldName = name;
        name = newName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wfs20Package.PARAMETER_EXPRESSION_TYPE__NAME, oldName, name));
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
            eNotify(new ENotificationImpl(this, Notification.SET, Wfs20Package.PARAMETER_EXPRESSION_TYPE__TYPE, oldType, type));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Wfs20Package.PARAMETER_EXPRESSION_TYPE__TITLE:
                return ((InternalEList<?>)getTitle()).basicRemove(otherEnd, msgs);
            case Wfs20Package.PARAMETER_EXPRESSION_TYPE__ABSTRACT:
                return ((InternalEList<?>)getAbstract()).basicRemove(otherEnd, msgs);
            case Wfs20Package.PARAMETER_EXPRESSION_TYPE__METADATA:
                return ((InternalEList<?>)getMetadata()).basicRemove(otherEnd, msgs);
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
            case Wfs20Package.PARAMETER_EXPRESSION_TYPE__TITLE:
                return getTitle();
            case Wfs20Package.PARAMETER_EXPRESSION_TYPE__ABSTRACT:
                return getAbstract();
            case Wfs20Package.PARAMETER_EXPRESSION_TYPE__METADATA:
                return getMetadata();
            case Wfs20Package.PARAMETER_EXPRESSION_TYPE__NAME:
                return getName();
            case Wfs20Package.PARAMETER_EXPRESSION_TYPE__TYPE:
                return getType();
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
            case Wfs20Package.PARAMETER_EXPRESSION_TYPE__TITLE:
                getTitle().clear();
                getTitle().addAll((Collection<? extends TitleType>)newValue);
                return;
            case Wfs20Package.PARAMETER_EXPRESSION_TYPE__ABSTRACT:
                getAbstract().clear();
                getAbstract().addAll((Collection<? extends AbstractType>)newValue);
                return;
            case Wfs20Package.PARAMETER_EXPRESSION_TYPE__METADATA:
                getMetadata().clear();
                getMetadata().addAll((Collection<? extends MetadataType>)newValue);
                return;
            case Wfs20Package.PARAMETER_EXPRESSION_TYPE__NAME:
                setName((String)newValue);
                return;
            case Wfs20Package.PARAMETER_EXPRESSION_TYPE__TYPE:
                setType((QName)newValue);
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
            case Wfs20Package.PARAMETER_EXPRESSION_TYPE__TITLE:
                getTitle().clear();
                return;
            case Wfs20Package.PARAMETER_EXPRESSION_TYPE__ABSTRACT:
                getAbstract().clear();
                return;
            case Wfs20Package.PARAMETER_EXPRESSION_TYPE__METADATA:
                getMetadata().clear();
                return;
            case Wfs20Package.PARAMETER_EXPRESSION_TYPE__NAME:
                setName(NAME_EDEFAULT);
                return;
            case Wfs20Package.PARAMETER_EXPRESSION_TYPE__TYPE:
                setType(TYPE_EDEFAULT);
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
            case Wfs20Package.PARAMETER_EXPRESSION_TYPE__TITLE:
                return title != null && !title.isEmpty();
            case Wfs20Package.PARAMETER_EXPRESSION_TYPE__ABSTRACT:
                return abstract_ != null && !abstract_.isEmpty();
            case Wfs20Package.PARAMETER_EXPRESSION_TYPE__METADATA:
                return metadata != null && !metadata.isEmpty();
            case Wfs20Package.PARAMETER_EXPRESSION_TYPE__NAME:
                return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
            case Wfs20Package.PARAMETER_EXPRESSION_TYPE__TYPE:
                return TYPE_EDEFAULT == null ? type != null : !TYPE_EDEFAULT.equals(type);
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
        result.append(" (name: ");
        result.append(name);
        result.append(", type: ");
        result.append(type);
        result.append(')');
        return result.toString();
    }

} //ParameterExpressionTypeImpl
