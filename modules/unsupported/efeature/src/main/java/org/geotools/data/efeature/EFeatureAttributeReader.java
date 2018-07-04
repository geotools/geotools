package org.geotools.data.efeature;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.WeakHashMap;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.geotools.data.AttributeReader;
import org.geotools.data.Query;
import org.geotools.data.efeature.query.EFeatureEncoderException;
import org.geotools.data.efeature.query.EFeatureQuery;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;

/**
 * @author kengu
 * @source $URL$
 */
public class EFeatureAttributeReader implements AttributeReader {

    private final SimpleFeatureType type;

    private final EFeatureFolderInfo eFolderInfo;

    private final EFeatureInfo eFeatureInfo;

    private EFeatureQuery eQuery;

    private EObject eNext;

    private EFeatureIterator eIterator;

    private Map<String, EAttribute> eAttributes =
            Collections.synchronizedMap(new WeakHashMap<String, EAttribute>());

    // -----------------------------------------------------
    //  Constructors
    // -----------------------------------------------------

    /**
     * The {@link EFeatureAttributeReader} constructor
     *
     * @param eDataStore - {@link EFeatureDataStore} instance
     * @param query - GeoTools {@link Query} instance
     * @throws IOException
     */
    public EFeatureAttributeReader(EFeatureDataStore eDataStore, Query query) throws IOException {
        //
        // Initialize reader
        //
        String eType = query.getTypeName();
        String eFolder = EFeatureUtils.toFolderName(eType);
        String eFeature = EFeatureUtils.toFeatureName(eType);
        eFolderInfo = eDataStore.ePackageInfo.eGetFolderInfo(eFolder);
        eFeatureInfo = eFolderInfo.eGetFeatureInfo(eFeature);
        this.type = eFeatureInfo.getFeatureType();
        //
        // Get attribute names from query
        //
        String[] eNames = (query == null ? new String[0] : query.getPropertyNames());
        //
        // Get all attributes as defined by EFeatureInfo?
        //
        if (eNames == null || eNames.length == 0) {
            //
            // Get subset of attributes from EClass as defined by EFeatureInfo only
            //
            this.eAttributes.putAll(eFeatureInfo.eGetAttributeMap(true));
        } else {
            //
            // Get subset of attributes as defined by EFeatureInfo AND GeoTools Query
            //
            this.eAttributes.putAll(eFeatureInfo.eGetAttributeMap(eNames, true));
        }
        //
        // Get objects as tree iterator (enables lazy loading)
        //
        TreeIterator<EObject> eObjects = eDataStore.eResource().getAllContents();
        //
        // Convert GeoTools query to EFeatureQuery statement
        //
        try {
            eQuery = EFeatureUtils.toEFeatureQuery(eFeatureInfo, eObjects, query.getFilter());
        } catch (EFeatureEncoderException ex) {
            throw (IOException) new IOException("Failed to create EFeatureQuery").initCause(ex);
        }
    }

    // -----------------------------------------------------
    //  EFeatureAttributeReader methods
    // -----------------------------------------------------

    /**
     * Get {@link SimpleFeatureType} instance.
     *
     * @return a {@link SimpleFeatureType} instance.
     */
    public SimpleFeatureType getFeatureType() {
        return type;
    }

    @Override
    public int getAttributeCount() {
        return type.getAttributeCount();
    }

    @Override
    public AttributeDescriptor getAttributeType(int index) throws ArrayIndexOutOfBoundsException {
        return type.getDescriptor(index);
    }

    public void reset() {
        this.eNext = null;
        this.eIterator = null;
    }

    @Override
    public void close() throws IOException {
        this.eNext = null;
        this.eIterator = null;
        this.eQuery.dispose();
        this.eQuery = null;
        this.eAttributes.clear();
    }

    @Override
    public boolean hasNext() throws IOException {
        if (eNext != null) {
            return true;
        }
        if (eIterator == null) {
            // Get EFeature iterator
            //
            eIterator = eQuery.iterator();
        }
        if (eIterator.hasNext()) {
            eNext = eIterator.next();
        }
        return eNext != null;
    }

    @Override
    public void next() throws IOException {
        if (hasNext()) {
            eNext = null;
        } else {
            throw new NoSuchElementException();
        }
    }

    public EObject get() {
        return eNext;
    }

    @Override
    public Object read(int index) throws IOException, ArrayIndexOutOfBoundsException {

        if (eNext == null) {
            throw new IOException("No content available - did you remeber to call next?");
        }

        EAttribute eAttribute = eAttributes.get(getAttributeName(index));

        return (eAttribute != null ? eNext.eGet(eAttribute) : null);
    }

    /**
     * Get {@link EFeature} attribute name at given index
     *
     * @param index - given index
     * @return a {@link EFeature} attribute name if found.
     * @see {@link AttributeDescriptor#getLocalName()}
     */
    public String getAttributeName(int index) {
        return getAttributeType(index).getLocalName();
    }

    // -----------------------------------------------------
    //  Helper methods
    // -----------------------------------------------------

    /**
     * Get current {@link EFeature} id
     *
     * @return a {@link EFeature} id.
     * @see {@link EcoreUtil#getID(EObject)}
     */
    protected String getFeatureID() {
        if (eNext == null) {
            return null;
        }
        return EcoreUtil.getID(eNext);
    }
}
