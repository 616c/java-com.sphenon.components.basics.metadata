// instantiated with javainst.pl from /workspace/sphenon/projects/components/basics/many/v0001/origin/source/java/com/sphenon/basics/many/templates/OSetImpl.javatpl

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
import com.sphenon.basics.metadata.*;
import com.sphenon.basics.metadata.tplinst.*;

import com.sphenon.basics.many.returncodes.*;

import java.util.Hashtable;

public class OSetImpl_Object_Type_
  implements OSet_Object_Type_ {
    protected java.util.Hashtable map;
    protected Set_Object_ toplevel_set;
    static protected Type itemtype;


    public OSetImpl_Object_Type_ (CallContext context) {
        if (itemtype == null) { itemtype = TypeManager.get(context, Object.class); }
    }

    public OSetImpl_Object_Type_ (CallContext context, java.util.Hashtable map) {
        this.map = map;
        if (itemtype == null) { itemtype = TypeManager.get(context, Object.class); }
    }

    protected void initialise(CallContext context) {
        if (this.toplevel_set == null) {
            if (this.map == null) {
                this.map = new java.util.Hashtable(4);
            }
            this.toplevel_set = new SetImpl_Object_ (context);
            map.put(this.itemtype, this.toplevel_set);
        }
    }


    public Set_Object_ get     (CallContext context, Type index) throws DoesNotExist {
        if (! index.isA(context, itemtype)) DoesNotExist.createAndThrow(context);
        Object item = (map == null ? null : map.get(index));
        if (item == null) DoesNotExist.createAndThrow(context);
        return (Set_Object_) item;
    }

    public Set_Object_ getMany    (CallContext context, Type index) throws DoesNotExist {
        return this.get(context, index);
    }

    public Object      getSole    (CallContext context, Type index) throws DoesNotExist, MoreThanOne {
        Set_Object_ set = this.get(context, index);
        if (set.getSize(context) != 1) MoreThanOne.createAndThrow(context);
        return set.getNavigator(context).tryGetCurrent(context);
    }

    public Set_Object_ tryGet  (CallContext context, Type index) {
        if (! index.isA(context, itemtype)) return null;
        return (Set_Object_) (map == null ? null : map.get(index));
    }

    public Set_Object_ tryGetMany (CallContext context, Type index) {
        return this.tryGet(context, index);
    }

    public Object      tryGetSole (CallContext context, Type index) {
        Set_Object_ set = this.tryGet(context, index);
        if (set == null || set.getSize(context) != 1) return null;
        return set.getNavigator(context).tryGetCurrent(context);
    }

    public boolean  canGet  (CallContext context, Type index) {
        if (! index.isA(context, itemtype)) return false;
        return (map == null ? false : map.containsKey(index));
    }

    public boolean       canGetMany (CallContext context, Type index) {
        return this.canGet(context, index);
   
    }

    public boolean       canGetSole (CallContext context, Type index) {
        Set_Object_ set = this.tryGet(context, index);
        if (set == null || set.getSize(context) != 1) return false;
        return true;
    }

    private Set_Object_ getEntry(CallContext context, Type index) {
        initialise(context);
        Set_Object_ set = (Set_Object_) map.get(index);
        if (set == null) {
            set = new SetImpl_Object_ (context);
            map.put(index, set);
        }
        return set;
    }

    private void    _setSuper  (CallContext context, Object item, Type index) {
        if (index.equals(itemtype)) return;
        // Iterator_Type_ supertypes = index.getSuperTypes(context).getNavigator(context);
        Iterator_Type_ supertypes = index.getAllSuperInterfaces(context).getNavigator(context);
        Type supertype;
        while ((supertype = supertypes.tryGetCurrent(context)) != null) {
            if (supertype.isA(context, itemtype)) {
                this.getEntry(context, supertype).set(context, item);
                // this._setSuper(context, item, supertype);
            }
            supertypes.next(context);
        }
    }

    private void    _unsetSuper  (CallContext context, Object item, Type index) {
        if (index.equals(itemtype)) return;
        // Iterator_Type_ supertypes = index.getSuperTypes(context).getNavigator(context);
        Iterator_Type_ supertypes = index.getAllSuperInterfaces(context).getNavigator(context);
        Type supertype;
        while ((supertype = supertypes.tryGetCurrent(context)) != null) {
            if (supertype.isA(context, itemtype)) {
                this.getEntry(context, supertype).unset(context, item);
                // this._unsetSuper(context, item, supertype);
            }
            supertypes.next(context);
        }
    }

    public void     set     (CallContext context, Object item) {
        Type index = TypeManager.get(context, item.getClass());
        this.getEntry(context, index).set(context, item);
        this._setSuper(context, item, index);
    }

    public void     add     (CallContext context, Object item) throws AlreadyExists {
        Type index = TypeManager.get(context, item.getClass());
        this.getEntry(context, index).add(context, item);
        this._setSuper(context, item, index);
    }

    public void     replace (CallContext context, Object item) throws DoesNotExist {
        Type index = TypeManager.get(context, item.getClass());
        this.getEntry(context, index).replace(context, item);
        this._setSuper(context, item, index);
    }

    public void     unset   (CallContext context, Object item) {
        Type index = TypeManager.get(context, item.getClass());
        this.getEntry(context, index).unset(context, item);
        this._unsetSuper(context, item, index);
    }

    public void     remove  (CallContext context, Object item) throws DoesNotExist {
        Type index = TypeManager.get(context, item.getClass());
        this.getEntry(context, index).remove(context, item);
        this._unsetSuper(context, item, index);
    }

    public Iterator_Object_ getNavigator (CallContext context) {
        initialise(context);
        return this.toplevel_set.getNavigator(context);
    }

    public long     getSize (CallContext context) {
        return (this.itemtype == null || this.toplevel_set == null ? 0L : this.toplevel_set.getSize(context));
    }
}
