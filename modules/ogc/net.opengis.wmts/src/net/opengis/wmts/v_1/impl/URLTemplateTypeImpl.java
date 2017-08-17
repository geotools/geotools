/**
 */
package net.opengis.wmts.v_1.impl;

import net.opengis.wmts.v_1.ResourceTypeType;
import net.opengis.wmts.v_1.URLTemplateType;
import net.opengis.wmts.v_1.wmtsv_1Package;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>URL Template Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wmts.v_1.impl.URLTemplateTypeImpl#getFormat <em>Format</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.URLTemplateTypeImpl#getResourceType <em>Resource Type</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.URLTemplateTypeImpl#getTemplate <em>Template</em>}</li>
 * </ul>
 *
 * @generated
 */
public class URLTemplateTypeImpl extends MinimalEObjectImpl.Container implements URLTemplateType {
    /**
     * The default value of the '{@link #getFormat() <em>Format</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFormat()
     * @generated
     * @ordered
     */
    protected static final String FORMAT_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getFormat() <em>Format</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFormat()
     * @generated
     * @ordered
     */
    protected String format = FORMAT_EDEFAULT;

    /**
     * The default value of the '{@link #getResourceType() <em>Resource Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getResourceType()
     * @generated
     * @ordered
     */
    protected static final ResourceTypeType RESOURCE_TYPE_EDEFAULT = ResourceTypeType.TILE;

    /**
     * The cached value of the '{@link #getResourceType() <em>Resource Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getResourceType()
     * @generated
     * @ordered
     */
    protected ResourceTypeType resourceType = RESOURCE_TYPE_EDEFAULT;

    /**
     * This is true if the Resource Type attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean resourceTypeESet;

    /**
     * The default value of the '{@link #getTemplate() <em>Template</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTemplate()
     * @generated
     * @ordered
     */
    protected static final String TEMPLATE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getTemplate() <em>Template</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTemplate()
     * @generated
     * @ordered
     */
    protected String template = TEMPLATE_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected URLTemplateTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return wmtsv_1Package.Literals.URL_TEMPLATE_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getFormat() {
        return format;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setFormat(String newFormat) {
        String oldFormat = format;
        format = newFormat;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, wmtsv_1Package.URL_TEMPLATE_TYPE__FORMAT, oldFormat, format));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ResourceTypeType getResourceType() {
        return resourceType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setResourceType(ResourceTypeType newResourceType) {
        ResourceTypeType oldResourceType = resourceType;
        resourceType = newResourceType == null ? RESOURCE_TYPE_EDEFAULT : newResourceType;
        boolean oldResourceTypeESet = resourceTypeESet;
        resourceTypeESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, wmtsv_1Package.URL_TEMPLATE_TYPE__RESOURCE_TYPE, oldResourceType, resourceType, !oldResourceTypeESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetResourceType() {
        ResourceTypeType oldResourceType = resourceType;
        boolean oldResourceTypeESet = resourceTypeESet;
        resourceType = RESOURCE_TYPE_EDEFAULT;
        resourceTypeESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, wmtsv_1Package.URL_TEMPLATE_TYPE__RESOURCE_TYPE, oldResourceType, RESOURCE_TYPE_EDEFAULT, oldResourceTypeESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetResourceType() {
        return resourceTypeESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getTemplate() {
        return template;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTemplate(String newTemplate) {
        String oldTemplate = template;
        template = newTemplate;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, wmtsv_1Package.URL_TEMPLATE_TYPE__TEMPLATE, oldTemplate, template));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case wmtsv_1Package.URL_TEMPLATE_TYPE__FORMAT:
                return getFormat();
            case wmtsv_1Package.URL_TEMPLATE_TYPE__RESOURCE_TYPE:
                return getResourceType();
            case wmtsv_1Package.URL_TEMPLATE_TYPE__TEMPLATE:
                return getTemplate();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case wmtsv_1Package.URL_TEMPLATE_TYPE__FORMAT:
                setFormat((String)newValue);
                return;
            case wmtsv_1Package.URL_TEMPLATE_TYPE__RESOURCE_TYPE:
                setResourceType((ResourceTypeType)newValue);
                return;
            case wmtsv_1Package.URL_TEMPLATE_TYPE__TEMPLATE:
                setTemplate((String)newValue);
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
            case wmtsv_1Package.URL_TEMPLATE_TYPE__FORMAT:
                setFormat(FORMAT_EDEFAULT);
                return;
            case wmtsv_1Package.URL_TEMPLATE_TYPE__RESOURCE_TYPE:
                unsetResourceType();
                return;
            case wmtsv_1Package.URL_TEMPLATE_TYPE__TEMPLATE:
                setTemplate(TEMPLATE_EDEFAULT);
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
            case wmtsv_1Package.URL_TEMPLATE_TYPE__FORMAT:
                return FORMAT_EDEFAULT == null ? format != null : !FORMAT_EDEFAULT.equals(format);
            case wmtsv_1Package.URL_TEMPLATE_TYPE__RESOURCE_TYPE:
                return isSetResourceType();
            case wmtsv_1Package.URL_TEMPLATE_TYPE__TEMPLATE:
                return TEMPLATE_EDEFAULT == null ? template != null : !TEMPLATE_EDEFAULT.equals(template);
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
        result.append(", resourceType: ");
        if (resourceTypeESet) result.append(resourceType); else result.append("<unset>");
        result.append(", template: ");
        result.append(template);
        result.append(')');
        return result.toString();
    }

} //URLTemplateTypeImpl
