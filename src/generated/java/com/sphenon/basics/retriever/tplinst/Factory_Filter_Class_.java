// instantiated with javainst.pl from /workspace/sphenon/projects/components/basics/retriever/v0001/origin/source/java/com/sphenon/basics/retriever/templates/Factory_Filter.javatpl
// please do not modify this file directly
package com.sphenon.basics.retriever.tplinst;

import com.sphenon.basics.retriever.*;
import java.util.Date;

import com.sphenon.basics.context.*;
import com.sphenon.basics.context.classes.*;
import com.sphenon.basics.exception.*;
import com.sphenon.basics.configuration.*;
import com.sphenon.basics.customary.*;
import com.sphenon.basics.retriever.*;
import com.sphenon.basics.factory.*;

public class Factory_Filter_Class_ {

    /* -------------- extensible factory instantiation --------------------------------------------------------------------------------------- */
    static protected Configuration config;
    static protected FactoryInstantiator<Factory_Filter_Class_> factory_instantiator;
    static {
      CallContext context = RootContext.getInitialisationContext();
      config = Configuration.create(context, "--__Package__--.Factory_Filter_Class_");
      factory_instantiator = new FactoryInstantiator(context, Factory_Filter_Class_.class) { protected Factory_Filter_Class_ createDefault(CallContext context) { return new Factory_Filter_Class_(context); } };
    };
    /* --------------------------------------------------------------------------------------------------------------------------------------- */
    static public Factory_Filter_Class_ newInstance (CallContext context) {
        return factory_instantiator.newInstance(context);
    }
    /* --------------------------------------------------------------------------------------------------------------------------------------- */

    static public Filter_Class_ construct (CallContext context) {
        return newInstance(context).create(context);
    }

    protected Factory_Filter_Class_ (CallContext context) {
    }

    public Filter_Class_ create (CallContext context) {
        FilterByValue_Class_ filter = new FilterByValue_Class_(context);
        return filter;
    }

    static public Filter_Class_ construct (CallContext context, Class value) {
        return newInstance(context).create(context, value);
    }

    public Filter_Class_ create (CallContext context, Class value) {
        return new FilterByValue_Class_(context, value);
    }
}
