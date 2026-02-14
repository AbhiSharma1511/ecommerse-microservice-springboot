package com.ecommerce.user.services;

import com.ecommerce.user.dto.UserRequest;
import com.ecommerce.user.dto.UserResponse;
import com.ecommerce.user.models.User;
import com.ecommerce.user.repository.UserRepository;
import com.ecommerce.user.utils.UserUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User addUser(UserRequest userRequest) {
        User user = new User();
        UserUtils.mapUserRequestToUser(user, userRequest);
        return userRepository.save(user);
    }

    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
//        List<UserResponse> userResponseList = new ArrayList<>();
//        for (User user : users) {
//            UserResponse userResponse = mapToUserResponse(user);
//            userResponseList.add(userResponse);
//        }
//        return userResponseList;
        return userRepository.findAll().stream()
                .map(UserUtils::mapToUserResponse)
                .collect(Collectors.toList());
    }

    public User getUserById(String id) {
        try {
            Optional<User> userOpt = userRepository.findById(id);
            if(userOpt.isEmpty()){
                throw new RuntimeException("User not found");
            }
            return userOpt.get();
        }
        catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    public boolean updateUser(String id ,UserRequest userRequest) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    //System.out.println(userRequest);
                    //System.out.println(existingUser);
                    UserUtils.mapUserRequestToUser(existingUser, userRequest);
                    userRepository.save(existingUser);
                    return true;
                })
                .orElse(false);
    }

    public boolean removeAllUser(){
        userRepository.deleteAll();
        return true;
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
