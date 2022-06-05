/* Описание внутреннего представления картинки. */

#ifndef IMAGE_H
#define IMAGE_H

#include <stdint.h>
#include <stddef.h>

struct pixel { uint8_t b, g, r; };

struct image {
  uint64_t width, height;
  struct pixel* data;
};

struct pixel* pixel_of(struct image const* img, uint32_t x, uint32_t y);

struct image create_image(uint64_t width, uint64_t height);
void destruct_image(struct image* img);

#endif
