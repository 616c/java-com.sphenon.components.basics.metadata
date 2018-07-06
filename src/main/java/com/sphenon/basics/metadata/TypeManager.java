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
import com.sphenon.basics.message.*;
import com.sphenon.basics.exception.*;
import com.sphenon.basics.encoding.*;
import com.sphenon.basics.system.*;
import com.sphenon.basics.expression.*;
import com.sphenon.basics.expression.templates.*;
import com.sphenon.basics.customary.*;
import com.sphenon.basics.notification.*;
import com.sphenon.basics.configuration.*;
import com.sphenon.basics.performance.*;
import com.sphenon.basics.metadata.returncodes.*;
import com.sphenon.basics.metadata.exceptions.*;
import com.sphenon.basics.metadata.tplinst.*;
import com.sphenon.basics.many.tplinst.*;
import com.sphenon.basics.validation.returncodes.ValidationFailure;

import java.util.regex.*;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Vector;
import java.lang.reflect.Constructor;

import java.io.*;

public class TypeManager
{
    static protected long notification_level;
    static public    long adjustNotificationLevel(long new_level) { long old_level = notification_level; notification_level = new_level; return old_level; }
    static public    long getNotificationLevel() { return notification_level; }
    static { notification_level = NotificationLocationContext.getLevel(RootContext.getInitialisationContext(), "com.sphenon.basics.metadata.TypeManager"); };

    static protected Configuration config;
    static { config = Configuration.create(RootContext.getInitialisationContext(), "com.sphenon.basics.metadata.TypeManager"); };

    static public boolean isValidSearchPath(CallContext context, String spc_name) {
        String spc = SearchPathContext.getSearchPathConfiguration(context, spc_name);
        return (spc == null || spc.isEmpty() ? false : true);
    }

    static protected class SearchPathContext {
        static protected java.util.Map search_path_contexts;
        static public    java.util.Map getSearchPathContexts(CallContext context) { return search_path_contexts; }

        static {
            search_path_contexts = new java.util.concurrent.ConcurrentHashMap();
            MetaDataPackageInitialiser.initialise(RootContext.getInitialisationContext());
        }

        static String getSearchPathConfiguration(CallContext context, String name) {
            return Configuration.get(context, "com.sphenon.basics.metadata.TypeManager.SearchPathes", name, (String) null);
        }

        static SearchPathContext getSearchPathContext(CallContext context, String name) {
            SearchPathContext spc = (SearchPathContext) search_path_contexts.get(name);
            if (spc == null) {
                String search_path_property = getSearchPathConfiguration(context, name);
                if ((notification_level & Notifier.SELF_DIAGNOSTICS) != 0) { CustomaryContext.create((Context) context).sendTrace(context, Notifier.SELF_DIAGNOSTICS, "Initialising search path context '%(name)', value: '%(value)'", "name", name, "value", search_path_property); }
                if (search_path_property == null) {
                    CustomaryContext.create(Context.create(context)).throwConfigurationError(context, MetadataStringPool.get(context, "0.0.0" /* Property 'com.sphenon.basics.metadata.TypeManager.SearchPathes.%(name)' is not defined */), "name", name);
                    throw (ExceptionConfigurationError) null; // compiler insists
                }
                String parent_name_property = Configuration.get(context, "com.sphenon.basics.metadata.TypeManager.ParentTypeContext", name, (String) null);
                String[] parent_names = ((parent_name_property == null || parent_name_property.isEmpty()) ? null : parent_name_property.split(":"));
                SearchPathContext[] parents = null;
                if (parent_names != null) {
                    parents = new SearchPathContext[parent_names.length];
                    int i=0;
                    for (String parent_name : parent_names) {
                        parents[i++] = getSearchPathContext(context, parent_name);
                    }
                }

                if ((notification_level & Notifier.SELF_DIAGNOSTICS) != 0) { CustomaryContext.create((Context) context).sendTrace(context, Notifier.SELF_DIAGNOSTICS, "parent context is '%(name)'", "name", parent_name_property); }

                spc = new SearchPathContext(context, name, search_path_property, parents);
                search_path_contexts.put(name, spc);
            }
            return spc;
        }

        static void destroySearchPathContext(CallContext context, String name) {
            SearchPathContext spc = (SearchPathContext) search_path_contexts.get(name);
            if (spc != null) {
                spc.destroy(context);
            }
        }
        
        // hack, see TypeManager.SearchPathContext.iss file
        static void clearTypesByClassLoader(CallContext context, ClassLoader loader) {
            java.util.ArrayList<String> paths = new java.util.ArrayList();
            for (Object key : search_path_contexts.keySet()) {
                if (key instanceof String && loader != null) {
                    String skey = (String)key;
                    SearchPathContext spc = (SearchPathContext) search_path_contexts.get(skey);
                    if (spc != null) {
                        if (spc.cache != null && !spc.cache.isEmpty()) {
                            boolean to_remove = false;
                            for (Object otype : spc.cache.values()) {
                                if (otype instanceof JavaType) {
                                    JavaType jt = (JavaType)otype;
                                    if (jt.getJavaClass(context) != null && jt.getJavaClass(context).getClassLoader() != null && jt.getJavaClass(context).getClassLoader() == loader) {
                                        to_remove = true;
                                    }
                                }
                            }
                            if (to_remove) {
                                paths.add( skey );
                            }
                        }
                    }
                }
            }
            if (!paths.isEmpty()) {
                for (String key : paths) {
                    destroySearchPathContext(context, key);
                }
            }
        }

        protected String                      name;
        protected String                      search_path_string;
        protected Vector_String_long_         search_path;
        protected java.util.Map               cache;
        protected SearchPathContext[]         parents;
        protected Vector<SearchPathContext>   childs;

        public java.util.Map getCache(CallContext context) { return cache; }

        protected SearchPathContext (CallContext context, String name, String search_path_string, SearchPathContext[] parents) {
            this.name = name;
            this.search_path_string = search_path_string;
            this.search_path = Factory_Vector_String_long_.construct(context);
            this.cache = new java.util.concurrent.ConcurrentHashMap();
            this.parents = parents;
            if (this.parents != null) {
                for (SearchPathContext parent : this.parents) {
                    parent.addChild(context, this);
                }
            }
            if (search_path_string != null) {
                int last_pos = -1;
                int pos = -1;
                String spe;
                while ((pos = search_path_string.indexOf(":", last_pos + 1)) != -1) {
                    spe = search_path_string.substring(last_pos + 1, pos);
                    if (spe.length() > 0) {
                        search_path.append(context, spe);
                    }
                    last_pos = pos;
                }
                spe = search_path_string.substring(last_pos + 1);
                if (spe.length() > 0) {
                    search_path.append(context, spe);
                }
            }

            if (this.parents != null) {
                for (SearchPathContext parent : this.parents) {
                    for (String path : parent.getSearchPath(context).getIterable_String_(context)) {
                        search_path.append(context, path);
                    }
                    this.search_path_string += ":" + parent.getSearchPathString(context);
                }
            }
        }

