// instantiated with javainst.pl from /workspace/sphenon/projects/components/basics/retriever/v0001/origin/source/java/com/sphenon/basics/retriever/templates/FilterByRegExp.javatpl
// please do not modify this file directly
package com.sphenon.basics.retriever.tplinst;

import com.sphenon.basics.retriever.*;
import java.util.Date;

import com.sphenon.basics.context.*;
import com.sphenon.basics.exception.*;
import com.sphenon.basics.retriever.*;
import com.sphenon.basics.retriever.classes.*;
import com.sphenon.basics.metadata.*;

import java.util.regex.*;

public class FilterByRegExp_String_ extends GenericFilterBase<String> implements FilterByExpression_String_ {

    protected String  value;
    protected String  regexp;
    protected Pattern regexp_pattern;

    static protected Type target_type;

    public FilterByRegExp_String_ (CallContext context) {
        super(context, target_type == null ? (target_type = TypeManager.get(context, String.class)) : target_type);
    }

    public FilterByRegExp_String_ (CallContext context, String regexp) throws PatternSyntaxException {
        this(context);
        this.setRegExp(context, regexp);
    }

    public String getExpression (CallContext context) {
        return this.getRegExp(context);
    }

    public void setExpression (CallContext context, String expression) {
        this.setRegExp(context, expression);
    }

    public String getRegExp (CallContext context) {
        return this.regexp;
    }

    public void setRegExp (CallContext context, String expression_or_value) throws PatternSyntaxException {
        try {
            if (expression_or_value != null) {
                if (expression_or_value.matches("^(?:~{3,4}|≈{1,2}|:).*")) {
                    throw new PatternSyntaxException("Fuzzy pattern matching not yet supported in InMemory datastore", expression_or_value, 0);
                    // but there's already: StringUtilities.distance
                } else if (expression_or_value.startsWith("~")) {
                    this.regexp = expression_or_value.substring(1);
                    try {
                        this.regexp_pattern = (this.regexp == null ? null : Pattern.compile(this.regexp));
                    } catch (PatternSyntaxException pse) {
                        this.regexp_pattern = null;
                        if (RetrieverPackageInitialiser.getConfiguration(context).get(context, "DEBUG.DisableRegExpMatcher", false) == false) {
                            throw pse;
                        }
                    }
                } else {
                    this.value = expression_or_value.isEmpty() ? null : expression_or_value.startsWith("=") ? expression_or_value.substring(1) : expression_or_value;
                }
            }
        } finally {
            setFilterEnabled(context, this.regexp_pattern == null && this.value == null ? false : true);
        }
    }

    public boolean matches (CallContext context, String object) {
        String instance = object;
        return ( this.regexp != null
                 ? (    this.regexp.length() == 0
                     || this.regexp_pattern == null
                     || (object != null && regexp_pattern.matcher(instance.toString() ).matches())
                   ) :
                 this.value != null
                 ? (object != null && this.value.equals(instance.toString() )
                   ) :
                 true);
    }

    static public FilterByRegExp_String_ newInstance(CallContext context) {
        return (FilterByRegExp_String_) Factory_Filter_String_.construct(context);
    }
}

