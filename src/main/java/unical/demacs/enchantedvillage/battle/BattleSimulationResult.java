package unical.demacs.enchantedvillage.battle;

public class BattleSimulationResult {
    private final boolean victory;
    private final int percentageDestroyed;

    public BattleSimulationResult(boolean victory, int percentageDestroyed) {
        this.victory = victory;
        this.percentageDestroyed = percentageDestroyed;
    }

    public boolean isVictory() {
        return victory;
    }

    public int getPercentageDestroyed() {
        return percentageDestroyed;
    }
}