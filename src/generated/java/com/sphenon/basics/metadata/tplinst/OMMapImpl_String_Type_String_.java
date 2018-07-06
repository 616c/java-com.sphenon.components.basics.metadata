// instantiated with javainst.pl from /workspace/sphenon/projects/components/basics/many/v0001/origin/source/java/com/sphenon/basics/many/templates/OMMapImpl.javatpl

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

import java.util.Hashtable;

public class OMMapImpl_String_Type_String_
  implements OMMap_String_Type_String_
{
    protected java.util.Hashtable map1;

    protected class Entry {
        private String                      actual_item;
        private String                      cached_item;
        private java.util.Vector              child_entries;
        private java.util.Vector              parent_entries;
        private OMMapImpl_String_Type_String_ ommap;
        private Type                    index1;
        private String                    index2;
        private int                           path_length1;
        private int                           path_length2;
        private Type                    cached_index1;

        public Entry (CallContext context, OMMapImpl_String_Type_String_ ommap, Type index1, String index2) {
            this.ommap          = ommap;
            this.index1         = index1;
            this.index2         = index2;
            this.child_entries  = new java.util.Vector();
            this.parent_entries = new java.util.Vector();
            this.actual_item    = null;
            this.path_length1   = 9999;
            this.cached_item    = null;
            this.cached_index1  = null;

            Iterator_Type_ supertypes1;
            Type supertype1;

            supertypes1 = index1.getSuperTypes(context).getNavigator(context);
            while ((supertype1 = supertypes1.tryGetCurrent(context)) != null) {
                Entry parent = this.ommap._getEntry(context, supertype1, index2);
                this.parent_entries.add(parent);
                parent.addChildEntry(context, this);
                supertypes1.next(context);
            }
            this.update(context);
        }

        public boolean isActualEntry(CallContext context) {
            return this.actual_item == null ? false : true;
        }

        public void setItem(CallContext context, String item) {
            this.actual_item   = item;
            this.cached_item   = null;
            this.cached_index1 = null;
            if (item == null) {
                this.path_length1 = 9999;
                this.update(context);
            } else {
                this.path_length1 = 0;
                this.updateChildEntries(context);
            }
        }

        public String getItem(CallContext context) {
            return this.actual_item != null ? this.actual_item : this.cached_item;
        }

        public void addChildEntry (CallContext context, Entry child_entry) {
            this.child_entries.add(child_entry);
        }

        public int getPathLength1(CallContext context) {
            return this.path_length1;
        }

        public Type getIndexType1(CallContext context) {
            return this.actual_item != null ? this.index1 : this.cached_index1;
        }

        public void update (CallContext context) {
            if (this.actual_item != null) {
                return;
            }
            boolean changed = false;
            for (int i=0; i < this.parent_entries.size(); i++) {
                Entry parent_entry = (Entry) this.parent_entries.elementAt(i);
                String parent_item = parent_entry.getItem(context);
                if (parent_item != null) {
                    if (    this.cached_item != parent_item
                         && (    this.cached_item == null
                              || (    this.path_length1 > parent_entry.getPathLength1(context) + 1
                                   && (this.cached_index1 == null || ! this.cached_index1.isA(context, parent_entry.getIndexType1(context)))
                                 )
                              || (    this.cached_index1 != null && this.cached_index1 != parent_entry.getIndexType1(context) && parent_entry.getIndexType1(context).isA(context, this.cached_index1))
                            )
                       ) {
                        this.cached_item = parent_item;
                        this.path_length1 = parent_entry.getPathLength1(context) + 1;
                        this.cached_index1 = parent_entry.getIndexType1(context);
                        changed = true;
                    }
                }
            }
            if (changed) {
                this.updateChildEntries(context);
            }
        }

        public void updateChildEntries (CallContext context) {
            for (int i=0; i < this.child_entries.size(); i++) {
                ((Entry) this.child_entries.elementAt(i)).update(context);
            }
        }
    }

    public OMMapImpl_String_Type_String_ (CallContext context)
    {
        map1 = new java.util.Hashtable ();
    }

    public OMMapImpl_String_Type_String_ (CallContext context, java.util.Hashtable map1)
    {
        this.map1 = map1;
    }

    private Entry _getEntry  (CallContext context, Type index1, String index2)
    {
        java.util.Hashtable map2 = (java.util.Hashtable) map1.get(index1);
        if (map2 == null) {
            map2 = new java.util.Hashtable();
            map1.put(index1, map2);
        }
        Entry entry = (Entry) map2.get(index2);
        if (entry == null) {
            entry = new Entry (context, this, index1, index2);
            map2.put(index2, entry);
        }
        return entry;
    }

    public String get     (CallContext context, Type index1, String index2) throws DoesNotExist
    {
        String item = this.tryGet(context, index1, index2);
        if (item == null) DoesNotExist.createAndThrow(context);
        return item;
    }

    public String tryGet  (CallContext context, Type index1, String index2)
    {
        return this._getEntry(context, index1, index2).getItem(context);
    }

    public boolean  canGet  (CallContext context, Type index1, String index2)
    {
        if (this.tryGet(context, index1, index2) == null) return false;
        return true;
    }

    public void     set     (CallContext context, Type index1, String index2, String item)
    {
        Entry entry = this._getEntry(context, index1, index2);
        entry.setItem(context, item);
    }

    public void     add     (CallContext context, Type index1, String index2, String item) throws AlreadyExists
    {
        Entry entry = this._getEntry(context, index1, index2);
        if (entry.isActualEntry(context)) AlreadyExists.createAndThrow (context);
        entry.setItem(context, item);
    }

    public void     replace (CallContext context, Type index1, String index2, String item) throws DoesNotExist
    {
        Entry entry = this._getEntry(context, index1, index2);
        if (!entry.isActualEntry(context)) DoesNotExist.createAndThrow (context);
        entry.setItem(context, item);
    }

    public void     unset   (CallContext context, Type index1, String index2)
    {
        Entry entry = this._getEntry(context, index1, index2);
        entry.setItem(context, null);
    }

    public void     remove  (CallContext context, Type index1, String index2) throws DoesNotExist
    {
        Entry entry = this._getEntry(context, index1, index2);
        if (!entry.isActualEntry(context)) DoesNotExist.createAndThrow (context);
        entry.setItem(context, null);
    }

    public boolean canGetExactMatch (CallContext context, Type index1, String index2) {
        return this._getEntry(context, index1, index2).isActualEntry(context);
    }
}
