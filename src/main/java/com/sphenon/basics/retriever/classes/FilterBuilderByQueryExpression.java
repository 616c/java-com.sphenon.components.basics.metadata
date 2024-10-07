package com.sphenon.basics.retriever.classes;

/****************************************************************************
  Copyright 2001-2024 Sphenon GmbH

  Licensed under the Apache License, Version 2.0 (the "License"); you may not
  use this file except in compliance with the License. You may obtain a copy
  of the License at http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
  License for the specific language governing permissions and limitations
  under the License.
*****************************************************************************/

import com.sphenon.basics.context.*;
import com.sphenon.basics.context.classes.*;
import com.sphenon.basics.exception.*;
import com.sphenon.basics.configuration.*;
import com.sphenon.basics.customary.*;
import com.sphenon.basics.expression.*;
import com.sphenon.basics.expression.query.*;
import com.sphenon.basics.expression.returncodes.*;
import com.sphenon.basics.retriever.*;
import com.sphenon.basics.retriever.factories.*;
import com.sphenon.basics.retriever.returncodes.*;
import com.sphenon.basics.factory.*;
import com.sphenon.basics.metadata.*;
import com.sphenon.basics.validation.returncodes.*;

import java.util.List;

/*
  [Topic] UI/Retriever Queries; Test0070_QueryExpressions.tsx, QueryExpressionParser.jj, FilterBuilderByQueryExpression.java, GenericFilterBuilder.java, GenericRetrieverFilterBuilder.java, Test_QueryExpressions.java, QueryExpression.java


  grfb = new Packages.com.sphenon.basics.retriever.classes.Test_GenericRetrieverFilterBuilder(context, "personO"); // O ist wichtig !

  Packages.com.sphenon.basics.retriever.classes.FilterBuilderByQueryExpression.build(context, "name = 1 && name#2 = 2 && name = 3", grfb)

  [x] define FilterBuilder for types other than String
      for numbers, accept <≤>≥
  [ ] cleanup and improve FilterByRange (...JPA...) to distinguish
      less and less/equal etc., by setting "IncludeBoundary" or so
  [ ] define FilterBuilder String for operators: ≠ and ≁
  [ ] cleanup and improve Filter_JPA_String_ to accept =≠~≁
  [ ] improve filters to maybe also accept QueryLanguage
      to replace the ugly "OR" behaviour in FilterBuilder String;
      like e.g. when setting some specific expression to automatically
      start QueryExpressionParser and buildup a tree && list of filters
      from there
  [ ] ...and cleanup all the ugly filter stuff:
        /workspace/sphenon/projects/components/basics/retriever/v0001/origin/source/java/com/sphenon/basics/retriever/templates/
        /workspace/sphenon/projects/components/sm/backends/jpa/v0001/origin/source/java/com/sphenon/basics/retriever/
        /workspace/sphenon/projects/components/sm/backends/jpa/v0001/origin/source/java/com/sphenon/basics/retriever/tplinst/
      jpa and in memory is inconsistent, jpa is hacked stuff,
      definition for char in FilterByRange.traits will never be used
      since it's ByRegExp etc.
  [ ] also cleanup this:
          Factory_Filter.java-template and JavaTemplateTraits_JavaTypes.java
      vs. Factory_Filter.javatpl and Factory_Filter.traits
      -> the filter types for boolean,byte,char do not match
      (but only the ones in tpl/traits are used)
  [ ] implement this stuff for class filters: ⇾¬⇾
  [ ] implement "not" variants of operators, by implementing "not"
      flag for (more or less) all filters
  [x] cleanup oomodels: Model01, property XMLogicRetriever/HasTextQuery
      create new test case from that -> Model39
  [ ] remove special handling "id.equals("text_query")" in tablecolumnheadercell.jsp
      and introduce a new VUIIndicator, something like "additional filter field..."
  [ ] QueryExpressionParser.jj - support multiple tags, just as convenient abbreviation
      approach: in Tag: allow x,y,z
                QEPath returns Pathes (List)
                all QEBinaryCondition deriveds: make clonable
                in Condition: foreach path: clone condition, process one of the pathes
  [ ] QueryExpressionParser.jj - support pathes as values

  [ ] Inheritance Handling, i.e. filtering by members of derived classes
      - Introduce syntax here: QueryExpressionParser.jj (maybe xy «type» =~ "...")
      - Add this «type» to MemberFilter
          here: QECondition.java :: public void prepare(CallContext context, QEFilters filters)
          or possibly here: QEObject.java :: public void prepare(CallContext context, QEFilters filters)
          (but maybe condition is a good place, dont know, but then what to do with conflicts
           between condition, like «t1» =~ ... && «t2» =~ ? maybe object is actually better)
      - add it here
          QEMemberFilter.java -> new member
          QEFilters.java -> handling:
             public QEMemberFilter getMemberFilter (CallContext context, String member, String tag, boolean create) throws InvalidQueryExpression {
             public QEMemberFilter addMemberFilterCondition (CallContext context, QECondition condition) throws InvalidQueryExpression {
      - then here (in this file)
          processQEFilters(CallContext context, QEFilters filters, GenericRetrieverFilterBuilder builder)
        in this line: get it and tell it which type we would like:
          GenericFilterBuilder gfb = builder.getFilterBuilder(context, member, sub_filters != null ? true : false);
        pass it here:
          FilterBuilder_RetrieverFilter_.java :: public GenericRetrieverFilterBuilder addFilter(CallContext context, String unique_filter_id)
             Filter_RetrieverFilter filter = this.complex_creator.create(context);
        and here:
          Class_RetrieverFilter_Entity.java :: public GenericFilterBuilder getFilterBuilder(CallContext context, String field, boolean complex)
        and finally in:
            public Filter_RetrieverFilter<Name> createNameFilterContainer(CallContext context) {
          here make a choice, depending on type
          based on a registration mechanism similar to "RootOfKnown..." but simpler,
          maybe for starters just a property which is by default true, so that each
          retriever maintains such a registry
        Look here for a sample: Class_Factory_Name.java
*/