        protected void addChild(CallContext context, SearchPathContext child) {
            if (this.childs == null) {
                this.childs = new Vector<SearchPathContext>();
            }
            this.childs.add(child);
        }

        protected void removeChild(CallContext context, SearchPathContext child) {
            this.childs.remove(child);
        }

        public void destroy(CallContext context) {
            if (this.name != null) {
                this.search_path_contexts.remove(this.name);
            }
            this.name = null;
            this.search_path_string = null;
            this.search_path = null;
            this.cache = null;
            if (this.parents != null) {
                for (SearchPathContext parent : this.parents) {
                    parent.removeChild(context, this);
                }
            }
            this.parents = null;
            if (this.childs != null) {
                for (SearchPathContext child : ((Vector<SearchPathContext>) (this.childs.clone()))) {
                    child.destroy(context);
                }
            }
            this.childs = null;
        }

        public String getName (CallContext call_context) {
            return this.name;
        }
        public String getSearchPathString (CallContext call_context) {
            return this.search_path_string;
        }
        public Vector_String_long_ getSearchPath (CallContext call_context) {
            return this.search_path;
        }
        public Type getFromCache(CallContext context, String key) {
            Type value = (Type) this.cache.get(key);
            if ((notification_level & Notifier.SELF_DIAGNOSTICS) != 0) { CustomaryContext.create((Context) context).sendTrace(context, Notifier.SELF_DIAGNOSTICS, "got '%(typename)' ('%(type)') with '%(key)' from '%(cache)'", "typename", value == null ? "" : value.getName(context), "type", value, "key", key, "cache", this.getName(context)); }
            if (value != null) { return value; }
            if (this.parents != null) {
                for (SearchPathContext parent : this.parents) {
                    value = parent.getFromCache(context, key);
                    if (value != null) { return value; }
                }
            }
            return null;
        }
        public void putIntoCache(CallContext context, String key, Type value) {
            ClassLoader cl = null;
            if (    value instanceof JavaType
                 && (cl = ((JavaType) value).getJavaClass(context).getClassLoader()) instanceof URLClassLoaderWithId
                 && cl instanceof URLClassLoaderWithId
                 && ((URLClassLoaderWithId) cl).getId(context).matches("DynamicClassLoader#.*")
               ) {
            } else {
                if ((notification_level & Notifier.SELF_DIAGNOSTICS) != 0) { CustomaryContext.create((Context) context).sendTrace(context, Notifier.SELF_DIAGNOSTICS, "adding '%(typename)' ('%(type)') as '%(key)' in '%(cache)'", "typename", value.getName(context), "type", value, "key", key, "cache", this.getName(context)); }
                this.cache.put(key, value);
            }
        }
    }

    static public void cleanupTypeContext(CallContext context, String type_context_name) {
        SearchPathContext.destroySearchPathContext(context, type_context_name);
    }
    
    static public void cleanupTypesByClassLoader(CallContext context, ClassLoader loader) {
        SearchPathContext.clearTypesByClassLoader(context, loader);
    }

    static public Vector_String_long_ getSearchPath(CallContext context) {
        TypeContext tc = TypeContext.get((Context) context);
        if (tc == null) { return null; }
        return SearchPathContext.getSearchPathContext(context, tc.getSearchPathContext(context)).getSearchPath(context);
    }

    static protected java.util.Vector<SpecificTypeFactory> type_factories;

    static public void registerSpecificTypeFactory(CallContext context, SpecificTypeFactory specific_type_factory) {
        if (type_factories == null) {
            type_factories = new java.util.Vector<SpecificTypeFactory>();
        }
        type_factories.add(specific_type_factory);
    }

    static protected java.util.Map primitive_types;

    static {
        CallContext context = RootContext.getInitialisationContext();
        primitive_types = new java.util.HashMap();

        primitive_types.put("boolean", "java.lang.Boolean");
        primitive_types.put("byte", "java.lang.Byte");
        primitive_types.put("char", "java.lang.Character");
        primitive_types.put("short", "java.lang.Short");
        primitive_types.put("int", "java.lang.Integer");
        primitive_types.put("long", "java.lang.Long");
        primitive_types.put("float", "java.lang.Float");
        primitive_types.put("double", "java.lang.Double");
    }

    static public Type getNamed (CallContext context, String type_name, Type super_type) {
        try {
            return get(context, type_name);
        } catch (NoSuchClass nsc) {
            CustomaryContext.create(Context.create(context)).throwPreConditionViolation(context, nsc, "jibbat nich");
            throw (ExceptionPreConditionViolation) null; // compiler insists

        }
    }
    static public Type getNamed (CallContext context, String type_name) {
        try {
            return get(context, type_name);
        } catch (NoSuchClass nsc) {
            CustomaryContext.create(Context.create(context)).throwPreConditionViolation(context, nsc, "jibbat nich");
            throw (ExceptionPreConditionViolation) null; // compiler insists

        }
    }

    static public Type get (CallContext context, String type_name) throws NoSuchClass {
        return get(context, type_name, false);
    }

    static public Type get (CallContext context, String type_name, boolean extensive) throws NoSuchClass {
        Type type = doGet(context, type_name, true, extensive, null, null);
        if (type == null) {
            TypeContext tc = TypeContext.get((Context)context);
            SearchPathContext spc = SearchPathContext.getSearchPathContext(context, tc.getSearchPathContext(context));
            NoSuchClass.createAndThrow(context, "looking for '%(class)', TypeManager search path context name '%(name)', path '%(path)'", "class", type_name == null ? "" : type_name, "name", spc.getName(context), "path", spc.getSearchPathString(context));
            throw (NoSuchClass) null; // compiler insists
        }
        return type;
    }

    static protected RegularExpression par1arg = new RegularExpression("([^<>,]+)<([^<>,]+)>");

    static public Type defineType (CallContext call_context, String type_name) {
        return defineType(call_context, type_name, null, false);
    }

    static public Type defineType (CallContext context, String type_name, Type super_type) {
        return defineType(context, type_name, super_type, false);
    }

    static public Type defineType (CallContext context, String type_name, Type super_type, boolean extensive) {
        TypeContext tc = TypeContext.get((Context) context);
        SearchPathContext spc = SearchPathContext.getSearchPathContext(context, tc.getSearchPathContext(context));
        return defineType(context, type_name, super_type, extensive, spc);
    }

