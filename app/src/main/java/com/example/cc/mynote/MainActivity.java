package com.example.cc.mynote;

import android.content.DialogInterface;
import android.content.Intent;;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aserbao.aserbaosandroid.functions.database.greenDao.db.DaoMaster;
import com.aserbao.aserbaosandroid.functions.database.greenDao.db.DaoSession;
import com.aserbao.aserbaosandroid.functions.database.greenDao.db.PadDao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity {
//    private Cursor listItemCursor = null;
    private Cursor cursor;
   // private DaoMaster daoMaster;
   // private DaoSession daoSession;
    private ListView lv_note;
    private Button btn_add;
    private PadDao padDao;
    private List<Pad> padList;
    private SQLiteDatabase db;
    private SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv_note = (ListView) findViewById(R.id.lv_note);
        btn_add = (Button) findViewById(R.id.btn_add);
        padList = new ArrayList<Pad>();
        padDao = MyGreenDAOApplication.getInstances().getDaoSession().getPadDao();
        db = MyGreenDAOApplication.getInstances().getDb();
        cursor = db.query(true, padDao.getTablename(), padDao.getAllColumns(), null, null, null, null, null, null);

        String[] from = {PadDao.Properties.Title.columnName, PadDao.Properties.Createtime.columnName};
        int[] to = {R.id.noteTitle, R.id.noteCreateTime};
        adapter = new SimpleCursorAdapter(this, R.layout.note_item, cursor, from, to, 0);
        lv_note.setAdapter(adapter);
        initListNoteListener();
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,MainActivity2.class);
                startActivity(intent);

            }
        });
        cursor.requery();
        adapter.notifyDataSetChanged();
    }


    private void initListNoteListener() {
        // 长按删除
        ((ListView) this.findViewById(R.id.lv_note))
                .setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent,
                                                   View view, int position, final long id) {
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("提示框")
                                .setMessage("确认删除该笔记？？")
                                .setPositiveButton("确定",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface arg0,int arg1) {
                                                padDao.deleteByKey(id);
//                                                DBService.deleteNoteById((int) id);
                                                //删除后刷新列表
//                                                MainActivity.this.onResume();
                                                //MainActivity.this.refresh();
//                                                refresh();
//                                                adapter
                                               // adapter.notifyDataSetChanged();
                                                cursor.requery();
                                                adapter.notifyDataSetChanged();
                                                Toast.makeText(
                                                        MainActivity.this,
                                                        "删除成功！！",
                                                        Toast.LENGTH_LONG)
                                                        .show();
                                            }
                                        }).setNegativeButton("取消", null).show();
                        return true;
                    }
                });

        //点击进行修改操作
        ((ListView) this.findViewById(R.id.lv_note))
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        Intent in = new Intent();
                       // in.setClass(MainActivity.this,MainActivity2.class);
                        in.setClass(MainActivity.this,MainActivity2.class);
                        // 将id数据放置到Intent，切换视图后可以将数据传递过去
                        in.putExtra("id", id);
                        startActivity(in);
                    }
                });

    }

    protected void onResume() {
        super.onResume();
        cursor.requery();
        adapter.notifyDataSetChanged();

    }

}

