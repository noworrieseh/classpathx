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
	source/nistMCT anubis 288 > tv/anubis-288-mct.txt
	source/nistMCT anubis 320 > tv/anubis-320-mct.txt
	source/cipherSpeed
	source/hashSpeed

