
.org $0040
avariable:      .byte $05
another:        .byte $13
result:         .byte $00
anarray:        .byte $01
        .byte $02
        .byte $03
        .byte $04

.org $0200
        clc
        lda avariable
        adc another
        sta result

.org $1305
        ldy #2
        ldx anarray,Y
        txa