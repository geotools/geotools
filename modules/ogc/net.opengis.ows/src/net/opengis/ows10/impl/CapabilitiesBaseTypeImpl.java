/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.ows10.impl;

import net.opengis.ows10.CapabilitiesBaseType;
import net.opengis.ows10.OperationsMetadataType;
import net.opengis.ows10.Ows10Package;
import net.opengis.ows10.ServiceIdentificationType;
import net.opengis.ows10.ServiceProviderType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Capabilities Base Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.ows10.impl.CapabilitiesBaseTypeImpl#getServiceIdentification <em>Service Identification</em>}</li>
 *   <li>{@link net.opengis.ows10.impl.CapabilitiesBaseTypeImpl#getServiceProvider <em>Service Provider</em>}</li>
 *   <li>{@link net.opengis.ows10.impl.CapabilitiesBaseTypeImpl#getOperationsMetadata <em>Operations Metadata</em>}</li>
 *   <li>{@link net.opengis.ows10.impl.CapabilitiesBaseTypeImpl#getUpdateSequence <em>Update Sequence</em>}</li>
 *   <li>{@link net.opengis.ows10.impl.CapabilitiesBaseTypeImpl#getVersion <em>Version</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CapabilitiesBaseTypeImpl extends EObjectImpl implements CapabilitiesBaseType {
	/**
   * The cached value of the '{@link #getServiceIdentification() <em>Service Identification</em>}' containment reference.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getServiceIdentification()
   * @generated
   * @ordered
   */
	protected ServiceIdentificationType serviceIdentification;

	/**
   * The cached value of the '{@link #getServiceProvider() <em>Service Provider</em>}' containment reference.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getServiceProvider()
   * @generated
   * @ordered
   */
	protected ServiceProviderType serviceProvider;

	/**
   * The cached value of the '{@link #getOperationsMetadata() <em>Operations Metadata</em>}' containment reference.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getOperationsMetadata()
   * @generated
   * @ordered
   */
	protected OperationsMetadataType operationsMetadata;

	/**
   * The default value of the '{@link #getUpdateSequence() <em>Update Sequence</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getUpdateSequence()
   * @generated
   * @ordered
   */
	protected static final String UPDATE_SEQUENCE_EDEFAULT = null;

	/**
   * The cached value of the '{@link #getUpdateSequence() <em>Update Sequence</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getUpdateSequence()
   * @generated
   * @ordered
   */
	protected String updateSequence = UPDATE_SEQUENCE_EDEFAULT;

	/**
   * The default value of the '{@link #getVersion() <em>Version</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getVersion()
   * @generated
   * @ordered
   */
	protected static final String VERSION_EDEFAULT = null;

	/**
   * The cached value of the '{@link #getVersion() <em>Version</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getVersion()
   * @generated
   * @ordered
   */
	protected String version = VERSION_EDEFAULT;

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	protected CapabilitiesBaseTypeImpl() {
    super();
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	@Override
  protected EClass eStaticClass() {
    return Ows10Package.eINSTANCE.getCapabilitiesBaseType();
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public ServiceIdentificationType getServiceIdentification() {
    return serviceIdentification;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public NotificationChain basicSetServiceIdentification(ServiceIdentificationType newServiceIdentification, NotificationChain msgs) {
    ServiceIdentificationType oldServiceIdentification = serviceIdentification;
    serviceIdentification = newServiceIdentification;
    if (eNotificationRequired()) {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Ows10Package.CAPABILITIES_BASE_TYPE__SERVICE_IDENTIFICATION, oldServiceIdentification, newServiceIdentification);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public void setServiceIdentification(ServiceIdentificationType newServiceIdentification) {
    if (newServiceIdentification != serviceIdentification) {
      NotificationChain msgs = null;
      if (serviceIdentification != null)
        msgs = ((InternalEObject)serviceIdentification).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Ows10Package.CAPABILITIES_BASE_TYPE__SERVICE_IDENTIFICATION, null, msgs);
      if (newServiceIdentification != null)
        msgs = ((InternalEObject)newServiceIdentification).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Ows10Package.CAPABILITIES_BASE_TYPE__SERVICE_IDENTIFICATION, null, msgs);
      msgs = basicSetServiceIdentification(newServiceIdentification, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, Ows10Package.CAPABILITIES_BASE_TYPE__SERVICE_IDENTIFICATION, newServiceIdentification, newServiceIdentification));
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public ServiceProviderType getServiceProvider() {
    return serviceProvider;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public NotificationChain basicSetServiceProvider(ServiceProviderType newServiceProvider, NotificationChain msgs) {
    ServiceProviderType oldServiceProvider = serviceProvider;
    serviceProvider = newServiceProvider;
    if (eNotificationRequired()) {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Ows10Package.CAPABILITIES_BASE_TYPE__SERVICE_PROVIDER, oldServiceProvider, newServiceProvider);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public void setServiceProvider(ServiceProviderType newServiceProvider) {
    if (newServiceProvider != serviceProvider) {
      NotificationChain msgs = null;
      if (serviceProvider != null)
        msgs = ((InternalEObject)serviceProvider).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Ows10Package.CAPABILITIES_BASE_TYPE__SERVICE_PROVIDER, null, msgs);
      if (newServiceProvider != null)
        msgs = ((InternalEObject)newServiceProvider).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Ows10Package.CAPABILITIES_BASE_TYPE__SERVICE_PROVIDER, null, msgs);
      msgs = basicSetServiceProvider(newServiceProvider, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, Ows10Package.CAPABILITIES_BASE_TYPE__SERVICE_PROVIDER, newServiceProvider, newServiceProvider));
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public OperationsMetadataType getOperationsMetadata() {
    return operationsMetadata;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public NotificationChain basicSetOperationsMetadata(OperationsMetadataType newOperationsMetadata, NotificationChain msgs) {
    OperationsMetadataType oldOperationsMetadata = operationsMetadata;
    operationsMetadata = newOperationsMetadata;
    if (eNotificationRequired()) {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Ows10Package.CAPABILITIES_BASE_TYPE__OPERATIONS_METADATA, oldOperationsMetadata, newOperationsMetadata);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public void setOperationsMetadata(OperationsMetadataType newOperationsMetadata) {
    if (newOperationsMetadata != operationsMetadata) {
      NotificationChain msgs = null;
      if (operationsMetadata != null)
        msgs = ((InternalEObject)operationsMetadata).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Ows10Package.CAPABILITIES_BASE_TYPE__OPERATIONS_METADATA, null, msgs);
      if (newOperationsMetadata != null)
        msgs = ((InternalEObject)newOperationsMetadata).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Ows10Package.CAPABILITIES_BASE_TYPE__OPERATIONS_METADATA, null, msgs);
      msgs = basicSetOperationsMetadata(newOperationsMetadata, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, Ows10Package.CAPABILITIES_BASE_TYPE__OPERATIONS_METADATA, newOperationsMetadata, newOperationsMetadata));
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public String getUpdateSequence() {
    return updateSequence;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public void setUpdateSequence(String newUpdateSequence) {
    String oldUpdateSequence = updateSequence;
    updateSequence = newUpdateSequence;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, Ows10Package.CAPABILITIES_BASE_TYPE__UPDATE_SEQUENCE, oldUpdateSequence, updateSequence));
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public String getVersion() {
    return version;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public void setVersion(String newVersion) {
    String oldVersion = version;
    version = newVersion;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, Ows10Package.CAPABILITIES_BASE_TYPE__VERSION, oldVersion, version));
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	@Override
  public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
    switch (featureID) {
      case Ows10Package.CAPABILITIES_BASE_TYPE__SERVICE_IDENTIFICATION:
        return basicSetServiceIdentification(null, msgs);
      case Ows10Package.CAPABILITIES_BASE_TYPE__SERVICE_PROVIDER:
        return basicSetServiceProvider(null, msgs);
      case Ows10Package.CAPABILITIES_BASE_TYPE__OPERATIONS_METADATA:
        return basicSetOperationsMetadata(null, msgs);
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
      case Ows10Package.CAPABILITIES_BASE_TYPE__SERVICE_IDENTIFICATION:
        return getServiceIdentification();
      case Ows10Package.CAPABILITIES_BASE_TYPE__SERVICE_PROVIDER:
        return getServiceProvider();
      case Ows10Package.CAPABILITIES_BASE_TYPE__OPERATIONS_METADATA:
        return getOperationsMetadata();
      case Ows10Package.CAPABILITIES_BASE_TYPE__UPDATE_SEQUENCE:
        return getUpdateSequence();
      case Ows10Package.CAPABILITIES_BASE_TYPE__VERSION:
        return getVersion();
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
      case Ows10Package.CAPABILITIES_BASE_TYPE__SERVICE_IDENTIFICATION:
        setServiceIdentification((ServiceIdentificationType)newValue);
        return;
      case Ows10Package.CAPABILITIES_BASE_TYPE__SERVICE_PROVIDER:
        setServiceProvider((ServiceProviderType)newValue);
        return;
      case Ows10Package.CAPABILITIES_BASE_TYPE__OPERATIONS_METADATA:
        setOperationsMetadata((OperationsMetadataType)newValue);
        return;
      case Ows10Package.CAPABILITIES_BASE_TYPE__UPDATE_SEQUENCE:
        setUpdateSequence((String)newValue);
        return;
      case Ows10Package.CAPABILITIES_BASE_TYPE__VERSION:
        setVersion((String)newValue);
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
      case Ows10Package.CAPABILITIES_BASE_TYPE__SERVICE_IDENTIFICATION:
        setServiceIdentification((ServiceIdentificationType)null);
        return;
      case Ows10Package.CAPABILITIES_BASE_TYPE__SERVICE_PROVIDER:
        setServiceProvider((ServiceProviderType)null);
        return;
      case Ows10Package.CAPABILITIES_BASE_TYPE__OPERATIONS_METADATA:
        setOperationsMetadata((OperationsMetadataType)null);
        return;
      case Ows10Package.CAPABILITIES_BASE_TYPE__UPDATE_SEQUENCE:
        setUpdateSequence(UPDATE_SEQUENCE_EDEFAULT);
        return;
      case Ows10Package.CAPABILITIES_BASE_TYPE__VERSION:
        setVersion(VERSION_EDEFAULT);
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
      case Ows10Package.CAPABILITIES_BASE_TYPE__SERVICE_IDENTIFICATION:
        return serviceIdentification != null;
      case Ows10Package.CAPABILITIES_BASE_TYPE__SERVICE_PROVIDER:
        return serviceProvider != null;
      case Ows10Package.CAPABILITIES_BASE_TYPE__OPERATIONS_METADATA:
        return operationsMetadata != null;
      case Ows10Package.CAPABILITIES_BASE_TYPE__UPDATE_SEQUENCE:
        return UPDATE_SEQUENCE_EDEFAULT == null ? updateSequence != null : !UPDATE_SEQUENCE_EDEFAULT.equals(updateSequence);
      case Ows10Package.CAPABILITIES_BASE_TYPE__VERSION:
        return VERSION_EDEFAULT == null ? version != null : !VERSION_EDEFAULT.equals(version);
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

    StringBuilder result = new StringBuilder(super.toString());
    result.append(" (updateSequence: ");
    result.append(updateSequence);
    result.append(", version: ");
    result.append(version);
    result.append(')');
    return result.toString();
  }

} //CapabilitiesBaseTypeImpl
