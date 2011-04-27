Creating your own Module
==========================

So you want to do something wild, exciting, and cough dangerous? Chances are people have encouraged you in your GeoTools endeavour (we all love volunteers!). The following document outlines some guidelines to help make all of our lives easier (including yours).

The first section deals with communication and the rest show where new code can be incorporated into GeoTools.

Give warning - Get Permission
------------------------------
Lets follow a couple of steps - just to start out with. The goal here is to let everyone know what is happening, and see if other are willing/able to help.

Step One: Email the List
^^^^^^^^^^^^^^^^^^^^^^^^^^

Someone on the list may already have plans, or they might help, they may even do step number two for you...

  To: geotools-devel@lists.sourceforge.net
  CC: jgarnett@refractions.net
  Subject: Proposed Fish module
  
  Hi Jody, I noticed that the geotools library lacks support for the small fish POJO objects you keep mentioning in your examples.
  
  I would like to add this feature, by making use of Hibernate DataStore with a H2 backend. It will serve as an example use for new users and will be funny.
  
  Sincerely,
  ARandomDeveloper

Most likely Jody will respond with the request that you make a Wiki page, (Jody is good that way).

Step Two: Set up a Wiki page
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

We use the wiki for collaboration, and sometimes even documentation. Make sure you have access to the wiki (if you have not already), and set up a page describing your idea.

1. Login to Confluence
2. Navigate to the RnD page
3. Select "Add Content" > and then "Add Page"

You can add as much, or as little, details as you require to communicate. Feel free to attach any design documents you have to the page, introduce yourself and so on. It is always good to highlight any deadlines you have associated with the work.

Step Three: Ask via Email
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

We are not too formal, we just want to keep the lines of communication open.::

  To: jgarnett@refractions.net
  CC: geotools-devel@lists.sourceforge.net
  Subject: SVN Access request for Fish module
  
  Well as requested I have read the developers guide (some of it looks out of date?),
  and created a wiki page for my proposed module:
  
     http://docs.codehaus.org/display/GEOTOOLS/Fish
  
  I think all I need is svn access and I can get going.
  
  Sincerely,
  ARandomDeveloper

Even if if you do already have svn access is nice to email and let people review that wiki page. You never know, people may offer to help 

Hopefully the email discussion will settle down, (perhaps with skill testing question about the developers guide), leaving you armed with an svn password.

Step Four: Read the Developers Guide
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

This gives you something to do while waiting for feedback or authorisation from a GeoTools PMC member, and makes you up to speed with the conventions you need to know to effectively work with others.

Step Five: Create a little slice of SVN
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The modules/unsupported/ directory is there to welcome your experimental work; that is what the directory is for - a little RnD.

1. Create a skeleton
   
   The first step is to create a skeleton module for you work. The easiest way to do this is to copy, within svn, an existing module and alter that.

2. svn copy
   
   Choose a module that is close to what you want, copy it to capture the 5.3 Module Directory Structure, and gut the src/main/java, src/test/java and pom.xml files. Make sure to not copy the hidden .svn directories.
  
3. We have an "example" module all ready for you to copy::
  
      svn cp http://svn.osgeo.org/geotools/trunk/modules/unsupported/example/ \
             http://svn.osgeo.org/geotools/trunk/modules/unsupported/fish

   Alternatively::
     
      cd modules/unsupported
      svn cp example fish
   
4. Th should give you a working skeleton for your work.

5. Edit the base files
   
   All modules have a standard set of files, which are detailed in the Module Directory page. We need to edit these to match the new module's name and purpose.
   
6. pom.xml
   
   We start by getting the pom.xml configured since maven will need that to work against the module. The following will start you out:
   
   Change all occurances of the word example to the name of your module::
     
     <groupId>org.geotools</groupId>
     <artifactId>example</artifactId>
       <packaging>jar</packaging>
       <name>Example</name>
       <description>
         Supply a quick description here.
       </description>
       
   Supply information about yourself::
     
     <developer>
      <id>YOURID</id>
      <name>YOUR NAME</name>
        <email>you@server.org</email>
        <organization>University, Organisation or Company</organization>
        <organizationUrl>http://organization.url</organizationUrl>
        <timezone>YOUR_OFFSET_IN_HOURS</timezone>
        <roles>
          <role>Java Developer</role>
        </roles>
      </developer>

   Note: YOURID should be your SVN login name.

7. src/site/apt/review.apt
   
   This file describes the origin of the contents of your module and needs to be used to track any issues of copyright and licensing related to the module. We need to know about any code which was not written directly by those with svn access. For example, if the module depends on an external library, we need to know how it is that we are able to re-distribute that library under the LGPL. All modules should have such a file so, if you started by copying a module such as the example module, you should have an example of the file, the contents which are required and the formatting needed for those files.

