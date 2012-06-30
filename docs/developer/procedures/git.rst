Working with Git
================

The page is a guide for GeoTools committers describing how to work with Git.

Repository Distribution
-----------------------

Git is a distributed versioning system which means rather than one strictly central 
repository a git versioning system typically contains multiple distributed repositories.
This section describes the distribution of repositories for GeoTools.

Canonical Repository
^^^^^^^^^^^^^^^^^^^^

The *canonical* repository is the authoritative copy of the Git repository. It is the 
equivalent of the central Subversion repository. This repository is located at::

   https://github.com/geotools/geotools/

As stated this repository is the official repository which means that unless code is
present in this repository it is not officially part of the project and not released.

Developer Forks
^^^^^^^^^^^^^^^

In a distributed versioning system rather than work directly with the canonical
repository (if one exists) it is typical that a developer maintain their own fork of 
the canonical repository. For example:

* https://github.com/groldan/geotools
* https://github.com/jdeolive/geotools

A fork shares history which whatever repository it was forked from. Therefore a fork
really isn't treated any different from the canonical repository except for that it may
contain content (always in the form of branches) not present in the canonical repository.

Creating a fork is done directly from 
`GitHub <https://github.com/geotools/geotools/fork_select>`_.

Local Clones
^^^^^^^^^^^^

Developers always work from local copy of a git repository. To get a local copy an online 
git repository must be cloned. Whether a developer clones the canonical repository or a
developer fork is of no consequence because they are equivalent to each other and are 
represented by "remotes".

A *remote* is a link from a local repository to an online one. When you clone a git 
repository a remote named "origin" is implicitly created that points back to the online
repository it was cloned from. For example::

  % git clone git@github.com:jdeolive/geotools.git geotools
  % cd geotools
  % git remote -v
  origin	  git@github.com:jdeolive/geotools.git (fetch)
  origin	  git@github.com:jdeolive/geotools.git (push)

Adding links to other forks of the repository is trivial. For instance to link back to 
the canonical repository::

  % git remote add geotools git@github.com:geotools/geotools.git

It is recommended that for clarity local remotes be named after the github account they 
reference. Therefore in the previous example the "origin" remote should be renamed::

  % git remote rename origin jdeolive
  % git remote -v
  geotools	  git@github.com:geotools/geotools.git (fetch)
  geotools	  git@github.com:geotools/geotools.git (push)
  jdeolive	  git@github.com:jdeolive/geotools.git (fetch)
  jdeolive	  git@github.com:jdeolive/geotools.git (push)

Repository Structure
--------------------

This section describes how branches are organized in the git repository.

Primary Branches
^^^^^^^^^^^^^^^^

Like the old Subversion repository maintains typically two branches that are usually 
under active development:

#. ``master`` - The equivalent of what was ``trunk`` in svn
#. ``<?>.x`` - The current stable branch, what was ``2.7.x`` in svn

In addition to these two branches a number of "maintenance" branches, branches that were
previously the stable branch but have reached end-of-life, are also maintained. Listing
branches should show::

  % git branch
  2.7.x
  8.x
  master

.. note::

   Primary branches live in the canonical repository and all developer forks. 

Feature Branches
^^^^^^^^^^^^^^^^  

To work effectively with Git one must embrace the notion of branch development. Taking
this to the extreme would mean that a developer should never do any work on a primary 
branch. Instead creating a *feature* branch, performing a change, and then merging the
feature branch into a primary branch. A typical branch workflow looks like the following:

#. Create a new feature branch from a primary branch::

   % git checkout -b my_feature master

#. Make some changes

#. Stage and commit changes::

   % git add ...
   % git commit -m "added a new feature"

#. Merge into primary branch::

   % git checkout master
   % git merge my_feature

In reality often a change is trivial enough to not require a feature branch and just be
committed directly to a primary branch. But generally this practice is discouraged as it
is always wise to keep the primary branches clean of any work until one is ready to push
changes.

