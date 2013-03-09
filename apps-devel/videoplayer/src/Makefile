CORE = log.o main.o
OBJ  = $(addprefix $(O),$(CORE))
CFLAGS += $(DCE_CFLAGS) $(AV_CFLAGS) $(PVR_CFLAGS) $(GLES_CFLAGS)
LDFLAGS += $(DCE_LDFLAGS) $(AV_LDFLAGS) $(PVR_LDFLAGS) $(GLES_LDFLAGS)

$(O)videoplayer: $(OBJ)

$(O)%.o: %.c
	$(CC) $(CFLAGS) -c -o $@ $<

clean:
	rm -f $(O)*.o $(O)*.d $(O)videoplayer

-include $(OBJ:.o=.d)
