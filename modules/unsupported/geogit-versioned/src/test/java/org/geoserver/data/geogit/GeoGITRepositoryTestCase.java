/* Copyright (c) 2011 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.data.geogit;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import junit.framework.TestCase;

import org.apache.commons.io.FileUtils;
import org.geogit.api.GeoGIT;
import org.geogit.api.ObjectId;
import org.geogit.api.Ref;
import org.geogit.api.RevCommit;
import org.geogit.repository.Repository;
import org.geogit.repository.StagingArea;
import org.geogit.repository.Triplet;
import org.geogit.storage.ObjectWriter;
import org.geogit.storage.RepositoryDatabase;
import org.geogit.storage.WrappedSerialisingFactory;
import org.geogit.storage.bdbje.EntityStoreConfig;
import org.geogit.storage.bdbje.EnvironmentBuilder;
import org.geogit.storage.bdbje.JERepositoryDatabase;
import org.geoserver.data.RepositoryTestCase;
import org.geoserver.data.versioning.decorator.DecoratedTestCase;
import org.geotools.data.DataUtilities;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.NameImpl;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.geometry.jts.WKTReader2;
import org.geotools.referencing.CRS;
import org.geotools.util.NullProgressListener;
import org.geotools.util.logging.Logging;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.Name;
import org.opengis.geometry.BoundingBox;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.sleepycat.je.Environment;
import com.sleepycat.je.dbi.DbEnvPool;
import com.sleepycat.je.dbi.EnvironmentImpl;
import com.vividsolutions.jts.io.ParseException;

public abstract class GeoGITRepositoryTestCase extends RepositoryTestCase {

    protected static final String idL1 = "Lines.1";

    protected static final String idL2 = "Lines.2";

    protected static final String idL3 = "Lines.3";

    protected static final String idP1 = "Points.1";

    protected static final String idP2 = "Points.2";

    protected static final String idP3 = "Points.3";

    protected static final String pointsNs = "http://geogit.points";

    protected static final String pointsName = "Points";

    protected static final String pointsTypeSpec = "sp:String,ip:Integer,pp:Point:srid=4326";

    protected static final Name pointsTypeName = new NameImpl(pointsNs, pointsName);

    protected SimpleFeatureType pointsType;

    protected Feature points1;

    protected Feature points2;

    protected Feature points3;

    protected static final String linesNs = "http://geogit.lines";

    protected static final String linesName = "Lines";

    protected static final String linesTypeSpec = "sp:String,ip:Integer,pp:LineString:srid=4326";

    protected static final Name linesTypeName = new NameImpl(linesNs, linesName);

    protected SimpleFeatureType linesType;

    protected Feature lines1;

    protected Feature lines2;

    protected Feature lines3;

    private boolean setupInternal = false;

    @Override
    protected final void setUpInternal() throws Exception {
        if (setupInternal) {
            throw new IllegalStateException("Are you calling super.setUpInternal()!?");
        }
        setupInternal = true;
        pointsType = DataUtilities.createType(pointsNs, pointsName, pointsTypeSpec);

        points1 = feature(pointsType, idP1, "StringProp1_1", new Integer(1000), "POINT(1 1)");
        points2 = feature(pointsType, idP2, "StringProp1_2", new Integer(2000), "POINT(2 2)");
        points3 = feature(pointsType, idP3, "StringProp1_3", new Integer(3000), "POINT(3 3)");

        linesType = DataUtilities.createType(linesNs, linesName, linesTypeSpec);

        lines1 = feature(linesType, idL1, "StringProp2_1", new Integer(1000),
                "LINESTRING (1 1, 2 2)");
        lines2 = feature(linesType, idL2, "StringProp2_2", new Integer(2000),
                "LINESTRING (3 3, 4 4)");
        lines3 = feature(linesType, idL3, "StringProp2_3", new Integer(3000),
                "LINESTRING (5 5, 6 6)");

        setUpChild();
    }

    @Override
    protected final void tearDownInternal() throws Exception {
        if (!setupInternal) {
            throw new IllegalStateException("Are you calling super.tearDownInternal()!?");
        }
        setupInternal = false;
        tearDownChild();
        
    }

    /**
     * Called as the last step in {@link #setUp()}
     */
    protected abstract void setUpChild() throws Exception;

    /**
     * Called before {@link #tearDown()}, subclasses may override as appropriate
     */
    protected abstract void tearDownChild() throws Exception;

    public Repository getRepository() {
        return repo;
    }

    protected List<RevCommit> populate(boolean oneCommitPerFeature, Feature... features)
            throws Exception {
        return populate(oneCommitPerFeature, Arrays.asList(features));
    }

    protected List<RevCommit> populate(boolean oneCommitPerFeature, List<Feature> features)
            throws Exception {

        final GeoGIT ggit = new GeoGIT(getRepository());

        List<RevCommit> commits = new ArrayList<RevCommit>();

        for (Feature f : features) {
            insertAndAdd(f);
            if (oneCommitPerFeature) {
                RevCommit commit = ggit.commit().call();
                commits.add(commit);
            }
        }

        if (!oneCommitPerFeature) {
            RevCommit commit = ggit.commit().call();
            commits.add(commit);
        }

        return commits;
    }

    /**
     * Inserts the Feature to the index and stages it to be committed.
     */
    protected ObjectId insertAndAdd(Feature f) throws Exception {
        ObjectId objectId = insert(f);

        new GeoGIT(getRepository()).add().call();
        return objectId;
    }

    /**
     * Inserts the feature to the index but does not stages it to be committed
     */
    protected ObjectId insert(Feature f) throws Exception {
        final StagingArea index = getRepository().getIndex();
        Name name = f.getType().getName();
        String namespaceURI = name.getNamespaceURI();
        String localPart = name.getLocalPart();
        String id = f.getIdentifier().getID();

        Ref ref = index.inserted(
        		WrappedSerialisingFactory.getInstance().createFeatureWriter(f), f.getBounds(), namespaceURI, localPart, id);
        ObjectId objectId = ref.getObjectId();
        return objectId;
    }

    protected void insertAndAdd(Feature... features) throws Exception {
        insert(features);
        new GeoGIT(getRepository()).add().call();
    }

    protected void insert(Feature... features) throws Exception {

        final StagingArea index = getRepository().getIndex();

        Iterator<Triplet<ObjectWriter<?>, BoundingBox, List<String>>> iterator;
        Function<Feature, Triplet<ObjectWriter<?>, BoundingBox, List<String>>> function = new Function<Feature, Triplet<ObjectWriter<?>, BoundingBox, List<String>>>() {

            @Override
            public Triplet<ObjectWriter<?>, BoundingBox, List<String>> apply(final Feature f) {
                Name name = f.getType().getName();
                String namespaceURI = name.getNamespaceURI();
                String localPart = name.getLocalPart();
                String id = f.getIdentifier().getID();

                Triplet<ObjectWriter<?>, BoundingBox, List<String>> tuple;
                ObjectWriter<?> writer = WrappedSerialisingFactory.getInstance().createFeatureWriter(f);
                BoundingBox bounds = f.getBounds();
                List<String> path = Arrays.asList(namespaceURI, localPart, id);
                tuple = new Triplet<ObjectWriter<?>, BoundingBox, List<String>>(writer, bounds,
                        path);
                return tuple;
            }
        };

        iterator = Iterators.transform(Iterators.forArray(features), function);

        index.inserted(iterator, new NullProgressListener(), null);

    }

    /**
     * Deletes a feature from the index
     * 
     * @param f
     * @return
     * @throws Exception
     */
    protected boolean deleteAndAdd(Feature f) throws Exception {
        boolean existed = delete(f);
        if (existed) {
            new GeoGIT(getRepository()).add().call();
        }

        return existed;
    }

    protected boolean delete(Feature f) throws Exception {
        final StagingArea index = getRepository().getIndex();
        Name name = f.getType().getName();
        String namespaceURI = name.getNamespaceURI();
        String localPart = name.getLocalPart();
        String id = f.getIdentifier().getID();
        boolean existed = index.deleted(namespaceURI, localPart, id);
        return existed;
    }

    protected <F extends Feature> List<F> toList(FeatureIterator<F> logs) {
        List<F> logged = new ArrayList<F>();
        try {
            while( logs.hasNext() ){
                logged.add( logs.next() );
            }
        }
        finally {
            logs.close();
        }
        return logged;
    }
    
    protected <E> List<E> toList(Iterator<E> logs) {
        List<E> logged = new ArrayList<E>();
        Iterators.addAll(logged, logs);
        return logged;
    }

    protected <E> List<E> toList(Iterable<E> logs) {
        List<E> logged = new ArrayList<E>();
        Iterables.addAll(logged, logs);
        return logged;
    }

    /**
     * Computes the aggregated bounds of {@code features}, assuming all of them are in the same CRS
     */
    protected ReferencedEnvelope boundsOf(Feature... features) {
        ReferencedEnvelope bounds = null;
        for (int i = 0; i < features.length; i++) {
            Feature f = features[i];
            if (bounds == null) {
                bounds = (ReferencedEnvelope) f.getBounds();
            } else {
                bounds.include(f.getBounds());
            }
        }
        return bounds;
    }

    /**
     * Computes the aggregated bounds of {@code features} in the {@code targetCrs}
     */
    protected ReferencedEnvelope boundsOf(CoordinateReferenceSystem targetCrs, Feature... features)
            throws Exception {
        ReferencedEnvelope bounds = new ReferencedEnvelope(targetCrs);

        for (int i = 0; i < features.length; i++) {
            Feature f = features[i];
            BoundingBox fbounds = f.getBounds();
            if (!CRS.equalsIgnoreMetadata(targetCrs, fbounds)) {
                fbounds = fbounds.toBounds(targetCrs);
            }
            bounds.include(fbounds);
        }
        return bounds;
    }
}
