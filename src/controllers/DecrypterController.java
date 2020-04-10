package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;
import managers.KeyManager;
import managers.MathManager;

import java.math.BigInteger;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Random;

public class DecrypterController {

    @FXML
    private TextField userN;

    @FXML
    private TextField userE;

    @FXML
    private Text decryptStep1ExecutionTime;

    @FXML
    private Text decryptStep2ExecutionTime;

    @FXML
    private Text valueOfD;

    @FXML
    private Button btnDecrypt;

    @FXML
    private TextArea msgToDecrypt;

    @FXML
    private TextArea decryptedMsg;

    private long d, n;

    public Alert generateAlert(String title, String header, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(DecrypterController.class.getResource("/resources/dark-theme.css").toExternalForm());
        dialogPane.getStyleClass().add("custom-alert");
        return alert;
    }

    @FXML
    public void onGenerateClick() {
        long e;
        Instant startTime = Instant.now();

        try {
            n = Long.parseLong(userN.getText().replace(" ", ""));
        } catch (Exception ex) {
            this.generateAlert(
                    "Error",
                    "Invalid N provided",
                    "The N you provided could not be parsed to a long"
            ).showAndWait();
            return;
        }

        try {
            e = Long.parseLong(userE.getText().replace(" ", ""));
        } catch (Exception ex) {
            this.generateAlert(
                    "Error",
                    "Invalid E provided",
                    "The E you provided could not be parsed to a long"
            ).showAndWait();
            return;
        }

        List<Long> factors = MathManager.factorizePrime(n);

        if (factors.size() < 2) {
            this.generateAlert(
                    "Error",
                    "Invalid N provided",
                    "The N you provided has less then 2 factors"
            ).showAndWait();
            return;
        }

        long p = factors.get(0);

        // Multiply all the primes together, except the last one
        for (int i = 1; i <= factors.size() - 2; i++) {
            p *= factors.get(i);
        }

        // Get the last prime
        long q = factors.get(factors.size() - 1);

        long eulerTotient = MathManager.eulerTotient(p, q);

        if (MathManager.gcdByEuclidsAlgorithm(eulerTotient, e) != 1) {
            this.generateAlert(
                    "Error",
                    "Invalid E provided",
                    "The gcd between e and  φ(n) is not 1"
            ).showAndWait();
            return;
        }

        this.d = MathManager.calculateExponentD(e, eulerTotient);

        valueOfD.setText(String.valueOf(d));

        btnDecrypt.setDisable(false);

        Instant endTime = Instant.now();
        long duration = Duration.between(startTime, endTime).toMillis();
        decryptStep1ExecutionTime.setText(duration + "ms");
    }

    @FXML
    public void onDecryptClick() {
        Instant startTime = Instant.now();

        String textToDecrypt = msgToDecrypt.getText();
        String[] encryptedChars = textToDecrypt.split(" ");

        StringBuilder result = new StringBuilder();

        for (String encryptedChar : encryptedChars) {
            BigInteger encryptedCharNumber = new BigInteger(encryptedChar);
            BigInteger decryptedCharNumber = encryptedCharNumber.pow((int) this.d)
                                                .mod(BigInteger.valueOf(this.n));
            result.append((char) decryptedCharNumber.intValue());
        }

        decryptedMsg.setText(result.toString());
        Instant endTime = Instant.now();
        long duration = Duration.between(startTime, endTime).toMillis();
        decryptStep2ExecutionTime.setText(duration + "ms");
    }

    @FXML
    public void onAutofillClick() {
        userN.setText(KeyManager.getN().toString());
        userE.setText(KeyManager.getE().toString());
    }
}
