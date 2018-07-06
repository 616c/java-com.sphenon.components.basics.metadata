package com.sphenon.basics.metadata.test;

/****************************************************************************
  Copyright 2001-2018 Sphenon GmbH

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
import com.sphenon.basics.notification.*;
import com.sphenon.basics.many.returncodes.*;
import com.sphenon.basics.metadata.*;
import com.sphenon.basics.metadata.returncodes.*;
import com.sphenon.basics.metadata.tplinst.*;
import com.sphenon.basics.testing.*;

public class Test_Basics extends com.sphenon.basics.testing.classes.TestBase {
    static protected long notification_level;
    static public    long adjustNotificationLevel(long new_level) { long old_level = notification_level; notification_level = new_level; return old_level; }
    static public    long getNotificationLevel() { return notification_level; }
    static { notification_level = NotificationLocationContext.getLevel(RootContext.getInitialisationContext(), "com.sphenon.basics.metadata.test.Test_Basics"); };

    public Test_Basics (CallContext context) {
    }

    public String getId(CallContext context) {
        if (this.id == null) {
            this.id = "MetaDataBasics";
        }
        return this.id;
    }

    public TestResult perform (CallContext context, TestRun test_run) {

        try {

            try {
                Type a = TypeManager.get(context, "java.util.Hashtable");
                Type b = TypeManager.get(context, java.util.Hashtable.class);
                Type c = TypeManager.get(context, "java.util.Hashtable");
                Type d = TypeManager.get(context, java.util.Hashtable.class);

                if (a != b) {
                    return new TestResult_Failure(context, "type access by name and by class is not identical");
                }
                if (a != c) {
                    return new TestResult_Failure(context, "first and second access by name are not identical");
                }
                if (b != d) {
                    return new TestResult_Failure(context, "first and second access by class are not identical");
                }

                Type e = TypeManager.get(context, java.util.Vector.class);
                Type f = TypeManager.get(context, "java.util.Vector");
                Type g = TypeManager.get(context, java.util.Vector.class);
                Type h = TypeManager.get(context, "java.util.Vector");

                if (e != f) {
                    return new TestResult_Failure(context, "Type access by name and by class is not identical");
                }
                if (f != h) {
                    return new TestResult_Failure(context, "first and second access by name are not identical");
                }
                if (e != g) {
                    return new TestResult_Failure(context, "first and second access by class are not identical");
                }
            } catch (NoSuchClass ex) {
                return new TestResult_ExceptionRaised(context, ex);
            }

            {
                Type a = TypeManager.get(context, A.class);
                Type b = TypeManager.get(context, B.class);
                Type c = TypeManager.get(context, C.class);
                Type d = TypeManager.get(context, D.class);
                Type e = TypeManager.get(context, E.class);
                Type f = TypeManager.get(context, F.class);
                Type g = TypeManager.get(context, G.class);

                if (b.isA(context, a) == false) {
                    return new TestResult_Failure(context, "b.isA(a) method failed");
                }
                if (c.isA(context, a) == false) {
                    return new TestResult_Failure(context, "c.isA(a) method failed");
                }
                if (d.isA(context, b) == false) {
                    return new TestResult_Failure(context, "d.isA(b) method failed");
                }
                if (e.isA(context, b) == false) {
                    return new TestResult_Failure(context, "e.isA(b) method failed");
                }
                if (f.isA(context, c) == false) {
                    return new TestResult_Failure(context, "f.isA(c) method failed");
                }
                if (g.isA(context, c) == false) {
                    return new TestResult_Failure(context, "g.isA(c) method failed");
                }

                if (a.isA(context, b) == true) {
                    return new TestResult_Failure(context, "a.isA(b) method failed");
                }
            }

            if ((notification_level & Notifier.DIAGNOSTICS) != 0) { NotificationContext.sendTrace(context, Notifier.DIAGNOSTICS, "creating OSet..."); }

            OSet_Object_Type_ oset = new OSetImpl_Object_Type_ (context);

            if ((notification_level & Notifier.DIAGNOSTICS) != 0) { NotificationContext.sendTrace(context, Notifier.DIAGNOSTICS, "populating OSet..." ); }

            try {
                oset.add(context, new A());
                oset.add(context, new B());
                oset.add(context, new C());
                oset.add(context, new D());
                oset.add(context, new E());
                oset.add(context, new F());
                oset.add(context, new G());
            } catch (AlreadyExists ex) {
                return new TestResult_ExceptionRaised(context, ex);
            }

            if ((notification_level & Notifier.DIAGNOSTICS) != 0) { NotificationContext.sendTrace(context, Notifier.DIAGNOSTICS, "checking OSet..." ); }

            try {
                if ((notification_level & Notifier.DIAGNOSTICS) != 0) { NotificationContext.sendTrace(context, Notifier.DIAGNOSTICS, "retrieving A's..." ); }
                Iterator_Object_ as = oset.get(context, TypeManager.get(context, A.class)).getNavigator(context);
                for (Object o; (o = as.tryGetCurrent(context)) != null; as.next(context)) {
                    if ((notification_level & Notifier.DIAGNOSTICS) != 0) { NotificationContext.sendTrace(context, Notifier.DIAGNOSTICS, "   - " + o.getClass().toString()); }
                }

                if ((notification_level & Notifier.DIAGNOSTICS) != 0) { NotificationContext.sendTrace(context, Notifier.DIAGNOSTICS, "retrieving B's..." ); }
                Iterator_Object_ bs = oset.get(context, TypeManager.get(context, B.class)).getNavigator(context);
                for (Object o; (o = bs.tryGetCurrent(context)) != null; bs.next(context)) {
                    if ((notification_level & Notifier.DIAGNOSTICS) != 0) { NotificationContext.sendTrace(context, Notifier.DIAGNOSTICS, "   - " + o.getClass().toString()); }
                }

                if ((notification_level & Notifier.DIAGNOSTICS) != 0) { NotificationContext.sendTrace(context, Notifier.DIAGNOSTICS, "retrieving C's..." ); }
                Iterator_Object_ cs = oset.get(context, TypeManager.get(context, C.class)).getNavigator(context);
                for (Object o; (o = cs.tryGetCurrent(context)) != null; cs.next(context)) {
                    if ((notification_level & Notifier.DIAGNOSTICS) != 0) { NotificationContext.sendTrace(context, Notifier.DIAGNOSTICS, "   - " + o.getClass().toString()); }
                }

                if ((notification_level & Notifier.DIAGNOSTICS) != 0) { NotificationContext.sendTrace(context, Notifier.DIAGNOSTICS, "retrieving D's..." ); }
                Iterator_Object_ ds = oset.get(context, TypeManager.get(context, D.class)).getNavigator(context);
                for (Object o; (o = ds.tryGetCurrent(context)) != null; ds.next(context)) {
                    if ((notification_level & Notifier.DIAGNOSTICS) != 0) { NotificationContext.sendTrace(context, Notifier.DIAGNOSTICS, "   - " + o.getClass().toString()); }
                }

                if ((notification_level & Notifier.DIAGNOSTICS) != 0) { NotificationContext.sendTrace(context, Notifier.DIAGNOSTICS, "retrieving E's..." ); }
                Iterator_Object_ es = oset.get(context, TypeManager.get(context, E.class)).getNavigator(context);
                for (Object o; (o = es.tryGetCurrent(context)) != null; es.next(context)) {
                    if ((notification_level & Notifier.DIAGNOSTICS) != 0) { NotificationContext.sendTrace(context, Notifier.DIAGNOSTICS, "   - " + o.getClass().toString()); }
                }

                if ((notification_level & Notifier.DIAGNOSTICS) != 0) { NotificationContext.sendTrace(context, Notifier.DIAGNOSTICS, "retrieving F's..." ); }
                Iterator_Object_ fs = oset.get(context, TypeManager.get(context, F.class)).getNavigator(context);
                for (Object o; (o = fs.tryGetCurrent(context)) != null; fs.next(context)) {
                    if ((notification_level & Notifier.DIAGNOSTICS) != 0) { NotificationContext.sendTrace(context, Notifier.DIAGNOSTICS, "   - " + o.getClass().toString()); }
                }

                if ((notification_level & Notifier.DIAGNOSTICS) != 0) { NotificationContext.sendTrace(context, Notifier.DIAGNOSTICS, "retrieving G's..." ); }
                Iterator_Object_ gs = oset.get(context, TypeManager.get(context, G.class)).getNavigator(context);
                for (Object o; (o = gs.tryGetCurrent(context)) != null; gs.next(context)) {
                    if ((notification_level & Notifier.DIAGNOSTICS) != 0) { NotificationContext.sendTrace(context, Notifier.DIAGNOSTICS, "   - " + o.getClass().toString()); }
                }

            } catch (DoesNotExist ex) {
                return new TestResult_ExceptionRaised(context, ex);
            }
            if ((notification_level & Notifier.DIAGNOSTICS) != 0) { NotificationContext.sendTrace(context, Notifier.DIAGNOSTICS, "that's cool, ey?" ); }

            if ((notification_level & Notifier.DIAGNOSTICS) != 0) { NotificationContext.sendTrace(context, Notifier.DIAGNOSTICS, "creating OMap..." ); }

            OMap_Object_Type_ omap = new OMapImpl_Object_Type_ (context);

            if ((notification_level & Notifier.DIAGNOSTICS) != 0) { NotificationContext.sendTrace(context, Notifier.DIAGNOSTICS, "populating OMap..." ); }

            try {
                omap.add(context, TypeManager.get(context, B.class), new BHandler());
                omap.add(context, TypeManager.get(context, D.class), new DHandler());
                omap.add(context, TypeManager.get(context, G.class), new GHandler());

                if ((notification_level & Notifier.DIAGNOSTICS) != 0) { NotificationContext.sendTrace(context, Notifier.DIAGNOSTICS, "checking OMap..." ); }

                {
                    if ((notification_level & Notifier.DIAGNOSTICS) != 0) { NotificationContext.sendTrace(context, Notifier.DIAGNOSTICS, "checking A... (should fail)" ); }
                    Object o = omap.tryGet(context, TypeManager.get(context, A.class));
                    if (o == null) {
                        if ((notification_level & Notifier.DIAGNOSTICS) != 0) { NotificationContext.sendTrace(context, Notifier.DIAGNOSTICS, "   no handler for A" ); }
                    } else {
                        if ((notification_level & Notifier.DIAGNOSTICS) != 0) { NotificationContext.sendTrace(context, Notifier.DIAGNOSTICS, "   got a " + o.getClass().toString()); }
                    }
                }

                {
                    if ((notification_level & Notifier.DIAGNOSTICS) != 0) { NotificationContext.sendTrace(context, Notifier.DIAGNOSTICS, "checking B... (should find B)" ); }
                    Object o = omap.tryGet(context, TypeManager.get(context, B.class));
                    if (o == null) {
                        if ((notification_level & Notifier.DIAGNOSTICS) != 0) { NotificationContext.sendTrace(context, Notifier.DIAGNOSTICS, "   no handler for B" ); }
                    } else {
                        if ((notification_level & Notifier.DIAGNOSTICS) != 0) { NotificationContext.sendTrace(context, Notifier.DIAGNOSTICS, "   got a " + o.getClass().toString()); }
                    }
                }

                {
                    if ((notification_level & Notifier.DIAGNOSTICS) != 0) { NotificationContext.sendTrace(context, Notifier.DIAGNOSTICS, "checking C... (should fail)" ); }
                    Object o = omap.tryGet(context, TypeManager.get(context, C.class));
                    if (o == null) {
                        if ((notification_level & Notifier.DIAGNOSTICS) != 0) { NotificationContext.sendTrace(context, Notifier.DIAGNOSTICS, "   no handler for C" ); }
                    } else {
                        if ((notification_level & Notifier.DIAGNOSTICS) != 0) { NotificationContext.sendTrace(context, Notifier.DIAGNOSTICS, "   got a " + o.getClass().toString()); }
                    }
                }

                {
                    if ((notification_level & Notifier.DIAGNOSTICS) != 0) { NotificationContext.sendTrace(context, Notifier.DIAGNOSTICS, "checking D... (should find D)" ); }
                    Object o = omap.tryGet(context, TypeManager.get(context, D.class));
                    if (o == null) {
                        if ((notification_level & Notifier.DIAGNOSTICS) != 0) { NotificationContext.sendTrace(context, Notifier.DIAGNOSTICS, "   no handler for D" ); }
                    } else {
                        if ((notification_level & Notifier.DIAGNOSTICS) != 0) { NotificationContext.sendTrace(context, Notifier.DIAGNOSTICS, "   got a " + o.getClass().toString()); }
                    }
                }

                {
                    if ((notification_level & Notifier.DIAGNOSTICS) != 0) { NotificationContext.sendTrace(context, Notifier.DIAGNOSTICS, "checking E... (should find B)" ); }
                    Object o = omap.tryGet(context, TypeManager.get(context, E.class));
                    if (o == null) {
                        if ((notification_level & Notifier.DIAGNOSTICS) != 0) { NotificationContext.sendTrace(context, Notifier.DIAGNOSTICS, "   no handler for E" ); }
                    } else {
                        if ((notification_level & Notifier.DIAGNOSTICS) != 0) { NotificationContext.sendTrace(context, Notifier.DIAGNOSTICS, "   got a " + o.getClass().toString()); }
                    }
                }

                {
                    if ((notification_level & Notifier.DIAGNOSTICS) != 0) { NotificationContext.sendTrace(context, Notifier.DIAGNOSTICS, "checking F... (should fail)" ); }
                    Object o = omap.tryGet(context, TypeManager.get(context, F.class));
                    if (o == null) {
                        if ((notification_level & Notifier.DIAGNOSTICS) != 0) { NotificationContext.sendTrace(context, Notifier.DIAGNOSTICS, "   no handler for F" ); }
                    } else {
                        if ((notification_level & Notifier.DIAGNOSTICS) != 0) { NotificationContext.sendTrace(context, Notifier.DIAGNOSTICS, "   got a " + o.getClass().toString()); }
                    }
                }

                {
                    if ((notification_level & Notifier.DIAGNOSTICS) != 0) { NotificationContext.sendTrace(context, Notifier.DIAGNOSTICS, "checking G... (should find G)" ); }
                    Object o = omap.tryGet(context, TypeManager.get(context, G.class));
                    if (o == null) {
                        if ((notification_level & Notifier.DIAGNOSTICS) != 0) { NotificationContext.sendTrace(context, Notifier.DIAGNOSTICS, "   no handler for G" ); }
                    } else {
                        if ((notification_level & Notifier.DIAGNOSTICS) != 0) { NotificationContext.sendTrace(context, Notifier.DIAGNOSTICS, "   got a " + o.getClass().toString()); }
                    }
                }
            } catch (AlreadyExists ex) {
                return new TestResult_ExceptionRaised(context, ex);
            }


        } catch (Throwable t) {
            return new TestResult_ExceptionRaised(context, t);
        }
        
        return TestResult.OK;
    }
}
