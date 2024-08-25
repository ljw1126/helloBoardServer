package hello.board.server.service;

import hello.board.server.dto.UserDto;

public interface UserService {
    void register(UserDto userDto);

    UserDto login(String userId, String password);

    UserDto getUserInfo(String userId);

    void updatePassword(String userId, String beforePassword, String afterPassword);

    void deleteUser(String userId, String password);
}
