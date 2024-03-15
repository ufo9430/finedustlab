package com.finedustlab.domain.repository;

import com.finedustlab.domain.DataRepository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class UserRepository implements DataRepository {
    @Override
    public void save(Optional<Object> entity) {

    }

    @Override
    public Object findByID(String id) {
        return null;
    }

}
