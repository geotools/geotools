SWT
===

After some discussion in the GeoTools mailing list about the possibility of a SWT port of the
GeoTools swing module, Ian Mayo from the Debrief project funded the port. He also wanted the
resulting port and documentation to be Open Sourced (thanks!).

The port now lives in the unsupported space of the GeoTools repository and is called ``gt-swt``.
During the port whatever possible from the Swing module has been kept the same in order to help
in a future extraction of common interfaces for GUI modules.

**Maven**::
   
    <dependency>
      <groupId>org.geotools</groupId>
      <artifactId>gt-swt</artifactId>
      <version>${geotools.version}</version>
    </dependency>

**Contents**

.. toctree::
   :maxdepth: 1

   swtmapframe
   rcp
