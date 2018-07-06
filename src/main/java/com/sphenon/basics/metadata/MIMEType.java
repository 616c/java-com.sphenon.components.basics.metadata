package com.sphenon.basics.metadata;

/****************************************************************************
  Copyright 2001-2018 Sphenon GmbH

  Licensed under the Apache License, Version 2.0 (the "License"); you may not
  use this file except in compliance with the License. You may obtain a copy
  of the License at http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
  License for the specific language governing permissions and limitations
  under the License.
*****************************************************************************/

import com.sphenon.basics.context.*;
import com.sphenon.basics.configuration.*;
import com.sphenon.basics.customary.*;
import com.sphenon.basics.exception.*;

import com.sphenon.basics.metadata.returncodes.*;
import com.sphenon.basics.metadata.exceptions.*;
import com.sphenon.basics.metadata.tplinst.*;

import java.util.Hashtable;

public class MIMEType {

    static protected Hashtable<String,MIMEType> mime_types_by_extension;
    static protected Hashtable<String,MIMEType> mime_types_by_mime_type;

    static public void defineMIMEType(CallContext context, String mime, String extension, String name) {
        if (mime_types_by_extension == null) {
            mime_types_by_extension = new Hashtable<String,MIMEType>();
        }
        if (mime_types_by_extension.get(extension) != null) {
            CustomaryContext.create((Context)context).throwConfigurationError(context, "MIME Type '%(mime)' is already defined (registered with extension '%(extension)').", "mime", mime, "extension", extension);
            throw (ExceptionConfigurationError) null; // compiler insists
        }

        if (mime_types_by_mime_type == null) {
            mime_types_by_mime_type = new Hashtable<String,MIMEType>();
        }
        if (mime_types_by_mime_type.get(extension) != null) {
            CustomaryContext.create((Context)context).throwConfigurationError(context, "MIME Type '%(mime)' is already defined (registered with extension '%(extension)').", "mime", mime, "extension", extension);
            throw (ExceptionConfigurationError) null; // compiler insists
        }

        MIMEType mt = new MIMEType (context, mime, extension, name);
        mime_types_by_extension.put(extension, mt);
        mime_types_by_mime_type.put(mime, mt);
    }

    static public MIMEType getMIMEType(CallContext context, String extension) {
        return (mime_types_by_extension == null ? null : mime_types_by_extension.get(extension));
    }

    static public MIMEType getMIMEType(CallContext context, String type, String subtype) {
        return (mime_types_by_mime_type == null ? null : mime_types_by_mime_type.get(type + "/" + subtype));
    }

    public MIMEType (CallContext context, String mime, String extension, String name) {
        this.mime      = mime;
        this.extension = extension;
        this.name      = name;
    }

    protected String mime;

    public String getMIME(CallContext context) {
        return this.mime;
    }

    protected String extension;

    public String getExtension(CallContext context) {
        return this.extension;
    }

    protected String name;

    public String getName(CallContext context) {
        return this.name;
    }
}
