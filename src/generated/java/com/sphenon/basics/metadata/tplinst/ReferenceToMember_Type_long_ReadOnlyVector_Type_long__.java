// instantiated with javainst.pl from /workspace/sphenon/projects/components/basics/many/v0001/origin/source/java/com/sphenon/basics/many/templates/ReferenceToMember.javatpl
// please do not modify this file directly
package com.sphenon.basics.metadata.tplinst;

import com.sphenon.basics.metadata.*;
import com.sphenon.basics.metadata.traits.*;

import com.sphenon.basics.context.*;
import com.sphenon.basics.exception.*;
import com.sphenon.basics.reference.*;
import com.sphenon.basics.many.*;

public interface ReferenceToMember_Type_long_ReadOnlyVector_Type_long__
  extends Reference_Type_
    , ReferenceToMember<Type,ReadOnlyVector<Type>>
{
    public ReadOnlyVector_Type_long_ getContainer(CallContext context);
    public long     getIndex    (CallContext context);
}
