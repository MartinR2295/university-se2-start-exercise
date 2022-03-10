package at.rms.university.se2.first;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;

import at.rms.university.se2.first.network.SE2Client;
import at.rms.university.se2.first.utils.MathUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SE2Client.Response {

    Button btnSend, btnCalc;
    TextView tvAnswer;
    TextInputEditText tiInput;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get the views
        btnSend = findViewById(R.id.btnSend);
        btnCalc = findViewById(R.id.btnCalc);
        tvAnswer = findViewById(R.id.tvAnswer);
        tiInput = findViewById(R.id.tiInput);
        progressBar = findViewById(R.id.progressBar);

        // set click listeners
        btnSend.setOnClickListener(this);
        btnCalc.setOnClickListener(this);

        // hide progress bar (do it here, to see the element in the xml viewer)
        progressBar.setVisibility(View.GONE);
    }

    /**
     * server response from se2 server
     * @return
     */
    @Override
    public void onSE2ServerResponse(String response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvAnswer.setText(response);
            }
        });
    }

    @Override
    public void onSE2ServerError(IOException e) {
        // in the case we want to show an error
    }

    @Override
    public void onSE2ServerFinished() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // hide progress bar if we are finished
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    /**
     * implemented click listener
     * @param view
     */
    public void onClick(View view) {
        // compare reference of view to reference of the buttons, to decide the related action
        if (view == btnSend) {
            this.onBtnSend();
        } else if (view == btnCalc) {
            this.onBtnCalculate();
        }
    }

    /**
     * Click-Handler for btnSend.
     * Related to exercise 2.1
     */
    public void onBtnSend() {
        SE2Client client = new SE2Client(this, tiInput.getText().toString());
        progressBar.setVisibility(View.VISIBLE);
        client.start();
    }

    /**
     * Click-Handler for btnCalculate.
     * Related to exercise 2.2.
     * My student-id is 11844898, so 11844898 % 7 = 2.
     * Because of that, I need to implement task 2 of 2.2
     *
     * 2.2 - Task 2
     * Prüfen, ob zwei beliebige Ziffern existieren die einen gemeinsamen Teiler > 1 haben.
     * Werden zwei Ziffern mit gemeinsamem Teiler gefunden, soll
     * deren Index ausgegeben werden
     */
    public void onBtnCalculate() {
        // reset answer textview
        tvAnswer.setText("");
        // get input of the inputField
        String input = tiInput.getText().toString();
        // get the char array out of the input string
        char[] characters = input.toCharArray();
        // define a numbers byte array
        // I use a byte array here, because we only have numbers between 0 and 9
        byte[] numbers = new byte[characters.length];

        // fill the byte array with the ascii numbers of the characters
        for (int i = 0; i < characters.length; i++) {
            numbers[i] = (byte) Character.getNumericValue(characters[i]);
        }

        // calculate the greates common divisor of all combinations of numbers
        // and write it to the output textview
        for (int i = 0; i < numbers.length; i++) {
            // use j = i, to prevent duplicate combinations
            // because index (1,2) and (2,1) should be the same
            for (int j = i; j < numbers.length; j++) {
                if (i != j) { // don't use the same index
                    // calculate the gcd with our helper method
                    byte gcd = (byte) MathUtil.euclid(numbers[i], numbers[j]);

                    // write it to the output label, if the gcd is larger than 1
                    if (gcd > 1) {
                        // set a comma, if we already have a pair in the label
                        if (tvAnswer.getText().length() > 0) tvAnswer.append(", ");
                        tvAnswer.append("("+ i + "," + j + ")");
                    }
                }
            }
        }
    }
}