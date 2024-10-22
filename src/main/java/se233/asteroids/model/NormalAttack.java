package se233.asteroids.model;

public class NormalAttack extends Projectile {
    private static final String LASER_SPRITE = "/se233/asteroids/assets/projectile/Charge_1.png";

    public NormalAttack(double x, double y, double maxSpeed, double acceleration, double friction, double width, double height) {
        super(LASER_SPRITE, 20, 20, x, y, maxSpeed, acceleration, friction, width, height);
    }
}
