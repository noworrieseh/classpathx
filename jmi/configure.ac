dnl Process this file with autoconf to produce a configure script.

AC_PREREQ(2.59)
AC_INIT(jmi, 1.0, classpathx-discuss@gnu.org)
AM_INIT_AUTOMAKE(1.7.0)
AC_CONFIG_SRCDIR(source)
AC_CONFIG_TESTDIR([tests])
AM_CONFIG_HEADER(config.h)

AM_MAINTAINER_MODE

AC_PROG_CXX
AM_PROG_GCJ
AC_C_CONST

AC_DISABLE_STATIC
AC_PROG_LIBTOOL

AC_CHECK_FUNCS([strdup])

gcj_present=no
PKG_CHECK_MODULES([LIBGCJ],[libgcj >= 3.4],
		  [gcj_present=yes
		   AC_SUBST(LIBGCJ_CFLAGS)
		  ],
		  [gcj_present=no])

AM_CONDITIONAL([BUILD_NATIVE_VIEWER],
               [test "x$gcj_present" = xyes])

AC_CONFIG_FILES([Makefile])
AC_OUTPUT
