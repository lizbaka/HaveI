package org.teamhavei.havei.activities;

import android.os.Bundle;
import android.text.NoCopySpan;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import org.teamhavei.havei.R;
import org.teamhavei.havei.databases.BookkeepDBHelper;
import org.teamhavei.havei.databases.EventDBHelper;

import java.sql.DatabaseMetaData;

public class ActivityBookkeepAdd extends AppCompatActivity {

    private Integer tag;
    private Double now_Num;
    private String Num_text;
    private int ioJud=0;
    private int dot=0;
    EditText BookTag;
    EditText BookNum;
    BookkeepDBHelper bookDBHelper;


    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookkeep_add_detail);
        init();
        BookTag = (EditText) findViewById(R.id.add_account_detail_account_label);
        BookNum = (EditText) findViewById(R.id.add_account_detail__money_value);

//        BookTag.setText(tag.toString());
//        BookNum.setText(Num_text);
        RadioGroup io = (RadioGroup) findViewById(R.id.add_account_detail_expenditure_or_income);
        io.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton radbtn = (RadioButton) findViewById(checkedId);

                if(radbtn.getText().equals(R.string.bookkeep_expenditure))
                    ioJud=0;
                else
                    ioJud=1;
            }

        });
        tag=editTag();

        now_Num= editGetnum();
        if(ioJud==1) {
            now_Num=now_Num*(-1);
        }
    }

    public void init()
    {
        tag=0;
        now_Num =0d;
        dot=0;
        bookDBHelper=new BookkeepDBHelper(ActivityBookkeepAdd.this,BookkeepDBHelper.DB_NAME,null, BookkeepDBHelper.DATABASE_VERSION);
//        Num_text=now_Num.toString();
    }

    public double editGetnum()
    {

        return Double.parseDouble(BookNum.getText().toString());
    }
    public  int editTag() {
        return 0;
    }




//    public void onClick_One(View view) {
//        if(dot==0)
//        {
//            now_Num=now_Num*10;
//            now_Num=now_Num+1;
//            Num_text=now_Num.toString();
//            BookNum.setText(Num_text);
//
//        }
//        else if(dot==1)
//        {
////            Num_text=now_Num.toString()+".";
////            BookNum.setText(Num_text);
//            dot=2;
//        }
//        else if(dot==2)
//        {
//            now_Num=now_Num+0.1f;
//            Num_text=now_Num.toString();
//            BookNum.setText(Num_text);
//            dot=0;
//        }



    }

