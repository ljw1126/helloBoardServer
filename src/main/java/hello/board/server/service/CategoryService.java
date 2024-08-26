package hello.board.server.service;

import hello.board.server.dto.CategoryDto;

public interface CategoryService {
    void register(CategoryDto categoryDto);

    void update(CategoryDto categoryDto);

    void deleteById(int categoryId);
}
