package com.zhangyf.xiaoneng;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import com.orhanobut.hawk.Hawk;
import java.util.ArrayList;
import java.util.List;

import static com.zhangyf.xiaoneng.MainActivity.STUDENT_LIST_KEY;

public class StudentListActivity extends AppCompatActivity {

    RecyclerView rvStudent;
    StudentListAdapter studentListAdapter;
    List<Student> studentList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);
        studentList = Hawk.get(STUDENT_LIST_KEY);
        rvStudent = findViewById(R.id.rv_student);
        if(studentList!=null){
            rvStudent.setLayoutManager(new LinearLayoutManager(this));
            studentListAdapter = new StudentListAdapter(this,studentList);
            rvStudent.setAdapter(studentListAdapter);
        }
    }
}
