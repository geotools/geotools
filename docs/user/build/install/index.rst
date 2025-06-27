Install Build Tools
===================

.. toctree::
   :maxdepth: 1
   
   jdk
   mvn
   git
   sphinx

Download Links
^^^^^^^^^^^^^^

Before using or developing GeoTools you should download the software listed here. You can start
downloading now before proceeding on to the step by step install instructions.

The following tools are required to build GeoTools:

1. Required: Java Development Kit 17 (JDK 17)
  
   Open JDK:
  
   * https://adoptium.net/temurin/releases/?version=17 Temurin 17 (LTS) - Recommended
  
   `SDKMAN! <https://sdkman.io>`__ installation:
   
   .. code-block:: bash
   
      # list to determine latest Temurin JDK 17
      sdk list java | grep "17.*-tem"
   
      # Installing latest Temurin JDK 17 shown above
      sdk install java 17.0.15-tem

2. Optional: Java Development Kit 21 (JDK 21)
  
   Open JDK:
  
   * https://adoptium.net/temurin/releases/?version=21 Temurin 21 (LTS)

   `SDKMAN! <https://sdkman.io>`__ installation:
   
   .. code-block:: bash
   
      # list to determine latest Temurin JDK 21
      sdk list java | grep "21.*-tem"
   
      # Installing latest Temurin JDK 21 shown above
      sdk install java 21.0.7-tem
   
   GeoTools build process takes advantage of quality assurance tools compatible with the JDK you have installed.
   Pull-requests are tested with both JDK 17 and JDK 21.

   It is recommended you have both JDK 17 and JDK 21 both on hand troubleshoot QA failures.

3. Apache Maven
  
   Maven is the primary build tool used for GeoTools.
   
   Maven excels at downloading and managing dependencies.
   Jars are staged in your home directory :file:`.m2/repository` folder for use when building.
     
   * http://maven.apache.org/download.html
   
  `SDKMAN! <https://sdkman.io>`__ installation:
  
   .. code-block:: bash
   
      sdk install maven

4. Optional: Apache Ant
  
   A few update scripts and build integration scripts use Apache Ant.
   
   Apache Ant provides a cross-platform `Makefile` of build targets .. with all the usability of XML.
  
   * https://ant.apache.org/bindownload.cgi
   
  `SDKMAN! <https://sdkman.io>`__ installation:
  
   .. code-block:: bash
   
      sdk install ant
