package com.example.demo.controller;

import com.example.demo.dto.request.CreateStudentRequest;
import com.example.demo.dto.request.CriteriaSearch;
import com.example.demo.dto.request.UpdateStudentRequest;
import com.example.demo.dto.response.StudentResponse;
import com.example.demo.entity.StudentEntity;
import com.example.demo.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StudentController.class)
public class StudentControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    private UpdateStudentRequest student;
    private List<StudentResponse> studentList;
    private List<StudentEntity> studentEntityList;
    private final CreateStudentRequest createStudentRequest = new CreateStudentRequest("TempName2", "TempLast1", new Date(), "+27123456789", "test@mail.com", 50);
    private final UpdateStudentRequest updateStudentRequest = new UpdateStudentRequest("string-id-1", "TempName2", "TempLast1", new Date(), "+27123456789", "test@mail.com");
    private final StudentResponse studentResponse = new StudentResponse("string-id-1", "TempName2", "TempLast1", new Date(), "+27123456789", "test@mail.com", null, null);

    @BeforeEach
    void setUp() {
        this.studentList = new ArrayList<>();
        this.studentList.add(new StudentResponse("string-id-1", "TempName2", "TempLast1", new Date(),
                "+27123456789", "test@mail.com", 0, 0.0));
        this.studentList.add(new StudentResponse("string-id-2", "TempName3", "TempLast2", new Date(),
                "+27123456789", "test@mail.com", 0, 0.0));
        this.studentList.add(new StudentResponse("string-id-3", "TempName3", "TempLast3", new Date(),
                "+27123456789", "test@mail.com", 0, 0.0));

        StudentEntity studentEntity1 = new StudentEntity("string-id-1", "TempName2", "TempLast1", new Date(), "+27123456789", "test@mail.com", null, null);
        StudentEntity studentEntity2 = new StudentEntity("string-id-2", "TempName3", "TempLast2", new Date(), "+27123456789", "test@mail.com", 68, null);
        StudentEntity studentEntity3 = new StudentEntity("string-id-3", "TempName3", "TempLast3", new Date(), "+27123456789", "test@mail.com", 75, null);

        this.studentEntityList = new ArrayList<>();
        this.studentEntityList.add(studentEntity1);
        this.studentEntityList.add(studentEntity2);
        this.studentEntityList.add(studentEntity3);
    }

    @Test
    public void testGetAllStudentsAPI() throws Exception {
        given(studentService.getAllStudents()).willReturn(studentList);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/experian/student")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(studentList.size()));
    }

    @Test
    public void testGetStudentsByIdAPI() throws Exception {
        given(studentService.getStudentById("string-id-1"))
                .willReturn(StudentEntity.builder()
                        .id("string-id-1")
                        .firstName("TempName2")
                        .lastName("TempLast1")
                        .dateOfBirth(new Date())
                        .cellNumber("+27123456789")
                        .emailAddress("test@mail.com")
                        .build());

        mockMvc.perform(MockMvcRequestBuilders.get("/experian/student/{id}", "string-id-1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.dateOfBirth").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.cellNumber").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.emailAddress").exists());
    }

    @Test
    public void testGetStudentsByIdNotFoundAPI() throws Exception {
        given(studentService.getStudentById("x")).willThrow(RuntimeException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/experian/student/{id}", "x"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateStudentAPI() throws Exception {
        given(studentService.createStudent(any(CreateStudentRequest.class))).willReturn(studentResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/experian/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content((new ObjectMapper()).writeValueAsString(createStudentRequest)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
    }

    @Test
    public void testUpdateStudentAPI() throws Exception {
        given(studentService.updateStudent(any(UpdateStudentRequest.class))).willReturn(studentResponse);

        mockMvc.perform(MockMvcRequestBuilders.put("/experian/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content((new ObjectMapper()).writeValueAsString(updateStudentRequest)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
    }

    @Test
    public void testSaveStudentInvalidNameAPI() throws Exception {
        given(studentService.createStudent(any(CreateStudentRequest.class))).willReturn(studentResponse);

        CreateStudentRequest student = CreateStudentRequest.builder()
                .firstName(null)
                .lastName("TempLast1")
                .dateOfBirth(new Date())
                .cellNumber("+27123456789")
                .emailAddress("test@email.com")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/experian/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content((new ObjectMapper()).writeValueAsString(student)))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].field").value("firstName"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].message").value("First name needs to be provided"));
    }

    @Test
    public void testSaveStudentInvalidLastNameAPI() throws Exception {
        given(studentService.createStudent(any(CreateStudentRequest.class))).willReturn(studentResponse);

        CreateStudentRequest student = CreateStudentRequest.builder()
                .firstName("TempName1")
                .lastName(null)
                .dateOfBirth(new Date())
                .cellNumber("+27123456789")
                .emailAddress("test@email.com")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/experian/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content((new ObjectMapper()).writeValueAsString(student)))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].field").value("lastName"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].message").value("Last name needs to be provided"));
    }

    @Test
    public void testSaveStudentInvalidNullDateAPI() throws Exception {
        given(studentService.createStudent(any(CreateStudentRequest.class))).willReturn(studentResponse);

        CreateStudentRequest student = CreateStudentRequest.builder()
                .firstName("TempName1")
                .lastName("TempLast1")
                .dateOfBirth(null)
                .cellNumber("+27123456789")
                .emailAddress("test@email.com")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/experian/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content((new ObjectMapper()).writeValueAsString(student)))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].field").value("dateOfBirth"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].message").value("Date of birth needs to be provided"));
    }

    @Test
    public void testSaveStudentInvalidInvalidDateAPI() throws Exception {
        given(studentService.createStudent(any(CreateStudentRequest.class))).willReturn(studentResponse);

        Map<String, Object> map = new HashMap<>();
        map.put("firstName", "TempName1");
        map.put("lastName", "TempLast1");
        map.put("dateOfBirth", "2013-12-12");
        map.put("cellNumber", "+27123456789");
        map.put("emailAddress", "test@email.com");

        mockMvc.perform(MockMvcRequestBuilders.put("/experian/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content((new ObjectMapper()).writeValueAsString(map)))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].field").value("dateOfBirth"));
//        Not sure how to do this
//                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].message").value(containsString("Cannot deserialize")));
    }

    @Test
    public void testSaveStudentNullEmptyCellNumberAPI() throws Exception {
        given(studentService.createStudent(any(CreateStudentRequest.class))).willReturn(studentResponse);

        CreateStudentRequest student = CreateStudentRequest.builder()
                .firstName("TempName1")
                .lastName("TempLast1")
                .dateOfBirth(new Date())
                .cellNumber(null)
                .emailAddress("test@email.com")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/experian/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content((new ObjectMapper()).writeValueAsString(student)))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].field").value("cellNumber"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].message").value("Cell number needs to be provided"));
    }

    @Test
    public void testSaveStudentInvalidCellNumberAPI() throws Exception {
        given(studentService.createStudent(any(CreateStudentRequest.class))).willReturn(studentResponse);

        CreateStudentRequest student = CreateStudentRequest.builder()
                .firstName("TempName1")
                .lastName("TempLast1")
                .dateOfBirth(new Date())
                .cellNumber("+271234567891")
                .emailAddress("test@email.com")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/experian/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content((new ObjectMapper()).writeValueAsString(student)))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].field").value("cellNumber"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].message").value("Invalid phone number"));
    }

    @Test
    public void testSaveStudentInvalidEmailAPI() throws Exception {
        given(studentService.createStudent(any(CreateStudentRequest.class))).willReturn(studentResponse);

        CreateStudentRequest student = CreateStudentRequest.builder()
                .firstName("TempName1")
                .lastName("TempLast1")
                .dateOfBirth(new Date())
                .cellNumber("+27123456789")
                .emailAddress("test")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/experian/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content((new ObjectMapper()).writeValueAsString(student)))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].field").value("emailAddress"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].message").value("Invalid email"));
    }

    @Test
    public void testSaveStudentEmptyEmailAPI() throws Exception {
        given(studentService.createStudent(any(CreateStudentRequest.class))).willReturn(studentResponse);

        CreateStudentRequest student = CreateStudentRequest.builder()
                .firstName("TempName1")
                .lastName("TempLast1")
                .dateOfBirth(new Date())
                .cellNumber("+27123456789")
                .emailAddress(null)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/experian/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content((new ObjectMapper()).writeValueAsString(student)))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].field").value("emailAddress"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].message").value("Email needs to be provided"));
    }

    @Test
    public void testDeleteStudentAPI() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/experian/student/string-id")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testSearchStudentAPI() throws Exception {
        given(studentService.findStudentsByCriteria(any(CriteriaSearch.class))).willReturn(studentEntityList);
        mockMvc.perform(MockMvcRequestBuilders.post("/experian/student/search")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content((new ObjectMapper())
                                .writeValueAsString(CriteriaSearch
                                        .builder()
                                        .propertyName("firstName")
                                        .searchValue("searchValue")
                                        .build())))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(studentList.size()));
    }
}
