# Makefile.am for GNU Classpathx Mail.

# The java source files that make up the GNU version of the javax.mail API.
API_JAVASRC = \
source/javax/mail/Address.java \
source/javax/mail/AuthenticationFailedException.java \
source/javax/mail/Authenticator.java \
source/javax/mail/BodyPart.java \
source/javax/mail/FetchProfile.java \
source/javax/mail/Flags.java \
source/javax/mail/Folder.java \
source/javax/mail/FolderClosedException.java \
source/javax/mail/FolderNotFoundException.java \
source/javax/mail/Header.java \
source/javax/mail/IllegalWriteException.java \
source/javax/mail/Message.java \
source/javax/mail/MessageAware.java \
source/javax/mail/MessageContext.java \
source/javax/mail/MessageRemovedException.java \
source/javax/mail/MessagingException.java \
source/javax/mail/MethodNotSupportedException.java \
source/javax/mail/Multipart.java \
source/javax/mail/MultipartDataSource.java \
source/javax/mail/NoSuchProviderException.java \
source/javax/mail/Part.java \
source/javax/mail/PasswordAuthentication.java \
source/javax/mail/Provider.java \
source/javax/mail/ReadOnlyFolderException.java \
source/javax/mail/SendFailedException.java \
source/javax/mail/Service.java \
source/javax/mail/Session.java \
source/javax/mail/Store.java \
source/javax/mail/StoreClosedException.java \
source/javax/mail/Transport.java \
source/javax/mail/UIDFolder.java \
source/javax/mail/URLName.java \
source/javax/mail/event/ConnectionAdapter.java \
source/javax/mail/event/ConnectionEvent.java \
source/javax/mail/event/ConnectionListener.java \
source/javax/mail/event/FolderAdapter.java \
source/javax/mail/event/FolderEvent.java \
source/javax/mail/event/FolderListener.java \
source/javax/mail/event/MailEvent.java \
source/javax/mail/event/MessageChangedEvent.java \
source/javax/mail/event/MessageChangedListener.java \
source/javax/mail/event/MessageCountAdapter.java \
source/javax/mail/event/MessageCountEvent.java \
source/javax/mail/event/MessageCountListener.java \
source/javax/mail/event/StoreEvent.java \
source/javax/mail/event/StoreListener.java \
source/javax/mail/event/TransportAdapter.java \
source/javax/mail/event/TransportEvent.java \
source/javax/mail/event/TransportListener.java \
source/javax/mail/internet/AddressException.java \
source/javax/mail/internet/ContentDisposition.java \
source/javax/mail/internet/ContentType.java \
source/javax/mail/internet/HeaderTokenizer.java \
source/javax/mail/internet/InternetAddress.java \
source/javax/mail/internet/InternetHeaders.java \
source/javax/mail/internet/MailDateFormat.java \
source/javax/mail/internet/MimeBodyPart.java \
source/javax/mail/internet/MimeMessage.java \
source/javax/mail/internet/MimeMultipart.java \
source/javax/mail/internet/MimePart.java \
source/javax/mail/internet/MimePartDataSource.java \
source/javax/mail/internet/MimeUtility.java \
source/javax/mail/internet/NewsAddress.java \
source/javax/mail/internet/ParameterList.java \
source/javax/mail/internet/ParseException.java \
source/javax/mail/internet/SharedInputStream.java \
source/javax/mail/search/AddressStringTerm.java \
source/javax/mail/search/AddressTerm.java \
source/javax/mail/search/AndTerm.java \
source/javax/mail/search/BodyTerm.java \
source/javax/mail/search/ComparisonTerm.java \
source/javax/mail/search/DateTerm.java \
source/javax/mail/search/FlagTerm.java \
source/javax/mail/search/FromStringTerm.java \
source/javax/mail/search/FromTerm.java \
source/javax/mail/search/HeaderTerm.java \
source/javax/mail/search/IntegerComparisonTerm.java \
source/javax/mail/search/MessageIDTerm.java \
source/javax/mail/search/MessageNumberTerm.java \
source/javax/mail/search/NotTerm.java \
source/javax/mail/search/OrTerm.java \
source/javax/mail/search/ReceivedDateTerm.java \
source/javax/mail/search/RecipientStringTerm.java \
source/javax/mail/search/RecipientTerm.java \
source/javax/mail/search/SearchException.java \
source/javax/mail/search/SearchTerm.java \
source/javax/mail/search/SentDateTerm.java \
source/javax/mail/search/SizeTerm.java \
source/javax/mail/search/StringTerm.java \
source/javax/mail/search/SubjectTerm.java

