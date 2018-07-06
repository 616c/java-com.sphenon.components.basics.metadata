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
import com.sphenon.basics.notification.*;
import com.sphenon.basics.customary.*;
import com.sphenon.basics.configuration.*;
// import com.sphenon.basics.metadata.*;
import com.sphenon.basics.variatives.*;
// import com.sphenon.engines.factorysite.*;
import com.sphenon.basics.metadata.returncodes.*;


public class MetaDataPackageInitialiser {

    static protected boolean initialised = false;

//     static {
//         initialise(RootContext.getRootContext());
//     }

    static public synchronized void initialise (CallContext context) {
        
        if (initialised == false) {
            initialised = true;

            // wem nuetzt das?
            // TypeManager.get(context, Object.class);
            // TypeManager.getNamed(context, "--------");

            // com.sphenon.basics.metadata.factories.MetaDataFactorySitePreloader.initialise(context);

            Configuration.loadDefaultProperties(context, com.sphenon.basics.metadata.MetaDataPackageInitialiser.class);

            TypeManager.getMediaTypeRoot (context);
            TypeManager.getMediaTypeDirectory (context);

            loadMediaTypes(context, getConfiguration(context));

            defineTypes(context, getConfiguration(context).get(context, "TypeDefinition", (String) null));

            ETMImpl.setup(context); // binding to ExpressionParser.jj

            if (getConfiguration(context).get(context, "SaveCacheOnExit", false)) {
                TypeManager.saveCacheOnExit(context);
            }
        }
    }

    static protected boolean initialised_cache_rebuild = false;

    static public synchronized void initialise (CallContext context, String phase) {
        
        if (initialised == false) {
            // in that case, no initialisation in the primary phase took place
            return;
        }

        if (initialised_cache_rebuild == false && phase.equals("TypeCacheRebuild")) {
            initialised_cache_rebuild = true;

            if (getConfiguration(context).get(context, "LoadCache", false)) {
                TypeManager.loadCache(context);
            }
        }
    }

    static protected Configuration config;
    static public Configuration getConfiguration (CallContext context) {
        if (config == null) {
            config = Configuration.create(RootContext.getInitialisationContext(), "com.sphenon.basics.metadata");
        }
        return config;
    }
    
    static public void defineTypes (CallContext context, Configuration config) {
        defineTypes(context, config.get(context, "TypeDefinition", (String) null));
    }

    static public void defineTypes (CallContext context, String named_types) {
        if (named_types == null || named_types.length() == 0) { return; }
        int last_pos = -1;
        int pos = -1;
        while ((pos = named_types.indexOf(",", last_pos + 1)) != -1) {
            defineType(context, named_types.substring(last_pos + 1, pos));
            last_pos = pos;
        }
        defineType(context, named_types.substring(last_pos + 1));
    }

    static protected void defineType(CallContext context, String named_type) {
        if (named_type == null || named_type.length() == 0) { return; }
        int pos = named_type.indexOf("==>");
        if (pos == -1) {
            TypeManager.defineType(context, named_type, null);
        } else {
            try {
                TypeManager.defineType(context, named_type.substring(0,pos), TypeManager.get(context, named_type.substring(pos+3)));
            } catch (NoSuchClass nsc) {
                CustomaryContext.create(Context.create(context)).throwConfigurationError(context, "Named type initialisation failed, in entry '%(entry)', base class does not exist", "entry", named_type);
                throw (ExceptionConfigurationError) null; // compiler insists
            }
        }
    }

    static public void loadMediaTypes (CallContext context, Configuration configuration) {
        String extension;
        String property;
        int entry_number = 0;
        while ((extension = configuration.get(context, (property = "MediaTypes." + ++entry_number) + ".Extension", (String) null)) != null) {
            processEntry(context, configuration, property, extension);
        }
    }

    static public void processEntry(CallContext context, Configuration configuration, String property_prefix, String extension) {
        CustomaryContext cc = CustomaryContext.create((Context)context);

        String property;

        property = property_prefix + ".Name";
        String name = configuration.get(context, property, (String) null);
        if (name == null) {
            cc.throwConfigurationError(context, "No property '%(property)' found", "property", property);
            throw (ExceptionConfigurationError) null; // compiler insists
        }

        property = property_prefix + ".MIME";
        String mime = configuration.get(context, property, (String) null);
        if (mime == null) {
            cc.throwConfigurationError(context, "No property '%(property)' found", "property", property);
            throw (ExceptionConfigurationError) null; // compiler insists
        }

        MIMEType.defineMIMEType(context, mime, extension, name);
        TypeManager.getMediaType(context, extension);
    }
}
