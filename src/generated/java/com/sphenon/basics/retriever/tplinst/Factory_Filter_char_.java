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

public class Factory_Filter_char_ {

    /* -------------- extensible factory instantiation --------------------------------------------------------------------------------------- */
    static protected Configuration config;
    static protected FactoryInstantiator<Factory_Filter_char_> factory_instantiator;
    static {
      CallContext context = RootContext.getInitialisationContext();
      config = Configuration.create(context, "--__Package__--.Factory_Filter_char_");
      factory_instantiator = new FactoryInstantiator(context, Factory_Filter_char_.class) { protected Factory_Filter_char_ createDefault(CallContext context) { return new Factory_Filter_char_(context); } };
    };
    /* --------------------------------------------------------------------------------------------------------------------------------------- */
    static public Factory_Filter_char_ newInstance (CallContext context) {
        return factory_instantiator.newInstance(context);
    }
    /* --------------------------------------------------------------------------------------------------------------------------------------- */

    static public Filter_char_ construct (CallContext context) {
        return newInstance(context).create(context);
    }

    protected Factory_Filter_char_ (CallContext context) {
    }

    public Filter_char_ create (CallContext context) {
        FilterByRegExp_char_ filter = new FilterByRegExp_char_(context);
        return filter;
    }

    static public Filter_char_ construct (CallContext context, String expression) {
        return newInstance(context).create(context, expression);
    }

    public Filter_char_ create (CallContext context, String expression) {
        return new FilterByRegExp_char_(context, expression);
    }
}
