# 
# $Id: Makefile.am,v 1.2 2003-02-27 13:00:37 julian Exp $
# Copyright (C) 2003 Julian Scheid
# 
# This file is part of GNU LibxmlJ, a JAXP-compliant Java wrapper for
# the XML and XSLT C libraries for Gnome (libxml2/libxslt).
# 
# GNU LibxmlJ is free software; you can redistribute it and/or modify it
# under the terms of the GNU General Public License as published by the
# Free Software Foundation; either version 2, or (at your option) any
# later version.
#  
# GNU LibxmlJ is distributed in the hope that it will be useful, but
# WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
# General Public License for more details.
# 
# You should have received a copy of the GNU General Public License
# along with GNU LibxmlJ; see the file COPYING.  If not, write to the
# Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
# 02111-1307 USA. 
#

lib_LTLIBRARIES = libxmlj.la

libxmlj_la_DEPENDENCIES = \
	javax_xml_transform_OutputKeys.h \
	gnu_xml_libxmlj_transform_LibxsltStylesheet.h \
	gnu_xml_libxmlj_transform_TransformerFactoryImpl.h \
	gnu_xml_libxmlj_transform_JavaContext.h

libxmlj_la_SOURCES = src/libxsltj.c src/xmlj_io.c src/xmlj_error.c
libxmlj_la_LDFLAGS = -release 0.1.1
libxmlj_la_CFLAGS = -pedantic -Wall -fbounds-check

JARFILE = libxmlj.jar
data_DATA = ${JARFILE}

BUILT_SOURCES = ${libxmlj_la_DEPENDENCIES} ${JARFILE}

# Generate header files for classes containing native code.

gnu_xml_libxmlj_transform_%.h: \
		gnu/xml/libxmlj/transform/%.class
	@JAVAH@ -force -classpath . gnu.xml.libxmlj.transform.$*

javax_xml_transform_%.h:
	@JAVAH@ -force javax.xml.transform.$*

# Rule for compiling Java source files

gnu/%.class: @srcdir@/src/gnu/%.java
	echo "Compiling $*.java..."
	@JAVAC@ @JAVACFLAGS@ \
		-d . \
		-classpath "${CLASSPATH}:@srcdir@/src:." \
		@srcdir@/src/gnu/xml/libxmlj/transform/*.java \
		@srcdir@/src/gnu/xml/libxmlj/transform/LibxsltStylesheet.java

# Assemble Libxmlj Jar file

${JARFILE}: \
		gnu/xml/libxmlj/transform/TransformerFactoryImpl.class \
		gnu/xml/libxmlj/transform/TransformerImpl.class \
		gnu/xml/libxmlj/transform/DefaultErrorListenerImpl.class \
		gnu/xml/libxmlj/transform/DefaultURIResolverImpl.class \
		gnu/xml/libxmlj/transform/JavaContext.class \
		gnu/xml/libxmlj/transform/IOToolkit.class \
		gnu/xml/libxmlj/transform/SourceLocatorImpl.class \
		gnu/xml/libxmlj/transform/TemplatesImpl.class \
		gnu/xml/libxmlj/transform/SourceWrapper.class \
		gnu/xml/libxmlj/transform/LibxsltStylesheet.class 
	@JAR@ cf ${JARFILE} $^

# Remove classes and jar file

clean-local:
	rm -rf gnu
	rm -f ${JARFILE}

# Run a simple transformation to check that the package is working

check-local: \
		gnu/xml/libxmlj/transform/TransformTest.class
	@JAVA@  -classpath "${CLASSPATH}:${JARFILE}:." \
		-Djava.library.path=.libs \
		gnu.xml.libxmlj.transform.TransformTest \
		@srcdir@/testdocs/test.xml \
		@srcdir@/testdocs/testsheet.xsl \
		transformed.xml
	diff @srcdir@/testdocs/transformed.xml transformed.xml

dist-hook:
	mkdir $(distdir)/src/gnu
	cp -r $(srcdir)/src/gnu/* $(distdir)/src/gnu
	mkdir $(distdir)/testdocs
	cp $(srcdir)/testdocs/* $(distdir)/testdocs
