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
import com.sphenon.basics.expression.parsed.*;
import com.sphenon.basics.expression.returncodes.*;

import com.sphenon.basics.metadata.returncodes.*;
import com.sphenon.basics.metadata.exceptions.*;
import com.sphenon.basics.metadata.tplinst.*;

public class ETMImpl extends TypeName.ETM {

    static public void setup(CallContext context) {
        etm = new ETMImpl(context);
    }

    protected ETMImpl(CallContext context) {
    }

    public Object get(CallContext context, String name) throws EvaluationFailure {
        try {
            return TypeManager.get(context, name);
        } catch (NoSuchClass nsc) {
            EvaluationFailure.createAndThrow(context, nsc, "In expression to evaluate, type '%(type)' does not exist", "type", name);
            throw (EvaluationFailure) null; // compiler insists
        }
    }

    public Object get(CallContext context, Object object) {
        return TypeManager.get(context, object);
    }

    public boolean equals(CallContext context, Object t1, Object t2) {
        return ((Type) t1).equals(((Type) t2));
    }

    public boolean isA(CallContext context, Object t1, Object t2) {
        return ((Type) t1).isA(context, ((Type) t2));
    }
}
