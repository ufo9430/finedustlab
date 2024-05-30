package com.finedustlab.service;

import com.finedustlab.domain.repository.UserRepository;
import com.finedustlab.model.user.TeacherProfile;
import com.google.firebase.auth.FirebaseAuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public void set(String email, TeacherProfile userProfile) throws FirebaseAuthException {
        //선생 코드 생성
        String code = "";
        userRepository.setUser(email,userProfile);
    }

    public Map<String, Object> get(String uid) throws ExecutionException, FirebaseAuthException, InterruptedException {
        return  userRepository.getUserProfileByUID(uid);
    }

    public Map<String, String> getEmail(String name, String schoolName)throws ExecutionException, InterruptedException{
        Map<String,String> result = new HashMap<>();
        String email = userRepository.findUserEmailByNameAndSchoolName(name, schoolName);
        result.put("email",email);
        if(email.equals("-")) result.put("result","notfound");
        else result.put("result","complete");
        return result;
    }
}
