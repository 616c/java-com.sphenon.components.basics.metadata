// instantiated with javainst.pl from /workspace/sphenon/projects/components/basics/many/v0001/origin/source/java/com/sphenon/basics/many/templates/Factory_OMap.javatpl

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
// please do not modify this file directly
package com.sphenon.basics.metadata.tplinst;

import com.sphenon.basics.metadata.*;
import com.sphenon.basics.metadata.traits.*;

import com.sphenon.basics.context.*;
import com.sphenon.basics.exception.*;
import com.sphenon.basics.notification.*;
import com.sphenon.basics.customary.*;
import com.sphenon.basics.factory.returncodes.*;
import com.sphenon.basics.many.*;
import com.sphenon.basics.many.returncodes.*;
import com.sphenon.basics.many.traits.*;

public class Factory_OMap_String_Type_
{
    private String[] names;
    private String[] values;

    public Factory_OMap_String_Type_ (CallContext context) {
    }

    static public OMap_String_Type_ construct (CallContext context) {
        Factory_OMap_String_Type_ factory = new Factory_OMap_String_Type_(context);
        factory.set_ParametersAtOnce(context, new String[0], new String[0]);        
        return factory.create(context);
    }

    public OMap_String_Type_ create (CallContext context) {
        OMap_String_Type_ omap = new OMapImpl_String_Type_(context);
        for (int i=0; i<names.length; i++) {
            Type index = ConversionTraits_Type_.tryConvertFromString(context, names[i]);
            omap.set(context, index, values[i]);
            // naja, eigentlich "add" statt "set"
        }
        return omap;
    }

    public void set_ParametersAtOnce(CallContext call_context, String[] names, String[] values) {
        if (names.length != values.length) {
            Context context = Context.create(call_context);
            CustomaryContext cc = CustomaryContext.create(context);
            cc.throwPreConditionViolation(context, ManyStringPool.get(context, "0.0.0" /* number of names differs from number of values */));
        }
        this.names = names;
        this.values = values;
    }
}
