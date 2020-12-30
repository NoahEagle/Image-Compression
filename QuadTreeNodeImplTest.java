import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class QuadTreeNodeImplTest {

    @Test (expected = IllegalArgumentException.class)
     public void testBuildWithNullArray() {

        QuadTreeNodeImpl.buildFromIntArray(null);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testBuildWithEmptyArray() {

        int[][] emptyArray = new int[0][0];

        QuadTreeNodeImpl.buildFromIntArray(emptyArray);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testBuildWithNonPo2Array() {

        int[][] nonPo2Array = new int[3][3];

        QuadTreeNodeImpl.buildFromIntArray(nonPo2Array);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testBuildWithNonSquareP02Array() {

        int[][] nonSquarePo2Array = new int[4][3];

        QuadTreeNodeImpl.buildFromIntArray(nonSquarePo2Array);
    }

    @Test
    public void testBuildWithUniformColor() {

        int[][] uniformArray = new int[4][4];

        for (int row = 0; row < 4; row++) {

            for (int col = 0; col < 4; col++) {

                uniformArray[row][col] = 2;
            }
        }

        QuadTreeNodeImpl testTree = QuadTreeNodeImpl.buildFromIntArray(uniformArray);

        assertEquals(1, testTree.getSize());
        assertEquals(2, testTree.nodeColor);
    }

    @Test
    public void testBuildWithDifferentColors() {

        int[][] uniqueArray = new int[4][4];

        uniqueArray[0][0] = 1;
        uniqueArray[0][1] = 1;
        uniqueArray[0][2] = 1;
        uniqueArray[0][3] = 2;
        uniqueArray[1][0] = 1;
        uniqueArray[1][1] = 1;
        uniqueArray[1][2] = 2;
        uniqueArray[1][3] = 1;
        uniqueArray[2][0] = 1;
        uniqueArray[2][1] = 2;
        uniqueArray[2][2] = 2;
        uniqueArray[2][3] = 2;
        uniqueArray[3][0] = 2;
        uniqueArray[3][1] = 1;
        uniqueArray[3][2] = 2;
        uniqueArray[3][3] = 2;

        QuadTreeNodeImpl testTree = QuadTreeNodeImpl.buildFromIntArray(uniqueArray);

        assertEquals(13, testTree.getSize());
        assertEquals(-1, testTree.nodeColor);

        QuadTreeNodeImpl tLChild = testTree.tLChild;
        QuadTreeNodeImpl bRChild = testTree.bRChild;

        QuadTreeNodeImpl tRChild = testTree.tRChild;

        QuadTreeNodeImpl tRGrandchild = tRChild.tRChild;

        assertEquals(1, tLChild.nodeColor);
        assertEquals(2, bRChild.nodeColor);
        assertEquals(2, tRGrandchild.nodeColor);
    }

    @Test
    public void testCompressionRatioUniformArray() {

        int[][] uniformArray = new int[4][4];

        for (int row = 0; row < 4; row++) {

            for (int col = 0; col < 4; col++) {

                uniformArray[row][col] = 2;
            }
        }

        QuadTreeNodeImpl testTree = QuadTreeNodeImpl.buildFromIntArray(uniformArray);

        assertEquals(1.0 / 16.0, testTree.getCompressionRatio(), 0);
    }

    @Test
    public void testCompressionRatioMulitColoredArray() {

        int[][] uniqueArray = new int[4][4];

        uniqueArray[0][0] = 1;
        uniqueArray[0][1] = 1;
        uniqueArray[0][2] = 1;
        uniqueArray[0][3] = 2;
        uniqueArray[1][0] = 1;
        uniqueArray[1][1] = 1;
        uniqueArray[1][2] = 2;
        uniqueArray[1][3] = 1;
        uniqueArray[2][0] = 1;
        uniqueArray[2][1] = 2;
        uniqueArray[2][2] = 2;
        uniqueArray[2][3] = 2;
        uniqueArray[3][0] = 2;
        uniqueArray[3][1] = 1;
        uniqueArray[3][2] = 2;
        uniqueArray[3][3] = 2;

        QuadTreeNodeImpl testTree = QuadTreeNodeImpl.buildFromIntArray(uniqueArray);

        assertEquals(13.0 / 16.0, testTree.getCompressionRatio(), 0);

    }

    @Test
    public void testDimensionCheck() {

        int[][] uniqueArray = new int[4][4];

        uniqueArray[0][0] = 1;
        uniqueArray[0][1] = 1;
        uniqueArray[0][2] = 1;
        uniqueArray[0][3] = 2;
        uniqueArray[1][0] = 1;
        uniqueArray[1][1] = 1;
        uniqueArray[1][2] = 2;
        uniqueArray[1][3] = 1;
        uniqueArray[2][0] = 1;
        uniqueArray[2][1] = 2;
        uniqueArray[2][2] = 2;
        uniqueArray[2][3] = 2;
        uniqueArray[3][0] = 2;
        uniqueArray[3][1] = 1;
        uniqueArray[3][2] = 2;
        uniqueArray[3][3] = 2;

        QuadTreeNodeImpl testTree = QuadTreeNodeImpl.buildFromIntArray(uniqueArray);

        assertEquals(4, testTree.getDimension());

        QuadTreeNodeImpl bRChild = testTree.bRChild;

        assertEquals(2, bRChild.getDimension());

        QuadTreeNodeImpl tRChild = testTree.tRChild;

        QuadTreeNodeImpl tRGrandchild = tRChild.tRChild;

        assertEquals(1, tRGrandchild.getDimension());

    }

    @Test
    public void testLeafCheckUniformArray() {

        int[][] uniformArray = new int[4][4];

        for (int row = 0; row < 4; row++) {

            for (int col = 0; col < 4; col++) {

                uniformArray[row][col] = 2;
            }
        }

        QuadTreeNodeImpl testTree = QuadTreeNodeImpl.buildFromIntArray(uniformArray);

        assertTrue(testTree.isLeaf());
    }

    @Test
    public void testLeafCheckMultiColorArray() {

        int[][] uniqueArray = new int[4][4];

        uniqueArray[0][0] = 1;
        uniqueArray[0][1] = 1;
        uniqueArray[0][2] = 1;
        uniqueArray[0][3] = 2;
        uniqueArray[1][0] = 1;
        uniqueArray[1][1] = 1;
        uniqueArray[1][2] = 2;
        uniqueArray[1][3] = 1;
        uniqueArray[2][0] = 1;
        uniqueArray[2][1] = 2;
        uniqueArray[2][2] = 2;
        uniqueArray[2][3] = 2;
        uniqueArray[3][0] = 2;
        uniqueArray[3][1] = 1;
        uniqueArray[3][2] = 2;
        uniqueArray[3][3] = 2;

        QuadTreeNodeImpl testTree = QuadTreeNodeImpl.buildFromIntArray(uniqueArray);

        assertFalse(testTree.isLeaf());

        QuadTreeNodeImpl bRChild = testTree.bRChild;

        assertTrue(bRChild.isLeaf());

        QuadTreeNodeImpl tRChild = testTree.tRChild;

        QuadTreeNodeImpl tRGrandchild = tRChild.tRChild;

        assertTrue(tRGrandchild.isLeaf());
    }

    @Test
    public void testLoselessUniformArray() {

        int[][] uniformArray = new int[4][4];

        for (int row = 0; row < 4; row++) {

            for (int col = 0; col < 4; col++) {

                uniformArray[row][col] = 2;
            }
        }

        QuadTreeNodeImpl testTree = QuadTreeNodeImpl.buildFromIntArray(uniformArray);

        int[][] decompressedArray = testTree.decompress();

        assertArrayEquals(uniformArray, decompressedArray);
    }

    @Test
    public void testLoselessMultiColorArray() {

        int[][] uniqueArray = new int[4][4];

        uniqueArray[0][0] = 1;
        uniqueArray[0][1] = 1;
        uniqueArray[0][2] = 1;
        uniqueArray[0][3] = 2;
        uniqueArray[1][0] = 1;
        uniqueArray[1][1] = 1;
        uniqueArray[1][2] = 2;
        uniqueArray[1][3] = 1;
        uniqueArray[2][0] = 1;
        uniqueArray[2][1] = 2;
        uniqueArray[2][2] = 2;
        uniqueArray[2][3] = 2;
        uniqueArray[3][0] = 2;
        uniqueArray[3][1] = 1;
        uniqueArray[3][2] = 2;
        uniqueArray[3][3] = 2;

        QuadTreeNodeImpl testTree = QuadTreeNodeImpl.buildFromIntArray(uniqueArray);

        int[][] decompressedArray = testTree.decompress();

        assertArrayEquals(uniqueArray, decompressedArray);
    }

    @Test
    public void testGetColorUniformArray() {

        int[][] uniformArray = new int[4][4];

        for (int row = 0; row < 4; row++) {

            for (int col = 0; col < 4; col++) {

                uniformArray[row][col] = 2;
            }
        }

        QuadTreeNodeImpl testTree = QuadTreeNodeImpl.buildFromIntArray(uniformArray);

        assertEquals(2, testTree.getColor(0, 0));
        assertEquals(2, testTree.getColor(2, 3));
        assertEquals(2, testTree.getColor(1, 2));
    }

    @Test
    public void testGetColorMultiColorArray() {

        int[][] uniqueArray = new int[4][4];

        uniqueArray[0][0] = 1;
        uniqueArray[0][1] = 1;
        uniqueArray[0][2] = 1;
        uniqueArray[0][3] = 2;
        uniqueArray[1][0] = 1;
        uniqueArray[1][1] = 1;
        uniqueArray[1][2] = 2;
        uniqueArray[1][3] = 1;
        uniqueArray[2][0] = 1;
        uniqueArray[2][1] = 2;
        uniqueArray[2][2] = 2;
        uniqueArray[2][3] = 2;
        uniqueArray[3][0] = 2;
        uniqueArray[3][1] = 1;
        uniqueArray[3][2] = 2;
        uniqueArray[3][3] = 2;

        QuadTreeNodeImpl testTree = QuadTreeNodeImpl.buildFromIntArray(uniqueArray);

        assertEquals(1, testTree.getColor(1, 0));
        assertEquals(2, testTree.getColor(3, 0));
        assertEquals(1, testTree.getColor(0, 2));
        assertEquals(2, testTree.getColor(3, 3));
    }

    @Test (expected = IllegalArgumentException.class)
    public void testGetColorIllegalCoords() {

        int[][] uniformArray = new int[4][4];

        for (int row = 0; row < 4; row++) {

            for (int col = 0; col < 4; col++) {

                uniformArray[row][col] = 2;
            }
        }

        QuadTreeNodeImpl testTree = QuadTreeNodeImpl.buildFromIntArray(uniformArray);

        testTree.getColor(4, 4);
    }

    @Test
    public void testgetQuadrantsNonLeaves() {

        int[][] uniqueArray = new int[4][4];

        uniqueArray[0][0] = 1;
        uniqueArray[0][1] = 1;
        uniqueArray[0][2] = 1;
        uniqueArray[0][3] = 2;
        uniqueArray[1][0] = 1;
        uniqueArray[1][1] = 1;
        uniqueArray[1][2] = 2;
        uniqueArray[1][3] = 1;
        uniqueArray[2][0] = 1;
        uniqueArray[2][1] = 2;
        uniqueArray[2][2] = 2;
        uniqueArray[2][3] = 2;
        uniqueArray[3][0] = 2;
        uniqueArray[3][1] = 1;
        uniqueArray[3][2] = 2;
        uniqueArray[3][3] = 2;

        QuadTreeNodeImpl testTree = QuadTreeNodeImpl.buildFromIntArray(uniqueArray);

        QuadTreeNodeImpl bRChild = testTree.bRChild;
        QuadTreeNodeImpl tLChild = testTree.tLChild;
        QuadTreeNodeImpl tRChild = testTree.tRChild;
        QuadTreeNodeImpl bLChild = testTree.bLChild;

        assertEquals(bRChild, testTree.getQuadrant(QuadTreeNode.QuadName.BOTTOM_RIGHT));
        assertEquals(bLChild, testTree.getQuadrant(QuadTreeNode.QuadName.BOTTOM_LEFT));
        assertEquals(tRChild, testTree.getQuadrant(QuadTreeNode.QuadName.TOP_RIGHT));
        assertEquals(tLChild, testTree.getQuadrant(QuadTreeNode.QuadName.TOP_LEFT));
    }

    @Test
    public void testGetQuadrantLeaf() {

        int[][] uniformArray = new int[4][4];

        for (int row = 0; row < 4; row++) {

            for (int col = 0; col < 4; col++) {

                uniformArray[row][col] = 2;
            }
        }

        QuadTreeNodeImpl testTree = QuadTreeNodeImpl.buildFromIntArray(uniformArray);

        assertNull(testTree.getQuadrant(QuadTreeNode.QuadName.BOTTOM_RIGHT));
    }

    @Test (expected = IllegalArgumentException.class)
    public void testSetColorIllegalCoords() {

        int[][] uniformArray = new int[4][4];

        for (int row = 0; row < 4; row++) {

            for (int col = 0; col < 4; col++) {

                uniformArray[row][col] = 2;
            }
        }

        QuadTreeNodeImpl testTree = QuadTreeNodeImpl.buildFromIntArray(uniformArray);

        testTree.setColor(4, 4, 0);
    }

    @Test
    public void testsetColorNoStructuralChange() {

        int[][] uniqueArray = new int[4][4];

        uniqueArray[0][0] = 1;
        uniqueArray[0][1] = 1;
        uniqueArray[0][2] = 1;
        uniqueArray[0][3] = 2;
        uniqueArray[1][0] = 1;
        uniqueArray[1][1] = 1;
        uniqueArray[1][2] = 2;
        uniqueArray[1][3] = 1;
        uniqueArray[2][0] = 1;
        uniqueArray[2][1] = 2;
        uniqueArray[2][2] = 2;
        uniqueArray[2][3] = 2;
        uniqueArray[3][0] = 2;
        uniqueArray[3][1] = 1;
        uniqueArray[3][2] = 2;
        uniqueArray[3][3] = 2;

        QuadTreeNodeImpl testTree = QuadTreeNodeImpl.buildFromIntArray(uniqueArray);

        assertEquals(2, testTree.getColor(3, 0));
        assertEquals(13, testTree.getSize());

        int[][] before = testTree.decompress();

        testTree.setColor(3, 0, 3);

        assertEquals(3, testTree.getColor(3,  0));
        assertEquals(13, testTree.getSize());

        int[][] after = testTree.decompress();

        before[0][3] = 3;

        assertArrayEquals(before, after);
    }

    @Test
     public void testsetColorLeafBrokenDown() {

        int[][] uniqueArray = new int[4][4];

        uniqueArray[0][0] = 1;
        uniqueArray[0][1] = 1;
        uniqueArray[0][2] = 1;
        uniqueArray[0][3] = 2;
        uniqueArray[1][0] = 1;
        uniqueArray[1][1] = 1;
        uniqueArray[1][2] = 2;
        uniqueArray[1][3] = 1;
        uniqueArray[2][0] = 1;
        uniqueArray[2][1] = 2;
        uniqueArray[2][2] = 2;
        uniqueArray[2][3] = 2;
        uniqueArray[3][0] = 2;
        uniqueArray[3][1] = 1;
        uniqueArray[3][2] = 2;
        uniqueArray[3][3] = 2;

        QuadTreeNodeImpl testTree = QuadTreeNodeImpl.buildFromIntArray(uniqueArray);

        assertEquals(1, testTree.getColor(0, 0));
        assertEquals(13, testTree.getSize());

        int[][] before = testTree.decompress();

        testTree.setColor(0, 0, 3);

        assertEquals(3, testTree.getColor(0,  0));
        assertEquals(17, testTree.getSize());

        QuadTreeNodeImpl tLChild = testTree.tLChild;
        QuadTreeNodeImpl tLGrandchild = tLChild.tLChild;

        int newColor = tLGrandchild.nodeColor;

        assertEquals(3, newColor);

        int[][] after = testTree.decompress();

        before[0][0] = 3;

        assertArrayEquals(before, after);
    }

    @Test
    public void testsetColorLeavesCombine() {

        int[][] uniqueArray = new int[4][4];

        uniqueArray[0][0] = 1;
        uniqueArray[0][1] = 1;
        uniqueArray[0][2] = 1;
        uniqueArray[0][3] = 2;
        uniqueArray[1][0] = 1;
        uniqueArray[1][1] = 1;
        uniqueArray[1][2] = 2;
        uniqueArray[1][3] = 1;
        uniqueArray[2][0] = 1;
        uniqueArray[2][1] = 2;
        uniqueArray[2][2] = 2;
        uniqueArray[2][3] = 2;
        uniqueArray[3][0] = 1;
        uniqueArray[3][1] = 1;
        uniqueArray[3][2] = 2;
        uniqueArray[3][3] = 2;

        QuadTreeNodeImpl testTree = QuadTreeNodeImpl.buildFromIntArray(uniqueArray);

        assertEquals(2, testTree.getColor(1, 2));
        assertEquals(13, testTree.getSize());

        int[][] before = testTree.decompress();

        testTree.setColor(1, 2, 1);

        assertEquals(1, testTree.getColor(1, 2));
        assertEquals(9, testTree.getSize());

        QuadTreeNodeImpl bLChild = testTree.bLChild;

        assertTrue(bLChild.isLeaf());

        int[][] after = testTree.decompress();

        before[2][1] = 1;

        assertArrayEquals(before, after);
    }

    @Test
    public void testsetColorToCurrentColor() {

        int[][] uniqueArray = new int[4][4];

        uniqueArray[0][0] = 1;
        uniqueArray[0][1] = 1;
        uniqueArray[0][2] = 1;
        uniqueArray[0][3] = 2;
        uniqueArray[1][0] = 1;
        uniqueArray[1][1] = 1;
        uniqueArray[1][2] = 2;
        uniqueArray[1][3] = 1;
        uniqueArray[2][0] = 1;
        uniqueArray[2][1] = 2;
        uniqueArray[2][2] = 2;
        uniqueArray[2][3] = 2;
        uniqueArray[3][0] = 1;
        uniqueArray[3][1] = 1;
        uniqueArray[3][2] = 2;
        uniqueArray[3][3] = 2;

        QuadTreeNodeImpl testTree = QuadTreeNodeImpl.buildFromIntArray(uniqueArray);

        assertEquals(1, testTree.getColor(0, 0));
        assertEquals(13, testTree.getSize());

        int[][] before = testTree.decompress();

        testTree.setColor(0, 0, 1);

        assertEquals(1, testTree.getColor(0, 0));
        assertEquals(13, testTree.getSize());

        int[][] after = testTree.decompress();

        assertArrayEquals(before, after);
    }
}