package com.finedustlab.domain.repository;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.finedustlab.model.user.UserAccount;
import com.finedustlab.model.user.StudentProfile;
import com.finedustlab.model.user.UserProfile;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Repository
public class UserRepository {
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    Firestore firestore = FirestoreClient.getFirestore();
    private static final String USER_PROFILE = "user_profile";
    private String getUserUIDByEmail(String email) throws FirebaseAuthException {
        UserRecord userRecord = firebaseAuth.getUserByEmail(email);
        return userRecord.getUid();
    }

    @SuppressWarnings("unchecked")
    public void setUser(String email, UserProfile userProfile) throws FirebaseAuthException {
        ObjectMapper objectMapper = new ObjectMapper();
        HashMap<String, Object> profileHashmap = objectMapper.convertValue(userProfile, HashMap.class);
        String userUID = getUserUIDByEmail(email);

        firestore.collection(USER_PROFILE).document(userUID).set(profileHashmap);
    }

    public Map<String, Object> getUserProfileByEmail(String email) throws FirebaseAuthException, ExecutionException, InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper();
        String uid = getUserUIDByEmail(email);
        ApiFuture<DocumentSnapshot> future = firestore.collection(USER_PROFILE).document(uid).get();
        Map<String, Object> data = future.get().getData();
        return data;
    }
}
