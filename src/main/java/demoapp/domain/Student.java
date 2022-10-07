package demoapp.domain;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;

@Entity
@Table(name = "students")
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 5, max = 25)
    @Column(unique = true, length = 25, nullable = false)
    private String code;

    @Size(min = 5, max = 60)
    @Column(length = 60, nullable = false)
    private String fullName;

    @Size(min = 2, max = 10)
    @Column(length = 10, nullable = false)
    private String gender;

    @Size(min = 1, max = 15)
    @Column(length = 15, nullable = false)
    private String grade;

    @Column(length = 10, nullable = false)
    private Double point;

    public Student() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Double getPoint() {
        return point;
    }

    public void setPoint(Double point) {
        this.point = point;
    }
    
}
