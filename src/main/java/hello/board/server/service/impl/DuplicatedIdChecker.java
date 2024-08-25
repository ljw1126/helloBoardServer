package hello.board.server.service.impl;

import hello.board.server.mapper.UserProfileMapper;
import hello.board.server.service.IdChecker;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DuplicatedIdChecker implements IdChecker {

    private final UserProfileMapper userProfileMapper;

    public DuplicatedIdChecker(UserProfileMapper userProfileMapper) {
        this.userProfileMapper = userProfileMapper;
    }

    @Transactional(readOnly = true)
    @Override
    public boolean isDuplicatedId(String userId) {
        return userProfileMapper.idCheck(userId) == 1;
    }
}
