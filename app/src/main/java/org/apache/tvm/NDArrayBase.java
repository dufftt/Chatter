package org.apache.tvm;
/* loaded from: d:\Downloads\Project\Chatter\mlc-chat-extracted\classes3.dex */
public class NDArrayBase extends TVMValue {
    protected final long handle;
    private boolean isReleased;
    protected final boolean isView;

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // org.apache.tvm.TVMValue
    public long asHandle() {
        return this.handle;
    }

    @Override // org.apache.tvm.TVMValue
    public NDArrayBase asNDArray() {
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public NDArrayBase(long j, boolean z) {
        super(ArgTypeCode.ARRAY_HANDLE);
        this.isReleased = false;
        this.handle = j;
        this.isView = z;
    }

    NDArrayBase(long j) {
        this(j, true);
    }

    public NDArrayBase copyTo(NDArrayBase nDArrayBase) {
        Base.checkCall(Base._LIB.tvmArrayCopyFromTo(this.handle, nDArrayBase.handle));
        return nDArrayBase;
    }

    @Override // org.apache.tvm.TVMValue
    public void release() {
        if (this.isReleased || this.isView) {
            return;
        }
        Base.checkCall(Base._LIB.tvmArrayFree(this.handle));
        this.isReleased = true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void finalize() throws Throwable {
        release();
        super.finalize();
    }
}
