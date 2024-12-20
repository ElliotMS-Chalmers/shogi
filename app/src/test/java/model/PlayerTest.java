package model;

import model.game.Player;
import model.pieces.Piece;
import model.pieces.PieceFactory;
import model.variants.Standard;
import model.variants.Variant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.Side;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {
    private Player player;
    private Variant variant = new Standard();

    @BeforeEach
    void setUp(){
        player = new Player(Side.SENTE);
        player.intializeHand(variant.getHand());
    }

    @Test
    void testInitialSfen(){
        String expectedSfen = "";
        assertEquals(player.getHandAsSfen(),expectedSfen);
    }

    @Test
    void testAddAcpturedPiece(){
        Class<? extends Piece> pieceClass1 = variant.getHand().get(0);
        Class<? extends Piece> pieceClass2 = variant.getHand().get(1);
        player.addCapturedPiece(pieceClass1);
        player.addCapturedPiece(pieceClass2,3);
        String sfenAbbreviation1 = PieceFactory.fromClass(pieceClass1,Side.SENTE).getSfenAbbreviation();
        String sfenAbbreviation2 = PieceFactory.fromClass(pieceClass2,Side.SENTE).getSfenAbbreviation();
        String expectedSfen = sfenAbbreviation1+"3"+sfenAbbreviation2;
        assertEquals(player.getHandAsSfen(),expectedSfen);

        player.addCapturedPiece(null);
        assertEquals(player.getHandAsSfen(),expectedSfen);
    }

    @Test
    void testRemoveCapturedPiece(){
        Class<? extends Piece> pieceClass = variant.getHand().get(0);
        player.addCapturedPiece(pieceClass,3);
        player.removeCapturedPiece(pieceClass);
        String sfenAbbreviation = PieceFactory.fromClass(pieceClass,Side.SENTE).getSfenAbbreviation();
        String expectedSfen = "2"+sfenAbbreviation;
        assertEquals(player.getHandAsSfen(),expectedSfen);
    }

    @Test
    void testGetSfenException(){
        //Adds null to the list of piece classes in order to cause an exception in getHandAsSfen
        ArrayList<Class<? extends Piece>> list = new ArrayList();
        list.add(null);
        player.intializeHand(list);
        player.addCapturedPiece(null);
        assertDoesNotThrow(() -> {player.getHandAsSfen();});
    }
}
