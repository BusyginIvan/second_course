/* Загрузка и сохранение изображений формата bmp. */

#include "bmp.h"
#include <stdint.h>

#pragma pack(push, 1)
struct bmp_header {
    char byteB; char byteM;
    uint32_t bfileSize;
    uint32_t bfReserved;
    uint32_t bOffBits;
    uint32_t biSize;
    uint32_t biWidth;
    uint32_t biHeight;
    uint16_t biPlanes;
    uint16_t biBitCount;
    uint32_t biCompression;
    uint32_t biSizeImage;
    uint32_t biXPelsPerMeter;
    uint32_t biYPelsPerMeter;
    uint32_t biClrUsed;
    uint32_t biClrImportant;
};
#pragma pack(pop)

static uint8_t padding(uint32_t width) {
  return (width % 4 == 0) ? 0 : (4 - (width * sizeof(struct pixel)) % 4);
}

enum bmp_error_code bmp_to_img(FILE* file, struct image* const img) {
  struct bmp_header header;
  size_t size_res = fread(&header, sizeof(struct bmp_header), 1, file);
  if (size_res != 1) return BMP_ANOTHER_ERROR;

  if (header.byteB != 'B' || header.byteM != 'M')
    return BMP_INVALID_HEADER;

  *img = create_image(header.biWidth, header.biHeight);
  for (uint32_t y = 0; y < img->height; y++) {
    size_res = fread(pixel_of(img, 0, y),
                            sizeof(struct pixel), img->width, file);
    if (size_res != img->width) return BMP_ANOTHER_ERROR;

    size_res = fseek(file, padding(img->width), SEEK_CUR);
    if (size_res != 0) return BMP_ANOTHER_ERROR;
  }

  return BMP_SUCCESS;
}

static struct bmp_header create_bmp_header(uint32_t width, uint32_t height) {
  const uint32_t INFO_HEADER_SIZE = 40;
  const uint32_t WHOLE_HEADER_SIZE = 14 + INFO_HEADER_SIZE;
  const uint32_t IMAGE_SIZE = (sizeof(struct pixel) * width + padding(width)) * height;
  return (struct bmp_header) {
          .byteB = 'B', .byteM = 'M',
          .bfileSize = WHOLE_HEADER_SIZE + IMAGE_SIZE,
          .bfReserved = 0,
          .bOffBits = WHOLE_HEADER_SIZE,
          .biSize = INFO_HEADER_SIZE,
          .biWidth = width,
          .biHeight = height,
          .biPlanes = 1,
          .biBitCount = 24,
          .biCompression = 0,
          .biSizeImage = IMAGE_SIZE,
          .biXPelsPerMeter = 0,
          .biYPelsPerMeter = 0,
          .biClrUsed = 0,
          .biClrImportant = 0
  };
}

enum bmp_error_code img_to_bmp(FILE* file, struct image const img) {
  struct bmp_header header = create_bmp_header(img.width, img.height);
  size_t size_res = fwrite(&header,
                           sizeof(struct bmp_header), 1, file);
  if (size_res != 1) return BMP_ANOTHER_ERROR;

  for (uint32_t y = 0; y < img.height; y++) {
    size_res = fwrite(pixel_of(&img, 0, y),
                      sizeof(struct pixel), img.width, file);
    if (size_res != img.width) return BMP_ANOTHER_ERROR;

    for (int i = 0; i < padding(img.width); i++) {
      int int_res = putc(0, file);
      if (int_res == EOF) return BMP_ANOTHER_ERROR;
    }
  }

  return BMP_SUCCESS;
}
