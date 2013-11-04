## Process this file with automake to produce Makefile.in.

# Makefile.am for GNU classpathx activation.

JAVAC = @JAVAC@
#JAVACFLAGS = @JAVACFLAGS@
JAVACFLAGS = -g
JAVADOC = javadoc
JAVADOCFLAGS =

src = @srcdir@/source
dst = @srcdir@/classes
doc = @srcdir@/docs

activation_jar = @srcdir@/activation.jar

# Source files.
sources = \
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
$(src)/javax/activation/URLDataSource.java \
$(src)/gnu/activation/viewers/ImageViewer.java \
$(src)/gnu/activation/viewers/TextEditor.java \
$(src)/gnu/activation/viewers/TextViewer.java

# Targets
BUILT_SOURCES = $(activation_jar)

ACTIVATION_CLASSPATH = $(dst):$(src)
META_INF = $(dst)/META-INF
META_INF_FILES = \
$(META_INF)/COPYING \
$(META_INF)/mailcap.default \
$(META_INF)/mimetypes.default

classes = $(patsubst $(src)/%.java, $(dst)/%.class, $(sources))

package_names = javax.activation

# Targets...

# Jar

$(activation_jar): meta-inf activation
	$(JAR) cf $(activation_jar) -C $(dst) META-INF -C $(dst) gnu -C $(dst) javax

meta-inf: $(META_INF) $(META_INF_FILES)

$(META_INF):
	test ! -d $(dst)/META-INF && mkdir -p $(dst)/META-INF

$(META_INF)/COPYING: COPYING
	cp COPYING $(META_INF)/

$(META_INF)/mailcap.default: mailcap.default
	cp mailcap.default $(META_INF)/

$(META_INF)/mimetypes.default: mimetypes.default
	cp mimetypes.default $(META_INF)/

# Compilation

activation: $(classes)

$(classes): $(dst)/%.class: $(src)/%.java
	$(JAVAC) $(JAVACFLAGS) -classpath $(ACTIVATION_CLASSPATH) -d $(dst) $<

# Documentation

javadoc:
	mkdir -p $(doc)
	$(JAVADOC) $(JAVADOCFLAGS) -d $(doc) -sourcepath $(src) \
          -windowtitle "GNU JAF API documentation" $(package_names)

clean-local:
	rm -rf $(META_INF_FILES) $(META_INF) $(doc) $(activation_jar)
	find $(dst) -name '*.class' -exec rm -f '{}' ';'

distclean-local: clean-local
	rm -f config.log config.status

install-data-local:
	@srcdir@/mkinstalldirs $(DESTDIR)/$(datadir)/java
	$(INSTALL_DATA) $(activation_jar) $(DESTDIR)/$(datadir)/java

# End of Makefile.am
