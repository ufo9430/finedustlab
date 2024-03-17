package com.finedustlab.domain.repository;

import com.finedustlab.domain.DataRepository;

import java.util.Optional;

public class UserRepository implements DataRepository {
    @Override
    public void save(Optional<Object> entity) {

    }

    @Override
    public Object findByID(Long id) {
        return null;
    }

}
