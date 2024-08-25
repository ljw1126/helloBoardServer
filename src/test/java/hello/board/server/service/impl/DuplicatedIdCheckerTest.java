package hello.board.server.service.impl;

import hello.board.server.mapper.UserProfileMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class DuplicatedIdCheckerTest {

    @Mock
    private UserProfileMapper userProfileMapper;

    @InjectMocks
    private DuplicatedIdChecker duplicatedIdChecker;

    @Test
    void 아이디가_유일하면_거짓을_반환한다() {
        given(userProfileMapper.idCheck(anyString()))
                .willReturn(0);

        boolean result = duplicatedIdChecker.isDuplicatedId("test");
        assertThat(result).isFalse();
    }

    @Test
    void 아이디가_중복이면_참을_반환한다() {
        given(userProfileMapper.idCheck(anyString()))
                .willReturn(1);

        boolean result = duplicatedIdChecker.isDuplicatedId("test");
        assertThat(result).isTrue();
    }

}
