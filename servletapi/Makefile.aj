# Makefile for servlet classes project
# (c) Nic Ferrier - Tapsell-Ferrier Limited 2001
#
# You are free to redistribute this file. NO WARRANTY or fitness 
# for purpose is implied by this notice.
#

# Variables which define some usefull constants
newline:=\\n
empty:=
space:=$(empty) $(empty)


# Set by autoconf
PROJECTROOT = @srcdir@
SOURCEDIR=$(PROJECTROOT)/source

# Tools managed by autoconf.
JAVAC = @JAVA_CC@
JAVAC_OPTS = @JAVA_CC_OPTS@
JAR = @JAR_TOOL@


# The files that make up the servlet API.
SOURCEFILES = 	$(SOURCEDIR)/javax/servlet/Filter.java \
		$(SOURCEDIR)/javax/servlet/FilterChain.java \
		$(SOURCEDIR)/javax/servlet/FilterConfig.java \
		$(SOURCEDIR)/javax/servlet/GenericServlet.java \
		$(SOURCEDIR)/javax/servlet/RequestDispatcher.java \
		$(SOURCEDIR)/javax/servlet/Servlet.java \
		$(SOURCEDIR)/javax/servlet/ServletConfig.java \
		$(SOURCEDIR)/javax/servlet/ServletContext.java \
		$(SOURCEDIR)/javax/servlet/ServletContextAttributeEvent.java \
		$(SOURCEDIR)/javax/servlet/ServletContextAttributeListener.java \
		$(SOURCEDIR)/javax/servlet/ServletContextEvent.java \
		$(SOURCEDIR)/javax/servlet/ServletContextListener.java \
		$(SOURCEDIR)/javax/servlet/ServletException.java \
                $(SOURCEDIR)/javax/servlet/ServletInputStream.java \
                $(SOURCEDIR)/javax/servlet/ServletOutputStream.java \
		${SOURCEDIR}/javax/servlet/ServletRequest.java \
		${SOURCEDIR}/javax/servlet/ServletRequestWrapper.java \
		${SOURCEDIR}/javax/servlet/ServletResponse.java \
		${SOURCEDIR}/javax/servlet/ServletResponseWrapper.java \
		${SOURCEDIR}/javax/servlet/SingleThreadModel.java \
		$(SOURCEDIR)/javax/servlet/UnavailableException.java \
		$(SOURCEDIR)/javax/servlet/http/Cookie.java \
		$(SOURCEDIR)/javax/servlet/http/HttpServlet.java \
		$(SOURCEDIR)/javax/servlet/http/HttpServletRequest.java \
		$(SOURCEDIR)/javax/servlet/http/HttpServletRequestWrapper.java \
		$(SOURCEDIR)/javax/servlet/http/HttpServletResponse.java \
		$(SOURCEDIR)/javax/servlet/http/HttpServletResponseWrapper.java \
		$(SOURCEDIR)/javax/servlet/http/HttpSession.java \
		$(SOURCEDIR)/javax/servlet/http/HttpSessionActivationListener.java \
		$(SOURCEDIR)/javax/servlet/http/HttpSessionAttributeListener.java \
		$(SOURCEDIR)/javax/servlet/http/HttpSessionBindingEvent.java \
		$(SOURCEDIR)/javax/servlet/http/HttpSessionBindingListener.java \
		$(SOURCEDIR)/javax/servlet/http/HttpSessionContext.java \
		$(SOURCEDIR)/javax/servlet/http/HttpSessionEvent.java \
	        $(SOURCEDIR)/javax/servlet/http/HttpSessionListener.java \
                $(SOURCEDIR)/javax/servlet/http/HttpUtils.java \
                $(SOURCEDIR)/javax/servlet/jsp/HttpJspPage.java \
                $(SOURCEDIR)/javax/servlet/jsp/JspEngineInfo.java \
                $(SOURCEDIR)/javax/servlet/jsp/JspException.java \
                $(SOURCEDIR)/javax/servlet/jsp/JspFactory.java \
                $(SOURCEDIR)/javax/servlet/jsp/JspPage.java \
		$(SOURCEDIR)/javax/servlet/jsp/JspTagException.java \
		$(SOURCEDIR)/javax/servlet/jsp/JspWriter.java \
		$(SOURCEDIR)/javax/servlet/jsp/PageContext.java

# Compile the servlet API.
all: servlet.jar

# Ensure the makefile can update itself.
Makefile: Makefile.in configure.in
	   $(SHELL) ./config.status

Makefile.in: Makefile.aj
	     $(SHELL) $(PROJECTROOT)/automakejar ./Makefile.in

# This is an automakejar target. Run automakejar
# on Makefile.aj to turn this into a Make compatible target.
servlet.jar: 
	sourcedir=$(SOURCEDIR)
	sourcefiles=$(SOURCEFILES)
	classesdest=classes

# Cleaning targets.
clean:
	-rm -rf servlet.jar classes filelist

distclean: clean
	-rm -rf Makefile Makefile.in config.*

mrproper: distclean
	-rm -rf configure


# Build a distribution.
VERSION=2_3_6

dist: distclean
ifeq (${OSTYPE},linux-gnu)
	$(TAR) cfz gnuservletapi-$(VERSION).tar.gz -C .. $(shell basename $(shell pwd))
else
	echo Sorry, 'make dist' only works on GNU/Linux machines.
endif

##End Makefile