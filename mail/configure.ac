# Process this file with autoconf to produce a configure script.

# 
# $Id: configure.ac,v 1.1 2003-08-12 15:19:04 nferrier Exp $
# Copyright (C) 2003 Nic Ferrier
# 
# This file is part of GNU Paperclips, a free servlet container.
# 
# GNU Paperclips is free software; you can redistribute it and/or modify it
# under the terms of the GNU General Public License as published by the
# Free Software Foundation; either version 2, or (at your option) any
# later version.
#  
# GNU Paperclips is distributed in the hope that it will be useful, but
# WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
# General Public License for more details.
# 
# You should have received a copy of the GNU General Public License
# along with GNU Paperclips; see the file COPYING.  If not, write to the
# Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
# 02111-1307 USA. 
#

AC_INIT(gnumail, 0.1)
AC_CONFIG_SRCDIR(source/gnu/mail/providers/ReadOnlyMessage.java)
AC_CONFIG_AUX_DIR(config)

AM_INIT_AUTOMAKE(gnumail, 0.1)

AC_JAVA_OPTIONS

AC_SUBST(LIBS)
AC_SUBST(CFLAGS)
AC_SUBST(CPPFLAGS)
AC_SUBST(JAVACFLAGS)

AC_CANONICAL_HOST
AC_CHECK_CLASSPATH

# Checks for programs.

AC_PROG_CC
#AC_PROG_LIBTOOL
AC_ISC_POSIX
AC_PROG_JAVAC
AC_PROG_JAVAH
AC_PROG_JAR


AC_STDC_HEADERS

AC_CONFIG_FILES(Makefile)

AC_OUTPUT
