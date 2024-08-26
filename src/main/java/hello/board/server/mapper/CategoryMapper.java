package hello.board.server.mapper;

import hello.board.server.dto.CategoryDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper {
    int register(CategoryDto categoryDto);

    int update(CategoryDto categoryDto);

    int deleteById(int categoryId);
}
