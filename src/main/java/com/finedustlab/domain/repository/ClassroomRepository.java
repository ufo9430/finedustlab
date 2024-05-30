package com.finedustlab.domain.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finedustlab.model.classroom.ClassroomRequestDTO;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class ClassroomRepository {

    private final String CLASSROOM = "classroom";

    public void save(int schoolCode, int grade, int classNum, ClassroomRequestDTO classroomInfo){

        Map<String, Map<String, Object>> classroomMap = new HashMap<>();
        Map<String, Object> classroom = new HashMap<>();

        classroom.put(String.valueOf(classNum), classroomInfo);
        classroomMap.put(String.valueOf(grade), classroom);

        DocumentReference document = FirestoreClient.getFirestore().collection(CLASSROOM).document(String.valueOf(schoolCode));
        document.set(classroomMap, SetOptions.merge());
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> findBySchoolInfo(String schoolCode, String grade, String classNum) {
        Map<String, Object> result;
       try{
           CollectionReference collection = FirestoreClient.getFirestore().collection(CLASSROOM);
           ApiFuture<DocumentSnapshot> future = collection.document(schoolCode).get();

           ObjectMapper objectMapper = new ObjectMapper();
           Object classroomObj = future.get().get(grade);
           result = objectMapper.convertValue(classroomObj, Map.class);
           HashMap<String,Object> classroom = objectMapper.convertValue(result.get(classNum), HashMap.class);
           classroom.put("result","complete");
           if(classroom.isEmpty()){
               throw new NullPointerException();
           }
           return classroom;
       }catch (Exception e){
           result = new HashMap<>();
           result.put("finedust_factor",-1);
           result.put("ultrafine_factor",-1);
           result.put("fine_status",-1);
           result.put("ultra_status",-1);
           result.put("result","error");
           return result;
       }
    }
}
