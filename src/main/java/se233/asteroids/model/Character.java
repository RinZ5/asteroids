package se233.asteroids.model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import se233.asteroids.util.ImageUtil;
import se233.asteroids.util.SpriteUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Character {
    private static final Logger logger = LogManager.getLogger(Character.class);

    protected double x;
    protected double y;
    protected double speed;
    protected double maxSpeed;
    protected double acceleration;
    protected double rotationSpeed;
    protected double friction;
    public double velocityX;
    public double velocityY;
    protected double gameWidth;
    protected double gameHeight;

    protected int health;
    protected ImageView imageView;
    protected AnimatedSprite animatedSprite;

    protected final List<String> currentAnimations;
    protected final Map<String, AnimatedSprite> animations;
    protected final Map<String, double[]> animationOffsets;
    protected final Map<String, Double> animationRotates;

    protected boolean isCharacter = true;

    protected boolean isCollided = false;

    public Character(String imagePath, double width, double height, double x, double y, double initialSpeed, double maxSpeed, double acceleration, double rotationSpeed, double friction, int health, double gameWidth, double gameHeight) {
        this.x = x;
        this.y = y;
        this.speed = initialSpeed;
        this.maxSpeed = maxSpeed;
        this.acceleration = acceleration;
        this.rotationSpeed = rotationSpeed;
        this.friction = friction;
        this.health = health;
        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;
        this.velocityX = 0;
        this.velocityY = 0;

        this.currentAnimations = new ArrayList<>();
        this.animations = new HashMap<>();
        this.animationOffsets = new HashMap<>();
        this.animationRotates = new HashMap<>();

        Image image = ImageUtil.loadImage(imagePath);
        if (image != null) {
            this.animatedSprite = SpriteUtil.createAnimatedSprite(imagePath, 5, 5, 1, 32, 32, width, height);
            if (this.animatedSprite != null) {
                this.animatedSprite.setFitWidth(width);
                this.animatedSprite.setFitHeight(height);
                this.animatedSprite.setPreserveRatio(false);
            } else {
                logger.error("Failed to create animatedSprite for path: {}", imagePath);
            }

            try {
                this.imageView = new ImageView(image);
                this.imageView.setFitWidth(width);
                this.imageView.setFitHeight(height);
                this.imageView.setPreserveRatio(false);
                setX(x);
                setY(y);
            } catch (Exception e) {
                logger.error("Error while setting up ImageView: {}", e.getMessage());
            }
        } else {
            logger.error("Failed to load image: {}", imagePath);
        }
    }

    public void updatePosition() {
        this.imageView.setX(x - imageView.getFitWidth() / 2.0);
        this.imageView.setY(y - imageView.getFitHeight() / 2.0);
    }

    public boolean isCharacter() {
        return this.isCharacter;
    }

    public int getHealth() {
        return this.health;
    }

    public void setX(double x) {
        this.x = x;
        updatePosition();
    }

    public void setY(double y) {
        this.y = y;
        updatePosition();
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getSpeed() {
        return this.speed;
    }

    public double getRotationSpeed() {
        return rotationSpeed;
    }

    public void setRotate(double angle) {
        this.imageView.setRotate(angle);
    }

    public double getRotate() {
        return this.imageView.getRotate();
    }

    public double getVelocityX() {
        return velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public List<String> getCurrentAnimations() {
        return currentAnimations;
    }

    public Map<String, AnimatedSprite> getAnimations() {
        return animations;
    }

    public Map<String, double[]> getAnimationOffsets() {
        return animationOffsets;
    }

    public Map<String, Double> getAnimationRotates() {return animationRotates;}

    public ImageView getImageView() {return this.imageView;}

    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public void collided(boolean player) {
        this.maxSpeed = 0;
    }

    public void moveForward() {
        double angle = Math.toRadians(this.getRotate());
        velocityX += Math.cos(angle) * acceleration;
        velocityY += Math.sin(angle) * acceleration;
        limitSpeed();

        if (!currentAnimations.contains("boost")) {
            currentAnimations.add("boost");
        }
    }

    public void stopMoveForward() {
        currentAnimations.remove("boost");
    }

    public void moveBackward() {
        double angle = Math.toRadians(this.getRotate());
        velocityX -= Math.cos(angle) * acceleration;
        velocityY -= Math.sin(angle) * acceleration;
        limitSpeed();
    }

    public void moveLeft() {
        double angle = Math.toRadians(this.getRotate() - 90);
        velocityX += Math.cos(angle) * acceleration;
        velocityY += Math.sin(angle) * acceleration;
        limitSpeed();
    }

    public void moveRight() {
        double angle = Math.toRadians(this.getRotate() + 90);
        velocityX += Math.cos(angle) * acceleration;
        velocityY += Math.sin(angle) * acceleration;
        limitSpeed();
    }

    public void rotate(double angle) {
        setRotate(this.getRotate() + angle);
    }

    private void limitSpeed() {
        this.setSpeed(Math.sqrt((velocityX * velocityX) + (velocityY * velocityY)));
        if (this.getSpeed() > maxSpeed) {
            velocityX = (velocityX / speed) * maxSpeed;
            velocityY = (velocityY / speed) * maxSpeed;
        }
    }

    private void applyFriction() {
        velocityX *= friction;
        velocityY *= friction;
    }

    public void checkWallCollisions() {
        if (getX() < 0) setX(gameWidth);
        if (getX() > gameWidth) setX(0);
        if (getY() < 0) setY(gameHeight);
        if (getY() > gameHeight) setY(0);
    }

    public void update() {
        setX(this.getX() + velocityX);
        setY(this.getY() + velocityY);
        applyFriction();
        checkWallCollisions();
    }

    public abstract boolean isDead();
}
