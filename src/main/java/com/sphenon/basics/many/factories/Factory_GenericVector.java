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
import com.sphenon.basics.factory.*;
import com.sphenon.basics.validation.returncodes.*;
import com.sphenon.basics.metadata.*;
import com.sphenon.basics.interaction.*;

import com.sphenon.basics.many.*;
import com.sphenon.basics.many.tplinst.*;

import com.sphenon.engines.aggregator.annotations.*;

public class Factory_GenericVector<T> implements Factory, GenericWithRunTimeTypes
{
    protected String[] names;
    protected T[]      values;
    protected Type     component_type;

    static public<T> GenericVector<T> construct (CallContext context, Type component_type) {
        Factory_GenericVector<T> factory = new Factory_GenericVector<T>(context);
        factory.set_ComponentType(context, component_type);
        return factory.create(context);
    }

    static public<T> GenericVector<T> construct (CallContext context, Class c) {
        return construct(context, TypeManager.get(context, c));
    }

    public Factory_GenericVector (CallContext context) {
    }

    public Factory_GenericVector () {
    }

    protected GenericVector<T> instance;

    static public class _Factory<T> {
        static public _Factory factory;
        protected _Factory (CallContext context) {
        }
        static public<T> GenericVector<T> create (CallContext context, Type component_type) {
            return factory.doCreate(context, component_type);
        }
        protected GenericVector<T> doCreate (CallContext context, Type component_type) {
            return GenericVectorImpl.create(context, component_type);
        }
    }

    public GenericVector<T> precreate (CallContext context) {
        return (this.instance = _Factory.create(context, this.component_type));
    }

    public GenericVector<T> create (CallContext context) {
        GenericVector<T> vector = this.instance;
        if (vector == null) {
            vector = _Factory.create(context, this.component_type);
        }
        this.instance = null;
        if (names != null && values != null) {
            for (int i=0; i<names.length; i++) {
                vector.set(context, i, values[i]);
            }
        }
        return vector;
    }

    public void set_ParametersAtOnce(CallContext call_context, String[] names, T [] values) {
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

    public void validate (CallContext context) throws ValidationFailure {
    }

    public void confirmAttributes (CallContext context) {
    }

    public void validateFinally (CallContext context) throws ValidationFailure {
    }

    public Object createObject (CallContext context) throws ValidationFailure {
        return create(context);
    }

    public void reset (CallContext context) {
    }
}
