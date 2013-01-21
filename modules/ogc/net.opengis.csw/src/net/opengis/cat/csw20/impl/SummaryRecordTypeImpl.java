/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.cat.csw20.impl;

import java.util.Collection;

import net.opengis.cat.csw20.Csw20Package;
import net.opengis.cat.csw20.SimpleLiteral;
import net.opengis.cat.csw20.SummaryRecordType;

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
 * An implementation of the model object '<em><b>Summary Record Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.cat.csw20.impl.SummaryRecordTypeImpl#getIdentifier <em>Identifier</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.SummaryRecordTypeImpl#getTitle <em>Title</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.SummaryRecordTypeImpl#getType <em>Type</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.SummaryRecordTypeImpl#getSubject <em>Subject</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.SummaryRecordTypeImpl#getFormat <em>Format</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.SummaryRecordTypeImpl#getRelation <em>Relation</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.SummaryRecordTypeImpl#getModified <em>Modified</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.SummaryRecordTypeImpl#getAbstract <em>Abstract</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.SummaryRecordTypeImpl#getSpatial <em>Spatial</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.SummaryRecordTypeImpl#getBoundingBox <em>Bounding Box</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SummaryRecordTypeImpl extends AbstractRecordTypeImpl implements SummaryRecordType {
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
     * The cached value of the '{@link #getSubject() <em>Identifier</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSubject()
     * @ordered
     */
    protected EList<SimpleLiteral> identifier;
    
    /**
     * The cached value of the '{@link #getSubject() <em>title</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSubject()
     * @ordered
     */
    protected EList<SimpleLiteral> title;



    /**
     * The cached value of the '{@link #getSubject() <em>Subject</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSubject()
     * @generated
     * @ordered
     */
    protected EList<SimpleLiteral> subject;

    /**
     * The cached value of the '{@link #getModified() <em>Modified</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getModified()
     * @generated
     * @ordered
     */
    protected EList<SimpleLiteral> modified;

    /**
     * The cached value of the '{@link #getAbstract() <em>Abstract</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAbstract()
     * @generated
     * @ordered
     */
    protected EList<SimpleLiteral> abstract_;

    /**
     * The cached value of the '{@link #getFormat() <em>Format</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSpatial()
     * @ordered
     */
    protected EList<SimpleLiteral> format;
    
    /**
     * The cached value of the '{@link #getRelation() <em>Relation</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSpatial()
     * @ordered
     */
    protected EList<SimpleLiteral> relation;
    
    /**
     * The cached value of the '{@link #getBoundingBox() <em>BoundingBox</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSpatial()
     * @ordered
     */
    protected EList<BoundingBoxType> boundingBox;
    
    /**
     * The cached value of the '{@link #getSpatial() <em>Spatial</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSpatial()
     * @generated
     * @ordered
     */
    protected EList<SimpleLiteral> spatial;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected SummaryRecordTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Csw20Package.Literals.SUMMARY_RECORD_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     */
    public EList<SimpleLiteral> getIdentifier() {
        if (identifier == null) {
            identifier = new EObjectContainmentEList<SimpleLiteral>(SimpleLiteral.class, this, Csw20Package.SUMMARY_RECORD_TYPE__IDENTIFIER);
        }
        return identifier;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     */
    public EList<SimpleLiteral> getTitle() {
        if (title == null) {
            title = new EObjectResolvingEList<SimpleLiteral>(SimpleLiteral.class, this, Csw20Package.SUMMARY_RECORD_TYPE__TITLE);
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
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, Csw20Package.SUMMARY_RECORD_TYPE__TYPE, oldType, type));
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
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.SUMMARY_RECORD_TYPE__TYPE, oldType, type));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<SimpleLiteral> getSubject() {
        if (subject == null) {
            subject = new EObjectContainmentEList<SimpleLiteral>(SimpleLiteral.class, this, Csw20Package.SUMMARY_RECORD_TYPE__SUBJECT);
        }
        return subject;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     */
    public EList<SimpleLiteral> getFormat() {
        if (format == null) {
            format = new EObjectContainmentEList<SimpleLiteral>(SimpleLiteral.class, this, Csw20Package.SUMMARY_RECORD_TYPE__FORMAT);
        }
        return format;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     */
    public EList<SimpleLiteral> getRelation() {
        if (relation == null) {
            relation = new EObjectContainmentEList<SimpleLiteral>(SimpleLiteral.class, this, Csw20Package.SUMMARY_RECORD_TYPE__RELATION);
        }
        return relation;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<SimpleLiteral> getModified() {
        if (modified == null) {
            modified = new EObjectContainmentEList<SimpleLiteral>(SimpleLiteral.class, this, Csw20Package.SUMMARY_RECORD_TYPE__MODIFIED);
        }
        return modified;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<SimpleLiteral> getAbstract() {
        if (abstract_ == null) {
            abstract_ = new EObjectContainmentEList<SimpleLiteral>(SimpleLiteral.class, this, Csw20Package.SUMMARY_RECORD_TYPE__ABSTRACT);
        }
        return abstract_;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<SimpleLiteral> getSpatial() {
        if (spatial == null) {
            spatial = new EObjectContainmentEList<SimpleLiteral>(SimpleLiteral.class, this, Csw20Package.SUMMARY_RECORD_TYPE__SPATIAL);
        }
        return spatial;
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
            case Csw20Package.SUMMARY_RECORD_TYPE__IDENTIFIER:
                return ((InternalEList<?>)getIdentifier()).basicRemove(otherEnd, msgs);
            case Csw20Package.SUMMARY_RECORD_TYPE__TITLE:
                return ((InternalEList<?>)getTitle()).basicRemove(otherEnd, msgs);
            case Csw20Package.SUMMARY_RECORD_TYPE__SUBJECT:
                return ((InternalEList<?>)getSubject()).basicRemove(otherEnd, msgs);
            case Csw20Package.SUMMARY_RECORD_TYPE__FORMAT:
                return ((InternalEList<?>)getFormat()).basicRemove(otherEnd, msgs);
            case Csw20Package.SUMMARY_RECORD_TYPE__RELATION:
                return ((InternalEList<?>)getRelation()).basicRemove(otherEnd, msgs);
            case Csw20Package.SUMMARY_RECORD_TYPE__MODIFIED:
                return ((InternalEList<?>)getModified()).basicRemove(otherEnd, msgs);
            case Csw20Package.SUMMARY_RECORD_TYPE__ABSTRACT:
                return ((InternalEList<?>)getAbstract()).basicRemove(otherEnd, msgs);
            case Csw20Package.SUMMARY_RECORD_TYPE__SPATIAL:
                return ((InternalEList<?>)getSpatial()).basicRemove(otherEnd, msgs);
            case Csw20Package.SUMMARY_RECORD_TYPE__BOUNDING_BOX:
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
            case Csw20Package.SUMMARY_RECORD_TYPE__IDENTIFIER:
                return getIdentifier();
            case Csw20Package.SUMMARY_RECORD_TYPE__TITLE:
                return getTitle();
            case Csw20Package.SUMMARY_RECORD_TYPE__TYPE:
                if (resolve) return getType();
                return basicGetType();
            case Csw20Package.SUMMARY_RECORD_TYPE__SUBJECT:
                return getSubject();
            case Csw20Package.SUMMARY_RECORD_TYPE__FORMAT:
                return getFormat();
            case Csw20Package.SUMMARY_RECORD_TYPE__RELATION:
                return getRelation();
            case Csw20Package.SUMMARY_RECORD_TYPE__MODIFIED:
                return getModified();
            case Csw20Package.SUMMARY_RECORD_TYPE__ABSTRACT:
                return getAbstract();
            case Csw20Package.SUMMARY_RECORD_TYPE__SPATIAL:
                return getSpatial();
            case Csw20Package.SUMMARY_RECORD_TYPE__BOUNDING_BOX:
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
            case Csw20Package.SUMMARY_RECORD_TYPE__IDENTIFIER:
                getIdentifier().clear();
                getIdentifier().addAll((Collection<? extends SimpleLiteral>)newValue);
                return;
            case Csw20Package.SUMMARY_RECORD_TYPE__TITLE:
                getTitle().clear();
                getTitle().addAll((Collection<? extends SimpleLiteral>)newValue);
                return;
            case Csw20Package.SUMMARY_RECORD_TYPE__TYPE:
                setType((SimpleLiteral)newValue);
                return;
            case Csw20Package.SUMMARY_RECORD_TYPE__SUBJECT:
                getSubject().clear();
                getSubject().addAll((Collection<? extends SimpleLiteral>)newValue);
                return;
            case Csw20Package.SUMMARY_RECORD_TYPE__FORMAT:
                getFormat().clear();
                getFormat().addAll((Collection<? extends SimpleLiteral>)newValue);
                return;
            case Csw20Package.SUMMARY_RECORD_TYPE__RELATION:
                getRelation().clear();
                getRelation().addAll((Collection<? extends SimpleLiteral>)newValue);
                return;
            case Csw20Package.SUMMARY_RECORD_TYPE__MODIFIED:
                getModified().clear();
                getModified().addAll((Collection<? extends SimpleLiteral>)newValue);
                return;
            case Csw20Package.SUMMARY_RECORD_TYPE__ABSTRACT:
                getAbstract().clear();
                getAbstract().addAll((Collection<? extends SimpleLiteral>)newValue);
                return;
            case Csw20Package.SUMMARY_RECORD_TYPE__SPATIAL:
                getSpatial().clear();
                getSpatial().addAll((Collection<? extends SimpleLiteral>)newValue);
                return;
            case Csw20Package.SUMMARY_RECORD_TYPE__BOUNDING_BOX:
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
            case Csw20Package.SUMMARY_RECORD_TYPE__IDENTIFIER:
                getIdentifier().clear();
                return;
            case Csw20Package.SUMMARY_RECORD_TYPE__TITLE:
                getTitle().clear();
                return;
            case Csw20Package.SUMMARY_RECORD_TYPE__TYPE:
                setType((SimpleLiteral)null);
                return;
            case Csw20Package.SUMMARY_RECORD_TYPE__SUBJECT:
                getSubject().clear();
                return;
            case Csw20Package.SUMMARY_RECORD_TYPE__FORMAT:
                getFormat().clear();
                return;
            case Csw20Package.SUMMARY_RECORD_TYPE__RELATION:
                getRelation().clear();
                return;
            case Csw20Package.SUMMARY_RECORD_TYPE__MODIFIED:
                getModified().clear();
                return;
            case Csw20Package.SUMMARY_RECORD_TYPE__ABSTRACT:
                getAbstract().clear();
                return;
            case Csw20Package.SUMMARY_RECORD_TYPE__SPATIAL:
                getSpatial().clear();
                return;
            case Csw20Package.SUMMARY_RECORD_TYPE__BOUNDING_BOX:
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
            case Csw20Package.SUMMARY_RECORD_TYPE__IDENTIFIER:
                return !getIdentifier().isEmpty();
            case Csw20Package.SUMMARY_RECORD_TYPE__TITLE:
                return !getTitle().isEmpty();
            case Csw20Package.SUMMARY_RECORD_TYPE__TYPE:
                return type != null;
            case Csw20Package.SUMMARY_RECORD_TYPE__SUBJECT:
                return subject != null && !subject.isEmpty();
            case Csw20Package.SUMMARY_RECORD_TYPE__FORMAT:
                return !getFormat().isEmpty();
            case Csw20Package.SUMMARY_RECORD_TYPE__RELATION:
                return !getRelation().isEmpty();
            case Csw20Package.SUMMARY_RECORD_TYPE__MODIFIED:
                return modified != null && !modified.isEmpty();
            case Csw20Package.SUMMARY_RECORD_TYPE__ABSTRACT:
                return abstract_ != null && !abstract_.isEmpty();
            case Csw20Package.SUMMARY_RECORD_TYPE__SPATIAL:
                return spatial != null && !spatial.isEmpty();
            case Csw20Package.SUMMARY_RECORD_TYPE__BOUNDING_BOX:
                return !getBoundingBox().isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //SummaryRecordTypeImpl
