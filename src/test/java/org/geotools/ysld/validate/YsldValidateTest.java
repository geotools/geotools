package org.geotools.ysld.validate;

import org.geotools.ysld.Ysld;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.error.MarkedYAMLException;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.Matchers.allOf;
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
    
    @SuppressWarnings("unchecked")
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
                "  name: EPSG:4326\n");
        
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

    List<MarkedYAMLException> dump(List<MarkedYAMLException> errors) {
        for (MarkedYAMLException e : errors) {
            System.out.println(e.toString());
        }
        return errors;
    }
    
    @Test
    public void testWellKnownZoomContextWithOtherError() throws Exception {
        // Making sure that doing the grid validation doesn't screw up other validation later
        StringBuilder builder =  new StringBuilder();
        builder.append("grid:\n");
        builder.append("  name: EPSG:4326\n");
        builder.append("filter: foo\n");
        
        List<MarkedYAMLException> errors = validate(builder.toString());
        assertThat(errors.size(), is(1));
        assertThat(errors.get(0).getProblemMark(), problemOn(3));
    }

    List<MarkedYAMLException> validate(String ysld) throws IOException {
        //return dump(Ysld.validate(ysld));
        return Ysld.validate(ysld);
    }
}
