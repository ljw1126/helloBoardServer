package hello.board.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.board.server.dto.CategoryDto;
import hello.board.server.dto.request.CategoryRequest;
import hello.board.server.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = CategoryController.class)
class CategoryControllerTest {

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void register() throws Exception {
        doNothing()
                .when(categoryService).register(any());

        CategoryDto categoryDto = CategoryDto.from(new CategoryRequest(0, "new category"));

        mockMvc.perform(post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryDto))
        ).andExpect(status().isCreated());
    }

    @Test
    void update() throws Exception {
        doNothing()
                .when(categoryService).update(any());

        CategoryRequest updatedCategory = new CategoryRequest(1, "updated category");
        mockMvc.perform(patch("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedCategory))
        ).andExpect(status().isOk());
    }

    @Test
    void deleteById() throws Exception {
        doNothing()
                .when(categoryService).deleteById(anyInt());

        mockMvc.perform(delete("/categories/1")
        ).andExpect(status().isOk());

        verify(categoryService, times(1)).deleteById(1);
    }
}
