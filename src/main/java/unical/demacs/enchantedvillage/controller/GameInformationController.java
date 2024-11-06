package unical.demacs.enchantedvillage.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import unical.demacs.enchantedvillage.persistence.dto.GameInformationDTO;
import unical.demacs.enchantedvillage.persistence.dto.UserDTO;
import unical.demacs.enchantedvillage.persistence.service.implementation.GameInformationServiceImpl;

@RestController
@RequestMapping(value = "/api/v1/game-information", produces = "application/json")
@AllArgsConstructor
@Tag(name = "game-information-controller", description = "Operations related to game information management.")
public class GameInformationController {

    private final ModelMapper modelMapper;
    private final GameInformationServiceImpl gameInformationServiceImpl;


    @Operation(summary = "Get game information by user email", description = "Retrieve a user's game information using their email address.",
            tags = {"game-information-controller"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Game information retrieved successfully.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "404", description = "Game information not found.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Server error. Please try again later.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
    })
    @GetMapping(path="/getGameInformation")
    @PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_USER') and email.equals(authentication.principal.username))")
    public ResponseEntity<GameInformationDTO> getGameInformation(@RequestParam("email") String email) {
        return ResponseEntity.ok(modelMapper.map(gameInformationServiceImpl.getGameInformation(email), GameInformationDTO.class));
    }

    @Operation(summary = "Create game information", description = "Create a new game information for a user.",
            tags = {"game-information-controller"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Game information created successfully.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Boolean.class))),
            @ApiResponse(responseCode = "400", description = "Bad request: Invalid game information data.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Server error. Please try again later.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
    })
    @PostMapping(path="/createGameInformation")
    @PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_USER') and email.equals(authentication.principal.username))")
    public ResponseEntity<GameInformationDTO> createGameInformation(@RequestParam("email") String email, @RequestBody GameInformationDTO gameInformationDTO) {
        return ResponseEntity.ok(modelMapper.map(gameInformationServiceImpl.createGameInformation(email, gameInformationDTO), GameInformationDTO.class));
    }

    @Operation(summary = "Update game information", description = "Update the details of a user's game information.",
            tags = {"game-information-controller"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Game information updated successfully.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Boolean.class))),
            @ApiResponse(responseCode = "400", description = "Bad request: Invalid game information data.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Server error. Please try again later.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
    })
    @PatchMapping(path="/updateGameInformation")
    @PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_USER') and email.equals(authentication.principal.username))")
    public ResponseEntity<GameInformationDTO> updateGameInformation(@RequestParam("email") String email, @RequestBody GameInformationDTO gameInformationDTO) {
        return ResponseEntity.ok(modelMapper.map(gameInformationServiceImpl.updateGameInformation(email, gameInformationDTO), GameInformationDTO.class));
    }

    @Operation(summary = "Delete game information", description = "Delete the game information of a user.",
            tags = {"game-information-controller"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Game information deleted successfully.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Boolean.class))),
            @ApiResponse(responseCode = "400", description = "Bad request: Invalid game information data.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Server error. Please try again later.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
    })
    @DeleteMapping(path="/deleteGameInformation")
    @PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_USER') and email.equals(authentication.principal.username))")
    public ResponseEntity<Boolean> deleteGameInformation(@RequestParam("email") String email) {
        gameInformationServiceImpl.deleteGameInformation(email);
        return ResponseEntity.ok(true);
    }
}
