/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.ows11.impl;

import java.util.Collection;

import net.opengis.ows11.ContentsBaseType;
import net.opengis.ows11.DatasetDescriptionSummaryBaseType;
import net.opengis.ows11.MetadataType;
import net.opengis.ows11.Ows11Package;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Contents Base Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.ows11.impl.ContentsBaseTypeImpl#getDatasetDescriptionSummary <em>Dataset Description Summary</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.ContentsBaseTypeImpl#getOtherSource <em>Other Source</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ContentsBaseTypeImpl extends EObjectImpl implements ContentsBaseType {
    /**
     * The cached value of the '{@link #getDatasetDescriptionSummary() <em>Dataset Description Summary</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDatasetDescriptionSummary()
     * @generated
     * @ordered
     */
    protected EList datasetDescriptionSummary;

    /**
     * The cached value of the '{@link #getOtherSource() <em>Other Source</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getOtherSource()
     * @generated
     * @ordered
     */
    protected EList otherSource;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ContentsBaseTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return Ows11Package.Literals.CONTENTS_BASE_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList getDatasetDescriptionSummary() {
        if (datasetDescriptionSummary == null) {
            datasetDescriptionSummary = new EObjectContainmentEList(DatasetDescriptionSummaryBaseType.class, this, Ows11Package.CONTENTS_BASE_TYPE__DATASET_DESCRIPTION_SUMMARY);
        }
        return datasetDescriptionSummary;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList getOtherSource() {
        if (otherSource == null) {
            otherSource = new EObjectContainmentEList(MetadataType.class, this, Ows11Package.CONTENTS_BASE_TYPE__OTHER_SOURCE);
        }
        return otherSource;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Ows11Package.CONTENTS_BASE_TYPE__DATASET_DESCRIPTION_SUMMARY:
                return ((InternalEList)getDatasetDescriptionSummary()).basicRemove(otherEnd, msgs);
            case Ows11Package.CONTENTS_BASE_TYPE__OTHER_SOURCE:
                return ((InternalEList)getOtherSource()).basicRemove(otherEnd, msgs);
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
            case Ows11Package.CONTENTS_BASE_TYPE__DATASET_DESCRIPTION_SUMMARY:
                return getDatasetDescriptionSummary();
            case Ows11Package.CONTENTS_BASE_TYPE__OTHER_SOURCE:
                return getOtherSource();
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
            case Ows11Package.CONTENTS_BASE_TYPE__DATASET_DESCRIPTION_SUMMARY:
                getDatasetDescriptionSummary().clear();
                getDatasetDescriptionSummary().addAll((Collection)newValue);
                return;
            case Ows11Package.CONTENTS_BASE_TYPE__OTHER_SOURCE:
                getOtherSource().clear();
                getOtherSource().addAll((Collection)newValue);
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
            case Ows11Package.CONTENTS_BASE_TYPE__DATASET_DESCRIPTION_SUMMARY:
                getDatasetDescriptionSummary().clear();
                return;
            case Ows11Package.CONTENTS_BASE_TYPE__OTHER_SOURCE:
                getOtherSource().clear();
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
            case Ows11Package.CONTENTS_BASE_TYPE__DATASET_DESCRIPTION_SUMMARY:
                return datasetDescriptionSummary != null && !datasetDescriptionSummary.isEmpty();
            case Ows11Package.CONTENTS_BASE_TYPE__OTHER_SOURCE:
                return otherSource != null && !otherSource.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //ContentsBaseTypeImpl
