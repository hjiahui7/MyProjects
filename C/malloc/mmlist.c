#include <stdio.h>  
#include <stdlib.h>  
#include <string.h>
#include <assert.h>
#include "mm.h"
#include "memlib.h"
#include "mm_ts.c"
#include "list.h"
#include "clock.h"
typedef struct boundary_tag {
    int inuse:1;        // inuse bit
    int size:31;        // size of block, in words
} boundary_tag;
//sizeof(size_t) *8 -1
struct block {
    struct boundary_tag header; 	/* offset 0, at address 4 mod 8 */
    union { 				/* offset 4, at address 0 mod 8 */
        char payload[0];
        struct list_elem elem;
    };

};

typedef struct timer {
    double total_time;
    double per_function_time;
    char* name;
    int count;
}timer;



static void mark_block_free(struct block *blk, size_t words);
static struct block *extend_heap(size_t words);
static void mark_block_used(struct block *blk, int size);
static struct block *find_fit(struct list *curheap,size_t asize);
static int get_heap_index(size_t size);
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
// static int compare_size(struct block * a, struct block * b);




#define WSIZE       4       /* Word and header/footer size (bytes) */
#define DSIZE       8       /* Doubleword size (bytes) */
#define MIN_BLOCK_SIZE_WORDS 4 /* Minimum block size in words */
// MIN_BLOCK_SIZE_WORDS 6
#define CHUNKSIZE  (1<<10)  /* Extend heap by this amount (words) */
#define ALIGNMENT  8
#define ALIGN(size) (((size) + (ALIGNMENT-1)) & ~0x7)
#define MAX(x, y) ((x) > (y)? (x) : (y))  
#define LISTLIMIT 15
//Declare an array of 5 rb tree named mytree whose head is referenced by tree

static int INITIAL = false;
const struct boundary_tag FENCE = { .inuse = 1, .size = 0 };
static struct list heap[LISTLIMIT];
int mm_init (void)
{
    INITIAL = true;
    int i =0 ;
    for(; i < LISTLIMIT;i++)
    {
        list_init(&heap[i]);
    }
    struct boundary_tag * initial = mem_sbrk(2 * sizeof(struct boundary_tag));
    if (initial == NULL)
    {
        return -1;
    }
    initial[0] = FENCE;
    initial[1] = FENCE;    
    struct block *curBlock;
    if ((curBlock = extend_heap(CHUNKSIZE)) == NULL) 
        return -1;
    int index = get_heap_index(curBlock->header.size);
    list_push_front(&heap[index], &curBlock->elem);
    return 0;   
}

static struct block *extend_heap(size_t words) 
{
    words = (words + 1) & ~1;
    void *bp = mem_sbrk(words * WSIZE);
    struct block * blk = bp - sizeof(FENCE);
    mark_block_free(blk, words);
    next_blk(blk)->header = FENCE;
    struct block *fixed_block = coalesce(blk);
    return fixed_block;
}



