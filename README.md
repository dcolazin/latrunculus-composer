# Latrunculus Composer

## Info

Latrunculus Composer is a music software based on the concepts and models of mathematical music theory, forked from Rubato Composer.  
Visit the [Rubato website](http://rubato.org) and [Guerino Mazzola website](http://www.encyclospace.org) for more information.

## Install

Currently, no releases are available and Latrunculus must be build from source. The build produces a multiplatform jar.

## Build

Clone the project, move to the folder, then install the dependencies to the local maven repository and run maven
```bash
$ sh install_libraries.sh
$ mvn package
```
which generates the latest `jar` file in the target folder. Moreover, it is possible to generate the javadocs with the command

```bash
$ mvn javadoc:javadoc
```

## 0.1.0 roadmap
* [x] Build with Maven
* [x] Update to Java 8
* [ ] Be ready to be able to update to Java 11 when needed (and consider updating)
* [ ] Separation of the server "core" components and of the client "gui" components
  * [ ] latrunculus-core (math library)
  * [ ] latrunculus-server
  * [ ] latrunculus-swing-client
* [ ] Move the BigBang rubette to a different jar, as many dependencies are required for this rubette and not for the core composer?
* [ ] Handle all the dependencies in the pom, without having a separate script to install the libraries
* [ ] Review the rubette loading
* [ ] Test coverage >70% and review current tests
* [ ] OSC support
* [ ] JSON serialization
* [ ] Linting and improve maintainability
* [ ] SLF4J
* [ ] Review i18n
* [ ] Move package org.rubato to org.vetronauta.latrunculus
* [ ] Initial splash art

## List of known Rubato bugs

* **Rubato Composer GUI:** Sometimes dialog windows may become unresponsive to keyboard input. A slight resize of the window will normally make it responsive again. This seems happen on JDK 1.6.0.
* **Rubato Composer GUI, ScorePlayRubette:** There is a delay between the current position indicator and the playing.
