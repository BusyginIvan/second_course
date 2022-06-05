/* Сепиа фильтр для изображения. */

#include "../include/sepia.h"
#include <stdio.h>
#include <stdint.h>
#include <stddef.h>

extern void sepia_four(uint8_t src[12]);

unsigned char sat(uint64_t x) {
  if (x < 256) return x;
  return 255;
}

static void sepia_one(struct pixel* const restrict pixel) {
  struct pixel const old = *pixel;
  pixel->b = sat((uint64_t) ((float) old.b * .131f + (float) old.g * .543f + (float) old.r * .272f));
  pixel->g = sat((uint64_t) ((float) old.b * .168f + (float) old.g * .686f + (float) old.r * .349f));
  pixel->r = sat((uint64_t) ((float) old.b * .189f + (float) old.g * .769f + (float) old.r * .393f));
}

void sepia_without_sse(struct image* const restrict img) {
  for(uint32_t y = 0; y < img->height; y++)
    for(uint32_t x = 0; x < img->width; x++)
      sepia_one(pixel_of(img, x, y));
}

void sepia_with_sse(struct image* const restrict img) {
  size_t size = img->width * img->height;

  struct pixel* i = img->data;
  for (; i < img->data + size % 4; i++)
    sepia_one(i);
  
  for (; i < img->data + size; i += 4)
    sepia_four((uint8_t*) (void*) i);
}