# Process this file with autoconf to produce a configure script.

# 
# $Id: configure.ac,v 1.5 2004-05-06 14:30:05 dog Exp $
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

#AC_INIT(libxmlj, 0.1.1)

AC_INIT(source/javax/xml/parsers/SAXParserFactory.java)
AC_CONFIG_SRCDIR(xmlj_util.c)
AC_CONFIG_AUX_DIR(config)

#AM_INIT_AUTOMAKE(libxmlj, 0.1.1)
AM_INIT_AUTOMAKE(jaxp, 1.1)

AC_JAVA_OPTIONS

AC_SUBST(LIBS)
AC_SUBST(CFLAGS)
AC_SUBST(CPPFLAGS)
AC_SUBST(JAVACFLAGS)

AC_CANONICAL_HOST
AC_CHECK_CLASSPATH

# Checks for programs.

AC_PROG_CC
AC_PROG_LIBTOOL
AC_ISC_POSIX
AC_PROG_JAVAC
AC_PROG_JAVAH
AC_PROG_JAR

# Checks for header files.

AM_PATH_XML([2.5.1],[CFLAGS="${CFLAGS}"] [CPPFLAGS="${CPPFLAGS}"] [LIBS="${XML_LIBS} ${LIBS}"] , 
	    AC_MSG_ERROR([unable to locate libxml.]))
AM_PATH_XSLT([1.0.24],[CFLAGS="${XSLT_CFLAGS} ${CFLAGS}"] [CPPFLAGS="${XSLT_CFLAGS} ${CPPFLAGS}"] [LIBS="${XSLT_LIBS} ${LIBS}"] , 
	    AC_MSG_ERROR([unable to locate libxslt.]))

AC_CONFIG_FILES(Makefile)

AC_OUTPUT
