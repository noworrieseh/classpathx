##
## ----------------------------------------------------------------------------
## $Id: Makefile.am,v 1.4 2002-06-15 07:32:25 olf Exp $
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

AUTOMAKE_OPTIONS = gnu
EXTRA_DIST =
SUBDIRS = source

###############################################################################
# test targets
test-vectors:
	test -e tv || mkdir tv
	source/nessieCipherTVEmitter anubis 128 > tv/anubis-128-tv.txt
	source/nessieCipherTVEmitter anubis 160 > tv/anubis-160-tv.txt
	source/nessieCipherTVEmitter anubis 192 > tv/anubis-192-tv.txt
	source/nessieCipherTVEmitter anubis 224 > tv/anubis-224-tv.txt
	source/nessieCipherTVEmitter anubis 256 > tv/anubis-256-tv.txt
	source/nessieCipherTVEmitter anubis 288 > tv/anubis-288-tv.txt
	source/nessieCipherTVEmitter anubis 320 > tv/anubis-320-tv.txt
	source/nessieCipherTVEmitter khazad     > tv/khazad-tv.txt
	source/nessieHashTVEmitter whirlpool    > tv/whirlpool-tv.txt
	source/nistKat khazad 128 > tv/khazad-ecb-128-kat.txt
	source/nistKat anubis 128 > tv/anubis-ecb-128-kat.txt
	source/nistKat anubis 160 > tv/anubis-ecb-160-kat.txt
	source/nistKat anubis 192 > tv/anubis-ecb-192-kat.txt
	source/nistKat anubis 224 > tv/anubis-ecb-224-kat.txt
	source/nistKat anubis 256 > tv/anubis-ecb-256-kat.txt
	source/nistKat anubis 288 > tv/anubis-ecb-288-kat.txt
	source/nistKat anubis 320 > tv/anubis-ecb-320-kat.txt
	source/nistMCT khazad 128 > tv/khazad-128-mct.txt
	source/nistMCT anubis 128 > tv/anubis-128-mct.txt
	source/nistMCT anubis 160 > tv/anubis-160-mct.txt
	source/nistMCT anubis 192 > tv/anubis-192-mct.txt
	source/nistMCT anubis 224 > tv/anubis-224-mct.txt
	source/nistMCT anubis 256 > tv/anubis-256-mct.txt
	source/cipherSpeed
	source/hashSpeed

