# aclocal.m4 generated automatically by aclocal 1.6.1 -*- Autoconf -*-

# Copyright 1996, 1997, 1998, 1999, 2000, 2001, 2002
# Free Software Foundation, Inc.
# This file is free software; the Free Software Foundation
# gives unlimited permission to copy and/or distribute it,
# with or without modifications, as long as this notice is preserved.

# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY, to the extent permitted by law; without
# even the implied warranty of MERCHANTABILITY or FITNESS FOR A
# PARTICULAR PURPOSE.

# AC_PROG_JAVA_CC([COMPILER ...])
# --------------------------
# COMPILER ... is a space separated list of java compilers to search for.
# This just gives the user an opportunity to specify an alternative
# search list for the java compiler.
AC_DEFUN([AC_PROG_JAVA_CC],
[AC_ARG_VAR([JAVA_CC],                [java compiler command])dnl
AC_ARG_VAR([JAVA_CC_FLAGS],           [java compiler flags])dnl
m4_ifval([$1],
      [AC_CHECK_TOOLS(JAVA_CC, [$1])],
[AC_CHECK_TOOL(JAVA_CC, gcj)
if test -z "$JAVA_CC"; then
  AC_CHECK_TOOL(JAVA_CC, javac)
fi
if test -z "$JAVA_CC"; then
  AC_CHECK_TOOL(JAVA_CC, jikes)
fi
])

if test "$JAVA_CC" = "gcj"; then
   if test "$GCJ_OPTS" = ""; then
      AC_SUBST(GCJ_OPTS,-C)
   fi
   AC_SUBST(JAVA_CC_OPTS, @GCJ_OPTS@,
        [Define the compilation options for GCJ])
fi
test -z "$JAVA_CC" && AC_MSG_ERROR([no acceptable java compiler found in
\$PATH])
])# AC_PROG_JAVA_CC

# AC_PROG_JAR()
AC_DEFUN([AC_PROG_JAR],[
AC_REQUIRE([AC_EXEEXT])dnl
if test "x$JAVAPREFIX" = x; then
        test "x$JAR" = x && AC_CHECK_PROGS(JAR, jar$EXEEXT)
else
        test "x$JAR" = x && AC_CHECK_PROGS(JAR, jar, $JAVAPREFIX)
fi
test "x$JAR" = x && AC_MSG_ERROR([no acceptable jar program found in
\$PATH])
AC_PROVIDE([$0])dnl
])

