// instantiated with javainst.pl from /workspace/sphenon/projects/components/basics/many/v0001/origin/source/java/com/sphenon/basics/many/templates/OSet.javatpl
// please do not modify this file directly
package com.sphenon.basics.metadata.tplinst;

import com.sphenon.basics.metadata.*;
import com.sphenon.basics.metadata.traits.*;

import com.sphenon.basics.context.*;
import com.sphenon.basics.exception.*;

import com.sphenon.basics.many.*;
import com.sphenon.basics.many.returncodes.*;

public interface OSet_Object_Type_
  extends ReadMap_Set_Object__Type_,
          WriteSet_Object_,
          Navigatable_Iterator_Object__,
          OfKnownSize
{
    public Set_Object_ get        (CallContext context, Type index) throws DoesNotExist;
    public Set_Object_ tryGet     (CallContext context, Type index);
    public boolean       canGet     (CallContext context, Type index);

    public Set_Object_ getMany    (CallContext context, Type index) throws DoesNotExist;
    public Set_Object_ tryGetMany (CallContext context, Type index);
    public boolean       canGetMany (CallContext context, Type index);

    public Object      getSole    (CallContext context, Type index) throws DoesNotExist, MoreThanOne;
    public Object      tryGetSole (CallContext context, Type index);
    public boolean       canGetSole (CallContext context, Type index);

    public void          set        (CallContext context, Object item);
    public void          add        (CallContext context, Object item) throws AlreadyExists;
    public void          replace    (CallContext context, Object item) throws DoesNotExist;
    public void          unset      (CallContext context, Object item);
    public void          remove     (CallContext context, Object item) throws DoesNotExist;

    public Iterator_Object_ getNavigator (CallContext context);

    public long          getSize (CallContext context);
}

