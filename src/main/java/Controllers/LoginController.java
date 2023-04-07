package Controllers;


import com.example.ftpclient.Global.CallAlert;
import com.example.ftpclient.Global.Connection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    public static Stage loginStage;

    @FXML
    private TextField Hostname;

    @FXML
    private TextField Password;

    @FXML
    private TextField Port;

    @FXML
    private TextField Username;

    @FXML
    private Button ConnectBTN;

    @FXML
    private void OnClickConnectBTN(ActionEvent event) throws IOException {
        if (Hostname.getText().isBlank()){
            CallAlert.Alert(Alert.AlertType.ERROR, "Пустая строка", "Поле hostname пустое.", event);
            return;
        } else if (Port.getText().isBlank()) {
            CallAlert.Alert(Alert.AlertType.ERROR, "Пустая строка", "Поле port пустое.", event);
            return;
        } else if (Username.getText().isBlank()) {
            CallAlert.Alert(Alert.AlertType.ERROR, "Пустая строка", "Поле username пустое.", event);
            return;
        }

        if (!Port.getText().isBlank()){
            try{
                Integer.parseInt(Port.getText());
            }
            catch (NumberFormatException ex){
                CallAlert.Alert(Alert.AlertType.ERROR,"Ошибка", "Проверьте значение port. Ожидается целое число", event);
                return;
            }
        }

        try {
            Connection.ftpClient.connect(Hostname.getText(), Integer.parseInt(Port.getText()));
            Connection.ftpClient.login(Username.getText(),Password.getText());
        } catch (Exception socketException){
            CallAlert.Alert(Alert.AlertType.ERROR,"Ошибка при подключении", socketException.getMessage(), event);
            return;
        }
        System.out.println(Connection.ftpClient.getStatus());
        if (Connection.ftpClient.getStatus()==null){
            CallAlert.Alert(Alert.AlertType.ERROR, "Ошибка", "Подключиться не удалось. \nПроверьте корректность username и password", event);
            Connection.ftpClient.logout();
            Connection.ftpClient.disconnect();
        }
        if (Connection.ftpClient.isConnected()&& Connection.ftpClient.getStatus()!=null){
            System.out.println("Connection success");
            CallAlert.Alert(Alert.AlertType.INFORMATION, "Подключение", "Подключение произошло успешно", event);
            loginStage.hide();
            Stage MainStage = new Stage();
            MainController.mainWindow = MainStage;
            FXMLLoader MainStageLoaderLoad = new FXMLLoader(getClass().getResource("/com/example/ftpclient/MainWindow.fxml"));
            Scene MainScene = new Scene(MainStageLoaderLoad.load());
            MainStage.setOnHiding(e -> {
                System.out.println("Hello");
                Connection.CloseAndDisconnect();
                loginStage.show();
            });
            MainStage.setTitle("Проводник");
            MainStage.setScene(MainScene);
            MainStage.show();
        }
        else {return;}
    }
}
