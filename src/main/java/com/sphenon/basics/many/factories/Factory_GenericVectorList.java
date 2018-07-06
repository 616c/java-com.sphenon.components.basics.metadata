package com.sphenon.basics.many.factories;

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
import com.sphenon.basics.notification.Notifier;
import com.sphenon.basics.notification.NotificationLevel;
import com.sphenon.basics.notification.NotificationContext;
import com.sphenon.basics.notification.NotificationLocationContext;
import com.sphenon.basics.customary.*;
import com.sphenon.basics.many.*;
import com.sphenon.basics.many.tplinst.*;
import com.sphenon.basics.metadata.*;

import com.sphenon.engines.aggregator.annotations.*;

public class Factory_GenericVectorList<T> implements GenericWithRunTimeTypes {
    protected String[] names;
    protected T[]      values;
    protected Type     component_type;

    static public<T> GenericVectorImplList<T> construct (CallContext context, Type component_type) {
        Factory_GenericVectorList<T> factory = new Factory_GenericVectorList<T>(context);
        factory.set_ComponentType(context, component_type);
        return factory.create(context);
    }

    static public<T> GenericVectorImplList<T> construct (CallContext context, Class c) {
        return construct(context, TypeManager.get(context, c));
    }

    static public<T> GenericVectorImplList<T> construct (CallContext context, Type component_type, java.util.List vector) {
        Factory_GenericVectorList<T> factory = new Factory_GenericVectorList<T>(context);
        factory.set_ComponentType(context, component_type);
        return factory.create(context, vector);
    }

    static public<T> GenericVectorImplList<T> construct (CallContext context, Class c, java.util.List vector) {
        return construct(context, TypeManager.get(context, c), vector);
    }

    public Factory_GenericVectorList (CallContext context) {
    }

    public Factory_GenericVectorList () {
    }

    public GenericVectorImplList<T> create (CallContext context) {
        GenericVectorImplList<T> vector = GenericVectorImplList.create(context, this.component_type);
        if (names != null && values != null) {
            for (int i=0; i<names.length; i++) {
                vector.set(context, i, values[i]);
            }
        }
        return vector;
    }

    public GenericVectorImplList<T> create (CallContext context, java.util.List list) {
        GenericVectorImplList<T> vector = GenericVectorImplList.create(context, this.component_type, list);
        if (names != null && values != null) {
            for (int i=0; i<names.length; i++) {
                vector.set(context, i, values[i]);
            }
        }
        return vector;
    }

    public void set_ParametersAtOnce(CallContext call_context, String[] names, T[] values) {
        if (names.length != values.length) {
            Context context = Context.create(call_context);
            CustomaryContext cc = CustomaryContext.create(context);
            cc.throwPreConditionViolation(context, ManyStringPool.get(context, "0.5.0" /* Number of names differs from number of values */));
        }
        this.names = names;
        this.values = values;
    }

    static public Type get_GenericComponentTypeMethod(CallContext context, Type type) {
        if ((type instanceof TypeParametrised) == false) { return null; }
        Vector_Object_long_ ps = ((TypeParametrised) type).getParameters(context);
        if (ps == null || ps.getSize(context) != 1) { return null; }
        Object p = ps.tryGet(context, 0);
        if ((p instanceof JavaType) || (p instanceof TypeParametrised)) {
            return (Type) p;
        }
        return null;
    }

    protected Type[] runtime_types;

    public Type[] getRuntimeTypes(CallContext context) {
        if (    this.runtime_types == null
             && this.component_type != null
            ) {
            this.runtime_types = new Type[1];
            this.runtime_types[0] = this.component_type;
        }
        return this.runtime_types;
    }

    @OCPIgnore
    public void setRuntimeTypes(CallContext context, Type[] runtime_types) {
        if (    runtime_types == null
             || runtime_types.length != 1
             || runtime_types[0] == null
            ) {
            CustomaryContext.create((Context)context).throwPreConditionViolation(context, "Setting a runtime type for Factory_GenericVector requires exactly one valid type, got '%(what)'", "what", runtime_types == null ? "null array" : runtime_types.length != 1 ? ("array of length " + runtime_types.length) : "null array entry");
            throw (ExceptionPreConditionViolation) null; // compiler insists
        }
        this.runtime_types = runtime_types;
        this.set_ComponentType(context, runtime_types[0]);
    }

    public void set_ComponentType(CallContext context, Type component_type) {
        this.component_type = component_type;
    }
}
