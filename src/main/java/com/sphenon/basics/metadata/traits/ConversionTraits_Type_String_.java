package com.sphenon.basics.metadata.traits;

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

import com.sphenon.basics.metadata.*;
import com.sphenon.basics.metadata.returncodes.*;
import com.sphenon.basics.metadata.exceptions.*;
import com.sphenon.basics.metadata.tplinst.*;

public class ConversionTraits_Type_String_
{
    static public String tryConvertToString (CallContext context, Type type, String string) {
        return type.getName(context) + "," + string;
    }

    static public Pair_Type_String_ tryConvertFromString (CallContext context, String string) {
        int pos = string.indexOf(",");
        String string1 = null;
        String string2 = null;
        if (pos == -1) {
            string1 = "Object";
            string2 = string;
        } else {
            string1 = string.substring(0,pos);
            string2 = string.substring(pos+1);
        }
        return new Pair_Type_String_(context, TypeManager.tryGet(context, string1), string2);
    }
}
