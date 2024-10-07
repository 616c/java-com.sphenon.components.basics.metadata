// instantiated with javainst.pl from /workspace/sphenon/projects/components/basics/many/v0001/origin/source/java/com/sphenon/basics/many/templates/IteratorItemIndex.javatpl
// please do not modify this file directly
package com.sphenon.basics.metadata.tplinst;

import com.sphenon.basics.metadata.*;
import com.sphenon.basics.metadata.traits.*;

import com.sphenon.basics.context.*;
import com.sphenon.basics.exception.*;

import com.sphenon.basics.many.*;
import com.sphenon.basics.many.returncodes.*;

public interface IteratorItemIndex_Type_long_
  extends Iterator_Type_
    , IteratorItemIndex<Type>
{
    // returns current index
    public long getCurrentIndex (CallContext context) throws DoesNotExist;

    // like "getCurrentIndex", but returns null instead of throwing exception
    public long tryGetCurrentIndex (CallContext context);
}
