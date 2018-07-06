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

public class TypeParametrisedImpl
  implements TypeParametrised
{
    static protected boolean debug = false;

    protected Type                base_type;
    protected Vector_Object_long_ parameters;
    protected Vector_String_long_ parameter_names;
    protected Vector_Type_long_   supertypes;

    protected void initialise (CallContext context, Type base_type) {
        initialise (context, base_type, Factory_Vector_Object_long_.construct(context), null);
    }

    protected void initialise (CallContext context, Type base_type, Vector_Object_long_ parameters, Vector_String_long_ parameter_names) {
        this.base_type = base_type;
        this.supertypes = null;
        this.parameters = parameters;
        this.parameter_names = parameter_names;

        boolean problem = false;
        if (this.parameters != null) {
            for (long l=0; l<this.parameters.getSize(context); l++) {
                Object p = this.parameters.tryGet(context, l);
                if (p instanceof Class) {
                    problem = true;
                }
            }
        }
        if (problem) {
            System.err.println("PROBLEM (TypeParametrisedImpl.java): " + this.getId(context));
        }
    }

    public TypeParametrisedImpl (CallContext context, Type base_type, Object... parameters) {
        this.initialise(context, base_type);
        if (parameters != null) {
            for (Object parameter : parameters) {
                if (parameter instanceof Class) {
                    // modded = true;
                    this.parameters.append(context, TypeManager.get(context, (Class) parameter));
                } else {
                    this.parameters.append(context, parameter);
                }
            }
        }
    }

    public TypeParametrisedImpl (CallContext context, Type base_type, Vector_Object_long_ parameters) {
        this.initialise(context, base_type, parameters, null);
    }

    public TypeParametrisedImpl (CallContext context, Type base_type, Vector_Object_long_ parameters, Vector_String_long_ parameter_names) {
        this.initialise(context, base_type, parameters, parameter_names);
    }

    protected TypeParametrisedImpl (CallContext context) {
    }

    public String getId(CallContext context) {
        String id = this.base_type.getId(context) + "<";
        for (long l=0; l<parameters.getSize(context); l++) {
            if (l != 0) { id += ","; }
            Object op = parameters.tryGet(context, l);
            id += (op instanceof Type ? ((Type)op).getId(context) : op.toString());
        }
        id += ">";
        return id;
    }

    public String getName(CallContext context) {
        String name = this.base_type.getName(context) + "<";
        for (long l=0; l<parameters.getSize(context); l++) {
            if (l != 0) { name += ","; }
            Object op = parameters.tryGet(context, l);
            name += (op instanceof Type ? ((Type)op).getName(context) : op.toString());
        }
        name += ">";
        return name;
    }

    public Vector_Type_long_ getSuperTypes (CallContext context) {
        if (this.supertypes == null) {
            this.supertypes = Factory_Vector_Type_long_.construct(context);
            this.supertypes.append(context, base_type);
            // why base_parameters?
            // actually, there are possibly plenty more supertypes in case
            // the parameters are themselves parametrised; doing this completely
            // is risky, since it might lead to combinatorial exploded supertypes
            // the base_parameters are slighty more than just the parameters
            Vector_Object_long_ base_parameters = null;
            if (this.parameters != null) {
                boolean need_copy = false;
                for (Object p : this.parameters.getIterable(context)) {
                    if (p instanceof TypeParametrised) {
                        need_copy = true;
                        break;
                    }
                }
                if (need_copy) {
                    base_parameters = Factory_Vector_Object_long_.construct(context);
                    for (Object p : this.parameters.getIterable(context)) {
                        if (p instanceof TypeParametrised) {
                            base_parameters.append(context, ((TypeParametrised) p).getBaseType(context));
                        } else {
                            base_parameters.append(context, p);
                        }
                    }
                }
            }
            if (base_parameters != null) {
                this.supertypes.append(context, TypeManager.getParametrised(context, this.base_type, base_parameters));
            }
            if (this.base_type.getSuperTypes(context) != null) {
                for (Iterator_Type_ it = this.base_type.getSuperTypes(context).getNavigator(context);
                     it.canGetCurrent(context);
                     it.next(context)
                    ) {
                    this.supertypes.append(context, TypeManager.getParametrised(context, it.tryGetCurrent(context), this.parameters));
                    if (base_parameters != null) {
                        this.supertypes.append(context, TypeManager.getParametrised(context, it.tryGetCurrent(context), base_parameters));
                    }
                }
            }
        }
        return this.supertypes;
    }

    private Vector_Type_long_ all_interfaces;

    public Vector_Type_long_ getAllSuperInterfaces (CallContext context) {
        return (this.all_interfaces = TypeImpl.getOrBuildAllSuperInterfaces (context, this, this.all_interfaces));
    }

    private Vector_Type_long_ all_shortest_path_interfaces;

    public Vector_Type_long_ getAllShortestPathSuperInterfaces (CallContext context) {
        return (this.all_shortest_path_interfaces = TypeImpl.getOrBuildAllShortestPathSuperInterfaces (context, this, this.all_shortest_path_interfaces));
    }

    public boolean isA (CallContext context, Type type) {
        if (type == null) return false;
        if (this.equals(context, type)) return true;
        if (type instanceof TypeParametrised) {
            TypeParametrised tp = (TypeParametrised) type;
            boolean match = true;
            Vector_Object_long_ tppars = tp.getParameters(context);
            long size = tppars.getSize(context);

            // well, this is just a very limited hack yet:
            // problem: a check is needed as follows
            // T1<X,Y,Z> must be examined (getGenericInerfaces, getGenericSuperclass)
            // e.g. implements T2<X,Y>, T3<Y,Z>, T4<X>
            // again, T2<A,B> (declared as A,B) might implement T5<A>
            // then all those type variables must be mapped along the inheritance path
            // like T1<X,Y,Z> -> T2<X,Y>, X=A, Y=B -> T5<A=X>
            // before this can be done, the inheritance path from this type
            // to it's supertype must be calculated
            //
            // the hack now works as follows
            // it is assumed that T1<X,Y,Z> is in the same order as all
            // it's superinterfaces, e.g. T2<X,Y>, but for t3<Y,Z>
            // this here will fail definitely

            if (this.parameters.getSize(context) >= size && this.base_type.isA(context, tp.getBaseType(context))) {
                for (long p=0; p<size; p++) {
                    Object mypar = this.parameters.tryGet(context, p);
                    Object tppar = tp.getParameters(context).tryGet(context, p);
                    if (mypar.equals(tppar)) continue;
                    Type mytype = mypar instanceof Type ? ((Type) mypar) : mypar instanceof Class ? TypeManager.get(context, (Class) mypar) : null;
                    Type tptype = tppar instanceof Type ? ((Type) tppar) : tppar instanceof Class ? TypeManager.get(context, (Class) tppar) : null;
                    if (mytype != null && tptype != null && mytype.isA(context, tptype)) continue;
                    match = false;
                    break;
                }
                if (match) return true;
            }
        } else {
            if (this.base_type.isA(context, type)) return true;
        }
        return false;
    }

    public Type getBaseType (CallContext context) {
        return this.base_type;
    }

    public Vector_Object_long_ getParameters (CallContext context) {
        return this.parameters;
    }

    public Vector_String_long_ getParameterNames (CallContext context) {
        return this.parameter_names;
    }

    public boolean equals (Object object) {
        CallContext context = RootContext.getFallbackCallContext();
        if (object == null) return false;
        if (! (object instanceof TypeParametrisedImpl)) return false;
        if (! ((TypeParametrisedImpl) object).getBaseType(context).equals(this.base_type)) return false;
        Vector_Object_long_ other_pars = ((TypeParametrisedImpl) object).getParameters(context);
        if (other_pars.getSize(context) != this.parameters.getSize(context)) return false;
        for (long l=0; l<parameters.getSize(context); l++) {
            if (! other_pars.tryGet(context, l).equals(this.parameters.tryGet(context, l))) return false;
        }
        return true;
    }

    public int hashCode () {
        CallContext context = RootContext.getFallbackCallContext();
        int hc = this.base_type.hashCode();
        for (long l=0; l<this.parameters.getSize(context); l++) {
            hc ^=  this.parameters.tryGet(context, l).hashCode();
        }
        return hc;
    }

    public boolean equals (CallContext context, Object object) {
        return this.equals(object);
    }

    public String toString() {
        return (debug ? super.toString() : "") + "[" + getId(RootContext.getFallbackCallContext()) + "]";
    }
}
