/* Описание внутреннего представления картинки. */

#include "image.h"
#include <malloc.h>

struct pixel* pixel_of(struct image const* const img, uint32_t x, uint32_t y) {
    return img->data + x + y * img->width;
}

struct image create_image(uint64_t width, uint64_t height) {
  return (struct image) { width, height, malloc(sizeof(struct pixel) * width * height) };
}

void destruct_image(struct image* const img) {
    img->width = 0;
    img->height = 0;
    if (img->data != NULL) free(img->data);
}
