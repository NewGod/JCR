package com.floz.jcr;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button btn;
    EditText input,output;
    Handler handler=new Handler();
    String name,room="make love";
    boolean mIsActivityDone=true;
    void insert(final String name,final String info,final String room) {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        db_chat.insert(name, info,room);
                    }
                }
        ).start();
    }
    void setname(){
        mIsActivityDone = false;
        startActivityForResult(new Intent("com.floz.namedialog"),1);
        while (mIsActivityDone == false)
        {
            try{
                Thread.sleep(250);
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    void setroom(){
        mIsActivityDone = false;
        startActivityForResult(new Intent("com.floz.roomdialog"),2);
        while (mIsActivityDone == false)
        {
            try{
                Thread.sleep(250);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        btn = (Button) findViewById(R.id.Send);
        input = (EditText) findViewById(R.id.input);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(input.getText())) {
                    Toast.makeText(MainActivity.this,"您输入的内容不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    insert(name, input.getText().toString(),room);
                    input.setText("");
                }
            }
        });
        output=(EditText) findViewById(R.id.output);
        new Thread(new Runnable() {
            @Override
            public void run() {
                setroom();
                setname();
                insert("root", name + "进入了房间", room);
                Thread tmp_out = new Thread(new output(output, handler, room));
                tmp_out.start();
            }
        }).start();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (resultCode) {
            case 1:         // 子窗口ChildActivity的回传数据
                if (data != null) {
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        //处理代码在此地
                        name = bundle.getString("result");// 得到子窗口ChildActivity的回传数据
                    }
                }
                mIsActivityDone=true;
                break;
            case 2:
                if (data != null){
                    Bundle bundle = data.getExtras();
                    if (bundle!=null) {
                        room=bundle.getString("result");
                    }
                }
                mIsActivityDone=true;
                break;
            default:
                //其它窗口的回传数据
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
