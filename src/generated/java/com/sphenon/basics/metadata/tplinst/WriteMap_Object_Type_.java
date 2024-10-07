// instantiated with javainst.pl from /workspace/sphenon/projects/components/basics/many/v0001/origin/source/java/com/sphenon/basics/many/templates/WriteMap.javatpl
// please do not modify this file directly
package com.sphenon.basics.metadata.tplinst;

import com.sphenon.basics.metadata.*;
import com.sphenon.basics.metadata.traits.*;

import com.sphenon.basics.context.*;
import com.sphenon.basics.exception.*;

import com.sphenon.basics.many.returncodes.*;

public interface WriteMap_Object_Type_
{
    // associates index with item, previous association may already exist
    public void     set     (CallContext context, Type index, Object item);

    // associates index with item, previous association must not exist
    public void     add     (CallContext context, Type index, Object item) throws AlreadyExists;

    // associates index with item, previous association must exist
    public void     replace (CallContext context, Type index, Object item) throws DoesNotExist;

    // removes index entry, entry needs not exist
    public void     unset   (CallContext context, Type index);

    // removes index entry, entry must exist
    public void     remove  (CallContext context, Type index) throws DoesNotExist;
}

