
/**
 * manage the memory, the memory manager will control the memory, when insert a
 * byte data, it will find a suitable feel memory to store the data
 *
 * @author jiahui huang<hjiahui7>
 * @author Yusheng Cao<cyschris>
 * @version 2018.9.4
 */
public class MemManager {
    /**
     * create a hashTable object
     *
     * @author jiahui huang<hjiahui7>
     * @author Yusheng Cao<cyschris>
     * @version 2018.9.4
     * @param <K>
     *            key of data
     * @param <V>
     *            value of data
     */
    private static class Block {

        private int offset;
        private int size;
        private boolean inUse;
        private int occupySize;


        /**
         * create new block
         * 
         * @param offset
         *            location in memory
         * @param size
         *            size of the block
         * @param inUse
         *            use status
         * @param occupySize
         *            size in the memory
         */
        public Block(int offset, int size, boolean inUse, int occupySize) {
            this.offset = offset;
            this.size = size;
            this.inUse = inUse;
            this.occupySize = occupySize;
        }


        /**
         * get the offset place
         * 
         * @return offset value
         */
        public int getOffset() {
            return offset;
        }


        /**
         * get the real size occupy in memory
         * 
         * @return the size
         */
        public int getOccupySize() {
            return occupySize;
        }


        /**
         * change the size in the memory
         * 
         * @param occupySize
         *            new size
         */
        public void setOccupySize(int occupySize) {
            this.occupySize = occupySize;
        }


        /**
         * get the size of block
         * 
         * @param size
         *            block size
         */
        public void setSize(int size) {
            this.size = size;
        }


        /**
         * check the block status it is currently using or not
         * 
         * @return the using status
         */
        public boolean isInUse() {
            return inUse;
        }


        /**
         * change the in use status
         * 
         * @param inUse
         *            new status
         */
        public void setInUse(boolean inUse) {
            this.inUse = inUse;
        }
    }

    /**
     * the field
     */
    private Memorypool pool;
    private int memSize;
    private DLList<Block> memBlocks;
    private int remainMem;


    /**
     * create new memory pool
     * 
     * @param poolsize
     *            size of pool
     */
    public MemManager(int poolsize) {
        memBlocks = new DLList<Block>();
        pool = new Memorypool(poolsize);
        memSize = 0;
        Block block = new Block(0, memSize, false, poolsize);
        memBlocks.add(block);
        setRemainMem(poolsize);
    }


    /**
     * insert new value into the pool, it will expand if necessary, it will also
     * follow the best fit, which means that we will find a most suitable block
     * for inserted data
     * 
     * @param space
     *            new space contain data
     * @param length
     *            length of the new data
     * @return the handle represent the change
     */
    public Handle insert(byte[] space, int length) {
        int suitableSize = findSpace(length);
        boolean find = false;
        int count = 0;
        if (remainMem == 0) {
            this.expandThePool();
        }
        Iterator<Block> iterator = this.memBlocks.iterator();
        int smallBlock = pool.getSize();
        int smallIndex = 0;
        while (iterator.hasNext()) {
            Block thisBlock = iterator.next();
            if (!thisBlock.inUse && suitableSize <= thisBlock.getOccupySize()) {
                find = true;
                if (smallBlock > thisBlock.getOccupySize()) {
                    smallBlock = thisBlock.getOccupySize();
                    smallIndex = count;
                }
            }
            count++;
        }
        if (!find) {
            int size = 0;
            while (size < suitableSize) {
                this.expandThePool();
                if (iterator.hasNext()) {
                    iterator.next();
                }
                size = iterator.getCurrent().occupySize;
            }
        }
        else {
            iterator.getIndex(smallIndex);
        }
        Block curBlock = detachMem(iterator, suitableSize, smallIndex);
        curBlock.setOccupySize(suitableSize);
        curBlock.setSize(length);
        curBlock.setInUse(true);
        pool.insert(space, curBlock.getOffset());
        Handle handle = new Handle(curBlock.getOffset(), curBlock.size);
        this.memSize += suitableSize;
        setRemainMem(remainMem - suitableSize);
        return handle;
    }


    /**
     * it will return the remain memory
     * 
     * @return the integer which represent the remaining memory
     */
    public int getRemainMem() {
        return remainMem;
    }


    /**
     * remove the specific space in the memory
     * 
     * @param theHandle
     *            handle represent the change
     */
    public void remove(Handle theHandle) {
        Iterator<Block> iterator = memBlocks.iterator();
        while (iterator.hasNext()) {
            Block thisBlock = iterator.next();
            if (theHandle.getOffset() == thisBlock.getOffset()) {
                break;
            }
        }
        Block curBlock = iterator.getCurrent();
        curBlock.setInUse(false);
        memSize -= curBlock.getOccupySize();
        setRemainMem(remainMem + curBlock.getOccupySize());
        curBlock.setSize(0);
        combineMem(iterator);
    }


