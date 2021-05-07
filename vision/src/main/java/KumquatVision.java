import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;
import org.opencv.videoio.VideoCapture;

import java.util.ArrayList;
import java.util.HashMap;

public class KumquatVision implements Runnable {

    private String pictureLocation = "";
    private boolean debug = false;
    private boolean running = false;
    private VideoCapture capture;
    private OpenCVServer server;
    private KumquatVisionConfig config;

    public KumquatVision(int port) {
        this(port, new KumquatVisionConfig());
    }

    public KumquatVision(int port, KumquatVisionConfig config) {
        this.config = config;
        server = new OpenCVServer(port);
    }

    private void start() {
        if (!debug) { setupCapture(); }
        running = true;
        new Thread(server).start();

        while (true) {
            loop();
        }
    }

    public KumquatVision setDebug(String pictureLocation) {
        if (running) { return this; }
        this.debug = true;
        this.pictureLocation = pictureLocation;
        return this;
    }

    @Override
    public void run() {
        start();
    }

    /* MAIN LOOP */
    private void loop() {
        Mat[] mat = getFrame();
        Mat origin = mat[0].clone();

        Mat preprocessed_mat = preprocess(mat[0]);
        var circles = houghCircles(preprocessed_mat);
        var candidates = findContours(preprocessed_mat);

        filterContours(candidates);
        findCircles(candidates, circles);

        drawContours(origin, candidates);
        // drawCircles(origin, circles);

        HashMap<String, Object> data = new HashMap<>();
        addCenters(data, candidates, origin);
        addRadii(data, candidates);
        System.out.println("Sending data :)");
        server.sendData(data, origin);
    }

    /* CAPTURE */
    private void setupCapture() {
        capture = new VideoCapture(0);
    }

    private Mat[] getFrame() {
        Mat frame = new Mat();
        if (debug) {
            frame = Imgcodecs.imread(this.pictureLocation);
        } else {
            capture.read(frame);
        }

        Imgproc.resize(frame, frame, new Size(800, 800));
        // Mat mask = new Mat(frame.rows(), frame.cols(), CvType.CV_8UC1, new Scalar(0));
        // MatOfPoint2f tmpTrapeziod = new MatOfPoint2f();
        // MatOfPoint2f tmpROI = new MatOfPoint2f();
        // tmpTrapeziod.fromList(pointsForTrapezoid);
        // tmpROI.fromList(roiPolygonized);
        // Imgproc.approxPolyDP(tmpTrapeziod, tmpROI, 1.0, true);

        // Imgproc.fillConvexPoly(mask, tmpROI, new Scalar(255), 8, 0);
//        Imgproc.rectangle(frame, new Point(0, 100), new Point(400, 200), new Scalar(0, 0, 0), 5, 0);
        Mat[] mats = new Mat[]{frame.submat(new Rect(0, 450, 800, 350)), frame};
        return mats;
    }

    /* PROCESSING */
    private Mat preprocess(Mat mat) {
        Imgproc.blur(mat, mat, new Size(config.blurSize, config.blurSize));
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2HSV);
        Core.inRange(mat, config.lowerBound, config.higherBound, mat);
        Imgproc.erode(mat, mat, Imgproc.getStructuringElement(Imgproc.CV_SHAPE_ELLIPSE, new Size(3, 3)));
        Imgproc.dilate(mat, mat, Imgproc.getStructuringElement(Imgproc.CV_SHAPE_ELLIPSE, new Size(4, 4)));

