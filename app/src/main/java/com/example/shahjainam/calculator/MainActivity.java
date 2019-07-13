package com.example.shahjainam.calculator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class MainActivity extends AppCompatActivity {

    private TextView result, exp;
    private Button equal, back, clear, dot;
    private Boolean resultState = TRUE, newTerm = FALSE, errorState = FALSE, dotFlag = FALSE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        initialize();
        listeners();
    }

    private void initialize() {
        exp = findViewById(R.id.expression);
        result = findViewById(R.id.result);
        equal = findViewById(R.id.equal);
        back = findViewById(R.id.back);
        clear = findViewById(R.id.clear);
        dot = findViewById(R.id.dot);
    }

    private void listeners() {
        equal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = result.getText().toString();
                exp.setText(str);
                try{
                    Expression expression = new ExpressionBuilder(str).build();
                    double ans = expression.evaluate();
                    if(Math.floor(ans)==ans){
                        result.setText(String.valueOf((int) ans));
                    }else{
                        result.setText(String.valueOf(ans));
                    }
                    dotFlag = FALSE;
                }catch(ArithmeticException ex) {
                    errorState = TRUE;
                    result.setText(check(str));
                }catch (IllegalArgumentException ex){
                    errorState = TRUE;
                    result.setText(getString(R.string.error));
                } finally {
                    resultState = TRUE;
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str;
                if(errorState==TRUE){
                    str = getString(R.string.zero);
                    resultState = TRUE;
                    newTerm = FALSE;
                    errorState = FALSE;
                    dotFlag = FALSE;
                }else{
                    str = result.getText().toString();
                    if(str.length()>1){
                        if(str.charAt(str.length()-1)=='.'){
                            str = str.substring(0, str.length() - 1);
                            if(str.equals(getString(R.string.zero))){
                                resultState = TRUE;
                                newTerm = FALSE;
                                errorState = FALSE;
                            }else if(isOperator(str.charAt(str.length()-1))){
                                newTerm = TRUE;
                            }else{
                                newTerm = FALSE;
                            }
                            dotFlag = FALSE;
                        } else if(isOperator(str.charAt(str.length()-2))) {
                            str = str.substring(0, str.length() - 1);
                            if(str.contains("+") || str.contains("-") || str.contains("*") || str.contains("/") || str.contains(".")){
                                newTerm = TRUE;
                            }else {
                                newTerm = FALSE;
                            }
                        } else{
                            str = str.substring(0, str.length() - 1);
                            if(str.equals(getString(R.string.zero))){
                                resultState = TRUE;
                                newTerm = FALSE;
                                errorState = FALSE;
                                dotFlag = FALSE;
                            }
                        }
                    }else{
                        str=getString(R.string.zero);
                        resultState = TRUE;
                        newTerm = FALSE;
                        errorState = FALSE;
                        dotFlag = FALSE;
                    }
                }
                result.setText(str);
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result.setText(getString(R.string.zero));
                resultState = TRUE;
                newTerm = FALSE;
                errorState = FALSE;
                dotFlag = FALSE;
            }
        });

        dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dotFlag == FALSE) {
                    String str = result.getText().toString();
                    if (resultState == TRUE) {
                        str = getString(R.string.zero) + dot.getText().toString();
                        resultState = FALSE;
                        errorState = FALSE;
                        newTerm = FALSE;
                        dotFlag = TRUE;
                    }else if(newTerm == TRUE){
                        str = str + getString(R.string.zero) + dot.getText().toString();
                        dotFlag = TRUE;
                        newTerm = FALSE;
                    }else{
                        str = str + dot.getText().toString();
                        dotFlag = TRUE;
                    }
                    result.setText(str);
                }
            }
        });
    }

    private String check(String str) {
        String res = getString(R.string.error);
        if(str.contains("0/0")){
            res = getString(R.string.undefined);
        }else if(str.contains("/0")){
            res = getString(R.string.divideByZero);
        } else if(str.contains("%0")){
            res = getString(R.string.undefined);
        }
        return res;
    }

    private Boolean isOperator(char c){
        char[] op = new char[]{'+','-','*','/','%'};
        for(char i:op){
            if(c==i)
                return true;
        }
        return false;
    }

    public void numericListener(View view) {
        Button btn = findViewById(view.getId());
        if (resultState == TRUE) {
            String str = btn.getText().toString();
            result.setText(str);
            dotFlag = FALSE;
            if(!str.equals(getString(R.string.zero)))
                resultState = FALSE;
        } else {
            String str = result.getText().toString();
            if(newTerm == TRUE){
                if(str.charAt(str.length()-1)=='0' && isOperator(str.charAt(str.length()-2))){
                    str = str.substring(0,str.length()-1) + btn.getText().toString();
                }else{
                    str = str + btn.getText().toString();
                }
                if(!btn.getText().toString().equals(getString(R.string.zero)))
                    newTerm = FALSE;
            }else{
                str = str + btn.getText().toString();
            }
            result.setText(str);
        }
        errorState = FALSE;
    }

    public void operatorListener(View view){
        Button btn = findViewById(view.getId());
        String str = result.getText().toString();
        if(errorState==TRUE){
            str = getString(R.string.zero) + btn.getText().toString();
            errorState = FALSE;
        }else{
            if(isOperator(str.charAt(str.length()-1))){
                str = str.substring(0,str.length()-1) + btn.getText().toString();
            }else{
                str = str + btn.getText().toString();
            }
        }
        result.setText(str);
        resultState = FALSE;
        newTerm = TRUE;
        dotFlag = FALSE;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.about:
                startActivity(new Intent(MainActivity.this,AboutActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}