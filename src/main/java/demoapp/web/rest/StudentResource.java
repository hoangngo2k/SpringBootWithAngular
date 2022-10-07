package demoapp.web.rest;

import demoapp.domain.Student;
import demoapp.service.StudentService;
import demoapp.service.dto.StudentDTO;
import demoapp.service.mapper.StudentMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class StudentResource {

    private final StudentService studentService;

    public StudentResource(StudentService service) {
        this.studentService = service;
    }

    @GetMapping("/students")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(studentService.getAll().stream()
            .map(StudentMapper::toDTO)
            .collect(Collectors.toList()));
    }

    @GetMapping("/students/paging")
    public ResponseEntity<?> getAllStudents(Pageable pageable , @RequestParam(required = false) String search,
                                            @RequestParam(required = false , defaultValue = "0") Long min,
                                            @RequestParam(required = false ,defaultValue = "0") Long max) {
        Page<Student> students = studentService.getAll(pageable, search, min, max);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(StudentMapper.toDTO(studentService.getById(id)));
    }

    @GetMapping("/students/search")
    public ResponseEntity<?> searchStudentByName(@RequestParam String fullName) {
        return ResponseEntity.ok(studentService.searchStudentByName(fullName).stream()
            .map(StudentMapper::toDTO)
            .collect(Collectors.toList()));
    }

    @PostMapping("/students")
    public ResponseEntity<?> createStudent(@RequestBody StudentDTO studentDTO) {
        return ResponseEntity.ok(StudentMapper.toDTO(studentService.createStudent(studentDTO)));
    }

    @PutMapping("/students/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable Long id, @RequestBody StudentDTO studentDTO) {
        return ResponseEntity.ok(StudentMapper.toDTO(studentService.updateStudent(studentDTO, id)));
    }

    @DeleteMapping("/students/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable Collection<Long> id) {
        studentService.delete(id);
        return ResponseEntity.ok(true);
    }
}
