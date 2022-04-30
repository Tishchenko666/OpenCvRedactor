package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static org.opencv.imgproc.Imgproc.FONT_HERSHEY_PLAIN;
import static org.opencv.imgproc.Imgproc.getTextSize;

public class Controller implements Initializable {
    //center
    public ImageView mainImage;
    //top
    public RadioButton addTextRB;
    public RadioButton makeMirrorRB;
    public RadioButton changeColorRB;
    //bottom
    public Button loadImageButton;
    public Button saveImageButton;
    public Button resetButton;
    public Button changeButton;
    //right addText
    public VBox textVBox;
    public TextField textField;
    public ComboBox textColorCB = new ComboBox();
    public ComboBox backColorCB = new ComboBox();
    public RadioButton textIntoImageRB;
    public RadioButton textOnImageRB;
    //right mirror
    public VBox mirrorVBox;
    public CheckBox verticalCheckBox;
    public CheckBox horizontalCheckBox;
    public CheckBox mergeOriginalCheckBox;
    //right changeColor
    public VBox changeColorVBox;
    public ComboBox chooseColorModeCB = new ComboBox();
    public RadioButton mergeVerticalRB;
    public RadioButton mergeHorizontalRB;

    //other
    private String name;
    private int saveCounter = 0;
    private int counter = 0;
    private Mat matImage;
    private FileChooser fileChooser;
    private DirectoryChooser directoryChooser;
    ObservableList<String> colorList =
            FXCollections.observableArrayList("Black", "White", "Red", "Blue", "Green", "Yellow");
    ObservableList<String> modeList =
            FXCollections.observableArrayList("Серый", "Черно-белый", "Контуры", "Контуры инверсия", "Негатив", "Сепия");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        textColorCB.setItems(colorList);
        textColorCB.setValue(colorList.get(1));
        backColorCB.setItems(colorList);
        backColorCB.setValue(colorList.get(0));
        chooseColorModeCB.setItems(modeList);
        fileChooser = new FileChooser();
        directoryChooser = new DirectoryChooser();
    }

    @FXML
    private void enableText(ActionEvent event) {
        textVBox.setDisable(false);
        mirrorVBox.setDisable(true);
        changeColorVBox.setDisable(true);
    }

    @FXML
    private void enableMirror(ActionEvent event) {
        textVBox.setDisable(true);
        mirrorVBox.setDisable(false);
        changeColorVBox.setDisable(true);
    }

    @FXML
    private void enableColor(ActionEvent event) {
        textVBox.setDisable(true);
        mirrorVBox.setDisable(true);
        changeColorVBox.setDisable(false);
    }

    @FXML
    private void loadImage(ActionEvent event) {
        File file = this.fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            String path = file.getAbsolutePath();
            this.matImage = Imgcodecs.imread(path);
            if (this.matImage.empty()) {
                System.out.println("Не удалось загрузить изображение");
                return;
            }
            name = path.substring(path.lastIndexOf('\\') + 1, path.lastIndexOf('.'));
            mainImage.setImage(CvUtilsFX.MatToImageFX(this.matImage));
        }
    }

    @FXML
    private void saveImage(ActionEvent event) {
        File directory = directoryChooser.showDialog(new Stage());
        if (directory != null) {
            saveCounter++;
            boolean saved = Imgcodecs.imwrite(directory.getAbsolutePath() + "/" + name + saveCounter + ".jpg",
                    CvUtilsFX.ImageFXToMat(mainImage.getImage()));
            if (!saved)
                System.out.println("Не удалось сохранить изображение");
        }
    }

    @FXML
    private void resetImage(ActionEvent event) {
        mainImage.setImage(CvUtilsFX.MatToImageFX(this.matImage));
    }

    @FXML
    private void changeImage(ActionEvent event) {
        if (!changeColorVBox.isDisable()) {
            if (chooseColorModeCB.getValue() != null) {
                switch (chooseColorModeCB.getSelectionModel().getSelectedIndex()) {
                    case 0:
                        colorToGray();
                        break;
                    case 1:
                        colorToBW();
                        break;
                    case 2:
                        colorToOutlineBW();
                        break;
                    case 3:
                        colorToOutlineInversionBW();
                        break;
                    case 4:
                        makeNegative();
                        break;
                    case 5:
                        makeSepia();
                        break;
                }
            }
        } else if (!mirrorVBox.isDisable()) {
            if (counter == 2)
                mirror(-1, false);
            else if (counter == 1) {
                if (verticalCheckBox.isSelected())
                    mirror(0, false);
                else if (horizontalCheckBox.isSelected())
                    mirror(1, false);
            } else if (counter == 0) {
                if (mergeOriginalCheckBox.isSelected()) {
                    if (mergeVerticalRB.isSelected())
                        mirror(0, true);
                    else if (mergeHorizontalRB.isSelected())
                        mirror(1, true);
                }
            }
        } else if (!textVBox.isDisable()) {
            Scalar bc = CvUtils.COLOR_BLACK, tc = CvUtils.COLOR_WHITE;
            if (backColorCB.getValue() != null) {
                switch (backColorCB.getSelectionModel().getSelectedIndex()) {
                    case 0:
                        bc = CvUtils.COLOR_BLACK;
                        break;
                    case 1:
                        bc = CvUtils.COLOR_WHITE;
                        break;
                    case 2:
                        bc = CvUtils.COLOR_RED;
                        break;
                    case 3:
                        bc = CvUtils.COLOR_BLUE;
                        break;
                    case 4:
                        bc = CvUtils.COLOR_GREEN;
                        break;
                    case 5:
                        bc = CvUtils.COLOR_YELLOW;
                        break;
                }
            }
            if (textColorCB.getValue() != null) {
                switch (textColorCB.getSelectionModel().getSelectedIndex()) {
                    case 0:
                        tc = CvUtils.COLOR_BLACK;
                        break;
                    case 1:
                        tc = CvUtils.COLOR_WHITE;
                        break;
                    case 2:
                        tc = CvUtils.COLOR_RED;
                        break;
                    case 3:
                        tc = CvUtils.COLOR_BLUE;
                        break;
                    case 4:
                        tc = CvUtils.COLOR_GREEN;
                        break;
                    case 5:
                        tc = CvUtils.COLOR_YELLOW;
                        break;
                }
            }
            if (textIntoImageRB.isSelected()) {
                if (!bc.equals(tc))
                    addImageIntoImage(bc, tc);
                else {
                    addImageIntoImage(CvUtils.COLOR_BLACK, CvUtils.COLOR_WHITE);
                }
            } else if (textOnImageRB.isSelected()) {
                if (!bc.equals(tc))
                    addImageOnImage(bc, tc);
                else {
                    addImageOnImage(CvUtils.COLOR_BLACK, CvUtils.COLOR_WHITE);
                }
            }

        }
    }

    @FXML
    private void countFlags(ActionEvent event) {
        counter = 0;
        if (verticalCheckBox.isSelected()) counter++;
        if (horizontalCheckBox.isSelected()) counter++;
        if (counter > 0) {
            mergeOriginalCheckBox.setSelected(false);
            mergeVerticalRB.setVisible(false);
            mergeHorizontalRB.setVisible(false);
        }
    }

    @FXML
    private void detouchMirrorMode(ActionEvent event) {
        counter = 0;
        if (mergeOriginalCheckBox.isSelected()) {
            verticalCheckBox.setSelected(false);
            horizontalCheckBox.setSelected(false);
            mergeVerticalRB.setVisible(true);
            mergeHorizontalRB.setVisible(true);
        } else {
            mergeVerticalRB.setVisible(false);
            mergeHorizontalRB.setVisible(false);
        }
    }

    private void colorToGray() {
        Mat img = new Mat();
        this.matImage.copyTo(img);
        Imgproc.cvtColor(img, img, Imgproc.COLOR_BGR2GRAY);
        mainImage.setImage(CvUtilsFX.MatToImageFX(img));
        img.release();
    }

    private void colorToBW() {
        Mat img = new Mat();
        matImage.copyTo(img);
        Mat img2 = new Mat();
        Imgproc.cvtColor(img, img2, Imgproc.COLOR_BGR2GRAY);
        Mat img3 = new Mat();
        double thresh = Imgproc.threshold(img2, img3, 100, 255, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);
        mainImage.setImage(CvUtilsFX.MatToImageFX(img3));
        img.release();
        img2.release();
        img3.release();
    }

    private void colorToOutlineBW() {
        Mat img = new Mat();
        matImage.copyTo(img);
        Mat img2 = new Mat();
        Imgproc.cvtColor(img, img2, Imgproc.COLOR_BGR2GRAY);
        Mat img3 = new Mat();
        Imgproc.adaptiveThreshold(img2, img3, 255,
                Imgproc.ADAPTIVE_THRESH_MEAN_C,
                Imgproc.THRESH_BINARY, 3, 5);
        mainImage.setImage(CvUtilsFX.MatToImageFX(img3));
        img.release();
        img2.release();
        img3.release();

    }

    private void colorToOutlineInversionBW() {
        Mat img = new Mat();
        matImage.copyTo(img);
        Mat img2 = new Mat();
        Imgproc.cvtColor(img, img2, Imgproc.COLOR_BGR2GRAY);
        Mat img4 = new Mat();
        Imgproc.adaptiveThreshold(img2, img4, 255,
                Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C,
                Imgproc.THRESH_BINARY_INV, 3, 5);
        mainImage.setImage(CvUtilsFX.MatToImageFX(img4));
        img.release();
        img2.release();
        img4.release();
    }

    private void makeNegative() {
        Mat img = new Mat();
        matImage.copyTo(img);
        Mat m = new Mat(img.rows(), img.cols(), img.type(), CvUtils.COLOR_WHITE);
        Mat negative = new Mat();
        Core.subtract(m, img, negative);
        mainImage.setImage(CvUtilsFX.MatToImageFX(negative));
        img.release();
        negative.release();
        m.release();
    }

    private void makeSepia() {
        Mat img = new Mat();
        matImage.copyTo(img);
        // Построение матрицы трансформации
        Mat kernel = new Mat(3, 3, CvType.CV_32FC1);
        kernel.put(0, 0,
                0.131, 0.534, 0.272, // blue = b * b1 + g * g1 + r * r1
                0.168, 0.686, 0.349, // green = b * b2 + g * g2 + r * r2
                0.189, 0.769, 0.393 // red = b * b3 + g * g3 + r * r3
        );
        Mat sepia = new Mat();
        Core.transform(img, sepia, kernel);
        mainImage.setImage(CvUtilsFX.MatToImageFX(sepia));
        img.release();
        kernel.release();
        sepia.release();
    }

    private void mirror(int flipType, boolean concat) {
        Image image = null;
        Mat img = new Mat();
        matImage.copyTo(img);
        Mat img2 = new Mat();
        switch (flipType) {
            case 0:
                Core.flip(img, img2, 0);//вертикаль
                if (concat)
                    image = mergeMirrors(img, img2, 0);
                else
                    image = CvUtilsFX.MatToImageFX(img2);
                break;
            case 1:
                Core.flip(img, img2, 1);//горизонталь
                if (concat)
                    image = mergeMirrors(img, img2, 1);
                else
                    image = CvUtilsFX.MatToImageFX(img2);
                break;
            case -1:
                Core.flip(img, img2, -1);//оба
                image = CvUtilsFX.MatToImageFX(img2);
                break;
        }
        mainImage.setImage(image);
        img.release();
        img2.release();
    }

    private Image mergeMirrors(Mat img, Mat img2, int code) {
        ArrayList<Mat> list = new ArrayList<Mat>();
        list.add(img);
        list.add(img2);
        Mat imgM = new Mat();
        if (code == 0) {
            Core.vconcat(list, imgM);//вертикаль
        } else if (code == 1) {
            Core.hconcat(list, imgM);//горизонталь
        }
        img.release();
        img2.release();
        return CvUtilsFX.MatToImageFX(imgM);
    }

    private void addImageIntoImage(Scalar backColor, Scalar textColor) {
        Mat img = new Mat();
        matImage.copyTo(img);
        int[] baseline = new int[1];
        String text = textField.getText();
        Size textSize = getTextSize(text, FONT_HERSHEY_PLAIN, 3, 2, baseline);
        Mat img2 = new Mat((int) Math.ceil(textSize.height * 2.5), (int) Math.ceil(textSize.width * 1.1),
                CvType.CV_8UC3, backColor);
        Imgproc.putText(img2, text, new Point(10, 50),
                FONT_HERSHEY_PLAIN, 3, textColor,
                2, Imgproc.LINE_AA, false);
        Mat roi = img.submat(new Rect(0, 0, img2.width(), img2.height()));
        img2.copyTo(roi);
        mainImage.setImage(CvUtilsFX.MatToImageFX(img));
        img.release();
        img2.release();
    }

    private void addImageOnImage(Scalar backColor, Scalar textColor) {
        Mat img = new Mat();
        matImage.copyTo(img);
        int[] baseline = new int[1];
        String text = textField.getText();
        Size textSize = getTextSize(text, FONT_HERSHEY_PLAIN, 3, 2, baseline);
        Mat img2 = new Mat((int) Math.ceil(textSize.height * 2.5), (int) Math.ceil(textSize.width * 1.1),
                CvType.CV_8UC3, backColor);
        Imgproc.putText(img2, text, new Point(10, 50),
                FONT_HERSHEY_PLAIN, 3, textColor,
                2, Imgproc.LINE_AA, false);
        Mat roi = img.submat(new Rect(0, 0, img2.width(), img2.height()));
        Core.addWeighted(roi, 0.75, img2, 0.25, 0, roi);
        mainImage.setImage(CvUtilsFX.MatToImageFX(img));
        img.release();
        img2.release();
    }

}
