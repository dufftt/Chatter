package org.apache.tvm;
/* loaded from: d:\Downloads\Project\Chatter\mlc-chat-extracted\classes3.dex */
public class TVMValueLong extends TVMValue {
    public final long value;

    @Override // org.apache.tvm.TVMValue
    public long asLong() {
        return this.value;
    }

    public TVMValueLong(long j) {
        super(ArgTypeCode.INT);
        this.value = j;
    }
}
