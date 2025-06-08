/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.feature.visitor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.filter.sort.SortBy;
import org.geotools.data.DataTestCase;
import org.geotools.data.DataUtilities;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.collection.TreeSetFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.visitor.MaxVisitor.MaxResult;
import org.geotools.feature.visitor.MedianVisitor.MedianResult;
import org.geotools.feature.visitor.MinVisitor.MinResult;
import org.geotools.feature.visitor.UniqueVisitor.UniqueResult;
import org.geotools.filter.IllegalFilterException;
import org.junit.Test;
import org.locationtech.jts.geom.Envelope;

/** Purpose: these tests ensure the proper operation of feature visitation, with CalcResult merging too! */
public class VisitorCalculationTest extends DataTestCase {
    SimpleFeatureCollection empty;
    SimpleFeatureCollection fc;
    SimpleFeatureCollection invfc;
    SimpleFeatureType ft;
    SimpleFeatureCollection fc2;
    SimpleFeatureType ft2;
    SimpleFeatureCollection fc3;
    SimpleFeatureType ft3;
    SimpleFeatureCollection fc4;
    SimpleFeatureType ft4;
    SimpleFeatureCollection fc5;
    SimpleFeatureType ft5;
    SimpleFeatureCollection fc6;
    SimpleFeatureType ft6;

    @Override
    public void init() throws Exception {
        super.init();
        empty = new DefaultFeatureCollection();
        fc = DataUtilities.collection(roadFeatures);
        invfc = new TreeSetFeatureCollection(fc).sort(SortBy.REVERSE_ORDER);
        fc2 = DataUtilities.collection(riverFeatures);
        ft = roadType;
        ft2 = riverType;

        // create our own fc3
        SimpleFeatureType boringType = DataUtilities.createType("fc3.boring", "id:0");
        SimpleFeature[] boringFeatures = new SimpleFeature[100];
        for (int i = 1; i <= 100; i++) {
            boringFeatures[i - 1] = SimpleFeatureBuilder.build(boringType, new Object[] {Integer.valueOf(i)}, null);
        }

        ft3 = boringType;
        fc3 = DataUtilities.collection(boringFeatures);

        ft4 = lakeType;
        fc4 = DataUtilities.collection(lakeFeatures);

        ft5 = buildingType;
        fc5 = DataUtilities.collection(buildingFeatures);

        ft6 = invalidGeomType;
        fc6 = DataUtilities.collection(invalidGeomFeatures);
    }

    // test only the visitor functions themselves, and try the merge operation
    @Test
    public void testMin() throws IllegalFilterException, IOException {
        // index 0 is the id field, so the data isn't terribly exciting (1,2,3).
        MinVisitor minVisitor = new MinVisitor(0, ft);
        fc.accepts(minVisitor, null);
        MinVisitor minVisitor2 = new MinVisitor(0, ft2);
        fc2.accepts(minVisitor2, null);
        // 1 is min
        Object result = minVisitor.getResult().getValue();
        int value = ((Integer) result).intValue();
        assertEquals(1, value);
        int value2 = minVisitor.getResult().toInt();
        assertEquals(1, value2);
        // min of 1 and 1 is 1
        CalcResult minResult1 = minVisitor.getResult();
        CalcResult minResult2 = minVisitor2.getResult();
        CalcResult minResult3 = minResult1.merge(minResult2);
        assertEquals(1, minResult3.toInt());
        // test for destruction during merge
        CalcResult minResult4 = new MinResult(Integer.valueOf(10));
        CalcResult minResult5 = minResult4.merge(minResult1);
        assertEquals(1, minResult5.toInt());
        assertEquals(10, minResult4.toInt());
        // test negative result
        CalcResult minResult6 = new MinResult(Integer.valueOf(-5));
        CalcResult minResult7 = minResult1.merge(minResult6);
        assertEquals(-5, minResult7.toInt());
        assertEquals(-5, minResult6.toInt());
        // test a mock optimization
        minVisitor.setValue(Integer.valueOf(-50));
        minResult1 = minVisitor.getResult();
        minResult7 = minResult7.merge(minResult1);
        assertEquals(-50, minResult7.toInt());
        // test varying data types
        minVisitor.setValue(Double.valueOf(-100.0));
        minResult1 = minVisitor.getResult();
        minResult7 = minResult7.merge(minResult1);
        assertEquals(-100.0, minResult7.toDouble(), 0);
        assertEquals(-100, minResult7.toInt());
        // test empty collection
        minVisitor.reset();
        empty.accepts(minVisitor, null);
        assertEquals(CalcResult.NULL_RESULT, minVisitor.getResult());
        // test merge
        assertSame(minResult2, minVisitor.getResult().merge(minResult2));
        assertSame(minResult2, minResult2.merge(minVisitor.getResult()));
    }

