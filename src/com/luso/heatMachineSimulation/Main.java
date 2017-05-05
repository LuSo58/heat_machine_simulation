package com.luso.heatMachineSimulation;
//
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
//
@SuppressWarnings("WeakerAccess")
public class Main extends Application {

    //region Globals
    public enum heatMachineInputs {
        Volume1,
        Volume2,
        Temperature2,
        Pleasure2,
        HeatValue,
        AirFuelRatio,
        Rpm,
        CylinderCount,
    }
    public static final double airHeatCapacity = 0.72;
    public static final double airDensity = 1.29;
    public static final double specificHeatRatioAir = 1.4;
    public static final double specificHeatRatioCO2 = 1.6666666666666667;

    public static final String[] heatMachineInputsShorts = new String[]{
            "V₂",
            "V₃",
            "T₂",
            "p₂",
            "Q",
            "f",
            "rpm",
            "n",
    };
    public static final double[] heatMachineInputsStartingValues = new double[]{
            625,
            30,
            289,
            0.1,
            44800,
            50,
            4000,
            4,
    };
    public static final double[] heatMachineInputsMin = new double[]{
            100,
            5,
            240,
            0.08,
            10000,
            15,
            600,
            1,
    };
    public static final double[] heatMachineInputsMax = new double[]{
            2000,
            100,
            310,
            0.13,
            60000,
            120,
            5000,
            14,
    };
    public static final double[] heatMachineInputsSteps = new double[]{
            1,
            30,
            1,
            0.005,
            1000,
            2,
            100,
            1,
    };
    @SuppressWarnings("SpellCheckingInspection")
    public static final double frac = .0000001;
    public static int samples;
    //
    private Canvas diagramCanvas;
    private Image diagramPlaceholder;
    private Canvas pistonCanvas;
    private Button buttonStart;
    private Button buttonStop;
    private Button buttonPause;
    //
    private final Spinner[] spinners = new Spinner[8] ;
    private final WebView infoOutput = new WebView();
    private final WebView infoDynamic = new WebView();
    private Timeline timeline;
    //
    private double animationPScale;
    private double animationVScale;
    private Image animationDiagram;
    private Snapshot[] animationTrace;
    private int animationState = 0;
    private static final int animationCircleSize = 12;
    //endregion

