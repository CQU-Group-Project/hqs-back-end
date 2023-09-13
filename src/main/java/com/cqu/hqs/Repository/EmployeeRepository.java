package com.cqu.hqs.Repository;

import com.cqu.hqs.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Deependra Karki
 */
public interface EmployeeRepository extends JpaRepository<Employee,Long> {
    boolean existsByEmail(String email);

    Employee findByUserId(Long id);
}
