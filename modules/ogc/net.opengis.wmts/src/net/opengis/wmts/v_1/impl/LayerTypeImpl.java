/**
 */
package net.opengis.wmts.v_1.impl;

import java.util.Collection;

import net.opengis.ows11.impl.DatasetDescriptionSummaryBaseTypeImpl;

import net.opengis.wmts.v_1.DimensionType;
import net.opengis.wmts.v_1.LayerType;
import net.opengis.wmts.v_1.StyleType;
import net.opengis.wmts.v_1.TileMatrixSetLinkType;
import net.opengis.wmts.v_1.URLTemplateType;
import net.opengis.wmts.v_1.wmtsv_1Package;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EDataTypeEList;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Layer Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wmts.v_1.impl.LayerTypeImpl#getStyle <em>Style</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.LayerTypeImpl#getFormat <em>Format</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.LayerTypeImpl#getInfoFormat <em>Info Format</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.LayerTypeImpl#getDimension <em>Dimension</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.LayerTypeImpl#getTileMatrixSetLink <em>Tile Matrix Set Link</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.LayerTypeImpl#getResourceURL <em>Resource URL</em>}</li>
 * </ul>
 *
 * @generated
 */
public class LayerTypeImpl extends DatasetDescriptionSummaryBaseTypeImpl implements LayerType {
    /**
     * The cached value of the '{@link #getStyle() <em>Style</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getStyle()
     * @generated
     * @ordered
     */
    protected EList<StyleType> style;

    /**
     * The cached value of the '{@link #getFormat() <em>Format</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFormat()
     * @generated
     * @ordered
     */
    protected EList<String> format;

    /**
     * The cached value of the '{@link #getInfoFormat() <em>Info Format</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getInfoFormat()
     * @generated
     * @ordered
     */
    protected EList<String> infoFormat;

    /**
     * The cached value of the '{@link #getDimension() <em>Dimension</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDimension()
     * @generated
     * @ordered
     */
    protected EList<DimensionType> dimension;

    /**
     * The cached value of the '{@link #getTileMatrixSetLink() <em>Tile Matrix Set Link</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTileMatrixSetLink()
     * @generated
     * @ordered
     */
    protected EList<TileMatrixSetLinkType> tileMatrixSetLink;

