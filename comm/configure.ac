# Process this file with autoconf to produce a configure script.

# 
# $Id: configure.ac,v 1.1 2004-03-15 08:17:37 dog Exp $
# Copyright (C) 2004 The Free Software Foundation
# 
# This file is part of GNU CommAPI.
# 
# GNU CommAPI is free software; you can redistribute it and/or modify it
# under the terms of the GNU General Public License as published by the
# Free Software Foundation; either version 2, or (at your option) any
# later version.
#  
# GNU CommAPI is distributed in the hope that it will be useful, but
# WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
# General Public License for more details.
# 
# You should have received a copy of the GNU General Public License
# along with GNU CommAPI; see the file COPYING.  If not, write to the
# Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
# 02111-1307 USA. 
#

AC_INIT(comm, 0.1)
AC_CONFIG_SRCDIR(source/javax/comm/CommPortIdentifier.java)
AM_INIT_AUTOMAKE

PACKAGE="comm"
VERSION="0.1"
AC_SUBST(PACKAGE)
AC_SUBST(VERSION)

AC_PROG_JAVAC
#AC_PROG_JAVADOC
AC_PROG_JAR
AC_CHECK_CLASSPATH

AC_CONFIG_FILES(Makefile)

AC_OUTPUT

