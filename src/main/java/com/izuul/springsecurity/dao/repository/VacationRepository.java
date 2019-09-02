package com.izuul.springsecurity.dao.repository;

import com.izuul.springsecurity.entity.activity.Vacation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author: Guihong.Zhang
 * @date: 2019-08-21 15:34
 **/
public interface VacationRepository extends JpaRepository<Vacation, String> {

    List<Vacation> findAllByInstanceIdIn(List<String> ids);

    List<Vacation> findAllByApplicantIdOrderByCreateTimeDesc(String applicantId);
}
