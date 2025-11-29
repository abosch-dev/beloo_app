package com.beloo.test.repository;

import com.beloo.test.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, String> {

    Optional<Location> findById(String id);

    @Query(value = "SELECT l FROM Location l WHERE l.ciclystId = :cyclistId")
    List<Location> findLocationsByCyclist(String cyclistId);
}
