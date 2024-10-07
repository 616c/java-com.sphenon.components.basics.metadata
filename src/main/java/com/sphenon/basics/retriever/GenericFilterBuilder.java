package com.sphenon.basics.retriever;

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
import com.sphenon.basics.function.*;
import com.sphenon.basics.retriever.returncodes.*;
import com.sphenon.basics.expression.*;
import com.sphenon.basics.expression.returncodes.*;

// [Topic] UI/Retriever Queries; Test0070_QueryExpressions.tsx, QueryExpressionParser.jj, FilterBuilderByQueryExpression.java, GenericFilterBuilder.java, GenericRetrieverFilterBuilder.java, Test_QueryExpressions.java, QueryExpression.java

public interface GenericFilterBuilder<TargetType> {
    public boolean isComplexFilter(CallContext context);

    public void clearFilter(CallContext context);

    public void addFilter(CallContext context, String unique_id, String operator, Object value) throws InvalidQueryExpression;
    public GenericRetrieverFilterBuilder addFilter(CallContext context, String unique_id) throws InvalidQueryExpression;
}
