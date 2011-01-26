/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wps10.impl;

import java.util.Collection;

import net.opengis.ows11.CodeType;
import net.opengis.ows11.LanguageStringType;
import net.opengis.ows11.MetadataType;

import net.opengis.wps10.DescriptionType;
import net.opengis.wps10.Wps10Package;

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
 * An implementation of the model object '<em><b>Description Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wps10.impl.DescriptionTypeImpl#getIdentifier <em>Identifier</em>}</li>
 *   <li>{@link net.opengis.wps10.impl.DescriptionTypeImpl#getTitle <em>Title</em>}</li>
 *   <li>{@link net.opengis.wps10.impl.DescriptionTypeImpl#getAbstract <em>Abstract</em>}</li>
 *   <li>{@link net.opengis.wps10.impl.DescriptionTypeImpl#getMetadata <em>Metadata</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DescriptionTypeImpl extends EObjectImpl implements DescriptionType {
    /**
     * The cached value of the '{@link #getIdentifier() <em>Identifier</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getIdentifier()
     * @generated
     * @ordered
     */
    protected CodeType identifier;

    /**
     * The cached value of the '{@link #getTitle() <em>Title</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTitle()
     * @generated
     * @ordered
     */
    protected LanguageStringType title;

    /**
     * The cached value of the '{@link #getAbstract() <em>Abstract</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAbstract()
     * @generated
     * @ordered
     */
    protected LanguageStringType abstract_;

    /**
     * The cached value of the '{@link #getMetadata() <em>Metadata</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMetadata()
     * @generated
     * @ordered
     */
    protected EList metadata;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected DescriptionTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return Wps10Package.Literals.DESCRIPTION_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CodeType getIdentifier() {
        return identifier;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetIdentifier(CodeType newIdentifier, NotificationChain msgs) {
        CodeType oldIdentifier = identifier;
        identifier = newIdentifier;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wps10Package.DESCRIPTION_TYPE__IDENTIFIER, oldIdentifier, newIdentifier);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setIdentifier(CodeType newIdentifier) {
        if (newIdentifier != identifier) {
            NotificationChain msgs = null;
            if (identifier != null)
                msgs = ((InternalEObject)identifier).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wps10Package.DESCRIPTION_TYPE__IDENTIFIER, null, msgs);
            if (newIdentifier != null)
                msgs = ((InternalEObject)newIdentifier).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wps10Package.DESCRIPTION_TYPE__IDENTIFIER, null, msgs);
            msgs = basicSetIdentifier(newIdentifier, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.DESCRIPTION_TYPE__IDENTIFIER, newIdentifier, newIdentifier));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public LanguageStringType getTitle() {
        return title;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetTitle(LanguageStringType newTitle, NotificationChain msgs) {
        LanguageStringType oldTitle = title;
        title = newTitle;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wps10Package.DESCRIPTION_TYPE__TITLE, oldTitle, newTitle);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTitle(LanguageStringType newTitle) {
        if (newTitle != title) {
            NotificationChain msgs = null;
            if (title != null)
                msgs = ((InternalEObject)title).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wps10Package.DESCRIPTION_TYPE__TITLE, null, msgs);
            if (newTitle != null)
                msgs = ((InternalEObject)newTitle).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wps10Package.DESCRIPTION_TYPE__TITLE, null, msgs);
            msgs = basicSetTitle(newTitle, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.DESCRIPTION_TYPE__TITLE, newTitle, newTitle));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public LanguageStringType getAbstract() {
        return abstract_;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetAbstract(LanguageStringType newAbstract, NotificationChain msgs) {
        LanguageStringType oldAbstract = abstract_;
        abstract_ = newAbstract;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wps10Package.DESCRIPTION_TYPE__ABSTRACT, oldAbstract, newAbstract);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setAbstract(LanguageStringType newAbstract) {
        if (newAbstract != abstract_) {
            NotificationChain msgs = null;
            if (abstract_ != null)
                msgs = ((InternalEObject)abstract_).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wps10Package.DESCRIPTION_TYPE__ABSTRACT, null, msgs);
            if (newAbstract != null)
                msgs = ((InternalEObject)newAbstract).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wps10Package.DESCRIPTION_TYPE__ABSTRACT, null, msgs);
            msgs = basicSetAbstract(newAbstract, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.DESCRIPTION_TYPE__ABSTRACT, newAbstract, newAbstract));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList getMetadata() {
        if (metadata == null) {
            metadata = new EObjectContainmentEList(MetadataType.class, this, Wps10Package.DESCRIPTION_TYPE__METADATA);
        }
        return metadata;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Wps10Package.DESCRIPTION_TYPE__IDENTIFIER:
                return basicSetIdentifier(null, msgs);
            case Wps10Package.DESCRIPTION_TYPE__TITLE:
                return basicSetTitle(null, msgs);
            case Wps10Package.DESCRIPTION_TYPE__ABSTRACT:
                return basicSetAbstract(null, msgs);
            case Wps10Package.DESCRIPTION_TYPE__METADATA:
                return ((InternalEList)getMetadata()).basicRemove(otherEnd, msgs);
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
            case Wps10Package.DESCRIPTION_TYPE__IDENTIFIER:
                return getIdentifier();
            case Wps10Package.DESCRIPTION_TYPE__TITLE:
                return getTitle();
            case Wps10Package.DESCRIPTION_TYPE__ABSTRACT:
                return getAbstract();
            case Wps10Package.DESCRIPTION_TYPE__METADATA:
                return getMetadata();
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
            case Wps10Package.DESCRIPTION_TYPE__IDENTIFIER:
                setIdentifier((CodeType)newValue);
                return;
            case Wps10Package.DESCRIPTION_TYPE__TITLE:
                setTitle((LanguageStringType)newValue);
                return;
            case Wps10Package.DESCRIPTION_TYPE__ABSTRACT:
                setAbstract((LanguageStringType)newValue);
                return;
            case Wps10Package.DESCRIPTION_TYPE__METADATA:
                getMetadata().clear();
                getMetadata().addAll((Collection)newValue);
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
            case Wps10Package.DESCRIPTION_TYPE__IDENTIFIER:
                setIdentifier((CodeType)null);
                return;
            case Wps10Package.DESCRIPTION_TYPE__TITLE:
                setTitle((LanguageStringType)null);
                return;
            case Wps10Package.DESCRIPTION_TYPE__ABSTRACT:
                setAbstract((LanguageStringType)null);
                return;
            case Wps10Package.DESCRIPTION_TYPE__METADATA:
                getMetadata().clear();
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
            case Wps10Package.DESCRIPTION_TYPE__IDENTIFIER:
                return identifier != null;
            case Wps10Package.DESCRIPTION_TYPE__TITLE:
                return title != null;
            case Wps10Package.DESCRIPTION_TYPE__ABSTRACT:
                return abstract_ != null;
            case Wps10Package.DESCRIPTION_TYPE__METADATA:
                return metadata != null && !metadata.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //DescriptionTypeImpl
