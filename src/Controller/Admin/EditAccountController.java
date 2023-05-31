/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller.Admin;

import Model.Accounts;
import Model.AccountsJpaController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * FXML Controller class
 *
 * @author PC-ASUS
 */
public class EditAccountController implements Initializable {
    private Accounts oldAccount;
    @FXML
    private Button cancelBtn;
    @FXML
    private Button saveNewAccountBTN;
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
        this.oldAccount = AccountsManagmentController.selectedAccountToUpdate;
        userIdTF.setText(String.valueOf(oldAccount.getUserId()));
        accountNumberTF.setText(String.valueOf(oldAccount.getAccountNumber()));
        usernameTF.setText(oldAccount.getUsername());
        currencyTF.setText(oldAccount.getCurrency());
        balanceTF.setText(String.valueOf(oldAccount.getBalance()));
        creationDateTF.setText(oldAccount.getCreationDate());
        
    }    

    @FXML
    private void saveNewAccount(ActionEvent event) {
        int userId = Integer.parseInt(userIdTF.getText());
        int accountNumber =  Integer.parseInt(accountNumberTF.getText());
        String username = usernameTF.getText();
        String currenncy = currencyTF.getText();
        double balance = Double.parseDouble(balanceTF.getText());
        String creationDate = creationDateTF.getText();
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("BankPU");
        AccountsJpaController accController = new AccountsJpaController(emf);
            oldAccount = accController.findAccounts(oldAccount.getId());
        if (oldAccount != null) {
           oldAccount.setUserId(userId);
           oldAccount.setAccountNumber(accountNumber);
           oldAccount.setUsername(username);
           oldAccount.setCurrency(currenncy);
           oldAccount.setBalance(balance);
           oldAccount.setCreationDate(creationDate);
              try {
            accController.edit(oldAccount);
            oldAccount = accController.getEntityManager().merge(oldAccount);
            System.out.println("Account information updated successfully.");
        } catch (Exception ex) {
            System.out.println("Error updating account information: " + ex.getMessage());
        }
        }else {
        System.out.println("Account not found.");
    }
        
    }

    @FXML
    private void cancelAccountCreation(ActionEvent event) {
        Controller.Admin.AccountsManagmentController.updateStage.close(); 
    }
    
}
