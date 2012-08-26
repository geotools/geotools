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

import net.opengis.ows10.BoundingBoxType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Brief Record Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.cat.csw20.impl.BriefRecordTypeImpl#getIdentifierGroup <em>Identifier Group</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.BriefRecordTypeImpl#getIdentifier <em>Identifier</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.BriefRecordTypeImpl#getTitleGroup <em>Title Group</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.BriefRecordTypeImpl#getTitle <em>Title</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.BriefRecordTypeImpl#getType <em>Type</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.BriefRecordTypeImpl#getBoundingBoxGroup <em>Bounding Box Group</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.BriefRecordTypeImpl#getBoundingBox <em>Bounding Box</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class BriefRecordTypeImpl extends AbstractRecordTypeImpl implements BriefRecordType {
    /**
     * The cached value of the '{@link #getIdentifierGroup() <em>Identifier Group</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getIdentifierGroup()
     * @generated
     * @ordered
     */
    protected FeatureMap identifierGroup;

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
     * The default value of the '{@link #getType() <em>Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getType()
     * @generated
     * @ordered
     */
    protected static final String TYPE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getType() <em>Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getType()
     * @generated
     * @ordered
     */
    protected String type = TYPE_EDEFAULT;

    /**
     * The cached value of the '{@link #getBoundingBoxGroup() <em>Bounding Box Group</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getBoundingBoxGroup()
     * @generated
     * @ordered
     */
    protected FeatureMap boundingBoxGroup;

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
    public FeatureMap getIdentifierGroup() {
        if (identifierGroup == null) {
            identifierGroup = new BasicFeatureMap(this, Csw20Package.BRIEF_RECORD_TYPE__IDENTIFIER_GROUP);
        }
        return identifierGroup;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<String> getIdentifier() {
        return getIdentifierGroup().list(Csw20Package.Literals.BRIEF_RECORD_TYPE__IDENTIFIER);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureMap getTitleGroup() {
        if (titleGroup == null) {
            titleGroup = new BasicFeatureMap(this, Csw20Package.BRIEF_RECORD_TYPE__TITLE_GROUP);
        }
        return titleGroup;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<String> getTitle() {
        return getTitleGroup().list(Csw20Package.Literals.BRIEF_RECORD_TYPE__TITLE);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getType() {
        return type;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setType(String newType) {
        String oldType = type;
        type = newType;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.BRIEF_RECORD_TYPE__TYPE, oldType, type));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureMap getBoundingBoxGroup() {
        if (boundingBoxGroup == null) {
            boundingBoxGroup = new BasicFeatureMap(this, Csw20Package.BRIEF_RECORD_TYPE__BOUNDING_BOX_GROUP);
        }
        return boundingBoxGroup;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<BoundingBoxType> getBoundingBox() {
        return getBoundingBoxGroup().list(Csw20Package.Literals.BRIEF_RECORD_TYPE__BOUNDING_BOX);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Csw20Package.BRIEF_RECORD_TYPE__IDENTIFIER_GROUP:
                return ((InternalEList<?>)getIdentifierGroup()).basicRemove(otherEnd, msgs);
            case Csw20Package.BRIEF_RECORD_TYPE__IDENTIFIER:
                return ((InternalEList<?>)getIdentifier()).basicRemove(otherEnd, msgs);
            case Csw20Package.BRIEF_RECORD_TYPE__TITLE_GROUP:
                return ((InternalEList<?>)getTitleGroup()).basicRemove(otherEnd, msgs);
            case Csw20Package.BRIEF_RECORD_TYPE__TITLE:
                return ((InternalEList<?>)getTitle()).basicRemove(otherEnd, msgs);
            case Csw20Package.BRIEF_RECORD_TYPE__BOUNDING_BOX_GROUP:
                return ((InternalEList<?>)getBoundingBoxGroup()).basicRemove(otherEnd, msgs);
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
            case Csw20Package.BRIEF_RECORD_TYPE__IDENTIFIER_GROUP:
                if (coreType) return getIdentifierGroup();
                return ((FeatureMap.Internal)getIdentifierGroup()).getWrapper();
            case Csw20Package.BRIEF_RECORD_TYPE__IDENTIFIER:
                return getIdentifier();
            case Csw20Package.BRIEF_RECORD_TYPE__TITLE_GROUP:
                if (coreType) return getTitleGroup();
                return ((FeatureMap.Internal)getTitleGroup()).getWrapper();
            case Csw20Package.BRIEF_RECORD_TYPE__TITLE:
                return getTitle();
            case Csw20Package.BRIEF_RECORD_TYPE__TYPE:
                return getType();
            case Csw20Package.BRIEF_RECORD_TYPE__BOUNDING_BOX_GROUP:
                if (coreType) return getBoundingBoxGroup();
                return ((FeatureMap.Internal)getBoundingBoxGroup()).getWrapper();
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
            case Csw20Package.BRIEF_RECORD_TYPE__IDENTIFIER_GROUP:
                ((FeatureMap.Internal)getIdentifierGroup()).set(newValue);
                return;
            case Csw20Package.BRIEF_RECORD_TYPE__IDENTIFIER:
                getIdentifier().clear();
                getIdentifier().addAll((Collection<? extends String>)newValue);
                return;
            case Csw20Package.BRIEF_RECORD_TYPE__TITLE_GROUP:
                ((FeatureMap.Internal)getTitleGroup()).set(newValue);
                return;
            case Csw20Package.BRIEF_RECORD_TYPE__TITLE:
                getTitle().clear();
                getTitle().addAll((Collection<? extends String>)newValue);
                return;
            case Csw20Package.BRIEF_RECORD_TYPE__TYPE:
                setType((String)newValue);
                return;
            case Csw20Package.BRIEF_RECORD_TYPE__BOUNDING_BOX_GROUP:
                ((FeatureMap.Internal)getBoundingBoxGroup()).set(newValue);
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
            case Csw20Package.BRIEF_RECORD_TYPE__IDENTIFIER_GROUP:
                getIdentifierGroup().clear();
                return;
            case Csw20Package.BRIEF_RECORD_TYPE__IDENTIFIER:
                getIdentifier().clear();
                return;
            case Csw20Package.BRIEF_RECORD_TYPE__TITLE_GROUP:
                getTitleGroup().clear();
                return;
            case Csw20Package.BRIEF_RECORD_TYPE__TITLE:
                getTitle().clear();
                return;
            case Csw20Package.BRIEF_RECORD_TYPE__TYPE:
                setType(TYPE_EDEFAULT);
                return;
            case Csw20Package.BRIEF_RECORD_TYPE__BOUNDING_BOX_GROUP:
                getBoundingBoxGroup().clear();
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
            case Csw20Package.BRIEF_RECORD_TYPE__IDENTIFIER_GROUP:
                return identifierGroup != null && !identifierGroup.isEmpty();
            case Csw20Package.BRIEF_RECORD_TYPE__IDENTIFIER:
                return !getIdentifier().isEmpty();
            case Csw20Package.BRIEF_RECORD_TYPE__TITLE_GROUP:
                return titleGroup != null && !titleGroup.isEmpty();
            case Csw20Package.BRIEF_RECORD_TYPE__TITLE:
                return !getTitle().isEmpty();
            case Csw20Package.BRIEF_RECORD_TYPE__TYPE:
                return TYPE_EDEFAULT == null ? type != null : !TYPE_EDEFAULT.equals(type);
            case Csw20Package.BRIEF_RECORD_TYPE__BOUNDING_BOX_GROUP:
                return boundingBoxGroup != null && !boundingBoxGroup.isEmpty();
            case Csw20Package.BRIEF_RECORD_TYPE__BOUNDING_BOX:
                return !getBoundingBox().isEmpty();
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
        result.append(" (identifierGroup: ");
        result.append(identifierGroup);
        result.append(", titleGroup: ");
        result.append(titleGroup);
        result.append(", type: ");
        result.append(type);
        result.append(", boundingBoxGroup: ");
        result.append(boundingBoxGroup);
        result.append(')');
        return result.toString();
    }

} //BriefRecordTypeImpl
