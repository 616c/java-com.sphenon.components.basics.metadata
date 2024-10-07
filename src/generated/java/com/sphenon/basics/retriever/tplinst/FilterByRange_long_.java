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

public class FilterByRange_long_ extends GenericFilterBase<Long> implements Filter_long_, FilterByRange {

    protected Long minimum;
    protected Long maximum;
    protected boolean include_minimum;
    protected boolean include_maximum;

    static protected Type target_type;

    public FilterByRange_long_ (CallContext context) {
        super(context, target_type == null ? (target_type = TypeManager.get(context, Long.class)) : target_type);
        this.include_minimum = true;
        this.include_maximum = true;
    }

    public FilterByRange_long_ (CallContext context, Long minimum, Long maximum) {
        this(context);
        this.setMinimum(context, minimum);
        this.setMaximum(context, maximum);
    }

    public FilterByRange_long_ (CallContext context, Long minimum, Long maximum, boolean include_minimum, boolean include_maximum) {
        this(context);
        this.setMinimum(context, minimum);
        this.setMaximum(context, maximum);
        this.setIncludeMinimum(context, include_minimum);
        this.setIncludeMaximum(context, include_maximum);
    }

    public Long getMinimum (CallContext context) {
        return this.minimum;
    }

    public Long getMaximum (CallContext context) {
        return this.maximum;
    }

    public void setMinimum (CallContext context, Long minimum) {
        this.minimum = minimum;
        this.setFilterEnabled(context, (this.minimum == null && this.maximum == null ? false : true));
    }

    public void setMaximum (CallContext context, Long maximum) {
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

    protected Long toType(CallContext context, String value) {
        return (value.matches("[0-9]+") ? Long.parseLong(value) : 0);
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

    public boolean matches (CallContext context, long object) {
        return (    (    this.minimum == null
                      || (  this.include_minimum
                            ? (this.minimum).longValue() <= object
                            : (this.minimum).longValue() < object
                         )
                    )
                 && (    this.maximum == null
                      || (  this.include_maximum
                            ? (this.maximum).longValue() >= object
                            : (this.maximum).longValue() > object
                         )
                    )
               );
    }

    public boolean matches (CallContext context, Long object) {
        if (object != null) {
            return matches(context, (long) object);
        }
        return (    this.minimum == null
                 && this.maximum == null
               );
    }

    static public FilterByRange_long_ newInstance(CallContext context) {
        return (FilterByRange_long_) Factory_Filter_long_.construct(context);
    }
}
