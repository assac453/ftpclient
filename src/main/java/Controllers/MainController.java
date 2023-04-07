package Controllers;

import com.example.ftpclient.FTPFileExtended;
import com.example.ftpclient.Global.CallAlert;
import com.example.ftpclient.Global.Connection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.commons.net.ftp.FTPFile;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;

public class MainController {
    public static Stage mainWindow;
    @FXML    private Label statusLabel;
    @FXML    private Label systemTypeLabel;
    @FXML    private AnchorPane AnchorPane;
    @FXML    private TableView<Object> FilesTable;
    @FXML    private TableColumn<FTPFileExtended, String> typeCol;
    @FXML    private TableColumn<FTPFileExtended, String> nameCol;
    @FXML    private TableColumn<FTPFileExtended, Long> sizeCol;
    @FXML    private TableColumn<FTPFileExtended, Instant> dateCol;
    @FXML    private ListView<FTPFileExtended> ListDirectory;
    private ObservableList<Object> ftpFiles;
    @FXML    private Label filePath; // remote file path
    @FXML    private Label localDirectory;

    @FXML
    private void initialize() throws IOException {
        FilesTable.getSelectionModel().getSelectedItem();
        FilesTable.addEventFilter(MouseEvent.MOUSE_CLICKED,mouseEventEventListener);
        loadData();
    }
    private void loadData() throws IOException {
        filePath.setText(Connection.ftpClient.printWorkingDirectory());
        statusLabel.setText(Connection.ftpClient.getStatus());
        systemTypeLabel.setText(Connection.ftpClient.getSystemType());
        typeCol.setCellValueFactory(new PropertyValueFactory<FTPFileExtended, String>("typeStr"));
        nameCol.setCellValueFactory(new PropertyValueFactory<FTPFileExtended, String>("name"));
        sizeCol.setCellValueFactory(new PropertyValueFactory<FTPFileExtended, Long>("size"));
        dateCol.setCellValueFactory(new PropertyValueFactory<FTPFileExtended, Instant>("TimestampInstant"));
        ObservableList<FTPFile>  files = FXCollections.observableArrayList(Connection.ftpClient.listFiles());
        ftpFiles = FXCollections.observableArrayList(files.stream().map(FTPFileExtended::new).toArray());
        FilesTable.setItems(ftpFiles);
    }
    private final EventHandler<MouseEvent> mouseEventEventListener = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            if (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount()==2) {
                if (FilesTable.getSelectionModel().getSelectedItem() != null
                        && ((FTPFileExtended) FilesTable.getSelectionModel().getSelectedItem()).isDirectory()) {
                    try {
                        Connection.ftpClient.cwd("./" + ((FTPFile) FilesTable.getSelectionModel().getSelectedItem()).getName());
                        loadData();
                        System.out.println(Connection.ftpClient.printWorkingDirectory());
                        System.out.println(Arrays.toString(Connection.ftpClient.listDirectories()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            if (mouseEvent.getButton()==MouseButton.SECONDARY && mouseEvent.getClickCount()==1
                    && ((FTPFile) FilesTable.getSelectionModel().getSelectedItem()).isDirectory()){
                Stage SubStage = new Stage();
                SubStage.initOwner(((Node)mouseEvent.getSource()).getScene().getWindow());
                SubStage.initModality(Modality.WINDOW_MODAL);
                Label label = new Label("Enter template");
                TextField field = new TextField();
                Button button = new Button("Submit");
                button.setOnAction(e->{
                    if (localDirectory.getText().equals("Not choosen")){
                        CallAlert.Alert(Alert.AlertType.INFORMATION, "Информация", "Выберите директорию для сохранения файлов", e);
                    }else if (field.getText().isBlank()){
                        CallAlert.Alert(Alert.AlertType.INFORMATION, "Информация","Пустой шаблон заполнения", e);
                    }else{
                        try {
                            FtpDownloader.template = field.getText();
                            FtpDownloader.downloadDirectory(Connection.ftpClient,
                                    ((FTPFile) FilesTable.getSelectionModel().getSelectedItem()).getName()
                                    ,localDirectory.getText());
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        System.out.println(field.getText());
                        SubStage.close();
                    }
                });
                VBox box = new VBox(label, field, button);
                Scene SubScene = new Scene(box);
                SubStage.setTitle("Template type");
                SubStage.setScene(SubScene);
                SubStage.show();
            }
        }
    };
    @FXML
    private void UpDirectoryClickBTN(ActionEvent event) throws IOException {
        Connection.ftpClient.cwd("../");
        Connection.ftpClient.getStatus();
        Connection.ftpClient.getSystemType();
        loadData();
    }
    @FXML
    void ChooseLocalDirectoryBTN(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File("C:\\"));
        File selectedDirectory = directoryChooser.showDialog(mainWindow);
        localDirectory.setText(selectedDirectory!=null ? selectedDirectory.getAbsolutePath(): "Not choosen");
    }
}
