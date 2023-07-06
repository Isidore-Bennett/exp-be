package com.example.demo.service;

import com.example.demo.dto.request.CriteriaSearch;
import com.example.demo.dto.response.StudentResponse;
import com.example.demo.entity.StudentEntity;
import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.StudentScoreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class StudentServiceTests {

    private StudentRepository studentRepository;
    private StudentScoreRepository studentScoreRepository;
    private StudentService studentService;
    private EntityManager entityManager;

    @Captor
    private ArgumentCaptor<StudentEntity> studentEntityArgumentCaptor;

    @BeforeEach
    public void setUp() {
        studentRepository = Mockito.mock(StudentRepository.class);
        entityManager = Mockito.mock(EntityManager.class);
        studentService = new StudentService(studentRepository, entityManager, studentScoreRepository);
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllStudents() {
        List<StudentEntity> studentList = new ArrayList<>();
        studentList.add(new StudentEntity("string-id-1", "TempName2", "TempLast1", new Date(),
                "+27123456789", "test@mail.com", null, null));
        studentList.add(new StudentEntity("string-id-2", "TempName3", "TempLast2", new Date(),
                "+27123456789", "test@mail.com", 68, null));
//        studentList.add(new StudentEntity("string-id-3", "TempName3", "TempLast3", new Date(),
//                "+27123456789", "test@mail.com", 75, null));

        Mockito.when(studentRepository.findAll()).thenReturn(studentList);

        List<StudentResponse> result = studentService.getAllStudents();

        Assertions.assertEquals(studentList.size(), result.size());
        Assertions.assertEquals(studentList.get(0).getFirstName(), result.get(0).getFirstName());
        Assertions.assertEquals(studentList.get(1).getFirstName(), result.get(1).getFirstName());
        Assertions.assertEquals(studentList.get(0).getLastName(), result.get(0).getLastName());
        Assertions.assertEquals(studentList.get(1).getLastName(), result.get(1).getLastName());
        Assertions.assertEquals(studentList.get(0).getEmailAddress(), result.get(0).getEmailAddress());
        Assertions.assertEquals(studentList.get(1).getEmailAddress(), result.get(1).getEmailAddress());
        Assertions.assertEquals(studentList.get(0).getCurrentScore(), result.get(0).getCurrentScore());
        Assertions.assertEquals(studentList.get(1).getCurrentScore(), result.get(1).getCurrentScore());
        Assertions.assertEquals(studentList.get(0).getCellNumber(), result.get(0).getCellNumber());
        Assertions.assertEquals(studentList.get(1).getCellNumber(), result.get(1).getCellNumber());
    }

    @Test
    public void testGetStudentById() {
        StudentEntity student = new StudentEntity("string-id-1", "TempName2", "TempLast1", new Date(),
                "+27123456789", "test@mail.com", null, null);

        Mockito.when(studentRepository.findById("string-id")).thenReturn(Optional.of(student));

        StudentEntity result = studentService.getStudentById("string-id");

        Assertions.assertEquals(student.getId(), result.getId());
        Assertions.assertEquals(student.getFirstName(), result.getFirstName());
        Assertions.assertEquals(student.getLastName(), result.getLastName());
        Assertions.assertEquals(student.getEmailAddress(), result.getEmailAddress());
        Assertions.assertEquals(student.getCurrentScore(), result.getCurrentScore());
        Assertions.assertEquals(student.getCellNumber(), result.getCellNumber());
    }

    @Test
    public void testGetStudentByIdNotFound() {
        String id = "string-id";
        Mockito.when(studentRepository.findById(id)).thenThrow(RuntimeException.class);

        Assertions.assertThrows(RuntimeException.class, () -> studentService.getStudentById(id));
    }

    @Test
    public void testSaveStudentEntity() {
        StudentEntity studentEntity = StudentEntity.builder()
                .firstName("null")
                .lastName("TempLast1")
                .dateOfBirth(new Date())
                .cellNumber("+27123456789")
                .emailAddress("test@email.com")
                .build();
        Mockito.when(studentRepository.save(Mockito.any(StudentEntity.class)))
                .thenReturn(studentEntity);

        StudentResponse result = studentService.saveStudent(studentEntity);


        Assertions.assertEquals(studentEntity.getId(), result.getId());
        Assertions.assertEquals(studentEntity.getFirstName(), result.getFirstName());
        Assertions.assertEquals(studentEntity.getLastName(), result.getLastName());
        Assertions.assertEquals(studentEntity.getEmailAddress(), result.getEmailAddress());
        Assertions.assertEquals(studentEntity.getCurrentScore(), result.getCurrentScore());
        Assertions.assertEquals(studentEntity.getCellNumber(), result.getCellNumber());
    }

    @Test
    public void testSaveStudentRequestWithID() {
        StudentEntity studentEntity = StudentEntity.builder()
                .id("FL-123456")
                .firstName("First")
                .lastName("Last")
                .dateOfBirth(new Date())
                .cellNumber("+27123456789")
                .emailAddress("test@email.com")
                .currentScore(12)
                .build();
        Mockito.when(studentRepository.save(Mockito.any(StudentEntity.class)))
                .thenReturn(studentEntity);

        StudentResponse result = studentService.saveStudent(StudentEntity.builder()
                .id("FL-123456")
                .firstName("First")
                .lastName("Last")
                .dateOfBirth(new Date())
                .cellNumber("+27123456789")
                .emailAddress("test@email.com")
                .build());

        Assertions.assertEquals(studentEntity.getId(), result.getId());
        Assertions.assertEquals(studentEntity.getFirstName(), result.getFirstName());
        Assertions.assertEquals(studentEntity.getLastName(), result.getLastName());
        Assertions.assertEquals(studentEntity.getEmailAddress(), result.getEmailAddress());
        Assertions.assertEquals(studentEntity.getCurrentScore(), result.getCurrentScore());
        Assertions.assertEquals(studentEntity.getCellNumber(), result.getCellNumber());
    }

    @Test
    public void testSaveStudentRequestWithoutID() {
        StudentEntity studentEntity = StudentEntity.builder()
                .id("FL-123456")
                .firstName("First")
                .lastName("Last")
                .dateOfBirth(new Date())
                .cellNumber("+27123456789")
                .emailAddress("test@email.com")
                .currentScore(12)
                .build();
        Mockito.when(studentRepository.save(Mockito.any(StudentEntity.class)))
                .thenReturn(studentEntity);

        StudentResponse result = studentService.saveStudent(StudentEntity.builder()
                .firstName("First")
                .lastName("Last")
                .dateOfBirth(new Date())
                .cellNumber("+27123456789")
                .emailAddress("test@email.com")
                .build());

        Assertions.assertEquals("FL-", result.getId().substring(0,3));
        Assertions.assertEquals(studentEntity.getFirstName(), result.getFirstName());
        Assertions.assertEquals(studentEntity.getLastName(), result.getLastName());
        Assertions.assertEquals(studentEntity.getEmailAddress(), result.getEmailAddress());
        Assertions.assertEquals(studentEntity.getCurrentScore(), result.getCurrentScore());
        Assertions.assertEquals(studentEntity.getCellNumber(), result.getCellNumber());
        Assertions.assertTrue(true);
    }

    @Test
    public void testDeleteStudent() {
        String id = "string-id";
        studentService.deleteStudent(id);
        Mockito.verify(studentRepository, Mockito.times(1)).deleteById(id);
    }

    @Test
    public void testFindStudentsByCriteria() {
        CriteriaBuilder criteriaBuilder = Mockito.mock(CriteriaBuilder.class);
        CriteriaQuery<StudentEntity> criteriaQuery = Mockito.mock(CriteriaQuery.class);
        Root<StudentEntity> root = Mockito.mock(Root.class);
        TypedQuery<StudentEntity> typedQuery = Mockito.mock(TypedQuery.class);

        Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        Mockito.when(criteriaBuilder.createQuery(StudentEntity.class)).thenReturn(criteriaQuery);
        Mockito.when(criteriaQuery.from(StudentEntity.class)).thenReturn(root);
        Mockito.when(criteriaBuilder.equal(root.get(Mockito.anyString()), "null")).thenReturn(Mockito.mock(Predicate.class));
        Mockito.when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);

        List<StudentEntity> studentEntities = Arrays.asList(
                StudentEntity.builder().firstName("null").lastName("TempLast1").dateOfBirth(new Date()).cellNumber("+27123456789").emailAddress("test@email.com").build(),
                StudentEntity.builder().firstName("null").lastName("TempLast2").dateOfBirth(new Date()).cellNumber("+27123456789").emailAddress("test@email.com").build());

        Mockito.when(typedQuery.getResultList()).thenReturn(studentEntities);

        CriteriaSearch criteriaSearch = new CriteriaSearch("null", "firstName");

        List<StudentEntity> result = studentService.findStudentsByCriteria(criteriaSearch);

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(studentEntities.get(0), result.get(0));
        Assertions.assertEquals(studentEntities.get(1), result.get(1));

        Mockito.verify(criteriaBuilder).equal(root.get(criteriaSearch.getPropertyName()), criteriaSearch.getSearchValue());
        Mockito.verify(entityManager).createQuery(criteriaQuery);
        Mockito.verify(typedQuery).getResultList();
    }
}