package com.example.demo.service;

import com.example.demo.dto.request.CreateStudentRequest;
import com.example.demo.dto.request.CriteriaSearch;
import com.example.demo.dto.request.UpdateStudentRequest;
import com.example.demo.dto.response.StudentResponse;
import com.example.demo.entity.StudentEntity;
import com.example.demo.entity.StudentScoreEntity;
import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.StudentScoreRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Objects;


import static com.example.demo.dto.request.CreateStudentRequest.createToStudentEntity;
import static com.example.demo.dto.request.UpdateStudentRequest.updateToStudentEntity;
import static com.example.demo.entity.StudentEntity.toStudentResponse;
import static com.example.demo.entity.StudentEntity.toStudentListDto;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final EntityManager entityManager;
    private final StudentScoreRepository studentScoreRepository;

    public StudentService(StudentRepository studentRepository, EntityManager entityManager, StudentScoreRepository studentScoreRepository) {
        this.studentRepository = studentRepository;
        this.entityManager = entityManager;
        this.studentScoreRepository = studentScoreRepository;
    }

    public List<StudentResponse> getAllStudents() {
        return toStudentListDto.apply(studentRepository.findAll());
    }

    public StudentEntity getStudentById(String id) {
        try {
            return studentRepository.findById(id).orElseThrow(() -> new Exception("Student not found: " + id));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public StudentResponse createStudent(CreateStudentRequest student) {
        StudentEntity studentEntity = createToStudentEntity.apply(student);
        studentEntity.setId(student.getFirstName().substring(0,1)
                .concat(student.getLastName().substring(0,1))
                .concat("-")
                .concat(Integer.valueOf((int) (Math.random() * 35471)).toString()));

        StudentResponse studentResponse = toStudentResponse.apply(studentRepository.save(studentEntity));
        if (Objects.nonNull(student.getCurrentScore())) {
            studentScoreRepository.save(StudentScoreEntity
                    .builder()
                    .student(StudentEntity.builder().id(studentResponse.getId()).build())
                    .score(studentResponse.getCurrentScore())
                    .build());
        }
        return studentResponse;
    }

    public StudentResponse updateStudent(UpdateStudentRequest student) {
        return toStudentResponse.apply(studentRepository.save(updateToStudentEntity.apply(student)));
    }

    public StudentResponse saveStudent(StudentEntity student) {
        return toStudentResponse.apply(studentRepository.save(student));
    }

    public void deleteStudent(String id) {
        studentRepository.deleteById(id);
    }

    public List<StudentEntity> findStudentsByCriteria(CriteriaSearch criteriaSearch) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<StudentEntity> studentEntityCriteriaQuery = criteriaBuilder.createQuery(StudentEntity.class);
        Root<StudentEntity> studentRoot = studentEntityCriteriaQuery.from(StudentEntity.class);
        studentEntityCriteriaQuery.select(studentRoot);
        studentEntityCriteriaQuery.where(criteriaBuilder.equal(studentRoot.get(criteriaSearch.getPropertyName()), criteriaSearch.getSearchValue()));
        TypedQuery<StudentEntity> typedQuery = entityManager.createQuery(studentEntityCriteriaQuery);
        return typedQuery.getResultList();
    }
}
