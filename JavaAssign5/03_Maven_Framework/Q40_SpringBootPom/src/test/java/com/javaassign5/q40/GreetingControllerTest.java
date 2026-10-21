package com.javaassign5.q40;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Q40: tests the controller without starting a real server. Run with: mvn test */
@WebMvcTest(GreetingController.class)
class GreetingControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void helloUsesName() throws Exception {
        mvc.perform(get("/hello").param("name", "Shrey"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.message").value("Hello, Shrey!"));
    }

    @Test
    void invalidStudentIsRejected() throws Exception {
        mvc.perform(post("/students")
                   .contentType(MediaType.APPLICATION_JSON)
                   .content("{\"name\":\"\",\"year\":9}"))
           .andExpect(status().isBadRequest());
    }
}
