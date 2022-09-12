package com.idb.hmis.dao;

import com.idb.hmis.entity.Hostel;
import com.idb.hmis.entity.HostelInfo;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HostelInfoDao extends JpaRepository<HostelInfo, Long> {

    Optional<HostelInfo> findByHostel(Hostel hostel);
    
}
