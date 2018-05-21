package komis;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Controller {
    @FXML
    TextArea txtResult;
    @FXML
    TextArea txtMedium;
    @FXML
    TextArea txtNumber;
    @FXML
    TextArea txtConsole;
    @FXML
    ChoiceBox boxSelect;
    @FXML
    ChoiceBox boxModify;
    @FXML
    ChoiceBox boxOperation;
    @FXML
    TextField txt1, txt2, txt3, txt4, txt5, txt6;
    @FXML
    Label lbl1, lbl2, lbl3, lbl4, lbl5, lbl6;
    @FXML
    Button btnApply;

    private KomisDAO DAO = null;
    private int operation = 0; //[1]Insert [2]Update [3]Delete

    @FXML
    private void initialize() {
        try {
            System.out.println("tutaj");
            DAO = new KomisDAO();
            DAO.connect();

            boxSelect.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    selectEvent(newValue.intValue());
                }
            });

            boxModify.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    modifyEvent(newValue.intValue());
                }
            });

            boxOperation.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    operationEvent(newValue.intValue());
                }
            });

            average();
            count();


        } catch (SQLException e) {
            System.out.println("Failed to connect database: " + e.getMessage());
        }
    }

    private void operationEvent(int choice) {
        operation = choice + 1;
        if (choice == 1 || choice == 2) { //UPDATE lub DELETE
            lbl1.setTextFill(Color.web("#FF0000"));
            txtConsole.setText("Wyszukiwanie po kluczu głównym\nAtrybut wymagany zanzaczony na czerwono");
        }
        if (choice == 2) //DELETE
        {
            textDisable(false, true, true, true, true, true);
            lbl2.setText("[Nieaktywny]");
            lbl3.setText("[Nieaktywny]");
            lbl4.setText("[Nieaktywny]");
            lbl5.setText("[Nieaktywny]");
            lbl6.setText("[Nieaktywny]");

        }
        if (boxModify.getSelectionModel().getSelectedItem() != null)
            btnApply.setDisable(false);
    }

    private void modifyEvent(int choice) {
        switch (choice) {
            case 0:
            case 1:
                textDisable(false, false, false, true, true, true);
                lbl1.setText("PESEL: ");
                lbl2.setText("Imię: ");
                lbl3.setText("Nazwisko: ");
                lbl4.setText("[Nieaktywny]");
                lbl5.setText("[Nieaktywny]");
                lbl6.setText("[Nieaktywny]");
                break;

            case 2:
                textDisable(false, false, false, true, true, true);
                lbl1.setText("ID Dostawcy: ");
                lbl2.setText("Nazwa firmy: ");
                lbl3.setText("Lokalizacja: ");
                lbl4.setText("[Nieaktywny]");
                lbl5.setText("[Nieaktywny]");
                lbl6.setText("[Nieaktywny]");
                break;

            case 3:
                textDisable(false, false, false, false, true, true);
                lbl1.setText("Numer miejsca: ");
                lbl2.setText("Rząd: ");
                lbl3.setText("Kolumna: ");
                lbl4.setText("Nr rejestracyjny:");
                lbl5.setText("[Nieaktywny]");
                lbl6.setText("[Nieaktywny]");
                break;

            case 4:
                textDisable(false, false, false, false, false, false);
                lbl1.setText("Nr rejestracyjny: ");
                lbl2.setText("Marka: ");
                lbl3.setText("Model: ");
                lbl4.setText("Cena:");
                lbl5.setText("Kupujący_PESEL:");
                lbl6.setText("Sprzedający_PESEL:");
                break;


        }
        boxOperation.setDisable(false);

    }

    private void selectEvent(int choice) {
        StringBuffer results = DAO.select(choice);
        if (results == null) {
            txtConsole.clear();
            txtConsole.setText("Błąd przy pobraniu danych z bazy!");
            return;
        }
        String output = results.toString();

        txtResult.setText(output);

    }

    @FXML
    private void btnApply_clicked() {
        int operation = boxOperation.getSelectionModel().getSelectedIndex();
        int table = boxModify.getSelectionModel().getSelectedIndex();
        String[] parameters = new String[6];
        parameters[0] = txt1.getText();
        parameters[1] = txt2.getText();
        parameters[2] = txt3.getText();
        parameters[3] = txt4.getText();
        parameters[4] = txt5.getText();
        parameters[5] = txt6.getText();
        boolean result = false;
        switch (operation) {
            case 0:
                result = DAO.insertHandler(table, parameters);
                if (result)
                    txtConsole.setText("Dodano rekord");
                break;
            case 1:
                result = DAO.updateHandler(table, parameters);
                if (result)
                    txtConsole.setText("Zaktualizowano rekord");
                break;
            case 2:
                result = DAO.deleteHandler(table, parameters);
                if (result)
                    txtConsole.setText("Usunięto rekord");
                break;
        }
        average();
        count();
        if (!result)
            txtConsole.setText("Błąd przy operacji!\nSprawdź poprawność danych");


    }

    private void average()
    {
        int average = DAO.average();
        txtMedium.setText(Integer.toString(average));
    }

    private void count()
    {
        int sum = DAO.count();
        txtNumber.setText(Integer.toString(sum));


    }


    private void textDisable(boolean a, boolean b, boolean c, boolean d, boolean e, boolean f) {
        txt1.setEditable(!a);
        txt1.clear();
        if(boxOperation.getSelectionModel().getSelectedIndex()==2)
            return;
        txt2.setEditable(!b);
        txt2.clear();
        txt3.setEditable(!c);
        txt3.clear();
        txt4.setEditable(!d);
        txt4.clear();
        txt5.setEditable(!e);
        txt5.clear();
        txt6.setEditable(!f);
        txt6.clear();

    }
}

