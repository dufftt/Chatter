package org.apache.tvm;

import java.util.List;
import org.apache.tvm.Base;
import org.apache.tvm.Function;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: d:\Downloads\Project\Chatter\mlc-chat-extracted\classes3.dex */
public class LibInfo {
    /* JADX INFO: Access modifiers changed from: package-private */
    public native int nativeLibInit(String str);

    /* JADX INFO: Access modifiers changed from: package-private */
    public native int shutdown();

    /* JADX INFO: Access modifiers changed from: package-private */
    public native int tvmArrayAlloc(long[] jArr, int i, int i2, int i3, int i4, int i5, Base.RefLong refLong);

    /* JADX INFO: Access modifiers changed from: package-private */
    public native int tvmArrayCopyFromJArray(byte[] bArr, long j, long j2);

    /* JADX INFO: Access modifiers changed from: package-private */
    public native int tvmArrayCopyFromTo(long j, long j2);

    /* JADX INFO: Access modifiers changed from: package-private */
    public native int tvmArrayCopyToJArray(long j, byte[] bArr);

    /* JADX INFO: Access modifiers changed from: package-private */
    public native int tvmArrayFree(long j);

    /* JADX INFO: Access modifiers changed from: package-private */
    public native int tvmArrayGetShape(long j, List<Long> list);

    /* JADX INFO: Access modifiers changed from: package-private */
    public native int tvmFuncCall(long j, Base.RefTVMValue refTVMValue);

    /* JADX INFO: Access modifiers changed from: package-private */
    public native int tvmFuncCreateFromCFunc(Function.Callback callback, Base.RefLong refLong);

    /* JADX INFO: Access modifiers changed from: package-private */
    public native int tvmFuncFree(long j);

    /* JADX INFO: Access modifiers changed from: package-private */
    public native int tvmFuncGetGlobal(String str, Base.RefLong refLong);

    /* JADX INFO: Access modifiers changed from: package-private */
    public native int tvmFuncListGlobalNames(List<String> list);

    /* JADX INFO: Access modifiers changed from: package-private */
    public native void tvmFuncPushArgBytes(byte[] bArr);

    /* JADX INFO: Access modifiers changed from: package-private */
    public native void tvmFuncPushArgDouble(double d);

    /* JADX INFO: Access modifiers changed from: package-private */
    public native void tvmFuncPushArgHandle(long j, int i);

    /* JADX INFO: Access modifiers changed from: package-private */
    public native void tvmFuncPushArgLong(long j);

    /* JADX INFO: Access modifiers changed from: package-private */
    public native void tvmFuncPushArgString(String str);

    /* JADX INFO: Access modifiers changed from: package-private */
    public native int tvmFuncRegisterGlobal(String str, long j, int i);

    /* JADX INFO: Access modifiers changed from: package-private */
    public native String tvmGetLastError();

    /* JADX INFO: Access modifiers changed from: package-private */
    public native int tvmModFree(long j);

    /* JADX INFO: Access modifiers changed from: package-private */
    public native int tvmModGetFunction(long j, String str, int i, Base.RefLong refLong);

    /* JADX INFO: Access modifiers changed from: package-private */
    public native int tvmModImport(long j, long j2);

    /* JADX INFO: Access modifiers changed from: package-private */
    public native int tvmSynchronize(int i, int i2);
}
