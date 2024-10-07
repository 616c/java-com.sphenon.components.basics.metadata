// instantiated with javainst.pl from /workspace/sphenon/projects/components/basics/many/v0001/origin/source/java/com/sphenon/basics/many/templates/Queue.javatpl
// please do not modify this file directly
package com.sphenon.basics.metadata.tplinst;

import com.sphenon.basics.metadata.*;
import com.sphenon.basics.metadata.traits.*;

import com.sphenon.basics.context.*;
import com.sphenon.basics.exception.*;

import com.sphenon.basics.many.returncodes.*;

public interface Queue_Type_
  extends ReadQueue_Type_,
          WriteQueue_Type_
{
    public Type getFront     (CallContext context) throws DoesNotExist;
    public Type tryGetFront  (CallContext context);
    public boolean  isEmpty      (CallContext context);
    public void     pushBack     (CallContext context, Type item);
    public Type popFront     (CallContext context) throws DoesNotExist;
    public Type tryPopFront  (CallContext context);
}
