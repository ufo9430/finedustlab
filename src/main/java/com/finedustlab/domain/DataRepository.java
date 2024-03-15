package com.finedustlab.domain;

import com.finedustlab.model.Survey;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public interface DataRepository {

    public void save(Optional<Object> entity);
    public Object findByID(String id) throws ExecutionException, InterruptedException;

}
