package com.sphenon.basics.metadata;

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

import com.sphenon.basics.metadata.*;
import com.sphenon.basics.metadata.traits.*;

import com.sphenon.basics.context.*;
import com.sphenon.basics.exception.*;

import com.sphenon.basics.many.*;
import com.sphenon.basics.many.returncodes.*;

public interface OMMap<T>
{
    public T        get     (CallContext context, Type index1, String index2) throws DoesNotExist;
    public T        tryGet  (CallContext context, Type index1, String index2);
    public boolean  canGet  (CallContext context, Type index1, String index2);

    public void     set     (CallContext context, Type index1, String index2, T item);
    public void     add     (CallContext context, Type index1, String index2, T item) throws AlreadyExists;
    public void     replace (CallContext context, Type index1, String index2, T item) throws DoesNotExist;
    public void     unset   (CallContext context, Type index1, String index2);
    public void     remove  (CallContext context, Type index1, String index2) throws DoesNotExist;

    public boolean  canGetExactMatch (CallContext context, Type index1, String index2);
}

