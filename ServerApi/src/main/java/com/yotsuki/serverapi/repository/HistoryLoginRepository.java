package com.yotsuki.serverapi.repository;

import com.yotsuki.serverapi.entity.HistoryLogin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryLoginRepository extends JpaRepository<HistoryLogin ,Long > {

}
