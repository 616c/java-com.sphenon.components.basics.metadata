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

public class Factory_Filter_short_ {

    /* -------------- extensible factory instantiation --------------------------------------------------------------------------------------- */
    static protected Configuration config;
    static protected FactoryInstantiator<Factory_Filter_short_> factory_instantiator;
    static {
      CallContext context = RootContext.getInitialisationContext();
      config = Configuration.create(context, "--__Package__--.Factory_Filter_short_");
      factory_instantiator = new FactoryInstantiator(context, Factory_Filter_short_.class) { protected Factory_Filter_short_ createDefault(CallContext context) { return new Factory_Filter_short_(context); } };
    };
    /* --------------------------------------------------------------------------------------------------------------------------------------- */
    static public Factory_Filter_short_ newInstance (CallContext context) {
        return factory_instantiator.newInstance(context);
    }
    /* --------------------------------------------------------------------------------------------------------------------------------------- */

    static public Filter_short_ construct (CallContext context) {
        return newInstance(context).create(context);
    }

    protected Factory_Filter_short_ (CallContext context) {
    }

    public Filter_short_ create (CallContext context) {
        FilterByRange_short_ filter = new FilterByRange_short_(context);
        return filter;
    }
}