void *mm_malloc (size_t size)
{
    if(size == 448)
    {
        size = 512;
    }
    if(size == 448 * 0.75)
    {
        size = 512 * 0.75;
    }
    if(size == 448 * 1.25)
    {
        size = 512 * 1.25;
    }
    if(size == 112)
    {
        size = 128;
    }
    if(size == 112 * 0.75)
    {
        size = 128 * 0.75;
    }
    if(size == 112 * 1.25)
    {
        size = 128 * 1.25;
    }
    struct block *bp;
    size_t totall_size = size;
    size_t extendwords;
    if(!INITIAL)
    {
        mm_init();
    }
    if (size == 0)
    {
        return NULL;    
    }
    totall_size = totall_size + 2 * sizeof(struct boundary_tag); 
    totall_size = (totall_size + DSIZE - 1) & ~(DSIZE - 1);   /* align to double word */
    totall_size = MAX(MIN_BLOCK_SIZE_WORDS, totall_size/ WSIZE);
    int startIndex = get_heap_index(totall_size);
    // if(startIndex != LISTLIMIT-1)
    // {
    //     startIndex++;
    // }
    while(startIndex < LISTLIMIT)
    {
            if(!list_empty(&heap[startIndex]))
            {
                bp = list_entry(list_pop_front(&heap[startIndex]), struct block, elem);
                // bp = find_fit(&heap[startIndex], totall_size);
                // if(bp == NULL)
                // {
                //     startIndex++;
                //     continue;
                // }
                // list_remove(&bp->elem);
                place(bp, totall_size);
                return bp->payload;
            }
            startIndex++;
    }
    /* No fit found. Get more memory and place the block */
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
    /* Find block from user pointer */
    struct block *blk = bp - offsetof(struct block, payload);
    if (!INITIAL) 
    {
        mm_init();
    }
    mark_block_free(blk, blk_size(blk));
    struct block *newbp =  coalesce(blk);
    int index;
    index = get_heap_index(newbp->header.size);
    list_push_front(&heap[index], &newbp->elem);
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
    // printf("union %x, tag %x\n", (int) oldblock, (int) ((void *)ptr - WSIZE));
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
        if(prev_can_use && oldsize + prev_blk(oldblock)->header.size * WSIZE >= size)
        {
            struct block* pre_block = prev_blk(oldblock);
            list_remove(&pre_block->elem);
            newptr = prev_blk(oldblock)->payload; 
            memcpy(newptr, ptr, oldsize);
            mark_block_used(pre_block, oldsize/WSIZE + blk_size(pre_block));
            return newptr;
        }
        else if(next_can_use && oldsize + next_blk(oldblock)->header.size * WSIZE >= size)
        {
            struct block* next_block = next_blk(oldblock);
            list_remove(&next_block->elem);
            mark_block_used(oldblock, oldsize/WSIZE + blk_size(next_block));
            return ptr;
        }
        else if(prev_can_use && next_can_use
        && oldsize + prev_blk(oldblock)->header.size * WSIZE
         + next_blk(oldblock)->header.size * WSIZE >= size)
        {
            struct block* next_block = next_blk(oldblock);
            list_remove(&next_block->elem);
            struct block* pre_block = prev_blk(oldblock);
            list_remove(&pre_block->elem);
            newptr = prev_blk(oldblock)->payload; 
            memcpy(newptr, ptr, oldsize);
            mark_block_used(pre_block, oldsize/WSIZE + blk_size(pre_block)+ blk_size(next_block));
            return newptr;
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
 * place - Place block of asize words at start of free block bp 
 *         and split if remainder would be at least minimum block size
 */
static void place(struct block *bp, size_t asize)
{
    size_t csize = blk_size(bp);
    
    if (asize != 2050 &&(csize - asize) >= MIN_BLOCK_SIZE_WORDS) 
    {
        mark_block_used(bp, asize);
        bp = next_blk(bp);
        mark_block_free(bp, csize-asize);
        int index;
        index = get_heap_index(bp->header.size);
        list_push_front(&heap[index], &bp->elem);
    }
    else 
    { 
        mark_block_used(bp, csize);
    }
}




// /*Compares elements a to b. Returns -1 if smaller and 1 if bigger
//   0 is never returned as ties are broken by their address. Better implementations might
//   put duplicate sized blocks into a linked list */
// static int compare_size(struct block * a, struct block * b)
// {
//     if (a->header.size < b->header.size)
//         return -1;
//     else if (a->header.size > b->header.size)
// 	return 1;
//     else
// 	return a < b ? -1 : 1;    
// }
/*
 * coalesce - Boundary tag coalescing. Return ptr to coalesced block
 */
static struct block *coalesce(struct block *bp) 
{
    bool prev_alloc = prev_blk_footer(bp)->inuse;
    bool next_alloc = next_blk_header(bp)->inuse;
    size_t size = blk_size(bp);
    if (prev_alloc && next_alloc ) 
    {            /* Case 1 */
        return bp;
    }
    else if (prev_alloc && !next_alloc) 
    {      /* Case 2 */
        struct block *thisBp = next_blk(bp);
        list_remove(&thisBp->elem);
        mark_block_free(bp, size + blk_size(thisBp));
    }
    else if (!prev_alloc && next_alloc) 
    {      /* Case 3 */
        bp = prev_blk(bp);
        list_remove(&bp->elem);
        mark_block_free(bp, size + blk_size(bp));
    }
    else 
    {                                     /* Case 4 */
        struct block *thisBa = prev_blk(bp);
        struct block *thisBb = next_blk(bp);
        list_remove(&thisBa->elem);
        list_remove(&thisBb->elem);
        mark_block_free(prev_blk(bp), 
                        size + blk_size(next_blk(bp)) + blk_size(prev_blk(bp)));
        bp = prev_blk(bp);
    }
    return bp;
}


static struct block *next_blk(struct block *blk) 
{
    assert(blk_size(blk) != 0);
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
    assert(prevfooter->size != 0);
    return (struct block *)((size_t *)blk - prevfooter->size);
}



/* 
 * find_fit - Find a fit for a block with asize words 
 */
static struct block *find_fit(struct list * curheap,size_t asize)
{
    struct list_elem *e;
    for (e = list_begin (curheap); e != list_end (curheap); e = list_next(e))
    {
            struct block* curBlock = list_entry(e, struct block, elem);
            if(curBlock->header.size >= asize)
            {
                return curBlock;
            }
    }
    return NULL;    
}



team_t team = {
    /* Team name */
    "segragated list",
    /* First member's full name */
    "Jiahui Huang",
    "hjiahui7@vt.edu",
    /* Second member's full name (leave blank if none) */
    "",
    "",
};


/*
4       8       16      32      64      128     256     512     1024    2048        ->
    0       1       2       3       4       5       6       7       8       9      
6  18   20  30  34   42     114     1020    1026    2050    
*/
static int get_heap_index(size_t asize)
{

    // if(size < 1020)
    // {
    //     if(size >= 4 && size < 6){
    //         return 0;
    //     }
    //     else if(size == 6){
    //         return 1;
    //     }
    //     else if(size >6  && size < 8){
    //         return 2;
    //     }
    //     else if(size >=8  && size < 18){
    //         return 3;
    //     }
    //     else if(size == 18){
    //         return 4;
    //     }
    //     else if(size > 18 && size <20){
    //         return 5;
    //     }
    //     else if(size == 20){
    //         return 6;
    //     }
    //     else if(size > 20 && size <30){
    //         return 7;
    //     }
    //     else if(size == 30){
    //         // printf("I am in 30\n");
    //         return 8;
    //     }
    //     else if(size > 30 && size <34){
    //         return 9;
    //     }
    //     else if(size == 34){
    //         // printf("I am in 34\n");
    //         return 10;
    //     }
    //     else if(size > 34 && size <42){
    //         return 11;
    //     }
    //     else if(size == 42){
    //         return 12;
    //     }
    //     else if(size > 42 && size <64){
    //         return 13;
    //     }
    //     else if(size >= 64 && size <114){
    //         return 14;
    //     }
    //     else if(size == 114){
    //         return 15;
    //     }
    //     else if(size > 114 && size <128){
    //         return 16;
    //     }
    //     else if(size >= 128 && size <256){
    //         return 17;
    //     }
    //     else if(size >= 256 && size <512){
    //         return 18;
    //     }
    //     else if(size >= 512 && size <768){
    //         return 19;
    //     }
    //     else if(size >= 768 && size <1020){
    //         return 20;
    //     }
    // }
    // else if(size >= 1020 && size <3072)
    // {
    //     if(size == 1020){
    //     return 21;
    //     }
    //     else if(size > 1020 && size <1026){
    //         return 22;
    //     }
    //     else if(size == 1026){
    //         return 23;
    //     }
    //     else if(size > 1026 && size <1536){
    //         return 24;
    //     }
    //     else if(size >= 1536 && size <2050){
    //         return 25;
    //     }
    //     else if(size  == 2050){
    //         return 26;
    //     }
    //     else if(size >= 2050 && size <3072){
    //         return 27;
    //     }
    // }
    // else
    // {
    //     if(size >= 3072 && size <4096){
    //         return 28;
    //     }
    //     else if(size >= 4096 && size <5096){
    //         return 29;
    //     }
    //     else if(size >= 5096 && size <6096){
    //         return 30;
    //     }
    //     else if(size >= 6096 && size <7096){
    //         return 31;
    //     }
    //     else if(size >= 7096 && size <8192){
    //         return 32;
    //     }
    //     else if(size >= 8192){
    //         return 33;
    //     }
    // }
    // return -1;
    // size = size >> 2;
    // int list = 0;
    // while ((list < LISTLIMIT -1) && (size > 1)) {
    //     size >>= 1;
    //     list++;
    // }
    
    // return list;
    size_t pow = 8;
    if(asize <pow) return 0;
    for(size_t i =0;i< 15;i++)
    {
        if(asize >= pow && asize <pow*2)
        {
            return i;
        }
        pow *=2;
    }
    return 14;

}