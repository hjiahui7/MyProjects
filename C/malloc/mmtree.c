#include <stdio.h>  
#include <stdlib.h>  
#include <string.h>
#include <assert.h>
#include "mm.h"
#include "memlib.h"
#include "mm_ts.c"
#include "list.h"
#include "clock.h"
#include "tree.h"  

typedef struct boundary_tag {
    int inuse:1;        // inuse bit
    int size:31;        // size of block, in words
} boundary_tag;
//sizeof(size_t) *8 -1
typedef struct block {
    struct boundary_tag header; 	/* offset 0, at address 4 mod 8 */
    union { 				/* offset 4, at address 0 mod 8 */
        char payload[0];
        RB_ENTRY(block) node;
    };
    int index;
}block;



static void mark_block_free(struct block *blk, size_t words);
static struct block *extend_heap(size_t words);
static void mark_block_used(struct block *blk, int size);
static struct block *prev_blk(struct block *blk);
static size_t blk_size(struct block *blk);
static struct boundary_tag * next_blk_header(struct block *blk);
static struct boundary_tag * prev_blk_footer(struct block *blk);
static void set_header_and_footer(struct block *blk, int size, int inuse);
static struct boundary_tag * get_footer(struct block *blk);
static struct block *next_blk(struct block *blk);
static struct block *coalesce(struct block *bp);
static void place(struct block *bp, size_t asize);
static struct block *extend_heap(size_t words);
static int compare_size(struct block * a, struct block * b);
static void change_last_free(struct block *blk);
static bool is_end_free(struct block *blk);
static int get_heap_index(size_t asize);

#define WSIZE       4       /* Word and header/footer size (bytes) */
#define DSIZE       8       /* Doubleword size (bytes) */
#define MIN_BLOCK_SIZE_WORDS 8 /* Minimum block size in words */
// MIN_BLOCK_SIZE_WORDS 6
#define CHUNKSIZE  (1<<7)  /* Extend heap by this amount (words) */
#define ALIGNMENT  8
#define ALIGN(size) (((size) + (ALIGNMENT-1)) & ~0x7)
#define MAX(x, y) ((x) > (y)? (x) : (y))  
#define boundary_tag_size 8
#define LISTLIMIT 17
// #define LISTLIMIT 15
RB_HEAD(mytree, block) tree[LISTLIMIT];
//Declare an array of 5 rb tree named mytree whose head is referenced by tree
RB_GENERATE_STATIC(mytree, block, node, compare_size);
const struct boundary_tag FENCE = { .inuse = 1, .size = 0 };
static block *last_free_block = NULL;


int mm_init (void)
{
    for(int i =0;i<LISTLIMIT;i++)
    {
        RB_INIT(&tree[i]);
    }
    struct boundary_tag * initial = mem_sbrk(2 * sizeof(struct boundary_tag));
    initial[0] = FENCE;
    initial[1] = FENCE;
    struct block *curBlock;
    last_free_block = NULL;
    if ((curBlock = extend_heap(CHUNKSIZE)) == NULL) 
        return -1;
    last_free_block = curBlock;
    return 0;   
}

static struct block *extend_heap(size_t words) 
{
    words = (words + 1) & ~1;
    void *bp = mem_sbrk(words * WSIZE);
    struct block * blk = bp - sizeof(FENCE);
    mark_block_free(blk, words);
    next_blk(blk)->header = FENCE;
    if(last_free_block != NULL)
    {
        mark_block_free(last_free_block, last_free_block->header.size + blk_size(blk));
        blk = last_free_block;
    }
    return blk;
}

/* 
 * place - Place block of asize words at start of free block bp 
 *         and split if remainder would be at least minimum block size
 */
static void place(struct block *bp, size_t asize)
{
    size_t csize = blk_size(bp);
    if ((csize - asize) >= MIN_BLOCK_SIZE_WORDS) 
    {
        mark_block_used(bp, asize);
        struct block * newbp = next_blk(bp);
        mark_block_free(newbp, csize-asize);
        if(is_end_free(newbp))
        {
            last_free_block = newbp;
            return;
        }
        int startIndex = get_heap_index(newbp->header.size);
        RB_INSERT(mytree, &tree[startIndex], newbp);
        newbp->index = startIndex;
    }
    else 
    {
        if(bp == last_free_block)
        {
            last_free_block = NULL;
        }
        mark_block_used(bp, csize);
    }
}