    static public Type defineType (CallContext context, String type_name, Type super_type, boolean extensive, SearchPathContext spc) {
        return defineType(context, type_name, super_type, extensive, spc, false);
    }

    static public Type defineType (CallContext context, String type_name, Type super_type, boolean extensive, String spc_name, boolean allow_existing) {
        SearchPathContext spc = SearchPathContext.getSearchPathContext(context, spc_name);
        return defineType(context, type_name, super_type, extensive, spc, allow_existing);
    }

    static public boolean sanity_check = true;
    static public boolean sanity_check_warning = false;

    static public Type defineType (CallContext context, String type_name, Type super_type, boolean extensive, SearchPathContext spc, boolean allow_existing) {
        if ((notification_level & Notifier.SELF_DIAGNOSTICS) != 0) { NotificationContext.sendTrace(context, Notifier.SELF_DIAGNOSTICS, "Defining type '%(typename)', super type '%(supertype)' in '%(context)'", "typename", type_name, "supertype", super_type, "context", spc.getName(context)); }

        if (sanity_check) {
            if (sanity_check_warning == false) {
                if ((notification_level & Notifier.OBSERVATION) != 0) { NotificationContext.sendNotice(context, "Sanity check enabled (performance issue, disable for production)"); }
                sanity_check_warning = true;
            }
            TypeContext tc = TypeContext.get((Context) context);
            String name1 = spc == null ? null : spc.getName(context);
            String name2 = tc == null ? null : tc.getSearchPathContext(context);
            if (   (name1 == null && name2 != null)
                || (name1 != null && name2 == null)
                || (name1 != null && name2 != null && name1.equals(name2) == false)
               ) {
                if ((notification_level & Notifier.MONITORING) != 0) { NotificationContext.sendCaution(context, "Explicit search path context provided by argument '(spc1)' and implicit search path context found in type context '(spc2)' do not match; consider checking call stack", "spc1", name1, "spc2", name2); }
            }
        }

        Type type = spc.getFromCache(context, type_name);
        if (type != null && Type_Invalid.getSingleton(context).equals(type)) {
            type = null;
        }
        if (type != null) {
            if (allow_existing) {
                return type;
            } else {
                CustomaryContext.create((Context)context).throwConfigurationError(context, "Type '%(typename)' is already defined", "typename", type_name);
                throw (ExceptionConfigurationError) null; // compiler insists
            }
        }

        TemplateInstance ti = null;
        try {
            ti = TemplateInstanceParser.parse(context, type_name);
        } catch (ParseException pe) {
            CustomaryContext.create((Context)context).throwPreConditionViolation(context, pe, "Invalid template expression in type '%(typename)'", "typename", type_name);
            throw (ExceptionPreConditionViolation) null; // compiler insists
        }

        if (ti.getNumberOfArguments(context) != 0) {
            try {
                Type base_type = get(context, ti.getIdentifier(context), extensive);
                Object[] type_parameters = new Type[ti.getNumberOfArguments(context)];
                int tindex = 0;
                for (TemplateInstanceArguments tia : ti.getArguments(context)) {
                    for (TemplateInstance tip : tia.getArguments(context)) {
                        type_parameters[tindex++] = get(context, tip.getExpressionString(context), extensive);
                    }
                }
                type = getParametrised(context, base_type, type_parameters);
            } catch (NoSuchClass nsc) {
                CustomaryContext.create((Context)context).throwConfigurationError(context, nsc, "Type '%(typename)' cannot be defined, base or paremeter type does not exist", "typename", type_name);
                throw (ExceptionConfigurationError) null; // compiler insists
            }
        } else {
            if (super_type != null) {
                if ((notification_level & Notifier.SELF_DIAGNOSTICS) != 0) { NotificationContext.sendTrace(context, Notifier.SELF_DIAGNOSTICS, "...creating with supertype"); }
                type = new TypeImplNamed(context, type_name, super_type);
            } else {
                if ((notification_level & Notifier.SELF_DIAGNOSTICS) != 0) { NotificationContext.sendTrace(context, Notifier.SELF_DIAGNOSTICS, "...creating without supertype"); }
                type = new TypeImplNamed(context, type_name);
            }
        }
        spc.putIntoCache(context, type_name, type);

        return type;
    }

    static public Type tryGet (CallContext context, String type_name) {
        try {
            return doGet(context, type_name, false, false, null, null);
        } catch (NoSuchClass nsc) {
            return null; // should not happen
        }
    }

    static public Type mustGet (CallContext context, String type_name) {
        try {
            return doGet(context, type_name, false, false, null, null);
        } catch (NoSuchClass nsc) {
            CustomaryContext.create((Context)context).throwPreConditionViolation(context, nsc, "Type '%(type)' does not exist", "type", type_name);
            throw (ExceptionPreConditionViolation) null; // compiler insists
        }
    }

    static public Type tryGetById (CallContext context, String type_id) {
        try {
            return doGet(context, type_id, false, true, null, null);
        } catch (NoSuchClass nsc) {
            return null; // should not happen
        }
    }

