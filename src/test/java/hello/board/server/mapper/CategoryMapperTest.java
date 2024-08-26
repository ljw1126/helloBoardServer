package hello.board.server.mapper;

import hello.board.server.dto.CategoryDto;
import hello.board.server.dto.request.CategoryRequest;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@MybatisTest
class CategoryMapperTest {

    @Autowired
    private CategoryMapper categoryMapper;

    @Test
    void register() {
        CategoryDto categoryDto = CategoryDto.from(new CategoryRequest(0, "new category"));

        int count = categoryMapper.register(categoryDto);

        assertThat(count).isEqualTo(1);
        assertThat(categoryDto.getId()).isPositive();
    }

    @Test
    void update() {
        CategoryDto categoryDto = CategoryDto.from(new CategoryRequest(0, "new category"));
        categoryMapper.register(categoryDto);

        categoryDto.setName("updated Category");
        int count = categoryMapper.update(categoryDto);

        assertThat(count).isEqualTo(1);
    }

    @Test
    void deleteById() {
        CategoryDto categoryDto = CategoryDto.from(new CategoryRequest(0, "new category"));
        categoryMapper.register(categoryDto);

        int count = categoryMapper.deleteById(categoryDto.getId());

        assertThat(count).isEqualTo(1);
    }
}
