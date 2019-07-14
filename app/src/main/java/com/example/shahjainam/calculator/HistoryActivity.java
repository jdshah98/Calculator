package com.example.shahjainam.calculator;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private ArrayList<String> expr = new ArrayList<String>();
    private ArrayList<String> res = new ArrayList<String>();
    private Button clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        List<String> l = Arrays.asList(SaveHistoryPreference.getExp(this).split(","));

        if (l.size() != 1){
            for (int i = 0; i < l.size(); i = i + 2) {
                expr.add(l.get(i));
                res.add(l.get(i + 1));
            }
        }else{
            Toast.makeText(this,"No History Found",Toast.LENGTH_SHORT).show();
            finish();
        }

        clear = findViewById(R.id.clearButton);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveHistoryPreference.clear(HistoryActivity.this);
                Toast.makeText(HistoryActivity.this,"History Cleared",Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        ListView historyView = findViewById(R.id.historyView);
        HistoryAdapter historyAdapter = new HistoryAdapter(this, expr.toArray(new String[expr.size()]), res.toArray(new String[res.size()]));
        historyView.setAdapter(historyAdapter);
    }

    public class HistoryAdapter extends BaseAdapter {

        private Context context;
        private LayoutInflater layoutInflater;
        private String[] expression, result;
        private TextView exp, res;

        public HistoryAdapter(Context context, String[] expression, String[] result) {
            this.context = context;
            this.expression = expression;
            this.result = result;
            layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return expression.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.history_card_view, parent,false);
            }
            exp = convertView.findViewById(R.id.expression_history);
            res = convertView.findViewById(R.id.result_history);
            exp.setText(expression[position]);
            res.setText(result[position]);
            return convertView;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}