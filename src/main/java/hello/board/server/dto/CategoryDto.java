package hello.board.server.dto;

import hello.board.server.dto.request.CategoryRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {

    public enum SortStatus {
        CATEGORIES, NEWEST, OLDEST, HIGH_PRICE, LOW_PRICE, GRADE
    }

    private int id;
    private String name;
    private SortStatus sortStatus;
    private int searchCount;
    private int pagingStartOffset;

    public static CategoryDto from(CategoryRequest request) {
        return new CategoryDto(request.getId(), request.getName(), SortStatus.NEWEST, 10, 1);
    }
}