    @Test
    public void testMax() throws IllegalFilterException, IOException {
        // index 0 is the id field, so the data isn't terribly exciting
        MaxVisitor maxVisitor = new MaxVisitor(0, ft);
        fc.accepts(maxVisitor, null); // 1,2,3
        MaxVisitor maxVisitor2 = new MaxVisitor(3, ft2);
        fc2.accepts(maxVisitor2, null); // 3,4.5
        // 3 is max
        int value1 = maxVisitor.getResult().toInt();
        assertEquals(3, value1);
        // 4.5 is max
        double value2 = maxVisitor2.getResult().toDouble();
        assertEquals(4.5, value2, 0);
        // max of 3 and 4.5 is 4.5
        CalcResult maxResult1 = maxVisitor.getResult();
        CalcResult maxResult2 = maxVisitor2.getResult();
        CalcResult maxResult3 = maxResult1.merge(maxResult2);
        assertEquals(4.5, maxResult3.toDouble(), 0);
        // test for destruction during merge
        CalcResult maxResult4 = new MaxResult(Double.valueOf(2));
        CalcResult maxResult5 = maxResult4.merge(maxResult1);
        assertEquals(3, maxResult5.toDouble(), 0);
        assertEquals(2, maxResult4.toDouble(), 0);
        // test negative result
        CalcResult maxResult6 = new MaxResult(Integer.valueOf(-5));
        CalcResult maxResult7 = maxResult1.merge(maxResult6);
        assertEquals(3, maxResult7.toDouble(), 0);
        assertEquals(-5, maxResult6.toDouble(), 0);
        // test a mock optimization
        maxVisitor.setValue(Double.valueOf(544));
        maxResult1 = maxVisitor.getResult();
        maxResult7 = maxResult7.merge(maxResult1);
        assertEquals(544, maxResult7.toDouble(), 0);
        // test varying data types
        maxVisitor.setValue(Long.valueOf(6453));
        maxResult1 = maxVisitor.getResult();
        maxResult7 = maxResult7.merge(maxResult1);
        assertEquals(6453, maxResult7.toDouble(), 0);
        assertEquals(6453, maxResult7.toInt());
        // test empty collection
        maxVisitor.reset();
        empty.accepts(maxVisitor, null);
        assertEquals(CalcResult.NULL_RESULT, maxVisitor.getResult());
        // test merge
        assertSame(maxResult2, maxVisitor.getResult().merge(maxResult2));
        assertSame(maxResult2, maxResult2.merge(maxVisitor.getResult()));
    }

    @Test
    public void testMedian() throws IllegalFilterException, IOException {
        MedianVisitor medianVisitor1 = new MedianVisitor(0, ft);
        fc.accepts(medianVisitor1, null); // 1,2,3
        MedianVisitor medianVisitor2 = new MedianVisitor(0, ft2);
        fc2.accepts(medianVisitor2, null); // 3,4.5
        // 1,2,3 --> 2, 1,2 --> 1.5
        CalcResult medianResult1 = medianVisitor1.getResult();
        CalcResult medianResult2 = medianVisitor2.getResult();
        assertEquals(2, medianResult1.toInt());
        assertEquals(1.5, medianResult2.toDouble(), 0);
        // 1,1,2,2,3 --> 2
        CalcResult medianResult3 = medianResult1.merge(medianResult2);
        assertEquals(2, medianResult3.toDouble(), 0);
        // test for destruction during merge
        List<Comparable> vals = new ArrayList<>();
        vals.add(Double.valueOf(2.5));
        vals.add(Double.valueOf(3.5));
        vals.add(Double.valueOf(4.5));
        CalcResult medianResult4 = new MedianResult(vals);
        CalcResult medianResult5 = medianResult4.merge(medianResult1);
        assertEquals(2.75, medianResult5.toDouble(), 0);
        assertEquals(3.5, medianResult4.toDouble(), 0);
        // test a mock optimization
        medianVisitor1.setValue(Double.valueOf(544));
        medianResult1 = medianVisitor1.getResult();
        try {
            medianResult3 = medianResult5.merge(medianResult1);
            fail(); // merge should fail
        } catch (Exception e) {
            assertEquals("Optimized median results cannot be merged.", e.getMessage());
        }
        // test empty collection
        medianVisitor1.reset();
        empty.accepts(medianVisitor1, null);
        assertEquals(CalcResult.NULL_RESULT, medianVisitor1.getResult());
        // test merge
        assertSame(medianResult2, medianVisitor1.getResult().merge(medianResult2));
        assertSame(medianResult2, medianResult2.merge(medianVisitor1.getResult()));
    }

