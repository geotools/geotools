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
    public EList<BoundingBoxType> getBoundingBox() {
        // TODO: implement this method to return the 'Bounding Box' containment reference list
        // Ensure that you remove @generated or mark it @generated NOT
        // The list is expected to implement org.eclipse.emf.ecore.util.InternalEList and org.eclipse.emf.ecore.EStructuralFeature.Setting
        // so it's likely that an appropriate subclass of org.eclipse.emf.ecore.util.EcoreEList should be used.
        throw new UnsupportedOperationException();
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
                return getType();
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
                setType((String)newValue);
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
                setType(TYPE_EDEFAULT);
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
                return TYPE_EDEFAULT == null ? type != null : !TYPE_EDEFAULT.equals(type);
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
        result.append(" (type: ");
        result.append(type);
        result.append(')');
        return result.toString();
    }

} //BriefRecordTypeImpl