    // SearchPathCache Issue: Bitte das TypeManager.SearchPathCache.iss File hierzu lesen
    static protected Type doGet (CallContext context, String type_name, boolean throw_exception, boolean extensive, String alias, SearchPathContext spc) throws NoSuchClass {
        if (spc == null) {
            TypeContext tc = TypeContext.get((Context) context);
            spc = SearchPathContext.getSearchPathContext(context, tc.getSearchPathContext(context));
        }

        if ((notification_level & Notifier.DIAGNOSTICS) != 0) { NotificationContext.sendTrace(context, Notifier.DIAGNOSTICS, "Looking for type '%(type)' in '%(name)'", "type", type_name, "name", spc.getName(context)); }

        if (type_name == null || type_name.equals("")) { return TypeImpl_Null.getSingleton(context); }

        if (type_name != null && type_name.equals("<INVALID>")) {
            if (alias != null && alias.length() != 0) {
                spc.putIntoCache(context, alias, Type_Invalid.getSingleton(context));
            }
            return Type_Invalid.getSingleton(context);
        }

        if (extensive) {
            if (type_name.startsWith("Java::")) {
                type_name = type_name.substring(6);
            } else if (type_name.startsWith("Named::")) {
                type_name = type_name.substring(7);
            }
        }

        boolean got_dot  = false;
        boolean is_array = false;
        boolean is_parametrised = false;
        int tnl = 0;

        Type type = spc.getFromCache(context, type_name);
        if (type != null && Type_Invalid.getSingleton(context).equals(type)) {
            return null;
        }
        if (type == null) {
            java.text.StringCharacterIterator sci = new java.text.StringCharacterIterator(type_name);
            boolean last_is_clbr       = false;
            boolean last_is_opbr       = false;
            boolean last_is_gt         = false;
            boolean got_lt             = false;
            boolean beforelast_is_opbr = false;
            for (char c = sci.first(); c != java.text.CharacterIterator.DONE; c = sci.next()) {
                tnl++;
                beforelast_is_opbr = last_is_opbr;
                switch (c) {
                    case '.':
                        got_dot = true;
                        last_is_opbr = false;
                        last_is_clbr = false;
                        last_is_gt   = false;
                        break;
                    case '[':
                        last_is_opbr = true;
                        last_is_clbr = false;
                        last_is_gt   = false;
                        break;
                    case ']':
                        last_is_opbr = false;
                        last_is_clbr = true;
                        last_is_gt   = false;
                        break;
                    case '>':
                        last_is_opbr = false;
                        last_is_clbr = false;
                        last_is_gt   = true;
                        break;
                    case '<':
                        got_lt       = true;
                        last_is_opbr = false;
                        last_is_clbr = false;
                        last_is_gt   = false;
                        break;
                    default :
                        last_is_opbr = false;
                        last_is_clbr = false;
                        last_is_gt   = false;
                        break;
                }
            }
            if (beforelast_is_opbr && last_is_clbr) {
                is_array = true;
            }
            if (got_lt && last_is_gt) {
                is_parametrised = true;
            }
            if (got_dot == false) {
                for (int i=0; type == null && i<spc.getSearchPath(context).getSize(context); i++) {
                    StringBuffer sb = new StringBuffer();
                    sb.append(spc.getSearchPath(context).tryGet(context, i));
                    sb.append(".");
                    sb.append(type_name);
                    String fullclass = sb.toString();
                    type = (Type) spc.getFromCache(context, fullclass);
                }
                if (type != null) {
                    spc.putIntoCache(context, type_name, type);
                }
            }
        }
        if (type != null) {
            if ((notification_level & Notifier.DIAGNOSTICS) != 0) { NotificationContext.sendTrace(context, Notifier.DIAGNOSTICS, "Type is already in cache"); }
            if (alias != null && alias.equals(type_name) == false) {
                spc.putIntoCache(context, alias, type);
            }
            if (Type_Invalid.getSingleton(context).equals(type)) {
                return null;
            }
            return type;
        }
        String base_type_name = null;
        if (is_array) {
            base_type_name = type_name.substring(0,tnl-2);
        } else {
            base_type_name = type_name;
        }
        Object ptc = primitive_types.get(base_type_name);
        if (ptc != null) {
            try {
                base_type_name = (String) ptc;
                got_dot = true;
            } catch (ClassCastException cce) { }
        } else {

            // try registered specific factories (e.g. from factory site for aggregates)
            if (type_factories != null) {
                for (SpecificTypeFactory stf : type_factories) {
                    Type t = null;
                    try {
                        t = stf.tryCreation(context, type_name);
                    } catch (ValidationFailure vf) {
                        if (throw_exception) {
                            NoSuchClass.createAndThrow(context, vf, "");
                        }
                    }
                    if (t != null) {
                        spc.putIntoCache(context, type_name, t);
                        if (alias != null && alias.equals(type_name) == false) {
                            spc.putIntoCache(context, alias, t);
                        }
                        return t;
                    }
                }
            }
        }

        if (is_parametrised) {
            Type t = defineType(context, type_name, null, extensive, spc);
            if (alias != null && alias.equals(type_name) == false) {
                spc.putIntoCache(context, alias, t);
            }
            return t;
        }

        if (got_dot == true) {
            try {
                if ((notification_level & Notifier.DIAGNOSTICS) != 0) { NotificationContext.sendTrace(context, Notifier.DIAGNOSTICS, "Trying '%(type)'", "type", type_name); }
                type = new TypeImpl(context, com.sphenon.basics.cache.ClassCache.getClassForName(context, is_array == false ? base_type_name : "[L" + base_type_name + ";"));
                spc.putIntoCache(context, type_name, type);
                if (alias != null && alias.equals(type_name) == false) {
                    spc.putIntoCache(context, alias, type);
                }
                return type;
            } catch (ClassNotFoundException cnfe1) {
            }
        } else {
            for (int i=0; i<spc.getSearchPath(context).getSize(context); i++) {
                StringBuffer sb = new StringBuffer();
                if (is_array) sb.append("[L");
                sb.append(spc.getSearchPath(context).tryGet(context, i));
                sb.append(".");
                sb.append(is_array == false ? type_name : base_type_name);
                if (is_array) sb.append(";");
                String fullclass = sb.toString();
                try {
                    if ((notification_level & Notifier.DIAGNOSTICS) != 0) { NotificationContext.sendTrace(context, Notifier.DIAGNOSTICS, "Trying '%(type)'", "type", fullclass); }
                    type = new TypeImpl(context, com.sphenon.basics.cache.ClassCache.getClassForName(context, fullclass));
                    spc.putIntoCache(context, type_name, type);
                    if (type_name.equals(fullclass) == false) {
                        spc.putIntoCache(context, fullclass, type);
                        if (alias != null && alias.equals(type_name) == false) {
                            spc.putIntoCache(context, alias, type);
                        }
                    }
                    return type;
                } catch (ClassNotFoundException cnfe2) {
                    continue;
                }
            }
        }

        if ((notification_level & Notifier.DIAGNOSTICS) != 0) { NotificationContext.sendTrace(context, Notifier.DIAGNOSTICS, "Type not found in '%(context)'", "context", spc); }

        spc.putIntoCache(context, type_name, Type_Invalid.getSingleton(context));
        if (alias != null && alias.equals(type_name) == false) {
            spc.putIntoCache(context, alias, Type_Invalid.getSingleton(context));
        }

        return null;
    }

    static public Type get (CallContext context, java.lang.reflect.Type type) {
        return get(context, type, null);
    }

