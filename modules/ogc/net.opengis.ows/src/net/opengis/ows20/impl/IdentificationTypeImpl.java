/**
 */
package net.opengis.ows20.impl;

import java.util.Collection;

import net.opengis.ows20.BoundingBoxType;
import net.opengis.ows20.IdentificationType;
import net.opengis.ows20.Ows20Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
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
 *   <li>{@link net.opengis.ows20.impl.IdentificationTypeImpl#getBoundingBoxGroup <em>Bounding Box Group</em>}</li>
 *   <li>{@link net.opengis.ows20.impl.IdentificationTypeImpl#getBoundingBox <em>Bounding Box</em>}</li>
 *   <li>{@link net.opengis.ows20.impl.IdentificationTypeImpl#getOutputFormat <em>Output Format</em>}</li>
 *   <li>{@link net.opengis.ows20.impl.IdentificationTypeImpl#getAvailableCRSGroup <em>Available CRS Group</em>}</li>
 *   <li>{@link net.opengis.ows20.impl.IdentificationTypeImpl#getAvailableCRS <em>Available CRS</em>}</li>
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
     * The default value of the '{@link #getOutputFormat() <em>Output Format</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getOutputFormat()
     * @generated
     * @ordered
     */
    protected static final String OUTPUT_FORMAT_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getOutputFormat() <em>Output Format</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getOutputFormat()
     * @generated
     * @ordered
     */
    protected String outputFormat = OUTPUT_FORMAT_EDEFAULT;

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
     * The default value of the '{@link #getAvailableCRS() <em>Available CRS</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAvailableCRS()
     * @generated
     * @ordered
     */
    protected static final String AVAILABLE_CRS_EDEFAULT = null;

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
    @Override
    protected EClass eStaticClass() {
        return Ows20Package.Literals.IDENTIFICATION_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureMap getBoundingBoxGroup() {
        if (boundingBoxGroup == null) {
            boundingBoxGroup = new BasicFeatureMap(this, Ows20Package.IDENTIFICATION_TYPE__BOUNDING_BOX_GROUP);
        }
        return boundingBoxGroup;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<BoundingBoxType> getBoundingBox() {
        return getBoundingBoxGroup().list(Ows20Package.Literals.IDENTIFICATION_TYPE__BOUNDING_BOX);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getOutputFormat() {
        return outputFormat;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setOutputFormat(String newOutputFormat) {
        String oldOutputFormat = outputFormat;
        outputFormat = newOutputFormat;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Ows20Package.IDENTIFICATION_TYPE__OUTPUT_FORMAT, oldOutputFormat, outputFormat));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureMap getAvailableCRSGroup() {
        if (availableCRSGroup == null) {
            availableCRSGroup = new BasicFeatureMap(this, Ows20Package.IDENTIFICATION_TYPE__AVAILABLE_CRS_GROUP);
        }
        return availableCRSGroup;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getAvailableCRS() {
        return (String)getAvailableCRSGroup().get(Ows20Package.Literals.IDENTIFICATION_TYPE__AVAILABLE_CRS, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setAvailableCRS(String newAvailableCRS) {
        ((FeatureMap.Internal)getAvailableCRSGroup()).set(Ows20Package.Literals.IDENTIFICATION_TYPE__AVAILABLE_CRS, newAvailableCRS);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Ows20Package.IDENTIFICATION_TYPE__BOUNDING_BOX_GROUP:
                return ((InternalEList<?>)getBoundingBoxGroup()).basicRemove(otherEnd, msgs);
            case Ows20Package.IDENTIFICATION_TYPE__BOUNDING_BOX:
                return ((InternalEList<?>)getBoundingBox()).basicRemove(otherEnd, msgs);
            case Ows20Package.IDENTIFICATION_TYPE__AVAILABLE_CRS_GROUP:
                return ((InternalEList<?>)getAvailableCRSGroup()).basicRemove(otherEnd, msgs);
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
            case Ows20Package.IDENTIFICATION_TYPE__BOUNDING_BOX_GROUP:
                if (coreType) return getBoundingBoxGroup();
                return ((FeatureMap.Internal)getBoundingBoxGroup()).getWrapper();
            case Ows20Package.IDENTIFICATION_TYPE__BOUNDING_BOX:
                return getBoundingBox();
            case Ows20Package.IDENTIFICATION_TYPE__OUTPUT_FORMAT:
                return getOutputFormat();
            case Ows20Package.IDENTIFICATION_TYPE__AVAILABLE_CRS_GROUP:
                if (coreType) return getAvailableCRSGroup();
                return ((FeatureMap.Internal)getAvailableCRSGroup()).getWrapper();
            case Ows20Package.IDENTIFICATION_TYPE__AVAILABLE_CRS:
                return getAvailableCRS();
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
            case Ows20Package.IDENTIFICATION_TYPE__BOUNDING_BOX_GROUP:
                ((FeatureMap.Internal)getBoundingBoxGroup()).set(newValue);
                return;
            case Ows20Package.IDENTIFICATION_TYPE__BOUNDING_BOX:
                getBoundingBox().clear();
                getBoundingBox().addAll((Collection<? extends BoundingBoxType>)newValue);
                return;
            case Ows20Package.IDENTIFICATION_TYPE__OUTPUT_FORMAT:
                setOutputFormat((String)newValue);
                return;
            case Ows20Package.IDENTIFICATION_TYPE__AVAILABLE_CRS_GROUP:
                ((FeatureMap.Internal)getAvailableCRSGroup()).set(newValue);
                return;
            case Ows20Package.IDENTIFICATION_TYPE__AVAILABLE_CRS:
                setAvailableCRS((String)newValue);
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
            case Ows20Package.IDENTIFICATION_TYPE__BOUNDING_BOX_GROUP:
                getBoundingBoxGroup().clear();
                return;
            case Ows20Package.IDENTIFICATION_TYPE__BOUNDING_BOX:
                getBoundingBox().clear();
                return;
            case Ows20Package.IDENTIFICATION_TYPE__OUTPUT_FORMAT:
                setOutputFormat(OUTPUT_FORMAT_EDEFAULT);
                return;
            case Ows20Package.IDENTIFICATION_TYPE__AVAILABLE_CRS_GROUP:
                getAvailableCRSGroup().clear();
                return;
            case Ows20Package.IDENTIFICATION_TYPE__AVAILABLE_CRS:
                setAvailableCRS(AVAILABLE_CRS_EDEFAULT);
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
            case Ows20Package.IDENTIFICATION_TYPE__BOUNDING_BOX_GROUP:
                return boundingBoxGroup != null && !boundingBoxGroup.isEmpty();
            case Ows20Package.IDENTIFICATION_TYPE__BOUNDING_BOX:
                return !getBoundingBox().isEmpty();
            case Ows20Package.IDENTIFICATION_TYPE__OUTPUT_FORMAT:
                return OUTPUT_FORMAT_EDEFAULT == null ? outputFormat != null : !OUTPUT_FORMAT_EDEFAULT.equals(outputFormat);
            case Ows20Package.IDENTIFICATION_TYPE__AVAILABLE_CRS_GROUP:
                return availableCRSGroup != null && !availableCRSGroup.isEmpty();
            case Ows20Package.IDENTIFICATION_TYPE__AVAILABLE_CRS:
                return AVAILABLE_CRS_EDEFAULT == null ? getAvailableCRS() != null : !AVAILABLE_CRS_EDEFAULT.equals(getAvailableCRS());
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
