package com.floz.jcr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class namedialog extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.namedialog);
        Button btn=(Button) findViewById(R.id.exitBtn);
        final EditText input = (EditText) findViewById(R.id.name);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (TextUtils.isEmpty(input.getText())) {
                    Toast.makeText(namedialog.this,"您输入的内容不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra("result",input.getText().toString());// 把返回数据存入Intent
                namedialog.this.setResult(1, intent);// 设置回传数据。resultCode值是1，这个值在主窗口将用来区分回传数据的来源，以做不同的处理
                namedialog.this.finish();// 关闭子窗口ChildActivity
            }
        });
    }
    @Override
    public void onBackPressed(){
    }

}
