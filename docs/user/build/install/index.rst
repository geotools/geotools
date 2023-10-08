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

1. Required: Java Development Kit 11 (JDK 11)
  
   Open JDK:
  
   * https://adoptium.net/temurin/releases/?version=11 Temurin 11 (LTS) - Recommended
  
   `SDKMAN! <https://sdkman.io>`__ installation:
   
   .. code-block:: bash
   
      # list to determine latest Temurin JDK 11
      sdk list java 11
   
      # Installing latest Temurin JDK 11 shown above
      sdk install java 11.0.20.1-tem

2. Optional: Java Development Kit 17 (JDK 17)
  
   Open JDK:
  
   * https://adoptium.net/temurin/releases/?version=17 Temurin 17 (LTS)

   `SDKMAN! <https://sdkman.io>`__ installation:
   
   .. code-block:: bash
   
      # list to determine latest Temurin JDK 17
      sdk list java 17
   
      # Installing latest Temurin JDK 11 shown above
      sdk install java 17.0.8.1-tem
   
   GeoTools build process takes advantage of quality assurance tools compatible with the JDK you have installed.
   Pull-requests are tested with both JDK 11 and JDK 17.

   It is recommended you have both JDK 11 and JDK 17 both on hand troubleshoot QA failures.

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
  
   * http://maven.apache.org/download.html
   
  `SDKMAN! <https://sdkman.io>`__ installation:
  
   .. code-block:: bash
   
      sdk install ant
