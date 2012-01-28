CPPFLAGS = -O3 -g3 -Wall -fomit-frame-pointer -fno-tree-vectorize -march=armv7-a -mtune=cortex-a9 -mfpu=neon -mfloat-abi=softfp -mno-thumb-interwork -mno-thumb
LDLIBS = -lEGL -lGLESv1_CM

CORE = OGLESInitialization_NullWS.o
OBJ  = $(addprefix $(O),$(CORE))

$(O)OGLESInitialization_NullWS: $(OBJ)

$(O)%.o: %.cpp
	$(CC) $(CPPFLAGS) $(CFLAGS) -c -o $@ $<

$(O)%.o: %.S
	$(CC) $(CPPFLAGS) $(ASFLAGS) -c -o $@ $<

clean:
	rm -f $(O)*.o $(O)*.d $(O)OGLESInitialization_NullWS

-include $(OBJ:.o=.d)
