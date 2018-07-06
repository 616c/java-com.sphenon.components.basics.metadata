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
import com.sphenon.basics.configuration.*;
import com.sphenon.basics.metadata.tplinst.*;

import java.io.*;

public class TypeImpl_MediaObject implements Type {

    private String name;
    private String media_type;
    private Vector_Type_long_ supertypes;

    static private String default_media_type_mime = null;
    static private String default_media_type_name = null;
    static private String getDefaultMediaTypeMIME (CallContext context) {
        if (default_media_type_mime == null) {
            default_media_type_mime = Configuration.get(context, "com.sphenon.basics.metadata.MediaTypes.DEFAULT.MIME", null, (String) null);
            if (default_media_type_mime == null) {
                default_media_type_mime = "application/octet-stream";
            }
        }
        return default_media_type_mime;
    }

    static private String getDefaultMediaTypeName (CallContext context) {
        if (default_media_type_name == null) {
            default_media_type_name = Configuration.get(context, "com.sphenon.basics.metadata.MediaTypes.DEFAULT.Name", null, (String) null);
            if (default_media_type_name == null) {
                default_media_type_name = "Object";
            }
        }
        return default_media_type_name;
    }

    public TypeImpl_MediaObject (CallContext context, boolean is_file) {
        if (is_file) {
            this.name = "Media::" + getDefaultMediaTypeName(context);
            this.media_type = getDefaultMediaTypeMIME(context);
        } else {
            this.name = "Media::Directory";
            this.media_type = "directory";
        }
        this.supertypes = null;
    }

    public TypeImpl_MediaObject (CallContext context, File file, Type super_type) {
        if (file.isFile()) {
            String filename = file.getName();
            int pos = filename.lastIndexOf('.');
            this.name = null;
            this.media_type = null;
            if (pos != -1) {
                String extension = filename.substring(pos+1);
                MIMEType mt = MIMEType.getMIMEType(context, extension);
                if (mt != null) {
                    this.name       = "Media::" + mt.getName(context);
                    this.media_type = mt.getMIME(context);
                }
            }
            if (this.name == null) {
                this.name = "Media::" + getDefaultMediaTypeName(context);
            }
            if (this.media_type == null) {
                this.media_type = getDefaultMediaTypeMIME(context);
            }
        } else if (file.isDirectory()) {
            this.name = "Media::Directory";
            this.media_type = "directory";
        } else {
            this.name = "Media::Unknown";
            this.media_type = "unknown";
        }

        if (super_type == null) {
            this.supertypes = null;
        } else {
            this.supertypes = Factory_Vector_Type_long_.construct(context);
            this.supertypes.append(context, super_type);
        }
    }

    public TypeImpl_MediaObject (CallContext context, String extension, Type super_type) {
        this(context, MIMEType.getMIMEType(context, extension), super_type);
    }

    public TypeImpl_MediaObject (CallContext context, MIMEType mt, Type super_type) {
        if (mt != null) {
            this.name       = "Media::" + mt.getName(context);
            this.media_type = mt.getMIME(context);
        }
        if (this.name == null) {
            this.name = "Media::" + getDefaultMediaTypeName(context);
        }
        if (this.media_type == null) {
            this.media_type = getDefaultMediaTypeMIME(context);
        }

        if (super_type == null) {
            this.supertypes = null;
        } else {
            this.supertypes = Factory_Vector_Type_long_.construct(context);
            this.supertypes.append(context, super_type);
        }
    }

    public String getId (CallContext context) {
        return this.name;
    }

    public String getName (CallContext context) {
        return this.name;
    }

    public String getMediaType (CallContext context) {
        return this.media_type;
    }

    public String toString () {
        return super.toString() + "[" + this.name + "]";
    }

    public boolean equals (Object object) {
        if (object == null) return false;
        if (! (object instanceof TypeImpl_MediaObject)) return false;
        if (! ((TypeImpl_MediaObject) object).getName(null).equals(this.name)) return false;
        return true;
    }

    public int hashCode () {
        return this.name.hashCode();
    }

    public boolean equals (CallContext context, Object object) {
        return this.equals(object);
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
