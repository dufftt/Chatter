package org.apache.tvm;

import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import org.apache.tvm.Base;
/* loaded from: d:\Downloads\Project\Chatter\mlc-chat-extracted\classes3.dex */
public class NDArray extends NDArrayBase {
    private final Device device;
    private final TVMType dtype;

    public Device device() {
        return this.device;
    }

    NDArray(long j, boolean z, TVMType tVMType, Device device) {
        super(j, z);
        this.dtype = tVMType;
        this.device = device;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tvm.NDArrayBase
    public void finalize() throws Throwable {
        super.finalize();
    }

    public void copyFrom(double[] dArr) {
        checkCopySize(dArr.length);
        if (this.dtype.typeCode != 2 || this.dtype.bits != 64) {
            throw new IllegalArgumentException("Cannot set double[] for " + this.dtype.toString() + " array");
        }
        byte[] bArr = new byte[dArr.length * this.dtype.numOfBytes];
        for (int i = 0; i < dArr.length; i++) {
            wrapBytes(bArr, this.dtype.numOfBytes * i, this.dtype.numOfBytes).putDouble(dArr[i]);
        }
        NDArray empty = empty(shape(), this.dtype);
        Base.checkCall(Base._LIB.tvmArrayCopyFromJArray(bArr, empty.handle, this.handle));
        empty.release();
    }

    public void copyFrom(float[] fArr) {
        checkCopySize(fArr.length);
        if (this.dtype.typeCode != 2 || this.dtype.bits != 32) {
            throw new IllegalArgumentException("Cannot set float[] for " + this.dtype.toString() + " array");
        }
        byte[] bArr = new byte[fArr.length * this.dtype.numOfBytes];
        for (int i = 0; i < fArr.length; i++) {
            wrapBytes(bArr, this.dtype.numOfBytes * i, this.dtype.numOfBytes).putFloat(fArr[i]);
        }
        NDArray empty = empty(shape(), this.dtype);
        Base.checkCall(Base._LIB.tvmArrayCopyFromJArray(bArr, empty.handle, this.handle));
        empty.release();
    }

    public void copyFrom(long[] jArr) {
        checkCopySize(jArr.length);
        if (this.dtype.typeCode != 0 || this.dtype.bits != 64) {
            throw new IllegalArgumentException("Cannot set long[] for " + this.dtype.toString() + " array");
        }
        byte[] bArr = new byte[jArr.length * this.dtype.numOfBytes];
        for (int i = 0; i < jArr.length; i++) {
            wrapBytes(bArr, this.dtype.numOfBytes * i, this.dtype.numOfBytes).putLong(jArr[i]);
        }
        NDArray empty = empty(shape(), this.dtype);
        Base.checkCall(Base._LIB.tvmArrayCopyFromJArray(bArr, empty.handle, this.handle));
        empty.release();
    }

    public void copyFrom(int[] iArr) {
        checkCopySize(iArr.length);
        if (this.dtype.typeCode != 0 || this.dtype.bits != 32) {
            throw new IllegalArgumentException("Cannot set int[] for " + this.dtype.toString() + " array");
        }
        byte[] bArr = new byte[iArr.length * this.dtype.numOfBytes];
        for (int i = 0; i < iArr.length; i++) {
            wrapBytes(bArr, this.dtype.numOfBytes * i, this.dtype.numOfBytes).putInt(iArr[i]);
        }
        NDArray empty = empty(shape(), this.dtype);
        Base.checkCall(Base._LIB.tvmArrayCopyFromJArray(bArr, empty.handle, this.handle));
        empty.release();
    }

    public void copyFrom(short[] sArr) {
        checkCopySize(sArr.length);
        if (this.dtype.typeCode != 0 || this.dtype.bits != 16) {
            throw new IllegalArgumentException("Cannot set short[] for " + this.dtype.toString() + " array");
        }
        byte[] bArr = new byte[sArr.length * this.dtype.numOfBytes];
        for (int i = 0; i < sArr.length; i++) {
            wrapBytes(bArr, this.dtype.numOfBytes * i, this.dtype.numOfBytes).putShort(sArr[i]);
        }
        NDArray empty = empty(shape(), this.dtype);
        Base.checkCall(Base._LIB.tvmArrayCopyFromJArray(bArr, empty.handle, this.handle));
        empty.release();
    }

    public void copyFrom(byte[] bArr) {
        checkCopySize(bArr.length);
        if (this.dtype.typeCode != 0 || this.dtype.bits != 8) {
            throw new IllegalArgumentException("Cannot set byte[] for " + this.dtype.toString() + " array");
        } else {
            copyFromRaw(bArr);
        }
    }

    public void copyFrom(char[] cArr) {
        checkCopySize(cArr.length);
        if (this.dtype.typeCode != 1 || this.dtype.bits != 16) {
            throw new IllegalArgumentException("Cannot set char[] for " + this.dtype.toString() + " array");
        }
        byte[] bArr = new byte[cArr.length * this.dtype.numOfBytes];
        for (int i = 0; i < cArr.length; i++) {
            wrapBytes(bArr, this.dtype.numOfBytes * i, this.dtype.numOfBytes).putChar(cArr[i]);
        }
        NDArray empty = empty(shape(), this.dtype);
        Base.checkCall(Base._LIB.tvmArrayCopyFromJArray(bArr, empty.handle, this.handle));
        empty.release();
    }

    private void checkCopySize(int i) {
        if (size() != i) {
            throw new IllegalArgumentException(String.format("Array shape size not match: %d v.s. %d", Integer.valueOf(i), Long.valueOf(size())));
        }
    }

    public void copyFromRaw(byte[] bArr) {
        NDArray empty = empty(shape(), this.dtype);
        Base.checkCall(Base._LIB.tvmArrayCopyFromJArray(bArr, empty.handle, this.handle));
        empty.release();
    }

    public long[] shape() {
        ArrayList arrayList = new ArrayList();
        Base.checkCall(Base._LIB.tvmArrayGetShape(this.handle, arrayList));
        int size = arrayList.size();
        long[] jArr = new long[size];
        for (int i = 0; i < size; i++) {
            jArr[i] = ((Long) arrayList.get(i)).longValue();
        }
        return jArr;
    }

    public long size() {
        long j = 1;
        for (long j2 : shape()) {
            j *= j2;
        }
        return j;
    }

    public double[] asDoubleArray() {
        if (this.dtype.typeCode != 2 || this.dtype.bits != 64) {
            throw new IllegalArgumentException("Cannot set convert to double[] for " + this.dtype.toString() + " array");
        }
        byte[][] groupInternalBytes = groupInternalBytes();
        double[] dArr = new double[groupInternalBytes.length];
        for (int i = 0; i < groupInternalBytes.length; i++) {
            dArr[i] = wrapBytes(groupInternalBytes[i]).getDouble();
        }
        return dArr;
    }

    public float[] asFloatArray() {
        if (this.dtype.typeCode != 2 || this.dtype.bits != 32) {
            throw new IllegalArgumentException("Cannot set convert to float[] for " + this.dtype.toString() + " array");
        }
        byte[][] groupInternalBytes = groupInternalBytes();
        float[] fArr = new float[groupInternalBytes.length];
        for (int i = 0; i < groupInternalBytes.length; i++) {
            fArr[i] = wrapBytes(groupInternalBytes[i]).getFloat();
        }
        return fArr;
    }

    public long[] asLongArray() {
        if (this.dtype.typeCode != 0 || this.dtype.bits != 64) {
            throw new IllegalArgumentException("Cannot set convert to long[] for " + this.dtype.toString() + " array");
        }
        byte[][] groupInternalBytes = groupInternalBytes();
        long[] jArr = new long[groupInternalBytes.length];
        for (int i = 0; i < groupInternalBytes.length; i++) {
            jArr[i] = wrapBytes(groupInternalBytes[i]).getLong();
        }
        return jArr;
    }

    public int[] asIntArray() {
        if (this.dtype.typeCode != 0 || this.dtype.bits != 32) {
            throw new IllegalArgumentException("Cannot set convert to int[] for " + this.dtype.toString() + " array");
        }
        byte[][] groupInternalBytes = groupInternalBytes();
        int[] iArr = new int[groupInternalBytes.length];
        for (int i = 0; i < groupInternalBytes.length; i++) {
            iArr[i] = wrapBytes(groupInternalBytes[i]).getInt();
        }
        return iArr;
    }

    public short[] asShortArray() {
        if (this.dtype.typeCode != 0 || this.dtype.bits != 16) {
            throw new IllegalArgumentException("Cannot set convert to short[] for " + this.dtype.toString() + " array");
        }
        byte[][] groupInternalBytes = groupInternalBytes();
        short[] sArr = new short[groupInternalBytes.length];
        for (int i = 0; i < groupInternalBytes.length; i++) {
            sArr[i] = wrapBytes(groupInternalBytes[i]).getShort();
        }
        return sArr;
    }

    public char[] asCharArray() {
        if (this.dtype.typeCode != 1 || this.dtype.bits != 16) {
            throw new IllegalArgumentException("Cannot set convert to char[] for " + this.dtype.toString() + " array");
        }
        byte[][] groupInternalBytes = groupInternalBytes();
        char[] cArr = new char[groupInternalBytes.length];
        for (int i = 0; i < groupInternalBytes.length; i++) {
            cArr[i] = wrapBytes(groupInternalBytes[i]).getChar();
        }
        return cArr;
    }

    public byte[] asByteArray() {
        if (this.dtype.typeCode != 0 || this.dtype.bits != 8) {
            throw new IllegalArgumentException("Cannot set convert to byte[] for " + this.dtype.toString() + " array");
        }
        return internal();
    }

    public byte[] internal() {
        NDArray empty = empty(shape(), this.dtype);
        copyTo(empty);
        byte[] bArr = new byte[this.dtype.numOfBytes * ((int) size())];
        Base.checkCall(Base._LIB.tvmArrayCopyToJArray(empty.handle, bArr));
        return bArr;
    }

    private byte[][] groupInternalBytes() {
        byte[] internal = internal();
        int i = this.dtype.numOfBytes;
        if (internal.length <= 0 || internal.length % i != 0) {
            throw new IllegalArgumentException(String.format("%s size %d cannot divide byte array size %d", this.dtype.toString(), Integer.valueOf(i), Integer.valueOf(internal.length)));
        }
        int length = internal.length / i;
        byte[][] bArr = (byte[][]) Array.newInstance(Byte.TYPE, length, i);
        for (int i2 = 0; i2 < length; i2++) {
            System.arraycopy(internal, i2 * i, bArr[i2], 0, i);
        }
        return bArr;
    }

    public static NDArray empty(long[] jArr, TVMType tVMType, Device device) {
        Base.RefLong refLong = new Base.RefLong();
        Base.checkCall(Base._LIB.tvmArrayAlloc(jArr, tVMType.typeCode, tVMType.bits, tVMType.lanes, device.deviceType, device.deviceId, refLong));
        return new NDArray(refLong.value, false, tVMType, device);
    }

    public static NDArray empty(long[] jArr, TVMType tVMType) {
        return empty(jArr, tVMType, Device.cpu(0));
    }

    public static NDArray empty(long[] jArr) {
        return empty(jArr, new TVMType("float32", 1), Device.cpu(0));
    }

    public static NDArray empty(long[] jArr, Device device) {
        return empty(jArr, new TVMType("float32", 1), device);
    }

    private static ByteBuffer wrapBytes(byte[] bArr) {
        ByteBuffer wrap = ByteBuffer.wrap(bArr);
        wrap.order(ByteOrder.LITTLE_ENDIAN);
        return wrap;
    }

    private static ByteBuffer wrapBytes(byte[] bArr, int i, int i2) {
        ByteBuffer wrap = ByteBuffer.wrap(bArr, i, i2);
        wrap.order(ByteOrder.LITTLE_ENDIAN);
        return wrap;
    }
}
