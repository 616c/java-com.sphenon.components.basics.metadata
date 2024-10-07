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

public class FilterByValue_boolean_ extends GenericFilterBase<Boolean> implements Filter_boolean_, FilterByValue , Filter_Java_Primitivetype {

    static protected Type target_type;

    public FilterByValue_boolean_ (CallContext context) {
        super(context, target_type == null ? (target_type = TypeManager.get(context, Boolean.class)) : target_type);
    }

    public FilterByValue_boolean_ (CallContext context, Boolean value) {
        this(context);
        this.setValue(context, value);
    }

    public FilterByValue_boolean_ (CallContext context, Boolean... values) {
        this(context);
        this.setValues(context, values);
    }

    protected Boolean[] values;

    public Boolean getValue (CallContext context) {
        return this.values == null || this.values.length != 1 ? null : this.values[0];
    }

    public void setValue (CallContext context, Boolean value) {
        this.setValues(context, value);
    }

    public void setValue (CallContext context, String value) {
        setValue(context, toType(context, value));
    }

    protected Boolean toType(CallContext context, String value) {
        return (value.matches("(true|TRUE|✔|1)") ? true : value.matches("(false|FALSE|✘|0)") ? false : false);
    }

    public Boolean[] getValues (CallContext context) {
        return this.values;
    }

    public void setValues (CallContext context, Boolean... values) {
        this.setFilterEnabled(context, true);
        this.values = values;
    }

    public Object getValueAsObject (CallContext context) {
        return this.values != null && this.values.length == 1 ? this.values[0] : this.values;
    }

    public FilterByValue setValueAsObject(CallContext context, Object value) {
        return this.setValueAsObject(context, (Boolean) value);
    }

    public boolean matches (CallContext context, boolean object) {
        if (this.filter_enabled == false) { return true; }
        if (this.values != null) {
                for (boolean value : values) {
                    if (value == object) { return true; }
                }
        }
        return false;
    }

    public boolean matches (CallContext context, Boolean object) {
        if (object != null) {
            return matches(context, (boolean) object);
        } else {
            if (this.filter_enabled == false) { return true; }
            return false;
        }
    }

    public boolean isActive(CallContext context) {
        if (this.filter_enabled == false) { return false; }
        if (this.values != null) { return true; }
        return false;
    }

    static public FilterByValue_boolean_ newInstance(CallContext context) {
        return (FilterByValue_boolean_) Factory_Filter_boolean_.construct(context);
    }
}
