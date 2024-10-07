// instantiated with javainst.pl from /workspace/sphenon/projects/components/basics/many/v0001/origin/source/java/com/sphenon/basics/many/templates/Factory_OMap.javatpl
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

public class Factory_OMap_Object_Type_
{
    private String[] names;
    private Object[] values;

    public Factory_OMap_Object_Type_ (CallContext context) {
    }

    static public OMap_Object_Type_ construct (CallContext context) {
        Factory_OMap_Object_Type_ factory = new Factory_OMap_Object_Type_(context);
        factory.set_ParametersAtOnce(context, new String[0], new Object[0]);        
        return factory.create(context);
    }

    public OMap_Object_Type_ create (CallContext context) {
        OMap_Object_Type_ omap = new OMapImpl_Object_Type_(context);
        for (int i=0; i<names.length; i++) {
            Type index = ConversionTraits_Type_.tryConvertFromString(context, names[i]);
            omap.set(context, index, values[i]);
            // naja, eigentlich "add" statt "set"
        }
        return omap;
    }

    public void set_ParametersAtOnce(CallContext call_context, String[] names, Object[] values) {
        if (names.length != values.length) {
            Context context = Context.create(call_context);
            CustomaryContext cc = CustomaryContext.create(context);
            cc.throwPreConditionViolation(context, ManyStringPool.get(context, "0.0.0" /* number of names differs from number of values */));
        }
        this.names = names;
        this.values = values;
    }
}
