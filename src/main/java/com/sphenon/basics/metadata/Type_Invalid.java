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

public class Type_Invalid implements Type
{
    static protected Type_Invalid singleton;

    static public Type_Invalid getSingleton (CallContext context) {
        if (singleton == null) {
            singleton = new Type_Invalid (context);
        }
        return singleton;
    }

    protected Type_Invalid (CallContext context) {
    }

    public String getId (CallContext context) {
        return "<INVALID>";
    }

    public String getName (CallContext context) {
        return "<INVALID>";
    }

    public String toString () {
        return "<INVALID>";
    }

    public boolean equals (Object object) {
        return object == this;
    }

    public boolean equals (CallContext context, Object object) {
        return this.equals(object);
    }

    public Vector_Type_long_ getSuperTypes (CallContext context) {
        return null;
    }

    public Vector_Type_long_ getAllSuperInterfaces (CallContext context) {
        return null;
    }

    public Vector_Type_long_ getAllShortestPathSuperInterfaces (CallContext context) {
        return null;
    }

    public boolean isA (CallContext context, Type type) {
        return false;
    }
}
