package com.finedustlab.domain.repository;

import com.finedustlab.domain.DataRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Repository
public class UserRepository implements DataRepository {
    @Override
    public void save(Optional<Object> entity) {

    }

    @Override
    public Object findByID(Long id) {
        return null;
    }

}
