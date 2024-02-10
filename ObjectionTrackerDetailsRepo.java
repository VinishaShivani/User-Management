package com.trb.allocationservice.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.trb.allocationservice.entity.ObjectionTrackerDetails;

@Repository
public interface ObjectionTrackerDetailsRepo extends JpaRepository<ObjectionTrackerDetails, Integer> {

}
