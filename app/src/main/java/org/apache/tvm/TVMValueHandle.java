package org.apache.tvm;
/* loaded from: d:\Downloads\Project\Chatter\mlc-chat-extracted\classes3.dex */
public class TVMValueHandle extends TVMValue {
    public final long value;

    @Override // org.apache.tvm.TVMValue
    public long asHandle() {
        return this.value;
    }

    public TVMValueHandle(long j) {
        super(ArgTypeCode.HANDLE);
        this.value = j;
    }
}