    @Test
    public void testSum() throws IllegalFilterException, IOException {
        SumVisitor sumVisitor = new SumVisitor(0, ft);
        fc.accepts(sumVisitor, null); // 1,2,3
        SumVisitor sumVisitor2 = new SumVisitor(3, ft2);
        fc2.accepts(sumVisitor2, null); // 3,4.5
        // 6 is sum
        int value1 = sumVisitor.getResult().toInt();
        assertEquals(6, value1);
        // 7.5 is sum
        double value2 = sumVisitor2.getResult().toDouble();
        assertEquals(7.5, value2, 0);
        // sum of 6 and 7.5 is 13.5
        CalcResult sumResult1 = sumVisitor.getResult();
        CalcResult sumResult2 = sumVisitor2.getResult();
        CalcResult sumResult3 = sumResult1.merge(sumResult2);
        assertEquals(13.5, sumResult3.toDouble(), 0);
        // test a mock optimization
        sumVisitor2.setValue(Integer.valueOf(-42));
        CalcResult sumResult4 = sumVisitor2.getResult();
        CalcResult sumResult5 = sumResult3.merge(sumResult4);
        assertEquals(-28.5, sumResult5.toDouble(), 0);
        // test for destruction during merge
        assertEquals(13.5, sumResult3.toDouble(), 0);
        assertEquals(-42.0, sumResult4.toDouble(), 0);
        // test empty collection
        sumVisitor.reset();
        empty.accepts(sumVisitor, null);
        assertEquals(CalcResult.NULL_RESULT, sumVisitor.getResult());
        // test merge
        assertSame(sumResult2, sumVisitor.getResult().merge(sumResult2));
        assertSame(sumResult2, sumResult2.merge(sumVisitor.getResult()));
    }

    @Test
    public void testArea() throws IllegalFilterException, IOException {
        SumAreaVisitor areaVisitor = new SumAreaVisitor(1, ft4);
        fc4.accepts(areaVisitor, null);

        SumAreaVisitor areaVisitor2 = new SumAreaVisitor(1, ft5);
        fc5.accepts(areaVisitor2, null);

        double value1 = areaVisitor.getResult().toDouble();
        assertEquals(10.0, value1, 0d);

        double value2 = areaVisitor2.getResult().toDouble();
        assertEquals(12.0, value2, 0d);

        CalcResult areaResult1 = areaVisitor.getResult();
        CalcResult areaResult2 = areaVisitor2.getResult();
        CalcResult areaResult3 = areaResult1.merge(areaResult2);
        assertEquals(22.0, areaResult3.toDouble(), 0);
    }

    @Test
    public void testAreaInvalidPolygon() throws IllegalFilterException, IOException {
        SumAreaVisitor areaVisitor = new SumAreaVisitor(1, ft6);
        fc6.accepts(areaVisitor, null);
        double value1 = areaVisitor.getResult().toDouble();
        assertEquals(0.0, value1, 0d);
    }

