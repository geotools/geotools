/**
 */
package net.opengis.wps20.impl;

import java.util.List;

import net.opengis.wps20.DataTransmissionModeType;
import net.opengis.wps20.ProcessSummaryType;
import net.opengis.wps20.Wps20Package;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Process Summary Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wps20.impl.ProcessSummaryTypeImpl#getJobControlOptions <em>Job Control Options</em>}</li>
 *   <li>{@link net.opengis.wps20.impl.ProcessSummaryTypeImpl#getOutputTransmission <em>Output Transmission</em>}</li>
 *   <li>{@link net.opengis.wps20.impl.ProcessSummaryTypeImpl#getProcessModel <em>Process Model</em>}</li>
 *   <li>{@link net.opengis.wps20.impl.ProcessSummaryTypeImpl#getProcessVersion <em>Process Version</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ProcessSummaryTypeImpl extends DescriptionTypeImpl implements ProcessSummaryType {
	/**
	 * The default value of the '{@link #getJobControlOptions() <em>Job Control Options</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getJobControlOptions()
	 * @generated
	 * @ordered
	 */
	protected static final List<Object> JOB_CONTROL_OPTIONS_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getJobControlOptions() <em>Job Control Options</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getJobControlOptions()
	 * @generated
	 * @ordered
	 */
	protected List<Object> jobControlOptions = JOB_CONTROL_OPTIONS_EDEFAULT;

	/**
	 * The default value of the '{@link #getOutputTransmission() <em>Output Transmission</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOutputTransmission()
	 * @generated
	 * @ordered
	 */
	protected static final List<DataTransmissionModeType> OUTPUT_TRANSMISSION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getOutputTransmission() <em>Output Transmission</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOutputTransmission()
	 * @generated
	 * @ordered
	 */
	protected List<DataTransmissionModeType> outputTransmission = OUTPUT_TRANSMISSION_EDEFAULT;

	/**
	 * The default value of the '{@link #getProcessModel() <em>Process Model</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProcessModel()
	 * @generated
	 * @ordered
	 */
	protected static final String PROCESS_MODEL_EDEFAULT = "native";

	/**
	 * The cached value of the '{@link #getProcessModel() <em>Process Model</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProcessModel()
	 * @generated
	 * @ordered
	 */
	protected String processModel = PROCESS_MODEL_EDEFAULT;

	/**
	 * This is true if the Process Model attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean processModelESet;

	/**
	 * The default value of the '{@link #getProcessVersion() <em>Process Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProcessVersion()
	 * @generated
	 * @ordered
	 */
	protected static final String PROCESS_VERSION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getProcessVersion() <em>Process Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProcessVersion()
	 * @generated
	 * @ordered
	 */
	protected String processVersion = PROCESS_VERSION_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ProcessSummaryTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return Wps20Package.Literals.PROCESS_SUMMARY_TYPE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public List<Object> getJobControlOptions() {
		return jobControlOptions;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setJobControlOptions(List<Object> newJobControlOptions) {
		List<Object> oldJobControlOptions = jobControlOptions;
		jobControlOptions = newJobControlOptions;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wps20Package.PROCESS_SUMMARY_TYPE__JOB_CONTROL_OPTIONS, oldJobControlOptions, jobControlOptions));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public List<DataTransmissionModeType> getOutputTransmission() {
		return outputTransmission;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOutputTransmission(List<DataTransmissionModeType> newOutputTransmission) {
		List<DataTransmissionModeType> oldOutputTransmission = outputTransmission;
		outputTransmission = newOutputTransmission;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wps20Package.PROCESS_SUMMARY_TYPE__OUTPUT_TRANSMISSION, oldOutputTransmission, outputTransmission));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getProcessModel() {
		return processModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setProcessModel(String newProcessModel) {
		String oldProcessModel = processModel;
		processModel = newProcessModel;
		boolean oldProcessModelESet = processModelESet;
		processModelESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wps20Package.PROCESS_SUMMARY_TYPE__PROCESS_MODEL, oldProcessModel, processModel, !oldProcessModelESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetProcessModel() {
		String oldProcessModel = processModel;
		boolean oldProcessModelESet = processModelESet;
		processModel = PROCESS_MODEL_EDEFAULT;
		processModelESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, Wps20Package.PROCESS_SUMMARY_TYPE__PROCESS_MODEL, oldProcessModel, PROCESS_MODEL_EDEFAULT, oldProcessModelESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetProcessModel() {
		return processModelESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getProcessVersion() {
		return processVersion;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setProcessVersion(String newProcessVersion) {
		String oldProcessVersion = processVersion;
		processVersion = newProcessVersion;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wps20Package.PROCESS_SUMMARY_TYPE__PROCESS_VERSION, oldProcessVersion, processVersion));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case Wps20Package.PROCESS_SUMMARY_TYPE__JOB_CONTROL_OPTIONS:
				return getJobControlOptions();
			case Wps20Package.PROCESS_SUMMARY_TYPE__OUTPUT_TRANSMISSION:
				return getOutputTransmission();
			case Wps20Package.PROCESS_SUMMARY_TYPE__PROCESS_MODEL:
				return getProcessModel();
			case Wps20Package.PROCESS_SUMMARY_TYPE__PROCESS_VERSION:
				return getProcessVersion();
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
			case Wps20Package.PROCESS_SUMMARY_TYPE__JOB_CONTROL_OPTIONS:
				setJobControlOptions((List<Object>)newValue);
				return;
			case Wps20Package.PROCESS_SUMMARY_TYPE__OUTPUT_TRANSMISSION:
				setOutputTransmission((List<DataTransmissionModeType>)newValue);
				return;
			case Wps20Package.PROCESS_SUMMARY_TYPE__PROCESS_MODEL:
				setProcessModel((String)newValue);
				return;
			case Wps20Package.PROCESS_SUMMARY_TYPE__PROCESS_VERSION:
				setProcessVersion((String)newValue);
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
			case Wps20Package.PROCESS_SUMMARY_TYPE__JOB_CONTROL_OPTIONS:
				setJobControlOptions(JOB_CONTROL_OPTIONS_EDEFAULT);
				return;
			case Wps20Package.PROCESS_SUMMARY_TYPE__OUTPUT_TRANSMISSION:
				setOutputTransmission(OUTPUT_TRANSMISSION_EDEFAULT);
				return;
			case Wps20Package.PROCESS_SUMMARY_TYPE__PROCESS_MODEL:
				unsetProcessModel();
				return;
			case Wps20Package.PROCESS_SUMMARY_TYPE__PROCESS_VERSION:
				setProcessVersion(PROCESS_VERSION_EDEFAULT);
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
			case Wps20Package.PROCESS_SUMMARY_TYPE__JOB_CONTROL_OPTIONS:
				return JOB_CONTROL_OPTIONS_EDEFAULT == null ? jobControlOptions != null : !JOB_CONTROL_OPTIONS_EDEFAULT.equals(jobControlOptions);
			case Wps20Package.PROCESS_SUMMARY_TYPE__OUTPUT_TRANSMISSION:
				return OUTPUT_TRANSMISSION_EDEFAULT == null ? outputTransmission != null : !OUTPUT_TRANSMISSION_EDEFAULT.equals(outputTransmission);
			case Wps20Package.PROCESS_SUMMARY_TYPE__PROCESS_MODEL:
				return isSetProcessModel();
			case Wps20Package.PROCESS_SUMMARY_TYPE__PROCESS_VERSION:
				return PROCESS_VERSION_EDEFAULT == null ? processVersion != null : !PROCESS_VERSION_EDEFAULT.equals(processVersion);
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
		result.append(" (jobControlOptions: ");
		result.append(jobControlOptions);
		result.append(", outputTransmission: ");
		result.append(outputTransmission);
		result.append(", processModel: ");
		if (processModelESet) result.append(processModel); else result.append("<unset>");
		result.append(", processVersion: ");
		result.append(processVersion);
		result.append(')');
		return result.toString();
	}

} //ProcessSummaryTypeImpl
