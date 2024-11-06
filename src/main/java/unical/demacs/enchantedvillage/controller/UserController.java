package unical.demacs.enchantedvillage.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import unical.demacs.enchantedvillage.config.handler.exception.UserException;
import unical.demacs.enchantedvillage.persistence.dto.UserDTO;
import unical.demacs.enchantedvillage.persistence.service.implementation.UserServiceImpl;

@RestController
@RequestMapping(value = "/api/v1/user", produces = "application/json")
@CrossOrigin
@AllArgsConstructor
@Tag(name = "user-controller", description = "Operations related to user management.")
public class UserController {
    private final UserServiceImpl userServiceImpl;
    private final ModelMapper modelMapper;

    @Operation(summary = "Get user by email", description = "Retrieve a user's details using their email address.",
            tags = {"user-controller"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User retrieved successfully.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "404", description = "User not found.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Server error. Please try again later.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
    })
    @GetMapping(path="/getUser/{email}")
    //@PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_USER') and email.equals(authentication.principal.username))")
    public ResponseEntity<UserDTO> getUser(@PathVariable("email") String email) {
        return ResponseEntity.ok(modelMapper.map(userServiceImpl.getUserByEmail(email), UserDTO.class));
    }


    @Operation(summary = "Update user details", description = "Update the details of a user.",
            tags = {"user-controller"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User updated successfully.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Boolean.class))),
            @ApiResponse(responseCode = "400", description = "Bad request: Invalid user data.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Server error. Please try again later.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
    })
    @PatchMapping(path="/updateUser")
    @PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_USER') and #userDTO.getEmail().equals(authentication.principal.username))")
    public ResponseEntity<Boolean> updateUser(@RequestBody @Valid UserDTO userDTO) {

        try {
            return ResponseEntity.ok(userServiceImpl.updateUser(userDTO));
        }catch(UserException u){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
        }
    }


    @Operation(summary = "Exists by email", description = "Check if a user exists using their email address.",
            tags = {"user-controller"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User exists.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Boolean.class))),
            @ApiResponse(responseCode = "404", description = "User does not exist.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Boolean.class))),
            @ApiResponse(responseCode = "500", description = "Server error. Please try again later.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
    })
    @GetMapping(path="/existsByEmail/{email}")
    //@PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_USER') and email.equals(authentication.principal.username))")
    public ResponseEntity<Boolean> existsByEmail(@PathVariable("email") String email) {
        return ResponseEntity.ok(userServiceImpl.existsByEmail(email));
    }
}