    static public Type get (CallContext context, java.lang.reflect.Type type, Type class_type) {
        if (type instanceof Class) {
            return get(context, (Class) type);
        }
        if (type instanceof java.lang.reflect.ParameterizedType) {
            return getParametrised (context, (java.lang.reflect.ParameterizedType) type);
        }
        if (type instanceof java.lang.reflect.GenericArrayType) {
            if (((java.lang.reflect.GenericArrayType) type).getGenericComponentType() instanceof Class) {
                try {
                    return new TypeImpl(context, com.sphenon.basics.cache.ClassCache.getClassForName(context, "[L" + ((Class)(((java.lang.reflect.GenericArrayType) type).getGenericComponentType())).getName() + ";"));
                } catch (ClassNotFoundException cnfe) {
                }
            }
            // what if not? ...
        }
        if (type instanceof java.lang.reflect.TypeVariable) {
            if (class_type == null) {
                return get(context, Object.class);
            }
            if ((class_type instanceof TypeParametrised) == false) {
                return get(context, Object.class);
            }

            Vector_String_long_ pns = ((TypeParametrised)class_type).getParameterNames(context);
            if (pns == null) {
                CustomaryContext.create((Context)context).throwPreConditionViolation(context, "Type '%(type)' is variable and parametrised, but has no parameter names", "type", type.getClass().getName());
                throw (ExceptionPreConditionViolation) null; // compiler insists
            }

            String name = ((java.lang.reflect.TypeVariable) type).getName();
            int index = -1;
            for(int i=0; i<pns.getSize(context); i++) {
                String variable_name = pns.tryGet(context, i);
                if (name.equals(variable_name)) {
                    index = i;
                    break;
                }
            }
            if (index == -1) {
                CustomaryContext.create((Context)context).throwPreConditionViolation(context, "Type '%(type)' is variable and parametrised, but name not found among declared names", "type", type.getClass().getName());
                throw (ExceptionPreConditionViolation) null; // compiler insists
            }

            Vector_Object_long_ ps = ((TypeParametrised)class_type).getParameters(context);
            if (ps == null) {
                CustomaryContext.create((Context)context).throwPreConditionViolation(context, "Type '%(type)' is variable and parametrised, but has no parameters", "type", type.getClass().getName());
                throw (ExceptionPreConditionViolation) null; // compiler insists
            }
            Object variable = ps.tryGet(context, index);
            if (variable == null) {
                CustomaryContext.create((Context)context).throwPreConditionViolation(context, "Type '%(type)' is variable and parametrised, and type variable found, but corresponding parameter does not exist", "type", type.getClass().getName());
                throw (ExceptionPreConditionViolation) null; // compiler insists
            }
            if ((variable instanceof Type) == false) {
                CustomaryContext.create((Context)context).throwPreConditionViolation(context, "Type '%(type)' is variable and parametrised, and type variable found, but corresponding parameter '%(parameter)' is not a Type", "type", type.getClass().getName(), "parameter", variable);
                throw (ExceptionPreConditionViolation) null; // compiler insists
            }
            
            return (Type) variable;
        }
        CustomaryContext.create((Context)context).throwLimitation(context, "Type '%(type)' is currently not supported by type manager", "type", type.getClass().getName());
        throw (ExceptionLimitation) null; // compiler insists
    }

    static public Type get (CallContext context, Class yourclass) {
        TypeContext tc = TypeContext.get((Context) context);
        SearchPathContext spc = SearchPathContext.getSearchPathContext(context, tc.getSearchPathContext(context));
        
        if (yourclass == null) { return TypeImpl_Null.getSingleton(context); }

        Type type = spc.getFromCache(context, yourclass.getName());
        if (type != null && Type_Invalid.getSingleton(context).equals(type)) {
            type = null;
        }
        if (type != null) {
            return type;
        }
        type = new TypeImpl(context, yourclass);
        spc.putIntoCache(context, yourclass.getName(), type);
        return type;
    }

    static public Type get (CallContext context, Object instance) {
        if (instance instanceof Typed) {
            Type type = ((Typed) instance).getType(context);
            if (type != null) { return type; }
        }

        return get(context, instance.getClass());
    }

    static public Type getMediaTypeRoot (CallContext context) {
        return getCachedMediaType(context, null, null, null, true, false);
    }

    static public Type getMediaTypeDirectory (CallContext context) {
        return getCachedMediaType(context, null, null, null, false, true);
    }

    static public Type getMediaType (CallContext context, File media_object) {
        return getCachedMediaType(context, media_object, null, null, false, false);
    }

    static public Type getMediaType (CallContext context, String extension) {
        return getCachedMediaType(context, null, extension, null, false, false);
    }

    static public Type getMediaType (CallContext context, String type, String subtype) {
        MIMEType mt = MIMEType.getMIMEType(context, type, subtype);
        return getCachedMediaType(context, null, null, mt, false, false);
    }

    static public Type getMediaTypeMIME (CallContext context, String mime_type) {
        if (mime_type == null || mime_type.isEmpty() || mime_type.indexOf("/") == -1) {
            System.err.println("Invalid MIME type in TypeManager: " + mime_type);
            return getMediaType_text_plain(context);
        }
        String[] mt = mime_type.split("/");
        return getMediaType (context, mt[0], mt[1]);
    }

    static public Type media_type_text_plain;
    static public Type getMediaType_text_plain (CallContext context) {
        if (media_type_text_plain == null) {
            media_type_text_plain = getMediaType(context, "text", "plain");
        }
        return media_type_text_plain;
    }

    static protected Type getCachedMediaType (CallContext context, File media_object, String extension, MIMEType mt, boolean get_root, boolean get_directory) {
        TypeContext tc = TypeContext.get((Context) context);
        SearchPathContext spc = SearchPathContext.getSearchPathContext(context, tc.getSearchPathContext(context));

        TypeImpl_MediaObject media_type = (get_root ?
                                             new TypeImpl_MediaObject(context, true)
                                           : (get_directory ?
                                                new TypeImpl_MediaObject(context, false)
                                              : (media_object != null ? 
                                                   new TypeImpl_MediaObject(context, media_object, getMediaTypeRoot(context))
                                                 : (extension != null ? 
                                                      new TypeImpl_MediaObject(context, extension, getMediaTypeRoot(context))
                                                    : new TypeImpl_MediaObject(context, mt, getMediaTypeRoot(context))
                                                   )
                                                )
                                             )
                                          );
        String media_type_name = media_type.getName(context);

        if ((notification_level & Notifier.DIAGNOSTICS) != 0) { NotificationContext.sendTrace(context, Notifier.DIAGNOSTICS, "TypeManager MediaType request: file '%(file)', extension '%(extension)', root: '%(root)', directory: '%(directory), result: '%(name)/%(type)''", "file", (media_object == null ? "null" : media_object.getName()), "extension", extension, "root", t.s(get_root), "directory", t.s(get_directory), "name", media_type.getName(context), "type", media_type.getMediaType(context)); }

        Type existing_type = spc.getFromCache(context, media_type_name);
        if (existing_type != null && Type_Invalid.getSingleton(context).equals(existing_type)) {
            existing_type = null;
        }
        if (existing_type != null) {
            return existing_type;
        }
        spc.putIntoCache(context, media_type_name, media_type);
        return media_type;
    }

    static public Type getParametrised (CallContext context, Type base_type, Object... parameters) {
        return getCachedParametrised (context, new TypeParametrisedImpl(context, base_type, parameters));
    }

