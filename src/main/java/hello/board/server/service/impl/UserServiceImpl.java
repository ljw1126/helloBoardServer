package hello.board.server.service.impl;

import hello.board.server.dto.UserDto;
import hello.board.server.exception.DuplicateIdException;
import hello.board.server.exception.UserRegistrationFailedException;
import hello.board.server.mapper.UserProfileMapper;
import hello.board.server.service.IdChecker;
import hello.board.server.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static hello.board.server.utils.SHA256Util.encrptySHA256;

@Slf4j
@Transactional
@Service
public class UserServiceImpl implements UserService {

    private final UserProfileMapper userProfileMapper;
    private final IdChecker idChecker;

    public UserServiceImpl(UserProfileMapper userProfileMapper, IdChecker idChecker) {
        this.userProfileMapper = userProfileMapper;
        this.idChecker = idChecker;
    }

    @Override
    public void register(UserDto userDto) {
        if (idChecker.isDuplicatedId(userDto.getUserId())) {
            throw new DuplicateIdException("중복된 아이디 입니다");
        }

        userDto.setCreateTime(LocalDateTime.now());
        userDto.setPassword(encrptySHA256(userDto.getPassword()));

        try {
            int insertCount = userProfileMapper.register(userDto);
            if (insertCount != 1) {
                throw new IllegalStateException("register ERROR!\n" + userDto);
            }
        } catch (DataAccessException e) {
            throw new UserRegistrationFailedException("Database error occurred during user registration.", e);
        } catch (Exception e) {
            throw new UserRegistrationFailedException("An unexpected error occurred during user registration.", e);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public UserDto login(String userId, String password) {
        String cryptoPassword = encrptySHA256(password);
        return userProfileMapper.findByIdAndPassword(userId, cryptoPassword);
    }

    @Transactional(readOnly = true)
    @Override
    public UserDto getUserInfo(String userId) {
        return userProfileMapper.getUserProfile(userId);
    }

    @Override
    public void updatePassword(String userId, String beforePassword, String afterPassword) {
        String cryptoPassword = encrptySHA256(beforePassword);
        UserDto userDto = userProfileMapper.findByIdAndPassword(userId, cryptoPassword);

        if (userDto == null) {
            throw new IllegalArgumentException("updatePassword ERROR!\n" + userId);
        }

        userDto.setPassword(encrptySHA256(afterPassword));
        userDto.setUpdateTime(LocalDateTime.now());
        userProfileMapper.updatePassword(userDto);
    }

    @Override
    public void deleteUser(String userId, String password) {
        String cryptoPassword = encrptySHA256(password);
        UserDto userDto = userProfileMapper.findByIdAndPassword(userId, cryptoPassword);

        if (userDto == null) {
            throw new IllegalStateException("deleteId ERROR!\n" + "Params : " + userId);
        }

        userProfileMapper.deleteByUserId(userDto.getUserId());
    }
}
