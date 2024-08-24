package hello.board.server.mapper;

import hello.board.server.dto.UserDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface UserProfileMapper {

    int register(UserDto userDto);

    UserDto findByIdAndPassword(@Param("userId") String userId, @Param("password") String password);

    int idCheck(@Param("userId") String userId);

    UserDto getUserProfile(@Param("userId") String userId);

    int updatePassword(UserDto userDto);

    int deleteByUserId(@Param("userId") String userId);
}