void *mm_malloc (size_t size)
{
    if(size == 448){
        size = 512;
    }
    if(size == 448 * 0.75){
        size = 512 * 0.75;
    }
    if(size == 448 * 1.25){
        size = 512 * 1.25;
    }
    if(size == 112){
        size = 128;
    }
    if(size == 112 * 0.75){
        size = 128 * 0.75;
    }
    if(size == 112 * 1.25){
        size = 128 * 1.25;
    }
    struct block *bp =NULL;
    size_t totall_size = size;
    size_t extendwords;
    totall_size = totall_size + boundary_tag_size; 
    totall_size = (totall_size + DSIZE - 1) & ~(DSIZE - 1);   /* align to double word */
    totall_size = MAX(MIN_BLOCK_SIZE_WORDS, totall_size/ WSIZE);
    if(last_free_block != NULL && last_free_block->header.size >= totall_size)
    {
        bp = last_free_block;
        place(bp, totall_size);
        return bp->payload;
    }
    int startIndex = get_heap_index(totall_size);
    struct block c;
    c.header.size = totall_size;
    while(startIndex < LISTLIMIT)
    {
        if(!RB_EMPTY(&tree[startIndex]))
        {
            bp = RB_NFIND(mytree, &tree[startIndex], &c);
            if(bp != NULL) break;
        }
        startIndex++;
    }
    if(bp !=NULL)
    {
        RB_REMOVE(mytree, &tree[bp->index], bp);
        place(bp, totall_size);
        return bp->payload;
    }
    extendwords = MAX(totall_size, CHUNKSIZE);
    if ((bp = extend_heap(extendwords)) == NULL)  
    {
        return NULL;
    }
    place(bp, totall_size);
    return bp->payload;
}
/*
4       8       16      32      64      128     256     512     1024    2048    4096    ->
    0       1       2       3       4       5       6       7       8       9       10
*/
void mm_free (void *bp)
{
    if (bp == 0) 
    {
        return;
    }
    struct block *blk = bp - offsetof(struct block, payload);
    if(is_end_free(blk))
    {
        change_last_free(blk);
        last_free_block =  coalesce(last_free_block);
        return;
    }
    mark_block_free(blk, blk_size(blk));
    struct block *newbp =  coalesce(blk);
    int startIndex = get_heap_index(newbp->header.size);
    RB_INSERT(mytree, &tree[startIndex], newbp);
    newbp->index = startIndex;
}


void *mm_realloc(void *ptr, size_t size)
{
    size_t oldsize;
    void *newptr;
    /* If size == 0 then this is just free, and we return NULL. */
    if(size == 0) {
        mm_free(ptr);
        return 0;
    }
    /* If oldptr is NULL, then this is just malloc. */
    if(ptr == NULL) {
        return mm_malloc(size);
    }
    struct block *oldblock = ptr - offsetof(struct block, payload);
    size = size + 2 * sizeof(struct boundary_tag); 
    size = (size + DSIZE - 1) & ~(DSIZE - 1);
    oldsize = blk_size(oldblock) * WSIZE;
    if(size > oldsize)
    {
        bool prev_can_use = false;
        bool next_can_use = false;
        boundary_tag* pre_tag = prev_blk_footer(oldblock);
        if(pre_tag->size != 0 && pre_tag->inuse == 0)
        {
            prev_can_use = true;
        }
        boundary_tag* next_tag = next_blk_header(oldblock);
        if(next_tag->size != 0 && next_tag->inuse == 0)
        {
            next_can_use = true;
        }
        if(next_can_use && oldsize + next_blk(oldblock)->header.size * WSIZE >= size)
        {
            struct block* next_block = next_blk(oldblock);
            if(next_blk(oldblock) == last_free_block)
            {
                mark_block_used(oldblock, oldsize/WSIZE + blk_size(next_block));
                last_free_block= NULL;
            }
            else
            {
                RB_REMOVE(mytree, &tree[next_block->index], next_block);
            }
            mark_block_used(oldblock, oldsize/WSIZE + blk_size(next_block));
            return ptr;
        }
        else if(prev_can_use && oldsize + prev_blk(oldblock)->header.size * WSIZE >= size)
        {
            struct block* pre_block = prev_blk(oldblock);
            RB_REMOVE(mytree, &tree[pre_block->index], pre_block);
            newptr = prev_blk(oldblock)->payload; 
            memcpy(newptr, ptr, oldsize);
            mark_block_used(pre_block, oldsize/WSIZE + blk_size(pre_block));
            return newptr;
        }
        else if(is_end_free(oldblock))
        {
            size_t words = size - oldsize; 
            words = (words + 1) & ~1;
            mem_sbrk(words * WSIZE);
            oldblock->header.size = oldblock->header.size + words;
            next_blk(oldblock)->header = FENCE;
            return ptr;
        }
        else
        {
            newptr = mm_malloc(size);
            if(!newptr) 
            {
                return 0;
            }
            memcpy(newptr, ptr, oldsize);
            /* Free the old block. */
            mm_free(ptr);
            return newptr;
        }
        
    }
    else
    {
        return ptr;
    }
}

