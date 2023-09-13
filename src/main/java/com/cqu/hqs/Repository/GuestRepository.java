package com.cqu.hqs.Repository;

import com.cqu.hqs.entity.Guest;
import com.cqu.hqs.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Deependra Karki
 */


public interface GuestRepository extends JpaRepository<Guest, Long> {
    
    boolean existsByEmail(String email);

//    Guest findByUserId(Long id);

    Guest findByUser(User user);
}
