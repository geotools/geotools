package org.geotools.renderer.chart;

import javax.swing.Icon;
import org.geotools.factory.CommonFactoryFinder;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.junit.Assert;
import org.junit.Test;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Literal;

public class ChartFactoryTest {

    ChartGraphicFactory factory = new ChartGraphicFactory();

    FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

    @Test
    public void testPie() throws Exception {
        Literal url = ff.literal("http://chart?cht=p&chd=t:10,20,70&chs=200x100");
        JFreeChart chart = factory.getChart(null, url, ChartGraphicFactory.FORMAT, 500);
        Assert.assertNotNull(chart);
        Assert.assertTrue(chart.getPlot() instanceof PiePlot);
        PiePlot p = (PiePlot) chart.getPlot();

        // values are turned in percentages
        Assert.assertEquals(3, p.getDataset().getItemCount());
        Assert.assertEquals(0.1f, p.getDataset().getValue(0));
        Assert.assertEquals(0.2f, p.getDataset().getValue(1));
        Assert.assertEquals(0.7f, p.getDataset().getValue(2));
    }

    @Test
    public void testInvalidLocation() throws Exception {
        Literal url = ff.literal("http://weirdo?cht=p&chd=t:10,20,70&chs=200x100");
        Assert.assertNull(factory.getChart(null, url, ChartGraphicFactory.FORMAT, 500));
        Assert.assertNull(factory.getIcon(null, url, ChartGraphicFactory.FORMAT, 500));
    }

    @Test
    public void testInvalidFormat() throws Exception {
        Literal url = ff.literal("http://chart?cht=p&chd=t:10,20,70&chs=200x100");
        Assert.assertNull(factory.getChart(null, url, "application/invalid", 500));
        Assert.assertNull(factory.getIcon(null, url, "application/invalid", 500));
    }

    @Test
    public void testInvalidSize() throws Exception {
        Literal url = ff.literal("http://chart?cht=p&chd=t:10,20,70&chs=200x100x1000");
        try {
            factory.getIcon(null, url, ChartGraphicFactory.FORMAT, 500);
            Assert.fail("Test should have thrown an exception");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    @Test
    public void testMissingSize() throws Exception {
        Literal url = ff.literal("http://chart?cht=p&chd=t:10,20,70");
        try {
            factory.getIcon(null, url, ChartGraphicFactory.FORMAT, -1);
            Assert.fail("Test should have thrown an exception");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    @Test
    public void testSizeFromChartSpec() throws Exception {
        Literal url = ff.literal("http://chart?cht=p&chd=t:10,20,70&chs=200x100");
        Icon icon = factory.getIcon(null, url, ChartGraphicFactory.FORMAT, -1);
        Assert.assertNotNull(icon);
        Assert.assertEquals(200, icon.getIconWidth());
        Assert.assertEquals(100, icon.getIconHeight());
    }

    @Test
    public void testSizeFromSLD() throws Exception {
        Literal url = ff.literal("http://chart?cht=p&chd=t:10,20,70");
        Icon icon = factory.getIcon(null, url, ChartGraphicFactory.FORMAT, 200);
        Assert.assertNotNull(icon);
        Assert.assertEquals(200, icon.getIconWidth());
        Assert.assertEquals(200, icon.getIconHeight());
    }

    @Test
    public void testSizeFromSLDAndChart() throws Exception {
        Literal url = ff.literal("http://chart?cht=p&chd=t:10,20,70&chs=200x100");
        Icon icon = factory.getIcon(null, url, ChartGraphicFactory.FORMAT, 600);
        Assert.assertNotNull(icon);
        Assert.assertEquals(600, icon.getIconWidth());
        Assert.assertEquals(300, icon.getIconHeight());
    }

    @Test
    public void testSizeFromSLDAndChartVertical() throws Exception {
        Literal url = ff.literal("http://chart?cht=p&chd=t:10,20,70&chs=100x300");
        Icon icon = factory.getIcon(null, url, ChartGraphicFactory.FORMAT, 600);
        Assert.assertNotNull(icon);
        Assert.assertEquals(200, icon.getIconWidth());
        Assert.assertEquals(600, icon.getIconHeight());
    }
}