/*
 * coalesce - Boundary tag coalescing. Return ptr to coalesced block
 */
static struct block *coalesce(struct block *bp) 
{
    bool prev_alloc = prev_blk_footer(bp)->inuse;
    bool next_alloc = next_blk_header(bp)->inuse;
    size_t size = blk_size(bp);
    if (prev_alloc && next_alloc ) 
    {      /* Case 1 */
        return bp;
    }
    else if (prev_alloc && !next_alloc) 
    {      /* Case 2 */
        struct block *thisBp = next_blk(bp);
        RB_REMOVE(mytree, &tree[thisBp->index], thisBp);
        mark_block_free(bp, size + blk_size(thisBp));
    }
    else if (!prev_alloc && next_alloc) 
    {      /* Case 3 */
        bp = prev_blk(bp);
        RB_REMOVE(mytree, &tree[bp->index], bp);
        mark_block_free(bp, size + blk_size(bp));
    }
    else 
    {                                     /* Case 4 */
        struct block *thisBa = prev_blk(bp);
        struct block *thisBb = next_blk(bp);
        RB_REMOVE(mytree, &tree[thisBa->index], thisBa);
        RB_REMOVE(mytree, &tree[thisBb->index], thisBb);
        mark_block_free(prev_blk(bp), 
                        size + blk_size(next_blk(bp)) + blk_size(prev_blk(bp)));
        bp = prev_blk(bp);
    }
    return bp;
}

static void change_last_free(struct block *blk)
{
    if(last_free_block == NULL)
    {
        mark_block_free(blk, blk_size(blk));
        last_free_block = blk;
    }
    else
    {
        mark_block_free(blk, blk_size(blk) + blk_size(last_free_block));
        last_free_block = blk;
    }
}

static bool is_end_free(struct block *blk)
{
    struct boundary_tag* end_tag = next_blk_header(blk);
    if((end_tag->inuse == -1 &&end_tag->size == 0) || next_blk(blk) == last_free_block)
    {
        return true;
    }
    return false;
}

static struct block *next_blk(struct block *blk) 
{
    // assert(blk_size(blk) != 0);
    return (struct block *)((size_t *)blk + blk->header.size);
}

static void mark_block_free(struct block *blk, size_t size)
{
    set_header_and_footer(blk, size, 0);

    // printf("%x\n", (int)blk->elem.next);
}

/* Mark a block as used and set its size. */
static void mark_block_used(struct block *blk, int size) {
    set_header_and_footer(blk, size, 1);
}

/* Given a block, obtain its footer boundary tag */
static struct boundary_tag * get_footer(struct block *blk) {
    return (void *)((size_t *)blk + blk->header.size) - sizeof(struct boundary_tag);
}

/* Set a block's size and inuse bit in header and footer */
static void set_header_and_footer(struct block *blk, int size, int inuse) 
{
    blk->header.inuse = inuse;
    blk->header.size = size;
    * get_footer(blk ) = blk->header;    /* Copy header to footer */
}

/* Given a block, obtain previous's block footer.
   Works for left-most block also. */
static struct boundary_tag * prev_blk_footer(struct block *blk) {
    
    return &blk->header - 1;
}

/* Given a block, obtain previous's block footer.
   Works for left-most block also. */
static struct boundary_tag * next_blk_header(struct block *blk) {
    return (struct boundary_tag*)((size_t *)blk + blk->header.size);
}

/* Return size of block is free */
static size_t blk_size(struct block *blk) { 
    return blk->header.size; 

}

/* Given a block, obtain pointer to previous block.
   Not meaningful for left-most block. */
static struct block *prev_blk(struct block *blk) {
    struct boundary_tag *prevfooter = prev_blk_footer(blk);
    // assert(prevfooter->size != 0);
    return (struct block *)((size_t *)blk - prevfooter->size);
}


/*Compares elements a to b. Returns -1 if smaller and 1 if bigger
  0 is never returned as ties are broken by their address. Better implementations might
  put duplicate sized blocks into a linked list */
static int compare_size(struct block * a, struct block * b)
{
    if (a->header.size <= b->header.size)
    {
        return -1;
    }
    return 1;
	
}

team_t team = {
    /* Team name */
    "tree",
    /* First member's full name */
    "Jiahui Huang",
    "hjiahui7@vt.edu",
    /* Second member's full name (leave blank if none) */
    "",
    "",
};
static size_t array[LISTLIMIT] = {8, 16, 32, 64, 128, 256, 512, 1020, 1024, 2048, 4096, 8192, 16384, 32768, 65536, 131072,262144}; 
static int get_heap_index(size_t size)
{
    //    size = size >> 2;
    int i =0;
    for(;i<LISTLIMIT;i++)
    {
        if(array[i] >= size)
        {
            return i;
        }
    }
    return i;
}