/*
 * xmlj_sax.h
 * Copyright (C) 2004 The Free Software Foundation
 * 
 * This file is part of GNU JAXP, a library.
 * 
 * GNU JAXP is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * GNU JAXP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * As a special exception, if you link this library with other files to
 * produce an executable, this library does not by itself cause the
 * resulting executable to be covered by the GNU General Public License.
 * This exception does not however invalidate any other reasons why the
 * executable file might be covered by the GNU General Public License.
 */

#include "gnu_xml_libxmlj_sax_GnomeLocator.h"
#include "gnu_xml_libxmlj_sax_GnomeXMLReader.h"

#include <libxml/SAX.h>
#include <libxml/parser.h>

/* -- Function declarations for callback functions -- */

xmlParserInputPtr jaxpResolveEntity(void *, const xmlChar *, const xmlChar *);

void jaxpNotationDecl(void *, const xmlChar *, const xmlChar *, const xmlChar *);

void jaxpUnparsedEntityDecl(void *, const xmlChar *, const xmlChar *, 
		const xmlChar *, const xmlChar *);

void jaxpSetDocumentLocator(void *, xmlSAXLocatorPtr);

void jaxpStartDocument(void *);

void jaxpEndDocument(void *);

void jaxpStartElement(void *, const xmlChar *, const xmlChar **);

void jaxpEndElement(void *, const xmlChar *);

void jaxpCharacters(void *, const xmlChar *, int);

void jaxpIgnorableWhitespace(void *, const xmlChar *, int);

void jaxpProcessingInstruction(void *, const xmlChar *, const xmlChar *);

void jaxpWarning(void *, const char *, ...);

void jaxpError(void *, const char *, ...);

void jaxpFatalError(void *, const char *, ...);

