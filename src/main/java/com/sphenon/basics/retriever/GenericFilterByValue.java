package com.sphenon.basics.retriever;

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
import com.sphenon.basics.exception.*;
import com.sphenon.basics.retriever.*;
import com.sphenon.basics.metadata.*;

public class GenericFilterByValue<TargetType> implements GenericFilter<TargetType>, FilterByValue {

    public GenericFilterByValue (CallContext context, Type target_type) {
        this.target_type = target_type;
        this.setFilterEnabled(context, false);
    }

    public GenericFilterByValue (CallContext context, Type target_type, TargetType value) {
        this.target_type = target_type;
        this.setValue(context, value);
    }

    public GenericFilterByValue (CallContext context, Type target_type, TargetType... values) {
        this.target_type = target_type;
        this.setValues(context, values);
    }

    protected Type target_type;

    public Type getTargetType (CallContext context) {
        return this.target_type;
    }

    protected boolean filter_enabled;

    public boolean getFilterEnabled (CallContext context) {
        return this.filter_enabled;
    }

    public void setFilterEnabled (CallContext context, boolean filter_enabled) {
        this.filter_enabled = filter_enabled;
    }

    protected TargetType[] values;

    public TargetType getValue (CallContext context) {
        return this.values == null || this.values.length != 1 ? null : this.values[0];
    }

    public GenericFilterByValue<TargetType> setValue (CallContext context, TargetType value) {
        this.setValues(context, value);
        return this;
    }

    public TargetType[] getValues (CallContext context) {
        return this.values;
    }

    public GenericFilterByValue<TargetType> setValues (CallContext context, TargetType... values) {
        this.setFilterEnabled(context, true);
        this.values = values;
        return this;
    }

    public Object getValueAsObject (CallContext context) {
        return this.values != null && this.values.length == 1 ? this.values[0] : this.values;
    }

    public boolean matches (CallContext context, TargetType object) {
        if (this.filter_enabled == false) { return true; }
        if (this.values != null && object != null) {
            for (TargetType value : values) {
                if (value == object) { return true; }
            }
        }
        return false;
    }
}