# Utility and helper classes for the provders.
GNU_SRC = \
source/gnu/mail/treeutil/StatusEvent.java \
source/gnu/mail/treeutil/StatusListener.java \
source/gnu/mail/treeutil/StatusSource.java \
source/gnu/mail/handler/Application.java \
source/gnu/mail/handler/ApplicationOctetStream.java \
source/gnu/mail/handler/Message.java \
source/gnu/mail/handler/MessageRFC822.java \
source/gnu/mail/handler/Multipart.java \
source/gnu/mail/handler/MultipartAlternative.java \
source/gnu/mail/handler/MultipartMixed.java \
source/gnu/mail/handler/MultipartRelated.java \
source/gnu/mail/handler/MultipartSigned.java \
source/gnu/mail/handler/Text.java \
source/gnu/mail/handler/TextHtml.java \
source/gnu/mail/handler/TextPlain.java \
source/gnu/mail/handler/TextXml.java \
source/gnu/mail/util/BASE64.java \
source/gnu/mail/util/BOutputStream.java \
source/gnu/mail/util/Base64InputStream.java \
source/gnu/mail/util/Base64OutputStream.java \
source/gnu/mail/util/CRLFInputStream.java \
source/gnu/mail/util/CRLFOutputStream.java \
source/gnu/mail/util/LineInputStream.java \
source/gnu/mail/util/Logger.java \
source/gnu/mail/util/MessageInputStream.java \
source/gnu/mail/util/MessageOutputStream.java \
source/gnu/mail/util/QInputStream.java \
source/gnu/mail/util/QOutputStream.java \
source/gnu/mail/util/QPInputStream.java \
source/gnu/mail/util/QPOutputStream.java \
source/gnu/mail/util/RFC2822OutputStream.java \
source/gnu/mail/util/StderrLogger.java \
source/gnu/mail/util/UUDecoderStream.java \
source/gnu/mail/util/UUEncoderStream.java \
source/gnu/mail/providers/ReadOnlyMessage.java

# The SMTP provider
SMTP_JAVASRC = \
source/gnu/mail/providers/smtp/Parameter.java \
source/gnu/mail/providers/smtp/ParameterList.java \
source/gnu/mail/providers/smtp/SMTPConnection.java \
source/gnu/mail/providers/smtp/SMTPOutputStream.java \
source/gnu/mail/providers/smtp/SMTPTransport.java

# The IMAP provider
IMAP_JAVASRC = \
source/gnu/mail/providers/imap/IMAPBodyPart.java \
source/gnu/mail/providers/imap/IMAPCommand.java \
source/gnu/mail/providers/imap/IMAPConnection.java \
source/gnu/mail/providers/imap/IMAPConstants.java \
source/gnu/mail/providers/imap/IMAPException.java \
source/gnu/mail/providers/imap/IMAPFolder.java \
source/gnu/mail/providers/imap/IMAPMessage.java \
source/gnu/mail/providers/imap/IMAPMultipart.java \
source/gnu/mail/providers/imap/IMAPMultipartDataSource.java \
source/gnu/mail/providers/imap/IMAPResponse.java \
source/gnu/mail/providers/imap/IMAPResponseTokenizer.java \
source/gnu/mail/providers/imap/IMAPStore.java \
source/gnu/mail/providers/imap/IMAPTest.java \
source/gnu/mail/providers/imap/ListEntry.java \
source/gnu/mail/providers/imap/MailboxStatus.java \
source/gnu/mail/providers/imap/MessageStatus.java \
source/gnu/mail/providers/imap/Pair.java \
source/gnu/mail/providers/imap/UTF7imap.java

# POP3 protocol
POP3_JAVASRC = \
source/gnu/mail/providers/pop3/POP3Connection.java \
source/gnu/mail/providers/pop3/POP3Folder.java \
source/gnu/mail/providers/pop3/POP3Message.java \
source/gnu/mail/providers/pop3/POP3Store.java

# News protocol
NNTP_JAVASRC = \
source/gnu/mail/providers/nntp/NNTPFolder.java \
source/gnu/mail/providers/nntp/NNTPMessage.java \
source/gnu/mail/providers/nntp/NNTPRootFolder.java \
source/gnu/mail/providers/nntp/NNTPStore.java \
source/gnu/mail/providers/nntp/NNTPTransport.java

# Mbox provider.
MBOX_JAVASRC = \
source/gnu/mail/providers/mbox/MboxFolder.java \
source/gnu/mail/providers/mbox/MboxMessage.java \
source/gnu/mail/providers/mbox/MboxOutputStream.java \
source/gnu/mail/providers/mbox/MboxStore.java

# Maildir message store provider.
MAILDIR_JAVASRC = \
source/gnu/mail/providers/maildir/MaildirFolder.java \
source/gnu/mail/providers/maildir/MaildirMessage.java \
source/gnu/mail/providers/maildir/MaildirStore.java

# The main jar file.
GNUMAILJAR = gnumail.jar

# The GNU classpath inetlib library.
INETLIBJAR = lib/inetlib.jar

