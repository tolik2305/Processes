package sample.Controllers;

import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class InputPriority {

    public static int priority = 0;
    public static boolean isCancel;
    public Button btnCancel;
    public Button btnOk;
    public TextField txtPriority;

    public boolean OnClickedCancel(MouseEvent mouseEvent) {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
        return isCancel = true;
    }

    public boolean OnClickedOk(MouseEvent mouseEvent) {
        if(txtPriority.getText().matches("[0-9]{1,2}")&&
           Integer.parseInt(txtPriority.getText())<33&&
                Integer.parseInt(txtPriority.getText())>0){
            priority = Integer.parseInt(txtPriority.getText());
            OnClickedCancel(mouseEvent);
        }
        return isCancel = false;
    }
}