    @Override
    public void start(Stage stage) throws Exception {
        AnchorPane anchorPane = new AnchorPane();
        AnchorPane inputPane;
        for (int i = 0; i < heatMachineInputs.values().length; i++) {
            Label label = new Label(heatMachineInputsShorts[i]);
            label.setFont(new Font("Arial", 17));
            label.setTextAlignment(TextAlignment.CENTER);
            label.setLayoutX(10);
            label.setLayoutY(12);
            spinners[i] = new Spinner<Double>(heatMachineInputsMin[i], heatMachineInputsMax[i], heatMachineInputsStartingValues[i], heatMachineInputsSteps[i]);
            spinners[i].setLayoutX(43);
            spinners[i].setLayoutY(10);
            spinners[i].setEditable(true);
            spinners[i].focusedProperty().addListener((s, ov, nv) -> {
                if (nv) return;
                commitEditorText();
            });
            inputPane = new AnchorPane();
            inputPane.getChildren().addAll(label, spinners[i]);
            inputPane.setPrefWidth(230);
            inputPane.setPrefHeight(50);
            inputPane.setLayoutX(i/3*230);
            inputPane.setLayoutY(i%3*50);
            anchorPane.getChildren().add(inputPane);
        }
        //
        Button btn = new Button("Start");
        btn.setLayoutX(700);
        btn.setLayoutY(10);
        btn.setPrefWidth(90);
        btn.setPrefHeight(30);
        btn.setOnAction(buttonStartEvent);
        buttonStart = btn;
        anchorPane.getChildren().add(btn);
        btn = new Button("Stop");
        btn.setLayoutX(700);
        btn.setLayoutY(50);
        btn.setPrefWidth(90);
        btn.setPrefHeight(30);
        btn.setDisable(true);
        btn.setOnAction(buttonStopEvent);
        buttonStop = btn;
        anchorPane.getChildren().add(btn);
        btn = new Button("Pause");
        btn.setLayoutX(700);
        btn.setLayoutY(90);
        btn.setPrefWidth(90);
        btn.setPrefHeight(30);
        btn.setDisable(true);
        btn.setOnAction(buttonPauseEvent);
        buttonPause = btn;
        anchorPane.getChildren().add(btn);
        //
        WebView infoLegend = new WebView();
        infoLegend.getEngine().loadContent("" +
                "<html><style rel=\"stylesheet\" type=\"text/css\">" +
                "html { " +
                "background: transparent; " +
                "margin: 0; " +
                "padding: 5px; " +
                "font-size: 1.05em; " +
                "font-family: Roboto, Arial; " +
                "line-height: 1.3em; " +
                "} " +
                "html * { " +
                "background: transparent; " +
                "margin: 0; " +
                "padding: 0; " +
                "line-height: 1.5em; " +
                "}" +
                "</style>" +
                "<p>V<sub>2</sub> - Objem při expanzi [cm<sup>3</sup>]<br>" +
                "V<sub>3</sub> - Objem při kompresi [cm<sup>3</sup>]<br>" +
                "T<sub>2</sub> - Venkovní teplota [K]<br>" +
                "p<sub>2</sub> - Atmosférický tlak [MPa]<br>" +
                "Q - Výhřevnost paliva [kJ&times;kg<sup>-1</sup>]<br>" +
                "f - Poměr vzduch/palivo<br>" +
                "rpm - Otáčky za minutu [rpm]<br>" +
                "n - Počet válců<br>" +
                "η - Účinnost motoru<br>" +
                "r - Kompresní poměr<br><br><br>" +
                "c<sub>V</sub> - Měrná tepelná kapacita [kJ&times;kg<sup>-1</sup>&times;K<sup>-1</sup>]<br>" +
                "ρ - Hustota vzduchu [kg&times;m<sup>-3</sup>]<br>" +
                "γ - Poissonova konstanta</p><html>");
        infoOutput.getEngine().loadContent("");
        infoDynamic.getEngine().loadContent("");
        infoLegend.setPrefWidth(400);
        infoLegend.setPrefHeight(490);
        infoLegend.setLayoutX(10);
        infoLegend.setLayoutY(160);
        infoOutput.setPrefWidth(195);
        infoOutput.setPrefHeight(490);
        infoOutput.setLayoutX(420);
        infoOutput.setLayoutY(160);
        infoDynamic.setPrefWidth(195);
        infoDynamic.setPrefHeight(490);
        infoDynamic.setLayoutX(625);
        infoDynamic.setLayoutY(160);
        //
        anchorPane.getChildren().addAll(infoLegend, infoOutput, infoDynamic);
        //
        anchorPane.setMinWidth(830);
        anchorPane.setMinHeight(660);
        //
        VBox vBox = new VBox();
        //
        diagramCanvas = new Canvas(330, 330);
        diagramPlaceholder = new Image(this.getClass().getResourceAsStream("/diagram_diesel.png"));
        diagramCanvas.getGraphicsContext2D().drawImage(diagramPlaceholder, 0, 0);
        //
        pistonCanvas = new Canvas(330, 330);
        pistonCanvas.getGraphicsContext2D().setFill(new Color(0, 0, 0, 1));
        pistonCanvas.getGraphicsContext2D().fillRect(0, 0, 330, 330);
        //
        vBox.getChildren().addAll(diagramCanvas, pistonCanvas);
        //
        assert animate != null;
        timeline = new Timeline(new KeyFrame(Duration.millis(30), animate));
        timeline.setCycleCount(Timeline.INDEFINITE);
        //
        BorderPane rootPane = new BorderPane();
        rootPane.setCenter(anchorPane);
        rootPane.setRight(vBox);
        Scene scene = new Scene(rootPane);
        stage.setScene(scene);
        stage.setTitle("Heat Machine Simulation");
        stage.show();
    }

    private EventHandler<ActionEvent> buttonStartEvent = event -> {
        Calculation calculation = calculate(
                (double)spinners[0].getValue() / 1000000,
                (double)spinners[1].getValue() / 1000000,
                (double)spinners[2].getValue(),
                (double)spinners[3].getValue(),
                (double)spinners[4].getValue(),
                (double)spinners[5].getValue(),
                ((Double)spinners[6].getValue()).intValue(),
                ((Double)spinners[7].getValue()).intValue());
        infoOutput.getEngine().loadContent(fillOutput(calculation));
        animationVScale = 280 / calculation.volume2;
        animationPScale = 280 / calculation.pressure4;
        prepairDiagram(calculation);
        timeline.play();
        buttonStop.setDisable(false);
        buttonPause.setDisable(false);
        buttonStart.setDisable(true);
    };

