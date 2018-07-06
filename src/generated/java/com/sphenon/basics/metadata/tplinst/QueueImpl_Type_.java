// instantiated with javainst.pl from /workspace/sphenon/projects/components/basics/many/v0001/origin/source/java/com/sphenon/basics/many/templates/QueueImpl.javatpl

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

import com.sphenon.basics.many.returncodes.*;

public class QueueImpl_Type_
  implements Queue_Type_
{
    private java.util.LinkedList list;

    public QueueImpl_Type_ (CallContext context)
    {
        list = new java.util.LinkedList ();
    }

    public QueueImpl_Type_ (CallContext context, java.util.LinkedList list)
    {
        this.list = list;
    }

    public Type getFront     (CallContext context) throws DoesNotExist
    {
        try {
            return (Type) list.getFirst();
        } catch (java.util.NoSuchElementException e) {
            DoesNotExist.createAndThrow (context);
            throw (DoesNotExist) null; // compiler insists
        }
    }

    public Type tryGetFront  (CallContext context)
    {
        try {
            return (Type) list.getFirst();
        } catch (java.util.NoSuchElementException e) {
            return null;
        }
    }

    public boolean  isEmpty      (CallContext context)
    {
        return (list.size() == 0) ? true : false;
    }

    public void     pushBack     (CallContext context, Type item)
    {
        list.addLast(item);
    }

    public Type popFront     (CallContext context) throws DoesNotExist
    {
        try {
            return (Type) list.removeFirst();
        } catch (java.util.NoSuchElementException e) {
            DoesNotExist.createAndThrow (context);
            throw (DoesNotExist) null; // compiler insists
        }
    }

    public Type tryPopFront  (CallContext context)
    {
        try {
            return (Type) list.removeFirst();
        } catch (java.util.NoSuchElementException e) {
            return null;
        }
    }
}

