package heatMachineSimulation;
//
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

//
public class Main extends Application {

    @FXML
    private WebView infoOutput;
    @FXML
    private WebView infoLegend;
    @FXML
    private Pane rootPane;

    @Override
    public void start(Stage stage) throws Exception {
        BorderPane rootPane = FXMLLoader.load(getClass().getResource("/MainWindow.fxml"));
        Scene scene = new Scene(rootPane);
        stage.setScene(scene);
        stage.setTitle("Heat Machine Simulation");
        stage.show();
    }

    @FXML
    public void initialize() {
        infoOutput.getEngine().loadContent("<html><style rel=\"stylesheet\" type=\"text/css\">html { background: transparent; margin: 0; padding: 5px; font-size: 1.05em; font-family: Roboto, Arial; line-height: 1.3em; } html * { background: transparent; margin: 0; padding: 0; line-height: 1.5em; }</style><p>r = 21<br>c<sub>V</sub> = 0.72 kJ&times;kg<sup>-1</sup>&times;K<sup>-1</sup><br>ρ = 1.29 kg × m<sup>3</sup><br>γ = 1.4<br><br><br>T<sub>2</sub> = 997.26 K<br>p<sub>2</sub> = 7.097 MPa<br>T<sub>4</sub> = 2221.71 K<br>p<sub>4</sub> = 16.136 MPa<br>T<sub>5</sub> = 657.35 K<br>p<sub>5</sub> = 0.227 MPa<br><br>W = 0.5 kJ<br><br>P = 36 kW</p></html>");
        infoLegend.getEngine().loadContent("<html><style rel=\"stylesheet\" type=\"text/css\">html { background: transparent; margin: 0; padding: 5px; font-size: 1.05em; font-family: Roboto, Arial; line-height: 1.3em; } html * { background: transparent; margin: 0; padding: 0; line-height: 1.5em; }</style><p>V<sub>2</sub> - Oběm při expanzi [cm<sup>3</sup>]<br>V<sub>3</sub> - Oběm při compresi [cm<sup>3</sup>]<br>T<sub>2</sub> - Venkovní teplota [K]<br>p<sub>2</sub> - Atmosférický tlak [MPa]<br>Q - Výhřevnost paliva [kJ&times;kg<sup>-1</sup>]<br>f - Poměr vzduch/palivo<br>rpm - Otáčky za minutu [rpm]<br>n - Počet válců<br>η - Účinnost motoru<br>r - Kompresní poměr<br><br><br>c<sub>V</sub> - Měrná tepelná kapacita [kJ&times;kg<sup>-1</sup>&times;K<sup>-1</sup>]<br>ρ - Hustota vzduchu [kg&times;m<sup>-3</sup>]<br>γ - Poissonova konstana</p><html>");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
