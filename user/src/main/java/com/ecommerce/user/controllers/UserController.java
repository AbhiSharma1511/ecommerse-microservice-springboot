package com.ecommerce.user.controllers;

import com.ecommerce.user.dto.ErrorResponse;
import com.ecommerce.user.dto.UserRequest;
import com.ecommerce.user.dto.UserResponse;
import com.ecommerce.user.models.User;
import com.ecommerce.user.models.UserRole;
import com.ecommerce.user.services.UserService;
import com.ecommerce.user.utils.UserUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        System.out.println("REQUEST RECEIVED TO GET ALL USERS");
        return new ResponseEntity<>(userService.getAllUsers(),HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> addUser(@RequestBody UserRequest userRequest) {
        User exitedUser = userService.getUserByEmail(userRequest.getEmail());

        if(exitedUser != null){
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse("User already exists!"));
        }

        User createduser = userService.addUser(userRequest);
        if(createduser != null){
            return ResponseEntity.ok("User has been created");
        }
        else
            return ResponseEntity.badRequest().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        try {
            User user = userService.getUserById(id);
            UserResponse userResponse = UserUtils.mapToUserResponse(user);
            return new ResponseEntity<>(userResponse, HttpStatus.OK);
        }
        catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(e.getMessage()));
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable String id ,@RequestBody UserRequest userRequest) {
        try {
            boolean userUpdate =  userService.updateUser(id, userRequest);
            System.out.println(userRequest);
            if(userUpdate){
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("User has been updated"));
            }
            else{
                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ErrorResponse("Server error"));
            }
        }
        catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/delete-all")
    public boolean deleteAllUser(@RequestBody User user) {
        if(user.getUserId()==null || user.getUserId().isEmpty())
            return false;
        User admin = userService.getUserById(user.getUserId());

        if(admin==null){
            return false;
        }
        else{
            if(admin.getRole() == UserRole.ADMIN){
                if(admin.getPassword().equals(user.getPassword())){
                    return userService.removeAllUser();
                }
                else {
                    return false;
                }
            }
            else{
                return false;
            }
        }
    }

}
