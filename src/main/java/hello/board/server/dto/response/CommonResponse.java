package hello.board.server.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.OK;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse<T> {

    private HttpStatus status;
    private int code;
    private String message;
    private T body;

    public static <T> CommonResponse<T> success(String message, T body) {
        return new CommonResponse<>(OK, OK.value(), message, body);
    }
}
