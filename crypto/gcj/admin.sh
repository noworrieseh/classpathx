#!/bin/sh

case $1 in
  conf )
    echo 'aclocal && automake && autoconf'
    aclocal && automake && autoconf
    ;;

  links )
    cd source
    test -e gnu || ln -s ../../source/gnu gnu
    cd ..
    test -e NEWS || ln -s ../NEWS NEWS
    test -e README || ln -s ../README README
    test -e COPYING || ln -s ../COPYING COPYING
    test -e AUTHORS || ln -s ../AUTHORS AUTHORS
    test -e ChangeLog || ln -s ../ChangeLog ChangeLog
    ;;

  clean )
    rm -f NEWS README COPYING AUTHORS ChangeLog *~
    rm -f source/gnu source/*~
    rm -fr autom4te.cache
    ;;

  cleanall )
    rm -f NEWS README COPYING AUTHORS ChangeLog *~
    rm -f source/gnu source/*~
    rm -fr autom4te.cache
    rm -f configure aclocal.m4 Makefile.in source/Makefile.in
    ;;

  * )
    echo 'choose one of the targets: conf, links, clean or cleanall'
    ;;
esac