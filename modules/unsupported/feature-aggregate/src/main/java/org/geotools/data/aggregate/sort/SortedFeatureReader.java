package org.geotools.data.aggregate.sort;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.DelegateSimpleFeatureReader;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureReader;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;

public class SortedFeatureReader implements SimpleFeatureReader {

    SimpleFeatureReader delegate;

    /**
     * Checks if the schema and the sortBy are suitable for merge/sort. All attributes need to be
     * {@link Serializable}, all sorting attributes need to be {@link Comparable}
     * 
     * @param schema
     * @param sortBy
     * @return
     */
    public static final boolean canSort(SimpleFeatureType schema, SortBy[] sortBy) {
        if (sortBy == SortBy.UNSORTED) {
            return true;
        }

        // check all attributes are serializable
        for (AttributeDescriptor ad : schema.getAttributeDescriptors()) {
            Class<?> binding = ad.getType().getBinding();
            if (!Serializable.class.isAssignableFrom(binding)) {
                return false;
            }
        }

        // check all sorting attributes are comparable
        for (SortBy sb : sortBy) {
            if (sb != SortBy.NATURAL_ORDER && sb != SortBy.REVERSE_ORDER) {
                AttributeDescriptor ad = schema.getDescriptor(sb.getPropertyName()
                        .getPropertyName());
                if (ad == null || !Comparable.class.isAssignableFrom(ad.getType().getBinding())) {
                    return false;
                }
            }
        }

        return true;
    }

    public SortedFeatureReader(SimpleFeatureReader reader, SortBy[] sortBy, int maxFeatures,
            int maxFiles) throws IOException {
        this.delegate = getDelegateReader(reader, sortBy, maxFeatures, maxFiles);
    }

    SimpleFeatureReader getDelegateReader(SimpleFeatureReader reader, SortBy[] sortBy,
            int maxFeatures, int maxFiles) throws IOException {
        Comparator<SimpleFeature> comparator = getComparator(sortBy);

        // easy case, no sorting needed
        if (comparator == null) {
            return reader;
        }

        // double check
        SimpleFeatureType schema = reader.getFeatureType();
        if (!canSort(schema, sortBy)) {
            throw new IllegalArgumentException(
                    "The specified reader cannot be sorted, either the "
                            + "sorting properties are not comparable or the attributes are not serializable");
        }

        int count = 0;
        List<File> files = new ArrayList<File>();
        List<SimpleFeature> features = new ArrayList<SimpleFeature>();
        boolean cleanFiles = true;
        try {
            // read and store into files as necessary
            while (reader.hasNext()) {
                SimpleFeature f = reader.next();
                features.add(f);
                count++;

                if (count > maxFeatures) {
                    Collections.sort(features, comparator);
                    File file = storeToFile(features);
                    files.add(file);
                    count = 0;
                    features.clear();
                }
            }

            if (files.isEmpty()) {
                // simple case, we managed to keep everything in memory, sort and return a
                // reader based on the collection contents
                Collections.sort(features, comparator);

                SimpleFeatureIterator fi = new ListFeatureCollection(schema, features).features();
                return new DelegateSimpleFeatureReader(schema, fi);
            } else {
                // we saved at least one file. For the sake of simplicity store the last
                // partial collection in files as well and then return a merge/sort reader
                if (count > 0) {
                    Collections.sort(features, comparator);
                    File file = storeToFile(features);
                    files.add(file);
                }

                // make sure we are not going to keep too many files open
                if (files.size() > maxFiles) {
                    reduceFiles(files, maxFiles, schema, comparator);
                }

                cleanFiles = false;
                return new MergeSortReader(schema, files, comparator);
            }

        } finally {
            if (cleanFiles) {
                for (File file : files) {
                    file.delete();
                }
            }

            reader.close();
        }
    }

    /**
     * Reduces the file list to the max number of files ensuring that we won't keep more than
     * maxFiles open at any time
     * 
     * @param files
     * @param maxFiles
     */
    void reduceFiles(List<File> files, int maxFiles, SimpleFeatureType schema,
            Comparator<SimpleFeature> comparator) throws IOException {
        while (files.size() > maxFiles) {
            // merge the first batch into a single file
            List<File> currFiles = new ArrayList<File>(files.subList(0, maxFiles));
            SimpleFeatureReader reader = new MergeSortReader(schema, currFiles, comparator);
            File file = storeToFile(reader);

            // remove those files from the list and add the merged file at the end
            files.removeAll(currFiles);
            files.add(file);
        }

    }

    /**
     * Writes the feature attributes to a binary file
     * 
     * @param features
     * @return
     * @throws IOException
     */
    File storeToFile(List<SimpleFeature> features) throws IOException {
        SimpleFeatureType schema = features.get(0).getFeatureType();
        SimpleFeatureIterator fi = new ListFeatureCollection(schema, features).features();
        return storeToFile(new DelegateSimpleFeatureReader(schema, fi));
    }

    /**
     * Writes teh feature attributes to a binary file
     * 
     * @param reader
     * @return
     * @throws IOException
     */
    File storeToFile(SimpleFeatureReader reader) throws IOException {
        File file = File.createTempFile("sorted", ".features");

        ObjectOutputStream os = null;
        try {
            os = new ObjectOutputStream(new FileOutputStream(file));
            while (reader.hasNext()) {
                SimpleFeature f = reader.next();
                os.writeObject(f.getID());
                for (Object att : f.getAttributes()) {
                    os.writeObject(att);
                }
            }
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    // fine, we tried
                }
            }
            reader.close();
        }

        return file;
    }

    /**
     * Builds a comparator out of the sortBy list
     * 
     * @param sortBy
     * @return
     */
    Comparator<SimpleFeature> getComparator(SortBy[] sortBy) {
        // handle the easy cases, no sorting or natural sorting
        if (sortBy == SortBy.UNSORTED || sortBy == null) {
            return null;
        }

        // build a list of comparators
        List<Comparator<SimpleFeature>> comparators = new ArrayList<Comparator<SimpleFeature>>();
        for (SortBy sb : sortBy) {
            if (sb == SortBy.NATURAL_ORDER) {
                comparators.add(new FidComparator(true));
            } else if (sb == SortBy.REVERSE_ORDER) {
                comparators.add(new FidComparator(false));
            } else {
                String name = sb.getPropertyName().getPropertyName();
                boolean ascending = sb.getSortOrder() == SortOrder.ASCENDING;
                comparators.add(new PropertyComparator(name, ascending));
            }
        }

        // return the final comparator
        if (comparators.size() == 1) {
            return comparators.get(0);
        } else {
            return new CompositeComparator(comparators);
        }

    }

    @Override
    public SimpleFeatureType getFeatureType() {
        return delegate.getFeatureType();
    }

    @Override
    public SimpleFeature next() throws IOException, IllegalArgumentException,
            NoSuchElementException {
        return delegate.next();
    }

    @Override
    public boolean hasNext() throws IOException {
        return delegate.hasNext();
    }

    @Override
    public void close() throws IOException {
        delegate.close();
    }

}
