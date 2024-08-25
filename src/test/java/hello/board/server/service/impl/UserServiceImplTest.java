package hello.board.server.service.impl;

import hello.board.server.dto.UserDto;
import hello.board.server.exception.DuplicateIdException;
import hello.board.server.mapper.UserProfileMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import static hello.board.server.dto.UserDto.Status;
import static hello.board.server.utils.SHA256Util.encrptySHA256;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserProfileMapper userProfileMapper;

    @Mock
    private DuplicatedIdChecker duplicatedIdChecker;

    @InjectMocks
    private UserServiceImpl userService;


    @Test
    void 회원가입을_한다() {
        UserDto userDto = new UserDto("tester", "test1234", "테스터", false, Status.DEFAULT, null, null);

        given(duplicatedIdChecker.isDuplicatedId(anyString()))
                .willReturn(false);
        given(userProfileMapper.register(any()))
                .willReturn(1);

        assertThatNoException()
                .isThrownBy(() -> userService.register(userDto));

        verify(userProfileMapper, times(1)).register(userDto);
    }

    @Test
    void 회원가입시_아이디가_중복되는경우_예외를던진다() {
        UserDto userDto = new UserDto("tester", "test1234", "테스터", false, Status.DEFAULT, null, null);

        given(duplicatedIdChecker.isDuplicatedId(anyString()))
                .willReturn(true);

        assertThatThrownBy(() -> userService.register(userDto))
                .isInstanceOf(DuplicateIdException.class)
                .hasMessage("중복된 아이디 입니다");
    }

    @Test
    void 회원가입_실패한_경우_런타임예외를_던진다() {
        UserDto userDto = new UserDto("tester", "test1234", "테스터", false, Status.DEFAULT, null, null);

        given(duplicatedIdChecker.isDuplicatedId(anyString()))
                .willReturn(false);
        given(userProfileMapper.register(any()))
                .willThrow(new DataIntegrityViolationException(""));

        assertThatThrownBy(() -> userService.register(userDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Database error occurred during user registration.");
    }

    @Test
    void 로그인() {
        String userId = "tester";
        String password = "test1234";

        UserDto userDto = new UserDto("tester", encrptySHA256(password), "테스터", false, Status.DEFAULT, null, null);

        given(userProfileMapper.findByIdAndPassword(anyString(), anyString()))
                .willReturn(userDto);

        UserDto result = userService.login(userId, password);

        assertThat(result.getUserId()).isEqualTo(userId);
        assertThat(result.getPassword()).isEqualTo(encrptySHA256(password));
    }

    @Test
    void 아이디로_정보를_조회한다() {
        UserDto userDto = new UserDto("tester", encrptySHA256("test1234"), "테스터", false, Status.DEFAULT, null, null);
        given(userProfileMapper.getUserProfile(anyString()))
                .willReturn(userDto);

        String userId = "tester";
        UserDto result = userService.getUserInfo(userId);

        assertThat(result.getUserId()).isEqualTo(userId);
    }

    @Test
    void 비밀번호를_수정한다() {
        String userId = "tester";
        String beforePassword = "test1234";
        String afterPassword = "qwer!@#$";

        given(userProfileMapper.findByIdAndPassword(anyString(), anyString()))
                .willReturn(new UserDto("tester", encrptySHA256("test1234"), "테스터", false, Status.DEFAULT, null, null));

        userService.updatePassword(userId, beforePassword, afterPassword);

        verify(userProfileMapper).updatePassword(any());
    }

    @Test
    void 비밀번호수정시_회원정보가_조회되지않으면_예외를_던진다() {
        given(userProfileMapper.findByIdAndPassword(anyString(), anyString()))
                .willReturn(null);

        assertThatThrownBy(() -> userService.updatePassword("unknown", "password", "password2"))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @Test
    void 회원을_삭제한다() {
        String userId = "tester";
        String password = "test1234";

        given(userProfileMapper.findByIdAndPassword(anyString(), anyString()))
                .willReturn(new UserDto("tester", encrptySHA256("test1234"), "테스터", false, Status.DEFAULT, null, null));

        userService.deleteUser(userId, password);

        verify(userProfileMapper).deleteByUserId(userId);
    }

    @Test
    void 삭제시_회원정보_조회되지않으면_예외를_던진다() {
        given(userProfileMapper.findByIdAndPassword(anyString(), anyString()))
                .willReturn(null);

        assertThatThrownBy(() -> userService.deleteUser("unknown", "password"))
                .isInstanceOf(RuntimeException.class);
    }
}