    @Test
    public void testCount() throws IllegalFilterException, IOException {
        CountVisitor countVisitor = new CountVisitor();
        fc.accepts(countVisitor, null);
        CountVisitor countVisitor2 = new CountVisitor();
        fc2.accepts(countVisitor2, null);
        // 3 features
        int value1 = countVisitor.getResult().toInt();
        assertEquals(3, value1);
        // 2 features
        int value2 = countVisitor2.getResult().toInt();
        assertEquals(2, value2);
        // merge = 5 features
        CalcResult countResult1 = countVisitor.getResult();
        CalcResult countResult2 = countVisitor2.getResult();
        CalcResult countResult3 = countResult1.merge(countResult2);
        assertEquals(5, countResult3.toInt());
        // test a mock optimization
        countVisitor.setValue(20);
        CalcResult countResult4 = countVisitor.getResult();
        assertEquals(20, countResult4.toInt());
        // test for destruction during merge
        CalcResult countResult5 = countResult4.merge(countResult3);
        assertEquals(5, countResult3.toInt());
        assertEquals(20, countResult4.toInt());
        assertEquals(25, countResult5.toInt());
        // test empty collection
        countVisitor.reset();
        empty.accepts(countVisitor, null);
        assertEquals(CalcResult.NULL_RESULT, countVisitor.getResult());
        // test merge
        assertSame(countResult2, countVisitor.getResult().merge(countResult2));
        assertSame(countResult2, countResult2.merge(countVisitor.getResult()));
    }

    @Test
    public void testAverage() throws IllegalFilterException, IOException {
        AverageVisitor averageVisitor = new AverageVisitor(0, ft);
        fc.accepts(averageVisitor, null); // 1,2,3
        AverageVisitor averageVisitor2 = new AverageVisitor(3, ft2);
        fc2.accepts(averageVisitor2, null); // 3,4.5
        // 2 is average
        int value1 = averageVisitor.getResult().toInt();
        assertEquals(2, value1);
        // 3.75 is average
        double value2 = averageVisitor2.getResult().toDouble();
        assertEquals(3.75, value2, 0);
        // average of 1,2,3,3,4.5 is 2.7
        CalcResult averageResult1 = averageVisitor.getResult();
        CalcResult averageResult2 = averageVisitor2.getResult();
        CalcResult averageResult3 = averageResult1.merge(averageResult2);
        assertEquals(2.7, averageResult3.toDouble(), 0);
        // test for destruction during merge
        assertEquals(3.75, averageResult2.toDouble(), 0);
        // test mock optimizations
        averageVisitor2.setValue(5, Integer.valueOf(100)); // mergeable optimization
        averageResult2 = averageVisitor2.getResult();
        assertEquals(20, averageResult2.toInt());
        averageResult3 = averageResult1.merge(averageResult2);
        assertEquals(13.25, averageResult3.toDouble(), 0);
        averageVisitor2.setValue(Double.valueOf(15.4)); // un-mergeable optimization
        averageResult2 = averageVisitor2.getResult();
        assertEquals(15.4, averageResult2.toDouble(), 0);
        try {
            averageResult3 = averageResult1.merge(averageResult2);
            fail(); // merge should throw an exception
        } catch (Exception e) {
            assertEquals("Optimized average results cannot be merged.", e.getMessage());
        }
        // throw a monkey in the wrench (combine number classes)
        averageVisitor.setValue(5, Integer.valueOf(10));
        averageResult1 = averageVisitor.getResult();
        averageVisitor2.setValue(5, Double.valueOf(33.3));
        averageResult2 = averageVisitor2.getResult();
        averageResult3 = averageResult1.merge(averageResult2); // int + double --> double?
        assertEquals(4.33, averageResult3.toDouble(), 0);
        // test empty collection
        averageVisitor.reset();
        empty.accepts(averageVisitor, null);
        assertEquals(CalcResult.NULL_RESULT, averageVisitor.getResult());
        // test merge
        assertSame(averageResult2, averageVisitor.getResult().merge(averageResult2));
        assertSame(averageResult2, averageResult2.merge(averageVisitor.getResult()));
    }

    @Test
    public void testUniquePreserveOrder() throws IOException {
        UniqueVisitor uniqueVisitor = new UniqueVisitor(0, ft);
        uniqueVisitor.setPreserveOrder(true);
        fc.accepts(uniqueVisitor, null);
        Set value1 = uniqueVisitor.getResult().toSet();
        assertEquals(1, value1.iterator().next());

        uniqueVisitor.reset();
        invfc.accepts(uniqueVisitor, null);
        value1 = uniqueVisitor.getResult().toSet();
        assertEquals(3, value1.iterator().next());
    }

