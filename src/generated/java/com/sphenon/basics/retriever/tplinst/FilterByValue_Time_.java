// instantiated with javainst.pl from /workspace/sphenon/projects/components/basics/retriever/v0001/origin/source/java/com/sphenon/basics/retriever/templates/FilterByValue.javatpl
// please do not modify this file directly
package com.sphenon.basics.retriever.tplinst;

import com.sphenon.basics.retriever.*;
import java.util.Date;

import com.sphenon.basics.context.*;
import com.sphenon.basics.exception.*;
import com.sphenon.basics.retriever.*;
import com.sphenon.basics.retriever.classes.*;
import com.sphenon.basics.metadata.*;

public class FilterByValue_Time_ extends GenericFilterBase<Time> implements Filter_Time_, FilterByValue , Filter_Java_Librarytype {

    static protected Type target_type;

    public FilterByValue_Time_ (CallContext context) {
        super(context, target_type == null ? (target_type = TypeManager.get(context, Time.class)) : target_type);
    }

    public FilterByValue_Time_ (CallContext context, Time value) {
        this(context);
        this.setValue(context, value);
    }

    public FilterByValue_Time_ (CallContext context, Time... values) {
        this(context);
        this.setValues(context, values);
    }

    protected Time[] values;

    public Time getValue (CallContext context) {
        return this.values == null || this.values.length != 1 ? null : this.values[0];
    }

    public void setValue (CallContext context, Time value) {
        this.setValues(context, value);
    }

    public void setValue (CallContext context, String value) {
        setValue(context, toType(context, value));
    }

    protected Time toType(CallContext context, String value) {
        return null;
    }

    public Time[] getValues (CallContext context) {
        return this.values;
    }

    public void setValues (CallContext context, Time... values) {
        this.setFilterEnabled(context, true);
        this.values = values;
    }

    public Object getValueAsObject (CallContext context) {
        return this.values != null && this.values.length == 1 ? this.values[0] : this.values;
    }

    public FilterByValue setValueAsObject(CallContext context, Object value) {
        return this.setValueAsObject(context, (Time) value);
    }

    public boolean matches (CallContext context, Time object) {
        if (this.filter_enabled == false) { return true; }
        if (this.values != null) {
            if (object != null) {
                for (Time value : values) {
                    if (value.equals(object)) { return true; }
                }
            }
        }
        return false;
    }

    public boolean isActive(CallContext context) {
        if (this.filter_enabled == false) { return false; }
        if (this.values != null) { return true; }
        return false;
    }
}
