package org.geotools.ysld.validate;

import org.geotools.ysld.UomMapper;
import org.geotools.ysld.Ysld;
import org.geotools.ysld.parse.ZoomContext;
import org.geotools.ysld.parse.ZoomContextFinder;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.error.MarkedYAMLException;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class YsldValidateTest {

    @Test
    public void testMalformed() throws Exception {
        String ysld =
            "foo:\n" +
            "  foo: bar\n" +
            "   foo: bar\n";
        List<MarkedYAMLException> errors = validate(ysld);
        assertEquals(1, errors.size());

        MarkedYAMLException e = errors.get(0);
        assertEquals(2, e.getProblemMark().getLine());
    }

    @Test
    public void testColor() throws Exception {
        String ysld =
            "fill-color: 21xyz";

        List<MarkedYAMLException> errors = validate(ysld);
        assertEquals(1, errors.size());

        MarkedYAMLException e = errors.get(0);
        assertEquals(0, e.getProblemMark().getLine());
        assertEquals(12, e.getProblemMark().getColumn());
    }

    @Test
    public void testFilter() throws Exception {
        String ysld = "filter: foo";

        List<MarkedYAMLException> errors = validate(ysld);
        assertEquals(1, errors.size());

        MarkedYAMLException e = errors.get(0);
        // Note this is the start of the expression, a better version would find the error within the CQL
        assertThat(e.getProblemMark(), problemAt(1, 9));
    }
    
    @Test
    public void testFilterOK() throws Exception {
        String ysld = "filter: scalerank < 4";
        
        List<MarkedYAMLException> errors = validate(ysld);
        assertEquals(0, errors.size());
    }
    
    @Test
    public void testFilterOWithBracesK() throws Exception {
        String ysld = "filter: ${scalerank < 4}";
        
        List<MarkedYAMLException> errors = validate(ysld);
        assertEquals(0, errors.size());
    }
    
    Matcher<Mark> problemAt(int line, int column) {
        return Matchers.describedAs("Problem at Line %0 Column %1", allOf(
                  Matchers.<Mark>hasProperty("line", is(line-1)),
                  Matchers.<Mark>hasProperty("column", is(column-1))
                ), line, column);
    }
    
    @SuppressWarnings("unchecked")
    Matcher<Mark> problemOn(int line) {
        return Matchers.describedAs("Problem somewhere on Line %0", allOf(
                  Matchers.<Mark>hasProperty("line", is(line-1))
                ), line);
    }
    
    final static String[] EXPRESSION_KEYS = {
            "offset",
            "shape",
            "gamma",
            "stroke-width",
            "stroke-opacity",
            "stroke-linejoin",
            "stroke-linecap",
            "stroke-dashoffset",
            "geometry",
            "label",
            "font-family",
            "font-size",
            "font-style",
            "font-weight",
            "size",
            "rotation",
            "gap",
            "initial-gap",
            "radius"};
    
    @Test
    public void testExpression() throws Exception {
        StringBuilder builder =  new StringBuilder();
        builder.append(
                "rules:\n"+
                "- symbolizers:\n"+
                "  - line:\n");
        for(String key: EXPRESSION_KEYS) {
            builder.append("      ").append(key).append(": ").append("${round([len] 1000)}").append("\n"); // Invalid
            builder.append("      ").append(key).append(": ").append("${round([len] / 1000)}").append("\n"); // Valid
        }
        List<MarkedYAMLException> errors = validate(builder.toString());
        assertThat(errors.size(), is(EXPRESSION_KEYS.length));
        
        final int firstErrorLine = 4;
        
        for(int i=0; i<EXPRESSION_KEYS.length; i++) {
            assertThat(errors.get(i).getProblemMark(), problemOn(i*2+firstErrorLine));
        }
    }
    
    @Test
    public void testWellKnownZoomContext() throws Exception {
        StringBuilder builder =  new StringBuilder();
        builder.append(
                "grid:\n"+
                "  name: WGS84\n");
        
        List<MarkedYAMLException> errors = validate(builder.toString());
        assertThat(errors.size(), is(0));
    }
    
    @Test
    public void testNotWellKnownZoomContext() throws Exception {
        StringBuilder builder =  new StringBuilder();
        builder.append(
                "grid:\n"+
                "  name: SIGMA:957\n");
        
        List<MarkedYAMLException> errors = validate(builder.toString());
        assertThat(errors.size(), is(1));
        
        assertThat(errors.get(0).getProblemMark(), problemOn(2));
    }
    
    @Test
    public void testExtendedZoomContext() throws Exception {
        StringBuilder builder =  new StringBuilder();
        builder.append(
                "grid:\n"+
                "  name: SIGMA:957\n");
        
        ZoomContextFinder finder = createMock("finder", ZoomContextFinder.class);
        ZoomContext zctxt = createMock("zctxt", ZoomContext.class);
        
        expect(finder.get("SIGMA:957")).andStubReturn(zctxt);
        
        replay(finder, zctxt);
        
        List<MarkedYAMLException> errors = validate(builder.toString(),Collections.singletonList(finder));
        assertThat(errors.size(), is(0));
        
        verify(finder, zctxt);
    }
    
    @Test
    public void testZoomContextEPSG4326IsBad() throws Exception {
        StringBuilder builder =  new StringBuilder();
        builder.append(
                "grid:\n"+
                "  name: EPSG:4326\n");
        
        ZoomContextFinder finder = createMock("finder", ZoomContextFinder.class);
        ZoomContext zctxt = createMock("zctxt", ZoomContext.class);
        
        expect(finder.get("EPSG:4326")).andStubReturn(zctxt);
        
        replay(finder, zctxt);
        
        List<MarkedYAMLException> errors = validate(builder.toString(),Collections.singletonList(finder));
        assertThat(errors.size(), is(1));
        
        assertThat(errors.get(0).getProblemMark(), problemOn(2));
        
        verify(finder, zctxt);
    }

    List<MarkedYAMLException> dump(List<MarkedYAMLException> errors) {
        for (MarkedYAMLException e : errors) {
            System.out.println(e.toString());
        }
        return errors;
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testZoomWithDefaultGridValidateOrder() throws Exception {
        StringBuilder builder =  new StringBuilder();
        builder.append(
                "rules:\n"+          // 1
                "- zoom: [0,1]\n"+ // 2 OK
                "- zoom: [1,2]\n"+ // 3 OK
                "- zoom: [-1,2]\n"+ // 4 OK
                "- zoom: [1, 0]\n"+ // 5 Bad
                "- zoom: [100, 10]\n"+ // 6 Bad
                "- zoom: [-2, -10]\n"+ // 7 Bad
                "- zoom: [2, -2]\n"); // 8 Bad
        
        List<MarkedYAMLException> errors = validate(builder.toString());
        assertThat(errors, contains(
                hasProperty("problemMark", problemOn(5)),
                hasProperty("problemMark", problemOn(6)),
                hasProperty("problemMark", problemOn(7)),
                hasProperty("problemMark", problemOn(8))
                ));
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testZoomWithDefaultGridTupleSize() throws Exception {
        StringBuilder builder =  new StringBuilder();
        builder.append(
                "rules:\n"+          // 1
                "- zoom: [0,0]\n"+ // 2 OK
                "- zoom: []\n"+ // 3 Bad
                "- zoom: [0]\n"+ // 4 Bad
                "- zoom: [0,0,0]\n"); // 5 Bad
        
        List<MarkedYAMLException> errors = validate(builder.toString());
        assertThat(errors, contains(
                hasProperty("problemMark", problemOn(3)),
                hasProperty("problemMark", problemOn(4)),
                hasProperty("problemMark", problemOn(5))
                ));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testZoomWithDefaultGridValidateRange() throws Exception {
        StringBuilder builder =  new StringBuilder();
        builder.append(
                "rules:\n"+          // 1
                "- zoom: [0,max]\n"+ // 2 OK
                "- zoom: [min,0]\n"+ // 3 OK
                "- zoom: [19,max]\n"+ // 4 OK
                "- zoom: [min,19]\n"+ // 5 OK
                "- zoom: [-1, max]\n"+ // 6 OK, RatioZoomContext allows negative
                "- zoom: [min, -1]\n"+ // 7 OK, RatioZoomContext allows negative
                "- zoom: [min, foo]\n"+ // 8 Bad
                "- zoom: [foo, max]\n"+ // 9 Bad
                "- zoom: [0.5, max]\n"+ // 10 Bad
                "- zoom: [min, 0.5]\n"); // 11 Bad
        
        List<MarkedYAMLException> errors = validate(builder.toString());
        assertThat(errors, contains(
                hasProperty("problemMark", problemOn(8)),
                hasProperty("problemMark", problemOn(9)),
                hasProperty("problemMark", problemOn(10)),
                hasProperty("problemMark", problemOn(11))
                ));
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testZoomWithExtendedGridValidateRange() throws Exception {
        StringBuilder builder =  new StringBuilder();
        builder.append(
                "grid:\n"+           // 1
                "  name: SIGMA:957\n"+// 2
                "rules:\n"+          // 3
                "- zoom: [0,max]\n"+ // 4 OK
                "- zoom: [min,0]\n"+ // 5 OK
                "- zoom: [2,max]\n"+ // 6 OK
                "- zoom: [min,2]\n"+ // 7 OK
                "- zoom: [5,max]\n"+ // 8 OK
                "- zoom: [min,5]\n"+ // 9 OK
                "- zoom: [6, max]\n"+ // 10 Bad, Out of Range
                "- zoom: [min, 6]\n"+ // 11 Bad, Out of Range
                "- zoom: [-1, max]\n"+ // 12 Bad, Out of Range
                "- zoom: [min, -1]\n"+ // 13 Bad, Out of Range
                "- zoom: [min, foo]\n"+ // 14 Bad
                "- zoom: [foo, max]\n"+ // 15 Bad
                "- zoom: [0.5, max]\n"+ // 16 Bad
                "- zoom: [min, 0.5]\n"); // 17 Bad
        
        ZoomContextFinder finder = createMock("finder", ZoomContextFinder.class);
        ZoomContext zctxt = createMock("zctxt", ZoomContext.class);
        
        expect(finder.get("SIGMA:957")).andStubReturn(zctxt);
        
        expect(zctxt.isInRange(0)).andStubReturn(true);
        expect(zctxt.isInRange(2)).andStubReturn(true);
        expect(zctxt.isInRange(5)).andStubReturn(true);
        expect(zctxt.isInRange(-1)).andStubReturn(false);
        expect(zctxt.isInRange(6)).andStubReturn(false);
       
        replay(finder, zctxt);
        
        List<MarkedYAMLException> errors = validate(builder.toString(),Collections.singletonList(finder));
        assertThat(errors, contains(
                hasProperty("problemMark", problemOn(10)),
                hasProperty("problemMark", problemOn(11)),
                hasProperty("problemMark", problemOn(12)),
                hasProperty("problemMark", problemOn(13)),
                hasProperty("problemMark", problemOn(14)),
                hasProperty("problemMark", problemOn(15)),
                hasProperty("problemMark", problemOn(16)),
                hasProperty("problemMark", problemOn(17))
                ));
        
        verify(finder, zctxt);
    }
    
    @Test
    public void testWellKnownZoomContextWithOtherError() throws Exception {
        // Making sure that doing the grid validation doesn't screw up other validation later
        StringBuilder builder =  new StringBuilder();
        builder.append("grid:\n");
        builder.append("  name: WebMercator\n");
        builder.append("filter: foo\n");
        
        List<MarkedYAMLException> errors = validate(builder.toString());
        assertThat(errors.size(), is(1));
        assertThat(errors.get(0).getProblemMark(), problemOn(3));
    }

    List<MarkedYAMLException> validate(String ysld) throws IOException {
        //return dump(Ysld.validate(ysld));
        return this.validate(ysld, Collections.<ZoomContextFinder> emptyList());
    }
    List<MarkedYAMLException> validate(String ysld, List<ZoomContextFinder> ctxts) throws IOException {
        //return dump(Ysld.validate(ysld));
        return Ysld.validate(ysld, ctxts, new UomMapper());
    }
}
