package com.sphenon.basics.retriever.classes;

/****************************************************************************
  Copyright 2001-2024 Sphenon GmbH

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
import com.sphenon.basics.variatives.*;
import com.sphenon.basics.variatives.classes.*;
import com.sphenon.basics.metadata.*;
import com.sphenon.basics.retriever.*;
import com.sphenon.basics.retriever.returncodes.*;

public class Test_GenericRetrieverFilterBuilder implements GenericRetrieverFilterBuilder {
    protected String id;

    public Test_GenericRetrieverFilterBuilder (CallContext context, String id) {
        this.id = id;
    }

    public void clearFilter(CallContext context) {
        System.err.println("GRFB " + id + " cleared");
    }

    public GenericFilterBuilder getFilterBuilder(CallContext context, String field, boolean complex) {
        System.err.println("GRFB " + id + " getFB " + field);
        return new Test_GenericFilterBuilder(context, id + "." + field);
    }

    public boolean getFilterEnabled (CallContext context) {
        return true;
    }

    public Type getTargetType (CallContext context) {
        return null;
    }

    public boolean matches (CallContext context, Object object) {
        return true;
    }
}