    @Test
    public void testUniquePagination() throws IOException {
        UniqueVisitor uniqueVisitor = new UniqueVisitor(0, ft);
        uniqueVisitor.setPreserveOrder(true);
        uniqueVisitor.setStartIndex(0);
        uniqueVisitor.setMaxFeatures(1);
        fc.accepts(uniqueVisitor, null);
        Set value1 = uniqueVisitor.getResult().toSet();
        assertEquals(1, value1.size());
        assertEquals(1, value1.iterator().next());

        uniqueVisitor.reset();
        uniqueVisitor.setStartIndex(1);
        uniqueVisitor.setMaxFeatures(2);
        fc.accepts(uniqueVisitor, null);
        value1 = uniqueVisitor.getResult().toSet();
        assertEquals(2, value1.size());
        assertEquals(2, value1.iterator().next());

        uniqueVisitor.reset();
        uniqueVisitor.setStartIndex(2);
        uniqueVisitor.setMaxFeatures(2);
        fc.accepts(uniqueVisitor, null);
        value1 = uniqueVisitor.getResult().toSet();
        assertEquals(1, value1.size());
        assertEquals(3, value1.iterator().next());

        uniqueVisitor.reset();
        uniqueVisitor.setStartIndex(3);
        uniqueVisitor.setMaxFeatures(2);
        fc.accepts(uniqueVisitor, null);
        value1 = uniqueVisitor.getResult().toSet();
        // the UniqueVisitor returns null if the list of values is null
        assertNull(value1);
    }

    @Test
    public void testUnique() throws IllegalFilterException, IOException {
        UniqueVisitor uniqueVisitor = new UniqueVisitor(0, ft);
        fc.accepts(uniqueVisitor, null);
        UniqueVisitor uniqueVisitor2 = new UniqueVisitor(3, ft2);
        fc2.accepts(uniqueVisitor2, null);
        // 1, 2, 3
        Set value1 = uniqueVisitor.getResult().toSet();
        assertEquals(3, value1.size()); // 3 items in the set
        // 3.0, 4.5
        Object[] value2 = uniqueVisitor2.getResult().toArray();
        assertEquals(2, value2.length); // 2 items in the set
        // test a merge
        CalcResult uniqueResult1 = uniqueVisitor.getResult();
        CalcResult uniqueResult2 = uniqueVisitor2.getResult();
        CalcResult uniqueResult3 = uniqueResult1.merge(uniqueResult2);
        assertEquals(5, uniqueResult3.toSet().size()); // 3 and 3.0 are different, so there are actually 5
        // ensure merge was not destructive
        assertEquals(3, uniqueResult1.toSet().size());
        // test a merge with duplicate elements
        Set<Object> anotherSet = new HashSet<>();
        anotherSet.add(Integer.valueOf(2));
        anotherSet.add(Integer.valueOf(4));
        CalcResult uniqueResult4 = new UniqueResult(anotherSet);
        CalcResult uniqueResult5 = uniqueResult1.merge(uniqueResult4); // 1,2,3 + 2,4
        assertEquals(4, uniqueResult5.toSet().size());
        // mock optimization
        uniqueVisitor.setValue(anotherSet);
        uniqueResult1 = uniqueVisitor.getResult();
        assertEquals(anotherSet, uniqueResult1.toSet());
        // int + double --> ?
        uniqueResult3 = uniqueResult2.merge(uniqueResult1);
        @SuppressWarnings("unchecked")
        Set<Object> set = uniqueResult3.toSet();
        assertEquals(4, set.size());
        assertTrue(set.contains(3.0));
        assertTrue(set.contains(4.5));
        assertTrue(set.contains(2));
        assertTrue(set.contains(4));
        assertFalse(set.contains(6));
        // test empty collection
        uniqueVisitor.reset();
        empty.accepts(uniqueVisitor, null);
        assertEquals(CalcResult.NULL_RESULT, uniqueVisitor.getResult());
        // test merge
        assertSame(uniqueResult2, uniqueVisitor.getResult().merge(uniqueResult2));
        assertSame(uniqueResult2, uniqueResult2.merge(uniqueVisitor.getResult()));
    }

