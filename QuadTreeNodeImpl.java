// CIS 121, QuadTree

public class QuadTreeNodeImpl implements QuadTreeNode {

    // These fields are other nodes representing the four children (the four subquadrants)
    QuadTreeNodeImpl tLChild;
    QuadTreeNodeImpl tRChild;
    QuadTreeNodeImpl bLChild;
    QuadTreeNodeImpl bRChild;

    // This field is an int representing the node's color (-1 for non-uniform color)
    int nodeColor;

    // This field is an int representing the node's dimensions (D would mean a D x D square)
    int nodeDimensions;

    // These fields are ints representing the top left coordinates of the node (first row & col)
    int startRow;
    int startCol;

    /**
     * ! Do not delete this method !
     * Please implement your logic inside this method without modifying the signature
     * of this method, or else your code won't compile.
     * <p/>
     * As always, if you want to create another method, make sure it is not public.
     *
     * @param image image to put into the tree
     * @return the newly build QuadTreeNode instance which stores the compressed image
     * @throws IllegalArgumentException if image is null
     * @throws IllegalArgumentException if image is empty
     * @throws IllegalArgumentException if image.length is not a power of 2
     * @throws IllegalArgumentException if image, the 2d-array, is not a perfect square
     */
    public static QuadTreeNodeImpl buildFromIntArray(int[][] image) {
       
        // If the image array is null, throw IllegalArgumentException
        if (image == null) {

            throw new IllegalArgumentException("Image array is null");
        }

        int imageLength = image.length;

        // If the image array is empty, throw IllegalArgumentException
        if (imageLength == 0) {

            throw new IllegalArgumentException("Image array is empty");
        }

        /*
         * If image.length is not a power of 2, throw IllegalArgumentException
         * (the equation calculates log base 2 of .length, and it should be a whole number)
         */
        if (Math.log(imageLength) / Math.log(2) % 1 != 0) {

            throw new IllegalArgumentException("Image length isn't a power of 2");
        }

        // If image array isn't a perfect square, throw IllegalArgumentException
        for (int col = 0; col < imageLength; col++) {

            // If any column is a different length to the width, it ain't a square, buddy
            if (image[col].length != imageLength) {

                throw new IllegalArgumentException("Image array isn't a perfect square");
            }
        }

        // Create the root node (which creates the whole quad tree)
        QuadTreeNodeImpl rootNode = new QuadTreeNodeImpl(imageLength, 0, 0, image);

        // Return the root node (which holds the quad tree's info)
        return rootNode;     
    }
    
    /**
     * Creates a Node of the Quad-Tree with the input dimensions and starting coordinates. It'll
     * eventually determine and set the node's color and children nodes as well. This node will
     * serve as the root of its resulting Quad-Tree.
     * 
     * @param dimensions This represents the length and width (same value) of the node's quadrant.
     * @param row This represents the lowest y value in the quadrant (the highest row).
     * @param col This represents the lowest x value in the quadrant (the leftmost column).
     */
    private QuadTreeNodeImpl(int dimensions, int row, int col, int[][] image) {

        // Set the node's dimensions to the input dimensions
        nodeDimensions = dimensions;

        // Set the node's top left row number to the input row
        startRow = row;

        // Set the node's top right row number to the input row
        startCol = col;

        // If the dimension is 1 (a 1x1 square), we're at the individual pixel level
        if (nodeDimensions == 1) {

            // Get the color of this pixel
            int color = image[startRow][startCol];

            // And color the node with it
            nodeColor = color;

            // It's a leaf, so make their children null
            tLChild = null;
            tRChild = null;
            bLChild = null;
            bRChild = null;

        // If we're not at the individual pixel level...
        } else {

            // Repeat the process with each of the node's four quadrants
            tLChild = new QuadTreeNodeImpl(nodeDimensions / 2, startRow, startCol, image);
            tRChild = new QuadTreeNodeImpl(nodeDimensions / 2, startRow, 
                    startCol + (nodeDimensions / 2), image);
            bLChild = new QuadTreeNodeImpl(nodeDimensions / 2, startRow + (nodeDimensions / 2), 
                    startCol, image);
            bRChild = new QuadTreeNodeImpl(nodeDimensions / 2, startRow + (nodeDimensions / 2), 
                    startCol + (nodeDimensions / 2), image);

            /*
             *  If the four children are all the same color 
             *  (and are each uniform colors themselves)...
             */
            if (tLChild.tLChild == null && tRChild.tLChild == null && bLChild.tLChild == null 
                    && bRChild.tLChild == null && tLChild.nodeColor == tRChild.nodeColor 
                    && tLChild.nodeColor == bLChild.nodeColor 
                    && tLChild.nodeColor == bRChild.nodeColor) {

                // Find that color
                int color = tLChild.nodeColor;

                // And color the node with it
                nodeColor = color;

                // Delete all the now useless children
                tLChild = null;
                tRChild = null;
                bLChild = null;
                bRChild = null;

            // If the children weren't a uniform color, color the node -1 (for colorless)
            } else {

                nodeColor = -1;
            }
        }
    }
    
