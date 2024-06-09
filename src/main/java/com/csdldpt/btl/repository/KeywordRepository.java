package com.csdldpt.btl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.csdldpt.btl.entity.Keyword;

@Repository
public interface KeywordRepository extends JpaRepository<Keyword, Long>{

}
