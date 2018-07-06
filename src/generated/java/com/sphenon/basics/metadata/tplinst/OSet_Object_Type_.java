// instantiated with javainst.pl from /workspace/sphenon/projects/components/basics/many/v0001/origin/source/java/com/sphenon/basics/many/templates/OSet.javatpl

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

