package com.sphenon.basics.many;

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
import com.sphenon.engines.aggregator.*;
import com.sphenon.basics.metadata.Type;
import com.sphenon.basics.metadata.Typed;
import com.sphenon.basics.metadata.TypeManager;

import com.sphenon.basics.many.returncodes.*;

import com.sphenon.engines.aggregator.annotations.*;

import java.util.List;

abstract public class VectorAdapter1ToNBase<T1,T2>
  implements GenericVector<T1>,
             ManagedResource,
             Typed,
             OCPSerialisable {

    private GenericVector<T2> source_vector;

    public VectorAdapter1ToNBase (CallContext context, Type component_type, GenericVector<T2> source_vector) {
        this.source_vector = source_vector;
        this.setComponentType(context, component_type);
    }

    public VectorAdapter1ToNBase (CallContext context, Type component_type) {
        this.setComponentType(context, component_type);
    }

    public void setSourceVector (CallContext context, GenericVector<T2> source_vector) {
        this.source_vector = source_vector;
    }

    public GenericVector<T2> getSourceVector (CallContext context) {
        return source_vector;
    }

    protected void setComponentType(CallContext context, Type component_type) {
        this.type = TypeManager.getParametrised(context, GenericVector.class, component_type);
    }

    protected Type type;

    public Type getType(CallContext context) {
        return type;
    }

    public void setType(CallContext context, Type type) {
        this.type = type;
    }

    abstract protected List<T1> convert(CallContext context, T2 source_item);

    public T1 get (CallContext context, long index) throws DoesNotExist {
        int source_index = 0;
        int lower = 0;
        int upper;
        List<T1> items = null;
        while (
            (upper = lower + 
                     (  (items = this.convert(context, this.source_vector.get(context, source_index++))) == null ?
                        0 : items.size()
                     )
            ) <= index
              ) { lower = upper; }
        return items.get((int)(index - lower));
    }

    public T1 tryGet (CallContext context, long index) {
        T2 source_item;
        int source_index = 0;
        int lower = 0;
        int upper;
        List<T1> items = null;
        while (
            (source_item = this.source_vector.tryGet(context, source_index++)) != null &&
            (upper = lower + 
                     (  (items = this.convert(context, source_item)) == null ?
                        0 : items.size()
                     )
            ) <= index
              ) { lower = upper; }
        if (source_item == null) { return null; }
        return items.get((int)(index - lower));
    }

    public boolean canGet (CallContext context, long index) {
        return index < this.getSize(context);
    }

    public VectorReferenceToMember<T1> getReference (CallContext context, long index) throws DoesNotExist {
        if ( ! canGet(context, index)) {
            DoesNotExist.createAndThrow (context);
            throw (DoesNotExist) null; // compiler insists
        }
        return new VectorReferenceToMember<T1>(context, this, index);
    }

    public VectorReferenceToMember<T1> tryGetReference (CallContext context, long index) {
        if ( ! canGet(context, index)) { return null; }
        return new VectorReferenceToMember<T1>(context, this, index);
    }

    public T1 set (CallContext context, long index, T1 item) {
        CustomaryContext.create((Context)context).throwLimitation(context, "cannot modify VectorAdapter1ToN, no N to 1 reconversion implemented");
        throw (ExceptionLimitation) null; // compiler insists
    }

    public void add (CallContext context, long index, T1 item) throws AlreadyExists {
        CustomaryContext.create((Context)context).throwLimitation(context, "cannot modify VectorAdapter1ToN, no N to 1 reconversion implemented");
        throw (ExceptionLimitation) null; // compiler insists
    }

    public void prepend (CallContext context, T1 item) {
        CustomaryContext.create((Context)context).throwLimitation(context, "cannot modify VectorAdapter1ToN, no N to 1 reconversion implemented");
        throw (ExceptionLimitation) null; // compiler insists
    }

    public void append (CallContext context, T1 item) {
        CustomaryContext.create((Context)context).throwLimitation(context, "cannot modify VectorAdapter1ToN, no N to 1 reconversion implemented");
        throw (ExceptionLimitation) null; // compiler insists
    }

    public void insertBefore (CallContext context, long index, T1 item) throws DoesNotExist {
        CustomaryContext.create((Context)context).throwLimitation(context, "cannot modify VectorAdapter1ToN, no N to 1 reconversion implemented");
        throw (ExceptionLimitation) null; // compiler insists
    }

    public void insertBehind (CallContext context, long index, T1 item) throws DoesNotExist {
        CustomaryContext.create((Context)context).throwLimitation(context, "cannot modify VectorAdapter1ToN, no N to 1 reconversion implemented");
        throw (ExceptionLimitation) null; // compiler insists
    }

    public T1 replace (CallContext context, long index, T1 item) throws DoesNotExist {
        CustomaryContext.create((Context)context).throwLimitation(context, "cannot modify VectorAdapter1ToN, no N to 1 reconversion implemented");
        throw (ExceptionLimitation) null; // compiler insists
    }

    public T1 unset (CallContext context, long index) {
        CustomaryContext.create((Context)context).throwLimitation(context, "cannot modify VectorAdapter1ToN, no N to 1 reconversion implemented");
        throw (ExceptionLimitation) null; // compiler insists
    }

    public T1 remove (CallContext context, long index) throws DoesNotExist {
        CustomaryContext.create((Context)context).throwLimitation(context, "cannot modify VectorAdapter1ToN, no N to 1 reconversion implemented");
        throw (ExceptionLimitation) null; // compiler insists
    }

    public IteratorItemIndex<T1> getNavigator (CallContext context) {
        return new VectorIteratorImpl<T1> (context, this);
    }

    protected long size = -1;

    public long getSize (CallContext context) {
        if (this.size == -1) {
            size = 0;
            for (int i=0; i<this.source_vector.getSize(context); i++) {
                this.size += this.convert(context, this.source_vector.tryGet(context, i)).size();
            }
        }
        return this.size;
    }

    protected class IteratorAdapter implements java.util.Iterator<T1> {
        protected IteratorItemIndex<T1> iterator;
        protected CallContext context;
        public IteratorAdapter(CallContext context, IteratorItemIndex<T1> iterator) {
            this.iterator = iterator;
            this.context = context;
        }
        public boolean hasNext() { return iterator.canGetCurrent(this.context); }
        public void remove() { throw new UnsupportedOperationException(); }
        public T1 next() {
            if (iterator.canGetCurrent(this.context) == false) {
                throw new java.util.NoSuchElementException();
            }
            T1 current = iterator.tryGetCurrent(this.context);
            iterator.next(this.context);
            return current;
        }
    }

    public java.util.Iterator<T1> getIterator (CallContext context) {
        return new IteratorAdapter(context, this.getNavigator(context));
    }

    public VectorIterable<T1> getIterable (CallContext context) {
        return new VectorIterable<T1>(context, this);
    }

    public void release(CallContext context) {
        if (this.source_vector != null && this.source_vector instanceof ManagedResource) {
            ((ManagedResource)(this.source_vector)).release(context);
        }
    }

    public String ocpDefaultName(CallContext context) {
        return "Vector";
    }

    public String ocpClass(CallContext context) {
        return null;
    }

    public String ocpFactory(CallContext context) {
        return null;
    }

    public String ocpRetriever(CallContext context) {
        return null;
    }

    public boolean ocpContainsData(CallContext context) {
        return false;
    }

    public boolean ocpEmpty(CallContext context) {
        return this.getSize(context) == 0;
    }

    public void ocpSerialise(CallContext context, com.sphenon.engines.aggregator.OCPSerialiser serialiser, boolean as_reference) {
        int i=1;
        for (Object o : this.getIterable(context)) {
            serialiser.serialise(context, o, "Entry" + (i++), as_reference);
                                                              // hmm, debatable: pass reference or create
                                                              // reference to vector itself?
                                                              // should be ok, but meaning of "as_reference"
                                                              // is not really clear here
        }
    }
}
