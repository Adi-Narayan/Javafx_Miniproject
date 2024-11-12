package oop;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import security.PasswordHashing;
import javafx.scene.shape.Circle;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.*;
import java.util.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;

public class Main extends Application implements BackgroundLoader
{
    private User[] users;
    private int userCount;
    private int[] cattleCount;
    private Random random = new Random();
    private String[] uid_list = new String[1000];
    private int uid_count;

    // Constructor
    public Main() {
        this.users = new User[100];  // Maximum 100 users
        this.userCount = 0;
        this.cattleCount = new int[100];
        for(int k = 0; k < 100; k++)
            this.cattleCount[k] = -1;
        this.uid_count = 0;
        this.uid_list[0] = "";
    }

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Cattle Management System");

        // Set the application icon
        Image icon = new Image(new File("C:\\Users\\newpassword\\Desktop\\OOP miniproject 2nd yr\\" +
                "oop\\src\\main\\java\\images\\Icon.png").toURI().toString());
        primaryStage.getIcons().add(icon);

        StackPane root = new StackPane();
        setBackgroundImage(root, "C:\\Users\\newpassword\\Desktop\\OOP miniproject 2nd yr\\oop\\src\\main\\java\\images\\background.png");

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        showLoginScene(primaryStage);
    }

    private void showLoginScene(Stage stage) {
        // Create layout
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        // Add controls
        Label usernameLabel = new Label("Username:");
        usernameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        grid.add(usernameLabel, 0, 0);

        TextField usernameField = new TextField();
        grid.add(usernameField, 1, 0);

        Label passwordLabel = new Label("Password:");
        passwordLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        grid.add(passwordLabel, 0, 1);

        PasswordField passwordField = new PasswordField();
        grid.add(passwordField, 1, 1);

        Button loginBtn = new Button("Login");
        Button registerBtn = new Button("New User? Register");

        HBox hbButtons = new HBox(10);
        hbButtons.setAlignment(Pos.CENTER);
        hbButtons.getChildren().addAll(loginBtn, registerBtn);
        grid.add(hbButtons, 1, 3);

        // Login button action
        loginBtn.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (checkSum(stage, username, password) == 1)
            {
                int i;
                for(i = 0; i < userCount; i++)
                {
                    if(users[i].getUsername().equals(username))
                        break;
                }
                if(cattleCount[i] > 0)
                {
                    Tabs(stage, username);
                }
                else
                {
                    cattleDetails(stage, username);
                }
            }
            else if(checkSum(stage, username, password) == 2)
            {
                AdminPage(stage, username);
            }
            else
            {
                showAlert("Login Failed", "Invalid username or password!");
            }
        });

        // Register button action
        registerBtn.setOnAction(e -> showRegistrationScene(stage));

        setBackgroundImageGRID(grid, "C:\\Users\\newpassword\\Desktop\\OOP miniproject 2nd yr\\oop\\src\\main\\java\\images\\background.png");

        Scene scene = new Scene(grid, 400, 300);
        stage.setScene(scene);
        stage.show();
    }

    private void Tabs(Stage stage, String username) {
        int k;
        for(k = 0; k < userCount; k++) {
            if(users[k].getUsername().equals(username))
                break;
        }

        if(k == userCount) {
            showAlert("Error", "No such user exists!");
            AdminPage(stage, username);
        } else {
            VBox mainContainer = new VBox(20);
            mainContainer.setPadding(new Insets(20));

            Label titleLabel = new Label("All Cattle Details");
            titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

            TableView<Cattle> tableView = new TableView<>();

            TableColumn<Cattle, String> idColumn = new TableColumn<>("Cattle ID");
            idColumn.setCellValueFactory(new PropertyValueFactory<>("UID"));

            TableColumn<Cattle, String> breedColumn = new TableColumn<>("Breed");
            breedColumn.setCellValueFactory(new PropertyValueFactory<>("breed"));

            TableColumn<Cattle, Double> weightColumn = new TableColumn<>("Weight");
            weightColumn.setCellValueFactory(new PropertyValueFactory<>("weight"));

            TableColumn<Cattle, Integer> ageColumn = new TableColumn<>("Age");
            ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));

            TableColumn<Cattle, String> healthColumn = new TableColumn<>("Health Status");
            healthColumn.setCellValueFactory(new PropertyValueFactory<>("health"));

            idColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.2));
            breedColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.2));
            weightColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.2));
            ageColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.2));
            healthColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.2));

            ObservableList<Cattle> cattleList = FXCollections.observableArrayList();
            List<Cattle> userCattle = Cattle.getCattleForUser(username);
            cattleList.addAll(userCattle);

            tableView.getColumns().addAll(idColumn, breedColumn, weightColumn, ageColumn, healthColumn);
            tableView.setItems(cattleList);

            Button logoutbtn = new Button("Logout");
            logoutbtn.setOnAction(e -> showLoginScene(stage));

            mainContainer.getChildren().addAll(titleLabel, tableView, logoutbtn);

            setBackgroundImageVBOX(mainContainer, "C:\\Users\\newpassword\\Desktop\\OOP miniproject 2nd yr\\oop\\src\\main\\java\\images\\background.png");

            Scene scene = new Scene(mainContainer, 800, 600);
            stage.setScene(scene);
            stage.setTitle("Cattle Details");
            stage.show();
        }
    }

    private void showRegistrationScene(Stage stage) {
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(25, 25, 25, 25));

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        TextField emailField = new TextField();
        emailField.setPromptText("Farm Location");

        Button registerBtn = new Button("Register");
        Button backBtn = new Button("Back to Login");

        // Register button action
        registerBtn.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            String email = emailField.getText();

            if (registerUser(username, password, email)) {
                showAlert("Success", "Registration successful!");
                showLoginScene(stage);
            } else {
                showAlert("Error", "Registration failed. Username might be taken or reached maximum users.");
            }
        });

        Label reg = new Label("Registration");
        reg.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        backBtn.setOnAction(e -> showLoginScene(stage));

        layout.getChildren().addAll(
                reg,
                usernameField,
                passwordField,
                emailField,
                registerBtn,
                backBtn
        );
        setBackgroundImageVBOX(layout, "C:\\Users\\newpassword\\Desktop\\OOP miniproject 2nd yr\\oop\\src\\main\\java\\images\\background.png");


        Scene scene = new Scene(layout, 400, 300);
        stage.setScene(scene);
    }

    private void AdminPage(Stage stage, String username) {
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(25, 25, 25, 25));

        Button Encrypt = new Button("Encrypt Passwords");
        Button Decrypt = new Button("Decrypt Passwords");
        TextField desc = new TextField();
        desc.setPromptText("Farmer Username");
        Label DESC = new Label("Enter farmer username to check details");
        DESC.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        DESC.setStyle("-fx-text-fill: white;");


        HBox bot = new HBox(Encrypt, Decrypt);
        bot.setAlignment(Pos.CENTER);
        bot.setPadding(new Insets(10));
        HBox.setMargin(Encrypt, new Insets(0, 10, 0, 10));
        HBox.setMargin(Decrypt, new Insets(0, 10, 0, 10));

        Button go = new Button("Go");

        go.setOnAction(e ->
        {
            Tabs(stage, desc.getText());
        });

        try {
            Image img1 = new Image(new File("C:\\Users\\newpassword\\Desktop\\OOP miniproject 2nd yr\\oop\\" +
                    "src\\main\\java\\images\\go.png").toURI().toString());
            ImageView view1 = new ImageView(img1);
            view1.setFitHeight(30);
            view1.setFitWidth(30);
            Circle clip = new Circle(15, 15, 15);
            view1.setClip(clip);
            go.setGraphic(view1);
        } catch (NullPointerException e) {
            System.out.println("Generate button Icon cannot be displayed");
        }

        Encrypt.setOnAction(e -> {
            PasswordHashing.init(users, users, userCount);
        });

        Decrypt.setOnAction(e -> {
            System.out.println("Usernames \t Passwords \t Farm Location");
            for(int i = 0; i < userCount; i++)
                System.out.println(users[i].getUsername() + " \t " + users[i].getPassword() + " \t " + users[i].getEmail());
        });

        layout.getChildren().addAll(bot, DESC, desc, go);
        setBackgroundImageVBOX(layout, "C:\\Users\\newpassword\\Desktop\\OOP miniproject 2nd yr\\oop\\src\\main\\java\\images\\background.png");

        Scene scene = new Scene(layout, 400, 300);
        stage.setScene(scene);
    }

    private void cattleDetails(Stage stage, String username) {
        String b = "Male";
        TextField CattleAge = new TextField();
        CattleAge.setPromptText("Cattle Age");
        TextField Cattle_weight = new TextField();
        Cattle_weight.setPromptText("Enter cattle weight");

        TextField Cattleuid = new TextField();
        Cattleuid.setPromptText("Press Button to Generate Cattle UID");
        Cattleuid.setEditable(false);

        ToggleGroup genderGroup = new ToggleGroup();

        RadioButton maleRB = new RadioButton("Male");
        RadioButton femaleRB = new RadioButton("Female");
        maleRB.setSelected(true);

        maleRB.setToggleGroup(genderGroup);
        femaleRB.setToggleGroup(genderGroup);

        if (maleRB.isSelected())
            b = "Male";
        if (femaleRB.isSelected())
            b = "Female";

        Label gender = new Label(b);
        genderGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            RadioButton selected = (RadioButton) newValue;
            System.out.println("Selected: " + selected.getText());
        });

        CheckBox HealthCert = new CheckBox("Vaccinated");
        HealthCert.setSelected(false);
        HealthCert.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        HealthCert.setStyle("-fx-text-fill: white;");

        Button save = new Button("Save");
        Button logoutBtn = new Button("Logout");
        Button contin = new Button("Continue");
        HBox but = new HBox(logoutBtn, save, contin);

        Button generateButton = new Button();
        generateButton.setMinSize(30, 30);
        generateButton.setMaxSize(30, 30);
        generateButton.setStyle(
                "-fx-background-radius: 15;" +
                        "-fx-min-width: 30px;" +
                        "-fx-min-height: 30px;" +
                        "-fx-max-width: 30px;" +
                        "-fx-max-height: 30px;" +
                        "-fx-background-insets: 0;" +
                        "-fx-padding: 0;"
        );

        try {
            Image img1 = new Image(new File("C:\\Users\\newpassword\\Desktop\\OOP miniproject 2nd yr\\oop\\" +
                    "src\\main\\java\\images\\restartbtn.png").toURI().toString());
            ImageView view1 = new ImageView(img1);
            view1.setFitHeight(30);
            view1.setFitWidth(30);
            Circle clip = new Circle(15, 15, 15); // (centerX, centerY, radius)
            view1.setClip(clip);
            generateButton.setGraphic(view1);
        } catch (NullPointerException e) {
            System.out.println("Generate button Icon cannot be displayed");
        }

        logoutBtn.setOnAction(e -> showLoginScene(stage));

        generateButton.setOnAction(e ->
        {
            int j;
            String prefix = String.valueOf((char) ('A' + random.nextInt(26)));
            String p = prefix + String.format("%06d", random.nextInt(1000000));
            for (j = 0; j <= uid_count; j++)
            {
                if (p.equals(uid_list[j]))
                    break;
            }
            if (j == (uid_count + 1))
            {
                uid_count = uid_count + 1;
                uid_list[uid_count] = p;
                System.out.println(uid_count);
                System.out.println(uid_list[uid_count]);
                Cattleuid.setText(p);
            }
        });

        save.setOnAction(event ->
        {
            try {
                int i;
                for(i = 0; i < userCount; i++)
                {
                    if(users[i].getUsername().equals(username))
                        break;
                }
                addCattleForUser(username, gender.getText(), Cattle_weight.getText(), CattleAge.getText(),
                        Cattleuid.getText(), HealthCert.isSelected(), i);
            }
            catch(NumberFormatException exc)
            {
                showAlert("Error", "Enter a valid number!");
            }
            Cattle_weight.clear();
            Cattleuid.clear();
            CattleAge.clear();
            HealthCert.setSelected(false);
            if (femaleRB.isSelected())
            {
                maleRB.setSelected(true);
                femaleRB.setSelected(false);
            }
        });

        contin.setOnAction(e -> policies(stage, username));

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.add(Cattleuid, 0, 0);
        grid.add(generateButton, 1, 0);
        grid.add(maleRB, 0, 1);
        grid.add(femaleRB, 1, 1);
        grid.add(CattleAge, 0, 2);
        grid.add(Cattle_weight, 0, 3);
        grid.add(HealthCert, 0, 4);
        grid.add(but, 0, 5);

        setBackgroundImageGRID(grid, "C:\\Users\\newpassword\\Desktop\\OOP miniproject 2nd yr\\oop\\" +
                "src\\main\\java\\images\\background.png");

        Scene scene = new Scene(grid, 400, 300);
        stage.setScene(scene);
        stage.show();

    }

    private void showPremiumCalculation(Stage stage, String username, String policyType, double sumInsured,
                                        String purpose, int claimHistory, int i) {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        Cattle userCattle = null;
        if (users[i] == null) {
            showAlert("Error", "Cattle information not found!");
            return;
        }

        if (users[i] != null && users[i].getUsername().equals(username) && users[i] instanceof Cattle) {
            userCattle = (Cattle) users[i];
        }
        
        TextField ask = new TextField();
        ask.setPromptText("Enter Cattle UID");


        assert userCattle != null;
        CattleInsurancePremiumCalculator.InsurancePolicy policy = new CattleInsurancePremiumCalculator.InsurancePolicy(
                username, users[i].getPassword(), users[i].getEmail(),
                userCattle.getBreed(), userCattle.getAge(), userCattle.getWeight(),
                userCattle.getUID(), policyType, sumInsured, userCattle.getHealth(),
                purpose, claimHistory
        );

        CattleInsurancePremiumCalculator calculator = new CattleInsurancePremiumCalculator();
        CattleInsurancePremiumCalculator.PremiumCalculationResult result = calculator.calculatePremium(policy);

        // Create labels with premium calculation details
        Label titleLabel = new Label("Insurance Premium Calculation");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        GridPane detailsGrid = new GridPane();
        detailsGrid.setHgap(10);
        detailsGrid.setVgap(10);
        detailsGrid.setAlignment(Pos.CENTER);

        // Add calculation details to the grid
        int row = 0;
        addLabelPair(detailsGrid, "Base Premium:", String.format("₹%.2f", result.basePremium), row++);
        addLabelPair(detailsGrid, "Policy Type:", policyType, row++);
        addLabelPair(detailsGrid, "Sum Insured:", String.format("₹%.2f", sumInsured), row++);

        // Add adjustment factors
        Label adjustmentsTitle = new Label("Risk Adjustment Factors:");
        adjustmentsTitle.setStyle("-fx-font-weight: bold");
        detailsGrid.add(adjustmentsTitle, 0, row++, 2, 1);

        for (Map.Entry<String, Double> factor : result.adjustmentFactors.entrySet()) {
            addLabelPair(detailsGrid, factor.getKey() + " Factor:",
                    String.format("%.2f", factor.getValue()), row++);
        }

        // Add final calculations
        addLabelPair(detailsGrid, "Adjusted Premium:", String.format("₹%.2f", result.adjustedPremium), row++);
        addLabelPair(detailsGrid, "GST (18%):", String.format("₹%.2f", result.gst), row++);

        Label totalLabel = new Label("Total Premium:");
        totalLabel.setStyle("-fx-font-weight: bold");
        Label totalAmount = new Label(String.format("₹%.2f", result.totalAmount));
        totalAmount.setStyle("-fx-font-weight: bold");
        detailsGrid.add(totalLabel, 0, row);
        detailsGrid.add(totalAmount, 1, row);

        // Create buttons
        Button backButton = new Button("Back");
        Button logoutButton = new Button("Logout");
        Button downloadButton = new Button("Download Quote");

        backButton.setOnAction(e -> policies(stage, username));
        logoutButton.setOnAction(e -> showLoginScene(stage));
        downloadButton.setOnAction(e -> {

            showAlert("Success", "Quote downloaded successfully!");
        });

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(backButton, downloadButton, logoutButton);

        // Add all components to the root
        root.getChildren().addAll(titleLabel, detailsGrid, buttonBox);

        // Set up the scene
        setBackgroundImageVBOX(root, "C:\\Users\\newpassword\\Desktop\\OOP miniproject 2nd yr\\oop\\src\\main\\java\\images\\background.png");
        Scene scene = new Scene(root, 600, 800);
        stage.setScene(scene);
        stage.show();
    }

    private void addLabelPair(GridPane grid, String labelText, String valueText, int row) {
        Label label = new Label(labelText);
        Label value = new Label(valueText);
        label.setStyle("-fx-text-fill: white;");
        value.setStyle("-fx-text-fill: white;");
        grid.add(label, 0, row);
        grid.add(value, 1, row);
    }

    private void policies(Stage stage, String username)
    {

        VBox root = new VBox(10);  // 10 is spacing between elements
        root.setPadding(new Insets(15));

        HBox cb1 = createCheckboxWithInfo("New India Assurance",
                "New India Assurance offers cattle insurance as part of its coverage for livestock,\n" +
                        " helping farmers manage risks related to cattle health, theft, and accidents. \nThis " +
                        "supports cattle management by providing financial protection and \npromoting sustainable " +
                        "livestock practices.");

        HBox cb2 = createCheckboxWithInfo("United India Insurance",
                "United India Insurance offers livestock insurance that provides financial protection \n" +
                        " for cattle owners, covering losses due to cattle death, accidents, or diseases. \n " +
                        "This insurance helps farmers manage risks, ensuring compensation\n" +
                        "for losses and support for veterinary expenses, which contributes to \n " +
                        "the sustainability and profitability of cattle farming.");

        HBox cb3 = createCheckboxWithInfo("Oriental Insurance",
                "Oriental Insurance provides livestock insurance that safeguards cattle owners \n " +
                        "against financial losses from cattle death, accidents, or diseases. This \n" +
                        "insurance supports farmers by covering risks associated with cattle rearing, helping \n" +
                        " ensure stability and reducing the financial impact of unexpected cattle health issues.");

        HBox cb4 = createCheckboxWithInfo("Livestock Insurance Scheme (LIS)",
                "The Livestock Insurance Scheme is designed to protect cattle owners from financial \n " +
                        "losses due to cattle mortality caused by accidents, disease, or natural calamities.\n " +
                        "It provides a safety net, helping farmers manage risks and maintain economic stability\n" +
                        "by covering the costs of lost or deceased animals.");

        HBox cb5 = createCheckboxWithInfo("Integrated Rural Development Program (IRDP) Insurance\n",
                "Provides low-cost insurance coverage to rural families below the poverty line \n under" +
                        " the Integrated Rural Development Program, protecting them against financial \n loss due" +
                        " to death or loss of assets. This insurance aims to support economic resilience \nfor rural" +
                        " households involved in self-employment and income-generating \nactivities.");

        HBox cb6 = createCheckboxWithInfo("National Livestock Mission",
                "An Initiative by the Government of India to improve the productivity and \n sustainability" +
                        " of the livestock sector, focusing on small farmers and pastoralists. It supports \n the " +
                        "development of animal husbandry, sustainable feed resources, breed improvement, \n and " +
                        "livestock health, thereby enhancing rural income and employment opportunities.");

        HBox cb7 = createCheckboxWithInfo("Rashtriya Gokul Mission insurance provisions",
                "Insurance provisions to protect indigenous cattle owners against financial \n losses from" +
                        " the death of their livestock. Under this scheme, premium subsidies are \n provided, making" +
                        " insurance affordable for farmers, particularly for vulnerable and  small-scale \n cattle " +
                        "owners, to promote the conservation and productivity of indigenous cattle breeds.");

        HBox cb8 = createCheckboxWithInfo("Dairy Entrepreneurship Development Scheme",
                "Aims to support dairy farmers by providing financial assistance and subsidies to\n set up" +
                        " dairy units, purchase milking machines, and improve milk processing. This scheme \n" +
                        "encourages self-employment in the dairy sector, promotes quality milk production, \n " +
                        "and enhances the income of small and marginal farmers.");

        Button btn = new Button("Save");
        TextField Sum = new TextField();
        Sum.setPromptText("Enter Insured Sum");

        CheckBox checkbox1 = (CheckBox) cb1.getChildren().get(0);
        CheckBox checkbox2 = (CheckBox) cb2.getChildren().get(0);
        CheckBox checkbox3 = (CheckBox) cb3.getChildren().get(0);
        CheckBox checkbox4 = (CheckBox) cb4.getChildren().get(0);
        CheckBox checkbox5 = (CheckBox) cb5.getChildren().get(0);
        CheckBox checkbox6 = (CheckBox) cb6.getChildren().get(0);
        CheckBox checkbox7 = (CheckBox) cb7.getChildren().get(0);
        CheckBox checkbox8 = (CheckBox) cb8.getChildren().get(0);

        CheckBox[] checkboxes = {
                (CheckBox) cb1.getChildren().get(0),
                (CheckBox) cb2.getChildren().get(0),
                (CheckBox) cb3.getChildren().get(0),
                (CheckBox) cb4.getChildren().get(0),
                (CheckBox) cb5.getChildren().get(0),
                (CheckBox) cb6.getChildren().get(0),
                (CheckBox) cb7.getChildren().get(0),
                (CheckBox) cb8.getChildren().get(0)
        };

        // Add listeners to ensure only one checkbox can be selected at a time
        for (CheckBox checkbox : checkboxes) {
            checkbox.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
                if (isSelected) {
                    for (CheckBox other : checkboxes) {
                        if (other != checkbox) {
                            other.setSelected(false);
                        }
                    }
                }
            });
        }

        Button logoutBtn = new Button("Logout");
        TextField purp = new TextField();
        purp.setPromptText("Choose from DAIRY, BEEF or BREEDING");

        TextField claim_hist = new TextField();
        claim_hist.setPromptText("How many times have you claimed insurance");
        HBox buttonbox = new HBox(btn, logoutBtn);

        btn.setOnAction(e ->
        {
            if (!validatePolicyInputs(checkboxes, Sum, purp, claim_hist))
            {
                showAlert("Error", "Please fill in all fields correctly:\n" +
                        "- Select one policy\n" +
                        "- Enter a valid insured sum\n" +
                        "- Enter a valid purpose (DAIRY, BEEF, or BREEDING)\n" +
                        "- Enter a valid claim history number");
                return;
            }
            String selectedPolicy = "";
            for (int i = 0; i < checkboxes.length; i++) {
                if (checkboxes[i].isSelected()) {
                    selectedPolicy = checkboxes[i].getText();
                    break;
                }
            }

            // Find the user and process the policy
            for (int i = 0; i < userCount; i++) {
                if (users[i].getUsername().equals(username) && users[i] instanceof Cattle)
                {
                    System.out.println("nigga");
                    showPremiumCalculation(
                            stage,
                            users[i].getUsername(),
                            selectedPolicy,
                            Double.parseDouble(Sum.getText()),
                            purp.getText().toUpperCase(),
                            Integer.parseInt(claim_hist.getText()),
                            i);
                    return;
                }
            }

            showAlert("Error", "User not found or invalid user type");

        });

        logoutBtn.setOnAction(e -> showLoginScene(stage));

        root.getChildren().addAll(
                new Label("Insurance Policies"), cb1, cb2, cb3, cb4, cb5, cb6, cb7, cb8, purp,
                claim_hist, Sum, buttonbox);
        setBackgroundImageVBOX(root, "C:\\Users\\newpassword\\Desktop\\OOP miniproject 2nd yr\\oop\\" +
                "src\\main\\java\\images\\background.png");

        Scene scene = new Scene(root, 900, 600);
        stage.setScene(scene);
        stage.show();
    }

    private int checkSum(Stage stage, String username, String password) {

        if(username.equals("Admin") && password.equals("AdminCSEB"))
            return 2;

        for (int i = 0; i < userCount; i++)
        {
            if(users[i].getUsername().equals(username) &&
                    users[i].getPassword().equals(password))
            {
                return 1;
            }
        }
        return 0;
    }

    private boolean registerUser(String username, String password, String email) {
        for (int i = 0; i < userCount; i++) {
            if (users[i].getUsername().equals(username)) {
                return false;
            }
        }
        if (userCount >= users.length) {
            return false;
        }

        users[userCount] = new Cattle(username, password, email);
        userCount++;
        return true;
    }
    private void showAlert(String title, String message)
    {
        Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
        alert1.setTitle(title);
        alert1.setHeaderText(null);
        alert1.setContentText(message);
        alert1.showAndWait();
    }


    public void addCattleForUser(String username, String breed, String weight, String age, String uid, boolean health, int i) {
        if (users[i] instanceof Cattle) {
            Cattle newCattle = new Cattle(
                    username,
                    users[i].getPassword(),
                    users[i].getEmail(),
                    breed,
                    age,
                    weight,
                    uid,
                    health
            );

            Cattle.addCattle(newCattle);
            cattleCount[i]++;

            System.out.println("Added new cattle:");
            System.out.println("UID: " + newCattle.getUID());
            System.out.println("Breed: " + newCattle.getBreed());
            System.out.println("Age: " + newCattle.getAge());
            System.out.println("Weight: " + newCattle.getWeight());
            System.out.println("Health: " + newCattle.getHealth());
        } else {
            System.out.println("Error: User is not a Cattle type");
        }
    }

    private HBox createCheckboxWithInfo(String checkboxText, String tooltipText)
    {
        HBox container = new HBox(5);
        container.setAlignment(Pos.CENTER_LEFT);
        CheckBox checkbox = new CheckBox(checkboxText);

        Label infoLabel = new Label("ⓘ");
        infoLabel.setStyle(
                "-fx-text-fill: #2196F3;" +
                        "-fx-cursor: hand;" +
                        "-fx-font-size: 14px;"
        );

        Tooltip tooltip = new Tooltip(tooltipText);
        tooltip.setStyle(
                "-fx-font-size: 12px;" +
                        "-fx-padding: 10px;" +
                        "-fx-background-radius: 5px;" +
                        "-fx-background-color: #f0f0f0;" +
                        "-fx-text-fill: #333333;" +
                        "-fx-border-color: #cccccc;" +
                        "-fx-border-radius: 5px;"
        );
        tooltip.setShowDelay(javafx.util.Duration.millis(100));
        tooltip.setHideDelay(javafx.util.Duration.millis(200));

        Tooltip.install(infoLabel, tooltip);

        container.getChildren().addAll(checkbox, infoLabel);

        return container;
    }

    @Override
    public void setBackgroundImageVBOX(VBox pane, String imagePath) {
        Image bg = new Image(new File(imagePath).toURI().toString());
        BackgroundImage backgroundImage = new BackgroundImage(
                bg,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(
                        BackgroundSize.AUTO,
                        BackgroundSize.AUTO,
                        false, false, true, true)
        );
        pane.setBackground(new Background(backgroundImage));
    }

    @Override
    public void setBackgroundImage(StackPane pane, String imagePath) {
        Image bg = new Image(new File(imagePath).toURI().toString());
        BackgroundImage backgroundImage = new BackgroundImage(
                bg,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(
                        BackgroundSize.AUTO,
                        BackgroundSize.AUTO,
                        false, false, true, true)
        );
        pane.setBackground(new Background(backgroundImage));
    }

    @Override
    public void setBackgroundImageGRID(GridPane pane, String imagePath) {
        Image bg = new Image(new File(imagePath).toURI().toString());
        BackgroundImage backgroundImage = new BackgroundImage(
                bg,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(
                        BackgroundSize.AUTO,
                        BackgroundSize.AUTO,
                        false, false, true, true)
        );
        pane.setBackground(new Background(backgroundImage));
    }

    private boolean validatePolicyInputs(CheckBox[] checkboxes, TextField sumField, TextField purposeField, TextField claimField) {
        // Check if any policy is selected
        boolean policySelected = false;
        for (CheckBox checkbox : checkboxes) {
            if (checkbox.isSelected()) {
                policySelected = true;
                break;
            }
        }
        if (!policySelected) return false;

        // Validate sum insured
        try {
            double sum = Double.parseDouble(sumField.getText());
            if (sum <= 0) return false;
        } catch (NumberFormatException e) {
            return false;
        }

        // Validate purpose
        String purpose = purposeField.getText().toUpperCase();
        if (!purpose.equals("DAIRY") && !purpose.equals("BEEF") && !purpose.equals("BREEDING")) {
            return false;
        }

        // Validate claim history
        try {
            int claims = Integer.parseInt(claimField.getText());
            if (claims < 0) return false;
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }
}