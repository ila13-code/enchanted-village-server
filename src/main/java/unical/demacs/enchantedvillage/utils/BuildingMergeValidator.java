package unical.demacs.enchantedvillage.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import unical.demacs.enchantedvillage.buildings.BuildingData;
import unical.demacs.enchantedvillage.buildings.converter.BuildingDataConverter;
import unical.demacs.enchantedvillage.config.handler.exception.InvalidBuildingDataException;
import unical.demacs.enchantedvillage.persistence.dto.GameInformationDTO;
import unical.demacs.enchantedvillage.persistence.entities.GameInformation;
import unical.demacs.enchantedvillage.persistence.service.implementation.GameInformationServiceImpl;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BuildingMergeValidator {
    private static final int RESOURCE_GENERATION_INTERVAL_MINUTES = 5;
    private static final int GOLD_GENERATION_AMOUNT = 50;
    private static final int ELIXIR_GENERATION_AMOUNT = 50;

    public static final Logger log = LoggerFactory.getLogger(BuildingMergeValidator.class);

    public List<BuildingData> validateAndMergeBuildings(GameInformation serverState, GameInformationDTO clientState) {
        ResourcePair maxLegitResources = calculateMaxLegitimateResources(
                serverState.getGold(),
                serverState.getElixir(),
                serverState.getLastSyncTimestamp()
        );

        Map<String, BuildingData> serverBuildings = serverState.getBuildingData().stream()
                .collect(Collectors.toMap(BuildingData::getUniqueId, b -> b));

        Map<String, BuildingData> clientBuildings = clientState.getBuildings().stream()
                .collect(Collectors.toMap(BuildingData::getUniqueId, b -> b));

        if (clientState.getGold() > maxLegitResources.gold ||
                clientState.getElixir() > maxLegitResources.elixir) {
            log.warn("Client reported illegitimate resource amounts. Max legitimate: gold={}, elixir={}, Client reported: gold={}, elixir={}",
                    maxLegitResources.gold, maxLegitResources.elixir,
                    clientState.getGold(), clientState.getElixir());
            log.warn("Client reported illegitimate resource amounts");
            return serverState.getBuildingData();
        }

        Set<String> newBuildingIds = new HashSet<>(clientBuildings.keySet());
        newBuildingIds.removeAll(serverBuildings.keySet());

        int totalGoldCost = 0;
        int totalElixirCost = 0;

        for (String newBuildingId : newBuildingIds) {
            BuildingData newBuilding = clientBuildings.get(newBuildingId);
            try {
                BuildingCosts buildingType = BuildingCosts.fromPrefabIndex(newBuilding.getPrefabIndex());
                totalGoldCost += buildingType.getGoldCost();
                totalElixirCost += buildingType.getElixirCost();
            } catch (IllegalArgumentException e) {
                log.error("Invalid building prefab index: {}", newBuilding.getPrefabIndex());
                log.warn("Invalid building type received from client");
                return serverState.getBuildingData();
            }
        }

        if (totalGoldCost > maxLegitResources.gold || totalElixirCost > maxLegitResources.elixir) {
            log.warn("Client attempted to add buildings requiring more resources than possible. " +
                            "Required: gold={}, elixir={}. Max legitimate: gold={}, elixir={}",
                    totalGoldCost, totalElixirCost, maxLegitResources.gold, maxLegitResources.elixir);
            log.warn("Attempted to add buildings without sufficient resources");
            return serverState.getBuildingData();
        }

        List<BuildingData> mergedBuildings = new ArrayList<>();

        for (BuildingData serverBuilding : serverState.getBuildingData()) {
            String buildingId = serverBuilding.getUniqueId();
            if (clientBuildings.containsKey(buildingId)) {
                mergedBuildings.add(clientBuildings.get(buildingId));
            } else {
                mergedBuildings.add(serverBuilding);
            }
        }

        for (String newBuildingId : newBuildingIds) {
            mergedBuildings.add(clientBuildings.get(newBuildingId));
        }

        return mergedBuildings;
    }

    private ResourcePair calculateMaxLegitimateResources(int baseGold, int baseElixir, LocalDateTime lastSync) {
        long minutesSinceLastSync = ChronoUnit.MINUTES.between(lastSync, LocalDateTime.now());
        int resourceGenerationCycles = (int) (minutesSinceLastSync / RESOURCE_GENERATION_INTERVAL_MINUTES);

        int generatedGold = resourceGenerationCycles * GOLD_GENERATION_AMOUNT;
        int generatedElixir = resourceGenerationCycles * ELIXIR_GENERATION_AMOUNT;

        return new ResourcePair(
                baseGold + generatedGold,
                baseElixir + generatedElixir
        );
    }

    @lombok.Data
    @lombok.AllArgsConstructor
    private static class ResourcePair {
        private int gold;
        private int elixir;
    }
}