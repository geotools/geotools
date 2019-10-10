Sphinx (Optional)
-----------------

To build the GeoTools "doc" folder you will need to install the components of the sphinx documentation system.

Reference:

* http://docs.geoserver.org/latest/en/docguide/install.html
* http://stackoverflow.com/questions/1815080/easy-install-pil-fails

Windows Sphinx Install
^^^^^^^^^^^^^^^^^^^^^^

Install Python:

1. Python version 2.7 has been verified to work: http://www.python.org/download/releases/2.7/
2. You will need to add it to your path.::
     
     set 'PYTHON=C:\Python27\'
     set 'PATH=%PATH%;%PYTHON%'

3. You will need Setup Tools for Python 2.7
   
   http://pypi.python.org/pypi/setuptools#downloads
   
4. Install and add Setup Tools to your path::
        
        run 'set SETUPTOOLS=C:\Python27\Scripts'
        run 'set PATH=%PATH%;%SETUPTOOLS%'

5. Install Sphinx::
   
        easy_install sphinx
   
   Optionally you could install a specific version of sphinx (although we try and use the latest)::
    
        easy_install sphinx==1.0.7

6. Install spellchecker::

       pip install pyenchant
       pip install sphinxcontrib-spelling

rst2pdf Optional Install
^^^^^^^^^^^^^^^^^^^^^^^^

You can optionally install ``rst2pdf`` to build PDF documentation:

1. Install Visual Studio 2008 Express Edition (this provides windows with
   a C compiler). It is a free download on the Microsoft site. You need to be
   sure to use the 2008 edition so that ``easy_install`` will compile something
   that can actually be linked to the Python executable.

2. Use easy install to produce ``rst2pdf``::
      
      easy_install rst2pdf
      
3. This depends on the Python Image Library (which it can probably build now that you have a compiler).
4. If you cannot manage to build you can download a precompiled Python Image
   Library (PIL) from here:
   
   * http://effbot.org/downloads/#pil (download the one for python 2.7)

Mac Sphinx Install
^^^^^^^^^^^^^^^^^^

You can use the distribution manager of your choice (example ``bru``, ``macports``,
etc...). The following example is for ``macports``.

1. On OSX Use ``macports`` to install Python 2.7::
     
     sudo port install python27
     sudo port install python_select
     sudo python_select python27
     
2. You can use ``macports`` to install Python Image Library::
     
     sudo port install py27-pil
     
3. You can now use python ``easy_install`` to install ``sphinx``::
     
     sudo easy_install sphinx
   
   Optionally you could ask for a specific version (we try and use the latest)::
     
      sudo easy_install sphinx==1.0.7
 
4. Install spellchecker::

       pip install pyenchant
       pip install sphinxcontrib-spelling

5. To build the PDF targets you will also need ``rst2pdf``.::
     
     sudo easy_install rst2pdf

6. If you uses ``easy_install`` to grab the python image library it easy to get
   compile errors.
      

Linux Sphinx Install
^^^^^^^^^^^^^^^^^^^^

Use ``apt-get`` and ``easy_install``.

1. Python is usually available by default, if not::
     
      apt-get install Python
  
   You may need to use ``sudo`` (if for example you are on Ubuntu)
      
2. Use easy_install to graph sphinx (using ``sudo`` if required)::
     
     easy_install sphinx
  
  Optionally you can install a specific version::
  
     easy_install sphinx==1.0.7

3. Install spellchecker::

       pip install pyenchant
       pip install sphinxcontrib-spelling
