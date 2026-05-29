package org.apache.tvm;

import java.util.HashMap;
import java.util.Map;
/* loaded from: d:\Downloads\Project\Chatter\mlc-chat-extracted\classes3.dex */
public final class API {
    private static ThreadLocal<Map<String, Function>> apiFuncs = new ThreadLocal<Map<String, Function>>() { // from class: org.apache.tvm.API.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // java.lang.ThreadLocal
        public Map<String, Function> initialValue() {
            return new HashMap();
        }
    };

    public static Function get(String str) {
        Function function = apiFuncs.get().get(str);
        if (function == null) {
            Function function2 = Function.getFunction(str);
            apiFuncs.get().put(str, function2);
            return function2;
        }
        return function;
    }

    private API() {
    }
}
