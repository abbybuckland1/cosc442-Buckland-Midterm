package game;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class CombatRulesTest {
    int maxHp;

    @BeforeEach 
    void setUp(){
        maxHp = 100; //setting the max HP to 100 before every test case, dont need after each because it is always 100
    }

 @ParameterizedTest
    @CsvSource({
        "40, 20, 60, false",    //Normal healing
        "40, -20, 20, false",   //Normal damage creature survives
        "40, 70, 100, false",   //Healing exceeds max HP capped at 100
        "40, -50, -10, true"    //Damage reduces HP below 1 creature dies
    })
    void testCombatRulesValid(int currentHp, int amount, int expectedHp, boolean expectedDead) {
        // arrange in setup

        //act: calculate HP, then check if creature is dead
        int result = CombatRules.calculateNewHp(currentHp, maxHp, amount);
        boolean dead = CombatRules.isDead(result);

        //assert check both expected HP and death yes or no
        assertEquals(expectedHp, result);
        assertEquals(expectedDead, dead);
    }

    //boundary values
    @ParameterizedTest
    @CsvSource({
        "40, 0, 40, false",     //HP stays unchanged
        "40, -39, 1, false",    //creature survives
        "40, -40, 0, true",     //creature dies
        "40, 60, 100, false",   //Resulting HP exactly 100
        "40, 61, 100, false",   //Resulting HP would be 101 capped at 100
        "1, -1, 0, true",       
        "100, 1, 100, false",   
        "2, 0, 2, false",       
        "99, 0, 99, false"      
    })
    void testCombatRulesBoundary(int currentHp, int amount, int expectedHp, boolean expectedDead) {
        // arrange in setup

        // act calculate HP, then check if creature is dead
        int result = CombatRules.calculateNewHp(currentHp, maxHp, amount);
        boolean dead = CombatRules.isDead(result);

        // assert boundaries
        assertEquals(expectedHp, result);
        assertEquals(expectedDead, dead);
    }

    // Tests isDead for dead and living creatures
    @ParameterizedTest
    @CsvSource({
        "-10, true",   
        "20, false"     
    })
    void testIsDeadValid(int hp, boolean expected) {
        //arrange

        //act 
        boolean result = CombatRules.isDead(hp);

        // assert 
        assertEquals(expected, result);
    }

    // Tests the death boundary
    @ParameterizedTest
    @CsvSource({
        "0, true", //HP = 0 creature is dead
        "-1, true",      //HP = -1 creature is dead
        "1, false",     // HP = 1 creature is still alive
        "2, false"      //hP = 2 creature is alive
    })
    void testIsDeadBoundary(int hp, boolean expected) {
        // arrange

        //act
        boolean result = CombatRules.isDead(hp);

        // assert
        assertEquals(expected, result);
    }
}
