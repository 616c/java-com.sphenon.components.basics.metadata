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

public class FilterByValue_Date_ extends GenericFilterBase<Date> implements Filter_Date_, FilterByValue , Filter_Java_Librarytype {

    static protected Type target_type;

    public FilterByValue_Date_ (CallContext context) {
        super(context, target_type == null ? (target_type = TypeManager.get(context, Date.class)) : target_type);
    }

    public FilterByValue_Date_ (CallContext context, Date value) {
        this(context);
        this.setValue(context, value);
    }

    public FilterByValue_Date_ (CallContext context, Date... values) {
        this(context);
        this.setValues(context, values);
    }

    protected Date[] values;

    public Date getValue (CallContext context) {
        return this.values == null || this.values.length != 1 ? null : this.values[0];
    }

    public void setValue (CallContext context, Date value) {
        this.setValues(context, value);
    }

    public void setValue (CallContext context, String value) {
        setValue(context, toType(context, value));
    }

    protected Date toType(CallContext context, String value) {
        return null;
    }

    public Date[] getValues (CallContext context) {
        return this.values;
    }

    public void setValues (CallContext context, Date... values) {
        this.setFilterEnabled(context, true);
        this.values = values;
    }

    public Object getValueAsObject (CallContext context) {
        return this.values != null && this.values.length == 1 ? this.values[0] : this.values;
    }

    public FilterByValue setValueAsObject(CallContext context, Object value) {
        return this.setValueAsObject(context, (Date) value);
    }

    public boolean matches (CallContext context, Date object) {
        if (this.filter_enabled == false) { return true; }
        if (this.values != null) {
            if (object != null) {
                for (Date value : values) {
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
