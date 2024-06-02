package com.finedustlab.domain.repository;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.finedustlab.model.user.TeacherProfile;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.multitenancy.Tenant;
import com.google.firebase.auth.multitenancy.TenantManager;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Repository
public class UserRepository {
    Firestore firestore = FirestoreClient.getFirestore();
    private static final String USER_PROFILE = "user_profile";
    @SuppressWarnings("unchecked")
    public void setUser(String userUID, TeacherProfile userProfile) throws FirebaseAuthException {
        ObjectMapper objectMapper = new ObjectMapper();
        HashMap<String, Object> profileHashmap = objectMapper.convertValue(userProfile, HashMap.class);

        firestore.collection(USER_PROFILE).document(userUID).set(profileHashmap);
    }

    public Map<String, Object> getUserProfileByUID(String uid) throws ExecutionException, InterruptedException{
        ApiFuture<DocumentSnapshot> future = firestore.collection(USER_PROFILE).document(uid).get();
        Map<String, Object> data = future.get().getData();
        return data;
    }


    public String findUserEmailByNameAndSchoolName(String name, String schoolCode) throws ExecutionException, InterruptedException{
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        ApiFuture<QuerySnapshot> future = firestore.collection(USER_PROFILE).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            TeacherProfile profile = document.toObject(TeacherProfile.class);
            String docUsername = profile.getName();
            String docSchoolCode = Integer.toString(profile.getSchool_code());
            if(docUsername.equals(name) && docSchoolCode.equals(schoolCode)){
                String uid = document.getId();
                ApiFuture<UserRecord> userAsync = firebaseAuth.getUserAsync(uid);
                String email = userAsync.get().getEmail();
                return email;
            }
        }
        return "-";
    }

    public String findUsernameBySchoolInfo(String schoolCode, String grade, String classNum) throws ExecutionException, InterruptedException{
        ApiFuture<QuerySnapshot> future = firestore.collection(USER_PROFILE).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            TeacherProfile profile = document.toObject(TeacherProfile.class);
            if(String.valueOf(profile.getGrade()).equals(grade)
                    && String.valueOf(profile.getSchool_code()).equals(schoolCode)
                    && String.valueOf(profile.getClass_num()).equals(classNum)){
                return document.get("name").toString();
            }
        }
        return "-";
    }
}
