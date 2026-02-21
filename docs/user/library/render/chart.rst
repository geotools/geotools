Chart Plugin
------------

The **gt-chart** plugin supports the definition of a Chart as a Mark or ExternalGraphic. This is implemented as a
"dynamic symbolizer" using the JFreeChart and Eastwood projects.

References:

* http://www.jfree.org/eastwood/
* http://www.jfree.org/jfreechart/

**Maven**::
   
    <dependency>
      <groupId>org.geotools</groupId>
      <artifactId>gt-charts</artifactId>
      <version>${geotools.version}</version>
    </dependency>

Example
^^^^^^^

The following example is taken from test cases:

* :download:`streams.sld </../../modules/plugin/charts/src/test/resources/org/geotools/renderer/chart/test-data/pieCharts.sld>`
* :download:`cities.properties </../../modules/plugin/charts/src/test/resources/org/geotools/renderer/chart/test-data/cities.properties>`

Here is the example SLD:

.. literalinclude:: /../../modules/plugin/charts/src/test/resources/org/geotools/renderer/chart/test-data/pieCharts.sld
     :language: xml


Configuration
-------------

Charts are configured by an  URL of the form http://chart?key=value&key=value  .  In an SLD, the URL needs to be
XML-encoded, so you'll end up with xlink:href="http://chart?key=value&amp;key=valu&amp;...."

Most keys have multiple arguments, whose name encodes the type: f= floating point, i= integer, c = color RRGGBB or RRGGBBAA

The following keys are supported.



.. list-table:: Supported keys for the chart URL
   :name: tables-listchartkeys
   :widths: 20, 80
   :class: longtable
   :header-rows: 1
   :align: left

   * - Key
     - Description
   * - cht
     - Chart type - Supported values are:


        ======   ================================== =====================
        Value    Resulting type                     Data series
        ======   ================================== =====================
        p        Pie chart (1 series)               1
        p3       3D Pie chart                       1
        lc       Line chart                         n
        ls       Sparkline chart                    n
        lxy      XY Line chart                      2n
        bhg      Bar chart - Horizontal             n
        bhs      Bar chart - Horizontal - Stacked   n
        bhs3     Bar chart - Horizontal - 3D        n
        bvg      Bar chart - Vertical               n
        bvs      Bar chart - Vertical -Stacked      n
        bvg3     Bar chart - Vertical - 3D          n
        s        Scatter chart                      2 (XY) or 3 (XYZ)
        dial     Dial chart                         special
        v        (Venn diagrams not implemented)
        ======   ================================== =====================

   * - chco
     - Chart color - format is cColor1 ',' cColor2 ',' cColor3 ...

   * - chd
     - Data -  can be formatted in 3 ways: simple/text/extended:

       * Format 's:' serie ',' serie ',' ...

         Each serie contains characters A..Z,a..z,0..9  with A being the lowest 0.0 and 9 the highest possible
         value 1.0
       * Format 't:' fValueSerie1 ',' fValueSerie1 ','  ... '|' fValueSerie2 ',' ...
       * Format 'e:' serie  ',' serie ',' ...

         Each serie contains character pairs A..Z,a..z,0..9, -.  with AA being the lowest 0.0 and `..` the
         highest possible value 1.0

       Dials have a special syntax: item ',' item ',' ...

       Items are:

       * Data range:  'dr=' fNumber ':' fNumber ':' cColor
       * lower bound: 'lb=' fNumber
       * upper bound: 'ub=' fNumber
       * minor tick increment: 'mt=' fNumber
       * major tick increment: 'mjt=' fNumber
   * - chds
     - Data scaling - format is fScale ',' fScale ',' ...
   * - chf
     - Fill spec - format is spec '|' spec

       Spec is one of:

       * background color: 'bgs' cColor
       * background gradient: 'blg' fAngle ',' cColor0 ',' f0 ',' cColor1 ',' f1
       * plot color: 'cs' cColor
       * plot gradient: 'clg'  fAngle ',' cColor0 ',' f0 ',' cColor1 ',' f1
   * - chg
     - Grid line spec - format is fXStep ',' fYStep  ',' fLineSegLen  ', ' fBlankSegLen
   * - chl
     -  Labels - format is  label '|' label ...
   * - chdl
     - Legend - format is label '|' label ...
   * - chls
     - Line style -  format is fWidth ',' fLineRun ',' fGapRun '|'  ...
   * - chm
     - Markers - format is type ',' fStart ',' fEnd '|'   ...
       Types are 'r' for a ranger marjern 'R' for a domain marker
   * - chp
     - Pie chart rotation - format is fAngle
   * - chs
     - Chart size - format is iWidth 'x' iHeight ( in pixels)
   * - chts
     - Axis title style - format is: sFontName ',' cTitleColor
   * - chtt
     - Axis title - format is title '|' title '|' ...
   * - chxr
     - Axis range - format is: iAxisIndex ',' fLowerBound ',' fUpperBound '|' ...
   * - chxl
     - Axis label - format is: iAxisIndex ':' sText '|' ...
   * - chxp
     - Axis position - format is: iAxisIndex ','  fTickNumber ',' fTickNumber  ... '|' ...
   * - chxs
     - Axis style - format is: iAxisIndex ',' cAxisColor  ',' fFontsize ','  unused  ',' drawingcontrol  ',' cTickColor '|' ...

       drawingcontrol is 'l' /'t' / '_'  / 'lt' where l and t make tickmarks and axis line visible
   * - chxt
     - Axis text - format is: axis ',' axis ',' ...

       axis is one of the characters 'x' 'y' 'r' 't'.
       Every axis defines a new numbered axisindex, starting from 0
   * - ewd2
     - Extra dataset, format is the same as chd
   * - ewlo
     - Orientations - format is: iAxisIndex  ',' orientation '|' ...

       orientation is 's' / 'u' / 'd' for standard/up/down
   * - ewtr
     - format is iSeries ',' cColor ',' cLineWidth
