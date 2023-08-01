package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProjectileTest {

    private Projectile projectile;

    @BeforeEach
    void runBefore() {
        projectile = new Projectile(new Position(6, 5));
    }

    @Test
    void testConstructor() {
        assertEquals(new Position(6, 5), projectile.getPos());
    }

    @Test
    void testMovePos() {
        projectile.movePos();
        assertEquals(new Position(11, 5), projectile.getPos());
    }

    @Test
    void testSetPos() {
        assertEquals(new Position(6, 5), projectile.getPos());
        projectile.setPos(new Position(7, 8));
        assertEquals(new Position(7, 8), projectile.getPos());
    }
}