    private EventHandler<ActionEvent> buttonStopEvent = event -> {
        timeline.pause();
        animationState = 0;
        buttonStop.setDisable(true);
        buttonPause.setDisable(true);
        buttonStart.setDisable(false);
        diagramCanvas.getGraphicsContext2D().drawImage(diagramPlaceholder, 0, 0);
    };

    private EventHandler<ActionEvent> buttonPauseEvent = event -> {
        timeline.pause();
        buttonStop.setDisable(true);
        buttonPause.setDisable(true);
        buttonStart.setDisable(false);
    };

    private void prepairDiagram(Calculation calculation) {
        BufferedImage bufferedImage = new BufferedImage(330, 330, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = (Graphics2D)bufferedImage.getGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setPaint(java.awt.Color.WHITE);
        graphics.fillRect(0, 0, 330, 330);
        graphics.setStroke(new BasicStroke(3));
        graphics.setColor(java.awt.Color.BLACK);
        graphics.drawLine(30, 0, 30, 300);
        graphics.drawLine(30, 300, 330, 300);
        graphics.setStroke(new BasicStroke(1));
        graphics.drawLine(30, 300 - (int)animationPScale, 35, 300 - (int)animationPScale);
        graphics.drawString("1", 5, 300 - ((int)animationPScale) + 5);
        graphics.drawLine(30, 300 - (int)animationPScale * 2, 35, 300 - (int)animationPScale * 2);
        graphics.drawString("2", 5, 300 - ((int)animationPScale * 2) + 5);
        graphics.drawLine(30, 300 - (int)animationPScale * 5, 35, 300 - (int)animationPScale * 5);
        graphics.drawString("5", 5, 300 - ((int)animationPScale * 5) + 5);
        graphics.drawLine(30, 300 - (int)animationPScale * 10, 35, 300 - (int)animationPScale * 10);
        graphics.drawString("10", 5, 300 - ((int)animationPScale * 10) + 5);
        graphics.drawLine(30, 300 - (int)animationPScale * 15, 35, 300 - (int)animationPScale * 15);
        graphics.drawString("15", 5, 300 - ((int)animationPScale * 15) + 5);
        //
        System.out.println(animationVScale);
        graphics.drawLine((int)(animationVScale * .0001) + 30, 300, (int)(animationVScale * .0001) + 30, 295);
        graphics.drawString("100", (int)(animationVScale * .0001) + 20, 315);
        graphics.drawLine((int)(animationVScale * .0002) + 30, 300, (int)(animationVScale * .0002) + 30, 295);
        graphics.drawString("200", (int)(animationVScale * .0002) + 20, 315);
        graphics.drawLine((int)(animationVScale * .0005) + 30, 300, (int)(animationVScale * .0005) + 30, 295);
        graphics.drawString("500", (int)(animationVScale * .0005) + 20, 315);
        //
        //region Calculate Snapshots
        LinkedList<Snapshot> temporaryList = new LinkedList<>();
        {
            for (double i = calculation.volume3; i < calculation.volume2; i += frac) {
                Snapshot temp = new Snapshot();
                temp.pressure = calculation.pressure2;
                temp.temp = calculation.temperature2;
                temp.volume = i;
                temp.X = (int) (temp.volume * animationVScale + 30);
                temp.Y = (int) (300 - temp.pressure * animationPScale);
                temporaryList.add(temp);
            }
            for (int i = 0; i < samples; i++) {
                Snapshot temp = new Snapshot();
                temp.pressure = calculation.process23[1][i];
                temp.temp = calculation.process23[2][i];
                temp.volume = calculation.process23[0][i];
                temp.X = (int) (temp.volume * animationVScale + 30);
                temp.Y = (int) (300 - temp.pressure * animationPScale);
                temporaryList.add(temp);
            }
            int samplesForExplosion = 5;
            for (int i = 1; i < samplesForExplosion; i++) {
                Snapshot temp = new Snapshot();
                temp.pressure = calculation.process23[1][samples - 1] + (calculation.process35[1][0] - calculation.process23[1][samples - 1]) * (i / samplesForExplosion);
                temp.temp = calculation.process23[2][samples - 1] + (calculation.process35[2][0] - calculation.process23[2][samples - 1]) * (i / samplesForExplosion);
                temp.volume = calculation.process23[0][samples - 1];
                temp.X = (int) (temp.volume * animationVScale + 30);
                temp.Y = (int) (300 - temp.pressure * animationPScale);
                temporaryList.add(temp);
            }
            for (int i = 0; i < samples; i++) {
                Snapshot temp = new Snapshot();
                temp.pressure = calculation.process35[1][i];
                temp.temp = calculation.process35[2][i];
                temp.volume = calculation.process35[0][i];
                temp.X = (int) (temp.volume * animationVScale + 30);
                temp.Y = (int) (300 - temp.pressure * animationPScale);
                temporaryList.add(temp);
            }
            {
                double temperature = temporaryList.getLast().temp;
                double startingVolume = temporaryList.getLast().volume;
                double pressure = temporaryList.getLast().pressure;
                for (double i = startingVolume; i > calculation.volume3; i -= frac) {
                    Snapshot temp = new Snapshot();
                    temp.pressure = pressure;
                    temp.temp = temperature;
                    temp.volume = i;
                    temp.X = (int) (temp.volume * animationVScale + 30);
                    temp.Y = (int) (300 - temp.pressure * animationPScale);
                    temporaryList.add(temp);
                }
            }
        }
        animationTrace = (temporaryList.toArray(new Snapshot[temporaryList.size()]));
        //endregion
        //
        Polygon temporaryPolygon = new Polygon();
        for (Snapshot snapshot : animationTrace) {
            temporaryPolygon.addPoint(snapshot.X, snapshot.Y);
        }
        graphics.setColor(new java.awt.Color(255, 0, 0));
        graphics.drawPolygon(temporaryPolygon);
        //
        bufferedImage.flush();
        animationDiagram = SwingFXUtils.toFXImage(bufferedImage, null);
        diagramCanvas.getGraphicsContext2D().drawImage(animationDiagram, 0, 0);
    }

    private EventHandler<ActionEvent> animate = event -> {
        GraphicsContext graphics = diagramCanvas.getGraphicsContext2D();
        graphics.drawImage(animationDiagram, 0, 0);
        graphics.setFill(Color.rgb(0, 141, 255, 0.502));
        graphics.fillOval(animationTrace[animationState].X - animationCircleSize / 2,
                animationTrace[animationState].Y - animationCircleSize / 2,
                animationCircleSize,
                animationCircleSize);
        graphics.setFill(Color.rgb(0, 0, 255));
        graphics.setLineWidth(2);
        graphics.strokeOval(animationTrace[animationState].X - animationCircleSize / 2,
                animationTrace[animationState].Y - animationCircleSize / 2,
                animationCircleSize,
                animationCircleSize);
        //
        infoDynamic.getEngine().loadContent("" +
                "<html><style rel=\"stylesheet\" type=\"text/css\">" +
                "html { " +
                "background: transparent; " +
                "margin: 0; " +
                "padding: 5px; " +
                "font-size: 1.05em; " +
                "font-family: Roboto, Arial; " +
                "line-height: 1.3em; " +
                "} " +
                "html * { " +
                "background: transparent; " +
                "margin: 0; " +
                "padding: 0; " +
                "line-height: 1.5em; " +
                "}" +
                "</style>" +
                "<p>" +
                "p = " + (double) Math.round(animationTrace[animationState].pressure * 1000) / 1000 + "MPa<br>" +
                "V = " + (double) Math.round(animationTrace[animationState].volume * 1000000) + " cm<sup>3</sup><br>" +
                "T = " + (double) Math.round(animationTrace[animationState].temp * 1000) / 1000 + " K<br>");
        //
        animationState+=30;
        if (animationState >= animationTrace.length) animationState = 0;
    };

    private static String fillOutput(Calculation calculation) {
        return "<html><style rel=\"stylesheet\" type=\"text/css\">" +
                "html { " +
                "background: transparent; " +
                "margin: 0; " +
                "padding: 5px; " +
                "font-size: 1.05em; " +
                "font-family: Roboto, Arial; " +
                "line-height: 1.3em; " +
                "} " +
                "html * { " +
                "background: transparent; " +
                "margin: 0; " +
                "padding: 0; " +
                "line-height: 1.5em; " +
                "}" +
                "</style>" +
                "<p>" +
                "r = " + (double)Math.round(calculation.compressionRatio * 1000) / 1000 + "<br>" +
                "c<sub>V</sub> = 0.72 kJ&times;kg<sup>-1</sup>&times;K<sup>-1</sup><br>" +
                "&rho; = 1.29 kg × m<sup>3</sup><br>" +
                "&gamma; = 1.4<br><br><br>" +
                "T<sub>3</sub> = " + (double)Math.round(calculation.temperature3 * 1000) / 1000 + " K<br>" +
                "p<sub>3</sub> = " + (double)Math.round(calculation.pressure3 * 1000) / 1000 + " MPa<br>" +
                "T<sub>4</sub> = " + (double)Math.round(calculation.temperature4 * 1000) / 1000 + " K<br>" +
                "p<sub>4</sub> = " + (double)Math.round(calculation.pressure4 * 1000) / 1000 + " MPa<br>" +
                "T<sub>5</sub> = " + (double)Math.round(calculation.temperature5 * 1000) / 1000 + " K<br>" +
                "p<sub>5</sub> = " + (double)Math.round(calculation.pressure5 * 1000) / 1000 + " MPa<br><br>" +
                "W = " + (double)Math.round(calculation.work * 1000) / 1000 + " kJ<br><br>" +
                "P = " + (double)Math.round(calculation.power * 1000) / 1000 + " kW</p></html>";
    }

    public static Calculation calculate(double volume2,
                                        double volume3,
                                        double temperature2,
                                        double pressure2,
                                        double heatValue,
                                        double airFuelRatio,
                                        int rpm,
                                        int cylinderCount) {
        Calculation calculation = new Calculation();
        calculation.compressionRatio = volume2 / volume3;
        //
        samples = (int)((volume2 - volume3) / frac) + 1;
        calculation.process23 = new double[3][samples];
        calculation.process23[0][0] = calculation.volume2 = volume2;
        calculation.process23[1][0] = calculation.pressure2 = pressure2;
        calculation.process23[2][0] = calculation.temperature2 = temperature2;
        for (int i = 1; i < samples; i++) {
            calculation.process23[0][i] = calculation.process23[0][i - 1] - frac;
            calculation.process23[1][i] = calculation.process23[1][i - 1] * Math.pow(calculation.process23[0][i - 1] / calculation.process23[0][i], specificHeatRatioAir);
            calculation.process23[2][i] = calculation.process23[2][i - 1] * Math.pow(calculation.process23[0][i - 1] / calculation.process23[0][i], specificHeatRatioAir - 1);
        }
        calculation.volume3 = calculation.process23[0][samples - 1];
        calculation.temperature3 = calculation.process23[2][samples - 1];
        calculation.temperature4 = calculation.temperature3 + (heatValue / (airFuelRatio * airHeatCapacity));
        calculation.volume4 = calculation.volume3 * calculation.temperature4 / calculation.temperature3;
        calculation.process35 = new double[3][samples];
        calculation.process35[0][0] = calculation.volume3;
        calculation.process35[1][0] = calculation.pressure3 = calculation.pressure4 = calculation.process23[1][samples - 1];
        calculation.process35[2][0] = calculation.temperature3;
        int samplesForIsobaric = (int)((calculation.volume4 - calculation.volume3)/frac);
        for (int i = 1; i < samplesForIsobaric; i++) {
            calculation.process35[0][i] = calculation.process35[0][i - 1] + frac;
            calculation.process35[1][i] = calculation.process35[1][i - 1];
            calculation.process35[2][i] = calculation.process35[2][i - 1] + (calculation.temperature4 - calculation.temperature3) / samplesForIsobaric;
        }
        for (int i = samplesForIsobaric; i < samples; i++) {
            calculation.process35[0][i] = calculation.process35[0][i - 1] + frac;
            calculation.process35[1][i] = calculation.process35[1][i - 1] * Math.pow(calculation.process35[0][i - 1] / calculation.process35[0][i], specificHeatRatioCO2);
            calculation.process35[2][i] = calculation.process35[2][i - 1] * (Math.pow(calculation.process35[0][i - 1] / calculation.process35[0][i], specificHeatRatioCO2 - 1));
        }
        calculation.volume5 = calculation.process35[0][samples - 1];
        calculation.pressure5 = calculation.process35[1][samples - 1];
        calculation.temperature5 = calculation.process35[2][samples - 1];
        double work = 0;
        for (int i = 0; i < samples; i++) {
            work += ((calculation.process35[1][samples - i - 1] - calculation.process23[1][i]));
        }
        work *= frac * 1000;
        calculation.work = work;
        calculation.power = work * rpm / 60 * cylinderCount;
        //
        return calculation;
    }

    @SuppressWarnings("unchecked")
    private <T> void commitEditorText() {
        for (Spinner spinner : spinners) {
            if (!spinner.isEditable()) return;
            String text = spinner.getEditor().getText();
            SpinnerValueFactory<T> valueFactory = spinner.getValueFactory();
            if (valueFactory != null) {
                StringConverter<T> converter = valueFactory.getConverter();
                if (converter != null) {
                    T value = converter.fromString(text);
                    valueFactory.setValue(value);
                }
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
