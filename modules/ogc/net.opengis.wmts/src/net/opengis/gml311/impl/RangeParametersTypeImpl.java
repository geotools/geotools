/**
 */
package net.opengis.gml311.impl;

import java.math.BigInteger;

import java.util.List;

import net.opengis.gml311.CategoryExtentType;
import net.opengis.gml311.CodeOrNullListType;
import net.opengis.gml311.CodeType;
import net.opengis.gml311.CompositeValueType;
import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.MeasureOrNullListType;
import net.opengis.gml311.MeasureType;
import net.opengis.gml311.QuantityExtentType;
import net.opengis.gml311.RangeParametersType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

import org.w3.xlink.ActuateType;
import org.w3.xlink.ShowType;
import org.w3.xlink.TypeType;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Range Parameters Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.RangeParametersTypeImpl#isBoolean <em>Boolean</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.RangeParametersTypeImpl#getCategory <em>Category</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.RangeParametersTypeImpl#getQuantity <em>Quantity</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.RangeParametersTypeImpl#getCount <em>Count</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.RangeParametersTypeImpl#getBooleanList <em>Boolean List</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.RangeParametersTypeImpl#getCategoryList <em>Category List</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.RangeParametersTypeImpl#getQuantityList <em>Quantity List</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.RangeParametersTypeImpl#getCountList <em>Count List</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.RangeParametersTypeImpl#getCategoryExtent <em>Category Extent</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.RangeParametersTypeImpl#getQuantityExtent <em>Quantity Extent</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.RangeParametersTypeImpl#getCountExtent <em>Count Extent</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.RangeParametersTypeImpl#getCompositeValueGroup <em>Composite Value Group</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.RangeParametersTypeImpl#getCompositeValue <em>Composite Value</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.RangeParametersTypeImpl#getActuate <em>Actuate</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.RangeParametersTypeImpl#getArcrole <em>Arcrole</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.RangeParametersTypeImpl#getHref <em>Href</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.RangeParametersTypeImpl#getRemoteSchema <em>Remote Schema</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.RangeParametersTypeImpl#getRole <em>Role</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.RangeParametersTypeImpl#getShow <em>Show</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.RangeParametersTypeImpl#getTitle <em>Title</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.RangeParametersTypeImpl#getType <em>Type</em>}</li>
 * </ul>
 *
 * @generated
 */
public class RangeParametersTypeImpl extends MinimalEObjectImpl.Container implements RangeParametersType {
    /**
     * The default value of the '{@link #isBoolean() <em>Boolean</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isBoolean()
     * @generated
     * @ordered
     */
    protected static final boolean BOOLEAN_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isBoolean() <em>Boolean</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isBoolean()
     * @generated
     * @ordered
     */
    protected boolean boolean_ = BOOLEAN_EDEFAULT;

    /**
     * This is true if the Boolean attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean booleanESet;

    /**
     * The cached value of the '{@link #getCategory() <em>Category</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCategory()
     * @generated
     * @ordered
     */
    protected CodeType category;

    /**
     * The cached value of the '{@link #getQuantity() <em>Quantity</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getQuantity()
     * @generated
     * @ordered
     */
    protected MeasureType quantity;

    /**
     * The default value of the '{@link #getCount() <em>Count</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCount()
     * @generated
     * @ordered
     */
    protected static final BigInteger COUNT_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getCount() <em>Count</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCount()
     * @generated
     * @ordered
     */
    protected BigInteger count = COUNT_EDEFAULT;

    /**
     * The default value of the '{@link #getBooleanList() <em>Boolean List</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getBooleanList()
     * @generated
     * @ordered
     */
    protected static final List<Object> BOOLEAN_LIST_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getBooleanList() <em>Boolean List</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getBooleanList()
     * @generated
     * @ordered
     */
    protected List<Object> booleanList = BOOLEAN_LIST_EDEFAULT;

    /**
     * The cached value of the '{@link #getCategoryList() <em>Category List</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCategoryList()
     * @generated
     * @ordered
     */
    protected CodeOrNullListType categoryList;

    /**
     * The cached value of the '{@link #getQuantityList() <em>Quantity List</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getQuantityList()
     * @generated
     * @ordered
     */
    protected MeasureOrNullListType quantityList;

