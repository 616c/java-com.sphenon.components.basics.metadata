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

public class FilterByValue_char_ extends GenericFilterBase<Character> implements Filter_char_, FilterByValue , Filter_Java_Primitivetype {

    static protected Type target_type;

    public FilterByValue_char_ (CallContext context) {
        super(context, target_type == null ? (target_type = TypeManager.get(context, Character.class)) : target_type);
    }

    public FilterByValue_char_ (CallContext context, Character value) {
        this(context);
        this.setValue(context, value);
    }

    public FilterByValue_char_ (CallContext context, Character... values) {
        this(context);
        this.setValues(context, values);
    }

    protected Character[] values;

    public Character getValue (CallContext context) {
        return this.values == null || this.values.length != 1 ? null : this.values[0];
    }

    public void setValue (CallContext context, Character value) {
        this.setValues(context, value);
    }

    public void setValue (CallContext context, String value) {
        setValue(context, toType(context, value));
    }

    protected Character toType(CallContext context, String value) {
        return null;
    }

    public Character[] getValues (CallContext context) {
        return this.values;
    }

    public void setValues (CallContext context, Character... values) {
        this.setFilterEnabled(context, true);
        this.values = values;
    }

    public Object getValueAsObject (CallContext context) {
        return this.values != null && this.values.length == 1 ? this.values[0] : this.values;
    }

    public FilterByValue setValueAsObject(CallContext context, Object value) {
        return this.setValueAsObject(context, (Character) value);
    }

    public boolean matches (CallContext context, char object) {
        if (this.filter_enabled == false) { return true; }
        if (this.values != null) {
                for (char value : values) {
                    if (value == object) { return true; }
                }
        }
        return false;
    }

    public boolean matches (CallContext context, Character object) {
        if (object != null) {
            return matches(context, (char) object);
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
}
