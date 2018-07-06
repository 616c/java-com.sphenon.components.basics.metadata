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

import java.util.*;

import com.sphenon.basics.context.*;
import com.sphenon.basics.customary.*;
import com.sphenon.basics.debug.*;
import com.sphenon.basics.exception.*;
import com.sphenon.basics.many.*;
import com.sphenon.basics.many.returncodes.*;
import com.sphenon.basics.metadata.returncodes.*;
import com.sphenon.basics.metadata.exceptions.*;
import com.sphenon.basics.metadata.tplinst.*;

public class TypeImpl implements Type, JavaType {
    static protected boolean debug = false;

    // [Refactor:redundant legacy] -> use/unify with VectorImplList_Type_long
    static public class VektorImplArrayList_Type_Long_   implements Vector_Type_long_, Dumpable, ManagedResource {
        private java.util.ArrayList vector;
  
        protected VektorImplArrayList_Type_Long_ (CallContext context) {
            vector = new java.util.ArrayList ();
        }
  
        static public VektorImplArrayList_Type_Long_ create (CallContext context) {
            return new VektorImplArrayList_Type_Long_(context);
        }
  
        protected VektorImplArrayList_Type_Long_ (CallContext context, java.util.ArrayList vector) {
            this.vector = vector;
        }
  
        static public Vector_Type_long_ create (CallContext context, java.util.ArrayList vector) {
            return new VektorImplArrayList_Type_Long_(context, vector);
        }
  
        public Type get          (CallContext context, long index) throws DoesNotExist {
            try {
                return (Type) vector.get((int)index);
            } catch (IndexOutOfBoundsException e) {
                DoesNotExist.createAndThrow (context);
                throw (DoesNotExist) null; // compiler insists
            }
        }
        
        public Type tryGet       (CallContext context, long index) {
            if (index < 0 || index >= vector.size()) {
                return null;
            }
            return (Type) vector.get((int) index);
        }
  
        public boolean  canGet       (CallContext context, long index) {
            return (index >= 0 && index < vector.size()) ? true : false;
        }
  
        public VectorReferenceToMember_Type_long_ getReference    (CallContext context, long index) throws DoesNotExist {
            if ( ! canGet(context, index)) {
                DoesNotExist.createAndThrow (context);
                throw (DoesNotExist) null; // compiler insists
            }
            return new VectorReferenceToMember_Type_long_(context, this, index);
        }
  
        public VectorReferenceToMember_Type_long_ tryGetReference (CallContext context, long index) {
            if ( ! canGet(context, index)) { return null; }
            return new VectorReferenceToMember_Type_long_(context, this, index);
        }
  
        public Type set(CallContext context, long index, Type item) {
            if (index >= vector.size()) {
                vector.ensureCapacity((int)index+1);
                for (int i = vector.size(); i<=index; i++) {
                    vector.add(null);
                }
            }
            return (Type) vector.set((int) index, item);
        }
  
        public void     add          (CallContext context, long index, Type item) throws AlreadyExists {
            if (index < vector.size()) { AlreadyExists.createAndThrow (context); }
            set(context, index, item);
        }
  
        public void     prepend      (CallContext call_context, Type item) {
            if (vector.size() == 0) {
                vector.add(item);
            } else {
                try {
                    vector.add(0, item);
                } catch (IndexOutOfBoundsException e) {
                    Context context = Context.create(call_context);
                    CustomaryContext cc = CustomaryContext.create(context);
                    cc.throwImpossibleState(context, ManyStringPool.get(context, "0.0.1" /* cannot insert element at position 0, java-lib says 'out of bounds' ??? */));
                }
            }
        }
  
        public void     append       (CallContext context, Type item) {
            vector.add(item);
        }
  
        public void     insertBefore (CallContext context, long index, Type item) throws DoesNotExist {
            try {
                vector.add((int) index, item);
            } catch (IndexOutOfBoundsException e) {
                DoesNotExist.createAndThrow(context);
            }
        }
  
        public void     insertBehind (CallContext context, long index, Type item) throws DoesNotExist {
            if (index == vector.size() - 1) {
                vector.add(item);
            } else {
                try {
                    vector.add((int) (index+1), item);
                } catch (IndexOutOfBoundsException e) {
                    DoesNotExist.createAndThrow (context);
                }
            }
        }

        public void release(CallContext context) {
            if (this.vector != null && this.vector instanceof ManagedResource) {
                ((ManagedResource)(this.vector)).release(context);
            }
        }
  
