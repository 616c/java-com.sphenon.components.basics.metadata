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
    /*[Issue:GenericsVsParametrised - TypeManager.java,TypeImpl.java,TypeParametrisedImpl.java,TypeParametrisedImplGenerics.java]
    protected boolean is_pseudo_specific; */

    public boolean getIsUnspecific (CallContext context) {
        return this.is_unspecific;
    }

    /*[Issue:GenericsVsParametrised - TypeManager.java,TypeImpl.java,TypeParametrisedImpl.java,TypeParametrisedImplGenerics.java]
    public boolean getIsPseudoSpecific (CallContext context) {
        return this.is_pseudo_specific;
    }*/

    public TypeParametrisedImplGenerics (CallContext context, java.lang.reflect.ParameterizedType parameterized_type) {
        super(context);
        this.parameterized_type = parameterized_type;
        this.initialise(context, TypeManager.get(context, (Class) parameterized_type.getRawType()));
        this.parameter_names = Factory_Vector_String_long_.construct(context);
        java.lang.reflect.Type[] type_arguments = parameterized_type.getActualTypeArguments();
        this.is_unspecific = true;
        /*[Issue:GenericsVsParametrised - TypeManager.java,TypeImpl.java,TypeParametrisedImpl.java,TypeParametrisedImplGenerics.java]
        this.is_pseudo_specific = true;*/
        for (java.lang.reflect.Type type_argument : type_arguments) {
            if (type_argument instanceof java.lang.reflect.WildcardType) {
                this.parameters.append(context, Type_Wildcard.getSingleton(context));
            } else {
                this.is_unspecific = false;
                this.parameters.append(context, TypeManager.get(context, type_argument));
                /*[Issue:GenericsVsParametrised - TypeManager.java,TypeImpl.java,TypeParametrisedImpl.java,TypeParametrisedImplGenerics.java]
                if (    (type_argument instanceof Class) == false
                     || ((Class) type_argument).equals(Object.class) == false
                   ) {
                    this.is_pseudo_specific = false;
                }*/
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
