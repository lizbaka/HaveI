package org.teamhavei.havei.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.teamhavei.havei.Event.BookCou;
import org.teamhavei.havei.Event.BookTag;
import org.teamhavei.havei.Event.Bookkeep;
import org.teamhavei.havei.Event.HaveITag;
import org.teamhavei.havei.R;
import org.teamhavei.havei.adapters.TagListAdapter;
import org.teamhavei.havei.databases.BookkeepDBHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ActivityBookkeepAdd extends AppCompatActivity {

    private Integer tagId;
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
    String sDate;
    Bookkeep mBookkeep;
    BookCou mBookCou;
    double couio=0;

    int iconid;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookkeep_add_detail);
        init();
//        BookTag.setText(tag.toString());
//        BookNum.setText(Num_text);
       init_tag();
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
        tagId =0;
        now_Num =0d;
        dot=0;
        bookDBHelper=new BookkeepDBHelper(ActivityBookkeepAdd.this,BookkeepDBHelper.DB_NAME,null, BookkeepDBHelper.DATABASE_VERSION);
        BookTitle = (EditText) findViewById(R.id.add_account_detail_account_label);
        BookNum = (EditText) findViewById(R.id.add_account_detail__money_value);
        mTagRecyc=(RecyclerView)findViewById(R.id.add_recyc_booklist);
        io = (RadioGroup) findViewById(R.id.add_account_detail_expenditure_or_income);
        //        Num_text=now_Num.toString();
    }
    public void edit()
    {
        Num_text=BookTitle.getText().toString();
        now_Num= Double.parseDouble(BookNum.getText().toString());
        if(ioJud==0) {
            now_Num=now_Num*(-1);
        }
        Date=editGetDate();
        sDate=Date.substring(0,7);
        mBookkeep=setBookkeep();
        bookDBHelper.insertBookkeep(mBookkeep);
        setBookCou();
    }
//    public  void init_tag() {
//            List<BookTag> B = bookDBHelper.findAllBoktag();
//            for(int i=0;i<B.size();i++)
//            {
//                mtaglist.add((HaveITag) B.get(i));
//            }
//            mTaglistAdapter= new TagListAdapter(mtaglist,this);
//            mTagRecyc.setAdapter(mTaglistAdapter);
//            RecyclerView.LayoutManager layoutManager= new GridLayoutManager (this,5);
//            mTagRecyc.setLayoutManager(layoutManager);
//
//    }
    public void init_tag(){
        List<BookTag> List = bookDBHelper.findAllBoktag();
        mtaglist = new ArrayList<>();
        mtaglist.addAll(List);
        mTaglistAdapter = new TagListAdapter(mtaglist, ActivityBookkeepAdd.this,0, new TagListAdapter.OnTagClickListener() {
            @Override
            public void onClick(HaveITag tag) {
                tagId = tag.getId();
            }
        });
        mTagRecyc.setLayoutManager(new GridLayoutManager(ActivityBookkeepAdd.this, 3, GridLayoutManager.HORIZONTAL, false));
        mTagRecyc.setAdapter(mTaglistAdapter);
    }
    public String editGetDate()
    {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat   sDateFormat   =   new SimpleDateFormat("yyyy-MM-dd");
//        String sdate  =;
        return  sDateFormat.format(new java.util.Date());
    }


    public Bookkeep setBookkeep()
    {
        Bookkeep A=new Bookkeep();
        A.setIconId(iconid);
        A.setname(Num_text);
        A.setPM(now_Num);
        A.settag(tagId);
        A.setTime(Date);
        return A;
    }
    public void setBookCou()
    {
        if(bookDBHelper.findBookcouByMonth(sDate)==null)
        {
            if(ioJud==1)
            {
                couio=now_Num;
                mBookCou.setTime(sDate);
                mBookCou.setIn(couio);
                mBookCou.setOut(0d);
            }
            else
            {
                couio=(-1)*now_Num;
                mBookCou.setTime(sDate);
                mBookCou.setIn(0d);
                mBookCou.setOut(couio);
            }

            bookDBHelper.insertBookCou(mBookCou);
        }
        else
        {
            BookCou oldCou= bookDBHelper.findBookcouByMonth(sDate);
            mBookCou.setTime(sDate);
            if(ioJud==1)
            {
                mBookCou.setIn(oldCou.getIn()+now_Num);
                mBookCou.setOut(oldCou.getOut());
            }
            else
            {
                mBookCou.setIn(oldCou.getIn());
                mBookCou.setOut(oldCou.getOut()-now_Num);
            }
            bookDBHelper.updateBookCou(oldCou,mBookCou);
        }
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

