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


# Ensure everything generated is cleaned.
# We could probably do with automake for this.
clean:
	rm activation.jar ; rm -r $(PROJECTROOT)/classes



# This is an automakejar target.
activation.jar:
	sourcedir=$(PROJECTROOT)/source
	sourcefiles=`find $sourcedir -name "*.java" -print`
	classpath=$(wildcard lib/*.jar)
	classesdest=$(PROJECTROOT)/classes
	otherfiles=META-INF/mailcap.default META-INF/mimetypes.default

# End Makefile.
