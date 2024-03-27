package com.finedustlab.domain.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finedustlab.model.classroom.Classroom;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
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
        document.set(classroomMap, SetOptions.merge());
    }

    @SuppressWarnings("unchecked")
    public Object findBySchoolInfo(String schoolCode, String grade, String classNum) {
        Map result;
       try{
           CollectionReference collection = FirestoreClient.getFirestore().collection(CLASSROOM);
           ApiFuture<DocumentSnapshot> future = collection.document(schoolCode).get();

           ObjectMapper objectMapper = new ObjectMapper();
           Object classroomObj = future.get().get(grade);
           result = objectMapper.convertValue(classroomObj, Map.class);
           return result.get(classNum);
       }catch (Exception e){
           result = new HashMap<>();
           result.put("finedust_factor",-1);
           result.put("ultrafine_factor",-1);
           return result;
       }
    }
}
