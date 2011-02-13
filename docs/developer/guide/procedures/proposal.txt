GeoTools change proposal
==========================

This procedure is used to propose a change to the GeoTools API. By necessity all API changes must be applied to trunk (although RnD experiments may of been carried out in a branch or unsupported module prior to being proposed).

So you want to make a Change
----------------------------

When to use this Procedure
^^^^^^^^^^^^^^^^^^^^^^^^^^

This procedure is used at the start of your project; before deadlines are established to ensure that the work you need to accomplish can be done and delivered.

When not to use this Procedure
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

This procedure is focused on API changes to the core library, for work limited to a plugin you should just be able to sort things out with the module maintainer.

Repeat - this procedure is for changes to public interfaces, for simple bug fixes and mistakes chat the the module maintainer.

Playing nicely with others
^^^^^^^^^^^^^^^^^^^^^^^^^^

This procedure is used to let us work in a controlled and managed fashion:

* preserve library stability
* allow evolution
* forbid controversial changes
* up to date documentation

When will this procedure be revised
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

We have the following measures of success:

* We want trunk to be directly used by GeoServer and uDig projects 
  
  If this no longer occurs we will revisit this policy

* We want the user documentation to reflect trunk
  
  If a release is delayed waiting for documentation we will need to revisit this policy

* Client code will have a full release cycle to change over from deprecated code 
  
  This is not always possible when the interface is not defined by the GeoTools project (JTS and apache commons etc...)

* Release update instructions should be exacting

Check List
^^^^^^^^^^

====== ============== ===========================================================
Check  Task           Notes
====== ============== ===========================================================
x      Issue          The Jira issue will be used to record on going discussion
x      Proposal       This page reflects the current proposal
x      Email          Seek public feedback
x      Voting         Change is discussed and/or approved
x      Update         Introduce API change
x      Deprecate      Any prior art can be deprecated at this time
x      Documentation  Update documentation and examples
x      Release        API change must be included in a point release
x      Remove         Deprecated API can be removed after point release
====== ============== ===========================================================

Issue
------

You will need to create a new Jira issue to track discussion; this issue will eventually be marked as closed against a formal release and be accessible to users.

By using Jira we get a nice record for our release notes.


Proposal
--------

The proposal page is used to track the current state of the proposal; it will be modified as discussions and compromises are reached.

1. Open up Proposals
2. Select **Add Content** and then **Add Page**
3. Click on Select a page template and select Proposal
4. Press Next>> and fill in the blanks

The resulting document is yours to play with - here is what you get out of the box:

* Motivation: Quick description of what is needed
* Contact: Use your [~confluence_profile] or email address
* Tracker: Link to your Jira Issue
* Tagline: Have some fun
* Description: Contains as much background material as you need to convey the problem; and towards
  the end of the process enough design material to describe the solution
* Status: Initially "under under construction", eventually we will move on to "approved" and
  "completed in GeoTools 2.5.0". There is also a quick task list to mark where you are stuck.
* Resources: List of child pages and attachments; often used for diagrams and documents
* Tasks: a rough outline of the plan, we are looking to make sure that volunteer time exists to
  solve the problem (rather then just started). Remember half a solution just looks like another
  problem.
* API Changes: simple BEFORE and AFTER, this will be used in the update instructions associated
  with each release
* Documentation Changes: Identify the developer and user documentation that needs to be modified
  as part of this proposal

The amount of detail you need depends on what you are up to:

* Changes effecting multiple modules is significant and will need most of the above
* Changes localised in the public API of a single module may be able to get by with only API
  changes and documentation updates

Email
-----

Once you have the proposal page in place, and the Jira issue ready to capture discussion it is time to send an email.::
  
  From: jgarnett@refractions.net
  To: ~geotools-devel@lists.sourceforge.net
  Subject: FunctionImpl Library separation
  
  Justin found a short coming in our implementation of Functions; right
  now we are focused on Implementation to the point we forgot to capture
  the "idea" of a function.
  
  Significantly if we do not have an implementation for a particular
  function we cannot even represent that Expression in GeoTools.
  
  This will require the introduction of a new API; and thus I am following
  the GeoTools change proposal process and emailing the list.
  
  Here are the links:
  - http://jira.codehaus.org/browse/GEOT-1083
  - http://docs.codehaus.org/display/GEOTOOLS/FunctionImpl Library separation
  
  This change requires the introduction of *new* API (namely Library), and
  will require the deprecation of our existing FunctionFactory extension
  mechanism (as it is flawed). We can however "run with both" (i.e. duplicate,
  deprecate, replace).

  Cheers,
  Jody Garnett

Voting
------

PMC members do vote on the change proposal, with the following votes:

===== =====================================================
+1    For	 
+0    Mildly for, but mostly indifferent
-0    Mildly against, but mostly indifferent
-1    Against - required to have an alternate suggestion
===== =====================================================

To avoid stagnation by lack of interest/time from community members the following assurances are provided:

* svn access for changes is granted within 3 days from the proposal
* proposal is accepted 'automatically' within 15 days (unless objections are raised)

Since I am sure your change is interesting enough here is what you need to proceed:

* A successful proposal with no negative -1 votes
* You can consider the the alternatives provided with the -1 votes and revise your proposal

It is also strongly recommended that you have:

* Approval from the module maintainer for the API you are changing, if they have a grand vision it is good to go along with it
* A lack of panic from effected module maintainers

Update
------

You will need to update GeoTools to make the new API available, creating new classes adding new methods as needed.::
   
   interface Library {
      FilterCapabilities getFilterCapabilities();
      Object evaluate( String name, List params );
   }

Please understand that adding methods to an interface is not always an option (since client code will be forced to update right away). You can create an additional interface with the required methods (and give users one release cycle to implement this additional API).

Deprecate
---------

The next step is to deprecate the API being replaced (in the case of new API this step can be skipped).::
   
   /** @deprecated Please use Library as a replacement */
   interface FunctionFactory {
      ..
   }

You (of course) must provide a replacement for deprecated API, it could be as simple as a workaround provided as part of the documentation.

Documentation
-------------

You will need to update all test cases and demo code to use the new API; since our test cases are often used by those learning the library this is especially important (also leaving deprecated code in the test cases simply assures us of a broken build one release cycle later when the plug is pulled).

The User Guide, and User Manual are often the target of documentation updates. The developers guide tries to stick to library architecture and only needs updating if you are changing the scope and/or focus of one of the core modules.

Release
-------

The next step is to make the changed API available in at least a milestone release. It is good to ensure that an API change is in releasable state (this may pick up PMD failures or QA coverage needs that were missed earlier).

Remove
------

After the API change is included in a point release the deprecated code can be removed from trunk.