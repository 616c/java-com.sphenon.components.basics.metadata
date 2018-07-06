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
import com.sphenon.basics.data.*;
import com.sphenon.engines.aggregator.*;
import com.sphenon.basics.metadata.Type;
import com.sphenon.basics.metadata.Typed;
import com.sphenon.basics.metadata.TypeManager;

import com.sphenon.basics.many.returncodes.*;

import com.sphenon.engines.aggregator.annotations.*;

abstract public class VectorAdapterBase<T1,T2>
  implements GenericVector<T1>,
             VectorOptimized<T1>,
             VectorReorderable<T1>,
             ManagedResource,
             Typed,
             OCPSerialisable {

    private GenericVector<T2> source_vector;
    private DataSource<GenericVector<T2>> source_vector_ds;

    public VectorAdapterBase (CallContext context, Type component_type, GenericVector<T2> source_vector) {
        this.source_vector = source_vector;
        this.setComponentType(context, component_type);
    }

    public VectorAdapterBase (CallContext context, Type component_type, DataSource<GenericVector<T2>> source_vector_ds) {
        this.source_vector_ds = source_vector_ds;
        this.setComponentType(context, component_type);
    }

    public VectorAdapterBase (CallContext context, Type component_type) {
        this.setComponentType(context, component_type);
    }

    public void setSourceVector (CallContext context, GenericVector<T2> source_vector) {
        this.source_vector = source_vector;
    }

    public void setSourceVectorDS (CallContext context, DataSource<GenericVector<T2>> source_vector_ds) {
        this.source_vector_ds = source_vector_ds;
    }

    public GenericVector<T2> getSourceVector (CallContext context) {
        return source_vector != null ? source_vector : source_vector_ds != null ? (source_vector = source_vector_ds.get(context)) : null;
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

    abstract protected T1 convert(CallContext context, T2 source_item);

    abstract protected T2 reconvert(CallContext context, T1 target_item);

    public T1 get          (CallContext context, long index) throws DoesNotExist {
        T2 result = this.getSourceVector(context).get(context, index);
        return this.convert(context, result);
    }

    public T1 tryGet       (CallContext context, long index) {
        T2 result = this.getSourceVector(context).tryGet(context, index);
        return result == null ? null : this.convert(context, result);
    }

    public boolean  canGet       (CallContext context, long index) {
        return this.getSourceVector(context).canGet(context, index);
    }

    public VectorReferenceToMember<T1> getReference    (CallContext context, long index) throws DoesNotExist {
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

    public T1 set          (CallContext context, long index, T1 item) {
        return this.convert(context, this.getSourceVector(context).set(context, index, this.reconvert(context, item)));
    }

    public void     add          (CallContext context, long index, T1 item) throws AlreadyExists {
        this.getSourceVector(context).add(context, index, this.reconvert(context, item));
    }

    public void     prepend      (CallContext context, T1 item) {
        this.getSourceVector(context).prepend(context, this.reconvert(context, item));
    }

    public void     append       (CallContext context, T1 item) {
        this.getSourceVector(context).append(context, this.reconvert(context, item));
    }

    public void     insertBefore (CallContext context, long index, T1 item) throws DoesNotExist {
        this.getSourceVector(context).insertBefore(context, index, this.reconvert(context, item));
    }

    public void     insertBehind (CallContext context, long index, T1 item) throws DoesNotExist {
        this.getSourceVector(context).insertBehind(context, index, this.reconvert(context, item));
    }

    public T1 replace      (CallContext context, long index, T1 item) throws DoesNotExist {
        return this.convert(context, this.getSourceVector(context).replace(context, index, this.reconvert(context, item)));
    }

    public T1 unset        (CallContext context, long index) {
        return this.convert(context, this.getSourceVector(context).unset(context, index));
    }

    public T1 remove       (CallContext context, long index) throws DoesNotExist {
        return this.convert(context, this.getSourceVector(context).remove(context, index));
    }

    public IteratorItemIndex<T1> getNavigator (CallContext context) {
        return new VectorIteratorImpl<T1> (context, this);
    }

    public long     getSize      (CallContext context) {
        return this.getSourceVector(context).getSize(context);
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

    protected VectorOptimized<T2> getOptimizedSourceVector(CallContext context) {
        if (getSourceVector(context) instanceof VectorOptimized)  {
            return ((VectorOptimized<T2>) getSourceVector(context));
        } else {
            CustomaryContext.create((Context)context).throwLimitation(context, "Source vector in VectorAdapterBase<T1,T2> does not support optimized interface");
            throw (ExceptionLimitation) null; // compiler insists
        }
    }

    public boolean contains(CallContext context, T1 item) {
        return this.getOptimizedSourceVector(context).contains(context, this.reconvert(context, item));
    }

    public boolean removeFirst(CallContext context, T1 item) {
        return this.getOptimizedSourceVector(context).removeFirst(context, this.reconvert(context, item));
    }

    public void removeAll(CallContext context, T1 item, VectorOptimized.Notifier<T1> notifier) {
        VectorOptimized.Notifier<T2> my_notifier = null;
        if (notifier != null) {
            my_notifier = new MyNotifier(context, notifier);
        }
        this.getOptimizedSourceVector(context).removeAll(context, this.reconvert(context, item), my_notifier);
    }

    public void release(CallContext context) {
        if (this.getSourceVector(context) != null && this.getSourceVector(context) instanceof ManagedResource) {
            ((ManagedResource)(this.getSourceVector(context))).release(context);
        }
    }

    protected class MyNotifier implements VectorOptimized.Notifier<T2> {
        protected VectorOptimized.Notifier<T1> notifier;
        public MyNotifier(CallContext context, VectorOptimized.Notifier<T1> notifier) {
            this.notifier = notifier;
        }
        public void onAdd(CallContext context, T2 item) {
            if (notifier != null) { notifier.onAdd(context, convert(context, item)); }
        }
        public void onRemove(CallContext context, T2 item) {
            if (notifier != null) { notifier.onRemove(context, convert(context, item)); }
        }
    }        

    public boolean isReorderable(CallContext context) {
        return (this.getSourceVector(context) instanceof VectorReorderable && ((VectorReorderable)(this.getSourceVector(context))).isReorderable(context));
    }

    public void move (CallContext context, long from_index, long to_index) throws DoesNotExist {
        ((VectorReorderable)(this.getSourceVector(context))).move(context, from_index, to_index);
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
        return this.getSourceVector(context) == null || this.getSourceVector(context).getSize(context) == 0;
    }

    public void ocpSerialise(CallContext context, com.sphenon.engines.aggregator.OCPSerialiser serialiser, boolean as_reference) {
        int i=1;
        if (this.getSourceVector(context) != null) {
            for (Object o : this.getIterable(context)) {
                serialiser.serialise(context, o, "Entry" + (i++), as_reference);
                                                                  // hmm, debatable: pass reference or create
                                                                  // reference to vector itself?
                                                                  // should be ok, but meaning of "as_reference"
                                                                  // is not really clear here
            }
        }
    }
}