    /**
     * get data from memory
     * 
     * @param space
     *            empty place which hold the data
     * @param theHandle
     *            handle represent the change
     * @param size
     *            size of the working space
     * @return actual size of data which put in the space array
     */
    public int get(byte[] space, Handle theHandle, int size) {
        space = pool.get(size, theHandle.getOffset(), space);
        return size;
    }


    /**
     * expand the pool by double the size of the pool
     */
    public void expandThePool() {
        pool.expandThePool();
        remainMem = pool.getSize() - memSize;
        Block last = this.memBlocks.get(memBlocks.size() - 1);
        Block newBlock = new Block(last.offset + last.occupySize, 0, false, pool
            .getSize() / 2);
        if (!last.inUse && Integer.bitCount(newBlock.getOffset() ^ last
            .getOffset()) == 1) {
            last.setOccupySize(last.occupySize * 2);
        }
        else {
            this.memBlocks.add(memBlocks.size(), newBlock);
        }
    }


    /**
     * detach the memory to many smaller piece if necessary
     * 
     * @param iterator
     *            the current position iterator
     * @param size
     *            the block size which need to detach
     * @param count
     *            the index of that block in the double link list
     * @return the block that is suitable for current insert
     */
    public Block detachMem(Iterator<Block> iterator, int size, int count) {
        Block curBlock = iterator.getCurrent();
        while (true) {
            int necessarySize = curBlock.occupySize;
            if (necessarySize == size) {
                return curBlock;
            }
            Block newBlock = new Block(curBlock.getOffset(), 0, false,
                curBlock.occupySize / 2);
            curBlock.offset = curBlock.getOffset() + (curBlock.occupySize / 2);
            curBlock.setOccupySize(newBlock.getOccupySize());
            memBlocks.add(count, newBlock);
            curBlock = newBlock;
        }
    }


    /**
     * combine two part of mem if necessary until there is no more block can
     * combine
     * 
     * @param curIterator
     *            the iterator from double link list
     */
    public void combineMem(Iterator<Block> curIterator) {
        boolean find = true;
        while (find) {
            Block preBlock = null;
            Block afterBlock = null;
            Block curBlock = curIterator.getCurrent();
            if (curIterator.hasPre()) {
                preBlock = curIterator.previous();
                curIterator.next();
            }
            if (curIterator.hasNext()) {
                afterBlock = curIterator.next();
                curIterator.previous();
            }
            find = false;
            if (afterBlock != null && !afterBlock.isInUse() && Integer.bitCount(
                afterBlock.getOffset() ^ curBlock.getOffset()) == 1
                && afterBlock.getOccupySize() == curBlock.getOccupySize()) {
                curBlock.setOccupySize(curBlock.getOccupySize() + afterBlock
                    .getOccupySize());
                curBlock.setSize(0);
                memBlocks.remove(afterBlock);
                find = true;
            }
            else if (preBlock != null && !preBlock.isInUse() && Integer
                .bitCount(preBlock.getOffset() ^ curBlock.getOffset()) == 1
                && preBlock.getOccupySize() == curBlock.getOccupySize()) {
                preBlock.setSize(0);
                preBlock.setOccupySize(preBlock.getOccupySize() + curBlock
                    .getOccupySize());
                memBlocks.remove(curBlock);
                curIterator.previous();
                find = true;
            }
        }
    }


    /**
     * get the current memory pool
     * 
     * @return memory pool
     */
    public Memorypool getPool() {
        return pool;
    }


    /**
     * get the size of the memory pool
     * 
     * @return the size
     */
    public int getSize() {
        return memSize;
    }


    /**
     * print the feel memory
     * 
     */
    public void printBlocks() {
        if (getRemainMem() == 0) {
            System.out.println("No free blocks are available.");
        }
        else {
            for (int i = 1; i <= pool.getSize(); i *= 2) {
                boolean find = false;
                Iterator<Block> iterator = memBlocks.iterator();
                while (iterator.hasNext()) {
                    Block curBlock = iterator.next();
                    if (!curBlock.inUse && curBlock.getOccupySize() == i) {
                        if (!find) {
                            System.out.print(i + ": " + curBlock.offset);
                            find = true;
                        }
                        else {
                            System.out.print(" " + curBlock.offset);
                        }
                    }
                }
                if (find) {
                    System.out.println();
                }
            }
        }
    }


    /**
     * find space based on the number
     * 
     * @param number
     *            represent the location
     * @return index of the space
     */
    public int findSpace(int number) {
        int result = 0;
        for (double i = 0;; i++) {
            int fixSize = (int)Math.pow(2.0, i);
            if (number <= fixSize) {
                result = fixSize;
                break;
            }
        }
        return result;
    }


    /**
     * change the data of the remain memory size
     * 
     * @param remainMem
     *            new size of the remain memory
     */
    public void setRemainMem(int remainMem) {
        this.remainMem = remainMem;
    }
}
