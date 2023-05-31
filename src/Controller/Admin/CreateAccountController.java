/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller.Admin;

import Model.Accounts;
import Model.AccountsJpaController;
import View.ViewManager;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * FXML Controller class
 *
 * @author PC-ASUS
 */
public class CreateAccountController implements Initializable {

    @FXML
    private Button usersManagmentPageBtn;
    @FXML
    private Button accountsPageBtn;
    @FXML
    private Button operationsPageBtn;
    @FXML
    private Button saveNewAccountBTN;
    @FXML
    private Button cancelBtn;
    @FXML
    private TextField userIdTF;
    @FXML
    private TextField accountNumberTF;
    @FXML
    private TextField usernameTF;
    @FXML
    private TextField currencyTF;
    @FXML
    private TextField balanceTF;
    @FXML
    private TextField creationDateTF;
    
    

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void showUsersManagmentPage(ActionEvent event) {
        ViewManager.adminPage.changeSceneToUsersManagment();
    }

    @FXML
    private void showAccountsPage(ActionEvent event) {
        ViewManager.adminPage.changeSceneToAccountsManagment();
    }

    @FXML
    private void showOperationsPage(ActionEvent event) {
    }

    @FXML
    private void saveNewAccount(ActionEvent event) {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("BankPU");
    AccountsJpaController accountsController =  new AccountsJpaController(emf);    
    int userId = Integer.parseInt(userIdTF.getText());
    int accountNumber = Integer.parseInt(accountNumberTF.getText());
    String username = usernameTF.getText();
    String currency = currencyTF.getText();
    double balance = Double.parseDouble(balanceTF.getText());
    String creationDate = creationDateTF.getText();
    Accounts a =  new Accounts(userId, accountNumber, username, currency, balance, creationDate);
    accountsController.create(a);
        
        ViewManager.adminPage.changeSceneToAccountsManagment();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Account inserted");
        alert.setContentText("Account inserted");
        alert.showAndWait();
    }

    @FXML
    private void cancelAccountCreation(ActionEvent event) {
        ViewManager.adminPage.changeSceneToAccountsManagment();
    }
 
    
    
    
    
    
    
    
}
