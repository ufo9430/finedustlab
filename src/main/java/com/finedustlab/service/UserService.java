package com.finedustlab.service;

import com.finedustlab.domain.repository.UserRepository;
import com.finedustlab.model.user.UserAccount;
import com.finedustlab.model.user.StudentProfile;
import com.finedustlab.model.user.UserProfile;
import com.google.firebase.auth.FirebaseAuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public void set(String email, StudentProfile userProfile) throws FirebaseAuthException {
        userRepository.setUser(email,userProfile);
    }

    public Map<String, Object> get(String email) throws ExecutionException, FirebaseAuthException, InterruptedException {
        return  userRepository.getUserProfileByEmail(email);
    }
}
