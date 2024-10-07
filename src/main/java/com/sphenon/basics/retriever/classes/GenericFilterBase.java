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
import com.sphenon.basics.exception.*;
import com.sphenon.basics.retriever.*;
import com.sphenon.basics.metadata.*;

import com.sphenon.basics.retriever.*;

import com.sphenon.engines.aggregator.annotations.OCPIgnore;

abstract public class GenericFilterBase<TargetType> implements GenericFilter<TargetType> {

    public GenericFilterBase (CallContext context, Type target_type) {
        this.target_type = target_type;
        this.setFilterEnabled(context, false);
    }

    protected Type target_type;

    public Type getTargetType (CallContext context) {
        return this.target_type;
    }

    protected boolean filter_enabled;

    public boolean getFilterEnabled (CallContext context) {
        return this.filter_enabled;
    }

    @OCPIgnore
    public void setFilterEnabled (CallContext context, boolean filter_enabled) {
        this.filter_enabled = filter_enabled;
    }

    protected String unique_filter_id;
    
    public String getUniqueFilterId (CallContext context) {
        return this.unique_filter_id;
    }

    @OCPIgnore
    public void setUniqueFilterId (CallContext context, String unique_filter_id) {
        this.unique_filter_id = unique_filter_id;
    }
}
