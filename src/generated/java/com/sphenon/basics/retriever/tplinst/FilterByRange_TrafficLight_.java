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

public class FilterByRange_TrafficLight_ extends GenericFilterBase<TrafficLight> implements Filter_TrafficLight_, FilterByRange {

    protected TrafficLight minimum;
    protected TrafficLight maximum;
    protected boolean include_minimum;
    protected boolean include_maximum;

    static protected Type target_type;

    public FilterByRange_TrafficLight_ (CallContext context) {
        super(context, target_type == null ? (target_type = TypeManager.get(context, TrafficLight.class)) : target_type);
        this.include_minimum = true;
        this.include_maximum = true;
    }

    public FilterByRange_TrafficLight_ (CallContext context, TrafficLight minimum, TrafficLight maximum) {
        this(context);
        this.setMinimum(context, minimum);
        this.setMaximum(context, maximum);
    }

    public FilterByRange_TrafficLight_ (CallContext context, TrafficLight minimum, TrafficLight maximum, boolean include_minimum, boolean include_maximum) {
        this(context);
        this.setMinimum(context, minimum);
        this.setMaximum(context, maximum);
        this.setIncludeMinimum(context, include_minimum);
        this.setIncludeMaximum(context, include_maximum);
    }

    public TrafficLight getMinimum (CallContext context) {
        return this.minimum;
    }

    public TrafficLight getMaximum (CallContext context) {
        return this.maximum;
    }

    public void setMinimum (CallContext context, TrafficLight minimum) {
        this.minimum = minimum;
        this.setFilterEnabled(context, (this.minimum == null && this.maximum == null ? false : true));
    }

    public void setMaximum (CallContext context, TrafficLight maximum) {
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

    protected TrafficLight toType(CallContext context, String value) {
        return (new com.sphenon.basics.retriever.classes.Class_TrafficLight(context, value.matches("[0-6]") ? Short.parseShort(value) : value.matches("(?i:OFFLINE)") ? ((short) 1) : value.matches("(?i:GREEN)") ? ((short) 2) : value.matches("(?i:YELLOW)") ? ((short) 3) : value.matches("(?i:YELLOW(?:_?)RED)") ? ((short) 4) : value.matches("(?i:YELLOW(?:_?)GREEN)") ? ((short) 5) : value.matches("(?i:RED)") ? ((short) 6) : ((short) -1)));
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

    public boolean matches (CallContext context, TrafficLight object) {
        return (    (    this.minimum == null
                      || (  this.include_minimum
                            ? (this.minimum.getValue(context) <= object.getValue(context))
                            : (this.minimum.getValue(context) < object.getValue(context))
                         )
                    )
                 && (    this.maximum == null
                      || (  this.include_maximum
                            ? (this.maximum.getValue(context) >= object.getValue(context))
                            : (this.maximum.getValue(context) > object.getValue(context))
                         )
                    )
               );
    }

    static public FilterByRange_TrafficLight_ newInstance(CallContext context) {
        return (FilterByRange_TrafficLight_) Factory_Filter_TrafficLight_.construct(context);
    }
}
