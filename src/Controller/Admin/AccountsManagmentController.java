/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller.Admin;


import Model.Accounts;
import Model.AccountsJpaController;
import Model.DB;
import Model.exceptions.NonexistentEntityException;
import View.ViewManager;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * FXML Controller class
 *
 * @author Yahya
 */
public class AccountsManagmentController implements Initializable {
    public static Accounts selectedAccountToUpdate;
    public static Stage updateStage;
    ObservableList<Accounts> accountsList = FXCollections.observableArrayList();
    @FXML
    private Button usersManagmentPageBtn;
    @FXML
    private Button accountsPageBtn;
    @FXML
    private Button operationsPageBtn;
    @FXML
    private Button createNewAccountrBtn;
    @FXML
    private Button showAllAccountsBtn;
    @FXML
    private Button updateSelectedAccountBtn;
    @FXML
    private Button deleteSelectedAccountBtn;
    @FXML
    private Button searchAccountBtn;
    @FXML
    private TextField accontSearchTF;
    @FXML
    private TableView<Accounts> AccountsTV;
    @FXML
    private TableColumn<Accounts, Integer> id_col;
    @FXML
    private TableColumn<Accounts, Integer> accountNumber_col;
    @FXML
    private TableColumn<Accounts, String> username_col;
    @FXML
    private TableColumn<Accounts, String> currency_col;
    @FXML
    private TableColumn<Accounts, Double> balance_col;
    @FXML
    private TableColumn<Accounts, Date> creationDate_col;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         id_col.setCellValueFactory(new PropertyValueFactory<>("id"));
    accountNumber_col.setCellValueFactory(new PropertyValueFactory<>("accountNumber"));
    username_col.setCellValueFactory(new PropertyValueFactory<>("username"));
    currency_col.setCellValueFactory(new PropertyValueFactory<>("currency"));
    balance_col.setCellValueFactory(new PropertyValueFactory<>("balance"));
    creationDate_col.setCellValueFactory(new PropertyValueFactory<>("creationDate"));
    }    

    @FXML
    private void showUsersManagmentPage(ActionEvent event) {
         ViewManager.adminPage.changeSceneToUsersManagment();
    }

    @FXML
    private void showAccountsPage(ActionEvent event) {
    }

    @FXML
    private void showOperationsPage(ActionEvent event) {
    }

    @FXML
    private void showAccountCreationPage(ActionEvent event) {
        View.ViewManager.adminPage.changeSceneToCreateAccount();
    }

    @FXML
    private void showAllAccounts(ActionEvent event) {
        accountsList.clear();
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("BankPU");
        AccountsJpaController accountsController = new AccountsJpaController(emf);
        List<Accounts> allAccounts = accountsController.findAccountsEntities();
        accountsList.addAll(allAccounts);
        AccountsTV.setItems(accountsList);
    }

    @FXML
    private void updateSelectedAccount(ActionEvent event) throws IOException {   
        if(AccountsTV.getSelectionModel().getSelectedItem() != null){
        selectedAccountToUpdate = AccountsTV.getSelectionModel().getSelectedItem();
        FXMLLoader loaderUpdate = new FXMLLoader(getClass().getResource("/View/AdminFXML/EditAccount.fxml"));
        Parent rootUpdate = loaderUpdate.load();     
        Scene updateAccountScene = new Scene(rootUpdate); 
        updateStage = new Stage();
        updateStage.setScene(updateAccountScene);
        updateStage.setTitle("Update Account " + selectedAccountToUpdate.getAccountNumber() );
        updateStage.show();
        }
    }

    @FXML
    private void deleteSelectedAccount(ActionEvent event) throws NonexistentEntityException {
        if(AccountsTV.getSelectionModel().getSelectedItem() != null){
            Alert deleteConfirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            deleteConfirmAlert.setTitle("Account delete");
            deleteConfirmAlert.setContentText("Are you sure to delete this Account ?");
            deleteConfirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
       EntityManagerFactory emf = Persistence.createEntityManagerFactory("BankPU");
       AccountsJpaController accountsController = new AccountsJpaController(emf);
        Accounts selectedAccount = AccountsTV.getSelectionModel().getSelectedItem();

    if (selectedAccount != null) {
        
        int AccountId = selectedAccount.getId();

       
        

        try {
           
            accountsController.destroy(AccountId);
            System.out.println("Account with ID " + AccountId + " has been deleted.");
            
        } catch (NonexistentEntityException e) {
            System.out.println("Error deleting Account: " + e.getMessage());
        }
    } else {
        System.out.println("No Account selected.");
    }
      }
            });
              }else{
        Alert warnAlert = new Alert(Alert.AlertType.WARNING);
        warnAlert.setTitle("Select an user");
        warnAlert.setContentText("Please select an account from the table view");
        warnAlert.show();  
        }

    }

   @FXML
    private void searchForAnAccount(ActionEvent event) throws SQLException {
        if(!(accontSearchTF.getText().isEmpty())){
    accountsList.clear();
    String accNumber = accontSearchTF.getText();
       
    Connection conn = DB.getInstance().getConnection();
    PreparedStatement stmt;
    ResultSet rs;   
       String sql = "SELECT * FROM accounts WHERE account_number LIKE ?";
       stmt = conn.prepareStatement(sql);
       stmt.setString(1, "%" + accNumber + "%");
        rs = stmt.executeQuery();
        if (rs.next()) {
        int id = rs.getInt("id");
        int userId = rs.getInt("user_id");
        int accountNumber = rs.getInt("account_number");
        String username = rs.getString("username");
        String currency = rs.getString("currency");
        double balance = rs.getDouble("balance");
        String creationDate = rs.getString("creation_date");
        Accounts a = new Accounts(id,userId, accountNumber, username, currency, balance, creationDate);
        accountsList.add(a);
        AccountsTV.setItems(accountsList);
        conn.close();
        stmt.close();
        rs.close();
        } else {
           accountsList.clear();
           System.out.println("Account not found.");
        }
}

    }




    
}