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

public class FilterByRange_Date_ extends GenericFilterBase<Date> implements Filter_Date_, FilterByRange {

    protected Date minimum;
    protected Date maximum;
    protected boolean include_minimum;
    protected boolean include_maximum;

    static protected Type target_type;

    public FilterByRange_Date_ (CallContext context) {
        super(context, target_type == null ? (target_type = TypeManager.get(context, Date.class)) : target_type);
        this.include_minimum = true;
        this.include_maximum = true;
    }

    public FilterByRange_Date_ (CallContext context, Date minimum, Date maximum) {
        this(context);
        this.setMinimum(context, minimum);
        this.setMaximum(context, maximum);
    }

    public FilterByRange_Date_ (CallContext context, Date minimum, Date maximum, boolean include_minimum, boolean include_maximum) {
        this(context);
        this.setMinimum(context, minimum);
        this.setMaximum(context, maximum);
        this.setIncludeMinimum(context, include_minimum);
        this.setIncludeMaximum(context, include_maximum);
    }

    public Date getMinimum (CallContext context) {
        return this.minimum;
    }

    public Date getMaximum (CallContext context) {
        return this.maximum;
    }

    public void setMinimum (CallContext context, Date minimum) {
        this.minimum = minimum;
        this.setFilterEnabled(context, (this.minimum == null && this.maximum == null ? false : true));
    }

    public void setMaximum (CallContext context, Date maximum) {
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

    protected Date toType(CallContext context, String value) {
        return ((java.util.Date) com.sphenon.basics.format.Formatter.parse(context, "date:", value));
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

    public boolean matches (CallContext context, Date object) {
        return (    (    this.minimum == null
                      || (  this.include_minimum
                            ? ( ! this.minimum.after(object))
                            : (this.minimum.before(object))
                         )
                    )
                 && (    this.maximum == null
                      || (  this.include_maximum
                            ? ( ! this.maximum.before(object))
                            : (this.maximum.after(object))
                         )
                    )
               );
    }

    static public FilterByRange_Date_ newInstance(CallContext context) {
        return (FilterByRange_Date_) Factory_Filter_Date_.construct(context);
    }
}
