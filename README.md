# dslink-java-v2-example

* Version: 0.0.0
* Java - version 1.6 and up.
* [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0)


## Overview

This is a simple example link that can used to create the boilerplate for a new Java based
DSLink.

If you are not familiar with DSA, an overview can be found at
[here](http://iot-dsa.org/get-started/how-dsa-works).

This link was built using the Java DSLink SDK which can be found
[here](https://github.com/iot-dsa-v2/sdk-dslink-java).


## Creating a New Link

To create a new link by using this link as the source material, you will need to do the following:

1. Modify build.gradle
    - group - Your organization's identifier.
    - version - The current version of the link.  Don't forget to update this!
2. Modify LICENSE
    - At the very least, change the copyright holder.
3. Modify dslink.json
    - Change name, version, and description.
    - Change main, this is the path to the shell script used to launch the link.
    - Change the value of the config named "rootType". This must be the fully qualified class name 
      of your root node.
4. Edit this README
    - Please maintain a helpful readme.
    - Change the title.
    - Maintain a version number.
    - Change the license if necessary.
    - Provide an overview of the link's purpose.  Keep the text linking to the DSA overview
      and core SDK for context.
    - Remote this section (Creating a New Link).
    - Update the Link Architecture.
    - Update the Node Guide.
    - Acknowledge any 3rd party libraries you use.
    - Maintain a version history.
5. Edit the code
    - Change the package and directories to match your organization.
    - Change the root node class name.


## Link Architecture

This section outlines the hierarchy of nodes defined by this link.

- _ExampleRoot_ - The root node of the link.
  - _ExampleChild_ - There is no child, this is just a documentation example.


## Node Guide

The following section provide a detail description of each node in the link as well as
descriptions of key values and actions.


### ExampleRoot

This is the root node of the link.  It has a counter that is automatically updated
whenever the node is subscribed.  It also has a simple action to reset the counter.

**Values**
- Counter - Automatically updates whenever the node is subscribed.

**Child Nodes**
- There are not child nodes.

**Actions**
- Reset - Resets the counter to 0.


## Acknowledgements

SDK-DSLINK-JAVA

This software contains unmodified binary redistributions of 
[sdk-dslink-java](https://github.com/iot-dsa-v2/sdk-dslink-java), which is licensed 
and available under the Apache License 2.0. An original copy of the license agreement can be found 
at https://github.com/iot-dsa-v2/sdk-dslink-java/blob/master/LICENSE

## History

* Version 0.0.0
  - Hello World

