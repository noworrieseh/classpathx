##
## ----------------------------------------------------------------------------
## $Id: Makefile.am,v 1.6 2002-07-06 07:56:16 olf Exp $
##
## Copyright (C) 2002 Free Software Foundation, Inc.
##
## This program is free software; you can redistribute it and/or modify it
## under the terms of the GNU General Public License as published by the Free
## Software Foundation; either version 2 of the License or (at your option) any
## later version.
##
## This program is distributed in the hope that it will be useful, but WITHOUT
## ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
## FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
## more details.
##
## You should have received a copy of the GNU General Public License along with
## this program; see the file COPYING.  If not, write to the
##
##    Free Software Foundation Inc.,
##    59 Temple Place - Suite 330,
##    Boston, MA 02111-1307
##    USA
##
## As a special exception, if you link this library with other files to produce
## an executable, this library does not by itself cause the resulting
## executable to be covered by the GNU General Public License.  This exception
## does not however invalidate any other reasons why the executable file might
## be covered by the GNU General Public License.
## ----------------------------------------------------------------------------
##
## Process this file with automake to produce Makefile.in.

EXTRA_DIST =
SUBDIRS = source

###############################################################################
# test targets
test-vectors:
	@ test -e tv || mkdir tv
	@ test -e tv/nessie || mkdir tv/nessie
	@ test -e tv/nist || mkdir tv/nist
	@ test -e tv/nist/anubis || mkdir tv/nist/anubis
	@ test -e tv/nist/rijndael || mkdir tv/nist/rijndael
	@ test -e tv/nist/serpent || mkdir tv/nist/serpent
	@ test -e tv/nist/twofish || mkdir tv/nist/twofish
	source/nessieCipherTVEmitter anubis 128 > tv/nessie/anubis-128-tv.txt
	source/nessieCipherTVEmitter anubis 160 > tv/nessie/anubis-160-tv.txt
	source/nessieCipherTVEmitter anubis 192 > tv/nessie/anubis-192-tv.txt
	source/nessieCipherTVEmitter anubis 224 > tv/nessie/anubis-224-tv.txt
	source/nessieCipherTVEmitter anubis 256 > tv/nessie/anubis-256-tv.txt
	source/nessieCipherTVEmitter anubis 288 > tv/nessie/anubis-288-tv.txt
	source/nessieCipherTVEmitter anubis 320 > tv/nessie/anubis-320-tv.txt
	source/nessieCipherTVEmitter khazad     > tv/nessie/khazad-tv.txt
	source/nessieHashTVEmitter whirlpool    > tv/nessie/whirlpool-tv.txt
	source/nistKat -k anubis > tv/nist/anubis/ecb_vk.txt
	source/nistKat -t anubis > tv/nist/anubis/ecb_vt.txt
	source/nistKat -k rijndael > tv/nist/rijndael/ecb_vk.txt
	source/nistKat -t rijndael > tv/nist/rijndael/ecb_vt.txt
	source/nistKat -k serpent > tv/nist/serpent/ecb_vk.txt
	source/nistKat -t serpent > tv/nist/serpent/ecb_vt.txt
	source/nistKat -k twofish > tv/nist/twofish/ecb_vk.txt
	source/nistKat -t twofish > tv/nist/twofish/ecb_vt.txt
	source/nistMCT -E -e anubis > tv/nist/anubis/ecb_e_m.txt
	source/nistMCT -E -d anubis > tv/nist/anubis/ecb_d_m.txt
	source/nistMCT -C -e anubis > tv/nist/anubis/cbc_e_m.txt
	source/nistMCT -C -d anubis > tv/nist/anubis/cbc_d_m.txt
	source/nistMCT -E -e rijndael > tv/nist/rijndael/ecb_e_m.txt
	source/nistMCT -E -d rijndael > tv/nist/rijndael/ecb_d_m.txt
	source/nistMCT -C -e rijndael > tv/nist/rijndael/cbc_e_m.txt
	source/nistMCT -C -d rijndael > tv/nist/rijndael/cbc_d_m.txt
	source/nistMCT -E -e serpent > tv/nist/serpent/ecb_e_m.txt
	source/nistMCT -E -d serpent > tv/nist/serpent/ecb_d_m.txt
	source/nistMCT -C -e serpent > tv/nist/serpent/cbc_e_m.txt
	source/nistMCT -C -d serpent > tv/nist/serpent/cbc_d_m.txt
	source/nistMCT -E -e twofish > tv/nist/twofish/ecb_e_m.txt
	source/nistMCT -E -d twofish > tv/nist/twofish/ecb_d_m.txt
	source/nistMCT -C -e twofish > tv/nist/twofish/cbc_e_m.txt
	source/nistMCT -C -d twofish > tv/nist/twofish/cbc_d_m.txt
	source/cipherSpeed
	source/hashSpeed

###############################################################################
# Remove internal automake dependencies.
#
$(srcdir)/Makefile.in:

Makefile:

$(top_builddir)/config.status:

$(srcdir)/configure:

$(ACLOCAL_M4):

