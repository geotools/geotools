# GeoTools Documentation

GeoTools documentation is provided using a [Attribution 3.0 Unported](LICENSE.md) license.

Code examples are [explicitly placed into the public domain](LICENSE.txt), to facilitate the easy cut-and-paste of example code.

## Python and Sphinx Setup

GeoTools makes use of the python sphinx documentation system:

1. Install Python (macOS example):

   ``` bash
   brew install python
   ```

2. To install ``sphinx-build`` and ``sphinx-autobuild`` using ``requirements.txt``:

   ``` bash
   pip3 install -r requirements.txt
   ```

3. To confirm installation:

   ``` bash 
   sphinx-build --version
   sphinx-autobuild --version
   ```

### Python Virtual Environment Setup (Alternative)

1. To establish a virtual environment just for this project (macOS example):
   
   ``` bash
   brew install virtualenv
   virtualenv venv
   ```

2. To activate python:

   ``` bash
   source venv/bin/activate
   ```

3. To install requirements into virtual environment:

   ``` bash
   pip install -r requirements.txt
   ```

4. To confirm installation:

   ``` bash
   sphinx-build --version
   sphinx-autobuild --version
   ```

## Building with Maven


1. The maven compile stage is used to generate documentation:

   ``` bash
   mvn compile
   ```
   
2. Pofiles are avaialble to build just one section:

   ``` bash
   mvn compile -Puser
   mvn compile -Pdeveloper
   ```

3. The `release` module `package` phase is responsible for bundling the user documentation download.

   See [userDocDist.xml](../release/src/assembly/userDocDist.xml) for details. 

## Building with Ant

1. You may also quickly test by calling the ant ``build.xml`` directly::

   ``` bash 
   ant user
   ```
   
   The files are generated into ``target/user/html/index.html``.
   
2. Interactive feedback while editing is availle:

   ``` bash
   ant user-site
   ```
   
   Opens ``http://localhost:8000/`` for a live preview which will update as you edit.

3. For the complete list of build targets:
   
   ``` bash
   ant -p
   ```
   ``` text
   Buildfile: /Users/jgarnett/dev/geotools/geotools/docs/build.xml

       sphin-build integration for GeoTools documentation
   
   Main targets:

    developer       Generate sphinx-build developper guide
    developer-site  Interactive autobuild evelopper guide, opens browser to http://localhost:8000
    full            Generate sphinx-build of all content
    index           Generate sphinx-build index page
    user            Generate sphinx-build user guide
    user-site       Interactive autobuild user guide, opens browser to http://localhost:8000
    web             Generate sphinx-build website
   Default target: full
   ```

Please see the user guide build instructions for more detail.