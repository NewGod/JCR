package com.floz.jcr;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class roomdialog extends ActionBarActivity {
    RadioGroup roomGroup;
    public class roomThread implements Runnable {
        List<String> name;
        roomThread(List<String> t){name=t;}
        public void run() {
            db_chat.getRoomName(name);
        }
    }
    void getRoomName(){
        roomGroup.removeAllViews();
        int id=0;
        List<String> roomName=new ArrayList<String>();
        Thread run=new Thread(new roomThread(roomName));
        run.start();
        try {
            run.join();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        for (String st:roomName) {
            RadioButton button=new RadioButton(this);
            setRaidBtnAttribute(button,st,id);
            roomGroup.addView(button);
            ++id;
        }
        roomGroup.check(0);
    }
    private void setRaidBtnAttribute( final RadioButton codeBtn, String btnContent, int id ){
        if( null == codeBtn ){
            return;
        }
        codeBtn.setId( id );
        codeBtn.setText( btnContent );

        LinearLayout.LayoutParams rlp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        codeBtn.setLayoutParams( rlp );
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.roomdialog);
        Button enter=(Button) findViewById(R.id.EnterRoom);
        Button create=(Button) findViewById(R.id.CreateRoom);
        roomGroup=(RadioGroup) findViewById(R.id.roomGroup);
        getRoomName();
        enter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent intent = new Intent();
                RadioButton Btn= (RadioButton)findViewById(roomGroup.getCheckedRadioButtonId());
                intent.putExtra("result",
                        Btn.getText().toString());// 把返回数据存入Intent
                roomdialog.this.setResult(2, intent);// 设置回传数据。resultCode值是1，这个值在主窗口将用来区分回传数据的来源，以做不同的处理
                roomdialog.this.finish();// 关闭子窗口ChildActivity
            }
        });
        create.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                final EditText editText = new EditText(roomdialog.this);
                final AlertDialog.Builder inputDialog =
                        new AlertDialog.Builder(roomdialog.this);
                inputDialog.setTitle("请输入房间名").setView(editText);
                inputDialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (TextUtils.isEmpty(editText.getText())) {
                                    Toast.makeText(roomdialog.this,"您输入的内容不能为空",Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                Thread tmp=new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        db_chat.creat_room(editText.getText().toString());
                                    }
                                });
                                tmp.start();
                                try{
                                    tmp.join();
                                }catch (InterruptedException e){
                                    e.printStackTrace();
                                }
                                getRoomName();
                            }
                        });
                inputDialog.setNegativeButton("取消",null).show();
            }
        });

    }
    @Override
    public void onBackPressed(){
    }
}
