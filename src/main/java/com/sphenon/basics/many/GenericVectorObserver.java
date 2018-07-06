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
import com.sphenon.basics.debug.*;
import com.sphenon.basics.customary.*;
import com.sphenon.basics.exception.*;
import com.sphenon.basics.interaction.*;
import com.sphenon.basics.event.*;
import com.sphenon.basics.event.tplinst.*;
import com.sphenon.basics.validation.returncodes.*;
import com.sphenon.ad.adcore.ModificationType;
import com.sphenon.engines.aggregator.*;
import com.sphenon.basics.metadata.Type;
import com.sphenon.basics.metadata.Typed;

import com.sphenon.basics.many.returncodes.*;

public class GenericVectorObserver<T>
    implements GenericVector<T>,
               VectorOptimized<T>,
               VectorModifiability<T>,
               VectorReorderable<T>,
               Changing,
               Dumpable,
               ManagedResource,
               Anchorable,
               Typed,
               OCPSerialisable
{
    protected GenericVector<T> observed_vector;
    protected boolean need_notification;
    protected VectorObserverEventPolicy event_policy = VectorObserverEventPolicy.ON_DEMAND;

    protected VectorObserverEventPolicy getEventPolicy(CallContext context) {
        return this.event_policy;
    }

    protected void setEventPolicy(CallContext context, VectorObserverEventPolicy event_policy) {
        this.event_policy = event_policy;
    }

    public GenericVectorObserver (CallContext context, GenericVector<T> observed_vector, VectorObserverEventPolicy event_policy, ModificationType modification_type) {
        this.setEventPolicy(context,event_policy);
        this.observed_vector = observed_vector;
        need_notification = false;
        if (this.observed_vector != null) {
            if (modification_type != ModificationType.STATE_LOAD) {
                if (    getEventPolicy(context) == VectorObserverEventPolicy.ON_DEMAND
                     || getEventPolicy(context) == VectorObserverEventPolicy.ON_DEMAND_AND_LOAD
                   ) {
                    need_notification = true;
                } else if (getEventPolicy(context) == VectorObserverEventPolicy.IMMEDIATELY) {
                    for (T item : observed_vector.getIterable(context)) {
                        onAdd(context, item, null);
                    }
                }
            } else {
                if (getEventPolicy(context) == VectorObserverEventPolicy.ON_DEMAND_AND_LOAD) {
                    for (T item : observed_vector.getIterable(context)) {
                        onAdd(context, item, modification_type);
                    }
                }
            }
        }
    }

    public GenericVectorObserver (CallContext context, GenericVector<T> observed_vector, VectorObserverEventPolicy event_policy) {
        this(context, observed_vector, event_policy, null);
    }

    public GenericVectorObserver (CallContext context, GenericVector<T> observed_vector) {
        this(context, observed_vector, VectorObserverEventPolicy.ON_DEMAND);
    }

    public Type getType(CallContext context) {
        GenericVector<T> gvo = this.getObserved(context);
        return (gvo instanceof Typed) ? ((Typed) gvo).getType(context) : null;
    }

    // -----------------------------------------------------------------------
    // -- Anchorable ---------------------------------------------------------
    public com.sphenon.basics.interaction.Anchor createAnchor(CallContext context, Workspace workspace, Transaction transaction) {
        return (com.sphenon.basics.interaction.Anchor) com.sphenon.basics.interaction.AnchorInterceptor.wrap(context, this, workspace, transaction);
    }
    // -----------------------------------------------------------------------

    public void setObserved (CallContext context, GenericVector<T> observed_vector) {
        if (this.getObserved(context) != null) {
            for (T item : this.observed_vector.getIterable(context)) {
                onRemove(context, item, null);
            }
        }

        this.observed_vector = observed_vector;

        need_notification = false;
        if (this.observed_vector != null) {

            if (    getEventPolicy(context) == VectorObserverEventPolicy.ON_DEMAND
                 || getEventPolicy(context) == VectorObserverEventPolicy.ON_DEMAND_AND_LOAD
                 || getEventPolicy(context) == VectorObserverEventPolicy.ON_DEMAND_ONLY_EXPLICIT) {
                need_notification = true;
            } else if (    getEventPolicy(context) == VectorObserverEventPolicy.IMMEDIATELY
                        || getEventPolicy(context) == VectorObserverEventPolicy.IMMEDIATELY_ONLY_EXPLICIT) {
                for (T item : this.observed_vector.getIterable(context)) {
                    onAdd(context, item, null);
                }
            }
        }
    }

    public GenericVector<T> getObserved (CallContext context) {
        if (need_notification && this.observed_vector != null) {
            need_notification = false;
            for (T item : this.observed_vector.getIterable(context)) {
                onAdd(context, item, null);
            }
        }
        return this.observed_vector;
    }

    // ------------------------------------------------------------------------------------
    // Aspect: Changing
    protected EventDispatcher_ChangeEvent_ change_event_dispatcher = null;
    protected java.util.Date last_update = null;

    protected void notify(CallContext context, ChangeEvent change_event) {
        if (change_event_dispatcher != null) {
            change_event_dispatcher.notify(context, change_event);
        }
        if (this.last_update != null) {
            this.last_update.setTime(System.currentTimeMillis());
        }
    }

    protected void notify(CallContext context) {
        if (change_event_dispatcher != null) {
            change_event_dispatcher.notify(context);
        }
        if (this.last_update != null) {
            this.last_update.setTime(System.currentTimeMillis());
        }
    }

    protected void onAdd(CallContext context, T item, ModificationType modification_type) {
    }

    protected void onRemove(CallContext context, T item, ModificationType modification_type) {
    }

    protected boolean hasHooks(CallContext context) {
        return false;
    }

    protected boolean needsNotification(CallContext context) {
        return (change_event_dispatcher != null && change_event_dispatcher.hasListeners(context));
    }

    public EventDispatcher_ChangeEvent_ getChangeEventDispatcher(CallContext context) {
        if (change_event_dispatcher == null) {
            this.change_event_dispatcher = new EventDispatcher_ChangeEvent_(context);
        }
        return this.change_event_dispatcher;
    }

    public java.util.Date getLastUpdate(CallContext context) {
        if (this.last_update == null) {
            this.last_update = new java.util.Date();
        }
        return this.last_update;
    }
    // ------------------------------------------------------------------------------------

    public T                                    get             (CallContext context, long index) throws DoesNotExist {
        return this.getObserved(context).get(context, index);
    }

    public T                                    tryGet          (CallContext context, long index) {
        return this.getObserved(context).tryGet(context, index);
    }

    public boolean                                     canGet          (CallContext context, long index) {
        return this.getObserved(context).canGet(context, index);
    }

    public ReferenceToMember<T,ReadOnlyVector<T>> getReference    (CallContext context, long index) throws DoesNotExist {
        if ( ! canGet(context, index)) {
            DoesNotExist.createAndThrow (context);
            throw (DoesNotExist) null; // compiler insists
        }
        return new VectorReferenceToMember<T>(context, this, index);   
    }

    public ReferenceToMember<T,ReadOnlyVector<T>> tryGetReference (CallContext context, long index) {
        if ( ! canGet(context, index)) { return null; }
        return new VectorReferenceToMember<T>(context, this, index);   
    }

    /* --------------------------------------------------------------------------------------------------------- */
    /* allows simplified implementation                                                                          */
    protected ValidationFailure canAttach (CallContext context, T item, boolean deep) {
        return null;
    }
    protected ValidationFailure canDetach (CallContext context, Long index, boolean deep) {
        return null;
    }
    /* --------------------------------------------------------------------------------------------------------- */

    public ValidationFailure    canSet          (CallContext context, long index, T item, boolean deep) {
        return canAttach(context, item, deep);
    }

    public T                                    set             (CallContext context, long index, T item) {
        ValidationFailure.assertValidationOk(context, canSet(context, index, item, true));
        T old_item = this.getObserved(context).set(context, index, item);
        if (old_item != null) { 
            onRemove(context, old_item, null); 
            notify(context, new RemoveEvent(context,old_item,index));
        }
        onAdd(context, item, null);
        notify(context, new AddEvent(context,item,index));
        return old_item;
    }

    public ValidationFailure    canAdd          (CallContext context, long index, T item, boolean deep) {
        return canAttach(context, item, deep);
    }

    public void                                        add             (CallContext context, long index, T item) throws AlreadyExists {
        ValidationFailure.assertValidationOk(context, canAdd(context, index, item, true));
        this.getObserved(context).add(context, index, item);
        onAdd(context, item, null);
        notify(context, new AddEvent(context,item,index));
    }


    public ValidationFailure    canPrepend      (CallContext context, T item, boolean deep) {
        return canAttach(context, item, deep);
    }

    public void                                        prepend         (CallContext context, T item) {
        ValidationFailure.assertValidationOk(context, canPrepend(context, item, true));
        this.getObserved(context).prepend(context, item);
        onAdd(context, item, null);
        notify(context, new AddEvent(context,item));
    }

    public ValidationFailure    canAppend       (CallContext context, T item, boolean deep) {
        return canAttach(context, item, deep);
    }

    public void                                        append          (CallContext context, T item) {
        append(context, item, null);
    }

    public void                                        append          (CallContext context, T item, ModificationType modification_type) {
        ValidationFailure.assertValidationOk(context, canAppend(context, item, true));
        this.getObserved(context).append(context, item);
        onAdd(context, item, modification_type);
        notify(context, new AddEvent(context,item));
    }

    public ValidationFailure    canInsertBefore (CallContext context, long index, T item, boolean deep) {
        return canAttach(context, item, deep);
    }

    public void                                        insertBefore    (CallContext context, long index, T item) throws DoesNotExist {
        ValidationFailure.assertValidationOk(context, canInsertBefore(context, index, item, true));
        this.getObserved(context).insertBefore(context, index, item);
        onAdd(context, item, null);
        notify(context, new AddEvent(context,item,index));
    }

    public ValidationFailure    canInsertBehind (CallContext context, long index, T item, boolean deep) {
        return canAttach(context, item, deep);
    }

    public void                                        insertBehind    (CallContext context, long index, T item) throws DoesNotExist {
        ValidationFailure.assertValidationOk(context, canInsertBehind(context, index, item, true));
        this.getObserved(context).insertBehind(context, index, item);
        onAdd(context, item, null);
        notify(context, new AddEvent(context,item,index));
    }

    public ValidationFailure    canReplace      (CallContext context, long index, T item, boolean deep) {
        return canDetach(context, index, deep);
    }

    public T                                    replace         (CallContext context, long index, T item) throws DoesNotExist {
        ValidationFailure.assertValidationOk(context, canReplace(context, index, item, true));
        T old_item = this.getObserved(context).replace(context, index, item);
        onRemove(context, old_item, null);
        notify(context, new RemoveEvent(context,old_item,index));
        onAdd(context, item, null);
        notify(context, new AddEvent(context,item,index));
        return old_item;
    }

    public ValidationFailure    canUnset        (CallContext context, long index, boolean deep) {
        return canDetach(context, index, deep);
    }

    public T                                    unset           (CallContext context, long index) {
        ValidationFailure.assertValidationOk(context, canUnset(context, index, true));
        T old_item = this.getObserved(context).unset(context, index);
        if (old_item != null) { 
           onRemove(context, old_item, null); 
           notify(context, new RemoveEvent(context,old_item,index));
        }
        
        return old_item;
    }

    public ValidationFailure    canRemove       (CallContext context, long index, boolean deep) {
        return canDetach(context, index, deep);
    }

    public T                                    remove          (CallContext context, long index) throws DoesNotExist {
        return remove(context, index, null);
    }

    public T                                    remove          (CallContext context, long index, ModificationType modification_type) throws DoesNotExist {
        ValidationFailure.assertValidationOk(context, canRemove(context, index, true));
        T old_item = this.getObserved(context).remove(context, index);
        onRemove(context, old_item, modification_type);
        notify(context, new RemoveEvent(context,old_item,index));
        return old_item;
    }

    public IteratorItemIndex<T>       getNavigator    (CallContext context) {
        return this.getObserved(context).getNavigator(context);
    }

    public long                                        getSize         (CallContext context) {
        return this.getObserved(context).getSize(context);
    }

    public java.util.Iterator<T> getIterator(CallContext context) {
        return this.getObserved(context).getIterator(context);
    }

    public VectorIterable<T> getIterable (CallContext context) {
        return new VectorIterable<T>(context, this);
    }

    protected VectorOptimized<T> getOptimizedVector(CallContext context) {
        GenericVector<T> observed = getObserved(context);
        if (observed instanceof VectorOptimized)  {
            return ((VectorOptimized<T>) observed);
        } else {
            CustomaryContext.create((Context)context).throwLimitation(context, "Observed vector in GenericVectorObserver<T> does not support optimized interface");
            throw (ExceptionLimitation) null; // compiler insists
        }
    }

    public boolean contains(CallContext context, T item) {
        return this.getOptimizedVector(context).contains(context, item);
    }

    public boolean removeFirst(CallContext context, T item) {
        return this.removeFirst(context, item, null);
    }

    public boolean removeFirst(CallContext context, T item, ModificationType modification_type) {
        if (this.getOptimizedVector(context).removeFirst(context, item)) {
            onRemove(context, item, modification_type);
            notify(context, new RemoveEvent(context,item));
            return true;
        }
        return false;
    }

    public void removeAll(CallContext context, T item, VectorOptimized.Notifier<T> notifier) {
        this.removeAll(context, item, null, notifier);
    }

    protected class MyNotifier implements VectorOptimized.Notifier<T> {
        protected VectorOptimized.Notifier<T> notifier;
        protected ModificationType modification_type;
        public MyNotifier(CallContext context, VectorOptimized.Notifier<T> notifier, ModificationType modification_type) {
            this.notifier = notifier;
            this.modification_type = modification_type;
        }
        public void onAdd(CallContext context, T item) {
            if (notifier != null) { notifier.onAdd(context, item); }
        }
        public void onRemove(CallContext context, T item) {
            GenericVectorObserver.this.onRemove(context, item, modification_type);
            GenericVectorObserver.this.notify(context, new RemoveEvent(context,item));
            if (notifier != null) { notifier.onRemove(context, item); }
        }
    }        

    public void removeAll(CallContext context, T item, ModificationType modification_type, VectorOptimized.Notifier<T> notifier) {
        VectorOptimized.Notifier<T> my_notifier = null;
        if (notifier != null || this.hasHooks(context) || this.needsNotification(context)) {
            my_notifier = new MyNotifier(context, notifier, modification_type);
        }
        this.getOptimizedVector(context).removeAll(context, item, my_notifier);
    }

    public boolean isReorderable(CallContext context) {
        return (this.getObserved(context) instanceof VectorReorderable && ((VectorReorderable)(this.getObserved(context))).isReorderable(context));
    }

    public void move (CallContext context, long from_index, long to_index) throws DoesNotExist {
        ((VectorReorderable)(this.getObserved(context))).move(context, from_index, to_index);
    }

    public void dump(CallContext context, DumpNode dump_node) {
        if (observed_vector instanceof Dumpable) {
            ((Dumpable)observed_vector).dump(context, dump_node);
        } else {
            dump_node.dump(context, observed_vector.toString());
        }
    }

    public void release(CallContext context) {
        if (this.observed_vector != null && this.observed_vector instanceof ManagedResource) {
            ((ManagedResource)(this.observed_vector)).release(context);
        }
    }

    public String ocpDefaultName(CallContext context) {
        return ((OCPSerialisable) observed_vector).ocpDefaultName(context);
    }

    public String ocpClass(CallContext context) {
        return ((OCPSerialisable) observed_vector).ocpClass(context);
    }

    public String ocpFactory(CallContext context) {
        return ((OCPSerialisable) observed_vector).ocpFactory(context);
    }

    public String ocpRetriever(CallContext context) {
        return ((OCPSerialisable) observed_vector).ocpRetriever(context);
    }

    public boolean ocpContainsData(CallContext context) {
        return ((OCPSerialisable) observed_vector).ocpContainsData(context);
    }

    public boolean ocpEmpty(CallContext context) {
        return ((OCPSerialisable) observed_vector).ocpEmpty(context);
    }

    public void ocpSerialise(CallContext context, com.sphenon.engines.aggregator.OCPSerialiser serialiser, boolean as_reference) {
        ((OCPSerialisable) observed_vector).ocpSerialise(context, serialiser, as_reference);
    }
}
