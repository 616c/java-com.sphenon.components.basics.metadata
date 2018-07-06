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

public class GenericFilterByValue_Optional<TargetType> extends GenericFilterByValue<TargetType> {

    public GenericFilterByValue_Optional (CallContext context, Type target_type) {
        super(context, target_type);
    }

    public GenericFilterByValue_Optional (CallContext context, Type target_type, TargetType value) {
        super(context, target_type, value);
    }

    public GenericFilterByValue_Optional (CallContext context, Type target_type, TargetType... values) {
        super(context, target_type, values);
    }

    public boolean matches (CallContext context, TargetType object) {
        if (this.filter_enabled == false) { return true; }
        if (this.values != null) {
            for (TargetType value : values) {
                if (    (value == null && object == null)
                     || (    value != null
                          && object != null
                          && value == object
                        )
                   ) {
                    return true;
                }
            }
        }
        return false;
    }
}
