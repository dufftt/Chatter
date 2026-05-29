package org.apache.tvm;

import java.util.HashMap;
import java.util.Map;
import org.apache.tvm.Base;
/* loaded from: d:\Downloads\Project\Chatter\mlc-chat-extracted\classes3.dex */
public class Module extends TVMValue {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static ThreadLocal<Map<String, Function>> apiFuncs = new ThreadLocal<Map<String, Function>>() { // from class: org.apache.tvm.Module.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // java.lang.ThreadLocal
        public Map<String, Function> initialValue() {
            return new HashMap();
        }
    };
    private Function entry;
    private final String entryName;
    public final long handle;
    private boolean isReleased;

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // org.apache.tvm.TVMValue
    public long asHandle() {
        return this.handle;
    }

    @Override // org.apache.tvm.TVMValue
    public Module asModule() {
        return this;
    }

    private static Function getApi(String str) {
        Function function = apiFuncs.get().get(str);
        if (function == null) {
            Function function2 = Function.getFunction("runtime." + str);
            apiFuncs.get().put(str, function2);
            return function2;
        }
        return function;
    }

    Module(long j) {
        super(ArgTypeCode.MODULE_HANDLE);
        this.isReleased = false;
        this.entry = null;
        this.entryName = "__tvm_main__";
        this.handle = j;
    }

    protected void finalize() throws Throwable {
        release();
        super.finalize();
    }

    @Override // org.apache.tvm.TVMValue
    public void release() {
        if (this.isReleased) {
            return;
        }
        Base.checkCall(Base._LIB.tvmModFree(this.handle));
        this.isReleased = true;
    }

    public Function entryFunc() {
        if (this.entry == null) {
            this.entry = getFunction("__tvm_main__");
        }
        return this.entry;
    }

    public Function getFunction(String str, boolean z) {
        Base.RefLong refLong = new Base.RefLong();
        Base.checkCall(Base._LIB.tvmModGetFunction(this.handle, str, z ? 1 : 0, refLong));
        if (refLong.value == 0) {
            throw new IllegalArgumentException("Module has no function " + str);
        }
        return new Function(refLong.value, false);
    }

    public Function getFunction(String str) {
        return getFunction(str, false);
    }

    public void importModule(Module module) {
        Base.checkCall(Base._LIB.tvmModImport(this.handle, module.handle));
    }

    public String typeKey() {
        return getApi("ModuleGetTypeKey").pushArg(this).invoke().asString();
    }

    public static Module load(String str, String str2) {
        return getApi("ModuleLoadFromFile").pushArg(str).pushArg(str2).invoke().asModule();
    }

    public static Module load(String str) {
        return load(str, "");
    }

    public static boolean enabled(String str) {
        return getApi("RuntimeEnabled").pushArg(str).invoke().asLong() != 0;
    }
}
