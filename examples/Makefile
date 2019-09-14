SOURCES = bubblesort.asm testloop.asm example.asm

OBJECTS = $(subst .asm,.o,$(SOURCES))

all: $(OBJECTS)

$(OBJECTS) : %.o : %.asm
	orgasm -o $@ $<
