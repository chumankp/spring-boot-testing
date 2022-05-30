package com.chuman.repository;

import com.chuman.model.EmployeeDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


public interface EmployeeRepository extends JpaRepository<EmployeeDTO, Long> {
    //Repository
    //custom query method
    Optional<EmployeeDTO> findByEmail(String email);

    //custom query on jpql with index parameters
    @Query("select e from Employee e where e.firstName = ?1 and e.lastName = ?2")
    EmployeeDTO findByCustomQuery(String firstName, String lastName);

    //named parameters
    @Query("select e from Employee e where e.firstName =:firstName and e.lastName =:lastName")
    EmployeeDTO findByCustomQueryNamedParams(@Param("firstName") String firstName, @Param("lastName") String lastName);

    //Sql native query
    @Query(value = "select * from employees e where e.first_name = ?1 and e.last_name = ?2",nativeQuery = true)
    EmployeeDTO findByCustomNativeSql(String firstName, String lastName);

    //Sql native query with named parameters
    @Query(value = "select * from employees e where e.first_name =:firstName and e.last_name =:lastName", nativeQuery = true)
    EmployeeDTO findByCustomNativeSqlNamedParams(@Param("firstName") String firstName, @Param("lastName") String lastName);
}
