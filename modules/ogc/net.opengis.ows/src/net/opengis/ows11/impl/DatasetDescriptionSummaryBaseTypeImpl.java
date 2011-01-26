/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.ows11.impl;

import java.util.Collection;

import net.opengis.ows11.CodeType;
import net.opengis.ows11.DatasetDescriptionSummaryBaseType;
import net.opengis.ows11.MetadataType;
import net.opengis.ows11.Ows11Package;
import net.opengis.ows11.WGS84BoundingBoxType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Dataset Description Summary Base Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.ows11.impl.DatasetDescriptionSummaryBaseTypeImpl#getWGS84BoundingBox <em>WGS84 Bounding Box</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.DatasetDescriptionSummaryBaseTypeImpl#getIdentifier <em>Identifier</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.DatasetDescriptionSummaryBaseTypeImpl#getBoundingBoxGroup <em>Bounding Box Group</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.DatasetDescriptionSummaryBaseTypeImpl#getBoundingBox <em>Bounding Box</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.DatasetDescriptionSummaryBaseTypeImpl#getMetadata <em>Metadata</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.DatasetDescriptionSummaryBaseTypeImpl#getDatasetDescriptionSummary <em>Dataset Description Summary</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DatasetDescriptionSummaryBaseTypeImpl extends DescriptionTypeImpl implements DatasetDescriptionSummaryBaseType {
    /**
     * The cached value of the '{@link #getWGS84BoundingBox() <em>WGS84 Bounding Box</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getWGS84BoundingBox()
     * @generated
     * @ordered
     */
    protected EList wGS84BoundingBox;

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
     * The cached value of the '{@link #getBoundingBoxGroup() <em>Bounding Box Group</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getBoundingBoxGroup()
     * @generated
     * @ordered
     */
    protected FeatureMap boundingBoxGroup;

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
     * The cached value of the '{@link #getDatasetDescriptionSummary() <em>Dataset Description Summary</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDatasetDescriptionSummary()
     * @generated
     * @ordered
     */
    protected EList datasetDescriptionSummary;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected DatasetDescriptionSummaryBaseTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return Ows11Package.Literals.DATASET_DESCRIPTION_SUMMARY_BASE_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList getWGS84BoundingBox() {
        if (wGS84BoundingBox == null) {
            wGS84BoundingBox = new EObjectContainmentEList(WGS84BoundingBoxType.class, this, Ows11Package.DATASET_DESCRIPTION_SUMMARY_BASE_TYPE__WGS84_BOUNDING_BOX);
        }
        return wGS84BoundingBox;
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
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Ows11Package.DATASET_DESCRIPTION_SUMMARY_BASE_TYPE__IDENTIFIER, oldIdentifier, newIdentifier);
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
                msgs = ((InternalEObject)identifier).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Ows11Package.DATASET_DESCRIPTION_SUMMARY_BASE_TYPE__IDENTIFIER, null, msgs);
            if (newIdentifier != null)
                msgs = ((InternalEObject)newIdentifier).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Ows11Package.DATASET_DESCRIPTION_SUMMARY_BASE_TYPE__IDENTIFIER, null, msgs);
            msgs = basicSetIdentifier(newIdentifier, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Ows11Package.DATASET_DESCRIPTION_SUMMARY_BASE_TYPE__IDENTIFIER, newIdentifier, newIdentifier));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureMap getBoundingBoxGroup() {
        if (boundingBoxGroup == null) {
            boundingBoxGroup = new BasicFeatureMap(this, Ows11Package.DATASET_DESCRIPTION_SUMMARY_BASE_TYPE__BOUNDING_BOX_GROUP);
        }
        return boundingBoxGroup;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList getBoundingBox() {
        return getBoundingBoxGroup().list(Ows11Package.Literals.DATASET_DESCRIPTION_SUMMARY_BASE_TYPE__BOUNDING_BOX);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList getMetadata() {
        if (metadata == null) {
            metadata = new EObjectContainmentEList(MetadataType.class, this, Ows11Package.DATASET_DESCRIPTION_SUMMARY_BASE_TYPE__METADATA);
        }
        return metadata;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList getDatasetDescriptionSummary() {
        if (datasetDescriptionSummary == null) {
            datasetDescriptionSummary = new EObjectContainmentEList(DatasetDescriptionSummaryBaseType.class, this, Ows11Package.DATASET_DESCRIPTION_SUMMARY_BASE_TYPE__DATASET_DESCRIPTION_SUMMARY);
        }
        return datasetDescriptionSummary;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Ows11Package.DATASET_DESCRIPTION_SUMMARY_BASE_TYPE__WGS84_BOUNDING_BOX:
                return ((InternalEList)getWGS84BoundingBox()).basicRemove(otherEnd, msgs);
            case Ows11Package.DATASET_DESCRIPTION_SUMMARY_BASE_TYPE__IDENTIFIER:
                return basicSetIdentifier(null, msgs);
            case Ows11Package.DATASET_DESCRIPTION_SUMMARY_BASE_TYPE__BOUNDING_BOX_GROUP:
                return ((InternalEList)getBoundingBoxGroup()).basicRemove(otherEnd, msgs);
            case Ows11Package.DATASET_DESCRIPTION_SUMMARY_BASE_TYPE__BOUNDING_BOX:
                return ((InternalEList)getBoundingBox()).basicRemove(otherEnd, msgs);
            case Ows11Package.DATASET_DESCRIPTION_SUMMARY_BASE_TYPE__METADATA:
                return ((InternalEList)getMetadata()).basicRemove(otherEnd, msgs);
            case Ows11Package.DATASET_DESCRIPTION_SUMMARY_BASE_TYPE__DATASET_DESCRIPTION_SUMMARY:
                return ((InternalEList)getDatasetDescriptionSummary()).basicRemove(otherEnd, msgs);
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
            case Ows11Package.DATASET_DESCRIPTION_SUMMARY_BASE_TYPE__WGS84_BOUNDING_BOX:
                return getWGS84BoundingBox();
            case Ows11Package.DATASET_DESCRIPTION_SUMMARY_BASE_TYPE__IDENTIFIER:
                return getIdentifier();
            case Ows11Package.DATASET_DESCRIPTION_SUMMARY_BASE_TYPE__BOUNDING_BOX_GROUP:
                if (coreType) return getBoundingBoxGroup();
                return ((FeatureMap.Internal)getBoundingBoxGroup()).getWrapper();
            case Ows11Package.DATASET_DESCRIPTION_SUMMARY_BASE_TYPE__BOUNDING_BOX:
                return getBoundingBox();
            case Ows11Package.DATASET_DESCRIPTION_SUMMARY_BASE_TYPE__METADATA:
                return getMetadata();
            case Ows11Package.DATASET_DESCRIPTION_SUMMARY_BASE_TYPE__DATASET_DESCRIPTION_SUMMARY:
                return getDatasetDescriptionSummary();
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
            case Ows11Package.DATASET_DESCRIPTION_SUMMARY_BASE_TYPE__WGS84_BOUNDING_BOX:
                getWGS84BoundingBox().clear();
                getWGS84BoundingBox().addAll((Collection)newValue);
                return;
            case Ows11Package.DATASET_DESCRIPTION_SUMMARY_BASE_TYPE__IDENTIFIER:
                setIdentifier((CodeType)newValue);
                return;
            case Ows11Package.DATASET_DESCRIPTION_SUMMARY_BASE_TYPE__BOUNDING_BOX_GROUP:
                ((FeatureMap.Internal)getBoundingBoxGroup()).set(newValue);
                return;
            case Ows11Package.DATASET_DESCRIPTION_SUMMARY_BASE_TYPE__BOUNDING_BOX:
                getBoundingBox().clear();
                getBoundingBox().addAll((Collection)newValue);
                return;
            case Ows11Package.DATASET_DESCRIPTION_SUMMARY_BASE_TYPE__METADATA:
                getMetadata().clear();
                getMetadata().addAll((Collection)newValue);
                return;
            case Ows11Package.DATASET_DESCRIPTION_SUMMARY_BASE_TYPE__DATASET_DESCRIPTION_SUMMARY:
                getDatasetDescriptionSummary().clear();
                getDatasetDescriptionSummary().addAll((Collection)newValue);
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
            case Ows11Package.DATASET_DESCRIPTION_SUMMARY_BASE_TYPE__WGS84_BOUNDING_BOX:
                getWGS84BoundingBox().clear();
                return;
            case Ows11Package.DATASET_DESCRIPTION_SUMMARY_BASE_TYPE__IDENTIFIER:
                setIdentifier((CodeType)null);
                return;
            case Ows11Package.DATASET_DESCRIPTION_SUMMARY_BASE_TYPE__BOUNDING_BOX_GROUP:
                getBoundingBoxGroup().clear();
                return;
            case Ows11Package.DATASET_DESCRIPTION_SUMMARY_BASE_TYPE__BOUNDING_BOX:
                getBoundingBox().clear();
                return;
            case Ows11Package.DATASET_DESCRIPTION_SUMMARY_BASE_TYPE__METADATA:
                getMetadata().clear();
                return;
            case Ows11Package.DATASET_DESCRIPTION_SUMMARY_BASE_TYPE__DATASET_DESCRIPTION_SUMMARY:
                getDatasetDescriptionSummary().clear();
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
            case Ows11Package.DATASET_DESCRIPTION_SUMMARY_BASE_TYPE__WGS84_BOUNDING_BOX:
                return wGS84BoundingBox != null && !wGS84BoundingBox.isEmpty();
            case Ows11Package.DATASET_DESCRIPTION_SUMMARY_BASE_TYPE__IDENTIFIER:
                return identifier != null;
            case Ows11Package.DATASET_DESCRIPTION_SUMMARY_BASE_TYPE__BOUNDING_BOX_GROUP:
                return boundingBoxGroup != null && !boundingBoxGroup.isEmpty();
            case Ows11Package.DATASET_DESCRIPTION_SUMMARY_BASE_TYPE__BOUNDING_BOX:
                return !getBoundingBox().isEmpty();
            case Ows11Package.DATASET_DESCRIPTION_SUMMARY_BASE_TYPE__METADATA:
                return metadata != null && !metadata.isEmpty();
            case Ows11Package.DATASET_DESCRIPTION_SUMMARY_BASE_TYPE__DATASET_DESCRIPTION_SUMMARY:
                return datasetDescriptionSummary != null && !datasetDescriptionSummary.isEmpty();
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
        result.append(" (boundingBoxGroup: ");
        result.append(boundingBoxGroup);
        result.append(')');
        return result.toString();
    }

} //DatasetDescriptionSummaryBaseTypeImpl
