// instantiated with javainst.pl from /workspace/sphenon/projects/components/basics/many/v0001/origin/source/java/com/sphenon/basics/many/templates/ReadOnlyVector.javatpl
// please do not modify this file directly
package com.sphenon.basics.metadata.tplinst;

import com.sphenon.basics.metadata.*;
import com.sphenon.basics.metadata.traits.*;


import com.sphenon.basics.context.*;
import com.sphenon.basics.exception.*;

import com.sphenon.basics.many.*;
import com.sphenon.basics.many.returncodes.*;

public interface ReadOnlyVector_Type_long_
  extends ReadVector_Type_long_,
          ReadOnlyVector<Type>,
          OfKnownSize
{
    public Type                                    get             (CallContext context, long index) throws DoesNotExist;
    public Type                                    tryGet          (CallContext context, long index);
    public boolean                                     canGet          (CallContext context, long index);

    public ReferenceToMember_Type_long_ReadOnlyVector_Type_long__  getReference    (CallContext context, long index) throws DoesNotExist;
    public ReferenceToMember_Type_long_ReadOnlyVector_Type_long__  tryGetReference (CallContext context, long index);
}