        public Type replace      (CallContext call_context, long index, Type item) throws DoesNotExist {
            try {
                return (Type) vector.set((int) index, item);
            } catch (IndexOutOfBoundsException e) {
                DoesNotExist.createAndThrow(call_context);
                throw (DoesNotExist) null;
            } catch (IllegalArgumentException e) {
                Context context = Context.create(call_context);
                CustomaryContext cc = CustomaryContext.create(context);
                cc.throwImpossibleState (context, ManyStringPool.get(context, "0.0.2" /* An exception occured, with respect to which the java-lib documentation is unfortunately incorrect */));
                throw (ExceptionImpossibleState) null;
            }
        }
        
        public Type unset        (CallContext context, long index) {
            if (index < 0 || index >= vector.size()) {
                return null;
            }
            return (Type) vector.remove((int) index);
        }
        
        public Type remove       (CallContext context, long index) throws DoesNotExist {
            try {
                return (Type) vector.remove((int) index);
            } catch (IndexOutOfBoundsException e) {
                DoesNotExist.createAndThrow (context);
                throw (DoesNotExist) null;
            }
        }
  
        public IteratorItemIndex_Type_long_ getNavigator (CallContext context) {
            return new VectorIteratorImpl_Type_long_ (context, this);
        }
  
        public long     getSize      (CallContext context) {
            return vector.size();
        }
  
        public java.util.Iterator<Type> getIterator_Type_ (CallContext context) {
            return vector.iterator();
        }
  
        public java.util.Iterator getIterator (CallContext context) {
            return getIterator_Type_(context);
        }
  
        public VectorIterable_Type_long_ getIterable_Type_ (CallContext context) {
            return new VectorIterable_Type_long_(context, this);
        }
  
        public Iterable getIterable (CallContext context) {
            return getIterable_Type_ (context);
        }
  
        public java.util.ArrayList getImplementationVector(CallContext context){
            return this.vector;
        }
  
  
        public void dump(CallContext context, DumpNode dump_node) {
            int i=1;
            for (Object o : vector) {
                dump_node.dump(context, (new Integer(i++)).toString(), o);
            }
        }

    };
    
    private String java_class_name;
    private Class myclass;
    private Vector_Type_long_ supertypes;
  
    public TypeImpl (CallContext context, String name) throws NoSuchClass {
        this.java_class_name = name;
        this.supertypes = null;
        this.all_interfaces = null;
        this.all_shortest_path_interfaces = null;
  
        try {
            this.myclass = com.sphenon.basics.cache.ClassCache.getClassForName(context, name);
        } catch (ClassNotFoundException e) {
            NoSuchClass.createAndThrow(context, "name");
        }
    }
  

    public TypeImpl (CallContext context, Class yourclass) {
        this.java_class_name = yourclass.getName();
        this.myclass = yourclass;
        this.supertypes = null;
        this.all_interfaces = null;
        this.all_shortest_path_interfaces = null;

        if      (this.myclass == boolean.class) { this.java_class_name = "java.lang.Boolean";   this.myclass = java.lang.Boolean.class; }
        else if (this.myclass == byte.class)    { this.java_class_name = "java.lang.Byte";      this.myclass = java.lang.Byte.class; }
        else if (this.myclass == char.class)    { this.java_class_name = "java.lang.Character"; this.myclass = java.lang.Character.class; }
        else if (this.myclass == short.class)   { this.java_class_name = "java.lang.Short";     this.myclass = java.lang.Short.class; }
        else if (this.myclass == int.class)     { this.java_class_name = "java.lang.Integer";   this.myclass = java.lang.Integer.class; }
        else if (this.myclass == long.class)    { this.java_class_name = "java.lang.Long";      this.myclass = java.lang.Long.class; }
        else if (this.myclass == float.class)   { this.java_class_name = "java.lang.Float";     this.myclass = java.lang.Float.class; }
        else if (this.myclass == double.class)  { this.java_class_name = "java.lang.Double";    this.myclass = java.lang.Double.class; }
    }

    public String getId (CallContext context) {
        return "Java::" + this.java_class_name;
    }

    public String getName (CallContext context) {
        return this.java_class_name;
    }

    public String toString () {
        return (debug ? super.toString() : "") + "[" + this.java_class_name + "]";
    }

    public Class getJavaClass (CallContext context) {
        // note: context can be null!
        return this.myclass;
    }

    public String getJavaClassName (CallContext context) {
        return this.java_class_name;
    }

    public boolean equals (Object object) {
        if (object == null) return false;
        if (! (object instanceof TypeImpl)) return false;
        if (! ((TypeImpl) object).getJavaClass(null).equals(this.myclass)) return false;
        return true;
    }

    public int hashCode () {
        return this.myclass.hashCode();
    }