    /**
     * Creates a Leaf of the Quad-Tree. (This constructor is really only used to create leaves
     * for the setColor function. It's kinda useless for me otherwise).
     * 
     * @param dimensions An int that represents the length and width of the square quadrant the
     * leaf node represents
     * @param row An int representing the top row in the quadrant the leaf node represents
     * @param col An int representing the leftmost column in the quadrant the leaf node represents
     * @param color An int representing the color of the node
     */
    private QuadTreeNodeImpl(int dimensions, int row, int col, int color) {

        // Set the node's dimensions to the input dimensions
        nodeDimensions = dimensions;

        // Set the node's upper left corner to the input row and col
        startRow = row;
        startCol = col;

        // Set the node's color to the input color
        nodeColor = color;

        // Set all the node's children to null because it's supposed to be a leaf
        tLChild = null;
        tRChild = null;
        bLChild = null;
        bRChild = null;
    }

    /**
     * 
     * This function finds the color of a specified coordinate from the image & returns it as an int
     * 
     * @param x It represents the x coordinate (the column) of the image we're searching for
     * the color of
     * @param y It represents the y coordinate (the row) of the image we're searching for the
     * color of
     * 
     * @return It returns an integer representing the color at the input coordinates
     */
    @Override
    public int getColor(int x, int y) {

        // Set the y value to the desired row and x to the desired column (because array is [y][x])
        int targetRow = y;
        int targetCol = x;

        // Get the dimensions of the node's quadrant
        int dimensions = getDimension();

        // Find the top left corner of the node's quadrant
        int rowStart = startRow;
        int colStart = startCol;

        // If the coordinates are out of bounds, throw an illegal argument exception
        if (targetRow < 0 || targetCol < 0 || targetRow >= dimensions + rowStart || 
                targetCol >= dimensions + colStart) {

            throw new IllegalArgumentException("Those coordinates are out of bounds");
        }

        // If the node is a leaf, who cares about coords? Everything is the same color, return it
        if (isLeaf()) {

            return nodeColor;

        // If the node isn't a leaf, we've got a situation...
        } else {

            // If the target coords are in the top left, run the function on the top left child
            if (targetRow < rowStart + dimensions / 2 && targetCol < colStart + dimensions / 2) {

                return tLChild.getColor(x, y);

            // If the target coords are in the top right, run the function on the top right child
            } else if (targetRow < rowStart + dimensions / 2 && 
                targetCol >= colStart + dimensions / 2) {

                return tRChild.getColor(x, y);
                
            // If the target coords are in the bot left, run the function on the bot left child
            } else if (targetRow >= rowStart + dimensions / 2 
                   && targetCol < colStart + dimensions / 2) {

                return bLChild.getColor(x, y);

            // The target coords are in the bot right, run the function on the bot right child
            } else {

                return bRChild.getColor(x, y);
            }
        }
    }

