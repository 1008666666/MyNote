package com.example.cc.mynote;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.aserbao.aserbaosandroid.functions.database.greenDao.db.DaoMaster;
import com.aserbao.aserbaosandroid.functions.database.greenDao.db.DaoSession;
import com.aserbao.aserbaosandroid.functions.database.greenDao.db.PadDao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity2 extends MainActivity {

    private Button btn_save,btn_cancle;
    private EditText et_title,et_content;
    private long noteId = -1;

    private Cursor cursor;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private ListView lv_note;
    private Button btn_add;
    private PadDao padDao;
    private List<Pad> padList;
    private SQLiteDatabase db;
    private SimpleCursorAdapter adapter;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_editor);
        et_title = (EditText)findViewById(R.id.et_title);
        et_content = (EditText)findViewById(R.id.et_content);
        btn_save = (Button)findViewById(R.id.btn_save);
        btn_cancle = (Button)findViewById(R.id.btn_cancel);
        padList = new ArrayList<Pad>();
        padDao = MyGreenDAOApplication.getInstances().getDaoSession().getPadDao();
        db = MyGreenDAOApplication.getInstances().getDb();
        initNoteEditValue();
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  if(pad.getId()!=null)
//                padDao.insert(pad1);
                if(noteId!=-1){
                    Pad pad=padDao.load(noteId);
                    pad.setTitle(et_title.getText().toString());
                    pad.setContent(et_content.getText().toString());
                    Date date = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String dateNowStr = sdf.format(date);
                    pad.setCreatetime(dateNowStr);
                    padDao.update(pad);
                }
                else {
                    Pad pad1 = new Pad();
                    pad1.setTitle(et_title.getText().toString());
                    pad1.setContent(et_content.getText().toString());
                    Date date = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String dateNowStr = sdf.format(date);
                    pad1.setCreatetime(dateNowStr);
                    padDao.insert(pad1);
                }
                finish();
            }
        });
        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    protected void initNoteEditValue() {
        // 从Intent中获取id的值
        long id = this.getIntent().getLongExtra("id", -1L);
        // 如果有传入id那么id！=-1
        if (id != -1L) {
            // 使用noteId保存id
            noteId = id;
            // 查询该id的笔记
           // Cursor cursor = db.rawQuery("select * from Pad where _id =?",new String[]{id.toString()});
            Pad pad=padDao.load(id);
           // return db.rawQuery("select * from NoteBook where _id =?",new String[]{id.toString()});

            if (pad.getId()!=null) {
                // 将内容提取出来
                et_title.setText(pad.getTitle());
                et_content.setText(pad.getContent());
            }
        }
    }



}