    static public Type getParametrised (CallContext context, Class base_class, Object... parameters) {
        return getCachedParametrised (context, new TypeParametrisedImpl(context, TypeManager.get(context, base_class), parameters));
    }

    static public Type getParametrised (CallContext context, Type base_type, Vector_Object_long_ parameters) {
        return getCachedParametrised (context, new TypeParametrisedImpl(context, base_type, parameters));
    }

    static public Type getParametrised (CallContext context, java.lang.reflect.ParameterizedType parameterized_type) {
        TypeParametrisedImplGenerics tpig = new TypeParametrisedImplGenerics(context, parameterized_type);
        if (tpig.getIsUnspecific(context)) {
            return get(context, (Class) parameterized_type.getRawType());
        }
        return getCachedParametrised (context, tpig);
    }

    static protected Type getCachedParametrised (CallContext context, Type test_type) {
        TypeContext tc = TypeContext.get((Context) context);
        SearchPathContext spc = SearchPathContext.getSearchPathContext(context, tc.getSearchPathContext(context));

        String type_name = test_type.getName(context);

        if ((notification_level & Notifier.SELF_DIAGNOSTICS) != 0) { NotificationContext.sendTrace(context, Notifier.SELF_DIAGNOSTICS, "Looking for parametrised type '%(typename)' in '%(name)'", "typename", type_name, "name", spc.getName(context)); }

        Type type = spc.getFromCache(context, type_name);
        if (type != null && Type_Invalid.getSingleton(context).equals(type)) {
            type = null;
        }
        if (type != null) {
            if ((notification_level & Notifier.SELF_DIAGNOSTICS) != 0) { NotificationContext.sendTrace(context, Notifier.SELF_DIAGNOSTICS, "...already in cache"); }
            return type;
        } else {
            if ((notification_level & Notifier.SELF_DIAGNOSTICS) != 0) { NotificationContext.sendTrace(context, Notifier.SELF_DIAGNOSTICS, "...not in cache, inserting..."); }
            type = test_type;
            spc.putIntoCache(context, type_name, type);
        }
        return type;
    }

    static public TypeTuple getTuple (CallContext context, Type parameter1) {
        return getCachedTuple (context, new TypeTupleImpl(context, parameter1));
    }

    static public TypeTuple getTuple (CallContext context, Type parameter1, Type parameter2) {
        return getCachedTuple (context, new TypeTupleImpl(context, parameter1, parameter2));
    }

    static public TypeTuple getTuple (CallContext context, Type parameter1, Type parameter2, Type parameter3) {
        return getCachedTuple (context, new TypeTupleImpl(context, parameter1, parameter2, parameter3));
    }

    static public TypeTuple  getTuple (CallContext context, Vector_Type_long_ parameters) {
        return getCachedTuple (context, new TypeTupleImpl(context, parameters));
    }

    static protected TypeTuple getCachedTuple (CallContext context, Type test_type) {
        TypeContext tc = TypeContext.get((Context) context);
        SearchPathContext spc = SearchPathContext.getSearchPathContext(context, tc.getSearchPathContext(context));

        String type_name = test_type.getName(context);

        if ((notification_level & Notifier.SELF_DIAGNOSTICS) != 0) { NotificationContext.sendTrace(context, Notifier.SELF_DIAGNOSTICS, "Looking for parametrised type '%(typename)' in '%(name)'", "typename", type_name, "name", spc.getName(context)); }

        Type type = spc.getFromCache(context, type_name);
        if (type != null && Type_Invalid.getSingleton(context).equals(type)) {
            type = null;
        }
        if (type != null) {
            if ((notification_level & Notifier.SELF_DIAGNOSTICS) != 0) { NotificationContext.sendTrace(context, Notifier.SELF_DIAGNOSTICS, "...already in cache"); }
            return (TypeTuple) type;
        } else {
            if ((notification_level & Notifier.SELF_DIAGNOSTICS) != 0) { NotificationContext.sendTrace(context, Notifier.SELF_DIAGNOSTICS, "...not in cache, inserting..."); }
            type = test_type;
            spc.putIntoCache(context, type_name, type);
        }
        return (TypeTuple) type;
    }

    static public void saveCacheOnExit(CallContext context) {
        com.sphenon.basics.cache.ClassCache.remember_classes = true;
        java.lang.Runtime.getRuntime().addShutdownHook(new Thread() { public void run() { saveCache(RootContext.getDestructionContext()); } });
    }

    static public boolean defer_save_cache;
    static public boolean save_cache_deferred;

    static final protected String     cp_cocp      = "(?:cache\\.cocp)";
    static final protected String     cp_templates = "(?:.*\\.templates)";
    static final protected String     cp_ee        = "(?:\\[L)?(?:(?:com\\.sphenon\\.(?:(?:ad\\.interaction)|(?:sm\\.(?:versioning|basics))|(?:basics\\.work2)|(?:domains\\.basics\\.(?:actors|analytics|workable|messages))))|(?:org\\.ooem)|(?:net\\.venfragrid))";

    static final protected String[][] cache_parts = {
        { ""          , ".*"                        , "^(?:" + cp_cocp + "|" + cp_templates + "|" + cp_ee + ")\\..*" },
        { "_COCP"     , "^" + cp_cocp      + "\\..*", "^(?:" +                 cp_templates + "|" + cp_ee + ")\\..*" },
        { "_Templates", "^" + cp_templates + "\\..*", "^(?:" + cp_cocp + "|" +                      cp_ee + ")\\..*" },
        { "_EE"       , "^" + cp_ee        + "\\..*", "^(?:" + cp_cocp + "|" + cp_templates +               ")\\..*" }
    };

    static protected int tmc_verbose = 1; // 0: none, 1: summary, 2: detailed
    static protected int cc_verbose = 1; // 0: none, 1: summary, 2: detailed

