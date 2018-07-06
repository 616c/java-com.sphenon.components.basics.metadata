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
import com.sphenon.basics.message.*;
import com.sphenon.basics.exception.*;
import com.sphenon.basics.customary.*;
import com.sphenon.basics.metadata.tplinst.*;

public class TypeContext extends SpecificContext {

    static protected TypeContext default_singleton;
    protected boolean is_default_singelton;

    static public TypeContext get(Context context) {
        TypeContext tc = (TypeContext) context.getSpecificContext(TypeContext.class);
        if (tc != null) {
            return tc;
        }
        return default_singleton == null ? (default_singleton = new TypeContext(context, true)) : default_singleton;
    }

    static public TypeContext create(Context context) {
        TypeContext tc = new TypeContext(context, false);
        context.setSpecificContext(TypeContext.class, tc);
        return tc;
    }

    protected TypeContext (Context context, boolean is_default_singelton) {
        super(context);
        this.is_default_singelton = is_default_singelton;
        this.search_path_context = null;
    }

    /**
       UUUhhhh - this is legacy as legacy can...
       Should be really renamed to "TypeContextId" or so...
    */

    protected String search_path_context;

    public void setSearchPathContext(CallContext context, String search_path_context) {
        if (is_default_singelton) {
            CustomaryContext.create((Context) context).throwPreConditionViolation(context, "Cannot modify default singelton TypeContext");
            throw (ExceptionPreConditionViolation) null; // compiler insists
        }
        this.search_path_context = search_path_context;
    }

    public String getSearchPathContext(CallContext cc) {
        if (is_default_singelton) { return "DEFAULT"; }
        TypeContext tc;
        return (this.search_path_context != null ?
                     this.search_path_context
                  : (tc = (TypeContext) this.getCallContext(TypeContext.class)) != null ?
                       tc.getSearchPathContext(cc)
                     : "DEFAULT"
               );
    }

    public void setTypeContextId(CallContext context, String type_context_id) {
        this.setSearchPathContext(context, type_context_id);
    }

    public String getTypeContextId(CallContext context) {
        return this.getSearchPathContext(context);
    }
}
