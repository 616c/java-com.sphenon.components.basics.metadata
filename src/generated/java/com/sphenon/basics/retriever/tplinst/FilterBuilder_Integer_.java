// instantiated with javainst.pl from /workspace/sphenon/projects/components/basics/retriever/v0001/origin/source/java/com/sphenon/basics/retriever/templates/FilterBuilder.javatpl
// please do not modify this file directly
package com.sphenon.basics.retriever.tplinst;

import com.sphenon.basics.retriever.*;
import java.util.Date;

import com.sphenon.basics.context.*;
import com.sphenon.basics.function.*;
import com.sphenon.basics.exception.*;
import com.sphenon.basics.expression.*;
import com.sphenon.basics.expression.returncodes.*;
import com.sphenon.basics.retriever.*;
import com.sphenon.basics.retriever.classes.*;
import com.sphenon.basics.metadata.*;

public class FilterBuilder_Integer_ implements GenericFilterBuilder<Integer> {

    public FilterBuilder_Integer_ (CallContext context) {
    }

    static protected Type target_type;

    public Type getTargetType (CallContext context) {
        return (target_type == null ? (target_type = TypeManager.get(context, Integer.class)) : target_type);
    }

    public boolean isComplexFilter(CallContext context) {
        return false;
    }

    public void clearFilter(CallContext context) {
        this.setter.set(context, null);
    }

    protected GenericFilter<Integer> createFilter(CallContext context, String unique_filter_id, String operator, Object value) throws InvalidQueryExpression {
        GenericFilter<Integer> filter = Factory_Filter_Integer_.construct(context);

        if ((filter instanceof FilterByRange_int_) == false || operator == null || operator.matches("[=<>≤≥]") == false) {
                                    InvalidQueryExpression.createAndThrow(context, "Cannot apply condition '%(operator)' '%(value)' to filter of type '%(type)'", "operator", operator, "value", value, "type", filter.getClass().getName());
                                } if (operator.matches("[>≥]")) {
                                    if (value instanceof Long) {
                                        ((FilterByRange_int_) filter).setMinimum(context, (int) (long) (Long) value);
                                    } else if (value instanceof String) {
                                        ((FilterByRange_int_) filter).setMinimum(context, (String) value);
                                    } else {
                                        InvalidQueryExpression.createAndThrow(context, "Cannot apply condition '%(operator)' '%(value)' (of type '%(valuetype)') to filter of type '%(type)'", "operator", operator, "value", value, "valuetype", value.getClass().getName(), "type", filter.getClass().getName());
                                    } ((FilterByRange_int_) filter).setIncludeMinimum(context, operator.equals("≥") ? true : false);
                                } if (operator.matches("[<≤]")) {
                                    if (value instanceof Long) {
                                        ((FilterByRange_int_) filter).setMaximum(context, (int) (long) (Long) value);
                                    } else if (value instanceof String) {
                                        ((FilterByRange_int_) filter).setMaximum(context, (String) value);
                                    } else {
                                        InvalidQueryExpression.createAndThrow(context, "Cannot apply condition '%(operator)' '%(value)' (of type '%(valuetype)') to filter of type '%(type)'", "operator", operator, "value", value, "valuetype", value.getClass().getName(), "type", filter.getClass().getName());
                                    } ((FilterByRange_int_) filter).setIncludeMaximum(context, operator.equals("≤") ? true : false);
                                } if (operator.equals("=")) {
                                    if (value instanceof Long) {
                                        ((FilterByRange_int_) filter).setMinimum(context, (int) (long) (Long) value);
                                        ((FilterByRange_int_) filter).setMaximum(context, (int) (long) (Long) value);
                                    } else if (value instanceof String) {
                                        ((FilterByRange_int_) filter).setMinimum(context, (String) value);
                                        ((FilterByRange_int_) filter).setMaximum(context, (String) value);
                                    } else {
                                        InvalidQueryExpression.createAndThrow(context, "Cannot apply condition '%(operator)' '%(value)' (of type '%(valuetype)') to filter of type '%(type)'", "operator", operator, "value", value, "valuetype", value.getClass().getName(), "type", filter.getClass().getName());
                                    }
                                    ((FilterByRange_int_) filter).setIncludeMinimum(context, true);
                                    ((FilterByRange_int_) filter).setIncludeMaximum(context, true);
                                }


        filter.setUniqueFilterId(context, unique_filter_id);

        return filter;
    }

    public void addFilter(CallContext context, String unique_filter_id, String operator, Object value) throws InvalidQueryExpression {
        GenericFilter filter = createFilter(context, unique_filter_id, operator, value);

        GenericFilter current = this.getter.get(context);
        if (current == null) {
            this.setter.set(context, filter);
        } else if (current instanceof GenericFilterList) {
            ((GenericFilterList) current).add(context, unique_filter_id, filter);
        } else {
            GenericFilterList list = new GenericFilterList(context, this.getTargetType(context));
            list.add(context, current.getUniqueFilterId(context), current);
            list.add(context, unique_filter_id, filter);
            this.setter.set(context, list);
        }
    }

    public GenericRetrieverFilterBuilder addFilter(CallContext context, String unique_filter_id) throws InvalidQueryExpression {
        InvalidQueryExpression.createAndThrow(context, "Cannot add complex filter to simple filter member");
        throw (InvalidQueryExpression) null;
    }

    protected Getter<GenericFilter<Integer>> getter;
    protected Setter<GenericFilter<Integer>> setter;

    protected String member_name;

    public GenericFilterBuilder wireToSimpleMember(CallContext context, String member_name, Getter<GenericFilter<Integer>> getter, Setter<GenericFilter<Integer>> setter, Creator<GenericFilter<Integer>> creator) {
        this.getter = getter;
        this.setter = setter;
        this.member_name = member_name;
        return this;
    }
}
