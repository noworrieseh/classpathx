## Process this file with automake to produce Makefile.in.

# Makefile.am for GNU classpathx activation.

JAVAC = @JAVAC@
#JAVACFLAGS = @JAVACFLAGS@
JAVACFLAGS = -g

src = source
dst = classes

# Source files.
sources = \
$(src)/gnu/activation/MagicFileTypeMap.java \
$(src)/gnu/activation/MIMETypeParser.java \
$(src)/javax/activation/ActivationDataFlavor.java \
$(src)/javax/activation/CommandInfo.java \
$(src)/javax/activation/CommandMap.java \
$(src)/javax/activation/CommandObject.java \
$(src)/javax/activation/DataContentHandler.java \
$(src)/javax/activation/DataContentHandlerFactory.java \
$(src)/javax/activation/DataHandler.java \
$(src)/javax/activation/DataHandlerDataSource.java \
$(src)/javax/activation/DataSource.java \
$(src)/javax/activation/DataSourceDataContentHandler.java \
$(src)/javax/activation/FileDataSource.java \
$(src)/javax/activation/FileTypeMap.java \
$(src)/javax/activation/MailcapCommandMap.java \
$(src)/javax/activation/MimeType.java \
$(src)/javax/activation/MimeTypeParameterList.java \
$(src)/javax/activation/MimeTypeParseException.java \
$(src)/javax/activation/MimetypesFileTypeMap.java \
$(src)/javax/activation/ObjectDataContentHandler.java \
$(src)/javax/activation/UnsupportedDataTypeException.java \
$(src)/javax/activation/URLDataSource.java

# Targets
BUILT_SOURCES = activation.jar

ACTIVATION_CLASSPATH = $(dst):$(src)
META_INF = $(dst)/META-INF
META_INF_FILES = $(META_INF)/COPYING

classes = $(patsubst $(src)/%.java, $(dst)/%.class, $(sources))

# Targets...

# Jar

activation.jar: meta-inf activation
	$(JAR) cf activation.jar -C $(dst) META-INF -C $(dst) gnu

meta-inf: $(META_INF_FILES)

$(META_INF)/COPYING: COPYING
	mkdir -p $(META_INF)
	cp COPYING $(META_INF)/

# Compilation

activation: $(classes)

$(classes): $(dst)/%.class: $(src)/%.java
	$(JAVAC) $(JAVACFLAGS) -classpath $(ACTIVATION_CLASSPATH) -d $(dst) $<

clean-local:
	rm $(META_INF_FILES)
	rmdir $(META_INF)
	find $(dst) -name '*.class' -exec rm -f '{}' ';'

distclean-local: clean-local
	rm -f Makefile config.log config.status

# End of Makefile.am
