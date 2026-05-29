package org.apache.tvm;

import java.util.HashMap;
import java.util.Map;
/* loaded from: d:\Downloads\Project\Chatter\mlc-chat-extracted\classes3.dex */
public class Device {
    private static final Map<Integer, String> MASK2STR;
    private static final Map<String, Integer> STR2MASK;
    static final int kDLAOCL = 32;
    static final int kDLCPU = 1;
    static final int kDLCUDA = 2;
    static final int kDLCUDAHost = 3;
    static final int kDLCUDAManaged = 13;
    static final int kDLExtDev = 12;
    static final int kDLHexagon = 16;
    static final int kDLMetal = 8;
    static final int kDLMicroDev = 35;
    static final int kDLOneAPI = 14;
    static final int kDLOpenCL = 4;
    static final int kDLROCM = 10;
    static final int kDLROCMHost = 11;
    static final int kDLSDAccel = 33;
    static final int kDLVPI = 9;
    static final int kDLVulkan = 7;
    static final int kDLWebGPU = 15;
    static final int kOpenGL = 34;
    public final int deviceId;
    public final int deviceType;

    public int hashCode() {
        return (this.deviceType << 16) | this.deviceId;
    }

    static {
        HashMap hashMap = new HashMap();
        MASK2STR = hashMap;
        HashMap hashMap2 = new HashMap();
        STR2MASK = hashMap2;
        hashMap.put(1, "cpu");
        hashMap.put(2, "cuda");
        hashMap.put(4, "opencl");
        hashMap.put(7, "vulkan");
        hashMap.put(8, "metal");
        hashMap.put(9, "vpi");
        hashMap.put(16, "hexagon");
        hashMap2.put("cpu", 1);
        hashMap2.put("cuda", 2);
        hashMap2.put("cl", 4);
        hashMap2.put("opencl", 4);
        hashMap2.put("vulkan", 7);
        hashMap2.put("metal", 8);
        hashMap2.put("vpi", 9);
        hashMap2.put("hexagon", 16);
    }

    public static Device cpu(int i) {
        return new Device(1, i);
    }

    public static Device cpu() {
        return cpu(0);
    }

    public static Device cuda(int i) {
        return new Device(2, i);
    }

    public static Device cuda() {
        return cuda(0);
    }

    public static Device opencl(int i) {
        return new Device(4, i);
    }

    public static Device opencl() {
        return opencl(0);
    }

    public static Device vulkan(int i) {
        return new Device(7, i);
    }

    public static Device vulkan() {
        return vulkan(0);
    }

    public static Device metal(int i) {
        return new Device(8, i);
    }

    public static Device metal() {
        return metal(0);
    }

    public static Device vpi(int i) {
        return new Device(9, i);
    }

    public static Device vpi() {
        return vpi(0);
    }

    public static Device hexagon(int i) {
        return new Device(16, i);
    }

    public static Device hexagon() {
        return hexagon(0);
    }

    public Device(int i, int i2) {
        this.deviceType = i;
        this.deviceId = i2;
    }

    public Device(String str, int i) {
        this(STR2MASK.get(str).intValue(), i);
    }

    public boolean exist() {
        return ((TVMValueLong) APIInternal.get("_GetDeviceAttr").pushArg(this.deviceType).pushArg(this.deviceId).pushArg(0).invoke()).value != 0;
    }

    public long maxThreadsPerBlock() {
        return ((TVMValueLong) APIInternal.get("_GetDeviceAttr").pushArg(this.deviceType).pushArg(this.deviceId).pushArg(1).invoke()).value;
    }

    public long warpSize() {
        return ((TVMValueLong) APIInternal.get("_GetDeviceAttr").pushArg(this.deviceType).pushArg(this.deviceId).pushArg(2).invoke()).value;
    }

    public void sync() {
        Base.checkCall(Base._LIB.tvmSynchronize(this.deviceType, this.deviceId));
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Device)) {
            return false;
        }
        Device device = (Device) obj;
        return this.deviceId == device.deviceId && this.deviceType == device.deviceType;
    }

    public String toString() {
        int i = this.deviceType;
        if (i >= 128) {
            return String.format("remote[%d]:%s(%d)", Integer.valueOf((i / 128) - 1), MASK2STR.get(Integer.valueOf(i % 128)), Integer.valueOf(this.deviceId));
        }
        return String.format("%s(%d)", MASK2STR.get(Integer.valueOf(i)), Integer.valueOf(this.deviceId));
    }
}