    /**
     * The cached value of the '{@link #getResourceURL() <em>Resource URL</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getResourceURL()
     * @generated
     * @ordered
     */
    protected EList<URLTemplateType> resourceURL;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected LayerTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return wmtsv_1Package.Literals.LAYER_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<StyleType> getStyle() {
        if (style == null) {
            style = new EObjectContainmentEList<StyleType>(StyleType.class, this, wmtsv_1Package.LAYER_TYPE__STYLE);
        }
        return style;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<String> getFormat() {
        if (format == null) {
            format = new EDataTypeEList<String>(String.class, this, wmtsv_1Package.LAYER_TYPE__FORMAT);
        }
        return format;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<String> getInfoFormat() {
        if (infoFormat == null) {
            infoFormat = new EDataTypeEList<String>(String.class, this, wmtsv_1Package.LAYER_TYPE__INFO_FORMAT);
        }
        return infoFormat;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<DimensionType> getDimension() {
        if (dimension == null) {
            dimension = new EObjectContainmentEList<DimensionType>(DimensionType.class, this, wmtsv_1Package.LAYER_TYPE__DIMENSION);
        }
        return dimension;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<TileMatrixSetLinkType> getTileMatrixSetLink() {
        if (tileMatrixSetLink == null) {
            tileMatrixSetLink = new EObjectContainmentEList<TileMatrixSetLinkType>(TileMatrixSetLinkType.class, this, wmtsv_1Package.LAYER_TYPE__TILE_MATRIX_SET_LINK);
        }
        return tileMatrixSetLink;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<URLTemplateType> getResourceURL() {
        if (resourceURL == null) {
            resourceURL = new EObjectContainmentEList<URLTemplateType>(URLTemplateType.class, this, wmtsv_1Package.LAYER_TYPE__RESOURCE_URL);
        }
        return resourceURL;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case wmtsv_1Package.LAYER_TYPE__STYLE:
                return ((InternalEList<?>)getStyle()).basicRemove(otherEnd, msgs);
            case wmtsv_1Package.LAYER_TYPE__DIMENSION:
                return ((InternalEList<?>)getDimension()).basicRemove(otherEnd, msgs);
            case wmtsv_1Package.LAYER_TYPE__TILE_MATRIX_SET_LINK:
                return ((InternalEList<?>)getTileMatrixSetLink()).basicRemove(otherEnd, msgs);
            case wmtsv_1Package.LAYER_TYPE__RESOURCE_URL:
                return ((InternalEList<?>)getResourceURL()).basicRemove(otherEnd, msgs);
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
            case wmtsv_1Package.LAYER_TYPE__STYLE:
                return getStyle();
            case wmtsv_1Package.LAYER_TYPE__FORMAT:
                return getFormat();
            case wmtsv_1Package.LAYER_TYPE__INFO_FORMAT:
                return getInfoFormat();
            case wmtsv_1Package.LAYER_TYPE__DIMENSION:
                return getDimension();
            case wmtsv_1Package.LAYER_TYPE__TILE_MATRIX_SET_LINK:
                return getTileMatrixSetLink();
            case wmtsv_1Package.LAYER_TYPE__RESOURCE_URL:
                return getResourceURL();
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
            case wmtsv_1Package.LAYER_TYPE__STYLE:
                getStyle().clear();
                getStyle().addAll((Collection<? extends StyleType>)newValue);
                return;
            case wmtsv_1Package.LAYER_TYPE__FORMAT:
                getFormat().clear();
                getFormat().addAll((Collection<? extends String>)newValue);
                return;
            case wmtsv_1Package.LAYER_TYPE__INFO_FORMAT:
                getInfoFormat().clear();
                getInfoFormat().addAll((Collection<? extends String>)newValue);
                return;
            case wmtsv_1Package.LAYER_TYPE__DIMENSION:
                getDimension().clear();
                getDimension().addAll((Collection<? extends DimensionType>)newValue);
                return;
            case wmtsv_1Package.LAYER_TYPE__TILE_MATRIX_SET_LINK:
                getTileMatrixSetLink().clear();
                getTileMatrixSetLink().addAll((Collection<? extends TileMatrixSetLinkType>)newValue);
                return;
            case wmtsv_1Package.LAYER_TYPE__RESOURCE_URL:
                getResourceURL().clear();
                getResourceURL().addAll((Collection<? extends URLTemplateType>)newValue);
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
            case wmtsv_1Package.LAYER_TYPE__STYLE:
                getStyle().clear();
                return;
            case wmtsv_1Package.LAYER_TYPE__FORMAT:
                getFormat().clear();
                return;
            case wmtsv_1Package.LAYER_TYPE__INFO_FORMAT:
                getInfoFormat().clear();
                return;
            case wmtsv_1Package.LAYER_TYPE__DIMENSION:
                getDimension().clear();
                return;
            case wmtsv_1Package.LAYER_TYPE__TILE_MATRIX_SET_LINK:
                getTileMatrixSetLink().clear();
                return;
            case wmtsv_1Package.LAYER_TYPE__RESOURCE_URL:
                getResourceURL().clear();
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
            case wmtsv_1Package.LAYER_TYPE__STYLE:
                return style != null && !style.isEmpty();
            case wmtsv_1Package.LAYER_TYPE__FORMAT:
                return format != null && !format.isEmpty();
            case wmtsv_1Package.LAYER_TYPE__INFO_FORMAT:
                return infoFormat != null && !infoFormat.isEmpty();
            case wmtsv_1Package.LAYER_TYPE__DIMENSION:
                return dimension != null && !dimension.isEmpty();
            case wmtsv_1Package.LAYER_TYPE__TILE_MATRIX_SET_LINK:
                return tileMatrixSetLink != null && !tileMatrixSetLink.isEmpty();
            case wmtsv_1Package.LAYER_TYPE__RESOURCE_URL:
                return resourceURL != null && !resourceURL.isEmpty();
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
        result.append(" (format: ");
        result.append(format);
        result.append(", infoFormat: ");
        result.append(infoFormat);
        result.append(')');
        return result.toString();
    }

} //LayerTypeImpl