    static public void saveCache(CallContext context) {
        if (defer_save_cache) {
            save_cache_deferred = true;
            return;
        }
        int tmc_written = 0;
        int tmc_skipped = 0;

        String filename   = config.get(context, "CacheFile", (String) null);
        String ccfilename = config.get(context, "ClassCacheFile", (String) null);
        String ccinre = config.get(context, "ClassCacheInclude", (String) null);
        String ccexre = config.get(context, "ClassCacheExclude", (String) null);
        String type_context_exclude = config.get(context, "Cache.TypeContext.Exclude", (String) null);
        String type_context_include = config.get(context, "Cache.TypeContext.Include", (String) null);
        String type_exclude = config.get(context, "Cache.Type.Exclude", (String) null);
        String type_include = config.get(context, "Cache.Type.Include", (String) null);
        try {
            if (filename != null) {
                File f = new File(filename);
                f.setWritable(true);
                FileOutputStream fos = new FileOutputStream(f);
                OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
                BufferedWriter bw = new BufferedWriter(osw);
                PrintWriter pw = new PrintWriter(bw);

                pw.print("com.sphenon.basics.metadata.TypeManager.Cache=");

                boolean firstentry = true;

                for (Object ome : SearchPathContext.getSearchPathContexts(context).entrySet()) {
                    Map.Entry me = (Map.Entry) ome;
                    String tcname = ((String)(me.getKey()));
                    if (    (    type_context_include == null
                              || tcname.matches(type_context_include) == true
                            )
                         && (    type_context_exclude == null
                              || tcname.matches(type_context_exclude) == false
                            )
                       ) {
                        // System.err.println("NOW SAVING: " + tcname);

                        pw.print((firstentry ? "" : ";") + tcname + "=");
                        firstentry = false;
                        SearchPathContext spc = (SearchPathContext) me.getValue();
                        boolean first = true;
                        for (Object ome2 : spc.getCache(context).entrySet()) {
                            Map.Entry me2 = (Map.Entry) ome2;
                            if (((Type)me2.getValue() instanceof TypeTuple) == false) {
                                String tname = (String)me2.getKey();
                                if (    (    type_context_include == null
                                          || tname.matches(type_context_include) == true
                                        )
                                     && (    type_context_exclude == null
                                          || tname.matches(type_context_exclude) == false
                                        )
                                   ) {
                                    String tid = ((Type)me2.getValue()).getId(context);
                                    if (    tid.matches("[0-9A-Za-z_<>≤≥≦≧=,.:$ \n\r\t\f-]*")
                                         && tid.matches("Media::.*") == false
                                         && tid.equals("<INVALID>") == false
                                       ) {
                                        if ( ! first) { pw.print(","); }
                                        first = false;
                                        pw.print(Encoding.recode(context, tname, Encoding.UTF8, Encoding.URI));
                                        pw.print("+");
                                        pw.print(Encoding.recode(context, tid, Encoding.UTF8, Encoding.URI));
                                        tmc_written++;
                                    } else {
                                        if (tmc_verbose >= 2) {
                                            System.err.println("TMC SKIPPED: " + tid);
                                        }
                                        tmc_skipped++;
                                    }
                                }
                            }
                        }
                    }
                }
                pw.println("");

                pw.close();
                bw.close();
                osw.close();
                fos.close();
            }
        } catch (FileNotFoundException fnfe) {
            CustomaryContext.create((Context)context).throwPreConditionViolation(context, fnfe, "Cannot write to file '%(filename)'", "filename", filename);
            throw (ExceptionPreConditionViolation) null; // compiler insists
        } catch (UnsupportedEncodingException uee) {
            CustomaryContext.create((Context)context).throwPreConditionViolation(context, uee, "Cannot write to file '%(filename)'", "filename", filename);
            throw (ExceptionPreConditionViolation) null; // compiler insists
        } catch (IOException ioe) {
            CustomaryContext.create((Context)context).throwPreConditionViolation(context, ioe, "Cannot write to file '%(filename)'", "filename", filename);
            throw (ExceptionPreConditionViolation) null; // compiler insists
        }

        if (tmc_verbose >= 1) {
            System.err.println("Type Manager Cache: written " + tmc_written + ", skipped " + tmc_skipped);
        }

        int cc_written = 0;
        int cc_skipped = 0;

        try {
            if (ccfilename != null && com.sphenon.basics.cache.ClassCache.all_class_caches != null) {

                for (String clid : com.sphenon.basics.cache.ClassCache.all_class_caches.keySet()) {
                    Map<String,Class> cc = com.sphenon.basics.cache.ClassCache.all_class_caches.get(clid);
                    Set<String> cnfc = com.sphenon.basics.cache.ClassCache.all_class_not_found_caches.get(clid);

                    int pi = 0;
                    String jclid = clid.replaceAll("[^A-Za-z0-9_]","_");
                    for (String[] cache_part : cache_parts) {

                        File f = new File(ccfilename + "_" + jclid + cache_part[0] + ".java");
                        f.setWritable(true);
                        FileOutputStream ccfos = new FileOutputStream(f);
                        OutputStreamWriter ccosw = new OutputStreamWriter(ccfos, "UTF-8");
                        BufferedWriter ccbw = new BufferedWriter(ccosw);
                        PrintWriter ccpw = new PrintWriter(ccbw);

                        ccpw.print("package com.sphenon.basics.metadata;\n");
                        ccpw.print("import com.sphenon.basics.cache.*;\n");
                        ccpw.print("import com.sphenon.basics.context.*;\n");
                        ccpw.print("import com.sphenon.basics.context.classes.*;\n");
                        ccpw.print("import com.sphenon.basics.system.*;\n");
                        ccpw.print("import java.util.*;\n");
                        ccpw.print("import java.lang.reflect.*;\n");
                        ccpw.print("public class ClassCacheDataImpl_" +  jclid + cache_part[0]);
                        if (pi == 0) {
                            ccpw.print(" implements ClassCacheData" + " {\n");
                            ccpw.print("    public Map<String,Class> get() {\n");
                            ccpw.print("        Map<String,Class> cc = new HashMap<String,Class>();\n");
                            ccpw.print("        addToMap(cc);\n");
                            ccpw.print("        ClassCacheDataImpl_" +  jclid + "_COCP.addToMap(cc);\n");
                            ccpw.print("        ClassCacheDataImpl_" +  jclid + "_Templates.addToMap(cc);\n");
                            ccpw.print("        tryToAddToMap(cc, \"com.sphenon.basics.metadata.ClassCacheDataImpl_" +  jclid + "_EE\");\n");
                            ccpw.print("        return cc;\n");
                            ccpw.print("    }\n");
                            ccpw.print("    public void tryToAddToMap(Map<String,Class> cc, String class_name) {\n");
                            ccpw.print("        CallContext context = RootContext.getRootContext();\n");
                            ccpw.print("        ReflectionUtilities ru = new ReflectionUtilities(context);\n");
                            ccpw.print("        ClassLoader cl = Thread.currentThread().getContextClassLoader();\n");
                            ccpw.print("        Method method = null;\n");
                            ccpw.print("        try {\n");
                            ccpw.print("            Class ccd = Class.forName(class_name, true, cl);\n");
                            ccpw.print("            method = ru.tryGetMethod(context, ccd, \"addToMap\", Map.class);\n");
                            ccpw.print("        } catch (ClassNotFoundException cnfe) {\n");
                            ccpw.print("        }\n");
                            ccpw.print("        if (method != null) {\n");
                            ccpw.print("            Object exception = ru.tryInvoke(context, method, null, cc);\n");
                            ccpw.print("            if (exception != null) {\n");
                            ccpw.print("                System.err.println(\"Could not load class cache 'EE' - \" + exception.toString());\n");
                            ccpw.print("            }\n");
                            ccpw.print("        }\n");
                            ccpw.print("    }\n");
                        } else {
                            ccpw.print(" {\n");
                        }
                        ccpw.print("    static public void addToMap(Map<String,Class> cc) {\n");
                        for (Map.Entry<String,Class> ome2 : cc.entrySet()) {
                            Map.Entry<String,Class> me2 = ome2;
                            String name = me2.getKey();
                            if (    (ccinre == null || name.matches(ccinre) == true)
                                 && (ccexre == null || name.matches(ccexre) == false)
                                 && name.matches(cache_part[1]) == true
                                 && name.matches(cache_part[2]) == false
                               ) {
                                Class c = ((Class)me2.getValue());
                                String clsname = c.isArray() ? c.getComponentType().getName() : c.getName();
                                if (clsname.matches(".*\\$[0-9]+$") == false) {
                                    ccpw.print("        cc.put(\"" + name + "\", ");
                                    ccpw.print(clsname.replace('$','.'));
                                    if (c.isArray()) { ccpw.print("[]"); }
                                    ccpw.print(".class);\n");
                                }
                                cc_written++;
                            } else {
                                cc_skipped++;
                            }
                        }
                        ccpw.print("    }\n");
                        if (pi == 0) {
                            ccpw.print("    public Set<String> getNotFound() {\n");
                            ccpw.print("        Set<String> cnfc = new HashSet<String>();\n");
                            //          die liste wird zu lang (problem anders gelöst: neuer cache in factorysite)
                            //                     for (String name : cnfc) {
                            //                         if (    (ccinre == null || name.matches(ccinre) == true)
                            //                              && (ccexre == null || name.matches(ccexre) == false)
                            //                            ) {
                            //                             ccpw.print("        cnfc.put(\"" + name + "\");\n");
                            //                         }
                            //                     }
                            ccpw.print("        return cnfc;\n");
                            ccpw.print("    }\n");
                        }
                        ccpw.print("}\n");

                        ccpw.close();
                        ccbw.close();
                        ccosw.close();
                        ccfos.close();

                        pi++;
                    }
                }
            }
        } catch (FileNotFoundException fnfe) {
            CustomaryContext.create((Context)context).throwPreConditionViolation(context, fnfe, "Cannot write to file '%(filename)'", "filename", ccfilename);
            throw (ExceptionPreConditionViolation) null; // compiler insists
        } catch (UnsupportedEncodingException uee) {
            CustomaryContext.create((Context)context).throwPreConditionViolation(context, uee, "Cannot write to file '%(filename)'", "filename", ccfilename);
            throw (ExceptionPreConditionViolation) null; // compiler insists
        } catch (IOException ioe) {
            CustomaryContext.create((Context)context).throwPreConditionViolation(context, ioe, "Cannot write to file '%(filename)'", "filename", ccfilename);
            throw (ExceptionPreConditionViolation) null; // compiler insists
        }

        if (cc_verbose >= 1) {
            System.err.println("Class Cache: written " + cc_written + ", skipped " + cc_skipped);
        }
    }

