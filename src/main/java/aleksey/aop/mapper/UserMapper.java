package aleksey.aop.mapper;

import aleksey.aop.dto.UserRequest;
import aleksey.aop.dto.UserResponse;
import aleksey.aop.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public static UserResponse map(User user) {
        return UserResponse.builder()
                .name(user.getName())
                .description(user.getDescription())
                .build();
    }

    public static User map(UserRequest user) {
        return User.builder()
                .name(user.getName())
                .description(user.getDescription())
                .build();
    }
}
