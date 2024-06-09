package com.csdldpt.btl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.csdldpt.btl.entity.Story;

@Repository
public interface StoryRepository extends JpaRepository<Story, Long>{

}
