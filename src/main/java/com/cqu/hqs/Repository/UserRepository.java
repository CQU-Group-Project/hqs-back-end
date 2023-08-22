package com.cqu.hqs.Repository;

import com.cqu.hqs.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Deependra Karki
 */


public interface UserRepository extends JpaRepository<User, Long> {
    
}
