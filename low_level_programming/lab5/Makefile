SRC_DIR = src
INC_DIR = include
OBJ_DIR = obj
BUILD_DIR = build
MAIN = main

ASM_COMP = nasm -f elf64 -g
C_COMP = gcc -std=c17 -ggdb -c -I $(INC_DIR) -Werror -pedantic -Wall
LINK = gcc -no-pie -g
RM = rm -rf

FILES = image files bmp sepia sepia_sse main

$(MAIN): $(addprefix $(OBJ_DIR)/, $(addsuffix .o, $(FILES))) | $(BUILD_DIR)
	$(LINK) -no-pie -o $(BUILD_DIR)/$@ $(filter %.o, $^)

$(OBJ_DIR)/%.o: $(SRC_DIR)/%.c | $(OBJ_DIR)
	$(C_COMP) $< -o $@

$(OBJ_DIR)/%.o: $(SRC_DIR)/%.asm | $(OBJ_DIR)
	$(ASM_COMP) -o $@ $<

$(OBJ_DIR):
	mkdir -p $(OBJ_DIR)

$(BUILD_DIR):
	mkdir -p $(BUILD_DIR)

clean:
	$(RM) $(OBJ_DIR)
	$(RM) $(BUILD_DIR)