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
import com.sphenon.basics.exception.*;
import com.sphenon.basics.retriever.*;
import com.sphenon.basics.metadata.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class Filter_RetrieverFilter_List<TargetType> implements Filter_RetrieverFilter<TargetType> {

    public Filter_RetrieverFilter_List(CallContext context, Type target_type) {
        this.target_type = target_type;
        this.filter_list = new ArrayList<Filter_RetrieverFilter>();
        this.filter_map = new HashMap<String,Filter_RetrieverFilter>();
    }

    protected Type target_type;

    public Type getTargetType (CallContext context) {
        return this.target_type;
    }

    public String getUniqueFilterId (CallContext context) {
        return null;
    }

    public void setUniqueFilterId (CallContext context, String unique_filter_id) {
    }

    protected List<Filter_RetrieverFilter> filter_list;
    protected Map<String,Filter_RetrieverFilter> filter_map;

    public List<Filter_RetrieverFilter> getFilters (CallContext context) {
        return this.filter_list;
    }

    public GenericFilter getFilter (CallContext context, String id) {
        return this.filter_map.get(id);
    }

    public void add (CallContext context, String id, Filter_RetrieverFilter filter) {
        this.filter_list.add(filter);
        this.filter_map.put(id, filter);
    }

    public boolean getFilterEnabled (CallContext context) {
        if (filter_list.size() == 0) { return false; }
        for (Filter_RetrieverFilter<TargetType> filter : filter_list) {
            if (filter.getFilterEnabled(context)) { return true; }
        }
        return false;
    }

    public boolean matches (CallContext context, TargetType object) {
        for (Filter_RetrieverFilter<TargetType> filter : filter_list) {
            if (filter.matches(context, object) == false) { return false; }
        }
        return true;
    }

    // these three make sense, they are called in RetrieverFilters on their members

    public void resetSortOrderState(CallContext context, boolean invoked_from_topmost_container) {
        for (GenericFilter<TargetType> filter : filter_list) {
            ((Filter_RetrieverFilter<TargetType>) filter).resetSortOrderState(context, invoked_from_topmost_container);
        }
    }

    public void resetFilterToDefaultValues(CallContext context) {
        for (GenericFilter<TargetType> filter : filter_list) {
            ((Filter_RetrieverFilter<TargetType>) filter).resetFilterToDefaultValues(context);
        }
    }

    public void resetFilterToDefaultValues(CallContext context, com.sphenon.ad.adcore.ModificationType modification_type) {
        for (GenericFilter<TargetType> filter : filter_list) {
            ((Filter_RetrieverFilter<TargetType>) filter).resetFilterToDefaultValues(context, modification_type);
        }
    }

    // the following ones are questionable, possibly they should be factored
    // somehow (reduced interface to be used in RetrieverFilters)

    public void processFields(CallContext context, RetrieverFilterFieldProcessor rffp, boolean top_level) {
        return;
    }

    public boolean containsEnabledFilters(CallContext context) {
        return this.getFilterEnabled(context);
    }

    public String getItemClass(CallContext context) {
        return null;
    }

    public long getNextSortOrderPosition(CallContext context) {
        return -1;
    }

    public RetrieverFilter getParentFilter (CallContext context) {
        return null;
    }
}
