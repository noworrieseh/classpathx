# Process this file with autoconf to produce a configure script.

# 
# $Id: configure.ac,v 1.1.1.1 2003-02-27 01:22:15 julian Exp $
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

AC_INIT(libxmlj, 0.1.1)
AC_CONFIG_SRCDIR(src/libxsltj.c)
AC_CONFIG_AUX_DIR(config)

AM_INIT_AUTOMAKE(libxmlj, 0.1.1)

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

AC_STDC_HEADERS
AC_CHECK_CLASS([javax.xml.transform.Transformer])
if test "$HAVE_LAST_CLASS" = "no"; then
	echo "*** You don't seem to have JAXP installed."
	echo "*** Please make sure that gnujaxp.jar is listed on your"
	echo "*** CLASSPATH environment variable. The current contents"
	echo "*** of your CLASSPATH variable is:"
	echo "$CLASSPATH"
        AC_MSG_ERROR([unable to locate class javax.xml.transform.Transformer.])
fi
AC_CHECK_CLASS_COMPILATION([javax.xml.transform.Transformer])
if test "$HAVE_LAST_CLASS" = "no"; then
	echo "*** You have JAXP installed, but it cannot be found by"
	echo "*** the chosen Java compiler, $JAVAC."
	echo "*** This means probably that you are using gcj and do not have"
	echo "*** GNU JAXP installed."
	echo "*** Please make sure that gnujaxp.jar is listed on your"
	echo "*** CLASSPATH environment variable. The current contents"
	echo "*** of your CLASSPATH variable is:"
	echo "$CLASSPATH"
	echo "*** You can also force use of a different compiler using --with-javac"
        AC_MSG_ERROR([unable to locate class javax.xml.transform.Transformer at compile time.])
fi

AC_CONFIG_FILES(Makefile)

AC_OUTPUT