    /**
     * The default value of the '{@link #getCountList() <em>Count List</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCountList()
     * @generated
     * @ordered
     */
    protected static final List<Object> COUNT_LIST_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getCountList() <em>Count List</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCountList()
     * @generated
     * @ordered
     */
    protected List<Object> countList = COUNT_LIST_EDEFAULT;

    /**
     * The cached value of the '{@link #getCategoryExtent() <em>Category Extent</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCategoryExtent()
     * @generated
     * @ordered
     */
    protected CategoryExtentType categoryExtent;

    /**
     * The cached value of the '{@link #getQuantityExtent() <em>Quantity Extent</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getQuantityExtent()
     * @generated
     * @ordered
     */
    protected QuantityExtentType quantityExtent;

    /**
     * The default value of the '{@link #getCountExtent() <em>Count Extent</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCountExtent()
     * @generated
     * @ordered
     */
    protected static final List<Object> COUNT_EXTENT_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getCountExtent() <em>Count Extent</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCountExtent()
     * @generated
     * @ordered
     */
    protected List<Object> countExtent = COUNT_EXTENT_EDEFAULT;

    /**
     * The cached value of the '{@link #getCompositeValueGroup() <em>Composite Value Group</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCompositeValueGroup()
     * @generated
     * @ordered
     */
    protected FeatureMap compositeValueGroup;

    /**
     * The default value of the '{@link #getActuate() <em>Actuate</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getActuate()
     * @generated
     * @ordered
     */
    protected static final ActuateType ACTUATE_EDEFAULT = ActuateType.ON_LOAD_LITERAL;

    /**
     * The cached value of the '{@link #getActuate() <em>Actuate</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getActuate()
     * @generated
     * @ordered
     */
    protected ActuateType actuate = ACTUATE_EDEFAULT;

    /**
     * This is true if the Actuate attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean actuateESet;

    /**
     * The default value of the '{@link #getArcrole() <em>Arcrole</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getArcrole()
     * @generated
     * @ordered
     */
    protected static final String ARCROLE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getArcrole() <em>Arcrole</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getArcrole()
     * @generated
     * @ordered
     */
    protected String arcrole = ARCROLE_EDEFAULT;

    /**
     * The default value of the '{@link #getHref() <em>Href</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getHref()
     * @generated
     * @ordered
     */
    protected static final String HREF_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getHref() <em>Href</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getHref()
     * @generated
     * @ordered
     */
    protected String href = HREF_EDEFAULT;

    /**
     * The default value of the '{@link #getRemoteSchema() <em>Remote Schema</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRemoteSchema()
     * @generated
     * @ordered
     */
    protected static final String REMOTE_SCHEMA_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getRemoteSchema() <em>Remote Schema</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRemoteSchema()
     * @generated
     * @ordered
     */
    protected String remoteSchema = REMOTE_SCHEMA_EDEFAULT;

    /**
     * The default value of the '{@link #getRole() <em>Role</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRole()
     * @generated
     * @ordered
     */
    protected static final String ROLE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getRole() <em>Role</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRole()
     * @generated
     * @ordered
     */
    protected String role = ROLE_EDEFAULT;

    /**
     * The default value of the '{@link #getShow() <em>Show</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getShow()
     * @generated
     * @ordered
     */
    protected static final ShowType SHOW_EDEFAULT = ShowType.NEW_LITERAL;

    /**
     * The cached value of the '{@link #getShow() <em>Show</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getShow()
     * @generated
     * @ordered
     */
    protected ShowType show = SHOW_EDEFAULT;

    /**
     * This is true if the Show attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean showESet;

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
     * The default value of the '{@link #getType() <em>Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getType()
     * @generated
     * @ordered
     */
    protected static final TypeType TYPE_EDEFAULT = TypeType.SIMPLE_LITERAL;

    /**
     * The cached value of the '{@link #getType() <em>Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getType()
     * @generated
     * @ordered
     */
    protected TypeType type = TYPE_EDEFAULT;