.. note::

   Feature branches generally live only in developer forks.

Do's and Don'ts
---------------

Git is a much more flexible system than Subversion by design. But with great power comes
great responsibility. This section provides some guidelines for avoiding shooting 
yourself and your fellow developers in the foot.

Porting changes across primary branches
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Generally when working on a change or bug fix it must be committed to both the ``master``
branch and the current ``stable branch``. This should **not** be done with a standard 
git merge. Instead making use of "cherry picking" is recommended.

  .. note:: 
  
     With feature branches this is not the case. Feature branches when ready should be 
     merged into the parent branch. However if the feature branch is to be merged into
     multiple primary branches it should first be merged into its parent and then cherry
     picked across to the second primary branch.

Cherry-picking basically takes a commit from another branch and applies it to the working 
branch. It should be noted that the resulting commit is a totally different commit to 
git. It is simply a convenient way to work with change sets that is the equivalent of 
manually generating a diff and applying it with a patch command::

  % git checkout master
  % git commit -m "GEOT-XYZ, fixing a bug"
  % git log 
  commit 9e6b6fca0104ac4d3630bd8444713fa2e2089547
  Author: jdeolive <jdeolive@gmail.com>
  Date:   Thu Jun 28 10:28:19 2012 -0600

      GEOT-XYZ, fixing a bug
      
  % git checkout 8.x
  % git cherry-pick 9e6b6fca0104ac4d3630bd8444713fa2e2089547

The above commands make a commit to ``master`` and then cherry-pick the commit onto the 
``8.x`` branch. An equivalent but far less convenient approach would be::

    % git checkout master
    % git commit -m "GEOT-XYZ, fixing a bug"
    % git log 
    commit 9e6b6fca0104ac4d3630bd8444713fa2e2089547
    Author: jdeolive <jdeolive@gmail.com>
    Date:   Thu Jun 28 10:28:19 2012 -0600

        GEOT-XYZ, fixing a bug
    
    % git show 9e6b6fca0104ac4d3630bd8444713fa2e2089547 > GEOT-XYZ.patch
    % git checkout 8.x
    % patch -p1 < GEOT-XYZ.patch
    % git add .
    % git commit -m "GEOT-XYZ, fixing a bug"

Line Endings
^^^^^^^^^^^^

When a repository is shared across different platforms it is necessary to have a 
strategy in place for dealing with file line endings. In general git is pretty good about
dealing this without explicit configuration but to be safe developers should set the 
``core.autocrlf`` setting to "input"::

    % git config --global core.autocrfl input

The value "input" essentially tells git to respect whatever line ending form is present
in the git repository.

.. note::

   It is also a good idea, especially for Windows users, to set the ``core.safecrlf`` 
   option to "true"::

      % git config --global core.safecrlf true

   This will basically prevent commits that may potentially modify file line endings.

Some useful reading on this subject:

* http://www.kernel.org/pub/software/scm/git/docs/git-config.html
* https://help.github.com/articles/dealing-with-line-endings
* http://stackoverflow.com/questions/170961/whats-the-best-crlf-handling-strategy-with-git

Rebasing
^^^^^^^^

In git *rebasing* is the act of rewriting the history of a branch. Some common use cases
of for rebasing include:

* Rewriting all local changes relative to another branch "b" so that all local changes
  on the branch appear at the head or tip of "b". For example::

    % git checkout my_feature
    % git rebase master

* Reorganizing commits on a branch to be more sensible, merging commits, deleting
  others, etc... This is referred to as "interactive rebasing". For example::

    % git checkout my_feature
    % git log --pretty=oneline --abbrev-commit
    882217a tweaking doc version to avoid @RELEASE@ tag in snapshot builds
    bf0d28b GEOT-4185, fixing version substitution for release
    d737e0f building release with process profile
    bf75ec1 A note about volitile function with an example of generating a random co
    1d2bd35 Update javadocs with an example using FeatureIterator and remove some no

    % git rebase -i d737e0f
      
