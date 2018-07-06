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

public class TypeTupleImpl
  implements TypeTuple
{
    private Vector_Type_long_   elements;

    public TypeTupleImpl (CallContext context, Type parameter1) {
        this.elements = Factory_Vector_Type_long_.construct(context);
        this.elements.append(context, parameter1);
    }

    public TypeTupleImpl (CallContext context, Type parameter1, Type parameter2) {
        this.elements = Factory_Vector_Type_long_.construct(context);
        this.elements.append(context, parameter1);
        this.elements.append(context, parameter2);
    }

    public TypeTupleImpl (CallContext context, Type parameter1, Type parameter2, Type parameter3) {
        this.elements = Factory_Vector_Type_long_.construct(context);
        this.elements.append(context, parameter1);
        this.elements.append(context, parameter2);
        this.elements.append(context, parameter3);
    }

    public TypeTupleImpl (CallContext context, Vector_Type_long_ elements) {
        this.elements = elements;
    }

    public String getId(CallContext context) {
        String id = "[";
        for (long l=0; l<elements.getSize(context); l++) {
            if (l != 0) { id += ","; }
            id += elements.tryGet(context, l).getId(context);
        }
        id += "]";
        return id;
    }

    public String getName(CallContext context) {
        String name = "[";
        for (long l=0; l<elements.getSize(context); l++) {
            if (l != 0) { name += ","; }
            name += elements.tryGet(context, l).getName(context);
        }
        name += "]";
        return name;
    }

    private Vector_Type_long_ supertypes;

    public Vector_Type_long_ getSuperTypes (CallContext context) {
        if (this.supertypes == null) {
            this.supertypes = Factory_Vector_Type_long_.construct(context);
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
        if (type instanceof TypeTuple) {
            TypeTuple tp = (TypeTuple) type;
            boolean match = true;
            Vector_Type_long_ tppars = tp.getElements(context);
            long size = tppars.getSize(context);
            for (long p=0; p<size; p++) {
                Type mytype = this.elements.tryGet(context, p);
                Type tptype = tp.getElements(context).tryGet(context, p);
                if (mytype != null && tptype != null && mytype.isA(context, tptype)) continue;
                match = false;
                break;
            }
            if (match) return true;
        }
        return false;
    }

    public Vector_Type_long_ getElements (CallContext context) {
        return this.elements;
    }

    public boolean equals (Object object) {
        CallContext context = RootContext.getFallbackCallContext();
        if (object == null) return false;
        if (! (object instanceof TypeTupleImpl)) return false;
        Vector_Type_long_ other_pars = ((TypeTupleImpl) object).getElements(context);
        if (other_pars.getSize(context) != this.elements.getSize(context)) return false;
        for (long l=0; l<elements.getSize(context); l++) {
            if (! other_pars.tryGet(context, l).equals(this.elements.tryGet(context, l))) return false;
        }
        return true;
    }

    public int hashCode () {
        CallContext context = RootContext.getFallbackCallContext();
        int hc = 0;
        for (long l=0; l<this.elements.getSize(context); l++) {
            hc ^=  this.elements.tryGet(context, l).hashCode();
        }
        return hc;
    }

    public boolean equals (CallContext context, Object object) {
        return this.equals(object);
    }

    public String toString() {
        return super.toString() + "[" + getId(RootContext.getFallbackCallContext()) + "]";
    }
}
