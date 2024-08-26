package hello.board.server.controller;

import hello.board.server.aop.LoginCheck;
import hello.board.server.dto.CategoryDto;
import hello.board.server.dto.request.CategoryRequest;
import hello.board.server.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @LoginCheck(type = LoginCheck.UserType.ADMIN)
    @PostMapping
    public ResponseEntity<Void> register(String userId, @RequestBody CategoryDto categoryDto) {
        categoryService.register(categoryDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @LoginCheck(type = LoginCheck.UserType.ADMIN)
    @PatchMapping
    public ResponseEntity<Void> update(String userId,
                                       @RequestBody CategoryRequest categoryRequest) {
        categoryService.update(CategoryDto.from(categoryRequest));
        return ResponseEntity.ok().build();
    }

    @LoginCheck(type = LoginCheck.UserType.ADMIN)
    @DeleteMapping("{categoryId}")
    public ResponseEntity<Void> delete(String userId, @PathVariable(name = "categoryId") int categoryId) {
        categoryService.deleteById(categoryId);
        return ResponseEntity.ok().build();
    }
}