    @Test
    public void testBounds() throws IOException {
        BoundsVisitor boundsVisitor1 = new BoundsVisitor();
        fc.accepts(boundsVisitor1, null);
        BoundsVisitor boundsVisitor2 = new BoundsVisitor();
        fc2.accepts(boundsVisitor2, null);
        Envelope env1 = new Envelope(1, 5, 0, 4);
        CalcResult boundsResult1 = boundsVisitor1.getResult();
        assertEquals(env1, boundsResult1.toEnvelope());
        Envelope env2 = new Envelope(4, 13, 3, 10);
        CalcResult boundsResult2 = boundsVisitor2.getResult();
        assertEquals(env2, boundsResult2.toEnvelope());
        CalcResult boundsResult3 = boundsResult2.merge(boundsResult1);
        Envelope env3 = new Envelope(1, 13, 0, 10);
        assertEquals(env3, boundsResult3.toEnvelope());
        // test empty collection
        boundsVisitor1.reset(null);
        empty.accepts(boundsVisitor1, null);
        assertEquals(CalcResult.NULL_RESULT, boundsVisitor1.getResult());
        // test merge
        assertSame(boundsResult2, boundsVisitor1.getResult().merge(boundsResult2));
        assertSame(boundsResult2, boundsResult2.merge(boundsVisitor1.getResult()));
    }

    @Test
    public void testQuantileList() throws Exception {
        FilterFactory factory = CommonFactoryFinder.getFilterFactory(null);
        Expression expr = factory.property(ft.getDescriptor(0).getLocalName());
        QuantileListVisitor visitor = new QuantileListVisitor(expr, 2);
        fc.accepts(visitor, null);
        CalcResult result = visitor.getResult();
        List[] qResult = (List[]) result.getValue();
        assertEquals(2, qResult.length);
        assertEquals(2, qResult[0].size());
        assertEquals(1, qResult[1].size());
        // test empty collection
        QuantileListVisitor emptyVisitor = new QuantileListVisitor(expr, 2);
        empty.accepts(emptyVisitor, null);
        assertEquals(CalcResult.NULL_RESULT, emptyVisitor.getResult());
        // test merge
        assertSame(result, emptyVisitor.getResult().merge(result));
        assertSame(result, result.merge(emptyVisitor.getResult()));
    }

    @Test
    public void testStandardDeviation() throws Exception {
        FilterFactory factory = CommonFactoryFinder.getFilterFactory(null);
        Expression expr = factory.property(ft3.getDescriptor(0).getLocalName());
        // first do it the old fashioned way to ensure backwards compatibility
        AverageVisitor visit1 = new AverageVisitor(expr);
        fc3.accepts(visit1, null);
        CalcResult result = visit1.getResult();

        StandardDeviationVisitor visit2 = new StandardDeviationVisitor(expr);
        fc3.accepts(visit2, null);
        assertEquals(28.86, visit2.getResult().toDouble(), 0.01);
        // then do it single pass
        StandardDeviationVisitor visit3 = new StandardDeviationVisitor(expr);
        fc3.accepts(visit3, null);
        assertEquals(28.86, visit3.getResult().toDouble(), 0.01);
        // test empty collection
        StandardDeviationVisitor emptyVisitor = new StandardDeviationVisitor(expr);
        empty.accepts(emptyVisitor, null);
        assertEquals(CalcResult.NULL_RESULT, emptyVisitor.getResult());
        // test merge
        assertSame(result, emptyVisitor.getResult().merge(result));
        assertSame(result, result.merge(emptyVisitor.getResult()));
    }

    // try merging a count and sum to get an average, both count+sum and sum+count
    @Test
    public void testCountSumMerge() throws IllegalFilterException, IOException {
        CountVisitor countVisitor = new CountVisitor();
        fc2.accepts(countVisitor, null); // count = 2
        SumVisitor sumVisitor = new SumVisitor(3, ft2);
        fc2.accepts(sumVisitor, null); // sum = 7.5
        CalcResult countResult = countVisitor.getResult();
        CalcResult sumResult = sumVisitor.getResult();
        CalcResult averageResult1 = countResult.merge(sumResult);
        CalcResult averageResult2 = sumResult.merge(countResult);
        // both average results were correct?
        assertEquals(3.75, averageResult1.toDouble(), 0);
        assertEquals(3.75, averageResult2.toDouble(), 0);
        // neither sum nor count was destroyed?
        assertEquals(2, countResult.toInt());
        assertEquals(7.5, sumResult.toDouble(), 0);
    }

