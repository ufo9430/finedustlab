package com.finedustlab.domain.repository;

import com.finedustlab.model.classroom.Classroom;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.FirebaseDatabase;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Repository
public class ClassroomRepository {

    private final String CLASSROOM = "classroom";

    public void save(Map<String, String> schoolInfo, Classroom classroomInfo){
        String school_code = schoolInfo.get("school_code");
        String grade = schoolInfo.get("grade");
        String class_num = schoolInfo.get("class_num");

        Map<String, Map<String, Object>> classrooms = new HashMap<>();
        Map<String, Object> classroom = new HashMap<>();

        classroom.put(class_num, classroomInfo);
        classrooms.put(grade, classroom);

        DocumentReference document = FirestoreClient.getFirestore().collection(CLASSROOM).document(school_code);
        document.set(classrooms);
    }

    public Classroom findBySchoolInfo(Map<String, String> schoolInfo) throws ExecutionException, InterruptedException {
        CollectionReference collection = FirestoreClient.getFirestore().collection(CLASSROOM);
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = collection.orderBy("1").get();
        return new Classroom();
    }
}
