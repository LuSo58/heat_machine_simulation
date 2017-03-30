package heatMachineSimulation;
//
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class Main extends Application {

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
    public static final double specificHeatRatio = 1.4;

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
            2000,
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
    private final Spinner[] spinners = new Spinner[8] ;
    private final WebView infoOutput = new WebView();

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
        Button buttonStart = new Button("Start");
        buttonStart.setLayoutX(728);
        buttonStart.setLayoutY(60);
        buttonStart.setPrefWidth(50);
        buttonStart.setPrefHeight(30);
        buttonStart.setOnAction(buttonStartEvent);
        //
        WebView infoLegend = new WebView();
        infoLegend.getEngine().loadContent("<html><style rel=\"stylesheet\" type=\"text/css\">html { background: transparent; margin: 0; padding: 5px; font-size: 1.05em; font-family: Roboto, Arial; line-height: 1.3em; } html * { background: transparent; margin: 0; padding: 0; line-height: 1.5em; }</style><p>V<sub>2</sub> - Objem při expanzi [cm<sup>3</sup>]<br>V<sub>3</sub> - Objem při kompresi [cm<sup>3</sup>]<br>T<sub>2</sub> - Venkovní teplota [K]<br>p<sub>2</sub> - Atmosférický tlak [MPa]<br>Q - Výhřevnost paliva [kJ&times;kg<sup>-1</sup>]<br>f - Poměr vzduch/palivo<br>rpm - Otáčky za minutu [rpm]<br>n - Počet válců<br>η - Účinnost motoru<br>r - Kompresní poměr<br><br><br>c<sub>V</sub> - Měrná tepelná kapacita [kJ&times;kg<sup>-1</sup>&times;K<sup>-1</sup>]<br>ρ - Hustota vzduchu [kg&times;m<sup>-3</sup>]<br>γ - Poissonova konstanta</p><html>");
        infoOutput.getEngine().loadContent("");
        infoLegend.setPrefWidth(400);
        infoLegend.setPrefHeight(430);
        infoLegend.setLayoutX(10);
        infoLegend.setLayoutY(160);
        infoOutput.setPrefWidth(400);
        infoOutput.setPrefHeight(430);
        infoOutput.setLayoutX(420);
        infoOutput.setLayoutY(160);
        //
        anchorPane.getChildren().addAll(buttonStart, infoLegend, infoOutput);
        //
        anchorPane.setMinWidth(830);
        anchorPane.setMinHeight(600);
        //
        VBox vBox = new VBox();
        //
        Canvas diagramCanvas = new Canvas(300, 300);
        diagramCanvas.getGraphicsContext2D().drawImage(new Image(this.getClass().getResourceAsStream("/diagram_otto.png")), 0, 0);
        //
        Canvas animationCanvas = new Canvas(300, 300);
        //
        vBox.getChildren().addAll(diagramCanvas, animationCanvas);
        //
        BorderPane rootPane = new BorderPane();
        rootPane.setCenter(anchorPane);
        rootPane.setRight(vBox);
        Scene scene = new Scene(rootPane);
        stage.setScene(scene);
        stage.setTitle("Heat Machine Simulation");
        stage.show();
    }

    private EventHandler buttonStartEvent = event -> {
        Calculation calculation = calculate((double)spinners[0].getValue() / 1000000, (double)spinners[1].getValue() / 1000000, (double)spinners[2].getValue(), (double)spinners[3].getValue(), (double)spinners[4].getValue(), (double)spinners[5].getValue(), ((Double)spinners[6].getValue()).intValue(), ((Double)spinners[7].getValue()).intValue());
        infoOutput.getEngine().loadContent(fillOutput(calculation));
    };

    public static String fillOutput(Calculation calculation) {
        return "<html><style rel=\"stylesheet\" type=\"text/css\">html { background: transparent; margin: 0; padding: 5px; font-size: 1.05em; font-family: Roboto, Arial; line-height: 1.3em; } html * { background: transparent; margin: 0; padding: 0; line-height: 1.5em; }</style><p>" +
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

    public static Calculation calculate(double volume2, double volume3, double temperature2, double pressure2, double heatValue, double airFuelRatio, int rpm, int cylinderCount) {
        Calculation calculation = new Calculation();
        calculation.compressionRatio = volume2 / volume3;
        calculation.temperature3 = temperature2 * Math.pow(calculation.compressionRatio, specificHeatRatio - 1);
        calculation.pressure3 = pressure2 * Math.pow(calculation.compressionRatio, specificHeatRatio);
        calculation.temperature4 = calculation.temperature3 + (heatValue / (airFuelRatio * airHeatCapacity));
        calculation.pressure4 = calculation.temperature4 * calculation.pressure3 / calculation.temperature3;
        calculation.temperature5 = calculation.temperature4 * Math.pow(calculation.compressionRatio, 1 - specificHeatRatio);
        calculation.pressure5 = calculation.pressure4 * Math.pow(calculation.compressionRatio, - specificHeatRatio);
        //
        double frac = .000001;
        int samples = (int)((volume2 - volume3) / frac) + 1;
        calculation.process23 = new double[3][samples];
        calculation.process23[0][0] = volume2;
        calculation.process23[1][0] = pressure2;
        calculation.process23[2][0] = temperature2;
        for (int i = 1; i < samples; i++) {
            calculation.process23[0][i] = calculation.process23[0][i - 1] - frac;
            calculation.process23[1][i] = calculation.process23[1][i - 1] * Math.pow(calculation.process23[0][i - 1] / calculation.process23[0][i], specificHeatRatio);
            calculation.process23[2][i] = calculation.process23[2][i - 1] * Math.pow(calculation.process23[0][i - 1] / calculation.process23[0][i], specificHeatRatio - 1);
        }
        calculation.process45 = new double[3][samples];
        calculation.process45[0][0] = calculation.process23[0][samples - 1];
        calculation.process45[2][0] = calculation.process23[2][samples - 1] + (heatValue / (airFuelRatio * airHeatCapacity));
        calculation.process45[1][0] = calculation.process23[1][samples - 1] * calculation.process45[2][0] / calculation.process23[2][samples - 1];
        for (int i = 1; i < samples; i++) {
            calculation.process45[0][i] = calculation.process45[0][i - 1] + frac;
            calculation.process45[1][i] = calculation.process45[1][i - 1] * Math.pow(calculation.process45[0][i - 1] / calculation.process45[0][i], -specificHeatRatio);
            calculation.process45[2][i] = calculation.process45[2][i - 1] * Math.pow(calculation.process45[0][i - 1] / calculation.process45[0][i], 1 - specificHeatRatio);
        }
        double work = 0;
        for (int i = 0; i < samples; i++) {
            work += 1 * (calculation.process45[1][samples - i - 1] - calculation.process23[1][i]);
        }
        work *= frac;
        calculation.work = work;
        calculation.power = work * rpm / 60 * cylinderCount;
        //
        return calculation;
    }

    private <T> void commitEditorText() {
        for (int i = 0; i < spinners.length; i++) {
            if (!spinners[i].isEditable()) return;
            String text = spinners[i].getEditor().getText();
            SpinnerValueFactory<T> valueFactory = spinners[i].getValueFactory();
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
