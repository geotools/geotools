<Include a few sentences describing the overall goals for this Pull Request>

## Checklist

> Reviewing is a process done by project maintainers, mostly on a volunteer basis. We try to keep the overhead as small as possible and appreciate if you help us to do so by completing the following items. Feel free to ask in a comment if you have troubles with any of them.

For all pull requests:

- [ ] Confirm you have read the [contribution guidelines](https://github.com/geotools/geotools/blob/master/CONTRIBUTING.md) 
- [ ] You have sent a Contribution Licence Agreement (CLA) as necessary (not required for small changes, e.g., fixing typos in documentation)
- [ ] Make sure the first PR targets the master branch, eventual backports will be managed later. This can be ignored if the PR is fixing an issue that only happens in a specific branch, but not in newer ones.
- [ ] The changes are not breaking the build in downstream projects using SNAPSHOT dependencies, GeoWebCache and GeoServer.

The following are required only for core and extension modules (they are welcomed, but not required, for unsupported modules):
- [ ] There is a ticket in Jira describing the issue/improvement/feature (a notable exemptions is, changes not visible to end users)
- [ ] PR for bug fixes and small new features are presented as a single commit
- [ ] Commit message must be in the form "[GEOT-XYZW] Title of the Jira ticket"
- [ ] New unit tests have been added covering the changes
- [ ] This PR passes all existing unit tests (test results will be reported by travis-ci after opening this PR)
- [ ] This PR passes the [QA checks](https://docs.geotools.org/latest/developer/conventions/code/qa.html) (QA checks results will be reported by travis-ci after opening this PR)
- [ ] Documentation has been updated accordingly.

Submitting the PR does not require you to check all items, but by the time it gets merged, they should be either satisfied or inapplicable.