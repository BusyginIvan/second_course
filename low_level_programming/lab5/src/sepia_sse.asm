global sepia_four

%macro macro1 1
  mov eax, [buf + %1 * 4]
  mov [rsp + 0 * 4], eax
  mov [rsp + 1 * 4], eax
  mov [rsp + 2 * 4], eax
  %assign i  %1 + 3
  mov eax, [buf + i * 4]
  mov [rsp + 3 * 4], eax
%endmacro

%macro macro2 1
  %assign i  %1 + 3
  mov eax, [buf + i * 4]
  mov [rsp + 0 * 4], eax
  mov [rsp + 1 * 4], eax
  %assign i  %1 + 6
  mov eax, [buf + i * 4]
  mov [rsp + 2 * 4], eax
  mov [rsp + 3 * 4], eax
%endmacro

%macro macro3 1
  %assign i  %1 + 6
  mov eax, [buf + i * 4]
  mov [rsp + 0 * 4], eax
  %assign i  %1 + 9
  mov eax, [buf + i * 4]
  mov [rsp + 1 * 4], eax
  mov [rsp + 2 * 4], eax
  mov [rsp + 3 * 4], eax
%endmacro

%macro line 3
  macro%1 %2
  movups xmm%2, [rsp]
  movups xmm3, [%3%1]
  mulps xmm%2, xmm3
%endmacro

%macro save 1
  addps xmm0, xmm1
  addps xmm0, xmm2
  cvtps2dq xmm0, xmm0             ; Преобразуем в 4-байтовые целые числа.
  packusdw xmm0, xmm4             ; Преобразуем в 2-байтовые.
  packuswb xmm0, xmm4             ; Преобразуем в однобайтовые.
  %assign i  %1 - 1
  movd dword[rdi + i * 4], xmm0   ; Сохраняем.
%endmacro

%macro iter 1
  line %1, 0, b
  line %1, 1, g
  line %1, 2, r
  save %1
%endmacro

section .rodata
  b1: dd 0.131, 0.168, 0.189, 0.131   ; c11 c21 c31 c11
  g1: dd 0.543, 0.686, 0.769, 0.543   ; c12 c22 c32 c12
  r1: dd 0.272, 0.349, 0.393, 0.272   ; c13 c23 c33 c13

  b2: dd 0.168, 0.189, 0.131, 0.168   ; c21 c31 c11 c21
  g2: dd 0.686, 0.769, 0.543, 0.686   ; c22 c32 c12 c22
  r2: dd 0.349, 0.393, 0.272, 0.349   ; c23 c33 c13 c23

  b3: dd 0.189, 0.131, 0.168, 0.189   ; c31 c11 c21 c31
  g3: dd 0.769, 0.543, 0.686, 0.769   ; c32 c12 c22 c32
  r3: dd 0.393, 0.272, 0.349, 0.393   ; c33 c13 c23 c33

section .bss
  buf: dd 12 dup ?

section .text
  ; rdi - указывает на массив из 4-х троек однобайтовых беззнаковых целых чисел, цветовых компонент 4-х пикселей.
  ; Преобразует эти 4 пикселя в сепиа.
  sepia_four:
    ; Преобразуем все числа в float'ы и записываем их в buf.
    pxor xmm4, xmm4
    xor rcx, rcx
    .while:
      movd xmm0, [rdi + rcx]          ; Достаём 4 однобайтовых целых числа.
      punpcklbw xmm0, xmm4            ; Преобразуем в 2-байтовые.
      punpcklwd xmm0, xmm4            ; Преобразуем в 4-байтовые.
      cvtdq2ps xmm0, xmm0             ; Преобразуем в float'ы.
      movups [buf + rcx * 4], xmm0    ; Сохраняем.
      add rcx, 4
      cmp rcx, 12
      jne .while
    
    sub rsp, 16
    iter 1
    iter 2
    iter 3
    add rsp, 16

    ret