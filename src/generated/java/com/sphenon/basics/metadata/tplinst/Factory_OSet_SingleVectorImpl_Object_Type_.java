// instantiated with javainst.pl from /workspace/sphenon/projects/components/basics/many/v0001/origin/source/java/com/sphenon/basics/many/templates/Factory_OSet_SingleVectorImpl.javatpl
// please do not modify this file directly
package com.sphenon.basics.metadata.tplinst;

import com.sphenon.basics.metadata.*;
import com.sphenon.basics.metadata.traits.*;

import com.sphenon.basics.context.*;
import com.sphenon.basics.exception.*;
import com.sphenon.basics.notification.*;
import com.sphenon.basics.customary.*;
import com.sphenon.basics.many.*;

public class Factory_OSet_SingleVectorImpl_Object_Type_ extends Factory_OSet_Object_Type_
{
    public Factory_OSet_SingleVectorImpl_Object_Type_ (CallContext context) {
        super(context);
    }

    public OSet_Object_Type_ create (CallContext context) {
        OSet_Object_Type_ oset = new OSet_SingleVectorImpl_Object_Type_(context);
        if (names != null && values != null) {
            for (int i=0; i<names.length; i++) {
                oset.set(context, values[i]);
                // naja, eigentlich "add" statt "set"
            }
        }
        return oset;
    }
}
