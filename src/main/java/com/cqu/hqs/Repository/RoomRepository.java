package com.cqu.hqs.Repository;

import com.cqu.hqs.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room,Long> {
    boolean existsByRoomNumber(int roomNumber);
}
