package com.finedustlab.domain;

import java.util.Optional;

public interface DataRepository {

    public void save(Optional<Object> entity);
    public Object findByID(Long id);

}
