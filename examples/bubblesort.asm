        ;; bubblesort.asm
        ;; Richard Russo <rrusso@cs.ucf.edu>
        ;; 6502 assembly program example for use with orgasm and
        ;; the J6502 debugger.
        ;;
        ;; To assemble the program:
        ;; orgasm bubblesort.asm -o bubblesort.o
        ;;
        ;; Load the program in the debugger, then set PC to be
        ;; $0200, and happily step away to your heart's content.
        ;;
        ;; The array that is sorted has 10 elements, and starts at
        ;; address 0.  You can scroll the displayed memory page back
        ;; to the first address while the program is executing to
        ;; look at the progress.  It takes a long time, 3180 clocks!
        ;; That's probably just because I lack 6502 assembly skills.
        ;; 
        ;; 08|10|2000
        
        .org $0050
array:  .byte 3
        .byte 9
        .byte 5
        .byte 8
        .byte 20
        .byte 5
        .byte 200
        .byte 255
        .byte 0
        .byte 5
array_end:
counter1:       .byte 0

        array_size = 10

        .org $0000
start:  lda counter1
        cmp #array_size
        bpl all_done

        lda array
        ldx #1
loop:   cmp array,X
        bpl no_swap
        jsr swapem
no_swap:
        lda array,X
        inx
        tay
        txa
        cmp #array_size
        bpl done
        tya
        jmp loop
done:
        lda counter1
        tay
        iny
        tya
        sta counter1
        jmp start
all_done:       
        brk

swapem: lda array,X
        dex
        ldy array,X
        sta array,X
        inx
        sty array,X
        rts
        .byte $b4
        .byte $00
