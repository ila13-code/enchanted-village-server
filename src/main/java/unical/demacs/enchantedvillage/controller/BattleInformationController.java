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
import org.springframework.web.bind.annotation.*;
import unical.demacs.enchantedvillage.persistence.dto.BattleInformationDTO;
import unical.demacs.enchantedvillage.persistence.dto.GameInformationDTO;
import unical.demacs.enchantedvillage.persistence.dto.UserDTO;
import unical.demacs.enchantedvillage.persistence.entities.BattleInformation;
import unical.demacs.enchantedvillage.persistence.entities.User;
import unical.demacs.enchantedvillage.persistence.service.implementation.BattleInformationServiceImpl;
import unical.demacs.enchantedvillage.persistence.service.implementation.GameInformationServiceImpl;

@RestController
@RequestMapping(value = "/api/v1/battle-information", produces = "application/json")
@AllArgsConstructor
@Tag(name = "battle-information-controller", description = "Operations related to battle information management.")
public class BattleInformationController {


    // api/v1/battle-information/available/{playerEmail}
    private final ModelMapper modelMapper;
    private final BattleInformationServiceImpl battleInformationServiceImpl;


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
    //@PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_USER') and email.equals(authentication.principal.username))")
    public ResponseEntity<User> getUser(@RequestParam("userEmail") String userEmail, @RequestParam("enemyEmail") String enemyEmail) {
        return ResponseEntity.ok(modelMapper.map(battleInformationServiceImpl.getUser(enemyEmail), User.class));
    }

    @PostMapping(path="/result")
    public ResponseEntity<BattleInformationDTO> registerResult(@RequestParam("userEmail") String userEmail, @RequestBody BattleInformationDTO battleInformationDTO) {
        return ResponseEntity.ok(modelMapper.map(battleInformationServiceImpl.registerResult(userEmail, battleInformationDTO).get(), BattleInformationDTO.class));
    }

    @GetMapping(path="/last")
    public ResponseEntity<BattleInformationDTO> getLastBattleInformation(@RequestParam("userEmail") String userEmail) {
        return ResponseEntity.ok(modelMapper.map(battleInformationServiceImpl.getLastBattleInformation(userEmail).get(), BattleInformationDTO.class));
    }
}
