# GeoTools Documentation

GeoTools documentation is provided using a [Attribution 3.0 Unported](LICENSE.md) license.

Code examples are [explicitly placed into the public domain](LICENSE.txt), to facilitate the easy cut-and-paste of example code.

## Sphinx

GeoTools makes use of the python sphinx documentation system:

```
   pip install sphinx
```

The maven compile stage is used to generate documentation:

```   
   mvn compile
   mvn compile -Puser
   mvn compile -Pdeveloper
```

You may also quickly test by calling the ant `build.xml` directly::

```   
   ant user
   ant developer
```

Please see the user guide build instructions for more detail.