public class FilterBuilderByQueryExpression {

    static public void build (CallContext context, String query_expression, GenericRetrieverFilterBuilder builder) throws InvalidQueryExpression {
        if (query_expression != null && query_expression.isEmpty() == false) {

            QETerm qe = QueryExpression.create(context, query_expression);

            build (context, qe, builder);
        }
    }

    static public void build (CallContext context, QETerm query_expression, GenericRetrieverFilterBuilder builder) throws InvalidQueryExpression {
        if (query_expression != null) {

            QEFilters filters = query_expression.getFilters(context);

            builder.clearFilter(context);

            processQEFilters(context, filters, builder);
        }
    }

    static protected void processQEFilters(CallContext context, QEFilters filters, GenericRetrieverFilterBuilder builder) throws InvalidQueryExpression {
        filters.foreachMember(context, (CallContext c, QEMemberFilter member_filter, int index, int size)
                              -> {
                                  String member    = member_filter.getMember(context);
                                  String tag       = member_filter.getTag(context);
                                  String unique_id = member_filter.getUniqueId(context);
                                  List<QECondition> conditions  = member_filter.getConditions(context);
                                  QEFilters         sub_filters = member_filter.getFilters(context);

                                  // System.err.println("MF: " + member + "#" + tag + " " + index + "/" + size + " C" + conditions.size() + " " + (sub_filters == null ? "-" : "F"));

                                  GenericFilterBuilder gfb = builder.getFilterBuilder(context, member, sub_filters != null ? true : false);

                                  if (gfb == null) {
                                      InvalidQueryExpression.createAndThrow(context, "Field '%(member)' does not exist", "member", member);
                                      throw (InvalidQueryExpression) null;
                                  }

                                  if (index == 0) {
                                      gfb.clearFilter(context);
                                  }

                                  if (conditions != null && conditions.isEmpty() == false) {
                                      for (QECondition condition : conditions) {
                                          // condition.toFilterTreeString(context, buffer, indent + "    ");
                                          if (gfb.isComplexFilter(context)) {
                                              // System.err.println("condition for complex filter");
                                              if (condition instanceof QEBinaryCondition) {
                                                  InvalidQueryExpression.createAndThrow(context, "binary condition for complex filter");
                                                  throw (InvalidQueryExpression) null;
                                              }
                                          } else {
                                              if ((condition instanceof QEBinaryCondition) == false) {
                                                  InvalidQueryExpression.createAndThrow(context, "non binary condition for simple filter");
                                                  throw (InvalidQueryExpression) null;
                                              } else {
                                                  QEBinaryCondition qebc = (QEBinaryCondition) condition;
                                                  gfb.addFilter(context, unique_id, qebc.getOperator(context), qebc.getValue(context).getValue(context));
                                              }
                                          }
                                      }
                                  }
                                  if (sub_filters != null) {
                                      if (gfb.isComplexFilter(context)) {
                                          GenericRetrieverFilterBuilder sub_builder = gfb.addFilter(context, unique_id);
                                          processQEFilters(context, sub_filters, sub_builder);
                                      } else {
                                          InvalidQueryExpression.createAndThrow(context, "filters for simple filter");
                                          throw (InvalidQueryExpression) null;
                                      }
                                  }
                              });
    }
}
