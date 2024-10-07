package com.sphenon.basics.metadata;

/****************************************************************************
  Copyright 2001-2024 Sphenon GmbH

  Licensed under the Apache License, Version 2.0 (the "License"); you may not
  use this file except in compliance with the License. You may obtain a copy
  of the License at http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
  License for the specific language governing permissions and limitations
  under the License.
*****************************************************************************/

import com.sphenon.basics.context.*;
import com.sphenon.basics.exception.*;
import com.sphenon.basics.customary.*;

import com.sphenon.basics.metadata.returncodes.*;
import com.sphenon.basics.metadata.exceptions.*;
import com.sphenon.basics.metadata.tplinst.*;

public interface JavaType extends Type
{
    public Class getJavaClass (CallContext context);
    public String getJavaClassName (CallContext context);

    static public JavaType tryGetJavaType (CallContext context, Type type) {
        if (type instanceof JavaType) {
            return ((JavaType) type);
        } else if (type instanceof TypeParametrised) {
            TypeParametrised tp = (TypeParametrised) type;
            if (tp.getBaseType(context) instanceof JavaType) {
                return ((JavaType) tp.getBaseType(context));
            } else {
                CustomaryContext.create((Context)context).throwLimitation(context, "Type not an instance of 'JavaType', but of '%(class)'", "class", tp.getBaseType(context).getClass());
                throw (ExceptionLimitation) null;
            }
        } else {
            CustomaryContext.create((Context)context).throwLimitation(context, "Type neither an instance of 'JavaType' nor of 'TypeParametrised', but of '%(class)'", "class", type.getClass());
            throw (ExceptionLimitation) null;
        }
    }
}
