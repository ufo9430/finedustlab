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

    public void save(int schoolCode, int grade, int classNum, Classroom classroomInfo){

        Map<String, Map<String, Object>> classroomMap = new HashMap<>();
        Map<String, Object> classroom = new HashMap<>();

        classroom.put(String.valueOf(classNum), classroomInfo);
        classroomMap.put(String.valueOf(grade), classroom);

        DocumentReference document = FirestoreClient.getFirestore().collection(CLASSROOM).document(String.valueOf(schoolCode));
        document.set(classroomMap);
    }

    public Classroom findBySchoolInfo(Map<String, String> schoolInfo) throws ExecutionException, InterruptedException {
        CollectionReference collection = FirestoreClient.getFirestore().collection(CLASSROOM);
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = collection.orderBy("1").get();
        return new Classroom();
    }
}