# The META stuff
MAIL_META = \
META-INF/COPYING \
META-INF/mime.types \
META-INF/mailcap \
META-INF/javamail.default.providers \
META-INF/javamail.default.address.map

# Targets
BUILT_SOURCES = $(GNUMAILJAR) smtp.jar imap.jar pop3.jar nntp.jar mbox.jar maildir.jar

EXTRA_DIST = jarcompiler $(API_JAVASRC) $(GNU_SRC) $(MAIL_META)

#if ENABLE_SMTP
#smtp.jar \
#endif
#if ENABLE_IMAP
#imap.jar \
#endif
#if ENABLE_POP3
#pop3.jar \
#endif
#if ENABLE_NNTP
#nntp.jar \
#endif
#if ENABLE_MBOX
#mbox.jar \
#endif
#if ENABLE_MAILDIR
#maildir.jar \
#endif
#$(GNUMAILJAR)

META-INF/COPYING: COPYING
	mkdir -p META-INF
	cp COPYING META-INF/

META-INF/mime.types: mime.types
	mkdir -p META-INF
	cp mime.types META-INF/

META-INF/mailcap: mailcap
	mkdir -p META-INF
	cp mailcap META-INF/

META-INF/javamail.default.providers: javamail.providers.in
	mkdir -p META-INF
	cp javamail.providers.in META-INF/javamail.default.providers

META-INF/javamail.default.address.map: javamail.address.map.in
	mkdir -p META-INF
	cp javamail.address.map.in META-INF/javamail.default.address.map

MAIL_CLASSPATH = lib/activation.jar

# Targets for the various tools
$(GNUMAILJAR): $(API_JAVASRC) $(GNU_SRC) $(MAIL_META)
	echo $? | JARCOMPILER_JAVAC="@JAVAC@" \
		   JARCOMPILER_CLASSPATH=$(MAIL_CLASSPATH):$(INETLIBJAR) \
		   JARCOMPILER_JAR=@JAR@ \
                         ./jarcompiler $(GNUMAILJAR)

imap.jar: $(GNUMAILJAR) $(IMAP_JAVASRC) $(MAIL_META)
	echo $? | JARCOMPILER_JAVAC="@JAVAC@" \
		   JARCOMPILER_JAR=@JAR@ \
		   JARCOMPILER_CLASSPATH=$(MAIL_CLASSPATH):$(GNUMAILJAR):$(INETLIBJAR) \
                         ./jarcompiler imap.jar

smtp.jar: $(GNUMAILJAR) $(SMTP_JAVASRC) $(MAIL_META)
	echo $? | JARCOMPILER_JAVAC="@JAVAC@" \
		   JARCOMPILER_JAR=@JAR@ \
		   JARCOMPILER_CLASSPATH=$(MAIL_CLASSPATH):$(GNUMAILJAR):$(INETLIBJAR) \
                         ./jarcompiler smtp.jar

pop3.jar: $(GNUMAILJAR) $(POP3_JAVASRC) $(MAIL_META)
	echo $? | JARCOMPILER_JAVAC="@JAVAC@" \
		   JARCOMPILER_JAR=@JAR@ \
		   JARCOMPILER_CLASSPATH=$(MAIL_CLASSPATH):$(GNUMAILJAR):$(INETLIBJAR) \
                         ./jarcompiler pop3.jar

nntp.jar: $(GNUMAILJAR) $(NNTP_JAVASRC) $(MAIL_META)
	echo $? | JARCOMPILER_JAVAC="@JAVAC@" \
		   JARCOMPILER_JAR=@JAR@ \
		   JARCOMPILER_CLASSPATH=$(MAIL_CLASSPATH):$(GNUMAILJAR):$(INETLIBJAR) \
                         ./jarcompiler nntp.jar

maildir.jar: $(GNUMAILJAR) $(MAILDIR_JAVASRC) $(MAIL_META)
	echo $? | JARCOMPILER_JAVAC="@JAVAC@" \
		   JARCOMPILER_JAR=@JAR@ \
		   JARCOMPILER_CLASSPATH=$(MAIL_CLASSPATH):$(GNUMAILJAR):$(INETLIBJAR) \
                         ./jarcompiler maildir.jar

mbox.jar: $(GNUMAILJAR) $(MBOX_JAVASRC) $(MAIL_META)
	echo $? | JARCOMPILER_JAVAC="@JAVAC@" \
		   JARCOMPILER_JAR=@JAR@ \
		   JARCOMPILER_CLASSPATH=$(MAIL_CLASSPATH):$(GNUMAILJAR):$(INETLIBJAR) \
                         ./jarcompiler mbox.jar

# Remove jar file
clean-local:
	rm -f $(GNUMAILJAR) 
	rm -f imap.jar smtp.jar pop3.jar nntp.jar maildir.jar mbox.jar
	rm -f $(MAIL_META)

# End of Makefile.am
