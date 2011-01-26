/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs.impl;

import java.util.Collection;

import javax.xml.namespace.QName;

import net.opengis.ows10.KeywordsType;
import net.opengis.ows10.WGS84BoundingBoxType;

import net.opengis.wfs.FeatureTypeType;
import net.opengis.wfs.MetadataURLType;
import net.opengis.wfs.NoSRSType;
import net.opengis.wfs.OperationsType;
import net.opengis.wfs.OutputFormatListType;
import net.opengis.wfs.WfsPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EDataTypeEList;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Feature Type Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wfs.impl.FeatureTypeTypeImpl#getName <em>Name</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.FeatureTypeTypeImpl#getTitle <em>Title</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.FeatureTypeTypeImpl#getAbstract <em>Abstract</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.FeatureTypeTypeImpl#getKeywords <em>Keywords</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.FeatureTypeTypeImpl#getDefaultSRS <em>Default SRS</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.FeatureTypeTypeImpl#getOtherSRS <em>Other SRS</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.FeatureTypeTypeImpl#getNoSRS <em>No SRS</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.FeatureTypeTypeImpl#getOperations <em>Operations</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.FeatureTypeTypeImpl#getOutputFormats <em>Output Formats</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.FeatureTypeTypeImpl#getWGS84BoundingBox <em>WGS84 Bounding Box</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.FeatureTypeTypeImpl#getMetadataURL <em>Metadata URL</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class FeatureTypeTypeImpl extends EObjectImpl implements FeatureTypeType {
	/**
     * The default value of the '{@link #getName() <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getName()
     * @generated
     * @ordered
     */
	protected static final QName NAME_EDEFAULT = null;

	/**
     * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getName()
     * @generated
     * @ordered
     */
	protected QName name = NAME_EDEFAULT;

	/**
     * The default value of the '{@link #getTitle() <em>Title</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getTitle()
     * @generated
     * @ordered
     */
	protected static final String TITLE_EDEFAULT = null;

	/**
     * The cached value of the '{@link #getTitle() <em>Title</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getTitle()
     * @generated
     * @ordered
     */
	protected String title = TITLE_EDEFAULT;

	/**
     * The default value of the '{@link #getAbstract() <em>Abstract</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getAbstract()
     * @generated
     * @ordered
     */
	protected static final String ABSTRACT_EDEFAULT = null;

	/**
     * The cached value of the '{@link #getAbstract() <em>Abstract</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getAbstract()
     * @generated
     * @ordered
     */
	protected String abstract_ = ABSTRACT_EDEFAULT;

	/**
     * The cached value of the '{@link #getKeywords() <em>Keywords</em>}' containment reference list.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getKeywords()
     * @generated
     * @ordered
     */
	protected EList keywords;

	/**
     * The default value of the '{@link #getDefaultSRS() <em>Default SRS</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getDefaultSRS()
     * @generated
     * @ordered
     */
	protected static final String DEFAULT_SRS_EDEFAULT = null;

	/**
     * The cached value of the '{@link #getDefaultSRS() <em>Default SRS</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getDefaultSRS()
     * @generated
     * @ordered
     */
	protected String defaultSRS = DEFAULT_SRS_EDEFAULT;

	/**
     * The cached value of the '{@link #getOtherSRS() <em>Other SRS</em>}' attribute list.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getOtherSRS()
     * @generated
     * @ordered
     */
	protected EList otherSRS;

	/**
     * The cached value of the '{@link #getNoSRS() <em>No SRS</em>}' containment reference.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getNoSRS()
     * @generated
     * @ordered
     */
	protected NoSRSType noSRS;

	/**
     * The cached value of the '{@link #getOperations() <em>Operations</em>}' containment reference.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getOperations()
     * @generated
     * @ordered
     */
	protected OperationsType operations;

	/**
     * The cached value of the '{@link #getOutputFormats() <em>Output Formats</em>}' containment reference.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getOutputFormats()
     * @generated
     * @ordered
     */
	protected OutputFormatListType outputFormats;

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
     * The cached value of the '{@link #getMetadataURL() <em>Metadata URL</em>}' containment reference list.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getMetadataURL()
     * @generated
     * @ordered
     */
	protected EList metadataURL;

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	protected FeatureTypeTypeImpl() {
        super();
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	protected EClass eStaticClass() {
        return WfsPackage.Literals.FEATURE_TYPE_TYPE;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public QName getName() {
        return name;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setName(QName newName) {
        QName oldName = name;
        name = newName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, WfsPackage.FEATURE_TYPE_TYPE__NAME, oldName, name));
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public String getTitle() {
        return title;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setTitle(String newTitle) {
        String oldTitle = title;
        title = newTitle;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, WfsPackage.FEATURE_TYPE_TYPE__TITLE, oldTitle, title));
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public String getAbstract() {
        return abstract_;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setAbstract(String newAbstract) {
        String oldAbstract = abstract_;
        abstract_ = newAbstract;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, WfsPackage.FEATURE_TYPE_TYPE__ABSTRACT, oldAbstract, abstract_));
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EList getKeywords() {
        if (keywords == null) {
            keywords = new EObjectContainmentEList(KeywordsType.class, this, WfsPackage.FEATURE_TYPE_TYPE__KEYWORDS);
        }
        return keywords;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public String getDefaultSRS() {
        return defaultSRS;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setDefaultSRS(String newDefaultSRS) {
        String oldDefaultSRS = defaultSRS;
        defaultSRS = newDefaultSRS;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, WfsPackage.FEATURE_TYPE_TYPE__DEFAULT_SRS, oldDefaultSRS, defaultSRS));
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EList getOtherSRS() {
        if (otherSRS == null) {
            otherSRS = new EDataTypeEList(String.class, this, WfsPackage.FEATURE_TYPE_TYPE__OTHER_SRS);
        }
        return otherSRS;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public NoSRSType getNoSRS() {
        return noSRS;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public NotificationChain basicSetNoSRS(NoSRSType newNoSRS, NotificationChain msgs) {
        NoSRSType oldNoSRS = noSRS;
        noSRS = newNoSRS;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, WfsPackage.FEATURE_TYPE_TYPE__NO_SRS, oldNoSRS, newNoSRS);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setNoSRS(NoSRSType newNoSRS) {
        if (newNoSRS != noSRS) {
            NotificationChain msgs = null;
            if (noSRS != null)
                msgs = ((InternalEObject)noSRS).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - WfsPackage.FEATURE_TYPE_TYPE__NO_SRS, null, msgs);
            if (newNoSRS != null)
                msgs = ((InternalEObject)newNoSRS).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - WfsPackage.FEATURE_TYPE_TYPE__NO_SRS, null, msgs);
            msgs = basicSetNoSRS(newNoSRS, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, WfsPackage.FEATURE_TYPE_TYPE__NO_SRS, newNoSRS, newNoSRS));
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public OperationsType getOperations() {
        return operations;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public NotificationChain basicSetOperations(OperationsType newOperations, NotificationChain msgs) {
        OperationsType oldOperations = operations;
        operations = newOperations;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, WfsPackage.FEATURE_TYPE_TYPE__OPERATIONS, oldOperations, newOperations);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setOperations(OperationsType newOperations) {
        if (newOperations != operations) {
            NotificationChain msgs = null;
            if (operations != null)
                msgs = ((InternalEObject)operations).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - WfsPackage.FEATURE_TYPE_TYPE__OPERATIONS, null, msgs);
            if (newOperations != null)
                msgs = ((InternalEObject)newOperations).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - WfsPackage.FEATURE_TYPE_TYPE__OPERATIONS, null, msgs);
            msgs = basicSetOperations(newOperations, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, WfsPackage.FEATURE_TYPE_TYPE__OPERATIONS, newOperations, newOperations));
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public OutputFormatListType getOutputFormats() {
        return outputFormats;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public NotificationChain basicSetOutputFormats(OutputFormatListType newOutputFormats, NotificationChain msgs) {
        OutputFormatListType oldOutputFormats = outputFormats;
        outputFormats = newOutputFormats;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, WfsPackage.FEATURE_TYPE_TYPE__OUTPUT_FORMATS, oldOutputFormats, newOutputFormats);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setOutputFormats(OutputFormatListType newOutputFormats) {
        if (newOutputFormats != outputFormats) {
            NotificationChain msgs = null;
            if (outputFormats != null)
                msgs = ((InternalEObject)outputFormats).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - WfsPackage.FEATURE_TYPE_TYPE__OUTPUT_FORMATS, null, msgs);
            if (newOutputFormats != null)
                msgs = ((InternalEObject)newOutputFormats).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - WfsPackage.FEATURE_TYPE_TYPE__OUTPUT_FORMATS, null, msgs);
            msgs = basicSetOutputFormats(newOutputFormats, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, WfsPackage.FEATURE_TYPE_TYPE__OUTPUT_FORMATS, newOutputFormats, newOutputFormats));
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EList getWGS84BoundingBox() {
        if (wGS84BoundingBox == null) {
            wGS84BoundingBox = new EObjectContainmentEList(WGS84BoundingBoxType.class, this, WfsPackage.FEATURE_TYPE_TYPE__WGS84_BOUNDING_BOX);
        }
        return wGS84BoundingBox;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EList getMetadataURL() {
        if (metadataURL == null) {
            metadataURL = new EObjectContainmentEList(MetadataURLType.class, this, WfsPackage.FEATURE_TYPE_TYPE__METADATA_URL);
        }
        return metadataURL;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case WfsPackage.FEATURE_TYPE_TYPE__KEYWORDS:
                return ((InternalEList)getKeywords()).basicRemove(otherEnd, msgs);
            case WfsPackage.FEATURE_TYPE_TYPE__NO_SRS:
                return basicSetNoSRS(null, msgs);
            case WfsPackage.FEATURE_TYPE_TYPE__OPERATIONS:
                return basicSetOperations(null, msgs);
            case WfsPackage.FEATURE_TYPE_TYPE__OUTPUT_FORMATS:
                return basicSetOutputFormats(null, msgs);
            case WfsPackage.FEATURE_TYPE_TYPE__WGS84_BOUNDING_BOX:
                return ((InternalEList)getWGS84BoundingBox()).basicRemove(otherEnd, msgs);
            case WfsPackage.FEATURE_TYPE_TYPE__METADATA_URL:
                return ((InternalEList)getMetadataURL()).basicRemove(otherEnd, msgs);
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
            case WfsPackage.FEATURE_TYPE_TYPE__NAME:
                return getName();
            case WfsPackage.FEATURE_TYPE_TYPE__TITLE:
                return getTitle();
            case WfsPackage.FEATURE_TYPE_TYPE__ABSTRACT:
                return getAbstract();
            case WfsPackage.FEATURE_TYPE_TYPE__KEYWORDS:
                return getKeywords();
            case WfsPackage.FEATURE_TYPE_TYPE__DEFAULT_SRS:
                return getDefaultSRS();
            case WfsPackage.FEATURE_TYPE_TYPE__OTHER_SRS:
                return getOtherSRS();
            case WfsPackage.FEATURE_TYPE_TYPE__NO_SRS:
                return getNoSRS();
            case WfsPackage.FEATURE_TYPE_TYPE__OPERATIONS:
                return getOperations();
            case WfsPackage.FEATURE_TYPE_TYPE__OUTPUT_FORMATS:
                return getOutputFormats();
            case WfsPackage.FEATURE_TYPE_TYPE__WGS84_BOUNDING_BOX:
                return getWGS84BoundingBox();
            case WfsPackage.FEATURE_TYPE_TYPE__METADATA_URL:
                return getMetadataURL();
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
            case WfsPackage.FEATURE_TYPE_TYPE__NAME:
                setName((QName)newValue);
                return;
            case WfsPackage.FEATURE_TYPE_TYPE__TITLE:
                setTitle((String)newValue);
                return;
            case WfsPackage.FEATURE_TYPE_TYPE__ABSTRACT:
                setAbstract((String)newValue);
                return;
            case WfsPackage.FEATURE_TYPE_TYPE__KEYWORDS:
                getKeywords().clear();
                getKeywords().addAll((Collection)newValue);
                return;
            case WfsPackage.FEATURE_TYPE_TYPE__DEFAULT_SRS:
                setDefaultSRS((String)newValue);
                return;
            case WfsPackage.FEATURE_TYPE_TYPE__OTHER_SRS:
                getOtherSRS().clear();
                getOtherSRS().addAll((Collection)newValue);
                return;
            case WfsPackage.FEATURE_TYPE_TYPE__NO_SRS:
                setNoSRS((NoSRSType)newValue);
                return;
            case WfsPackage.FEATURE_TYPE_TYPE__OPERATIONS:
                setOperations((OperationsType)newValue);
                return;
            case WfsPackage.FEATURE_TYPE_TYPE__OUTPUT_FORMATS:
                setOutputFormats((OutputFormatListType)newValue);
                return;
            case WfsPackage.FEATURE_TYPE_TYPE__WGS84_BOUNDING_BOX:
                getWGS84BoundingBox().clear();
                getWGS84BoundingBox().addAll((Collection)newValue);
                return;
            case WfsPackage.FEATURE_TYPE_TYPE__METADATA_URL:
                getMetadataURL().clear();
                getMetadataURL().addAll((Collection)newValue);
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
            case WfsPackage.FEATURE_TYPE_TYPE__NAME:
                setName(NAME_EDEFAULT);
                return;
            case WfsPackage.FEATURE_TYPE_TYPE__TITLE:
                setTitle(TITLE_EDEFAULT);
                return;
            case WfsPackage.FEATURE_TYPE_TYPE__ABSTRACT:
                setAbstract(ABSTRACT_EDEFAULT);
                return;
            case WfsPackage.FEATURE_TYPE_TYPE__KEYWORDS:
                getKeywords().clear();
                return;
            case WfsPackage.FEATURE_TYPE_TYPE__DEFAULT_SRS:
                setDefaultSRS(DEFAULT_SRS_EDEFAULT);
                return;
            case WfsPackage.FEATURE_TYPE_TYPE__OTHER_SRS:
                getOtherSRS().clear();
                return;
            case WfsPackage.FEATURE_TYPE_TYPE__NO_SRS:
                setNoSRS((NoSRSType)null);
                return;
            case WfsPackage.FEATURE_TYPE_TYPE__OPERATIONS:
                setOperations((OperationsType)null);
                return;
            case WfsPackage.FEATURE_TYPE_TYPE__OUTPUT_FORMATS:
                setOutputFormats((OutputFormatListType)null);
                return;
            case WfsPackage.FEATURE_TYPE_TYPE__WGS84_BOUNDING_BOX:
                getWGS84BoundingBox().clear();
                return;
            case WfsPackage.FEATURE_TYPE_TYPE__METADATA_URL:
                getMetadataURL().clear();
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
            case WfsPackage.FEATURE_TYPE_TYPE__NAME:
                return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
            case WfsPackage.FEATURE_TYPE_TYPE__TITLE:
                return TITLE_EDEFAULT == null ? title != null : !TITLE_EDEFAULT.equals(title);
            case WfsPackage.FEATURE_TYPE_TYPE__ABSTRACT:
                return ABSTRACT_EDEFAULT == null ? abstract_ != null : !ABSTRACT_EDEFAULT.equals(abstract_);
            case WfsPackage.FEATURE_TYPE_TYPE__KEYWORDS:
                return keywords != null && !keywords.isEmpty();
            case WfsPackage.FEATURE_TYPE_TYPE__DEFAULT_SRS:
                return DEFAULT_SRS_EDEFAULT == null ? defaultSRS != null : !DEFAULT_SRS_EDEFAULT.equals(defaultSRS);
            case WfsPackage.FEATURE_TYPE_TYPE__OTHER_SRS:
                return otherSRS != null && !otherSRS.isEmpty();
            case WfsPackage.FEATURE_TYPE_TYPE__NO_SRS:
                return noSRS != null;
            case WfsPackage.FEATURE_TYPE_TYPE__OPERATIONS:
                return operations != null;
            case WfsPackage.FEATURE_TYPE_TYPE__OUTPUT_FORMATS:
                return outputFormats != null;
            case WfsPackage.FEATURE_TYPE_TYPE__WGS84_BOUNDING_BOX:
                return wGS84BoundingBox != null && !wGS84BoundingBox.isEmpty();
            case WfsPackage.FEATURE_TYPE_TYPE__METADATA_URL:
                return metadataURL != null && !metadataURL.isEmpty();
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
        result.append(" (name: ");
        result.append(name);
        result.append(", title: ");
        result.append(title);
        result.append(", abstract: ");
        result.append(abstract_);
        result.append(", defaultSRS: ");
        result.append(defaultSRS);
        result.append(", otherSRS: ");
        result.append(otherSRS);
        result.append(')');
        return result.toString();
    }

} //FeatureTypeTypeImpl