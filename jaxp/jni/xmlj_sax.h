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

#ifndef XMLJ_SAX_H
#define XMLJ_SAX_H

#include "gnu_xml_libxmlj_sax_GnomeLocator.h"
#include "gnu_xml_libxmlj_sax_GnomeXMLReader.h"

#include <libxml/SAX.h>
#include <libxml/parser.h>

/* -- Function declarations for callback functions -- */

xmlParserInputPtr xmljSAXResolveEntity(void *, const xmlChar *, const xmlChar *);

void xmljSAXNotationDecl(void *, const xmlChar *, const xmlChar *, const xmlChar *);

void xmljSAXUnparsedEntityDecl(void *, const xmlChar *, const xmlChar *, 
		const xmlChar *, const xmlChar *);

void xmljSAXSetDocumentLocator(void *, xmlSAXLocatorPtr);

void xmljSAXStartDocument(void *);

void xmljSAXEndDocument(void *);

void xmljSAXStartElement(void *, const xmlChar *, const xmlChar **);

void xmljSAXEndElement(void *, const xmlChar *);

void xmljSAXCharacters(void *, const xmlChar *, int);

void xmljSAXIgnorableWhitespace(void *, const xmlChar *, int);

void xmljSAXProcessingInstruction(void *, const xmlChar *, const xmlChar *);

void xmljSAXWarning(void *, const char *, ...);

void xmljSAXError(void *, const char *, ...);

void xmljSAXFatalError(void *, const char *, ...);

#endif /* !defined XMLJ_SAX_H */
