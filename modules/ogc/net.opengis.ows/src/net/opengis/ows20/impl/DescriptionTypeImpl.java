/**
 */
package net.opengis.ows20.impl;

import java.util.Collection;

import net.opengis.ows20.DescriptionType;
import net.opengis.ows20.KeywordsType;
import net.opengis.ows20.LanguageStringType;
import net.opengis.ows20.Ows20Package;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

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
 *   <li>{@link net.opengis.ows20.impl.DescriptionTypeImpl#getTitle <em>Title</em>}</li>
 *   <li>{@link net.opengis.ows20.impl.DescriptionTypeImpl#getAbstract <em>Abstract</em>}</li>
 *   <li>{@link net.opengis.ows20.impl.DescriptionTypeImpl#getKeywords <em>Keywords</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DescriptionTypeImpl extends EObjectImpl implements DescriptionType {
    /**
     * The cached value of the '{@link #getTitle() <em>Title</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTitle()
     * @generated
     * @ordered
     */
    protected EList<LanguageStringType> title;

    /**
     * The cached value of the '{@link #getAbstract() <em>Abstract</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAbstract()
     * @generated
     * @ordered
     */
    protected EList<LanguageStringType> abstract_;

    /**
     * The cached value of the '{@link #getKeywords() <em>Keywords</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getKeywords()
     * @generated
     * @ordered
     */
    protected EList<KeywordsType> keywords;

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
    @Override
    protected EClass eStaticClass() {
        return Ows20Package.Literals.DESCRIPTION_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<LanguageStringType> getTitle() {
        if (title == null) {
            title = new EObjectContainmentEList<LanguageStringType>(LanguageStringType.class, this, Ows20Package.DESCRIPTION_TYPE__TITLE);
        }
        return title;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<LanguageStringType> getAbstract() {
        if (abstract_ == null) {
            abstract_ = new EObjectContainmentEList<LanguageStringType>(LanguageStringType.class, this, Ows20Package.DESCRIPTION_TYPE__ABSTRACT);
        }
        return abstract_;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<KeywordsType> getKeywords() {
        if (keywords == null) {
            keywords = new EObjectContainmentEList<KeywordsType>(KeywordsType.class, this, Ows20Package.DESCRIPTION_TYPE__KEYWORDS);
        }
        return keywords;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Ows20Package.DESCRIPTION_TYPE__TITLE:
                return ((InternalEList<?>)getTitle()).basicRemove(otherEnd, msgs);
            case Ows20Package.DESCRIPTION_TYPE__ABSTRACT:
                return ((InternalEList<?>)getAbstract()).basicRemove(otherEnd, msgs);
            case Ows20Package.DESCRIPTION_TYPE__KEYWORDS:
                return ((InternalEList<?>)getKeywords()).basicRemove(otherEnd, msgs);
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
            case Ows20Package.DESCRIPTION_TYPE__TITLE:
                return getTitle();
            case Ows20Package.DESCRIPTION_TYPE__ABSTRACT:
                return getAbstract();
            case Ows20Package.DESCRIPTION_TYPE__KEYWORDS:
                return getKeywords();
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
            case Ows20Package.DESCRIPTION_TYPE__TITLE:
                getTitle().clear();
                getTitle().addAll((Collection<? extends LanguageStringType>)newValue);
                return;
            case Ows20Package.DESCRIPTION_TYPE__ABSTRACT:
                getAbstract().clear();
                getAbstract().addAll((Collection<? extends LanguageStringType>)newValue);
                return;
            case Ows20Package.DESCRIPTION_TYPE__KEYWORDS:
                getKeywords().clear();
                getKeywords().addAll((Collection<? extends KeywordsType>)newValue);
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
            case Ows20Package.DESCRIPTION_TYPE__TITLE:
                getTitle().clear();
                return;
            case Ows20Package.DESCRIPTION_TYPE__ABSTRACT:
                getAbstract().clear();
                return;
            case Ows20Package.DESCRIPTION_TYPE__KEYWORDS:
                getKeywords().clear();
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
            case Ows20Package.DESCRIPTION_TYPE__TITLE:
                return title != null && !title.isEmpty();
            case Ows20Package.DESCRIPTION_TYPE__ABSTRACT:
                return abstract_ != null && !abstract_.isEmpty();
            case Ows20Package.DESCRIPTION_TYPE__KEYWORDS:
                return keywords != null && !keywords.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //DescriptionTypeImpl
