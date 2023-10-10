Sphinx (Optional)
-----------------

To build the GeoTools "doc" folder you will need to install the components of the sphinx documentation system.

Reference:

* http://docs.geoserver.org/latest/en/docguide/install.html
* http://stackoverflow.com/questions/1815080/easy-install-pil-fails

Windows Sphinx Install
^^^^^^^^^^^^^^^^^^^^^^

Install :command:`python` and :command:`sphinx-build` environment:

1. Python version 3 is required:

   https://www.python.org/downloads/

2. Once installed python will need to add it to your path.
   
   .. code-block:: shell
     
      set 'PYTHON=C:\Python3\'
      set 'PATH=%PATH%;%PYTHON%'

5. The Python :command:`pip` is used to install python libraries and tools required:
    
   .. code-block:: shell
      
      cd docs
      pip install -r requirements.txt
      
  A the time of writing requirements.txt installs:
  
  .. literalinclude:: ../../../requirements.txt
     :language: text
     
6. To confirm installation:

   .. code-block:: shell
      
      sphinx-build --version
   
   ::
      
      sphinx-build 6.2.1

macOS Sphinx Install
^^^^^^^^^^^^^^^^^^^^

Install :command:`python` and :command:`sphinx-build` environment:

1. Python version 3 is required:
   
   .. code-block:: bash
      
      brew install python 

2. Once installed python will need to add it to your path in :file:`~/.zshrc`:
   
   .. code-block:: shell
     
      export PATH="$PATH:~/Library/Python/3.9/bin"

5. The Python :command:`pip3` is used to install python libraries and tools required:
    
   .. code-block:: shell
      
      cd docs
      pip3 install -r requirements.txt
      
  A the time of writing requirements.txt installs:
  
  .. literalinclude:: ../../../requirements.txt
     :language: text
     
6. To confirm installation:

   .. code-block:: shell
      
      sphinx-build --version
   
   ::
      
      sphinx-build 6.2.1

Linux Sphinx Install
^^^^^^^^^^^^^^^^^^^^

Use your linux package manager such as ``apt-get``:

1. Python is usually available by default, if not::
     
      apt-get install Python
  
   You may need to use ``sudo`` (if for example you are on Ubuntu)
      
2. The Python command:`pip` is used to install python libraries and tools required:
    
   .. code-block:: shell
      
      cd docs
      pip install -r requirements.txt
      
  A the time of writing requirements.txt installs:
  
  .. literalinclude:: ../../../requirements.txt
     :language: text
     
3. To confirm installation:

   .. code-block:: shell
      
      sphinx-build --version
   
   ::
      
      sphinx-build 6.2.1