    @Override
    public void setColor(int x, int y, int c) {

        // If they're trying to change the color to what it already is, just return immediately
        if (getColor(x, y) == c) {

            return;
        }

        // Set the y value to desired row and x to the desired column (because the array is [y][x])
        int targetRow = y;
        int targetCol = x;

        // Set c to the new color to change the coordinate to
        int newColor = c;

        int currColor = nodeColor;

        // Get the dimensions of the node's quadrant
        int dimensions = getDimension();

        // Find the top left corner of the node's quadrant
        int rowStart = startRow;
        int colStart = startCol;

        // If the coordinates are out of bounds, throw an illegal argument exception
        if (targetRow < 0 || targetCol < 0 || targetRow >= rowStart + dimensions || 
                targetCol >= colStart + dimensions) {

            throw new IllegalArgumentException("Those coordinates are out of bounds");
        }

        // If we're at the individual pixel level, it's time to swap those colors
        if (dimensions == 1) {

            nodeColor = newColor;

        // If we aren't at the individvual pixel level, we've gotta keep going down the tree...
        } else {

            // If we're at a leaf (that isn't a 1x1), we need to break it down
            if (isLeaf()) {

                // Create the non TL children as new leaves
                QuadTreeNodeImpl tR = new QuadTreeNodeImpl(dimensions / 2, 
                        rowStart, colStart + (dimensions / 2), currColor);
                QuadTreeNodeImpl tL = new QuadTreeNodeImpl(dimensions / 2, rowStart, colStart, 
                        currColor);
                QuadTreeNodeImpl bR = new QuadTreeNodeImpl(dimensions / 2, 
                        rowStart + (dimensions / 2), colStart + (dimensions / 2), currColor);
                QuadTreeNodeImpl bL = new QuadTreeNodeImpl(dimensions / 2, 
                        rowStart + (dimensions / 2), colStart, currColor);

                // Set the non TL children to the node's new children
                tRChild = tR;
                tLChild = tL;
                bRChild = bR;
                bLChild = bL;

                // Set the now non-leaf's color to -1 (for colorless)
                nodeColor = -1;

                // The target coords are in the top left, run the function on the top left child
                if (targetRow < rowStart + dimensions / 2 && 
                        targetCol < colStart + dimensions / 2) {

                    tLChild.setColor(targetCol, targetRow, newColor);

                // The target coords are in the top right, run the function on the top right child
                } else if (targetRow < rowStart + dimensions / 2 && 
                        targetCol >= colStart + dimensions / 2) {

                    tRChild.setColor(targetCol, targetRow, newColor);

                // The target coords are in the bottom left, run the function on the bot left child
                } else if (targetRow >= rowStart + dimensions / 2 &&
                        targetCol < colStart + dimensions / 2) {

                    bLChild.setColor(targetCol, targetRow, newColor);

                // The target coords are in the bot right, run the function on the bot right child
                } else {

                    bRChild.setColor(targetCol, targetRow, newColor);
                }

            // The target coords are in the top left, run the function on the top left child
            } else if (targetRow < rowStart + dimensions / 2 && 
                    targetCol < colStart + dimensions / 2) {

                tLChild.setColor(targetCol, targetRow, newColor);

            // The target coords are in the top right, run the function on the top right child
            } else if (targetRow < rowStart + dimensions / 2 && 
                    targetCol >= colStart + dimensions / 2) {

                tRChild.setColor(targetCol, targetRow, newColor);

            // The target coords are in the bottom left, run the function on the bottom left child
            } else if (targetRow >= rowStart + dimensions / 2 && 
                    targetCol < colStart + dimensions / 2) {
    
                bLChild.setColor(targetCol, targetRow, newColor);
                
            // The target coords are in the bottom right, run the function on the bottom right child
            } else {

                bRChild.setColor(targetCol, targetRow, newColor);
            }
            
            /*
             *  If the four children are all the same color 
             *  (and are each uniform colors themselves)...
             */
            if (tLChild.tLChild == null && tRChild.tLChild == null && bLChild.tLChild == null 
                    && bRChild.tLChild == null && tLChild.nodeColor == tRChild.nodeColor 
                    && tLChild.nodeColor == bLChild.nodeColor 
                    && tLChild.nodeColor == bRChild.nodeColor) {

                // Find that color
                int color = tLChild.nodeColor;

                // And color the node with it
                nodeColor = color;

                // Delete all the now useless children
                tLChild = null;
                tRChild = null;
                bLChild = null;
                bRChild = null;
            }
        }
    }

    /**
     * This function takes in a quadrant name (TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, or BOTTOM_RIGHT)
     * and spits out the node corresponding to that subquadrant of the current node it's being
     * run on (returns null if for some reason the input wasn't one of the four names).
     * 
     * @param quadrant The QuadName of the subquadrant you desire. The names are pretty
     * self-explanatory
     * 
     * @return It returns a QuadTreeNode that represents the specifid subquadrant of the node you 
     * ran the function on
     */
    @Override
    public QuadTreeNode getQuadrant(QuadName quadrant) {

        // If the input was TOP_LEFT, return the top left child
        if (quadrant == QuadName.TOP_LEFT) {

            return tLChild;

        // If the input was TOP_RIGHT, return the top right child
        } else if (quadrant == QuadName.TOP_RIGHT) {

            return tRChild;
            
        // If the input was BOTTOM_LEFT, return the bottom left child
        } else if (quadrant == QuadName.BOTTOM_LEFT) {

            return bLChild;

        // If the input was BOTTOM_RIGHT, return the bottom right child
        } else if (quadrant == QuadName.BOTTOM_RIGHT) {

            return bRChild;

        // If the input wasn't one of the four valid subquadrants, just return null
        } else {
 
            return null;
        }
    }

