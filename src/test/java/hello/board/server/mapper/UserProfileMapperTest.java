package hello.board.server.mapper;

import hello.board.server.dto.UserDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;

import static hello.board.server.dto.UserDto.Status;
import static hello.board.server.utils.SHA256Util.encrptySHA256;
import static org.assertj.core.api.Assertions.assertThat;

@MybatisTest
class UserProfileMapperTest {

    @Autowired
    private UserProfileMapper userProfileMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("INSERT INTO users (userId, password, nickname, status, isAdmin, isWithDraw, createTime, updateTime) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                "tester", encrptySHA256("test1234"), "테스터", "DEFAULT", false, false, LocalDateTime.now(), LocalDateTime.now());
    }

    @AfterEach
    void tearDown() {
        jdbcTemplate.update("DELETE FROM users");
    }

    @Test
    void register() {
        UserDto userDto = new UserDto("tester2", encrptySHA256("test1234"), "테스터", false, Status.DEFAULT, LocalDateTime.now(), null);

        int count = userProfileMapper.register(userDto);

        assertThat(count).isEqualTo(1);
    }

    @Test
    void findByIdAndPassword() {
        String userId = "tester";
        String password = encrptySHA256("test1234");

        UserDto result = userProfileMapper.findByIdAndPassword(userId, password);

        assertThat(result).extracting("userId", "password")
                .containsExactly(userId, password);
    }

    @Test
    void findByIdAndPassword_데이터가없는경우_null반환한다() {
        String userId = "unknown";
        String password = encrptySHA256("password");
        UserDto result = userProfileMapper.findByIdAndPassword(userId, password);

        assertThat(result).isNull();
    }

    @Test
    void idCheck_유일한_아이디면_0을_반환한다() {
        int result = userProfileMapper.idCheck("uniqueUserId");

        assertThat(result).isEqualTo(0);
    }

    @Test
    void idCheck_중복_아이디면_1을_반환한다() {
        int result = userProfileMapper.idCheck("tester");

        assertThat(result).isEqualTo(1);
    }

    @Test
    void getUserProfile() {
        UserDto result = userProfileMapper.getUserProfile("tester");
        assertThat(result.getUserId()).isEqualTo("tester");
    }

    @Test
    void getUserProfile_유저정보가_없으면_null반환한다() {
        UserDto result = userProfileMapper.getUserProfile("unknown");

        assertThat(result).isNull();
    }

    @Test
    void updatePassword() {
        UserDto userDto = userProfileMapper.getUserProfile("tester");
        String afterPassword = encrptySHA256("qwer1234!@#$");
        userDto.setPassword(afterPassword);

        int count = userProfileMapper.updatePassword(userDto);
        UserDto updated = userProfileMapper.getUserProfile("tester");

        assertThat(count).isEqualTo(1);
        assertThat(updated).extracting("userId", "password")
                .containsExactly("tester", afterPassword);
    }

    @Test
    void deleteByUserId() {
        int count = userProfileMapper.deleteByUserId("tester");
        assertThat(count).isEqualTo(1);
    }


    @Test
    void deleteByUserId_유저가없으면_0을_반환한다() {
        int count = userProfileMapper.deleteByUserId("unknown");

        assertThat(count).isEqualTo(0);
    }
}
