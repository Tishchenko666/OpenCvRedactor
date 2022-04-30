package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.opencv.core.*;

/*import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;

import static org.opencv.imgproc.Imgproc.FONT_HERSHEY_PLAIN;
import static org.opencv.imgproc.Imgproc.getTextSize;*/

public class Main extends Application {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        BorderPane root = FXMLLoader.load(getClass().getResource("main.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setTitle("Redactor");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(event -> {
            System.exit(0);
        });
        primaryStage.show();
    }

    /*private void onClickButton(ActionEvent e) {
        //getImage();
        //addImageIntoImage();
        //addImageOnImage();
        //mirror();
        //mirrorAndConcat();
        //colorToGray();
        //colorToBW();
        //colorToOutlineBW();
        //makeNegative();
        //makeSepia();
    }

    private void getImage() {
        // Загружаем изображение из файла
        Mat img = Imgcodecs.imread("C:\\Users\\Asus\\Pictures\\image.jpg");
        if (img.empty()) {
            System.out.println("Не удалось загрузить изображение");
            return;
        }
        // Обрабатываем изображение
        // Отображаем в отдельном окне
        CvUtilsFX.showImage(img, "Test");
        img.release();
    }

    private void addImageIntoImage() {
        Mat img = Imgcodecs.imread("C:\\Users\\Asus\\Pictures\\image.jpg");
        if (img.empty()) {
            System.out.println("Не удалось загрузить изображение");
            return;
        }
        int[] baseline = new int[1];
        Size textSize = getTextSize("My Odesa", FONT_HERSHEY_PLAIN, 3, 2, baseline);
        Mat img2 = new Mat((int) Math.ceil(textSize.height * 2.5), (int) Math.ceil(textSize.width * 1.1),
                CvType.CV_8UC3, CvUtils.COLOR_BLACK);
        Imgproc.putText(img2, "My Odesa", new Point(10, 50),
                FONT_HERSHEY_PLAIN, 3, CvUtils.COLOR_WHITE,
                2, Imgproc.LINE_AA, false);
        Mat roi = img.submat(new Rect(0, 0, img2.width(), img2.height()));
        img2.copyTo(roi);
        CvUtilsFX.showImage(img, "Результат");
        img.release();
        img2.release();
    }

    private void addImageOnImage() {
        Mat img = Imgcodecs.imread("C:\\Users\\Asus\\Pictures\\image.jpg");
        if (img.empty()) {
            System.out.println("Не удалось загрузить изображение");
            return;
        }
        int[] baseline = new int[1];
        Size textSize = getTextSize("My Odesa", FONT_HERSHEY_PLAIN, 3, 2, baseline);
        Mat img2 = new Mat((int) Math.ceil(textSize.height * 2.5), (int) Math.ceil(textSize.width * 1.1),
                CvType.CV_8UC3, CvUtils.COLOR_BLACK);
        Imgproc.putText(img2, "My Odesa", new Point(10, 50),
                FONT_HERSHEY_PLAIN, 3, CvUtils.COLOR_WHITE,
                2, Imgproc.LINE_AA, false);
        Mat roi = img.submat(new Rect(0, 0, img2.width(), img2.height()));
        Core.addWeighted(roi, 0.25, img2, 0.75, 0, roi);
        CvUtilsFX.showImage(img, "Результат");
        img.release();
        img2.release();
    }

    private void mirror() {
        Mat img = Imgcodecs.imread("C:\\Users\\Asus\\Pictures\\image.jpg");
        if (img.empty()) {
            System.out.println("Не удалось загрузить изображение");
            return;
        }
        Mat img2 = new Mat();
        Core.flip(img, img2, 0);//вертикаль
        Mat img3 = new Mat();
        Core.flip(img, img3, 1);//горизонталь
        Mat img4 = new Mat();
        Core.flip(img, img4, -1);//оба
        CvUtilsFX.showImage(img2, "flipCode = 0");
        CvUtilsFX.showImage(img3, "flipCode = 1");
        CvUtilsFX.showImage(img4, "flipCode = -1");
        img.release();
        img2.release();
        img3.release();
        img4.release();
    }

    private void mirrorAndConcat() {
        Mat img = Imgcodecs.imread("C:\\Users\\Asus\\Pictures\\image.jpg");
        if (img.empty()) {
            System.out.println("Не удалось загрузить изображение");
            return;
        }
        Mat img2 = new Mat();
        Core.flip(img, img2, 0);
        Mat img3 = new Mat();
        Core.flip(img, img3, 1);
        ArrayList<Mat> listH = new ArrayList<Mat>();
        listH.add(img);
        listH.add(img3);
        ArrayList<Mat> listV = new ArrayList<Mat>();
        listV.add(img);
        listV.add(img2);
        Mat imgH = new Mat();
        Core.hconcat(listH, imgH);
        Mat imgV = new Mat();
        Core.vconcat(listV, imgV);
        CvUtilsFX.showImage(imgH, "hconcat");
        CvUtilsFX.showImage(imgV, "vconcat");
        img.release();
        img2.release();
        img3.release();
        imgH.release();
        imgV.release();
    }

    private void colorToGray() {
        Mat img = Imgcodecs.imread("C:\\Users\\Asus\\Pictures\\image.jpg");
        if (img.empty()) {
            System.out.println("Не удалось загрузить изображение");
            return;
        }
        Imgproc.cvtColor(img, img, Imgproc.COLOR_BGR2GRAY);
        CvUtilsFX.showImage(img, "To gray");
        img.release();
    }

    private void colorToBW() {
        Mat img = Imgcodecs.imread("C:\\Users\\Asus\\Pictures\\image.jpg");
        if (img.empty()) {
            System.out.println("Не удалось загрузить изображение");
            return;
        }
        Mat img2 = new Mat();
        Imgproc.cvtColor(img, img2, Imgproc.COLOR_BGR2GRAY);
        Mat img3 = new Mat();
        double thresh = Imgproc.threshold(img2, img3, 100, 255,
                Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);
        System.out.println(thresh);
        CvUtilsFX.showImage(img3, "THRESH_OTSU");
        img.release();
        img2.release();
        img3.release();
    }

    private void colorToOutlineBW() {
        Mat img = Imgcodecs.imread("C:\\Users\\Asus\\Pictures\\image.jpg");
        if (img.empty()) {
            System.out.println("Не удалось загрузить изображение");
            return;
        }
        Mat img2 = new Mat();
        Imgproc.cvtColor(img, img2, Imgproc.COLOR_BGR2GRAY);
        Mat img3 = new Mat();
        Imgproc.adaptiveThreshold(img2, img3, 255,
                Imgproc.ADAPTIVE_THRESH_MEAN_C,
                Imgproc.THRESH_BINARY, 3, 5);
        CvUtilsFX.showImage(img3, "ADAPTIVE_THRESH_MEAN_C");
        Mat img4 = new Mat();
        Imgproc.adaptiveThreshold(img2, img4, 255,
                Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C,
                Imgproc.THRESH_BINARY_INV, 3, 5);
        CvUtilsFX.showImage(img4, "ADAPTIVE_THRESH_GAUSSIAN_C");
        // Инверсия с помощью таблицы соответствия
        Mat lut = new Mat(1, 256, CvType.CV_8UC1);
        byte[] arr = new byte[256];
        for (int i = 0; i < 256; i++) {
            arr[i] = (byte) (255 - i);
        }
        lut.put(0, 0, arr);
        // Преобразование в соответствии с таблицей
        Mat imgInv = new Mat();
        Core.LUT(img3, lut, imgInv);
        CvUtilsFX.showImage(imgInv, "ADAPTIVE_THRESH_MEAN_C + Inv");
        img.release();
        img2.release();
        img3.release();
        img4.release();
        imgInv.release();
        lut.release();
    }

    private void makeNegative() {
        Mat img = Imgcodecs.imread("C:\\Users\\Asus\\Pictures\\image.jpg");
        if (img.empty()) {
            System.out.println("Не удалось загрузить изображение");
            return;
        }
        Mat m = new Mat(img.rows(), img.cols(), img.type(), CvUtils.COLOR_WHITE);
        Mat negative = new Mat();
        Core.subtract(m, img, negative);
        CvUtilsFX.showImage(negative, "Негатив");
        img.release();
        negative.release();
        m.release();
    }

    private void makeSepia() {
        Mat img = Imgcodecs.imread("C:\\Users\\Asus\\Pictures\\image.jpg");
        if (img.empty()) {
            System.out.println("Не удалось загрузить изображение");
            return;
        }
        // Построение матрицы трансформации
        Mat kernel = new Mat(3, 3, CvType.CV_32FC1);
        kernel.put(0, 0,
                0.131, 0.534, 0.272, // blue = b * b1 + g * g1 + r * r1
                0.168, 0.686, 0.349, // green = b * b2 + g * g2 + r * r2
                0.189, 0.769, 0.393 // red = b * b3 + g * g3 + r * r3
        );
        Mat sepia = new Mat();
        Core.transform(img, sepia, kernel);
        CvUtilsFX.showImage(sepia, "Сепия");
        img.release();
        kernel.release();
        sepia.release();
    }*/
}
