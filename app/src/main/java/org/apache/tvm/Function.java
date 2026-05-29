package org.apache.tvm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.tvm.Base;
/* loaded from: d:\Downloads\Project\Chatter\mlc-chat-extracted\classes3.dex */
public class Function extends TVMValue {
    final long handle;
    private boolean isReleased;
    public final boolean isResident;

    /* loaded from: d:\Downloads\Project\Chatter\mlc-chat-extracted\classes3.dex */
    public interface Callback {
        Object invoke(TVMValue... tVMValueArr);
    }

    @Override // org.apache.tvm.TVMValue
    public Function asFunction() {
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // org.apache.tvm.TVMValue
    public long asHandle() {
        return this.handle;
    }

    public static Function getFunction(String str) {
        for (String str2 : listGlobalFuncNames()) {
            if (str2.equals(str)) {
                return getGlobalFunc(str2, true, false);
            }
        }
        return null;
    }

    private static List<String> listGlobalFuncNames() {
        ArrayList arrayList = new ArrayList();
        Base.checkCall(Base._LIB.tvmFuncListGlobalNames(arrayList));
        return Collections.unmodifiableList(arrayList);
    }

    private static Function getGlobalFunc(String str, boolean z, boolean z2) {
        Base.RefLong refLong = new Base.RefLong();
        Base.checkCall(Base._LIB.tvmFuncGetGlobal(str, refLong));
        if (refLong.value != 0) {
            return new Function(refLong.value, z);
        }
        if (z2) {
            return null;
        }
        throw new IllegalArgumentException("Cannot find global function " + str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Function(long j, boolean z) {
        super(ArgTypeCode.FUNC_HANDLE);
        this.isReleased = false;
        this.handle = j;
        this.isResident = z;
    }

    Function(long j) {
        this(j, false);
    }

    protected void finalize() throws Throwable {
        release();
        super.finalize();
    }

    @Override // org.apache.tvm.TVMValue
    public void release() {
        if (this.isReleased || this.isResident) {
            return;
        }
        Base.checkCall(Base._LIB.tvmFuncFree(this.handle));
        this.isReleased = true;
    }

    public TVMValue invoke() {
        Base.RefTVMValue refTVMValue = new Base.RefTVMValue();
        Base.checkCall(Base._LIB.tvmFuncCall(this.handle, refTVMValue));
        return refTVMValue.value;
    }

    public Function pushArg(int i) {
        Base._LIB.tvmFuncPushArgLong(i);
        return this;
    }

    public Function pushArg(long j) {
        Base._LIB.tvmFuncPushArgLong(j);
        return this;
    }

    public Function pushArg(float f) {
        Base._LIB.tvmFuncPushArgDouble(f);
        return this;
    }

    public Function pushArg(double d) {
        Base._LIB.tvmFuncPushArgDouble(d);
        return this;
    }

    public Function pushArg(String str) {
        Base._LIB.tvmFuncPushArgString(str);
        return this;
    }

    public Function pushArg(NDArrayBase nDArrayBase) {
        Base._LIB.tvmFuncPushArgHandle(nDArrayBase.handle, (nDArrayBase.isView ? ArgTypeCode.ARRAY_HANDLE : ArgTypeCode.NDARRAY_CONTAINER).id);
        return this;
    }

    public Function pushArg(Module module) {
        Base._LIB.tvmFuncPushArgHandle(module.handle, ArgTypeCode.MODULE_HANDLE.id);
        return this;
    }

    public Function pushArg(Function function) {
        Base._LIB.tvmFuncPushArgHandle(function.handle, ArgTypeCode.FUNC_HANDLE.id);
        return this;
    }

    public Function pushArg(byte[] bArr) {
        Base._LIB.tvmFuncPushArgBytes(bArr);
        return this;
    }

    public TVMValue call(Object... objArr) {
        for (Object obj : objArr) {
            pushArgToStack(obj);
        }
        return invoke();
    }

    private static void pushArgToStack(Object obj) {
        if (obj instanceof Integer) {
            Base._LIB.tvmFuncPushArgLong(((Integer) obj).intValue());
        } else if (obj instanceof Long) {
            Base._LIB.tvmFuncPushArgLong(((Long) obj).longValue());
        } else if (obj instanceof Float) {
            Base._LIB.tvmFuncPushArgDouble(((Float) obj).floatValue());
        } else if (obj instanceof Double) {
            Base._LIB.tvmFuncPushArgDouble(((Double) obj).doubleValue());
        } else if (obj instanceof String) {
            Base._LIB.tvmFuncPushArgString((String) obj);
        } else if (obj instanceof byte[]) {
            Base._LIB.tvmFuncPushArgBytes((byte[]) obj);
        } else if (obj instanceof NDArrayBase) {
            NDArrayBase nDArrayBase = (NDArrayBase) obj;
            Base._LIB.tvmFuncPushArgHandle(nDArrayBase.handle, (nDArrayBase.isView ? ArgTypeCode.ARRAY_HANDLE : ArgTypeCode.NDARRAY_CONTAINER).id);
        } else if (obj instanceof Module) {
            Base._LIB.tvmFuncPushArgHandle(((Module) obj).handle, ArgTypeCode.MODULE_HANDLE.id);
        } else if (obj instanceof Function) {
            Base._LIB.tvmFuncPushArgHandle(((Function) obj).handle, ArgTypeCode.FUNC_HANDLE.id);
        } else if (obj instanceof TVMValue) {
            TVMValue tVMValue = (TVMValue) obj;
            switch (AnonymousClass1.$SwitchMap$org$apache$tvm$ArgTypeCode[tVMValue.typeCode.ordinal()]) {
                case 1:
                case 2:
                    Base._LIB.tvmFuncPushArgLong(tVMValue.asLong());
                    return;
                case 3:
                    Base._LIB.tvmFuncPushArgDouble(tVMValue.asDouble());
                    return;
                case 4:
                    Base._LIB.tvmFuncPushArgString(tVMValue.asString());
                    return;
                case 5:
                    Base._LIB.tvmFuncPushArgBytes(tVMValue.asBytes());
                    return;
                case 6:
                case 7:
                case 8:
                case 9:
                    Base._LIB.tvmFuncPushArgHandle(tVMValue.asHandle(), tVMValue.typeCode.id);
                    return;
                default:
                    throw new IllegalArgumentException("Invalid argument: " + obj);
            }
        } else {
            throw new IllegalArgumentException("Invalid argument: " + obj);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: org.apache.tvm.Function$1  reason: invalid class name */
    /* loaded from: d:\Downloads\Project\Chatter\mlc-chat-extracted\classes3.dex */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$org$apache$tvm$ArgTypeCode;

        static {
            int[] iArr = new int[ArgTypeCode.values().length];
            $SwitchMap$org$apache$tvm$ArgTypeCode = iArr;
            try {
                iArr[ArgTypeCode.UINT.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$org$apache$tvm$ArgTypeCode[ArgTypeCode.INT.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$org$apache$tvm$ArgTypeCode[ArgTypeCode.FLOAT.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$org$apache$tvm$ArgTypeCode[ArgTypeCode.STR.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$org$apache$tvm$ArgTypeCode[ArgTypeCode.BYTES.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$org$apache$tvm$ArgTypeCode[ArgTypeCode.HANDLE.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$org$apache$tvm$ArgTypeCode[ArgTypeCode.ARRAY_HANDLE.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$org$apache$tvm$ArgTypeCode[ArgTypeCode.MODULE_HANDLE.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$org$apache$tvm$ArgTypeCode[ArgTypeCode.FUNC_HANDLE.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
        }
    }

    public static void register(String str, Callback callback, boolean z) {
        Base.RefLong refLong = new Base.RefLong();
        Base.checkCall(Base._LIB.tvmFuncCreateFromCFunc(callback, refLong));
        Base.checkCall(Base._LIB.tvmFuncRegisterGlobal(str, refLong.value, z ? 1 : 0));
    }

    public static void register(String str, Callback callback) {
        register(str, callback, false);
    }

    public static Function convertFunc(Callback callback) {
        Base.RefLong refLong = new Base.RefLong();
        Base.checkCall(Base._LIB.tvmFuncCreateFromCFunc(callback, refLong));
        return new Function(refLong.value);
    }

    private static Object invokeRegisteredCbFunc(Callback callback, TVMValue[] tVMValueArr) {
        if (callback == null) {
            System.err.println("[ERROR] Failed to get registered function");
            return null;
        }
        return callback.invoke(tVMValueArr);
    }
}
