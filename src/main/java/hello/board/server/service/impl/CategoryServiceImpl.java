package hello.board.server.service.impl;

import hello.board.server.dto.CategoryDto;
import hello.board.server.mapper.CategoryMapper;
import hello.board.server.service.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    @Override
    public void register(CategoryDto categoryDto) {
        if (categoryDto == null) {
            throw new IllegalArgumentException("category register error");
        }

        categoryMapper.register(categoryDto);
    }

    @Override
    public void update(CategoryDto categoryDto) {
        if (categoryDto == null) {
            throw new IllegalArgumentException("category update error");
        }

        categoryMapper.update(categoryDto);
    }

    @Override
    public void deleteById(int categoryId) {
        if (categoryId < 1) {
            throw new IllegalArgumentException("category delete error");
        }

        categoryMapper.deleteById(categoryId);
    }
}
