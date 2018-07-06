package com.sphenon.basics.metadata.factories;

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
import com.sphenon.basics.message.*;
import com.sphenon.basics.exception.*;
import com.sphenon.basics.notification.*;
import com.sphenon.basics.customary.*;
import com.sphenon.basics.metadata.*;
import com.sphenon.basics.metadata.returncodes.*;
import com.sphenon.basics.validation.returncodes.*;

public class Factory_TypeImpl_MediaObject {

    public Factory_TypeImpl_MediaObject (CallContext call_context) {
    }

    protected String mime_type;
    protected TypeImpl_MediaObject type;

    public String getMIMEType (CallContext context) {
        return this.mime_type;
    }

    public void setMIMEType (CallContext context, String mime_type) {
        this.mime_type = mime_type;
    }

    public void validateMIMEType (CallContext context) throws ValidationFailure {
        this.type = (TypeImpl_MediaObject) TypeManager.getMediaTypeMIME(context, this.mime_type);
    }

    public TypeImpl_MediaObject create (CallContext context) {
        try {
            this.validateMIMEType(context);
            return this.type;
        } catch (ValidationFailure vf) {
            CustomaryContext.create((Context)context).throwPreConditionViolation(context, vf, "Factory invocation (create) with invalid parameters");
            throw (ExceptionPreConditionViolation) null; // compiler insists
        }
    }
}
