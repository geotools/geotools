/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs20.impl;

import java.util.Collection;

import net.opengis.ows11.MetadataType;

import net.opengis.wfs20.AbstractType;
import net.opengis.wfs20.ParameterExpressionType;
import net.opengis.wfs20.QueryExpressionTextType;
import net.opengis.wfs20.StoredQueryDescriptionType;
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
 * An implementation of the model object '<em><b>Stored Query Description Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wfs20.impl.StoredQueryDescriptionTypeImpl#getTitle <em>Title</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.StoredQueryDescriptionTypeImpl#getAbstract <em>Abstract</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.StoredQueryDescriptionTypeImpl#getMetadata <em>Metadata</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.StoredQueryDescriptionTypeImpl#getParameter <em>Parameter</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.StoredQueryDescriptionTypeImpl#getQueryExpressionText <em>Query Expression Text</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.StoredQueryDescriptionTypeImpl#getId <em>Id</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class StoredQueryDescriptionTypeImpl extends EObjectImpl implements StoredQueryDescriptionType {
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
     * The cached value of the '{@link #getParameter() <em>Parameter</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getParameter()
     * @generated
     * @ordered
     */
    protected EList<ParameterExpressionType> parameter;

    /**
     * The cached value of the '{@link #getQueryExpressionText() <em>Query Expression Text</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getQueryExpressionText()
     * @generated
     * @ordered
     */
    protected EList<QueryExpressionTextType> queryExpressionText;

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
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected StoredQueryDescriptionTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Wfs20Package.Literals.STORED_QUERY_DESCRIPTION_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<TitleType> getTitle() {
        if (title == null) {
            title = new EObjectContainmentEList<TitleType>(TitleType.class, this, Wfs20Package.STORED_QUERY_DESCRIPTION_TYPE__TITLE);
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
            abstract_ = new EObjectContainmentEList<AbstractType>(AbstractType.class, this, Wfs20Package.STORED_QUERY_DESCRIPTION_TYPE__ABSTRACT);
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
            metadata = new EObjectContainmentEList<MetadataType>(MetadataType.class, this, Wfs20Package.STORED_QUERY_DESCRIPTION_TYPE__METADATA);
        }
        return metadata;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<ParameterExpressionType> getParameter() {
        if (parameter == null) {
            parameter = new EObjectContainmentEList<ParameterExpressionType>(ParameterExpressionType.class, this, Wfs20Package.STORED_QUERY_DESCRIPTION_TYPE__PARAMETER);
        }
        return parameter;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<QueryExpressionTextType> getQueryExpressionText() {
        if (queryExpressionText == null) {
            queryExpressionText = new EObjectContainmentEList<QueryExpressionTextType>(QueryExpressionTextType.class, this, Wfs20Package.STORED_QUERY_DESCRIPTION_TYPE__QUERY_EXPRESSION_TEXT);
        }
        return queryExpressionText;
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
            eNotify(new ENotificationImpl(this, Notification.SET, Wfs20Package.STORED_QUERY_DESCRIPTION_TYPE__ID, oldId, id));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Wfs20Package.STORED_QUERY_DESCRIPTION_TYPE__TITLE:
                return ((InternalEList<?>)getTitle()).basicRemove(otherEnd, msgs);
            case Wfs20Package.STORED_QUERY_DESCRIPTION_TYPE__ABSTRACT:
                return ((InternalEList<?>)getAbstract()).basicRemove(otherEnd, msgs);
            case Wfs20Package.STORED_QUERY_DESCRIPTION_TYPE__METADATA:
                return ((InternalEList<?>)getMetadata()).basicRemove(otherEnd, msgs);
            case Wfs20Package.STORED_QUERY_DESCRIPTION_TYPE__PARAMETER:
                return ((InternalEList<?>)getParameter()).basicRemove(otherEnd, msgs);
            case Wfs20Package.STORED_QUERY_DESCRIPTION_TYPE__QUERY_EXPRESSION_TEXT:
                return ((InternalEList<?>)getQueryExpressionText()).basicRemove(otherEnd, msgs);
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
            case Wfs20Package.STORED_QUERY_DESCRIPTION_TYPE__TITLE:
                return getTitle();
            case Wfs20Package.STORED_QUERY_DESCRIPTION_TYPE__ABSTRACT:
                return getAbstract();
            case Wfs20Package.STORED_QUERY_DESCRIPTION_TYPE__METADATA:
                return getMetadata();
            case Wfs20Package.STORED_QUERY_DESCRIPTION_TYPE__PARAMETER:
                return getParameter();
            case Wfs20Package.STORED_QUERY_DESCRIPTION_TYPE__QUERY_EXPRESSION_TEXT:
                return getQueryExpressionText();
            case Wfs20Package.STORED_QUERY_DESCRIPTION_TYPE__ID:
                return getId();
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
            case Wfs20Package.STORED_QUERY_DESCRIPTION_TYPE__TITLE:
                getTitle().clear();
                getTitle().addAll((Collection<? extends TitleType>)newValue);
                return;
            case Wfs20Package.STORED_QUERY_DESCRIPTION_TYPE__ABSTRACT:
                getAbstract().clear();
                getAbstract().addAll((Collection<? extends AbstractType>)newValue);
                return;
            case Wfs20Package.STORED_QUERY_DESCRIPTION_TYPE__METADATA:
                getMetadata().clear();
                getMetadata().addAll((Collection<? extends MetadataType>)newValue);
                return;
            case Wfs20Package.STORED_QUERY_DESCRIPTION_TYPE__PARAMETER:
                getParameter().clear();
                getParameter().addAll((Collection<? extends ParameterExpressionType>)newValue);
                return;
            case Wfs20Package.STORED_QUERY_DESCRIPTION_TYPE__QUERY_EXPRESSION_TEXT:
                getQueryExpressionText().clear();
                getQueryExpressionText().addAll((Collection<? extends QueryExpressionTextType>)newValue);
                return;
            case Wfs20Package.STORED_QUERY_DESCRIPTION_TYPE__ID:
                setId((String)newValue);
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
            case Wfs20Package.STORED_QUERY_DESCRIPTION_TYPE__TITLE:
                getTitle().clear();
                return;
            case Wfs20Package.STORED_QUERY_DESCRIPTION_TYPE__ABSTRACT:
                getAbstract().clear();
                return;
            case Wfs20Package.STORED_QUERY_DESCRIPTION_TYPE__METADATA:
                getMetadata().clear();
                return;
            case Wfs20Package.STORED_QUERY_DESCRIPTION_TYPE__PARAMETER:
                getParameter().clear();
                return;
            case Wfs20Package.STORED_QUERY_DESCRIPTION_TYPE__QUERY_EXPRESSION_TEXT:
                getQueryExpressionText().clear();
                return;
            case Wfs20Package.STORED_QUERY_DESCRIPTION_TYPE__ID:
                setId(ID_EDEFAULT);
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
            case Wfs20Package.STORED_QUERY_DESCRIPTION_TYPE__TITLE:
                return title != null && !title.isEmpty();
            case Wfs20Package.STORED_QUERY_DESCRIPTION_TYPE__ABSTRACT:
                return abstract_ != null && !abstract_.isEmpty();
            case Wfs20Package.STORED_QUERY_DESCRIPTION_TYPE__METADATA:
                return metadata != null && !metadata.isEmpty();
            case Wfs20Package.STORED_QUERY_DESCRIPTION_TYPE__PARAMETER:
                return parameter != null && !parameter.isEmpty();
            case Wfs20Package.STORED_QUERY_DESCRIPTION_TYPE__QUERY_EXPRESSION_TEXT:
                return queryExpressionText != null && !queryExpressionText.isEmpty();
            case Wfs20Package.STORED_QUERY_DESCRIPTION_TYPE__ID:
                return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
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
        result.append(')');
        return result.toString();
    }

} //StoredQueryDescriptionTypeImpl
