// instantiated with javainst.pl from /workspace/sphenon/projects/components/basics/many/v0001/origin/source/java/com/sphenon/basics/many/templates/VectorIterable.javatpl
// please do not modify this file directly
package com.sphenon.basics.metadata.tplinst;

import com.sphenon.basics.metadata.*;
import com.sphenon.basics.metadata.traits.*;

import com.sphenon.basics.context.*;
import com.sphenon.basics.exception.*;

import com.sphenon.basics.many.*;
import com.sphenon.basics.many.returncodes.*;

public class VectorIterable_Type_long_ implements Iterable<Type>
{
    protected java.util.Iterator<Type> iterator;

    public VectorIterable_Type_long_ (CallContext context, Vector_Type_long_ vector) {
        this.iterator = (vector == null ? (new java.util.Vector<Type>()).iterator() : vector.getIterator_Type_(context));
    }

    public java.util.Iterator<Type> iterator () {
        return this.iterator;
    }
}

