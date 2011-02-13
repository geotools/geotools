Hacking
========================

So you want to fix something, or gasp improve, a part of the GeoTools library?

Chances are people have encouraged you in your GeoTools endevour (we all love volunteers!). The
following document outlines some guidelines to help make all of our lives easier (including
yours).

Give warning - Get Permission
-----------------------------

Lets follow a couple of steps - just to start out with. The goal here is to let everyone know what
is happening, and see if other are willing/able to help.

1. Step One: Talk to the module maintainer
   
   They may already have plans, and they might help, they may do step number two for you...::
      
      To: geotools-devel@lists.sourceforge.net
      CC: jdeolive@openplans.org
      Subject: Proposed change to Postgis DataStore
      
      Hi chris I noticed that the postgis datastore lacks a small penguin support from GO-1.
      
      I would like to add this feature, by making use of new new SVG SLD icons support
      storing the symbol used for pengins in the Postgis metadata table information.
      
      The MIL2525B spec contains the icon definition. Adding this metadata entry will allow
      GTRenderer (and thus GeoServer) to know the default icon associated with the table without
      additional client intervention.
      
      Sinceryly,
      ARandomDeveloper
   
   Most likely Justin will respond with the request that you make a Jira task (Justin is good
   that way).

2. Step Two: Set up a Jira task
   
   Jira tracks features as well as bugs. Create a Jira account (if you have not already), and set
   up a Jira task for your proposed work.

3. Step Three: Email the list and explain what you want to do.
   
   We are not too formal, we just want to keep the lines of communication open.::
      
      To: jdeolive@openplans.org
      CC: geotools-devel@lists.sourceforge.net
      Subject: Adding Penguin support to Postgis DataStore
      
      I noticed that the postgis datastore lacks a small penguin support from  MIL2525B.
      
      I have created a Jira task: JIRA-234 and would like to begin work.
      
      I will be making use of new new SVG SLD icons support
      storing the symbol used for penguins in the Postgis metadata table information.
      
      The MIL2525B spec contains the icon definition. Adding this metadata entry will allow
      GTRenderer (and thus GeoServer) to know the default icon associated with the table without
      additional client intervention.
      
      This change should be transparent to existing code. 
      Sinceryly,
      ARandomDeveloper
   
   Even if if you are the module maintainer it is nice to email and give people a couple days
   notice, or ask for a vote if your changes will break other peoples code.
   
   You never know, people may offer to help 

4. Step Four: Ask for forgiveness
   
   Sometimes what you are asking so so strange that nobody will reply (or vote), and as a
   guideline I wait about three days before going ahead.
   
   Do send a final email out to the list and maybe ask the project steering committee speak for
   the module maintainer that did not reply to you?
   
   It is also recommended that you strike up an ext/module or attach a patch to your jira issues.
   Having code to look at will help people understand what you are talking about.::
      
      TO: jgarnett@gmail.com
      CC: jdeolive@openplans.org
      CC: geotools-devel@lists.sourceforge.net
      Subject: Starting work on Postgis DataStore Penguin support
      
      Hi Jody / Justin,
      
      Justin is really busy, or lost in south america and has been unable to get back to me. The
      email list has not responded to JIRA-234 either. Apparently everyone is really busy.
      
      I have started "ext/postgis-pengin" where I will prototype penguin support for postgis. When
      complete I would like to get feedback from the list, or your approval before merging these
      changes into plugin/postgis.
      
      Thanks,
      ARandomDeveloper

5. Depending on what feedback you got you are in one of the following positions:
   
   * New unsupported module
   * Working on a Branch
   * Working on Trunk
   
0. Step 0: Don't break the Build
   
   And remember don't break the build.
   
   We do have this nice rule about breaking the build (don't). This means that if you are hacking
   core interfaces you will be running all over the place cleaning up modules.
   
   One very good reason to talk to the list first is to give other module maintainers a chance to
   get out of the way, or offer to help you clean up the mess.

New Unsupported Module
----------------------

A recent procedure addition is the idea of working on an "unsupported" module in GeoTools. These modules are located in the modules/unsupported directory and are not generally included in the
nightly build.

You can create the module following the procedures in this developers guide; leaving you with a slice of svn to work in, and a profile to include your module in the build when you are working on it.

When ready your module can be included in the normal build for everyone; and you can go through the quality assurance procedures checks to make the module part of GeoTools proper.

Working on a Branch
-------------------

This is what all those branches in the svn repository are about - and they are not fun  Many great GeoTools capabilities started out life in a branch: from the datastore api, through to grid coverage exchange and feature id mapping.

When asked to work on a branch please don't feel neglected. You are working on important aspects of the GeoTools library. In fact your work is so important that it would impact current development.

By having your own branch you are free to work on any module and do great things.

With Great Powers comes great responsibilities - you are responsible for:

* merging your work back onto trunk
* releasing a new GeoTools point release
* If your branch lasts more then a couple of weeks this is going to represent a lot of work.
  
  Please be warned and try to work within the plugin system if at all possible.

1. branching
   
   The svn book is a great resource here, you have read it haven't you?
   
   Of course you have - so this will only serve as reminder for you::
    
     svn cp http://svn.geotools.org/geotools/trunk http://svn.geotools/org/geotools/branches/wild_exp

   Here is a good tip though - write down the revision number - there will be a test after class
   
   Of course when you eventually merge your work back onto trunk you will need to go through that
   get permission cycle again - hopefully you can scare up a few volunteers (at least for testing).

2. svn merging
   
   Guess what it is after class you need that revision number.
   
   Subversion merging is a strange beast - you are basically going to gather all the changes
   to your source tree from that version number to now as one big happy diff.
   
   And then apply that diff to trunk.::
     
     svn merge -r REVISION:HEAD http://svn.geotools/org/geotools/branches/wild_exp .
   
   Or if you are more brave (or trusting)::
     
     svn merge http://svn.geotools/org/geotools/branches/wild_exp http://svn.geotools/org/geotools/trunk .
   
   Where "." is your working copy.
   
   In practice, we have often found that using svn copy for individual files that are entirely
   new, and use svn merge on the other files one at a time works the best.

Working on Trunk
----------------

This is actually the worst outcome - why?

* Because you have landed on a critical path. That don't break the build guideline will really
  start to kick in.
* This means that you really have to run maven (from scratch), and pass all tests, before you even
  think about committing.
* You will also need to send out those permission emails all the time as you run over module after
  module with changes.

Working on an unsupported/ module or in a branch is way more fun.

However don't lose hope - others have braved this process. And on the bright side you will get lots of feedback when you break things.

We have taken steps to make working on trunk more enjoyable; there are build boxes that will run tests after each commit and send you email if anything is broken. But it is more stressful than quietly working on a branch while you think about the best way to do things.
