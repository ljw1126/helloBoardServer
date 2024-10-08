package hello.board.server.service.impl;

import hello.board.server.dto.CategoryDto;
import hello.board.server.dto.request.CategoryRequest;
import hello.board.server.mapper.CategoryMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    void register() {
        CategoryDto categoryDto = CategoryDto.from(new CategoryRequest(0, "new category"));

        when(categoryMapper.register(any()))
                .thenReturn(1);

        assertThatNoException()
                .isThrownBy(() -> categoryService.register(categoryDto));

        verify(categoryMapper, times(1)).register(categoryDto);
    }

    @Test
    void 신규생성시_dto가_null이면_예외를_던진다() {
        assertThatThrownBy(() -> categoryService.register(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void update() {
        CategoryDto categoryDto = new CategoryDto();
        when(categoryMapper.update(categoryDto))
                .thenReturn(1);

        assertThatNoException()
                .isThrownBy(() -> categoryService.update(categoryDto));

        verify(categoryMapper, times(1)).update(categoryDto);
    }

    @Test
    void 수정시_dto가_null이면_예외를_던진다() {
        assertThatThrownBy(() -> categoryService.update(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void deleteById() {
        when(categoryMapper.deleteById(anyInt()))
                .thenReturn(1);

        assertThatNoException()
                .isThrownBy(() -> categoryService.deleteById(1));

        verify(categoryMapper, times(1)).deleteById(1);
    }

    @Test
    void 아이디값이_1보다_작으면_예외를_던진다() {
        assertThatThrownBy(() -> categoryService.deleteById(0))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
