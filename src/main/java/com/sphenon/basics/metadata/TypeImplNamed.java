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

import com.sphenon.basics.metadata.returncodes.*;
import com.sphenon.basics.metadata.exceptions.*;
import com.sphenon.basics.metadata.tplinst.*;

public class TypeImplNamed
  implements Type
{
    private String name;
    protected Vector_Type_long_ supertypes = null;

    public TypeImplNamed (CallContext context, String name) {
        this.name = name;
    }

    public TypeImplNamed (CallContext context, String name, Type super_type) {
        this.name = name;
        this.supertypes = Factory_Vector_Type_long_.construct(context);
        this.supertypes.append(context, super_type);
    }

    public String getId (CallContext context) {
        return "Named::" + this.name;
    }

    public String getName (CallContext context) {
        return this.name;
    }

    public String toString () {
        return super.toString() + "[" + this.name + "]";
    }

    public boolean equals (Object object) {
        if (object == null) return false;
        if (! (object instanceof TypeImplNamed)) return false;
        if (! ((TypeImplNamed) object).name.equals(this.name)) return false;
        return true;
    }

    // from Object
    public int hashCode () {
        return this.name.hashCode();
    }

    public boolean equals (CallContext context, Object object) {
        if (object == null) return false;
        if (! (object instanceof TypeImplNamed)) return false;
        if (! ((TypeImplNamed) object).name.equals(this.name)) return false;
        return true;
    }

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
        if (this.supertypes == null) return false;
        for (Iterator_Type_ it = this.supertypes.getNavigator(context);
             it.canGetCurrent(context);
             it.next(context)
            ) {
            if (it.tryGetCurrent(context).isA(context, type)) return true;
        }
        return false;
    }
}