    /**
     * This is true if the Type attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean typeESet;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected RangeParametersTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getRangeParametersType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isBoolean() {
        return boolean_;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setBoolean(boolean newBoolean) {
        boolean oldBoolean = boolean_;
        boolean_ = newBoolean;
        boolean oldBooleanESet = booleanESet;
        booleanESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.RANGE_PARAMETERS_TYPE__BOOLEAN, oldBoolean, boolean_, !oldBooleanESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetBoolean() {
        boolean oldBoolean = boolean_;
        boolean oldBooleanESet = booleanESet;
        boolean_ = BOOLEAN_EDEFAULT;
        booleanESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Gml311Package.RANGE_PARAMETERS_TYPE__BOOLEAN, oldBoolean, BOOLEAN_EDEFAULT, oldBooleanESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetBoolean() {
        return booleanESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CodeType getCategory() {
        return category;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetCategory(CodeType newCategory, NotificationChain msgs) {
        CodeType oldCategory = category;
        category = newCategory;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.RANGE_PARAMETERS_TYPE__CATEGORY, oldCategory, newCategory);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setCategory(CodeType newCategory) {
        if (newCategory != category) {
            NotificationChain msgs = null;
            if (category != null)
                msgs = ((InternalEObject)category).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.RANGE_PARAMETERS_TYPE__CATEGORY, null, msgs);
            if (newCategory != null)
                msgs = ((InternalEObject)newCategory).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.RANGE_PARAMETERS_TYPE__CATEGORY, null, msgs);
            msgs = basicSetCategory(newCategory, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.RANGE_PARAMETERS_TYPE__CATEGORY, newCategory, newCategory));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public MeasureType getQuantity() {
        return quantity;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetQuantity(MeasureType newQuantity, NotificationChain msgs) {
        MeasureType oldQuantity = quantity;
        quantity = newQuantity;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.RANGE_PARAMETERS_TYPE__QUANTITY, oldQuantity, newQuantity);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setQuantity(MeasureType newQuantity) {
        if (newQuantity != quantity) {
            NotificationChain msgs = null;
            if (quantity != null)
                msgs = ((InternalEObject)quantity).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.RANGE_PARAMETERS_TYPE__QUANTITY, null, msgs);
            if (newQuantity != null)
                msgs = ((InternalEObject)newQuantity).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.RANGE_PARAMETERS_TYPE__QUANTITY, null, msgs);
            msgs = basicSetQuantity(newQuantity, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.RANGE_PARAMETERS_TYPE__QUANTITY, newQuantity, newQuantity));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BigInteger getCount() {
        return count;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setCount(BigInteger newCount) {
        BigInteger oldCount = count;
        count = newCount;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.RANGE_PARAMETERS_TYPE__COUNT, oldCount, count));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public List<Object> getBooleanList() {
        return booleanList;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setBooleanList(List<Object> newBooleanList) {
        List<Object> oldBooleanList = booleanList;
        booleanList = newBooleanList;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.RANGE_PARAMETERS_TYPE__BOOLEAN_LIST, oldBooleanList, booleanList));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CodeOrNullListType getCategoryList() {
        return categoryList;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetCategoryList(CodeOrNullListType newCategoryList, NotificationChain msgs) {
        CodeOrNullListType oldCategoryList = categoryList;
        categoryList = newCategoryList;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.RANGE_PARAMETERS_TYPE__CATEGORY_LIST, oldCategoryList, newCategoryList);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setCategoryList(CodeOrNullListType newCategoryList) {
        if (newCategoryList != categoryList) {
            NotificationChain msgs = null;
            if (categoryList != null)
                msgs = ((InternalEObject)categoryList).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.RANGE_PARAMETERS_TYPE__CATEGORY_LIST, null, msgs);
            if (newCategoryList != null)
                msgs = ((InternalEObject)newCategoryList).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.RANGE_PARAMETERS_TYPE__CATEGORY_LIST, null, msgs);
            msgs = basicSetCategoryList(newCategoryList, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.RANGE_PARAMETERS_TYPE__CATEGORY_LIST, newCategoryList, newCategoryList));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public MeasureOrNullListType getQuantityList() {
        return quantityList;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetQuantityList(MeasureOrNullListType newQuantityList, NotificationChain msgs) {
        MeasureOrNullListType oldQuantityList = quantityList;
        quantityList = newQuantityList;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.RANGE_PARAMETERS_TYPE__QUANTITY_LIST, oldQuantityList, newQuantityList);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setQuantityList(MeasureOrNullListType newQuantityList) {
        if (newQuantityList != quantityList) {
            NotificationChain msgs = null;
            if (quantityList != null)
                msgs = ((InternalEObject)quantityList).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.RANGE_PARAMETERS_TYPE__QUANTITY_LIST, null, msgs);
            if (newQuantityList != null)
                msgs = ((InternalEObject)newQuantityList).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.RANGE_PARAMETERS_TYPE__QUANTITY_LIST, null, msgs);
            msgs = basicSetQuantityList(newQuantityList, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.RANGE_PARAMETERS_TYPE__QUANTITY_LIST, newQuantityList, newQuantityList));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public List<Object> getCountList() {
        return countList;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setCountList(List<Object> newCountList) {
        List<Object> oldCountList = countList;
        countList = newCountList;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.RANGE_PARAMETERS_TYPE__COUNT_LIST, oldCountList, countList));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CategoryExtentType getCategoryExtent() {
        return categoryExtent;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetCategoryExtent(CategoryExtentType newCategoryExtent, NotificationChain msgs) {
        CategoryExtentType oldCategoryExtent = categoryExtent;
        categoryExtent = newCategoryExtent;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.RANGE_PARAMETERS_TYPE__CATEGORY_EXTENT, oldCategoryExtent, newCategoryExtent);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setCategoryExtent(CategoryExtentType newCategoryExtent) {
        if (newCategoryExtent != categoryExtent) {
            NotificationChain msgs = null;
            if (categoryExtent != null)
                msgs = ((InternalEObject)categoryExtent).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.RANGE_PARAMETERS_TYPE__CATEGORY_EXTENT, null, msgs);
            if (newCategoryExtent != null)
                msgs = ((InternalEObject)newCategoryExtent).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.RANGE_PARAMETERS_TYPE__CATEGORY_EXTENT, null, msgs);
            msgs = basicSetCategoryExtent(newCategoryExtent, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.RANGE_PARAMETERS_TYPE__CATEGORY_EXTENT, newCategoryExtent, newCategoryExtent));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public QuantityExtentType getQuantityExtent() {
        return quantityExtent;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetQuantityExtent(QuantityExtentType newQuantityExtent, NotificationChain msgs) {
        QuantityExtentType oldQuantityExtent = quantityExtent;
        quantityExtent = newQuantityExtent;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.RANGE_PARAMETERS_TYPE__QUANTITY_EXTENT, oldQuantityExtent, newQuantityExtent);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setQuantityExtent(QuantityExtentType newQuantityExtent) {
        if (newQuantityExtent != quantityExtent) {
            NotificationChain msgs = null;
            if (quantityExtent != null)
                msgs = ((InternalEObject)quantityExtent).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.RANGE_PARAMETERS_TYPE__QUANTITY_EXTENT, null, msgs);
            if (newQuantityExtent != null)
                msgs = ((InternalEObject)newQuantityExtent).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.RANGE_PARAMETERS_TYPE__QUANTITY_EXTENT, null, msgs);
            msgs = basicSetQuantityExtent(newQuantityExtent, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.RANGE_PARAMETERS_TYPE__QUANTITY_EXTENT, newQuantityExtent, newQuantityExtent));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public List<Object> getCountExtent() {
        return countExtent;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setCountExtent(List<Object> newCountExtent) {
        List<Object> oldCountExtent = countExtent;
        countExtent = newCountExtent;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.RANGE_PARAMETERS_TYPE__COUNT_EXTENT, oldCountExtent, countExtent));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureMap getCompositeValueGroup() {
        if (compositeValueGroup == null) {
            compositeValueGroup = new BasicFeatureMap(this, Gml311Package.RANGE_PARAMETERS_TYPE__COMPOSITE_VALUE_GROUP);
        }
        return compositeValueGroup;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CompositeValueType getCompositeValue() {
        return (CompositeValueType)getCompositeValueGroup().get(Gml311Package.eINSTANCE.getRangeParametersType_CompositeValue(), true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetCompositeValue(CompositeValueType newCompositeValue, NotificationChain msgs) {
        return ((FeatureMap.Internal)getCompositeValueGroup()).basicAdd(Gml311Package.eINSTANCE.getRangeParametersType_CompositeValue(), newCompositeValue, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setCompositeValue(CompositeValueType newCompositeValue) {
        ((FeatureMap.Internal)getCompositeValueGroup()).set(Gml311Package.eINSTANCE.getRangeParametersType_CompositeValue(), newCompositeValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ActuateType getActuate() {
        return actuate;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setActuate(ActuateType newActuate) {
        ActuateType oldActuate = actuate;
        actuate = newActuate == null ? ACTUATE_EDEFAULT : newActuate;
        boolean oldActuateESet = actuateESet;
        actuateESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.RANGE_PARAMETERS_TYPE__ACTUATE, oldActuate, actuate, !oldActuateESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetActuate() {
        ActuateType oldActuate = actuate;
        boolean oldActuateESet = actuateESet;
        actuate = ACTUATE_EDEFAULT;
        actuateESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Gml311Package.RANGE_PARAMETERS_TYPE__ACTUATE, oldActuate, ACTUATE_EDEFAULT, oldActuateESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetActuate() {
        return actuateESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getArcrole() {
        return arcrole;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setArcrole(String newArcrole) {
        String oldArcrole = arcrole;
        arcrole = newArcrole;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.RANGE_PARAMETERS_TYPE__ARCROLE, oldArcrole, arcrole));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getHref() {
        return href;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setHref(String newHref) {
        String oldHref = href;
        href = newHref;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.RANGE_PARAMETERS_TYPE__HREF, oldHref, href));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getRemoteSchema() {
        return remoteSchema;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setRemoteSchema(String newRemoteSchema) {
        String oldRemoteSchema = remoteSchema;
        remoteSchema = newRemoteSchema;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.RANGE_PARAMETERS_TYPE__REMOTE_SCHEMA, oldRemoteSchema, remoteSchema));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getRole() {
        return role;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setRole(String newRole) {
        String oldRole = role;
        role = newRole;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.RANGE_PARAMETERS_TYPE__ROLE, oldRole, role));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ShowType getShow() {
        return show;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setShow(ShowType newShow) {
        ShowType oldShow = show;
        show = newShow == null ? SHOW_EDEFAULT : newShow;
        boolean oldShowESet = showESet;
        showESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.RANGE_PARAMETERS_TYPE__SHOW, oldShow, show, !oldShowESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetShow() {
        ShowType oldShow = show;
        boolean oldShowESet = showESet;
        show = SHOW_EDEFAULT;
        showESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Gml311Package.RANGE_PARAMETERS_TYPE__SHOW, oldShow, SHOW_EDEFAULT, oldShowESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetShow() {
        return showESet;
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
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.RANGE_PARAMETERS_TYPE__TITLE, oldTitle, title));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TypeType getType() {
        return type;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setType(TypeType newType) {
        TypeType oldType = type;
        type = newType == null ? TYPE_EDEFAULT : newType;
        boolean oldTypeESet = typeESet;
        typeESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.RANGE_PARAMETERS_TYPE__TYPE, oldType, type, !oldTypeESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetType() {
        TypeType oldType = type;
        boolean oldTypeESet = typeESet;
        type = TYPE_EDEFAULT;
        typeESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Gml311Package.RANGE_PARAMETERS_TYPE__TYPE, oldType, TYPE_EDEFAULT, oldTypeESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetType() {
        return typeESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.RANGE_PARAMETERS_TYPE__CATEGORY:
                return basicSetCategory(null, msgs);
            case Gml311Package.RANGE_PARAMETERS_TYPE__QUANTITY:
                return basicSetQuantity(null, msgs);
            case Gml311Package.RANGE_PARAMETERS_TYPE__CATEGORY_LIST:
                return basicSetCategoryList(null, msgs);
            case Gml311Package.RANGE_PARAMETERS_TYPE__QUANTITY_LIST:
                return basicSetQuantityList(null, msgs);
            case Gml311Package.RANGE_PARAMETERS_TYPE__CATEGORY_EXTENT:
                return basicSetCategoryExtent(null, msgs);
            case Gml311Package.RANGE_PARAMETERS_TYPE__QUANTITY_EXTENT:
                return basicSetQuantityExtent(null, msgs);
            case Gml311Package.RANGE_PARAMETERS_TYPE__COMPOSITE_VALUE_GROUP:
                return ((InternalEList<?>)getCompositeValueGroup()).basicRemove(otherEnd, msgs);
            case Gml311Package.RANGE_PARAMETERS_TYPE__COMPOSITE_VALUE:
                return basicSetCompositeValue(null, msgs);
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
            case Gml311Package.RANGE_PARAMETERS_TYPE__BOOLEAN:
                return isBoolean();
            case Gml311Package.RANGE_PARAMETERS_TYPE__CATEGORY:
                return getCategory();
            case Gml311Package.RANGE_PARAMETERS_TYPE__QUANTITY:
                return getQuantity();
            case Gml311Package.RANGE_PARAMETERS_TYPE__COUNT:
                return getCount();
            case Gml311Package.RANGE_PARAMETERS_TYPE__BOOLEAN_LIST:
                return getBooleanList();
            case Gml311Package.RANGE_PARAMETERS_TYPE__CATEGORY_LIST:
                return getCategoryList();
            case Gml311Package.RANGE_PARAMETERS_TYPE__QUANTITY_LIST:
                return getQuantityList();
            case Gml311Package.RANGE_PARAMETERS_TYPE__COUNT_LIST:
                return getCountList();
            case Gml311Package.RANGE_PARAMETERS_TYPE__CATEGORY_EXTENT:
                return getCategoryExtent();
            case Gml311Package.RANGE_PARAMETERS_TYPE__QUANTITY_EXTENT:
                return getQuantityExtent();
            case Gml311Package.RANGE_PARAMETERS_TYPE__COUNT_EXTENT:
                return getCountExtent();
            case Gml311Package.RANGE_PARAMETERS_TYPE__COMPOSITE_VALUE_GROUP:
                if (coreType) return getCompositeValueGroup();
                return ((FeatureMap.Internal)getCompositeValueGroup()).getWrapper();
            case Gml311Package.RANGE_PARAMETERS_TYPE__COMPOSITE_VALUE:
                return getCompositeValue();
            case Gml311Package.RANGE_PARAMETERS_TYPE__ACTUATE:
                return getActuate();
            case Gml311Package.RANGE_PARAMETERS_TYPE__ARCROLE:
                return getArcrole();
            case Gml311Package.RANGE_PARAMETERS_TYPE__HREF:
                return getHref();
            case Gml311Package.RANGE_PARAMETERS_TYPE__REMOTE_SCHEMA:
                return getRemoteSchema();
            case Gml311Package.RANGE_PARAMETERS_TYPE__ROLE:
                return getRole();
            case Gml311Package.RANGE_PARAMETERS_TYPE__SHOW:
                return getShow();
            case Gml311Package.RANGE_PARAMETERS_TYPE__TITLE:
                return getTitle();
            case Gml311Package.RANGE_PARAMETERS_TYPE__TYPE:
                return getType();
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
            case Gml311Package.RANGE_PARAMETERS_TYPE__BOOLEAN:
                setBoolean((Boolean)newValue);
                return;
            case Gml311Package.RANGE_PARAMETERS_TYPE__CATEGORY:
                setCategory((CodeType)newValue);
                return;
            case Gml311Package.RANGE_PARAMETERS_TYPE__QUANTITY:
                setQuantity((MeasureType)newValue);
                return;
            case Gml311Package.RANGE_PARAMETERS_TYPE__COUNT:
                setCount((BigInteger)newValue);
                return;
            case Gml311Package.RANGE_PARAMETERS_TYPE__BOOLEAN_LIST:
                setBooleanList((List<Object>)newValue);
                return;
            case Gml311Package.RANGE_PARAMETERS_TYPE__CATEGORY_LIST:
                setCategoryList((CodeOrNullListType)newValue);
                return;
            case Gml311Package.RANGE_PARAMETERS_TYPE__QUANTITY_LIST:
                setQuantityList((MeasureOrNullListType)newValue);
                return;
            case Gml311Package.RANGE_PARAMETERS_TYPE__COUNT_LIST:
                setCountList((List<Object>)newValue);
                return;
            case Gml311Package.RANGE_PARAMETERS_TYPE__CATEGORY_EXTENT:
                setCategoryExtent((CategoryExtentType)newValue);
                return;
            case Gml311Package.RANGE_PARAMETERS_TYPE__QUANTITY_EXTENT:
                setQuantityExtent((QuantityExtentType)newValue);
                return;
            case Gml311Package.RANGE_PARAMETERS_TYPE__COUNT_EXTENT:
                setCountExtent((List<Object>)newValue);
                return;
            case Gml311Package.RANGE_PARAMETERS_TYPE__COMPOSITE_VALUE_GROUP:
                ((FeatureMap.Internal)getCompositeValueGroup()).set(newValue);
                return;
            case Gml311Package.RANGE_PARAMETERS_TYPE__COMPOSITE_VALUE:
                setCompositeValue((CompositeValueType)newValue);
                return;
            case Gml311Package.RANGE_PARAMETERS_TYPE__ACTUATE:
                setActuate((ActuateType)newValue);
                return;
            case Gml311Package.RANGE_PARAMETERS_TYPE__ARCROLE:
                setArcrole((String)newValue);
                return;
            case Gml311Package.RANGE_PARAMETERS_TYPE__HREF:
                setHref((String)newValue);
                return;
            case Gml311Package.RANGE_PARAMETERS_TYPE__REMOTE_SCHEMA:
                setRemoteSchema((String)newValue);
                return;
            case Gml311Package.RANGE_PARAMETERS_TYPE__ROLE:
                setRole((String)newValue);
                return;
            case Gml311Package.RANGE_PARAMETERS_TYPE__SHOW:
                setShow((ShowType)newValue);
                return;
            case Gml311Package.RANGE_PARAMETERS_TYPE__TITLE:
                setTitle((String)newValue);
                return;
            case Gml311Package.RANGE_PARAMETERS_TYPE__TYPE:
                setType((TypeType)newValue);
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
            case Gml311Package.RANGE_PARAMETERS_TYPE__BOOLEAN:
                unsetBoolean();
                return;
            case Gml311Package.RANGE_PARAMETERS_TYPE__CATEGORY:
                setCategory((CodeType)null);
                return;
            case Gml311Package.RANGE_PARAMETERS_TYPE__QUANTITY:
                setQuantity((MeasureType)null);
                return;
            case Gml311Package.RANGE_PARAMETERS_TYPE__COUNT:
                setCount(COUNT_EDEFAULT);
                return;
            case Gml311Package.RANGE_PARAMETERS_TYPE__BOOLEAN_LIST:
                setBooleanList(BOOLEAN_LIST_EDEFAULT);
                return;
            case Gml311Package.RANGE_PARAMETERS_TYPE__CATEGORY_LIST:
                setCategoryList((CodeOrNullListType)null);
                return;
            case Gml311Package.RANGE_PARAMETERS_TYPE__QUANTITY_LIST:
                setQuantityList((MeasureOrNullListType)null);
                return;
            case Gml311Package.RANGE_PARAMETERS_TYPE__COUNT_LIST:
                setCountList(COUNT_LIST_EDEFAULT);
                return;
            case Gml311Package.RANGE_PARAMETERS_TYPE__CATEGORY_EXTENT:
                setCategoryExtent((CategoryExtentType)null);
                return;
            case Gml311Package.RANGE_PARAMETERS_TYPE__QUANTITY_EXTENT:
                setQuantityExtent((QuantityExtentType)null);
                return;
            case Gml311Package.RANGE_PARAMETERS_TYPE__COUNT_EXTENT:
                setCountExtent(COUNT_EXTENT_EDEFAULT);
                return;
            case Gml311Package.RANGE_PARAMETERS_TYPE__COMPOSITE_VALUE_GROUP:
                getCompositeValueGroup().clear();
                return;
            case Gml311Package.RANGE_PARAMETERS_TYPE__COMPOSITE_VALUE:
                setCompositeValue((CompositeValueType)null);
                return;
            case Gml311Package.RANGE_PARAMETERS_TYPE__ACTUATE:
                unsetActuate();
                return;
            case Gml311Package.RANGE_PARAMETERS_TYPE__ARCROLE:
                setArcrole(ARCROLE_EDEFAULT);
                return;
            case Gml311Package.RANGE_PARAMETERS_TYPE__HREF:
                setHref(HREF_EDEFAULT);
                return;
            case Gml311Package.RANGE_PARAMETERS_TYPE__REMOTE_SCHEMA:
                setRemoteSchema(REMOTE_SCHEMA_EDEFAULT);
                return;
            case Gml311Package.RANGE_PARAMETERS_TYPE__ROLE:
                setRole(ROLE_EDEFAULT);
                return;
            case Gml311Package.RANGE_PARAMETERS_TYPE__SHOW:
                unsetShow();
                return;
            case Gml311Package.RANGE_PARAMETERS_TYPE__TITLE:
                setTitle(TITLE_EDEFAULT);
                return;
            case Gml311Package.RANGE_PARAMETERS_TYPE__TYPE:
                unsetType();
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
            case Gml311Package.RANGE_PARAMETERS_TYPE__BOOLEAN:
                return isSetBoolean();
            case Gml311Package.RANGE_PARAMETERS_TYPE__CATEGORY:
                return category != null;
            case Gml311Package.RANGE_PARAMETERS_TYPE__QUANTITY:
                return quantity != null;
            case Gml311Package.RANGE_PARAMETERS_TYPE__COUNT:
                return COUNT_EDEFAULT == null ? count != null : !COUNT_EDEFAULT.equals(count);
            case Gml311Package.RANGE_PARAMETERS_TYPE__BOOLEAN_LIST:
                return BOOLEAN_LIST_EDEFAULT == null ? booleanList != null : !BOOLEAN_LIST_EDEFAULT.equals(booleanList);
            case Gml311Package.RANGE_PARAMETERS_TYPE__CATEGORY_LIST:
                return categoryList != null;
            case Gml311Package.RANGE_PARAMETERS_TYPE__QUANTITY_LIST:
                return quantityList != null;
            case Gml311Package.RANGE_PARAMETERS_TYPE__COUNT_LIST:
                return COUNT_LIST_EDEFAULT == null ? countList != null : !COUNT_LIST_EDEFAULT.equals(countList);
            case Gml311Package.RANGE_PARAMETERS_TYPE__CATEGORY_EXTENT:
                return categoryExtent != null;
            case Gml311Package.RANGE_PARAMETERS_TYPE__QUANTITY_EXTENT:
                return quantityExtent != null;
            case Gml311Package.RANGE_PARAMETERS_TYPE__COUNT_EXTENT:
                return COUNT_EXTENT_EDEFAULT == null ? countExtent != null : !COUNT_EXTENT_EDEFAULT.equals(countExtent);
            case Gml311Package.RANGE_PARAMETERS_TYPE__COMPOSITE_VALUE_GROUP:
                return compositeValueGroup != null && !compositeValueGroup.isEmpty();
            case Gml311Package.RANGE_PARAMETERS_TYPE__COMPOSITE_VALUE:
                return getCompositeValue() != null;
            case Gml311Package.RANGE_PARAMETERS_TYPE__ACTUATE:
                return isSetActuate();
            case Gml311Package.RANGE_PARAMETERS_TYPE__ARCROLE:
                return ARCROLE_EDEFAULT == null ? arcrole != null : !ARCROLE_EDEFAULT.equals(arcrole);
            case Gml311Package.RANGE_PARAMETERS_TYPE__HREF:
                return HREF_EDEFAULT == null ? href != null : !HREF_EDEFAULT.equals(href);
            case Gml311Package.RANGE_PARAMETERS_TYPE__REMOTE_SCHEMA:
                return REMOTE_SCHEMA_EDEFAULT == null ? remoteSchema != null : !REMOTE_SCHEMA_EDEFAULT.equals(remoteSchema);
            case Gml311Package.RANGE_PARAMETERS_TYPE__ROLE:
                return ROLE_EDEFAULT == null ? role != null : !ROLE_EDEFAULT.equals(role);
            case Gml311Package.RANGE_PARAMETERS_TYPE__SHOW:
                return isSetShow();
            case Gml311Package.RANGE_PARAMETERS_TYPE__TITLE:
                return TITLE_EDEFAULT == null ? title != null : !TITLE_EDEFAULT.equals(title);
            case Gml311Package.RANGE_PARAMETERS_TYPE__TYPE:
                return isSetType();
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
        result.append(" (boolean: ");
        if (booleanESet) result.append(boolean_); else result.append("<unset>");
        result.append(", count: ");
        result.append(count);
        result.append(", booleanList: ");
        result.append(booleanList);
        result.append(", countList: ");
        result.append(countList);
        result.append(", countExtent: ");
        result.append(countExtent);
        result.append(", compositeValueGroup: ");
        result.append(compositeValueGroup);
        result.append(", actuate: ");
        if (actuateESet) result.append(actuate); else result.append("<unset>");
        result.append(", arcrole: ");
        result.append(arcrole);
        result.append(", href: ");
        result.append(href);
        result.append(", remoteSchema: ");
        result.append(remoteSchema);
        result.append(", role: ");
        result.append(role);
        result.append(", show: ");
        if (showESet) result.append(show); else result.append("<unset>");
        result.append(", title: ");
        result.append(title);
        result.append(", type: ");
        if (typeESet) result.append(type); else result.append("<unset>");
        result.append(')');
        return result.toString();
    }

} //RangeParametersTypeImpl
