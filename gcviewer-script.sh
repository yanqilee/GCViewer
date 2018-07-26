#!/bin/bash
set -ev
echo "TRAVIS_PULL_REQUEST = ${TRAVIS_PULL_REQUEST}"
echo "TRAVIS_BRANCH = ${TRAVIS_BRANCH}"
echo "TRAVIS_JDK_VERSION = ${TRAVIS_JDK_VERSION}"

# only deploy, if it is chewiebug/gcviewer#develop branch and openjdk8; for all other cases just verify
# reason: avoid deploying multiple times to SourceForge
if [ "${TRAVIS_PULL_REQUEST}" = "false" ] && [ "${TRAVIS_BRANCH}" = "develop" ] && [ "${TRAVIS_JDK_VERSION}" = "openjdk8" ]
then
  echo build and deploy
  mvn clean deploy javadoc:javadoc -P sourceforge-release --settings ./target/travis/settings.xml
else
  echo only verify
  mvn clean verify javadoc:javadoc
fi
