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

public class Factory_Type {
    public Factory_Type (CallContext call_context) {
    }

    protected String type_name;
    protected Type type;

    public String getTypeName (CallContext context) {
        return this.type_name;
    }

    public void setTypeName (CallContext context, String type_name) {
        this.type_name = type_name;
    }

    public void validateTypeName (CallContext context) throws ValidationFailure {
        try {
            this.type = TypeManager.get(context, this.type_name);
        } catch (NoSuchClass nsc) {
            ValidationFailure.createAndThrow(context, nsc, "TypeName Argument is not valid");
            throw (ValidationFailure) null; // compiler insists
        }
    }

    public Type create (CallContext call_context) {
        Context context = Context.create(call_context);
        try {
            this.validateTypeName(context);
            return this.type;
        } catch (ValidationFailure vf) {
            CustomaryContext cc = CustomaryContext.create(context);
            cc.throwPreConditionViolation(context, vf, "Factory invocation (create) with invalid parameters");
            throw (ExceptionPreConditionViolation) null; // compiler insists
        }
    }
}
