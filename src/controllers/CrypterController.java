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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CrypterController {

    @FXML
    private TextField valueOfN;

    @FXML
    private TextArea msgToEncrypt;

    @FXML
    private TextArea encryptedMsg;

    @FXML
    private Text valueOfP;

    @FXML
    private Text valueOfQ;

    @FXML
    private Text step1ExecutionTime;

    @FXML
    private Text step2ExecutionTime;

    @FXML
    private Text step3ExecutionTime;

    @FXML
    private Text valueOfE;

    @FXML
    private Button btnGenerateE;

    @FXML
    private Button btnEncrypt;

    private long p, q;

    @FXML
    public void handleGeneratePQClick() {
        Instant startTime = Instant.now();
        long n;

        try {
            n = Long.parseLong(valueOfN.getText().replace(" ", ""));
        } catch (Exception e) {
            this.generateAlert(
                    "Error",
                    "Invalid N provided",
                    "The N you provided could not be parsed to a long"
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

        KeyManager.setN(BigInteger.valueOf(n));
        p = factors.get(0);

        // Multiply all the primes together, except the last one
        for (int i = 1; i <= factors.size() - 2; i++) {
            p *= factors.get(i);
        }

        // Get the last prime
        q = factors.get(factors.size() - 1);
        valueOfP.setText(String.valueOf(this.p));
        valueOfQ.setText(String.valueOf(this.q));

        btnGenerateE.setDisable(false);
        Instant endTime = Instant.now();
        long duration = Duration.between(startTime, endTime).toMillis();
        step1ExecutionTime.setText(duration + "ms");
    }

    @FXML
    public void handleGenerateEClick() {
        Instant startTime = Instant.now();

        long eulerTotient = MathManager.eulerTotient(this.p, this.q);
        List<Long> factorsOfEulerTotient = MathManager.factorizePrime(eulerTotient);

        List<Long> primesForRange = MathManager.getPrimesRange(
                1,
                eulerTotient > 5000 ? 5000 : eulerTotient // max range 100000 for efficiency
        );

        primesForRange.removeAll(factorsOfEulerTotient);

        Random rand = new Random();
        long e = primesForRange.get(rand.nextInt(primesForRange.size()));
        KeyManager.setE(BigInteger.valueOf(e));

        valueOfE.setText(String.valueOf(e));

        Instant endTime = Instant.now();
        long duration = Duration.between(startTime, endTime).toMillis();
        step2ExecutionTime.setText(duration + "ms");

        btnEncrypt.setDisable(false);
    }

    @FXML
    public void handleEncryptClick() {
        Instant startTime = Instant.now();

        char[] characters = msgToEncrypt.getText().toCharArray();

        StringBuilder result = new StringBuilder();

        BigInteger n = KeyManager.getN();
        BigInteger e = KeyManager.getE();

        for (long character : characters) {
            BigInteger m = BigInteger.valueOf(character);
            result.append(m.pow(e.intValue()).remainder(n).toString() + " ");
        }
        result.deleteCharAt(result.length() - 1);

        encryptedMsg.setText(result.toString());
        Instant endTime = Instant.now();
        long duration = Duration.between(startTime, endTime).toMillis();
        step3ExecutionTime.setText(duration + "ms");
    }

    public Alert generateAlert(String title, String header, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(CrypterController.class.getResource("/resources/dark-theme.css").toExternalForm());
        dialogPane.getStyleClass().add("custom-alert");
        return alert;
    }
}
