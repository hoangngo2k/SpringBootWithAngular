package demoapp.repository;

import demoapp.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long>  , JpaSpecificationExecutor<Student> {

    @Transactional
    void deleteStudentByIdIn(Collection<Long> id);

    List<Student> searchStudentByFullNameContainingIgnoreCase(String fullName);
}
