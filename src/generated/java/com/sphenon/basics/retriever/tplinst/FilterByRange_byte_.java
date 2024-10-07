// instantiated with javainst.pl from /workspace/sphenon/projects/components/basics/retriever/v0001/origin/source/java/com/sphenon/basics/retriever/templates/FilterByRange.javatpl
// please do not modify this file directly
package com.sphenon.basics.retriever.tplinst;

import com.sphenon.basics.retriever.*;
import java.util.Date;

import com.sphenon.basics.context.*;
import com.sphenon.basics.exception.*;
import com.sphenon.basics.retriever.*;
import com.sphenon.basics.retriever.classes.*;
import com.sphenon.basics.metadata.*;

public class FilterByRange_byte_ extends GenericFilterBase<Byte> implements Filter_byte_, FilterByRange {

    protected Byte minimum;
    protected Byte maximum;
    protected boolean include_minimum;
    protected boolean include_maximum;

    static protected Type target_type;

    public FilterByRange_byte_ (CallContext context) {
        super(context, target_type == null ? (target_type = TypeManager.get(context, Byte.class)) : target_type);
        this.include_minimum = true;
        this.include_maximum = true;
    }

    public FilterByRange_byte_ (CallContext context, Byte minimum, Byte maximum) {
        this(context);
        this.setMinimum(context, minimum);
        this.setMaximum(context, maximum);
    }

    public FilterByRange_byte_ (CallContext context, Byte minimum, Byte maximum, boolean include_minimum, boolean include_maximum) {
        this(context);
        this.setMinimum(context, minimum);
        this.setMaximum(context, maximum);
        this.setIncludeMinimum(context, include_minimum);
        this.setIncludeMaximum(context, include_maximum);
    }

    public Byte getMinimum (CallContext context) {
        return this.minimum;
    }

    public Byte getMaximum (CallContext context) {
        return this.maximum;
    }

    public void setMinimum (CallContext context, Byte minimum) {
        this.minimum = minimum;
        this.setFilterEnabled(context, (this.minimum == null && this.maximum == null ? false : true));
    }

    public void setMaximum (CallContext context, Byte maximum) {
        this.setFilterEnabled(context, true);
        this.maximum = maximum;
        this.setFilterEnabled(context, (this.minimum == null && this.maximum == null ? false : true));
    }

    public void setMinimum (CallContext context, String minimum) {
        setMinimum(context, toType(context, minimum));
    }

    public void setMaximum (CallContext context, String maximum) {
        setMaximum(context, toType(context, maximum));
    }

    protected Byte toType(CallContext context, String value) {
        return (value.matches("^.$") ? ((byte) (value.charAt(0))) : value.matches("[+-]?(?:(?:(?:0[xX])|#)[0-9A-Fa-f]+)|(?:[0-9]+)") ? Byte.decode(value) : 0);
    }

    public boolean getIncludeMinimum (CallContext context) {
        return this.include_minimum;
    }

    public void setIncludeMinimum (CallContext context, boolean include_minimum) {
        this.include_minimum = include_minimum;
    }

    public boolean getIncludeMaximum (CallContext context) {
        return this.include_maximum;
    }

    public void setIncludeMaximum (CallContext context, boolean include_maximum) {
        this.include_maximum = include_maximum;
    }

    public boolean matches (CallContext context, byte object) {
        return (    (    this.minimum == null
                      || (  this.include_minimum
                            ? (this.minimum).byteValue() <= object
                            : (this.minimum).byteValue() < object
                         )
                    )
                 && (    this.maximum == null
                      || (  this.include_maximum
                            ? (this.maximum).byteValue() >= object
                            : (this.maximum).byteValue() > object
                         )
                    )
               );
    }

    public boolean matches (CallContext context, Byte object) {
        if (object != null) {
            return matches(context, (byte) object);
        }
        return (    this.minimum == null
                 && this.maximum == null
               );
    }

    static public FilterByRange_byte_ newInstance(CallContext context) {
        return (FilterByRange_byte_) Factory_Filter_byte_.construct(context);
    }
}
