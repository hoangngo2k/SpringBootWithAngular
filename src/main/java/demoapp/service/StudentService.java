package demoapp.service;

import demoapp.domain.Student;
import demoapp.repository.StudentRepository;
import demoapp.service.dto.StudentDTO;
import demoapp.service.mapper.StudentMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository repository;

    public StudentService(StudentRepository studentRepository) {
        this.repository = studentRepository;
    }

    public Specification<Student> searchByFullName(String name){
        return new Specification<Student>() {
            @Override
            public Predicate toPredicate(Root<Student> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.like(root.get("fullName") , "%" + name + "%");
            }
        };
    }

    public Specification<Student> filterGreaterThanId(Long min) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.greaterThan(root.get("id"), min.longValue());
        };
    }

    public Specification<Student> filterLessThanId(Long max) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.lessThan(root.get("id"), max.longValue());
        };
    }

    public List<Student> getAll() {
        return repository.findAll();
    }

    public Page<Student> getAll(Pageable pageable , String name, Long min, Long max) {
        Specification<Student> where = null;

        if(!StringUtils.isEmpty(name))
            where = searchByFullName(name);
        if (max > 0) {
            if(where != null)
                where = where.and(filterLessThanId(max));
            else
                where = filterLessThanId(max);
        }
        if (min >= 0) {
            if(where != null)
                where = where.and(filterGreaterThanId(min));
            else
                where = filterGreaterThanId(min);
        }
        return repository.findAll(where , pageable);
    }

    public Student getById(Long id) {
        return repository.findById(id).get();
    }

    public Student createStudent(StudentDTO studentDTO) {
        Student student = StudentMapper.toEntity(studentDTO);
        return repository.save(student);
    }

    public Student updateStudent(StudentDTO studentDTO, Long id) {
        Student student = repository.findById(id).get();
        student.setCode(studentDTO.getCode());
        student.setFullName(studentDTO.getFullName());
        student.setGender(studentDTO.getGender());
        student.setGrade(studentDTO.getGrade());
        student.setPoint(studentDTO.getPoint());
        return repository.save(student);
    }

    public void delete(Collection<Long> id) {
        repository.deleteStudentByIdIn(id);
    }

    public List<Student> searchStudentByName(String fullName) {
        return repository.searchStudentByFullNameContainingIgnoreCase(fullName);
    }
}
