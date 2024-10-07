package com.sphenon.basics.retriever;

import java.util.Date;

import com.sphenon.basics.context.*;
import com.sphenon.basics.function.*;
import com.sphenon.basics.exception.*;
import com.sphenon.basics.expression.*;
import com.sphenon.basics.expression.returncodes.*;
import com.sphenon.basics.customary.*;
import com.sphenon.basics.retriever.*;
import com.sphenon.basics.retriever.classes.*;
import com.sphenon.basics.metadata.*;

public class FilterBuilder_RetrieverFilter_<TargetType> implements GenericFilterBuilder<TargetType> {

    public FilterBuilder_RetrieverFilter_ (CallContext context, Type target_type) {
        this.target_type = target_type;
    }

    public FilterBuilder_RetrieverFilter_ (CallContext context, Class target_class) {
        this.target_type = TypeManager.get(context, target_class);
    }

    static protected Type target_type;

    public Type getTargetType (CallContext context) {
        return this.target_type;
    }

    protected boolean is_wired;
    protected boolean is_complex_filter;

    public boolean isComplexFilter(CallContext context) {
        return this.is_complex_filter;
    }

    public void clearFilter(CallContext context) {
        if (this.is_complex_filter) {
            this.complex_setter.set(context, null);
        } else {
            this.simple_setter.set(context, null);
        }
    }

    public GenericRetrieverFilterBuilder addFilter(CallContext context, String unique_filter_id) throws InvalidQueryExpression {
        if (this.is_wired == false) {
            CustomaryContext.create((Context)context).throwProtocolViolation(context, "Cannot use unwired filter builder");
            throw (ExceptionProtocolViolation) null;
        }
        if (this.is_complex_filter == false) {
            InvalidQueryExpression.createAndThrow(context, "Cannot add complex filter to simple filter member");
            throw (InvalidQueryExpression) null;
        }

        Filter_RetrieverFilter filter = this.complex_creator.create(context);
        filter.setUniqueFilterId(context, unique_filter_id);

        Filter_RetrieverFilter current = this.complex_getter.get(context);
        if (current == null) {
            this.complex_setter.set(context, filter);
        } else if (current instanceof Filter_RetrieverFilter_List) {
            ((Filter_RetrieverFilter_List) current).add(context, unique_filter_id, filter);
        } else {
            Filter_RetrieverFilter_List list = new Filter_RetrieverFilter_List(context, this.getTargetType(context));
            list.add(context, current.getUniqueFilterId(context), current);
            list.add(context, unique_filter_id, filter);
            this.complex_setter.set(context, list);
        }

        return (GenericRetrieverFilterBuilder) filter;
    }

    public void addFilter(CallContext context, String unique_filter_id, String operator, Object value) throws InvalidQueryExpression {
        if (this.is_wired == false) {
            CustomaryContext.create((Context)context).throwProtocolViolation(context, "Cannot use unwired filter builder");
            throw (ExceptionProtocolViolation) null;
        }
        if (this.is_complex_filter == true) {
            InvalidQueryExpression.createAndThrow(context, "Cannot add simple filter to complex filter member");
            throw (InvalidQueryExpression) null;
        }

        GenericFilter filter = this.simple_creator.create(context);

        if ((filter instanceof FilterByValue) == false || operator == null || operator.matches("[=]") == false) {
            InvalidQueryExpression.createAndThrow(context, "Cannot apply condition '%(operator)' '%(value)' to filter of type '%(type)'", "operator", operator, "value", value, "type", filter.getClass().getName());
        }
        ((FilterByValue) filter).setValueAsObject(context, value);
        filter.setUniqueFilterId(context, unique_filter_id);

        GenericFilter current = this.simple_getter.get(context);
        if (current == null) {
            this.simple_setter.set(context, filter);
        } else if (current instanceof GenericFilterList) {
            ((GenericFilterList) current).add(context, unique_filter_id, filter);
        } else {
            GenericFilterList list = new GenericFilterList(context, this.getTargetType(context));
            list.add(context, current.getUniqueFilterId(context), current);
            list.add(context, unique_filter_id, filter);
            this.simple_setter.set(context, list);
        }
    }

    protected Getter<Filter_RetrieverFilter<TargetType>> complex_getter;
    protected Setter<Filter_RetrieverFilter<TargetType>> complex_setter;
    protected Creator<Filter_RetrieverFilter<TargetType>> complex_creator;

    protected String complex_member_name;

    public GenericFilterBuilder wireToComplexMember(CallContext context, String complex_member_name, Getter<Filter_RetrieverFilter<TargetType>> complex_getter, Setter<Filter_RetrieverFilter<TargetType>> complex_setter, Creator<Filter_RetrieverFilter<TargetType>> complex_creator) {
        if (this.is_wired == true) {
            CustomaryContext.create((Context)context).throwProtocolViolation(context, "Cannot wire same filter builder twice");
            throw (ExceptionProtocolViolation) null;
        }
        this.is_wired = true;
        this.is_complex_filter = true;
        this.complex_getter = complex_getter;
        this.complex_setter = complex_setter;
        this.complex_creator = complex_creator;
        this.complex_member_name = complex_member_name;
        return this;
    }

    protected Getter<GenericFilter<TargetType>> simple_getter;
    protected Setter<GenericFilter<TargetType>> simple_setter;
    protected Creator<GenericFilter<TargetType>> simple_creator;

    protected String simple_member_name;

    public GenericFilterBuilder wireToSimpleMember(CallContext context, String simple_member_name, Getter<GenericFilter<TargetType>> simple_getter, Setter<GenericFilter<TargetType>> simple_setter, Creator<GenericFilter<TargetType>> simple_creator) {
        if (this.is_wired == true) {
            CustomaryContext.create((Context)context).throwProtocolViolation(context, "Cannot wire same filter builder twice");
            throw (ExceptionProtocolViolation) null;
        }
        this.is_wired = true;
        this.is_complex_filter = false;
        this.simple_getter = simple_getter;
        this.simple_setter = simple_setter;
        this.simple_member_name = simple_member_name;
        return this;
    }

}
