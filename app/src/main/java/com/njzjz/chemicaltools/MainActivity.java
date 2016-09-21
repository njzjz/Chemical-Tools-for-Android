package com.njzjz.chemicaltools;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);

        final Button elementButton = (Button) findViewById(R.id.elementButton);
        elementButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String[] elementNameArray = getResources().getStringArray(R.array.elementNameArray);
                String[] elementAbbrArray = getResources().getStringArray(R.array.elementAbbrArray);
                String[] elementMassArray= getResources().getStringArray(R.array.elementMassArray);
                int i;
                int elementNumber=0;
                String elementInput="";
                EditText elementText =(EditText)findViewById(R.id.elementText);
                TextView elementTextview=(TextView)findViewById(R.id.elementTextview);
                elementInput=elementText.getText().toString();
                for(i=0;i<118;i++) {
                    if (elementInput.equals(String.valueOf(i+1))){
                        elementNumber=i+1;
                    }else{
                        if(elementInput.equals(elementNameArray[i])){
                            elementNumber=i+1;
                        }else{
                            if(elementInput.equals(elementAbbrArray[i])){
                                elementNumber=i+1;
                            }
                        }
                    }
                }
                Resources res = getResources();
                if(elementNumber>0){
                    elementTextview.setText(String.format(res.getString(R.string.elementOutput_name),elementNumber,elementNameArray[elementNumber-1],elementAbbrArray[elementNumber-1],elementMassArray[elementNumber-1]));
                }else{
                    elementTextview.setText(String.format(res.getString(R.string.error_name)));
                };
            };
        });
    };
};

