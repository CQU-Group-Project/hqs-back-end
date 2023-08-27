package com.cqu.hqs.Repository;

import com.cqu.hqs.entity.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomTypeRepository extends JpaRepository<RoomType,Long> {
    RoomType findByName(String name);
}
