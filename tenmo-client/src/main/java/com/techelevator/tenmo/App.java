package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.*;
import org.springframework.web.client.RestClientResponseException;

import java.math.BigDecimal;
import java.util.Objects;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private final AccountService accountService = new AccountService();
    private final TransferService transferService = new TransferService();


    private AuthenticatedUser currentUser;

    public static void main(String[] args) throws AuthServiceException {
        App app = new App();
        app.run();
    }

    private void run() throws AuthServiceException {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }

    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() throws AuthServiceException {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

    private void viewCurrentBalance() throws AuthServiceException {
        String authToken = currentUser.getToken();
        BigDecimal balance = null;
        try {
            balance = accountService.getBalance(authToken);
            System.out.println("Your Current Account Balance Is: $" + balance);
        } catch (AuthServiceException e) {
            System.out.println(e.getMessage());

        }


    }

    private void viewTransferHistory() throws AuthServiceException {
        String authToken = currentUser.getToken();
        Accounts userAccount = accountService.getAccountByUserName(currentUser.getUser().getUsername(), authToken);
        Long userAccountId = userAccount.getAccountId();
        Transfers[] transferlist = transferService.getTransfersByAccount(userAccountId, authToken);
        System.out.println("------------------------------------------------");
        System.out.println(" TRANSFERS                                      ");
        System.out.println(" ID                From/To              Amount  ");
        System.out.println("------------------------------------------------");
        displayTransferList(transferlist);

        Long transferId = consoleService.promptForLong("Enter ID of transfer ID to view details (0 to cancel): ");
        if (transferId == 0) {
            mainMenu();
        } else {
            for (Transfers transfer : transferlist) {
                if (Objects.equals(transfer.getTransferId(), transferId)) {
                    formattedTransferDetails(transfer);
                }
            }
            /*transfers = transferService.getTransfersByTransferId(transferId, authToken);
            formattedTransferDetails(transfers);
            System.out.println(transfers.toString());*/
        }
    }


    private void viewPendingRequests() throws AuthServiceException {
        String authToken = currentUser.getToken();
        Accounts userAccount = accountService.getAccountByUserName(currentUser.getUser().getUsername(), authToken);
        Long userAccountId = userAccount.getAccountId();
        Transfers[] transferlist = transferService.getTransfersByAccount(userAccountId, authToken);

        System.out.println("------------------------------------------------");
        System.out.println(" PENDING TRANSFERS                              ");
        System.out.println(" ID                 To                  Amount  ");
        System.out.println("------------------------------------------------");
        displayPendingTransferList(transferlist, 1);

        Long transferId = consoleService.promptForLong("Enter ID of transfers ID to approve/reject (0 to cancel): ");
        if (transferId == 0) {
            mainMenu();
        } else {
            for (Transfers transfer : transferlist) {
                if (Objects.equals(transfer.getTransferId(), transferId)) {
                    formattedTransferDetails(transfer);
                    promptUserToAcceptOrReject();
                    int userChoice;
                    userChoice = consoleService.promptForInt("Please choose an option: ");
                    if (userChoice == 0) {
                        viewPendingRequests();
                    } else if (userChoice == 1) {
                        handleAcceptingRequests(transfer);
                    } else if (userChoice == 2) {
                        handleRejectingRequests(transfer);
                    }
                }
            }

        }
    }

    private void sendBucks() throws AuthServiceException {
        String authToken = currentUser.getToken();
        User[] userList = accountService.listAllUsers(authToken);
        Long newTransferId = transferService.getNewTransferId(authToken);
        System.out.println("------------------------------------------------");
        System.out.println(" USER ID              NAME                      ");
        System.out.println("------------------------------------------------");

        for (User user : userList) {
            System.out.println("  " + user.getId() + "               " + user.getUsername());
        }
        Long userId = consoleService.promptForLong("Enter ID of user you are sending to (0 to cancel): ");

        if (userId == 0) {
            mainMenu();
        } else {
            try {
                BigDecimal currentBalance = accountService.getBalance(authToken);
                BigDecimal amountEntered = consoleService.promptForBigDecimal("Enter amount: $");
                if (amountEntered.compareTo(currentBalance) > 0) {
                    System.out.println("Insufficient Balance to Make Transfer");
                } else {
                    Accounts fromAccount = accountService.getAccountByUserName(currentUser.getUser().getUsername(), authToken);
                    Long fromAccountId = fromAccount.getAccountId();
                    Accounts toAccount = accountService.getAccountsByUserId(userId, authToken);
                    Long toAccountId = toAccount.getAccountId();
                    int defaultTypeId = 2;
                    int defaultStatusId = 2;
                    Transfers newTransfers = createTransfer(newTransferId, defaultTypeId, defaultStatusId, fromAccountId, toAccountId, amountEntered);
                    transferService.sendTransfer(newTransfers.getTransferId(), newTransfers, authToken);
                    accountService.updateBalances(newTransfers.getTransferId(), newTransfers, authToken);

                    formattedTransferDetails(newTransfers);
                }
            } catch (RestClientResponseException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void requestBucks() throws AuthServiceException {
        String authToken = currentUser.getToken();
        User[] userList = accountService.listAllUsers(authToken);
        Long newTransferId = transferService.getNewTransferId(authToken);
        System.out.println("------------------------------------------------");
        System.out.println(" USER ID              NAME                      ");
        System.out.println("------------------------------------------------");

        for (User user : userList) {
            System.out.println("  " + user.getId() + "               " + user.getUsername());
        }
        Long userId = consoleService.promptForLong("Enter ID of user you are requesting from (0 to cancel): ");

        if (userId == 0) {
            mainMenu();
        } else {
            try {
                BigDecimal amountEntered = consoleService.promptForBigDecimal("Enter amount: $");
                Accounts fromAccount = accountService.getAccountsByUserId(userId, authToken);
                Long fromAccountId = fromAccount.getAccountId();
                Accounts toAccount = accountService.getAccountsByUserId(currentUser.getUser().getId(), authToken);
                Long toAccountId = toAccount.getAccountId();
                int typeRequest = 1;
                int defaultPending = 1;
                Transfers newTransfers = createTransfer(newTransferId, typeRequest, defaultPending, fromAccountId, toAccountId, amountEntered);
                transferService.sendTransfer(newTransfers.getTransferId(), newTransfers, authToken);
            } catch (AuthServiceException e) {
                e.printStackTrace();
            }
        }
    }

    public Transfers createTransfer(Long transferId, int typeId, int statusId, Long accountFrom, Long accountTo, BigDecimal amount) throws AuthServiceException {
        Transfers transfer = new Transfers();
        transfer.setTransferId(transferId);
        transfer.setTransferTypeId(typeId);
        transfer.setTransferStatusId(statusId);
        transfer.setAccountFrom(accountFrom);
        transfer.setAccountTo(accountTo);
        transfer.setAmount(amount);
        return transfer;
    }

    public void displayTransferList(Transfers[] transferlist) throws AuthServiceException {
        String authToken = currentUser.getToken();
        for (Transfers transfer : transferlist) {

            Long id = transfer.getTransferId();

            String toOrFrom = "";

            Accounts account = accountService.getAccountByUserName(currentUser.getUser().getUsername(), authToken);
            Long yourAccountId = account.getAccountId();
            if (transfer.getAccountFrom().equals(yourAccountId)) {

                toOrFrom = "To: " + accountService.getUsernameByAccountId(transfer.getAccountTo(), authToken);
            } else {
                toOrFrom = "From: " + accountService.getUsernameByAccountId(transfer.getAccountFrom(), authToken);
            }
            BigDecimal amount = transfer.getAmount();

            System.out.println(id + "               " + toOrFrom + "            $" + amount);


        }
    }

    public void displayPendingTransferList(Transfers[] transferlist, int statusID) throws AuthServiceException {
        String authToken = currentUser.getToken();
        for (Transfers transfer : transferlist) {
            if (transfer.getTransferStatusId() == statusID) {
                Accounts account = accountService.getAccountByUserName(currentUser.getUser().getUsername(), authToken);
                Long yourAccountId = account.getAccountId();
                Long id = transfer.getTransferId();

                String requester = "";

                if (transfer.getAccountTo() != yourAccountId) {
                    requester = accountService.getUsernameByAccountId(transfer.getAccountTo(), authToken);
                    BigDecimal amount = transfer.getAmount();
                    System.out.println(id + "               " + requester + "              $" + amount);
                } else {
                    System.out.println();
                }

            }
        }
    }

    public void handleAcceptingRequests(Transfers transfer) throws AuthServiceException {
        String authToken = currentUser.getToken();
        BigDecimal userBalance = accountService.getBalance(authToken);
        BigDecimal requestAmount = transfer.getAmount();
        int acceptId = 2;
        int approvedStatus = 2;
        if (requestAmount.compareTo(userBalance) > 0) {
            System.out.println("Insufficient funds to complete request");
        } else {
            Transfers approvedTransfer = null;
            approvedTransfer = createTransfer(transfer.getTransferId(), acceptId, approvedStatus,
                    transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());
            transferService.sendTransfer(transfer.getTransferId(), approvedTransfer, authToken);
            accountService.updateBalances(transfer.getTransferId(), approvedTransfer, authToken);
            System.out.println("You approved the request");
        }

    }

    public void handleRejectingRequests(Transfers transfer) throws AuthServiceException {
        String authToken = currentUser.getToken();
        int typeId = 1;
        int rejectedId = 1;
        Transfers rejectedTransfer = null;
        rejectedTransfer = createTransfer(transfer.getTransferId(), typeId, rejectedId,
                transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());
        transferService.sendTransfer(transfer.getTransferId(), rejectedTransfer, authToken);
        System.out.println("You rejected the request");


    }


    public void promptUserToAcceptOrReject() {
        System.out.println("1: Approve");
        System.out.println("2: Reject");
        System.out.println("0: Don't approve or reject");
        System.out.println("---------");

    }


    public void formattedTransferDetails(Transfers transferDetails) throws AuthServiceException {
        String authToken = currentUser.getToken();
        System.out.println();
        System.out.println("--------------------------------------------");
        System.out.println("Transfer Details");
        System.out.println("--------------------------------------------");
        System.out.println("Id: " + transferDetails.getTransferId());
        System.out.println("From: " + accountService.getUsernameByAccountId(transferDetails.getAccountFrom(), authToken));
        System.out.println("To: " + accountService.getUsernameByAccountId(transferDetails.getAccountTo(), authToken));
        System.out.println("Type: " + transferService.getTransferTypeDesc(transferDetails.getTransferStatusId(), authToken));
        System.out.println("Status: " + transferService.getTransferStatusDesc(transferDetails.getTransferStatusId(), authToken));
        System.out.println("Amount: $" + transferDetails.getAmount());
    }
}
