package com.zhangyf.xiaoneng;



import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.orhanobut.hawk.Hawk;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button btn_register;
    Button btn_list;
    EditText et_name;
    EditText et_input_issue;

    public static final String STUDENT_LIST_KEY = "studentList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_register = findViewById(R.id.btn_register);
        btn_list = findViewById(R.id.btn_list);
        et_name = findViewById(R.id.et_name);
        et_input_issue = findViewById(R.id.et_input_issue);


        btn_register.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                List<Student> currentStudentList = Hawk.get(STUDENT_LIST_KEY);
                String name = et_name.getText().toString();
                String issueString = et_input_issue.getText().toString();
                if(name.isEmpty()||issueString.isEmpty()){
                    Toast.makeText(MainActivity.this,"不能有一处信息为空",Toast.LENGTH_SHORT).show();
                }else {
                    if(currentStudentList == null){
                        currentStudentList = new ArrayList<>();
                    }
                    currentStudentList.add(new Student(
                            String.valueOf(System.currentTimeMillis()),
                            name,
                            getIssueListFromString(issueString)
                    ));
                    Hawk.put(STUDENT_LIST_KEY,currentStudentList);
                    et_input_issue.setText("");
                    et_name.setText("");
                }

            }
        });

        btn_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,StudentListActivity.class);
                startActivity(intent);
            }
        });
    }

    private List<Issue> getIssueListFromString(String issueString){
        List<Issue> issueListTemp = new ArrayList();
        for(int i = 0;i<issueString.length();i++){
            int status = Integer.parseInt(String.valueOf(issueString.charAt(i)));
            issueListTemp.add(i,new Issue(i,status));
        }
        return issueListTemp;
    }
}
