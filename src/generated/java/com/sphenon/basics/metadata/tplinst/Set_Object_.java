// instantiated with javainst.pl from /workspace/sphenon/projects/components/basics/many/v0001/origin/source/java/com/sphenon/basics/many/templates/Set.javatpl
// please do not modify this file directly
package com.sphenon.basics.metadata.tplinst;

import com.sphenon.basics.metadata.*;
import com.sphenon.basics.metadata.traits.*;

import com.sphenon.basics.context.*;
import com.sphenon.basics.exception.*;

import com.sphenon.basics.many.*;
import com.sphenon.basics.many.returncodes.*;

public interface Set_Object_
  extends ReadSet_Object_,
          WriteSet_Object_,
          Navigatable_Iterator_Object__,
          OfKnownSize
{
    public boolean contains (CallContext context, Object item);

    public void     set     (CallContext context, Object item);
    public void     add     (CallContext context, Object item) throws AlreadyExists;
    public void     replace (CallContext context, Object item) throws DoesNotExist;
    public void     unset   (CallContext context, Object item);
    public void     remove  (CallContext context, Object item) throws DoesNotExist;

    public Iterator_Object_ getNavigator (CallContext context);

    public long     getSize (CallContext context);
}

