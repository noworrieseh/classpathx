#!/bin/sh

case $1 in

  # Reconfigure the tool chain. This is needed each time configure.in
  # or the various Makefile.am are updated.
  conf )
    echo 'aclocal && automake && autoconf'
    aclocal && automake && autoconf
    ;;

  # Build the sym links. Automake requires a numbers of files to be
  # present in all projects. We just sym-link to the files in crypto
  # root directory.
  links )
    cd source
    test -e gnu || ln -s ../../source/gnu gnu
    cd ..
    test -e NEWS || ln -s ../NEWS NEWS
    test -e README || ln -s ../README README
    test -e COPYING || ln -s ../COPYING COPYING
    test -e AUTHORS || ln -s ../AUTHORS AUTHORS
    test -e ChangeLog || ln -s ../ChangeLog ChangeLog
    chmod +x admin.sh config.guess config.sub configure depcomp install-sh ltconfig missing mkinstalldirs
    ;;

  # Clean all temporary files. This target should be called before a
  # CVS check-in.
  clean )
    rm -f NEWS README COPYING AUTHORS ChangeLog *~
    rm -f source/gnu source/*~
    rm -fr autom4te.cache
    ;;

  # Clean all generated files, including configure and Makefile.in makefiles.
  # Usefull for maintainers.
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