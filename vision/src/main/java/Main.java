import org.opencv.core.Core;
import java.io.File;

public class Main {

    // set this to false/true in order to make it easier to switch between testing/production code
    public static final boolean TESTING = false;

    static {
//         The OpenCV jar just contains a wrapper that allows us to interface with the implementation of OpenCV written in C++
//         So, we have to load those C++ libraries explicitly and linked them properly.
//         Just having the jars is not sufficient, OpenCV must be installed into the filesystem manually.
//         I prefer to build it from source using CMake
        if (TESTING) {
            if (System.getProperty("os.name").contains("Windows")) {
                System.load(new File("./lib/" + Core.NATIVE_LIBRARY_NAME).getAbsolutePath() + ".dll");
            } else if (System.getProperty("os.name").contains("Linux")) {

                System.load("/usr/lib/jni/libopencv_java420.so");
                // System.load(new File("./lib/lib" + Core.NATIVE_LIBRARY_NAME).getAbsolutePath() + ".so");
            } else {
                System.load(new File("./lib/lib" + Core.NATIVE_LIBRARY_NAME).getAbsolutePath() + ".dylib");
            }
        } else {
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        }
    }

    public static void main(String[] args) {
        System.out.println("Kumquat Vision V2");
        System.out.println("Version 0.1");

        int port = 5809;

        if (TESTING) {
//            new KumquatVision(port).setDebug("src/main/resources/image-3.jpg").run();
            new KumquatVision(port).run();
        } else {
            new KumquatVision(port).run();
        }
    }
}
