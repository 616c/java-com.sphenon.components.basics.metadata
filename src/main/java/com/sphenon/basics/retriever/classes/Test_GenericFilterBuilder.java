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
import com.sphenon.basics.function.*;
import com.sphenon.basics.variatives.*;
import com.sphenon.basics.variatives.classes.*;
import com.sphenon.basics.retriever.*;
import com.sphenon.basics.retriever.returncodes.*;

public class Test_GenericFilterBuilder implements GenericFilterBuilder {
    protected String id;
    protected boolean is_complex;

    public Test_GenericFilterBuilder (CallContext context, String id) {
        this.id = id;
        this.is_complex = id.matches(".*O");
    }

    public boolean isComplexFilter(CallContext context) {
        return this.is_complex;
    }

    public void clearFilter(CallContext context) {
        System.err.println("GFB " + id + " cleared");
    }

    public void addFilter(CallContext context, String unique_id, String operator, Object value) {
        System.err.println("GFB " + id + " add " + unique_id + " " + operator + " " + value);
    }

    public GenericRetrieverFilterBuilder addFilter(CallContext context, String unique_id) {
        System.err.println("GFB " + id + " add " + unique_id);
        return new Test_GenericRetrieverFilterBuilder(context, id);
    }

    public GenericFilterBuilder wireToMember(CallContext context, String member_name, Getter getter, Setter setter, Creator creator) {
        return this;
    }
}
