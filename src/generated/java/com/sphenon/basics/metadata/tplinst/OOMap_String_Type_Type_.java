// instantiated with javainst.pl from /workspace/sphenon/projects/components/basics/many/v0001/origin/source/java/com/sphenon/basics/many/templates/OOMap.javatpl
// please do not modify this file directly
package com.sphenon.basics.metadata.tplinst;

import com.sphenon.basics.metadata.*;
import com.sphenon.basics.metadata.traits.*;

import com.sphenon.basics.context.*;
import com.sphenon.basics.exception.*;

import com.sphenon.basics.many.*;
import com.sphenon.basics.many.returncodes.*;

public interface OOMap_String_Type_Type_
{
    public String      get     (CallContext context, Type index1, Type index2) throws DoesNotExist;
    public String      tryGet  (CallContext context, Type index1, Type index2);
    public boolean       canGet  (CallContext context, Type index1, Type index2);

    public void          set     (CallContext context, Type index1, Type index2, String item);
    public void          add     (CallContext context, Type index1, Type index2, String item) throws AlreadyExists;
    public void          replace (CallContext context, Type index1, Type index2, String item) throws DoesNotExist;
    public void          unset   (CallContext context, Type index1, Type index2);
    public void          remove  (CallContext context, Type index1, Type index2) throws DoesNotExist;

    public boolean       canGetExactMatch (CallContext context, Type index1, Type index2);
}

