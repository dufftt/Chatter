package org.apache.tvm;
/* loaded from: d:\Downloads\Project\Chatter\mlc-chat-extracted\classes3.dex */
public class TVMType {
    public static final int FLOAT = 2;
    public static final int HANDLE = 4;
    public static final int INT = 0;
    public static final int UINT = 1;
    public final int bits;
    public final int lanes;
    public final int numOfBytes;
    public final int typeCode;

    public int hashCode() {
        return (this.typeCode << 16) | (this.bits << 8) | this.lanes;
    }

    public TVMType(String str, int i) {
        int i2;
        this.lanes = i;
        if (str.startsWith("int")) {
            this.typeCode = 0;
            i2 = Integer.parseInt(str.substring(3));
        } else if (str.startsWith("uint")) {
            this.typeCode = 1;
            i2 = Integer.parseInt(str.substring(4));
        } else if (str.startsWith("float")) {
            this.typeCode = 2;
            i2 = Integer.parseInt(str.substring(5));
        } else if (!str.startsWith("handle")) {
            throw new IllegalArgumentException("Do not know how to handle type " + str);
        } else {
            this.typeCode = 4;
            i2 = 64;
        }
        i2 = i2 == 0 ? 32 : i2;
        this.bits = i2;
        if (((i2 - 1) & i2) != 0 || i2 < 8) {
            throw new IllegalArgumentException("Do not know how to handle type " + str);
        }
        this.numOfBytes = i2 / 8;
    }

    public TVMType(String str) {
        this(str, 1);
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof TVMType)) {
            return false;
        }
        TVMType tVMType = (TVMType) obj;
        return this.bits == tVMType.bits && this.typeCode == tVMType.typeCode && this.lanes == tVMType.lanes;
    }

    public String toString() {
        int i = this.typeCode;
        String str = (i != 0 ? i != 1 ? i != 2 ? i != 4 ? "Unknown" : "handle" : "float" : "uint" : "int") + this.bits;
        int i2 = this.lanes;
        return i2 != 1 ? str + i2 : str;
    }
}
