dnl Process this file with autoconf to produce a configure script.

dnl 
dnl Copyright (C) 2003 Julian Scheid
dnl 
dnl This file is part of GNU LibxmlJ, a JAXP-compliant Java wrapper for
dnl the XML and XSLT C libraries for Gnome (libxml2/libxslt).
dnl 
dnl GNU LibxmlJ is free software; you can redistribute it and/or modify it
dnl under the terms of the GNU General Public License as published by the
dnl Free Software Foundation; either version 2, or (at your option) any
dnl later version.
dnl  
dnl GNU LibxmlJ is distributed in the hope that it will be useful, but
dnl WITHOUT ANY WARRANTY; without even the implied warranty of
dnl MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
dnl General Public License for more details.
dnl 
dnl You should have received a copy of the GNU General Public License
dnl along with GNU LibxmlJ; see the file COPYING.  If not, write to the
dnl Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
dnl 02111-1307 USA. 
dnl

AC_INIT([jaxp], [1.1], [jaxp])
AC_CONFIG_SRCDIR([source])
AC_CONFIG_AUX_DIR([config])

AM_INIT_AUTOMAKE

AC_JAVA_OPTIONS

AC_SUBST(LIBS)
AC_SUBST(CFLAGS)
AC_SUBST(CPPFLAGS)
AC_SUBST(JAVACFLAGS)
AC_SUBST(SOFLAGS)

AC_CANONICAL_HOST
AC_CHECK_CLASSPATH

AC_SUBST(XMLJ_VERSION_MAJOR)
XMLJ_VERSION_MAJOR="0"
AC_SUBST(XMLJ_VERSION_MINOR)
XMLJ_VERSION_MINOR="99"

dnl Checks for header files.

AM_PATH_XML([2.6.8],[CFLAGS="${CFLAGS}"] [CPPFLAGS="${CPPFLAGS}"] [LIBS="${XML_LIBS} ${LIBS}"] , 
	    AC_MSG_ERROR([unable to locate libxml.]))
AM_PATH_XSLT([1.1.11],[CFLAGS="${XSLT_CFLAGS} ${CFLAGS}"] [CPPFLAGS="${XSLT_CFLAGS} ${CPPFLAGS}"] [LIBS="${XSLT_LIBS} ${LIBS}"] , 
	    AC_MSG_ERROR([unable to locate libxslt.]))

# Libtool configuration

SOFLAGS="-rpath \$(libdir)"
LIBTOOL_PROG="${SHELL} ./libtool"

dnl Checks for programs.

AC_PROG_CC
AC_PROG_LD
AC_ISC_POSIX
dnl FIXME:  need to offer choice between traditional javac and gcj
dnl AM_PROG_JAVAC
AM_PROG_GCJ
AC_DISABLE_STATIC
AC_PROG_LIBTOOL
AC_PROG_JAVAC
AC_PROG_JAVAH
AC_PROG_JAR

# Set SOSUFFIX and friends

SOSUFFIX_CONFIG
MODSUFFIX_CONFIG
JMODSUFFIX_CONFIG

AC_OUTPUT([Makefile])