8. Edit some code
   
   Finally, your time to shine. Add your code to the src/main/java/ and src/test/java/ directories. If you need to add resources, these can live in the src/main/resources/ and src/test/resources/ directories.

9. Ask for forgiveness
   
   Before committing your new module, you should make everyone aware you are about to do so by sending another email to let everyone know you are getting under way. Sometimes what you are asking so so strange that nobody will reply, and as a guideline I wait about three days before going ahead.
   
10. Do send a final email out to the list and maybe ask the exhaulted leader to speak up.
      
      TO: jrm33@psu.edu
      CC: jgarnett@refractions.net
      CC: geotools-devel@lists.sourceforge.net
      Subject: Starting work on Fish
      
      Hi Developers and/or PMC,
      
      The PMC is really busy, or exhausted from that last geotools breakout IRC has not gotten back to me.
      
      I have started the "unsupported/fish" module where I will prototype a hibernate datastore fish example. When complete I would like to get feedback from the list, it may be a candidate for inclusion in demo.
      
      Thanks,
      ARandomDeveloper

11. Commit to svn
    
    Once you have a working base, commit to the shared svn and we are off and running...

12. But what about ... Questions
    
    The Developers Guide should cover, or provide links to, information on:
    
    1. updating your pom.xml
    2. creating a test profile
    3. using svn ignore on your "target" directory
    
    In addition to answering most of the questions a new developer might have---its what we use to answer our own questions.
    
    Beyond that, there are the mailing lists for users and for developers.

Step Six: Include Yourself in the Build
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Once your module is stable and you are keeping it compiling as you work, you can include it in the shared build. This means everyone will have to compile your module whenever they compile the rest of GeoTools.

When you first do this commit, you should take special care. Ideally you will work with someone else who can confirm that the build works with their setup and you would try a test compile with a blank maven repository to ensure that others can access all the dependencies on which your module depends.

1. Edit the pom.xml
   
   Navigate to the unsupported/pom.xml file and update the list::
     
     <modules>
       <module>jpox</module>
       ...
       <module>fish</module>
     </modules>

2. Try a build
   
   First you should make sure that your module can build as part of the entire GeoTools build using maven clean install.
   
   Then you should try again, this time with a blank maven repository. First, backup or remove the maven repository which, by default, is hidden your home directory as ~/.m2/repository/. Then, run a full build once again using maven clean install. This build will need an internet connection and will take a while to download all the dependencies from the various servers. The build may even fail due to network issues; you may need to re-run the command, perhaps a few hours later, to work around temporary network or mirror issues.
   
   Ideally, you would then ask someone else, hopefully using a platform with a different architecture, to add the module to their build. If they succeed we can be fairly sure the module will build for everyone.

3. Commit
   
Then you can commit your one line change. Welcome to the build!
   
   Try to do this on a day when you will be around for the next few hours and available to deal with any problems which might arise. Your commit will probably trigger the automatic build systems to run a build. If they fail, they will send messages out to the developer's mailing list and to the IRC channel. If you can resolve the issues right away, you can avoid being kicked out of the build by someone else whose build suddenly starts failing when compiling or testing your module.

Step Seven: Bon Voyage
^^^^^^^^^^^^^^^^^^^^^^^

We all hope your work is a success and will eventually migrate from the land of radical development into the core GeoTools library. When you feel ready, you may decide to declare your module formally "supported" , at which point it could be moved into the modules/plugin/ or modules/extension/ directories. Eventually, the work could even become part of the core library.

Pearls of wisdom
^^^^^^^^^^^^^^^^^^^^^^^

Before we leave you here are some pearls of wisdom for you on your road to success:

* Do not break the Build
  
  We do have this nice rule about breaking the build: don't.
  
  Make sure you run a full maven install and test cycle before you commit: do a mvn clean install without using either the -DskipTests or the -Dmaven.test.skip=true flag. Yes, it takes longer; yes, it will save you some day.

* Communicate early and often
  
  Try and send email to the developers list about your progress. Once a week during active development is cool, or drop by the weekly IRC meeting. Ask for help, offer advice---it will all help you benefit from the expertise of others.

* Re-write your code
  
  Code only becomes polished and elegant when you have reworked it. You will improve as a coder as you work so re-writing old code will help you catch things the old-you used to write (yuck!) and replace them with things the new-you writes (aaaah).

* Unit tests are your friends
  
  Developing a well structured test suite is almost as valuable as developing a good set of code. A well structured test suite can help you develop high quality, robust, correct code.
