package org.teamhavei.havei.activities;

import android.os.Bundle;
import android.text.NoCopySpan;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import org.teamhavei.havei.Event.BookTag;
import org.teamhavei.havei.Event.Bookkeep;
import org.teamhavei.havei.Event.HaveITag;
import org.teamhavei.havei.R;
import org.teamhavei.havei.adapters.TagListAdapter;
import org.teamhavei.havei.databases.BookkeepDBHelper;
import org.teamhavei.havei.databases.EventDBHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.sql.DatabaseMetaData;

public class ActivityBookkeepAdd extends AppCompatActivity {

    private Integer tag;
    private Double now_Num;
    private String Num_text;
    private int ioJud=0;
    private int dot=0;
    EditText BookTitle;
    EditText BookNum;
    RecyclerView mTagRecyc;
    RadioGroup io;
    BookkeepDBHelper bookDBHelper;
    List<org.teamhavei.havei.Event.HaveITag> mtaglist;
    TagListAdapter mTaglistAdapter;
    String Date;
    Bookkeep mBookkeep;
    int iconid;




    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookkeep_add_detail);
        init();
        BookTitle = (EditText) findViewById(R.id.add_account_detail_account_label);
        BookNum = (EditText) findViewById(R.id.add_account_detail__money_value);
        mTagRecyc=(RecyclerView)findViewById(R.id.add_recyc_booklist);

//        BookTag.setText(tag.toString());
//        BookNum.setText(Num_text);
       init_tag();
        io = (RadioGroup) findViewById(R.id.add_account_detail_expenditure_or_income);
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




    }

    public void init()
    {
        tag=0;
        now_Num =0d;
        dot=0;
        bookDBHelper=new BookkeepDBHelper(ActivityBookkeepAdd.this,BookkeepDBHelper.DB_NAME,null, BookkeepDBHelper.DATABASE_VERSION);
//        Num_text=now_Num.toString();
    }

    public void edit()
    {

        Num_text=BookTitle.getText().toString();
        now_Num= Double.parseDouble(BookNum.getText().toString());
        if(ioJud==1) {
            now_Num=now_Num*(-1);
        }

        Date=editGetDate();

        mBookkeep=setBookkeep();

        bookDBHelper.insertBookkeep(mBookkeep);


    }
    public  void init_tag() {
            List<BookTag> B = bookDBHelper.findAllBoktag();
            for(int i=0;i<B.size();i++)
            {
                mtaglist.add((HaveITag) B.get(i));
            }
            mTaglistAdapter= new TagListAdapter(mtaglist,this);
            mTagRecyc.setAdapter(mTaglistAdapter);
            RecyclerView.LayoutManager layoutManager= new GridLayoutManager (this,5);
            mTagRecyc.setLayoutManager(layoutManager);

    }
    public String editGetDate()
    {
        SimpleDateFormat   sDateFormat   =   new SimpleDateFormat("yyyy-MM-dd");
        String   date   =   sDateFormat.format(new   java.util.Date());
        return date;
    }


    public Bookkeep setBookkeep()
    {
        Bookkeep A=new Bookkeep();
        A.setIconId(iconid);
        A.setname(Num_text);
        A.setPM(now_Num);
        A.settag(tag);
        A.setTime(Date);
        return A;

    }

    public void edit(View view) {
        edit();
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

