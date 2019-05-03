package com.example.cc.mynote;

import android.content.DialogInterface;
import android.content.Intent;;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
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

    static final String[] PROJECTION = new String[] { ContactsContract.RawContacts._ID, ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY };

    private Cursor cursor;
    private ListView lv_note;
    private Button btn_add;
    private PadDao padDao;
    private List<Pad> padList;
    private SQLiteDatabase db;
    private SimpleCursorAdapter adapter;
    private SearchView sv_search;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv_note = (ListView) findViewById(R.id.lv_note);
        btn_add = (Button) findViewById(R.id.btn_add);
        sv_search = (SearchView)findViewById(R.id.sv_search);
        padList = new ArrayList<Pad>();
        padDao = MyGreenDAOApplication.getInstances().getDaoSession().getPadDao();
        db = MyGreenDAOApplication.getInstances().getDb();
        cursor = db.query(true, padDao.getTablename(), padDao.getAllColumns(), null, null, null, null, null, null);
        String[] from = {PadDao.Properties.Title.columnName, PadDao.Properties.Createtime.columnName};
        int[] to = {R.id.noteTitle, R.id.noteCreateTime};
        //数据库适配器
        adapter = new SimpleCursorAdapter(this, R.layout.note_item, cursor, from, to, 0);
        lv_note.setAdapter(adapter);

        lv_note.setTextFilterEnabled(true);
        initListNoteListener();
        //添加按钮
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

        Toast.makeText(MainActivity.this, padDao.getTablename(), Toast.LENGTH_SHORT).show();
       // sv_search.setIconified(false);
       // sv_search.onActionViewExpanded();
        sv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sv_search.setIconified(false);
            }
        });
        sv_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                cursor = db.rawQuery("select * from Pads where title like '%" + newText + "%'", null);
                adapter.swapCursor(cursor); // 交换指针，展示新的数据
                 return true;

            }

        });
        sv_search.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                Toast.makeText(MainActivity.this, "关闭搜索框", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

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
        sv_search.setFocusable(true);
        sv_search.setFocusableInTouchMode(true);
        super.onResume();
        cursor.requery();
        adapter.notifyDataSetChanged();

    }

}

