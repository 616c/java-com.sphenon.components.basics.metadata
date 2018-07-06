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

import com.sphenon.basics.context.*;
import com.sphenon.basics.debug.*;
import com.sphenon.basics.exception.*;
import com.sphenon.basics.notification.Notifier;
import com.sphenon.basics.notification.NotificationLevel;
import com.sphenon.basics.notification.NotificationContext;
import com.sphenon.basics.notification.NotificationLocationContext;
import com.sphenon.basics.customary.*;
import com.sphenon.basics.event.tplinst.EventDispatcher_ChangeEvent_;
import com.sphenon.basics.event.Changing;
import com.sphenon.basics.interaction.*;
import com.sphenon.engines.aggregator.*;
import com.sphenon.basics.metadata.Type;
import com.sphenon.basics.metadata.TypedImpl;
import com.sphenon.basics.metadata.TypeManager;

import com.sphenon.basics.many.returncodes.*;
import com.sphenon.basics.many.tplinst.*;

import java.util.Arrays;

public class GenericVectorImplList<T>
    implements GenericVector<T>,
    ReadOnlyVector<T>,
    GenericIterable<T>,
    VectorOptimized<T>,
    VectorReorderable<T>,
    VectorImplList,
    Dumpable,
    Changing,
    ManagedResource,
    Anchorable,
    TypedImpl,
    OCPSerialisable
{
    protected java.util.List vector;
    protected DataSource_List_ vector_source;
    protected EventDispatcher_ChangeEvent_ dispatcher = null;

    public EventDispatcher_ChangeEvent_  getChangeEventDispatcher(CallContext context) {
        if (this.dispatcher == null) {
            this.dispatcher = new EventDispatcher_ChangeEvent_(context);
        }
        return this.dispatcher;
    }
    
    public java.util.Date getLastUpdate(CallContext call_context) {
        return new java.util.Date();
    }

    protected GenericVectorImplList (CallContext context, Type component_type) {
        this.vector = new java.util.ArrayList ();
        this.type = TypeManager.getParametrised(context, GenericVector.class, component_type);
    }

    static public<T> GenericVectorImplList<T> create (CallContext context, Type component_type) {
        return new GenericVectorImplList<T>(context, component_type);
    }

    protected GenericVectorImplList (CallContext context, Type component_type, java.util.List vector) {
        this.vector = vector;
        this.type = TypeManager.getParametrised(context, GenericVector.class, component_type);
    }

    static public<T> GenericVectorImplList<T> create (CallContext context, Type component_type, java.util.List vector) {
        return new GenericVectorImplList<T>(context, component_type, vector);
    }

    protected GenericVectorImplList (CallContext context, Type component_type, T[] array) {
        this.vector = Arrays.asList(array);
        this.type = TypeManager.getParametrised(context, GenericVector.class, component_type);
    }

    static public<T> GenericVectorImplList<T> create (CallContext context, Type component_type, T[] array) {
        return new GenericVectorImplList<T>(context, component_type, array);
    }

    protected GenericVectorImplList (CallContext context, Type component_type, DataSource_List_ vector_source) {
        this.vector_source = vector_source;
        this.type = TypeManager.getParametrised(context, GenericVector.class, component_type);
        if (this.vector_source instanceof Changing) {
            ((Changing) this.vector_source).getChangeEventDispatcher(context).addListener(context, this.getChangeEventDispatcher(context));
        } 
    }

    static public<T> GenericVectorImplList<T> create (CallContext context, Type component_type, DataSource_List_ vector_source) {
        return new GenericVectorImplList<T>(context, component_type, vector_source);
    }

    protected java.util.List getVector(CallContext context) {
        return this.vector != null ? this.vector : this.vector_source.get(context);
    }

    protected Type type;

    public Type getType(CallContext context) {
        return type;
    }

    public void setType(CallContext context, Type type) {
        this.type = type;
    }

    // -----------------------------------------------------------------------
    // -- Anchorable ---------------------------------------------------------
    public com.sphenon.basics.interaction.Anchor createAnchor(CallContext context, Workspace workspace, Transaction transaction) {
        return (com.sphenon.basics.interaction.Anchor) com.sphenon.basics.interaction.AnchorInterceptor.wrap(context, this, workspace, transaction);
    }
    // -----------------------------------------------------------------------

    public T get          (CallContext context, long index) throws DoesNotExist {
        try {
            return (T) this.getVector(context).get((int) index);
        } catch (IndexOutOfBoundsException e) {
            DoesNotExist.createAndThrow (context);
            throw (DoesNotExist) null; // compiler insists
        }
    }

    public T tryGet       (CallContext context, long index) {
        try {
            return (T) this.getVector(context).get((int) index);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public boolean  canGet       (CallContext context, long index) {
        return (index >= 0 && index < this.getVector(context).size()) ? true : false;
    }

    public VectorReferenceToMember<T> getReference (CallContext context, long index) throws DoesNotExist {
        if ( ! canGet(context, index)) {
            DoesNotExist.createAndThrow (context);
            throw (DoesNotExist) null; // compiler insists
        }
        return new VectorReferenceToMember<T>(context, this, index);
    }

    public VectorReferenceToMember<T> tryGetReference (CallContext context, long index) {
        if ( ! canGet(context, index)) { return null; }
        return new VectorReferenceToMember<T>(context, this, index);
    }

    public T set          (CallContext context, long index, T item) {
        while (index > this.getVector(context).size()) { this.getVector(context).add(null); }
        if( index == this.getVector(context).size()) {
            this.getVector(context).add(item);
            return null;
        } else {
            return (T) this.getVector(context).set((int) index, item);
        }
    }

    public void     add          (CallContext context, long index, T item) throws AlreadyExists {
        if (index < this.getVector(context).size()) { AlreadyExists.createAndThrow (context); }
        set(context, index, item);
    }

    public void     prepend      (CallContext context, T item) {
        if (this.getVector(context).size() == 0) {
            this.getVector(context).add(item);
        } else {
            this.getVector(context).add(0, item);
        }
    }

    public void     append       (CallContext context, T item) {
        this.getVector(context).add(item);
    }

    public void     insertBefore (CallContext context, long index, T item) throws DoesNotExist {
        try {
            this.getVector(context).add((int) index, item);
        } catch (IndexOutOfBoundsException e) {
            DoesNotExist.createAndThrow(context);
        }
    }

    public void     insertBehind (CallContext context, long index, T item) throws DoesNotExist {
        if (index == this.getVector(context).size() - 1) {
            this.getVector(context).add(item);
        } else {
            try {
                this.getVector(context).add((int) index + 1, item);
            } catch (IndexOutOfBoundsException e) {
                DoesNotExist.createAndThrow (context);
            }
        }
    }

    public T replace      (CallContext context, long index, T item) throws DoesNotExist {
        try {
            return (T) this.getVector(context).set((int) index, item);
        } catch (IndexOutOfBoundsException e) {
            DoesNotExist.createAndThrow(context);
            throw (DoesNotExist) null;
        }
    }

    public T unset        (CallContext context, long index) {
        try {
            return (T) this.getVector(context).remove((int) index);
        } catch (IndexOutOfBoundsException e) {
            // we kindly ignore this exception
            return null;
        }
    }

    public T remove       (CallContext context, long index) throws DoesNotExist {
        try {
            return (T) this.getVector(context).remove((int) index);
        } catch (IndexOutOfBoundsException e) {
            DoesNotExist.createAndThrow (context);
            throw (DoesNotExist) null;
        }
    }

    public IteratorItemIndex<T> getNavigator (CallContext context) {
        return new VectorIteratorImpl<T> (context, this);
    }

    public long     getSize      (CallContext context) {
        return this.getVector(context).size();
    }

    // to be used with care
    public java.util.List<T> getImplementationList (CallContext context) {
        return this.getVector(context);
    }

    static public<T> java.util.List<T> tryGetImplementationList (CallContext context, GenericVector<T> vector) {
        return vector == null ? null : ((GenericVectorImplList<T>) vector).getImplementationList(context);
    }

    public java.util.Iterator<T> getIterator (CallContext context) {
        return this.getVector(context).iterator();
    }

    public VectorIterable<T> getIterable (CallContext context) {
        return new VectorIterable<T>(context, this);
    }

    public boolean contains(CallContext context, T item) {
        return this.getVector(context).contains(item);
    }

    public boolean removeFirst(CallContext context, T item) {
        return this.getVector(context).remove(item);
    }

    public void removeAll(CallContext context, T item, VectorOptimized.Notifier<T> notifier) {
        java.util.Iterator i = this.getVector(context).iterator();
        while (i.hasNext()) {   
            if (i.next() == item) {
                i.remove();
                if (notifier != null) { notifier.onRemove(context, item); }
            }
        }
    }

    public boolean isReorderable(CallContext context) {
        return true;
    }

    public void move (CallContext context, long from_index, long to_index) throws DoesNotExist {
        java.util.List<T> vector = this.getVector(context);

        if (from_index == to_index) { return; }

        if (   from_index < 0 || from_index >= vector.size()
            || to_index   < 0 || to_index   >= vector.size()
           ) {
            DoesNotExist.createAndThrow(context);
        }

        T item = vector.remove((int) from_index);

        vector.add((int) to_index, item);
    }

    public void dump(CallContext context, DumpNode dump_node) {
        int i=1;
        for (Object o : this.getVector(context)) {
            dump_node.dump(context, (new Integer(i++)).toString(), o);
        }
    }

    public void release(CallContext context) {
        if (this.getVector(context) != null && this.getVector(context) instanceof ManagedResource) {
            ((ManagedResource)(this.getVector(context))).release(context);
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
        return this.getVector(context) == null || this.getVector(context).size() == 0;
    }

    public void ocpSerialise(CallContext context, com.sphenon.engines.aggregator.OCPSerialiser serialiser, boolean as_reference) {
        int i=1;
        if (this.getVector(context) != null) {
            for (Object o : this.getVector(context)) {
                serialiser.serialise(context, o, "Entry" + (i++), as_reference);
                                                                  // hmm, debatable: pass reference or create
                                                                  // reference to vector itself?
                                                                  // should be ok, but meaning of "as_reference"
                                                                  // is not really clear here
            }
        }
    }
}
