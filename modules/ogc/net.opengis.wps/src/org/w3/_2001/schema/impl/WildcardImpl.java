/**
 */
package org.w3._2001.schema.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.w3._2001.schema.ProcessContentsType;
import org.w3._2001.schema.SchemaFactory;
import org.w3._2001.schema.SchemaPackage;
import org.w3._2001.schema.Wildcard;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Wildcard</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.w3._2001.schema.impl.WildcardImpl#getNamespace <em>Namespace</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.WildcardImpl#getProcessContents <em>Process Contents</em>}</li>
 * </ul>
 *
 * @generated
 */
public class WildcardImpl extends AnnotatedImpl implements Wildcard {
	/**
	 * The default value of the '{@link #getNamespace() <em>Namespace</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNamespace()
	 * @generated
	 * @ordered
	 */
	protected static final Object NAMESPACE_EDEFAULT = SchemaFactory.eINSTANCE.createFromString(SchemaPackage.eINSTANCE.getNamespaceList(), "##any");

	/**
	 * The cached value of the '{@link #getNamespace() <em>Namespace</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNamespace()
	 * @generated
	 * @ordered
	 */
	protected Object namespace = NAMESPACE_EDEFAULT;

	/**
	 * This is true if the Namespace attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean namespaceESet;

	/**
	 * The default value of the '{@link #getProcessContents() <em>Process Contents</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProcessContents()
	 * @generated
	 * @ordered
	 */
	protected static final ProcessContentsType PROCESS_CONTENTS_EDEFAULT = ProcessContentsType.STRICT;

	/**
	 * The cached value of the '{@link #getProcessContents() <em>Process Contents</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProcessContents()
	 * @generated
	 * @ordered
	 */
	protected ProcessContentsType processContents = PROCESS_CONTENTS_EDEFAULT;

	/**
	 * This is true if the Process Contents attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean processContentsESet;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected WildcardImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchemaPackage.Literals.WILDCARD;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object getNamespace() {
		return namespace;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNamespace(Object newNamespace) {
		Object oldNamespace = namespace;
		namespace = newNamespace;
		boolean oldNamespaceESet = namespaceESet;
		namespaceESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.WILDCARD__NAMESPACE, oldNamespace, namespace, !oldNamespaceESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetNamespace() {
		Object oldNamespace = namespace;
		boolean oldNamespaceESet = namespaceESet;
		namespace = NAMESPACE_EDEFAULT;
		namespaceESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, SchemaPackage.WILDCARD__NAMESPACE, oldNamespace, NAMESPACE_EDEFAULT, oldNamespaceESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetNamespace() {
		return namespaceESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ProcessContentsType getProcessContents() {
		return processContents;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setProcessContents(ProcessContentsType newProcessContents) {
		ProcessContentsType oldProcessContents = processContents;
		processContents = newProcessContents == null ? PROCESS_CONTENTS_EDEFAULT : newProcessContents;
		boolean oldProcessContentsESet = processContentsESet;
		processContentsESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.WILDCARD__PROCESS_CONTENTS, oldProcessContents, processContents, !oldProcessContentsESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetProcessContents() {
		ProcessContentsType oldProcessContents = processContents;
		boolean oldProcessContentsESet = processContentsESet;
		processContents = PROCESS_CONTENTS_EDEFAULT;
		processContentsESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, SchemaPackage.WILDCARD__PROCESS_CONTENTS, oldProcessContents, PROCESS_CONTENTS_EDEFAULT, oldProcessContentsESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetProcessContents() {
		return processContentsESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SchemaPackage.WILDCARD__NAMESPACE:
				return getNamespace();
			case SchemaPackage.WILDCARD__PROCESS_CONTENTS:
				return getProcessContents();
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
			case SchemaPackage.WILDCARD__NAMESPACE:
				setNamespace(newValue);
				return;
			case SchemaPackage.WILDCARD__PROCESS_CONTENTS:
				setProcessContents((ProcessContentsType)newValue);
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
			case SchemaPackage.WILDCARD__NAMESPACE:
				unsetNamespace();
				return;
			case SchemaPackage.WILDCARD__PROCESS_CONTENTS:
				unsetProcessContents();
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
			case SchemaPackage.WILDCARD__NAMESPACE:
				return isSetNamespace();
			case SchemaPackage.WILDCARD__PROCESS_CONTENTS:
				return isSetProcessContents();
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
		result.append(" (namespace: ");
		if (namespaceESet) result.append(namespace); else result.append("<unset>");
		result.append(", processContents: ");
		if (processContentsESet) result.append(processContents); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}

} //WildcardImpl
