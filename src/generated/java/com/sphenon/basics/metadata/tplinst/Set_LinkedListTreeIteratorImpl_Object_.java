// instantiated with javainst.pl from /workspace/sphenon/projects/components/basics/many/v0001/origin/source/java/com/sphenon/basics/many/templates/Set_LinkedListTreeIteratorImpl.javatpl

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
// please do not modify this file directly
package com.sphenon.basics.metadata.tplinst;

import com.sphenon.basics.metadata.*;
import com.sphenon.basics.metadata.traits.*;

import com.sphenon.basics.context.*;
import com.sphenon.basics.exception.*;
import com.sphenon.basics.customary.*;

import com.sphenon.basics.many.returncodes.*;

import java.util.Hashtable;

public class Set_LinkedListTreeIteratorImpl_Object_
    implements Iterator_Object_
{
    private java.util.LinkedList linked_list;
    private java.util.ListIterator list_iterator;
    Set_LinkedListTreeIteratorImpl_Object_ sub_iterator;
    private Object current;

    public Set_LinkedListTreeIteratorImpl_Object_ (CallContext context, java.util.LinkedList linked_list) {
        this.linked_list = linked_list;
        this.list_iterator = linked_list.listIterator(0);

        this.next(context);
    }

    public void     next          (CallContext context)
    {
        if (sub_iterator != null) {
            sub_iterator.next(context);
            if (sub_iterator.canGetCurrent(context)) {
                this.current = sub_iterator.tryGetCurrent(context);
                return;
            }
            sub_iterator = null;
        }
        if (this.list_iterator.hasNext()) {
            Object o = this.list_iterator.next();
            if (o == null || o instanceof Object) {
                this.current = (Object) o;
            } else {
                Set_LinkedListTreeImpl_Object_ sllti = ((Set_LinkedListTreeImpl_Object_)o);
                sub_iterator = (Set_LinkedListTreeIteratorImpl_Object_) (sllti.getNavigator(context));
                if (sub_iterator.canGetCurrent(context)) {
                    this.current = sub_iterator.tryGetCurrent(context);
                } else {
                    sub_iterator = null;
                    next(context);
                }
            }
        } else {
            this.current = null;
        }
    }

    public Object getCurrent    (CallContext context) throws DoesNotExist
    {
        if (this.current == null) DoesNotExist.createAndThrow(context);
        return this.current;
    }

    public Object tryGetCurrent (CallContext context)
    {
        return this.current;
    }

    public boolean  canGetCurrent (CallContext context)
    {
        return (this.current != null) ? true : false;
    }

    public SetIteratorImpl_Object_ clone(CallContext context) {
        CustomaryContext.create((Context)context).throwLimitation(context, "cannot clone, Set_LinkedListTreeIteratorImpl is not cloneable");
        throw (ExceptionLimitation) null; // compiler insists
    }
}

