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

public class FilterByValue_Class_ extends GenericFilterBase<Class> implements Filter_Class_, FilterByValue  {

    static protected Type target_type;

    public FilterByValue_Class_ (CallContext context) {
        super(context, target_type == null ? (target_type = TypeManager.get(context, Class.class)) : target_type);
    }

    public FilterByValue_Class_ (CallContext context, Class value) {
        this(context);
        this.setValue(context, value);
    }

    public FilterByValue_Class_ (CallContext context, Class... values) {
        this(context);
        this.setValues(context, values);
    }

    protected Class[] values;

    public Class getValue (CallContext context) {
        return this.values == null || this.values.length != 1 ? null : this.values[0];
    }

    public void setValue (CallContext context, Class value) {
        this.setValues(context, value);
    }

    public void setValue (CallContext context, String value) {
        setValue(context, toType(context, value));
    }

    protected Class toType(CallContext context, String value) {
        return null;
    }

    public Class[] getValues (CallContext context) {
        return this.values;
    }

    public void setValues (CallContext context, Class... values) {
        this.setFilterEnabled(context, true);
        this.values = values;
    }

    public Object getValueAsObject (CallContext context) {
        return this.values != null && this.values.length == 1 ? this.values[0] : this.values;
    }

    public FilterByValue setValueAsObject(CallContext context, Object value) {
        return this.setValueAsObject(context, (Class) value);
    }

    public boolean matches (CallContext context, Class object) {
        if (this.filter_enabled == false) { return true; }
        if (this.values != null) {
            if (object != null) {
                for (Class value : values) {
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

    static public FilterByValue_Class_ newInstance(CallContext context) {
        return (FilterByValue_Class_) Factory_Filter_Class_.construct(context);
    }
}