    public boolean equals (CallContext context, Object object) {
        return this.equals(object);
    }

     public Vector_Type_long_ getSuperTypes (CallContext context) {
         synchronized(this) {
          if (this.supertypes != null) {
              return this.supertypes;
          }
          this.supertypes = new VektorImplArrayList_Type_Long_(context);
          Class superclass = this.myclass.getSuperclass();
          Class[] interfaces = this.myclass.getInterfaces();
          for (int i=0; i<interfaces.length; i++) {
              this.supertypes.append(context, TypeManager.get(context, interfaces[i]));
          }
          if (superclass != null) {
              this.supertypes.append(context, TypeManager.get(context, superclass));
          } else {
              if (interfaces.length == 0 && this.myclass != Object.class) {
                  this.supertypes.append(context, TypeManager.get(context, Object.class));
              }
          }
          
          return this.supertypes;
       }
    }

    static protected void appendSuperInterfaces(CallContext context, Type type, Queue_Type_ queue, Vector_Type_long_ interfaces, java.util.Hashtable unique_check_map) {
        synchronized (type) {
          for (Iterator_Type_ it = type.getSuperTypes(context).getNavigator(context);
               it.canGetCurrent(context);
               it.next(context)
              ) {
              Type super_type = it.tryGetCurrent(context);
              if (unique_check_map.get(super_type) == null) {
                  unique_check_map.put(super_type, super_type);
                  queue.pushBack(context, super_type);
                  if (    super_type instanceof TypeImpl
                       && ((TypeImpl) super_type).getJavaClass(context).isInterface()
                     ) {
                      interfaces.append(context, super_type);
                  }
              }
          }
        }
    }

    static public synchronized Vector_Type_long_ getOrBuildAllSuperInterfaces (CallContext context, Type type, Vector_Type_long_ all_interfaces) {
      if (all_interfaces == null) {
            all_interfaces = new VektorImplArrayList_Type_Long_(context);
            java.util.Hashtable unique_check_map = new java.util.Hashtable();
            Queue_Type_ queue = new QueueImpl_Type_(context);
            queue.pushBack(context, type);
            while ((type = queue.tryPopFront(context)) != null) {
                appendSuperInterfaces(context, type, queue, all_interfaces, unique_check_map);
            }
        }
        return all_interfaces;
    }

    static public synchronized Vector_Type_long_ getOrBuildAllShortestPathSuperInterfaces (CallContext context, Type type, Vector_Type_long_ all_shortest_path_interfaces) {
        if (all_shortest_path_interfaces == null) {
            System.err.println("=====================================================================");
            System.err.println("Doing it for " + type.getName(context));
            all_shortest_path_interfaces = Factory_Vector_Type_long_.construct(context);
            Vector_Type_long_ all = type.getAllSuperInterfaces (context);        
            for (Iterator_Type_ ia = all.getNavigator(context);
                                ia.canGetCurrent(context);
                                ia.next(context)) {
                Type candidate = ia.tryGetCurrent(context);
                System.err.println("Testing: " + candidate.getName(context));
                boolean shortest_path = true;
                for (Iterator_Type_ ia2 = all.getNavigator(context);
                                    shortest_path && ia2.canGetCurrent(context);
                                    ia2.next(context)) {
                    Type other = ia2.tryGetCurrent(context);
                    if (other != candidate && other.isA(context, candidate)) {
                        System.err.println("No, someone's shorter: " + other.getName(context));
                        shortest_path = false;
                    }
                }
                if (shortest_path) {
                    System.err.println("Yups, appending");
                    all_shortest_path_interfaces.append(context, candidate);
                }
            }
            System.err.println("Done with it!");
        }
        return all_shortest_path_interfaces;
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
        if (! (type instanceof TypeImpl)) return false;
        if (this == type) { return true; }
        Class yourclass = ((TypeImpl) type).myclass;
        if ((this.myclass == null) != (yourclass == null)) { return false; }
        if (this.myclass == null) { return true; }
        
        if (this.myclass.isPrimitive() == true || yourclass.isPrimitive() == true || this.myclass.getClassLoader() == null || yourclass.getClassLoader() == null || this.myclass.getClassLoader().equals(yourclass.getClassLoader())) {
            return yourclass.isAssignableFrom(this.myclass);
        } else {
            Vector_Type_long_ sts = this.getSuperTypes(context);        
            for (Iterator_Type_ ist = sts.getNavigator(context);
                                ist.canGetCurrent(context);
                                ist.next(context)) {
                Type supertype = ist.tryGetCurrent(context);
                if (supertype.isA(context, type)) { return true; }
            }
            return false;
        }
    }    
}
