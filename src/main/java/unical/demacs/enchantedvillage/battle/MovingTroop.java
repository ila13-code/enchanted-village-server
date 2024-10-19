package unical.demacs.enchantedvillage.battle;

import unical.demacs.enchantedvillage.buildings.TroopsType;

public class MovingTroop {
    private TroopsType type;
    private int x, y;
    private int health;
    private int attackRange;
    private int attackDamage;
    private int moveSpeed;

    public MovingTroop(TroopsType type, int x, int y) {
        this.type = type;
        this.x = x;
        this.y = y;
        initializeTroopStats();
    }

    private void initializeTroopStats() {
        switch (type) {
            case SWORDSMAN:
                health = 150;
                attackRange = 1;
                attackDamage = 30;
                moveSpeed = 1;
                break;
            case ARCHER:
                health = 100;
                attackRange = 4;
                attackDamage = 20;
                moveSpeed = 2;
                break;
            case VIKING:
                health = 200;
                attackRange = 1;
                attackDamage = 40;
                moveSpeed = 1;
                break;
        }
    }

    public void move(int dx, int dy) {
        x += dx * moveSpeed;
        y += dy * moveSpeed;
    }

    public int attack() {
        return attackDamage;
    }

    public void receiveDamage(int damage) {
        health -= damage;
        if (health < 0) health = 0;
    }

    public boolean isAlive() {
        return health > 0;
    }

    // Getters
    public int getX() { return x; }
    public int getY() { return y; }
    public int getAttackRange() { return attackRange; }
    public TroopsType getType() { return type; }
}