    static protected RegularExpression cache_entry_re = new RegularExpression("([^;=]+)=([^;]+)(?:;?)");

    static public void loadCache(CallContext context) {
        String cache = config.get(context, "Cache", (String) null);
        if (cache == null) { return; }

        StopWatch stop_watch = StopWatch.optionallyCreate (context, "com.sphenon.basics.metadata.TypeManager", "Cache", Notifier.SELF_DIAGNOSTICS);
        if (stop_watch != null) {
            stop_watch.start(context, "load cache begin");
        }

        Matcher m = cache_entry_re.getMatcher(context, cache);
        while (m.find()) {
            String spc_name = m.group(1);

            if (isValidSearchPath(context, spc_name) == false) {
                if ((notification_level & Notifier.MONITORING) != 0) { NotificationContext.sendCaution(context, "Search path '%(searchpath)' not configured  (type cache entry skipped)", "searchpath", spc_name); }
            } else {
                if ((notification_level & Notifier.DIAGNOSTICS) != 0) { NotificationContext.sendDiagnostics(context, "Loading Search Path Context '%(spc)'", "spc", spc_name); }
                SearchPathContext spc = SearchPathContext.getSearchPathContext(context, spc_name);

                context = Context.create((Context) context);
                TypeContext tc = TypeContext.create((Context)context);
                tc.setSearchPathContext(context, spc_name);

                String[] cache_entrys = m.group(2).split(",");
                for (String cache_entry : cache_entrys) {
                    String[] cenc = cache_entry.split("\\+", 2);
                    String typekey = Encoding.recode(context, cenc[0], Encoding.URI, Encoding.UTF8);
                    String typeid  = Encoding.recode(context, cenc[1], Encoding.URI, Encoding.UTF8);

                    if (    typeid.matches("[0-9A-Za-z_<>≤≥≦≧=,.:$ \n\r\t\f-]*")
                         && typeid.equals("<INVALID>") == false) {
                        try {
                            if (doGet (context, typeid, false, true, typekey, spc) == null) {
                                if ((notification_level & Notifier.OBSERVATION) != 0) { NotificationContext.sendNotice(context, "Could not load '%(class)' ('%(spc)')", "class", typeid, "spc", spc_name); }
                            }
                        } catch (Throwable t) {
                            if ((notification_level & Notifier.OBSERVATION) != 0) { NotificationContext.sendNotice(context, "Could not load: '%(reason)' ('%(spc)')", "reason", t, "spc", spc_name); }
                        }
                        if ((notification_level & Notifier.DIAGNOSTICS) != 0) { NotificationContext.sendDiagnostics(context, "Loaded '%(key)' --> '%(class)'", "key", typekey, "class", typeid); }
                    } else {
                        if ((notification_level & Notifier.DIAGNOSTICS) != 0) { NotificationContext.sendDiagnostics(context, "SKIPPED loading '%(key)' --> '%(class)'", "key", typekey, "class", typeid); }
                        System.err.println("SKIPPED: " + typeid);
                    }
                }
            }
        }

        if (stop_watch != null) {
            stop_watch.stop(context, "load cache end");
        }
    }

    static public Class getJavaClass(CallContext context, Type type) {
        Class java_type = (   type instanceof JavaType
                            ? ((JavaType) type).getJavaClass(context)
                            : (    type instanceof TypeParametrised
                                && ((TypeParametrised) type).getBaseType(context) instanceof JavaType
                              )
                            ? ((JavaType)(((TypeParametrised) type).getBaseType(context))).getJavaClass(context)
                            : Object.class
                          );
        return java_type;
    }
}