    /**
     * This function is essentially just a getter function that returns the dimensions of the node's
     * square quadrant as an int. (The int is both the length and width).
     * 
     * If you want the dimensions of the whole image, just run this on the tree (the root node).
     * 
     * @return It returns an int representing the dimensions of the square (the length and width)
     */
    @Override
    public int getDimension() {

        // Just return the dimension field of the current node
        return nodeDimensions;
    }

    /**
     * This function calculates the number of nodes in the tree (with the root node being the node
     * the function was run on), returning this number as an int.
     * 
     * @return It returns an int representing the number of nodes in the tree. (The node the
     * function was run on is the root node of the tree).
     */
    @Override
    public int getSize() {

        // Initialize the number of nodes to 1 (the node we just ran this function on)
        int numNodes = 1;
 
        // If the first child isn't null, the node has 4 children (because it's a full tree)
        if (!isLeaf()) {

            // Add the sizes of all the children's trees to the current node count
            numNodes = numNodes + tLChild.getSize();
            numNodes = numNodes + tRChild.getSize();
            numNodes = numNodes + bLChild.getSize();
            numNodes = numNodes + bRChild.getSize();
        }

        // Now return the calculated number of nodes in the tree
        return numNodes;
    }

    /**
     * This function returns a boolean telling you whether the node is a leaf or not (whether it has
     * any children nodes).
     * 
     * @return It returns a boolean. True means that the node has no children. False means that the
     * node does have children (4 children to be exact because it's a full tree).
     */
    @Override
    public boolean isLeaf() {

        // If the node doesn't have a top left child, it has no children (because the tree is full)
        return (tLChild == null);
    }

    /**
     * The function turns the quad tree back into its original 2D int array with the correct color
     * values for each element.
     * 
     * @return It returns a 2D array which will be identical to the original image array that was
     * compressed into the quad tree.
     */
    @Override
    public int[][] decompress() {

        // The dimension of the root node corresponds to the length and width of the image array
        int arraySize = getDimension();
 
        // Create the new 2D int array using the found dimensions
        int[][] decompressedImage = new int[arraySize][arraySize];
   
        // Run the helper function that actually fills in the array with the right color values
        fillInArray(decompressedImage);

        // Now that it's been filled in, return the decompressed image array
        return decompressedImage;
    }
    
    /**
     * This function just fills in the input array with the color values according to the quad-tree
     * it is run on. It doesn't return anything, it just updates the array.
     * 
     * @param decompressedImage The 2D int array to be updated by the function with the quad-tree's
     * color values.
     */
    void fillInArray(int[][] decompressedImage) {

        // If the node is a leaf, fill in the corresponding part of the image array
        if (isLeaf()) {

            // Get the dimensions of the node's quadrant
            int dimensions = getDimension();

            // Find the top left corner of the node's quadrant
            int rowStart = startRow;
            int colStart = startCol;

            // Iterate through the node's quadrant in the array and fill it in with the right color
            for (int row = rowStart; row < rowStart + dimensions; row++) {

                for (int col = colStart; col < colStart + dimensions; col++) {

                    decompressedImage[row][col] = nodeColor;
                }
            }

        // If the node isn't a leaf, continue on down to all of its children
        } else {

            tLChild.fillInArray(decompressedImage);
            tRChild.fillInArray(decompressedImage);
            bLChild.fillInArray(decompressedImage);
            bRChild.fillInArray(decompressedImage);
        }
    }

    /**
     * This function calculates the compression ratio of the image (the ratio of nodes in the
     * Quad-Tree to the pixel count of the image). It returns this ratio as a double.
     * 
     * @return It returns a double which represents the compression ratio of the image (essentially
     * the node count divided by the pixel count of the image the tree represents).
     */
    @Override
    public double getCompressionRatio() {

        // Calculate the node count (and cast it as a double so we avoid int division)
        double numberOfNodes = getSize();

        // Calculate the pixel count for the length of the image
        double lengthOfPixelArray = getDimension();

        // Since it's a square image, square the length to get the total pixel count
        double numberOfPixels = lengthOfPixelArray * lengthOfPixelArray;

        // Return the compression ration (node count / pixel count)
        return (numberOfNodes / numberOfPixels);
    }
}
