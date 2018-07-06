package com.sphenon.basics.retriever.factories;

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
import com.sphenon.basics.context.classes.*;
import com.sphenon.basics.exception.*;
import com.sphenon.basics.configuration.*;
import com.sphenon.basics.customary.*;
import com.sphenon.basics.retriever.*;
import com.sphenon.basics.factory.*;
import com.sphenon.basics.metadata.*;

import com.sphenon.basics.retriever.*;

public class Factory_GenericFilter<TargetType> {

    /* -------------- extensible factory instantiation --------------------------------------------------------------------------------------- */
    static final public Class _class = Factory_GenericFilter.class;

    static protected com.sphenon.basics.configuration.Configuration config;
    static protected FactoryInstantiator<Factory_GenericFilter> factory_instantiator;
    static {
        CallContext context = RootContext.getInitialisationContext();
        config = com.sphenon.basics.configuration.Configuration.create(context, _class.getName());
        factory_instantiator = new FactoryInstantiator(context, Factory_GenericFilter.class) { protected Factory_GenericFilter createDefault(CallContext context) { return new Factory_GenericFilter(context); } };
    };
    /* --------------------------------------------------------------------------------------------------------------------------------------- */
    static public Factory_GenericFilter newInstance (CallContext context) {
        return factory_instantiator.newInstance(context);
    }
    /* --------------------------------------------------------------------------------------------------------------------------------------- */

    static public<TargetType> GenericFilter<TargetType> construct (CallContext context, Type target_type, boolean optional) {
        return newInstance(context).create(context, target_type, optional);
    }

    static public<TargetType> GenericFilter<TargetType> construct (CallContext context, Type target_type) {
        return newInstance(context).create(context, target_type, false);
    }

    static public<TargetType> GenericFilter<TargetType> construct (CallContext context, Class target_type, boolean optional) {
        return newInstance(context).create(context, TypeManager.get(context, target_type), optional);
    }

    static public<TargetType> GenericFilter<TargetType> construct (CallContext context, Class target_type) {
        return newInstance(context).create(context, TypeManager.get(context, target_type), false);
    }

    protected Factory_GenericFilter (CallContext context) {
    }

    public GenericFilter<TargetType> create (CallContext context, Type target_type, boolean optional) {
        return optional ? new GenericFilterByValue_Optional(context, target_type) : new GenericFilterByValue(context, target_type);
    }
}
