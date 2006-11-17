## Process this file with automake to produce Makefile.in.

# Makefile.am for GNU Classpathx JavaMail.

JAVAC = @JAVAC@
#JAVACFLAGS = @JAVACFLAGS@
JAVACFLAGS = -g
JAVADOC = javadoc
JAVADOCFLAGS =

src = @srcdir@/source
dst = classes
doc = @srcdir@/docs

servlet_jar = servlet.jar

# The files that make up the servlet API.
sources = \
	$(src)/javax/servlet/Filter.java \
	$(src)/javax/servlet/FilterChain.java \
	$(src)/javax/servlet/FilterConfig.java \
	$(src)/javax/servlet/GenericServlet.java \
	$(src)/javax/servlet/RequestDispatcher.java \
	$(src)/javax/servlet/Servlet.java \
	$(src)/javax/servlet/ServletConfig.java \
	$(src)/javax/servlet/ServletContext.java \
	$(src)/javax/servlet/ServletContextAttributeEvent.java \
	$(src)/javax/servlet/ServletContextAttributeListener.java \
	$(src)/javax/servlet/ServletContextEvent.java \
	$(src)/javax/servlet/ServletContextListener.java \
	$(src)/javax/servlet/ServletException.java \
	$(src)/javax/servlet/ServletInputStream.java \
	$(src)/javax/servlet/ServletOutputStream.java \
	${src}/javax/servlet/ServletRequest.java \
	${src}/javax/servlet/ServletRequestAttributeEvent.java \
	${src}/javax/servlet/ServletRequestAttributeListener.java \
	${src}/javax/servlet/ServletRequestEvent.java \
	${src}/javax/servlet/ServletRequestListener.java \
	${src}/javax/servlet/ServletRequestWrapper.java \
	${src}/javax/servlet/ServletResponse.java \
	${src}/javax/servlet/ServletResponseWrapper.java \
	${src}/javax/servlet/SingleThreadModel.java \
	$(src)/javax/servlet/UnavailableException.java \
	$(src)/javax/servlet/http/Cookie.java \
	$(src)/javax/servlet/http/HttpServlet.java \
	$(src)/javax/servlet/http/HttpServletRequest.java \
	$(src)/javax/servlet/http/HttpServletRequestWrapper.java \
	$(src)/javax/servlet/http/HttpServletResponse.java \
	$(src)/javax/servlet/http/HttpServletResponseWrapper.java \
	$(src)/javax/servlet/http/HttpSession.java \
	$(src)/javax/servlet/http/HttpSessionActivationListener.java \
	$(src)/javax/servlet/http/HttpSessionAttributeListener.java \
	$(src)/javax/servlet/http/HttpSessionBindingEvent.java \
	$(src)/javax/servlet/http/HttpSessionBindingListener.java \
	$(src)/javax/servlet/http/HttpSessionContext.java \
	$(src)/javax/servlet/http/HttpSessionEvent.java \
	$(src)/javax/servlet/http/HttpSessionListener.java \
	$(src)/javax/servlet/http/HttpUtils.java \
	$(src)/javax/servlet/jsp/ErrorData.java \
	$(src)/javax/servlet/jsp/HttpJspPage.java \
	$(src)/javax/servlet/jsp/JspContext.java \
	$(src)/javax/servlet/jsp/JspEngineInfo.java \
	$(src)/javax/servlet/jsp/JspException.java \
	$(src)/javax/servlet/jsp/JspFactory.java \
	$(src)/javax/servlet/jsp/JspPage.java \
	$(src)/javax/servlet/jsp/JspTagException.java \
	$(src)/javax/servlet/jsp/JspWriter.java \
	$(src)/javax/servlet/jsp/PageContext.java \
	$(src)/javax/servlet/jsp/SkipPageException.java \
	$(src)/javax/servlet/jsp/el/ELException.java \
	$(src)/javax/servlet/jsp/el/ELParseException.java \
	$(src)/javax/servlet/jsp/el/Expression.java \
	$(src)/javax/servlet/jsp/el/ExpressionEvaluator.java \
	$(src)/javax/servlet/jsp/el/FunctionMapper.java \
	$(src)/javax/servlet/jsp/el/VariableResolver.java \
	$(src)/javax/servlet/jsp/tagext/BodyContent.java \
	$(src)/javax/servlet/jsp/tagext/BodyTag.java \
	$(src)/javax/servlet/jsp/tagext/BodyTagSupport.java \
	$(src)/javax/servlet/jsp/tagext/DynamicAttributes.java \
	$(src)/javax/servlet/jsp/tagext/FunctionInfo.java \
	$(src)/javax/servlet/jsp/tagext/IterationTag.java \
	$(src)/javax/servlet/jsp/tagext/JspFragment.java \
	$(src)/javax/servlet/jsp/tagext/JspTag.java \
	$(src)/javax/servlet/jsp/tagext/PageData.java \
	$(src)/javax/servlet/jsp/tagext/SimpleTag.java \
	$(src)/javax/servlet/jsp/tagext/SimpleTagSupport.java \
	$(src)/javax/servlet/jsp/tagext/Tag.java \
	$(src)/javax/servlet/jsp/tagext/TagAdapter.java \
	$(src)/javax/servlet/jsp/tagext/TagAttributeInfo.java \
	$(src)/javax/servlet/jsp/tagext/TagData.java \
	$(src)/javax/servlet/jsp/tagext/TagExtraInfo.java \
	$(src)/javax/servlet/jsp/tagext/TagFileInfo.java \
	$(src)/javax/servlet/jsp/tagext/TagInfo.java \
	$(src)/javax/servlet/jsp/tagext/TagLibraryInfo.java \
	$(src)/javax/servlet/jsp/tagext/TagLibraryValidator.java \
	$(src)/javax/servlet/jsp/tagext/TagSupport.java \
	$(src)/javax/servlet/jsp/tagext/TagVariableInfo.java \
	$(src)/javax/servlet/jsp/tagext/TryCatchFinally.java \
	$(src)/javax/servlet/jsp/tagext/ValidationMessage.java \
	$(src)/javax/servlet/jsp/tagext/VariableInfo.java

package_names = \
	javax.servlet \
	javax.servlet.http \
	javax.servlet.jsp \
	javax.servlet.jsp.el \
	javax.servlet.jsp.tagext

# Classpath
classpath = $(src)

# Target
BUILT_SOURCES = $(servlet_jar)

classes = $(patsubst $(src)/%.java, $(dst)/%.class, $(sources))

$(dst):
	mkdir -p $(dst)

# Jar
$(servlet_jar): $(classes)
	$(JAR) cf $(servlet_jar) -C $(dst) .

# Compilation
$(classes): $(dst)/%.class: $(src)/%.java $(dst)
	$(JAVAC) $(JAVACFLAGS) -classpath $(classpath) -d $(dst) $<

# Javadoc
javadoc:
	mkdir -p $(doc)
	$(JAVADOC) $(JAVADOCFLAGS) -d $(doc) -sourcepath $(src) \
	  -windowtitle "GNU Servlet API documentation" $(package_names)

# Remove jar files and directories
clean-local:
	rm -rf $(servlet_jar) $(dst) $(doc)

install-data-local:
	@srcdir@/mkinstalldirs $(DESTDIR)/$(datadir)/java
	$(INSTALL_DATA) $(servlet_jar) $(DESTDIR)/$(datadir)/java

# End of Makefile.am