.. warning::

   It is **critically important** that rebasing never occur on a shared or primary
   branch. The rule of thumb with rebasing is that you must **never** rewrite commits
   that are not strictly local, ie commits that have been pushed up to an online
   repository.
 
Other Tips
----------

This section contains some additional recommendations that are not critical but 
considered good practice.

Keep a tidy history
^^^^^^^^^^^^^^^^^^^

Unlike Subversion, Git is a sort of two phase commit system. 

#. A change is first committed locally
#. That change is then pushed up to a repository

The locality of commits usually tends to lend itself to multiple commits when performing 
a change. For example::

  % git commit -m "GEOT-XYZ, fixing a bug"
  % git commit -m "GEOT-XYZ, oops, forgot to add a file"
  % git commit -m "GEOT-XYZ, oops, forgot to fix that test failure"
  
Since all these changes are related to a single fix it is ideal to keep them as a single
commit. Since these commits are still local and not yet pushed up the canonical 
repository they can be "squashed". Commit *squashing* is an interactive rebase that
merges multiple commits into one. For example::

  % git log --pretty=oneline --abbrev-commit
  ad7sdfd GEOT-XYZ, oops, forgot to fix that test failure
  99dhdff GEOT-XYZ, oops, forgot to add a file
  8u3n8dd GEOT-XYZ, fixing a bug
  882217a tweaking doc version to avoid @RELEASE@ tag in snapshot builds
  % git rebase -i 8u3n8dd

An editor is then presented that allows a developer to merge first three commits into 
one.

Read more about `interactive rebasing <http://git-scm.com/book/en/Git-Tools-Rewriting-History#Changing-Multiple-Commit-Messages>`_.

Avoid merge commits when possible
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

In git a *merge commit* results when a branch is merged with another branch. This 
commonly occurs when a developer pulls in changes from a branch in the canonical 
repository. For example::

  % git checkout master
  % git commit -m "GEOT-XYZ, making a simple change"
  % git pull geotools master

The pull from the canonical repository will cause a merge commit to occur. A simple way
to avoid this is to force a "fast forward" by using the "--rebase" option to the pull 
command::

  % git checkout master
  % git commit -m "GEOT-XYZ, making a simple change"
  % git pull --rebase geotools master

The rebase option essentially will stash all local changes before doing the full, 
resulting in a fast forward update (avoiding a merge commit), and then replay the local
changes on top of that. 

Git Primer
----------

This section provides a basic introduction to git within the context of the GeoTools 
project, providing examples of some common workflows.

Initial repository setup
^^^^^^^^^^^^^^^^^^^^^^^^

::

  % mkdir geotools; cd geotools
  % git clone git@github.com:<userid>/geotools.git .
  % git remote rename origin <userid>
  % git remote add geotools git@github.com:geotools/geotools.git
  
The above commands will create a new local repository with two remotes. One named after
your account that points to your fork of the canonical repository. And one named 
"geotools" that points to the canonical repository.

.. note::

   Naturally you are free to organize your remote references as you see git. The above
   is simply a recommendation. One of the nice things about naming the remotes this way
   is that it makes it explicit as to where a changeset is being pushed to.
   
Pulling from canonical
^^^^^^^^^^^^^^^^^^^^^^

The equivalent of ``svn update``::

  % git checkout master
  % git pull --rebase geotools master
  % git checkout 8.x
  % git pull --rebase geotools 8.x
  

Pushing to canonical
^^^^^^^^^^^^^^^^^^^^

The equivalent of ``svn commit``::

  % git checkout master
  # make some local changes
  % git pull --rebase geotools master
  % git push geotools master
  
.. note::

   It is generally always a good idea to pull from a remote branch before pushing to it. 
   Actually git will abort the push request if the push results in a non fast-forward
   case.
   


