## Process this file with automake to produce Makefile.in.

# Makefile.am for GNU CommAPI.

JAVAC = @JAVAC@
#JAVACFLAGS = @JAVACFLAGS@
JAVACFLAGS = -g
JAVADOC = @JAVADOC@

src = source
dst = classes
doc = docs

# Source files.
sources = \
$(src)/javax/comm/CommDriver.java \
$(src)/javax/comm/CommPortIdentifier.java \
$(src)/javax/comm/CommPort.java \
$(src)/javax/comm/CommPortOwnershipListener.java \
$(src)/javax/comm/NoSuchPortException.java \
$(src)/javax/comm/ParallelPortEvent.java \
$(src)/javax/comm/ParallelPortEventListener.java \
$(src)/javax/comm/ParallelPort.java \
$(src)/javax/comm/PortInUseException.java \
$(src)/javax/comm/SerialPortEvent.java \
$(src)/javax/comm/SerialPortEventListener.java \
$(src)/javax/comm/SerialPort.java \
$(src)/javax/comm/UnsupportedCommOperationException.java

# Targets
BUILT_SOURCES = gnucomm.jar

META_INF = $(dst)/META-INF
META_INF_FILES = $(META_INF)/COPYING

classes = $(patsubst $(src)/%.java, $(dst)/%.class, $(sources))

package_names = javax.comm

# Targets...

# Jar

gnucomm.jar: $(META_INF) $(META_INF_FILES) $(classes)
	$(JAR) cf gnucomm.jar -C $(dst) META-INF -C $(dst) javax

$(META_INF):
	mkdir -p $(META_INF)

$(META_INF)/COPYING: COPYING
	cp COPYING $(META_INF)/

# Compilation

$(classes): $(dst)/%.class: $(src)/%.java
	$(JAVAC) $(JAVACFLAGS) -classpath $(dst):$(src) -d $(dst) $<

# Documentation

javadoc:
	mkdir -p $(doc)
	$(JAVADOC) -d $(doc) -windowtitle "GNU CommAPI documentation" \
		-sourcepath $(src) $(package_names)

clean-local:
	rm -rf $(META_INF_FILES) $(META_INF) $(doc)
	find $(dst) -name '*.class' -exec rm -f '{}' ';'

distclean-local: clean-local
	rm -f config.log config.status

# End of Makefile.am
