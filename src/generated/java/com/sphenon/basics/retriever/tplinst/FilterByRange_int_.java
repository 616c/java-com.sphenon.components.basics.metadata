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

public class FilterByRange_int_ extends GenericFilterBase<Integer> implements Filter_int_, FilterByRange {

    protected Integer minimum;
    protected Integer maximum;
    protected boolean include_minimum;
    protected boolean include_maximum;

    static protected Type target_type;

    public FilterByRange_int_ (CallContext context) {
        super(context, target_type == null ? (target_type = TypeManager.get(context, Integer.class)) : target_type);
        this.include_minimum = true;
        this.include_maximum = true;
    }

    public FilterByRange_int_ (CallContext context, Integer minimum, Integer maximum) {
        this(context);
        this.setMinimum(context, minimum);
        this.setMaximum(context, maximum);
    }

    public FilterByRange_int_ (CallContext context, Integer minimum, Integer maximum, boolean include_minimum, boolean include_maximum) {
        this(context);
        this.setMinimum(context, minimum);
        this.setMaximum(context, maximum);
        this.setIncludeMinimum(context, include_minimum);
        this.setIncludeMaximum(context, include_maximum);
    }

    public Integer getMinimum (CallContext context) {
        return this.minimum;
    }

    public Integer getMaximum (CallContext context) {
        return this.maximum;
    }

    public void setMinimum (CallContext context, Integer minimum) {
        this.minimum = minimum;
        this.setFilterEnabled(context, (this.minimum == null && this.maximum == null ? false : true));
    }

    public void setMaximum (CallContext context, Integer maximum) {
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

    protected Integer toType(CallContext context, String value) {
        return (value.matches("[0-9]+") ? Integer.parseInt(value) : 0);
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

    public boolean matches (CallContext context, int object) {
        return (    (    this.minimum == null
                      || (  this.include_minimum
                            ? (this.minimum).intValue() <= object
                            : (this.minimum).intValue() < object
                         )
                    )
                 && (    this.maximum == null
                      || (  this.include_maximum
                            ? (this.maximum).intValue() >= object
                            : (this.maximum).intValue() > object
                         )
                    )
               );
    }

    public boolean matches (CallContext context, Integer object) {
        if (object != null) {
            return matches(context, (int) object);
        }
        return (    this.minimum == null
                 && this.maximum == null
               );
    }

    static public FilterByRange_int_ newInstance(CallContext context) {
        return (FilterByRange_int_) Factory_Filter_int_.construct(context);
    }
}
