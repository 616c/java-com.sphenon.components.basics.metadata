// instantiated with javainst.pl from /workspace/sphenon/projects/components/basics/many/v0001/origin/source/java/com/sphenon/basics/many/templates/Factory_OOMap.javatpl
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

public class Factory_OOMap_String_Type_Type_
{
    private String[] names;
    private String[] values;

    public OOMap_String_Type_Type_ create (CallContext context) {
        OOMap_String_Type_Type_ oomap = new OOMapImpl_String_Type_Type_(context);
        for (int i=0; i<names.length; i++) {
            Pair_Type_Type_ index = ConversionTraits_Type_Type_.tryConvertFromString(context, names[i]);
            oomap.set(context, index.getItem1(context), index.getItem2(context), values[i]);
            // naja, eigentlich "add" statt "set"
        }
        return oomap;
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