        return mat;
    }

    private Mat houghCircles(Mat mat) {
        Mat gray = mat.clone();
        Imgproc.blur(gray, gray, new Size(5, 5));
        Mat circles = new Mat();
        Imgproc.HoughCircles(gray,
                circles,
                Imgproc.HOUGH_GRADIENT,
                1,
                12,
                config.param1,
                config.param2,
                (int) config.minRadius,
                (int) config.maxRadius);
        return circles;
    }

    private ArrayList<MatOfPoint> findContours(Mat mat) {
        ArrayList<MatOfPoint> contourList = new ArrayList<>();
        Imgproc.findContours(mat, contourList, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        return contourList;
    }

    private ArrayList<MatOfPoint> filterContours(ArrayList<MatOfPoint> candidates) {
        for (int i = candidates.size() - 1; i >= 0; i--) {
            // optimize this
            MatOfPoint2f dst = new MatOfPoint2f();
            candidates.get(i).convertTo(dst, CvType.CV_32F);
            var radiusGetter = new float[1];
            Imgproc.minEnclosingCircle(dst, new Point(), radiusGetter);
            if (radiusGetter[0] >= config.maxRadius) {
                candidates.remove(i);
            }
            if (radiusGetter[0] <= config.minRadius) {
                candidates.remove(i);
            }
        }
        return candidates;
    }

    private Mat drawContours(Mat src, ArrayList<MatOfPoint> contours) {
        for (int i = 0; i < contours.size(); i++) {
            Imgproc.drawContours(src, contours, i, new Scalar(255, 0, 0), 2);
        }
        return src;
    }

    private ArrayList<MatOfPoint> findCircles(ArrayList<MatOfPoint> candidates, Mat circles) {
        for (int i = candidates.size() - 1; i >= 0; i--) {
            Point candidateCircle = findContourCentroid(candidates, i);
            boolean works = false;
            if (candidateCircle == null) {
                break;
            }
            for (int j = 0; j < circles.cols(); j++) {
                double[] data = circles.get(0, j);
                Point circleCenter = new Point(Math.round(data[0]), Math.round(data[1]));
                if (Math.abs(circleCenter.x - candidateCircle.x) <= config.maxDelta && Math.abs(circleCenter.y - candidateCircle.y) <= config.maxDelta) {
                    works = true;
                    break;
                }
            }
            if (!works) {
                candidates.remove(i);
            }
        }
        return candidates;
    }

    private int findLargestContour(ArrayList<MatOfPoint> candidates) {
        int largestIndex = -1;
        for (int i = 0; i < candidates.size(); i++) {
            if (largestIndex == -1) {
                largestIndex = i;
            }
            // optimize this
            if (Imgproc.contourArea(candidates.get(i)) >= Imgproc.contourArea(candidates.get(largestIndex))) {
                largestIndex = i;
            }
        }
        return largestIndex;
    }

    private Point findContourCentroid(ArrayList<MatOfPoint> candidates, int index) {
        if (index == -1) {
            return null;
        }
        Moments coords = Imgproc.moments(candidates.get(index));
        Point point = new Point();
        point.x = (int) (coords.get_m10() / (coords.get_m00()));
        point.y = (int) (coords.get_m01() / (coords.get_m00()));
        return point;
    }

    /* DRAWING */
    private Mat drawCircles(Mat src, Mat circles) {
        for (int i = 0; i < circles.cols(); i++ ) {
            double[] data = circles.get(0, i);
            if (data.length >= 2) {
                Point center = new Point(Math.round(data[0]), Math.round(data[1]));
                // circle center
                Imgproc.circle(src, center, 1, new Scalar(0, 0, 255), 3, 8, 0);
                // circle outline
                int radius = (int) Math.round(data[2]);
                Imgproc.circle(src, center, radius, new Scalar(0, 0, 255), 3, 8, 0);
            }
        }
        return src;
    }

    private Mat drawPoint(Mat mat, Point point) {
        if (point != null) {
            Imgproc.circle(mat, point, 5, new Scalar(255, 0, 0), 10);
        }
        return mat;
    }

    private void draw(Mat mat) {
        HighGui.imshow("Debug", mat);
        HighGui.waitKey(1);
    }

    /* NETWORKING */
    private void addRadii(HashMap<String, Object> data, ArrayList<MatOfPoint> contours) {
        ArrayList<Float> radii = new ArrayList<>();
        for (int i = 0; i < contours.size(); i++) {
            MatOfPoint2f dst = new MatOfPoint2f();
            contours.get(i).convertTo(dst, CvType.CV_32F);
            float[] radiusGetter = new float[1];
            Imgproc.minEnclosingCircle(dst, new Point(), radiusGetter);
            float radius = radiusGetter[0];
            radii.add(radius);
        }
        System.out.println("Found " + radii.size() + " radii.");
        data.put("radii", radii);
    }

    private void addCenters(HashMap<String, Object> data, ArrayList<MatOfPoint> contours, Mat mat) {
        ArrayList<java.awt.Point> centers = new ArrayList<>();
        for (int i = 0; i < contours.size(); i++) {
            var point = findContourCentroid(contours, i);
            centers.add(new java.awt.Point((int) ((2 * point.x / mat.size().width - 1) * 100),
                        (int) ((2 * point.y / mat.size().height - 1) * 100)));
            System.out.println(centers.get(i).x);
        }
        System.out.println("Found " + centers.size() + " centers.");
        data.put("centers", centers);
    }
}
