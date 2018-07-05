Process Raster Plugin
----------------------

The gt-process-raster plugin a provides a number of ready to use raster processign chains - and
provide a great example of how to work with rasters.

**Maven**::
   
   <dependency>
      <groupId>org.geotools</groupId>
      <artifactId>gt-process-raster</artifactId>
      <version>${geotools.version}</version>
    </dependency>


Jiffle
^^^^^^

The Jiffle process allows to perform map algebra on one or more input rasters.
The Jiffle language reference `can be found here <https://github.com/geosolutions-it/jai-ext/wiki/Jiffle---language-summary>`_.


The following test snippet shows how to use Jiffle to multiply two rasters:

.. code-block:: java

        public void testMultiply() {
            // build the input rasters
            GridCoverage2D c1 =
                    buildCoverage(
                            new float[][] {
                                {1, 0, 0},
                                {0, 1, 0},
                                {0, 0, 1},
                            });
            GridCoverage2D c2 =
                    buildCoverage(
                            new float[][] {
                                {2, 2, 2},
                                {2, 2, 2},
                                {2, 2, 2},
                            });
    
            // invoke the process using the Process interface  
            Process jiffle = Processors.createProcess(new NameImpl("ras", "Jiffle"));
            Map<String, Object> inputs = new HashMap<>();
            inputs.put(JiffleProcess.IN_SOURCE_NAME, new String[] {"a", "b"});
            inputs.put(JiffleProcess.IN_COVERAGE, new GridCoverage2D[] {c1, c2});
            inputs.put(JiffleProcess.IN_SCRIPT, "dest = a * b;");
            Map<String, Object> output = jiffle.execute(inputs, null);
            GridCoverage2D result = (GridCoverage2D) output.get(JiffleProcess.OUT_RESULT);
    
            // test results
            float[][] resultData = data(result);
            float[][] expected =
                    new float[][] {
                        {2, 0, 0},
                        {0, 2, 0},
                        {0, 0, 2},
                    };
            Assert.assertArrayEquals(expected, resultData);
        }

