package org.apache.tvm;

import java.io.File;
import java.io.IOException;
import org.apache.tvm.NativeLibraryLoader;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: d:\Downloads\Project\Chatter\mlc-chat-extracted\classes3.dex */
public final class Base {
    public static final LibInfo _LIB = new LibInfo();

    /* loaded from: d:\Downloads\Project\Chatter\mlc-chat-extracted\classes3.dex */
    public static class RefLong {
        public final long value;

        public RefLong(long j) {
            this.value = j;
        }

        public RefLong() {
            this(0L);
        }
    }

    /* loaded from: d:\Downloads\Project\Chatter\mlc-chat-extracted\classes3.dex */
    public static class RefTVMValue {
        public final TVMValue value;

        public RefTVMValue(TVMValue tVMValue) {
            this.value = tVMValue;
        }

        public RefTVMValue() {
            this(null);
        }
    }

    static {
        boolean z;
        String str;
        try {
            try {
                tryLoadLibraryOS("tvm4j");
            } catch (UnsatisfiedLinkError unused) {
                System.err.println("[WARN] TVM native library not found in path. Copying native library from the archive. Consider installing the library somewhere in the path (for Windows: PATH, for Linux: LD_LIBRARY_PATH), or specifying by Java cmd option -Djava.library.path=[lib path].");
                NativeLibraryLoader.loadLibrary("tvm4j");
            }
            z = true;
        } catch (Throwable th) {
            System.err.println("[WARN] Couldn't find native library tvm4j.");
            th.printStackTrace();
            System.err.println("Try to load tvm4j (runtime packed version) ...");
            try {
                System.loadLibrary("tvm4j_runtime_packed");
                z = false;
            } catch (UnsatisfiedLinkError e) {
                System.err.println("[ERROR] Couldn't find native library tvm4j_runtime_packed.");
                throw new RuntimeException(e);
            }
        }
        System.err.println("libtvm4j loads successfully.");
        if (z) {
            String property = System.getProperty("libtvm.so.path");
            if (property == null || !new File(property).isFile() || _LIB.nativeLibInit(property) != 0) {
                try {
                    String property2 = System.getProperty("os.name");
                    if (property2.startsWith("Linux")) {
                        str = "libtvm_runtime.so";
                    } else if (!property2.startsWith("Mac")) {
                        throw new UnsatisfiedLinkError(property2 + " not supported currently");
                    } else {
                        str = "libtvm_runtime.dylib";
                    }
                    NativeLibraryLoader.extractResourceFileToTempDir(str, new NativeLibraryLoader.Action() { // from class: org.apache.tvm.Base.1
                        @Override // org.apache.tvm.NativeLibraryLoader.Action
                        public void invoke(File file) {
                            System.err.println("Loading tvm runtime from " + file.getPath());
                            Base.checkCall(Base._LIB.nativeLibInit(file.getPath()));
                        }
                    });
                } catch (IOException e2) {
                    throw new RuntimeException(e2);
                }
            }
        } else {
            _LIB.nativeLibInit(null);
        }
        Runtime.getRuntime().addShutdownHook(new Thread() { // from class: org.apache.tvm.Base.2
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                Base._LIB.shutdown();
            }
        });
    }

    private static void tryLoadLibraryOS(String str) throws UnsatisfiedLinkError {
        try {
            System.err.println(String.format("Try loading %s from native path.", str));
            System.loadLibrary(str);
        } catch (UnsatisfiedLinkError unused) {
            String property = System.getProperty("os.name");
            if (property.startsWith("Linux")) {
                tryLoadLibraryXPU(str, "linux-x86_64");
            } else if (property.startsWith("Mac")) {
                tryLoadLibraryXPU(str, "osx-x86_64");
            } else {
                throw new UnsatisfiedLinkError("Windows not supported currently");
            }
        }
    }

    private static void tryLoadLibraryXPU(String str, String str2) throws UnsatisfiedLinkError {
        System.err.println(String.format("Try loading %s-%s from native path.", str, str2));
        System.loadLibrary(String.format("%s-%s", str, str2));
    }

    public static void checkCall(int i) throws TVMError {
        if (i != 0) {
            throw new TVMError(_LIB.tvmGetLastError());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: d:\Downloads\Project\Chatter\mlc-chat-extracted\classes3.dex */
    public static class TVMError extends RuntimeException {
        public TVMError(String str) {
            super(str);
        }
    }

    private Base() {
    }
}
