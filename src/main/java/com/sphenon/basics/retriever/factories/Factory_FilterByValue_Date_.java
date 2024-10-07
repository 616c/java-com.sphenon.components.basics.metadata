package com.sphenon.basics.retriever.factories;

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

import com.sphenon.basics.retriever.*;
import java.util.Date;

import com.sphenon.basics.context.*;
import com.sphenon.basics.context.classes.*;
import com.sphenon.basics.exception.*;
import com.sphenon.basics.configuration.*;
import com.sphenon.basics.customary.*;
import com.sphenon.basics.retriever.*;
import com.sphenon.basics.factory.*;

public class Factory_FilterByValue_Date_ {

    /* -------------- extensible factory instantiation --------------------------------------------------------------------------------------- */
    static protected Configuration config;
    static protected FactoryInstantiator<Factory_FilterByValue_Date_> factory_instantiator;
    static {
      CallContext context = RootContext.getInitialisationContext();
      config = Configuration.create(context, "--__Package__--.Factory_Filter_Date_");
      factory_instantiator = new FactoryInstantiator(context, Factory_FilterByValue_Date_.class) { protected Factory_FilterByValue_Date_ createDefault(CallContext context) { return new Factory_FilterByValue_Date_(context); } };
    };
    /* --------------------------------------------------------------------------------------------------------------------------------------- */
    static public Factory_FilterByValue_Date_ newInstance (CallContext context) {
        return factory_instantiator.newInstance(context);
    }
    /* --------------------------------------------------------------------------------------------------------------------------------------- */

    static public com.sphenon.basics.retriever.tplinst.Filter_Date_ construct (CallContext context) {
        return newInstance(context).create(context);
    }

    protected Factory_FilterByValue_Date_ (CallContext context) {
    }

    public com.sphenon.basics.retriever.tplinst.Filter_Date_ create (CallContext context) {
        return new com.sphenon.basics.retriever.tplinst.FilterByValue_Date_(context);
    }
}