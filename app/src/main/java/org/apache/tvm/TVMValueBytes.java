package org.apache.tvm;
/* loaded from: d:\Downloads\Project\Chatter\mlc-chat-extracted\classes3.dex */
public class TVMValueBytes extends TVMValue {
    public final byte[] value;

    @Override // org.apache.tvm.TVMValue
    public byte[] asBytes() {
        return this.value;
    }

    public TVMValueBytes(byte[] bArr) {
        super(ArgTypeCode.BYTES);
        this.value = bArr;
    }
}
