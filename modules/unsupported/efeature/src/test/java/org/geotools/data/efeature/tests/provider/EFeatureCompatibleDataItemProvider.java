/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.geotools.data.efeature.tests.provider;


import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.ResourceLocator;

import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.emf.edit.provider.ViewerNotification;

import org.geotools.data.efeature.tests.EFeatureCompatibleData;
import org.geotools.data.efeature.tests.EFeatureTestsPackage;
import org.geotools.data.efeature.tests.EFeatureTestsPlugin;

/**
 * This is the item provider adapter for a {@link org.geotools.data.efeature.tests.EFeatureCompatibleData} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 *
 * @source $URL$
 */
public class EFeatureCompatibleDataItemProvider
    extends ItemProviderAdapter
    implements
        IEditingDomainItemProvider,
        IStructuredItemContentProvider,
        ITreeItemContentProvider,
        IItemLabelProvider,
        IItemPropertySource {
    /**
     * This constructs an instance from a factory and a notifier.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EFeatureCompatibleDataItemProvider(AdapterFactory adapterFactory) {
        super(adapterFactory);
    }

    /**
     * This returns the property descriptors for the adapted class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object) {
        if (itemPropertyDescriptors == null) {
            super.getPropertyDescriptors(object);

            addIDPropertyDescriptor(object);
            addAttributePropertyDescriptor(object);
            addGeometryPropertyDescriptor(object);
            addSRIDPropertyDescriptor(object);
            addDefaultPropertyDescriptor(object);
        }
        return itemPropertyDescriptors;
    }

    /**
     * This adds a property descriptor for the ID feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addIDPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_EFeatureCompatibleData_ID_feature"), //$NON-NLS-1$
                 getString("_UI_PropertyDescriptor_description", "_UI_EFeatureCompatibleData_ID_feature", "_UI_EFeatureCompatibleData_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                 EFeatureTestsPackage.Literals.EFEATURE_COMPATIBLE_DATA__ID,
                 true,
                 false,
                 false,
                 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addAttributePropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_EFeatureCompatibleData_attribute_feature"), //$NON-NLS-1$
                 getString("_UI_PropertyDescriptor_description", "_UI_EFeatureCompatibleData_attribute_feature", "_UI_EFeatureCompatibleData_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                 EFeatureTestsPackage.Literals.EFEATURE_COMPATIBLE_DATA__ATTRIBUTE,
                 true,
                 false,
                 false,
                 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Geometry feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addGeometryPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_EFeatureCompatibleData_geometry_feature"), //$NON-NLS-1$
                 getString("_UI_PropertyDescriptor_description", "_UI_EFeatureCompatibleData_geometry_feature", "_UI_EFeatureCompatibleData_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                 EFeatureTestsPackage.Literals.EFEATURE_COMPATIBLE_DATA__GEOMETRY,
                 true,
                 false,
                 false,
                 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the SRID feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addSRIDPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_EFeatureCompatibleData_SRID_feature"), //$NON-NLS-1$
                 getString("_UI_PropertyDescriptor_description", "_UI_EFeatureCompatibleData_SRID_feature", "_UI_EFeatureCompatibleData_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                 EFeatureTestsPackage.Literals.EFEATURE_COMPATIBLE_DATA__SRID,
                 true,
                 false,
                 false,
                 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Default feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addDefaultPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_EFeatureCompatibleData_default_feature"), //$NON-NLS-1$
                 getString("_UI_PropertyDescriptor_description", "_UI_EFeatureCompatibleData_default_feature", "_UI_EFeatureCompatibleData_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                 EFeatureTestsPackage.Literals.EFEATURE_COMPATIBLE_DATA__DEFAULT,
                 true,
                 false,
                 false,
                 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
                 null,
                 null));
    }

    /**
     * This returns EFeatureCompatibleData.gif.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object getImage(Object object) {
        return overlayImage(object, getResourceLocator().getImage("full/obj16/EFeatureCompatibleData")); //$NON-NLS-1$
    }

    /**
     * This returns the label text for the adapted class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String getText(Object object) {
        String label = ((EFeatureCompatibleData<?, ?>)object).getID();
        return label == null || label.length() == 0 ?
            getString("_UI_EFeatureCompatibleData_type") : //$NON-NLS-1$
            getString("_UI_EFeatureCompatibleData_type") + " " + label; //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * This handles model notifications by calling {@link #updateChildren} to update any cached
     * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void notifyChanged(Notification notification) {
        updateChildren(notification);

        switch (notification.getFeatureID(EFeatureCompatibleData.class)) {
            case EFeatureTestsPackage.EFEATURE_COMPATIBLE_DATA__ID:
            case EFeatureTestsPackage.EFEATURE_COMPATIBLE_DATA__ATTRIBUTE:
            case EFeatureTestsPackage.EFEATURE_COMPATIBLE_DATA__GEOMETRY:
            case EFeatureTestsPackage.EFEATURE_COMPATIBLE_DATA__SRID:
            case EFeatureTestsPackage.EFEATURE_COMPATIBLE_DATA__DEFAULT:
                fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
                return;
        }
        super.notifyChanged(notification);
    }

    /**
     * This adds {@link org.eclipse.emf.edit.command.CommandParameter}s describing the children
     * that can be created under this object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object) {
        super.collectNewChildDescriptors(newChildDescriptors, object);
    }

    /**
     * Return the resource locator for this item provider's resources.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ResourceLocator getResourceLocator() {
        return EFeatureTestsPlugin.INSTANCE;
    }

}
