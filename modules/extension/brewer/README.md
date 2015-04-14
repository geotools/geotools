#Brewer

* Use Guide: http://docs.geotools.org/latest/userguide/extension/brewer/index.html

Resources:

* [IP Review](REVIEW.md)

## Status

* :star: Passes IP check, correct headers
* :star: Releasable - no blocking issues in jira
* :star: Quality Assurance - corbertura reports 63% line coverage, 53% branch coverage
* :star: Implementation based on stable classifier functions and style api
* :star: user docs provided, not many questions on the user list

## Recent Development

The brewer extension was conceived on the 2.2.x branch and is still undergoing major redesign. For
the 2.4.x branch, much of the StyleGenerator class is being rewritten to make better use of
classification functions.

If you are a volunteer we could use help improving documentation of this
module. Contributing a tutorial would be a great help.

Full migration to ClassificationFunction and Classifier was performed for 2.5.x new feature model.