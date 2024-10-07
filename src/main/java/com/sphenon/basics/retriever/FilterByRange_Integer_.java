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

public class FilterByRange_Integer_ extends FilterByRange_int_ implements Filter_Integer_ {

    public FilterByRange_Integer_ (CallContext context) {
        super(context);
    }

    public FilterByRange_Integer_ (CallContext context, Integer minimum, Integer maximum) {
        super(context, minimum, maximum);
    }

    public boolean matches (CallContext context, Integer object) {
      if( object != null ){
        return super.matches(context, object);
      }
      return true;
    }

    static public FilterByRange_Integer_ newInstance(CallContext context) {
        return (FilterByRange_Integer_) Factory_Filter_Integer_.construct(context);
    }
}
