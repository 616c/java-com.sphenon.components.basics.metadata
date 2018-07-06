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

import com.sphenon.basics.context.*;
import com.sphenon.basics.context.classes.*;

import com.sphenon.basics.metadata.returncodes.*;
import com.sphenon.basics.metadata.exceptions.*;
import com.sphenon.basics.configuration.*;
import com.sphenon.basics.metadata.tplinst.*;
import com.sphenon.basics.many.tplinst.*;

import java.io.*;

public class TypeParametrisedImplGenerics
    extends TypeParametrisedImpl
    implements JavaType
{
    private java.lang.reflect.ParameterizedType parameterized_type;

    protected boolean is_unspecific;

    public boolean getIsUnspecific (CallContext context) {
        return this.is_unspecific;
    }

    public TypeParametrisedImplGenerics (CallContext context, java.lang.reflect.ParameterizedType parameterized_type) {
        super(context);
        this.parameterized_type = parameterized_type;
        this.initialise(context, TypeManager.get(context, (Class) parameterized_type.getRawType()));
        this.parameter_names = Factory_Vector_String_long_.construct(context);
        java.lang.reflect.Type[] type_arguments = parameterized_type.getActualTypeArguments();
        this.is_unspecific = true;
        for (java.lang.reflect.Type type_argument : type_arguments) {
            if ((type_argument instanceof java.lang.reflect.WildcardType) == false) {
                this.is_unspecific = false;
                this.parameters.append(context, TypeManager.get(context, type_argument));
            } else {
                this.parameters.append(context, Type_Wildcard.getSingleton(context));
            }
            if (type_argument instanceof java.lang.reflect.TypeVariable) {
                this.parameter_names.append(context, ((java.lang.reflect.TypeVariable)type_argument).getName());
            } else {
                this.parameter_names.append(context, "?");
            }
        }
    }

    public java.lang.reflect.ParameterizedType getParameterizedType(CallContext context) {
        return this.parameterized_type;
    }

    public Class getJavaClass (CallContext context) {
        return (Class) this.parameterized_type.getRawType();
    }

    public String getJavaClassName (CallContext context) {
        return ((Class) (this.parameterized_type.getRawType())).getName();
    }
}
