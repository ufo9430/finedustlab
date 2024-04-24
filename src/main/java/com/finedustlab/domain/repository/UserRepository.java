package com.finedustlab.domain.repository;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.finedustlab.model.user.TeacherProfile;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
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
}
