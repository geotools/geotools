/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.ows11.impl;

import java.util.Collection;

import net.opengis.ows11.IdentificationType;
import net.opengis.ows11.Ows11Package;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.EDataTypeEList;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Identification Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.ows11.impl.IdentificationTypeImpl#getBoundingBoxGroup <em>Bounding Box Group</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.IdentificationTypeImpl#getBoundingBox <em>Bounding Box</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.IdentificationTypeImpl#getOutputFormat <em>Output Format</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.IdentificationTypeImpl#getAvailableCRSGroup <em>Available CRS Group</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.IdentificationTypeImpl#getAvailableCRS <em>Available CRS</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class IdentificationTypeImpl extends BasicIdentificationTypeImpl implements IdentificationType {
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
     * The cached value of the '{@link #getOutputFormat() <em>Output Format</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getOutputFormat()
     * @generated
     * @ordered
     */
    protected EList outputFormat;

    /**
     * The cached value of the '{@link #getAvailableCRSGroup() <em>Available CRS Group</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAvailableCRSGroup()
     * @generated
     * @ordered
     */
    protected FeatureMap availableCRSGroup;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected IdentificationTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return Ows11Package.Literals.IDENTIFICATION_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureMap getBoundingBoxGroup() {
        if (boundingBoxGroup == null) {
            boundingBoxGroup = new BasicFeatureMap(this, Ows11Package.IDENTIFICATION_TYPE__BOUNDING_BOX_GROUP);
        }
        return boundingBoxGroup;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList getBoundingBox() {
        return getBoundingBoxGroup().list(Ows11Package.Literals.IDENTIFICATION_TYPE__BOUNDING_BOX);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList getOutputFormat() {
        if (outputFormat == null) {
            outputFormat = new EDataTypeEList(String.class, this, Ows11Package.IDENTIFICATION_TYPE__OUTPUT_FORMAT);
        }
        return outputFormat;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureMap getAvailableCRSGroup() {
        if (availableCRSGroup == null) {
            availableCRSGroup = new BasicFeatureMap(this, Ows11Package.IDENTIFICATION_TYPE__AVAILABLE_CRS_GROUP);
        }
        return availableCRSGroup;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList getAvailableCRS() {
        return getAvailableCRSGroup().list(Ows11Package.Literals.IDENTIFICATION_TYPE__AVAILABLE_CRS);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Ows11Package.IDENTIFICATION_TYPE__BOUNDING_BOX_GROUP:
                return ((InternalEList)getBoundingBoxGroup()).basicRemove(otherEnd, msgs);
            case Ows11Package.IDENTIFICATION_TYPE__BOUNDING_BOX:
                return ((InternalEList)getBoundingBox()).basicRemove(otherEnd, msgs);
            case Ows11Package.IDENTIFICATION_TYPE__AVAILABLE_CRS_GROUP:
                return ((InternalEList)getAvailableCRSGroup()).basicRemove(otherEnd, msgs);
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
            case Ows11Package.IDENTIFICATION_TYPE__BOUNDING_BOX_GROUP:
                if (coreType) return getBoundingBoxGroup();
                return ((FeatureMap.Internal)getBoundingBoxGroup()).getWrapper();
            case Ows11Package.IDENTIFICATION_TYPE__BOUNDING_BOX:
                return getBoundingBox();
            case Ows11Package.IDENTIFICATION_TYPE__OUTPUT_FORMAT:
                return getOutputFormat();
            case Ows11Package.IDENTIFICATION_TYPE__AVAILABLE_CRS_GROUP:
                if (coreType) return getAvailableCRSGroup();
                return ((FeatureMap.Internal)getAvailableCRSGroup()).getWrapper();
            case Ows11Package.IDENTIFICATION_TYPE__AVAILABLE_CRS:
                return getAvailableCRS();
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
            case Ows11Package.IDENTIFICATION_TYPE__BOUNDING_BOX_GROUP:
                ((FeatureMap.Internal)getBoundingBoxGroup()).set(newValue);
                return;
            case Ows11Package.IDENTIFICATION_TYPE__BOUNDING_BOX:
                getBoundingBox().clear();
                getBoundingBox().addAll((Collection)newValue);
                return;
            case Ows11Package.IDENTIFICATION_TYPE__OUTPUT_FORMAT:
                getOutputFormat().clear();
                getOutputFormat().addAll((Collection)newValue);
                return;
            case Ows11Package.IDENTIFICATION_TYPE__AVAILABLE_CRS_GROUP:
                ((FeatureMap.Internal)getAvailableCRSGroup()).set(newValue);
                return;
            case Ows11Package.IDENTIFICATION_TYPE__AVAILABLE_CRS:
                getAvailableCRS().clear();
                getAvailableCRS().addAll((Collection)newValue);
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
            case Ows11Package.IDENTIFICATION_TYPE__BOUNDING_BOX_GROUP:
                getBoundingBoxGroup().clear();
                return;
            case Ows11Package.IDENTIFICATION_TYPE__BOUNDING_BOX:
                getBoundingBox().clear();
                return;
            case Ows11Package.IDENTIFICATION_TYPE__OUTPUT_FORMAT:
                getOutputFormat().clear();
                return;
            case Ows11Package.IDENTIFICATION_TYPE__AVAILABLE_CRS_GROUP:
                getAvailableCRSGroup().clear();
                return;
            case Ows11Package.IDENTIFICATION_TYPE__AVAILABLE_CRS:
                getAvailableCRS().clear();
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
            case Ows11Package.IDENTIFICATION_TYPE__BOUNDING_BOX_GROUP:
                return boundingBoxGroup != null && !boundingBoxGroup.isEmpty();
            case Ows11Package.IDENTIFICATION_TYPE__BOUNDING_BOX:
                return !getBoundingBox().isEmpty();
            case Ows11Package.IDENTIFICATION_TYPE__OUTPUT_FORMAT:
                return outputFormat != null && !outputFormat.isEmpty();
            case Ows11Package.IDENTIFICATION_TYPE__AVAILABLE_CRS_GROUP:
                return availableCRSGroup != null && !availableCRSGroup.isEmpty();
            case Ows11Package.IDENTIFICATION_TYPE__AVAILABLE_CRS:
                return !getAvailableCRS().isEmpty();
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
        result.append(", outputFormat: ");
        result.append(outputFormat);
        result.append(", availableCRSGroup: ");
        result.append(availableCRSGroup);
        result.append(')');
        return result.toString();
    }

} //IdentificationTypeImpl
