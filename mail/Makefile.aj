# JavaMail build makefile.

# Variables which define some useful constants.
# These are required by automakejar.
empty:=
space:=$(empty) $(empty)

# Set by configure.
PROJECTROOT = @srcdir@
JAVAC = @JAVA_CC@
JAVAC_OPTS = @JAVA_CC_OPTS@
JAR = @JAR@

# The default is to just build the jar file.
all: mail.jar

# Ensure the makefile can update itself.
Makefile: Makefile.in configure.in
	$(SHELL) ./config.status

Makefile.in: Makefile.aj
	$(SHELL) $(PROJECTROOT)/automakejar ./Makefile.in

# Build the support files.
META-INF:
	mkdir $@

# TODO generate these correctly from the .in files using configure options
META-INF/javamail.default.address.map: META-INF $(PROJECTROOT)/javamail.address.map.in
	cp $(PROJECTROOT)/javamail.address.map.in $@

META-INF/javamail.default.providers: META-INF $(PROJECTROOT)/javamail.providers.in
	cp $(PROJECTROOT)/javamail.providers.in $@

META-INF/mime.types: META-INF $(PROJECTROOT)/mime.types
	cp $(PROJECTROOT)/mime.types $@

META-INF/mailcap: META-INF $(PROJECTROOT)/mailcap
	cp $(PROJECTROOT)/mailcap $@

# Admin file targets.
META-INF/COPYING: META-INF COPYING
	cp $(PROJECTROOT)/COPYING $@

SUPPORTFILES = \
	META-INF/javamail.default.address.map \
	META-INF/javamail.default.providers \
	META-INF/mailcap \
	META-INF/mime.types \
	META-INF/COPYING

# This is an automakejar target.
mail.jar:
	sourcedir=$(PROJECTROOT)/source
	sourcefiles=`find $sourcedir -name "*.java" -print`
	classpath=$(wildcard lib/*.jar)
	classesdest=$(PROJECTROOT)/classes
	otherfiles=$(SUPPORTFILES)

# Ensure everything generated is cleaned.
# We could probably do with automake for this.
clean:
	-rm -rf mail.jar $(PROJECTROOT)/classes META-INF filelist

distclean: clean
	-rm -rf Makefile Makefile.in config.*

mrproper: distclean
	-rm -rf configure  

# End Makefile.
