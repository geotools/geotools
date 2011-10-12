/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs20.impl;

import java.util.Collection;

import javax.xml.namespace.QName;

import net.opengis.ows11.KeywordsType;
import net.opengis.ows11.WGS84BoundingBoxType;

import net.opengis.wfs20.AbstractType;
import net.opengis.wfs20.ExtendedDescriptionType;
import net.opengis.wfs20.FeatureTypeType;
import net.opengis.wfs20.MetadataURLType;
import net.opengis.wfs20.NoCRSType;
import net.opengis.wfs20.OutputFormatListType;
import net.opengis.wfs20.TitleType;
import net.opengis.wfs20.Wfs20Package;

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
 *   <li>{@link net.opengis.wfs20.impl.FeatureTypeTypeImpl#getName <em>Name</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.FeatureTypeTypeImpl#getTitle <em>Title</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.FeatureTypeTypeImpl#getAbstract <em>Abstract</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.FeatureTypeTypeImpl#getKeywords <em>Keywords</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.FeatureTypeTypeImpl#getDefaultCRS <em>Default CRS</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.FeatureTypeTypeImpl#getOtherCRS <em>Other CRS</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.FeatureTypeTypeImpl#getNoCRS <em>No CRS</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.FeatureTypeTypeImpl#getOutputFormats <em>Output Formats</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.FeatureTypeTypeImpl#getWGS84BoundingBox <em>WGS84 Bounding Box</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.FeatureTypeTypeImpl#getMetadataURL <em>Metadata URL</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.FeatureTypeTypeImpl#getExtendedDescription <em>Extended Description</em>}</li>
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
     * The cached value of the '{@link #getTitle() <em>Title</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTitle()
     * @generated
     * @ordered
     */
    protected EList<TitleType> title;

    /**
     * The cached value of the '{@link #getAbstract() <em>Abstract</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAbstract()
     * @generated
     * @ordered
     */
    protected EList<AbstractType> abstract_;

    /**
     * The cached value of the '{@link #getKeywords() <em>Keywords</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getKeywords()
     * @generated
     * @ordered
     */
    protected EList<KeywordsType> keywords;

    /**
     * The default value of the '{@link #getDefaultCRS() <em>Default CRS</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDefaultCRS()
     * @generated
     * @ordered
     */
    protected static final String DEFAULT_CRS_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getDefaultCRS() <em>Default CRS</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDefaultCRS()
     * @generated
     * @ordered
     */
    protected String defaultCRS = DEFAULT_CRS_EDEFAULT;

    /**
     * The cached value of the '{@link #getOtherCRS() <em>Other CRS</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getOtherCRS()
     * @generated
     * @ordered
     */
    protected EList<String> otherCRS;

    /**
     * The cached value of the '{@link #getNoCRS() <em>No CRS</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getNoCRS()
     * @generated
     * @ordered
     */
    protected NoCRSType noCRS;

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
    protected EList<WGS84BoundingBoxType> wGS84BoundingBox;

    /**
     * The cached value of the '{@link #getMetadataURL() <em>Metadata URL</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMetadataURL()
     * @generated
     * @ordered
     */
    protected EList<MetadataURLType> metadataURL;

    /**
     * The cached value of the '{@link #getExtendedDescription() <em>Extended Description</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getExtendedDescription()
     * @generated
     * @ordered
     */
    protected ExtendedDescriptionType extendedDescription;

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
    @Override
    protected EClass eStaticClass() {
        return Wfs20Package.Literals.FEATURE_TYPE_TYPE;
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
            eNotify(new ENotificationImpl(this, Notification.SET, Wfs20Package.FEATURE_TYPE_TYPE__NAME, oldName, name));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<TitleType> getTitle() {
        if (title == null) {
            title = new EObjectContainmentEList<TitleType>(TitleType.class, this, Wfs20Package.FEATURE_TYPE_TYPE__TITLE);
        }
        return title;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<AbstractType> getAbstract() {
        if (abstract_ == null) {
            abstract_ = new EObjectContainmentEList<AbstractType>(AbstractType.class, this, Wfs20Package.FEATURE_TYPE_TYPE__ABSTRACT);
        }
        return abstract_;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<KeywordsType> getKeywords() {
        if (keywords == null) {
            keywords = new EObjectContainmentEList<KeywordsType>(KeywordsType.class, this, Wfs20Package.FEATURE_TYPE_TYPE__KEYWORDS);
        }
        return keywords;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getDefaultCRS() {
        return defaultCRS;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDefaultCRS(String newDefaultCRS) {
        String oldDefaultCRS = defaultCRS;
        defaultCRS = newDefaultCRS;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wfs20Package.FEATURE_TYPE_TYPE__DEFAULT_CRS, oldDefaultCRS, defaultCRS));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<String> getOtherCRS() {
        if (otherCRS == null) {
            otherCRS = new EDataTypeEList<String>(String.class, this, Wfs20Package.FEATURE_TYPE_TYPE__OTHER_CRS);
        }
        return otherCRS;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NoCRSType getNoCRS() {
        return noCRS;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetNoCRS(NoCRSType newNoCRS, NotificationChain msgs) {
        NoCRSType oldNoCRS = noCRS;
        noCRS = newNoCRS;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wfs20Package.FEATURE_TYPE_TYPE__NO_CRS, oldNoCRS, newNoCRS);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setNoCRS(NoCRSType newNoCRS) {
        if (newNoCRS != noCRS) {
            NotificationChain msgs = null;
            if (noCRS != null)
                msgs = ((InternalEObject)noCRS).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wfs20Package.FEATURE_TYPE_TYPE__NO_CRS, null, msgs);
            if (newNoCRS != null)
                msgs = ((InternalEObject)newNoCRS).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wfs20Package.FEATURE_TYPE_TYPE__NO_CRS, null, msgs);
            msgs = basicSetNoCRS(newNoCRS, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wfs20Package.FEATURE_TYPE_TYPE__NO_CRS, newNoCRS, newNoCRS));
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
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wfs20Package.FEATURE_TYPE_TYPE__OUTPUT_FORMATS, oldOutputFormats, newOutputFormats);
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
                msgs = ((InternalEObject)outputFormats).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wfs20Package.FEATURE_TYPE_TYPE__OUTPUT_FORMATS, null, msgs);
            if (newOutputFormats != null)
                msgs = ((InternalEObject)newOutputFormats).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wfs20Package.FEATURE_TYPE_TYPE__OUTPUT_FORMATS, null, msgs);
            msgs = basicSetOutputFormats(newOutputFormats, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wfs20Package.FEATURE_TYPE_TYPE__OUTPUT_FORMATS, newOutputFormats, newOutputFormats));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<WGS84BoundingBoxType> getWGS84BoundingBox() {
        if (wGS84BoundingBox == null) {
            wGS84BoundingBox = new EObjectContainmentEList<WGS84BoundingBoxType>(WGS84BoundingBoxType.class, this, Wfs20Package.FEATURE_TYPE_TYPE__WGS84_BOUNDING_BOX);
        }
        return wGS84BoundingBox;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<MetadataURLType> getMetadataURL() {
        if (metadataURL == null) {
            metadataURL = new EObjectContainmentEList<MetadataURLType>(MetadataURLType.class, this, Wfs20Package.FEATURE_TYPE_TYPE__METADATA_URL);
        }
        return metadataURL;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ExtendedDescriptionType getExtendedDescription() {
        return extendedDescription;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetExtendedDescription(ExtendedDescriptionType newExtendedDescription, NotificationChain msgs) {
        ExtendedDescriptionType oldExtendedDescription = extendedDescription;
        extendedDescription = newExtendedDescription;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wfs20Package.FEATURE_TYPE_TYPE__EXTENDED_DESCRIPTION, oldExtendedDescription, newExtendedDescription);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setExtendedDescription(ExtendedDescriptionType newExtendedDescription) {
        if (newExtendedDescription != extendedDescription) {
            NotificationChain msgs = null;
            if (extendedDescription != null)
                msgs = ((InternalEObject)extendedDescription).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wfs20Package.FEATURE_TYPE_TYPE__EXTENDED_DESCRIPTION, null, msgs);
            if (newExtendedDescription != null)
                msgs = ((InternalEObject)newExtendedDescription).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wfs20Package.FEATURE_TYPE_TYPE__EXTENDED_DESCRIPTION, null, msgs);
            msgs = basicSetExtendedDescription(newExtendedDescription, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wfs20Package.FEATURE_TYPE_TYPE__EXTENDED_DESCRIPTION, newExtendedDescription, newExtendedDescription));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Wfs20Package.FEATURE_TYPE_TYPE__TITLE:
                return ((InternalEList<?>)getTitle()).basicRemove(otherEnd, msgs);
            case Wfs20Package.FEATURE_TYPE_TYPE__ABSTRACT:
                return ((InternalEList<?>)getAbstract()).basicRemove(otherEnd, msgs);
            case Wfs20Package.FEATURE_TYPE_TYPE__KEYWORDS:
                return ((InternalEList<?>)getKeywords()).basicRemove(otherEnd, msgs);
            case Wfs20Package.FEATURE_TYPE_TYPE__NO_CRS:
                return basicSetNoCRS(null, msgs);
            case Wfs20Package.FEATURE_TYPE_TYPE__OUTPUT_FORMATS:
                return basicSetOutputFormats(null, msgs);
            case Wfs20Package.FEATURE_TYPE_TYPE__WGS84_BOUNDING_BOX:
                return ((InternalEList<?>)getWGS84BoundingBox()).basicRemove(otherEnd, msgs);
            case Wfs20Package.FEATURE_TYPE_TYPE__METADATA_URL:
                return ((InternalEList<?>)getMetadataURL()).basicRemove(otherEnd, msgs);
            case Wfs20Package.FEATURE_TYPE_TYPE__EXTENDED_DESCRIPTION:
                return basicSetExtendedDescription(null, msgs);
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
            case Wfs20Package.FEATURE_TYPE_TYPE__NAME:
                return getName();
            case Wfs20Package.FEATURE_TYPE_TYPE__TITLE:
                return getTitle();
            case Wfs20Package.FEATURE_TYPE_TYPE__ABSTRACT:
                return getAbstract();
            case Wfs20Package.FEATURE_TYPE_TYPE__KEYWORDS:
                return getKeywords();
            case Wfs20Package.FEATURE_TYPE_TYPE__DEFAULT_CRS:
                return getDefaultCRS();
            case Wfs20Package.FEATURE_TYPE_TYPE__OTHER_CRS:
                return getOtherCRS();
            case Wfs20Package.FEATURE_TYPE_TYPE__NO_CRS:
                return getNoCRS();
            case Wfs20Package.FEATURE_TYPE_TYPE__OUTPUT_FORMATS:
                return getOutputFormats();
            case Wfs20Package.FEATURE_TYPE_TYPE__WGS84_BOUNDING_BOX:
                return getWGS84BoundingBox();
            case Wfs20Package.FEATURE_TYPE_TYPE__METADATA_URL:
                return getMetadataURL();
            case Wfs20Package.FEATURE_TYPE_TYPE__EXTENDED_DESCRIPTION:
                return getExtendedDescription();
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
            case Wfs20Package.FEATURE_TYPE_TYPE__NAME:
                setName((QName)newValue);
                return;
            case Wfs20Package.FEATURE_TYPE_TYPE__TITLE:
                getTitle().clear();
                getTitle().addAll((Collection<? extends TitleType>)newValue);
                return;
            case Wfs20Package.FEATURE_TYPE_TYPE__ABSTRACT:
                getAbstract().clear();
                getAbstract().addAll((Collection<? extends AbstractType>)newValue);
                return;
            case Wfs20Package.FEATURE_TYPE_TYPE__KEYWORDS:
                getKeywords().clear();
                getKeywords().addAll((Collection<? extends KeywordsType>)newValue);
                return;
            case Wfs20Package.FEATURE_TYPE_TYPE__DEFAULT_CRS:
                setDefaultCRS((String)newValue);
                return;
            case Wfs20Package.FEATURE_TYPE_TYPE__OTHER_CRS:
                getOtherCRS().clear();
                getOtherCRS().addAll((Collection<? extends String>)newValue);
                return;
            case Wfs20Package.FEATURE_TYPE_TYPE__NO_CRS:
                setNoCRS((NoCRSType)newValue);
                return;
            case Wfs20Package.FEATURE_TYPE_TYPE__OUTPUT_FORMATS:
                setOutputFormats((OutputFormatListType)newValue);
                return;
            case Wfs20Package.FEATURE_TYPE_TYPE__WGS84_BOUNDING_BOX:
                getWGS84BoundingBox().clear();
                getWGS84BoundingBox().addAll((Collection<? extends WGS84BoundingBoxType>)newValue);
                return;
            case Wfs20Package.FEATURE_TYPE_TYPE__METADATA_URL:
                getMetadataURL().clear();
                getMetadataURL().addAll((Collection<? extends MetadataURLType>)newValue);
                return;
            case Wfs20Package.FEATURE_TYPE_TYPE__EXTENDED_DESCRIPTION:
                setExtendedDescription((ExtendedDescriptionType)newValue);
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
            case Wfs20Package.FEATURE_TYPE_TYPE__NAME:
                setName(NAME_EDEFAULT);
                return;
            case Wfs20Package.FEATURE_TYPE_TYPE__TITLE:
                getTitle().clear();
                return;
            case Wfs20Package.FEATURE_TYPE_TYPE__ABSTRACT:
                getAbstract().clear();
                return;
            case Wfs20Package.FEATURE_TYPE_TYPE__KEYWORDS:
                getKeywords().clear();
                return;
            case Wfs20Package.FEATURE_TYPE_TYPE__DEFAULT_CRS:
                setDefaultCRS(DEFAULT_CRS_EDEFAULT);
                return;
            case Wfs20Package.FEATURE_TYPE_TYPE__OTHER_CRS:
                getOtherCRS().clear();
                return;
            case Wfs20Package.FEATURE_TYPE_TYPE__NO_CRS:
                setNoCRS((NoCRSType)null);
                return;
            case Wfs20Package.FEATURE_TYPE_TYPE__OUTPUT_FORMATS:
                setOutputFormats((OutputFormatListType)null);
                return;
            case Wfs20Package.FEATURE_TYPE_TYPE__WGS84_BOUNDING_BOX:
                getWGS84BoundingBox().clear();
                return;
            case Wfs20Package.FEATURE_TYPE_TYPE__METADATA_URL:
                getMetadataURL().clear();
                return;
            case Wfs20Package.FEATURE_TYPE_TYPE__EXTENDED_DESCRIPTION:
                setExtendedDescription((ExtendedDescriptionType)null);
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
            case Wfs20Package.FEATURE_TYPE_TYPE__NAME:
                return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
            case Wfs20Package.FEATURE_TYPE_TYPE__TITLE:
                return title != null && !title.isEmpty();
            case Wfs20Package.FEATURE_TYPE_TYPE__ABSTRACT:
                return abstract_ != null && !abstract_.isEmpty();
            case Wfs20Package.FEATURE_TYPE_TYPE__KEYWORDS:
                return keywords != null && !keywords.isEmpty();
            case Wfs20Package.FEATURE_TYPE_TYPE__DEFAULT_CRS:
                return DEFAULT_CRS_EDEFAULT == null ? defaultCRS != null : !DEFAULT_CRS_EDEFAULT.equals(defaultCRS);
            case Wfs20Package.FEATURE_TYPE_TYPE__OTHER_CRS:
                return otherCRS != null && !otherCRS.isEmpty();
            case Wfs20Package.FEATURE_TYPE_TYPE__NO_CRS:
                return noCRS != null;
            case Wfs20Package.FEATURE_TYPE_TYPE__OUTPUT_FORMATS:
                return outputFormats != null;
            case Wfs20Package.FEATURE_TYPE_TYPE__WGS84_BOUNDING_BOX:
                return wGS84BoundingBox != null && !wGS84BoundingBox.isEmpty();
            case Wfs20Package.FEATURE_TYPE_TYPE__METADATA_URL:
                return metadataURL != null && !metadataURL.isEmpty();
            case Wfs20Package.FEATURE_TYPE_TYPE__EXTENDED_DESCRIPTION:
                return extendedDescription != null;
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
        result.append(" (name: ");
        result.append(name);
        result.append(", defaultCRS: ");
        result.append(defaultCRS);
        result.append(", otherCRS: ");
        result.append(otherCRS);
        result.append(')');
        return result.toString();
    }

} //FeatureTypeTypeImpl
