package com.example.calculator;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import org.mariuszgromada.math.mxparser.Expression;

public class MainActivity extends AppCompatActivity {

    private EditText display;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_main);

        Button clearBtn = findViewById(R.id.clearBtn);
        Button parenthesesBtn = findViewById(R.id.parenthesisBtn);
        Button exponentBtn = findViewById(R.id.exponentBtn);
        Button divBtn = findViewById(R.id.divBtn);
        Button sevenBtn = findViewById(R.id.sevenBtn);
        Button eightBtn = findViewById(R.id.eightBtn);
        Button nineBtn = findViewById(R.id.ninebtn);
        Button multiplyBtn = findViewById(R.id.multiplyBtn);
        Button fourBtn = findViewById(R.id.fourBtn);
        Button fiveBtn = findViewById(R.id.fiveBtn);
        Button sixBtn = findViewById(R.id.sixBtn);
        Button subBtn = findViewById(R.id.subBtn);
        Button oneBtn = findViewById(R.id.oneBtn);
        Button twoBtn = findViewById(R.id.twoBtn);
        Button threeBtn = findViewById(R.id.threeBtn);
        Button addBtn = findViewById(R.id.addBtn);
        Button percentBtn = findViewById(R.id.percentBtn);
        Button zeroBtn = findViewById(R.id.zeroBtn);
        Button pointBtn = findViewById(R.id.pointBtn);
        Button equalsBtn = findViewById(R.id.equalsBtn);
        Button backspaceBtn = findViewById(R.id.backspaceBtn);
        Button currencyBtn = findViewById(R.id.currencyBtn);

        display = findViewById(R.id.displayText);
        display.setShowSoftInputOnFocus(false);

        display.setOnClickListener(view -> {
            if (getString(R.string.display).equals(display.getText().toString())) {
                display.setText("");
            }
        });

        currencyBtn.setOnClickListener(view -> {
            Intent intent;
            intent = new Intent(MainActivity.this, CurrencyConvertor.class);
            startActivity(intent);
        });

        clickButton(clearBtn);
        clickButton(parenthesesBtn);
        clickButton(exponentBtn);
        clickButton(divBtn);
        clickButton(sevenBtn);
        clickButton(eightBtn);
        clickButton(nineBtn);
        clickButton(multiplyBtn);
        clickButton(fourBtn);
        clickButton(fiveBtn);
        clickButton(sixBtn);
        clickButton(subBtn);
        clickButton(oneBtn);
        clickButton(twoBtn);
        clickButton(threeBtn);
        clickButton(addBtn);
        clickButton(percentBtn);
        clickButton(zeroBtn);
        clickButton(pointBtn);
        clickButton(equalsBtn);
        clickButton(backspaceBtn);
    }


    private void updateText(String strToAdd) {
        String prevStr = display.getText().toString();
        int posOfCursor = display.getSelectionStart();
        String leftPartOfStr = prevStr.substring(0, posOfCursor);
        String rightPartOfStr = prevStr.substring(posOfCursor);

        if (getString(R.string.display).equals(display.getText().toString())) {
            display.setText(strToAdd);
        }
        else {
            display.setText(String.format("%s%s%s", leftPartOfStr, strToAdd, rightPartOfStr));
        }
        display.setSelection(posOfCursor + 1);
    }

    private void clickButton(Button button) {

        button.setOnClickListener(view -> {
            CharSequence text = button.getText();
            if ("C".contentEquals(text)) {
                display.setText("");
            } else if ("()".contentEquals(text)) {
                parenthesesBtn(view);
            } else if ("^".contentEquals(text)) {
                updateText("^");
            } else if ("÷".contentEquals(text)) {
                updateText("÷");
            } else if ("7".contentEquals(text)) {
                updateText("7");
            } else if ("8".contentEquals(text)) {
                updateText("8");
            } else if ("9".contentEquals(text)) {
                updateText("9");
            } else if ("×".contentEquals(text)) {
                updateText("×");
            } else if ("4".contentEquals(text)) {
                updateText("4");
            } else if ("5".contentEquals(text)) {
                updateText("5");
            } else if ("6".contentEquals(text)) {
                updateText("6");
            } else if ("-".contentEquals(text)) {
                updateText("-");
            } else if ("1".contentEquals(text)) {
                updateText("1");
            } else if ("2".contentEquals(text)) {
                updateText("2");
            } else if ("3".contentEquals(text)) {
                updateText("3");
            } else if ("+".contentEquals(text)) {
                updateText("+");
            } else if ("%".contentEquals(text)) {
                updateText("%");
            } else if ("0".contentEquals(text)) {
                updateText("0");
            } else if (".".contentEquals(text)) {
                updateText(".");
            } else if ("=".contentEquals(text)) {
                equalsBtn(view);
            } else {
                backspaceBtn(view);
            }
        });
    }


    public void parenthesesBtn(View view) {
        int posOfCursor = display.getSelectionStart();
        int leftPar = 0;
        int rightPar = 0;

        for (int i = 0; i < posOfCursor; i++) {
            if (display.getText().toString().charAt(i) == '(') {
                leftPar += 1;
            }
            if (display.getText().toString().charAt(i) == ')') {
                rightPar += 1;
            }
        }

        if (leftPar == rightPar || display.getText().toString().charAt(display.getText().length() - 1) == '(') {
            updateText("(");
        }
        else if (leftPar > rightPar && display.getText().toString().charAt(display.getText().length() - 1) != '(') {
            updateText(")");
        }
    }


    public void equalsBtn(View view) {
        String input = display.getText().toString();
        input = input.replaceAll("÷", "/");
        input = input.replaceAll("×", "*");
        Expression expression = new Expression(input);
        String output = String.valueOf(expression.calculate());

        display.setText(output);
        display.setSelection(output.length());
    }


    public void backspaceBtn(View view) {
        int posOfCursor = display.getSelectionStart();
        int textLen = display.getText().length();

        if (posOfCursor != 0 && textLen != 0) {
            SpannableStringBuilder replaceWithEmpty = (SpannableStringBuilder) display.getText();
            replaceWithEmpty.replace(posOfCursor - 1, posOfCursor, "");
            display.setText(replaceWithEmpty);
            display.setSelection(posOfCursor - 1);
        }
    }
}