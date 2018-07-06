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
import com.sphenon.basics.exception.*;
import com.sphenon.basics.notification.*;
import com.sphenon.basics.customary.*;
import com.sphenon.basics.debug.*;
import com.sphenon.basics.data.DataSource;
import com.sphenon.basics.interaction.*;
import com.sphenon.engines.aggregator.*;
import com.sphenon.basics.metadata.Type;
import com.sphenon.basics.metadata.TypedImpl;
import com.sphenon.basics.metadata.TypeManager;

import com.sphenon.basics.many.returncodes.*;

import java.util.Vector;

public class GenericVectorImpl<T>
    implements GenericVector<T>,
    ReadOnlyVector<T>,
    GenericIterable<T>,
    VectorOptimized<T>,
    VectorReorderable<T>,
    Dumpable,
    ManagedResource,
    Anchorable,
    TypedImpl,
    OCPSerialisable
    {
    private java.util.Vector vector;
    private DataSource<Vector> vector_source;


    protected GenericVectorImpl (CallContext context, Type component_type) {
        this.vector = new java.util.Vector ();
        this.type = TypeManager.getParametrised(context, GenericVector.class, component_type);
    }

    static public<T> GenericVectorImpl<T> create (CallContext context, Type component_type) {
        return new GenericVectorImpl<T>(context, component_type);
    }

    protected GenericVectorImpl (CallContext context, Type component_type, java.util.Vector vector) {
        this.vector = vector;
        this.type = TypeManager.getParametrised(context, GenericVector.class, component_type);
    }

    static public<T> GenericVectorImpl<T> create (CallContext context, Type component_type, java.util.Vector vector) {
        return new GenericVectorImpl<T>(context, component_type, vector);
    }

    protected GenericVectorImpl (CallContext context, Type component_type, DataSource<Vector> vector_source) {
        this.vector_source = vector_source;
        this.type = TypeManager.getParametrised(context, GenericVector.class, component_type);
    }

    static public<T> GenericVectorImpl<T> create (CallContext context, Type component_type, DataSource<Vector> vector_source, int hampelmann) {
        return new GenericVectorImpl<T>(context, component_type, vector_source);
    }

    static public<T> GenericVectorImpl<T> create (CallContext context, String component_type, DataSource<Vector> vector_source, int hampelmann) {
        return new GenericVectorImpl<T>(context, TypeManager.tryGet(context, component_type), vector_source);
    }

    protected java.util.Vector getVector(CallContext context) {
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
            return (T) this.getVector(context).elementAt((int) index);
        } catch (ArrayIndexOutOfBoundsException e) {
            DoesNotExist.createAndThrow (context);
            throw (DoesNotExist) null; // compiler insists
        }
    }

    public T tryGet       (CallContext context, long index) {
        try {
            return (T) this.getVector(context).elementAt((int) index);
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    public boolean  canGet       (CallContext context, long index) {
        return (index >= 0 && index < this.getVector(context).size()) ? true : false;
    }

    public VectorReferenceToMember<T> getReference    (CallContext context, long index) throws DoesNotExist {
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
        if (index >= this.getVector(context).size()) { this.getVector(context).setSize((int) (index+1)); }
        return (T) this.getVector(context).set((int) index, item);
    }

    public void     add          (CallContext context, long index, T item) throws AlreadyExists {
        if (index < this.getVector(context).size()) { AlreadyExists.createAndThrow (context); }
        this.getVector(context).setSize((int) (index+1));
        this.getVector(context).setElementAt(item, (int) index);
    }

    public void     prepend      (CallContext context, T item) {
        if (this.getVector(context).size() == 0) {
            this.getVector(context).add(item);
        } else {
            try {
                this.getVector(context).insertElementAt(item, 0);
            } catch (ArrayIndexOutOfBoundsException e) {
                CustomaryContext.create((Context)context).throwImpossibleState(context, ManyStringPool.get(context, "0.0.1" /* cannot insert element at position 0, java-lib says 'out of bounds' ??? */));
            }
        }
    }

    public void     append       (CallContext context, T item) {
        this.getVector(context).add(item);
    }

    public void     insertBefore (CallContext context, long index, T item) throws DoesNotExist {
        try {
            this.getVector(context).insertElementAt(item, (int) index);
        } catch (ArrayIndexOutOfBoundsException e) {
            DoesNotExist.createAndThrow(context);
        }
    }

    public void     insertBehind (CallContext context, long index, T item) throws DoesNotExist {
        if (index == this.getVector(context).size() - 1) {
            this.getVector(context).add(item);
        } else {
            try {
                this.getVector(context).insertElementAt(item, (int) (index+1));
            } catch (ArrayIndexOutOfBoundsException e) {
                DoesNotExist.createAndThrow (context);
            }
        }
    }

    public T replace      (CallContext context, long index, T item) throws DoesNotExist {
        try {
            return (T) this.getVector(context).set((int) index, item);
        } catch (ArrayIndexOutOfBoundsException e) {
            DoesNotExist.createAndThrow(context);
            throw (DoesNotExist) null;
        } catch (IllegalArgumentException e) {
            CustomaryContext.create((Context)context).throwImpossibleState (context, ManyStringPool.get(context, "0.0.2" /* An exception occured, with respect to which the java-lib documentation is unfortunately incorrect */));
            throw (ExceptionImpossibleState) null;
        }
    }

    public T unset        (CallContext context, long index) {
        try {
            return (T) this.getVector(context).remove((int) index);
        } catch (ArrayIndexOutOfBoundsException e) {
            // we kindly ignore this exception
            return null;
        }
    }

    public T remove       (CallContext context, long index) throws DoesNotExist {
        try {
            return (T) this.getVector(context).remove((int) index);
        } catch (ArrayIndexOutOfBoundsException e) {
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

    public java.util.Iterator<T> getIterator (CallContext context) {
        return (java.util.Iterator<T>) this.getVector(context).iterator();
    }

    public VectorIterable<T> getIterable (CallContext context) {
        return new VectorIterable<T>(context, this);
    }

    public java.util.Vector getImplementationVector(CallContext context){
      return this.getVector(context);
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
        Vector<T> vector = this.getVector(context);

        if (from_index == to_index) { return; }

        if (   from_index < 0 || from_index >= vector.size()
            || to_index   < 0 || to_index   >= vector.size()
           ) {
            DoesNotExist.createAndThrow(context);
        }

        T item = vector.remove((int) from_index);

        vector.insertElementAt(item, (int) to_index);
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