    // try merging 2 incompatible CalcResults and check for the exception
    @Test
    public void testBadMerge() throws IllegalFilterException, IOException {
        // count + max = boom!
        CountVisitor countVisitor = new CountVisitor();
        countVisitor.setValue(8);
        CalcResult countResult = countVisitor.getResult();
        MaxVisitor maxVisitor = new MaxVisitor((Expression) null);
        maxVisitor.setValue(Double.valueOf(99));
        CalcResult maxResult = maxVisitor.getResult();
        try {
            maxResult.merge(countResult);
            fail(); // merge should throw an exception
        } catch (Exception e) {
            assertEquals("Parameter is not a compatible type", e.getMessage());
        }
    }

    @Test
    public void testNearest() throws Exception {
        SimpleFeatureType type = DataUtilities.createType(
                "nearestTest", "name:String,size:int,flow:double,event:java.util.Date,data:java.io.File");
        ListFeatureCollection fc = new ListFeatureCollection(type);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        fc.add(SimpleFeatureBuilder.build(
                type, new Object[] {"abc", 10, 10.5, df.parse("2014-12-10"), new File("/tmp/test.txt")}, null));
        fc.add(SimpleFeatureBuilder.build(
                type, new Object[] {"ade", 5, 3.5, df.parse("2012-11-10"), new File("/tmp/abc.txt")}, null));
        fc.add(SimpleFeatureBuilder.build(
                type, new Object[] {"zaa", 2, 50.4, df.parse("2010-11-10"), new File("/tmp/zaa.txt")}, null));

        // test on integer
        testNearest(fc, "size", 5, 5); // exact match
        testNearest(fc, "size", 1, 2); // below min
        testNearest(fc, "size", 3, 2); // mid
        testNearest(fc, "size", 15, 10); // above max

        // test on double
        testNearest(fc, "flow", 3.5, 3.5); // exact match
        testNearest(fc, "flow", 1d, 3.5); // below min
        testNearest(fc, "flow", 10d, 10.5); // mid
        testNearest(fc, "flow", 100d, 50.4); // above max

        // test on date
        testNearest(fc, "event", df.parse("2014-12-10"), df.parse("2014-12-10")); // exact match
        testNearest(fc, "event", df.parse("2009-11-10"), df.parse("2010-11-10")); // below min
        testNearest(fc, "event", df.parse("2010-11-11"), df.parse("2010-11-10")); // mid
        testNearest(fc, "event", df.parse("2015-12-10"), df.parse("2014-12-10")); // above max

        // test on string
        testNearest(fc, "name", "ade", "ade"); // exact match
        testNearest(fc, "name", "aaa", "abc"); // below min
        testNearest(fc, "name", "mfc", "ade", "zaa"); // mid, both values could match
        testNearest(fc, "name", "zzz", "zaa"); // above max

        // test on random comparable (a file)
        testNearest(fc, "data", new File("/tmp/test.txt"), new File("/tmp/test.txt")); // exact match
        testNearest(fc, "data", new File("/tmp/aaa.txt"), new File("/tmp/abc.txt")); // below min
        testNearest(
                fc,
                "data",
                new File("/tmp/mfc.txt"),
                new File("/tmp/abc.txt"),
                new File("/tmp/test.txt")); // mid, both values could match
        testNearest(fc, "data", new File("/tmp/zzz.txt"), new File("/tmp/zaa.txt")); // above max
    }

    private void testNearest(SimpleFeatureCollection fc, String attributeName, Object target, Object... validResults)
            throws IOException {
        PropertyName expr = ff.property(attributeName);
        NearestVisitor visitor = new NearestVisitor(expr, target);
        fc.accepts(visitor, null);
        Object nearestMatch = visitor.getNearestMatch();
        if (validResults.length == 0) {
            assertNull(nearestMatch);
        } else {
            boolean found = false;
            for (Object object : validResults) {
                found |= object != null ? object.equals(nearestMatch) : nearestMatch == null;
            }

            assertTrue(
                    "Could match nearest " + nearestMatch + " among valid values " + Arrays.asList(validResults),
                    found);
        }
    }
}
