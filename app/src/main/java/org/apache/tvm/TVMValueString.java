package org.apache.tvm;
/* loaded from: d:\Downloads\Project\Chatter\mlc-chat-extracted\classes3.dex */
public class TVMValueString extends TVMValue {
    public final String value;

    @Override // org.apache.tvm.TVMValue
    public String asString() {
        return this.value;
    }

    public TVMValueString(String str) {
        super(ArgTypeCode.STR);
        this.value = str;
    }
}
