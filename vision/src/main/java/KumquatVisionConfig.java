import org.opencv.core.Scalar;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class KumquatVisionConfig {

    public KumquatVisionConfig() {
        try {
            BufferedReader in = new BufferedReader(new FileReader("config"));

            blurSize = Integer.parseInt(in.readLine());

            String lowerbound = in.readLine();
            var vals = lowerbound.split(",");
            lowerBound = new Scalar(Integer.parseInt(vals[0]), Integer.parseInt(vals[1]), Integer.parseInt(vals[2]));
            String upperBound = in.readLine();
            vals = upperBound.split(",");
            higherBound = new Scalar(Integer.parseInt(vals[0]), Integer.parseInt(vals[1]), Integer.parseInt(vals[2]));

            minRadius = Integer.parseInt(in.readLine());
            maxRadius = Integer.parseInt(in.readLine());
            maxDelta = Integer.parseInt(in.readLine());
            param1 = Integer.parseInt(in.readLine());
            param2 = Integer.parseInt(in.readLine());
        } catch (IOException e) {
            System.err.println("CAN NOT READ FILE, USING DEFAULTS");
            blurSize = 1;
            lowerBound = new Scalar(20, 108, 77);
            higherBound = new Scalar(47, 255, 184);
            minRadius = 0;
            maxRadius = 25;
            maxDelta = 2;

            param1 = 300;
            param2 = 10;
        }
        System.out.println("P2: " + param2 + "   !!!");
    }

    public int blurSize;
    public Scalar lowerBound;
    public Scalar higherBound;
    public double minRadius;
    public double maxRadius;
    public double maxDelta;

    // hough
    public int param1;
    public int param2;
}
