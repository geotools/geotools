/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.cat.csw20.impl;

import java.lang.String;

import java.util.Collection;

import net.opengis.cat.csw20.BriefRecordType;
import net.opengis.cat.csw20.Csw20Package;
import net.opengis.cat.csw20.SimpleLiteral;

import net.opengis.ows10.BoundingBoxType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Brief Record Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.cat.csw20.impl.BriefRecordTypeImpl#getIdentifier <em>Identifier</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.BriefRecordTypeImpl#getTitle <em>Title</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.BriefRecordTypeImpl#getType <em>Type</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.BriefRecordTypeImpl#getBoundingBox <em>Bounding Box</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class BriefRecordTypeImpl extends AbstractRecordTypeImpl implements BriefRecordType {
    /**
     * The cached value of the '{@link #getIdentifier() <em>Identifier</em>}' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getIdentifier()
     * @generated
     * @ordered
     */
    protected EList<SimpleLiteral> identifier;

    /**
     * The cached value of the '{@link #getTitle() <em>Title</em>}' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTitle()
     * @generated
     * @ordered
     */
    protected EList<SimpleLiteral> title;
    
    /**
     * The cached value of the '{@link #getBoundingBox() <em>BoundingBox</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSpatial()
     * @ordered
     */
    protected EList<BoundingBoxType> boundingBox;


    /**
     * The cached value of the '{@link #getType() <em>Type</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getType()
     * @generated
     * @ordered
     */
    protected SimpleLiteral type;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected BriefRecordTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Csw20Package.Literals.BRIEF_RECORD_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<SimpleLiteral> getIdentifier() {
        if (identifier == null) {
            identifier = new EObjectResolvingEList<SimpleLiteral>(SimpleLiteral.class, this, Csw20Package.BRIEF_RECORD_TYPE__IDENTIFIER);
        }
        return identifier;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<SimpleLiteral> getTitle() {
        if (title == null) {
            title = new EObjectResolvingEList<SimpleLiteral>(SimpleLiteral.class, this, Csw20Package.BRIEF_RECORD_TYPE__TITLE);
        }
        return title;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SimpleLiteral getType() {
        if (type != null && type.eIsProxy()) {
            InternalEObject oldType = (InternalEObject)type;
            type = (SimpleLiteral)eResolveProxy(oldType);
            if (type != oldType) {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, Csw20Package.BRIEF_RECORD_TYPE__TYPE, oldType, type));
            }
        }
        return type;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SimpleLiteral basicGetType() {
        return type;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setType(SimpleLiteral newType) {
        SimpleLiteral oldType = type;
        type = newType;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.BRIEF_RECORD_TYPE__TYPE, oldType, type));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    public EList<BoundingBoxType> getBoundingBox() {
        if (boundingBox == null) {
            boundingBox = new EObjectResolvingEList<BoundingBoxType>(BoundingBoxType.class, this, Csw20Package.SUMMARY_RECORD_TYPE__BOUNDING_BOX);
        }
        return boundingBox;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Csw20Package.BRIEF_RECORD_TYPE__BOUNDING_BOX:
                return ((InternalEList<?>)getBoundingBox()).basicRemove(otherEnd, msgs);
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
            case Csw20Package.BRIEF_RECORD_TYPE__IDENTIFIER:
                return getIdentifier();
            case Csw20Package.BRIEF_RECORD_TYPE__TITLE:
                return getTitle();
            case Csw20Package.BRIEF_RECORD_TYPE__TYPE:
                if (resolve) return getType();
                return basicGetType();
            case Csw20Package.BRIEF_RECORD_TYPE__BOUNDING_BOX:
                return getBoundingBox();
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
            case Csw20Package.BRIEF_RECORD_TYPE__IDENTIFIER:
                getIdentifier().clear();
                getIdentifier().addAll((Collection<? extends SimpleLiteral>)newValue);
                return;
            case Csw20Package.BRIEF_RECORD_TYPE__TITLE:
                getTitle().clear();
                getTitle().addAll((Collection<? extends SimpleLiteral>)newValue);
                return;
            case Csw20Package.BRIEF_RECORD_TYPE__TYPE:
                setType((SimpleLiteral)newValue);
                return;
            case Csw20Package.BRIEF_RECORD_TYPE__BOUNDING_BOX:
                getBoundingBox().clear();
                getBoundingBox().addAll((Collection<? extends BoundingBoxType>)newValue);
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
            case Csw20Package.BRIEF_RECORD_TYPE__IDENTIFIER:
                getIdentifier().clear();
                return;
            case Csw20Package.BRIEF_RECORD_TYPE__TITLE:
                getTitle().clear();
                return;
            case Csw20Package.BRIEF_RECORD_TYPE__TYPE:
                setType((SimpleLiteral)null);
                return;
            case Csw20Package.BRIEF_RECORD_TYPE__BOUNDING_BOX:
                getBoundingBox().clear();
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
            case Csw20Package.BRIEF_RECORD_TYPE__IDENTIFIER:
                return identifier != null && !identifier.isEmpty();
            case Csw20Package.BRIEF_RECORD_TYPE__TITLE:
                return title != null && !title.isEmpty();
            case Csw20Package.BRIEF_RECORD_TYPE__TYPE:
                return type != null;
            case Csw20Package.BRIEF_RECORD_TYPE__BOUNDING_BOX:
                return !getBoundingBox().isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //BriefRecordTypeImpl
