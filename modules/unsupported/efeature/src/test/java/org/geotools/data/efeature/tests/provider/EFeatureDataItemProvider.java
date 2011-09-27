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

import org.geotools.data.efeature.EFeaturePackage;

import org.geotools.data.efeature.tests.EFeatureData;
import org.geotools.data.efeature.tests.EFeatureTestsPackage;
import org.geotools.data.efeature.tests.EFeatureTestsPlugin;

/**
 * This is the item provider adapter for a {@link org.geotools.data.efeature.tests.EFeatureData} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 *
 * @source $URL$
 */
public class EFeatureDataItemProvider
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
    public EFeatureDataItemProvider(AdapterFactory adapterFactory) {
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
            addDataPropertyDescriptor(object);
            addSRIDPropertyDescriptor(object);
            addDefaultPropertyDescriptor(object);
            addStructurePropertyDescriptor(object);
            addAttributePropertyDescriptor(object);
            addGeometryPropertyDescriptor(object);
        }
        return itemPropertyDescriptors;
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
                 getString("_UI_EFeature_SRID_feature"), //$NON-NLS-1$
                 getString("_UI_PropertyDescriptor_description", "_UI_EFeature_SRID_feature", "_UI_EFeature_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                 EFeaturePackage.Literals.EFEATURE__SRID,
                 true,
                 false,
                 false,
                 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Data feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addDataPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_EFeature_data_feature"), //$NON-NLS-1$
                 getString("_UI_PropertyDescriptor_description", "_UI_EFeature_data_feature", "_UI_EFeature_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                 EFeaturePackage.Literals.EFEATURE__DATA,
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
                 getString("_UI_EFeature_default_feature"), //$NON-NLS-1$
                 getString("_UI_PropertyDescriptor_description", "_UI_EFeature_default_feature", "_UI_EFeature_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                 EFeaturePackage.Literals.EFEATURE__DEFAULT,
                 true,
                 false,
                 false,
                 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Structure feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addStructurePropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_EFeature_structure_feature"), //$NON-NLS-1$
                 getString("_UI_PropertyDescriptor_description", "_UI_EFeature_structure_feature", "_UI_EFeature_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                 EFeaturePackage.Literals.EFEATURE__STRUCTURE,
                 false,
                 false,
                 false,
                 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
                 null,
                 null));
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
                 getString("_UI_EFeature_ID_feature"), //$NON-NLS-1$
                 getString("_UI_PropertyDescriptor_description", "_UI_EFeature_ID_feature", "_UI_EFeature_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                 EFeaturePackage.Literals.EFEATURE__ID,
                 false,
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
                 getString("_UI_EFeatureData_attribute_feature"), //$NON-NLS-1$
                 getString("_UI_PropertyDescriptor_description", "_UI_EFeatureData_attribute_feature", "_UI_EFeatureData_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                 EFeatureTestsPackage.Literals.EFEATURE_DATA__ATTRIBUTE,
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
                 getString("_UI_EFeatureData_geometry_feature"), //$NON-NLS-1$
                 getString("_UI_PropertyDescriptor_description", "_UI_EFeatureData_geometry_feature", "_UI_EFeatureData_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                 EFeatureTestsPackage.Literals.EFEATURE_DATA__GEOMETRY,
                 true,
                 false,
                 false,
                 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
                 null,
                 null));
    }

    /**
     * This returns the label text for the adapted class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String getText(Object object) {
        String label = ((EFeatureData<?, ?>)object).getID();
        return label == null || label.length() == 0 ?
            getString("_UI_EFeatureData_type") : //$NON-NLS-1$
            getString("_UI_EFeatureData_type") + " " + label; //$NON-NLS-1$ //$NON-NLS-2$
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

        switch (notification.getFeatureID(EFeatureData.class)) {
            case EFeatureTestsPackage.EFEATURE_DATA__ID:
            case EFeatureTestsPackage.EFEATURE_DATA__DATA:
            case EFeatureTestsPackage.EFEATURE_DATA__SRID:
            case EFeatureTestsPackage.EFEATURE_DATA__DEFAULT:
            case EFeatureTestsPackage.EFEATURE_DATA__STRUCTURE:
            case EFeatureTestsPackage.EFEATURE_DATA__ATTRIBUTE:
            case EFeatureTestsPackage.EFEATURE_DATA__GEOMETRY:
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
