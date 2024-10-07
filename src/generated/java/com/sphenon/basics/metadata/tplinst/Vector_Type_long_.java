// instantiated with javainst.pl from /workspace/sphenon/projects/components/basics/many/v0001/origin/source/java/com/sphenon/basics/many/templates/Vector.javatpl
// please do not modify this file directly
package com.sphenon.basics.metadata.tplinst;

import com.sphenon.basics.metadata.*;
import com.sphenon.basics.metadata.traits.*;

import com.sphenon.basics.context.*;
import com.sphenon.basics.exception.*;

import com.sphenon.basics.many.*;
import com.sphenon.basics.many.returncodes.*;

import com.sphenon.ui.annotations.*;

@UIId("")
@UIName("")
@UIClassifier("Vector_Type_")
@UIParts("js:instance.getIterable(context)")
public interface Vector_Type_long_
  extends ReadOnlyVector_Type_long_,
          WriteVector_Type_long_
          , GenericVector<Type>
          , GenericIterable<Type>
{
    public Type                                    get             (CallContext context, long index) throws DoesNotExist;
    public Type                                    tryGet          (CallContext context, long index);
    public boolean                                     canGet          (CallContext context, long index);

    public ReferenceToMember_Type_long_ReadOnlyVector_Type_long__  getReference    (CallContext context, long index) throws DoesNotExist;
    public ReferenceToMember_Type_long_ReadOnlyVector_Type_long__  tryGetReference (CallContext context, long index);

    public Type                                    set             (CallContext context, long index, Type item);
    public void                                        add             (CallContext context, long index, Type item) throws AlreadyExists;
    public void                                        prepend         (CallContext context, Type item);
    public void                                        append          (CallContext context, Type item);
    public void                                        insertBefore    (CallContext context, long index, Type item) throws DoesNotExist;
    public void                                        insertBehind    (CallContext context, long index, Type item) throws DoesNotExist;
    public Type                                    replace         (CallContext context, long index, Type item) throws DoesNotExist;
    public Type                                    unset           (CallContext context, long index);
    public Type                                    remove          (CallContext context, long index) throws DoesNotExist;

    public IteratorItemIndex_Type_long_       getNavigator    (CallContext context);

    public long                                        getSize         (CallContext context);

    // for sake of Iterable's
    public java.util.Iterator<Type>              getIterator_Type_ (CallContext context);
    public java.util.Iterator                          getIterator (CallContext context);
    public VectorIterable_Type_long_          getIterable_Type_ (CallContext context);
    public Iterable<Type> getIterable (CallContext context);
}
