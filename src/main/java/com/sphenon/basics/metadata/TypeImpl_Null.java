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

public class TypeImpl_Null implements Type
{
    static protected TypeImpl_Null singleton;

    static public TypeImpl_Null getSingleton (CallContext context) {
        if (singleton == null) {
            singleton = new TypeImpl_Null (context);
        }
        return singleton;
    }

    protected TypeImpl_Null (CallContext context) {
    }

    public String getId (CallContext context) {
        return "Java/null";
    }

    public String getName (CallContext context) {
        return "(null)";
    }

    public String toString () {
        return "(null)";
    }

    public boolean equals (Object object) {
        return object == this;
    }

    public boolean equals (CallContext context, Object object) {
        return this.equals(object);
    }

    protected Vector_Type_long_ supertypes = null;
    protected Vector_Type_long_ all_interfaces = null;
    protected Vector_Type_long_ all_shortest_path_interfaces = null;

    public Vector_Type_long_ getSuperTypes (CallContext context) {
        if (this.supertypes == null) {
            this.supertypes = Factory_Vector_Type_long_.construct(context);
        }
        return this.supertypes;
    }

    public Vector_Type_long_ getAllSuperInterfaces (CallContext context) {
        if (this.all_interfaces == null) {
            this.all_interfaces = Factory_Vector_Type_long_.construct(context);
        }
        return this.all_interfaces;
    }

    public Vector_Type_long_ getAllShortestPathSuperInterfaces (CallContext context) {
        if (this.all_shortest_path_interfaces == null) {
            this.all_shortest_path_interfaces = Factory_Vector_Type_long_.construct(context);
        }
        return this.all_shortest_path_interfaces;
    }

    public boolean isA (CallContext context, Type type) {
        if (type == null) return false;
        return true;
    }
}
