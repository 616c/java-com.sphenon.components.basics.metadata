// instantiated with javainst.pl from /workspace/sphenon/projects/components/basics/many/v0001/origin/source/java/com/sphenon/basics/many/templates/WriteVector.javatpl
// please do not modify this file directly
package com.sphenon.basics.metadata.tplinst;

import com.sphenon.basics.metadata.*;
import com.sphenon.basics.metadata.traits.*;

import com.sphenon.basics.context.*;
import com.sphenon.basics.exception.*;

import com.sphenon.basics.many.returncodes.*;

public interface WriteVector_Type_long_
{
    public Type set          (CallContext context, long index, Type item);
    public void     add          (CallContext context, long index, Type item) throws AlreadyExists;
    public void     prepend      (CallContext context, Type item);
    public void     append       (CallContext context, Type item);
    public void     insertBefore (CallContext context, long index, Type item) throws DoesNotExist;
    public void     insertBehind (CallContext context, long index, Type item) throws DoesNotExist;
    public Type replace      (CallContext context, long index, Type item) throws DoesNotExist;
    public Type unset        (CallContext context, long index);
    public Type remove       (CallContext context, long index) throws DoesNotExist;
}

