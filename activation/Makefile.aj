# Activation build makefile.
# (C) Tapsell-Ferrier Limited 2002

# Variables which define some usefull constants.
# These are actually required by automakejar.
empty:=
space:=$(empty) $(empty)


# Set by configure.
PROJECTROOT = @srcdir@
JAVAC = @JAVA_CC@
JAVAC_OPTS = @JAVA_CC_OPTS@
JAR = @JAR_TOOL@


# The default is to just build the jar file.
all: activation.jar


# Ensure the makefile can update itself.
Makefile: Makefile.in configure.in
	   $(SHELL) ./config.status

Makefile.in: Makefile.aj
	     $(SHELL) $(PROJECTROOT)/automakejar ./Makefile.in


# Build the support files.
META-INF:
	mkdir $@

META-INF/mailcap.default: META-INF $(PROJECTROOT)/mailcap.default
	cp $(PROJECTROOT)/mailcap.default $@

META-INF/mimetypes.default: META-INF $(PROJECTROOT)/mimetypes.default
	cp $(PROJECTROOT)/mimetypes.default $@

# Admin file targets.
META-INF/COPYING: META-INF COPYING
	cp $(PROJECTROOT)/COPYING $@

SUPPORTFILES = 	META-INF/COPYING \
		META-INF/mailcap.default \
		META-INF/mimetypes.default


# This is an automakejar target.
activation.jar:
	sourcedir=$(PROJECTROOT)/source
	sourcefiles=`find $sourcedir -name "*.java" -print`
	classpath=$(wildcard lib/*.jar)
	classesdest=$(PROJECTROOT)/classes
	otherfiles=$(SUPPORTFILES)


# Ensure everything generated is cleaned.
# We could probably do with automake for this.
clean:
	-rm -rf activation.jar $(PROJECTROOT)/classes META-INF filelist

distclean: clean
	-rm -rf Makefile Makefile.in config.*

mrproper: distclean
	-rm -rf configure  


# End Makefile.
