package com.floz.jcr;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private Button sendButton = null;
    private EditText contentEditText = null;
    private ListView chatListView = null;
    private List<ChatEntity> chatList = null;
    private ChatAdapter chatAdapter = null;
    Handler handler=new Handler();
    String name="floz",room="make love";
    boolean mIsActivityDone=true;
    output out;
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
        while (!mIsActivityDone)
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
        while (!mIsActivityDone)
        {
            try{
                Thread.sleep(250);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
    Toolbar toolbar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        contentEditText = (EditText) this.findViewById(R.id.et_content);
        sendButton = (Button) this.findViewById(R.id.btn_send);
        chatListView = (ListView) this.findViewById(R.id.listview);

        chatList = new ArrayList<>();
        chatAdapter = new ChatAdapter(this,chatList);
        chatListView.setAdapter(chatAdapter);

        sendButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!contentEditText.getText().toString().equals("")) {
                    //发送消息
                    insert(name,contentEditText.getText().toString(),room);
                    contentEditText.setText("");
                }else {
                    Toast.makeText(MainActivity.this, "Content is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                setroom();
                setname();
                insert("root", name + "进入了房间", room);
                out=new output(chatAdapter,chatList,handler,room,name);
                out.start();
            }
        }).start();


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case 1:         // 子窗口ChildActivity的回传数据
                if (data != null) {
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        //处理代码在此地
                        name = bundle.getString("result");// 得到子窗口ChildActivity的回传数据
                    }
                }
                mIsActivityDone = true;
                break;
            case 2:
                if (data != null) {
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        room = bundle.getString("result");
                    }
                }
                mIsActivityDone = true;
                break;
            default:
                //其它窗口的回传数据
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private boolean mIsExit;
    /**
     * 双击返回键退出
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mIsExit) {
                this.finish();

            } else {
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
                mIsExit = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mIsExit = false;
                    }
                }, 2000);
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
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

        switch (item.getItemId())
        {
            case R.id.action_room:
                out._stop();
                chatList.clear();
                chatAdapter.notifyDataSetChanged();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        setroom();
                        setname();
                        try {
                            out.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        insert("root", name + "进入了房间", room);
                        out=new output(chatAdapter,chatList,handler,room,name);
                        out.start();
                    }
                }).start();
                break;
            case R.id.action_setting:
                Intent intent = new Intent();
                intent.setClass(MainActivity.this , SettingActivity.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }


}