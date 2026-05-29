package org.apache.tvm;
/* loaded from: d:\Downloads\Project\Chatter\mlc-chat-extracted\classes3.dex */
public class TVMValueDouble extends TVMValue {
    public final double value;

    @Override // org.apache.tvm.TVMValue
    public double asDouble() {
        return this.value;
    }

    public TVMValueDouble(double d) {
        super(ArgTypeCode.FLOAT);
        this.value = d;
    }
}
