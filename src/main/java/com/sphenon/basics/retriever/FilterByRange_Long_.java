package com.sphenon.basics.retriever;

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

import com.sphenon.basics.retriever.tplinst.*;

import com.sphenon.basics.context.*;
import com.sphenon.basics.exception.*;
import com.sphenon.basics.retriever.*;

public class FilterByRange_Long_ extends FilterByRange_long_ implements Filter_Long_ {

    public FilterByRange_Long_ (CallContext context) {
        super(context);
    }

    public FilterByRange_Long_ (CallContext context, Long minimum, Long maximum) {
        super(context, minimum, maximum);
    }

    public boolean matches (CallContext context, Long object) {
        return super.matches(context, object);
    }

    static public FilterByRange_Long_ newInstance(CallContext context) {
        return (FilterByRange_Long_) Factory_Filter_Long_.construct(context);
    }
}
